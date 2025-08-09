import 'package:flutter/material.dart';
import 'package:triprider/screens/home/HomeScreen.dart';
import 'package:triprider/screens/Login/widgets/Login_Screen_Button.dart';
import 'package:google_sign_in/google_sign_in.dart';
import 'dart:convert';
import 'package:http/http.dart' as http;
import 'package:shared_preferences/shared_preferences.dart';
import 'package:kakao_flutter_sdk_user/kakao_flutter_sdk_user.dart';

class Loginscreen extends StatefulWidget {
  const Loginscreen({super.key});

  @override
  State<Loginscreen> createState() => _LoginscreenState();
}

class _LoginscreenState extends State<Loginscreen> {
  final TextEditingController emailController = TextEditingController();
  final TextEditingController passwordController = TextEditingController();

  /// ------------------- UI -------------------
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        centerTitle: true,
        title: const Text(
          '로그인',
          style: TextStyle(fontSize: 25, fontWeight: FontWeight.w700),
        ),
        leading: IconButton(
          onPressed: () => Navigator.of(context).pop(),
          icon: const Icon(Icons.arrow_back_ios_new),
        ),
      ),
      body: SingleChildScrollView(
        padding: const EdgeInsets.symmetric(vertical: 20),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            _InputField(
              label: "이메일 주소",
              controller: emailController,
              onClear: () => emailController.clear(),
            ),
            _InputField(
              label: "비밀번호",
              controller: passwordController,
              onClear: () => passwordController.clear(),
              obscure: true,
            ),
            Align(
              alignment: Alignment.centerRight,
              child: TextButton(onPressed: () {}, child: const Text('비밀번호 찾기')),
            ),
            LoginScreenButton(
              T: 20,
              B: 80,
              L: 17,
              R: 17,
              child: const Text(
                '로그인',
                style: TextStyle(
                  color: Colors.white,
                  fontSize: 20,
                  fontWeight: FontWeight.w600,
                ),
              ),
              color: const Color(0XFFFF4E6B),
              onPressed: _loginWithEmail,
            ),
            const _Or(),
            SizedBox(height: 30),
            SocialLoginButton(
              color: Colors.yellow,
              assetPath: 'assets/image/kakaotalk.png',
              text: '카카오로 로그인',
              textColor: Colors.black,
              onPressed: _loginWithKakao, // ✅ Spring API 호출
            ),
            SocialLoginButton(
              color: Colors.white,
              assetPath: 'assets/image/Google.png',
              text: 'Google로 로그인',
              textColor: Colors.black,
              onPressed: _loginWithGoogle,
            ),
          ],
        ),
      ),
    );
  }

  /// ------------------- 이메일 로그인 -------------------
  Future<void> _loginWithEmail() async {
    final email = emailController.text.trim();
    final password = passwordController.text.trim();

    if (email.isEmpty || password.isEmpty) {
      _showErrorDialog('입력 오류', '이메일과 비밀번호를 입력하세요.');
      return;
    }

    try {
      final url = Uri.parse('http://10.0.2.2:8080/api/auth/login');
      final response = await http.post(
        url,
        headers: {'Content-Type': 'application/json'},
        body: jsonEncode({'email': email, 'password': password}),
      );

      if (response.statusCode == 200) {
        final data = jsonDecode(response.body);
        await _handleLoginSuccess(data['token']);
      } else {
        _showErrorDialog('로그인 실패', '이메일 또는 비밀번호가 올바르지 않습니다.');
      }
    } catch (e) {
      _showErrorDialog('네트워크 오류', '서버와 연결할 수 없습니다.\n$e');
    }
  }

  /// ------------------- 카카오 로그인 (Spring에만 요청) -------------------
  Future<void> _loginWithKakao() async {
    try {
      // 1. 카카오 계정 로그인 (톡 없으면 계정 로그인 시도)
      OAuthToken token;
      if (await isKakaoTalkInstalled()) {
        token = await UserApi.instance.loginWithKakaoTalk();
      } else {
        token = await UserApi.instance.loginWithKakaoAccount();
      }

      final accessToken = token.accessToken;
      if (accessToken.isEmpty) {
        _showSnackBar('카카오 access token을 가져올 수 없습니다.');
        return;
      }

      // 2. Spring 서버로 accessToken 전달
      final response = await http.post(
        Uri.parse('http://10.0.2.2:8080/api/auth/kakao'),
        headers: {'Content-Type': 'application/json'},
        body: jsonEncode({'accessToken': accessToken}),
      );

      if (response.statusCode == 200) {
        final data = jsonDecode(response.body);
        await _handleLoginSuccess(data['token']);
      } else {
        _showSnackBar('서버 로그인 실패: ${response.statusCode}\n${response.body}');
      }
    } catch (e) {
      _showSnackBar('카카오 로그인 오류: $e');
    }
  }

  /// ------------------- 구글 로그인 -------------------
  Future<void> _loginWithGoogle() async {
    try {
      final GoogleSignIn googleSignIn = GoogleSignIn(
        scopes: ['email', 'profile'],
      );
      final GoogleSignInAccount? googleUser = await googleSignIn.signIn();
      if (googleUser == null) return;

      final GoogleSignInAuthentication googleAuth =
          await googleUser.authentication;
      final accessToken = googleAuth.accessToken;
      if (accessToken == null) {
        _showSnackBar('Google access token을 가져올 수 없습니다.');
        return;
      }

      final response = await http.post(
        Uri.parse('http://10.0.2.2:8080/api/auth/google'),
        headers: {'Content-Type': 'application/json'},
        body: jsonEncode({'accessToken': accessToken}),
      );

      if (response.statusCode == 200) {
        final data = jsonDecode(response.body);
        await _handleLoginSuccess(data['token']);
      } else {
        _showSnackBar('서버 로그인 실패: ${response.statusCode}');
      }
    } catch (e) {
      _showSnackBar('구글 로그인 오류: $e');
    }
  }

  /// ------------------- 공통 처리 -------------------
  Future<void> _handleLoginSuccess(String jwtToken) async {
    final prefs = await SharedPreferences.getInstance();
    await prefs.setString('jwt', jwtToken);

    _showSnackBar('로그인 성공! JWT 저장됨');
    if (context.mounted) {
      Navigator.of(
        context,
      ).pushReplacement(MaterialPageRoute(builder: (_) => Homescreen()));
    }
  }

  void _showErrorDialog(String title, String message) {
    showDialog(
      context: context,
      builder:
          (context) => AlertDialog(
            title: Text(title),
            content: Text(message),
            actions: [
              TextButton(
                onPressed: () => Navigator.pop(context),
                child: const Text('확인'),
              ),
            ],
          ),
    );
  }

  void _showSnackBar(String message) {
    ScaffoldMessenger.of(
      context,
    ).showSnackBar(SnackBar(content: Text(message)));
  }
}

/// ------------------- 재사용 위젯 -------------------
class _InputField extends StatelessWidget {
  final String label;
  final TextEditingController controller;
  final VoidCallback onClear;
  final bool obscure;

  const _InputField({
    super.key,
    required this.label,
    required this.controller,
    required this.onClear,
    this.obscure = false,
  });

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.symmetric(horizontal: 20, vertical: 10),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Text(label),
          TextField(
            controller: controller,
            obscureText: obscure,
            style: const TextStyle(fontSize: 20),
            decoration: InputDecoration(
              suffixIcon: IconButton(
                icon: const Icon(Icons.close),
                onPressed: onClear,
              ),
              enabledBorder: const UnderlineInputBorder(
                borderSide: BorderSide(color: Colors.grey),
              ),
            ),
          ),
        ],
      ),
    );
  }
}

class _Or extends StatelessWidget {
  const _Or({super.key});

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.symmetric(horizontal: 20, vertical: 20),
      child: Row(
        children: const [
          Expanded(
            child: Divider(thickness: 2, color: Colors.black, endIndent: 10),
          ),
          Text("or", style: TextStyle(fontSize: 25)),
          Expanded(
            child: Divider(thickness: 2, color: Colors.black, indent: 10),
          ),
        ],
      ),
    );
  }
}

class SocialLoginButton extends StatelessWidget {
  final Color color;
  final String assetPath;
  final String text;
  final Color textColor;
  final VoidCallback onPressed;

  const SocialLoginButton({
    super.key,
    required this.color,
    required this.assetPath,
    required this.text,
    required this.textColor,
    required this.onPressed,
  });

  @override
  Widget build(BuildContext context) {
    return LoginScreenButton(
      T: 0,
      B: 30,
      L: 17,
      R: 17,
      color: color,
      onPressed: onPressed,
      child: Row(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          Image.asset(assetPath, height: 34, width: 34),
          const SizedBox(width: 20),
          Text(
            text,
            style: TextStyle(
              color: textColor,
              fontSize: 20,
              fontWeight: FontWeight.w600,
            ),
          ),
        ],
      ),
    );
  }
}

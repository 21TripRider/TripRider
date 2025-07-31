import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:triprider/screens/home/HomeScreen.dart';
import 'package:triprider/screens/Login/widgets/Login_Screen_Button.dart';
import 'package:kakao_flutter_sdk_user/kakao_flutter_sdk_user.dart' as kakao;
import 'dart:convert';
import 'package:http/http.dart' as http;

class Loginscreen extends StatefulWidget {
  const Loginscreen({super.key});

  @override
  State<Loginscreen> createState() => _LoginscreenState();
}

class _LoginscreenState extends State<Loginscreen> {
  /// 입력값 컨트롤러
  final TextEditingController emailController = TextEditingController();
  final TextEditingController passwordController = TextEditingController();

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        centerTitle: true,
        title: Text(
          '로그인',
          style: TextStyle(fontSize: 25, fontWeight: FontWeight.w700),
        ),
        leading: IconButton(
          onPressed: Arrow_Back_ios_Pressed,
          icon: Icon(Icons.arrow_back_ios_new),
        ),
      ),

      body: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          _insertEmail(controller: emailController, onPressed: Close_Button_Pressed),
          _insertPassword(controller: passwordController, onPressed: Close_Button_Pressed),

          _findPassword(onPressed: Password_Search_Pressed),

          /// 로그인 버튼
          LoginScreenButton(
            T: 20,
            B: 80,
            L: 17,
            R: 17,
            child: LoginButton_Child(),
            color: Color(0XFFFF4E6B),
            onPressed: Login_Pressed,
          ),

          _Or(),

          ///동일한 코드 반복 80 30 17 17
          LoginScreenButton(
            T: 80,
            B: 30,
            L: 17,
            R: 17,
            child: KakaoLogin_Child(),
            color: Colors.yellow,
            onPressed: Kakao_Login_Pressed,
          ),

          ///동일한 코드 반복 0,30,17,17
          LoginScreenButton(
            T: 0,
            B: 30,
            L: 17,
            R: 17,
            child: GoogleLogin_Child(),
            color: Colors.white,
            onPressed: Google_Login_Pressed,
          ),
        ],
      ),
    );
  }

  Arrow_Back_ios_Pressed() {
    Navigator.of(context).pop();
  }

  Close_Button_Pressed() {}

  Password_Search_Pressed() {}

  /// 로그인 요청 → 성공 시 홈 화면 이동
  Login_Pressed() async {
    final email = emailController.text;
    final password = passwordController.text;

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
        final token = data['token'];
        print('JWT 토큰: $token');

        Navigator.of(context).pushReplacement(
          MaterialPageRoute(builder: (_) => Homescreen()),
        );
      } else if (response.statusCode == 401) {
        _showErrorDialog('로그인 실패', '이메일 또는 비밀번호가 올바르지 않습니다.');
      } else {
        _showErrorDialog('로그인 실패', '이메일 또는 비밀번호가 올바르지 않습니다.');
      }
    } catch (e) {
      _showErrorDialog('네트워크 오류', '서버와 연결할 수 없습니다.\n$e');
    }
  }

  Kakao_Login_Pressed() async {
    try {
      kakao.OAuthToken token;

      if (await kakao.isKakaoTalkInstalled()) {
        token = await kakao.UserApi.instance.loginWithKakaoTalk();
      } else {
        token = await kakao.UserApi.instance.loginWithKakaoAccount();
      }

      print("카카오 AccessToken: ${token.accessToken}");

      // ✅ 여기까지 Flutter에서만 동작
      // 서버 연동은 나중에 JWT 붙이면 됨
      Navigator.of(context).pushReplacement(
        MaterialPageRoute(builder: (_) => Homescreen()),
      );
    } catch (e) {
      _showErrorDialog('카카오 로그인 오류', '$e');
    }
  }

  Google_Login_Pressed() {}

  void _showErrorDialog(String title, String message) {
    showDialog(
      context: context,
      builder: (context) => AlertDialog(
        title: Text(title),
        content: Text(message),
        actions: [
          TextButton(onPressed: () => Navigator.pop(context), child: Text('확인'))
        ],
      ),
    );
  }
}

///이메일 입력 위젯
class _insertEmail extends StatelessWidget {
  final VoidCallback onPressed;
  final TextEditingController controller;

  const _insertEmail({super.key, required this.onPressed, required this.controller});

  @override
  Widget build(BuildContext context) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Padding(
          padding: const EdgeInsets.only(top: 40, left: 16, bottom: 10),
          child: Text('이메일 주소'),
        ),

        Padding(
          padding: const EdgeInsets.only(left: 20, right: 20),
          child: TextField(
            controller: controller, // ✅ 컨트롤러 연결
            style: TextStyle(fontSize: 20),
            decoration: InputDecoration(
              suffixIcon: Container(
                child: IconButton(
                  padding: EdgeInsets.zero,
                  onPressed: onPressed,
                  icon: Icon(Icons.close),
                ),
              ),
              enabledBorder: UnderlineInputBorder(
                borderSide: BorderSide(color: Colors.grey),
              ),
            ),
          ),
        ),
      ],
    );
  }
}

///비밀번호 입력 위젯
class _insertPassword extends StatelessWidget {
  final VoidCallback onPressed;
  final TextEditingController controller;

  const _insertPassword({super.key, required this.onPressed, required this.controller});

  @override
  Widget build(BuildContext context) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Padding(
          padding: const EdgeInsets.only(top: 20, left: 16, bottom: 10),
          child: Text('비밀번호'),
        ),

        Padding(
          padding: const EdgeInsets.only(left: 20, right: 20),
          child: TextField(
            controller: controller, // ✅ 컨트롤러 연결
            obscureText: true,
            style: TextStyle(fontSize: 20),
            decoration: InputDecoration(
              suffixIcon: Container(
                child: IconButton(
                  padding: EdgeInsets.zero,
                  onPressed: onPressed,
                  icon: Icon(Icons.close),
                ),
              ),
              enabledBorder: UnderlineInputBorder(
                borderSide: BorderSide(color: Colors.grey),
              ),
            ),
          ),
        ),
      ],
    );
  }
}

///비밀번호 입력칸
class _findPassword extends StatelessWidget {
  final VoidCallback onPressed;
  const _findPassword({super.key, required this.onPressed});

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.only(left: 300),
      child: TextButton(onPressed: onPressed, child: Text('비밀번호 찾기')),
    );
  }
}

class _Or extends StatelessWidget {
  const _Or({super.key});

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.symmetric(horizontal: 20),
      child: Row(
        children: [
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

///로그인 버튼 파라미터
class LoginButton_Child extends StatelessWidget {
  const LoginButton_Child({super.key});

  @override
  Widget build(BuildContext context) {
    return Text(
      '로그인',
      style: TextStyle(
        color: Colors.white,
        fontSize: 20,
        fontWeight: FontWeight.w600,
      ),
    );
  }
}

///카카오톡으로 로그인
class KakaoLogin_Child extends StatelessWidget {
  const KakaoLogin_Child({super.key});

  @override
  Widget build(BuildContext context) {
    return Row(
      mainAxisAlignment: MainAxisAlignment.center,
      children: [
        Image.asset('assets/image/kakaotalk.png', height: 34, width: 34),

        SizedBox(width: 20),

        Text(
          '카카오톡으로 로그인',
          style: TextStyle(
            color: Colors.black,
            fontSize: 20,
            fontWeight: FontWeight.w600,
          ),
        ),
      ],
    );
  }
}

///구글로 로그인
class GoogleLogin_Child extends StatelessWidget {
  const GoogleLogin_Child({super.key});

  @override
  Widget build(BuildContext context) {
    return Row(
      mainAxisAlignment: MainAxisAlignment.center,
      children: [
        Image.asset('assets/image/Google.png', width: 34, height: 34),

        SizedBox(width: 20),

        Text(
          'Google로 로그인',
          style: TextStyle(
            color: Colors.black,
            fontSize: 20,
            fontWeight: FontWeight.w600,
          ),
        ),
      ],
    );
  }
}

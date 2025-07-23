import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:triprider/widgets/login_button.dart';

class Loginscreen extends StatelessWidget {
  const Loginscreen({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Padding(
          padding: const EdgeInsets.only(left: 106),
          child: Text(
            '로그인',
            style: TextStyle(fontSize: 25, fontWeight: FontWeight.w700),
          ),
        ),
        leading: IconButton(
          onPressed: Arrow_Back_ios_Pressed,
          icon: Icon(Icons.arrow_back_ios_new),
        ),
      ),

      body: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          _insertEmail(),

          _insertPassword(),

          _findPassword(),

          LoginButton(
            T: 20,
            B: 80,
            L: 17,
            R: 17,
            child: LoginButton_Child(),
            color: Color(0XFFFF4E6B),
          ),

          _Or(),

          ///동일한 코드 반복 80 30 17 17
          LoginButton(
            T: 80,
            B: 30,
            L: 17,
            R: 17,
            child: KakaoLogin_Child(),
            color: Colors.yellow,
          ),

          ///동일한 코드 반복 0,30,17,17
          LoginButton(
            T: 0,
            B: 30,
            L: 17,
            R: 17,
            child: GoogleLogin_Child(),
            color: Colors.white,
          ),
        ],
      ),
    );
  }
}

Arrow_Back_ios_Pressed() {}

Close_Button_Pressed() {}

Password_Search_Pressed() {}

onPressed_Login() {}

Kakao_Login_Pressed() {}

Google_Login_Pressed() {}

///이메일 입력 위젯
class _insertEmail extends StatelessWidget {
  const _insertEmail({super.key});

  @override
  Widget build(BuildContext context) {
    return Column(
      mainAxisAlignment: MainAxisAlignment.start,
      children: [
        Padding(
          padding: const EdgeInsets.only(top: 40, left: 16, bottom: 10),
          child: Text('이메일 주소'),
        ),

        Padding(
          padding: const EdgeInsets.only(left: 20, right: 20),
          child: TextField(
            style: TextStyle(fontSize: 20),
            decoration: InputDecoration(
              suffixIcon: Container(
                child: IconButton(
                  padding: EdgeInsets.zero,
                  onPressed: Close_Button_Pressed,
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
  const _insertPassword({super.key});

  @override
  Widget build(BuildContext context) {
    return Column(
      mainAxisAlignment: MainAxisAlignment.start,
      children: [
        Padding(
          padding: const EdgeInsets.only(top: 20, left: 16, bottom: 10),
          child: Text('비밀번호'),
        ),

        Padding(
          padding: const EdgeInsets.only(left: 20, right: 20),
          child: TextField(
            style: TextStyle(fontSize: 20),
            decoration: InputDecoration(
              suffixIcon: Container(
                child: IconButton(
                  padding: EdgeInsets.zero,
                  onPressed: Close_Button_Pressed,
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

class _findPassword extends StatelessWidget {
  const _findPassword({super.key});

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.only(left: 300),
      child: TextButton(
        onPressed: Password_Search_Pressed,
        child: Text('비밀번호 찾기'),
      ),
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
            child: Divider(thickness: 3, color: Colors.black, endIndent: 10),
          ),
          Text("or", style: TextStyle(fontSize: 25)),
          Expanded(
            child: Divider(thickness: 3, color: Colors.black, indent: 10),
          ),
        ],
      ),
    );
  }
}

class LoginButton_Child extends StatelessWidget {
  const LoginButton_Child({super.key});

  @override
  Widget build(BuildContext context) {
    return Text(
      '로그인',
      style: TextStyle(
        color: Colors.white,
        fontSize: 15,
        fontWeight: FontWeight.w600,
      ),
    );
  }
}

class KakaoLogin_Child extends StatelessWidget {
  const KakaoLogin_Child({super.key});

  @override
  Widget build(BuildContext context) {
    return Row(
      mainAxisAlignment: MainAxisAlignment.center,
      children: [
        Image.asset('asset/image/kakaotalk.png', height: 34, width: 34),

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

class GoogleLogin_Child extends StatelessWidget {
  const GoogleLogin_Child({super.key});

  @override
  Widget build(BuildContext context) {
    return Row(
      mainAxisAlignment: MainAxisAlignment.center,
      children: [
        Image.asset('asset/image/Google.png', width: 34, height: 34),

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

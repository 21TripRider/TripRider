import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

class Welcomescreen extends StatelessWidget {
  const Welcomescreen({super.key});

  @override
  Widget build(BuildContext context) {
    return SafeArea(
      child: Scaffold(
        body: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          crossAxisAlignment: CrossAxisAlignment.stretch,
          children: [
            TripRider_logo(),

            SizedBox(height: 150),

            ///동일한 코드 반복
            Login_Button(),

            SizedBox(height: 20),

            Account_Button(),
          ],
        ),
      ),
    );
  }
}

onPressed_Login() {}

onPressed_Account() {}


///첫 로고 화면
class TripRider_logo extends StatelessWidget {
  const TripRider_logo({super.key});

  @override
  Widget build(BuildContext context) {
    return Column(
      children: [
        Container(child: Image.asset('asset/image/logo.png')),

        SizedBox(height: 30),

        Container(
          child: Column(
            children: [
              Text(
                '트립라이더가 처음이신가요?',
                style: TextStyle(fontSize: 25, fontWeight: FontWeight.w700),
              ),
              SizedBox(height: 10),
              Text('계정이 없으시다면'),
              Text('앱서비스 이용을 위해 회원가입해주세요'),
            ],
          ),
        ),
      ],
    );
  }
}


///로그인 버튼
class Login_Button extends StatelessWidget {
  const Login_Button({super.key});

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.only(bottom: 15, left: 17, right: 17),
      child: Container(
        width: double.infinity,
        height: 68,
        child: ElevatedButton(
          style: ElevatedButton.styleFrom(backgroundColor: Color(0XFFFF4E6B)),
          onPressed: onPressed_Login,
          child: Text(
            '로그인',
            style: TextStyle(
              color: Colors.white,
              fontSize: 15,
              fontWeight: FontWeight.w600,
            ),
          ),
        ),
      ),
    );
  }
}


///회원가입 버튼
class Account_Button extends StatelessWidget {
  const Account_Button({super.key});

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.only(bottom: 15, left: 17, right: 17),
      child: Container(
        width: double.infinity,
        height: 68,
        child: ElevatedButton(
          style: ElevatedButton.styleFrom(backgroundColor: Colors.white),
          onPressed: onPressed_Login,
          child: Text(
            '회원가입',
            style: TextStyle(
              color: Colors.black,
              fontSize: 15,
              fontWeight: FontWeight.w600,
            ),
          ),
        ),
      ),
    );
  }
}

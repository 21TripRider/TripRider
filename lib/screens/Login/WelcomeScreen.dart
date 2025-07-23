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
            Container(child: Image.asset('asset/image/logo.png')),

            SizedBox(height: 30,),

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

            SizedBox(height: 150,),

            Padding(
              padding: const EdgeInsets.symmetric(horizontal: 30),
              child: Container(
                child: ElevatedButton(
                  style: ElevatedButton.styleFrom(
                    padding: EdgeInsets.symmetric(vertical: 25),
                    backgroundColor: Color(0XFFFF4E6B),
                  ),
                  onPressed: onPressed_Login,
                  child: Text(
                    '로그인',
                    style: TextStyle(
                      color: Colors.white,
                      fontWeight: FontWeight.w700,
                    ),
                  ),
                ),
              ),
            ),

            SizedBox(height: 20,),

            Padding(
              padding: const EdgeInsets.symmetric(horizontal: 30),
              child: Container(
                child: ElevatedButton(
                  style: ElevatedButton.styleFrom(
                    padding: EdgeInsets.symmetric(vertical: 25),
                  ),
                  onPressed: onPressed_Login,
                  child: Text(
                    '회원가입',
                    style: TextStyle(fontWeight: FontWeight.w700),
                  ),
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }
}

onPressed_Login() {}

onPressed_Account() {}

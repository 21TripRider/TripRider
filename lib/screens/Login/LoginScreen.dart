import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

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
          Padding(
            padding: const EdgeInsets.only(left: 300),
            child: TextButton(
              onPressed: Password_Search_Pressed,
              child: Text('비밀번호 찾기'),
            ),
          ),

          ///동일한 코드 반복
          Padding(
            padding: const EdgeInsets.only(
              top: 20,
              bottom: 80,
              left: 17,
              right: 17,
            ),
            child: Container(
              width: double.infinity,
              height: 68,
              child: ElevatedButton(
                style: ElevatedButton.styleFrom(
                  backgroundColor: Color(0XFFFF4E6B),
                ),
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
          ),

          Padding(
            padding: const EdgeInsets.symmetric(horizontal: 20),
            child: Row(
              children: <Widget>[
                Expanded(
                  child: Divider(
                    thickness: 3,
                    color: Colors.black,
                    endIndent: 10,
                  ),
                ),
                Text("or", style: TextStyle(fontSize: 25)),
                Expanded(
                  child: Divider(thickness: 3, color: Colors.black, indent: 10),
                ),
              ],
            ),
          ),

          ///동일한 코드 반복
          Padding(
            padding: const EdgeInsets.only(
              top: 80,
              bottom: 30,
              left: 17,
              right: 17,
            ),
            child: Container(
              width: double.infinity,
              height: 68,
              child: ElevatedButton(
                style: ElevatedButton.styleFrom(backgroundColor: Colors.yellow),
                onPressed: onPressed_Login,
                child: Row(
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: [
                    Image.asset(
                      'asset/image/kakaotalk.png',
                      height: 34,
                      width: 34,
                    ),

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
                ),
              ),
            ),
          ),

          ///동일한 코드 반복
          Padding(
            padding: const EdgeInsets.only(bottom: 30, left: 17, right: 17),
            child: Container(
              width: double.infinity,
              height: 68,
              child: ElevatedButton(
                style: ElevatedButton.styleFrom(backgroundColor: Colors.white),
                onPressed: Google_Login_Pressed,
                child: Row(
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: [
                    Image.asset(
                      'asset/image/Google.png',
                      width: 34,
                      height: 34,
                    ),

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
                ),
              ),
            ),
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

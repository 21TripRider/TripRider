import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

class CreateAccountscreen extends StatelessWidget {
  const CreateAccountscreen({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        leading: IconButton(
          onPressed: Arrow_Back_ios_Pressed,
          icon: Icon(Icons.arrow_back_ios_new),
        ),
      ),

      body: Container(
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,

          children: [
            Padding(
              padding: const EdgeInsets.only(top: 35, left: 16, bottom: 25),
              child: Text(
                '이메일을 입력해주세요.',
                style: TextStyle(fontSize: 25, fontWeight: FontWeight.w700),
              ),
            ),

            Padding(
              padding: const EdgeInsets.only(left: 16, bottom: 10),
              child: Text('이메일'),
            ),

            Padding(
              padding: const EdgeInsets.only(left: 20, right: 20),
              child: TextField(
                style: TextStyle(fontSize: 20),
                decoration: InputDecoration(
                  hintText: 'abc123456@XXXXX.com',
                  hintStyle: TextStyle(color: Colors.grey),

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

            Expanded(child: SizedBox()),

            ///동일한 코드 반복
            Padding(
              padding: const EdgeInsets.only(bottom: 55, left: 17, right: 17),
              child: Container(
                width: double.infinity,
                height: 68,
                child: ElevatedButton(
                  style: ElevatedButton.styleFrom(
                    backgroundColor: Color(0XFFFF4E6B),
                  ),
                  onPressed: Next_Button_Pressed,
                  child: Text(
                    '다음',
                    style: TextStyle(
                      color: Colors.white,
                      fontSize: 15,
                      fontWeight: FontWeight.w600,
                    ),
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

Close_Button_Pressed() {}

Arrow_Back_ios_Pressed() {}

Next_Button_Pressed() {}

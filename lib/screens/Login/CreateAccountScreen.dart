import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:triprider/widgets/Next_Widget_Child.dart';
import 'package:triprider/widgets/login_button.dart';

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
            _InsertEmail(),

            Expanded(child: SizedBox()),

            ///동일한 코드 반복 0,55,17,17,다음, 0XFF4E6B
            LoginButton(
              T: 0,
              B: 55,
              L: 17,
              R: 17,
              child: Next_Widget_Child(),
              color: Color(0XFFFF4E6B),
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

///이메일 입력창
class _InsertEmail extends StatelessWidget {
  const _InsertEmail({super.key});

  @override
  Widget build(BuildContext context) {
    return Column(
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
      ],
    );
  }
}



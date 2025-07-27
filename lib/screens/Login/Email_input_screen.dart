import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:triprider/screens/Login/password_input_screen.dart';
import 'package:triprider/widgets/Next_Button_Widget_Child.dart';
import 'package:triprider/widgets/Login_Screen_Button.dart';

class EmailInputScreen extends StatefulWidget {
  const EmailInputScreen({super.key});

  @override
  State<EmailInputScreen> createState() => _EmailInputScreenState();
}

class _EmailInputScreenState extends State<EmailInputScreen> {
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
            _InputEmail(onPressed: Close_Button_Pressed,),

            Expanded(child: SizedBox()),

            ///동일한 코드 반복 0,55,17,17,다음, 0XFF4E6B
            LoginScreenButton(
              T: 0,
              B: 55,
              L: 17,
              R: 17,
              child: Next_Widget_Child(),
              color: Color(0xFFFF4E6B),
              onPressed: Next_Button_Pressed,
            ),
          ],
        ),
      ),
    );
  }

  Close_Button_Pressed() {

  }

  Arrow_Back_ios_Pressed() {
    Navigator.of(context).pop();
  }

  Next_Button_Pressed() {
    Navigator.of(context).push(
      MaterialPageRoute(
        builder: (BuildContext context) {
          return PasswordInputScreen();
        },
      ),
    );
  }
}



///이메일 입력창
class _InputEmail extends StatelessWidget {
  final VoidCallback onPressed;

  const _InputEmail({super.key,required this.onPressed});

  @override
  Widget build(BuildContext context) {
    return Column(
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

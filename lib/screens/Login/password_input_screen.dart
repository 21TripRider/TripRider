import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:triprider/screens/Login/Email_input_screen.dart';
import 'package:triprider/widgets/Login_Screen_Button.dart';
import 'package:triprider/widgets/Next_Button_Widget_Child.dart';

class PasswordInputScreen extends StatefulWidget {
  const PasswordInputScreen({super.key});

  @override
  State<PasswordInputScreen> createState() => _PasswordInputScreenState();
}

class _PasswordInputScreenState extends State<PasswordInputScreen> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        leading: IconButton(
          onPressed: Arrow_Back_ios_Pressed(),
          icon: Icon(Icons.arrow_back_ios_new),
        ),
      ),

      body: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          _InputPassword(onPressed: Close_Button_Pressed),

          _PasswordCondition(),

          Expanded(child: SizedBox()),

          LoginScreenButton(
            T: 0,
            B: 55,
            L: 17,
            R: 17,
            child: Next_Widget_Child(),
            color: Color(0XFFFF4E6B),
            onPressed: () {},
          ),
        ],
      ),
    );
  }

  Close_Button_Pressed() {}

  Arrow_Back_ios_Pressed() {
    Navigator.of(context).pop();
  }

  Next_Button_Pressed() {}
}

class _InputPassword extends StatelessWidget {
  final VoidCallback onPressed;

  const _InputPassword({super.key, required this.onPressed});

  @override
  Widget build(BuildContext context) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Padding(
          padding: const EdgeInsets.only(top: 35, left: 16, bottom: 25),
          child: Text(
            '비밀번호를 입력해주세요.',
            style: TextStyle(fontSize: 25, fontWeight: FontWeight.w700),
          ),
        ),

        Padding(
          padding: const EdgeInsets.only(left: 16, bottom: 10),
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

class _PasswordCondition extends StatelessWidget {
  const _PasswordCondition({super.key});

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.only(left: 20, top: 20),
      child: Column(
        children: [
          Row(
            children: [
              Icon(Icons.check_circle_outline),
              SizedBox(width: 8),
              Text('10자리 이상'),
            ],
          ),

          Row(
            children: [
              Icon(Icons.check_circle_outline),
              SizedBox(width: 8),
              Text('영어 대문자'),
            ],
          ),

          Row(
            children: [
              Icon(Icons.check_circle_outline),
              SizedBox(width: 8),
              Text('영어 소문자'),
            ],
          ),

          Row(
            children: [
              Icon(Icons.check_circle_outline),
              SizedBox(width: 8),
              Text('숫자'),
            ],
          ),

          Row(
            children: [
              Icon(Icons.check_circle_outline),
              SizedBox(width: 8),
              Text('특수문자'),
            ],
          ),
        ],
      ),
    );
  }
}

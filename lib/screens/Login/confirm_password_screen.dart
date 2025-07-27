import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:triprider/screens/Login/Email_input_screen.dart';
import 'package:triprider/widgets/Login_Screen_Button.dart';
import 'package:triprider/widgets/Next_Button_Widget_Child.dart';

class ConfirmPasswordScreen extends StatefulWidget {
  const ConfirmPasswordScreen({super.key});

  @override
  State<ConfirmPasswordScreen> createState() => _ConfirmPasswordScreenState();
}

class _ConfirmPasswordScreenState extends State<ConfirmPasswordScreen> {
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
        children: [
          _ConfirmPassword(onPressed: Close_Button_Pressed,),

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

  Close_Button_Pressed() {

  }

  Arrow_Back_ios_Pressed() {
    Navigator.of(context).pop();
  }

  Next_Button_Pressed() {}
}


class _ConfirmPassword extends StatelessWidget {
  final VoidCallback onPressed;

  const _ConfirmPassword({super.key,required this.onPressed});

  @override
  Widget build(BuildContext context) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Padding(
          padding: const EdgeInsets.only(top: 35, left: 16, bottom: 25),
          child: Text(
            '한번 더 입력해주세요.',
            style: TextStyle(fontSize: 25, fontWeight: FontWeight.w700),
          ),
        ),

        Padding(
          padding: const EdgeInsets.only(left: 16, bottom: 10),
          child: Text('비밀번호 확인'),
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

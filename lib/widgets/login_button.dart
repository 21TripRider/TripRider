import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:triprider/screens/Login/LoginScreen.dart';


class LoginButton extends StatelessWidget {
  final double T, B, L, R;
  final Widget child;
  final Color color;

  const LoginButton({
    super.key,
    required this.T,
    required this.B,
    required this.L,
    required this.R,
    required this.child,
    required this.color
  });

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: EdgeInsets.only(top: T, bottom: B, left: L, right: R),
      child: Container(
        width: double.infinity,
        height: 68,
        child: ElevatedButton(
          style: ElevatedButton.styleFrom(backgroundColor: color),
          onPressed: onPressed_Login,
          child: child,
        ),
      ),
    );
  }
}

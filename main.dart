import 'package:flutter/material.dart';
import 'screens/nickname_input_screen.dart'; // ✅ 추가

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      debugShowCheckedModeBanner: false,
      title: 'Triprider UI Demo',
      theme: ThemeData(
        scaffoldBackgroundColor: Colors.white,
        fontFamily: 'Apple SD Gothic Neo',
      ),
      home: const NicknameInputScreen(), // ✅ 시작화면 설정
    );
  }
}

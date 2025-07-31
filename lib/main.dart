import 'package:flutter/material.dart';
import 'package:kakao_flutter_sdk_user/kakao_flutter_sdk_user.dart';
import 'package:triprider/screens/Login/WelcomeScreen.dart';

void main() async {
  WidgetsFlutterBinding.ensureInitialized();

  KakaoSdk.init(
    nativeAppKey: 'e39acb0d8abfaa427d9982f0ce199701', // ✅ 네이티브 키만
  );

  runApp(MaterialApp(home: Welcomescreen()));
}

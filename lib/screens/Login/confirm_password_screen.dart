import 'package:flutter/material.dart';
import '../constants/app_colors.dart';
import '../constants/text_styles.dart';
import '../widgets/common_next_button.dart';

class ConfirmPasswordScreen extends StatelessWidget {
  const ConfirmPasswordScreen({super.key});

  @override
  Widget build(BuildContext context) {
    final TextEditingController _checkController = TextEditingController();
    final TextEditingController _passwordController = TextEditingController(text: '비밀번호샘플'); // 임시 입력된 상태

    return Scaffold(
      backgroundColor: AppColors.white,
      appBar: AppBar(
        backgroundColor: Colors.transparent,
        elevation: 0,
        leading: IconButton(
          icon: const Icon(Icons.arrow_back_ios, color: AppColors.black),
          onPressed: () {
            Navigator.pop(context);
          },
        ),
      ),
      body: Padding(
        padding: const EdgeInsets.symmetric(horizontal: 24.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            const Text("한 번 더 입력해주세요.", style: TextStyles.title),
            const SizedBox(height: 32),
            TextField(
              controller: _checkController,
              obscureText: true,
              decoration: const InputDecoration(
                labelText: '비밀번호 확인',
                suffixIcon: Icon(Icons.clear, color: AppColors.gray),
                enabledBorder: UnderlineInputBorder(
                  borderSide: BorderSide(color: AppColors.gray),
                ),
              ),
            ),
            const SizedBox(height: 32),
            const Text("비밀번호를 입력해주세요.", style: TextStyles.title),
            const SizedBox(height: 8),
            TextField(
              controller: _passwordController,
              obscureText: true,
              readOnly: true,
              decoration: const InputDecoration(
                labelText: '비밀번호',
                suffixIcon: Icon(Icons.clear, color: AppColors.gray),
                enabledBorder: UnderlineInputBorder(
                  borderSide: BorderSide(color: AppColors.gray),
                ),
              ),
            ),
            const Spacer(),
            NextButton(
              onPressed: () {
                // 다음 화면 이동 로직
              },
              isEnabled: false, // 조건에 맞게 true로 바꾸면 활성화됨
            ),
            const SizedBox(height: 24),
          ],
        ),
      ),
    );
  }
}

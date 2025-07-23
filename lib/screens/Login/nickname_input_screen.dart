import 'package:flutter/material.dart';
import '../constants/app_colors.dart';
import '../constants/text_styles.dart';
import '../widgets/common_next_button.dart';

class NicknameInputScreen extends StatelessWidget {
  const NicknameInputScreen({super.key});

  @override
  Widget build(BuildContext context) {
    final TextEditingController _nicknameController = TextEditingController();

    return Scaffold(
      backgroundColor: AppColors.white,
      appBar: AppBar(
        backgroundColor: Colors.transparent,
        elevation: 0,
        leading: IconButton(
          icon: const Icon(Icons.arrow_back_ios, color: AppColors.black),
          onPressed: () => Navigator.pop(context),
        ),
      ),
      body: Padding(
        padding: const EdgeInsets.symmetric(horizontal: 24.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            const Text("닉네임을 입력해주세요.", style: TextStyles.title),
            const SizedBox(height: 32),
            TextField(
              controller: _nicknameController,
              decoration: const InputDecoration(
                labelText: '닉네임',
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
              isEnabled: false, // 닉네임이 입력되면 true로 바꾸세요
            ),
            const SizedBox(height: 24),
          ],
        ),
      ),
    );
  }
}

import 'package:flutter/material.dart';
import '../constants/app_colors.dart';
import '../constants/text_styles.dart';
import '../widgets/password_condition_item.dart';
import '../widgets/common_next_button.dart'; // ✅ 추가

class PasswordInputScreen extends StatefulWidget {
  const PasswordInputScreen({super.key});

  @override
  State<PasswordInputScreen> createState() => _PasswordInputScreenState();
}

class _PasswordInputScreenState extends State<PasswordInputScreen> {
  final TextEditingController _controller = TextEditingController();

  bool _isLengthValid = false;
  bool _hasUpper = false;
  bool _hasLower = false;
  bool _hasDigit = false;
  bool _hasSpecial = false;

  bool get _allValid =>
      _isLengthValid && _hasUpper && _hasLower && _hasDigit && _hasSpecial;

  void _validatePassword(String value) {
    setState(() {
      _isLengthValid = value.length >= 10;
      _hasUpper = RegExp(r'[A-Z]').hasMatch(value);
      _hasLower = RegExp(r'[a-z]').hasMatch(value);
      _hasDigit = RegExp(r'[0-9]').hasMatch(value);
      _hasSpecial = RegExp(r'[!@#\$&*~%^()\-_=+\[\]{};:,.<>?/|]').hasMatch(value);
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: AppColors.white,
      appBar: AppBar(
        backgroundColor: Colors.transparent,
        elevation: 0,
        leading: IconButton(
          icon: const Icon(Icons.arrow_back_ios),
          color: AppColors.black,
          onPressed: () {},
        ),
      ),
      body: Padding(
        padding: const EdgeInsets.symmetric(horizontal: 24.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            const SizedBox(height: 16),
            const Text("비밀번호를 입력해주세요.", style: TextStyles.title),
            const SizedBox(height: 24),
            TextField(
              controller: _controller,
              obscureText: true,
              onChanged: _validatePassword,
              decoration: InputDecoration(
                labelText: '비밀번호',
                labelStyle: TextStyles.label,
                suffixIcon: _controller.text.isNotEmpty
                    ? IconButton(
                  icon: const Icon(Icons.clear, color: AppColors.gray),
                  onPressed: () {
                    _controller.clear();
                    _validatePassword('');
                  },
                )
                    : null,
                enabledBorder: const UnderlineInputBorder(
                  borderSide: BorderSide(color: AppColors.gray),
                ),
              ),
            ),
            const SizedBox(height: 24),
            PasswordConditionItem(label: "10자리 이상", isValid: _isLengthValid),
            PasswordConditionItem(label: "영어 대문자", isValid: _hasUpper),
            PasswordConditionItem(label: "영어 소문자", isValid: _hasLower),
            PasswordConditionItem(label: "숫자", isValid: _hasDigit),
            PasswordConditionItem(label: "특수문자", isValid: _hasSpecial),
            const Spacer(),

            /// ✅ 공통 버튼 위젯 적용
            NextButton(
              onPressed: () {
                // 다음 화면 이동 로직
              },
              isEnabled: _allValid,
            ),

            const SizedBox(height: 24),
          ],
        ),
      ),
    );
  }
}

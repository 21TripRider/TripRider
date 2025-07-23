// lib/widgets/next_button.dart
import 'package:flutter/material.dart';
import '../constants/app_colors.dart';
import '../constants/text_styles.dart';

class NextButton extends StatelessWidget {
  final VoidCallback? onPressed;
  final bool isEnabled;

  const NextButton({
    Key? key,
    required this.onPressed,
    this.isEnabled = false,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.only(bottom: 24),
      child: SizedBox(
        width: double.infinity,
        height: 48,
        child: ElevatedButton(
          onPressed: isEnabled ? onPressed : null,
          style: ElevatedButton.styleFrom(
            backgroundColor:
            isEnabled ? AppColors.black : AppColors.lightGray,
            shape: RoundedRectangleBorder(
              borderRadius: BorderRadius.circular(30),
            ),
          ),
          child: const Text(
            '다음',
            style: TextStyles.button,
          ),
        ),
      ),
    );
  }
}

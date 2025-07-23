import 'package:flutter/material.dart';
import '../constants/app_colors.dart';

class PasswordConditionItem extends StatelessWidget {
  final String label;
  final bool isValid;

  const PasswordConditionItem({
    super.key,
    required this.label,
    required this.isValid,
  });

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.symmetric(vertical: 4.0),
      child: Row(
        crossAxisAlignment: CrossAxisAlignment.center,
        children: [
          Icon(
            isValid ? Icons.check_circle : Icons.radio_button_unchecked,
            color: isValid ? AppColors.green : AppColors.gray,
            size: 20,
          ),
          const SizedBox(width: 8),
          Text(
            label,
            style: TextStyle(
              fontSize: 14,
              color: isValid ? AppColors.black : AppColors.gray,
            ),
          ),
        ],
      ),
    );
  }
}

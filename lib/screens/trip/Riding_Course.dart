import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

class RidingCourse extends StatelessWidget {
  const RidingCourse({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        centerTitle: true,
        title: Text(
          '라이딩 코스 추천',
          style: TextStyle(fontSize: 25, fontWeight: FontWeight.w600),
        ),
      ),

      body: Column(
        children: [
          Select_Section(
            riding_course_pressed: Riding_Course_Pressed,
            custon_course_pressed: Custom_Course_Pressed,
          ),

          Padding(
            padding: const EdgeInsets.only(right: 260),
            child: Popular_Course(),
          ),
        ],
      ),
    );
  }

  Riding_Course_Pressed() {}
  Custom_Course_Pressed() {}
}

/// 라이딩 코스 or 맞춤형 여행 코스
class Select_Section extends StatelessWidget {
  final VoidCallback riding_course_pressed;
  final VoidCallback custon_course_pressed;

  const Select_Section({
    super.key,
    required this.custon_course_pressed,
    required this.riding_course_pressed,
  });

  @override
  Widget build(BuildContext context) {
    return Column(
      children: [
        Row(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            TextButton(
              onPressed: riding_course_pressed,
              child: Text(
                '라이딩 코스',
                style: TextStyle(fontSize: 20, color: Colors.black),
              ),
            ),

            SizedBox(width: 50),

            TextButton(
              onPressed: custon_course_pressed,
              child: Text(
                '맞춤형 여행 코스',
                style: TextStyle(fontSize: 20, color: Colors.black),
              ),
            ),
          ],
        ),
      ],
    );
  }
}

class Popular_Course extends StatelessWidget {
  const Popular_Course({super.key});

  @override
  Widget build(BuildContext context) {
    return Column(
      children: [
        Text(
          '인기코스',
          style: TextStyle(fontSize: 30, fontWeight: FontWeight.w700),
        ),

        SizedBox(
          height: 300,
          width: 200,
          child: PageView(
            children: [
              Image.asset('asset/image/image_1.png'),
              Image.asset('asset/image/image_2.png'),
              Image.asset('asset/image/image_3.png'),
              Image.asset('asset/image/image_4.png'),
              Image.asset('asset/image/image_5.png'),
            ],
          ),
        ),
      ],
    );
  }
}

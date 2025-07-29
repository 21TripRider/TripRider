import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:triprider/screens/trip/Course_Card_zip/Course_Card1.dart';

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

      body: ListView(
        children: [
          Column(
            children: [
              Select_Section(
                riding_course_pressed: Riding_Course_Pressed,
                custon_course_pressed: Custom_Course_Pressed,
              ),

              Padding(
                padding: const EdgeInsets.only(right: 300, bottom: 30, top: 20),
                child: Text(
                  '인기코스',
                  style: TextStyle(fontSize: 25, fontWeight: FontWeight.w700),
                ),
              ),

              Center(child: Popular_Course(favorite_Pressed: Favorite_Pressed)),

              Distance_Course(
                course_Pressed: Course_Pressed,
                represh_Pressed: Refresh_Pressed,
                favorite_Pressed: Favorite_Pressed,
              ),
            ],
          ),
        ],
      ),
    );
  }

  Favorite_Pressed() {}

  Course_Pressed() {}
  Refresh_Pressed() {}

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

/// 인기 코스
class Popular_Course extends StatelessWidget {
  final VoidCallback favorite_Pressed;

  const Popular_Course({super.key, required this.favorite_Pressed});

  @override
  Widget build(BuildContext context) {
    return Column(
      children: [
        AspectRatio(
          aspectRatio: 4/3,

          child: PageView(
            children: [
              CourseCard1(favorite_Pressed: favorite_Pressed),
              
            ],
          ),
        ),
      ],
    );
  }
}

class Distance_Course extends StatelessWidget {
  final VoidCallback represh_Pressed;
  final VoidCallback course_Pressed;
  final VoidCallback favorite_Pressed;

  const Distance_Course({
    super.key,
    required this.course_Pressed,
    required this.represh_Pressed,
    required this.favorite_Pressed,
  });

  @override
  Widget build(BuildContext context) {
    return Column(
      children: [
        Row(
          children: [
            Text(
              '거리순',
              style: TextStyle(fontSize: 25, fontWeight: FontWeight.w700),
            ),
            IconButton(onPressed: represh_Pressed, icon: Icon(Icons.refresh)),
          ],
        ),

        Row(children: []),
      ],
    );
  }
}

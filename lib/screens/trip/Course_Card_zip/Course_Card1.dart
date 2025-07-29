import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

class CourseCard1 extends StatelessWidget {
  final VoidCallback favorite_Pressed;

  const CourseCard1({super.key,required this.favorite_Pressed});

  @override
  Widget build(BuildContext context) {
    return ClipRRect(
      borderRadius: BorderRadius.circular(30),

      child: Stack(
        children: [
          Image.asset(
            'asset/image/courseview1.png',
            fit: BoxFit.cover,
            width: double.infinity,
            height: 350,
          ),

          ///배경 약간 어둡게
          Container(
            width: double.infinity,
            height: 350,
            color: Colors.black.withOpacity(0.15),
          ),

          Positioned(
            bottom: 16,
            left: 16,
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Text(
                  '애월해안도로',
                  style: TextStyle(
                    color: Colors.white,
                    fontSize: 25,
                    fontWeight: FontWeight.bold,
                  ),
                ),

                SizedBox(height: 5),

                Row(
                  children: [
                    GestureDetector(
                      onTap: favorite_Pressed,
                      child: Icon(Icons.favorite, color: Colors.red, size: 20),
                    ),
                    SizedBox(width: 4),
                    Text('128', style: TextStyle(color: Colors.white)),
                  ],
                )
              ],
            ),
          ),
        ],
      ),
    );
  }
}

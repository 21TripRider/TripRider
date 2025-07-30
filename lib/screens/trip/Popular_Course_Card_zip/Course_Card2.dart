import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

class CourseCard2 extends StatefulWidget {
  final VoidCallback? favorite_Pressed;

  const CourseCard2({super.key, this.favorite_Pressed});

  @override
  State<CourseCard2> createState() => _CourseCard1State();
}

class _CourseCard1State extends State<CourseCard2> {
  bool isFavorite = false;

  @override
  Widget build(BuildContext context) {
    return ClipRRect(
      borderRadius: BorderRadius.circular(30),
      child: Stack(
        children: [
          Image.asset(
            'asset/image/courseview2.png',
            fit: BoxFit.cover,
            width: double.infinity,
            height: 350,
          ),

          Container(
            width: double.infinity,
            height: 350,
            color: Colors.black.withOpacity(0.15),
          ),

          Positioned(
            top: 16,
            right: 16,
            child: IconButton(
              onPressed: () {
                setState(() {
                  isFavorite = !isFavorite;
                });
                widget.favorite_Pressed?.call();
              },
              icon: Icon(
                isFavorite ? Icons.favorite : Icons.favorite_border,
                color: isFavorite ? Colors.red : Colors.white,
                size: 35,
              ),
            ),
          ),

          Positioned(
            bottom: 16,
            left: 16,
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: const [
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
                    Icon(Icons.favorite, color: Colors.red, size: 20),
                    SizedBox(width: 4),
                    Text(
                      '128',
                      style: TextStyle(color: Colors.white, fontSize: 18),
                    ),
                  ],
                ),
                SizedBox(height: 15),
                Row(
                  children: [
                    Icon(Icons.gps_fixed, color: Colors.white),
                    SizedBox(width: 5),
                    Text(
                      '제주특별자치도 제주시 한강면 신창리 1323',
                      style: TextStyle(color: Colors.white, fontSize: 18),
                    ),
                  ],
                ),
              ],
            ),
          ),
        ],
      ),
    );
  }
}
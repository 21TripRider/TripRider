import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:triprider/screens/RiderGram/Comment.dart';
import 'package:triprider/screens/RiderGram/Search.dart';
import 'package:triprider/screens/RiderGram/Upload.dart';
import 'package:triprider/widgets/Bottom_App_Bar.dart';

class RidergramScreen extends StatelessWidget {
  const RidergramScreen({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        automaticallyImplyLeading: false,
        actions: [
          IconButton(
            onPressed: () {
              Navigator.push(
                context,
                MaterialPageRoute(builder: (context) => const Upload()),
              );
            },
            icon: Icon(Icons.add_box_outlined, size: 30),
          ),

          IconButton(
            onPressed: () {
              Navigator.push(
                context,
                MaterialPageRoute(builder: (context) => const Search()),
              );
            },
            icon: const Icon(Icons.search, size: 30),
          ),
        ],
      ),

      body: Padding(
        padding: const EdgeInsets.symmetric(horizontal: 20),
        child: ListView(
          children: [
            Upload_Card(),
            Upload_Card(),
            Upload_Card(),
            Upload_Card(),
            Upload_Card(),
          ],
        ),
      ),

      bottomNavigationBar: BottomAppBarWidget(),
    );
  }
}

class Upload_Card extends StatelessWidget {
  const Upload_Card({super.key});

  @override
  Widget build(BuildContext context) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Row(
          mainAxisAlignment: MainAxisAlignment.start,
          children: [
            Icon(Icons.account_circle, size: 25),
            SizedBox(width: 10),
            Text('사용자 닉네임', style: TextStyle(fontSize: 17)),
          ],
        ),

        SizedBox(height: 20),

        ClipRRect(
          borderRadius: BorderRadius.circular(40),
          child: Image.asset("assets/image/courseview2.png"),
        ),

        Row(
          children: [
            IconButton(onPressed: () {}, icon: Icon(Icons.favorite_border)),
            Comment(),
          ],
        ),

        Text('25.05.19 라이딩 기록(글쓰기)'),

        Row(
          children: [
            Text('#라이딩', style: TextStyle(color: Color(0XFF0088FF))),
            SizedBox(width: 10),
            Text('#헤시테그', style: TextStyle(color: Color(0XFF0088FF))),
          ],
        ),

        SizedBox(height: 40),
      ],
    );
  }
}

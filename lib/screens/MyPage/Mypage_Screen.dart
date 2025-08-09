import 'package:flutter/material.dart';
import 'package:triprider/screens/MyPage/Badge_Style_Screen.dart';
import 'package:triprider/screens/MyPage/My_Upload_Screen.dart';
import 'package:triprider/screens/MyPage/Record_Screen.dart';
import 'package:triprider/screens/MyPage/Save_Course_Screen.dart';
import 'package:triprider/widgets/Bottom_App_Bar.dart';

class MypageScreen extends StatelessWidget {
  const MypageScreen({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      extendBodyBehindAppBar: true,

      /// AppBar 뒤로 body 확장
      backgroundColor: Colors.grey[200],

      ///앱바 영역
      appBar: MyPage_AppBar(),

      body: SingleChildScrollView(
        child: Column(
          children: [
            /// 상단 카드 (AppBar 공간 포함)
            MyPage_top(),

            const SizedBox(height: 16),

            /// 하단 5개의 버튼
            MyPage_Bottom(),
          ],
        ),
      ),

      bottomNavigationBar: BottomAppBarWidget(),

    );
  }
}

class MyPage_AppBar extends StatelessWidget implements PreferredSizeWidget {
  const MyPage_AppBar({super.key});

  @override
  Size get preferredSize => const Size.fromHeight(kToolbarHeight); // AppBar 높이 지정

  @override
  Widget build(BuildContext context) {
    return AppBar(
      backgroundColor: Colors.transparent,

      /// AppBar를 투명 처리
      elevation: 0,
      automaticallyImplyLeading: false,
      title: const Text('닉네임', style: TextStyle(color: Colors.black)),
      actions: [
        IconButton(
          onPressed: () {},
          icon: Icon(
            Icons.drive_file_rename_outline,
            size: 30,
            color: Colors.black,
          ),
        ),
        const SizedBox(width: 10),
        IconButton(
          onPressed: () {},
          icon: Icon(Icons.menu, size: 30, color: Colors.black),
        ),
        const SizedBox(width: 10),
      ],
    );
  }
}

class MyPage_top extends StatelessWidget {
  const MyPage_top({super.key});

  @override
  Widget build(BuildContext context) {
    return Container(
      width: double.infinity,
      padding: EdgeInsets.only(
        top: MediaQuery.of(context).padding.top + 30,

        /// 상태바 + 높이
        left: 16,
        right: 16,
        bottom: 16,
      ),
      decoration: const BoxDecoration(
        gradient: LinearGradient(
          colors: [Color(0xFFFF5E7E), Color(0xFFFF7E9E)],
          begin: Alignment.topLeft,
          end: Alignment.bottomRight,
        ),
        borderRadius: BorderRadius.only(
          bottomLeft: Radius.circular(32),
          bottomRight: Radius.circular(32),
        ),
      ),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          /// 프로필 영역
          Row(
            children: [
              const CircleAvatar(
                radius: 40,
                backgroundImage: AssetImage('assets/image/logo.png'),
              ),
              const SizedBox(width: 16),
              Expanded(
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    const Text(
                      '제주도',
                      style: TextStyle(color: Colors.white, fontSize: 16),
                    ),
                    Row(
                      crossAxisAlignment: CrossAxisAlignment.end,
                      children: const [
                        Text(
                          '2.3',
                          style: TextStyle(
                            color: Colors.white,
                            fontSize: 32,
                            fontWeight: FontWeight.bold,
                          ),
                        ),
                        SizedBox(width: 4),
                        Text(
                          '바퀴',
                          style: TextStyle(color: Colors.white, fontSize: 16),
                        ),
                        Spacer(),
                        Text(
                          '누적거리 507 km',
                          style: TextStyle(color: Colors.white70, fontSize: 14),
                        ),
                      ],
                    ),
                    const SizedBox(height: 4),
                    ClipRRect(
                      borderRadius: BorderRadius.circular(8),
                      child: LinearProgressIndicator(
                        value: 0.77,
                        backgroundColor: Colors.white24,
                        valueColor: AlwaysStoppedAnimation<Color>(Colors.white),
                        minHeight: 6,
                      ),
                    ),
                    const SizedBox(height: 4),
                    const Text(
                      '3바퀴까지 153 km 남음',
                      style: TextStyle(color: Colors.white70, fontSize: 14),
                    ),
                  ],
                ),
              ),
            ],
          ),

          const SizedBox(height: 16),

          /// 뱃지/칭호 카드
          Container(
            padding: const EdgeInsets.symmetric(vertical: 12, horizontal: 24),
            decoration: BoxDecoration(
              color: Colors.white.withOpacity(0.15),
              borderRadius: BorderRadius.circular(30),
            ),
            child: Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [
                Row(
                  children: const [
                    Icon(Icons.star, color: Colors.white),
                    SizedBox(width: 6),
                    Text(
                      '+6',
                      style: TextStyle(color: Colors.white, fontSize: 18),
                    ),
                    SizedBox(width: 8),
                    Text('뱃지', style: TextStyle(color: Colors.white70)),
                  ],
                ),
                const Text(
                  '|',
                  style: TextStyle(fontSize: 25, color: Colors.white),
                ),
                Row(
                  children: const [
                    Text(
                      '제주 토박이 +2',
                      style: TextStyle(color: Colors.white, fontSize: 18),
                    ),
                    SizedBox(width: 8),
                    Text('칭호', style: TextStyle(color: Colors.white70)),
                  ],
                ),
              ],
            ),
          ),

          const SizedBox(height: 16),
        ],
      ),
    );
  }
}

class MyPage_Bottom extends StatelessWidget {
  const MyPage_Bottom({super.key});

  @override
  Widget build(BuildContext context) {
    return Column(
      children: [
        _buildMenuItem(context, '주행 기록', RecordScreen()),
        _buildMenuItem(context, '저장한 코스', SaveCourseScreen()),
        _buildMenuItem(context, '뱃지 & 칭호 관리', BadgeStyleScreen()),
        _buildMenuItem(context, '나의 게시물', MyUploadScreen()),
      ],
    );
  }

  Widget _buildMenuItem(
    BuildContext context,
    String title,
    Widget destinationPage,
  ) {
    return GestureDetector(
      onTap: () {
        Navigator.push(
          context,
          MaterialPageRoute(builder: (context) => destinationPage),
        );
      },
      child: Container(
        margin: const EdgeInsets.symmetric(horizontal: 16, vertical: 6),
        padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 14),
        decoration: BoxDecoration(
          color: Colors.white,
          borderRadius: BorderRadius.circular(12),
        ),
        child: Row(
          mainAxisAlignment: MainAxisAlignment.spaceBetween,
          children: [
            Text(
              title,
              style: const TextStyle(fontSize: 18, fontWeight: FontWeight.w500),
            ),
            const Icon(Icons.arrow_forward_ios, size: 18),
          ],
        ),
      ),
    );
  }
}

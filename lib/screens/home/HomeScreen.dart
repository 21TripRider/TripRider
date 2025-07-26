import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

class Homescreen extends StatelessWidget {
  const Homescreen({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: ListView(
        children: [
          _Weather(),
          SizedBox(height: 15),
          _Rent(),
          SizedBox(height: 15),
          _Record(),
        ],
      ),

      bottomNavigationBar: BottomAppBar(
        child: Row(
          mainAxisAlignment: MainAxisAlignment.spaceAround,
          children: [
            IconButton(
              onPressed: Home_Button_Pressed,
              icon: Icon(Icons.home_filled),
            ),
            IconButton(
              onPressed: Course_Button_Pressed,
              icon: Icon(Icons.motorcycle_sharp),
            ),
            IconButton(
              onPressed: Ridergram_Button_Pressed,
              icon: Icon(Icons.message),
            ),
            IconButton(
              onPressed: Mypage_Button_Pressed,
              icon: Icon(Icons.person),
            ),
          ],
        ),
      ),
    );
  }
}

Rent_Pressed() {}
Home_Button_Pressed() {}
Course_Button_Pressed() {}
Ridergram_Button_Pressed() {}
Mypage_Button_Pressed() {}

///날씨 위젯
class _Weather extends StatelessWidget {
  const _Weather({super.key});

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.symmetric(horizontal: 10),
      child: Container(
        decoration: BoxDecoration(
          color: Colors.lightBlueAccent,
          borderRadius: BorderRadius.circular(24),
        ),
        padding: EdgeInsets.all(20),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            /// 1. 지역명 + 온도
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [
                Text(
                  "제주시",
                  style: TextStyle(
                    color: Colors.white,
                    fontSize: 20,
                    fontWeight: FontWeight.bold,
                  ),
                ),
                Text(
                  "21°C",
                  style: TextStyle(
                    color: Colors.white,
                    fontSize: 28,
                    fontWeight: FontWeight.w500,
                  ),
                ),
              ],
            ),

            SizedBox(height: 10),

            /// 2. 날씨 아이콘
            Icon(Icons.cloud, color: Colors.white, size: 48),

            SizedBox(height: 10),

            /// 3. 강수확률 + 풍속
            Divider(color: Colors.white),

            Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [
                Text(
                  "강수확률",
                  style: TextStyle(color: Colors.white, fontSize: 14),
                ),
                Row(
                  children: [
                    Text(
                      "4%",
                      style: TextStyle(color: Colors.white, fontSize: 14),
                    ),
                    Icon(Icons.water_drop, color: Colors.white),
                  ],
                ),
              ],
            ),

            SizedBox(height: 8),

            Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [
                Text("풍속", style: TextStyle(color: Colors.white, fontSize: 14)),
                Row(
                  children: [
                    Text(
                      "0m/s",
                      style: TextStyle(color: Colors.white, fontSize: 14),
                    ),
                    Icon(Icons.air, color: Colors.white),
                  ],
                ),
              ],
            ),

            SizedBox(height: 16),

            /// 4. 메시지
            Text(
              "어제 내린 비의 영향으로\n도로가 미끄러울 수 있으니 주의하세요!",
              style: TextStyle(
                color: Colors.white,
                fontSize: 14,
                fontWeight: FontWeight.bold,
              ),
            ),

            SizedBox(height: 10),

            /// 인디케이터
            Center(child: Icon(Icons.circle, color: Colors.white, size: 8)),
          ],
        ),
      ),
    );
  }
}

///랜트 위젯
class _Rent extends StatelessWidget {
  const _Rent({super.key});

  @override
  Widget build(BuildContext context) {
    return Container(
      child: IconButton(
        onPressed: Rent_Pressed,
        icon: Image.asset('asset/image/RentCar.png'),
      ),
    );
  }
}

///최근 주행 기록 위젯
class _Record extends StatelessWidget {
  const _Record({super.key});

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.symmetric(horizontal: 10),
      child: Container(
        decoration: BoxDecoration(
          color: Colors.white,
          borderRadius: BorderRadius.circular(24),
        ),
        padding: const EdgeInsets.all(20.0),

        child: Column(
          mainAxisAlignment: MainAxisAlignment.spaceEvenly,
          children: [
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [
                Text(
                  '최근 주행 기록',
                  style: TextStyle(fontSize: 20, fontWeight: FontWeight.w600),
                ),

                Row(
                  children: [
                    Text(
                      '57',
                      style: TextStyle(
                        fontSize: 25,
                        fontWeight: FontWeight.w800,
                      ),
                    ),

                    Text(
                      'KM',
                      style: TextStyle(
                        color: Colors.grey,
                        fontSize: 20,
                        fontWeight: FontWeight.w600,
                      ),
                    ),
                  ],
                ),
              ],
            ),

            Row(
              children: [
                Container(
                  width: 90,
                  height: 30,
                  decoration: BoxDecoration(
                    color: Color(0XFFFFF4F6),
                    borderRadius: BorderRadius.circular(50),
                  ),
                  child: Center(
                    child: Text(
                      '2025.07.23',
                      style: TextStyle(color: Color(0XFFFF4E6B)),
                    ),
                  ),
                ),

                Container(
                  width: 140,
                  height: 30,
                  decoration: BoxDecoration(
                    color: Color(0XFFFFF4F6),
                    borderRadius: BorderRadius.circular(50),
                  ),
                  child: Center(
                    child: Text(
                      '제주도 제주시 한강면',
                      style: TextStyle(color: Color(0XFFFF4E6B)),
                    ),
                  ),
                ),
              ],
            ),

            SizedBox(height: 15),

            Divider(color: Colors.grey),

            SizedBox(height: 15),

            Row(
              mainAxisAlignment: MainAxisAlignment.spaceAround,
              children: [
                Column(
                  children: [
                    Text('평균 속도', style: TextStyle(color: Colors.grey)),
                    Text('53KM', style: TextStyle(fontSize: 20)),
                  ],
                ),

                Column(
                  children: [
                    Text('최고 속도', style: TextStyle(color: Colors.grey)),
                    Text('82 KM', style: TextStyle(fontSize: 20)),
                  ],
                ),

                Column(
                  children: [
                    Text('주행 시간', style: TextStyle(color: Colors.grey)),
                    Text('01:04:32', style: TextStyle(fontSize: 20)),
                  ],
                ),
              ],
            ),
          ],
        ),
      ),
    );
  }
}

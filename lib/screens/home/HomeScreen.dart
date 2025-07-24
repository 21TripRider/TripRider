import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

class Homescreen extends StatelessWidget {
  const Homescreen({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: ListView(children: [_Weather(), _Rent(), _Record()]),

      bottomNavigationBar: BottomAppBar(
        child: Row(
          mainAxisAlignment: MainAxisAlignment.spaceEvenly,

          children: [
            /// IconButton(onPressed: onPressed, icon: icon)
          ],
        ),
      ),
    );
  }
}


Rent_Pressed(){
  
}


///날씨 위젯
class _Weather extends StatelessWidget {
  const _Weather({super.key});

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.symmetric(horizontal: 16),
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
                Text("강수확률", style: TextStyle(color: Colors.white, fontSize: 14)),
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
      /// child: ElevatedButton(onPressed: Rent_Pressed(), child: Image.asset()),
    );
  }
}



///최근 주행 기록 위젯
class _Record extends StatelessWidget {
  const _Record({super.key});

  @override
  Widget build(BuildContext context) {
    return const Placeholder();
  }
}

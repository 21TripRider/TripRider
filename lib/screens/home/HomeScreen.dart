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


          _Rent(),


          _Record(),
          
        ],
      ),

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






class _Weather extends StatelessWidget {
  const _Weather({super.key});

  @override
  Widget build(BuildContext context) {
    return const Placeholder();
  }
}



class _Rent extends StatelessWidget {
  const _Rent({super.key});

  @override
  Widget build(BuildContext context) {
    return const Placeholder();
  }
}



class _Record extends StatelessWidget {
  const _Record({super.key});

  @override
  Widget build(BuildContext context) {
    return const Placeholder();
  }
}


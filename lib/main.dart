import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:triprider/screens/HomeScreen.dart';
import 'package:triprider/screens/Login/CreateAccountScreen.dart';
import 'package:triprider/screens/Login/WelcomeScreen.dart';

void main(){
  runApp(
    MaterialApp(
      home: CreateAccountScreen(),
    )
  );
}
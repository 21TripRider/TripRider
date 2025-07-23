#!/bin/zsh

# 시뮬레이터 부팅
echo "⏳ iPhone 16 Plus 부팅 중..."
xcrun simctl boot "iPhone 16 Plus"
sleep 5

# 시뮬레이터 앱 열기
open -a Simulator
sleep 2

# flutter run에서 디바이스 자동 지정
flutter run -d "iPhone 16 Plus"

# REMAR_CIDADÃO Android Application

There are thousands of crab fishers in Brazil. Crab fishing, however, is banned during the reproductive season of mangrove crabs.
The problem, however, is to find the exact days of the month where the crabs will reproduce.
It sometimes happen during new moon, sometimes during full moon, and sometimes during both moons.
The crabs are somehow able to predict which moon phase will have the highest tide 4 weeks ahead (as it takes 4 weeks for females to release their larvae),
and if they predict the tides will be roughly equal around each moon, there will be two reproductive events, where half the crab population reproduces during new moon and the other half during full moon.

The REMAR Project aims to analyse crab behaviours in order to accurately predict the days around which moon the crabs will mate, and aims 
to provide the Brazilian government with accurate advice on when the fishing should be banned.

This Android Application aims to help Brazilian citizens to get involved and allows them to report observed reproductive events of mangrove crabs.
The received data is then further analysed in order to find patterns and succesfully indentify the next reproductive events.

Links to the playstore:

[Portuguese (Brazil) Playstore Link](https://play.google.com/store/apps/details?id=com.github.hintofbasil.crabbler&hl=pt_BR) OR 
[English Playstore Link](https://play.google.com/store/apps/details?id=com.github.hintofbasil.crabbler&hl=en_UK)

(please note the app can only be downloaded from the Brazilian playstore, with a Brazilian google account)

![](https://raw.githubusercontent.com/musevarg/REMAR-android-app/android-app-2020/images/app-screenshots.png)

## Building & running

Build a debug APK:

    ./gradlew assembleDebug

Install and launch on a running emulator:

    adb install app/build/outputs/apk/debug/app-debug.apk
    adb shell am start -n com.github.hintofbasil.crabbler/.MainActivity

View crash logs:

    adb logcat -s AndroidRuntime:* *:S

Or stream all app logs in real time:

    adb logcat | grep crabbler

Build Release:

    STORE_PASSWORD="yourpass" KEY_PASSWORD="yourpass" ./gradlew assembleRelease
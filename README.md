# Places - powered by FourSquare API


This app allows you to search for places around the Austin, TX area and used the Foursquare API. You can view search results in a list; and view the individual details of each search result.
Each list item is a cardview containing details about a place like name, category, distance from center of Austin. There is also a button to favorite and un-favorite a place (stored in local DB - Room).
On the details screen, there is a mapview within the collapsible toolbar with two markers(center of Austin, and the current venue).
The details screen also provides details like rating, address, phone number and website information.

### App Architecture:
This application follows MVVM architecture pattern with View(Activity), ViewModel and Repository.

#### Extra Features:
1. Supports tablets (landscape mode)
2. Securely storing API in a properties file rather than hard-coding it into the code.

#### Libraries Used:
This app uses the latest AndroidX packages.

Architecture  component libraries used:
Lifecycle, ViewModel, Room and Kotlin Coroutines.

3rd Party libraries used:
* Retrofit - for Networking
* Glide - for Images

#### Notes:
* Built using Android Studio 3.5.

#### Building the APK:
1. Navigate to the project's root directory from command line.
2. From command line,  run "./gradlew assembleDebug".
3. Once build is complete, an .apk is generated in the "app/build/outputs/apk/debug/app-debug.apk"
4. You can install the apk on to an Android device by running "adb install app-debug.apk" from the command line.


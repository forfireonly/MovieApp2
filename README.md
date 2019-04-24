# MovieApp2

## Project Overview

This is second and final stage for Movie App, I add additional functionality to the app that I built in Stage 1.

* allow users to view and play trailers

![trailers](https://user-images.githubusercontent.com/29640816/56626779-5abed600-6600-11e9-96df-9fcd35eb61d5.gif)

* allow users to read reviews of a selected movie

![reviews](https://user-images.githubusercontent.com/29640816/56627581-b179df00-6603-11e9-9896-5d047470a006.gif)

* allow users to mark a movie as a favorite in the details view by tapping a button

![adding_to_favorites](https://user-images.githubusercontent.com/29640816/56627866-de7ac180-6604-11e9-83e8-27652d8602ad.gif)

* make use of Android Architecture Components (Room, LiveData, ViewModel and Lifecycle) to create a robust an efficient
  application.
  
*  create a database using Room to store the names and ids of the user's favorite movies (and optionally, the rest of 
   the information needed to display their favorites collection while offline). 
   
* modify the existing sorting criteria for the main view to include an additional pivot to show their favorites collection

![favorites](https://user-images.githubusercontent.com/29640816/56628521-5ea22680-6607-11e9-87d4-ec6ddffac9bb.gif)
![favorites_details](https://user-images.githubusercontent.com/29640816/56628739-2c44f900-6608-11e9-8a0e-20188c66ac7d.gif) 

## Installing APP on the Phone

To make a apk from this repo download android folder using git or any tool that you prefer.

And open android studio if don't have this download it from this link

If you don't have the android sdk with you then download android studio bundle

http://developer.android.com/sdk/index.html

For this you will need jdk . If you don't have this dowload it and install it.

http://www.oracle.com/technetwork/java/javase/downloads/index.html

And add the jdk path as JAVA_HOME in environmental variable.

And install your android studio.

Then open the android studio and import downladed repo earlier.

Android studio will resolve the dependencies and after all is done you can build an apk from it.

You can get the apk from the app-build-output-apk folder.

## API Key

Follow directions to obtain personal API key on https://www.themoviedb.org/

put your API key in MainActivity.java in line 40 public static String API_KEY = "YOUR-API-KEY"

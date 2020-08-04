# luasforecast
RiM Coding Challenge


Installation

Clone this repository and import into Android Studio
git clone https://github.com/gabidiaconu/luasforecast.git

Description 

This directory contains the full implementation of the assignment 
provided by Retail inMotion in the interview process for a Android Developer
position. The application is a simple Android App written in Kotlin, used for 
following the LUAS Forecast.
Project requirements: A lot of Retail inMotion employees commute from city center to the office using LUAS (tram
system in Dublin, Ireland) every day. To avoid waiting too much at the stops, people would
like to have an app where it is possible to see trams forecast.

Architecture

AndroidManifest.xml
This XML file describes to the Android platform what your application can do.
It is a required file, and is the mechanism you use to show your application
to the user (in the app launcher's list), handle data types, etc.


src/*
Under this directory is the Kotlin source for for your application.

res/*
Under this directory are the resources for your application.

Structure 
The project  is implemented with MVVM design pattern. This means that the view is composed by Fragments and Activities, 
and each of them has a coresponding ViewModel in order to build the logic. The data is provided by the models, populated inside 
repositories, in order to receive the informations via APIs.

SplashActivit - Basic starting app activity, with Permission Requests
MainActivity - Main activity of the app which work as a fragment holder
LuasForecastFragment- Fragment which shows the result of consuming the API, 
    based on the current time stamp. This is the intial fragment attached to the 
    previous activity
TramDetailsFragment - This fragment can be reached by clicking on any available 
    tram from the list populated in the previous fragment. This fragment comunicates
    to the user if he can, or cannot reach the station in order to catch the selected tram

Project requirements

minSdkVersion: 24
targetSdkVersion: 30
versionCode: 1

Project specifications:
(Libraries/Technologies used in project)

-DataBinding
-Kodein Dependency Injection
-Coroutines - FLOW API
-LifeCycle + LiveData
-sdp, ssp - scalable sizes
-Retrofit2
-TikXml - for parsing the xml result of the Retrofit calls
-Google location - maps/play servicies -> to get the users current location, in order to calculate the distance to the Tram Station
-LottieAnimations


Contributing

1. Fork it
2. Create your feature branch (git checkout -b my-new-feature)
3. Commit your changes (git commit -m 'Add some feature')
4. Push your branch (git push origin my-new-feature)
5. Create a new Pull Reques

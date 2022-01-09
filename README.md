# ITER CONNECT

## Problem statement

One of the most common problem faced by people in genral is to find a group of people that share the same vision and can work together. Students, Mentors, Seniors, freshers , Developers, Professors, etc all can help eachother to build something better. But its hard to form a group and even harder to manage.

## Proposed Solution

This Project Proposes a Real time Chating platform where one can create a Project(Room) and specify the name of the project, descriptions, Tags associated with the project, Positions he is looking for to add in the project and provide all other necessary information. The application then creates 2 chat rooms , one is official and private, another is unofficial and public. People can search for projects to join based on the tags and their positions and can chat in the public room of the project. If interested can send a "request to join" that sends a join request to all official members of the project and once everyone accepts the request the new member is added to the official project.

![splashscreen](./ScreenShots/ss7r.jpg?raw=true "Title")  ![Home Screen](./ScreenShots/ss5r.jpg?raw=true "Title")

![Sign In](./ScreenShots/ss6r.jpg?raw=true "Title")  ![About 1](./ScreenShots/ss8r.jpg?raw=true "Title")

![About 2](./ScreenShots/ss9r.jpg?raw=true "Title")  ![About 3](./ScreenShots/ss10r.jpg?raw=true "Title")

![Menu](./ScreenShots/ss4r.jpg?raw=true "Title")  ![setting](./ScreenShots/ss3r.jpg?raw=true "Title")

![Search](./ScreenShots/ss2r.jpg?raw=true "Title")  ![Chat Room](./ScreenShots/ss1r.jpg?raw=true "Title")

## Functionality and Concepts used

* The App has a very simple and intereactive interface which helps the students create a project/search a project to join/ chat in the project room
* Layout 
    * constraint layout : most activites uses constraint layout which is flexible and easy to handle for different screen sizes
    * frame layout : Most of the fragments uses FramLayout since they hold a single child view
    * linear layout: vertical linear layout for inside scroll views for easy allignments
* Fragments : Fragments and Fragment lifecycle to manage the ui ie drawn on the screen
* Activity : Activity , Intents, Activity Lifecycle 
* Recycler View: The messages in the chat room, chat rooms of a person, search list , etc all uses a recycler view

* Android Jetpack library : 
    * Navigation Library : To Navigate the fragments in AboutPage Activity using Navigation Graph and Naviation host and Fragments
    * Live data , Viewmodel and Room database: to store the user information at the signin and reuse wherever needed 

* Firebase : 
    * Real time database : To make the Real time chatting possible , usess Real time database of firebase
    * Firebase storage : To store the media files
    * Google Sign In 

* SharedPreferences : To store the state of user login

* View designs : fundamental Views and ViewGroups like textview, edit text , buttons , etc

* Others : 
    * Lottiefiles : JSON files for animated files (Lottie files) used to enhance the UI 
    * pl.droidsonroids.gif : To add GIFs in Activity

## Application Link and Future Scope

* The application is still in its developement phase and would be including features like video chat , sharable programs , work managers , tasks and role assignments, etc that would make working on a project easy and efficient and allow remote and online working, also will store individual progress. 

* Apart from adding new features , making sure the availble features and fully functional and enhancing the ui / ux for better expereince and ease of usage

* Currently the app is being designed for ITER and will include different college level features for different colleges, while keeping its fundamental features global 

* After the application is fully tested and ready for lunch , we will upload the application in play store making it available for public use

download apk : https://drive.google.com/file/d/16FsS7DDpDz3S7_AmoQxzawuYUR_1nqxu/view?usp=sharing


![ITER CONNECT Icon](./ITER%20CONNECT%20logo.png)

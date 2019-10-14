# MovementControllerByNeutralizer

## Introduction
This program is designed with the idea of losing weight while playing computer games <b>on a budget</b>. It is based on 2 technologies - computer vision and game bot command issuing(automation testing): Movement is detected in specific regions via a web camera (or emulated smartphone camera) and when it is detected, the respective keyboard key attached to that region is being pressed.
## Disclaimer
The movement controller is in its BETA state now, but is still unfinished. Check Known Issues section if you encounter any problems.
## Getting Started
What should I do to run the program:
### Download
The <a href="https://github.com/Neutralizer/MovementControllerByNeutralizer/releases">Releases</a> page holds the current version and presets - you need a preset (.properties file) in the same folder as the executable (.jar file) (now the program auto generates a default one).
### Preparation
1. In order to be able to run movement controller you need java installed: If you can't run the executable jar go to java official site and download it - https://www.java.com/en/download/ . <br>
2. You need a web camera in order for the whole "computer vision" magic to work (Make sure that you have drivers for your web camera (not required for windows 10)).<br>
### Running the movement controller
1. Select a camera and a preset(.properties file) from the dropdown menu.
2. After starting the camera and several(no more than 5) seconds pass, a black window will appear, which should detect your movement if it is not very dark in your room. If nothing happens, then check the Known Issues section.
3. With the sliders - adjust the detection: the white on the screen should appear only when you move.<br>
Insert picture here.<br>
#### What actually happens
When you move your hand in a square(region of interest) on the video feed, the program will press the respective key. Open a notepad and move your hands in the detection range of the camera, to see how the buttons are being pressed.<br>
When you are comfortable with the controls, alt tab to your game and enjoy.<br>
#### What about the mouse
Emulating mouse via computer vision is very unreliable, so the design is that the real mouse is to be used: Hold a carboard box in your hands as a gun and on its surface move the mouse to look around and aim.

Insert tutorial video here<br>
### Using the GUI - graphic user interface
After starting the program you can manipulate the virtual buttons from the button table and key dropdown menus.<br> 
#### Adding a new button: 
1. Click on the video feed on the location you want it to be placed.
2. Select the key you want to virtualise from the key dropdown menu
3. Choose the key type: 
* CONSTANT - whenever movement is detected, the key will be pressed and when more movement is detected it will refresh the action. When no movement is detected for a short period of time the key is released. Equal to constantly pressing the key and it will release by itself when left alone.
* PRESS - (Default virtual type) whenever movement is detected, the key will be pressed with a normal delay - Equal to pressing a key once and releasing it.
* TOGGLE - whenever movement is detected, the key will be held down. When movement is detected again the key will be released. Equal to leaving something heavy on your key to keep it pressed. 
4. Press Add.
#### Updating a button:
1. Click on the table row with the button you want to update.
2. Update it via the dropdown menus or by changing its location by clickin on the video feed.
3. Press Update.
#### Deleting a button:
1. Click on the table row with the button you want to delete.
2. Press Delete.
#### You can save the current preset by pressing save.

## History
### The idea
I wanted to create a project that will motivate me to lose weight, while doing the thing I love - playing games. The goal of the project was for it to be as cheap as possible, as easy to use as possible and to be accessible to others also. Currently the dominant lifestyle is very passive and one may sit at work and at home in front of the computer all day. With a program such as this one, you are motivated to exercise in your home, without worrying about bad weather, without having extra space taken for exercise equipment, without the problem of being bored - you can just change the game. Find excuse to skip exercising is also a problem - I am lazy to go out, I am too tired from work, I don't have someone to go out with and so on. With this program you will have to find an excuse not to play a game.
### The realisation
I went through a lot of computer vision algorithms that can make this program work, but many of the algorithms were calculation heavy, and the idea is to be able to run the game you are playing without any problems on a budget. <br>
I started with the most basic detection - pixel color check, but this one was quickly dropped, because with a low quality camera and low light conditions colors were never the same. It would be folly to have a free program, but in order to use it you have to buy a 200$+ web camera and photography studio level of lighting and equipment. <br>
Then I set my eyes on the YOLO deep learning algorithm and through youtube I found out that it is good only in good lighting conditions and when you have a good camera. Since most people play games during evening hours when you do not have access to direct sunlight, lightning conditions would be a problem.<br>
I looked at many computer vision algorithms that still could not provide the needed results - grabCut (processes 1 frame for ~5 sec), hough circles (in real world scenarios was heavy and did not provide accurate results),face detection (if you move your face away from the screen, you will not be able to see the said screen and you would need another face to press another button and by my knowledge humans have only 1 face), color normalization (cameras perceive different colors under different lighting conditions) and several more. <br>
Then I stopped on edge and shape detectors - showing a shape to a camera and pressing a button was fine, but the shapes must be very easily recognised and distinguished by the algorithm and in low lighting conditions, and by a cheap/simple camera. Showing white paper with shape on it with lots of edges did do the trick, but unfortunately only triangle, square and pentagon were reliably detected, due to the approximation nature of the algorithm - approxPolyDP. On the plus side it worked good at low lighting conditions with a cheap camera, but on the minus side when there was more light the detection was going crazy and detecting shadows on the wall behind you as shapes and on top of that there was this major drawback - you must not have any objects shaped like the uppermentioned that the camera sees. This extended even to the way the door was left open and the markings on your shirt. Then I noticed that every object detected that has more than 15 edges is a circle and decided that detecting and tracking the circle will control the mouse. Having to work only with 3 recognisable shapes for buttons I divided the camera into 4 quadrants, but then when the actual gameplay tests were done, I noticed a major problem - the recognition is accurate only if the shapes are near the camera (preprocessing did not help it, especially blurring). This left me with only 2 shapes - triangle and a circle (smallest and biggest amount of possible edges detected).<br>
Unable to complete my program with only 2 reliably detected shapes I almost scrapped the project, but fortunately after several weeks I found out a way to make background subtraction algorithm suit my needs: It detects movement, which is exactly what I want - If you do not move, you do not press buttons, but I needed movement to be detected only when I want it to be detected - this was achieved by having regions of interest that act like a separate screens. <br>
Thus I combined circle(shape) tracking for mouse control and background subtraction for key control via regions of interest to form the first stable working prototype. The plan was for the user to hold a cardboard box like a weapon (remember-low cost) with a piece of paper that has a circle on it attached to the supposed weapon muzzle and control it like a free look game mode. The problems that arose next: The mouse is used quite often to press mouse keys in many games, but with the then current setup moving the "gun" would interfere with movement detection and pressing buttons reliably very often is impossible. The next major problem is that since the circle detection uses approximation algorithm (and tracking on the other side), the location of the circle is being approximated and in between frames it was stuttering, though this was happening very often only in good lighting conditions. The next major problem that made me drop the circle mouse control was that IT WAS TOO GOOD: When walking in one place you naturally move your "gun", which is simulated as gun bobbing in some games, but in the current example it was bobbing the whole screen, which was undesirable. On top of everything the circle to mouse control was executed using calculations per frame, so if you had a weaker computer, your mouse speed would have been different, compared to if you have had a faster computer. Speaking about mouse speed, this program represented a 3rd layer of control (1st being operating system mouse speed, 2nd being game mouse speed, if no custom mouse settings are used(DPI settings)), which was confusing. The last problem was that my program had to emulate mouse look or free look (the gameplay FPS view) on top of the current game (normal mouse move was simple): 1st way was to make the mouse move when you move the circle away from the virtual center - but this had the uneasy feeling of balancing a stick on your finger and I did not like it. 2nd way was to turn only when the camera sees the circle near the border - you swing your "gun" to the left - you turn left and vice versa, but it was still uncomfortable and combined with previous problems I decided to drop the circle detection, with it the whole mouse emulation and use mouse on the cardboard "gun" while standing.
With the mouse problems solved by using the actual mouse the need to customise keys arouse and I chose to use .properties file as a simple offline database to store the location of the region and the type of the virtual key to be pressed. The program detects the amount of white pixels in those regions, and if they are above a certain threshold, then the key is pressed. Currently the only drawback of the program is that movement is not detected very good in poor lightning conditions, so this is curcumvented by increasing the detection threshold (this option is hardcoded for now).
## Known Issues
### General logical problems:
* The illumination must be good for the computer vision part.
* If there is someone else moving in the camera view, they will also press the buttons.
* Being dressed in white clothes improves movement detection, because it is easier for the human eye to see a white object in a dark room than a black object in a dark room. Well, same applies for cameras.
* Some cameras with slow autofocus simulate movement via the autofocus and the nature of the algorithm used and will detect stationary objects as moving. This can be avoided for now by having a monotone colored background, move your camera to avoid those big differences in color, or reposition the virtual buttons a little. Example - Behind the camera you have dark colored kitchen furniture and a white fridge - if your camera is with the uppermentioned slow autofocus, the fridge will be detected as moving during the autofocus. 
* Vision cone problem - when you back away from your camera, the camera feed will perceive you as a smaller object (just as the human eye sees an object that is far away as small). This means that when you back away and wave your hands you will not reach the end of the camera vision and you will not press the virtual button. This happens because the program emulates 3D vision space in a 2D plane, thus making the video feed cone shaped.
* While walking in one place you may move yourself away from your intended position a little to the left or right, but the virtual buttons will not move. The simple solution is to have a marker on the ground, which to follow and not move away from it. Example - you set a virtual button to be pressed when you lift your left arm and that button will be on the level of your shoulders. If you move a little to the right, the virtual button will remain a little to the left, and when you attempt to press it by raising your left arm, the button will not be pressed, because you have moved.
### Other issues
* Error files may be generated in the program's current folder.
* Camera properties button is unstable - it may lock the camera.
* Program can't save properties file if it is opened in notepad or if it has no permission to save.
* Program is 250+ MB due to deployment issues - all opencv packages are inside and that's why it is so big.
* Running the program 2 times can be computationally heavy and create lag (especially if 1 of them uses a virtual/HD camera).
* FPS will be low if the web camera has <b>low light compensation</b> checkmark active and it is dark in the room - can be disabled from camera properties button.
* Camera autofocus is detrimental for the movement controller-disable it via camera properties if your camera has that option.

## F.A.Q.
### Q. Why do I need to download this program, why not run it from my browser?
A. In order to protect the user's privacy I decided to make it run locally. It is definitely easier to use and maintain via site, but privacy is my top priority.
### Q. Can I use my smartphone as a web camera and how?
A. Yes, you can via software, although the video feed will flicker and consume much resources because it is a form of virtual camera. All virtual cameras have the same problem, but due to recent improvements this will not impair detection too much. I tried with DroidCam and it worked - enter its official site for info on how to download it: <a href="https://www.dev47apps.com/">www.dev47apps.com</a>. You don't need premium, because my program is designed to run 640x480 p.
### Q. Can I use other type of smartphone virtual camera emulation like IP camera?
A. Short answer - it may have delay, which is unacceptable. IP cameras' main purpose is to be used for surveillance, where the delay isn't a problem. The movement controller's reaction must be instant. Furthermore it drains a lot of battery power, and the video feed is sent to a browser - the movement controller can't access that video feed (may be changed in the future). 
### Q. Will this be useful in VR?
A. I think it would be great for VR - you just need a simple web camera. I have not tested it for VR, because I do not have one.
### Q. How do I determine the effect of the exercise?
A. The faster it makes you sweat, the more efficient is the exercise. Note that with time your body will adapt to constant exercise and you will not be sweating so easily.
### Q. Will the program work in 3D if I add another camera?
A. Yes, it will. Just start the program 2 times and load 2 different cameras with 2 different presets - 1st in front of you and the 2nd on your side. The front camera will detect your movement as you move from left to right and the side camera will detect your movement as you move forward and backward.
### Q. Will the program press buttons correctly if I change my keyboard language?
A. Yes, it will. Since the movement controller works with a virtual keyboard, it will press the respective key of your other keyboard layout (a tleast in Windows). 
### Q. Why First person and third person games (FPS) and not RTS (real time strategy) and strategy games?
A. 1st and 3rd person games let you take on the view of the soldier/warrior/ and is more immersive and intuitive - you move your feet - the avatar moves; you move your hand for the reload button - the avatar executes the reload animation with his hands. On the other side in RTS you play a commander, which is delegating the tough job of fighting to his subordinates, so basically he just shouts orders - go there, attack that, build this. I personally like very much strategy games and if I can make this program be immersive for them I will be very happy.
### Q. How about competitive games?
A. Unfortunately I don't recommend the program for competitive gaming and it is due to something I call <b>Superhuman game problem</b>: the character in your games doesn't tire, moves at its fastest always and usually jumps way higher. Furthermore to make your ingame character to jump you just move your finger(to press space), while if you attempt to emulate that amount of movement, it will not take much for a human to get tired and then you will be at an unfair disadvantage. 
### Q. So then casual gaming?
A. Casual and Forgiving games are the games that I recommend. The lower difficulty is hard enough when you get sweaty and tired. You can play competitive games if you don't mind the disadvantage in human response time. The purpose of the movement controller is to make you move and not be bored. Think about it like an exersice, but you are doing something interesting.
### Q. What do you mean by Forgiving games?
A. Games that let you make mistakes and don't punish you too hard I call forgiving games. Usually incorporate health/shield regeneration. Example is Destiny 2 - you take damage, hide for a while and the current skirmish is reset - you can try again. Things will be different if you go on a hard raid.
### Q. What about racing games?
A. In racing games you are seated while driving, so that is not much of an exercise. Though it still can be used, it will probably be like driving the flintstones' car and you spread your hands like an airplane in order to turn the car - I imagine it will be quite hilarious.

## Planned improvements
* Add customizable size/shape (rectangles) of virtual buttons - currently you can't edit the size of custom size virtual buttons from the GUI (you can from the text file). First click - top left point of a rectangle, second click - bottom right point of the rectangle.
* Add voice commands for key activation based on amount of volume detected. Although this will not make you move more, it will add a lot to the immersion element. Example - you yell "reloading" and the R button is pressed. Will include only 2-3 keys - quiet, normal and loud sound detection.
* Add several predefined sizes of virtual buttons (will be affected by percentages to detect movement).
* Add GUI for tweaking percentages to detect movement - now happens "under the hood" and is hardcoded to 50% - when there is movement in 50% of the virtual button, it is pressed. For custom size buttons it is hardcoded for 2% (the long W rectangle).
* Add mouse tracking for fps by shape detection (was not reliable so not a big priority). Can be implemented to detect big movement only in order to remove the stuttering effect, but may feel like throwing a fishing line. Example - you move your gun(box) to upper right corner, but to be moved again you have to move it at least 1/4 (or 1/8) of the screen for the cursor position to change.
* Scale down resolution to improve performance (your camera provides 1024 x 768 resolution and this can be scaled down to 640 x 480 or maybe even more).
* Modify the algorithm to skip detection frame(s) when movement is detected incorrectly in the entire screen (white flickering), which is unwanted because it presses all buttons (autofocus may cause this, or virtual cameras from smartphones).

## Conclusion
This program is targeted for casual gaming and has its limitations, but with the immersion and it's low budget I hope it will help many people to lose weight as it helped me.
# MovementControllerByNeutralizer

## Introduction
This program is designed with the idea of losing weight while playing computer games. It is based on 2 technologies - computer vision and game bot command issuing(automation testing): Movement is detected in specific regions via a web camera and when it is detected, the respective keyboard key attached to that region is being pressed. 
## Limitations
There are 2 main limitations - the illumination must be good for the computer vision part and you should not have anyone else moving in the camera view, or they will also press the buttons. It appears that being dressed in white clothes helps detecting movement more precisely.
## Disclaimer
The program is still in its ALPHA state and is unfinished. Check Known Issues section if you encounter any problems.
## Getting Started
What should I do to run this alpha concoction:
### Download
The <a href="https://github.com/Neutralizer/MovementControllerByNeutralizer/releases">Releases</a> page holds the current version and presets - you need a preset (.properties file) in the same folder as the executable (.jar file). 
### Preparation
1. In order to be able to run this program you need java installed: If you can't run the executable jar go to java official site and download it - https://www.java.com/en/download/ . <br>
2. You need a web camera in order for the whole "computer vision" magic to work (Make sure that you have drivers for your web camera (not needed for windows 10)).<br>
### Running the program
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
* CONSTANT - whenever movement is detected, the key will be pressed with low delay - Equal to pressing repeatedly the selected key.
* PRESS - (Default virtual type) whenever movement is detected, the key will be pressed with a normal delay - Equal to pressing a key once and releasing it.
* TOGGLE - whenever movement is detected, the key will be held down. When movement is detected again the key will be released. Equal to leaving something heavy on your key to keep it pressed. 
4. Press Add.
#### Updating a button:
1. Click on the table row with the button you want to update.
2. Update it via the dropdown menus or by changing its location by clickin on the video feed.
3. Press Update.
#### Deleting a button:
1. Click on the table row with the button you want to update.
2. Press Delete.
#### You can save the current preset by pressing save.

## History
### The idea
I wanted to create a project that will motivate me to lose weight, while doing the thing I love - playing games. The goal of the project was for it to be as cheap as possible, as easy to use as possible and to be accessible to others also. Currently the dominant lifestyle is very passive and one may sit at work and at home in front of the computer all day. With a program such as this one, you are motivated to exercise in your home, without worrying about bad weather, without having extra space taken for exercise equipment, without the problem of being bored - you can just change the game. Find excuse to skip exercising is also a problem - I am lazy to go out, I am too tired from work, I don't have someone to go out with and so on. With this program you will have to find an excuse not to play a game.
### The realisation
I went through a lot of computer vision algorithms that can make this program work, but many of the algorithms were calculation heavy, and the idea is to be able to run the game you are playing without any problems. <br>
I started with the most basic detection - pixel color check, but this one was quickly dropped, because with a low quality camera and low light conditions colors were never the same. It would be folly to have a free program, but in order to use it you have to buy a 200$+ web camera. <br>
Then I set my eyes on the YOLO deep learning algorithm and through youtube I found out that it is good only in good lighting conditions and when you have a good camera. Since most people play games during evening hours when you do not have access to direct sunlight, lightning conditions would be a problem.<br>
I looked at many computer vision algorithms that still could not provide the needed results - grabCut (processes 1 frame for ~5 sec), hough circles (in real world scenarios was heavy and did not provide accurate results),face detection (if you move your face away from the screen, you will not be able to see the said screen and you would need another face to press another button and by my knowledge humans have only 1 face), color normalization (cameras perceive different colors under different lighting conditions) and several more. <br>
Then I stopped on edge and shape detectors - showing a shape to a camera and pressing a button was fine, but the shapes must be very easily recognised and distinguished by the algorithm and in low lighting conditions, and by a cheap/simple camera. Showing white paper with shape on it with lots of edges did do the trick, but unfortunately only triangle, square and pentagon were reliably detected, due to the approximation nature of the algorithm - approxPolyDP. On the plus side it worked good at low lighting conditions with a cheap camera, but on the minus side when there was more light the detection was going crazy and detecting shadows on the wall behind you as shapes and on top of that there was this major drawback - you must not have any objects shaped like the uppermentioned that the camera sees. This extended even to the way the door was left open and the markings on your shirt. Then I noticed that every object detected that has more than 15 edges is a circle and decided that detecting and tracking the circle will control the mouse. Having to work only with 3 recognisable shapes for buttons I divided the camera into 4 quadrants, but then when the actual gameplay tests were done, I noticed a major problem - the recognition is accurate only if the shapes are near the camera (preprocessing did not help it, especially blurring). This left me with only 2 shapes - triangle and a circle (smallest and biggest amount of possible edges detected).<br>
Unable to complete my program with only 2 reliably detected shapes I almost scrapped the project, but fortunately after several weeks I found out a way to make background subtraction algorithm suit my needs: It detects movement, which is exactly what I want - If you do not move, you do not press buttons, but I needed movement to be detected only when I want it to be detected - this was achieved by having regions of interest that act like a separate screens. <br>
Thus I combined circle(shape) tracking for mouse control and background subtraction for key control via regions of interest to form the first stable working prototype. The plan was for the user to hold a cardboard box like a weapon (remember-low cost) with a piece of paper that has a circle on it attached to the supposed weapon muzzle and control it like a free look game mode. The problems that arose next: The mouse is used quite often to press mouse keys in many games, but with the then current setup moving the "gun" would interfere with movement detection and pressing buttons reliably very often is impossible. The next major problem is that since the circle detection uses approximation algorithm (and tracking on the other side), the location of the circle is being approximated and in between frames it was stuttering, though this was happening very often only in good lighting conditions. The next major problem that made me drop the circle mouse control was that IT WAS TOO GOOD: When walking in one place you naturally move your "gun", which is simulated as gun bobbing in some games, but in the current example it was bobbing the whole screen, which was undesirable. On top of everything the circle to mouse control was executed using calculations per frame, so if you had a weaker computer, your mouse speed would have been different, compared to if you have had a faster computer. Speaking about mouse speed, this program represented a 3rd layer of control (1st being operating system mouse speed, 2nd being game mouse speed, if no custom mouse settings are used(DPI settings)), which was confusing. The last problem was that my program had to emulate mouse look or free look (the gameplay FPS view) on top of the current game (normal mouse move was simple): 1st way was to make the mouse move when you move the circle away from the virtual center - but this had the uneasy feeling of balancing a stick on your finger and I did not like it. 2nd way was to turn only when the camera sees the circle near the border - you swing your "gun" to the left - you turn left and vice versa, but it was still uncomfortable and combined with previous problems I decided to drop the circle detection and use mouse on the cardboard "gun" while standing.
With the mouse problems solved by using the actual mouse the need to customise keys arouse and I chose to use .properties file as a simple offline database to store the location of the region and the type of the virtual key to be pressed. The program detects the amount of white pixels in those regions, and if they are above a certain threshold, then the key is pressed. Currently the only drawback of the program is that movement is not detected very good in poor lightning conditions, so this is curcumvented by increasing the detection threshold (this option is hardcoded for now).
## Known Issues



## F.A.Q.
### Q. Can I use my smartphone as a web camera?
A. You can via software, but unfortunately this creates a flickering input and is not good for detection. All virtual cameras have the same problem.
### Q. Will this be useful in VR?
A. I think it would be great for VR - you just need a simple web camera. I have not tested it for VR, because I do not have one.
### Q. How do I determine the effect of the exercise?
A. The faster it makes you sweat, the more efficient is the exercise. Note that with time your body will adapt to constant exercise and you will not be sweating so easily.
### Q. Why First person and third person games (FPS) and not RTS (real time strategy) and strategy games?
A. 1st and 3rd person games let you take on the view of the soldier/warrior/ and is more immersive and intuitive - you move your feet - the avatar moves; you move your hand for the reload button - the avatar executes the reload animation with his hands. On the other side in RTS you play a commander, which is delegating the tough job of fighting to his subordinates, so basically he just shouts orders - go there, attack that, build this. I personally like very much strategy games and if I can make this program be immersive for them I will be very happy.
### Q. What about racing games?
A. In racing games you are seated while driving, so that is not much of an exercise. Though it still can be used, but I don't have anything in mind conserning the steering wheel virtualisation.

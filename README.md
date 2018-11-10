# MovementControllerByNeutralizer

## Introduction
This program is designed with the idea of losing weight while playing computer games. It is based on 2 technologies - computer vision and game bot command issuing(automation testing): Movement is detected in specific regions via a web camera and when it is detected, the respective keyboard key attached to that region is being pressed.
## Disclaimer
The program is still in its ALPHA state and is quite unfinished. Please do not use it for online competitive games, because since it uses game bot technology, You may be detected as a cheater. Use it in single player FPS or TPS games for now, while this is under development.
## Getting Started
What should I do to run this alpha concoction:
### Download
The <a href="https://github.com/Neutralizer/MovementControllerByNeutralizer/releases">Releases</a> page holds the current version and presets
### Preparation
1. In order to be able to run this program you need java installed: <br>
Do I have java installed already? - If you can run the executable jar - then you don't need to install it.<br>
How do I install java? - go to java official site and download it - https://www.java.com/en/download/ . <br>
2. After downloading the executable jar You need the .properties files, which are templates of the regions, which press the keys when movement is detected. For now they are hardcoded, but in the future they will be manipulated via GUI.
3. Put the jar executable file and the .properties files in a folder and start the program.
4. You need a web camera, in order for the whole "computer vision" magic to work.<br>
4.1 Make sure that You have drivers for Your web camera.<br>
4.2 If You do not have a web camera, you can convert your smartphone's camera to a computer web camera with additional software - I tried  DroidCam and it works.
### Running the program
While there is currently a very mediocre GUI, You can select a camera and a preset from it. After starting the program and several(no more than 5) seconds pass, a black window will appear, which should detect your movement if it is not very dark in Your room. If nothing happens, then check the F.A.Q. <br>
Insert picture here.<br>
When You move Your hand in a square on the camera, the program will press the respective key. Open a notepad and move your hands in the detection range of the camera, to see how the buttons are being pressed.<br>
When you are comfortable with the controls, alt tab to your game and enjoy.<br>
Insert tutorial video here<br>
## History

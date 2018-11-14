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
1. In order to be able to run this program You need java installed: If You can't run the executable jar go to java official site and download - https://www.java.com/en/download/ . <br>
2. You need a web camera in order for the whole "computer vision" magic to work (Make sure that You have drivers for Your web camera).<br>
### Running the program
While there is currently a very mediocre GUI, You can select a camera and a preset(.properties file) from it. The .properties files hold predefined regions, which press their respective keys when movement is detected. For now they are hardcoded, but in the future they will be manipulated via GUI. After starting the program and several(no more than 5) seconds pass, a black window will appear, which should detect Your movement if it is not very dark in Your room. If nothing happens, then check the F.A.Q. <br>
Insert picture here.<br>
When You move Your hand in a square(region of interest) on the camera, the program will press the respective key. Open a notepad and move Your hands in the detection range of the camera, to see how the buttons are being pressed.<br>
When You are comfortable with the controls, alt tab to Your game and enjoy.<br>
Insert tutorial video here<br>
## History
### The idea

### The realisation
## F.A.Q.
### Q. Can I use my smartphone as a web camera? <br>
A. You can via software, but unfortunately this creates a flickering input and is not good for detection. All virtual cameras have the same problem.
### Q. 

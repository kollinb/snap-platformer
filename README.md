# Snap! Platformer

![Game Play](images/play-through.gif?raw=true "Game Play")

The actual file you would import into [Snap!](http://byob.eecs.berkeley.edu/snapsource/snap.html) is the XML file.

## Tools
In order to create a text engine in Snap!, I needed to generate a image for every character I wanted to use in my character set. I also needed to generate them at different sizes, because although there was a function to grow/shrink sprites, there was no support to use vectorized images. This caused the images to become blurry rather quickly. In order to ease the process of creating all these images, I made a program in Java that would generate them for me. This program is located under tools/font2png. It will ask you for input such as the font file location, the name you want for the font, and the size. Based off that that information, it will generate the images for you in a folder based off of the name.

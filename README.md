# Tetris (made in Java)

I don't play Tetris very well xd


https://github.com/RockyRickaby/Tetris/assets/111474120/2d0f13d0-64cd-487a-83e9-e4d21a8d62c1


This is just one of my little personal projects, which was, as you might've realized
by the title, implementing a Tetris clone (in Java. I'm kinda obsessed with Java
because of college, but I do wish to experiment with other languages too).

No external libraries were used and this game has not been tested (yet) on other platforms besides Windows. The only library used is the Swing library, which comes with the JDK.

Also, this is a VSCODE Java project :|

## Some implementation details (not many)

This implementation was developed with (at least part of) the [Tetris Guidelines](https://harddrop.com/wiki/Tetris_Guideline) in mind. More details about this implementation are listed below (and also, here I'll call the pieces *Tetrominoes* instead of *Tetriminos* whenever I mention them by name):

- The colors of the Tetrominoes are as defined by the guidelines
- The playfield is 10x24
- There was an attempt to implement the Super/Standard Rotation System (SRS) for movements and rotations
- The pieces spawn already in the playfield
- The Random Generator for the pieces is based on the 7-bag Random Generator (but for now, there's no next piece *queue*, as the current implementation only allows for *one* next piece to be shown)
- There's no "hold piece" (nor sounds (nor a scoring system :/ ))
- The ghost piece is enabled by default, but can be toggled on/off by pressing the G key during gameplay

As for the controls:

- Arrow keys move the Tetromino (down key is soft drop)
- Space hard drops the Tetromino
- Z rotates the piece 90° counterclockwise
- Up and X rotate the piece 90° clockwise
- G toggles the ghost piece on/off
- Esc pauses the game

The State enum is completely unused and will be removed whenever I feel like it

The game has yet to be properly centralized on wider screens.

### Some other things that might be worth talking about

For some reason I've not only decided that everything surrounding the game (except
for the rendering part) would have the y-axis growing upwards instead of downwards, I've also made everything essentially one-indexed ☠️. This has led me to a few problems (especially when rendering stuff on the screen), but in my humble opinion, it was worth it.

I also tried to apply certain concepts that I've learned in my Computer Graphics classes so far (19/05/2024), but without much success.

Also, a lot of the methods defined in the project return copies of attributes instead of references to the things. I hope you don't mind it :D

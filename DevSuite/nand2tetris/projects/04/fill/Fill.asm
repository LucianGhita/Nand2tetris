// This file is part of www.nand2tetris.org
// and the book "The Elements of Computing Systems"
// by Nisan and Schocken, MIT Press.
// File name: projects/04/Fill.asm

// Runs an infinite loop that listens to the keyboard input.
// When a key is pressed (any key), the program blackens the screen,
// i.e. writes "black" in every pixel;
// the screen should remain fully black as long as the key is pressed. 
// When no key is pressed, the program clears the screen, i.e. writes
// "white" in every pixel;
// the screen should remain fully clear as long as no key is pressed.

// Put your code here.

(READKBD)
@KBD
D=M
@CLEARSCREEN
D;JEQ

@DRAWBLACK
0;JMP

(DRAWBLACK)
@SCREEN
D=A

@addr
M=D

@KBD
D=A

@n
M=D

@i
M=0

(LOOP)

@addr
D=M
@n
D=D-M
@READKBD
D;JGT

@addr
A=M
M=-1

@addr
M=M+1

@KBD
D=M
@READKBD
D;JEQ

@LOOP
0;JMP

(END)
@END
0;JMP




(CLEARSCREEN)
@SCREEN
D=A

@addr
M=D

@KBD
D=A

@n
M=D

@i
M=0

(LOOPCLEAR)


@addr
D=M
@n
D=D-M
@READKBD
D;JGT

@addr
A=M
M=0

@addr
M=M+1

@KBD
D=M
@READKBD
D;JEQ

@LOOPCLEAR
0;JMP


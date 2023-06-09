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
(STARTPROGRAM)
@KBD
D=M

@DRAWBLACK
D;JGT

@CLEARSCREEN
D;JEQ

@STARTPROGRAM
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
@KBD
D=M
@CLEARSCREEN
D;JEQ

@addr
D=M
@n
D=D-M
@STARTPROGRAM
D;JGT

@addr
A=M
M=-1

@addr
M=M+1

@LOOP
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
@KBD
D=M
@DRAWBLACK
D;JGT

@addr
D=M
@n
D=D-M
@STARTPROGRAM
D;JEQ

@addr
A=M
M=0

@addr
M=M+1

@LOOPCLEAR
0;JMP



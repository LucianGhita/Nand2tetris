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
// check if a key is pressed
@DRAWBLACK
D;JGT
// if the KBD register has the value 0, clean the screen
@CLEARSCREEN
D;JEQ
// infinite loop for STARTPROGRAM, that means that it clears or it draws black indefinetly
@STARTPROGRAM
0;JMP

(DRAWBLACK)
// get the address of the screen
@SCREEN
D=A
// put the address of the screen in the variable addr
@addr
M=D
// get the address of the keyboard
@KBD
D=A
// put the value of the address in the variable n
@n
M=D
// initialize the variable i with 0
@i
M=0
// check if KBD is 0, then clear
(LOOP)
@KBD
D=M
@CLEARSCREEN
D;JEQ
// put the value at address var into D register
@addr
D=M
// this does current address - address of the keyboard
@n
D=D-M
// if current address - address of the keyboard is greater than 0 jump to startprogram
// if the result is < 0 that means that we are still in the range address of the screen 
// KBD address is 24,576 and the screen address ends up at 24,575
@STARTPROGRAM
D;JGT
// else point to the value of the address and add -1 as a value of the "pixel"
// (actually this puts 1111 1111 1111 1111 in the actual register because of 2's complement)
@addr
A=M
M=-1
// increase the value of the address in order to go to the next row in the memory
@addr
M=M+1
// do that forever (or until jumps to clear or start)
@LOOP
0;JMP

(CLEARSCREEN)
// get the address of the screen
@SCREEN
D=A
// put it in the addr variable
@addr
M=D
// get the address of the keyboard
@KBD
D=A
// put it in the n variable
@n
M=D
// initialize i = 0
@i
M=0
// this does the same thing as in drawing part, but puts 0 as a value not -1, in order to clear the screen
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



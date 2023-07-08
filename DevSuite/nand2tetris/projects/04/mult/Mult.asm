// This file is part of www.nand2tetris.org
// and the book "The Elements of Computing Systems"
// by Nisan and Schocken, MIT Press.
// File name: projects/04/Mult.asm

// Multiplies R0 and R1 and stores the result in R2.
// (R0, R1, R2 refer to RAM[0], RAM[1], and RAM[2], respectively.)
//
// This program only needs to handle arguments that satisfy
// R0 >= 0, R1 >= 0, and R0*R1 < 32768.

// Put your code here.

// add 0 to R2
@R2
M=0
@R0
D=M
// check if R0 is 0, if so, jump to END
@END
D;JEQ
@R1
D=M
// check if R1 is 0, if so, jump to END
@END
D;JEQ
// N is equal R1
@n
M=D
// in the loop, we get the value from N
(LOOP)
@n
D=M
// check if n is 0, if so, jump to END
@END
D;JEQ
// get the value of R0 in the D register
@R0
D=M
// put the sum in the R2 register
@R2
M=D+M
// decrease n with 1
@n
M=M-1
// do that until we jump to end inside the loop
@LOOP
0;JMP
(END)
@END
0;JMP
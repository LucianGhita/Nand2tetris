@256
D=A
@0
M=D
//call Sys.init 0
// push returnAddress
@Sys.init$0
D=A
@0
A=M
M=D
@0
M=M+1
// push LCL
@1
D=M
@0
A=M
M=D
@0
M=M+1
// push ARG
@2
D=M
@0
A=M
M=D
@0
M=M+1
// push THIS
@3
D=M
@0
A=M
M=D
@0
M=M+1
// push THAT
@4
D=M
@0
A=M
M=D
@0
M=M+1
// reposition ARG
@5
D=A
@0
D=M-D
@0
D=D-A
@2
M=D
// reposition LCL
@0
D=M
@1
M=D
// call function
@Sys.init
0;JMP
// declare return address
(Sys.init$0)
//Main.fibonacci 0
(Main.fibonacci)
//push argument 0
@2
D=M
@0
D=D+A
A=D
D=M
@0
A=M
M=D
@0
M=M+1
//push constant 2
@2
D=A
@0
A=M
M=D
@0
M=M+1
//lt
@0
M=M-1
A=M
D=M
@0
M=M-1
A=M
D=M-D
@PUSHT0
D;JLT
@0
A=M
M=0
@CONTINUE0
0;JMP
(PUSHT0)
@0
A=M
M=-1
(CONTINUE0)
@0
M=M+1
//C_IF IF_TRUE
@0
M=M-1
A=M
D=M
@IF_TRUE
D;JGT
D;JLT
//C_GOTO IF_FALSE
@IF_FALSE
0;JMP
//C_LABEL IF_TRUE
(IF_TRUE)
//push argument 0
@2
D=M
@0
D=D+A
A=D
D=M
@0
A=M
M=D
@0
M=M+1
//return
// endframe = LCL
@1
D=M
@R13
M=D
// retAddr = *(endframe-5)
@5
D=A
@R13
D=M-D
A=D
D=M
@R14
M=D
// *ARG = pop()
@0
M=M-1
A=M
D=M
@2
A=M
M=D
// SP = ARG + 1
@2
D=M
@0
M=D+1
// THAT = *(endframe-1)
@1
D=A
@R13
D=M-D
A=D
D=M
@4
M=D
// THIS = *(endframe-2)
@2
D=A
@R13
D=M-D
A=D
D=M
@3
M=D
// ARG = *(endframe-3)
@3
D=A
@R13
D=M-D
A=D
D=M
@2
M=D
// LCL = *(endframe-4)
@4
D=A
@R13
D=M-D
A=D
D=M
@1
M=D
// goto retAddr
@R14
A=M
0;JMP
//C_LABEL IF_FALSE
(IF_FALSE)
//push argument 0
@2
D=M
@0
D=D+A
A=D
D=M
@0
A=M
M=D
@0
M=M+1
//push constant 2
@2
D=A
@0
A=M
M=D
@0
M=M+1
//sub
@0
M=M-1
A=M
D=M
@0
M=M-1
A=M
M=M-D
@0
M=M+1
//call Main.fibonacci 1
// push returnAddress
@Main.fibonacci$1
D=A
@0
A=M
M=D
@0
M=M+1
// push LCL
@1
D=M
@0
A=M
M=D
@0
M=M+1
// push ARG
@2
D=M
@0
A=M
M=D
@0
M=M+1
// push THIS
@3
D=M
@0
A=M
M=D
@0
M=M+1
// push THAT
@4
D=M
@0
A=M
M=D
@0
M=M+1
// reposition ARG
@5
D=A
@0
D=M-D
@1
D=D-A
@2
M=D
// reposition LCL
@0
D=M
@1
M=D
// call function
@Main.fibonacci
0;JMP
// declare return address
(Main.fibonacci$1)
//push argument 0
@2
D=M
@0
D=D+A
A=D
D=M
@0
A=M
M=D
@0
M=M+1
//push constant 1
@1
D=A
@0
A=M
M=D
@0
M=M+1
//sub
@0
M=M-1
A=M
D=M
@0
M=M-1
A=M
M=M-D
@0
M=M+1
//call Main.fibonacci 1
// push returnAddress
@Main.fibonacci$2
D=A
@0
A=M
M=D
@0
M=M+1
// push LCL
@1
D=M
@0
A=M
M=D
@0
M=M+1
// push ARG
@2
D=M
@0
A=M
M=D
@0
M=M+1
// push THIS
@3
D=M
@0
A=M
M=D
@0
M=M+1
// push THAT
@4
D=M
@0
A=M
M=D
@0
M=M+1
// reposition ARG
@5
D=A
@0
D=M-D
@1
D=D-A
@2
M=D
// reposition LCL
@0
D=M
@1
M=D
// call function
@Main.fibonacci
0;JMP
// declare return address
(Main.fibonacci$2)
//add
@0
M=M-1
A=M
D=M
@0
M=M-1
A=M
M=D+M
@0
M=M+1
//return
// endframe = LCL
@1
D=M
@R13
M=D
// retAddr = *(endframe-5)
@5
D=A
@R13
D=M-D
A=D
D=M
@R14
M=D
// *ARG = pop()
@0
M=M-1
A=M
D=M
@2
A=M
M=D
// SP = ARG + 1
@2
D=M
@0
M=D+1
// THAT = *(endframe-1)
@1
D=A
@R13
D=M-D
A=D
D=M
@4
M=D
// THIS = *(endframe-2)
@2
D=A
@R13
D=M-D
A=D
D=M
@3
M=D
// ARG = *(endframe-3)
@3
D=A
@R13
D=M-D
A=D
D=M
@2
M=D
// LCL = *(endframe-4)
@4
D=A
@R13
D=M-D
A=D
D=M
@1
M=D
// goto retAddr
@R14
A=M
0;JMP
//Sys.init 0
(Sys.init)
//push constant 4
@4
D=A
@0
A=M
M=D
@0
M=M+1
//call Main.fibonacci 1
// push returnAddress
@Main.fibonacci$3
D=A
@0
A=M
M=D
@0
M=M+1
// push LCL
@1
D=M
@0
A=M
M=D
@0
M=M+1
// push ARG
@2
D=M
@0
A=M
M=D
@0
M=M+1
// push THIS
@3
D=M
@0
A=M
M=D
@0
M=M+1
// push THAT
@4
D=M
@0
A=M
M=D
@0
M=M+1
// reposition ARG
@5
D=A
@0
D=M-D
@1
D=D-A
@2
M=D
// reposition LCL
@0
D=M
@1
M=D
// call function
@Main.fibonacci
0;JMP
// declare return address
(Main.fibonacci$3)
//C_LABEL WHILE
(WHILE)
//C_GOTO WHILE
@WHILE
0;JMP

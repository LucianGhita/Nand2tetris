@256
D=A
@0
M=D
//call Sys.init 0
@Sys.init$0
D=A
@0
A=M
M=D
@0
M=M+1
@1
D=A
@0
A=M
M=D
@0
M=M+1
@2
D=A
@0
A=M
M=D
@0
M=M+1
@3
D=A
@0
A=M
M=D
@0
M=M+1
@4
D=A
@0
A=M
M=D
@0
M=M+1
@5
D=A
@0
D=M-D
@0
D=D-A
@2
M=D
@0
D=M
@1
M=D
@Sys.init
0;JMP
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
@1
D=M
@R13
M=D
@5
D=A
@R13
D=M-D
A=D
D=M
@R14
M=D
@0
M=M-1
A=M
D=M
@2
A=M
M=D
@2
D=M
@0
M=D+1
@1
D=A
@R13
D=M-D
A=D
D=M
@4
M=D
@2
D=A
@R13
D=M-D
A=D
D=M
@3
M=D
@3
D=A
@R13
D=M-D
A=D
D=M
@2
M=D
@4
D=A
@R13
D=M-D
A=D
D=M
@1
M=D
@R13
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
@Main.fibonacci$1
D=A
@0
A=M
M=D
@0
M=M+1
@1
D=A
@0
A=M
M=D
@0
M=M+1
@2
D=A
@0
A=M
M=D
@0
M=M+1
@3
D=A
@0
A=M
M=D
@0
M=M+1
@4
D=A
@0
A=M
M=D
@0
M=M+1
@5
D=A
@0
D=M-D
@1
D=D-A
@2
M=D
@0
D=M
@1
M=D
@Main.fibonacci
0;JMP
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
@Main.fibonacci$2
D=A
@0
A=M
M=D
@0
M=M+1
@1
D=A
@0
A=M
M=D
@0
M=M+1
@2
D=A
@0
A=M
M=D
@0
M=M+1
@3
D=A
@0
A=M
M=D
@0
M=M+1
@4
D=A
@0
A=M
M=D
@0
M=M+1
@5
D=A
@0
D=M-D
@1
D=D-A
@2
M=D
@0
D=M
@1
M=D
@Main.fibonacci
0;JMP
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
@1
D=M
@R13
M=D
@5
D=A
@R13
D=M-D
A=D
D=M
@R14
M=D
@0
M=M-1
A=M
D=M
@2
A=M
M=D
@2
D=M
@0
M=D+1
@1
D=A
@R13
D=M-D
A=D
D=M
@4
M=D
@2
D=A
@R13
D=M-D
A=D
D=M
@3
M=D
@3
D=A
@R13
D=M-D
A=D
D=M
@2
M=D
@4
D=A
@R13
D=M-D
A=D
D=M
@1
M=D
@R13
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
@Main.fibonacci$3
D=A
@0
A=M
M=D
@0
M=M+1
@1
D=A
@0
A=M
M=D
@0
M=M+1
@2
D=A
@0
A=M
M=D
@0
M=M+1
@3
D=A
@0
A=M
M=D
@0
M=M+1
@4
D=A
@0
A=M
M=D
@0
M=M+1
@5
D=A
@0
D=M-D
@1
D=D-A
@2
M=D
@0
D=M
@1
M=D
@Main.fibonacci
0;JMP
(Main.fibonacci$3)
//C_LABEL WHILE
(WHILE)
//C_GOTO WHILE
@WHILE
0;JMP

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
//Sys.init 0
(Sys.init)
//push constant 4000
@4000
D=A
@0
A=M
M=D
@0
M=M+1
//pop pointer 0
@0
M=M-1
A=M
D=M
@3
M=D
//push constant 5000
@5000
D=A
@0
A=M
M=D
@0
M=M+1
//pop pointer 1
@0
M=M-1
A=M
D=M
@4
M=D
//call Sys.main 0
// push returnAddress
@Sys.main$1
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
@Sys.main
0;JMP
// declare return address
(Sys.main$1)
//pop temp 1
@6
D=A
@R13
M=D
@0
M=M-1
A=M
D=M
@R13
A=M
M=D
//C_LABEL LOOP
(LOOP)
//C_GOTO LOOP
@LOOP
0;JMP
//Sys.main 5
(Sys.main)
@0
A=M
M=0
@0
M=M+1
@0
A=M
M=0
@0
M=M+1
@0
A=M
M=0
@0
M=M+1
@0
A=M
M=0
@0
M=M+1
@0
A=M
M=0
@0
M=M+1
//push constant 4001
@4001
D=A
@0
A=M
M=D
@0
M=M+1
//pop pointer 0
@0
M=M-1
A=M
D=M
@3
M=D
//push constant 5001
@5001
D=A
@0
A=M
M=D
@0
M=M+1
//pop pointer 1
@0
M=M-1
A=M
D=M
@4
M=D
//push constant 200
@200
D=A
@0
A=M
M=D
@0
M=M+1
//pop local 1
@1
D=A
@1
D=D+M
@R13
M=D
@0
M=M-1
A=M
D=M
@R13
A=M
M=D
//push constant 40
@40
D=A
@0
A=M
M=D
@0
M=M+1
//pop local 2
@2
D=A
@1
D=D+M
@R13
M=D
@0
M=M-1
A=M
D=M
@R13
A=M
M=D
//push constant 6
@6
D=A
@0
A=M
M=D
@0
M=M+1
//pop local 3
@3
D=A
@1
D=D+M
@R13
M=D
@0
M=M-1
A=M
D=M
@R13
A=M
M=D
//push constant 123
@123
D=A
@0
A=M
M=D
@0
M=M+1
//call Sys.add12 1
// push returnAddress
@Sys.add12$2
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
@Sys.add12
0;JMP
// declare return address
(Sys.add12$2)
//pop temp 0
@5
D=A
@R13
M=D
@0
M=M-1
A=M
D=M
@R13
A=M
M=D
//push local 0
@1
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
//push local 1
@1
D=M
@1
D=D+A
A=D
D=M
@0
A=M
M=D
@0
M=M+1
//push local 2
@1
D=M
@2
D=D+A
A=D
D=M
@0
A=M
M=D
@0
M=M+1
//push local 3
@1
D=M
@3
D=D+A
A=D
D=M
@0
A=M
M=D
@0
M=M+1
//push local 4
@1
D=M
@4
D=D+A
A=D
D=M
@0
A=M
M=D
@0
M=M+1
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
//Sys.add12 0
(Sys.add12)
//push constant 4002
@4002
D=A
@0
A=M
M=D
@0
M=M+1
//pop pointer 0
@0
M=M-1
A=M
D=M
@3
M=D
//push constant 5002
@5002
D=A
@0
A=M
M=D
@0
M=M+1
//pop pointer 1
@0
M=M-1
A=M
D=M
@4
M=D
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
//push constant 12
@12
D=A
@0
A=M
M=D
@0
M=M+1
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

//push constant 111
@111
D=A
@0
A=M
M=D
@0
M=M+1
//push constant 333
@333
D=A
@0
A=M
M=D
@0
M=M+1
//push constant 888
@888
D=A
@0
A=M
M=D
@0
M=M+1
//pop static 8
@8
D=A
@Foo.8
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
//pop static 3
@3
D=A
@Foo.3
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
//pop static 1
@1
D=A
@Foo.1
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
//push static 3
@Foo.3
D=A
@0
A=M
M=D
@0
M=M+1
//push static 1
@Foo.1
D=A
@0
A=M
M=D
@0
M=M+1
//sub
//push static 8
@Foo.8
D=A
@0
A=M
M=D
@0
M=M+1
//add

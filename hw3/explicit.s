	.file	"out.s"

	.data
Xx:
	.long	0
Xy:
	.long	0

	.text
	.globl	XinitGlobals
XinitGlobals:
	pushq	%rbp
	movq	%rsp, %rbp
  #pre

	movl	$2, %eax    #eax = 2
	movl	%eax, Xx    # x = 2
	movl	Xx, %eax    # x -> eax
	movl	Xx, %edi    # x -> edi
	imull	%edi, %eax  #eax = x*x
	movl	%eax, Xy    #x = x*x

  #post
	movq	%rbp, %rsp
	popq	%rbp
	ret

	.globl	Xmain
Xmain:
	pushq	%rbp
	movq	%rsp, %rbp
	movl	Xy, %edi
	call	Xprint
	movq	%rbp, %rsp
	popq	%rbp
	ret


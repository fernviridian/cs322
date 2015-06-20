	.file	"out.s"

	.data
Xx:
	.long	0
Xy:
	.long	0

	.text
	.globl	Xmain
Xmain:
	pushq	%rbp
	movq	%rsp, %rbp
	movl	Xy, %edi
	call	Xprint
	movq	%rbp, %rsp
	popq	%rbp
	ret


	.file	"out.s"

	.data

	.text
	.globl	Xmain
Xmain:
	pushq	%rbp
	movq	%rsp, %rbp
	movl	$1, %eax
	pushq	%rax
	movl	$2, %eax
	pushq	%rax
	movl	$2, %eax
	pushq	%rax
	jmp	l1
l0:
	movl	$0, %eax
	pushq	%rax
	jmp	l3
l2:
	movl	-16(%rbp), %edi
	movl	-32(%rbp), %esi
	call	Xf
	movl	%eax, -16(%rbp)
	movl	-32(%rbp), %edi
	call	Xg
	movq	%rax, %rdi
	call	Xh
	movq	%rax, %rdi
	call	Xprint
	movl	$1, %eax
	movl	-32(%rbp), %edi
	addl	%edi, %eax
	movl	%eax, -32(%rbp)
l3:
	movl	-8(%rbp), %eax
	movl	-32(%rbp), %edi
	cmpl	%eax, %edi
	jl	l2
	movl	-16(%rbp), %edi
	call	Xprint
	movl	-16(%rbp), %eax
	movl	-24(%rbp), %edi
	imull	%edi, %eax
	movl	%eax, -16(%rbp)
	movl	-8(%rbp), %eax
	movl	$1, %edi
	addl	%edi, %eax
	movl	%eax, -8(%rbp)
	addq	$8, %rsp
l1:
	movl	$10, %eax
	movl	-8(%rbp), %edi
	cmpl	%eax, %edi
	jl	l0
	movl	-8(%rbp), %edi
	call	Xprint
	movq	%rbp, %rsp
	popq	%rbp
	ret

	.globl	Xf
Xf:
	pushq	%rbp
	movq	%rsp, %rbp  #prologue
	movl	%edi, %eax  #eax = arg1
	movl	%esi, %ecx  #ecx = arg2
	subl	%ecx, %eax  #eax = arg1
	pushq	%rax        # new local = arg1 - arg2;
	movl	-8(%rbp), %eax  # int derp = local; -> eax
	pushq	%rax            #
	pushq	%rsi            
	pushq	%rdi
	movl	-8(%rbp), %edi #move derp into edi
	call	Xg              #g(derp)
	movq	%rax, %rcx      #return -> rcx
	popq	%rdi            #
	popq	%rsi
	popq	%rax
	imull	%ecx, %eax     #
	movl	%eax, -8(%rbp)  #
	movl	-8(%rbp), %eax  #
	movq	%rbp, %rsp
	popq	%rbp
	ret

	.globl	Xg
Xg:
	pushq	%rbp
	movq	%rsp, %rbp
	movl	$2, %eax
	movl	%edi, %esi
	imull	%esi, %eax
	movl	%edi, %esi
	subl	%esi, %eax
	movq	%rbp, %rsp
	popq	%rbp
	ret

	.globl	Xh
Xh:
	pushq	%rbp
	movq	%rsp, %rbp
	movl	$2, %eax
	movl	%edi, %esi
	cmpl	%eax, %esi
	jnl	l4
	movl	$1, %eax
	movq	%rbp, %rsp
	popq	%rbp
	ret
l4:
	pushq	%rdi
	movl	-8(%rbp), %edi
	movl	$1, %esi
	subl	%esi, %edi
	call	Xh
	popq	%rdi
	pushq	%rax
	pushq	%rdi
	movl	-16(%rbp), %edi
	movl	$2, %esi
	subl	%esi, %edi
	call	Xh
	movq	%rax, %rsi
	popq	%rdi
	popq	%rax
	addl	%esi, %eax
	movq	%rbp, %rsp
	popq	%rbp
	ret


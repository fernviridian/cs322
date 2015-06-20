	.file	"program4.s"
        .text
	.globl f
f:
      #first array rdi
      #second array rsi

      pushq   %rbx            #store in a nice safe place
      pushq   %rbp            #store in a nice safe place
      movl    (%rdi), %eax    #get length of array into eax
      movl    (%rsi), %ebx    #second array length in ebx
      movl    $0, %ecx        #set counter
      cmpl    %ebx, %eax
      je      swap

      #else they are different and return their lengths
      cmpl    %ebx, %eax      
      jg      sub             #if ebx < eax, eax = eax-ebx
      subl    %eax, %ebx      #else if ebx > eax, ebx = ebx-eax
      movl    %ebx, %eax      #set absolute value of diff length
      jmp     diff_done

sub:
      subl    %ebx, %eax      #get absolute value of length diff
      jmp     diff_done

swap:
      addq    $4, %rdi      #get next int
      addq    $4, %rsi
      cmpl    %ecx, %eax      # ecx= count, eax = array length
      je      same_done       # if they are the same, we're done, so set return to 0
      incl    %ecx            #increment counter
      movl    (%rsi), %ebp    #do the swap
      movl    (%rdi), %ebx    #SWAAPPPPINNN'
      movl    %ebp, (%rdi)  
      movl    %ebx, (%rsi)    #yep, still swappin'
      jmp     swap

same_done:
      movl    $0, %eax        #if they are the same length, return 0
diff_done:
      popq    %rbp            #restore vars
      popq    %rbx
      ret

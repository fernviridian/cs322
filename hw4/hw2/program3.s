	.file	"program3.s"
        .text
	.globl f
f:
      pushq   %rbx            #store in a nice safe place
      pushq   %rbp            #store in a nice safe place
      movl    (%rdi), %eax    #get length of array into eax
      movq    %rdi, %r8       #store pointer to next elemtn in r8    
      movl    $0, %ecx        #initalize counter
      addq    $4, %rdi        #increment rdi to next int
      cmpl    $0, %eax        #if eax is 0 (length of first array), we can be done already since there is no work to do
      je      done

outer_loop:
      #runs in a forward motion, moving one int at a time through the array
      addq    $4, %r8         #move forward one
      incl    %ecx            #increment count
      cmpl    %eax, %ecx      #if count and len dont match jump outer loop
      jne     outer_loop

inner_seek:
      #seek backwards through the array
      cmpq    %r8, %rdi       #if rdi > r8 then we are done since they met in the middle
      jnl     done
      movl    (%rdi), %ebx    #read from both ends
      movl    (%r8), %edx     #edx gets backwards way
      movl    %edx, (%rdi)    #move them back in reverse order
      movl    %ebx, (%r8)     
      addq    $4, %rdi        #increment forward
      subq    $4, %r8         #increment backwards
      jmp    inner_seek       #do it agaiiiiiinnnnnn

done:
      popq    %rbp            #restore these values
      popq    %rbx      
      movl    $1, %eax        #return 1 as per intructions
      ret

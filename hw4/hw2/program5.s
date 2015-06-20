	.file	"program5.s"
        .text
	.globl f
f:
      pushq   %rbx            #store in a nice safe place
      pushq   %rbp            #store in a nice safe place
      pushq   %rdi            #storing for later in mult
      pushq   %rsi            #storing for later in mult
      movl    (%rdi), %eax    #get length of array into eax
      movl    (%rsi), %ebx    #second array length in ebx
      movl    $0, %ecx
      cmpl    %eax, %ebx      #check to see if they are the same length
      jne     notequal

equal:
      addq    $4, %rsi
      addq    $4, %rdi        #set ptr to next int
      movl    (%rdi), %ebp
      movl    (%rsi), %ebx
      cmpl    %eax, %ecx
      je      mult
      cmpl    %ebp, %ebx      #if(rsi[i] < rdi[i]), then put ebx in ebp and visa-versa
      jl      swap
cont:
      #else we dont do anything
      incl    %ecx            #just increment the count and jump back to equal
      jmp     equal


swap: 
      #if the elements are in the wrong order, swap them and return to cont
      movl    (%rdi), %ebp 
      movl    (%rsi), %ebx
      movl    %ebp, (%rsi)
      movl    %ebx, (%rdi)
      jmp     cont

mult:
      #second part of problem, need to calculate . product
      popq    %rsi        #restore original values so we can start at the beginning of each array
      popq    %rdi        # <3 the stack
      movl    (%rsi), %edx  #get length
      movl    $0, %ecx    #INITialized
      movl    $0, %eax

loop: 
      addq    $4, %rdi    #increment both arrays
      addq    $4, %rsi
      movl    (%rsi), %ebp    #move values into 32 bit registers
      movl    (%rdi), %ebx    
      cmpl    %edx, %ecx      #if edx (length) and ecx (counter) are the same then we are done
      je      done

      imul    %ebp, %ebx  #top * bottom, stoore in ebx, then add to value in eax
      addl    %ebx, %eax
      incl    %ecx        #increment counter
      jmp     loop

notequal:
      #if the arrays are not equal length
      addl    %ebx, %eax  #add up lengths and return that
      popq    %rsi
      popq    %rdi        #need to pull these off the stack

done:
      popq    %rbp        #restore ebp rbx
      popq    %rbx
      ret

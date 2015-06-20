	.file	"program2.s"
        .text
	.globl f
f:
        pushq   %rbx         #save so we can use ebx name
        pushq   %rbp         #save so we can use rbp name
        movl    $0, %ecx     #initialize counter
        movl    $0, %ebp     #total sum value register -- ebp
        movl    (%rdi), %edx #edx now has length (first element)

firstloop:
        #counts total number zeros in first array and stores count in ebp
        cmpl    %edx, %ecx   #if were done iterating go to second loop
        je      secondsetup
                             #else, get and add next element
        addq    $4, %rdi
        incl    %ecx
        cmpl    $0, (%rdi)    #if rdi array element ==0, increment
        je      addzero

        jmp     firstloop

addzero: 
        #if we saw a zero in the first array, count it
        incl    %ebp
        jmp firstloop

secondsetup:
        movl    $0, %ecx     #set counter back to 0
        movl    (%rsi), %edx #get second array length
        cmpl    $0, %edx     #if empty, skip the second array
        je      done
        jmp     secondloop
  
secondloop:
        #count the number of nonzeros in the second array
        cmpl    %edx, %ecx  #if we reached the end of the array, were done
        je      done

        addq    $4, %rsi    #move forward
        incl    %ecx        #increment count
        cmpl    $0, (%rsi)  #if the value is nonzero, go to nzadd
        jne     nzadd

        jmp     secondloop

nzadd:
        #count the nonzero element
        incl    %ebp
        jmp     secondloop


done:
        movl    %ebp, %eax    #set total count as return value
        popq    %rbp          #bring back pushed on values into correct regs
        popq    %rbx
        ret

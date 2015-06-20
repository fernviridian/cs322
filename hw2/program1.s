	.file	"program1.s"
        .text
	.globl f
f:

        pushq   %rbx         #save so we can use ebx because I like ebx's name
        pushq   %rbp         #save for same reason
        movl    $0, %ecx     #initialize counter
        movl    $0, %ebp     #total sum value register
        movl    (%rdi), %edx #edx now has length (first element)

firstloop:
        #loop sums up total values in first array
        cmpl    %edx, %ecx   #if were done iterating go to second loop
        je      secondsetup
                             #else, get and add next element
        addq    $4, %rdi
        addl    (%rdi), %ebp #
        incl    %ecx
        jmp     firstloop

secondsetup:
        #reset counter for iterating before second loop
        movl    $0, %ecx
        movl    (%rsi), %edx #get second array length
        cmpl    $0, %edx    #if the second array is empty jump to done
        je      done
        jmp     secondloop  #the got to second loop
  
secondloop:
        #adds values of elements to current total for second arary
        cmpl    %edx, %ecx
        je      done
        addq    $4, %rsi
        addl    (%rsi), %ebp #add to total count
        incl    %ecx
        jmp     secondloop

done:
        movl    %ebp, %eax    #set total count as return value
        popq    %rbp          #bring back pushed on values into correct regs
        popq    %rbx
        ret                   #rerererereretuuuuurnnnnnnnn!

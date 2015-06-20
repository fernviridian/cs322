	.file	"program6.s"
        .text
	.globl f
f:
      pushq   %rbx            #store in a nice safe place
      pushq   %rbp            #store in a nice safe place
      movl    (%rdi), %eax    #get length of array into eax
      movl    (%rsi), %ebx    #second array length in ebx
      movl    %eax, %r11d     #r12d holds first array length
      pushq   %rbx
      addl    %eax, %ebx      #len + len -> ebx
      movl    %ebx, %edx      #store total length in r10
      popq    %rbx
      movl    $0, %ecx        #set counter
      cmpl    $0, %eax        #if first array is empty jump there
      je      firstempty
      cmpl    $0, %ebx        #if second array is empty jump there
      je      secondempty

      #else, both full, so we drop into bothfull

######---Both arrays contain values-----######
bothfull:
      pushq   4(%rdi)     #save first array first element
      pushq   4(%rsi)     #save second array first element
      decl    %ebx        #len of rsi-1
      decl    %r11d       #len of rdi-1

bloop:
      #shift first array left one
      addq    $4, %rdi        #move along the array
      cmpl    %ecx, %r11d     #if we have reached n-1 array elements, go to second loop
      je      b2loopsetup

      movl    4(%rdi), %r10d    #shove next element into current index, like array[0] = array[1], etc
      movl    %r10d, (%rdi)

      incl    %ecx       #count + 1
      jmp     bloop

b2loopsetup:
      #reset the counter
      movl    $0, %ecx
      jmp     b2loop

b2loop:
      addq    $4, %rsi        #move along second array, this is teh same as bloop but for second array
      cmpl    %ecx, %ebx      #if we reached n-1, restore end elements
      je      restore

      movl    4(%rsi), %r10d  #just like bloop
      movl    %r10d, (%rsi)
      
      incl    %ecx
      jmp     b2loop

restore:
      #place first element from both arrays in their proper spot
      popq    %rax
      movl    %eax, (%rdi)    #move second array first element into fist array last element
      popq    %rax
      movl    %eax, (%rsi)    #move first array first element into last array last element
      jmp     done
      
#######--first array is empty-----#####

firstempty:
      #check that the other is not empty
      cmpl    $0, %ebx
      je      done      #if both are empty then were are done

      decl    %ebx    #decrement so we dont overrun our floop

      #rsi is second array, rdi is first array
      #then rsi has something, but rdi is an "tempty array"
      pushq   4(%rsi)
      #r8d is 32bit
      #now we shift left all elements one
floop: 
      addq    $4, %rsi      #increment array element

      cmpl    %ecx, %ebx    #count <= len, then we do fplaceback, just like restore
      je      fplaceback

      movl    4(%rsi), %r10d  #array[0] = array[1], etc, etc
      movl    %r10d, (%rsi)

      incl    %ecx       #count + 1
      jmp     floop

fplaceback:
      #just like restore:, but with only one array
      popq    %rax
      movl    %eax, (%rsi)    #set last element as the first from original array
      jmp     done

#end first empty

########-----SECOND EMPTY-------########

secondempty:
      #check that the other is not emptry
      cmpl    $0, %r11d
      je      done      #if both are empty then were are done

      decl    %r11d    #decrement so we dont overrun our floop

      #rdi is second array, rdi is first array
      #then rdi has something, but rdi is an "tempty array"
      pushq   4(%rdi)
      #r8d is 32bit
      #now we shift left all elements one
sloop: 
      addq    $4, %rdi

      cmpl    %ecx, %r11d #count <= len
      je      splaceback

      movl    4(%rdi), %r10d
      movl    %r10d, (%rdi)

      incl    %ecx       #count + 1
      jmp     sloop

splaceback:
      #just like fplaceback or restore with other array
      popq    %rax
      movl    %eax, (%rdi)    #set last element as the first from original array
      jmp     done

done:
      popq    %rbp          #restore some variables
      popq    %rbx
      movl    %edx, %eax    #move sum of length into return value
      ret


.PHONY	: all clean

CC = gcc -O

all	: program1 program2 program3 program4 program5 program6
	./program1 "Total of numbers in two arrays"
	./program2 "Zeros in first plus nonzeros in second"
	./program3 "Reverse the first"
	./program4 "Swap arrays"
	./program5 "Pairwise compares and inner product"
	./program6 "Rotate"

program1: program1.s main.c
	$(CC) -o program1 -g main.c program1.s

program2: program2.s main.c
	$(CC) -o program2 -g main.c program2.s

program3: program3.s main.c
	$(CC) -o program3 -g main.c program3.s

program4: program4.s main.c
	$(CC) -o program4 -g main.c program4.s

program5: program5.s main.c
	$(CC) -o program5 -g main.c program5.s

program6: program6.s main.c
	$(CC) -o program6 -g main.c program6.s

clean	:
	rm program1 program2 program3 program4 program5 program6


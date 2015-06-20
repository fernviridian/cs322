#include <stdio.h>

//Uncomment the two lines in this file that refer
//to XinitGlobals() when you have implemented support for
//initializing global variables.

//extern void XinitGlobals();
extern void Xmain();

int main(int argc, char** argv) {
  //XinitGlobals();
    Xmain();
    return 0;
}

void Xprint(int val) {
    printf("output: %d\n", val);
}

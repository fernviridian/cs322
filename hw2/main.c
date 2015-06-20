#include <stdio.h>

extern int f(int*, int*);

void printArray(int* a) {
  char* msg = " ";
  int   len = *a;
  int   i   = 1;
  for (; i<=len; ++i) {
    printf("%s%d", msg, a[i]);
    msg = ", ";
  }
}

void test(char* label, int* l, int* r) {
    printf("%5s|", label);
    printArray(l);
    printf(" |");
    printArray(r);
    printf(" |\n");
    printf("%5d|",f(l,r));
    printArray(l);
    printf(" |");
    printArray(r);
    printf(" |\n\n");
}

int main() {
  int a[] = { 6, 0, 1, 2, 3, 4, 5 };
  int b[] = { 6, 4, 5, 6, 7, 8, 9 };
  int c[] = { 0 };
  int d[] = { 3, 1, 1, 7 };

  test("ab", a, b);
  test("bc", b, c);
  test("bd", b, d);
  test("db", b, d);
  test("ca", c, a);
  test("cc", c, c);
  return 0;
}

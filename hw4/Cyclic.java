class Cyclic {
  static final int S = 100;
  static final int N = 10;

  public static void main(String[] args) {
    Heap h = new TwoSpace(S);

    int t = h.alloc(1);
    int z = h.alloc(1);
    h.store(t, 1, z); //store something in offset 1
    h.store(z, 1, t); //store pointer back to beginning of obj
    h.a = t;
    h.a = z;

    System.out.println("Before garbage collection;");
    h.dump();
    System.out.println("Free space remaining = " + h.freeSpace());

    h.garbageCollect();

    System.out.println("After garbage collection;");
    h.dump();
    System.out.println("Free space remaining = " + h.freeSpace());
  }
}

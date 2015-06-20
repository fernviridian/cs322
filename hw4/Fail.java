class Fail {
  static final int S = 100;

  public static void main(String[] args) {
    Heap h = new TwoSpace(S);
    h.alloc(3);
    int t = h.alloc(9);
    //h.a = h.alloc(9);
    h.alloc(3);
    h.store(h.a, 1, h.alloc(5));
    h.alloc(5);
    h.store(h.a, 2, h.alloc(4));
    h.alloc(2);

    h.dump();
    System.out.println("Free space remaining = " + h.freeSpace());

    h.garbageCollect();

    h.dump();
    System.out.println("Free space remaining = " + h.freeSpace());
  }
}

class TestHeap1 {
  static final int S = 100;

  public static void main(String[] args) {
    Heap h    = Heap.make(S);
    int root  = h.alloc(5);
    h.store(root, 1, h.alloc(3));
    h.store(root, 2, h.alloc(2));
    h.dump();
    System.out.println("Free space remaining = " + h.freeSpace());
  }
}

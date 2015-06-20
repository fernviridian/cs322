class Heap {
  protected int   size;           // Size of heap
  protected int[] heap;           // Storage for heap
  protected int   hp;             // Heap allocation pointer

  public int a = 0;               // Some "registers"
  public int b = 0;
  public int c = 0;
  public int d = 0;

  /** Construct a new heap with the specified size.
   */
  public Heap(int size) {
     this.size = size;
     heap      = new int[size];
     hp        = 0;               // Initial heap pointer.
  }

  /** Make a new heap.  This is a so-called "factory" method.
   */
  public static Heap make(int size) {
    // Only one of the following two lines should be uncommented,
    // depending on whether you want to use a Heap or a TwoSpace,
    // either without or with garbage collection, respectively.
    return new Heap(size);
    //return new TwoSpace(size);
  }

  /** Print out a description of the heap contents.
   */
  public void dump() {
    int i = 0;
    while (i<hp) {
      int len = heap[i];
      System.out.print("Object at address " + (i - size)
                       + ", length=" + len
                       + ", data=[");
      for (int j=1; j<=len; j++) {
        System.out.print(Integer.toString(heap[i+j]));
        if (j<len) {
          System.out.print(", ");
        }
      }
      System.out.println("]");
      i += (len + 1);
    }
    System.out.println("Heap allocation pointer: " + hp);
  }

  /** Return the amount of free/unused heap space.
   */
  int freeSpace() {
    return size - hp;
  }

  /** Allocate a new object of the given length.
   */
  int alloc(int len) {
    if (hp+len+1 > size) {     // Check for space in the heap.
       garbageCollect();       // Invoke garbage collector.
       if (hp+len+1 > size) {  // And then retest for space in heap.
          fatal("Out of memory!");
       }
    }
    int result = hp-size;
    for (heap[hp++]=len; len>0; len--) {
      heap[hp++] = 0;
    }
    return result;
  }

  /** Run the garbage collector.
   */
  public void garbageCollect() {
    fatal("There is no garbage collector for a plain Heap!");
  }

  /** Calculate the address within the heap for a field of
   *  a given object with a specified offset for the field.
   */
  private int getAddr(int obj, int offs) {
    obj = obj + size;
    if (obj<0 || obj>=hp) {
      fatal("Invalid object reference");
    }
    int len = heap[obj];
    if (offs<1 || offs>len) {
      fatal("Invalid offset");
    }
    return obj+offs;
  }

  /** Load a value from a particular field of a given object.
   */
  public int load(int obj, int offs) {
    return heap[getAddr(obj, offs)];
  }
  
  /** Save a value in a particular field of a given object
   */
  public void store(int obj, int offs, int val) {
    heap[getAddr(obj, offs)] = val;
  }

  /** Display an error message when something goes wrong and
   *  terminate the program.
   */
  protected static void fatal(String msg) {
    System.out.print("Fatal error: ");
    System.out.println(msg);
    System.exit(1);
  }
}

import java.util.Hashtable;

class Memory {
  private Hashtable<String,Integer> store
     = new Hashtable<String,Integer>();

  int load(String name) {
    Integer i = store.get(name);
    return (i!=null) ? i.intValue() : 0;
  }

  void store(String name, int val) {
    store.put(name, new Integer(val));
  }
}

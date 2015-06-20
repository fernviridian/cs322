package ast;
import compiler.Failure;

/** Represents a linked list of location environments, with each entry
 *  documenting the location of a particular variable in memory.
 */
public abstract class LocEnv {

    protected String name;

    private LocEnv next;

    /** Default constructor.
     */
    public LocEnv(String name, LocEnv next) {
        this.name = name;
        this.next = next;
    }

    /** Return the variable name for this environment entry.
     */
    public String getName() { return name; }

    /** Return the tail of this environment.
     */
    public LocEnv next() { return next; }

    /** Search this environment for a the occurence of a given variable.
     *  We assume that a previous static analysis has already identified
     *  and eliminate references to unbound variables.
     */
    public LocEnv find(String name) {
        for (LocEnv env=this; env!=null; env=env.next) {
            if (name.equals(env.name)) {
                return env;
            }
        }
        throw new Error("Could not find environment entry for " + name);
    }

    /** Return a string that describes the location associated with
     *  this enviroment entry.
     */
    public abstract String loc32(Assembly a);
}

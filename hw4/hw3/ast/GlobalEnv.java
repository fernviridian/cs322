package ast;
import compiler.Failure;

/** Represents an environment entry for a global variable.
 */
public class GlobalEnv extends LocEnv {

    /** Default constructor.
     */
    public GlobalEnv(String name, LocEnv next) {
        super(name, next);
    }

    /** Return a string that describes the location associated with
     *  this enviroment entry.
     */
    public String loc32(Assembly a) { return a.name(name); }
}

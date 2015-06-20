package ast;
import compiler.Failure;

/** Represents an environment entry for a variable stored in a register.
 */
public class RegEnv extends LocEnv {

    private Reg reg;

    /** Default constructor.
     */
    public RegEnv(String name, LocEnv next, Reg reg) {
        super(name, next);
        this.reg = reg;
    }

    /** Return a string that describes the location associated with
     *  this enviroment entry.
     */
    public String loc32(Assembly a) { return reg.r32(); }
}

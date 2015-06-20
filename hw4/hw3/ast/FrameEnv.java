package ast;
import compiler.Failure;

/** Represents an environment entry for a variable stored in the stack frame.
 */
public class FrameEnv extends LocEnv {

    private int offset;

    /** Default constructor.
     */
    public FrameEnv(String name, LocEnv next, int offset) {
        super(name, next);
        this.offset = offset;
    }

    /** Return a string that describes the location associated with
     *  this enviroment entry.
     */
    public String loc32(Assembly a) { return a.indirect(offset, Reg.basePointer.r64()); }
}

package ast;
import compiler.Failure;

public class CallFrame extends Frame {

    /** Records the number of bytes that are pushed on the stack
     *  for arguments and alignment purposes.
     */
    private int argBytes;

    /** Construct a new Frame Layout object for a function call.
     */
    public CallFrame(LocEnv env, int pushed, int argBytes) {
        super(null, env); // TODO: eliminate need for null?
        this.pushed   = pushed;
        this.argBytes = argBytes;
        argOffset     = pushed;

        // Initialize the register map, including paramBase,
        // freeBase, and free:
        regmap = new Reg[Reg.calleeSaves.length
                       + Reg.args.length
                       + Reg.results.length
                       + Reg.callerSaves.length];
        int r = 0;
        for (int i=0; i<Reg.calleeSaves.length; i++) {
            regmap[r++] = Reg.calleeSaves[i];
        }
        // FreeBase coincides with ParamBase so we can start loading
        // parameter registers.
        free = freeBase = paramBase = r;
        for (int i=0; i<Reg.args.length; i++) {
            regmap[r++] = Reg.args[i];
        }
        for (int i=0; i<Reg.results.length; i++) {
            regmap[r++] = Reg.results[i];
        }
        for (int i=0; i<Reg.callerSaves.length; i++) {
            regmap[r++] = Reg.callerSaves[i];
        }
    }

    /** Counts the number of arguments added to the frame.
     */
    int argsAdded = 0;

    /** Records offset for next stack argument.
     */
    int argOffset;

    /** Save the value in the current free register as the next
     *  argument to this function.
     */
    public void saveArg(Assembly a) {
        if (argsAdded<Reg.args.length) {
            // The first few arguments are passed in registers:
            free++;
        } else {
            // Remaining arguments are passed on the stack:
            a.emit("movq", free64(), a.indirect(-argOffset,Reg.basePointer.r64()));
            argOffset -= a.QUADSIZE;
        }
        argsAdded++;
    }

    /** Call the function, and remove argument bytes.
     */
    public void call(Assembly a, String name) {
        // On systems where alignment is important, pushed should be
        // a multiple of 16 at this point.
        a.emit("call", a.name(name));
        insertAdjust(a, -argBytes);
    }
}

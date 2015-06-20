package ast;
import compiler.Failure;

public class FunctionFrame extends Frame {

    /** Construct a new Frame Layout object for a function with the
     *  given list of formal parameters and the given environment
     *  describing global variables.
     */
    public FunctionFrame(Formal[] formals, LocEnv globals) {
        super(formals, globals);

        // Initialize the register map, including paramBase,
        // freeBase, and free:
        regmap = new Reg[Reg.calleeSaves.length
                       + Reg.args.length
                       + Reg.results.length
                       + Reg.callerSaves.length];
        int r  = 0;
        int i;
        // Callee Saves Register are considered "in use" from the
        // start of the function.
        for (i=0; i<Reg.calleeSaves.length; i++) {
            regmap[r++] = Reg.calleeSaves[i];
        }

        // Next come the registers that are used to supply parameters.
        // These registers also contribute entries to the environment.
        paramBase = r;
        for (i=0; i<Reg.args.length && i<formals.length; i++) {
            regmap[r++] = Reg.args[i];
            env         = new RegEnv(formals[i].getName(), env, Reg.args[i]);
        }

        // Any formal parameters that did not fit in registers will be
        // found on the stack at positive offsets from the base pointer,
        // and will require corresponding entries in the stack frame.
        for (int j=i; j<formals.length; j++) {
            int offset = (2+j-i)*Assembly.QUADSIZE;
            env = new FrameEnv(formals[j].getName(), env, offset);
        }
        
        // Any remaining registers are considerd free for use, starting
        // with the result register(s):
        freeBase = free = r;
        for (int j=0; j<Reg.results.length; j++) {
            regmap[r++] = Reg.results[j];
        }
        // Followed by any unused argument registers:
        for (; i<Reg.args.length; i++) {
            regmap[r++] = Reg.args[i];
        }
        // And then any callerSaves registers:
        for (i=0; i<Reg.callerSaves.length; i++) {
            regmap[r++] = Reg.callerSaves[i];
        }
        // If we need any registers beyond this, we will need to wrap around
        // and start using the callee saves registers at the start of the
        // register map.  (With appropriate spilling, of course.)
    }
}

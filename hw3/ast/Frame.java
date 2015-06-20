package ast;
import compiler.Failure;

/** Captures information about the layout of the frame for a given
 *  function, including details about register use as well as stack
 *  layout.
 */
public abstract class Frame {

    /** Records the list of formal parameters for the corresponding
     *  function.
     */
    protected Formal[] formals;

    /** Holds an environment describing the mappings from variables
     *  to locations in the current stack frame.
     */
    protected LocEnv env;

    /** Default constructor.
     */
    public Frame(Formal[] formals, LocEnv env) {
        this.formals = formals;
        this.env = env;
    }

    /** Return the environment at this point in the code.
     */
    public LocEnv getEnv() { return env; }

    /** Holds the register map for this layout.
     */
    protected Reg[] regmap;

    /** Map from logical register number to physical register.
     */
    protected Reg reg(int n) {
        return regmap[n % regmap.length];
    }

    /** Holds the index of the first parameter register.
     */
    protected int paramBase;

    /** Holds the index of the first free register.
     */
    protected int freeBase;

    /** Holds the index of the current free register.
     */
    protected int free;

    /** Holds the number of bytes that are currently pushed on
     *  the stack.  Initially, of course, there are no bytes
     *  pushed on the stack.
     */
    protected int pushed = 0;

    /** Return the current free register.
     */
    public Reg free() { return reg(free); }

    /** Return the name of the current 32 bit register.
     */
    public String free32() { return free().r32(); }

    /** Return the name of the current 64 bit register.
     */
    public String free64() { return free().r64(); }

    /** Save the value in the current free 32 bit register in
     *  the location corresponding to a particular value.
     */
    void store32(Assembly a, String lhs) {
        a.emit("movl", free32(), env.find(lhs).loc32(a));
    }

    /** Load a value from a location corresponding to a particular
     *  variable into the current free 32 bit register.
     */
    void load32(Assembly a, String name) {
        a.emit("movl", env.find(name).loc32(a), free32());
    }

    /** Make the next available register free, spilling the contents
     *  of that register on to the stack if it was already in use.
     *  Every call to spill() must also be paired with a correponding
     *  call to unspill().
     */
    public Reg spill(Assembly a) {
        Reg r = reg(++free);
        // Save old register value if necessary:
        if (free>=regmap.length) {
            // Save register on the stack
            a.emit("pushq", r.r64());
            pushed += Assembly.QUADSIZE;

            // If we just spilled a formal parameter, update
            // the environment to reflect that.
            int n = free - (regmap.length + paramBase);
            if (n>=0 && formals!=null && n<formals.length && n<Reg.args.length) {
                env = new FrameEnv(formals[n].getName(), env, -pushed);
            }
        }
        return r;
    }

    /** Spill, as necessary, to ensure that the next free register is
     *  available for use, returning the associated 32 bit register
     *  name as a result.
     */
    public String spill32(Assembly a) { return spill(a).r32(); }

    /** Spill, as necessary, to ensure that the next free register is
     *  available for use, returning the associated 32 bit register
     *  name as a result.
     */
    public String spill64(Assembly a) { return spill(a).r64(); }

    /** Release the current free register, potentially unspilling a
     *  previously saved value for the underlying physical memory
     *  from the stack.  Pairs with a previous call to spill().
     */
    public void unspill(Assembly a) {
        Reg r = reg(free);
        if (free>=regmap.length) {
            // Restore saved register value:
            a.emit("popq", r.r64());
            pushed -= Assembly.QUADSIZE;

            // If we just unspilled a formal parameter, update
            // the environment to reflect that.
            int n = free - (regmap.length + paramBase);
            if (n>=0 && formals!=null && n<formals.length && n<Reg.args.length) {
                env = env.next();
            }
        }
        free--;
    }

    /** Allocate space on the stack for a local variable.
     */
    public void allocLocal(Assembly a, String name, String src) {
        a.emit("pushq", src);
        pushed += Assembly.QUADSIZE;
        env = new FrameEnv(name, env, -pushed);
    }

    /** Reset the stack pointer to a previous position at the end
     *  of a block, decrementing the stack pointer as necessary
     *  and removing items from the environment to reflect local
     *  variables going out of scope.
     */
    public void resetTo(Assembly a, LocEnv origEnv) {
        for (; env!=origEnv; env=env.next()) {
            pushed -= Assembly.QUADSIZE;
            a.insertAdjust(-Assembly.QUADSIZE);
        }
    }

    /** Add some number of bytes at the top of the stack, typically
     *  to meet some alignment constraint.
     */
    public void insertAdjust(Assembly a, int adjust) {
        pushed += adjust;
        a.insertAdjust(adjust);
    }

    /** Dump a description of this frame.
     */
    public void dump(Assembly a) {
        a.emit("# Registers: (free = " + free64() + ")");
        StringBuffer b = new StringBuffer("# ");
        int i = 0;
        for (; i<paramBase; i++) {
            b.append(" ");
            b.append(regmap[i].r64());
        }
        b.append(" <");
        for (; i<freeBase; i++) {
            b.append(" ");
            b.append(regmap[i].r64());
        }
        b.append(" >");
        for (; i<regmap.length; i++) {
            b.append(" ");
            b.append(regmap[i].r64());
        }
        a.emit(b.toString());
        a.emit("#");
        a.emit("# Pushed on stack: " + pushed);
        b = new StringBuffer("# Environment:");
        for (LocEnv env=this.env; env!=null; env=env.next()) {
            String n = env.name;
            String s = env.loc32(a);
            b.append(" ");
            b.append(n);
            if (!n.equals(s)) {
               b.append("->" + s);
            }
        }
        a.emit(b.toString());
        a.emit("#");
    }

    /** Create a new Frame for a call from within the current frame.
     *  This entails saving the values of any active caller saves
     *  registers on the stack and updating the environment to record
     *  the new locations for each formal parameter.
     */
    public CallFrame prepareCallFrame(Assembly a, int nargs) {
        // Save [b..r), counting down, skipping callee saves registers.
        int    r   = free;
        int    b   = Math.max(paramBase, free - (regmap.length+1));
        LocEnv env = this.env;
        while (--r>=b) {
            int rmod = r % regmap.length;
            if (rmod>=paramBase) {   // Caller saves registers
                pushed += a.QUADSIZE;// must be saved on the stack
                a.emit("pushq", reg(r).r64());
                if (r<freeBase) {    // Parameter registers?
                    // Update environment to indicate that a parameter
                    // variable is now on the stack instead of a register.
                    String name = formals[r-paramBase].getName();
                    env         = new FrameEnv(name, env, -pushed);
                }
            }
        }

        // Calculate space needed for stack arguments:
        int argBytes = Math.max(0, nargs-Reg.args.length) * a.QUADSIZE;

        // Add bytes as necessary to ensure correct alignment:
        argBytes    += a.alignmentAdjust(pushed + argBytes);

        // Create the new call frame:
        CallFrame cf = new CallFrame(env, pushed, argBytes);

        // The current frame includes saved registers for this frame,
        // but the argBytes are part of the call frame:
        cf.insertAdjust(a, argBytes);

        return cf;
    }

    /** Restore a frame to its original state after a call, ensuring that
     *  the return result from the function is in the free register and
     *  restoring any saved registers from the stack.
     */
    public void removeCallFrame(Assembly a) {
        // Move result into free register.
        Reg result = Reg.results[0];
        if (free()!=result) {
            a.emit("movq", result.r64(), free64());
        }

        // Restore [b..r), counting up, skipping callee saves registers.
        int r = free;
        int b = Math.max(paramBase, free - (regmap.length+1));
        for (; b<r; b++) {
            int bmod = b % regmap.length;
            if (bmod>=paramBase) {   // Caller saves registers
                pushed -= a.QUADSIZE;
                a.emit("popq", reg(b).r64());
            }
        }
    }
}

package ast;
import compiler.Failure;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/** Provides a simple mechanism for assembly language output.
 */
public class Assembly {

    /** Used to store the output stream for this assembly file.
     */
    private PrintWriter out;

    /** A private constructor, used from the assembleToFile() method.
     */
    private Assembly(PrintWriter out) {
        this.out = out;
    }

    /** Set the platform flag for this machine.
     */
    public final int platform = LINUX;

    /** Include this code in the platform choice if external symbols
     *  require a leading underscore.
     */
    static final int UNDERSCORES = 1;

    /** Include this code in the platform choice if the code generator
     *  should ensure that stack frames are aligned on a 16 byte boundary.
     */
    static final int ALIGN16 = 2;

    /** Platform flags for Linux.
     */
    static final int LINUX = 0;

    /** A convenience method that creates an assembly object for
     *  a named output file.
     */
    public static Assembly assembleToFile(String name) {
        try {
            PrintWriter out = new PrintWriter(new FileWriter(name));
            Assembly    a   = new Assembly(out);
            a.emit(".file",  "\"" + name + "\"");
            return a;
        } catch (IOException e) {
            return null;
        }
    }

    /** Close this Assembly object and free up associated resources.
     */
    public void close() {
        out.close();
        out = null;
    }

    /** A counter that is used to generate new labels; the counter is
     *  incremented each time a new label is produced.
     */
    private int labelCounter = 0;

    /** Generate a string for a label from an integer input.
     *  We require that distinct inputs produce distinct outputs
     *  and that none of the generated label names can clash with
     *  names in user programs.
     */
    public String label(int l) {
        return "l" + l;
    }

    /** Return a fresh (i.e., previously unused) label name.
     */
    public String newLabel() {
        return label(labelCounter++);
    }

    /** Output a label at the beginning of a line.
     */
    public void emitLabel(String name) {
        handlePendingAdjust();
        out.print(name);
        out.println(":");
    }

    /** Emit a blank line.
     */
    public void emit() {
        out.println();
    }

    /** Emit an instruction with no operands.
     */
    public void emit(String op) {
        handlePendingAdjust();
        out.println("\t" + op);
    }

    /** Emit an instruction with one operand.
     */
    public void emit(String op, String op1) {
        handlePendingAdjust();
        out.println("\t" + op + "\t" + op1);
    }

    /** Emit an instruction with two operands.
     */
    public void emit(String op, String op1, String op2) {
        handlePendingAdjust();
        out.println("\t" + op + "\t" + op1 + ", " + op2);
    }

    /** Return a number as a string for use in contexts where only
     *  a number is allowed and hence no special characters are
     *  required to distinguish an addressing mode.
     */
    public String number(int v) {
        return Integer.toString(v);
    }

    /** Return a string for an operand using immediate addressing.
     */
    public String immed(int v) {
        return "$" + v;
    }

    /** Output a function/variable name using the appropriate
     *  platform naming conventions with respect to underscores.
     */
    public String name(String n) {
        return (platform & UNDERSCORES)==0 ? ("X"+n) : ("_X" + n);
    }

    /** Return a reference to a memory location using indirect
     *  addressing.
     */
    public String indirect(int n, String s) {
        if (n==0) {
            return "(" + s + ")";
        } else {
            return n + "(" + s + ")";
        }
    }

    /** We assume that all values can be stored within a single "quadword"
     *  (i.e., 64 value), each of which takes 8 bytes in memory:
     */
    public static final int QUADSIZE = 8;

    /** Track the number of bytes by which the stack pointer should be
     *  adjusted before the next instruction.  This mechanism allows us
     *  to combine multiple adjustments into a single instruction, and
     *  to omit adjustments completely when they are not required (for
     *  example, immediately before a function's epilogue).
     */
    private int pendingAdjust = 0;

    /** Check to see if a stack adjustment is required before the next
     *  instruction is emitted.
     */
    public void handlePendingAdjust() {
        if (pendingAdjust!=0) {
            int adjust    = pendingAdjust;
            pendingAdjust = 0;
            if (adjust>0) {
                emit("subq", immed(adjust),  Reg.stackPointer.r64());
            } else {
                emit("addq", immed(-adjust), Reg.stackPointer.r64());
            }
        }
    }

    /** Adjust the stack by inserting space for the specified number of
     *  bytes.  Can be used to reserve space for locals, or to establish
     *  alignment constraints.  Can also be used with a negative argument
     *  to remove space.
     */
    void insertAdjust(int adjust) {
        pendingAdjust += adjust;
    }

    /** Calculate how many additional bytes need to be pushed onto
     *  the stack to ensure correct alignment once "pushed" bytes
     *  have been pushed.
     */
    int alignmentAdjust(int pushed) {
        // For platforms that need it (i.e., Mac OS X), we determine
        // how many extra bytes must be added to the stack to ensure
        // alignment on a 16 byte boundary.  For other platforms, we
        // can just return zero.
        return ((platform & ALIGN16)==0) ? 0 : ((16 - pushed) & 15);
    }

    /** Output the prologue code section at the start of a function.
     */
    public void emitPrologue() {
        emit("pushq", Reg.basePointer.r64());
        emit("movq",  Reg.stackPointer.r64(), Reg.basePointer.r64());
    }

    /** Output the epilogue code section at the end of a program.
     */
    public void emitEpilogue() {
        pendingAdjust = 0;
        emit("movq", Reg.basePointer.r64(), Reg.stackPointer.r64());
        emit("popq", Reg.basePointer.r64());
        emit("ret");
    }
}

package ast;
import compiler.Failure;

/** Represents a 64 bit register that also has a 32 bit register
 *  included as a component.
 */
public class Reg {

    /** The name of this register for 32 bit uses.
     */
    private String name32;

    /** The name of this register for 64 bit uses.
     */
    private String name64;

    /** Default constructor.
     */
    public Reg(String name32, String name64) {
        this.name32 = name32;
        this.name64 = name64;
    }

    /** Return the name of this register for 32 bit uses.
     */
    public String r32() { return name32; }

    /** Return the name of this register for 64 bit uses.
     */
    public String r64() { return name64; }

    /** The list of registers that are used to pass argument values.
     */
    public static final Reg[] args = new Reg[] {
        new Reg("%edi", "%rdi"),
        new Reg("%esi", "%rsi"),
        new Reg("%ecx", "%rcx"),
        new Reg("%edx", "%rdx"),
        new Reg("%r8d", "%r8"),
        new Reg("%r9d", "%r9")
      };

    /** The list of registers that are used to return results.
     */
    public static final Reg[] results = new Reg[] {
        new Reg("%eax", "%rax")
      };

    /** The list of caller saves registers.
     */
    public static final Reg[] callerSaves = new Reg[] {
        new Reg("%r10d", "%r10"),
        new Reg("%r11d", "%r11")
      };

    /** The list of callee saves registers.
     */
    public static final Reg[] calleeSaves = new Reg[] {
        new Reg("%ebx",  "%rbx"),
        new Reg("%r12d", "%r12"),
        new Reg("%r13d", "%r13"),
        new Reg("%r14d", "%r14"),
        new Reg("%r15d", "%r15")
      };

    /** The base pointer register.
     */
    public static final Reg basePointer = new Reg("%ebp",  "%rbp");

    /** The stack pointer register.
     */
    public static final Reg stackPointer = new Reg("%esp",  "%rsp");
}

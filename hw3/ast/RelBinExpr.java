package ast;
import compiler.Failure;

/** An abstract base class for relational binary expressions.
 */
public abstract class RelBinExpr extends BinExpr {

    /** Default constructor.
     */
    public RelBinExpr(Expr left, Expr right) {
        super(left, right);
    }

    /** Calculate the type of this expression, using the given context
     *  and type environment.
     */
    public Type typeOf(Context ctxt, TypeEnv locals)
      throws Failure {
        type = matchTypes(ctxt, locals);
        if (type==null) {
            ctxt.report(new Failure("RelBinArgs"));
        }
        return Type.BOOLEAN;
    }

    /** Generate code for a comparision operation.  The resulting
     *  code evaluates both left and right arguments, and then does
     *  a comparision, setting the flags ready for the appropriate
     *  conditional jump.  The free+1 register is both spilled and
     *  unspilled in this code, which means that the caller does
     *  not need to handle spilling.
     */
    void compileCond(Assembly a, Frame f) {
        String r0 = f.free32();
        String r1;
        if (left.getDepth()>right.getDepth() || right.getDepth()>=DEEP) {
            left.compileExpr(a, f);
            r1 = f.spill32(a);
            right.compileExpr(a, f);
            a.emit("cmpl", r1, r0);
        } else {
            right.compileExpr(a, f);
            r1 = f.spill32(a);
            left.compileExpr(a, f);
            a.emit("cmpl", r0, r1);
        }
        f.unspill(a);
    }

    /** Generate code for a comparison that computes either 1 (for
     *  true) or 0 (for false) in the specified free register.  The
     *  given "test" instruction is used to trigger a branch in the
     *  true case.
     */
    void compileCondValue(Assembly a, Frame f, String test) {
        String lab1 = a.newLabel();  // jump here if true
        String lab2 = a.newLabel();  // jump here when done
        compileCond(a, f);           // compare the two arguments
        a.emit(test, lab1);          // jump if condition is true
        a.emit("movl", a.immed(0), f.free32());
        a.emit("jmp",  lab2);
        a.emitLabel(lab1);
        a.emit("movl", a.immed(1), f.free32());
        a.emitLabel(lab2);           // continue with value in free
    }
}

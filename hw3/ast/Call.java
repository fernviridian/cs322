package ast;
import compiler.Failure;

public class Call extends StmtExpr {

    /** The name of the function that is being called.
     */
    private String name;

    /** The sequence of expressions provided as arguments.
     */
    private Expr[] args;

    /** Default constructor.
     */
    public Call(String name, Expr[] args) {
        this.name = name;
        this.args = args;
    }

    /** Print an indented description of this abstract syntax node,
     *  including a name for the node itself at the specified level
     *  of indentation, plus more deeply indented descriptions of
     *  any child nodes.
     */
    public void indent(IndentOutput out, int n) {
        out.indent(n, "Call");
        out.indent(n+1, "\"" + name + "\"");
        for (int i=0; i<args.length; i++) {
            args[i].indent(out, n+1);
        }
    }

    /** Calculate the type of this expression, using the given context
     *  and type environment.
     */
    public Type typeOf(Context ctxt, TypeEnv locals)
      throws Failure {
        Type rt = check(ctxt, locals);
        if (rt==null) {
            throw new Failure("CallReturnType");
        }
        return rt;
    }

    /** Type check this statement expression, using the specified
     *  context and the given typing environment.
     */
    public Type check(Context ctxt, TypeEnv locals)
      throws Failure {
        if (ctxt.isGlobal) {
            throw new Failure("GlobalsNoCalls");
        }
        FunctionEnv fe = FunctionEnv.find(name, ctxt.functions);
        if (fe==null) {
            throw new Failure("FunctionDefined");
        }
        return fe.getFunction().checkArgs(ctxt, locals, args);
    }

    /** Return the depth of this expression as a measure of how complicated
     *  the expression is / how many registers will be needed to evaluate it.
     */
    int getDepth() {
        // Possible side effects due to assignment, so do not change order.
        return DEEP;
    }

    /** Generate assembly language code for this expression that will
     *  evaluate the expression when it is executed and leave the result
     *  in the next free register, as specified by the layout.
     */
    public void compileExpr(Assembly a, Frame f) {   // name(args)
        // Create a new call frame, saving registers as necessary:
        CallFrame cf = f.prepareCallFrame(a, args.length);
        //cf.dump(a);
  
        // Evaluate the arguments and add them to the frame:
        for (int i=0; i<args.length; i++) {
            args[i].compileExpr(a, cf);
            cf.saveArg(a);
        }
  
        // Call the function, and remove stack arguments:
        cf.call(a, name);
  
        // Set result register and restore saved registers:
        f.removeCallFrame(a);
    }
}

package ast;
import compiler.Failure;

/** Abstract syntax for global variable definitions.
 */
public class Globals extends Defn {

    /** The type of the variable(s) being defined.
     */
    private Type type;

    /** The names and initial values of the variables.
     */
    private VarIntro[] vars;

    /** Default constructor.
     */
    public Globals(Type type, VarIntro[] vars) {
        this.type = type;
        this.vars = vars;
    }

    /** Print an indented description of this definition.
     */
    public void indent(IndentOutput out, int n) {
        out.indent(n, "Globals");
        out.indent(n+1, type.toString());
        for (int i=0; i<vars.length; i++) {
           vars[i].indent(out, n+1);
        }
    }

    /** Extend the environments in the given context with entries from
     *  this definition.
     */
    void extendGlobalEnv(Context ctxt)
      throws Failure {
        for (int i=0; i<vars.length; i++) {
           vars[i].extendGlobalEnv(ctxt, type);
        }
    }

    /** Declare storage for global variables.
     */
    LocEnv declareGlobals(Assembly a, LocEnv env) {
        // Initialize global variables:
        for (int i=0; i<vars.length; i++) {
           env = vars[i].declareGlobals(a, env);
        }
        return env;
    }

    /** Generate compiled code for a function.
     */
    void compileFunction(Assembly a, LocEnv globals) {
        // Nothing to do here, but this method is required
        // because Globals extends Defn.
    }

    void compileGlobals(Assembly a, Frame f){
       for(int i = 0; i < vars.length; i++){
          vars[i].compileGlobals(a,f); //initvarintro compile
       }
    }
}

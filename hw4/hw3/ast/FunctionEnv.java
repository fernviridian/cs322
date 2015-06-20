package ast;
import compiler.Failure;

/** Represents a function environment, mapping function
 *  names to corresponding function definitions.
 */
public class FunctionEnv {

    private String name;

    private Function function;

    private FunctionEnv next;

    public FunctionEnv(String name, Function function, FunctionEnv next) {
        this.name      = name;
        this.function  = function;
        this.next      = next;
    }

    /** Represents the empty environment that does not bind any
     *  variables.
     */
    public static final FunctionEnv empty = null;

    /** Search an environment for a specified variable name,
     *  returning null if no such entry is found, or else
     *  returning a pointer to the first matching FunctionEnv
     *  object in the list.
     */
    public static FunctionEnv find(String name, FunctionEnv env) {
        while (env!=null && !env.name.equals(name)) {
            env = env.next;
        }
        return env;
    }

    /** Return the Function associated with this entry.
     */
    public Function getFunction() {
        return function;
    }

    /** Check each of the functions in the specified environment
     *  using the given context.
     */
    public static void check(Context ctxt, FunctionEnv fenv)
      throws Failure {
        while (fenv!=null) {
            fenv.function.check(ctxt);
            fenv = fenv.next;
        }
    }
}

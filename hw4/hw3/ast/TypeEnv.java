package ast;
import compiler.Failure;

/** Represents a typing environment, mapping identifier
 *  names to corresponding types.
 */
public class TypeEnv {

    private String name;

    private Type type;

    private TypeEnv next;

    public TypeEnv(String name, Type type, TypeEnv next) {
        this.name  = name;
        this.type  = type;
        this.next  = next;
    }

    /** Represents the empty environment that does not bind any
     *  variables.
     */
    public static final TypeEnv empty = null;

    /** Search an environment for a specified variable name,
     *  returning null if no such entry is found, or else
     *  returning a pointer to the first matching TypeEnv
     *  object in the list.
     */
    public static TypeEnv find(String name, TypeEnv env) {
        while (env!=null && !env.name.equals(name)) {
            env = env.next;
        }
        return env;
    }

    /** Return the value associated with this entry.
     */
    public Type getType() {
        return type;
    }
}

package ast;
import compiler.Failure;

/** Abstract syntax for  basic types.
 */
public class Type {

    String typename;

    /** Default constructor.
     */
    private Type(String typename) {
        this.typename = typename;
    }

    /** Represents the type of integers.
     */
    public static final Type INT = new Type("int");

    /** Represents the type of double precision floating point numbers.
     */
    public static final Type DOUBLE = new Type("double");

    /** Represents the type of booleans.
     */
    public static final Type BOOLEAN = new Type("boolean");

    /** Generate a printable name for this type.
     */
    public String toString() {
        return typename;
    }

    /** Test for a numeric type.
     */
    public boolean isNumeric() {
        return equals(INT) || equals(DOUBLE);
    }
}

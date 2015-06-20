package ast;
import compiler.Failure;

/** Represents an output phase for producing textual output of
 *  abstract syntax trees using indentation.
 */
public class IndentOutput {

    private java.io.PrintStream out;

    /** Default constructor.
     */
    public IndentOutput(java.io.PrintStream out) {
        this.out = out;
    }

    /** Print a given String message indented some number of
     *  spaces (currently two times the given nesting level, n).
     */
    public void indent(int n, String msg) {
        for (int i=0; i<n; i++) {
            out.print("  ");
        }
        out.println(msg);
    }

    /** Output an indented description of the abstract syntax
     *  tree for the given expression.
     */
    public void indent(Expr e) {
        e.indent(this, 0);
    }

    /** Output an indented description of the abstract syntax
     *  tree for the given statement.
     */
    public void indent(Stmt stmt) {
        stmt.indent(this, 0);
    }

    /** Output an indented description of the abstract syntax
     *  tree for the given program.
     */
    public void indent(Defn[] program) {
        for (int i=0; i<program.length; i++) {
            program[i].indent(this, 0);
        }
    }

    /** Output an indented description of the abstract syntax
     *  tree for the given definition.
     */
    public void indent(Defn defn) {
        defn.indent(this, 0);
    }
}

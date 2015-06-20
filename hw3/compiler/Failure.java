package compiler;

/** Represents an error diagnostic.  To avoid a clash with java.lang.Error,
 *  we resisted the temptation to call this class Error.
 */
public class Failure extends Exception {
    /** Used to hold a simple description of the problem that
     *  occurred.  This field is used only by the default
     *  implementation of getDescription() that is provided in
     *  this class.  More complex diagnostics are likely to
     *  override this method, and hence will not use this field.
     *
     *  The format and interpretation of the description field
     *  have not yet been determined.  It would, however, make
     *  sense to allow the use of some kind of XML/HTML tags to
     *  allow embedding of structure/formatting hints.
     */
    private String text;

    /** Return the text associated with this diagnostic.
     */
    public String getText() {
        return text;
    }

    /** Construct a simple diagnostic with a fixed description.
     */
    public Failure(String text) {
        this.text = text;
    } 
}


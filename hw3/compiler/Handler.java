package compiler;

/** Represents an error handler.  This particular error handler
 *  just collects any errors that are reported in a lexicographically
 *  sorted list so that they can be dumped out as a single group,
 *  without concern for ordering.
 */
public class Handler {
    private static class ErrorList {
        String    err;
        ErrorList next;
        ErrorList(String err, ErrorList next) {
            this.err = err; this.next = next;
        }

        static ErrorList insert(String err, ErrorList errs) {
            ErrorList prev = null;
            ErrorList curr = errs;
            while (curr!=null && err.compareTo(curr.err) >= 0) {
                prev = curr;
                curr = curr.next;
            }
            if (prev == null) {
                return new ErrorList(err, curr);
            } else {
                prev.next = new ErrorList(err, curr);
                return errs;
            }
        }

        static void dump(ErrorList errs) {
            int count=0;
            for (; errs!=null; errs=errs.next) {
                count++;
                System.out.println("ERROR: " + errs.err);
            }
            String plural = (count==1) ? "error" : "errors";
            System.out.println(count + " " + plural + " reported");
        }
    }

    /** Record the list of errors that have been passed to this
     *  handler.
     */
    private ErrorList errs;

    /** Report a problem to this diagnostic handler.
     */
    public void report(Failure f) {
        errs = ErrorList.insert(f.getText(), errs);
    }

    /** Signal whether errors have been detected.
     */
    public boolean hasErrors() {
        return errs!=null;
    }

    /** Dump out the errors that have been reported to this handler.
     */
    public void dump() {
        ErrorList.dump(errs);
    }
}

import ast.*;
import compiler.*;

public class Static {
    public static void main(String[] args) {
        Handler  handler    = new Handler();
        Defn[]   program    = null;
        new Parser(System.in);
        try {
            // Parse an input program:
            program = Parser.Top();

            // Run static analysis on the program:
            new Context(handler).check(program);

        } catch (ParseException e) {
            handler.report(new Failure("Syntax Error"));
        } catch (Failure f) {
            handler.report(f);
        }

        // Output the annotated abstract syntax tree:
        if (handler.hasErrors()) {
            handler.dump();
        } else {
            new IndentOutput(System.out).indent(program);
        }
    }
}

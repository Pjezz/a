package Lisp.AST;

import Lisp.Environment;

public class AtomNode extends ASTNode {
    private String value;
    private boolean quoted = false;

    public AtomNode(String value) {
        if (value.startsWith("'")) {
            this.value = value.substring(1);
            this.quoted = true;
        } else {
            this.value = value;
        }
    }

    @Override
    public Object evaluate(Environment env) {
        if (quoted)
            return value;

        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return env.getVariable(value);
        }
    }

    public String getValue() {
        return value;
    }

    public boolean isQuoted() {
        return quoted;
    }
}
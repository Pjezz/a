package Lisp;

import Lisp.AST.ASTNode;
import java.util.List;

public class FunctionDef {
    private List<String> parameters;
    private ASTNode body;

    public FunctionDef(List<String> parameters, ASTNode body) {
        this.parameters = parameters;
        this.body = body;
    }

    public ASTNode getBody() {
        return body;
    }

    public List<String> getParameters() {
        return parameters;
    }
}
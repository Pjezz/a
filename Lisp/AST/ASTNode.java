package Lisp.AST;

import Lisp.Environment;

public abstract class ASTNode {
    public abstract Object evaluate(Environment env);
}
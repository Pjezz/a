package Lisp.AST;

import java.util.ArrayList;
import java.util.List;

import Lisp.Environment;
import Lisp.Evaluator;

public class ListNode extends ASTNode {
    private List<ASTNode> children = new ArrayList<>();

    public void addChild(ASTNode node) {
        children.add(node);
    }

    public List<ASTNode> getChildren() {
        return children;
    }

    @Override
    public Object evaluate(Environment env) {
        return Evaluator.evaluate(this, env);
    }
}

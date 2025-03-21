package Lisp;

import java.util.HashMap;
import java.util.Stack;

public class Environment {
    private Stack<HashMap<String, Object>> scopes = new Stack<>();
    private HashMap<String, FunctionDef> functions = new HashMap<>();

    public Environment() {
        scopes.push(new HashMap<>());
    }

    public void pushScope() {
        scopes.push(new HashMap<>());
    }

    public void popScope() {
        if (scopes.size() > 1)
            scopes.pop();
    }

    public void setVariable(String name, Object value) {
        scopes.peek().put(name, value);
    }

    public Object getVariable(String name) {
        for (int i = scopes.size() - 1; i >= 0; i--) {
            if (scopes.get(i).containsKey(name)) {
                return scopes.get(i).get(name);
            }
        }
        return null;
    }

    public void defineFunction(String name, FunctionDef func) {
        functions.put(name, func);
    }

    public FunctionDef getFunction(String name) {
        return functions.get(name);
    }
}
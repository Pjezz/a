package Lisp;

import Lisp.AST.*;
import java.util.List;

public class Evaluator {
    public static Object evaluate(ASTNode node, Environment env) {
        if (node instanceof AtomNode) {
            return evaluateAtom((AtomNode) node, env);
        } else if (node instanceof ListNode) {
            return evaluateList((ListNode) node, env);
        }
        return null;
    }

    private static Object evaluateAtom(AtomNode atom, Environment env) {
        if (atom.isQuoted()) {
            return atom.getValue();
        }

        try {
            return Double.parseDouble(atom.getValue());
        } catch (NumberFormatException e) {
            Object value = env.getVariable(atom.getValue());
            return value != null ? value : atom.getValue();
        }
    }

    private static Object evaluateList(ListNode list, Environment env) {
        if (list.getChildren().isEmpty()) {
            return null; // Lista vacía
        }

        ASTNode first = list.getChildren().get(0);
        if (!(first instanceof AtomNode)) {
            return evalUserFunction(list, env);
        }

        String operator = ((AtomNode) first).getValue();

        switch (operator) {
            // Operaciones aritméticas
            case "+":
                return sum(list.getChildren().subList(1, list.getChildren().size()), env);
            case "-":
                return subtract(list.getChildren().subList(1, list.getChildren().size()), env);
            case "*":
                return multiply(list.getChildren().subList(1, list.getChildren().size()), env);
            case "/":
                return divide(list.getChildren().subList(1, list.getChildren().size()), env);

            // Definición de variables
            case "SETQ":
                return handleSetq(list, env);

            // Definición de funciones
            case "DEFUN":
                return handleDefun(list, env);

            // Condicionales
            case "COND":
                return handleCond(list, env);

            // Predicados
            case "ATOM":
                return evaluate(list.getChildren().get(1), env) instanceof AtomNode;
            case "LIST":
                return evaluate(list.getChildren().get(1), env) instanceof ListNode;
            case "EQUAL":
                return handleEqual(list, env);
            case "<":
                return handleLessThan(list, env);
            case ">":
                return handleGreaterThan(list, env);

            // Quote
            case "QUOTE":
                return list.getChildren().get(1);

            default: // Llamada a funciones
                return evalFunctionCall(list, env);
        }
    }

    // ========== Implementación de operaciones ==========
    private static double sum(List<ASTNode> nodes, Environment env) {
        double total = 0;
        for (ASTNode node : nodes) {
            total += toNumber(evaluate(node, env));
        }
        return total;
    }

    private static double subtract(List<ASTNode> nodes, Environment env) {
        if (nodes.isEmpty())
            return 0;
        double result = toNumber(evaluate(nodes.get(0), env));
        for (int i = 1; i < nodes.size(); i++) {
            result -= toNumber(evaluate(nodes.get(i), env));
        }
        return result;
    }

    private static double multiply(List<ASTNode> nodes, Environment env) {
        double product = 1;
        for (ASTNode node : nodes) {
            product *= toNumber(evaluate(node, env));
        }
        return product;
    }

    private static double divide(List<ASTNode> nodes, Environment env) {
        if (nodes.isEmpty())
            return 1;
        double result = toNumber(evaluate(nodes.get(0), env));
        for (int i = 1; i < nodes.size(); i++) {
            result /= toNumber(evaluate(nodes.get(i), env));
        }
        return result;
    }

    // ========== Manejo de SETQ ==========
    private static Object handleSetq(ListNode list, Environment env) {
        String varName = ((AtomNode) list.getChildren().get(1)).getValue();
        Object value = evaluate(list.getChildren().get(2), env);
        env.setVariable(varName, value);
        return value;
    }

    // ========== Manejo de DEFUN ==========
    private static Object handleDefun(ListNode list, Environment env) {
        String funcName = ((AtomNode) list.getChildren().get(1)).getValue();
        ListNode paramsNode = (ListNode) list.getChildren().get(2);
        List<String> parameters = paramsNode.getChildren().stream()
                .map(node -> ((AtomNode) node).getValue())
                .toList();
        ASTNode body = list.getChildren().get(3);
        env.defineFunction(funcName, new FunctionDef(parameters, body));
        return funcName;
    }

    // ========== Manejo de COND ==========
    private static Object handleCond(ListNode list, Environment env) {
        for (ASTNode clause : list.getChildren().subList(1, list.getChildren().size())) {
            ListNode clauseList = (ListNode) clause;
            ASTNode condition = clauseList.getChildren().get(0);
            if (toBoolean(evaluate(condition, env))) {
                return evaluate(clauseList.getChildren().get(1), env);
            }
        }
        return null;
    }

    // ========== Implementación de predicados ==========
    private static boolean handleEqual(ListNode list, Environment env) {
        Object val1 = evaluate(list.getChildren().get(1), env);
        Object val2 = evaluate(list.getChildren().get(2), env);
        return val1.equals(val2);
    }

    private static boolean handleLessThan(ListNode list, Environment env) {
        double n1 = toNumber(evaluate(list.getChildren().get(1), env));
        double n2 = toNumber(evaluate(list.getChildren().get(2), env));
        return n1 < n2;
    }

    private static boolean handleGreaterThan(ListNode list, Environment env) {
        double n1 = toNumber(evaluate(list.getChildren().get(1), env));
        double n2 = toNumber(evaluate(list.getChildren().get(2), env));
        return n1 > n2;
    }

    // ========== Funciones de apoyo ==========
    private static Object evalFunctionCall(ListNode list, Environment env) {
        FunctionDef func = env.getFunction(((AtomNode) list.getChildren().get(0)).getValue());
        if (func != null) {
            Environment localEnv = new Environment();
            List<String> params = func.getParameters();
            List<ASTNode> args = list.getChildren().subList(1, list.getChildren().size());

            for (int i = 0; i < params.size(); i++) {
                localEnv.setVariable(params.get(i), evaluate(args.get(i), env));
            }
            return evaluate(func.getBody(), localEnv);
        }
        return null;
    }

    private static Object evalUserFunction(ListNode list, Environment env) {
        return list.getChildren().stream()
                .map(node -> evaluate(node, env))
                .toList();
    }

    private static double toNumber(Object value) {
        return value instanceof Double ? (Double) value : 0.0;
    }

    private static boolean toBoolean(Object value) {
        return value != null && !value.equals(0) && !value.equals(false);
    }
}
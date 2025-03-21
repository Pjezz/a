package Lisp;

import Lisp.AST.*;
import java.util.*;

public class Parser {
    public static ASTNode parse(String input) {
        LinkedList<String> tokens = tokenize(input);
        return parseTokens(tokens);
    }

    private static LinkedList<String> tokenize(String input) {
        String[] rawTokens = input.replaceAll("\\(", " ( ")
                .replaceAll("\\)", " ) ")
                .split("\\s+");
        return new LinkedList<>(Arrays.asList(rawTokens));
    }

    private static ASTNode parseTokens(LinkedList<String> tokens) {
        if (tokens.isEmpty())
            return null;

        String token = tokens.removeFirst();
        if (token.equals("(")) {
            ListNode list = new ListNode();
            while (!tokens.peekFirst().equals(")")) {
                list.addChild(parseTokens(tokens));
            }
            tokens.removeFirst(); // Remove ")"
            return list;
        } else {
            return new AtomNode(token);
        }
    }
}
package Lisp;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import Lisp.AST.ASTNode;

public class Main {
    // Ruta ABSOLUTA de tu archivo LISP
    private static final String RUTA = "programa.lsp";

    public static void main(String[] args) {
        Environment env = new Environment();
        System.out.println("Leyendo archivo: " + RUTA);

        try (BufferedReader reader = new BufferedReader(new FileReader(RUTA))) {
            // Leer y ejecutar código LISP
            StringBuilder codigo = new StringBuilder();
            String linea;
            while ((linea = reader.readLine()) != null) {
                codigo.append(linea).append("\n");
            }

            ASTNode ast = Parser.parse(codigo.toString());
            Object resultado = Evaluator.evaluate(ast, env);
            System.out.println("Resultado: " + resultado);

        } catch (IOException e) {
            System.err.println("Error al leer el archivo: " + e.getMessage());
        }
    }
}
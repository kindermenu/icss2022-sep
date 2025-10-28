package nl.han.ica.icss.checker;

import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.literals.*;
import nl.han.ica.icss.ast.types.ExpressionType;

import java.util.HashMap;
import java.util.LinkedList;

import static nl.han.ica.icss.ast.types.ExpressionType.UNDEFINED;


public class Checker {

    private LinkedList<HashMap<String, ExpressionType>> variableTypes = new LinkedList<>();
    public void check(AST ast) {
//         variableTypes = new HANLinkedList<>();
        variableTypes.add(new HashMap<>());
        checkStylesheet(ast.root);
    }

    public void checkStylesheet(Stylesheet sheet){
        for (ASTNode child : sheet.getChildren()) {
            if (child instanceof Stylerule) {
                checkStylerule((Stylerule) child);
            }
            if (child instanceof VariableAssignment) {
                checkVariableAssignment((VariableAssignment) child);
            }
        }
    }

    private void checkVariableAssignment(VariableAssignment node) {
        System.out.println("entered variable assignment");
        String name;
        Expression expression = node.expression;

        ExpressionType type = getExpressionType(expression);
        name = node.name.name;

        HashMap<String, ExpressionType> currentScope = variableTypes.getLast();
        currentScope.put(name, type);

        variableTypes.add(currentScope);
        System.out.println(currentScope);
    }

    private ExpressionType getExpressionType(Expression expression) {
        if (expression instanceof ColorLiteral) return ExpressionType.COLOR;
        if (expression instanceof PixelLiteral) return ExpressionType.PIXELSIZE;
        if (expression instanceof PercentageLiteral) return ExpressionType.PERCENTAGE;
        if (expression instanceof ScalarLiteral) return ExpressionType.SCALAR;
        if (expression instanceof BoolLiteral) return ExpressionType.BOOL;

        return ExpressionType.UNDEFINED;
    }

    private void checkStylerule(Stylerule rule) {
        for (ASTNode child : rule.getChildren()){
            if (child instanceof Declaration){
                checkDeclaration((Declaration)child);
            }
        }
    }

    private void checkDeclaration(Declaration declaration) {
        Expression expr = declaration.expression;
        ExpressionType type;

        if (expr instanceof VariableReference) {
            String name = ((VariableReference) expr).name;
            type = variableTypes.getLast().getOrDefault(name, UNDEFINED);
        }
        else{
            type = getExpressionType(expr);
        }

        if (declaration.property.name.equals("width") || declaration.property.name.equals("height")) {
            if (type == ExpressionType.COLOR)
                declaration.setError("Property 'width' can't be a color");
            if (type == ExpressionType.SCALAR)
                declaration.setError("Property 'width' must be px or %");
            if (type == ExpressionType.BOOL)
                declaration.setError("Property 'width' can't be boolean");
        }

        if (declaration.property.name.contains("color")) {
            if (type != ExpressionType.COLOR)
                declaration.setError("Property '" + declaration.property.name + "' must be a color");
        }
    }
}

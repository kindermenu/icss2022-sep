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
            if (child instanceof VariableAssignment) {
                checkVariableAssignment((VariableAssignment) child);
            }
            if (child instanceof Stylerule) {
                variableTypes.add(new HashMap<>(variableTypes.getLast()));
                checkStylerule((Stylerule) child);
                variableTypes.removeLast();
            }
        }
    }

    private void checkVariableAssignment(VariableAssignment node) {
        System.out.println("entered variable assignment");
        String name;
        Expression expression = node.expression;

        ExpressionType type = getExpressionType(expression);
        name = node.name.name;

        variableTypes.getLast().put(name, type);
        System.out.println(variableTypes);
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
            if (child instanceof IfClause){
                checkIfClause((IfClause)child);
            }
        }
    }

    private void checkIfClause(IfClause node) {
        Expression expr = node.conditionalExpression;
        ExpressionType type;

        if (expr instanceof VariableReference) {
            String name = ((VariableReference) expr).name;
            type = variableTypes.getLast().get(name);
            if (type == null) {
                node.setError("Variable '" + name + "' is not defined in this scope");
                type = UNDEFINED;
            }
        } else {
            type = getExpressionType(expr);
        }

        if (type != ExpressionType.BOOL) {
            node.setError("Condition clause must contain a boolean");
        }
    }


    private void checkDeclaration(Declaration declaration) {
        Expression expr = declaration.expression;
        ExpressionType type;

        if (expr instanceof VariableReference) {
            String name = ((VariableReference) expr).name;
            type = variableTypes.getLast().get(name);
            if (type == null) {
                declaration.setError("Variable '" + name + "' is not defined in this scope");
                type = UNDEFINED;
            }
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

        checkOperation(declaration.property.)
    }
}

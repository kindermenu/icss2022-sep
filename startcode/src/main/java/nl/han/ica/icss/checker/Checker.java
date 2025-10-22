package nl.han.ica.icss.checker;

import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.literals.*;
import nl.han.ica.icss.ast.types.ExpressionType;

import java.util.HashMap;
import java.util.LinkedList;


public class Checker {

    private LinkedList<HashMap<String, ExpressionType>> variabeTypes;
    public void check(AST ast) {
        // variableTypes = new HANLinkedList<>();
        checkStylesheet(ast.root);
    }

    public void checkStylesheet(Stylesheet sheet){
        for (ASTNode stylerule : sheet.getChildren()) {
            checkStylerule((Stylerule) stylerule);
        }
    }

    private void checkStylerule(Stylerule rule) {
        for (ASTNode child : rule.getChildren()){
            if (child instanceof Declaration){
                checkDeclaration((Declaration)child);
            }
        }
    }

    private void checkDeclaration(Declaration declaration) {
        if (declaration.property.name.equals("width")){
            if (declaration.expression instanceof ColorLiteral){
                declaration.setError("Property 'width' can't be a color");
            }
            if (declaration.expression instanceof ScalarLiteral){
                declaration.setError("Property 'width' must be px or %");
            }
            if (declaration.expression instanceof BoolLiteral){
                declaration.setError("Property 'width' can't be boolean");
            }
        }
        if (declaration.property.name.contains("color")){
            if (declaration.expression instanceof PixelLiteral){
                declaration.setError("Property '" + ((PixelLiteral) declaration.expression).value + "' must be a color");
            }
            if (declaration.expression instanceof ScalarLiteral){
                declaration.setError("Property '" + ((ScalarLiteral) declaration.expression).value + "' must be a color");
            }
            if (declaration.expression instanceof PercentageLiteral){
                declaration.setError("Property '" + ((PercentageLiteral) declaration.expression).value + "' must be a color");
            }
            if (declaration.expression instanceof BoolLiteral){
                declaration.setError("Property '" + ((BoolLiteral) declaration.expression).value + "' must be a color");
            }
        }
    }

}

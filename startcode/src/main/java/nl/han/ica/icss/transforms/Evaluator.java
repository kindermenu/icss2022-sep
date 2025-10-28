package nl.han.ica.icss.transforms;

import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.literals.PixelLiteral;
import nl.han.ica.icss.ast.operations.AddOperation;

import java.util.HashMap;
import java.util.LinkedList;

public class Evaluator implements Transform {

    private LinkedList<HashMap<String, Literal>> variableValues;
    public Evaluator() {
        LinkedList<HashMap<String, Literal>> variableValues = new LinkedList<>();
    }

    @Override
    public void apply(AST ast) {
//        variableValues = new LinkedList<>();
        applyStylesheet(ast.root);
    }

    private void applyStylesheet(Stylesheet node) {
        for (ASTNode child: node.getChildren()){
            if (child instanceof Stylerule){
                applyStylerule((Stylerule)child);
            }
            if (child instanceof VariableAssignment){
                applyVariableAssignment((VariableAssignment)child);
            }
        }
    }

    private void applyStylerule(Stylerule node) {
        for (ASTNode child: node.getChildren()){
            if (child instanceof Declaration) {
                applyDeclaration((Declaration) child);
            }
        }
    }

    private void applyVariableAssignment(VariableAssignment node) {
        System.out.println("entered variable assignment");
        String name;
        Literal value;
        name = node.name.name;
        value = (Literal)node.expression;

        HashMap<String, Literal> currentScope = variableValues.getLast();
        currentScope.put(name, value);

        variableValues.add(currentScope);
        System.out.println(currentScope);
    }

    private void applyDeclaration(Declaration node) {
        node.expression = evaluateExpression(node.expression);
    }

    private Literal evaluateExpression(Expression expression) {
        if (expression instanceof PixelLiteral){
            return (PixelLiteral) expression;
        }
        else {
            return evaluateAddOpperation((AddOperation)expression);
        }
    }

    private PixelLiteral evaluateAddOpperation(AddOperation expression) {
        PixelLiteral left = (PixelLiteral) evaluateExpression(expression.lhs);
        PixelLiteral right = (PixelLiteral) evaluateExpression(expression.rhs);
        return new PixelLiteral(left.value + right.value);
    }


}

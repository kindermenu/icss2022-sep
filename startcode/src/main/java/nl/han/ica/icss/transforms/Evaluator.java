package nl.han.ica.icss.transforms;

import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.literals.*;
import nl.han.ica.icss.ast.operations.*;

import java.util.HashMap;
import java.util.LinkedList;

public class Evaluator implements Transform {

    private LinkedList<HashMap<String, Literal>> variableValues;

    public Evaluator() {
        variableValues = new LinkedList<>();
    }

    @Override
    public void apply(AST ast) {
        variableValues.add(new HashMap<>());
        applyStylesheet(ast.root);
    }

    private void applyStylesheet(Stylesheet node) {
        for (ASTNode child : node.getChildren()) {
            if (child instanceof VariableAssignment) {
                applyVariableAssignment((VariableAssignment) child);
            }
            if (child instanceof Stylerule) {
                variableValues.add(new HashMap<>(variableValues.getLast()));
                applyStylerule((Stylerule) child);
                variableValues.removeLast();
            }
        }
    }

    private void applyVariableAssignment(VariableAssignment node) {
        // var values in hashmap zetten
        Literal value = evaluateExpression(node.expression);
        variableValues.getLast().put(node.name.name, value);
    }

    private void applyStylerule(Stylerule node) {
        variableValues.add(new HashMap<>(variableValues.getLast()));
        for (ASTNode child : node.getChildren()) {
            if (child instanceof VariableAssignment) {
                applyVariableAssignment((VariableAssignment) child);
            }
            if (child instanceof Declaration) {
                applyDeclaration((Declaration) child);
            }
            if (child instanceof IfClause) {
                applyIfClause((IfClause) child);
            }
        }
        variableValues.removeLast();
    }

    private void applyIfClause(IfClause node) {

        node.conditionalExpression = evaluateExpression(node.conditionalExpression);

        BoolLiteral condition = (BoolLiteral) node.conditionalExpression;

        if (condition.value) {
            for (ASTNode child : node.body) {
                if (child instanceof Declaration)
                    applyDeclaration((Declaration) child);
                if (child instanceof IfClause)
                    applyIfClause((IfClause) child);
            }
        } else {
            if (node.elseClause != null) {
                for (ASTNode child : node.elseClause.body) {
                    if (child instanceof Declaration)
                        applyDeclaration((Declaration) child);
                    if (child instanceof IfClause)
                        applyIfClause((IfClause) child);
                }
            }
        }
    }

    private void applyDeclaration(Declaration node) {
        node.expression = evaluateExpression(node.expression);
    }

    private Literal evaluateExpression(Expression expr) {
        if (expr instanceof VariableReference) {
            // door de opgeslagen variablen heenlopen
            String name = ((VariableReference) expr).name;
            for (int i = variableValues.size() - 1; i >= 0; i--) { // achteraan begin zodat je de meest recente value pakt
                HashMap<String, Literal> scope = variableValues.get(i);
                // returnen als de juiste key gevonden is
                if (scope.containsKey(name))
                    return scope.get(name);
            }
        }

        if (expr instanceof Literal) {
            return (Literal) expr;
        }

        if (expr instanceof Operation)
            return evaluateOperation((Operation) expr);

        return new ScalarLiteral(0);
    }

    private Literal evaluateOperation(Operation operation) {
        // operations afhandelen
        Literal left = evaluateExpression(operation.lhs);
        Literal right = evaluateExpression(operation.rhs);

        if (operation instanceof AddOperation) {
            return handleAddSubtract(left, right, true);
        }

        if (operation instanceof SubtractOperation) {
            return handleAddSubtract(left, right, false);
        }

        if (operation instanceof MultiplyOperation) {
            return handleMultiply(left, right);
        }

        return new ScalarLiteral(0);
    }

    private Literal handleAddSubtract(Literal left, Literal right, boolean isAdd) {
        int positive = isAdd ? 1 : -1;

        if (left instanceof PixelLiteral && right instanceof PixelLiteral) {
            return new PixelLiteral(((PixelLiteral) left).value + positive * ((PixelLiteral) right).value);
        }
        if (left instanceof PercentageLiteral && right instanceof PercentageLiteral) {
            return new PercentageLiteral(((PercentageLiteral) left).value + positive * ((PercentageLiteral) right).value);
        }
        if (left instanceof PixelLiteral && right instanceof ScalarLiteral) {
            return new PixelLiteral(((PixelLiteral) left).value + positive * ((ScalarLiteral) right).value);
        }
        if (left instanceof ScalarLiteral && right instanceof PixelLiteral) {
            return new PixelLiteral(((ScalarLiteral) left).value + positive * ((PixelLiteral) right).value);
        }
        if (left instanceof PercentageLiteral && right instanceof ScalarLiteral) {
            return new PercentageLiteral(((PercentageLiteral) left).value + positive * ((ScalarLiteral) right).value);
        }
        if (left instanceof ScalarLiteral && right instanceof PercentageLiteral) {
            return new PercentageLiteral(((ScalarLiteral) left).value + positive * ((PercentageLiteral) right).value);
        }

        return new PixelLiteral(0);
    }

    private Literal handleMultiply(Literal left, Literal right) {
        if (left instanceof ScalarLiteral && right instanceof ScalarLiteral) {
            return new ScalarLiteral(((ScalarLiteral) left).value * ((ScalarLiteral) right).value);
        }
        if (left instanceof ScalarLiteral && right instanceof PixelLiteral) {
            return new PixelLiteral(((ScalarLiteral) left).value * ((PixelLiteral) right).value);
        }
        if (left instanceof PixelLiteral && right instanceof ScalarLiteral) {
            return new PixelLiteral(((PixelLiteral) left).value * ((ScalarLiteral) right).value);
        }
        if (left instanceof ScalarLiteral && right instanceof PercentageLiteral) {
            return new PercentageLiteral(((ScalarLiteral) left).value * ((PercentageLiteral) right).value);
        }
        if (left instanceof PercentageLiteral && right instanceof ScalarLiteral) {
            return new PercentageLiteral(((PercentageLiteral) left).value * ((ScalarLiteral) right).value);
        }

        return new ScalarLiteral(0);
    }
}

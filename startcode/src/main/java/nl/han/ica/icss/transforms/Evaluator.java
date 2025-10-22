package nl.han.ica.icss.transforms;

import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.literals.PixelLiteral;
import nl.han.ica.icss.ast.operations.AddOperation;

import java.util.LinkedList;

public class Evaluator implements Transform {


    public Evaluator() {
        LinkedList variableValues = new LinkedList<>();
    }

    @Override
    public void apply(AST ast) {
        //variableValues = new HANLinkedList<>();
        applyStylesheet(ast.root);
    }

    private void applyStylesheet(Stylesheet node) {
       applyStylerule((Stylerule)node.getChildren().get(0));
    }

    private void applyStylerule(Stylerule node) {
        for (ASTNode child: node.getChildren()){
            if (child instanceof Declaration){
                applyDeclaration((Declaration)child);
            }
        }
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

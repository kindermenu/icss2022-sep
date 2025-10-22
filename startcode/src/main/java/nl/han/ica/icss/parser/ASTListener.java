package nl.han.ica.icss.parser;

import java.util.Stack;


import nl.han.ica.datastructures.HANStack;
import nl.han.ica.datastructures.IHANStack;
import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.literals.*;
import nl.han.ica.icss.ast.operations.AddOperation;
import nl.han.ica.icss.ast.operations.MultiplyOperation;
import nl.han.ica.icss.ast.operations.SubtractOperation;
import nl.han.ica.icss.ast.selectors.ClassSelector;
import nl.han.ica.icss.ast.selectors.IdSelector;
import nl.han.ica.icss.ast.selectors.TagSelector;

/**
 * This class extracts the ICSS Abstract Syntax Tree from the Antlr Parse tree.
 */
public class ASTListener extends ICSSBaseListener {
	
	//Accumulator attributes:
	private AST ast;

	//Use this to keep track of the parent nodes when recursively traversing the ast
	private IHANStack<ASTNode> currentContainer;

	public ASTListener() {
		ast = new AST();
		currentContainer = new HANStack<>();
	}
    public AST getAST() {
        return ast;
    }

	@Override
	public void enterStylesheet(ICSSParser.StylesheetContext ctx) {
		Stylesheet stylesheet = new Stylesheet();

		currentContainer.push(stylesheet);
	}

	@Override
	public void exitStylesheet(ICSSParser.StylesheetContext ctx) {
		Stylesheet stylesheet = (Stylesheet) currentContainer.pop();
		ast.setRoot(stylesheet);
	}

	@Override
	public void enterStylerule(ICSSParser.StyleruleContext ctx) {
		Stylerule stylerule = new Stylerule();

		currentContainer.push(stylerule);
	}

	@Override
	public void exitStylerule(ICSSParser.StyleruleContext ctx) {
		Stylerule stylerule = (Stylerule) currentContainer.pop();
		currentContainer.peek().addChild(stylerule);
	}


	@Override
	public void enterId_selector(ICSSParser.Id_selectorContext ctx) {
		IdSelector idSelector = new IdSelector(ctx.getText());
		currentContainer.push(idSelector);
	}

	@Override
	public void exitId_selector(ICSSParser.Id_selectorContext ctx) {
		IdSelector idSelector = (IdSelector) currentContainer.pop();
		currentContainer.peek().addChild(idSelector);
	}

	@Override
	public void enterClass_selector(ICSSParser.Class_selectorContext ctx) {
		ClassSelector classSelector = new ClassSelector(ctx.getText());
		currentContainer.push(classSelector);
	}

	@Override
	public void exitClass_selector(ICSSParser.Class_selectorContext ctx) {
		ClassSelector classSelector = (ClassSelector) currentContainer.pop();
		currentContainer.peek().addChild(classSelector);
	}

	@Override
	public void enterTag_selector(ICSSParser.Tag_selectorContext ctx) {
		TagSelector tagSelector = new TagSelector(ctx.getText());
		currentContainer.push(tagSelector);
	}

	@Override
	public void exitTag_selector(ICSSParser.Tag_selectorContext ctx) {
		TagSelector tagSelector = (TagSelector) currentContainer.pop();
		currentContainer.peek().addChild(tagSelector);
	}

	@Override
	public void enterDeclaration(ICSSParser.DeclarationContext ctx) {
		Declaration declaration = new Declaration(ctx.getText());
		currentContainer.push(declaration);
	}

	@Override
	public void exitDeclaration(ICSSParser.DeclarationContext ctx) {
		Declaration declaration = (Declaration) currentContainer.pop();
		currentContainer.peek().addChild(declaration);
	}

	@Override
	public void enterProperty(ICSSParser.PropertyContext ctx) {
		PropertyName property = new PropertyName(ctx.getText());
		currentContainer.push(property);
	}

	@Override
	public void exitProperty(ICSSParser.PropertyContext ctx) {
		PropertyName property = (PropertyName) currentContainer.pop();
		currentContainer.peek().addChild(property);
	}

	// LITERALS
	@Override
	public void enterPixel_literal(ICSSParser.Pixel_literalContext ctx) {
		PixelLiteral value = new PixelLiteral(ctx.getText());
		currentContainer.push(value);
	}

	@Override
	public void exitPixel_literal(ICSSParser.Pixel_literalContext ctx) {
		PixelLiteral value = (PixelLiteral) currentContainer.pop();
		currentContainer.peek().addChild(value);
	}

	@Override
	public void enterBool(ICSSParser.BoolContext ctx) {
		BoolLiteral value = new BoolLiteral(ctx.getText());
		currentContainer.push(value);
	}

	@Override
	public void exitBool(ICSSParser.BoolContext ctx) {
		BoolLiteral value = (BoolLiteral) currentContainer.pop();
		currentContainer.peek().addChild(value);
	}

	@Override
	public void enterPercentage_literal(ICSSParser.Percentage_literalContext ctx) {
		PercentageLiteral value = new PercentageLiteral(ctx.getText());
		currentContainer.push(value);
	}

	@Override
	public void exitPercentage_literal(ICSSParser.Percentage_literalContext ctx) {
		PercentageLiteral value = (PercentageLiteral) currentContainer.pop();
		currentContainer.peek().addChild(value);
	}

	@Override
	public void enterScalar_literal(ICSSParser.Scalar_literalContext ctx) {
		ScalarLiteral value = new ScalarLiteral(ctx.getText());
		currentContainer.push(value);
	}

	@Override
	public void exitScalar_literal(ICSSParser.Scalar_literalContext ctx) {
		ScalarLiteral value = (ScalarLiteral) currentContainer.pop();
		currentContainer.peek().addChild(value);
	}

	@Override
	public void enterColor_literal(ICSSParser.Color_literalContext ctx) {
		ColorLiteral value = new ColorLiteral(ctx.getText());
		currentContainer.push(value);
	}

	@Override
	public void exitAdd_operation(ICSSParser.Add_operationContext ctx) {
		AddOperation value = (AddOperation) currentContainer.pop();
		currentContainer.peek().addChild(value);
	}

	@Override
	public void enterAdd_operation(ICSSParser.Add_operationContext ctx) {
		AddOperation value = new AddOperation();
		currentContainer.push(value);
	}

	@Override
	public void exitSubtract_operation(ICSSParser.Subtract_operationContext ctx) {
		SubtractOperation value = (SubtractOperation) currentContainer.pop();
		currentContainer.peek().addChild(value);
	}

	@Override
	public void enterSubtract_operation(ICSSParser.Subtract_operationContext ctx) {
		SubtractOperation value = new SubtractOperation();
		currentContainer.push(value);
	}

	@Override
	public void exitMultiply_operation(ICSSParser.Multiply_operationContext ctx) {
		MultiplyOperation value = (MultiplyOperation) currentContainer.pop();
		currentContainer.peek().addChild(value);
	}

	@Override
	public void enterMultiply_operation(ICSSParser.Multiply_operationContext ctx) {
		MultiplyOperation value = new MultiplyOperation();
		currentContainer.push(value);
	}

	@Override
	public void exitColor_literal(ICSSParser.Color_literalContext ctx) {
		ColorLiteral value = (ColorLiteral) currentContainer.pop();
		currentContainer.peek().addChild(value);
	}

	@Override
	public void enterElse_clause(ICSSParser.Else_clauseContext ctx) {
		ElseClause value = new ElseClause();
		currentContainer.push(value);
	}

	@Override
	public void exitElse_clause(ICSSParser.Else_clauseContext ctx) {
		ElseClause value = (ElseClause) currentContainer.pop();
		currentContainer.peek().addChild(value);
	}

	@Override
	public void enterIf_clause(ICSSParser.If_clauseContext ctx) {
		IfClause value = new IfClause();
		currentContainer.push(value);
	}

	@Override
	public void exitIf_clause(ICSSParser.If_clauseContext ctx) {
		IfClause value = (IfClause) currentContainer.pop();
		currentContainer.peek().addChild(value);
	}

	@Override
	public void enterVariable_assignment(ICSSParser.Variable_assignmentContext ctx) {
		VariableAssignment value = new VariableAssignment();
		currentContainer.push(value);
	}

	@Override
	public void exitVariable_assignment(ICSSParser.Variable_assignmentContext ctx) {
		VariableAssignment value = (VariableAssignment) currentContainer.pop();
		currentContainer.peek().addChild(value);
	}

	@Override
	public void enterVariable_reference(ICSSParser.Variable_referenceContext ctx) {
		VariableReference value = new VariableReference(ctx.getText());
		currentContainer.push(value);
	}

	@Override
	public void exitVariable_reference(ICSSParser.Variable_referenceContext ctx) {
		VariableReference value = (VariableReference) currentContainer.pop();
		currentContainer.peek().addChild(value);
	}

}
package nl.han.ica.icss.generator;

import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.literals.*;

public class Generator {

	public String generate(AST ast) {
		return generateStylesheet(ast.root);
	}

	private String generateStylesheet(Stylesheet node) {
		StringBuilder css = new StringBuilder();
		for (ASTNode child : node.getChildren()) {
			if (child instanceof Stylerule) {
				css.append(generateStylerule((Stylerule) child)).append("\n");
			}
		}
		return css.toString();
	}

	private String generateStylerule(Stylerule node) {
		StringBuilder css = new StringBuilder();
		css.append(node.selectors.get(0).toString()).append(" {\n");

		for (ASTNode child : node.body) {
			if (child instanceof Declaration) {
				css.append("    ").append(generateDeclaration((Declaration) child));
			} else if (child instanceof IfClause) {
				css.append(generateIfClause((IfClause) child));
			}
		}

		css.append("}\n");
		return css.toString();
	}

	private String generateIfClause(IfClause ifClause) {
		StringBuilder result = new StringBuilder();

		if (!(ifClause.conditionalExpression instanceof BoolLiteral)) {
			return "";
		}

		BoolLiteral condition = (BoolLiteral) ifClause.conditionalExpression;

		if (condition.value) {
			for (ASTNode node : ifClause.body) {
				if (node instanceof Declaration) {
					result.append("    ").append(generateDeclaration((Declaration) node));
				} else if (node instanceof IfClause) {
					result.append(generateIfClause((IfClause) node));
				}
			}
		} else if (ifClause.elseClause != null) {
			result.append(generateElseClause(ifClause.elseClause));
		}

		return result.toString();
	}

	private String generateElseClause(ElseClause elseClause) {
		StringBuilder result = new StringBuilder();

		for (ASTNode node : elseClause.body) {
			if (node instanceof Declaration) {
				result.append("    ").append(generateDeclaration((Declaration) node));
			} else if (node instanceof IfClause) {
				result.append(generateIfClause((IfClause) node));
			}
		}

		return result.toString();
	}

	private String generateDeclaration(Declaration declaration) {
		StringBuilder declarationString = new StringBuilder();
		declarationString.append(declaration.property.name).append(": ");

		Expression expr = declaration.expression;

		if (expr instanceof PixelLiteral) {
			declarationString.append(((PixelLiteral) expr).value).append("px");
		} else if (expr instanceof PercentageLiteral) {
			declarationString.append(((PercentageLiteral) expr).value).append("%");
		} else if (expr instanceof ScalarLiteral) {
			declarationString.append(((ScalarLiteral) expr).value);
		} else if (expr instanceof ColorLiteral) {
			declarationString.append(((ColorLiteral) expr).value);
		} else if (expr instanceof BoolLiteral) {
			BoolLiteral bool = (BoolLiteral) expr;
			declarationString.append(bool.value ? "true" : "false");
		}

		declarationString.append(";\n");
		return declarationString.toString();
	}
}

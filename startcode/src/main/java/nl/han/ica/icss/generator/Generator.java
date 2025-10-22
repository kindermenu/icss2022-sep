package nl.han.ica.icss.generator;


import nl.han.ica.icss.ast.AST;
import nl.han.ica.icss.ast.Declaration;
import nl.han.ica.icss.ast.Stylerule;
import nl.han.ica.icss.ast.Stylesheet;
import nl.han.ica.icss.ast.literals.*;

public class Generator {

	public String generate(AST ast) {
		return generateStylesheet(ast.root);
	}

	private String generateStylesheet(Stylesheet node) {
		return generateStylerule((Stylerule) node.getChildren().get(0));
	}

	private String generateStylerule(Stylerule node) {
		return node.selectors.get(0).toString() + " {\n"
				+ generateDeclaration((Declaration) node.body.get(0)) + "}";
	}

	private String generateDeclaration(Declaration declaration) {
		StringBuilder declarationString = new StringBuilder();

		if (declaration.expression instanceof PixelLiteral) {
			declarationString.append(((PixelLiteral) declaration.expression).value).append("px");
		} else if (declaration.expression instanceof PercentageLiteral) {
			declarationString.append(((PercentageLiteral) declaration.expression).value).append("%");
		} else if (declaration.expression instanceof ScalarLiteral) {
			declarationString.append(((ScalarLiteral) declaration.expression).value);
		} else if (declaration.expression instanceof ColorLiteral) {
			declarationString.append(((ColorLiteral) declaration.expression).value);
		} else {
			BoolLiteral bool = (BoolLiteral) declaration.expression;
			declarationString.append(bool.value ? "true" : "false");
		}

		return declarationString.toString() + ";\n";
	}
}

package org.rebecalang.compiler.modelcompiler.corerebeca.statementsemanticchecker.expression;

import org.rebecalang.compiler.modelcompiler.AbstractExpressionSemanticCheck;
import org.rebecalang.compiler.modelcompiler.ExpressionSemanticCheckContainer;
import org.rebecalang.compiler.modelcompiler.SemanticCheckerUtils;
import org.rebecalang.compiler.modelcompiler.corerebeca.objectmodel.Expression;
import org.rebecalang.compiler.modelcompiler.corerebeca.objectmodel.Type;
import org.rebecalang.compiler.modelcompiler.corerebeca.objectmodel.UnaryExpression;
import org.rebecalang.compiler.modelcompiler.corerebeca.statementsemanticchecker.expression.BinaryExpressionSemanticCheck.LValueState;
import org.rebecalang.compiler.utils.CodeCompilationException;
import org.rebecalang.compiler.utils.Pair;
import org.rebecalang.compiler.utils.TypesUtilities;

public class UnaryExpressionSemanticCheck extends AbstractExpressionSemanticCheck {

	@Override
	public Pair<Type, Object> check(Expression expression, Type baseType) {
		Pair<Type, Object> returnValue = new Pair<Type, Object>();
		
		UnaryExpression uExpression = (UnaryExpression) expression;
		Pair<Type, Object> type = 
				((ExpressionSemanticCheckContainer)defaultContainer).check(uExpression.getExpression());
		String operator = uExpression.getOperator();
		if (operator.equals("++") || operator.equals("--")) {
			if (!TypesUtilities.getInstance().canTypeUpCastTo(
					type.getFirst(), TypesUtilities.INT_TYPE)) {
				TypesUtilities.addTypeMismatchException(exceptionContainer, type.getFirst(),
						TypesUtilities.INT_TYPE, uExpression);
			}
			if (BinaryExpressionSemanticCheck.isInLValueStyle(uExpression.getExpression(), scopeHandler) != LValueState.VARIABLE) {
				exceptionContainer.getExceptions().add(
						new CodeCompilationException(
								"Invalid argument to operation ++/--",
								uExpression.getLineNumber(), uExpression
										.getCharacter()));
			}
		} else if (operator.equals("-")) {
			if (!TypesUtilities.getInstance().canTypeUpCastTo(
					type.getFirst(), TypesUtilities.DOUBLE_TYPE)) {
				TypesUtilities.addTypeMismatchException(exceptionContainer, type.getFirst(),
						TypesUtilities.INT_TYPE, uExpression);
			} else {
				if (type.getSecond() != null)
					returnValue.setSecond(SemanticCheckerUtils.evaluateConstantTerm("-", type
							.getFirst(), 0, type.getSecond()));
			}
		} else if (operator.equals("!")) {
			if (!TypesUtilities.getInstance().canTypeUpCastTo(
					type.getFirst(), TypesUtilities.BOOLEAN_TYPE)) {
				TypesUtilities.addTypeMismatchException(exceptionContainer, type.getFirst(),
						TypesUtilities.BOOLEAN_TYPE, uExpression);
			} else {
				if (type.getSecond() != null)
					returnValue.setSecond(SemanticCheckerUtils.evaluateConstantTerm("!", type
							.getFirst(), type.getSecond(), null));
			}
		}
		uExpression.setType(type.getFirst());
		returnValue.setFirst(uExpression.getType());
		return returnValue;
	}

}

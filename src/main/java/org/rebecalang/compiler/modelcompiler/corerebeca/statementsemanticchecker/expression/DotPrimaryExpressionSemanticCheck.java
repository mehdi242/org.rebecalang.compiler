package org.rebecalang.compiler.modelcompiler.corerebeca.statementsemanticchecker.expression;

import org.rebecalang.compiler.modelcompiler.AbstractExpressionSemanticCheck;
import org.rebecalang.compiler.modelcompiler.ExpressionSemanticCheckContainer;
import org.rebecalang.compiler.modelcompiler.corerebeca.CoreRebecaLabelUtility;
import org.rebecalang.compiler.modelcompiler.corerebeca.objectmodel.DotPrimary;
import org.rebecalang.compiler.modelcompiler.corerebeca.objectmodel.Expression;
import org.rebecalang.compiler.modelcompiler.corerebeca.objectmodel.PrimaryExpression;
import org.rebecalang.compiler.modelcompiler.corerebeca.objectmodel.TermPrimary;
import org.rebecalang.compiler.modelcompiler.corerebeca.objectmodel.Type;
import org.rebecalang.compiler.utils.CodeCompilationException;
import org.rebecalang.compiler.utils.Pair;
import org.rebecalang.compiler.utils.TypesUtilities;

public class DotPrimaryExpressionSemanticCheck extends AbstractExpressionSemanticCheck {

	@Override
	public Pair<Type, Object> check(Expression expression, Type baseType) {
		Pair<Type, Object> returnValue = new Pair<Type, Object>();
		DotPrimary dotPrimary = (DotPrimary) expression;

		Expression leftTerm = dotPrimary.getLeft();
		Type leftTermType = ((ExpressionSemanticCheckContainer)defaultContainer).check(leftTerm, baseType).getFirst();
		//TODO: make sure why I put such a setter here
//		leftTerm.setType(leftTermType);
		PrimaryExpression rightTerm = dotPrimary.getRight();
		
		Type rightTermType = ((ExpressionSemanticCheckContainer)defaultContainer).check(rightTerm, leftTermType).getFirst();
		expression.setType(rightTermType);
		
		if (TypesUtilities.getInstance().canTypeCastTo(leftTermType, 
				TypesUtilities.REACTIVE_CLASS_TYPE)
				&& scopeHandler.isInScopeOf(CoreRebecaLabelUtility.CONSTRUCTOR)) {
			if (!(leftTerm instanceof TermPrimary)) {
				exceptionContainer.addException(new CodeCompilationException(
						"Direct sending to \"self\" is allowed in constructors",
					leftTerm.getLineNumber(), leftTerm.getCharacter()));
			} else if (!((TermPrimary)leftTerm).getName().equals("self")) {
				exceptionContainer.addException(new CodeCompilationException(
						"Direct sending to \"self\" is allowed in constructors",
					leftTerm.getLineNumber(), leftTerm.getCharacter()));
			}
			
		}

		returnValue.setFirst(expression.getType());
		return returnValue;
	}

}

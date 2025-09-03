package org.rebecalang.compiler.modelcompiler.timedrebeca.statementsemanticchecker.expression;

import org.rebecalang.compiler.modelcompiler.ExpressionSemanticCheckContainer;
import org.rebecalang.compiler.modelcompiler.abstractrebeca.AbstractExpressionSemanticCheck;
import org.rebecalang.compiler.modelcompiler.corerebeca.CoreRebecaTypeSystem;
import org.rebecalang.compiler.modelcompiler.corerebeca.objectmodel.Expression;
import org.rebecalang.compiler.modelcompiler.corerebeca.objectmodel.Type;
import org.rebecalang.compiler.modelcompiler.timedrebeca.objectmodel.DelayExpression;
import org.rebecalang.compiler.utils.CodeCompilationException;
import org.rebecalang.compiler.utils.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class DelayExpressionSemanticCheck extends AbstractExpressionSemanticCheck {
    ExpressionSemanticCheckContainer expressionSemanticCheckContainer;

    @Autowired
    public DelayExpressionSemanticCheck(ExpressionSemanticCheckContainer expressionSemanticCheckContainer) {
        this.expressionSemanticCheckContainer = expressionSemanticCheckContainer;
    }

    @Override
    public Pair<Type, Object> check(Expression expression, Type baseType) {
        Pair<Type, Object> returnValue = new Pair<>(CoreRebecaTypeSystem.VOID_TYPE,
                AbstractExpressionSemanticCheck.NO_VALUE);
        DelayExpression delayExpression = (DelayExpression) expression;
        //TODO: Do we need to check sender and receiver names are exists in knownNodes?
        Pair<Type, Object> amountType = expressionSemanticCheckContainer.check(delayExpression.getAmount());
        if (!amountType.getFirst().canTypeUpCastTo(CoreRebecaTypeSystem.INT_TYPE)) {
            exceptionContainer.addException(new CodeCompilationException(
                    "Only convertible int values are permitted here",
                    delayExpression.getAmount().getLineNumber(),
                    delayExpression.getAmount().getCharacter()
            ));
            return returnValue;
        }
        if (Integer.parseInt(amountType.getSecond().toString()) <= 0) {
            exceptionContainer.addException(new CodeCompilationException(
                    "Delay amount cannot be less than 0",
                    delayExpression.getAmount().getLineNumber(),
                    delayExpression.getAmount().getCharacter()
            ));
        }
        delayExpression.setType(CoreRebecaTypeSystem.VOID_TYPE);
        return returnValue;
    }
}

package org.rebecalang.compiler.modelcompiler.timedrebeca.statementsemanticchecker.expression;

import org.rebecalang.compiler.modelcompiler.ExpressionSemanticCheckContainer;
import org.rebecalang.compiler.modelcompiler.abstractrebeca.AbstractExpressionSemanticCheck;
import org.rebecalang.compiler.modelcompiler.corerebeca.CoreRebecaTypeSystem;
import org.rebecalang.compiler.modelcompiler.corerebeca.objectmodel.Expression;
import org.rebecalang.compiler.modelcompiler.corerebeca.objectmodel.Type;
import org.rebecalang.compiler.modelcompiler.timedrebeca.objectmodel.LossExpression;
import org.rebecalang.compiler.utils.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class LossExpressionSemanticCheck extends AbstractExpressionSemanticCheck {
    ExpressionSemanticCheckContainer expressionSemanticCheckContainer;

    @Autowired
    public LossExpressionSemanticCheck(ExpressionSemanticCheckContainer expressionSemanticCheckContainer) {
        this.expressionSemanticCheckContainer = expressionSemanticCheckContainer;
    }

    @Override
    public Pair<Type, Object> check(Expression expression, Type baseType) {
        Pair<Type, Object> returnValue = new Pair<>(CoreRebecaTypeSystem.VOID_TYPE,
                AbstractExpressionSemanticCheck.NO_VALUE);
        //TODO: Do we need to check sender and receiver names are exists in knownNodes?
        LossExpression lossExpression = (LossExpression) expression;
        lossExpression.setType(CoreRebecaTypeSystem.VOID_TYPE);
        return returnValue;
    }
}

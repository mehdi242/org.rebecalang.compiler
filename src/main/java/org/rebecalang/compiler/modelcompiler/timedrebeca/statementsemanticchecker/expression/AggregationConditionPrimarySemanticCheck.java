package org.rebecalang.compiler.modelcompiler.timedrebeca.statementsemanticchecker.expression;

import org.rebecalang.compiler.modelcompiler.ExpressionSemanticCheckContainer;
import org.rebecalang.compiler.modelcompiler.abstractrebeca.AbstractExpressionSemanticCheck;
import org.rebecalang.compiler.modelcompiler.abstractrebeca.AbstractTypeSystem;
import org.rebecalang.compiler.modelcompiler.corerebeca.CoreRebecaTypeSystem;
import org.rebecalang.compiler.modelcompiler.corerebeca.objectmodel.Expression;
import org.rebecalang.compiler.modelcompiler.corerebeca.objectmodel.Type;
import org.rebecalang.compiler.modelcompiler.corerebeca.statementsemanticchecker.expression.PrimaryTermExpressionSemanticCheck;
import org.rebecalang.compiler.modelcompiler.timedrebeca.TimedRebecaLabelUtility;
import org.rebecalang.compiler.modelcompiler.timedrebeca.objectmodel.AggregationConditionPrimary;
import org.rebecalang.compiler.utils.CodeCompilationException;
import org.rebecalang.compiler.utils.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AggregationConditionPrimarySemanticCheck extends PrimaryTermExpressionSemanticCheck {
    @Autowired
    public AggregationConditionPrimarySemanticCheck(@Qualifier("TIMED_REBECA") AbstractTypeSystem typeSystem,
                                                    ExpressionSemanticCheckContainer expressionSemanticCheckContainer) {
        super(typeSystem, expressionSemanticCheckContainer);
    }

    public Pair<Type, Object> check(Expression expression, Type baseType) {
        Pair<Type, Object> returnValue = new Pair<Type, Object>(
                AbstractTypeSystem.UNKNOWN_TYPE, AbstractExpressionSemanticCheck.NO_VALUE);
        AggregationConditionPrimary aggCondPrimary = (AggregationConditionPrimary) expression;
        semanticCheckArgument(aggCondPrimary.getArgument(), baseType);
        aggCondPrimary.setLabel(TimedRebecaLabelUtility.AGGREGATION_ORDERSPEC);
        aggCondPrimary.setType(CoreRebecaTypeSystem.BOOLEAN_TYPE);
        //TODO: return type should be boolean
        return new Pair<Type, Object>(
                CoreRebecaTypeSystem.BOOLEAN_TYPE, AbstractExpressionSemanticCheck.NO_VALUE);
    }

    private void semanticCheckArgument(Expression expression, Type baseType) {
        if (expression != null) {
            //TODO: What should I check !? Maybe checking whether argument type is INT?
            Pair<Type, Object> retValue = super.check(expression, baseType);
        } else {
            exceptionContainer.addException(new CodeCompilationException(
                    "A numerical message property should be used with min() or max()",
                    expression.getLineNumber(),
                    expression.getCharacter()));
        }
    }
}

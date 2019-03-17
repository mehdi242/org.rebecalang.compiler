package org.rebecalang.compiler.modelcompiler.corerebeca.statementsemanticchecker.expression;

import java.util.LinkedList;
import java.util.List;

import org.rebecalang.compiler.modelcompiler.AbstractExpressionSemanticCheck;
import org.rebecalang.compiler.modelcompiler.ExpressionSemanticCheckContainer;
import org.rebecalang.compiler.modelcompiler.ScopeHandler.VariableInScopeSpecifier;
import org.rebecalang.compiler.modelcompiler.SymbolTable;
import org.rebecalang.compiler.modelcompiler.SymbolTable.MethodInSymbolTableSpecifier;
import org.rebecalang.compiler.modelcompiler.SymbolTableException;
import org.rebecalang.compiler.modelcompiler.corerebeca.CoreRebecaCompilerFacade;
import org.rebecalang.compiler.modelcompiler.corerebeca.CoreRebecaLabelUtility;
import org.rebecalang.compiler.modelcompiler.corerebeca.objectmodel.AccessModifier;
import org.rebecalang.compiler.modelcompiler.corerebeca.objectmodel.ArrayType;
import org.rebecalang.compiler.modelcompiler.corerebeca.objectmodel.Expression;
import org.rebecalang.compiler.modelcompiler.corerebeca.objectmodel.GenericTypeInstance;
import org.rebecalang.compiler.modelcompiler.corerebeca.objectmodel.OrdinaryPrimitiveType;
import org.rebecalang.compiler.modelcompiler.corerebeca.objectmodel.ReactiveClassDeclaration;
import org.rebecalang.compiler.modelcompiler.corerebeca.objectmodel.TermPrimary;
import org.rebecalang.compiler.modelcompiler.corerebeca.objectmodel.Type;
import org.rebecalang.compiler.utils.AccessControlException;
import org.rebecalang.compiler.utils.AccessModifierUtilities;
import org.rebecalang.compiler.utils.CodeCompilationException;
import org.rebecalang.compiler.utils.Pair;
import org.rebecalang.compiler.utils.TypesUtilities;

public class PrimaryTermExpressionSemanticCheck extends AbstractExpressionSemanticCheck {

	@Override
	public Pair<Type, Object> check(Expression expression, Type baseType) {
		Pair<Type, Object> returnValue = new Pair<Type, Object>(TypesUtilities.UNKNOWN_TYPE, null);
		TermPrimary termPrimary = (TermPrimary) expression;
		String termName = termPrimary.getName();
		try {
			if (termPrimary.getParentSuffixPrimary() == null) {
				// The term specifies access to a variable
				if (baseType == TypesUtilities.NO_TYPE) {

					VariableInScopeSpecifier variableInScopeSpecifier = scopeHandler
							.retreiveVariableFromScope(termName);
					termPrimary.setLabel(variableInScopeSpecifier.getLabel());
					returnValue.setFirst(variableInScopeSpecifier.getType());
					termPrimary.setType(variableInScopeSpecifier.getType());
					Object value = variableInScopeSpecifier.getPrecompilationValue();
					returnValue.setSecond(value);
				} else {
					Type symbolType = symbolTable.getSymbolType(baseType, termName);
					if (symbolType == null) {
						if (baseType != TypesUtilities.UNKNOWN_TYPE)
							exceptionContainer.getExceptions()
							.add(new CodeCompilationException(
									"Undefiend variable " + termName + " in the type "
											+ TypesUtilities.getTypeName(baseType),
											termPrimary.getLineNumber(), termPrimary.getCharacter()));
					} else {
						VariableInScopeSpecifier owner = scopeHandler
								.retreiveVariableFromScope(CoreRebecaCompilerFacade.OWNER_REACTIVE_CLASS_KEY);
						AccessModifier accessModifier = symbolTable.getSymbolAccessModifier(baseType, termName);
						returnValue.setFirst(symbolType);
						termPrimary.setType(symbolType);
						if (accessModifier != AccessModifierUtilities.PUBLIC) {
							if (baseType != owner.getType())
								exceptionContainer.getExceptions()
								.add(new AccessControlException(
										"Invalid access to the variable " + termName + " of the type "
												+ TypesUtilities.getTypeName(baseType),
												termPrimary.getLineNumber(), termPrimary.getCharacter()));
						}
					}
				}
			} else {
				// Term specifies method call (synch method or message server)
				boolean hasUnknownType = false;
				List<Type> argumentTypes = new LinkedList<Type>();
				for (Expression expr : termPrimary.getParentSuffixPrimary().getArguments()) {
					Type argumentType = ((ExpressionSemanticCheckContainer) defaultContainer).check(expr).getFirst();
					argumentTypes.add(argumentType);
					if (argumentType == TypesUtilities.UNKNOWN_TYPE)
						hasUnknownType = true;
				}
				if (hasUnknownType)
					return returnValue;
				///
				MethodInSymbolTableSpecifier methodInSymbolTableSpecifier = null;
				if (baseType == TypesUtilities.NO_TYPE) {
					Type selfType = scopeHandler.retreiveVariableFromScope("self").getType();
					try {
						// methodInSymbolTableSpecifier = symbolTable.getMethodSpecification(selfType,
						// termName, argumentTypes);
						methodInSymbolTableSpecifier = checkMethodInParents(selfType, termName, argumentTypes);

					} catch (SymbolTableException se) {
					
						methodInSymbolTableSpecifier = 
								symbolTable.getCastableMethodSpecification(TypesUtilities.NO_TYPE, termName, argumentTypes);
					}
				} else {
						methodInSymbolTableSpecifier = checkMethodInParents(baseType, termName, argumentTypes);

				}
				if (methodInSymbolTableSpecifier.getLabel() == CoreRebecaLabelUtility.CONSTRUCTOR) {
					exceptionContainer.getExceptions()
					.add(new CodeCompilationException(
							"The method " + termName + SymbolTable.convertMethodArgumentsToString(argumentTypes)
							+ " is undefined"
							+ (baseType == null || baseType == TypesUtilities.NO_TYPE ? ""
									: " for the type " + TypesUtilities.getTypeName(baseType)),
							termPrimary.getLineNumber(), termPrimary.getCharacter()));
				}
				termPrimary.setLabel(methodInSymbolTableSpecifier.getLabel());
				returnValue.setFirst(methodInSymbolTableSpecifier.getReturnValue());
				termPrimary.setType(methodInSymbolTableSpecifier.getReturnValue());
			}
			if (!termPrimary.getIndices().isEmpty()) {
				if (!(returnValue.getFirst() instanceof ArrayType)) {
					ArrayType arrayType = new ArrayType();
					arrayType.setOrdinaryPrimitiveType((OrdinaryPrimitiveType) returnValue.getFirst());
					arrayType.getDimensions().add(0);
					TypesUtilities.addTypeMismatchException(exceptionContainer, returnValue.getFirst(), arrayType,
							termPrimary);
					return returnValue;
				}
				ArrayType foundTypeInArray = (ArrayType) returnValue.getFirst();
				if (termPrimary.getIndices().size() > foundTypeInArray.getDimensions().size()) {
					ArrayType arrayType = new ArrayType();
					arrayType.setOrdinaryPrimitiveType(foundTypeInArray.getOrdinaryPrimitiveType());
					for (int cnt = 0; cnt < termPrimary.getIndices().size(); cnt++)
						arrayType.getDimensions().add(0);
					TypesUtilities.addTypeMismatchException(exceptionContainer, returnValue.getFirst(), arrayType,
							termPrimary);
					return returnValue;
				}

				for (Expression expr : termPrimary.getIndices()) {
					Type type = ((ExpressionSemanticCheckContainer) defaultContainer).check(expr).getFirst();
					if (!TypesUtilities.getInstance().canTypeCastTo(type, TypesUtilities.INT_TYPE)) {
						TypesUtilities.addTypeMismatchException(exceptionContainer, type, TypesUtilities.INT_TYPE,
								termPrimary);
						return returnValue;
					}
				}
				if (termPrimary.getIndices().size() == foundTypeInArray.getDimensions().size()) {
					returnValue.setFirst(foundTypeInArray.getOrdinaryPrimitiveType());
				} else {
					ArrayType arrayType = new ArrayType();
					arrayType.setOrdinaryPrimitiveType(foundTypeInArray.getOrdinaryPrimitiveType());
					for (int cnt = 0; cnt < foundTypeInArray.getDimensions().size()
							- termPrimary.getIndices().size(); cnt++)
						arrayType.getDimensions().add(foundTypeInArray.getDimensions().get(cnt));
					returnValue.setFirst(arrayType);
				}

			}
		} catch (CodeCompilationException cce) {
			cce.setColumn(termPrimary.getCharacter());
			cce.setLine(termPrimary.getLineNumber());
			exceptionContainer.addException(cce);
		}

		return returnValue;
	}

	private MethodInSymbolTableSpecifier checkMethodInParents (Type type, String termName, List<Type> argumentTypes) throws CodeCompilationException {

		Type lookupBaseType = type;
		MethodInSymbolTableSpecifier methodInSymbolTableSpecifier = null;
		if (lookupBaseType instanceof GenericTypeInstance) {
			lookupBaseType = ((GenericTypeInstance) lookupBaseType).getBase();
		}
		while(true) {
			try {
				methodInSymbolTableSpecifier = symbolTable.getCastableMethodSpecification(lookupBaseType, termName, argumentTypes);
				// TODO this code only works for GenericType with 1 parameters
				if ( methodInSymbolTableSpecifier.getReturnValue() == TypesUtilities.UNKNOWN_TYPE )
					methodInSymbolTableSpecifier.setReturnValue(((GenericTypeInstance) type).getParameters().get(0));
				return methodInSymbolTableSpecifier;
			}
			catch(SymbolTableException ste) {
				ReactiveClassDeclaration rcd = (ReactiveClassDeclaration)TypesUtilities.getInstance().getMetaData(lookupBaseType);

				if (rcd.getExtends() == null)
					throw ste;
				lookupBaseType = rcd.getExtends();
			}


		} 	
	}
}

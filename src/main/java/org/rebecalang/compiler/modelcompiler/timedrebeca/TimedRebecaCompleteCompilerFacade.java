package org.rebecalang.compiler.modelcompiler.timedrebeca;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Parser;
import org.rebecalang.compiler.modelcompiler.ScopeException;
import org.rebecalang.compiler.modelcompiler.abstractrebeca.AbstractTypeSystem;
import org.rebecalang.compiler.modelcompiler.abstractrebeca.SymbolTableInitializer;
import org.rebecalang.compiler.modelcompiler.abstractrebeca.TypeSystemInitializer;
import org.rebecalang.compiler.modelcompiler.corerebeca.CoreRebecaCompleteCompilerFacade;
import org.rebecalang.compiler.modelcompiler.corerebeca.CoreRebecaLabelUtility;
import org.rebecalang.compiler.modelcompiler.corerebeca.CoreRebecaTypeSystem;
import org.rebecalang.compiler.modelcompiler.corerebeca.objectmodel.*;
import org.rebecalang.compiler.modelcompiler.timedrebeca.compiler.TimedRebecaCompleteLexer;
import org.rebecalang.compiler.modelcompiler.timedrebeca.compiler.TimedRebecaCompleteParser;
import org.rebecalang.compiler.modelcompiler.timedrebeca.network.NetworkUtilityDeclarationsGenerator;
import org.rebecalang.compiler.modelcompiler.timedrebeca.objectmodel.*;
import org.rebecalang.compiler.modelcompiler.timedrebeca.statementsemanticchecker.expression.AggregationConditionPrimarySemanticCheck;
import org.rebecalang.compiler.modelcompiler.timedrebeca.statementsemanticchecker.expression.DelayExpressionSemanticCheck;
import org.rebecalang.compiler.modelcompiler.timedrebeca.statementsemanticchecker.expression.LossExpressionSemanticCheck;
import org.rebecalang.compiler.modelcompiler.timedrebeca.statementsemanticchecker.expression.TimedPrimaryTermSemanticCheck;
import org.rebecalang.compiler.utils.CodeCompilationException;
import org.rebecalang.compiler.utils.Pair;
import org.rebecalang.compiler.utils.TypesUtilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Qualifier("TIMED_REBECA")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class TimedRebecaCompleteCompilerFacade extends CoreRebecaCompleteCompilerFacade {


	public TimedRebecaCompleteCompilerFacade(@Qualifier("TIMED_REBECA") TypeSystemInitializer typeSystemInitializer,
			@Qualifier("TIMED_REBECA") SymbolTableInitializer symbolTableInitializer) {
		super(typeSystemInitializer, symbolTableInitializer);
	}

	@Autowired
	private ApplicationContext appContext;
	private PriorityType modelPriorityType;
	

	@Autowired
	public void setTypeSystem(@Qualifier("TIMED_REBECA") AbstractTypeSystem typeSystem) {
		this.typeSystem = typeSystem;
	}

	@Override
	public void semanticCheck() {
		this.modelPriorityType = null;
		super.semanticCheck();
	}
	@Override
	protected void initializeExpressionSemanticCheckContainer() {
		super.initializeExpressionSemanticCheckContainer();

		expressionSemanticCheckContainer.registerSemanticsChecker(TermPrimary.class, 
				appContext.getBean(TimedPrimaryTermSemanticCheck.class,
				typeSystem,
				expressionSemanticCheckContainer));
		expressionSemanticCheckContainer.registerSemanticsChecker(AggregationConditionPrimary.class,
				appContext.getBean(AggregationConditionPrimarySemanticCheck.class,
				typeSystem,
				expressionSemanticCheckContainer));
		expressionSemanticCheckContainer.registerSemanticsChecker(DelayExpression.class,
				appContext.getBean(DelayExpressionSemanticCheck.class, expressionSemanticCheckContainer));
		expressionSemanticCheckContainer.registerSemanticsChecker(LossExpression.class,
				appContext.getBean(LossExpressionSemanticCheck.class, expressionSemanticCheckContainer));
	}

	@Override
	protected void addMethodsOfRebecaExtensionToSymbolTable() {
		super.addMethodsOfRebecaExtensionToSymbolTable();

		SynchMethodDeclaration delayMethod = new SynchMethodDeclaration();
		delayMethod.setName("delay");
		FormalParameterDeclaration fpd = new FormalParameterDeclaration();
		fpd.setName("arg0");
		fpd.setType(CoreRebecaTypeSystem.INT_TYPE);
		delayMethod.getFormalParameters().add(fpd);
		delayMethod.setReturnType(CoreRebecaTypeSystem.VOID_TYPE);

		symbolTable.addMethod(null, delayMethod, TimedRebecaLabelUtility.DELAY);
		
		SynchMethodDeclaration startTimerMethod = new SynchMethodDeclaration();
		startTimerMethod.setName("startTimer");
		fpd = new FormalParameterDeclaration();
		fpd.setName("arg0");
		fpd.setType(TimedRebecaTypeSystem.TIMER_TYPE);
		startTimerMethod.getFormalParameters().add(fpd);
		startTimerMethod.setReturnType(CoreRebecaTypeSystem.VOID_TYPE);
		SynchMethodDeclaration stopTimerMethod = new SynchMethodDeclaration();
		stopTimerMethod.setName("stopTimer");
		fpd = new FormalParameterDeclaration();
		fpd.setName("arg0");
		fpd.setType(TimedRebecaTypeSystem.TIMER_TYPE);
		stopTimerMethod.getFormalParameters().add(fpd);
		stopTimerMethod.setReturnType(CoreRebecaTypeSystem.VOID_TYPE);
		SynchMethodDeclaration getTimerValueMethod = new SynchMethodDeclaration();
		getTimerValueMethod.setName("getTimerValue");
		fpd = new FormalParameterDeclaration();
		fpd.setName("arg0");
		fpd.setType(TimedRebecaTypeSystem.TIMER_TYPE);
		getTimerValueMethod.getFormalParameters().add(fpd);
		getTimerValueMethod.setReturnType(CoreRebecaTypeSystem.INT_TYPE);
		
		symbolTable.addMethod(null, startTimerMethod,
				CoreRebecaLabelUtility.BUILT_IN_METHOD);
		symbolTable.addMethod(null, stopTimerMethod,
				CoreRebecaLabelUtility.BUILT_IN_METHOD);
		symbolTable.addMethod(null, getTimerValueMethod,
				CoreRebecaLabelUtility.BUILT_IN_METHOD);
	}
	@Override
	protected void addVariablesOfRebecaExtensionToScope() {
		super.addVariablesOfRebecaExtensionToScope();
		try {
			scopeHandler.addVariableToCurrentScope("now", CoreRebecaTypeSystem.INT_TYPE, 
					CoreRebecaLabelUtility.RESERVED_WORD, 0, 0);
			scopeHandler.addVariableToCurrentScope("currentMessageArrival", CoreRebecaTypeSystem.INT_TYPE, 
					CoreRebecaLabelUtility.RESERVED_WORD, 0, 0);
			scopeHandler.addVariableToCurrentScope("currentMessageDeadline", CoreRebecaTypeSystem.INT_TYPE,
					CoreRebecaLabelUtility.RESERVED_WORD, 0, 0);
			scopeHandler.addVariableToCurrentScope("currentMessageWaitingTime", CoreRebecaTypeSystem.INT_TYPE,
					CoreRebecaLabelUtility.RESERVED_WORD, 0, 0);
			scopeHandler.addVariableToCurrentScope("messageArrivalTime", CoreRebecaTypeSystem.INT_TYPE,
					CoreRebecaLabelUtility.RESERVED_WORD, 0, 0);
			scopeHandler.addVariableToCurrentScope("messageDeadline", CoreRebecaTypeSystem.INT_TYPE,
					CoreRebecaLabelUtility.RESERVED_WORD, 0, 0);
			scopeHandler.addVariableToCurrentScope("messageExecutionTime", CoreRebecaTypeSystem.INT_TYPE,
					CoreRebecaLabelUtility.RESERVED_WORD, 0, 0);
            scopeHandler.addVariableToCurrentScope("messageServerName", CoreRebecaTypeSystem.STRING_TYPE,
                    CoreRebecaLabelUtility.RESERVED_WORD, 0, 0);
		} catch (ScopeException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void addExtraMetaDeclarationsToRebecaExtension() {
		TimedRebecaCode rebecaCode = (TimedRebecaCode) rebecaModel.getRebecaCode();
		if (rebecaCode.getNetworkDeclaration().isEmpty())
			return;
		NetworkUtilityDeclarationsGenerator utilityDeclarationsGenerator = new NetworkUtilityDeclarationsGenerator();
		utilityDeclarationsGenerator.modifyRebecaModelToSupportNetwork(rebecaModel);
	}

	@Override
	public Parser getParser(CharStream input) {
		TimedRebecaCompleteLexer lexer = new TimedRebecaCompleteLexer(input);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		return new TimedRebecaCompleteParser(tokens);
	}

	protected void semanticCheckRebecaDeclarations() {
		super.semanticCheckRebecaDeclarations();
		semanticCheckMailboxDeclarations();
		semanticCheckNetworkDeclarations();
	}

	protected void semanticCheckMainBindings(RebecaModel rebecaModel) {
		scopeHandler.pushScopeRecord(CoreRebecaLabelUtility.MAIN);
		semanticCheckForActors(rebecaModel);
		semanticCheckForMailboxes(rebecaModel);
		semanticCheckForNetworks(rebecaModel);
		semanticCheckForActorsBundledMailbox(rebecaModel);
		semanticCheckForNetworksBundledMailbox(rebecaModel);
		scopeHandler.popScopeRecord();
	}

	private void semanticCheckForActorsBundledMailbox(RebecaModel rebecaModel) {
		for (MainRebecDefinition mrd : rebecaModel.getRebecaCode().getMainDeclaration().getMainRebecDefinition()) {
			TermPrimary mbExpression = (TermPrimary) ((TimedMainRebecDefinition)mrd).getMailbox();
			if (mbExpression == null)
				continue;
			((TimedMainRebecDefinition) mrd).getMailbox().setType(getTheMailboxInstanceTypeIfExists(mbExpression));
		}
	}

	private void semanticCheckForNetworksBundledMailbox(RebecaModel rebecaModel) {
		TimedMainDeclaration timedMainDeclaration = (TimedMainDeclaration) rebecaModel.getRebecaCode().getMainDeclaration();
		for (MainNetworkDefinition mnd : timedMainDeclaration.getMainNetworkDefinition()) {
			TermPrimary mbExpression = (TermPrimary) mnd.getMailbox();
			if (mbExpression == null)
				continue;
			mnd.getMailbox().setType(getTheMailboxInstanceTypeIfExists(mbExpression));
		}
	}

	private void semanticCheckForMailboxes(RebecaModel rebecaModel) {
		TimedMainDeclaration timedMainDeclaration = (TimedMainDeclaration) rebecaModel.getRebecaCode().getMainDeclaration();
		for(MainMailboxDefinition mmbd : timedMainDeclaration.getMainMailboxDefinition()) {
			try {
				scopeHandler.retreiveVariableFromScope(mmbd.getName());
				CodeCompilationException rce = new CodeCompilationException(
						"Multiple definition for the mailbox " + mmbd.getName(), mmbd.getLineNumber(), mmbd.getCharacter());
				this.exceptionContainer.addException(rce);
			} catch (ScopeException se) {
				try {
					Type type =typeSystem.getType(mmbd.getType());
					mmbd.setType(type);
					scopeHandler.addVariableToCurrentScope(mmbd.getName(), type, CoreRebecaLabelUtility.LOCAL_VARIABLE,
							mmbd.getLineNumber(), mmbd.getCharacter());
				} catch (CodeCompilationException cce) {
					cce.setColumn(mmbd.getCharacter());
					cce.setLine(mmbd.getLineNumber());
					exceptionContainer.addException(cce);
				}
			}
		}
		HashMap<String, MailboxDeclaration> mailboxDeclarations = getAllMailboxDeclarations();
		for(MainMailboxDefinition mmbd : timedMainDeclaration.getMainMailboxDefinition()) {
			LinkedList<Type> knownSendersBindingsTypes = semanticCheckKnownSendersBindings(mmbd);
			MailboxDeclaration mailboxDeclaration = mailboxDeclarations.get(mmbd.getType().getTypeName());
			List<FieldDeclaration> knownSenders = mailboxDeclaration.getKnownSenders();
			List<Type> expectedSendersTypes = getExpectedRebecsTypes(knownSenders);
			if (!TypesUtilities.areTheSame(knownSendersBindingsTypes, expectedSendersTypes, Type.getCastableComparator())) {
				CodeCompilationException cce = new CodeCompilationException(
						createCheckMainBindingsExceptionMessage(knownSenders, knownSendersBindingsTypes,
								mailboxDeclaration.getName(),
								"knownsenders"),
						mmbd.getLineNumber(), mmbd.getCharacter());
				exceptionContainer.addException(cce);
			}
		}
	}

	private void semanticCheckForNetworks(RebecaModel rebecaModel) {
		TimedMainDeclaration timedMainDeclaration = (TimedMainDeclaration) rebecaModel.getRebecaCode().getMainDeclaration();
		for (MainNetworkDefinition mnd : timedMainDeclaration.getMainNetworkDefinition()) {
			try {
				scopeHandler.retreiveVariableFromScope(mnd.getName());
				CodeCompilationException rce = new CodeCompilationException(
						"Multiple definition for the network " + mnd.getName(), mnd.getLineNumber(), mnd.getCharacter());
				this.exceptionContainer.addException(rce);
			} catch (ScopeException se) {
				try {
					Type type =typeSystem.getType(mnd.getType());
					mnd.setType(type);
					scopeHandler.addVariableToCurrentScope(mnd.getName(), type, CoreRebecaLabelUtility.LOCAL_VARIABLE,
							mnd.getLineNumber(), mnd.getCharacter());
				} catch (CodeCompilationException cce) {
					cce.setColumn(mnd.getCharacter());
					cce.setLine(mnd.getLineNumber());
					exceptionContainer.addException(cce);
				}
			}
		}
		HashMap<String, NetworkDeclaration> networkDeclarations = getAllNetworkDeclarations();
		for (MainNetworkDefinition mnd : timedMainDeclaration.getMainNetworkDefinition()) {
			LinkedList<Type> knownNodesBindingsType = semanticCheckKnownNodesBindings(mnd);
			NetworkDeclaration networkDeclaration = networkDeclarations.get(mnd.getType().getTypeName());
			List<FieldDeclaration> knownNodes = networkDeclaration.getKnownNodes();
			List<Type> expectedNodesTypes = getExpectedRebecsTypes(knownNodes);
			if (!TypesUtilities.areTheSame(knownNodesBindingsType, expectedNodesTypes, Type.getCastableComparator())) {
				CodeCompilationException cce = new CodeCompilationException(
						createCheckMainBindingsExceptionMessage(knownNodes, knownNodesBindingsType,
								networkDeclaration.getName(),
								"knownnodes"),
						mnd.getLineNumber(), mnd.getCharacter());
				exceptionContainer.addException(cce);
			}
		}
	}

	protected void checkPriorityAnnotations(List<Annotation> annotations) {
		for (Annotation annotation : annotations) {
			if (annotation.getIdentifier().equals(PRIORITY_LABEL) || annotation.getIdentifier().equals(GLOBAL_PRIORITY_LABEL)) {
				if (annotation.getIdentifier().equals(PRIORITY_LABEL))
					if(conflictInPriorityType(PriorityType.local, annotation)) 
						continue;
				if (annotation.getIdentifier().equals(GLOBAL_PRIORITY_LABEL))
					if(conflictInPriorityType(PriorityType.global, annotation)) 
						continue;
				
				Pair<Type, Object> valueCheckResult = statementSemanticCheckContainer.check(annotation.getValue());
				if (!checkIfTheValueOfPriorityAnnotationIsInteger(valueCheckResult, annotation))
					continue;
				if (!checkIfTheValueOfPriorityAnnotationIsConstantExpression(valueCheckResult.getSecond(), annotation)) {
					continue;
				}
				
				setAnnotationConstantValue(annotation, valueCheckResult.getSecond());
			}
		}
	}

	private void semanticCheckMailboxDeclarations() {
		for (MailboxDeclaration mailboxDeclaration : ((TimedRebecaCode)rebecaModel.getRebecaCode()).getMailboxDeclaration()) {
			scopeHandler.pushScopeRecord(TimedRebecaLabelUtility.MAILBOX);
			addKnownSendersOfMailboxToScope(mailboxDeclaration);
			semanticCheckForOrdersOfMailboxDeclaration(mailboxDeclaration);
			scopeHandler.popScopeRecord();
		}
	}

	private void semanticCheckNetworkDeclarations() {
		for (NetworkDeclaration networkDeclaration : ((TimedRebecaCode)rebecaModel.getRebecaCode()).getNetworkDeclaration()) {
			scopeHandler.pushScopeRecord(TimedRebecaLabelUtility.NETWORK);
			addKnownNodesOfNetworkToScope(networkDeclaration);
			semanticCheckForDelaysOfNetworkDeclaration(networkDeclaration);
			semanticCheckForLossesOfNetworkDeclarations(networkDeclaration);
			scopeHandler.popScopeRecord();
		}
	}

	private void addKnownSendersOfMailboxToScope(MailboxDeclaration mailboxDeclaration) {
		try {
			scopeHandler.addVariableToCurrentScope("sender", CoreRebecaTypeSystem.REACTIVE_CLASS_TYPE,
					CoreRebecaLabelUtility.RESERVED_WORD, 0, 0);
		} catch (ScopeException e) {
			e.printStackTrace();
		}
		for (FieldDeclaration fd : mailboxDeclaration.getKnownSenders()) {
			statementSemanticCheckContainer.check(fd);
			for (VariableDeclarator vd : fd.getVariableDeclarators()) {
				scopeHandler.updateVaribaleInCurrentScope(vd.getVariableName(), fd.getType(),
						TimedRebecaLabelUtility.KNOWNSENDER_VARIABLE, vd.getLineNumber(), vd.getCharacter());

				if (vd.getVariableInitializer() != null) {
					CodeCompilationException rce = new CodeCompilationException(
							"Known senders are only initialized during instantiation", vd.getLineNumber(),
							vd.getCharacter());
					exceptionContainer.addException(rce);
				}
			}
		}
	}

	private void addKnownNodesOfNetworkToScope(NetworkDeclaration networkDeclaration) {
		for (FieldDeclaration fd :networkDeclaration.getKnownNodes()) {
			statementSemanticCheckContainer.check(fd);
			for (VariableDeclarator vd : fd.getVariableDeclarators()) {
				scopeHandler.updateVaribaleInCurrentScope(vd.getVariableName(), fd.getType(),
						TimedRebecaLabelUtility.KNOWNNODE_VARIABLE, vd.getLineNumber(), vd.getCharacter());
				if (vd.getVariableInitializer() != null) {
					CodeCompilationException rce = new CodeCompilationException(
							"Known nodes are only initialized during instantiation", vd.getLineNumber(),
							vd.getCharacter());
					exceptionContainer.addException(rce);
				}
			}
		}
	}

	private void semanticCheckForOrdersOfMailboxDeclaration(MailboxDeclaration mailboxDeclaration) {
		//TODO: Should be implemented!
		addAllMessageServersNamesToScope();
		for(Expression expression : mailboxDeclaration.getOrders()) {
			Pair<Type, Object> result = expressionSemanticCheckContainer.check(expression);
//			if (result.getFirst() != CoreRebecaTypeSystem.BOOLEAN_TYPE) {
//				CodeCompilationException rce = new CodeCompilationException(
//						"order specification type should be boolean.",
//						expression.getLineNumber(),
//						expression.getCharacter());
//				exceptionContainer.addException(rce);
//			}
		}
	}

	private void semanticCheckForDelaysOfNetworkDeclaration(NetworkDeclaration networkDeclaration) {
		for (DelayExpression expression : networkDeclaration.getDelays()) {
			expressionSemanticCheckContainer.check(expression);
		}
	}

	private void semanticCheckForLossesOfNetworkDeclarations(NetworkDeclaration networkDeclaration) {
		for (LossExpression expression : networkDeclaration.getLosses()) {
			expressionSemanticCheckContainer.check(expression);
		}
	}

	private void addAllMessageServersNamesToScope() {
		for (ReactiveClassDeclaration rcd : rebecaModel.getRebecaCode().getReactiveClassDeclaration()) {
			for (MsgsrvDeclaration msgsrv : rcd.getMsgsrvs()) {
				try {
					scopeHandler.retreiveVariableFromScope(msgsrv.getName());
				} catch (ScopeException ignored) {
					try {
						scopeHandler.addVariableToCurrentScope(
								msgsrv.getName(),
								CoreRebecaTypeSystem.STRING_TYPE,
								CoreRebecaLabelUtility.MSGSRV,
								msgsrv.getLineNumber(),
								msgsrv.getCharacter()
						);
					} catch (ScopeException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	private HashMap<String, MailboxDeclaration> getAllMailboxDeclarations() {
		HashMap<String, MailboxDeclaration> mailboxDeclarations = new HashMap<>();
		for (MailboxDeclaration mbd : ((TimedRebecaCode)rebecaModel.getRebecaCode()).getMailboxDeclaration()) {
			mailboxDeclarations.put(mbd.getName(), mbd);
		}
		return mailboxDeclarations;
	}

	private <T> HashMap<String, NetworkDeclaration> getAllNetworkDeclarations() {
		HashMap<String, NetworkDeclaration> networkDeclarations = new HashMap<>();
		for (NetworkDeclaration nd : ((TimedRebecaCode)rebecaModel.getRebecaCode()).getNetworkDeclaration()) {
			networkDeclarations.put(nd.getName(), nd);
		}
		return networkDeclarations;
	}

	private LinkedList<Type> semanticCheckKnownSendersBindings(MainMailboxDefinition mainMailboxDefinition) {
		LinkedList<Type> knownSendersBindingsTypes = new LinkedList<>();
		for (Expression expression : mainMailboxDefinition.getBindings()) {
			knownSendersBindingsTypes.add(statementSemanticCheckContainer.check(expression).getFirst());
		}
		return knownSendersBindingsTypes;
	}

	private LinkedList<Type> semanticCheckKnownNodesBindings(MainNetworkDefinition mainNetworkDefinition) {
		LinkedList<Type> knownNodesBindingsTypes = new LinkedList<>();
		for (Expression expression : mainNetworkDefinition.getBindings()) {
			knownNodesBindingsTypes.add(statementSemanticCheckContainer.check(expression).getFirst());
		}
		return knownNodesBindingsTypes;
	}
	private List<Type> getExpectedRebecsTypes(List<FieldDeclaration> knowns) {
		List<Type> expectedTypes = new LinkedList<>();
		for (FieldDeclaration fd : knowns) {
			for (int variableCounter = 0; variableCounter < fd.getVariableDeclarators().size(); variableCounter++) {
				if (fd.getType() instanceof OrdinaryPrimitiveType) {
					try {
						expectedTypes.add(typeSystem.getType(fd.getType()));
					} catch (CodeCompilationException e) {
						expectedTypes.add(AbstractTypeSystem.UNKNOWN_TYPE);
						e.setColumn(fd.getCharacter());
						e.setLine(fd.getLineNumber());
						exceptionContainer.addException(e);
					}
				}
			}
		}
		return expectedTypes;
	}

	private Type getTheMailboxInstanceTypeIfExists(TermPrimary mbExpression) {
		try {
			scopeHandler.retreiveVariableFromScope(mbExpression.getName());
			return statementSemanticCheckContainer.check(mbExpression).getFirst();
		} catch (ScopeException e) {
			CodeCompilationException cce = new CodeCompilationException(
					"No Mailboxes were instantiated with name '" + mbExpression.getName() + "'",
					mbExpression.getLineNumber(),
					mbExpression.getCharacter());
			exceptionContainer.addException(cce);
		}
		return null;
	}
	private boolean conflictInPriorityType(PriorityType newPriorityType, Annotation annotation) {
		if((newPriorityType == PriorityType.local && modelPriorityType == PriorityType.global) ||
				(newPriorityType == PriorityType.global && modelPriorityType == PriorityType.local)) {
			CodeCompilationException cce = new CodeCompilationException(
					"Conflict between local and global priority definitions", annotation.getLineNumber(),
					annotation.getCharacter());
			exceptionContainer.addException(cce);
			return true;
		}
		modelPriorityType = newPriorityType;
		return false;
	}

	private void setAnnotationConstantValue(Annotation annotation, Object valueCheckResult) {
		Literal literal = new Literal();
		literal.setCharacter(annotation.getCharacter());
		literal.setLineNumber(annotation.getLineNumber());
		literal.setLiteralValue(valueCheckResult.toString());
		literal.setType(CoreRebecaTypeSystem.INT_TYPE);
		annotation.setValue(literal);
	}

	private boolean checkIfTheValueOfPriorityAnnotationIsConstantExpression(Object expressionValue, Annotation annotation) {
		if(expressionValue == null) {
			CodeCompilationException cce = new CodeCompilationException(
					"Priority value expressions must be constant expressions", annotation.getLineNumber(),
					annotation.getCharacter());
			exceptionContainer.addException(cce);
			return false;
		}
		return true;
	}

	private boolean checkIfTheValueOfPriorityAnnotationIsInteger(Pair<Type, Object> valueCheckResult, Annotation annotation) {
		if(!valueCheckResult.getFirst().canTypeUpCastTo(CoreRebecaTypeSystem.INT_TYPE)) {
			CodeCompilationException cce = new CodeCompilationException(
					"Value of priority must be an integer expression", annotation.getLineNumber(),
					annotation.getCharacter());
			exceptionContainer.addException(cce);
			return false;
		}
		return true;
	}

}

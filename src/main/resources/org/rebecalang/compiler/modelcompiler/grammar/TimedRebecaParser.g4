parser grammar TimedRebecaParser;

import CoreRebecaParser;

rebecaCode returns [TimedRebecaCode rc]
    :
        {$rc = new TimedRebecaCode();}
		(rd = recordDeclaration {$rc.getRecordDeclaration().add($rd.rd);})*
		(
			(ENV fd = fieldDeclaration SEMI {$rc.getEnvironmentVariables().add($fd.fd);})
			|
			(FEATUREVAR featureName = IDENTIFIER SEMI
				{
				VariableDeclarator vd = new VariableDeclarator();
				vd.setVariableName($featureName.text);
				vd.setLineNumber($featureName.getLine());
				vd.setCharacter($featureName.getCharPositionInLine());
				FieldDeclaration fd = new FieldDeclaration();
				fd.getVariableDeclarators().add(vd);
				fd.setType(CoreRebecaTypeSystem.BOOLEAN_TYPE);
    			fd.setCharacter($featureName.getCharPositionInLine());
				fd.setLineNumber($featureName.getLine());
				$rc.getFeatureVariables().add(fd);
				}
			)
		)*
        (
            mbd = mailboxDeclaration {$rc.getMailboxDeclaration().add($mbd.mbd);}
            |
            nd = networkDeclaration {$rc.getNetworkDeclaration().add($nd.nd);}
            |
        	rcd = reactiveClassDeclaration {$rc.getReactiveClassDeclaration().add($rcd.rcd);}
        	|
        	intd = interfaceDeclaration {$rc.getInterfaceDeclaration().add($intd.intd);}
    	)+
        md = timedMainDeclaration  {$rc.setMainDeclaration($md.md);}
    ;

mailboxDeclaration returns [MailboxDeclaration mbd]
    :
        {$mbd = new MailboxDeclaration();}
        MAILBOX mailboxName = IDENTIFIER
        	{	$mbd.setName($mailboxName.text);
        		$mbd.setLineNumber($mailboxName.getLine()); $mbd.setCharacter($mailboxName.getCharPositionInLine());
        	}
        LBRACE

        (KNOWNSENDERS
        LBRACE
            (fd = fieldDeclaration {$mbd.getKnownSenders().add($fd.fd);} SEMI)*
        RBRACE)?

        (ORDERS
        LBRACE
            (orders = orderSpecifications {$mbd.getOrders().addAll($orders.orders);})*
        RBRACE)?
        RBRACE {$mbd.setEndLineNumber($RBRACE.getLine());$mbd.setEndCharacter($RBRACE.getCharPositionInLine());}
    ;

networkDeclaration returns [NetworkDeclaration nd]
    :
        {$nd = new NetworkDeclaration();}
        NETWORK networkName = IDENTIFIER
            {
                $nd.setName($networkName.text);
                $nd.setLineNumber($networkName.getLine()); $nd.setCharacter($networkName.getCharPositionInLine());
            }
        LBRACE

        (KNOWNNODES
        LBRACE
            (fd = fieldDeclaration {$nd.getKnownNodes().add($fd.fd);} SEMI)*
        RBRACE)?

        (DELAYS
        LBRACE
            (delays = delaySpecifications {$nd.getDelays().addAll($delays.delays);})*
        RBRACE)?

        (LOSSES
        LBRACE
            (losses = lossSpecifications {$nd.getLosses().addAll($losses.losses);})*
        RBRACE
        )?
        RBRACE {$nd.setEndLineNumber($RBRACE.getLine()); $nd.setEndCharacter($RBRACE.getCharPositionInLine());}
    ;

timedMainDeclaration returns [TimedMainDeclaration md]
	:
		{$md = new TimedMainDeclaration();}
		MAIN {$md.setLineNumber($MAIN.getLine());$md.setCharacter($MAIN.getCharPositionInLine());}
		LBRACE
		(
    		mmbd = mainMailboxDefinition {$md.getMainMailboxDefinition().add($mmbd.mmbd);}
    		|
    		mnd = mainNetworkDefinition {$md.getMainNetworkDefinition().add($mnd.mnd);}
    		|
    		mrd = timedMainRebecDefinition {$md.getMainRebecDefinition().add($mrd.mrd);}
		)*
		RBRACE {$md.setEndLineNumber($RBRACE.getLine());$md.setEndCharacter($RBRACE.getCharPositionInLine());}
	;

mainMailboxDefinition returns [MainMailboxDefinition mmbd]
    :
        {$mmbd = new MainMailboxDefinition();}
        t = type mailboxName = IDENTIFIER {$mmbd.setType($t.t);$mmbd.setName($mailboxName.text);
            $mmbd.setLineNumber($mailboxName.getLine()); $mmbd.setCharacter($mailboxName.getCharPositionInLine());}
        LPAREN (el = expressionList {$mmbd.getBindings().addAll($el.el);})? RPAREN
        SEMI
    ;

mainNetworkDefinition returns [MainNetworkDefinition mnd]
    :
        {$mnd = new MainNetworkDefinition();}
        t = type networkName = IDENTIFIER {$mnd.setType($t.t);$mnd.setName($networkName.text);
            $mnd.setLineNumber($networkName.getLine()); $mnd.setCharacter($networkName.getCharPositionInLine());}
        LT (e = expression {$mnd.setMailbox($e.e);})? GT
        LPAREN (el = expressionList {$mnd.getBindings().addAll($el.el);})? RPAREN
        SEMI
    ;

timedMainRebecDefinition returns [TimedMainRebecDefinition mrd]
	:
		{$mrd = new TimedMainRebecDefinition();}
    	(an = annotation {$mrd.getAnnotations().add($an.an);})*
		t = type rebecName = IDENTIFIER {$mrd.setType($t.t);$mrd.setName($rebecName.text);
			$mrd.setLineNumber($rebecName.getLine()); $mrd.setCharacter($rebecName.getCharPositionInLine());}
		(LT e = expression {$mrd.setMailbox($e.e);} GT)?
		LPAREN (el = expressionList {$mrd.getBindings().addAll($el.el);})? RPAREN
		COLON
		LPAREN (el = expressionList {$mrd.getArguments().addAll($el.el);})? RPAREN
		SEMI
	;

delayExpression returns [DelayExpression e]
    :
     sender = IDENTIFIER {$e = new DelayExpression(); $e.setSenderName($sender.text);
         $e.setLineNumber($sender.getLine()); $e.setCharacter($sender.getCharPositionInLine());}
     CONNECT
     receiver = IDENTIFIER {$e.setReceiverName($receiver.text);}
     COLON
     amount = expression {$e.setAmount($amount.e);}
    ;

lossExpression returns [LossExpression e]
    :
     sender = IDENTIFIER {$e = new LossExpression(); $e.setSenderName($sender.text);
         $e.setLineNumber($sender.getLine()); $e.setCharacter($sender.getCharPositionInLine());}
     CONNECT
     receiver = IDENTIFIER {$e.setReceiverName($receiver.text);}
    ;

delaySpecifications returns [List<DelayExpression> delays]
    :
        {$delays = new LinkedList<DelayExpression>();}
        e = delayExpression {$delays.add($e.e);}
        (COMMA e = delayExpression {$delays.add($e.e);})*
    ;

lossSpecifications returns [List<LossExpression> losses]
    :
        {$losses = new LinkedList<LossExpression>();}
        e = lossExpression {$losses.add($e.e);}
        (COMMA e = lossExpression {$losses.add($e.e);})*
    ;

orderSpecifications returns [List<Expression> orders]
    :
        {$orders = new LinkedList<Expression>();}
        e = conditionalExpression {$orders.add($e.e);}
        (COMMA e = conditionalExpression {$orders.add($e.e);})*
    ;

primary returns [PrimaryExpression tp]
    :   
    id1 = IDENTIFIER {TermPrimary tpr = new TermPrimary(); tpr.setName($id1.text);
					  tpr.setLineNumber($id1.getLine()); tpr.setCharacter($id1.getCharPositionInLine());
					  $tp = tpr;
					  }
    (	lp = LPAREN 
    	{TimedRebecaParentSuffixPrimary psp = new TimedRebecaParentSuffixPrimary(); 
    	 psp.setLineNumber($lp.getLine()); psp.setCharacter($lp.getCharPositionInLine());
    	 tpr.setParentSuffixPrimary(psp);
    	 $tp = tpr;
    	 }
		(el = expressionList {tpr.getParentSuffixPrimary().getArguments().addAll($el.el); $tp = tpr;})?
		RPAREN
    	(AFTER LPAREN ef = expression RPAREN {psp.setAfterExpression($ef.e);})?
    	(DEADLINE LPAREN ed = expression RPAREN {psp.setDeadlineExpression($ed.e);})?
    )?
	(LBRACKET e2 = expression RBRACKET {tpr.getIndices().add($e2.e); $tp = tpr;})*

	| (agg = MIN | agg = MAX) {AggregationConditionPrimary ac = new AggregationConditionPrimary(); ac.setName($agg.text);
                                 ac.setLineNumber($agg.getLine()); ac.setCharacter($agg.getCharPositionInLine());}
        LPAREN e = expression {ac.setArgument($e.e); $tp = ac;} RPAREN
    ;

package org.rebecalang.compiler.modelcompiler.timedrebeca;

import java.util.Objects;
import org.rebecalang.compiler.modelcompiler.abstractrebeca.AbstractTypeSystem;
import org.rebecalang.compiler.modelcompiler.abstractrebeca.SymbolTableInitializer;
import org.rebecalang.compiler.modelcompiler.corerebeca.objectmodel.RebecaModel;
import org.rebecalang.compiler.modelcompiler.corerebeca.objectmodel.Type;
import org.rebecalang.compiler.modelcompiler.timedrebeca.objectmodel.MailboxDeclaration;
import org.rebecalang.compiler.modelcompiler.timedrebeca.objectmodel.NetworkDeclaration;
import org.rebecalang.compiler.modelcompiler.timedrebeca.objectmodel.TimedRebecaCode;
import org.rebecalang.compiler.utils.AccessModifierUtilities;
import org.rebecalang.compiler.utils.CodeCompilationException;
import org.rebecalang.compiler.utils.CoreVersion;

public class TimedRebecaSymbolTableInitializer extends SymbolTableInitializer {
    public TimedRebecaSymbolTableInitializer(AbstractTypeSystem typeSystem) {
        super(typeSystem);
    }

    public void fillSymbolTable(RebecaModel rebecaModel, CoreVersion coreVersion) {
        super.fillSymbolTable(rebecaModel, coreVersion);
        addMailboxDeclarationsToSymbolTableInInitialization((TimedRebecaCode) rebecaModel.getRebecaCode());
        addNetworkDeclarationsToSymbolTableInInitialization((TimedRebecaCode) rebecaModel.getRebecaCode());
    }

    protected void addMailboxDeclarationsToSymbolTableInInitialization(TimedRebecaCode rebecaCode) {
        for (MailboxDeclaration mailboxDeclaration : rebecaCode.getMailboxDeclaration()) {
//            checkMailboxDeclarationIsNotEmpty(mailboxDeclaration);
            //TODO: Make sure that no orders defined on unknown senders
            try {
                Type type = typeSystem.getType(mailboxDeclaration.getName());
                addFields(type, mailboxDeclaration.getKnownSenders(), AccessModifierUtilities.PRIVATE);
                //TODO: Do we need to add each order specification to symbolTable ?
            } catch (CodeCompilationException e) {
                e.printStackTrace();
            }
        }
    }

    protected void addNetworkDeclarationsToSymbolTableInInitialization(TimedRebecaCode rebecaCode) {
        for (NetworkDeclaration networkDeclaration : rebecaCode.getNetworkDeclaration()) {
            //TODO: Do we need to check delays and losses are defined on knownNodes ?
            try {
                Type type = typeSystem.getType(networkDeclaration.getName());
                addFields(type, networkDeclaration.getKnownNodes(), AccessModifierUtilities.PRIVATE);
                //TODO: Do we need to add each delays or losses specification to symbolTable ?
            } catch (CodeCompilationException e) {
                e.printStackTrace();
            }
        }
    }

    private void checkMailboxDeclarationIsNotEmpty(MailboxDeclaration mailboxDeclaration) {
        if (mailboxDeclaration.getOrders().isEmpty() || Objects.isNull(mailboxDeclaration.getOrders())) {
            CodeCompilationException rce = new CodeCompilationException(
                "Mailbox " + mailboxDeclaration.getName() + " should not be declared without any order specifications",
                mailboxDeclaration.getLineNumber(),
                mailboxDeclaration.getCharacter());
            //TODO: Exceptions are unique based on line&column, So if an error occurred in the same line and column in
            // the TypeSystem, then this error doesn't enter in the container
            exceptionContainer.addException(rce);
        }
    }
}

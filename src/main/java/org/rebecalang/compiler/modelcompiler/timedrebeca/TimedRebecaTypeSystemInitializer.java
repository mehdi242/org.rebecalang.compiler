package org.rebecalang.compiler.modelcompiler.timedrebeca;

import org.rebecalang.compiler.modelcompiler.abstractrebeca.AbstractTypeSystem;
import org.rebecalang.compiler.modelcompiler.abstractrebeca.TypeSystemInitializer;
import org.rebecalang.compiler.modelcompiler.corerebeca.objectmodel.RebecaModel;
import org.rebecalang.compiler.modelcompiler.timedrebeca.objectmodel.MailboxDeclaration;
import org.rebecalang.compiler.modelcompiler.timedrebeca.objectmodel.NetworkDeclaration;
import org.rebecalang.compiler.modelcompiler.timedrebeca.objectmodel.TimedRebecaCode;
import org.rebecalang.compiler.utils.CodeCompilationException;
import org.springframework.beans.factory.annotation.Autowired;

public class TimedRebecaTypeSystemInitializer extends TypeSystemInitializer {

    @Autowired
    public TimedRebecaTypeSystemInitializer(AbstractTypeSystem typeSystem) {
        super(typeSystem);
    }

    protected void fillTypeSystem(RebecaModel rebecaModel) {
        super.fillTypeSystem(rebecaModel);
        addMailboxDeclarationsToTypeSystem((TimedRebecaCode) rebecaModel.getRebecaCode());
        addNetworkDeclarationsToTypeSystem((TimedRebecaCode) rebecaModel.getRebecaCode());
    }

    private void addMailboxDeclarationsToTypeSystem(TimedRebecaCode rebecaCode) {
        for (MailboxDeclaration mailboxDeclaration : rebecaCode.getMailboxDeclaration()) {
            if (typeSystem.hasType(mailboxDeclaration.getName())) {
                CodeCompilationException rce = new CodeCompilationException(
                        "Multiple declaration of " + mailboxDeclaration.getName(),
                        mailboxDeclaration.getLineNumber(),
                        mailboxDeclaration.getCharacter()
                );
                exceptionContainer.addException(rce);
            } else {
                ((TimedRebecaTypeSystem) typeSystem).addMailboxClassType(mailboxDeclaration);
            }
        }
    }

    private void addNetworkDeclarationsToTypeSystem(TimedRebecaCode rebecaCode) {
        for (NetworkDeclaration networkDeclaration : rebecaCode.getNetworkDeclaration()) {
            if (typeSystem.hasType(networkDeclaration.getName())) {
                CodeCompilationException rce = new CodeCompilationException(
                        "Multiple declaration of " + networkDeclaration.getName(),
                        networkDeclaration.getLineNumber(),
                        networkDeclaration.getCharacter()
                );
                exceptionContainer.addException(rce);
            } else {
                ((TimedRebecaTypeSystem) typeSystem).addNetworkClassType(networkDeclaration);
            }
        }
    }
}

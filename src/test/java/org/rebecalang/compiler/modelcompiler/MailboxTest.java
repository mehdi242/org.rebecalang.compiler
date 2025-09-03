package org.rebecalang.compiler.modelcompiler;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.rebecalang.compiler.CompilerConfig;
import org.rebecalang.compiler.modelcompiler.corerebeca.objectmodel.RebecaModel;
import org.rebecalang.compiler.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

@ContextConfiguration(classes = CompilerConfig.class)
@SpringJUnitConfig
public class MailboxTest {
    public static final String MODEL_FILES_BASE = "src/test/resources/org/rebecalang/compiler/modelcompiler/mailbox/";
    @Autowired
    public RebecaModelCompiler compiler;
    @Autowired
    public ExceptionContainer exceptionContainer;

    @Test
    public void GIVEN_CorrectMailbox_WEHN_Compiling_THEN_NoError() {
        File model = new File(MODEL_FILES_BASE + "TimedRebecaMailbox.rebeca");
        Set<CompilerExtension> extension = new HashSet<CompilerExtension>();
        extension.add(CompilerExtension.TIMED_REBECA);

        Pair<RebecaModel, SymbolTable> result = compiler.compileRebecaFile(model, extension, CoreVersion.CORE_2_1);

        exceptionContainer.print(System.out);
        Assertions.assertTrue(exceptionContainer.exceptionsIsEmpty());
    }

    @Test
    public void GIVEN_TimedRebecaMailboxWithError_WHEN_Compiling_Then_getErrors() {
        File model = new File(MODEL_FILES_BASE + "TimedRebecaMailboxWithErrors.rebeca");
        Set<CompilerExtension> extension = new HashSet<CompilerExtension>();
        extension.add(CompilerExtension.TIMED_REBECA);

        compiler.compileRebecaFile(model, extension, CoreVersion.CORE_2_1);

        exceptionContainer.print(System.out);

        ExceptionContainer expectedExceptionContainer = new ExceptionContainer();
        expectedExceptionContainer.setCorrespondingResource(model);
        expectedExceptionContainer.addException(new ScopeException("\"bb\" undeclared", 8, 18));
        expectedExceptionContainer.addException(new ScopeException("\"foo\" undeclared", 9, 29));
        expectedExceptionContainer.addException(new CodeCompilationException("Multiple declaration of BMailbox", 13, 8));
        expectedExceptionContainer.addException(new CodeCompilationException("No Mailboxes were instantiated with name 'BMailbox'", 61, 5));
        expectedExceptionContainer.addException(new CodeCompilationException("The BMailbox knownsenders type binding of (A, A) is not applicable for the arguments ()", 62, 10));
        expectedExceptionContainer.addException(new CodeCompilationException("Multiple definition for the mailbox bmailbox", 63, 10));
        Assertions.assertEquals(exceptionContainer, expectedExceptionContainer);
    }

    @Test
    public void GIVEN_TicketServiceEmptyMailboxAndDuplicateMsgSrvName_WHEN_Compiling_THEN_noError() {
        File model = new File(MODEL_FILES_BASE + "TicketServiceMailbox.rebeca");
        Set<CompilerExtension> extension = new HashSet<CompilerExtension>();
        extension.add(CompilerExtension.TIMED_REBECA);

        compiler.compileRebecaFile(model, extension, CoreVersion.CORE_2_1);

        exceptionContainer.print(System.out);
        Assertions.assertTrue(exceptionContainer.exceptionsIsEmpty());
    }
}

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
public class NetworkTest {
    public static final String MODEL_FILES_BASE = "src/test/resources/org/rebecalang/compiler/modelcompiler/network/";
    @Autowired
    public RebecaModelCompiler compiler;
    @Autowired
    public ExceptionContainer exceptionContainer;

    @Test
    public void GIVEN_CorrectNetwork_WEHN_Compiling_THEN_NoError() {
        File model = new File(MODEL_FILES_BASE + "Network.rebeca");
        Set<CompilerExtension> extension = new HashSet<CompilerExtension>();
        extension.add(CompilerExtension.TIMED_REBECA);

        Pair<RebecaModel, SymbolTable> result = compiler.compileRebecaFile(model, extension, CoreVersion.CORE_2_1);

        exceptionContainer.print(System.out);
        Assertions.assertTrue(exceptionContainer.exceptionsIsEmpty());
    }

    @Test
    public void GIVEN_IncorrectNetwork_WEHN_Compiling_THEN_Error() {
        File model = new File(MODEL_FILES_BASE + "NetworkError.rebeca");
        Set<CompilerExtension> extension = new HashSet<CompilerExtension>();
        extension.add(CompilerExtension.TIMED_REBECA);

        Pair<RebecaModel, SymbolTable> result = compiler.compileRebecaFile(model, extension, CoreVersion.CORE_2_1);

        exceptionContainer.print(System.out);

        ExceptionContainer expectedExceptionContainer = new ExceptionContainer();
        expectedExceptionContainer.setCorrespondingResource(model);
        expectedExceptionContainer.addException(new CodeCompilationException("Only convertible int values are permitted here", 62, 23));
        expectedExceptionContainer.addException(new CodeCompilationException("Delay amount cannot be less than 0", 63, 24));
        expectedExceptionContainer.addException(new CodeCompilationException("Only convertible int values are permitted here", 64, 24));
        expectedExceptionContainer.addException(new CodeCompilationException("The MyNetwork knownnodes type binding of (A, B) is not applicable for the arguments ()", 78, 14));
        expectedExceptionContainer.addException(new CodeCompilationException("The MyNetwork knownnodes type binding of (A, B) is not applicable for the arguments (B, A)", 79, 14));
        expectedExceptionContainer.addException(new CodeCompilationException("No Mailboxes were instantiated with name 'noBox'", 79, 27));

        Assertions.assertEquals(exceptionContainer, expectedExceptionContainer);
    }
}

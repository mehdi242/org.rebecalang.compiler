package org.rebecalang.compiler.modelcompiler.timedrebeca;

import lombok.Getter;
import org.rebecalang.compiler.modelcompiler.corerebeca.CoreRebecaTypeSystem;
import org.rebecalang.compiler.modelcompiler.corerebeca.objectmodel.OrdinaryPrimitiveType;
import org.rebecalang.compiler.modelcompiler.corerebeca.objectmodel.Type;
import org.rebecalang.compiler.modelcompiler.timedrebeca.objectmodel.MailboxDeclaration;
import org.rebecalang.compiler.modelcompiler.timedrebeca.objectmodel.NetworkDeclaration;
import org.rebecalang.compiler.utils.CodeCompilationException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Qualifier("TIMED_REBECA")
public class TimedRebecaTypeSystem extends CoreRebecaTypeSystem {

    @Getter
    public final static OrdinaryPrimitiveType TIMER_TYPE;

    @Getter
    public final static OrdinaryPrimitiveType MAILBOX_TYPE;
    @Getter
    public final static OrdinaryPrimitiveType NETWORK_TYPE;

    protected Map<Type, MailboxDeclaration> mailboxDeclarationsMetaData;
    protected Map<Type, NetworkDeclaration> networkDeclarationsMetaData;

    static {
        TIMER_TYPE = new OrdinaryPrimitiveType();
        TIMER_TYPE.setName("Timer");
        MAILBOX_TYPE = new OrdinaryPrimitiveType();
        MAILBOX_TYPE.setName("Mailbox");
        NETWORK_TYPE = new OrdinaryPrimitiveType();
        NETWORK_TYPE.setName("Network");
    }

    @Override
    protected void initializeTypeSystem() {
        super.initializeTypeSystem();

        mailboxDeclarationsMetaData = new HashMap<>();
        networkDeclarationsMetaData = new HashMap<>();

        addNewType(TIMER_TYPE);
        addNewType(MAILBOX_TYPE);
        addNewType(NETWORK_TYPE);
    }

    public Type addMailboxClassType(MailboxDeclaration mbd) {
        OrdinaryPrimitiveType type = new OrdinaryPrimitiveType();
        type.setName(mbd.getName());
        type.setLineNumber(mbd.getLineNumber());
        type.setCharacter(mbd.getCharacter());
        addNewType(type);
        mailboxDeclarationsMetaData.put(type, mbd);
        try {
            addTypeCompatibility(getType(mbd.getName()), MAILBOX_TYPE);
        } catch (CodeCompilationException e) {
            e.printStackTrace();
        }
        return type;
    }
    public Type addNetworkClassType(NetworkDeclaration nd) {
        OrdinaryPrimitiveType type = new OrdinaryPrimitiveType();
        type.setName(nd.getName());
        type.setLineNumber(nd.getLineNumber());
        type.setCharacter(nd.getCharacter());
        addNewType(type);
        networkDeclarationsMetaData.put(type, nd);
        try {
            addTypeCompatibility(getType(nd.getName()), NETWORK_TYPE);
        } catch (CodeCompilationException e) {
            e.printStackTrace();
        }
        return type;
    }
}

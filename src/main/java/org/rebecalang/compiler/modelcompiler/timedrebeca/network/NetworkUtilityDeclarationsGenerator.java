package org.rebecalang.compiler.modelcompiler.timedrebeca.network;

import org.rebecalang.compiler.modelcompiler.corerebeca.objectmodel.*;

import java.util.ArrayList;
import java.util.List;

import static org.rebecalang.compiler.modelcompiler.timedrebeca.network.NetworkNameUtility.*;

public final class NetworkUtilityDeclarationsGenerator {
    public void modifyRebecaModelToSupportNetwork(RebecaModel rebecaModel) {
        List<ReactiveClassDeclaration> wiredReactiveDeclarations = new ArrayList<>();
        List<InterfaceDeclaration> networkInterfaceDeclarations = new ArrayList<>();
        for (ReactiveClassDeclaration rd : rebecaModel.getRebecaCode().getReactiveClassDeclaration()) {
            if (rd.isAbstract())
                continue;
            ReactiveClassDeclaration wiredReactiveDecl = createWiredReactiveClassOf(rd);
            InterfaceDeclaration networkInterface = createNetworkInterfaceFor(rd);
            OrdinaryPrimitiveType type = new OrdinaryPrimitiveType();
            type.setName(networkInterface.getName());
            wiredReactiveDecl.getImplements().add(type);

            wiredReactiveDeclarations.add(wiredReactiveDecl);
            networkInterfaceDeclarations.add(networkInterface);
        }
        rebecaModel.getRebecaCode().getInterfaceDeclaration().addAll(networkInterfaceDeclarations);
        rebecaModel.getRebecaCode().getReactiveClassDeclaration().addAll(wiredReactiveDeclarations);
    }

    private ReactiveClassDeclaration createWiredReactiveClassOf(ReactiveClassDeclaration originalReactiveDecl) {
        ReactiveClassDeclaration wiredReactiveClassDecl = new ReactiveClassDeclaration();
        wiredReactiveClassDecl.setName(generateWiredReactiveClassName(originalReactiveDecl.getName()));
        wiredReactiveClassDecl.setQueueSize(originalReactiveDecl.getQueueSize());
        List<MsgsrvDeclaration> msgSrvDeclarations = generateAbstractMsgSrvDeclarationsFor(originalReactiveDecl);
        msgSrvDeclarations.forEach(msgSrv -> msgSrv.setBlock(new BlockStatement()));
        wiredReactiveClassDecl.getMsgsrvs().addAll(msgSrvDeclarations);
        wiredReactiveClassDecl.setLineNumber(0);
        wiredReactiveClassDecl.setCharacter(0);
        return wiredReactiveClassDecl;
    }

    private InterfaceDeclaration createNetworkInterfaceFor(ReactiveClassDeclaration originalReactiveDecl) {
        InterfaceDeclaration netInterface = new InterfaceDeclaration();
        netInterface.setName(generateReactiveNetworkInterfaceName(originalReactiveDecl.getName()));
        List<MsgsrvDeclaration> msgSrvDeclarations = generateAbstractMsgSrvDeclarationsFor(originalReactiveDecl);
        msgSrvDeclarations.forEach(msgSrv -> msgSrv.setAbstract(true));
        netInterface.getMsgsrvs().addAll(msgSrvDeclarations);
        netInterface.setLineNumber(0);
        netInterface.setCharacter(0);
        return netInterface;
    }

    private List<MsgsrvDeclaration> generateAbstractMsgSrvDeclarationsFor(ReactiveClassDeclaration rd) {
        List<MsgsrvDeclaration> abstractMsgServers = new ArrayList<>();
        for (MsgsrvDeclaration msgSrv : rd.getMsgsrvs()) {
            MsgsrvDeclaration msgsrvDeclaration = new MsgsrvDeclaration();
            msgsrvDeclaration.setName(generateMsgSrvName(rd.getName(), msgSrv.getName()));

            FormalParameterDeclaration receiver = new FormalParameterDeclaration();
            receiver.setName(generateReceiverParameterName(rd.getName()));
            OrdinaryPrimitiveType selfType = new OrdinaryPrimitiveType();
            selfType.setName(rd.getName());
            receiver.setType(selfType);
            receiver.setLineNumber(0);
            receiver.setCharacter(0);
            msgsrvDeclaration.getFormalParameters().add(receiver);

            msgsrvDeclaration.getFormalParameters().addAll(msgSrv.getFormalParameters());
            msgsrvDeclaration.setAccessModifier(msgSrv.getAccessModifier());
            msgsrvDeclaration.setLineNumber(0);
            msgsrvDeclaration.setCharacter(0);
            abstractMsgServers.add(msgsrvDeclaration);
        }
        return abstractMsgServers;
    }
}

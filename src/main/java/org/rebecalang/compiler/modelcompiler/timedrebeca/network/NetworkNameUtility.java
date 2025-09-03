package org.rebecalang.compiler.modelcompiler.timedrebeca.network;

public class NetworkNameUtility {
    public static String generateWiredReactiveClassName(String originalReactiveClassName) {
        return "Wired" + originalReactiveClassName;
    }

    public static String generateReactiveNetworkInterfaceName(String originalReactiveClassName) {
        return "I" + originalReactiveClassName + "Network";
    }

    public static String generateMsgSrvName(String reactiveClassName, String msgSrvName) {
        return "_" + reactiveClassName.toLowerCase() + "_" + msgSrvName;
    }

    public static String generateReceiverParameterName(String originalReactiveClassName) {
        return "_self_" + originalReactiveClassName.toLowerCase();
    }
}

package me.lucko.spark.lib.bytebuddy.agent;

import java.lang.instrument.Instrumentation;

public class Installer {

    private static volatile Instrumentation instrumentation;

    private Installer() {
        throw new UnsupportedOperationException("This class is a utility class and not supposed to be instantiated");
    }

    public static Instrumentation getInstrumentation() {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(new RuntimePermission("getInstrumentation"));
        }
        Instrumentation instrumentation = Installer.instrumentation;
        if (instrumentation == null) {
            throw new IllegalStateException("The Byte Buddy agent is not loaded or this method is not called via the system class loader");
        } else {
            return instrumentation;
        }
    }

    public static void premain(String arguments, Instrumentation instrumentation) {
        Installer.instrumentation = instrumentation;
    }

    public static void agentmain(String arguments, Instrumentation instrumentation) {
        Installer.instrumentation = instrumentation;
    }
}
package me.lucko.spark.lib.bytebuddy.agent;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.lang.reflect.InvocationTargetException;

public class Attacher {

    private static final Object STATIC_MEMBER = null;

    private static final String ATTACH_METHOD_NAME = "attach";

    private static final String LOAD_AGENT_METHOD_NAME = "loadAgent";

    private static final String LOAD_AGENT_PATH_METHOD_NAME = "loadAgentPath";

    private static final String DETACH_METHOD_NAME = "detach";

    private Attacher() {
        throw new UnsupportedOperationException("This class is a utility class and not supposed to be instantiated");
    }

    @SuppressFBWarnings(value = { "REC_CATCH_EXCEPTION" }, justification = "Exception should not be rethrown but trigger a fallback")
    public static void main(String[] args) {
        try {
            String argument;
            if (args.length >= 5 && args[4].length() != 0) {
                StringBuilder stringBuilder = new StringBuilder(args[4].substring(1));
                for (int index = 5; index < args.length; index++) {
                    stringBuilder.append(' ').append(args[index]);
                }
                argument = stringBuilder.toString();
            } else {
                argument = null;
            }
            install(Class.forName(args[0]), args[1], args[2], Boolean.parseBoolean(args[3]), argument);
        } catch (Throwable var4) {
            System.exit(1);
        }
    }

    protected static void install(Class<?> virtualMachineType, String processId, String agent, boolean isNative, String argument) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Object virtualMachineInstance = virtualMachineType.getMethod("attach", String.class).invoke(STATIC_MEMBER, processId);
        try {
            virtualMachineType.getMethod(isNative ? "loadAgentPath" : "loadAgent", String.class, String.class).invoke(virtualMachineInstance, agent, argument);
        } finally {
            virtualMachineType.getMethod("detach").invoke(virtualMachineInstance);
        }
    }
}
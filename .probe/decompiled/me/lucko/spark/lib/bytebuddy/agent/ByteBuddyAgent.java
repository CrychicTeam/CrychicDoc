package me.lucko.spark.lib.bytebuddy.agent;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.instrument.Instrumentation;
import java.lang.management.ManagementFactory;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.AccessController;
import java.security.CodeSource;
import java.security.PrivilegedAction;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;
import java.util.jar.Attributes.Name;

public class ByteBuddyAgent {

    public static final String LATENT_RESOLVE = "me.lucko.spark.lib.bytebuddy.agent.latent";

    private static final String AGENT_CLASS_PROPERTY = "Agent-Class";

    private static final String CAN_REDEFINE_CLASSES_PROPERTY = "Can-Redefine-Classes";

    private static final String CAN_RETRANSFORM_CLASSES_PROPERTY = "Can-Retransform-Classes";

    private static final String CAN_SET_NATIVE_METHOD_PREFIX = "Can-Set-Native-Method-Prefix";

    private static final String MANIFEST_VERSION_VALUE = "1.0";

    private static final int BUFFER_SIZE = 1024;

    private static final int START_INDEX = 0;

    private static final int END_OF_FILE = -1;

    private static final int SUCCESSFUL_ATTACH = 0;

    private static final Object STATIC_MEMBER = null;

    private static final ClassLoader BOOTSTRAP_CLASS_LOADER = null;

    private static final String WITHOUT_ARGUMENT = null;

    private static final String ATTACHER_FILE_NAME = "byteBuddyAttacher";

    private static final String CLASS_FILE_EXTENSION = ".class";

    private static final String JAR_FILE_EXTENSION = ".jar";

    private static final String CLASS_PATH_ARGUMENT = "-cp";

    private static final String JAVA_HOME = "java.home";

    private static final String OS_NAME = "os.name";

    private static final String INSTRUMENTATION_METHOD = "getInstrumentation";

    private static final String FILE_PROTOCOL = "file";

    private static final Instrumentation UNAVAILABLE = null;

    private static final File CANNOT_SELF_RESOLVE = null;

    private static final ByteBuddyAgent.AttachmentTypeEvaluator ATTACHMENT_TYPE_EVALUATOR = (ByteBuddyAgent.AttachmentTypeEvaluator) AccessController.doPrivileged(ByteBuddyAgent.AttachmentTypeEvaluator.InstallationAction.INSTANCE);

    private ByteBuddyAgent() {
        throw new UnsupportedOperationException("This class is a utility class and not supposed to be instantiated");
    }

    public static Instrumentation getInstrumentation() {
        Instrumentation instrumentation = doGetInstrumentation();
        if (instrumentation == null) {
            throw new IllegalStateException("The Byte Buddy agent is not initialized");
        } else {
            return instrumentation;
        }
    }

    public static void attach(File agentJar, String processId) {
        attach(agentJar, processId, WITHOUT_ARGUMENT);
    }

    public static void attach(File agentJar, String processId, String argument) {
        attach(agentJar, processId, argument, ByteBuddyAgent.AttachmentProvider.DEFAULT);
    }

    public static void attach(File agentJar, String processId, ByteBuddyAgent.AttachmentProvider attachmentProvider) {
        attach(agentJar, processId, WITHOUT_ARGUMENT, attachmentProvider);
    }

    public static void attach(File agentJar, String processId, String argument, ByteBuddyAgent.AttachmentProvider attachmentProvider) {
        install(attachmentProvider, processId, argument, new ByteBuddyAgent.AgentProvider.ForExistingAgent(agentJar), false);
    }

    public static void attach(File agentJar, ByteBuddyAgent.ProcessProvider processProvider) {
        attach(agentJar, processProvider, WITHOUT_ARGUMENT);
    }

    public static void attach(File agentJar, ByteBuddyAgent.ProcessProvider processProvider, String argument) {
        attach(agentJar, processProvider, argument, ByteBuddyAgent.AttachmentProvider.DEFAULT);
    }

    public static void attach(File agentJar, ByteBuddyAgent.ProcessProvider processProvider, ByteBuddyAgent.AttachmentProvider attachmentProvider) {
        attach(agentJar, processProvider, WITHOUT_ARGUMENT, attachmentProvider);
    }

    public static void attach(File agentJar, ByteBuddyAgent.ProcessProvider processProvider, String argument, ByteBuddyAgent.AttachmentProvider attachmentProvider) {
        install(attachmentProvider, processProvider.resolve(), argument, new ByteBuddyAgent.AgentProvider.ForExistingAgent(agentJar), false);
    }

    public static void attachNative(File agentLibrary, String processId) {
        attachNative(agentLibrary, processId, WITHOUT_ARGUMENT);
    }

    public static void attachNative(File agentLibrary, String processId, String argument) {
        attachNative(agentLibrary, processId, argument, ByteBuddyAgent.AttachmentProvider.DEFAULT);
    }

    public static void attachNative(File agentLibrary, String processId, ByteBuddyAgent.AttachmentProvider attachmentProvider) {
        attachNative(agentLibrary, processId, WITHOUT_ARGUMENT, attachmentProvider);
    }

    public static void attachNative(File agentLibrary, String processId, String argument, ByteBuddyAgent.AttachmentProvider attachmentProvider) {
        install(attachmentProvider, processId, argument, new ByteBuddyAgent.AgentProvider.ForExistingAgent(agentLibrary), true);
    }

    public static void attachNative(File agentLibrary, ByteBuddyAgent.ProcessProvider processProvider) {
        attachNative(agentLibrary, processProvider, WITHOUT_ARGUMENT);
    }

    public static void attachNative(File agentLibrary, ByteBuddyAgent.ProcessProvider processProvider, String argument) {
        attachNative(agentLibrary, processProvider, argument, ByteBuddyAgent.AttachmentProvider.DEFAULT);
    }

    public static void attachNative(File agentLibrary, ByteBuddyAgent.ProcessProvider processProvider, ByteBuddyAgent.AttachmentProvider attachmentProvider) {
        attachNative(agentLibrary, processProvider, WITHOUT_ARGUMENT, attachmentProvider);
    }

    public static void attachNative(File agentLibrary, ByteBuddyAgent.ProcessProvider processProvider, String argument, ByteBuddyAgent.AttachmentProvider attachmentProvider) {
        install(attachmentProvider, processProvider.resolve(), argument, new ByteBuddyAgent.AgentProvider.ForExistingAgent(agentLibrary), true);
    }

    public static Instrumentation install() {
        return install(ByteBuddyAgent.AttachmentProvider.DEFAULT);
    }

    public static Instrumentation install(ByteBuddyAgent.AttachmentProvider attachmentProvider) {
        return install(attachmentProvider, ByteBuddyAgent.ProcessProvider.ForCurrentVm.INSTANCE);
    }

    public static Instrumentation install(ByteBuddyAgent.ProcessProvider processProvider) {
        return install(ByteBuddyAgent.AttachmentProvider.DEFAULT, processProvider);
    }

    public static synchronized Instrumentation install(ByteBuddyAgent.AttachmentProvider attachmentProvider, ByteBuddyAgent.ProcessProvider processProvider) {
        Instrumentation instrumentation = doGetInstrumentation();
        if (instrumentation != null) {
            return instrumentation;
        } else {
            install(attachmentProvider, processProvider.resolve(), WITHOUT_ARGUMENT, ByteBuddyAgent.AgentProvider.ForByteBuddyAgent.INSTANCE, false);
            return doGetInstrumentation();
        }
    }

    private static void install(ByteBuddyAgent.AttachmentProvider attachmentProvider, String processId, String argument, ByteBuddyAgent.AgentProvider agentProvider, boolean isNative) {
        ByteBuddyAgent.AttachmentProvider.Accessor attachmentAccessor = attachmentProvider.attempt();
        if (!attachmentAccessor.isAvailable()) {
            throw new IllegalStateException("No compatible attachment provider is available");
        } else {
            try {
                if (attachmentAccessor.isExternalAttachmentRequired() && ATTACHMENT_TYPE_EVALUATOR.requiresExternalAttachment(processId)) {
                    installExternal(attachmentAccessor.getExternalAttachment(), processId, agentProvider.resolve(), isNative, argument);
                } else {
                    Attacher.install(attachmentAccessor.getVirtualMachineType(), processId, agentProvider.resolve().getAbsolutePath(), isNative, argument);
                }
            } catch (RuntimeException var7) {
                throw var7;
            } catch (Exception var8) {
                throw new IllegalStateException("Error during attachment using: " + attachmentProvider, var8);
            }
        }
    }

    private static void installExternal(ByteBuddyAgent.AttachmentProvider.Accessor.ExternalAttachment externalAttachment, String processId, File agent, boolean isNative, String argument) throws Exception {
        File selfResolvedJar = trySelfResolve();
        File attachmentJar = null;
        try {
            if (selfResolvedJar == null) {
                InputStream inputStream = Attacher.class.getResourceAsStream('/' + Attacher.class.getName().replace('.', '/') + ".class");
                if (inputStream == null) {
                    throw new IllegalStateException("Cannot locate class file for Byte Buddy installation process");
                }
                try {
                    attachmentJar = File.createTempFile("byteBuddyAttacher", ".jar");
                    JarOutputStream jarOutputStream = new JarOutputStream(new FileOutputStream(attachmentJar));
                    try {
                        jarOutputStream.putNextEntry(new JarEntry(Attacher.class.getName().replace('.', '/') + ".class"));
                        byte[] buffer = new byte[1024];
                        int index;
                        while ((index = inputStream.read(buffer)) != -1) {
                            jarOutputStream.write(buffer, 0, index);
                        }
                        jarOutputStream.closeEntry();
                    } finally {
                        jarOutputStream.close();
                    }
                } finally {
                    inputStream.close();
                }
            }
            StringBuilder classPath = new StringBuilder().append(quote((selfResolvedJar == null ? attachmentJar : selfResolvedJar).getCanonicalPath()));
            for (File jar : externalAttachment.getClassPath()) {
                classPath.append(File.pathSeparatorChar).append(quote(jar.getCanonicalPath()));
            }
            if (new ProcessBuilder(new String[] { System.getProperty("java.home") + File.separatorChar + "bin" + File.separatorChar + (System.getProperty("os.name", "").toLowerCase(Locale.US).contains("windows") ? "java.exe" : "java"), "-cp", classPath.toString(), Attacher.class.getName(), externalAttachment.getVirtualMachineType(), processId, quote(agent.getAbsolutePath()), Boolean.toString(isNative), argument == null ? "" : "=" + argument }).start().waitFor() != 0) {
                throw new IllegalStateException("Could not self-attach to current VM using external process");
            }
        } finally {
            if (attachmentJar != null && !attachmentJar.delete()) {
                attachmentJar.deleteOnExit();
            }
        }
    }

    @SuppressFBWarnings(value = { "REC_CATCH_EXCEPTION" }, justification = "Exception should not be rethrown but trigger a fallback")
    private static File trySelfResolve() {
        try {
            if (Boolean.getBoolean("me.lucko.spark.lib.bytebuddy.agent.latent")) {
                return CANNOT_SELF_RESOLVE;
            } else {
                ProtectionDomain protectionDomain = Attacher.class.getProtectionDomain();
                if (protectionDomain == null) {
                    return CANNOT_SELF_RESOLVE;
                } else {
                    CodeSource codeSource = protectionDomain.getCodeSource();
                    if (codeSource == null) {
                        return CANNOT_SELF_RESOLVE;
                    } else {
                        URL location = codeSource.getLocation();
                        if (!location.getProtocol().equals("file")) {
                            return CANNOT_SELF_RESOLVE;
                        } else {
                            try {
                                return new File(location.toURI());
                            } catch (URISyntaxException var4) {
                                return new File(location.getPath());
                            }
                        }
                    }
                }
            }
        } catch (Exception var5) {
            return CANNOT_SELF_RESOLVE;
        }
    }

    private static String quote(String value) {
        return value.contains(" ") ? '"' + value + '"' : value;
    }

    @SuppressFBWarnings(value = { "REC_CATCH_EXCEPTION" }, justification = "Legal outcome where reflection communicates errors by throwing an exception")
    private static Instrumentation doGetInstrumentation() {
        try {
            return (Instrumentation) ClassLoader.getSystemClassLoader().loadClass(Installer.class.getName()).getMethod("getInstrumentation").invoke(STATIC_MEMBER);
        } catch (Exception var1) {
            return UNAVAILABLE;
        }
    }

    protected interface AgentProvider {

        File resolve() throws IOException;

        public static enum ForByteBuddyAgent implements ByteBuddyAgent.AgentProvider {

            INSTANCE;

            private static final String AGENT_FILE_NAME = "byteBuddyAgent";

            private static File trySelfResolve() throws IOException {
                ProtectionDomain protectionDomain = Installer.class.getProtectionDomain();
                if (Boolean.getBoolean("me.lucko.spark.lib.bytebuddy.agent.latent")) {
                    return ByteBuddyAgent.CANNOT_SELF_RESOLVE;
                } else if (protectionDomain == null) {
                    return ByteBuddyAgent.CANNOT_SELF_RESOLVE;
                } else {
                    CodeSource codeSource = protectionDomain.getCodeSource();
                    if (codeSource == null) {
                        return ByteBuddyAgent.CANNOT_SELF_RESOLVE;
                    } else {
                        URL location = codeSource.getLocation();
                        if (!location.getProtocol().equals("file")) {
                            return ByteBuddyAgent.CANNOT_SELF_RESOLVE;
                        } else {
                            File agentJar;
                            try {
                                agentJar = new File(location.toURI());
                            } catch (URISyntaxException var11) {
                                agentJar = new File(location.getPath());
                            }
                            if (agentJar.isFile() && agentJar.canRead()) {
                                JarInputStream jarInputStream = new JarInputStream(new FileInputStream(agentJar));
                                File var7;
                                try {
                                    Manifest manifest = jarInputStream.getManifest();
                                    if (manifest == null) {
                                        return ByteBuddyAgent.CANNOT_SELF_RESOLVE;
                                    }
                                    Attributes attributes = manifest.getMainAttributes();
                                    if (attributes == null) {
                                        return ByteBuddyAgent.CANNOT_SELF_RESOLVE;
                                    }
                                    if (!Installer.class.getName().equals(attributes.getValue("Agent-Class")) || !Boolean.parseBoolean(attributes.getValue("Can-Redefine-Classes")) || !Boolean.parseBoolean(attributes.getValue("Can-Retransform-Classes")) || !Boolean.parseBoolean(attributes.getValue("Can-Set-Native-Method-Prefix"))) {
                                        return ByteBuddyAgent.CANNOT_SELF_RESOLVE;
                                    }
                                    var7 = agentJar;
                                } finally {
                                    jarInputStream.close();
                                }
                                return var7;
                            } else {
                                return ByteBuddyAgent.CANNOT_SELF_RESOLVE;
                            }
                        }
                    }
                }
            }

            private static File createJarFile() throws IOException {
                InputStream inputStream = Installer.class.getResourceAsStream('/' + Installer.class.getName().replace('.', '/') + ".class");
                if (inputStream == null) {
                    throw new IllegalStateException("Cannot locate class file for Byte Buddy installer");
                } else {
                    File var14;
                    try {
                        File agentJar = File.createTempFile("byteBuddyAgent", ".jar");
                        agentJar.deleteOnExit();
                        Manifest manifest = new Manifest();
                        manifest.getMainAttributes().put(Name.MANIFEST_VERSION, "1.0");
                        manifest.getMainAttributes().put(new Name("Agent-Class"), Installer.class.getName());
                        manifest.getMainAttributes().put(new Name("Can-Redefine-Classes"), Boolean.TRUE.toString());
                        manifest.getMainAttributes().put(new Name("Can-Retransform-Classes"), Boolean.TRUE.toString());
                        manifest.getMainAttributes().put(new Name("Can-Set-Native-Method-Prefix"), Boolean.TRUE.toString());
                        JarOutputStream jarOutputStream = new JarOutputStream(new FileOutputStream(agentJar), manifest);
                        try {
                            jarOutputStream.putNextEntry(new JarEntry(Installer.class.getName().replace('.', '/') + ".class"));
                            byte[] buffer = new byte[1024];
                            int index;
                            while ((index = inputStream.read(buffer)) != -1) {
                                jarOutputStream.write(buffer, 0, index);
                            }
                            jarOutputStream.closeEntry();
                        } finally {
                            jarOutputStream.close();
                        }
                        var14 = agentJar;
                    } finally {
                        inputStream.close();
                    }
                    return var14;
                }
            }

            @Override
            public File resolve() throws IOException {
                try {
                    File agentJar = trySelfResolve();
                    return agentJar == null ? createJarFile() : agentJar;
                } catch (Exception var2) {
                    return createJarFile();
                }
            }
        }

        public static class ForExistingAgent implements ByteBuddyAgent.AgentProvider {

            private File agent;

            protected ForExistingAgent(File agent) {
                this.agent = agent;
            }

            @Override
            public File resolve() {
                return this.agent;
            }
        }
    }

    @SuppressFBWarnings(value = { "IC_SUPERCLASS_USES_SUBCLASS_DURING_INITIALIZATION" }, justification = "Safe initialization is implied")
    public interface AttachmentProvider {

        ByteBuddyAgent.AttachmentProvider DEFAULT = new ByteBuddyAgent.AttachmentProvider.Compound(ByteBuddyAgent.AttachmentProvider.ForModularizedVm.INSTANCE, ByteBuddyAgent.AttachmentProvider.ForJ9Vm.INSTANCE, ByteBuddyAgent.AttachmentProvider.ForStandardToolsJarVm.JVM_ROOT, ByteBuddyAgent.AttachmentProvider.ForStandardToolsJarVm.JDK_ROOT, ByteBuddyAgent.AttachmentProvider.ForStandardToolsJarVm.MACINTOSH, ByteBuddyAgent.AttachmentProvider.ForUserDefinedToolsJar.INSTANCE, ByteBuddyAgent.AttachmentProvider.ForEmulatedAttachment.INSTANCE);

        ByteBuddyAgent.AttachmentProvider.Accessor attempt();

        public interface Accessor {

            String VIRTUAL_MACHINE_TYPE_NAME = "com.sun.tools.attach.VirtualMachine";

            String VIRTUAL_MACHINE_TYPE_NAME_J9 = "com.ibm.tools.attach.VirtualMachine";

            boolean isAvailable();

            boolean isExternalAttachmentRequired();

            Class<?> getVirtualMachineType();

            ByteBuddyAgent.AttachmentProvider.Accessor.ExternalAttachment getExternalAttachment();

            public static class ExternalAttachment {

                private final String virtualMachineType;

                private final List<File> classPath;

                public ExternalAttachment(String virtualMachineType, List<File> classPath) {
                    this.virtualMachineType = virtualMachineType;
                    this.classPath = classPath;
                }

                public String getVirtualMachineType() {
                    return this.virtualMachineType;
                }

                public List<File> getClassPath() {
                    return this.classPath;
                }
            }

            public abstract static class Simple implements ByteBuddyAgent.AttachmentProvider.Accessor {

                protected final Class<?> virtualMachineType;

                protected Simple(Class<?> virtualMachineType) {
                    this.virtualMachineType = virtualMachineType;
                }

                public static ByteBuddyAgent.AttachmentProvider.Accessor of(ClassLoader classLoader, File... classPath) {
                    try {
                        return new ByteBuddyAgent.AttachmentProvider.Accessor.Simple.WithExternalAttachment(classLoader.loadClass("com.sun.tools.attach.VirtualMachine"), Arrays.asList(classPath));
                    } catch (ClassNotFoundException var3) {
                        return ByteBuddyAgent.AttachmentProvider.Accessor.Unavailable.INSTANCE;
                    }
                }

                public static ByteBuddyAgent.AttachmentProvider.Accessor ofJ9() {
                    try {
                        return new ByteBuddyAgent.AttachmentProvider.Accessor.Simple.WithExternalAttachment(ClassLoader.getSystemClassLoader().loadClass("com.ibm.tools.attach.VirtualMachine"), Collections.emptyList());
                    } catch (ClassNotFoundException var1) {
                        return ByteBuddyAgent.AttachmentProvider.Accessor.Unavailable.INSTANCE;
                    }
                }

                @Override
                public boolean isAvailable() {
                    return true;
                }

                @Override
                public Class<?> getVirtualMachineType() {
                    return this.virtualMachineType;
                }

                protected static class WithDirectAttachment extends ByteBuddyAgent.AttachmentProvider.Accessor.Simple {

                    public WithDirectAttachment(Class<?> virtualMachineType) {
                        super(virtualMachineType);
                    }

                    @Override
                    public boolean isExternalAttachmentRequired() {
                        return false;
                    }

                    @Override
                    public ByteBuddyAgent.AttachmentProvider.Accessor.ExternalAttachment getExternalAttachment() {
                        throw new IllegalStateException("Cannot apply external attachment");
                    }
                }

                protected static class WithExternalAttachment extends ByteBuddyAgent.AttachmentProvider.Accessor.Simple {

                    private final List<File> classPath;

                    public WithExternalAttachment(Class<?> virtualMachineType, List<File> classPath) {
                        super(virtualMachineType);
                        this.classPath = classPath;
                    }

                    @Override
                    public boolean isExternalAttachmentRequired() {
                        return true;
                    }

                    @Override
                    public ByteBuddyAgent.AttachmentProvider.Accessor.ExternalAttachment getExternalAttachment() {
                        return new ByteBuddyAgent.AttachmentProvider.Accessor.ExternalAttachment(this.virtualMachineType.getName(), this.classPath);
                    }
                }
            }

            public static enum Unavailable implements ByteBuddyAgent.AttachmentProvider.Accessor {

                INSTANCE;

                @Override
                public boolean isAvailable() {
                    return false;
                }

                @Override
                public boolean isExternalAttachmentRequired() {
                    throw new IllegalStateException("Cannot read the virtual machine type for an unavailable accessor");
                }

                @Override
                public Class<?> getVirtualMachineType() {
                    throw new IllegalStateException("Cannot read the virtual machine type for an unavailable accessor");
                }

                @Override
                public ByteBuddyAgent.AttachmentProvider.Accessor.ExternalAttachment getExternalAttachment() {
                    throw new IllegalStateException("Cannot read the virtual machine type for an unavailable accessor");
                }
            }
        }

        public static class Compound implements ByteBuddyAgent.AttachmentProvider {

            private final List<ByteBuddyAgent.AttachmentProvider> attachmentProviders = new ArrayList();

            public Compound(ByteBuddyAgent.AttachmentProvider... attachmentProvider) {
                this(Arrays.asList(attachmentProvider));
            }

            public Compound(List<? extends ByteBuddyAgent.AttachmentProvider> attachmentProviders) {
                for (ByteBuddyAgent.AttachmentProvider attachmentProvider : attachmentProviders) {
                    if (attachmentProvider instanceof ByteBuddyAgent.AttachmentProvider.Compound) {
                        this.attachmentProviders.addAll(((ByteBuddyAgent.AttachmentProvider.Compound) attachmentProvider).attachmentProviders);
                    } else {
                        this.attachmentProviders.add(attachmentProvider);
                    }
                }
            }

            @Override
            public ByteBuddyAgent.AttachmentProvider.Accessor attempt() {
                for (ByteBuddyAgent.AttachmentProvider attachmentProvider : this.attachmentProviders) {
                    ByteBuddyAgent.AttachmentProvider.Accessor accessor = attachmentProvider.attempt();
                    if (accessor.isAvailable()) {
                        return accessor;
                    }
                }
                return ByteBuddyAgent.AttachmentProvider.Accessor.Unavailable.INSTANCE;
            }
        }

        public static enum ForEmulatedAttachment implements ByteBuddyAgent.AttachmentProvider {

            INSTANCE;

            @Override
            public ByteBuddyAgent.AttachmentProvider.Accessor attempt() {
                try {
                    return new ByteBuddyAgent.AttachmentProvider.Accessor.Simple.WithDirectAttachment((Class<?>) AccessController.doPrivileged(VirtualMachine.Resolver.INSTANCE));
                } catch (Throwable var2) {
                    return ByteBuddyAgent.AttachmentProvider.Accessor.Unavailable.INSTANCE;
                }
            }
        }

        public static enum ForJ9Vm implements ByteBuddyAgent.AttachmentProvider {

            INSTANCE;

            @Override
            public ByteBuddyAgent.AttachmentProvider.Accessor attempt() {
                return ByteBuddyAgent.AttachmentProvider.Accessor.Simple.ofJ9();
            }
        }

        public static enum ForModularizedVm implements ByteBuddyAgent.AttachmentProvider {

            INSTANCE;

            @Override
            public ByteBuddyAgent.AttachmentProvider.Accessor attempt() {
                return ByteBuddyAgent.AttachmentProvider.Accessor.Simple.of(ClassLoader.getSystemClassLoader());
            }
        }

        public static enum ForStandardToolsJarVm implements ByteBuddyAgent.AttachmentProvider {

            JVM_ROOT("../lib/tools.jar"), JDK_ROOT("lib/tools.jar"), MACINTOSH("../Classes/classes.jar");

            private static final String JAVA_HOME_PROPERTY = "java.home";

            private final String toolsJarPath;

            private ForStandardToolsJarVm(String toolsJarPath) {
                this.toolsJarPath = toolsJarPath;
            }

            @SuppressFBWarnings(value = { "DP_CREATE_CLASSLOADER_INSIDE_DO_PRIVILEGED" }, justification = "Privilege is explicit user responsibility")
            @Override
            public ByteBuddyAgent.AttachmentProvider.Accessor attempt() {
                File toolsJar = new File(System.getProperty("java.home"), this.toolsJarPath);
                try {
                    return (ByteBuddyAgent.AttachmentProvider.Accessor) (toolsJar.isFile() && toolsJar.canRead() ? ByteBuddyAgent.AttachmentProvider.Accessor.Simple.of(new URLClassLoader(new URL[] { toolsJar.toURI().toURL() }, ByteBuddyAgent.BOOTSTRAP_CLASS_LOADER), toolsJar) : ByteBuddyAgent.AttachmentProvider.Accessor.Unavailable.INSTANCE);
                } catch (MalformedURLException var3) {
                    throw new IllegalStateException("Could not represent " + toolsJar + " as URL");
                }
            }
        }

        public static enum ForUserDefinedToolsJar implements ByteBuddyAgent.AttachmentProvider {

            INSTANCE;

            public static final String PROPERTY = "me.lucko.spark.lib.bytebuddy.agent.toolsjar";

            @SuppressFBWarnings(value = { "DP_CREATE_CLASSLOADER_INSIDE_DO_PRIVILEGED" }, justification = "Privilege is explicit user responsibility")
            @Override
            public ByteBuddyAgent.AttachmentProvider.Accessor attempt() {
                String location = System.getProperty("me.lucko.spark.lib.bytebuddy.agent.toolsjar");
                if (location == null) {
                    return ByteBuddyAgent.AttachmentProvider.Accessor.Unavailable.INSTANCE;
                } else {
                    File toolsJar = new File(location);
                    try {
                        return ByteBuddyAgent.AttachmentProvider.Accessor.Simple.of(new URLClassLoader(new URL[] { toolsJar.toURI().toURL() }, ByteBuddyAgent.BOOTSTRAP_CLASS_LOADER), toolsJar);
                    } catch (MalformedURLException var4) {
                        throw new IllegalStateException("Could not represent " + toolsJar + " as URL");
                    }
                }
            }
        }
    }

    protected interface AttachmentTypeEvaluator {

        boolean requiresExternalAttachment(String var1);

        public static enum Disabled implements ByteBuddyAgent.AttachmentTypeEvaluator {

            INSTANCE;

            @Override
            public boolean requiresExternalAttachment(String processId) {
                return false;
            }
        }

        public static class ForJava9CapableVm implements ByteBuddyAgent.AttachmentTypeEvaluator {

            private final Method current;

            private final Method pid;

            protected ForJava9CapableVm(Method current, Method pid) {
                this.current = current;
                this.pid = pid;
            }

            @Override
            public boolean requiresExternalAttachment(String processId) {
                try {
                    return this.pid.invoke(this.current.invoke(ByteBuddyAgent.STATIC_MEMBER)).toString().equals(processId);
                } catch (IllegalAccessException var3) {
                    throw new IllegalStateException("Cannot access Java 9 process API", var3);
                } catch (InvocationTargetException var4) {
                    throw new IllegalStateException("Error when accessing Java 9 process API", var4.getCause());
                }
            }
        }

        public static enum InstallationAction implements PrivilegedAction<ByteBuddyAgent.AttachmentTypeEvaluator> {

            INSTANCE;

            private static final String JDK_ALLOW_SELF_ATTACH = "jdk.attach.allowAttachSelf";

            @SuppressFBWarnings(value = { "REC_CATCH_EXCEPTION" }, justification = "Exception should not be rethrown but trigger a fallback")
            public ByteBuddyAgent.AttachmentTypeEvaluator run() {
                try {
                    return (ByteBuddyAgent.AttachmentTypeEvaluator) (Boolean.getBoolean("jdk.attach.allowAttachSelf") ? ByteBuddyAgent.AttachmentTypeEvaluator.Disabled.INSTANCE : new ByteBuddyAgent.AttachmentTypeEvaluator.ForJava9CapableVm(Class.forName("java.lang.ProcessHandle").getMethod("current"), Class.forName("java.lang.ProcessHandle").getMethod("pid")));
                } catch (Exception var2) {
                    return ByteBuddyAgent.AttachmentTypeEvaluator.Disabled.INSTANCE;
                }
            }
        }
    }

    public interface ProcessProvider {

        String resolve();

        public static enum ForCurrentVm implements ByteBuddyAgent.ProcessProvider {

            INSTANCE;

            private final ByteBuddyAgent.ProcessProvider dispatcher = ByteBuddyAgent.ProcessProvider.ForCurrentVm.ForJava9CapableVm.make();

            @Override
            public String resolve() {
                return this.dispatcher.resolve();
            }

            protected static class ForJava9CapableVm implements ByteBuddyAgent.ProcessProvider {

                private final Method current;

                private final Method pid;

                protected ForJava9CapableVm(Method current, Method pid) {
                    this.current = current;
                    this.pid = pid;
                }

                @SuppressFBWarnings(value = { "REC_CATCH_EXCEPTION" }, justification = "Exception should not be rethrown but trigger a fallback")
                public static ByteBuddyAgent.ProcessProvider make() {
                    try {
                        return new ByteBuddyAgent.ProcessProvider.ForCurrentVm.ForJava9CapableVm(Class.forName("java.lang.ProcessHandle").getMethod("current"), Class.forName("java.lang.ProcessHandle").getMethod("pid"));
                    } catch (Exception var1) {
                        return ByteBuddyAgent.ProcessProvider.ForCurrentVm.ForLegacyVm.INSTANCE;
                    }
                }

                @Override
                public String resolve() {
                    try {
                        return this.pid.invoke(this.current.invoke(ByteBuddyAgent.STATIC_MEMBER)).toString();
                    } catch (IllegalAccessException var2) {
                        throw new IllegalStateException("Cannot access Java 9 process API", var2);
                    } catch (InvocationTargetException var3) {
                        throw new IllegalStateException("Error when accessing Java 9 process API", var3.getCause());
                    }
                }
            }

            protected static enum ForLegacyVm implements ByteBuddyAgent.ProcessProvider {

                INSTANCE;

                @Override
                public String resolve() {
                    String runtimeName = ManagementFactory.getRuntimeMXBean().getName();
                    int processIdIndex = runtimeName.indexOf(64);
                    if (processIdIndex == -1) {
                        throw new IllegalStateException("Cannot extract process id from runtime management bean");
                    } else {
                        return runtimeName.substring(0, processIdIndex);
                    }
                }
            }
        }
    }
}
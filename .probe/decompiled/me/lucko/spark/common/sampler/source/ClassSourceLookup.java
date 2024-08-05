package me.lucko.spark.common.sampler.source;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.ArrayDeque;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;
import me.lucko.spark.common.SparkPlatform;
import me.lucko.spark.common.sampler.node.StackTraceNode;
import me.lucko.spark.common.sampler.node.ThreadNode;
import me.lucko.spark.common.util.ClassFinder;
import org.checkerframework.checker.nullness.qual.Nullable;

public interface ClassSourceLookup {

    ClassSourceLookup NO_OP = new ClassSourceLookup() {

        @Nullable
        @Override
        public String identify(Class<?> clazz) {
            return null;
        }
    };

    @Nullable
    String identify(Class<?> var1) throws Exception;

    @Nullable
    default String identify(ClassSourceLookup.MethodCall methodCall) throws Exception {
        return null;
    }

    @Nullable
    default String identify(ClassSourceLookup.MethodCallByLine methodCall) throws Exception {
        return null;
    }

    static ClassSourceLookup create(SparkPlatform platform) {
        try {
            return platform.createClassSourceLookup();
        } catch (Exception var2) {
            var2.printStackTrace();
            return NO_OP;
        }
    }

    static ClassSourceLookup.Visitor createVisitor(ClassSourceLookup lookup) {
        return (ClassSourceLookup.Visitor) (lookup == NO_OP ? ClassSourceLookup.NoOpVisitor.INSTANCE : new ClassSourceLookup.VisitorImpl(lookup));
    }

    public abstract static class ByClassLoader implements ClassSourceLookup {

        @Nullable
        public abstract String identify(ClassLoader var1) throws Exception;

        @Nullable
        @Override
        public final String identify(Class<?> clazz) throws Exception {
            for (ClassLoader loader = clazz.getClassLoader(); loader != null; loader = loader.getParent()) {
                String source = this.identify(loader);
                if (source != null) {
                    return source;
                }
            }
            return null;
        }
    }

    public static class ByCodeSource implements ClassSourceLookup, ClassSourceLookup.ByUrl {

        @Nullable
        @Override
        public String identify(Class<?> clazz) throws URISyntaxException, MalformedURLException {
            ProtectionDomain protectionDomain = clazz.getProtectionDomain();
            if (protectionDomain == null) {
                return null;
            } else {
                CodeSource codeSource = protectionDomain.getCodeSource();
                if (codeSource == null) {
                    return null;
                } else {
                    URL url = codeSource.getLocation();
                    return url == null ? null : this.identifyUrl(url);
                }
            }
        }
    }

    public static class ByFirstUrlSource extends ClassSourceLookup.ByClassLoader implements ClassSourceLookup.ByUrl {

        @Nullable
        @Override
        public String identify(ClassLoader loader) throws IOException, URISyntaxException {
            if (loader instanceof URLClassLoader) {
                URLClassLoader urlClassLoader = (URLClassLoader) loader;
                URL[] urls = urlClassLoader.getURLs();
                return urls.length == 0 ? null : this.identifyUrl(urls[0]);
            } else {
                return null;
            }
        }
    }

    public interface ByUrl extends ClassSourceLookup {

        default String identifyUrl(URL url) throws URISyntaxException, MalformedURLException {
            Path path = null;
            String protocol = url.getProtocol();
            if (protocol.equals("file")) {
                path = Paths.get(url.toURI());
            } else if (protocol.equals("jar")) {
                URL innerUrl = new URL(url.getPath());
                path = Paths.get(innerUrl.getPath().split("!")[0]);
            }
            return path != null ? this.identifyFile(path.toAbsolutePath().normalize()) : null;
        }

        default String identifyFile(Path path) {
            return this.identifyFileName(path.getFileName().toString());
        }

        default String identifyFileName(String fileName) {
            return fileName.endsWith(".jar") ? fileName.substring(0, fileName.length() - 4) : null;
        }
    }

    public static final class MethodCall {

        private final String className;

        private final String methodName;

        private final String methodDescriptor;

        public MethodCall(String className, String methodName, String methodDescriptor) {
            this.className = className;
            this.methodName = methodName;
            this.methodDescriptor = methodDescriptor;
        }

        public String getClassName() {
            return this.className;
        }

        public String getMethodName() {
            return this.methodName;
        }

        public String getMethodDescriptor() {
            return this.methodDescriptor;
        }

        public String toString() {
            return this.className + ";" + this.methodName + ";" + this.methodDescriptor;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            } else if (!(o instanceof ClassSourceLookup.MethodCall)) {
                return false;
            } else {
                ClassSourceLookup.MethodCall that = (ClassSourceLookup.MethodCall) o;
                return this.className.equals(that.className) && this.methodName.equals(that.methodName) && this.methodDescriptor.equals(that.methodDescriptor);
            }
        }

        public int hashCode() {
            return Objects.hash(new Object[] { this.className, this.methodName, this.methodDescriptor });
        }
    }

    public static final class MethodCallByLine {

        private final String className;

        private final String methodName;

        private final int lineNumber;

        public MethodCallByLine(String className, String methodName, int lineNumber) {
            this.className = className;
            this.methodName = methodName;
            this.lineNumber = lineNumber;
        }

        public String getClassName() {
            return this.className;
        }

        public String getMethodName() {
            return this.methodName;
        }

        public int getLineNumber() {
            return this.lineNumber;
        }

        public String toString() {
            return this.className + ";" + this.lineNumber;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            } else if (!(o instanceof ClassSourceLookup.MethodCallByLine)) {
                return false;
            } else {
                ClassSourceLookup.MethodCallByLine that = (ClassSourceLookup.MethodCallByLine) o;
                return this.lineNumber == that.lineNumber && this.className.equals(that.className);
            }
        }

        public int hashCode() {
            return Objects.hash(new Object[] { this.className, this.lineNumber });
        }
    }

    public static enum NoOpVisitor implements ClassSourceLookup.Visitor {

        INSTANCE;

        @Override
        public void visit(ThreadNode node) {
        }

        @Override
        public boolean hasClassSourceMappings() {
            return false;
        }

        @Override
        public Map<String, String> getClassSourceMapping() {
            return Collections.emptyMap();
        }

        @Override
        public boolean hasMethodSourceMappings() {
            return false;
        }

        @Override
        public Map<String, String> getMethodSourceMapping() {
            return Collections.emptyMap();
        }

        @Override
        public boolean hasLineSourceMappings() {
            return false;
        }

        @Override
        public Map<String, String> getLineSourceMapping() {
            return Collections.emptyMap();
        }
    }

    public static final class SourcesMap<T> {

        private final Map<T, String> map = new HashMap();

        private final Function<? super T, String> keyToStringFunction;

        private SourcesMap(Function<? super T, String> keyToStringFunction) {
            this.keyToStringFunction = keyToStringFunction;
        }

        public void computeIfAbsent(T key, ClassSourceLookup.SourcesMap.ComputeSourceFunction<T> function) {
            if (!this.map.containsKey(key)) {
                try {
                    this.map.put(key, function.compute(key));
                } catch (Throwable var4) {
                    this.map.put(key, null);
                }
            }
        }

        public boolean hasMappings() {
            this.map.values().removeIf(Objects::isNull);
            return !this.map.isEmpty();
        }

        public Map<String, String> export() {
            this.map.values().removeIf(Objects::isNull);
            return this.keyToStringFunction.equals(Function.identity()) ? this.map : (Map) this.map.entrySet().stream().collect(Collectors.toMap(e -> (String) this.keyToStringFunction.apply(e.getKey()), Entry::getValue));
        }

        private interface ComputeSourceFunction<T> {

            String compute(T var1) throws Exception;
        }
    }

    public interface Visitor {

        void visit(ThreadNode var1);

        boolean hasClassSourceMappings();

        Map<String, String> getClassSourceMapping();

        boolean hasMethodSourceMappings();

        Map<String, String> getMethodSourceMapping();

        boolean hasLineSourceMappings();

        Map<String, String> getLineSourceMapping();
    }

    public static class VisitorImpl implements ClassSourceLookup.Visitor {

        private final ClassSourceLookup lookup;

        private final ClassFinder classFinder = new ClassFinder();

        private final ClassSourceLookup.SourcesMap<String> classSources = new ClassSourceLookup.SourcesMap<>(Function.identity());

        private final ClassSourceLookup.SourcesMap<ClassSourceLookup.MethodCall> methodSources = new ClassSourceLookup.SourcesMap<>(ClassSourceLookup.MethodCall::toString);

        private final ClassSourceLookup.SourcesMap<ClassSourceLookup.MethodCallByLine> lineSources = new ClassSourceLookup.SourcesMap<>(ClassSourceLookup.MethodCallByLine::toString);

        VisitorImpl(ClassSourceLookup lookup) {
            this.lookup = lookup;
        }

        @Override
        public void visit(ThreadNode node) {
            Queue<StackTraceNode> queue = new ArrayDeque(node.getChildren());
            for (StackTraceNode n = (StackTraceNode) queue.poll(); n != null; n = (StackTraceNode) queue.poll()) {
                this.visitStackNode(n);
                queue.addAll(n.getChildren());
            }
        }

        private void visitStackNode(StackTraceNode node) {
            this.classSources.computeIfAbsent(node.getClassName(), className -> {
                Class<?> clazz = this.classFinder.findClass(className);
                return clazz == null ? null : this.lookup.identify(clazz);
            });
            if (node.getMethodDescription() != null) {
                ClassSourceLookup.MethodCall methodCall = new ClassSourceLookup.MethodCall(node.getClassName(), node.getMethodName(), node.getMethodDescription());
                this.methodSources.computeIfAbsent(methodCall, this.lookup::identify);
            } else {
                ClassSourceLookup.MethodCallByLine methodCall = new ClassSourceLookup.MethodCallByLine(node.getClassName(), node.getMethodName(), node.getLineNumber());
                this.lineSources.computeIfAbsent(methodCall, this.lookup::identify);
            }
        }

        @Override
        public boolean hasClassSourceMappings() {
            return this.classSources.hasMappings();
        }

        @Override
        public Map<String, String> getClassSourceMapping() {
            return this.classSources.export();
        }

        @Override
        public boolean hasMethodSourceMappings() {
            return this.methodSources.hasMappings();
        }

        @Override
        public Map<String, String> getMethodSourceMapping() {
            return this.methodSources.export();
        }

        @Override
        public boolean hasLineSourceMappings() {
            return this.lineSources.hasMappings();
        }

        @Override
        public Map<String, String> getLineSourceMapping() {
            return this.lineSources.export();
        }
    }
}
package me.lucko.spark.common.sampler;

import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import me.lucko.spark.common.util.ThreadFinder;
import me.lucko.spark.proto.SparkSamplerProtos;

public interface ThreadDumper {

    ThreadDumper ALL = new ThreadDumper() {

        @Override
        public ThreadInfo[] dumpThreads(ThreadMXBean threadBean) {
            return threadBean.dumpAllThreads(false, false);
        }

        @Override
        public boolean isThreadIncluded(long threadId, String threadName) {
            return true;
        }

        @Override
        public SparkSamplerProtos.SamplerMetadata.ThreadDumper getMetadata() {
            return SparkSamplerProtos.SamplerMetadata.ThreadDumper.newBuilder().setType(SparkSamplerProtos.SamplerMetadata.ThreadDumper.Type.ALL).build();
        }
    };

    ThreadInfo[] dumpThreads(ThreadMXBean var1);

    boolean isThreadIncluded(long var1, String var3);

    SparkSamplerProtos.SamplerMetadata.ThreadDumper getMetadata();

    static ThreadDumper parseConfigSetting(String setting) {
        switch(setting) {
            case "default":
                return null;
            case "all":
                return ALL;
            default:
                Set<String> threadNames = (Set<String>) Arrays.stream(setting.split(",")).collect(Collectors.toSet());
                return new ThreadDumper.Specific(threadNames);
        }
    }

    public static final class GameThread implements Supplier<ThreadDumper> {

        private Supplier<Thread> threadSupplier;

        private ThreadDumper.Specific dumper = null;

        public GameThread() {
        }

        public GameThread(Supplier<Thread> threadSupplier) {
            this.threadSupplier = threadSupplier;
        }

        public ThreadDumper get() {
            if (this.dumper == null) {
                this.setThread((Thread) this.threadSupplier.get());
                this.threadSupplier = null;
            }
            return (ThreadDumper) Objects.requireNonNull(this.dumper, "dumper");
        }

        public void setThread(Thread thread) {
            this.dumper = new ThreadDumper.Specific(thread);
        }
    }

    public static final class Regex implements ThreadDumper {

        private final ThreadFinder threadFinder = new ThreadFinder();

        private final Set<Pattern> namePatterns;

        private final Map<Long, Boolean> cache = new HashMap();

        public Regex(Set<String> namePatterns) {
            this.namePatterns = (Set<Pattern>) namePatterns.stream().map(regex -> Pattern.compile(regex, 2)).collect(Collectors.toSet());
        }

        @Override
        public boolean isThreadIncluded(long threadId, String threadName) {
            Boolean result = (Boolean) this.cache.get(threadId);
            if (result != null) {
                return result;
            } else {
                for (Pattern pattern : this.namePatterns) {
                    if (pattern.matcher(threadName).matches()) {
                        this.cache.put(threadId, true);
                        return true;
                    }
                }
                this.cache.put(threadId, false);
                return false;
            }
        }

        @Override
        public ThreadInfo[] dumpThreads(ThreadMXBean threadBean) {
            return (ThreadInfo[]) this.threadFinder.getThreads().filter(thread -> this.isThreadIncluded(thread.getId(), thread.getName())).map(thread -> threadBean.getThreadInfo(thread.getId(), Integer.MAX_VALUE)).filter(Objects::nonNull).toArray(ThreadInfo[]::new);
        }

        @Override
        public SparkSamplerProtos.SamplerMetadata.ThreadDumper getMetadata() {
            return SparkSamplerProtos.SamplerMetadata.ThreadDumper.newBuilder().setType(SparkSamplerProtos.SamplerMetadata.ThreadDumper.Type.REGEX).addAllPatterns((Iterable<String>) this.namePatterns.stream().map(Pattern::pattern).collect(Collectors.toList())).build();
        }
    }

    public static final class Specific implements ThreadDumper {

        private final long[] ids;

        private Set<Thread> threads;

        private Set<String> threadNamesLowerCase;

        public Specific(Thread thread) {
            this.ids = new long[] { thread.getId() };
        }

        public Specific(Set<String> names) {
            this.threadNamesLowerCase = (Set<String>) names.stream().map(String::toLowerCase).collect(Collectors.toSet());
            this.ids = new ThreadFinder().getThreads().filter(t -> this.threadNamesLowerCase.contains(t.getName().toLowerCase())).mapToLong(Thread::getId).toArray();
            Arrays.sort(this.ids);
        }

        public Set<Thread> getThreads() {
            if (this.threads == null) {
                this.threads = (Set<Thread>) new ThreadFinder().getThreads().filter(t -> Arrays.binarySearch(this.ids, t.getId()) >= 0).collect(Collectors.toSet());
            }
            return this.threads;
        }

        public Set<String> getThreadNames() {
            if (this.threadNamesLowerCase == null) {
                this.threadNamesLowerCase = (Set<String>) this.getThreads().stream().map(t -> t.getName().toLowerCase()).collect(Collectors.toSet());
            }
            return this.threadNamesLowerCase;
        }

        @Override
        public boolean isThreadIncluded(long threadId, String threadName) {
            return Arrays.binarySearch(this.ids, threadId) >= 0 ? true : this.getThreadNames().contains(threadName.toLowerCase());
        }

        @Override
        public ThreadInfo[] dumpThreads(ThreadMXBean threadBean) {
            return threadBean.getThreadInfo(this.ids, Integer.MAX_VALUE);
        }

        @Override
        public SparkSamplerProtos.SamplerMetadata.ThreadDumper getMetadata() {
            return SparkSamplerProtos.SamplerMetadata.ThreadDumper.newBuilder().setType(SparkSamplerProtos.SamplerMetadata.ThreadDumper.Type.SPECIFIC).addAllIds((Iterable<? extends Long>) Arrays.stream(this.ids).boxed().collect(Collectors.toList())).build();
        }
    }
}
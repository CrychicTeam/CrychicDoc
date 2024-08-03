package me.lucko.spark.common.sampler.async;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicReference;
import me.lucko.spark.common.SparkPlatform;
import me.lucko.spark.common.sampler.ThreadDumper;
import me.lucko.spark.common.sampler.async.jfr.JfrReader;
import me.lucko.spark.lib.asyncprofiler.AsyncProfiler;

public class AsyncProfilerJob {

    private static final AtomicReference<AsyncProfilerJob> ACTIVE = new AtomicReference();

    private final AsyncProfilerAccess access;

    private final AsyncProfiler profiler;

    private SparkPlatform platform;

    private SampleCollector<?> sampleCollector;

    private ThreadDumper threadDumper;

    private int window;

    private boolean quiet;

    private Path outputFile;

    static AsyncProfilerJob createNew(AsyncProfilerAccess access, AsyncProfiler profiler) {
        synchronized (ACTIVE) {
            AsyncProfilerJob existing = (AsyncProfilerJob) ACTIVE.get();
            if (existing != null) {
                throw new IllegalStateException("Another profiler is already active: " + existing);
            } else {
                AsyncProfilerJob job = new AsyncProfilerJob(access, profiler);
                ACTIVE.set(job);
                return job;
            }
        }
    }

    private AsyncProfilerJob(AsyncProfilerAccess access, AsyncProfiler profiler) {
        this.access = access;
        this.profiler = profiler;
    }

    private String execute(Collection<String> command) {
        try {
            return this.profiler.execute(String.join(",", command));
        } catch (IOException var3) {
            throw new RuntimeException("Exception whilst executing profiler command", var3);
        }
    }

    private void checkActive() {
        if (ACTIVE.get() != this) {
            throw new IllegalStateException("Profiler job no longer active!");
        }
    }

    public void init(SparkPlatform platform, SampleCollector<?> collector, ThreadDumper threadDumper, int window, boolean quiet) {
        this.platform = platform;
        this.sampleCollector = collector;
        this.threadDumper = threadDumper;
        this.window = window;
        this.quiet = quiet;
    }

    public void start() {
        this.checkActive();
        try {
            try {
                this.outputFile = this.platform.getTemporaryFiles().create("spark-", "-profile-data.jfr.tmp");
            } catch (IOException var7) {
                throw new RuntimeException("Unable to create temporary output file", var7);
            }
            Builder<String> command = ImmutableList.builder().add("start").addAll(this.sampleCollector.initArguments(this.access)).add("threads").add("jfr").add("file=" + this.outputFile.toString());
            if (this.quiet) {
                command.add("loglevel=NONE");
            }
            if (this.threadDumper instanceof ThreadDumper.Specific) {
                command.add("filter");
            }
            String resp = this.execute(command.build()).trim();
            if (!resp.equalsIgnoreCase("profiling started")) {
                throw new RuntimeException("Unexpected response: " + resp);
            } else {
                if (this.threadDumper instanceof ThreadDumper.Specific) {
                    ThreadDumper.Specific threadDumper = (ThreadDumper.Specific) this.threadDumper;
                    for (Thread thread : threadDumper.getThreads()) {
                        this.profiler.addThread(thread);
                    }
                }
            }
        } catch (Exception var8) {
            try {
                this.profiler.stop();
            } catch (Exception var6) {
            }
            this.close();
            throw var8;
        }
    }

    public void stop() {
        this.checkActive();
        try {
            this.profiler.stop();
        } catch (IllegalStateException var5) {
            if (!var5.getMessage().equals("Profiler is not active")) {
                throw var5;
            }
        } finally {
            this.close();
        }
    }

    public void aggregate(AsyncDataAggregator dataAggregator) {
        try {
            JfrReader reader = new JfrReader(this.outputFile);
            try {
                this.readSegments(reader, this.sampleCollector, dataAggregator);
            } catch (Throwable var6) {
                try {
                    reader.close();
                } catch (Throwable var5) {
                    var6.addSuppressed(var5);
                }
                throw var6;
            }
            reader.close();
        } catch (Exception var8) {
            boolean fileExists;
            try {
                fileExists = Files.exists(this.outputFile, new LinkOption[0]) && Files.size(this.outputFile) != 0L;
            } catch (IOException var7) {
                fileExists = false;
            }
            if (fileExists) {
                throw new JfrParsingException("Error parsing JFR data from profiler output", var8);
            }
            throw new JfrParsingException("Error parsing JFR data from profiler output - file " + this.outputFile + " does not exist!", var8);
        }
        this.deleteOutputFile();
    }

    public void deleteOutputFile() {
        try {
            Files.deleteIfExists(this.outputFile);
        } catch (IOException var2) {
        }
    }

    private <E extends JfrReader.Event> void readSegments(JfrReader reader, SampleCollector<E> collector, AsyncDataAggregator dataAggregator) throws IOException {
        for (E sample : reader.readAllEvents(collector.eventClass())) {
            String threadName = (String) reader.threads.get((long) sample.tid);
            if (threadName != null && this.threadDumper.isThreadIncluded((long) sample.tid, threadName)) {
                long value = collector.measure(sample);
                ProfileSegment segment = ProfileSegment.parseSegment(reader, sample, threadName, value);
                dataAggregator.insertData(segment, this.window);
            }
        }
    }

    public int getWindow() {
        return this.window;
    }

    private void close() {
        ACTIVE.compareAndSet(this, null);
    }
}
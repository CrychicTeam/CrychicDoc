package me.lucko.spark.common.sampler.async;

import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Table;
import com.google.common.io.ByteStreams;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Objects;
import java.util.logging.Level;
import java.util.stream.Collectors;
import me.lucko.spark.common.SparkPlatform;
import me.lucko.spark.lib.asyncprofiler.AsyncProfiler;

public class AsyncProfilerAccess {

    private static AsyncProfilerAccess instance;

    private final AsyncProfiler profiler;

    private final AsyncProfilerAccess.ProfilingEvent profilingEvent;

    private final AsyncProfilerAccess.ProfilingEvent allocationProfilingEvent;

    private final Exception setupException;

    public static synchronized AsyncProfilerAccess getInstance(SparkPlatform platform) {
        if (instance == null) {
            Objects.requireNonNull(platform, "platform");
            instance = new AsyncProfilerAccess(platform);
        }
        return instance;
    }

    AsyncProfilerAccess(SparkPlatform platform) {
        AsyncProfilerAccess.ProfilingEvent profilingEvent = null;
        AsyncProfilerAccess.ProfilingEvent allocationProfilingEvent = null;
        Exception setupException = null;
        AsyncProfiler profiler;
        try {
            profiler = load(platform);
            if (isEventSupported(profiler, AsyncProfilerAccess.ProfilingEvent.ALLOC, false)) {
                allocationProfilingEvent = AsyncProfilerAccess.ProfilingEvent.ALLOC;
            }
            if (isEventSupported(profiler, AsyncProfilerAccess.ProfilingEvent.CPU, false)) {
                profilingEvent = AsyncProfilerAccess.ProfilingEvent.CPU;
            } else if (isEventSupported(profiler, AsyncProfilerAccess.ProfilingEvent.WALL, true)) {
                profilingEvent = AsyncProfilerAccess.ProfilingEvent.WALL;
            }
        } catch (Exception var7) {
            profiler = null;
            setupException = var7;
        }
        this.profiler = profiler;
        this.profilingEvent = profilingEvent;
        this.allocationProfilingEvent = allocationProfilingEvent;
        this.setupException = setupException;
    }

    public AsyncProfilerJob startNewProfilerJob() {
        if (this.profiler == null) {
            throw new UnsupportedOperationException("async-profiler not supported", this.setupException);
        } else {
            return AsyncProfilerJob.createNew(this, this.profiler);
        }
    }

    public AsyncProfilerAccess.ProfilingEvent getProfilingEvent() {
        return this.profilingEvent;
    }

    public AsyncProfilerAccess.ProfilingEvent getAllocationProfilingEvent() {
        return this.allocationProfilingEvent;
    }

    public boolean checkSupported(SparkPlatform platform) {
        if (this.setupException != null) {
            if (this.setupException instanceof AsyncProfilerAccess.UnsupportedSystemException) {
                platform.getPlugin().log(Level.INFO, "The async-profiler engine is not supported for your os/arch (" + this.setupException.getMessage() + "), so the built-in Java engine will be used instead.");
            } else if (this.setupException instanceof AsyncProfilerAccess.NativeLoadingException && this.setupException.getCause().getMessage().contains("libstdc++")) {
                platform.getPlugin().log(Level.WARNING, "Unable to initialise the async-profiler engine because libstdc++ is not installed.");
                platform.getPlugin().log(Level.WARNING, "Please see here for more information: https://spark.lucko.me/docs/misc/Using-async-profiler#install-libstdc");
            } else {
                platform.getPlugin().log(Level.WARNING, "Unable to initialise the async-profiler engine: " + this.setupException.getMessage());
                platform.getPlugin().log(Level.WARNING, "Please see here for more information: https://spark.lucko.me/docs/misc/Using-async-profiler");
                this.setupException.printStackTrace();
            }
        }
        return this.profiler != null;
    }

    public boolean checkAllocationProfilingSupported(SparkPlatform platform) {
        boolean supported = this.allocationProfilingEvent != null;
        if (!supported && this.profiler != null) {
            platform.getPlugin().log(Level.WARNING, "The allocation profiling mode is not supported on your system. This is most likely because Hotspot debug symbols are not available.");
            platform.getPlugin().log(Level.WARNING, "To resolve, try installing the 'openjdk-11-dbg' or 'openjdk-8-dbg' package using your OS package manager.");
        }
        return supported;
    }

    private static AsyncProfiler load(SparkPlatform platform) throws Exception {
        String os = System.getProperty("os.name").toLowerCase(Locale.ROOT).replace(" ", "");
        String arch = System.getProperty("os.arch").toLowerCase(Locale.ROOT);
        if (os.equals("linux") && arch.equals("amd64") && isLinuxMusl()) {
            arch = "amd64-musl";
        }
        Table<String, String, String> supported = ImmutableTable.builder().put("linux", "amd64", "linux/amd64").put("linux", "amd64-musl", "linux/amd64-musl").put("linux", "aarch64", "linux/aarch64").put("macosx", "amd64", "macos").put("macosx", "aarch64", "macos").build();
        String libPath = (String) supported.get(os, arch);
        if (libPath == null) {
            throw new AsyncProfilerAccess.UnsupportedSystemException(os, arch);
        } else {
            String resource = "spark/" + libPath + "/libasyncProfiler.so";
            URL profilerResource = AsyncProfilerAccess.class.getClassLoader().getResource(resource);
            if (profilerResource == null) {
                throw new IllegalStateException("Could not find " + resource + " in spark jar file");
            } else {
                Path extractPath = platform.getTemporaryFiles().create("spark-", "-libasyncProfiler.so.tmp");
                InputStream in = profilerResource.openStream();
                try {
                    OutputStream out = Files.newOutputStream(extractPath);
                    try {
                        ByteStreams.copy(in, out);
                    } catch (Throwable var15) {
                        if (out != null) {
                            try {
                                out.close();
                            } catch (Throwable var13) {
                                var15.addSuppressed(var13);
                            }
                        }
                        throw var15;
                    }
                    if (out != null) {
                        out.close();
                    }
                } catch (Throwable var16) {
                    if (in != null) {
                        try {
                            in.close();
                        } catch (Throwable var12) {
                            var16.addSuppressed(var12);
                        }
                    }
                    throw var16;
                }
                if (in != null) {
                    in.close();
                }
                try {
                    return AsyncProfiler.getInstance(extractPath.toAbsolutePath().toString());
                } catch (UnsatisfiedLinkError var14) {
                    throw new AsyncProfilerAccess.NativeLoadingException(var14);
                }
            }
        }
    }

    private static boolean isEventSupported(AsyncProfiler profiler, AsyncProfilerAccess.ProfilingEvent event, boolean throwException) {
        try {
            String resp = profiler.execute("check,event=" + event).trim();
            if (resp.equalsIgnoreCase("ok")) {
                return true;
            }
            if (throwException) {
                throw new IllegalArgumentException(resp);
            }
        } catch (Exception var4) {
            if (throwException) {
                throw new RuntimeException("Event " + event + " is not supported", var4);
            }
        }
        return false;
    }

    private static boolean isLinuxMusl() {
        try {
            InputStream stream = new ProcessBuilder(new String[] { "sh", "-c", "ldd `which ls`" }).start().getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            String output = (String) reader.lines().collect(Collectors.joining());
            return output.contains("musl");
        } catch (Throwable var3) {
            return false;
        }
    }

    private static final class NativeLoadingException extends RuntimeException {

        public NativeLoadingException(Throwable cause) {
            super("A runtime error occurred whilst loading the native library", cause);
        }
    }

    static enum ProfilingEvent {

        CPU("cpu"), WALL("wall"), ALLOC("alloc");

        private final String id;

        private ProfilingEvent(String id) {
            this.id = id;
        }

        public String toString() {
            return this.id;
        }
    }

    private static final class UnsupportedSystemException extends UnsupportedOperationException {

        public UnsupportedSystemException(String os, String arch) {
            super(os + '/' + arch);
        }
    }
}
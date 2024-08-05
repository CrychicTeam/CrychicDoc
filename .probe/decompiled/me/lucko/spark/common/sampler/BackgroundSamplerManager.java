package me.lucko.spark.common.sampler;

import java.util.logging.Level;
import me.lucko.spark.common.SparkPlatform;
import me.lucko.spark.common.platform.PlatformInfo;
import me.lucko.spark.common.util.Configuration;

public class BackgroundSamplerManager {

    private static final String OPTION_ENABLED = "backgroundProfiler";

    private static final String OPTION_ENGINE = "backgroundProfilerEngine";

    private static final String OPTION_INTERVAL = "backgroundProfilerInterval";

    private static final String OPTION_THREAD_GROUPER = "backgroundProfilerThreadGrouper";

    private static final String OPTION_THREAD_DUMPER = "backgroundProfilerThreadDumper";

    private static final String MARKER_FAILED = "_marker_background_profiler_failed";

    private final SparkPlatform platform;

    private final Configuration configuration;

    private final boolean enabled;

    public BackgroundSamplerManager(SparkPlatform platform, Configuration configuration) {
        this.platform = platform;
        this.configuration = configuration;
        PlatformInfo.Type type = this.platform.getPlugin().getPlatformInfo().getType();
        this.enabled = type != PlatformInfo.Type.CLIENT && this.configuration.getBoolean("backgroundProfiler", type == PlatformInfo.Type.SERVER);
    }

    public void initialise() {
        if (this.enabled) {
            boolean didEnableByDefault = false;
            if (!this.configuration.contains("backgroundProfiler")) {
                this.configuration.setBoolean("backgroundProfiler", true);
                didEnableByDefault = true;
            }
            if (this.configuration.getBoolean("_marker_background_profiler_failed", false)) {
                this.platform.getPlugin().log(Level.WARNING, "It seems the background profiler failed to start when spark was last enabled. Sorry about that!");
                this.platform.getPlugin().log(Level.WARNING, "In the future, spark will try to use the built-in Java profiling engine instead.");
                this.configuration.remove("_marker_background_profiler_failed");
                this.configuration.setString("backgroundProfilerEngine", "java");
                this.configuration.save();
            }
            this.platform.getPlugin().log(Level.INFO, "Starting background profiler...");
            if (didEnableByDefault) {
                this.configuration.setBoolean("_marker_background_profiler_failed", true);
                this.configuration.save();
            }
            try {
                this.startSampler();
                if (didEnableByDefault) {
                    this.configuration.remove("_marker_background_profiler_failed");
                    this.configuration.save();
                }
            } catch (Throwable var3) {
                var3.printStackTrace();
            }
        }
    }

    public boolean restartBackgroundSampler() {
        if (this.enabled) {
            this.startSampler();
            return true;
        } else {
            return false;
        }
    }

    private void startSampler() {
        boolean forceJavaEngine = this.configuration.getString("backgroundProfilerEngine", "async").equals("java");
        ThreadGrouper threadGrouper = ThreadGrouper.parseConfigSetting(this.configuration.getString("backgroundProfilerThreadGrouper", "by-pool"));
        ThreadDumper threadDumper = ThreadDumper.parseConfigSetting(this.configuration.getString("backgroundProfilerThreadDumper", "default"));
        if (threadDumper == null) {
            threadDumper = this.platform.getPlugin().getDefaultThreadDumper();
        }
        int interval = this.configuration.getInteger("backgroundProfilerInterval", 10);
        Sampler sampler = new SamplerBuilder().background(true).threadDumper(threadDumper).threadGrouper(threadGrouper).samplingInterval((double) interval).forceJavaSampler(forceJavaEngine).start(this.platform);
        this.platform.getSamplerContainer().setActiveSampler(sampler);
    }
}
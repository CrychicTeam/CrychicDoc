package org.embeddedt.modernfix.spark;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import it.unimi.dsi.fastutil.objects.Object2ReferenceOpenHashMap;
import java.nio.file.Path;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.stream.Stream;
import me.lucko.spark.common.SparkPlatform;
import me.lucko.spark.common.SparkPlugin;
import me.lucko.spark.common.command.sender.CommandSender;
import me.lucko.spark.common.platform.PlatformInfo;
import me.lucko.spark.common.sampler.Sampler;
import me.lucko.spark.common.sampler.SamplerSettings;
import me.lucko.spark.common.sampler.ThreadDumper;
import me.lucko.spark.common.sampler.ThreadGrouper;
import me.lucko.spark.common.sampler.java.JavaSampler;
import me.lucko.spark.common.sampler.node.MergeMode;
import me.lucko.spark.common.util.MethodDisambiguator;
import me.lucko.spark.lib.adventure.text.Component;
import me.lucko.spark.proto.SparkSamplerProtos;
import net.minecraft.SharedConstants;
import org.embeddedt.modernfix.core.ModernFixMixinPlugin;
import org.embeddedt.modernfix.platform.ModernFixPlatformHooks;

public class SparkLaunchProfiler {

    private static PlatformInfo platformInfo = new SparkLaunchProfiler.ModernFixPlatformInfo();

    private static CommandSender commandSender = new SparkLaunchProfiler.ModernFixCommandSender();

    private static Map<String, Sampler> ongoingSamplers = new Object2ReferenceOpenHashMap();

    private static ExecutorService executor = Executors.newSingleThreadScheduledExecutor(new ThreadFactoryBuilder().setNameFormat("spark-modernfix-async-worker").build());

    private static final SparkPlatform platform = new SparkPlatform(new SparkLaunchProfiler.ModernFixSparkPlugin());

    private static final boolean USE_JAVA_SAMPLER_FOR_LAUNCH = true;

    public static void start(String key) {
        if (!ongoingSamplers.containsKey(key)) {
            SamplerSettings settings = new SamplerSettings(4000, ThreadDumper.ALL, ThreadGrouper.BY_NAME, -1L, false);
            try {
                throw new UnsupportedOperationException();
            } catch (UnsupportedOperationException var4) {
                Sampler sampler = new JavaSampler(platform, settings, true, true);
                ongoingSamplers.put(key, sampler);
                ModernFixMixinPlugin.instance.logger.warn("Profiler has started for stage [{}]...", key);
                sampler.start();
            }
        }
    }

    public static void stop(String key) {
        Sampler sampler = (Sampler) ongoingSamplers.remove(key);
        if (sampler != null) {
            sampler.stop(true);
            output(key, sampler);
        }
    }

    private static void output(String key, Sampler sampler) {
        executor.execute(() -> {
            ModernFixMixinPlugin.instance.logger.warn("Stage [{}] profiler has stopped! Uploading results...", key);
            SparkSamplerProtos.SamplerData output = sampler.toProto(platform, new Sampler.ExportProps().creator(new CommandSender.Data(commandSender.getName(), commandSender.getUniqueId())).comment("Stage: " + key).mergeMode(() -> MergeMode.sameMethod(new MethodDisambiguator())).classSourceLookup(platform::createClassSourceLookup));
            try {
                String urlKey = platform.getBytebinClient().postContent(output, "application/x-spark-sampler").key();
                String url = "https://spark.lucko.me/" + urlKey;
                ModernFixMixinPlugin.instance.logger.warn("Profiler results for Stage [{}]: {}", key, url);
            } catch (Exception var5) {
                ModernFixMixinPlugin.instance.logger.fatal("An error occurred whilst uploading the results.", var5);
            }
        });
    }

    public static class ModernFixCommandSender implements CommandSender {

        private final UUID uuid = UUID.randomUUID();

        private final String name = "ModernFix";

        @Override
        public String getName() {
            return this.name;
        }

        @Override
        public UUID getUniqueId() {
            return this.uuid;
        }

        @Override
        public boolean hasPermission(String s) {
            return true;
        }

        @Override
        public void sendMessage(Component component) {
        }
    }

    static class ModernFixPlatformInfo implements PlatformInfo {

        @Override
        public PlatformInfo.Type getType() {
            return ModernFixPlatformHooks.INSTANCE.isClient() ? PlatformInfo.Type.CLIENT : PlatformInfo.Type.SERVER;
        }

        @Override
        public String getName() {
            return ModernFixPlatformHooks.INSTANCE.getPlatformName();
        }

        @Override
        public String getVersion() {
            return ModernFixPlatformHooks.INSTANCE.getVersionString();
        }

        @Override
        public String getMinecraftVersion() {
            return SharedConstants.getCurrentVersion().getName();
        }
    }

    static class ModernFixSparkPlugin implements SparkPlugin {

        @Override
        public String getVersion() {
            return "1.0";
        }

        @Override
        public Path getPluginDirectory() {
            return ModernFixPlatformHooks.INSTANCE.getGameDirectory().resolve("spark-modernfix");
        }

        @Override
        public String getCommandName() {
            return "spark-modernfix";
        }

        @Override
        public Stream<? extends CommandSender> getCommandSenders() {
            return Stream.of();
        }

        @Override
        public void executeAsync(Runnable runnable) {
            SparkLaunchProfiler.executor.execute(runnable);
        }

        @Override
        public void log(Level level, String s) {
            ModernFixMixinPlugin.instance.logger.warn(s);
        }

        @Override
        public PlatformInfo getPlatformInfo() {
            return SparkLaunchProfiler.platformInfo;
        }
    }
}
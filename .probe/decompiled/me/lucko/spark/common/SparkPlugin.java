package me.lucko.spark.common;

import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.logging.Level;
import java.util.stream.Stream;
import me.lucko.spark.api.Spark;
import me.lucko.spark.common.command.sender.CommandSender;
import me.lucko.spark.common.monitor.ping.PlayerPingProvider;
import me.lucko.spark.common.monitor.tick.TickStatistics;
import me.lucko.spark.common.platform.MetadataProvider;
import me.lucko.spark.common.platform.PlatformInfo;
import me.lucko.spark.common.platform.serverconfig.ServerConfigProvider;
import me.lucko.spark.common.platform.world.WorldInfoProvider;
import me.lucko.spark.common.sampler.ThreadDumper;
import me.lucko.spark.common.sampler.source.ClassSourceLookup;
import me.lucko.spark.common.sampler.source.SourceMetadata;
import me.lucko.spark.common.tick.TickHook;
import me.lucko.spark.common.tick.TickReporter;

public interface SparkPlugin {

    String getVersion();

    Path getPluginDirectory();

    String getCommandName();

    Stream<? extends CommandSender> getCommandSenders();

    void executeAsync(Runnable var1);

    default void executeSync(Runnable task) {
        throw new UnsupportedOperationException();
    }

    void log(Level var1, String var2);

    default ThreadDumper getDefaultThreadDumper() {
        return ThreadDumper.ALL;
    }

    default TickHook createTickHook() {
        return null;
    }

    default TickReporter createTickReporter() {
        return null;
    }

    default TickStatistics createTickStatistics() {
        return null;
    }

    default ClassSourceLookup createClassSourceLookup() {
        return ClassSourceLookup.NO_OP;
    }

    default Collection<SourceMetadata> getKnownSources() {
        return Collections.emptyList();
    }

    default PlayerPingProvider createPlayerPingProvider() {
        return null;
    }

    default ServerConfigProvider createServerConfigProvider() {
        return null;
    }

    default MetadataProvider createExtraMetadataProvider() {
        return null;
    }

    default WorldInfoProvider createWorldInfoProvider() {
        return WorldInfoProvider.NO_OP;
    }

    PlatformInfo getPlatformInfo();

    default void registerApi(Spark api) {
    }
}
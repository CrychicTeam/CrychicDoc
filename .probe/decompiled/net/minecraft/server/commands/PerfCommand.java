package net.minecraft.server.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.logging.LogUtils;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.function.Consumer;
import net.minecraft.FileUtil;
import net.minecraft.SharedConstants;
import net.minecraft.SystemReport;
import net.minecraft.Util;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.FileZipper;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.profiling.EmptyProfileResults;
import net.minecraft.util.profiling.ProfileResults;
import net.minecraft.util.profiling.metrics.storage.MetricsPersister;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;

public class PerfCommand {

    private static final Logger LOGGER = LogUtils.getLogger();

    private static final SimpleCommandExceptionType ERROR_NOT_RUNNING = new SimpleCommandExceptionType(Component.translatable("commands.perf.notRunning"));

    private static final SimpleCommandExceptionType ERROR_ALREADY_RUNNING = new SimpleCommandExceptionType(Component.translatable("commands.perf.alreadyRunning"));

    public static void register(CommandDispatcher<CommandSourceStack> commandDispatcherCommandSourceStack0) {
        commandDispatcherCommandSourceStack0.register((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("perf").requires(p_180462_ -> p_180462_.hasPermission(4))).then(Commands.literal("start").executes(p_180455_ -> startProfilingDedicatedServer((CommandSourceStack) p_180455_.getSource())))).then(Commands.literal("stop").executes(p_180440_ -> stopProfilingDedicatedServer((CommandSourceStack) p_180440_.getSource()))));
    }

    private static int startProfilingDedicatedServer(CommandSourceStack commandSourceStack0) throws CommandSyntaxException {
        MinecraftServer $$1 = commandSourceStack0.getServer();
        if ($$1.isRecordingMetrics()) {
            throw ERROR_ALREADY_RUNNING.create();
        } else {
            Consumer<ProfileResults> $$2 = p_180460_ -> whenStopped(commandSourceStack0, p_180460_);
            Consumer<Path> $$3 = p_180453_ -> saveResults(commandSourceStack0, p_180453_, $$1);
            $$1.startRecordingMetrics($$2, $$3);
            commandSourceStack0.sendSuccess(() -> Component.translatable("commands.perf.started"), false);
            return 0;
        }
    }

    private static int stopProfilingDedicatedServer(CommandSourceStack commandSourceStack0) throws CommandSyntaxException {
        MinecraftServer $$1 = commandSourceStack0.getServer();
        if (!$$1.isRecordingMetrics()) {
            throw ERROR_NOT_RUNNING.create();
        } else {
            $$1.finishRecordingMetrics();
            return 0;
        }
    }

    private static void saveResults(CommandSourceStack commandSourceStack0, Path path1, MinecraftServer minecraftServer2) {
        String $$3 = String.format(Locale.ROOT, "%s-%s-%s", Util.getFilenameFormattedDateTime(), minecraftServer2.getWorldData().getLevelName(), SharedConstants.getCurrentVersion().getId());
        String $$4;
        try {
            $$4 = FileUtil.findAvailableName(MetricsPersister.PROFILING_RESULTS_DIR, $$3, ".zip");
        } catch (IOException var11) {
            commandSourceStack0.sendFailure(Component.translatable("commands.perf.reportFailed"));
            LOGGER.error("Failed to create report name", var11);
            return;
        }
        FileZipper $$7 = new FileZipper(MetricsPersister.PROFILING_RESULTS_DIR.resolve($$4));
        try {
            $$7.add(Paths.get("system.txt"), minecraftServer2.fillSystemReport(new SystemReport()).toLineSeparatedString());
            $$7.add(path1);
        } catch (Throwable var10) {
            try {
                $$7.close();
            } catch (Throwable var8) {
                var10.addSuppressed(var8);
            }
            throw var10;
        }
        $$7.close();
        try {
            FileUtils.forceDelete(path1.toFile());
        } catch (IOException var9) {
            LOGGER.warn("Failed to delete temporary profiling file {}", path1, var9);
        }
        commandSourceStack0.sendSuccess(() -> Component.translatable("commands.perf.reportSaved", $$4), false);
    }

    private static void whenStopped(CommandSourceStack commandSourceStack0, ProfileResults profileResults1) {
        if (profileResults1 != EmptyProfileResults.EMPTY) {
            int $$2 = profileResults1.getTickDuration();
            double $$3 = (double) profileResults1.getNanoDuration() / (double) TimeUtil.NANOSECONDS_PER_SECOND;
            commandSourceStack0.sendSuccess(() -> Component.translatable("commands.perf.stopped", String.format(Locale.ROOT, "%.2f", $$3), $$2, String.format(Locale.ROOT, "%.2f", (double) $$2 / $$3)), false);
        }
    }
}
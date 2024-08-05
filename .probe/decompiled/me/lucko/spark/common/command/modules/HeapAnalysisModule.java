package me.lucko.spark.common.command.modules;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;
import java.util.function.LongConsumer;
import me.lucko.spark.common.SparkPlatform;
import me.lucko.spark.common.activitylog.Activity;
import me.lucko.spark.common.command.Arguments;
import me.lucko.spark.common.command.Command;
import me.lucko.spark.common.command.CommandModule;
import me.lucko.spark.common.command.CommandResponseHandler;
import me.lucko.spark.common.command.sender.CommandSender;
import me.lucko.spark.common.command.tabcomplete.TabCompleter;
import me.lucko.spark.common.heapdump.HeapDump;
import me.lucko.spark.common.heapdump.HeapDumpSummary;
import me.lucko.spark.common.util.Compression;
import me.lucko.spark.common.util.FormatUtil;
import me.lucko.spark.lib.adventure.text.Component;
import me.lucko.spark.lib.adventure.text.event.ClickEvent;
import me.lucko.spark.lib.adventure.text.format.NamedTextColor;
import me.lucko.spark.proto.SparkHeapProtos;

public class HeapAnalysisModule implements CommandModule {

    @Override
    public void registerCommands(Consumer<Command> consumer) {
        consumer.accept(Command.builder().aliases("heapsummary").argumentUsage("save-to-file", null).executor(HeapAnalysisModule::heapSummary).tabCompleter((platform, sender, arguments) -> TabCompleter.completeForOpts(arguments, "--save-to-file", "--run-gc-before")).build());
        consumer.accept(Command.builder().aliases("heapdump").argumentUsage("compress", "type").executor(HeapAnalysisModule::heapDump).tabCompleter((platform, sender, arguments) -> TabCompleter.completeForOpts(arguments, "--compress", "--run-gc-before", "--include-non-live")).build());
    }

    private static void heapSummary(SparkPlatform platform, CommandSender sender, CommandResponseHandler resp, Arguments arguments) {
        if (arguments.boolFlag("run-gc-before")) {
            resp.broadcastPrefixed(Component.text("Running garbage collector..."));
            System.gc();
        }
        resp.broadcastPrefixed(Component.text("Creating a new heap dump summary, please wait..."));
        HeapDumpSummary heapDump;
        try {
            heapDump = HeapDumpSummary.createNew();
        } catch (Exception var11) {
            resp.broadcastPrefixed(Component.text("An error occurred whilst inspecting the heap.", NamedTextColor.RED));
            var11.printStackTrace();
            return;
        }
        SparkHeapProtos.HeapData output = heapDump.toProto(platform, sender);
        boolean saveToFile = false;
        if (arguments.boolFlag("save-to-file")) {
            saveToFile = true;
        } else {
            try {
                String key = platform.getBytebinClient().postContent(output, "application/x-spark-heap").key();
                String url = platform.getViewerUrl() + key;
                resp.broadcastPrefixed(Component.text("Heap dump summmary output:", NamedTextColor.GOLD));
                resp.broadcast(Component.text().content(url).color(NamedTextColor.GRAY).clickEvent(ClickEvent.openUrl(url)).build());
                platform.getActivityLog().addToLog(Activity.urlActivity(sender, System.currentTimeMillis(), "Heap dump summary", url));
            } catch (Exception var10) {
                resp.broadcastPrefixed(Component.text("An error occurred whilst uploading the data. Attempting to save to disk instead.", NamedTextColor.RED));
                var10.printStackTrace();
                saveToFile = true;
            }
        }
        if (saveToFile) {
            Path file = platform.resolveSaveFile("heapsummary", "sparkheap");
            try {
                Files.write(file, output.toByteArray(), new OpenOption[0]);
                resp.broadcastPrefixed(Component.text().content("Heap dump summary written to: ").color(NamedTextColor.GOLD).append(Component.text(file.toString(), NamedTextColor.GRAY)).build());
                resp.broadcastPrefixed(Component.text("You can read the heap dump summary file using the viewer web-app - " + platform.getViewerUrl(), NamedTextColor.GRAY));
                platform.getActivityLog().addToLog(Activity.fileActivity(sender, System.currentTimeMillis(), "Heap dump summary", file.toString()));
            } catch (IOException var9) {
                resp.broadcastPrefixed(Component.text("An error occurred whilst saving the data.", NamedTextColor.RED));
                var9.printStackTrace();
            }
        }
    }

    private static void heapDump(SparkPlatform platform, CommandSender sender, CommandResponseHandler resp, Arguments arguments) {
        Path file = platform.resolveSaveFile("heap", HeapDump.isOpenJ9() ? "phd" : "hprof");
        boolean liveOnly = !arguments.boolFlag("include-non-live");
        if (arguments.boolFlag("run-gc-before")) {
            resp.broadcastPrefixed(Component.text("Running garbage collector..."));
            System.gc();
        }
        resp.broadcastPrefixed(Component.text("Creating a new heap dump, please wait..."));
        try {
            HeapDump.dumpHeap(file, liveOnly);
        } catch (Exception var11) {
            resp.broadcastPrefixed(Component.text("An error occurred whilst creating a heap dump.", NamedTextColor.RED));
            var11.printStackTrace();
            return;
        }
        resp.broadcastPrefixed(Component.text().content("Heap dump written to: ").color(NamedTextColor.GOLD).append(Component.text(file.toString(), NamedTextColor.GRAY)).build());
        platform.getActivityLog().addToLog(Activity.fileActivity(sender, System.currentTimeMillis(), "Heap dump", file.toString()));
        Compression compressionMethod = null;
        Iterator<String> compressArgs = arguments.stringFlag("compress").iterator();
        if (compressArgs.hasNext()) {
            try {
                compressionMethod = Compression.valueOf(((String) compressArgs.next()).toUpperCase());
            } catch (IllegalArgumentException var10) {
            }
        }
        if (compressionMethod != null) {
            try {
                heapDumpCompress(platform, resp, file, compressionMethod);
            } catch (IOException var9) {
                var9.printStackTrace();
            }
        }
    }

    private static void heapDumpCompress(SparkPlatform platform, CommandResponseHandler resp, Path file, Compression method) throws IOException {
        resp.broadcastPrefixed(Component.text("Compressing heap dump, please wait..."));
        long size = Files.size(file);
        AtomicLong lastReport = new AtomicLong(System.currentTimeMillis());
        LongConsumer progressHandler = progress -> {
            long timeSinceLastReport = System.currentTimeMillis() - lastReport.get();
            if (timeSinceLastReport > TimeUnit.SECONDS.toMillis(5L)) {
                lastReport.set(System.currentTimeMillis());
                platform.getPlugin().executeAsync(() -> resp.broadcastPrefixed(Component.text().color(NamedTextColor.GRAY).append(Component.text("Compressed ")).append(Component.text(FormatUtil.formatBytes(progress), NamedTextColor.GOLD)).append(Component.text(" / ")).append(Component.text(FormatUtil.formatBytes(size), NamedTextColor.GOLD)).append(Component.text(" so far... (")).append(Component.text(FormatUtil.percent((double) progress, (double) size), NamedTextColor.GREEN)).append(Component.text(")")).build()));
            }
        };
        Path compressedFile = method.compress(file, progressHandler);
        long compressedSize = Files.size(compressedFile);
        resp.broadcastPrefixed(Component.text().color(NamedTextColor.GRAY).append(Component.text("Compression complete: ")).append(Component.text(FormatUtil.formatBytes(size), NamedTextColor.GOLD)).append(Component.text(" --> ")).append(Component.text(FormatUtil.formatBytes(compressedSize), NamedTextColor.GOLD)).append(Component.text(" (")).append(Component.text(FormatUtil.percent((double) compressedSize, (double) size), NamedTextColor.GREEN)).append(Component.text(")")).build());
        resp.broadcastPrefixed(Component.text().content("Compressed heap dump written to: ").color(NamedTextColor.GOLD).append(Component.text(compressedFile.toString(), NamedTextColor.GRAY)).build());
    }
}
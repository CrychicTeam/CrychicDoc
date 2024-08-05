package me.lucko.spark.common;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableList.Builder;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.stream.Collectors;
import me.lucko.spark.common.activitylog.ActivityLog;
import me.lucko.spark.common.api.SparkApi;
import me.lucko.spark.common.command.Arguments;
import me.lucko.spark.common.command.Command;
import me.lucko.spark.common.command.CommandModule;
import me.lucko.spark.common.command.CommandResponseHandler;
import me.lucko.spark.common.command.modules.ActivityLogModule;
import me.lucko.spark.common.command.modules.GcMonitoringModule;
import me.lucko.spark.common.command.modules.HealthModule;
import me.lucko.spark.common.command.modules.HeapAnalysisModule;
import me.lucko.spark.common.command.modules.SamplerModule;
import me.lucko.spark.common.command.modules.TickMonitoringModule;
import me.lucko.spark.common.command.sender.CommandSender;
import me.lucko.spark.common.command.tabcomplete.CompletionSupplier;
import me.lucko.spark.common.command.tabcomplete.TabCompleter;
import me.lucko.spark.common.monitor.cpu.CpuMonitor;
import me.lucko.spark.common.monitor.memory.GarbageCollectorStatistics;
import me.lucko.spark.common.monitor.net.NetworkMonitor;
import me.lucko.spark.common.monitor.ping.PingStatistics;
import me.lucko.spark.common.monitor.ping.PlayerPingProvider;
import me.lucko.spark.common.monitor.tick.SparkTickStatistics;
import me.lucko.spark.common.monitor.tick.TickStatistics;
import me.lucko.spark.common.platform.PlatformStatisticsProvider;
import me.lucko.spark.common.sampler.BackgroundSamplerManager;
import me.lucko.spark.common.sampler.SamplerContainer;
import me.lucko.spark.common.sampler.source.ClassSourceLookup;
import me.lucko.spark.common.tick.TickHook;
import me.lucko.spark.common.tick.TickReporter;
import me.lucko.spark.common.util.BytebinClient;
import me.lucko.spark.common.util.Configuration;
import me.lucko.spark.common.util.TemporaryFiles;
import me.lucko.spark.common.ws.TrustedKeyStore;
import me.lucko.spark.lib.adventure.text.Component;
import me.lucko.spark.lib.adventure.text.event.ClickEvent;
import me.lucko.spark.lib.adventure.text.format.NamedTextColor;
import me.lucko.spark.lib.adventure.text.format.TextDecoration;
import me.lucko.spark.lib.bytesocks.BytesocksClient;

public class SparkPlatform {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH.mm.ss");

    private final SparkPlugin plugin;

    private final TemporaryFiles temporaryFiles;

    private final Configuration configuration;

    private final String viewerUrl;

    private final BytebinClient bytebinClient;

    private final BytesocksClient bytesocksClient;

    private final TrustedKeyStore trustedKeyStore;

    private final boolean disableResponseBroadcast;

    private final List<CommandModule> commandModules;

    private final List<Command> commands;

    private final ReentrantLock commandExecuteLock = new ReentrantLock(true);

    private final ActivityLog activityLog;

    private final SamplerContainer samplerContainer;

    private final BackgroundSamplerManager backgroundSamplerManager;

    private final TickHook tickHook;

    private final TickReporter tickReporter;

    private final TickStatistics tickStatistics;

    private final PingStatistics pingStatistics;

    private final PlatformStatisticsProvider statisticsProvider;

    private Map<String, GarbageCollectorStatistics> startupGcStatistics = ImmutableMap.of();

    private long serverNormalOperationStartTime;

    private final AtomicBoolean enabled = new AtomicBoolean(false);

    public SparkPlatform(SparkPlugin plugin) {
        this.plugin = plugin;
        this.temporaryFiles = new TemporaryFiles(this.plugin.getPluginDirectory().resolve("tmp"));
        this.configuration = new Configuration(this.plugin.getPluginDirectory().resolve("config.json"));
        this.viewerUrl = this.configuration.getString("viewerUrl", "https://spark.lucko.me/");
        String bytebinUrl = this.configuration.getString("bytebinUrl", "https://spark-usercontent.lucko.me/");
        String bytesocksHost = this.configuration.getString("bytesocksHost", "spark-usersockets.lucko.me");
        this.bytebinClient = new BytebinClient(bytebinUrl, "spark-plugin");
        this.bytesocksClient = BytesocksClient.create(bytesocksHost, "spark-plugin");
        this.trustedKeyStore = new TrustedKeyStore(this.configuration);
        this.disableResponseBroadcast = this.configuration.getBoolean("disableResponseBroadcast", false);
        this.commandModules = ImmutableList.of(new SamplerModule(), new HealthModule(), new TickMonitoringModule(), new GcMonitoringModule(), new HeapAnalysisModule(), new ActivityLogModule());
        Builder<Command> commandsBuilder = ImmutableList.builder();
        for (CommandModule module : this.commandModules) {
            module.registerCommands(commandsBuilder::add);
        }
        this.commands = commandsBuilder.build();
        this.activityLog = new ActivityLog(plugin.getPluginDirectory().resolve("activity.json"));
        this.activityLog.load();
        this.samplerContainer = new SamplerContainer();
        this.backgroundSamplerManager = new BackgroundSamplerManager(this, this.configuration);
        TickStatistics tickStatistics = plugin.createTickStatistics();
        this.tickHook = plugin.createTickHook();
        this.tickReporter = plugin.createTickReporter();
        if (tickStatistics == null && (this.tickHook != null || this.tickReporter != null)) {
            tickStatistics = new SparkTickStatistics();
        }
        this.tickStatistics = tickStatistics;
        PlayerPingProvider pingProvider = plugin.createPlayerPingProvider();
        this.pingStatistics = pingProvider != null ? new PingStatistics(pingProvider) : null;
        this.statisticsProvider = new PlatformStatisticsProvider(this);
    }

    public void enable() {
        if (!this.enabled.compareAndSet(false, true)) {
            throw new RuntimeException("Platform has already been enabled!");
        } else {
            if (this.tickHook != null && this.tickStatistics instanceof SparkTickStatistics) {
                this.tickHook.addCallback((TickHook.Callback) this.tickStatistics);
                this.tickHook.start();
            }
            if (this.tickReporter != null && this.tickStatistics instanceof SparkTickStatistics) {
                this.tickReporter.addCallback((TickReporter.Callback) this.tickStatistics);
                this.tickReporter.start();
            }
            if (this.pingStatistics != null) {
                this.pingStatistics.start();
            }
            CpuMonitor.ensureMonitoring();
            NetworkMonitor.ensureMonitoring();
            this.plugin.executeAsync(() -> {
                this.startupGcStatistics = GarbageCollectorStatistics.pollStats();
                this.serverNormalOperationStartTime = System.currentTimeMillis();
            });
            SparkApi api = new SparkApi(this);
            this.plugin.registerApi(api);
            SparkApi.register(api);
            this.backgroundSamplerManager.initialise();
        }
    }

    public void disable() {
        if (this.tickHook != null) {
            this.tickHook.close();
        }
        if (this.tickReporter != null) {
            this.tickReporter.close();
        }
        if (this.pingStatistics != null) {
            this.pingStatistics.close();
        }
        for (CommandModule module : this.commandModules) {
            module.close();
        }
        this.samplerContainer.close();
        SparkApi.unregister();
        this.temporaryFiles.deleteTemporaryFiles();
    }

    public SparkPlugin getPlugin() {
        return this.plugin;
    }

    public TemporaryFiles getTemporaryFiles() {
        return this.temporaryFiles;
    }

    public Configuration getConfiguration() {
        return this.configuration;
    }

    public String getViewerUrl() {
        return this.viewerUrl;
    }

    public BytebinClient getBytebinClient() {
        return this.bytebinClient;
    }

    public BytesocksClient getBytesocksClient() {
        return this.bytesocksClient;
    }

    public TrustedKeyStore getTrustedKeyStore() {
        return this.trustedKeyStore;
    }

    public boolean shouldBroadcastResponse() {
        return !this.disableResponseBroadcast;
    }

    public List<Command> getCommands() {
        return this.commands;
    }

    public ActivityLog getActivityLog() {
        return this.activityLog;
    }

    public SamplerContainer getSamplerContainer() {
        return this.samplerContainer;
    }

    public BackgroundSamplerManager getBackgroundSamplerManager() {
        return this.backgroundSamplerManager;
    }

    public TickHook getTickHook() {
        return this.tickHook;
    }

    public TickReporter getTickReporter() {
        return this.tickReporter;
    }

    public PlatformStatisticsProvider getStatisticsProvider() {
        return this.statisticsProvider;
    }

    public ClassSourceLookup createClassSourceLookup() {
        return this.plugin.createClassSourceLookup();
    }

    public TickStatistics getTickStatistics() {
        return this.tickStatistics;
    }

    public PingStatistics getPingStatistics() {
        return this.pingStatistics;
    }

    public Map<String, GarbageCollectorStatistics> getStartupGcStatistics() {
        return this.startupGcStatistics;
    }

    public long getServerNormalOperationStartTime() {
        return this.serverNormalOperationStartTime;
    }

    public Path resolveSaveFile(String prefix, String extension) {
        Path pluginFolder = this.plugin.getPluginDirectory();
        try {
            Files.createDirectories(pluginFolder);
        } catch (IOException var5) {
        }
        return pluginFolder.resolve(prefix + "-" + DATE_TIME_FORMATTER.format(LocalDateTime.now()) + "." + extension);
    }

    private List<Command> getAvailableCommands(CommandSender sender) {
        return sender.hasPermission("spark") ? this.commands : (List) this.commands.stream().filter(c -> sender.hasPermission("spark." + c.primaryAlias())).collect(Collectors.toList());
    }

    public boolean hasPermissionForAnyCommand(CommandSender sender) {
        return !this.getAvailableCommands(sender).isEmpty();
    }

    public void executeCommand(CommandSender sender, String[] args) {
        AtomicReference<Thread> executorThread = new AtomicReference();
        AtomicReference<Thread> timeoutThread = new AtomicReference();
        AtomicBoolean completed = new AtomicBoolean(false);
        this.plugin.executeAsync(() -> {
            executorThread.set(Thread.currentThread());
            this.commandExecuteLock.lock();
            try {
                this.executeCommand0(sender, args);
            } catch (Exception var11) {
                this.plugin.log(Level.SEVERE, "Exception occurred whilst executing a spark command");
                var11.printStackTrace();
            } finally {
                this.commandExecuteLock.unlock();
                executorThread.set(null);
                completed.set(true);
                Thread timeout = (Thread) timeoutThread.get();
                if (timeout != null) {
                    timeout.interrupt();
                }
            }
        });
        this.plugin.executeAsync(() -> {
            timeoutThread.set(Thread.currentThread());
            int warningIntervalSeconds = 5;
            try {
                for (int i = 1; i <= 3; i++) {
                    try {
                        Thread.sleep((long) (warningIntervalSeconds * 1000));
                    } catch (InterruptedException var11) {
                    }
                    if (completed.get()) {
                        return;
                    }
                    Thread executor = (Thread) executorThread.get();
                    if (executor == null) {
                        this.getPlugin().log(Level.WARNING, "A command execution has not completed after " + i * warningIntervalSeconds + " seconds but there is no executor present. Perhaps the executor shutdown?");
                        this.getPlugin().log(Level.WARNING, "If the command subsequently completes without any errors, this warning should be ignored. :)");
                    } else {
                        String stackTrace = (String) Arrays.stream(executor.getStackTrace()).map(el -> "  " + el.toString()).collect(Collectors.joining("\n"));
                        this.getPlugin().log(Level.WARNING, "A command execution has not completed after " + i * warningIntervalSeconds + " seconds, it *might* be stuck. Trace: \n" + stackTrace);
                        this.getPlugin().log(Level.WARNING, "If the command subsequently completes without any errors, this warning should be ignored. :)");
                    }
                }
            } finally {
                timeoutThread.set(null);
            }
        });
    }

    private void executeCommand0(CommandSender sender, String[] args) {
        CommandResponseHandler resp = new CommandResponseHandler(this, sender);
        List<Command> commands = this.getAvailableCommands(sender);
        if (commands.isEmpty()) {
            resp.replyPrefixed(Component.text("You do not have permission to use this command.", NamedTextColor.RED));
        } else if (args.length == 0) {
            resp.replyPrefixed(Component.text().append(Component.text("spark", NamedTextColor.WHITE)).append(Component.space()).append(Component.text("v" + this.getPlugin().getVersion(), NamedTextColor.GRAY)).build());
            String helpCmd = "/" + this.getPlugin().getCommandName() + " help";
            resp.replyPrefixed(Component.text().color(NamedTextColor.GRAY).append(Component.text("Run ")).append(Component.text().content(helpCmd).color(NamedTextColor.WHITE).clickEvent(ClickEvent.runCommand(helpCmd)).build()).append(Component.text(" to view usage information.")).build());
        } else {
            ArrayList<String> rawArgs = new ArrayList(Arrays.asList(args));
            String alias = ((String) rawArgs.remove(0)).toLowerCase();
            for (Command command : commands) {
                if (command.aliases().contains(alias)) {
                    resp.setCommandPrimaryAlias(command.primaryAlias());
                    try {
                        command.executor().execute(this, sender, resp, new Arguments(rawArgs, command.allowSubCommand()));
                    } catch (Arguments.ParseException var10) {
                        resp.replyPrefixed(Component.text(var10.getMessage(), NamedTextColor.RED));
                    }
                    return;
                }
            }
            this.sendUsage(commands, resp);
        }
    }

    public List<String> tabCompleteCommand(CommandSender sender, String[] args) {
        List<Command> commands = this.getAvailableCommands(sender);
        if (commands.isEmpty()) {
            return Collections.emptyList();
        } else {
            List<String> arguments = new ArrayList(Arrays.asList(args));
            if (args.length <= 1) {
                List<String> mainCommands = (List<String>) commands.stream().map(Command::primaryAlias).collect(Collectors.toList());
                return TabCompleter.create().at(0, CompletionSupplier.startsWith(mainCommands)).complete(arguments);
            } else {
                String alias = (String) arguments.remove(0);
                for (Command command : commands) {
                    if (command.aliases().contains(alias)) {
                        return command.tabCompleter().completions(this, sender, arguments);
                    }
                }
                return Collections.emptyList();
            }
        }
    }

    private void sendUsage(List<Command> commands, CommandResponseHandler sender) {
        sender.replyPrefixed(Component.text().append(Component.text("spark", NamedTextColor.WHITE)).append(Component.space()).append(Component.text("v" + this.getPlugin().getVersion(), NamedTextColor.GRAY)).build());
        for (Command command : commands) {
            String usage = "/" + this.getPlugin().getCommandName() + " " + command.primaryAlias();
            if (command.allowSubCommand()) {
                Map<String, List<Command.ArgumentInfo>> argumentsBySubCommand = (Map<String, List<Command.ArgumentInfo>>) command.arguments().stream().collect(Collectors.groupingBy(Command.ArgumentInfo::subCommandName, LinkedHashMap::new, Collectors.toList()));
                argumentsBySubCommand.forEach((subCommand, arguments) -> {
                    String subCommandUsage = usage + " " + subCommand;
                    sender.reply(Component.text().append(Component.text(">", NamedTextColor.GOLD, TextDecoration.BOLD)).append(Component.space()).append(Component.text().content(subCommandUsage).color(NamedTextColor.GRAY).clickEvent(ClickEvent.suggestCommand(subCommandUsage)).build()).build());
                    for (Command.ArgumentInfo argx : arguments) {
                        if (!argx.argumentName().isEmpty()) {
                            sender.reply(argx.toComponent("      "));
                        }
                    }
                });
            } else {
                sender.reply(Component.text().append(Component.text(">", NamedTextColor.GOLD, TextDecoration.BOLD)).append(Component.space()).append(Component.text().content(usage).color(NamedTextColor.GRAY).clickEvent(ClickEvent.suggestCommand(usage)).build()).build());
                for (Command.ArgumentInfo arg : command.arguments()) {
                    sender.reply(arg.toComponent("    "));
                }
            }
        }
        sender.reply(Component.empty());
        sender.replyPrefixed(Component.text().append(Component.text("For full usage information, please go to: ")).append(Component.text().content("https://spark.lucko.me/docs/Command-Usage").color(NamedTextColor.WHITE).clickEvent(ClickEvent.openUrl("https://spark.lucko.me/docs/Command-Usage")).build()).build());
    }
}
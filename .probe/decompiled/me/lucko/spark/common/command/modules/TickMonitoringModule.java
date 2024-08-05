package me.lucko.spark.common.command.modules;

import java.util.function.Consumer;
import me.lucko.spark.common.SparkPlatform;
import me.lucko.spark.common.command.Command;
import me.lucko.spark.common.command.CommandModule;
import me.lucko.spark.common.command.CommandResponseHandler;
import me.lucko.spark.common.command.tabcomplete.TabCompleter;
import me.lucko.spark.common.monitor.tick.ReportPredicate;
import me.lucko.spark.common.monitor.tick.TickMonitor;
import me.lucko.spark.common.tick.TickHook;
import me.lucko.spark.lib.adventure.text.Component;
import me.lucko.spark.lib.adventure.text.format.NamedTextColor;

public class TickMonitoringModule implements CommandModule {

    private TickMonitor activeTickMonitor = null;

    @Override
    public void close() {
        if (this.activeTickMonitor != null) {
            this.activeTickMonitor.close();
            this.activeTickMonitor = null;
        }
    }

    @Override
    public void registerCommands(Consumer<Command> consumer) {
        consumer.accept(Command.builder().aliases("tickmonitor", "tickmonitoring").argumentUsage("threshold", "percentage increase").argumentUsage("threshold-tick", "tick duration").argumentUsage("without-gc", null).executor((platform, sender, resp, arguments) -> {
            TickHook tickHook = platform.getTickHook();
            if (tickHook == null) {
                resp.replyPrefixed(Component.text("Not supported!", NamedTextColor.RED));
            } else {
                if (this.activeTickMonitor == null) {
                    ReportPredicate reportPredicate;
                    int threshold;
                    if ((threshold = arguments.intFlag("threshold")) != -1) {
                        reportPredicate = new ReportPredicate.PercentageChangeGt((double) threshold);
                    } else if ((threshold = arguments.intFlag("threshold-tick")) != -1) {
                        reportPredicate = new ReportPredicate.DurationGt((double) threshold);
                    } else {
                        reportPredicate = new ReportPredicate.PercentageChangeGt(100.0);
                    }
                    this.activeTickMonitor = new TickMonitoringModule.ReportingTickMonitor(platform, resp, tickHook, reportPredicate, !arguments.boolFlag("without-gc"));
                    this.activeTickMonitor.start();
                } else {
                    this.close();
                    resp.broadcastPrefixed(Component.text("Tick monitor disabled."));
                }
            }
        }).tabCompleter((platform, sender, arguments) -> TabCompleter.completeForOpts(arguments, "--threshold", "--threshold-tick", "--without-gc")).build());
    }

    private static class ReportingTickMonitor extends TickMonitor {

        private final CommandResponseHandler resp;

        ReportingTickMonitor(SparkPlatform platform, CommandResponseHandler resp, TickHook tickHook, ReportPredicate reportPredicate, boolean monitorGc) {
            super(platform, tickHook, reportPredicate, monitorGc);
            this.resp = resp;
        }

        @Override
        protected void sendMessage(Component message) {
            this.resp.broadcastPrefixed(message);
        }
    }
}
package me.lucko.spark.common.command;

import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import me.lucko.spark.common.SparkPlatform;
import me.lucko.spark.common.command.sender.CommandSender;
import me.lucko.spark.lib.adventure.text.Component;
import me.lucko.spark.lib.adventure.text.JoinConfiguration;
import me.lucko.spark.lib.adventure.text.TextComponent;
import me.lucko.spark.lib.adventure.text.format.NamedTextColor;
import me.lucko.spark.lib.adventure.text.format.TextDecoration;

public class CommandResponseHandler {

    private static final TextComponent PREFIX = Component.text().color(NamedTextColor.GRAY).append(Component.text("[", NamedTextColor.DARK_GRAY)).append(Component.text("⚡", NamedTextColor.YELLOW, TextDecoration.BOLD)).append(Component.text("]", NamedTextColor.DARK_GRAY)).append(Component.text(" ")).build();

    private final SparkPlatform platform;

    private final CommandSender sender;

    private String commandPrimaryAlias;

    public CommandResponseHandler(SparkPlatform platform, CommandSender sender) {
        this.platform = platform;
        this.sender = sender;
    }

    public void setCommandPrimaryAlias(String commandPrimaryAlias) {
        this.commandPrimaryAlias = commandPrimaryAlias;
    }

    public CommandSender sender() {
        return this.sender;
    }

    public void allSenders(Consumer<? super CommandSender> action) {
        if (this.commandPrimaryAlias == null) {
            throw new IllegalStateException("Command alias has not been set!");
        } else {
            Set<CommandSender> senders = (Set<CommandSender>) this.platform.getPlugin().getCommandSenders().filter(s -> s.hasPermission("spark") || s.hasPermission("spark." + this.commandPrimaryAlias)).collect(Collectors.toSet());
            senders.add(this.sender);
            senders.forEach(action);
        }
    }

    public void reply(Component message) {
        this.sender.sendMessage(message);
    }

    public void reply(Iterable<Component> message) {
        Component joinedMsg = Component.join(JoinConfiguration.separator(Component.newline()), message);
        this.sender.sendMessage(joinedMsg);
    }

    public void broadcast(Component message) {
        if (this.platform.shouldBroadcastResponse()) {
            this.allSenders(sender -> sender.sendMessage(message));
        } else {
            this.reply(message);
        }
    }

    public void broadcast(Iterable<Component> message) {
        if (this.platform.shouldBroadcastResponse()) {
            Component joinedMsg = Component.join(JoinConfiguration.separator(Component.newline()), message);
            this.allSenders(sender -> sender.sendMessage(joinedMsg));
        } else {
            this.reply(message);
        }
    }

    public void replyPrefixed(Component message) {
        this.reply(applyPrefix(message));
    }

    public void broadcastPrefixed(Component message) {
        this.broadcast(applyPrefix(message));
    }

    public static Component applyPrefix(Component message) {
        return PREFIX.append(message);
    }
}
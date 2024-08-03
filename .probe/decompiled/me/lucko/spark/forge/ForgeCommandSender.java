package me.lucko.spark.forge;

import java.util.Objects;
import java.util.UUID;
import me.lucko.spark.common.command.sender.AbstractCommandSender;
import me.lucko.spark.forge.plugin.ForgeSparkPlugin;
import me.lucko.spark.lib.adventure.text.Component;
import me.lucko.spark.lib.adventure.text.serializer.gson.GsonComponentSerializer;
import net.minecraft.commands.CommandSource;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.rcon.RconConsoleSource;
import net.minecraft.world.entity.player.Player;

public class ForgeCommandSender extends AbstractCommandSender<CommandSource> {

    private final ForgeSparkPlugin plugin;

    public ForgeCommandSender(CommandSource source, ForgeSparkPlugin plugin) {
        super(source);
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        if (super.delegate instanceof Player) {
            return ((Player) super.delegate).getGameProfile().getName();
        } else if (super.delegate instanceof MinecraftServer) {
            return "Console";
        } else {
            return super.delegate instanceof RconConsoleSource ? "RCON Console" : "unknown:" + super.delegate.getClass().getSimpleName();
        }
    }

    @Override
    public UUID getUniqueId() {
        return super.delegate instanceof Player ? ((Player) super.delegate).m_20148_() : null;
    }

    @Override
    public void sendMessage(Component message) {
        MutableComponent component = net.minecraft.network.chat.Component.Serializer.fromJson(GsonComponentSerializer.gson().serialize(message));
        Objects.requireNonNull(component, "component");
        ((CommandSource) super.delegate).sendSystemMessage(component);
    }

    @Override
    public boolean hasPermission(String permission) {
        return this.plugin.hasPermission((CommandSource) super.delegate, permission);
    }
}
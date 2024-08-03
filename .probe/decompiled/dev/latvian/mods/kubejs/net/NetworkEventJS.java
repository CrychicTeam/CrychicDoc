package dev.latvian.mods.kubejs.net;

import dev.latvian.mods.kubejs.player.PlayerEventJS;
import dev.latvian.mods.kubejs.typings.Info;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

@Info("Invoked when a network packet is received.\n\nNote that the behaviour of this event is depending on the **script type**.\n\nIn `server_scripts`, this event is invoked on the server side when a packet is received from a client.\n\nIn `client_scripts`, this event is invoked on the client side when a packet is received from the server.\n")
public class NetworkEventJS extends PlayerEventJS {

    private final Player player;

    private final String channel;

    private final CompoundTag data;

    public NetworkEventJS(Player p, String c, @Nullable CompoundTag d) {
        this.player = p;
        this.channel = c;
        this.data = d;
    }

    @Info("The player that sent the packet. Always `Minecraft.player` in `client_scripts`.")
    @Override
    public Player getEntity() {
        return this.player;
    }

    @Info("The channel of the packet.")
    public String getChannel() {
        return this.channel;
    }

    @Info("The data of the packet.")
    @Nullable
    public CompoundTag getData() {
        return this.data;
    }
}
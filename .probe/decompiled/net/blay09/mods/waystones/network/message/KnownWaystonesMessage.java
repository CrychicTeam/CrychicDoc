package net.blay09.mods.waystones.network.message;

import java.util.ArrayList;
import java.util.List;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.BalmEnvironment;
import net.blay09.mods.waystones.api.IWaystone;
import net.blay09.mods.waystones.api.KnownWaystonesEvent;
import net.blay09.mods.waystones.core.InMemoryPlayerWaystoneData;
import net.blay09.mods.waystones.core.PlayerWaystoneManager;
import net.blay09.mods.waystones.core.Waystone;
import net.blay09.mods.waystones.core.WaystoneManager;
import net.blay09.mods.waystones.core.WaystoneTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public class KnownWaystonesMessage {

    private final ResourceLocation type;

    private final List<IWaystone> waystones;

    public KnownWaystonesMessage(ResourceLocation type, List<IWaystone> waystones) {
        this.type = type;
        this.waystones = waystones;
    }

    public static void encode(KnownWaystonesMessage message, FriendlyByteBuf buf) {
        buf.writeResourceLocation(message.type);
        buf.writeShort(message.waystones.size());
        for (IWaystone waystone : message.waystones) {
            Waystone.write(buf, waystone);
        }
    }

    public static KnownWaystonesMessage decode(FriendlyByteBuf buf) {
        ResourceLocation type = buf.readResourceLocation();
        int waystoneCount = buf.readShort();
        List<IWaystone> waystones = new ArrayList();
        for (int i = 0; i < waystoneCount; i++) {
            waystones.add(Waystone.read(buf));
        }
        return new KnownWaystonesMessage(type, waystones);
    }

    public static void handle(Player player, KnownWaystonesMessage message) {
        if (message.type.equals(WaystoneTypes.WAYSTONE)) {
            InMemoryPlayerWaystoneData playerWaystoneData = (InMemoryPlayerWaystoneData) PlayerWaystoneManager.getPlayerWaystoneData(BalmEnvironment.CLIENT);
            playerWaystoneData.setWaystones(message.waystones);
            Balm.getEvents().fireEvent(new KnownWaystonesEvent(message.waystones));
        }
        for (IWaystone waystone : message.waystones) {
            WaystoneManager.get(player.m_20194_()).updateWaystone(waystone);
        }
    }
}
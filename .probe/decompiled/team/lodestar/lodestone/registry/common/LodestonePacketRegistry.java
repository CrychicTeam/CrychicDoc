package team.lodestar.lodestone.registry.common;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import team.lodestar.lodestone.LodestoneLib;
import team.lodestar.lodestone.network.ClearFireEffectInstancePacket;
import team.lodestar.lodestone.network.TotemOfUndyingEffectPacket;
import team.lodestar.lodestone.network.capability.SyncLodestoneEntityCapabilityPacket;
import team.lodestar.lodestone.network.capability.SyncLodestonePlayerCapabilityPacket;
import team.lodestar.lodestone.network.interaction.ResetRightClickDelayPacket;
import team.lodestar.lodestone.network.interaction.RightClickEmptyPacket;
import team.lodestar.lodestone.network.interaction.UpdateLeftClickPacket;
import team.lodestar.lodestone.network.interaction.UpdateRightClickPacket;
import team.lodestar.lodestone.network.screenshake.PositionedScreenshakePacket;
import team.lodestar.lodestone.network.screenshake.ScreenshakePacket;
import team.lodestar.lodestone.network.worldevent.SyncWorldEventPacket;
import team.lodestar.lodestone.network.worldevent.UpdateWorldEventPacket;

@EventBusSubscriber(modid = "lodestone", bus = Bus.MOD)
public class LodestonePacketRegistry {

    public static final String PROTOCOL_VERSION = "1";

    public static SimpleChannel LODESTONE_CHANNEL = NetworkRegistry.newSimpleChannel(LodestoneLib.lodestonePath("main"), () -> "1", "1"::equals, "1"::equals);

    @SubscribeEvent
    public static void registerPackets(FMLCommonSetupEvent event) {
        int index = 0;
        SyncLodestonePlayerCapabilityPacket.register(LODESTONE_CHANNEL, index++);
        SyncLodestoneEntityCapabilityPacket.register(LODESTONE_CHANNEL, index++);
        ClearFireEffectInstancePacket.register(LODESTONE_CHANNEL, index++);
        ScreenshakePacket.register(LODESTONE_CHANNEL, index++);
        PositionedScreenshakePacket.register(LODESTONE_CHANNEL, index++);
        SyncWorldEventPacket.register(LODESTONE_CHANNEL, index++);
        UpdateWorldEventPacket.register(LODESTONE_CHANNEL, index++);
        RightClickEmptyPacket.register(LODESTONE_CHANNEL, index++);
        UpdateLeftClickPacket.register(LODESTONE_CHANNEL, index++);
        UpdateRightClickPacket.register(LODESTONE_CHANNEL, index++);
        ResetRightClickDelayPacket.register(LODESTONE_CHANNEL, index++);
        TotemOfUndyingEffectPacket.register(LODESTONE_CHANNEL, index++);
    }
}
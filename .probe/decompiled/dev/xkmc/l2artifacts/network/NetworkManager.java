package dev.xkmc.l2artifacts.network;

import dev.xkmc.l2artifacts.content.config.ArtifactSetConfig;
import dev.xkmc.l2artifacts.content.config.LinearFuncConfig;
import dev.xkmc.l2artifacts.content.config.SlotStatConfig;
import dev.xkmc.l2artifacts.content.config.StatTypeConfig;
import dev.xkmc.l2library.serial.config.ConfigTypeEntry;
import dev.xkmc.l2library.serial.config.PacketHandlerWithConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;

public class NetworkManager {

    public static final PacketHandlerWithConfig HANDLER = new PacketHandlerWithConfig(new ResourceLocation("l2artifacts", "main"), 1, e -> e.create(ChooseArtifactToServer.class, NetworkDirection.PLAY_TO_SERVER), e -> e.create(SetFilterToServer.class, NetworkDirection.PLAY_TO_SERVER));

    public static final ConfigTypeEntry<ArtifactSetConfig> ARTIFACT_SETS = new ConfigTypeEntry<>(HANDLER, "artifact_sets", ArtifactSetConfig.class);

    public static final ConfigTypeEntry<SlotStatConfig> SLOT_STATS = new ConfigTypeEntry<>(HANDLER, "slot_stats", SlotStatConfig.class);

    public static final ConfigTypeEntry<StatTypeConfig> STAT_TYPES = new ConfigTypeEntry<>(HANDLER, "stat_types", StatTypeConfig.class);

    public static final ConfigTypeEntry<LinearFuncConfig> LINEAR = new ConfigTypeEntry<>(HANDLER, "linear", LinearFuncConfig.class);

    public static void register() {
    }
}
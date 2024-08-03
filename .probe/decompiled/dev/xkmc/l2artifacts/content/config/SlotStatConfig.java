package dev.xkmc.l2artifacts.content.config;

import dev.xkmc.l2artifacts.content.core.ArtifactSlot;
import dev.xkmc.l2artifacts.network.NetworkManager;
import dev.xkmc.l2library.serial.config.BaseConfig;
import dev.xkmc.l2library.serial.config.CollectType;
import dev.xkmc.l2library.serial.config.ConfigCollect;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.l2serial.serialization.SerialClass.SerialField;
import java.util.ArrayList;
import java.util.HashMap;
import net.minecraft.resources.ResourceLocation;

@SerialClass
public class SlotStatConfig extends BaseConfig {

    @ConfigCollect(CollectType.MAP_COLLECT)
    @SerialField
    public HashMap<ArtifactSlot, ArrayList<ResourceLocation>> available_main_stats = new HashMap();

    @ConfigCollect(CollectType.MAP_COLLECT)
    @SerialField
    public HashMap<ArtifactSlot, ArrayList<ResourceLocation>> available_sub_stats = new HashMap();

    public static SlotStatConfig getInstance() {
        return NetworkManager.SLOT_STATS.getMerged();
    }
}
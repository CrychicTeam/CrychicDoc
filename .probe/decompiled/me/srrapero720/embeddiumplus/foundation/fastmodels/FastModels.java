package me.srrapero720.embeddiumplus.foundation.fastmodels;

import com.jozufozu.flywheel.config.BackendType;
import com.jozufozu.flywheel.config.FlwConfig;
import me.srrapero720.embeddiumplus.EmbyTools;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(value = { Dist.CLIENT }, bus = Bus.FORGE, modid = "embeddiumplus")
public class FastModels {

    public static boolean canUseOnChests() {
        return EmbyTools.isModInstalled("flywheel") ? FlwConfig.get().getBackendType() == BackendType.OFF : !EmbyTools.isModInstalled("enhancedblockentities");
    }
}
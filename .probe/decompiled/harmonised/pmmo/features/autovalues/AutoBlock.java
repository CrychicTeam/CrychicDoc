package harmonised.pmmo.features.autovalues;

import harmonised.pmmo.api.enums.EventType;
import harmonised.pmmo.api.enums.ReqType;
import harmonised.pmmo.util.Reference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.ForgeRegistries;

public class AutoBlock {

    private static final double BASE_HARDNESS = 4.0;

    public static final ReqType[] REQTYPES = new ReqType[] { ReqType.BREAK };

    public static final EventType[] EVENTTYPES = new EventType[] { EventType.BLOCK_BREAK, EventType.BLOCK_PLACE, EventType.GROW };

    private static final List<String> WORLD_SENSITIVE_MOD_IDS = List.of("dynamictrees", "dtbop");

    public static Map<String, Integer> processReqs(ReqType type, ResourceLocation blockID) {
        if (type.blockApplicable && !isWorldSensitive(blockID)) {
            Block block = ForgeRegistries.BLOCKS.getValue(blockID);
            Map<String, Integer> outMap = new HashMap();
            switch(type) {
                case BREAK:
                    if (!block.equals(Blocks.WATER)) {
                        float breakSpeed = block.defaultBlockState().m_60800_(null, null);
                        AutoValueConfig.getBlockReq(type).forEach((skill, level) -> outMap.put(skill, (int) Math.max(0.0, ((double) breakSpeed - 4.0) * AutoValueConfig.HARDNESS_MODIFIER.get())));
                    }
                default:
                    return outMap;
            }
        } else {
            return new HashMap();
        }
    }

    public static Map<String, Long> processXpGains(EventType type, ResourceLocation blockID) {
        if (type.blockApplicable && !isWorldSensitive(blockID)) {
            Block block = ForgeRegistries.BLOCKS.getValue(blockID);
            Map<String, Long> outMap = new HashMap();
            switch(type) {
                case BLOCK_BREAK:
                case BLOCK_PLACE:
                    if (ForgeRegistries.BLOCKS.tags().getTag(Reference.CROPS).contains(block)) {
                        outMap.putAll(AutoValueConfig.getBlockXpAward(EventType.GROW));
                    } else if (ForgeRegistries.BLOCKS.tags().getTag(Reference.MINABLE_AXE).contains(block)) {
                        outMap.putAll(AutoValueConfig.AXE_OVERRIDE.get());
                    } else if (ForgeRegistries.BLOCKS.tags().getTag(Reference.MINABLE_HOE).contains(block)) {
                        outMap.putAll(AutoValueConfig.HOE_OVERRIDE.get());
                    } else if (ForgeRegistries.BLOCKS.tags().getTag(Reference.MINABLE_SHOVEL).contains(block)) {
                        outMap.putAll(AutoValueConfig.SHOVEL_OVERRIDE.get());
                    } else {
                        AutoValueConfig.getBlockXpAward(type).forEach((skill, level) -> {
                            float breakSpeed = Math.max(1.0F, block.defaultBlockState().m_60800_(null, null));
                            long xpOut = Double.valueOf(Math.max(1.0, (double) breakSpeed * AutoValueConfig.HARDNESS_MODIFIER.get() * (double) level.longValue())).longValue();
                            if (ForgeRegistries.BLOCKS.tags().getTag(Tags.Blocks.ORES).contains(block)) {
                                xpOut = (long) ((double) xpOut * AutoValueConfig.RARITIES_MODIFIER.get());
                            }
                            outMap.put(skill, xpOut);
                        });
                    }
                    break;
                case GROW:
                    if (block instanceof CropBlock) {
                        outMap.putAll(AutoValueConfig.getBlockXpAward(type));
                    }
            }
            return outMap;
        } else {
            return new HashMap();
        }
    }

    private static boolean isWorldSensitive(ResourceLocation id) {
        return WORLD_SENSITIVE_MOD_IDS.contains(id.getNamespace());
    }
}
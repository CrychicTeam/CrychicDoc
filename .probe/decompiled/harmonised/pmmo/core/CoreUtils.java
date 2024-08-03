package harmonised.pmmo.core;

import harmonised.pmmo.config.Config;
import harmonised.pmmo.config.SkillsConfig;
import harmonised.pmmo.config.codecs.CodecTypes;
import harmonised.pmmo.config.codecs.SkillData;
import harmonised.pmmo.features.autovalues.AutoValueConfig;
import harmonised.pmmo.util.MsLoggy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraftforge.registries.ForgeRegistries;

public class CoreUtils {

    public static Map<String, Long> deserializeAwardMap(CompoundTag nbt) {
        return new HashMap((Map) CodecTypes.LONG_CODEC.parse(NbtOps.INSTANCE, nbt).resultOrPartial(str -> MsLoggy.ERROR.log(MsLoggy.LOG_CODE.API, "Error Deserializing Award Map from API: {}", str)).orElse(new HashMap()));
    }

    public static Map<String, Long> mergeXpMapsWithSummateCondition(Map<String, Long> ogMap, Map<String, Long> newMap) {
        boolean summate = Config.SUMMATED_MAPS.get();
        if (!summate) {
            return newMap;
        } else {
            for (Entry<String, Long> entry : newMap.entrySet()) {
                ogMap.merge((String) entry.getKey(), (Long) entry.getValue(), (a, b) -> a > b ? a : b);
            }
            return ogMap;
        }
    }

    public static Map<String, Long> applyXpModifiers(Map<String, Long> mapIn, Map<String, Double> modifiers) {
        modifiers.forEach((skill, modifier) -> mapIn.computeIfPresent(skill, (key, xp) -> (long) ((double) xp.longValue() * modifier)));
        return mapIn;
    }

    public static Map<String, Long> processSkillGroupXP(Map<String, Long> map) {
        new HashMap(map).forEach((skill, baseXP) -> {
            SkillData data = (SkillData) SkillsConfig.SKILLS.get().getOrDefault(skill, SkillData.Builder.getDefault());
            if (data.isSkillGroup()) {
                map.remove(skill);
                data.getGroupXP(baseXP).forEach((member, xp) -> map.merge(member, xp, Long::sum));
            }
        });
        return map;
    }

    public static Map<String, Integer> processSkillGroupReqs(Map<String, Integer> map) {
        new HashMap(map).forEach((skill, level) -> {
            SkillData data = (SkillData) SkillsConfig.SKILLS.get().getOrDefault(skill, SkillData.Builder.getDefault());
            if (data.isSkillGroup() && !data.getUseTotalLevels()) {
                map.remove(skill);
                data.getGroupReq(level).forEach((member, xp) -> map.merge(member, xp, Integer::sum));
            }
        });
        return map;
    }

    public static Map<String, Double> processSkillGroupBonus(Map<String, Double> map) {
        new HashMap(map).forEach((skill, level) -> {
            SkillData data = (SkillData) SkillsConfig.SKILLS.get().getOrDefault(skill, SkillData.Builder.getDefault());
            if (data.isSkillGroup()) {
                map.remove(skill);
                map.putAll(data.getGroupBonus(level));
            }
        });
        return map;
    }

    public static int getSkillColor(String skill) {
        return ((SkillData) SkillsConfig.SKILLS.get().getOrDefault(skill, SkillData.Builder.getDefault())).getColor();
    }

    public static Style getSkillStyle(String skill, double transparency) {
        int skillColor = getSkillColor(skill);
        skillColor = setTransparency(skillColor, transparency);
        return Style.EMPTY.withColor(skillColor);
    }

    public static Style getSkillStyle(String skill) {
        return getSkillStyle(skill, 1.0);
    }

    public static int setTransparency(int color, double transparency) {
        int alpha = (int) (transparency * 255.0);
        return color & 16777215 | alpha << 24;
    }

    public static List<MobEffectInstance> getEffects(Map<ResourceLocation, Integer> config, boolean applyDefaultNegatives) {
        List<MobEffectInstance> effects = new ArrayList();
        if (applyDefaultNegatives && config.isEmpty()) {
            config = AutoValueConfig.ITEM_PENALTIES.get();
        }
        for (Entry<ResourceLocation, Integer> effect : config.entrySet()) {
            MobEffect effectRoot = ForgeRegistries.MOB_EFFECTS.getValue((ResourceLocation) effect.getKey());
            if (effectRoot != null) {
                effects.add(new MobEffectInstance(effectRoot, 75, (Integer) effect.getValue(), true, true));
            }
        }
        return effects;
    }
}
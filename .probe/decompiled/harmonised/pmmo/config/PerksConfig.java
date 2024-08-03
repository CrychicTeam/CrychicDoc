package harmonised.pmmo.config;

import com.mojang.serialization.Codec;
import harmonised.pmmo.api.enums.EventType;
import harmonised.pmmo.config.readers.TomlConfigHelper;
import harmonised.pmmo.util.TagBuilder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.StringTag;
import net.minecraftforge.common.ForgeConfigSpec;

public class PerksConfig {

    public static ForgeConfigSpec SERVER_CONFIG;

    private static final Codec<Map<EventType, List<CompoundTag>>> CODEC = Codec.unboundedMap(EventType.CODEC, CompoundTag.CODEC.listOf());

    public static TomlConfigHelper.ConfigObject<Map<EventType, List<CompoundTag>>> PERK_SETTINGS;

    private static Map<EventType, List<CompoundTag>> defaultSettings;

    private static void buildPerkSettings(ForgeConfigSpec.Builder builder) {
        builder.comment("These settings define which perks are used and the settings which govern them.").push("Perks");
        PERK_SETTINGS = TomlConfigHelper.defineObject(builder, "For_Event", CODEC, defaultSettings);
        builder.pop();
    }

    private static void generateDefaults() {
        defaultSettings = new HashMap();
        List<CompoundTag> bodyList = new ArrayList();
        bodyList.add(TagBuilder.start().withString("perk", "pmmo:break_speed").withString("skill", "mining").withDouble("pickaxe_dig", 0.005).build());
        bodyList.add(TagBuilder.start().withString("perk", "pmmo:break_speed").withString("skill", "excavation").withDouble("shovel_dig", 0.005).build());
        bodyList.add(TagBuilder.start().withString("perk", "pmmo:break_speed").withString("skill", "woodcutting").withDouble("axe_dig", 0.005).build());
        bodyList.add(TagBuilder.start().withString("perk", "pmmo:break_speed").withString("skill", "farming").withDouble("sword_dig", 0.005).withDouble("hoe_dig", 0.005).withDouble("shears_dig", 0.005).build());
        defaultSettings.put(EventType.BREAK_SPEED, new ArrayList(bodyList));
        bodyList.clear();
        bodyList.add(TagBuilder.start().withString("perk", "pmmo:fireworks").withString("skill", "mining").build());
        bodyList.add(TagBuilder.start().withString("perk", "pmmo:attribute").withString("skill", "building").withString("attribute", "forge:reach_distance").withDouble("per_level", 0.05).withDouble("max_boost", 10.0).build());
        bodyList.add(TagBuilder.start().withString("perk", "pmmo:fireworks").withString("skill", "building").build());
        bodyList.add(TagBuilder.start().withString("perk", "pmmo:fireworks").withString("skill", "excavation").build());
        bodyList.add(TagBuilder.start().withString("perk", "pmmo:fireworks").withString("skill", "woodcutting").build());
        bodyList.add(TagBuilder.start().withString("perk", "pmmo:fireworks").withString("skill", "farming").build());
        bodyList.add(TagBuilder.start().withString("perk", "pmmo:attribute").withString("skill", "agility").withString("attribute", "minecraft:generic.movement_speed").withDouble("per_level", 1.5E-5).withDouble("max_boost", 1.0).build());
        bodyList.add(TagBuilder.start().withString("perk", "pmmo:fireworks").withString("skill", "agility").build());
        bodyList.add(TagBuilder.start().withString("perk", "pmmo:attribute").withString("skill", "endurance").withString("attribute", "minecraft:generic.max_health").withDouble("per_level", 0.05).withDouble("max_boost", 10.0).build());
        bodyList.add(TagBuilder.start().withString("perk", "pmmo:fireworks").withString("skill", "endurance").build());
        bodyList.add(TagBuilder.start().withString("perk", "pmmo:attribute").withString("skill", "combat").withString("attribute", "minecraft:generic.attack_damage").withDouble("per_level", 0.005).withDouble("max_boost", 1.0).build());
        bodyList.add(TagBuilder.start().withString("perk", "pmmo:fireworks").withString("skill", "combat").build());
        bodyList.add(TagBuilder.start().withString("perk", "pmmo:fireworks").withString("skill", "gunslinging").build());
        bodyList.add(TagBuilder.start().withString("perk", "pmmo:fireworks").withString("skill", "archery").build());
        bodyList.add(TagBuilder.start().withString("perk", "pmmo:fireworks").withString("skill", "smithing").build());
        bodyList.add(TagBuilder.start().withString("perk", "pmmo:fireworks").withString("skill", "flying").build());
        bodyList.add(TagBuilder.start().withString("perk", "pmmo:fireworks").withString("skill", "swimming").build());
        bodyList.add(TagBuilder.start().withString("perk", "pmmo:fireworks").withString("skill", "sailing").build());
        bodyList.add(TagBuilder.start().withString("perk", "pmmo:fireworks").withString("skill", "fishing").build());
        bodyList.add(TagBuilder.start().withString("perk", "pmmo:fireworks").withString("skill", "crafting").build());
        bodyList.add(TagBuilder.start().withString("perk", "pmmo:fireworks").withString("skill", "magic").build());
        bodyList.add(TagBuilder.start().withString("perk", "ars_scalaes:mana_boost").withString("skill", "magic").withDouble("max_boost", 3000.0).withDouble("per_level", 3.0).build());
        bodyList.add(TagBuilder.start().withString("perk", "ars_scalaes:mana_regen").withString("skill", "magic").withDouble("max_boost", 100.0).withDouble("per_level", 0.06).build());
        bodyList.add(TagBuilder.start().withString("perk", "pmmo:fireworks").withString("skill", "slayer").build());
        bodyList.add(TagBuilder.start().withString("perk", "pmmo:fireworks").withString("skill", "hunter").build());
        bodyList.add(TagBuilder.start().withString("perk", "pmmo:fireworks").withString("skill", "taming").build());
        bodyList.add(TagBuilder.start().withString("perk", "pmmo:fireworks").withString("skill", "cooking").build());
        bodyList.add(TagBuilder.start().withString("perk", "pmmo:fireworks").withString("skill", "alchemy").build());
        defaultSettings.put(EventType.SKILL_UP, new ArrayList(bodyList));
        bodyList.clear();
        bodyList.add(TagBuilder.start().withString("perk", "pmmo:jump_boost").withString("skill", "agility").withDouble("per_level", 5.0E-4).build());
        defaultSettings.put(EventType.JUMP, new ArrayList(bodyList));
        bodyList.clear();
        bodyList.add(TagBuilder.start().withString("perk", "pmmo:jump_boost").withString("skill", "agility").withDouble("per_level", 0.001).build());
        defaultSettings.put(EventType.SPRINT_JUMP, new ArrayList(bodyList));
        bodyList.clear();
        bodyList.add(TagBuilder.start().withString("perk", "pmmo:jump_boost").withString("skill", "agility").withDouble("per_level", 0.0015).build());
        defaultSettings.put(EventType.CROUCH_JUMP, new ArrayList(bodyList));
        bodyList.clear();
        bodyList.add(TagBuilder.start().withString("perk", "pmmo:breath").withString("skill", "swimming").build());
        bodyList.add(TagBuilder.start().withString("perk", "pmmo:effect").withString("skill", "swimming").withString("effect", "minecraft:night_vision").withInt("min_level", 25).build());
        defaultSettings.put(EventType.SUBMERGED, new ArrayList(bodyList));
        bodyList.clear();
        bodyList.add(TagBuilder.start().withString("perk", "pmmo:villager_boost").withString("skill", "charisma").build());
        defaultSettings.put(EventType.ENTITY, new ArrayList(bodyList));
        bodyList.clear();
        bodyList.add(TagBuilder.start().withString("perk", "pmmo:damage_reduce").withString("skill", "agility").withString("for_damage", "minecraft:fall").withDouble("per_level", 0.025).build());
        bodyList.add(TagBuilder.start().withString("perk", "pmmo:damage_reduce").withString("skill", "endurance").withString("for_damage", "minecraft:mob_attack").withDouble("per_level", 0.025).build());
        defaultSettings.put(EventType.RECEIVE_DAMAGE, new ArrayList(bodyList));
        bodyList.clear();
        bodyList.add(TagBuilder.start().withString("perk", "pmmo:damage_boost").withString("skill", "archery").withList("applies_to", StringTag.valueOf("minecraft:bow"), StringTag.valueOf("minecraft:crossbow"), StringTag.valueOf("minecraft:trident")).build());
        bodyList.add(TagBuilder.start().withString("perk", "pmmo:damage_boost").withString("skill", "magic").withList("applies_to", StringTag.valueOf("ars_nouveau:spell_bow")).build());
        bodyList.add(TagBuilder.start().withString("perk", "pmmo:damage_boost").withString("skill", "gunslinging").withList("applies_to", StringTag.valueOf("cgm:pistol"), StringTag.valueOf("cgm:shotgun"), StringTag.valueOf("cgm:rifle")).build());
        defaultSettings.put(EventType.DEAL_DAMAGE, new ArrayList(bodyList));
    }

    static {
        generateDefaults();
        ForgeConfigSpec.Builder SERVER_BUILDER = new ForgeConfigSpec.Builder();
        buildPerkSettings(SERVER_BUILDER);
        SERVER_CONFIG = SERVER_BUILDER.build();
    }
}
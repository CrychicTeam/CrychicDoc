package harmonised.pmmo.config;

import com.mojang.serialization.Codec;
import harmonised.pmmo.config.codecs.SkillData;
import harmonised.pmmo.config.readers.TomlConfigHelper;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;

public class SkillsConfig {

    public static ForgeConfigSpec SERVER_CONFIG;

    public static TomlConfigHelper.ConfigObject<Map<String, SkillData>> SKILLS;

    private static Map<String, SkillData> defaultSkills;

    private static void buildGlobals(ForgeConfigSpec.Builder builder) {
        builder.comment("========================================================================", "", " All skills in pmmo are defined when they are used anywhere in PMMO.", " You do not need to define a skill here to use it. However, defining", " a skills attributes here will give you amore rounded skill list and", " a cleaner looking mod. Note that all the defaults here can be replaced", " if you wish. Also note that when using custom icon sizes they must be", " a square image(eg. 16x16, 24x24, 32x32) and they default to 18x18.", "", "========================================================================").push("Skills");
        SKILLS = TomlConfigHelper.defineObject(builder, "Entry", Codec.unboundedMap(Codec.STRING, SkillData.CODEC), defaultSkills);
        builder.pop();
    }

    private static void generateDefaults() {
        defaultSkills = new HashMap();
        defaultSkills.put("mining", SkillData.Builder.start().withColor(65535).withIcon(new ResourceLocation("textures/mob_effect/haste.png")).build());
        defaultSkills.put("building", SkillData.Builder.start().withColor(65535).withIcon(new ResourceLocation("pmmo:textures/skills/building.png")).withIconSize(32).build());
        defaultSkills.put("excavation", SkillData.Builder.start().withColor(15112448).withIcon(new ResourceLocation("textures/item/iron_shovel.png")).withIconSize(16).build());
        defaultSkills.put("woodcutting", SkillData.Builder.start().withColor(16753434).withIcon(new ResourceLocation("textures/item/iron_axe.png")).withIconSize(16).build());
        defaultSkills.put("farming", SkillData.Builder.start().withColor(58880).withIcon(new ResourceLocation("textures/item/wheat.png")).withIconSize(16).build());
        defaultSkills.put("agility", SkillData.Builder.start().withColor(6736998).withIcon(new ResourceLocation("textures/mob_effect/speed.png")).build());
        defaultSkills.put("endurance", SkillData.Builder.start().withColor(13369344).withIcon(new ResourceLocation("textures/mob_effect/absorption.png")).build());
        defaultSkills.put("combat", SkillData.Builder.start().withColor(16724736).withIcon(new ResourceLocation("textures/mob_effect/strength.png")).build());
        defaultSkills.put("gunslinging", SkillData.Builder.start().withColor(13877667).build());
        defaultSkills.put("archery", SkillData.Builder.start().withColor(16776960).withIcon(new ResourceLocation("textures/item/bow.png")).withIconSize(16).build());
        defaultSkills.put("smithing", SkillData.Builder.start().withColor(15790320).withAfkExempt(true).withIcon(new ResourceLocation("pmmo:textures/skills/smithing.png")).withIconSize(32).build());
        defaultSkills.put("flying", SkillData.Builder.start().withColor(13421823).withIcon(new ResourceLocation("textures/item/elytra.png")).withIconSize(16).build());
        defaultSkills.put("swimming", SkillData.Builder.start().withColor(3368703).withIcon(new ResourceLocation("textures/mob_effect/dolphins_grace.png")).build());
        defaultSkills.put("sailing", SkillData.Builder.start().withColor(10073087).withIcon(new ResourceLocation("textures/item/oak_boat.png")).withIconSize(16).build());
        defaultSkills.put("fishing", SkillData.Builder.start().withColor(52479).withIcon(new ResourceLocation("textures/item/fishing_rod.png")).withIconSize(16).build());
        defaultSkills.put("crafting", SkillData.Builder.start().withColor(16750848).withIcon(new ResourceLocation("pmmo:textures/skills/crafting.png")).withIconSize(32).build());
        defaultSkills.put("magic", SkillData.Builder.start().withColor(255).withIcon(new ResourceLocation("textures/particle/enchanted_hit.png")).withIconSize(8).build());
        defaultSkills.put("slayer", SkillData.Builder.start().withColor(16777215).withIcon(new ResourceLocation("textures/item/netherite_sword.png")).withIconSize(16).build());
        defaultSkills.put("hunter", SkillData.Builder.start().withColor(13596693).withIcon(new ResourceLocation("textures/item/diamond_sword.png")).withIconSize(16).build());
        defaultSkills.put("taming", SkillData.Builder.start().withColor(16777215).withIcon(new ResourceLocation("textures/item/lead.png")).withIconSize(16).build());
        defaultSkills.put("cooking", SkillData.Builder.start().withColor(15112448).withAfkExempt(true).withIcon(new ResourceLocation("textures/item/cooked_mutton.png")).withIconSize(16).build());
        defaultSkills.put("alchemy", SkillData.Builder.start().withColor(15112448).withAfkExempt(true).withIcon(new ResourceLocation("textures/item/potion.png")).withIconSize(16).build());
        defaultSkills.put("engineering", SkillData.Builder.start().withColor(16777215).withMaxLevel(100).withIcon(new ResourceLocation("textures/item/redstone.png")).withIconSize(16).build());
        defaultSkills.put("fightgroup", SkillData.Builder.start().setGroupOf(Map.of("combat", 0.5, "endurance", 0.3, "archery", 0.2)).build());
    }

    static {
        generateDefaults();
        ForgeConfigSpec.Builder SERVER_BUILDER = new ForgeConfigSpec.Builder();
        buildGlobals(SERVER_BUILDER);
        SERVER_CONFIG = SERVER_BUILDER.build();
    }
}
package net.cristellib.api;

import java.util.Set;
import net.cristellib.CristelLibRegistry;
import net.cristellib.StructureConfig;
import net.cristellib.config.ConfigType;
import net.minecraft.resources.ResourceLocation;

@CristelPlugin
public class BuiltInAPI implements CristelLibAPI {

    public static final StructureConfig MINECRAFT_ED = StructureConfig.createWithDefaultConfigPath("cristellib", "minecraftED", ConfigType.ENABLE_DISABLE);

    public static final StructureConfig MINECRAFT_P = StructureConfig.createWithDefaultConfigPath("cristellib", "minecraftP", ConfigType.PLACEMENT);

    @Override
    public void registerConfigs(Set<StructureConfig> sets) {
        sets.add(MINECRAFT_ED);
        sets.add(MINECRAFT_P);
        MINECRAFT_ED.setHeader("    This config file makes it possible to switch off any Minecraft structure.\n    To disable a structure, simply set the value of that structure to \"false\".\n    To change the rarity of a structure category, use the other file in the folder.\n");
        MINECRAFT_P.setHeader("This config file makes it possible to change the spacing, separation, salt (and frequency) of Minecraft's structure sets.\n\tSPACING ---  controls how far a structure can be from others of its kind\n\tSEPARATION --- controls how close to each other two structures of the same type can be.\nKEEP IN MIND THAT SPACING ALWAYS NEEDS TO BE HIGHER THAN SEPARATION.\n");
    }

    @Override
    public void registerStructureSets(CristelLibRegistry registry) {
        registry.registerSetToConfig("minecraft", new ResourceLocation("minecraft", "ancient_cities"), MINECRAFT_ED, MINECRAFT_P);
        registry.registerSetToConfig("minecraft", new ResourceLocation("minecraft", "buried_treasures"), MINECRAFT_ED, MINECRAFT_P);
        registry.registerSetToConfig("minecraft", new ResourceLocation("minecraft", "desert_pyramids"), MINECRAFT_ED, MINECRAFT_P);
        registry.registerSetToConfig("minecraft", new ResourceLocation("minecraft", "end_cities"), MINECRAFT_ED, MINECRAFT_P);
        registry.registerSetToConfig("minecraft", new ResourceLocation("minecraft", "igloos"), MINECRAFT_ED, MINECRAFT_P);
        registry.registerSetToConfig("minecraft", new ResourceLocation("minecraft", "jungle_temples"), MINECRAFT_ED, MINECRAFT_P);
        registry.registerSetToConfig("minecraft", new ResourceLocation("minecraft", "mineshafts"), MINECRAFT_ED, MINECRAFT_P);
        registry.registerSetToConfig("minecraft", new ResourceLocation("minecraft", "nether_complexes"), MINECRAFT_ED, MINECRAFT_P);
        registry.registerSetToConfig("minecraft", new ResourceLocation("minecraft", "nether_fossils"), MINECRAFT_ED, MINECRAFT_P);
        registry.registerSetToConfig("minecraft", new ResourceLocation("minecraft", "ocean_monuments"), MINECRAFT_ED, MINECRAFT_P);
        registry.registerSetToConfig("minecraft", new ResourceLocation("minecraft", "ocean_ruins"), MINECRAFT_ED, MINECRAFT_P);
        registry.registerSetToConfig("minecraft", new ResourceLocation("minecraft", "pillager_outposts"), MINECRAFT_ED, MINECRAFT_P);
        registry.registerSetToConfig("minecraft", new ResourceLocation("minecraft", "ruined_portals"), MINECRAFT_ED, MINECRAFT_P);
        registry.registerSetToConfig("minecraft", new ResourceLocation("minecraft", "shipwrecks"), MINECRAFT_ED, MINECRAFT_P);
        registry.registerSetToConfig("minecraft", new ResourceLocation("minecraft", "swamp_huts"), MINECRAFT_ED, MINECRAFT_P);
        registry.registerSetToConfig("minecraft", new ResourceLocation("minecraft", "villages"), MINECRAFT_ED, MINECRAFT_P);
        registry.registerSetToConfig("minecraft", new ResourceLocation("minecraft", "woodland_mansions"), MINECRAFT_ED, MINECRAFT_P);
        registry.registerSetToConfig("minecraft", new ResourceLocation("minecraft", "strongholds"), MINECRAFT_ED);
    }
}
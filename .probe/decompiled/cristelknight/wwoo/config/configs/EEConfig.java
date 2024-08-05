package cristelknight.wwoo.config.configs;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import cristelknight.wwoo.config.jankson.config.CommentedConfig;
import java.util.HashMap;
import net.minecraft.Util;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record EEConfig(String mode, boolean forceLargeBiomes, boolean removeOreBlobs, boolean showUpdates, boolean showBigUpdates, BlockState backGroundBlock) implements CommentedConfig<EEConfig> {

    private static EEConfig INSTANCE = null;

    public static final EEConfig DEFAULT = new EEConfig("DEFAULT", false, true, true, true, Blocks.CALCITE.defaultBlockState());

    public static final Codec<EEConfig> CODEC = RecordCodecBuilder.create(builder -> builder.group(Codec.STRING.fieldOf("mode").orElse("DEFAULT").forGetter(config -> config.mode), Codec.BOOL.fieldOf("forceLargeBiomes").orElse(false).forGetter(config -> config.forceLargeBiomes), Codec.BOOL.fieldOf("removeOreBlobs").orElse(true).forGetter(config -> config.removeOreBlobs), Codec.BOOL.fieldOf("showUpdates").orElse(true).forGetter(config -> config.showUpdates), Codec.BOOL.fieldOf("showBigUpdates").orElse(true).forGetter(config -> config.showBigUpdates), BlockState.CODEC.fieldOf("backGroundBlock").orElse(Blocks.CALCITE.defaultBlockState()).forGetter(config -> config.backGroundBlock)).apply(builder, EEConfig::new));

    @Override
    public String getSubPath() {
        return "expanded_ecosphere/config";
    }

    public EEConfig getInstance() {
        return INSTANCE;
    }

    public EEConfig getDefault() {
        return DEFAULT;
    }

    @Override
    public Codec<EEConfig> getCodec() {
        return CODEC;
    }

    @NotNull
    @Override
    public HashMap<String, String> getComments() {
        return Util.make(new HashMap(), map -> {
            map.put("removeOreBlobs", "Removes underground ores (andesite, diorite, granite, gravel and dirt)");
            map.put("mode", "Type DEFAULT or COMPATIBLE, for changing modes.\nCOMPATIBLE allows compat with Terralith and disabling biomes in banned_biomes.json5,\nbut it requires Terrablender and is a bit unstable.");
            map.put("showUpdates", "Whether updates should be announced in chat when entering a world");
            map.put("forceLargeBiomes", "Whether to force Minecraft to generate Large Biomes or not.\nThis option forces Minecraft to generate Large Biomes, this setting was introduced,\nbecause the default presets do not work (correctly).\nThis option will probably not work with Compatible mode, or other biome mods.");
            map.put("showBigUpdates", "Whether to announce important updates even if the above setting is disabled.");
            map.put("backGroundBlock", "Selects the background block in the config screen. Only makes sense in-game.");
        });
    }

    @Nullable
    @Override
    public String getHeader() {
        return String.format("Expanded Ecosphere Main Config\n\n===========\nDiscord: %s\nModrinth: %s\nCurseForge: %s", "https://discord.com/invite/yJng7sC44x", "https://modrinth.com/mod/expanded-ecosphere", "https://www.curseforge.com/minecraft/mc-mods/expanded-ecosphere");
    }

    @Override
    public boolean isSorted() {
        return false;
    }

    public void setInstance(EEConfig instance) {
        INSTANCE = instance;
    }
}
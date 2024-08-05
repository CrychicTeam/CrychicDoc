package cristelknight.wwoo.config.configs;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import cristelknight.wwoo.config.jankson.config.CommentedConfig;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.Util;

public record ReplaceBiomesConfig(boolean enableBiomes, Map<String, String> bannedBiomes) implements CommentedConfig<ReplaceBiomesConfig> {

    private static ReplaceBiomesConfig INSTANCE = null;

    public static final ReplaceBiomesConfig DEFAULT = new ReplaceBiomesConfig(false, Map.of());

    public static final Codec<ReplaceBiomesConfig> CODEC = RecordCodecBuilder.create(builder -> builder.group(Codec.BOOL.fieldOf("enableBiomes").forGetter(c -> c.enableBiomes), Codec.unboundedMap(Codec.STRING, Codec.STRING).fieldOf("replace").forGetter(c -> c.bannedBiomes)).apply(builder, ReplaceBiomesConfig::new));

    @Override
    public String getSubPath() {
        return "expanded_ecosphere/replace_biomes";
    }

    public ReplaceBiomesConfig getInstance() {
        return INSTANCE;
    }

    public ReplaceBiomesConfig getDefault() {
        return DEFAULT;
    }

    @Override
    public Codec<ReplaceBiomesConfig> getCodec() {
        return CODEC;
    }

    @Override
    public HashMap<String, String> getComments() {
        return Util.make(new HashMap(), map -> {
            map.put("enableBiomes", "Enable biome replacing");
            map.put("replace", "Enter all Expanded Ecosphere biomes that should be replaced into this map.\nAs the key put the biome you want to replace and as value the biome you want to replace it with.\nHere is an example:\n\"replace\": {\n    \"minecraft:plains\": \"minecraft:basalt_deltas\",\n    \"wythers:bayou\": \"minecraft:river\"\n}\nOnly biomes which are added trough Expanded Ecosphere will work.\nAlso on compatible mode you can only replace Expanded Ecosphere biomes and only replace them with non vanilla biomes.\nIf a biome doesn't exist, the game will crash!");
        });
    }

    @Override
    public String getHeader() {
        return "Expanded Ecosphere Replace Config\n\n===========\nThis config is only for replacing biomes, the main Expanded Ecosphere config is in the other file.";
    }

    @Override
    public boolean isSorted() {
        return false;
    }

    public void setInstance(ReplaceBiomesConfig instance) {
        INSTANCE = instance;
    }
}
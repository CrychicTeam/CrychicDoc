package net.minecraft.world.level;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;

public class DataPackConfig {

    public static final DataPackConfig DEFAULT = new DataPackConfig(ImmutableList.of("vanilla"), ImmutableList.of());

    public static final Codec<DataPackConfig> CODEC = RecordCodecBuilder.create(p_45854_ -> p_45854_.group(Codec.STRING.listOf().fieldOf("Enabled").forGetter(p_151457_ -> p_151457_.enabled), Codec.STRING.listOf().fieldOf("Disabled").forGetter(p_151455_ -> p_151455_.disabled)).apply(p_45854_, DataPackConfig::new));

    private final List<String> enabled;

    private final List<String> disabled;

    public DataPackConfig(List<String> listString0, List<String> listString1) {
        this.enabled = ImmutableList.copyOf(listString0);
        this.disabled = ImmutableList.copyOf(listString1);
    }

    public List<String> getEnabled() {
        return this.enabled;
    }

    public List<String> getDisabled() {
        return this.disabled;
    }
}
package dev.latvian.mods.kubejs.level.gen.filter.biome;

import dev.architectury.registry.level.biome.BiomeModifications;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.resources.ResourceLocation;

public record RegexIDFilter(Pattern pattern) implements BiomeFilter {

    @Override
    public boolean test(BiomeModifications.BiomeContext ctx) {
        return (Boolean) ctx.getKey().map(ResourceLocation::toString).map(this.pattern::matcher).map(Matcher::find).orElse(false);
    }
}
package io.github.steveplays28.noisium.mixin;

import com.google.common.collect.ImmutableMap;
import io.github.steveplays28.noisium.compat.lithium.NoisiumLithiumCompat;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

public class NoisiumMixinPlugin implements IMixinConfigPlugin {

    private static final Supplier<Boolean> TRUE = () -> true;

    private static final Map<String, Supplier<Boolean>> CONDITIONS = ImmutableMap.of("io.github.steveplays28.noisium.mixin.NoiseChunkGeneratorMixin", (Supplier) () -> !NoisiumLithiumCompat.isLithiumLoaded(), "io.github.steveplays28.noisium.mixin.compat.lithium.LithiumNoiseChunkGeneratorMixin", NoisiumLithiumCompat::isLithiumLoaded);

    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        return (Boolean) ((Supplier) CONDITIONS.getOrDefault(mixinClassName, TRUE)).get();
    }

    public void onLoad(String mixinPackage) {
    }

    public String getRefMapperConfig() {
        return null;
    }

    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
    }

    public List<String> getMixins() {
        return null;
    }

    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }

    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }
}
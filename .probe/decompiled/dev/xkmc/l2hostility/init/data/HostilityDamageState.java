package dev.xkmc.l2hostility.init.data;

import dev.xkmc.l2damagetracker.contents.damage.DamageState;
import java.util.Locale;
import java.util.function.Consumer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;

public enum HostilityDamageState implements DamageState {

    BYPASS_COOLDOWN(DamageTypeTags.BYPASSES_COOLDOWN);

    private final TagKey<DamageType>[] tags;

    @SafeVarargs
    private HostilityDamageState(TagKey<DamageType>... tags) {
        this.tags = tags;
    }

    public void gatherTags(Consumer<TagKey<DamageType>> collector) {
        for (TagKey<DamageType> tag : this.tags) {
            collector.accept(tag);
        }
    }

    public void removeTags(Consumer<TagKey<DamageType>> consumer) {
    }

    public ResourceLocation getId() {
        return new ResourceLocation("l2hostility", this.name().toLowerCase(Locale.ROOT));
    }

    public boolean overrides(DamageState state) {
        return false;
    }
}
package dev.xkmc.l2archery.init.data;

import dev.xkmc.l2damagetracker.contents.damage.DamageState;
import dev.xkmc.l2damagetracker.init.data.L2DamageTypes;
import java.util.Locale;
import java.util.function.Consumer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;

public enum ArcheryDamageState implements DamageState {

    BYPASS_INVUL(L2DamageTypes.BYPASS_INVUL.tags());

    private final TagKey<DamageType>[] tags;

    @SafeVarargs
    private ArcheryDamageState(TagKey<DamageType>... tags) {
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
        return new ResourceLocation("l2archery", this.name().toLowerCase(Locale.ROOT));
    }

    public boolean overrides(DamageState state) {
        return true;
    }
}
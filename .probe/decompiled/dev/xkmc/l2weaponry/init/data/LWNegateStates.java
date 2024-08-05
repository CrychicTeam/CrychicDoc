package dev.xkmc.l2weaponry.init.data;

import dev.xkmc.l2damagetracker.contents.damage.DamageState;
import java.util.Locale;
import java.util.function.Consumer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;

public enum LWNegateStates implements DamageState {

    NO_PROJECTILE(DamageTypeTags.IS_PROJECTILE);

    private final TagKey<DamageType> tag;

    private final ResourceLocation id;

    private LWNegateStates(TagKey<DamageType> tag) {
        this.tag = tag;
        this.id = new ResourceLocation("l2weaponry", this.name().toLowerCase(Locale.ROOT));
    }

    public void gatherTags(Consumer<TagKey<DamageType>> consumer) {
    }

    public void removeTags(Consumer<TagKey<DamageType>> consumer) {
        consumer.accept(this.tag);
    }

    public ResourceLocation getId() {
        return this.id;
    }
}
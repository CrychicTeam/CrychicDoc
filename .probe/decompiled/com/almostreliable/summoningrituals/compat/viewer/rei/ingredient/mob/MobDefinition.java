package com.almostreliable.summoningrituals.compat.viewer.rei.ingredient.mob;

import com.almostreliable.summoningrituals.compat.viewer.common.MobIngredient;
import com.almostreliable.summoningrituals.compat.viewer.rei.AlmostREI;
import com.almostreliable.summoningrituals.platform.Platform;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import me.shedaniel.rei.api.client.entry.renderer.EntryRenderer;
import me.shedaniel.rei.api.common.entry.EntrySerializer;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.entry.comparison.ComparisonContext;
import me.shedaniel.rei.api.common.entry.type.EntryDefinition;
import me.shedaniel.rei.api.common.entry.type.EntryType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;

public class MobDefinition implements EntryDefinition<MobIngredient> {

    private final REIMobRenderer renderer;

    public MobDefinition(int size) {
        this.renderer = new REIMobRenderer(size);
    }

    public Class<MobIngredient> getValueType() {
        return MobIngredient.class;
    }

    public EntryType<MobIngredient> getType() {
        return AlmostREI.MOB;
    }

    public EntryRenderer<MobIngredient> getRenderer() {
        return this.renderer;
    }

    @Nullable
    public ResourceLocation getIdentifier(EntryStack<MobIngredient> entry, MobIngredient mob) {
        return Platform.getId(mob.getEntityType());
    }

    public boolean isEmpty(EntryStack<MobIngredient> entry, MobIngredient mob) {
        return false;
    }

    public MobIngredient copy(EntryStack<MobIngredient> entry, MobIngredient mob) {
        return new MobIngredient(mob.getEntityType(), mob.getCount(), mob.getTag());
    }

    public MobIngredient normalize(EntryStack<MobIngredient> entry, MobIngredient mob) {
        return new MobIngredient(mob.getEntityType(), mob.getCount());
    }

    public MobIngredient wildcard(EntryStack<MobIngredient> entry, MobIngredient mob) {
        return new MobIngredient(mob.getEntityType(), mob.getCount());
    }

    public long hash(EntryStack<MobIngredient> entry, MobIngredient mob, ComparisonContext context) {
        int code = Platform.getId(mob.getEntityType()).hashCode();
        code = 31 * code + mob.getTag().hashCode();
        return (long) code;
    }

    public boolean equals(MobIngredient mob1, MobIngredient mob2, ComparisonContext context) {
        return mob1.getEntityType() == mob2.getEntityType() && mob1.getTag().equals(mob2.getTag());
    }

    @Nullable
    public EntrySerializer<MobIngredient> getSerializer() {
        return null;
    }

    public Component asFormattedText(EntryStack<MobIngredient> entry, MobIngredient mob) {
        return mob.getDisplayName();
    }

    public Stream<? extends TagKey<?>> getTagsFor(EntryStack<MobIngredient> entry, MobIngredient mob) {
        return Platform.getTagsFor(mob.getEntityType());
    }
}
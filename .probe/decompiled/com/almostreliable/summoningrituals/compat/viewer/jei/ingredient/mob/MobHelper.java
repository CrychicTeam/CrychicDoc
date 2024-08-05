package com.almostreliable.summoningrituals.compat.viewer.jei.ingredient.mob;

import com.almostreliable.summoningrituals.compat.viewer.common.MobIngredient;
import com.almostreliable.summoningrituals.compat.viewer.jei.AlmostJEI;
import com.almostreliable.summoningrituals.platform.Platform;
import javax.annotation.Nullable;
import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.resources.ResourceLocation;

public class MobHelper implements IIngredientHelper<MobIngredient> {

    @Override
    public IIngredientType<MobIngredient> getIngredientType() {
        return AlmostJEI.MOB;
    }

    public String getDisplayName(MobIngredient mob) {
        return mob.getDisplayName().getString();
    }

    public String getUniqueId(MobIngredient mob, UidContext context) {
        return Platform.getId(mob.getEntityType()).toString();
    }

    public ResourceLocation getResourceLocation(MobIngredient mob) {
        return Platform.getId(mob.getEntityType());
    }

    public MobIngredient copyIngredient(MobIngredient mob) {
        return new MobIngredient(mob.getEntityType(), mob.getCount(), mob.getTag());
    }

    public String getErrorInfo(@Nullable MobIngredient mob) {
        return mob == null ? "Null entity" : "Entity: " + Platform.getId(mob.getEntityType());
    }
}
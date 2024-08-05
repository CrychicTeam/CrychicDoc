package com.simibubi.create.compat.jei;

import com.simibubi.create.content.fluids.potion.PotionFluid;
import com.simibubi.create.foundation.utility.NBTHelper;
import java.util.List;
import mezz.jei.api.ingredients.subtypes.IIngredientSubtypeInterpreter;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraftforge.fluids.FluidStack;

public class PotionFluidSubtypeInterpreter implements IIngredientSubtypeInterpreter<FluidStack> {

    public String apply(FluidStack ingredient, UidContext context) {
        if (!ingredient.hasTag()) {
            return "";
        } else {
            CompoundTag tag = ingredient.getOrCreateTag();
            Potion potionType = PotionUtils.getPotion(tag);
            String potionTypeString = potionType.getName("");
            String bottleType = ((PotionFluid.BottleType) NBTHelper.readEnum(tag, "Bottle", PotionFluid.BottleType.class)).toString();
            StringBuilder stringBuilder = new StringBuilder(potionTypeString);
            List<MobEffectInstance> effects = PotionUtils.getCustomEffects(tag);
            stringBuilder.append(";").append(bottleType);
            for (MobEffectInstance effect : potionType.getEffects()) {
                stringBuilder.append(";").append(effect);
            }
            for (MobEffectInstance effect : effects) {
                stringBuilder.append(";").append(effect);
            }
            return stringBuilder.toString();
        }
    }
}
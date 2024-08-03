package com.almostreliable.summoningrituals.compat.viewer.jei.ingredient.block;

import com.almostreliable.summoningrituals.compat.viewer.jei.AlmostJEI;
import com.almostreliable.summoningrituals.platform.Platform;
import com.almostreliable.summoningrituals.recipe.component.BlockReference;
import javax.annotation.Nullable;
import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;

public class BlockReferenceHelper implements IIngredientHelper<BlockReference> {

    @Override
    public IIngredientType<BlockReference> getIngredientType() {
        return AlmostJEI.BLOCK_REFERENCE;
    }

    public String getDisplayName(BlockReference blockReference) {
        MutableComponent displayName = blockReference.getDisplayState().m_60734_().getName();
        return displayName.getString();
    }

    public String getUniqueId(BlockReference blockReference, UidContext context) {
        return Platform.getId(blockReference.getDisplayState().m_60734_()).toString();
    }

    public ResourceLocation getResourceLocation(BlockReference blockReference) {
        return Platform.getId(blockReference.getDisplayState().m_60734_());
    }

    public BlockReference copyIngredient(BlockReference blockReference) {
        return blockReference;
    }

    public String getErrorInfo(@Nullable BlockReference blockReference) {
        return blockReference == null ? "Null block reference" : "Block: " + blockReference.getDisplayState().m_60734_().getName() + "\nState: " + blockReference.getDisplayState();
    }
}
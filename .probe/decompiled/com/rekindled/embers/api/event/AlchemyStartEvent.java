package com.rekindled.embers.api.event;

import com.rekindled.embers.recipe.AlchemyContext;
import com.rekindled.embers.recipe.IAlchemyRecipe;
import net.minecraft.world.level.block.entity.BlockEntity;

public class AlchemyStartEvent extends UpgradeEvent {

    public AlchemyContext context;

    IAlchemyRecipe recipe;

    public AlchemyStartEvent(BlockEntity tile, AlchemyContext context, IAlchemyRecipe recipe) {
        super(tile);
        this.context = context;
        this.recipe = recipe;
    }

    public AlchemyContext getContext() {
        return this.context;
    }

    public IAlchemyRecipe getRecipe() {
        return this.recipe;
    }

    public void setRecipe(IAlchemyRecipe recipe) {
        this.recipe = recipe;
    }
}
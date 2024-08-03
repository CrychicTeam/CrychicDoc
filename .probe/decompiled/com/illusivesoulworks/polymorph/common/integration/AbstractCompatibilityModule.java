package com.illusivesoulworks.polymorph.common.integration;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.entity.BlockEntity;

public abstract class AbstractCompatibilityModule {

    protected void setup() {
    }

    protected void clientSetup() {
    }

    protected boolean selectRecipe(AbstractContainerMenu containerMenu, Recipe<?> recipe) {
        return false;
    }

    protected boolean selectRecipe(BlockEntity blockEntity, Recipe<?> recipe) {
        return false;
    }

    protected boolean openContainer(AbstractContainerMenu containerMenu, ServerPlayer serverPlayerEntity) {
        return false;
    }

    protected void disable() {
    }
}
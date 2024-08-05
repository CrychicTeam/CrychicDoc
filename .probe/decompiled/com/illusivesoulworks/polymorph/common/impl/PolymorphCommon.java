package com.illusivesoulworks.polymorph.common.impl;

import com.illusivesoulworks.polymorph.api.common.base.IPolymorphCommon;
import com.illusivesoulworks.polymorph.api.common.base.IPolymorphPacketDistributor;
import com.illusivesoulworks.polymorph.api.common.capability.IBlockEntityRecipeData;
import com.illusivesoulworks.polymorph.api.common.capability.IPlayerRecipeData;
import com.illusivesoulworks.polymorph.api.common.capability.IStackRecipeData;
import com.illusivesoulworks.polymorph.common.capability.PolymorphCapabilities;
import com.illusivesoulworks.polymorph.common.util.BlockEntityTicker;
import com.illusivesoulworks.polymorph.platform.Services;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

public class PolymorphCommon implements IPolymorphCommon {

    private static final IPolymorphCommon INSTANCE = new PolymorphCommon();

    private final List<IPolymorphCommon.IBlockEntity2RecipeData> blockEntity2RecipeData = new LinkedList();

    private final List<IPolymorphCommon.IContainer2BlockEntity> container2BlockEntities = new LinkedList();

    private final List<IPolymorphCommon.IContainer2ItemStack> container2ItemStacks = new LinkedList();

    private final List<IPolymorphCommon.IItemStack2RecipeData> itemStack2RecipeData = new LinkedList();

    private final IPolymorphPacketDistributor distributor = Services.PLATFORM.getPacketDistributor();

    private MinecraftServer server = null;

    public static IPolymorphCommon get() {
        return INSTANCE;
    }

    @Override
    public IPolymorphPacketDistributor getPacketDistributor() {
        return this.distributor;
    }

    @Override
    public void setServer(MinecraftServer server) {
        this.server = server;
        BlockEntityTicker.clear();
    }

    @Override
    public Optional<MinecraftServer> getServer() {
        return Optional.ofNullable(this.server);
    }

    @Override
    public Optional<? extends IBlockEntityRecipeData> tryCreateRecipeData(BlockEntity blockEntity) {
        for (IPolymorphCommon.IBlockEntity2RecipeData function : this.blockEntity2RecipeData) {
            IBlockEntityRecipeData recipeData = function.createRecipeData(blockEntity);
            if (recipeData != null) {
                return Optional.of(recipeData);
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<? extends IBlockEntityRecipeData> getRecipeData(BlockEntity blockEntity) {
        return PolymorphCapabilities.getRecipeData(blockEntity);
    }

    @Override
    public Optional<? extends IBlockEntityRecipeData> getRecipeDataFromBlockEntity(AbstractContainerMenu container) {
        for (IPolymorphCommon.IContainer2BlockEntity function : this.container2BlockEntities) {
            BlockEntity blockEntity = function.getBlockEntity(container);
            if (blockEntity != null) {
                return this.getRecipeData(blockEntity);
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<? extends IStackRecipeData> tryCreateRecipeData(ItemStack stack) {
        for (IPolymorphCommon.IItemStack2RecipeData function : this.itemStack2RecipeData) {
            IStackRecipeData recipeData = function.createRecipeData(stack);
            if (recipeData != null) {
                return Optional.of(recipeData);
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<? extends IStackRecipeData> getRecipeData(ItemStack stack) {
        return PolymorphCapabilities.getRecipeData(stack);
    }

    @Override
    public Optional<? extends IStackRecipeData> getRecipeDataFromItemStack(AbstractContainerMenu pContainer) {
        for (IPolymorphCommon.IContainer2ItemStack function : this.container2ItemStacks) {
            ItemStack itemstack = function.getItemStack(pContainer);
            if (!itemstack.isEmpty()) {
                return this.getRecipeData(itemstack);
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<? extends IPlayerRecipeData> getRecipeData(Player player) {
        return PolymorphCapabilities.getRecipeData(player);
    }

    @Override
    public void registerBlockEntity2RecipeData(IPolymorphCommon.IBlockEntity2RecipeData blockEntity2RecipeData) {
        this.blockEntity2RecipeData.add(blockEntity2RecipeData);
    }

    @Override
    public void registerContainer2BlockEntity(IPolymorphCommon.IContainer2BlockEntity container2BlockEntity) {
        this.container2BlockEntities.add(container2BlockEntity);
    }

    @Override
    public void registerItemStack2RecipeData(IPolymorphCommon.IItemStack2RecipeData stack2RecipeData) {
        this.itemStack2RecipeData.add(stack2RecipeData);
    }

    @Override
    public void registerContainer2ItemStack(IPolymorphCommon.IContainer2ItemStack container2ItemStack) {
        this.container2ItemStacks.add(container2ItemStack);
    }
}
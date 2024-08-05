package com.illusivesoulworks.polymorph.api.common.base;

import com.illusivesoulworks.polymorph.api.common.capability.IBlockEntityRecipeData;
import com.illusivesoulworks.polymorph.api.common.capability.IPlayerRecipeData;
import com.illusivesoulworks.polymorph.api.common.capability.IStackRecipeData;
import java.util.Optional;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

public interface IPolymorphCommon {

    Optional<? extends IBlockEntityRecipeData> tryCreateRecipeData(BlockEntity var1);

    Optional<? extends IBlockEntityRecipeData> getRecipeData(BlockEntity var1);

    Optional<? extends IBlockEntityRecipeData> getRecipeDataFromBlockEntity(AbstractContainerMenu var1);

    Optional<? extends IStackRecipeData> tryCreateRecipeData(ItemStack var1);

    Optional<? extends IStackRecipeData> getRecipeData(ItemStack var1);

    Optional<? extends IStackRecipeData> getRecipeDataFromItemStack(AbstractContainerMenu var1);

    Optional<? extends IPlayerRecipeData> getRecipeData(Player var1);

    void registerBlockEntity2RecipeData(IPolymorphCommon.IBlockEntity2RecipeData var1);

    void registerContainer2BlockEntity(IPolymorphCommon.IContainer2BlockEntity var1);

    void registerItemStack2RecipeData(IPolymorphCommon.IItemStack2RecipeData var1);

    void registerContainer2ItemStack(IPolymorphCommon.IContainer2ItemStack var1);

    IPolymorphPacketDistributor getPacketDistributor();

    void setServer(MinecraftServer var1);

    Optional<MinecraftServer> getServer();

    public interface IBlockEntity2RecipeData {

        IBlockEntityRecipeData createRecipeData(BlockEntity var1);
    }

    public interface IContainer2BlockEntity {

        BlockEntity getBlockEntity(AbstractContainerMenu var1);
    }

    public interface IContainer2ItemStack {

        ItemStack getItemStack(AbstractContainerMenu var1);
    }

    public interface IItemStack2RecipeData {

        IStackRecipeData createRecipeData(ItemStack var1);
    }
}
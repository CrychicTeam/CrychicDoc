package com.illusivesoulworks.polymorph.platform.services;

import com.illusivesoulworks.polymorph.api.common.base.IPolymorphPacketDistributor;
import com.illusivesoulworks.polymorph.api.common.capability.IBlockEntityRecipeData;
import com.illusivesoulworks.polymorph.api.common.capability.IPlayerRecipeData;
import com.illusivesoulworks.polymorph.api.common.capability.IStackRecipeData;
import com.illusivesoulworks.polymorph.common.integration.PolymorphIntegrations;
import java.nio.file.Path;
import java.util.Optional;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.entity.BlockEntity;

public interface IPlatform {

    PolymorphIntegrations.Loader getLoader();

    Path getGameDir();

    Path getConfigDir();

    boolean isModLoaded(String var1);

    boolean isModFileLoaded(String var1);

    boolean isShaped(Recipe<?> var1);

    boolean isSameShape(Recipe<?> var1, Recipe<?> var2);

    Optional<? extends IPlayerRecipeData> getRecipeData(Player var1);

    Optional<? extends IBlockEntityRecipeData> getRecipeData(BlockEntity var1);

    Optional<? extends IStackRecipeData> getRecipeData(ItemStack var1);

    IPolymorphPacketDistributor getPacketDistributor();
}
package com.mna.api.capabilities;

import com.mna.api.cantrips.ICantrip;
import java.util.List;
import java.util.Optional;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.crafting.Recipe;

public interface IPlayerCantrips {

    ICantrip matchAndCastCantrip(Player var1, InteractionHand var2, List<Recipe<?>> var3);

    CompoundTag writeToNBT(boolean var1);

    void readFromNBT(CompoundTag var1);

    void setPattern(ResourceLocation var1, List<ResourceLocation> var2);

    Container getAsInventory();

    boolean needsSync();

    void clearSync();

    void setNeedsSync();

    Optional<IPlayerCantrip> getCantrip(ResourceLocation var1);

    List<IPlayerCantrip> getCantrips();
}
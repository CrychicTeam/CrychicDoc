package com.mna.api.cantrips;

import java.util.List;
import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.apache.logging.log4j.util.TriConsumer;

public interface ICantripRegistry {

    @Nullable
    ICantrip registerCantrip(ResourceLocation var1, ResourceLocation var2, int var3, TriConsumer<Player, ICantrip, InteractionHand> var4, ItemStack var5, ResourceLocation... var6);

    int countRegisteredCantrips();

    Optional<ICantrip> getCantrip(ResourceLocation var1);

    List<ICantrip> getCantrips();

    List<ICantrip> getCantrips(Player var1);

    @Deprecated(forRemoval = true)
    List<ICantrip> getCantrips(int var1);
}
package noobanidus.mods.lootr.api;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface LootFiller {

    void unpackLootTable(@NotNull Player var1, Container var2, ResourceLocation var3, long var4);
}
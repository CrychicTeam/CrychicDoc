package com.mna.api.spells;

import javax.annotation.Nullable;
import net.minecraft.world.entity.player.Player;

public class SpellCraftingContext {

    @Nullable
    private final Player player;

    public SpellCraftingContext(@Nullable Player player) {
        this.player = player;
    }

    @Nullable
    public final Player getPlayer() {
        return this.player;
    }
}
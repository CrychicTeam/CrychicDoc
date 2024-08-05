package org.embeddedt.modernfix.common.mixin.perf.mojang_registry_size;

import com.google.common.collect.ArrayTable;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Table;
import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.state.properties.Property;
import org.embeddedt.modernfix.annotation.RequiresMod;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ StateHolder.class })
@RequiresMod("!ferritecore")
public class StateHolderMixin {

    @Shadow
    private Table<Property<?>, Comparable<?>, ?> neighbours;

    @Inject(method = { "populateNeighbours" }, at = { @At("RETURN") }, require = 0)
    private void replaceEmptyTable(CallbackInfo ci) {
        if ((this.neighbours instanceof ArrayTable || this.neighbours instanceof HashBasedTable) && this.neighbours.isEmpty()) {
            this.neighbours = ImmutableTable.of();
        }
    }
}
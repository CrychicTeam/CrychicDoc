package org.embeddedt.modernfix.common.mixin.perf.worldgen_allocation;

import java.util.List;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.SurfaceRules;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = { SurfaceRules.SequenceRule.class }, priority = 100)
public class SequenceRuleMixin {

    @Shadow
    @Final
    private List<SurfaceRules.SurfaceRule> rules;

    @Overwrite
    public BlockState tryApply(int x, int y, int z) {
        int s = this.rules.size();
        for (int i = 0; i < s; i++) {
            BlockState state = ((SurfaceRules.SurfaceRule) this.rules.get(i)).tryApply(x, y, z);
            if (state != null) {
                return state;
            }
        }
        return null;
    }
}
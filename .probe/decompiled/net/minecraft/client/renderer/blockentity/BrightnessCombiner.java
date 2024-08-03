package net.minecraft.client.renderer.blockentity;

import it.unimi.dsi.fastutil.ints.Int2IntFunction;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.world.level.block.DoubleBlockCombiner;
import net.minecraft.world.level.block.entity.BlockEntity;

public class BrightnessCombiner<S extends BlockEntity> implements DoubleBlockCombiner.Combiner<S, Int2IntFunction> {

    public Int2IntFunction acceptDouble(S s0, S s1) {
        return p_112325_ -> {
            int $$3 = LevelRenderer.getLightColor(s0.getLevel(), s0.getBlockPos());
            int $$4 = LevelRenderer.getLightColor(s1.getLevel(), s1.getBlockPos());
            int $$5 = LightTexture.block($$3);
            int $$6 = LightTexture.block($$4);
            int $$7 = LightTexture.sky($$3);
            int $$8 = LightTexture.sky($$4);
            return LightTexture.pack(Math.max($$5, $$6), Math.max($$7, $$8));
        };
    }

    public Int2IntFunction acceptSingle(S s0) {
        return p_112333_ -> p_112333_;
    }

    public Int2IntFunction acceptNone() {
        return p_112316_ -> p_112316_;
    }
}
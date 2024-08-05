package org.embeddedt.modernfix.common.mixin.perf.dynamic_resources;

import java.lang.ref.SoftReference;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.embeddedt.modernfix.annotation.ClientOnlyMixin;
import org.embeddedt.modernfix.duck.IModelHoldingBlockState;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ BlockBehaviour.BlockStateBase.class })
@ClientOnlyMixin
public class BlockStateBaseMixin implements IModelHoldingBlockState {

    private volatile SoftReference<BakedModel> mfix$model;

    @Override
    public BakedModel mfix$getModel() {
        SoftReference<BakedModel> ref = this.mfix$model;
        return ref != null ? (BakedModel) ref.get() : null;
    }

    @Override
    public void mfix$setModel(BakedModel model) {
        this.mfix$model = model != null ? new SoftReference(model) : null;
    }
}
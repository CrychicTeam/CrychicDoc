package com.simibubi.create.content.contraptions.chassis;

import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.api.instance.DynamicInstance;
import com.jozufozu.flywheel.backend.instancing.blockentity.BlockEntityInstance;
import com.jozufozu.flywheel.core.materials.FlatLit;
import com.jozufozu.flywheel.core.materials.model.ModelData;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.foundation.utility.AngleHelper;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;

public class StickerInstance extends BlockEntityInstance<StickerBlockEntity> implements DynamicInstance {

    float lastOffset = Float.NaN;

    final Direction facing;

    final boolean fakeWorld;

    final int offset;

    private final ModelData head = (ModelData) this.getTransformMaterial().getModel(AllPartialModels.STICKER_HEAD, this.blockState).createInstance();

    public StickerInstance(MaterialManager materialManager, StickerBlockEntity blockEntity) {
        super(materialManager, blockEntity);
        this.fakeWorld = blockEntity.m_58904_() != Minecraft.getInstance().level;
        this.facing = (Direction) this.blockState.m_61143_(StickerBlock.f_52588_);
        this.offset = this.blockState.m_61143_(StickerBlock.EXTENDED) ? 1 : 0;
        this.animateHead((float) this.offset);
    }

    public void beginFrame() {
        float offset = ((StickerBlockEntity) this.blockEntity).piston.getValue(AnimationTickHolder.getPartialTicks());
        if (this.fakeWorld) {
            offset = (float) this.offset;
        }
        if (!Mth.equal(offset, this.lastOffset)) {
            this.animateHead(offset);
            this.lastOffset = offset;
        }
    }

    private void animateHead(float offset) {
        ((ModelData) ((ModelData) ((ModelData) ((ModelData) ((ModelData) ((ModelData) this.head.loadIdentity().translate(this.getInstancePosition())).nudge(((StickerBlockEntity) this.blockEntity).hashCode())).centre()).rotateY((double) AngleHelper.horizontalAngle(this.facing))).rotateX((double) (AngleHelper.verticalAngle(this.facing) + 90.0F))).unCentre()).translate(0.0, (double) (offset * offset * 4.0F / 16.0F), 0.0);
    }

    public void updateLight() {
        this.relight(this.pos, new FlatLit[] { this.head });
    }

    public void remove() {
        this.head.delete();
    }
}
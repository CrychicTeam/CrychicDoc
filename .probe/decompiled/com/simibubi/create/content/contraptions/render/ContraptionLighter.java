package com.simibubi.create.content.contraptions.render;

import com.jozufozu.flywheel.light.GPULightVolume;
import com.jozufozu.flywheel.light.LightListener;
import com.jozufozu.flywheel.light.LightUpdater;
import com.jozufozu.flywheel.util.box.GridAlignedBB;
import com.jozufozu.flywheel.util.box.ImmutableBox;
import com.simibubi.create.content.contraptions.Contraption;
import net.minecraft.world.level.LightLayer;

public abstract class ContraptionLighter<C extends Contraption> implements LightListener {

    protected final C contraption;

    public final GPULightVolume lightVolume;

    protected final LightUpdater lightUpdater;

    protected final GridAlignedBB bounds;

    protected boolean scheduleRebuild;

    protected ContraptionLighter(C contraption) {
        this.contraption = contraption;
        this.lightUpdater = LightUpdater.get(contraption.entity.m_9236_());
        this.bounds = this.getContraptionBounds();
        growBoundsForEdgeData(this.bounds);
        this.lightVolume = new GPULightVolume(contraption.entity.m_9236_(), this.bounds);
        this.lightVolume.initialize();
        this.scheduleRebuild = true;
        this.lightUpdater.addListener(this);
    }

    public abstract GridAlignedBB getContraptionBounds();

    public boolean isListenerInvalid() {
        return this.lightVolume.isListenerInvalid();
    }

    public void onLightUpdate(LightLayer type, ImmutableBox changed) {
        this.lightVolume.onLightUpdate(type, changed);
    }

    public void onLightPacket(int chunkX, int chunkZ) {
        this.lightVolume.onLightPacket(chunkX, chunkZ);
    }

    protected static void growBoundsForEdgeData(GridAlignedBB bounds) {
        bounds.grow(2);
    }

    public ImmutableBox getVolume() {
        return this.bounds;
    }

    public void delete() {
        this.lightUpdater.removeListener(this);
        this.lightVolume.delete();
    }
}
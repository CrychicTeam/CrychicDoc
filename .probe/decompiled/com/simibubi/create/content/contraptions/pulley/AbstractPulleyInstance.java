package com.simibubi.create.content.contraptions.pulley;

import com.jozufozu.flywheel.api.Instancer;
import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.api.instance.DynamicInstance;
import com.jozufozu.flywheel.core.instancing.ConditionalInstance;
import com.jozufozu.flywheel.core.instancing.GroupInstance;
import com.jozufozu.flywheel.core.instancing.SelectInstance;
import com.jozufozu.flywheel.core.materials.FlatLit;
import com.jozufozu.flywheel.core.materials.oriented.OrientedData;
import com.jozufozu.flywheel.light.LightPacking;
import com.jozufozu.flywheel.light.LightVolume;
import com.jozufozu.flywheel.light.TickingLightListener;
import com.jozufozu.flywheel.util.box.GridAlignedBB;
import com.jozufozu.flywheel.util.box.ImmutableBox;
import com.mojang.math.Axis;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.base.ShaftInstance;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.LightLayer;

public abstract class AbstractPulleyInstance<T extends KineticBlockEntity> extends ShaftInstance<T> implements DynamicInstance, TickingLightListener {

    final OrientedData coil;

    final SelectInstance<OrientedData> magnet;

    final GroupInstance<OrientedData> rope;

    final ConditionalInstance<OrientedData> halfRope;

    protected float offset;

    protected final Direction rotatingAbout;

    protected final Axis rotationAxis;

    private final GridAlignedBB volume = new GridAlignedBB();

    private final LightVolume light;

    public AbstractPulleyInstance(MaterialManager dispatcher, T blockEntity) {
        super(dispatcher, blockEntity);
        this.rotatingAbout = Direction.get(Direction.AxisDirection.POSITIVE, this.axis);
        this.rotationAxis = Axis.of(this.rotatingAbout.step());
        this.coil = ((OrientedData) this.getCoilModel().createInstance()).setPosition(this.getInstancePosition());
        this.magnet = new SelectInstance(this::getMagnetModelIndex);
        this.magnet.addModel(this.getMagnetModel()).addModel(this.getHalfMagnetModel());
        this.rope = new GroupInstance(this.getRopeModel());
        this.halfRope = new ConditionalInstance(this.getHalfRopeModel()).withCondition(this::shouldRenderHalfRope);
        this.updateOffset();
        this.updateVolume();
        this.light = new LightVolume(this.world, this.volume);
        this.light.initialize();
    }

    public void beginFrame() {
        this.updateOffset();
        this.coil.setRotation(this.rotationAxis.rotationDegrees(this.offset * 180.0F));
        int neededRopeCount = this.getNeededRopeCount();
        this.rope.resize(neededRopeCount);
        this.magnet.update().get().ifPresent(data -> {
            int ix = Math.max(0, Mth.floor(this.offset));
            short packedx = this.light.getPackedLight(this.pos.m_123341_(), this.pos.m_123342_() - ix, this.pos.m_123343_());
            data.setPosition(this.getInstancePosition()).nudge(0.0F, -this.offset, 0.0F).setBlockLight(LightPacking.getBlock(packedx)).setSkyLight(LightPacking.getSky(packedx));
        });
        this.halfRope.update().get().ifPresent(rope1 -> {
            float f = this.offset % 1.0F;
            float halfRopeNudge = f > 0.75F ? f - 1.0F : f;
            short packedx = this.light.getPackedLight(this.pos.m_123341_(), this.pos.m_123342_(), this.pos.m_123343_());
            rope1.setPosition(this.getInstancePosition()).nudge(0.0F, -halfRopeNudge, 0.0F).setBlockLight(LightPacking.getBlock(packedx)).setSkyLight(LightPacking.getSky(packedx));
        });
        if (this.isRunning()) {
            int size = this.rope.size();
            int bottomY = this.pos.m_123342_() - size;
            for (int i = 0; i < size; i++) {
                short packed = this.light.getPackedLight(this.pos.m_123341_(), bottomY + i, this.pos.m_123343_());
                ((OrientedData) this.rope.get(i)).setPosition(this.getInstancePosition()).nudge(0.0F, -this.offset + (float) i + 1.0F, 0.0F).setBlockLight(LightPacking.getBlock(packed)).setSkyLight(LightPacking.getSky(packed));
            }
        } else {
            this.rope.clear();
        }
    }

    @Override
    public void updateLight() {
        super.updateLight();
        this.relight(this.pos, new FlatLit[] { this.coil });
    }

    @Override
    public void remove() {
        super.remove();
        this.coil.delete();
        this.magnet.delete();
        this.rope.clear();
        this.halfRope.delete();
        this.light.delete();
    }

    protected abstract Instancer<OrientedData> getRopeModel();

    protected abstract Instancer<OrientedData> getMagnetModel();

    protected abstract Instancer<OrientedData> getHalfMagnetModel();

    protected abstract Instancer<OrientedData> getCoilModel();

    protected abstract Instancer<OrientedData> getHalfRopeModel();

    protected abstract float getOffset();

    protected abstract boolean isRunning();

    public boolean tickLightListener() {
        if (this.updateVolume()) {
            this.light.move(this.volume);
            return true;
        } else {
            return false;
        }
    }

    private boolean updateVolume() {
        int length = Mth.ceil(this.offset) + 2;
        if (this.volume.sizeY() < length) {
            this.volume.assign(this.pos.below(length), this.pos).fixMinMax();
            return true;
        } else {
            return false;
        }
    }

    private void updateOffset() {
        this.offset = this.getOffset();
    }

    private int getNeededRopeCount() {
        return Math.max(0, Mth.ceil(this.offset - 1.25F));
    }

    private boolean shouldRenderHalfRope() {
        float f = this.offset % 1.0F;
        return this.offset > 0.75F && (f < 0.25F || f > 0.75F);
    }

    private int getMagnetModelIndex() {
        if (!this.isRunning() && this.offset != 0.0F) {
            return -1;
        } else {
            return this.offset > 0.25F ? 0 : 1;
        }
    }

    public boolean decreaseFramerateWithDistance() {
        return false;
    }

    public ImmutableBox getVolume() {
        return this.volume;
    }

    public void onLightUpdate(LightLayer type, ImmutableBox changed) {
        super.onLightUpdate(type, changed);
        this.light.onLightUpdate(type, changed);
    }
}
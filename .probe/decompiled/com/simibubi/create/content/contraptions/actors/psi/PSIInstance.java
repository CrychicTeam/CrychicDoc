package com.simibubi.create.content.contraptions.actors.psi;

import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.api.instance.DynamicInstance;
import com.jozufozu.flywheel.api.instance.TickableInstance;
import com.jozufozu.flywheel.backend.instancing.blockentity.BlockEntityInstance;
import com.jozufozu.flywheel.core.materials.FlatLit;
import com.simibubi.create.foundation.utility.AnimationTickHolder;

public class PSIInstance extends BlockEntityInstance<PortableStorageInterfaceBlockEntity> implements DynamicInstance, TickableInstance {

    private final PIInstance instance;

    public PSIInstance(MaterialManager materialManager, PortableStorageInterfaceBlockEntity blockEntity) {
        super(materialManager, blockEntity);
        this.instance = new PIInstance(materialManager, this.blockState, this.getInstancePosition());
    }

    public void init() {
        this.instance.init(this.isLit());
    }

    public void tick() {
        this.instance.tick(this.isLit());
    }

    public void beginFrame() {
        this.instance.beginFrame(((PortableStorageInterfaceBlockEntity) this.blockEntity).getExtensionDistance(AnimationTickHolder.getPartialTicks()));
    }

    public void updateLight() {
        this.relight(this.pos, new FlatLit[] { this.instance.middle, this.instance.top });
    }

    public void remove() {
        this.instance.remove();
    }

    private boolean isLit() {
        return ((PortableStorageInterfaceBlockEntity) this.blockEntity).isConnected();
    }
}
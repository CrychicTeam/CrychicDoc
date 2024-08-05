package com.mna.entities.constructs.ai.conditionals;

import com.mna.api.entities.construct.IConstruct;
import com.mna.api.entities.construct.ai.ConstructAITask;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

public abstract class ConstructConditional<V extends ConstructAITask<?>> extends ConstructAITask<V> {

    public ConstructConditional(IConstruct<?> construct, ResourceLocation guiIcon) {
        super(construct, guiIcon);
    }

    protected abstract boolean evaluate();

    @Override
    public void start() {
        super.start();
        if (this.evaluate()) {
            this.exitCode = 0;
        } else {
            this.exitCode = 1;
        }
    }

    @Override
    public void readNBT(CompoundTag nbt) {
    }

    @Override
    protected CompoundTag writeInternal(CompoundTag nbt) {
        return new CompoundTag();
    }
}
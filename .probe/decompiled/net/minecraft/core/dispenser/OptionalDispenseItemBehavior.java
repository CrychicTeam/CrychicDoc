package net.minecraft.core.dispenser;

import net.minecraft.core.BlockSource;

public abstract class OptionalDispenseItemBehavior extends DefaultDispenseItemBehavior {

    private boolean success = true;

    public boolean isSuccess() {
        return this.success;
    }

    public void setSuccess(boolean boolean0) {
        this.success = boolean0;
    }

    @Override
    protected void playSound(BlockSource blockSource0) {
        blockSource0.getLevel().m_46796_(this.isSuccess() ? 1000 : 1001, blockSource0.getPos(), 0);
    }
}
package org.embeddedt.modernfix.common.mixin.bugfix.concurrency;

import java.util.function.BooleanSupplier;
import net.minecraft.client.Minecraft;
import net.minecraft.util.thread.BlockableEventLoop;
import org.embeddedt.modernfix.ModernFix;
import org.embeddedt.modernfix.annotation.ClientOnlyMixin;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ Minecraft.class })
@ClientOnlyMixin
public abstract class MinecraftMixin<R extends Runnable> extends BlockableEventLoop<R> {

    protected MinecraftMixin(String p_i50403_1_) {
        super(p_i50403_1_);
    }

    @Override
    public void managedBlock(BooleanSupplier pIsDone) {
        if (!this.m_18695_()) {
            ModernFix.LOGGER.warn("A mod is calling Minecraft.managedBlock from the wrong thread. This is most likely related to one of our parallelizations.");
            ModernFix.LOGGER.warn("ModernFix will work around this, however ideally the issue should be patched in the other mod.");
            ModernFix.LOGGER.warn("Stacktrace", new IllegalThreadStateException());
            while (!pIsDone.getAsBoolean()) {
                try {
                    Thread.sleep(100L);
                } catch (InterruptedException var3) {
                    Thread.currentThread().interrupt();
                }
            }
        } else {
            super.managedBlock(pIsDone);
        }
    }
}
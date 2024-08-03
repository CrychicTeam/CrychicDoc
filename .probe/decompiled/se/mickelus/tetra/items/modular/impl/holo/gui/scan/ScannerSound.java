package se.mickelus.tetra.items.modular.impl.holo.gui.scan;

import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import se.mickelus.tetra.TetraSounds;

@ParametersAreNonnullByDefault
@OnlyIn(Dist.CLIENT)
public class ScannerSound extends AbstractTickableSoundInstance {

    private final Minecraft mc;

    private int activeCounter;

    private boolean hasStarted;

    public ScannerSound(Minecraft mc) {
        super(TetraSounds.scannerLoop, SoundSource.BLOCKS, SoundInstance.createUnseededRandom());
        this.mc = mc;
        this.f_119578_ = true;
        this.f_119580_ = SoundInstance.Attenuation.NONE;
        this.f_119573_ = 0.0F;
        this.f_119574_ = 0.5F;
    }

    public void activate() {
        if (!this.hasStarted) {
            this.mc.getSoundManager().play(this);
            this.hasStarted = true;
        }
        this.activeCounter = 2;
    }

    @Override
    public boolean canStartSilent() {
        return true;
    }

    @Override
    public void tick() {
        if (this.activeCounter > 0) {
            this.f_119573_ = Math.min(this.f_119573_ + 0.03F, 0.5F);
            this.activeCounter--;
        } else {
            this.f_119573_ = Math.max(this.f_119573_ - 0.027F, 0.0F);
        }
    }
}
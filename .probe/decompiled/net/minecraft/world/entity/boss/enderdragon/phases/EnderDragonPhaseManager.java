package net.minecraft.world.entity.boss.enderdragon.phases;

import com.mojang.logging.LogUtils;
import javax.annotation.Nullable;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import org.slf4j.Logger;

public class EnderDragonPhaseManager {

    private static final Logger LOGGER = LogUtils.getLogger();

    private final EnderDragon dragon;

    private final DragonPhaseInstance[] phases = new DragonPhaseInstance[EnderDragonPhase.getCount()];

    @Nullable
    private DragonPhaseInstance currentPhase;

    public EnderDragonPhaseManager(EnderDragon enderDragon0) {
        this.dragon = enderDragon0;
        this.setPhase(EnderDragonPhase.HOVERING);
    }

    public void setPhase(EnderDragonPhase<?> enderDragonPhase0) {
        if (this.currentPhase == null || enderDragonPhase0 != this.currentPhase.getPhase()) {
            if (this.currentPhase != null) {
                this.currentPhase.end();
            }
            this.currentPhase = this.getPhase((EnderDragonPhase<DragonPhaseInstance>) enderDragonPhase0);
            if (!this.dragon.m_9236_().isClientSide) {
                this.dragon.m_20088_().set(EnderDragon.DATA_PHASE, enderDragonPhase0.getId());
            }
            LOGGER.debug("Dragon is now in phase {} on the {}", enderDragonPhase0, this.dragon.m_9236_().isClientSide ? "client" : "server");
            this.currentPhase.begin();
        }
    }

    public DragonPhaseInstance getCurrentPhase() {
        return this.currentPhase;
    }

    public <T extends DragonPhaseInstance> T getPhase(EnderDragonPhase<T> enderDragonPhaseT0) {
        int $$1 = enderDragonPhaseT0.getId();
        if (this.phases[$$1] == null) {
            this.phases[$$1] = enderDragonPhaseT0.createInstance(this.dragon);
        }
        return (T) this.phases[$$1];
    }
}
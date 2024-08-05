package io.redspace.ironsspellbooks.entity.mobs.dead_king_boss;

import io.redspace.ironsspellbooks.config.ClientConfigs;
import net.minecraft.client.Minecraft;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class DeadKingAmbienceSoundManager {

    private final Vec3 vec3;

    @OnlyIn(Dist.CLIENT)
    private DeadKingAmbienceSoundInstance soundInstance;

    protected DeadKingAmbienceSoundManager(DeadKingCorpseEntity entity) {
        this.vec3 = entity.m_20182_();
    }

    public void trigger() {
        if (ClientConfigs.ENABLE_BOSS_MUSIC.get() && (this.soundInstance == null || this.soundInstance.m_7801_())) {
            this.soundInstance = new DeadKingAmbienceSoundInstance(this.vec3);
            Minecraft.getInstance().getSoundManager().play(this.soundInstance);
        }
    }

    public void triggerStop() {
        if (this.soundInstance != null) {
            this.soundInstance.triggerStop();
        }
    }
}
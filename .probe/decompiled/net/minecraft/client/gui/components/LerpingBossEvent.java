package net.minecraft.client.gui.components;

import java.util.UUID;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.BossEvent;

public class LerpingBossEvent extends BossEvent {

    private static final long LERP_MILLISECONDS = 100L;

    protected float targetPercent;

    protected long setTime;

    public LerpingBossEvent(UUID uUID0, Component component1, float float2, BossEvent.BossBarColor bossEventBossBarColor3, BossEvent.BossBarOverlay bossEventBossBarOverlay4, boolean boolean5, boolean boolean6, boolean boolean7) {
        super(uUID0, component1, bossEventBossBarColor3, bossEventBossBarOverlay4);
        this.targetPercent = float2;
        this.f_146638_ = float2;
        this.setTime = Util.getMillis();
        this.m_7003_(boolean5);
        this.m_7005_(boolean6);
        this.m_7006_(boolean7);
    }

    @Override
    public void setProgress(float float0) {
        this.f_146638_ = this.getProgress();
        this.targetPercent = float0;
        this.setTime = Util.getMillis();
    }

    @Override
    public float getProgress() {
        long $$0 = Util.getMillis() - this.setTime;
        float $$1 = Mth.clamp((float) $$0 / 100.0F, 0.0F, 1.0F);
        return Mth.lerp($$1, this.f_146638_, this.targetPercent);
    }
}
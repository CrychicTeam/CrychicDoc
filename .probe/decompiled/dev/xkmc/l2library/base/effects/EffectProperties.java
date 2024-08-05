package dev.xkmc.l2library.base.effects;

import net.minecraft.world.effect.MobEffectInstance;

public class EffectProperties {

    public Boolean ambient = null;

    public Boolean visible = null;

    public Boolean showIcon = null;

    public MobEffectInstance set(MobEffectInstance ins) {
        if (this.ambient != null) {
            ins.ambient = this.ambient;
        }
        if (this.visible != null) {
            ins.visible = this.visible;
        }
        if (this.showIcon != null) {
            ins.showIcon = this.showIcon;
        }
        return ins;
    }
}
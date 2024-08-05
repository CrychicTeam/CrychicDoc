package dev.xkmc.l2library.init.explosion;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Explosion;

public class BaseExplosion extends Explosion {

    public final BaseExplosionContext base;

    public final ModExplosionContext mod;

    public final VanillaExplosionContext mc;

    public BaseExplosion(BaseExplosionContext base, VanillaExplosionContext mc, ModExplosionContext mod) {
        super(base.level(), mc.entity(), mc.source(), mc.calculator(), base.x(), base.y(), base.z(), base.r(), mc.fire(), mc.type());
        this.base = base;
        this.mod = mod;
        this.mc = mc;
    }

    public boolean hurtEntity(Entity entity) {
        return this.mod.hurtEntity(entity);
    }
}
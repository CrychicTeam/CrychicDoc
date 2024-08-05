package dev.latvian.mods.kubejs.level;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;

public class ExplosionJS {

    private final LevelAccessor level;

    public final double x;

    public final double y;

    public final double z;

    public Entity exploder;

    public float strength;

    public boolean causesFire;

    public Level.ExplosionInteraction explosionMode;

    public ExplosionJS(LevelAccessor l, double _x, double _y, double _z) {
        this.level = l;
        this.x = _x;
        this.y = _y;
        this.z = _z;
        this.exploder = null;
        this.strength = 3.0F;
        this.causesFire = false;
        this.explosionMode = Level.ExplosionInteraction.NONE;
    }

    public ExplosionJS exploder(Entity entity) {
        this.exploder = entity;
        return this;
    }

    public ExplosionJS strength(float f) {
        this.strength = f;
        return this;
    }

    public ExplosionJS causesFire(boolean b) {
        this.causesFire = b;
        return this;
    }

    public ExplosionJS explosionMode(Level.ExplosionInteraction mode) {
        this.explosionMode = mode;
        return this;
    }

    public void explode() {
        if (this.level instanceof Level level) {
            level.explode(this.exploder, this.x, this.y, this.z, this.strength, this.causesFire, this.explosionMode);
        }
    }
}
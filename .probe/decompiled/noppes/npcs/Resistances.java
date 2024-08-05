package noppes.npcs;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;

public class Resistances {

    public float knockback = 1.0F;

    public float arrow = 1.0F;

    public float melee = 1.0F;

    public float explosion = 1.0F;

    public CompoundTag save() {
        CompoundTag compound = new CompoundTag();
        compound.putFloat("Knockback", this.knockback);
        compound.putFloat("Arrow", this.arrow);
        compound.putFloat("Melee", this.melee);
        compound.putFloat("Explosion", this.explosion);
        return compound;
    }

    public void readToNBT(CompoundTag compound) {
        this.knockback = compound.getFloat("Knockback");
        this.arrow = compound.getFloat("Arrow");
        this.melee = compound.getFloat("Melee");
        this.explosion = compound.getFloat("Explosion");
    }

    public float applyResistance(DamageSource source, float damage) {
        if (source.getMsgId().equals("arrow") || source.getMsgId().equals("thrown") || source.is(DamageTypeTags.IS_PROJECTILE)) {
            damage *= 2.0F - this.arrow;
        } else if (source.getMsgId().equals("player") || source.getMsgId().equals("mob")) {
            damage *= 2.0F - this.melee;
        } else if (source.getMsgId().equals("explosion") || source.getMsgId().equals("explosion.player")) {
            damage *= 2.0F - this.explosion;
        }
        return damage;
    }
}
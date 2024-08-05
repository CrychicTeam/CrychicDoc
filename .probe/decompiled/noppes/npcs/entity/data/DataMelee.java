package noppes.npcs.entity.data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.ai.attributes.Attributes;
import noppes.npcs.api.entity.data.INPCMelee;
import noppes.npcs.entity.EntityNPCInterface;

public class DataMelee implements INPCMelee {

    private EntityNPCInterface npc;

    private int attackStrength = 5;

    private int attackSpeed = 20;

    private int attackRange = 2;

    private int knockback = 0;

    private int potionType = 0;

    private int potionDuration = 5;

    private int potionAmp = 0;

    public DataMelee(EntityNPCInterface npc) {
        this.npc = npc;
    }

    public void load(CompoundTag compound) {
        this.attackSpeed = compound.getInt("AttackSpeed");
        this.setStrength(compound.getInt("AttackStrenght"));
        this.attackRange = compound.getInt("AttackRange");
        this.knockback = compound.getInt("KnockBack");
        this.potionType = compound.getInt("PotionEffect");
        this.potionDuration = compound.getInt("PotionDuration");
        this.potionAmp = compound.getInt("PotionAmp");
    }

    public CompoundTag save(CompoundTag compound) {
        compound.putInt("AttackStrenght", this.attackStrength);
        compound.putInt("AttackSpeed", this.attackSpeed);
        compound.putInt("AttackRange", this.attackRange);
        compound.putInt("KnockBack", this.knockback);
        compound.putInt("PotionEffect", this.potionType);
        compound.putInt("PotionDuration", this.potionDuration);
        compound.putInt("PotionAmp", this.potionAmp);
        return compound;
    }

    @Override
    public int getStrength() {
        return this.attackStrength;
    }

    @Override
    public void setStrength(int strength) {
        this.attackStrength = strength;
        this.npc.m_21051_(Attributes.ATTACK_DAMAGE).setBaseValue((double) this.attackStrength);
    }

    @Override
    public int getDelay() {
        return this.attackSpeed;
    }

    @Override
    public void setDelay(int speed) {
        this.attackSpeed = speed;
    }

    @Override
    public int getRange() {
        return this.attackRange;
    }

    @Override
    public void setRange(int range) {
        this.attackRange = range;
    }

    @Override
    public int getKnockback() {
        return this.knockback;
    }

    @Override
    public void setKnockback(int knockback) {
        this.knockback = knockback;
    }

    @Override
    public int getEffectType() {
        return this.potionType;
    }

    @Override
    public int getEffectTime() {
        return this.potionDuration;
    }

    @Override
    public int getEffectStrength() {
        return this.potionAmp;
    }

    @Override
    public void setEffect(int type, int strength, int time) {
        this.potionType = type;
        this.potionDuration = time;
        this.potionAmp = strength;
    }
}
package noppes.npcs.entity.data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import noppes.npcs.Resistances;
import noppes.npcs.api.CustomNPCsException;
import noppes.npcs.api.entity.data.INPCMelee;
import noppes.npcs.api.entity.data.INPCRanged;
import noppes.npcs.api.entity.data.INPCStats;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.util.ValueUtil;

public class DataStats implements INPCStats {

    public int aggroRange = 16;

    public int maxHealth = 20;

    public int respawnTime = 20;

    public int spawnCycle = 0;

    public boolean hideKilledBody = false;

    public Resistances resistances = new Resistances();

    public boolean immuneToFire = false;

    public boolean potionImmune = false;

    public boolean canDrown = true;

    public boolean burnInSun = false;

    public boolean noFallDamage = false;

    public boolean ignoreCobweb = false;

    public int healthRegen = 1;

    public int combatRegen = 0;

    public MobType creatureType = MobType.UNDEFINED;

    public DataMelee melee;

    public DataRanged ranged;

    private EntityNPCInterface npc;

    public DataStats(EntityNPCInterface npc) {
        this.npc = npc;
        this.melee = new DataMelee(npc);
        this.ranged = new DataRanged(npc);
    }

    public void readToNBT(CompoundTag compound) {
        this.resistances.readToNBT(compound.getCompound("Resistances"));
        this.setMaxHealth(compound.getInt("MaxHealth"));
        this.hideKilledBody = compound.getBoolean("HideBodyWhenKilled");
        this.aggroRange = compound.getInt("AggroRange");
        this.respawnTime = compound.getInt("RespawnTime");
        this.spawnCycle = compound.getInt("SpawnCycle");
        this.setCreatureType(compound.getInt("CreatureType"));
        this.healthRegen = compound.getInt("HealthRegen");
        this.combatRegen = compound.getInt("CombatRegen");
        this.immuneToFire = compound.getBoolean("ImmuneToFire");
        this.potionImmune = compound.getBoolean("PotionImmune");
        this.canDrown = compound.getBoolean("CanDrown");
        this.burnInSun = compound.getBoolean("BurnInSun");
        this.noFallDamage = compound.getBoolean("NoFallDamage");
        this.npc.setImmuneToFire(this.immuneToFire);
        this.ignoreCobweb = compound.getBoolean("IgnoreCobweb");
        this.melee.load(compound);
        this.ranged.load(compound);
    }

    public CompoundTag save(CompoundTag compound) {
        compound.put("Resistances", this.resistances.save());
        compound.putInt("MaxHealth", this.maxHealth);
        compound.putInt("AggroRange", this.aggroRange);
        compound.putBoolean("HideBodyWhenKilled", this.hideKilledBody);
        compound.putInt("RespawnTime", this.respawnTime);
        compound.putInt("SpawnCycle", this.spawnCycle);
        compound.putInt("CreatureType", this.getCreatureType());
        compound.putInt("HealthRegen", this.healthRegen);
        compound.putInt("CombatRegen", this.combatRegen);
        compound.putBoolean("ImmuneToFire", this.immuneToFire);
        compound.putBoolean("PotionImmune", this.potionImmune);
        compound.putBoolean("CanDrown", this.canDrown);
        compound.putBoolean("BurnInSun", this.burnInSun);
        compound.putBoolean("NoFallDamage", this.noFallDamage);
        compound.putBoolean("IgnoreCobweb", this.ignoreCobweb);
        this.melee.save(compound);
        this.ranged.save(compound);
        return compound;
    }

    @Override
    public void setMaxHealth(int maxHealth) {
        if (maxHealth != this.maxHealth) {
            this.maxHealth = maxHealth;
            this.npc.m_21051_(Attributes.MAX_HEALTH).setBaseValue((double) maxHealth);
            this.npc.updateClient = true;
        }
    }

    @Override
    public int getMaxHealth() {
        return this.maxHealth;
    }

    @Override
    public float getResistance(int type) {
        if (type == 0) {
            return this.resistances.melee;
        } else if (type == 1) {
            return this.resistances.arrow;
        } else if (type == 2) {
            return this.resistances.explosion;
        } else {
            return type == 3 ? this.resistances.knockback : 1.0F;
        }
    }

    @Override
    public void setResistance(int type, float value) {
        value = ValueUtil.correctFloat(value, 0.0F, 2.0F);
        if (type == 0) {
            this.resistances.melee = value;
        } else if (type == 1) {
            this.resistances.arrow = value;
        } else if (type == 2) {
            this.resistances.explosion = value;
        } else if (type == 3) {
            this.resistances.knockback = value;
        }
    }

    @Override
    public int getCombatRegen() {
        return this.combatRegen;
    }

    @Override
    public void setCombatRegen(int regen) {
        this.combatRegen = regen;
    }

    @Override
    public int getHealthRegen() {
        return this.healthRegen;
    }

    @Override
    public void setHealthRegen(int regen) {
        this.healthRegen = regen;
    }

    @Override
    public INPCMelee getMelee() {
        return this.melee;
    }

    @Override
    public INPCRanged getRanged() {
        return this.ranged;
    }

    @Override
    public boolean getImmune(int type) {
        if (type == 0) {
            return this.potionImmune;
        } else if (type == 1) {
            return !this.noFallDamage;
        } else if (type == 2) {
            return this.burnInSun;
        } else if (type == 3) {
            return this.immuneToFire;
        } else if (type == 4) {
            return !this.canDrown;
        } else if (type == 5) {
            return this.ignoreCobweb;
        } else {
            throw new CustomNPCsException("Unknown immune type: " + type);
        }
    }

    @Override
    public void setImmune(int type, boolean bo) {
        if (type == 0) {
            this.potionImmune = bo;
        } else if (type == 1) {
            this.noFallDamage = !bo;
        } else if (type == 2) {
            this.burnInSun = bo;
        } else if (type == 3) {
            this.npc.setImmuneToFire(bo);
        } else if (type == 4) {
            this.canDrown = !bo;
        } else {
            if (type != 5) {
                throw new CustomNPCsException("Unknown immune type: " + type);
            }
            this.ignoreCobweb = bo;
        }
    }

    @Override
    public int getCreatureType() {
        if (this.creatureType == MobType.UNDEAD) {
            return 1;
        } else if (this.creatureType == MobType.ARTHROPOD) {
            return 2;
        } else if (this.creatureType == MobType.ILLAGER) {
            return 3;
        } else {
            return this.creatureType == MobType.WATER ? 4 : 0;
        }
    }

    @Override
    public void setCreatureType(int type) {
        if (type == 1) {
            this.creatureType = MobType.UNDEAD;
        } else if (type == 2) {
            this.creatureType = MobType.ARTHROPOD;
        } else if (type == 3) {
            this.creatureType = MobType.ILLAGER;
        } else if (type == 4) {
            this.creatureType = MobType.WATER;
        } else {
            this.creatureType = MobType.UNDEFINED;
        }
    }

    @Override
    public int getRespawnType() {
        return this.spawnCycle;
    }

    @Override
    public void setRespawnType(int type) {
        this.spawnCycle = type;
    }

    @Override
    public int getRespawnTime() {
        return this.respawnTime;
    }

    @Override
    public void setRespawnTime(int seconds) {
        this.respawnTime = seconds;
    }

    @Override
    public boolean getHideDeadBody() {
        return this.hideKilledBody;
    }

    @Override
    public void setHideDeadBody(boolean hide) {
        this.hideKilledBody = hide;
        this.npc.updateClient = true;
    }

    @Override
    public int getAggroRange() {
        return this.aggroRange;
    }

    @Override
    public void setAggroRange(int range) {
        this.aggroRange = range;
        this.npc.m_21446_(this.npc.ais.startPos(), this.aggroRange * 2);
    }
}
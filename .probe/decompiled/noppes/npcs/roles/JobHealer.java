package noppes.npcs.roles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import noppes.npcs.NBTTags;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.util.ValueUtil;

public class JobHealer extends JobInterface {

    private int healTicks = 0;

    public int range = 8;

    public byte type = 2;

    public int speed = 20;

    public HashMap<Integer, Integer> effects = new HashMap();

    private List<LivingEntity> affected = new ArrayList();

    public JobHealer(EntityNPCInterface npc) {
        super(npc);
    }

    @Override
    public CompoundTag save(CompoundTag nbttagcompound) {
        nbttagcompound.putInt("HealerRange", this.range);
        nbttagcompound.putByte("HealerType", this.type);
        nbttagcompound.put("BeaconEffects", NBTTags.nbtIntegerIntegerMap(this.effects));
        nbttagcompound.putInt("HealerSpeed", this.speed);
        return nbttagcompound;
    }

    @Override
    public void load(CompoundTag nbttagcompound) {
        this.range = nbttagcompound.getInt("HealerRange");
        this.type = nbttagcompound.getByte("HealerType");
        this.effects = NBTTags.getIntegerIntegerMap(nbttagcompound.getList("BeaconEffects", 10));
        this.speed = ValueUtil.CorrectInt(nbttagcompound.getInt("HealerSpeed"), 10, Integer.MAX_VALUE);
    }

    @Override
    public boolean aiShouldExecute() {
        this.healTicks++;
        if (this.healTicks < this.speed) {
            return false;
        } else {
            this.healTicks = 0;
            this.affected = this.npc.m_9236_().m_45976_(LivingEntity.class, this.npc.m_20191_().inflate((double) this.range, (double) this.range / 2.0, (double) this.range));
            return !this.affected.isEmpty();
        }
    }

    @Override
    public boolean aiContinueExecute() {
        return false;
    }

    @Override
    public void aiStartExecuting() {
        for (LivingEntity entity : this.affected) {
            boolean isEnemy = false;
            if (entity instanceof Player) {
                isEnemy = this.npc.faction.isAggressiveToPlayer((Player) entity);
            } else if (entity instanceof EntityNPCInterface) {
                isEnemy = this.npc.faction.isAggressiveToNpc((EntityNPCInterface) entity);
            } else {
                isEnemy = entity instanceof Mob;
            }
            if (entity != this.npc && (this.type != 0 || !isEnemy) && (this.type != 1 || isEnemy)) {
                for (Integer potionEffect : this.effects.keySet()) {
                    MobEffect p = MobEffect.byId(potionEffect);
                    if (p != null) {
                        entity.addEffect(new MobEffectInstance(p, 100, (Integer) this.effects.get(potionEffect)));
                    }
                }
            }
        }
        this.affected.clear();
    }

    @Override
    public int getType() {
        return 2;
    }
}
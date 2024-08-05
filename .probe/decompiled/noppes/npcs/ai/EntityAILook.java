package noppes.npcs.ai;

import java.util.EnumSet;
import java.util.Iterator;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import noppes.npcs.entity.EntityNPCInterface;

public class EntityAILook extends Goal {

    private final EntityNPCInterface npc;

    private int idle = 0;

    private double lookX;

    private double lookZ;

    private boolean forced = false;

    private Entity forcedEntity = null;

    public EntityAILook(EntityNPCInterface npc) {
        this.npc = npc;
        this.m_7021_(EnumSet.of(Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (this.forced) {
            return true;
        } else if (this.npc.isAttacking() || !this.npc.m_21573_().isDone() || this.npc.isSleeping() || !this.npc.isAlive()) {
            return false;
        } else {
            return !this.npc.isInteracting() && this.npc.ais.getStandingType() <= 0 && this.idle <= 0 ? this.npc.m_217043_().nextFloat() < 0.004F : true;
        }
    }

    @Override
    public void start() {
        if (this.npc.ais.getStandingType() == 0 || this.npc.ais.getStandingType() == 3) {
            double var1 = (Math.PI * 2) * this.npc.m_217043_().nextDouble();
            if (this.npc.ais.getStandingType() == 3) {
                var1 = (Math.PI / 180.0) * (double) this.npc.ais.orientation + (Math.PI / 5) + (Math.PI * 3.0 / 5.0) * this.npc.m_217043_().nextDouble();
            }
            this.lookX = Math.cos(var1);
            this.lookZ = Math.sin(var1);
            this.idle = 20 + this.npc.m_217043_().nextInt(20);
        }
    }

    public void rotate(Entity entity) {
        this.forced = true;
        this.forcedEntity = entity;
    }

    public void rotate(int degrees) {
        this.forced = true;
        this.npc.f_20885_ = this.npc.f_20883_ = (float) degrees;
        this.npc.m_146922_((float) degrees);
    }

    @Override
    public void stop() {
        this.forced = false;
        this.forcedEntity = null;
    }

    @Override
    public void tick() {
        Entity lookat = null;
        if (this.forced && this.forcedEntity != null) {
            lookat = this.forcedEntity;
        } else if (this.npc.isInteracting()) {
            Iterator<LivingEntity> ita = this.npc.interactingEntities.iterator();
            double closestDistance = 12.0;
            while (ita.hasNext()) {
                LivingEntity entity = (LivingEntity) ita.next();
                double distance = entity.m_20280_(this.npc);
                if (distance < closestDistance) {
                    closestDistance = entity.m_20280_(this.npc);
                    lookat = entity;
                } else if (distance > 12.0) {
                    ita.remove();
                }
            }
        } else if (this.npc.ais.getStandingType() == 2) {
            lookat = this.npc.m_9236_().m_45930_(this.npc, 16.0);
        }
        if (lookat != null) {
            this.npc.m_21563_().setLookAt(lookat, 10.0F, (float) this.npc.m_8132_());
        } else {
            if (this.idle > 0) {
                this.idle--;
                this.npc.m_21563_().setLookAt(this.npc.m_20185_() + this.lookX, this.npc.m_20186_() + (double) this.npc.m_20192_(), this.npc.m_20189_() + this.lookZ, 10.0F, (float) this.npc.m_8132_());
            }
            if (this.npc.ais.getStandingType() == 1 && !this.forced) {
                this.npc.f_20885_ = this.npc.f_20883_ = (float) this.npc.ais.orientation;
                this.npc.m_146922_((float) this.npc.ais.orientation);
            }
        }
    }
}
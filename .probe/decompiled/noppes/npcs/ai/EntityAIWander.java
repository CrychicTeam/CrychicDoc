package noppes.npcs.ai;

import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.phys.Vec3;
import noppes.npcs.CustomNpcs;
import noppes.npcs.ai.selector.NPCInteractSelector;
import noppes.npcs.controllers.data.Line;
import noppes.npcs.entity.EntityNPCInterface;

public class EntityAIWander extends Goal {

    private EntityNPCInterface entity;

    public final NPCInteractSelector selector;

    private double x;

    private double y;

    private double zPosition;

    private EntityNPCInterface nearbyNPC;

    public EntityAIWander(EntityNPCInterface npc) {
        this.entity = npc;
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
        this.selector = new NPCInteractSelector(npc);
    }

    @Override
    public boolean canUse() {
        if (this.entity.m_21216_() < 100 && this.entity.m_21573_().isDone() && !this.entity.isInteracting() && !this.entity.m_20159_() && (!this.entity.ais.movingPause || this.entity.m_217043_().nextInt(80) == 0)) {
            if (this.entity.ais.npcInteracting && this.entity.m_217043_().nextInt(this.entity.ais.movingPause ? 6 : 16) == 1) {
                this.nearbyNPC = this.getNearbyNPC();
            }
            if (this.nearbyNPC != null) {
                this.x = (double) Mth.floor(this.nearbyNPC.m_20185_());
                this.y = (double) Mth.floor(this.nearbyNPC.m_20186_());
                this.zPosition = (double) Mth.floor(this.nearbyNPC.m_20189_());
                this.nearbyNPC.addInteract(this.entity);
            } else {
                Vec3 vec = this.getVec();
                if (vec == null) {
                    return false;
                }
                this.x = vec.x;
                this.y = vec.y;
                if (this.entity.ais.movementType == 1) {
                    this.y = this.entity.getStartYPos() + (double) this.entity.m_217043_().nextFloat() * 0.75 * (double) this.entity.ais.walkingRange;
                }
                this.zPosition = vec.z;
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void tick() {
        if (this.nearbyNPC != null) {
            this.nearbyNPC.m_21573_().stop();
        }
    }

    private EntityNPCInterface getNearbyNPC() {
        List<Entity> list = this.entity.m_9236_().getEntities(this.entity, this.entity.m_20191_().inflate((double) this.entity.ais.walkingRange, this.entity.ais.walkingRange > 7 ? 7.0 : (double) this.entity.ais.walkingRange, (double) this.entity.ais.walkingRange), this.selector);
        Iterator<Entity> ita = list.iterator();
        while (ita.hasNext()) {
            EntityNPCInterface npc = (EntityNPCInterface) ita.next();
            if (!npc.ais.stopAndInteract || npc.isAttacking() || !npc.isAlive() || this.entity.faction.isAggressiveToNpc(npc)) {
                ita.remove();
            }
        }
        return list.isEmpty() ? null : (EntityNPCInterface) list.get(this.entity.m_217043_().nextInt(list.size()));
    }

    private Vec3 getVec() {
        if (this.entity.ais.walkingRange > 0) {
            BlockPos start = new BlockPos((int) this.entity.getStartXPos(), (int) this.entity.getStartYPos(), (int) this.entity.getStartZPos());
            int distance = (int) Math.sqrt(this.entity.m_20183_().m_123331_(start));
            int range = Math.min(this.entity.ais.walkingRange, CustomNpcs.NpcNavRange);
            if (range - distance < 4) {
                Vec3 pos2 = new Vec3((this.entity.m_20185_() + (double) start.m_123341_()) / 2.0, (this.entity.m_20186_() + (double) start.m_123342_()) / 2.0, (this.entity.m_20189_() + (double) start.m_123343_()) / 2.0);
                return DefaultRandomPos.getPosTowards(this.entity, range / 2, Math.min(range / 2, 7), pos2, Math.PI / 2);
            } else {
                return DefaultRandomPos.getPos(this.entity, range / 2, Math.min(range / 2, 7));
            }
        } else {
            return DefaultRandomPos.getPos(this.entity, CustomNpcs.NpcNavRange, 7);
        }
    }

    @Override
    public boolean canContinueToUse() {
        return this.nearbyNPC == null || this.selector.apply(this.nearbyNPC) && !this.entity.isInRange(this.nearbyNPC, (double) this.entity.m_20205_()) ? !this.entity.m_21573_().isDone() && this.entity.isAlive() && !this.entity.isInteracting() : false;
    }

    @Override
    public void start() {
        this.entity.m_21573_().moveTo(this.entity.m_21573_().createPath(this.x, this.y, this.zPosition, 0), 1.0);
    }

    @Override
    public void stop() {
        if (this.nearbyNPC != null && this.entity.isInRange(this.nearbyNPC, 3.5)) {
            EntityNPCInterface talk = this.entity;
            if (this.entity.m_217043_().nextBoolean()) {
                talk = this.nearbyNPC;
            }
            Line line = talk.advanced.getNPCInteractLine();
            if (line == null) {
                line = new Line(".........");
            }
            line.setShowText(false);
            talk.saySurrounding(line);
            this.entity.addInteract(this.nearbyNPC);
            this.nearbyNPC.addInteract(this.entity);
        }
        this.nearbyNPC = null;
    }
}
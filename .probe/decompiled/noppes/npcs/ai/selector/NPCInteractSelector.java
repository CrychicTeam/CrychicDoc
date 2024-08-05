package noppes.npcs.ai.selector;

import com.google.common.base.Predicate;
import noppes.npcs.entity.EntityNPCInterface;

public class NPCInteractSelector implements Predicate {

    private EntityNPCInterface npc;

    public NPCInteractSelector(EntityNPCInterface npc) {
        this.npc = npc;
    }

    public boolean isEntityApplicable(EntityNPCInterface entity) {
        return entity != this.npc && this.npc.isAlive() ? !entity.isAttacking() && !this.npc.getFaction().isAggressiveToNpc(entity) && this.npc.ais.stopAndInteract : false;
    }

    public boolean apply(Object ob) {
        return !(ob instanceof EntityNPCInterface) ? false : this.isEntityApplicable((EntityNPCInterface) ob);
    }
}
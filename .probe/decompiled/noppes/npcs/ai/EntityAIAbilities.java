package noppes.npcs.ai;

import net.minecraft.world.entity.ai.goal.Goal;
import noppes.npcs.ability.AbstractAbility;
import noppes.npcs.ability.IAbilityUpdate;
import noppes.npcs.constants.EnumAbilityType;
import noppes.npcs.entity.EntityNPCInterface;

public class EntityAIAbilities extends Goal {

    private EntityNPCInterface npc;

    private IAbilityUpdate ability;

    public EntityAIAbilities(EntityNPCInterface npc) {
        this.npc = npc;
    }

    @Override
    public boolean canUse() {
        if (!this.npc.isAttacking()) {
            return false;
        } else {
            this.ability = (IAbilityUpdate) this.npc.abilities.getAbility(EnumAbilityType.UPDATE);
            return this.ability != null;
        }
    }

    @Override
    public boolean canContinueToUse() {
        return this.npc.isAttacking() && this.ability.isActive();
    }

    @Override
    public void tick() {
        this.ability.update();
    }

    @Override
    public void stop() {
        ((AbstractAbility) this.ability).endAbility();
        this.ability = null;
    }
}
package noppes.npcs.entity.data;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import noppes.npcs.ability.AbstractAbility;
import noppes.npcs.constants.EnumAbilityType;
import noppes.npcs.entity.EntityNPCInterface;

public class DataAbilities {

    public List<AbstractAbility> abilities = new ArrayList();

    public EntityNPCInterface npc;

    public DataAbilities(EntityNPCInterface npc) {
        this.npc = npc;
    }

    public CompoundTag save(CompoundTag compound) {
        return compound;
    }

    public void readToNBT(CompoundTag compound) {
    }

    public AbstractAbility getAbility(EnumAbilityType type) {
        LivingEntity target = this.npc.m_5448_();
        for (AbstractAbility ability : this.abilities) {
            if (ability.isType(type) && ability.canRun(target)) {
                return ability;
            }
        }
        return null;
    }
}
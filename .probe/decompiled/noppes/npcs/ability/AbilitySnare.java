package noppes.npcs.ability;

import noppes.npcs.constants.EnumAbilityType;
import noppes.npcs.entity.EntityNPCInterface;

public class AbilitySnare extends AbstractAbility implements IAbilityUpdate {

    public AbilitySnare(EntityNPCInterface entity) {
        super(entity);
    }

    @Override
    public boolean isActive() {
        return false;
    }

    @Override
    public void update() {
    }

    @Override
    public boolean isType(EnumAbilityType type) {
        return type == EnumAbilityType.ATTACKED || type == EnumAbilityType.UPDATE;
    }
}
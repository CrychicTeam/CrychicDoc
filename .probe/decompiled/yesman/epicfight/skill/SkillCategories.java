package yesman.epicfight.skill;

public enum SkillCategories implements SkillCategory {

    BASIC_ATTACK(false, false, false),
    AIR_ATTACK(false, false, false),
    DODGE(true, true, true),
    PASSIVE(true, true, true),
    WEAPON_PASSIVE(false, false, false),
    WEAPON_INNATE(false, true, false),
    GUARD(true, true, true),
    KNOCKDOWN_WAKEUP(false, false, false),
    MOVER(true, true, true),
    IDENTITY(true, true, true);

    boolean shouldSave;

    boolean shouldSyncronize;

    boolean modifiable;

    int id;

    private SkillCategories(boolean shouldSave, boolean shouldSyncronize, boolean modifiable) {
        this.shouldSave = shouldSave;
        this.shouldSyncronize = shouldSyncronize;
        this.modifiable = modifiable;
        this.id = SkillCategory.ENUM_MANAGER.assign(this);
    }

    @Override
    public boolean shouldSave() {
        return this.shouldSave;
    }

    @Override
    public boolean shouldSynchronize() {
        return this.shouldSyncronize;
    }

    @Override
    public boolean learnable() {
        return this.modifiable;
    }

    @Override
    public int universalOrdinal() {
        return this.id;
    }
}
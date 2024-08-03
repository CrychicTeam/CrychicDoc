package noppes.npcs.roles.companion;

import net.minecraft.nbt.CompoundTag;
import noppes.npcs.constants.EnumCompanionJobs;
import noppes.npcs.entity.EntityNPCInterface;

public abstract class CompanionJobInterface {

    public EntityNPCInterface npc;

    public abstract CompoundTag getNBT();

    public abstract void setNBT(CompoundTag var1);

    public abstract EnumCompanionJobs getType();

    public void onUpdate() {
    }

    public boolean isSelfSufficient() {
        return false;
    }
}
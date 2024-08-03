package noppes.npcs.roles.companion;

import net.minecraft.nbt.CompoundTag;
import noppes.npcs.constants.EnumCompanionJobs;

public class CompanionFarmer extends CompanionJobInterface {

    public boolean isStanding = false;

    @Override
    public CompoundTag getNBT() {
        CompoundTag compound = new CompoundTag();
        compound.putBoolean("CompanionFarmerStanding", this.isStanding);
        return compound;
    }

    @Override
    public void setNBT(CompoundTag compound) {
        this.isStanding = compound.getBoolean("CompanionFarmerStanding");
    }

    @Override
    public EnumCompanionJobs getType() {
        return EnumCompanionJobs.FARMER;
    }

    @Override
    public boolean isSelfSufficient() {
        return this.isStanding;
    }

    @Override
    public void onUpdate() {
    }
}
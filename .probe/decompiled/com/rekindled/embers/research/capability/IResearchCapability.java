package com.rekindled.embers.research.capability;

import java.util.Map;
import net.minecraft.nbt.CompoundTag;

public interface IResearchCapability {

    void setCheckmark(String var1, boolean var2);

    boolean isChecked(String var1);

    Map<String, Boolean> getCheckmarks();

    void writeToNBT(CompoundTag var1);

    void readFromNBT(CompoundTag var1);
}
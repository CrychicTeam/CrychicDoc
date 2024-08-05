package com.mna.api.faction;

import java.util.List;
import net.minecraft.resources.ResourceLocation;

public interface IFactionHelper {

    List<IFaction> getAllFactions();

    IFaction getFaction(ResourceLocation var1);

    List<IFaction> getFactionsExcept(IFaction... var1);
}
package fr.frinn.custommachinery.api.upgrade;

import com.mojang.datafixers.util.Pair;
import java.util.List;

public interface IMachineUpgradeManager {

    void markDirty();

    List<Pair<IRecipeModifier, Integer>> getAllModifiers();
}
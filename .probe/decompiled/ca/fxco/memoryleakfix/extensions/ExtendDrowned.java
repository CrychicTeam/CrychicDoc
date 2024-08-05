package ca.fxco.memoryleakfix.extensions;

import java.util.Set;
import net.minecraft.world.entity.ai.navigation.PathNavigation;

public interface ExtendDrowned {

    void memoryLeakFix$onRemoveNavigation(Set<PathNavigation> var1);
}
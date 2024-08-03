package net.minecraftforge.common.extensions;

import java.util.Collection;
import java.util.Collections;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.entity.PartEntity;

public interface IForgeLevel extends ICapabilityProvider {

    double getMaxEntityRadius();

    double increaseMaxEntityRadius(double var1);

    default Collection<PartEntity<?>> getPartEntities() {
        return Collections.emptyList();
    }
}
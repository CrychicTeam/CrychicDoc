package net.minecraft.advancements;

import java.util.Collection;

public interface RequirementsStrategy {

    RequirementsStrategy AND = p_15984_ -> {
        String[][] $$1 = new String[p_15984_.size()][];
        int $$2 = 0;
        for (String $$3 : p_15984_) {
            $$1[$$2++] = new String[] { $$3 };
        }
        return $$1;
    };

    RequirementsStrategy OR = p_15982_ -> new String[][] { (String[]) p_15982_.toArray(new String[0]) };

    String[][] createRequirements(Collection<String> var1);
}
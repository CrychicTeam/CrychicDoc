package net.mehvahdjukaar.modelfix.forge;

import net.mehvahdjukaar.modelfix.ModelFix;
import net.minecraftforge.fml.common.Mod;

@Mod("modelfix")
public class ModelFixForge {

    public ModelFixForge() {
        ModelFix.init(false);
    }
}
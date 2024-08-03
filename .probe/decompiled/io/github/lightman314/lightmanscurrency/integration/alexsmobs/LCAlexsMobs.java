package io.github.lightman314.lightmanscurrency.integration.alexsmobs;

import io.github.lightman314.lightmanscurrency.api.events.DroplistConfigGenerator;
import net.minecraftforge.fml.ModList;

public class LCAlexsMobs {

    public static boolean isLoaded() {
        return ModList.get().isLoaded("alexsmobs");
    }

    public static void registerDroplistListeners() {
        DroplistConfigGenerator.registerEntityListener(LCAlexsMobs::AddEntityLoot);
    }

    public static void AddEntityLoot(DroplistConfigGenerator.Entity event) {
        event.setDefaultNamespace("alexsmobs");
        switch(event.getTier()) {
            case T1:
                event.addEntry("cosmic_cod");
                event.addEntry("fly");
                event.addEntry("raccoon");
                event.addEntry("stradpole");
                break;
            case T2:
                event.addEntry("bone_serpent");
                event.addEntry("anaconda");
                event.addEntry("froststalker");
                event.addEntry("rattlesnake");
                event.addEntry("rockey_roller");
                event.addEntry("skreecher");
                event.addEntry("soul_vulture");
                event.addEntry("tarantula_hawk");
                break;
            case T3:
                event.addEntry("crimson_mosquito");
                event.addEntry("dropbear");
                event.addEntry("guster");
                event.addEntry("skelewag");
                event.addEntry("tusklin");
                break;
            case T4:
                event.addEntry("enderiophage");
                event.addEntry("farseer");
                event.addEntry("murmur");
                event.addEntry("straddler");
                break;
            case T6:
                event.addEntry("mimicube");
                break;
            case BOSS_T6:
                event.addEntry("void_worm");
        }
        event.resetDefaultNamespace();
    }
}
package snownee.loquat;

import net.minecraftforge.fml.ModLoadingContext;
import snownee.kiwi.util.Util;
import snownee.loquat.program.PlaceProgram;
import snownee.loquat.program.SeaLevelPlaceProgram;

public class PlaceProgramTypes {

    public static final SeaLevelPlaceProgram.Type SEA_LEVEL = register("sea_level", new SeaLevelPlaceProgram.Type());

    public static void init() {
    }

    public static <T extends PlaceProgram.Type<?>> T register(String name, T t) {
        ModLoadingContext.get().setActiveContainer(null);
        LoquatRegistries.PLACE_PROGRAM.register(Util.RL(name), t);
        return t;
    }
}
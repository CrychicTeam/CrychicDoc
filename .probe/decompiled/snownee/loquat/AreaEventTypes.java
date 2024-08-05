package snownee.loquat;

import net.minecraftforge.fml.ModLoadingContext;
import snownee.kiwi.util.Util;
import snownee.loquat.core.AreaEvent;
import snownee.loquat.spawner.SpawnMobAreaEvent;

public class AreaEventTypes {

    public static final SpawnMobAreaEvent.Type SPAWN_MOBS = register("spawn_mobs", new SpawnMobAreaEvent.Type());

    public static void init() {
    }

    public static <T extends AreaEvent.Type<?>> T register(String name, T t) {
        ModLoadingContext.get().setActiveContainer(null);
        LoquatRegistries.AREA_EVENT.register(Util.RL(name), t);
        return t;
    }
}
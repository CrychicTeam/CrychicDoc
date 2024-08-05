package fr.lucreeper74.createmetallurgy.registries;

import com.jozufozu.flywheel.core.PartialModel;
import fr.lucreeper74.createmetallurgy.CreateMetallurgy;
import fr.lucreeper74.createmetallurgy.utils.CMLang;
import java.util.EnumMap;
import java.util.Map;
import net.minecraft.world.item.DyeColor;

public class CMPartialModels {

    public static final PartialModel FOUNDRY_MIXER_POLE = block("foundry_mixer/pole");

    public static final PartialModel FOUNDRY_MIXER_HEAD = block("foundry_mixer/head");

    public static final PartialModel SHAFTLESS_STONE_COGWHEEL = block("foundry_mixer/cog");

    public static final PartialModel THERMOMETER_GAUGE = block("gauges/thermometer");

    public static final PartialModel GRINDER_BELT = block("mechanical_belt_grinder/belt");

    public static final PartialModel BULB_INNER_GLOW = block("light_bulb/inner_glow");

    public static final Map<DyeColor, PartialModel> BULB_TUBES = new EnumMap(DyeColor.class);

    public static final Map<DyeColor, PartialModel> BULB_TUBES_GLOW = new EnumMap(DyeColor.class);

    private static PartialModel block(String path) {
        return new PartialModel(CreateMetallurgy.genRL("block/" + path));
    }

    public static void init() {
    }

    static {
        for (DyeColor color : DyeColor.values()) {
            BULB_TUBES.put(color, block("light_bulb/tube/" + CMLang.asId(color.name())));
            BULB_TUBES_GLOW.put(color, block("light_bulb/tube_glow/" + CMLang.asId(color.name())));
        }
    }
}
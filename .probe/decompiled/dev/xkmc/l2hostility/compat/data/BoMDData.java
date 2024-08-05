package dev.xkmc.l2hostility.compat.data;

import com.cerbon.bosses_of_mass_destruction.entity.BMDEntities;
import dev.xkmc.l2hostility.content.config.EntityConfig;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import dev.xkmc.l2hostility.init.data.LHConfigGen;
import dev.xkmc.l2hostility.init.registrate.LHTraits;
import dev.xkmc.l2library.serial.config.ConfigDataProvider;
import java.util.List;

public class BoMDData {

    public static void genConfig(ConfigDataProvider.Collector collector) {
        LHConfigGen.addEntity(collector, 200, 50, BMDEntities.LICH, List.of(EntityConfig.trait((MobTrait) LHTraits.TANK.get(), 2, 3), EntityConfig.trait((MobTrait) LHTraits.REPRINT.get(), 1, 1), EntityConfig.trait((MobTrait) LHTraits.FREEZING.get(), 2, 3)), List.of());
        LHConfigGen.addEntity(collector, 200, 50, BMDEntities.OBSIDILITH, List.of(EntityConfig.trait((MobTrait) LHTraits.TANK.get(), 2, 3), EntityConfig.trait((MobTrait) LHTraits.REFLECT.get(), 2, 3), EntityConfig.trait((MobTrait) LHTraits.WEAKNESS.get(), 5, 5)), List.of());
        LHConfigGen.addEntity(collector, 200, 50, BMDEntities.GAUNTLET, List.of(EntityConfig.trait((MobTrait) LHTraits.TANK.get(), 2, 3), EntityConfig.trait((MobTrait) LHTraits.REFLECT.get(), 2, 3), EntityConfig.trait((MobTrait) LHTraits.SOUL_BURNER.get(), 2, 3)), List.of());
        LHConfigGen.addEntity(collector, 200, 50, BMDEntities.VOID_BLOSSOM, List.of(EntityConfig.trait((MobTrait) LHTraits.TANK.get(), 2, 3), EntityConfig.trait((MobTrait) LHTraits.REGEN.get(), 5, 5), EntityConfig.trait((MobTrait) LHTraits.ADAPTIVE.get(), 2, 3)), List.of());
    }
}
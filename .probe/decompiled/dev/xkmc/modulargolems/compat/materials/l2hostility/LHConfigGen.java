package dev.xkmc.modulargolems.compat.materials.l2hostility;

import dev.xkmc.l2library.serial.config.ConfigDataProvider;
import net.minecraft.data.DataGenerator;

public class LHConfigGen extends ConfigDataProvider {

    public LHConfigGen(DataGenerator generator) {
        super(generator, "L2Hostility config provider");
    }

    @Override
    public void add(ConfigDataProvider.Collector collector) {
    }
}
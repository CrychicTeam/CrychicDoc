package dev.xkmc.l2hostility.compat.data;

import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import dev.xkmc.l2hostility.init.data.LHConfigGen;
import dev.xkmc.l2library.serial.config.ConfigDataProvider;

public class MowzieData {

    public static void genConfig(ConfigDataProvider.Collector collector) {
        LHConfigGen.addEntity(collector, 100, 30, EntityHandler.FROSTMAW);
        LHConfigGen.addEntity(collector, 100, 30, EntityHandler.UMVUTHI);
        LHConfigGen.addEntity(collector, 100, 30, EntityHandler.WROUGHTNAUT);
    }
}
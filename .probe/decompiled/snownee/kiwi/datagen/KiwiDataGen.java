package snownee.kiwi.datagen;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator.Pack;

public class KiwiDataGen implements DataGeneratorEntrypoint {

    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        Pack pack = fabricDataGenerator.createPack();
        pack.addProvider($ -> new KiwiLanguageProvider($));
    }
}
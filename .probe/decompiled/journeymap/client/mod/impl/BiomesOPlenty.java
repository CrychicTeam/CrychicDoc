package journeymap.client.mod.impl;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import journeymap.client.mod.IModBlockHandler;
import journeymap.client.model.BlockFlag;
import journeymap.client.model.BlockMD;

public class BiomesOPlenty implements IModBlockHandler {

    private final List<String> plants = Arrays.asList("flower", "mushroom", "sapling", "plant", "ivy", "waterlily", "moss", "lavender");

    private final List<String> crops = Collections.singletonList("turnip");

    @Override
    public void initialize(BlockMD blockMD) {
        String name = blockMD.getBlockId().toLowerCase();
        for (String plant : this.plants) {
            if (name.contains(plant)) {
                blockMD.addFlags(BlockFlag.Plant);
                break;
            }
        }
        for (String crop : this.crops) {
            if (name.contains(crop)) {
                blockMD.addFlags(BlockFlag.Crop);
                break;
            }
        }
    }
}
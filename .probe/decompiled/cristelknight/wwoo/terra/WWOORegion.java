package cristelknight.wwoo.terra;

import com.mojang.datafixers.util.Pair;
import java.util.function.Consumer;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;
import terrablender.api.Region;
import terrablender.api.RegionType;

public class WWOORegion extends Region {

    public WWOORegion(ResourceLocation name, int weight) {
        super(name, RegionType.OVERWORLD, weight);
    }

    public void addBiomes(Registry<Biome> registry, Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> mapper) {
        for (Pair<Climate.ParameterPoint, ResourceKey<Biome>> p : TerraInit.readParameterPoints()) {
            mapper.accept(p);
        }
    }
}
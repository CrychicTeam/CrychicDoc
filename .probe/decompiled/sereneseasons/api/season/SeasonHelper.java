package sereneseasons.api.season;

import net.minecraft.core.Holder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;

public class SeasonHelper {

    public static SeasonHelper.ISeasonDataProvider dataProvider;

    public static ISeasonState getSeasonState(Level world) {
        ISeasonState data;
        if (!world.isClientSide()) {
            data = dataProvider.getServerSeasonState(world);
        } else {
            data = dataProvider.getClientSeasonState();
        }
        return data;
    }

    public static boolean usesTropicalSeasons(Holder<Biome> biome) {
        return dataProvider.usesTropicalSeasons(biome);
    }

    public interface ISeasonDataProvider {

        ISeasonState getServerSeasonState(Level var1);

        ISeasonState getClientSeasonState();

        boolean usesTropicalSeasons(Holder<Biome> var1);
    }
}
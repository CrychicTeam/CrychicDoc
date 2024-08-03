package snownee.loquat.program;

import com.google.gson.JsonObject;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import snownee.loquat.core.area.Area;

public class SeaLevelPlaceProgram implements PlaceProgram {

    public static final SeaLevelPlaceProgram INSTANCE = new SeaLevelPlaceProgram();

    @Override
    public boolean place(Level level, Area area) {
        int seaLevel = level.getSeaLevel();
        BlockState water = Blocks.WATER.defaultBlockState();
        area.allBlockPosIn().filter(pos -> pos.m_123342_() <= seaLevel).forEach(pos -> level.setBlock(pos, water, 2));
        return true;
    }

    public static class Type extends PlaceProgram.Type<SeaLevelPlaceProgram> {

        public SeaLevelPlaceProgram create(JsonObject asJsonObject) {
            return SeaLevelPlaceProgram.INSTANCE;
        }
    }
}
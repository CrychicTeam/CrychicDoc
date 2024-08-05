package dev.xkmc.modulargolems.content.client.outline;

import com.simibubi.create.CreateClient;
import dev.xkmc.modulargolems.content.item.card.PathRecordCard;
import it.unimi.dsi.fastutil.Pair;
import java.util.Collection;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlockOutliner {

    public static void drawOutlines(Player player, Collection<PathRecordCard.Pos> selection) {
        Level level = Minecraft.getInstance().level;
        if (level != null) {
            if (Minecraft.getInstance().player == player) {
                BlockPos pre = null;
                BlockPos first = null;
                float time = ((float) level.getGameTime() + Minecraft.getInstance().getPartialTick()) / 40.0F % 2.0F - 1.0F;
                for (PathRecordCard.Pos point : selection) {
                    ResourceLocation id = point.level();
                    if (level.dimension().location().equals(id)) {
                        BlockPos pos = point.pos();
                        VoxelShape shape = Shapes.block();
                        CreateClient.OUTLINER.showAABB(point, shape.bounds().move(pos)).colored(pre == null ? 8388479 : 8375776).lineWidth(0.0625F);
                        if (pre != null) {
                            line(pre, pos, time);
                        } else {
                            first = pos;
                        }
                        pre = pos;
                    }
                }
                if (pre != null) {
                    line(pre, first, time);
                }
            }
        }
    }

    private static void line(BlockPos a, BlockPos b, float time) {
        CreateClient.OUTLINER.endChasingLine(Pair.of(a, b), (time > 0.0F ? a : b).getCenter(), (time > 0.0F ? b : a).getCenter(), Math.abs(time), false).colored(8375776).lineWidth(0.0625F);
    }
}
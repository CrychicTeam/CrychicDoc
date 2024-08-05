package snownee.loquat.util;

import java.util.function.Supplier;
import java.util.stream.Stream;
import net.minecraft.commands.CommandSigningContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.TaskChainer;
import net.minecraft.world.Clearable;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

public interface LoquatUtil {

    static int runCommandSilently(ServerLevel world, String command) {
        return world.getServer().getCommands().performPrefixedCommand(new LoquatUtil.SilentCommandSourceStack(world), command);
    }

    static void emptyBlocks(ServerLevel world, Supplier<Stream<BlockPos>> supplier) {
        ((Stream) supplier.get()).forEach(pos -> {
            BlockEntity be0 = world.m_7702_(pos);
            if (be0 != null) {
                Clearable.tryClear(be0);
            }
            world.m_7731_(pos, Blocks.AIR.defaultBlockState(), 34);
        });
        ((Stream) supplier.get()).forEach(pos -> world.blockUpdated(pos, Blocks.AIR));
    }

    static boolean isAABBFullyInsideAABB(AABB smaller, AABB larger) {
        return smaller.minX >= larger.minX && smaller.minY >= larger.minY && smaller.minZ >= larger.minZ && smaller.maxX <= larger.maxX && smaller.maxY <= larger.maxY && smaller.maxZ <= larger.maxZ;
    }

    public static class SilentCommandSourceStack extends CommandSourceStack {

        public SilentCommandSourceStack(ServerLevel world) {
            super(world.getServer(), Vec3.atLowerCornerOf(world.m_220360_()), Vec2.ZERO, world, 4, "Server", Component.literal("Server"), world.getServer(), null, true, (commandContext, bl, i) -> {
            }, EntityAnchorArgument.Anchor.FEET, CommandSigningContext.ANONYMOUS, TaskChainer.immediate(world.getServer()), i -> {
            });
        }
    }
}
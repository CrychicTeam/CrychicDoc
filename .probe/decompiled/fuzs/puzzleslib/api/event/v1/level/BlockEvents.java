package fuzs.puzzleslib.api.event.v1.level;

import fuzs.puzzleslib.api.event.v1.core.EventInvoker;
import fuzs.puzzleslib.api.event.v1.core.EventResult;
import fuzs.puzzleslib.api.event.v1.data.MutableInt;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public final class BlockEvents {

    public static final EventInvoker<BlockEvents.Break> BREAK = EventInvoker.lookup(BlockEvents.Break.class);

    public static final EventInvoker<BlockEvents.DropExperience> DROP_EXPERIENCE = EventInvoker.lookup(BlockEvents.DropExperience.class);

    public static final EventInvoker<BlockEvents.FarmlandTrample> FARMLAND_TRAMPLE = EventInvoker.lookup(BlockEvents.FarmlandTrample.class);

    private BlockEvents() {
    }

    @FunctionalInterface
    public interface Break {

        EventResult onBreakBlock(ServerLevel var1, BlockPos var2, BlockState var3, Player var4, ItemStack var5);
    }

    @FunctionalInterface
    public interface DropExperience {

        void onDropExperience(ServerLevel var1, BlockPos var2, BlockState var3, Player var4, ItemStack var5, MutableInt var6);
    }

    @FunctionalInterface
    public interface FarmlandTrample {

        EventResult onFarmlandTrample(Level var1, BlockPos var2, BlockState var3, float var4, Entity var5);
    }
}
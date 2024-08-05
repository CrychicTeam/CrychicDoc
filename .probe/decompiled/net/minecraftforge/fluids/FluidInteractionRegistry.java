package net.minecraftforge.fluids;

import com.google.common.collect.UnmodifiableIterator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.ForgeEventFactory;

public final class FluidInteractionRegistry {

    private static final Map<FluidType, List<FluidInteractionRegistry.InteractionInformation>> INTERACTIONS = new HashMap();

    public static synchronized void addInteraction(FluidType source, FluidInteractionRegistry.InteractionInformation interaction) {
        ((List) INTERACTIONS.computeIfAbsent(source, s -> new ArrayList())).add(interaction);
    }

    public static boolean canInteract(Level level, BlockPos pos) {
        FluidState state = level.getFluidState(pos);
        UnmodifiableIterator var3 = LiquidBlock.POSSIBLE_FLOW_DIRECTIONS.iterator();
        while (var3.hasNext()) {
            Direction direction = (Direction) var3.next();
            BlockPos relativePos = pos.relative(direction.getOpposite());
            for (FluidInteractionRegistry.InteractionInformation interaction : (List) INTERACTIONS.getOrDefault(state.getFluidType(), Collections.emptyList())) {
                if (interaction.predicate().test(level, pos, relativePos, state)) {
                    interaction.interaction().interact(level, pos, relativePos, state);
                    return true;
                }
            }
        }
        return false;
    }

    static {
        addInteraction(ForgeMod.LAVA_TYPE.get(), new FluidInteractionRegistry.InteractionInformation(ForgeMod.WATER_TYPE.get(), fluidState -> fluidState.isSource() ? Blocks.OBSIDIAN.defaultBlockState() : Blocks.COBBLESTONE.defaultBlockState()));
        addInteraction(ForgeMod.LAVA_TYPE.get(), new FluidInteractionRegistry.InteractionInformation((level, currentPos, relativePos, currentState) -> level.getBlockState(currentPos.below()).m_60713_(Blocks.SOUL_SOIL) && level.getBlockState(relativePos).m_60713_(Blocks.BLUE_ICE), Blocks.BASALT.defaultBlockState()));
    }

    @FunctionalInterface
    public interface FluidInteraction {

        void interact(Level var1, BlockPos var2, BlockPos var3, FluidState var4);
    }

    @FunctionalInterface
    public interface HasFluidInteraction {

        boolean test(Level var1, BlockPos var2, BlockPos var3, FluidState var4);
    }

    public static record InteractionInformation(FluidInteractionRegistry.HasFluidInteraction predicate, FluidInteractionRegistry.FluidInteraction interaction) {

        public InteractionInformation(FluidType type, BlockState state) {
            this(type, fluidState -> state);
        }

        public InteractionInformation(FluidInteractionRegistry.HasFluidInteraction predicate, BlockState state) {
            this(predicate, fluidState -> state);
        }

        public InteractionInformation(FluidType type, Function<FluidState, BlockState> getState) {
            this((level, currentPos, relativePos, currentState) -> level.getFluidState(relativePos).getFluidType() == type, getState);
        }

        public InteractionInformation(FluidInteractionRegistry.HasFluidInteraction predicate, Function<FluidState, BlockState> getState) {
            this(predicate, (FluidInteractionRegistry.FluidInteraction) ((level, currentPos, relativePos, currentState) -> {
                level.setBlockAndUpdate(currentPos, ForgeEventFactory.fireFluidPlaceBlockEvent(level, currentPos, currentPos, (BlockState) getState.apply(currentState)));
                level.m_46796_(1501, currentPos, 0);
            }));
        }
    }
}
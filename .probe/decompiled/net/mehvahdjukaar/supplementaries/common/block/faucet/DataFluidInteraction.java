package net.mehvahdjukaar.supplementaries.common.block.faucet;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.mehvahdjukaar.moonlight.api.fluids.SoftFluidStack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;

public record DataFluidInteraction(RuleTest target, SoftFluidStack softFluid, int amount, Optional<BlockState> output) implements FaucetSource.BlState {

    public static final Codec<DataFluidInteraction> CODEC = RecordCodecBuilder.create(instance -> instance.group(RuleTest.CODEC.fieldOf("target").forGetter(DataFluidInteraction::target), SoftFluidStack.CODEC.fieldOf("fluid").forGetter(DataFluidInteraction::softFluid), ExtraCodecs.POSITIVE_INT.optionalFieldOf("amount", 1).forGetter(DataFluidInteraction::amount), BlockState.CODEC.optionalFieldOf("replace_with").forGetter(DataFluidInteraction::output)).apply(instance, DataFluidInteraction::new));

    public FluidOffer getProvidedFluid(Level level, BlockPos pos, Direction dir, BlockState state) {
        return this.target.test(state, level.random) ? FluidOffer.of(this.softFluid.copy()) : null;
    }

    public void drain(Level level, BlockPos pos, Direction dir, BlockState source, int amount) {
        this.output.ifPresent(s -> level.setBlock(pos, s, 3));
    }
}
package com.simibubi.create.foundation.ponder.instruction;

import com.simibubi.create.content.contraptions.bearing.IBearingBlockEntity;
import com.simibubi.create.content.contraptions.pulley.PulleyBlockEntity;
import com.simibubi.create.content.kinetics.deployer.DeployerBlockEntity;
import com.simibubi.create.content.trains.bogey.AbstractBogeyBlockEntity;
import com.simibubi.create.foundation.ponder.PonderScene;
import com.simibubi.create.foundation.ponder.PonderWorld;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;

public class AnimateBlockEntityInstruction extends TickingInstruction {

    protected double deltaPerTick;

    protected double totalDelta;

    protected double target;

    protected final BlockPos location;

    private BiConsumer<PonderWorld, Float> setter;

    private Function<PonderWorld, Float> getter;

    public static AnimateBlockEntityInstruction bearing(BlockPos location, float totalDelta, int ticks) {
        return new AnimateBlockEntityInstruction(location, totalDelta, ticks, (w, f) -> castIfPresent(w, location, IBearingBlockEntity.class).ifPresent(bte -> bte.setAngle(f)), w -> (Float) castIfPresent(w, location, IBearingBlockEntity.class).map(bte -> bte.getInterpolatedAngle(0.0F)).orElse(0.0F));
    }

    public static AnimateBlockEntityInstruction bogey(BlockPos location, float totalDelta, int ticks) {
        float movedPerTick = totalDelta / (float) ticks;
        return new AnimateBlockEntityInstruction(location, totalDelta, ticks, (w, f) -> castIfPresent(w, location, AbstractBogeyBlockEntity.class).ifPresent(bte -> bte.animate(f.equals(totalDelta) ? 0.0F : movedPerTick)), w -> 0.0F);
    }

    public static AnimateBlockEntityInstruction pulley(BlockPos location, float totalDelta, int ticks) {
        return new AnimateBlockEntityInstruction(location, totalDelta, ticks, (w, f) -> castIfPresent(w, location, PulleyBlockEntity.class).ifPresent(pulley -> pulley.animateOffset(f)), w -> (Float) castIfPresent(w, location, PulleyBlockEntity.class).map(pulley -> pulley.offset).orElse(0.0F));
    }

    public static AnimateBlockEntityInstruction deployer(BlockPos location, float totalDelta, int ticks) {
        return new AnimateBlockEntityInstruction(location, totalDelta, ticks, (w, f) -> castIfPresent(w, location, DeployerBlockEntity.class).ifPresent(deployer -> deployer.setAnimatedOffset(f)), w -> (Float) castIfPresent(w, location, DeployerBlockEntity.class).map(deployer -> deployer.getHandOffset(1.0F)).orElse(0.0F));
    }

    protected AnimateBlockEntityInstruction(BlockPos location, float totalDelta, int ticks, BiConsumer<PonderWorld, Float> setter, Function<PonderWorld, Float> getter) {
        super(false, ticks);
        this.location = location;
        this.setter = setter;
        this.getter = getter;
        this.deltaPerTick = (double) totalDelta * (1.0 / (double) ticks);
        this.totalDelta = (double) totalDelta;
        this.target = (double) totalDelta;
    }

    @Override
    protected final void firstTick(PonderScene scene) {
        super.firstTick(scene);
        this.target = (double) ((Float) this.getter.apply(scene.getWorld())).floatValue() + this.totalDelta;
    }

    @Override
    public void tick(PonderScene scene) {
        super.tick(scene);
        PonderWorld world = scene.getWorld();
        float current = (Float) this.getter.apply(world);
        float next = (float) (this.remainingTicks == 0 ? this.target : (double) current + this.deltaPerTick);
        this.setter.accept(world, next);
        if (this.remainingTicks == 0) {
            this.setter.accept(world, next);
        }
    }

    private static <T> Optional<T> castIfPresent(PonderWorld world, BlockPos pos, Class<T> beType) {
        BlockEntity blockEntity = world.m_7702_(pos);
        return beType.isInstance(blockEntity) ? Optional.of(beType.cast(blockEntity)) : Optional.empty();
    }
}
package net.mehvahdjukaar.moonlight.api.events;

import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.injectables.annotations.ExpectPlatform.Transformed;
import net.mehvahdjukaar.moonlight.api.events.forge.IFireConsumeBlockEventImpl;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public interface IFireConsumeBlockEvent extends SimpleEvent {

    @ExpectPlatform
    @Transformed
    static IFireConsumeBlockEvent create(BlockPos pos, Level level, BlockState state, int chance, int age, Direction face) {
        return IFireConsumeBlockEventImpl.create(pos, level, state, chance, age, face);
    }

    BlockPos getPos();

    BlockState getState();

    LevelAccessor getLevel();

    Direction getFace();

    int getAge();

    int getChance();

    void setFinalState(BlockState var1);

    @Nullable
    BlockState getFinalState();
}
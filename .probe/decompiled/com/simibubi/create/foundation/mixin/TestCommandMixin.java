package com.simibubi.create.foundation.mixin;

import com.simibubi.create.infrastructure.gametest.CreateTestFunction;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestRegistry;
import net.minecraft.gametest.framework.MultipleTestTracker;
import net.minecraft.gametest.framework.TestCommand;
import net.minecraft.gametest.framework.TestFunction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.StructureBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({ TestCommand.class })
public class TestCommandMixin {

    @Redirect(method = { "runTest(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/core/BlockPos;Lnet/minecraft/gametest/framework/MultipleTestTracker;)V" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/gametest/framework/GameTestRegistry;getTestFunction(Ljava/lang/String;)Lnet/minecraft/gametest/framework/TestFunction;"), require = 0)
    private static TestFunction create$getCorrectTestFunction(String testName, ServerLevel level, BlockPos pos, @Nullable MultipleTestTracker tracker) {
        StructureBlockEntity be = (StructureBlockEntity) level.m_7702_(pos);
        CompoundTag data = be.getPersistentData();
        if (!data.contains("CreateTestFunction", 8)) {
            return GameTestRegistry.getTestFunction(testName);
        } else {
            String name = data.getString("CreateTestFunction");
            CreateTestFunction function = (CreateTestFunction) CreateTestFunction.NAMES_TO_FUNCTIONS.get(name);
            if (function == null) {
                throw new IllegalStateException("Structure block has CreateTestFunction attached, but test [" + name + "] doesn't exist");
            } else {
                return function;
            }
        }
    }
}
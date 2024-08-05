package snownee.loquat.mixin;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.commands.PlaceCommand;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import snownee.loquat.Hooks;

@Mixin({ PlaceCommand.class })
public class PlaceCommandMixin {

    @Inject(method = { "placeStructure" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/server/commands/PlaceCommand;checkLoaded(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/level/ChunkPos;Lnet/minecraft/world/level/ChunkPos;)V") }, locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    private static void prePlaceStructure(CommandSourceStack commandSourceStack, Holder.Reference<Structure> reference, BlockPos blockPos, CallbackInfoReturnable<Integer> cir, ServerLevel serverLevel, Structure structure, ChunkGenerator chunkGenerator, StructureStart structureStart, BoundingBox boundingBox, ChunkPos chunkPos, ChunkPos chunkPos2) {
        Hooks.prePlaceStructure(serverLevel, chunkPos, chunkPos2);
    }

    @Inject(method = { "placeStructure" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/commands/CommandSourceStack;sendSuccess(Ljava/util/function/Supplier;Z)V") }, locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    private static void postPlaceStructure(CommandSourceStack commandSourceStack, Holder.Reference<Structure> reference, BlockPos blockPos, CallbackInfoReturnable<Integer> cir, ServerLevel serverLevel, Structure structure2, ChunkGenerator chunkGenerator, StructureStart structureStart, BoundingBox boundingBox, ChunkPos chunkPos, ChunkPos chunkPos2, String string) {
        Hooks.postPlaceStructure(serverLevel, chunkPos, chunkPos2);
    }
}
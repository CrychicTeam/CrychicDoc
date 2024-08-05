package noobanidus.mods.lootr.mixins;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.structures.EndCityPieces;
import noobanidus.mods.lootr.api.LootrAPI;
import noobanidus.mods.lootr.config.ConfigManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ EndCityPieces.EndCityPiece.class })
public class MixinEndCityPieces$EndCityPiece {

    @Inject(method = { "handleDataMarker" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/world/level/ServerLevelAccessor;addFreshEntity(Lnet/minecraft/world/entity/Entity;)Z") }, cancellable = true, require = 0)
    private void LootrHandleDataMarker(String marker, BlockPos position, ServerLevelAccessor level, RandomSource random, BoundingBox boundingBox, CallbackInfo ci) {
        if (ConfigManager.CONVERT_ELYTRAS.get()) {
            if (marker.startsWith("Elytra")) {
                EndCityPieces.EndCityPiece piece = (EndCityPieces.EndCityPiece) this;
                level.m_7731_(position.below(), (BlockState) Blocks.CHEST.defaultBlockState().m_61124_(ChestBlock.FACING, piece.m_6830_().rotate(Direction.SOUTH)), 3);
                if (level.m_7702_(position.below()) instanceof RandomizableContainerBlockEntity chest) {
                    chest.setLootTable(LootrAPI.ELYTRA_CHEST, random.nextLong());
                }
                ci.cancel();
            }
        }
    }
}
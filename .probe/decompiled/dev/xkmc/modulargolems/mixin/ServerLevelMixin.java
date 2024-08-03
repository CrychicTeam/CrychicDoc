package dev.xkmc.modulargolems.mixin;

import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.entity.common.GolemFlags;
import java.util.List;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ ServerLevel.class })
public class ServerLevelMixin {

    @Inject(at = { @At("HEAD") }, method = { "findLightningRod" }, cancellable = true)
    public void findLightningRod(BlockPos blockpos, CallbackInfoReturnable<Optional<BlockPos>> cir) {
        ServerLevel self = (ServerLevel) this;
        Optional<BlockPos> optional = self.getPoiManager().findClosest(poi -> poi.is(PoiTypes.LIGHTNING_ROD), pos -> pos.m_123342_() == self.m_6924_(Heightmap.Types.WORLD_SURFACE, pos.m_123341_(), pos.m_123343_()) - 1, blockpos, 128, PoiManager.Occupancy.ANY);
        if (optional.isPresent()) {
            cir.setReturnValue(optional);
        }
        AABB aabb = new AABB(blockpos, new BlockPos(blockpos.m_123341_(), self.m_151558_(), blockpos.m_123343_())).inflate(64.0);
        List<AbstractGolemEntity> list = self.m_6443_(AbstractGolemEntity.class, aabb, e -> e != null && e.m_6084_() && self.m_45527_(e.m_20183_()) && e.hasFlag(GolemFlags.THUNDER_IMMUNE));
        if (list.size() > 0) {
            cir.setReturnValue(Optional.of(((AbstractGolemEntity) list.get(self.f_46441_.nextInt(list.size()))).m_20183_()));
        }
    }
}
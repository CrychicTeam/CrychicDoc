package yesman.epicfight.world.capabilities.entitypatch.boss.enderdragon;

import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.enderdragon.phases.AbstractDragonPhaseInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.EndPodiumFeature;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;

public abstract class PatchedDragonPhase extends AbstractDragonPhaseInstance {

    protected final EnderDragonPatch dragonpatch;

    public PatchedDragonPhase(EnderDragon dragon) {
        super(dragon);
        this.dragonpatch = EpicFightCapabilities.getEntityPatch(dragon, EnderDragonPatch.class);
    }

    @Override
    public void doClientTick() {
        this.f_31176_.oFlapTime = 0.5F;
        this.f_31176_.flapTime = 0.5F;
    }

    protected static boolean isValidTarget(LivingEntity entity) {
        return entity.canBeSeenAsEnemy();
    }

    protected static boolean isInEndSpikes(LivingEntity entity) {
        BlockPos blockpos = entity.m_9236_().m_5452_(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, EndPodiumFeature.getLocation(new BlockPos(0, 0, 0)));
        return blockpos.m_123331_(new BlockPos.MutableBlockPos(entity.m_20185_(), (double) blockpos.m_123342_(), entity.m_20189_())) < 2000.0;
    }

    protected List<Player> getPlayersNearbyWithin(double within) {
        return this.f_31176_.m_9236_().m_45955_(EnderDragonPatch.DRAGON_TARGETING, this.f_31176_, this.f_31176_.m_20191_().inflate(within, within, within));
    }
}
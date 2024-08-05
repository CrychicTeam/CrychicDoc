package io.redspace.ironsspellbooks.entity.mobs.goals;

import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class RoamVillageGoal extends PatrolNearLocationGoal {

    GlobalPos villagePoi;

    int searchCooldown;

    public RoamVillageGoal(PathfinderMob pMob, float radius, double pSpeedModifier) {
        super(pMob, radius, pSpeedModifier);
    }

    @Nullable
    @Override
    protected Vec3 getPosition() {
        return this.villagePoi != null ? Vec3.atBottomCenterOf(this.villagePoi.pos()) : super.getPosition();
    }

    @Override
    public boolean canUse() {
        if (this.villagePoi == null && this.searchCooldown-- <= 0) {
            this.findVillagePoi();
            this.searchCooldown = 200;
        }
        return (this.f_25725_.f_19853_.isDay() || this.isDuringRaid()) && this.villagePoi != null && super.m_8036_();
    }

    private boolean isDuringRaid() {
        return false;
    }

    protected void findVillagePoi() {
        if (this.f_25725_.f_19853_ instanceof ServerLevel serverLevel) {
            Optional<BlockPos> optional1 = serverLevel.getPoiManager().find(poiTypeHolder -> poiTypeHolder.is(PoiTypes.MEETING), x -> true, this.f_25725_.m_20183_(), 100, PoiManager.Occupancy.ANY);
            optional1.ifPresent(blockPos -> this.villagePoi = GlobalPos.of(serverLevel.m_46472_(), blockPos));
        }
    }
}
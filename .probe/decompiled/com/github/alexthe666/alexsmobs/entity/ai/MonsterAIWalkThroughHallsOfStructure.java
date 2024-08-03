package com.github.alexthe666.alexsmobs.entity.ai;

import com.github.alexthe666.alexsmobs.AlexsMobs;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.phys.Vec3;

public class MonsterAIWalkThroughHallsOfStructure extends RandomStrollGoal {

    private TagKey<Structure> structureTagKey;

    private double maximumDistance = 0.0;

    private double maximumYDistance = 3.0;

    private int errorCooldown = 0;

    public MonsterAIWalkThroughHallsOfStructure(PathfinderMob mob, double speed, int chance, TagKey<Structure> structureTagKey, double maximumDistance) {
        super(mob, speed, chance, false);
        this.structureTagKey = structureTagKey;
        this.maximumDistance = 32.0;
    }

    @Override
    public boolean canUse() {
        if (this.errorCooldown > 0) {
            this.errorCooldown--;
        }
        return super.canUse();
    }

    @Override
    public void tick() {
        super.m_8037_();
        if (this.errorCooldown > 0) {
            this.errorCooldown--;
        }
    }

    @Nullable
    @Override
    protected Vec3 getPosition() {
        StructureStart start = this.getNearestStructure(this.f_25725_.m_20183_());
        if (start != null && start.isValid() || this.errorCooldown > 0) {
            List<BlockPos> validPieceCenters = new ArrayList();
            for (StructurePiece piece : start.getPieces()) {
                BoundingBox boundingbox = piece.getBoundingBox();
                BlockPos blockpos = boundingbox.getCenter();
                BlockPos blockpos1 = new BlockPos(blockpos.m_123341_(), boundingbox.minY(), blockpos.m_123343_());
                double yDist = (double) Math.abs(blockpos1.m_123342_() - this.f_25725_.m_20183_().m_123342_());
                if (this.f_25725_.m_20238_(Vec3.atCenterOf(blockpos1)) <= this.maximumDistance * this.maximumDistance && yDist < this.maximumYDistance) {
                    validPieceCenters.add(blockpos1);
                }
            }
            if (!validPieceCenters.isEmpty()) {
                BlockPos randomCenter = validPieceCenters.size() > 1 ? (BlockPos) validPieceCenters.get(this.f_25725_.m_217043_().nextInt(validPieceCenters.size() - 1)) : (BlockPos) validPieceCenters.get(0);
                return Vec3.atCenterOf(randomCenter.offset(this.f_25725_.m_217043_().nextInt(2) - 1, 0, this.f_25725_.m_217043_().nextInt(2) - 1));
            }
        }
        return this.getPositionTowardsAnywhere();
    }

    @Nullable
    private Vec3 getPositionTowardsAnywhere() {
        return DefaultRandomPos.getPos(this.f_25725_, 10, 7);
    }

    @Nullable
    private StructureStart getNearestStructure(BlockPos pos) {
        ServerLevel serverlevel = (ServerLevel) this.f_25725_.m_9236_();
        try {
            StructureStart start = serverlevel.structureManager().getStructureWithPieceAt(pos, this.structureTagKey);
            if (start.isValid()) {
                return start;
            } else {
                BlockPos nearestOf = serverlevel.findNearestMapStructure(this.structureTagKey, pos, (int) (this.maximumDistance / 16.0), false);
                return nearestOf != null && !(nearestOf.m_203198_(this.f_25725_.m_20185_(), this.f_25725_.m_20186_(), this.f_25725_.m_20189_()) > 256.0) && serverlevel.m_46749_(nearestOf) ? serverlevel.structureManager().getStructureWithPieceAt(nearestOf, this.structureTagKey) : null;
            }
        } catch (Exception var5) {
            AlexsMobs.LOGGER.warn(this.f_25725_ + " encountered an issue searching for a nearby structure.");
            this.errorCooldown = 2000 + this.f_25725_.m_217043_().nextInt(2000);
            return null;
        }
    }
}
package me.jellysquid.mods.lithium.common.entity.movement;

import com.google.common.collect.AbstractIterator;
import java.util.ArrayList;
import java.util.List;
import me.jellysquid.mods.lithium.common.block.BlockCountingSection;
import me.jellysquid.mods.lithium.common.block.BlockStateFlags;
import me.jellysquid.mods.lithium.common.shapes.VoxelShapeCaster;
import me.jellysquid.mods.lithium.common.util.Pos;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ChunkAwareBlockCollisionSweeper extends AbstractIterator<VoxelShape> {

    private final BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

    private final AABB box;

    private final VoxelShape shape;

    private final Level world;

    private final CollisionContext context;

    private final int minX;

    private final int minY;

    private final int minZ;

    private final int maxX;

    private final int maxY;

    private final int maxZ;

    private int chunkX;

    private int chunkYIndex;

    private int chunkZ;

    private int cStartX;

    private int cStartZ;

    private int cEndX;

    private int cEndZ;

    private int cX;

    private int cY;

    private int cZ;

    private int maxHitX;

    private int maxHitY;

    private int maxHitZ;

    private int maxIndex;

    private int index;

    private int cTotalSize;

    private int cIterated;

    private boolean sectionOversizedBlocks;

    private ChunkAccess cachedChunk;

    private LevelChunkSection cachedChunkSection;

    public ChunkAwareBlockCollisionSweeper(Level world, Entity entity, AABB box) {
        this.box = box;
        this.shape = Shapes.create(box);
        this.context = entity == null ? CollisionContext.empty() : CollisionContext.of(entity);
        this.world = world;
        this.minX = Mth.floor(box.minX - 1.0E-7);
        this.maxX = Mth.floor(box.maxX + 1.0E-7);
        this.minY = Mth.clamp(Mth.floor(box.minY - 1.0E-7), Pos.BlockCoord.getMinY(this.world), Pos.BlockCoord.getMaxYInclusive(this.world));
        this.maxY = Mth.clamp(Mth.floor(box.maxY + 1.0E-7), Pos.BlockCoord.getMinY(this.world), Pos.BlockCoord.getMaxYInclusive(this.world));
        this.minZ = Mth.floor(box.minZ - 1.0E-7);
        this.maxZ = Mth.floor(box.maxZ + 1.0E-7);
        this.chunkX = Pos.ChunkCoord.fromBlockCoord(expandMin(this.minX));
        this.chunkZ = Pos.ChunkCoord.fromBlockCoord(expandMin(this.minZ));
        this.cIterated = 0;
        this.cTotalSize = 0;
        this.maxHitX = Integer.MIN_VALUE;
        this.maxHitY = Integer.MIN_VALUE;
        this.maxHitZ = Integer.MIN_VALUE;
        this.maxIndex = Integer.MIN_VALUE;
        this.index = 0;
        this.chunkX--;
    }

    private boolean nextSection() {
        while (true) {
            if (this.cachedChunk != null && this.chunkYIndex < Pos.SectionYIndex.getMaxYSectionIndexInclusive(this.world) && this.chunkYIndex < Pos.SectionYIndex.fromBlockCoord(this.world, expandMax(this.maxY))) {
                this.chunkYIndex++;
                this.cachedChunkSection = this.cachedChunk.getSections()[this.chunkYIndex];
            } else {
                this.chunkYIndex = Mth.clamp(Pos.SectionYIndex.fromBlockCoord(this.world, expandMin(this.minY)), Pos.SectionYIndex.getMinYSectionIndex(this.world), Pos.SectionYIndex.getMaxYSectionIndexInclusive(this.world));
                if (this.chunkX < Pos.ChunkCoord.fromBlockCoord(expandMax(this.maxX))) {
                    this.chunkX++;
                } else {
                    this.chunkX = Pos.ChunkCoord.fromBlockCoord(expandMin(this.minX));
                    if (this.chunkZ >= Pos.ChunkCoord.fromBlockCoord(expandMax(this.maxZ))) {
                        return false;
                    }
                    this.chunkZ++;
                }
                this.cachedChunk = this.world.getChunk(this.chunkX, this.chunkZ, ChunkStatus.FULL, false);
                if (this.cachedChunk != null) {
                    this.cachedChunkSection = this.cachedChunk.getSections()[this.chunkYIndex];
                }
            }
            if (this.cachedChunk != null && this.cachedChunkSection != null && !this.cachedChunkSection.hasOnlyAir()) {
                this.sectionOversizedBlocks = hasChunkSectionOversizedBlocks(this.cachedChunk, this.chunkYIndex);
                int sizeExtension = this.sectionOversizedBlocks ? 1 : 0;
                this.cEndX = Math.min(this.maxX + sizeExtension, Pos.BlockCoord.getMaxInSectionCoord(this.chunkX));
                int cEndY = Math.min(this.maxY + sizeExtension, Pos.BlockCoord.getMaxYInSectionIndex(this.world, this.chunkYIndex));
                this.cEndZ = Math.min(this.maxZ + sizeExtension, Pos.BlockCoord.getMaxInSectionCoord(this.chunkZ));
                this.cStartX = Math.max(this.minX - sizeExtension, Pos.BlockCoord.getMinInSectionCoord(this.chunkX));
                int cStartY = Math.max(this.minY - sizeExtension, Pos.BlockCoord.getMinYInSectionIndex(this.world, this.chunkYIndex));
                this.cStartZ = Math.max(this.minZ - sizeExtension, Pos.BlockCoord.getMinInSectionCoord(this.chunkZ));
                this.cX = this.cStartX;
                this.cY = cStartY;
                this.cZ = this.cStartZ;
                this.cTotalSize = (this.cEndX - this.cStartX + 1) * (cEndY - cStartY + 1) * (this.cEndZ - this.cStartZ + 1);
                if (this.cTotalSize != 0) {
                    this.cIterated = 0;
                    return true;
                }
            }
        }
    }

    public VoxelShape computeNext() {
        while (this.cIterated < this.cTotalSize || this.nextSection()) {
            this.cIterated++;
            int x = this.cX;
            int y = this.cY;
            int z = this.cZ;
            if (this.cX < this.cEndX) {
                this.cX++;
            } else if (this.cZ < this.cEndZ) {
                this.cX = this.cStartX;
                this.cZ++;
            } else {
                this.cX = this.cStartX;
                this.cZ = this.cStartZ;
                this.cY++;
            }
            int edgesHit = this.sectionOversizedBlocks ? (x >= this.minX && x <= this.maxX ? 0 : 1) + (y >= this.minY && y <= this.maxY ? 0 : 1) + (z >= this.minZ && z <= this.maxZ ? 0 : 1) : 0;
            if (edgesHit != 3) {
                BlockState state = this.cachedChunkSection.getBlockState(x & 15, y & 15, z & 15);
                if (canInteractWithBlock(state, edgesHit)) {
                    this.pos.set(x, y, z);
                    VoxelShape collisionShape = state.m_60742_(this.world, this.pos, this.context);
                    if (collisionShape != Shapes.empty() && collisionShape != null) {
                        VoxelShape collidedShape = getCollidedShape(this.box, this.shape, collisionShape, x, y, z);
                        if (collidedShape != null) {
                            if (z >= this.maxHitZ && (z > this.maxHitZ || y >= this.maxHitY && (y > this.maxHitY || x > this.maxHitX))) {
                                this.maxHitX = x;
                                this.maxHitY = y;
                                this.maxHitZ = z;
                                this.maxIndex = this.index;
                            }
                            this.index++;
                            return collidedShape;
                        }
                    }
                }
            }
        }
        return (VoxelShape) this.endOfData();
    }

    private static boolean canInteractWithBlock(BlockState state, int edgesHit) {
        return (edgesHit != 1 || state.m_60779_()) && (edgesHit != 2 || state.m_60734_() == Blocks.MOVING_PISTON);
    }

    private static VoxelShape getCollidedShape(AABB entityBox, VoxelShape entityShape, VoxelShape shape, int x, int y, int z) {
        if (shape == Shapes.block()) {
            return entityBox.intersects((double) x, (double) y, (double) z, (double) x + 1.0, (double) y + 1.0, (double) z + 1.0) ? shape.move((double) x, (double) y, (double) z) : null;
        } else if (shape instanceof VoxelShapeCaster) {
            return ((VoxelShapeCaster) shape).intersects(entityBox, (double) x, (double) y, (double) z) ? shape.move((double) x, (double) y, (double) z) : null;
        } else {
            shape = shape.move((double) x, (double) y, (double) z);
            return Shapes.joinIsNotEmpty(shape, entityShape, BooleanOp.AND) ? shape : null;
        }
    }

    private static int expandMin(int coord) {
        return coord - 1;
    }

    private static int expandMax(int coord) {
        return coord + 1;
    }

    private static boolean hasChunkSectionOversizedBlocks(ChunkAccess chunk, int chunkY) {
        if (!BlockStateFlags.ENABLED) {
            return true;
        } else {
            LevelChunkSection section = chunk.getSections()[chunkY];
            return section != null && ((BlockCountingSection) section).mayContainAny(BlockStateFlags.OVERSIZED_SHAPE);
        }
    }

    public List<VoxelShape> collectAll() {
        ArrayList<VoxelShape> collisions = new ArrayList();
        while (this.hasNext()) {
            collisions.add((VoxelShape) this.next());
        }
        if (collisions.size() >= 2) {
            collisions.set(this.maxIndex, (VoxelShape) collisions.set(collisions.size() - 1, (VoxelShape) collisions.get(this.maxIndex)));
        }
        return collisions;
    }
}
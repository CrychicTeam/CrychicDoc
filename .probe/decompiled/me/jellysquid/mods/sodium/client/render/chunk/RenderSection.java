package me.jellysquid.mods.sodium.client.render.chunk;

import me.jellysquid.mods.sodium.client.render.chunk.data.BuiltSectionInfo;
import me.jellysquid.mods.sodium.client.render.chunk.occlusion.GraphDirectionSet;
import me.jellysquid.mods.sodium.client.render.chunk.region.RenderRegion;
import me.jellysquid.mods.sodium.client.util.task.CancellationToken;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.embeddedt.embeddium.render.chunk.sorting.TranslucentQuadAnalyzer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RenderSection {

    private final RenderRegion region;

    private final int sectionIndex;

    private final int chunkX;

    private final int chunkY;

    private final int chunkZ;

    private long visibilityData = 0L;

    private int incomingDirections;

    private int lastVisibleFrame = -1;

    private int adjacentMask;

    public RenderSection adjacentDown;

    public RenderSection adjacentUp;

    public RenderSection adjacentNorth;

    public RenderSection adjacentSouth;

    public RenderSection adjacentWest;

    public RenderSection adjacentEast;

    private boolean built = false;

    private int flags = 0;

    @Nullable
    private BlockEntity[] globalBlockEntities;

    @Nullable
    private BlockEntity[] culledBlockEntities;

    @Nullable
    private TextureAtlasSprite[] animatedSprites;

    private TranslucentQuadAnalyzer.SortState sortState;

    @Nullable
    private CancellationToken buildCancellationToken = null;

    @Nullable
    private ChunkUpdateType pendingUpdateType;

    private int lastBuiltFrame = -1;

    private int lastSubmittedFrame = -1;

    private boolean disposed;

    public double lastCameraX;

    public double lastCameraY;

    public double lastCameraZ;

    public RenderSection(RenderRegion region, int chunkX, int chunkY, int chunkZ) {
        this.chunkX = chunkX;
        this.chunkY = chunkY;
        this.chunkZ = chunkZ;
        int rX = this.getChunkX() & 7;
        int rY = this.getChunkY() & 3;
        int rZ = this.getChunkZ() & 7;
        this.sectionIndex = LocalSectionIndex.pack(rX, rY, rZ);
        this.region = region;
    }

    public RenderSection getAdjacent(int direction) {
        return switch(direction) {
            case 0 ->
                this.adjacentDown;
            case 1 ->
                this.adjacentUp;
            case 2 ->
                this.adjacentNorth;
            case 3 ->
                this.adjacentSouth;
            case 4 ->
                this.adjacentWest;
            case 5 ->
                this.adjacentEast;
            default ->
                null;
        };
    }

    public void setAdjacentNode(int direction, RenderSection node) {
        if (node == null) {
            this.adjacentMask = this.adjacentMask & ~GraphDirectionSet.of(direction);
        } else {
            this.adjacentMask = this.adjacentMask | GraphDirectionSet.of(direction);
        }
        switch(direction) {
            case 0:
                this.adjacentDown = node;
                break;
            case 1:
                this.adjacentUp = node;
                break;
            case 2:
                this.adjacentNorth = node;
                break;
            case 3:
                this.adjacentSouth = node;
                break;
            case 4:
                this.adjacentWest = node;
                break;
            case 5:
                this.adjacentEast = node;
        }
    }

    public int getAdjacentMask() {
        return this.adjacentMask;
    }

    public void delete() {
        if (this.buildCancellationToken != null) {
            this.buildCancellationToken.setCancelled();
            this.buildCancellationToken = null;
        }
        this.clearRenderState();
        this.disposed = true;
    }

    public void setInfo(@Nullable BuiltSectionInfo info) {
        if (info != null) {
            this.setRenderState(info);
        } else {
            this.clearRenderState();
        }
    }

    private void setRenderState(@NotNull BuiltSectionInfo info) {
        this.built = true;
        this.flags = info.flags;
        this.visibilityData = info.visibilityData;
        this.globalBlockEntities = info.globalBlockEntities;
        this.culledBlockEntities = info.culledBlockEntities;
        this.animatedSprites = info.animatedSprites;
    }

    private void clearRenderState() {
        this.built = false;
        this.flags = 0;
        this.visibilityData = 0L;
        this.globalBlockEntities = null;
        this.culledBlockEntities = null;
        this.animatedSprites = null;
    }

    public SectionPos getPosition() {
        return SectionPos.of(this.chunkX, this.chunkY, this.chunkZ);
    }

    public int getOriginX() {
        return this.chunkX << 4;
    }

    public int getOriginY() {
        return this.chunkY << 4;
    }

    public int getOriginZ() {
        return this.chunkZ << 4;
    }

    public float getSquaredDistance(BlockPos pos) {
        return this.getSquaredDistance((float) pos.m_123341_() + 0.5F, (float) pos.m_123342_() + 0.5F, (float) pos.m_123343_() + 0.5F);
    }

    public float getSquaredDistance(float x, float y, float z) {
        float xDist = x - (float) this.getCenterX();
        float yDist = y - (float) this.getCenterY();
        float zDist = z - (float) this.getCenterZ();
        return xDist * xDist + yDist * yDist + zDist * zDist;
    }

    public int getCenterX() {
        return this.getOriginX() + 8;
    }

    public int getCenterY() {
        return this.getOriginY() + 8;
    }

    public int getCenterZ() {
        return this.getOriginZ() + 8;
    }

    public int getChunkX() {
        return this.chunkX;
    }

    public int getChunkY() {
        return this.chunkY;
    }

    public int getChunkZ() {
        return this.chunkZ;
    }

    public boolean isDisposed() {
        return this.disposed;
    }

    public String toString() {
        return String.format("RenderSection at chunk (%d, %d, %d) from (%d, %d, %d) to (%d, %d, %d)", this.chunkX, this.chunkY, this.chunkZ, this.getOriginX(), this.getOriginY(), this.getOriginZ(), this.getOriginX() + 15, this.getOriginY() + 15, this.getOriginZ() + 15);
    }

    public boolean isBuilt() {
        return this.built;
    }

    public int getSectionIndex() {
        return this.sectionIndex;
    }

    public RenderRegion getRegion() {
        return this.region;
    }

    public void setLastVisibleFrame(int frame) {
        this.lastVisibleFrame = frame;
    }

    public int getLastVisibleFrame() {
        return this.lastVisibleFrame;
    }

    public int getIncomingDirections() {
        return this.incomingDirections;
    }

    public void addIncomingDirections(int directions) {
        this.incomingDirections |= directions;
    }

    public void setIncomingDirections(int directions) {
        this.incomingDirections = directions;
    }

    public int getFlags() {
        return this.flags;
    }

    public boolean isAlignedWithSectionOnGrid(int otherX, int otherY, int otherZ) {
        return this.chunkX == otherX || this.chunkY == otherY || this.chunkZ == otherZ;
    }

    public long getVisibilityData() {
        return this.visibilityData;
    }

    @Nullable
    public TextureAtlasSprite[] getAnimatedSprites() {
        return this.animatedSprites;
    }

    @Nullable
    public BlockEntity[] getCulledBlockEntities() {
        return this.culledBlockEntities;
    }

    @Nullable
    public BlockEntity[] getGlobalBlockEntities() {
        return this.globalBlockEntities;
    }

    @Nullable
    public TranslucentQuadAnalyzer.SortState getSortState() {
        return this.sortState;
    }

    public boolean containsTranslucentGeometry() {
        return (this.getFlags() & 16) != 0;
    }

    public void setSortState(@Nullable TranslucentQuadAnalyzer.SortState data) {
        this.sortState = data != null ? data.compactForStorage() : null;
    }

    @Nullable
    public CancellationToken getBuildCancellationToken() {
        return this.buildCancellationToken;
    }

    public void setBuildCancellationToken(@Nullable CancellationToken token) {
        this.buildCancellationToken = token;
    }

    @Nullable
    public ChunkUpdateType getPendingUpdate() {
        return this.pendingUpdateType;
    }

    public void setPendingUpdate(@Nullable ChunkUpdateType type) {
        this.pendingUpdateType = type;
    }

    public int getLastBuiltFrame() {
        return this.lastBuiltFrame;
    }

    public void setLastBuiltFrame(int lastBuiltFrame) {
        this.lastBuiltFrame = lastBuiltFrame;
    }

    public int getLastSubmittedFrame() {
        return this.lastSubmittedFrame;
    }

    public void setLastSubmittedFrame(int lastSubmittedFrame) {
        this.lastSubmittedFrame = lastSubmittedFrame;
    }
}
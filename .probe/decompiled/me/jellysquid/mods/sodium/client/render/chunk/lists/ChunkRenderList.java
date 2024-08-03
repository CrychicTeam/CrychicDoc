package me.jellysquid.mods.sodium.client.render.chunk.lists;

import me.jellysquid.mods.sodium.client.render.chunk.RenderSection;
import me.jellysquid.mods.sodium.client.render.chunk.region.RenderRegion;
import me.jellysquid.mods.sodium.client.util.iterator.ByteArrayIterator;
import me.jellysquid.mods.sodium.client.util.iterator.ByteIterator;
import me.jellysquid.mods.sodium.client.util.iterator.ReversibleByteArrayIterator;
import org.jetbrains.annotations.Nullable;

public class ChunkRenderList {

    private final RenderRegion region;

    private final byte[] sectionsWithGeometry = new byte[256];

    private int sectionsWithGeometryCount = 0;

    private final byte[] sectionsWithSprites = new byte[256];

    private int sectionsWithSpritesCount = 0;

    private final byte[] sectionsWithEntities = new byte[256];

    private int sectionsWithEntitiesCount = 0;

    private int size;

    private int lastVisibleFrame;

    public ChunkRenderList(RenderRegion region) {
        this.region = region;
    }

    public void reset(int frame) {
        this.sectionsWithGeometryCount = 0;
        this.sectionsWithSpritesCount = 0;
        this.sectionsWithEntitiesCount = 0;
        this.size = 0;
        this.lastVisibleFrame = frame;
    }

    public void add(RenderSection render) {
        if (this.size >= 256) {
            throw new ArrayIndexOutOfBoundsException("Render list is full");
        } else {
            this.size++;
            int index = render.getSectionIndex();
            int flags = render.getFlags();
            this.sectionsWithGeometry[this.sectionsWithGeometryCount] = (byte) index;
            this.sectionsWithGeometryCount += flags >>> 0 & 1;
            this.sectionsWithSprites[this.sectionsWithSpritesCount] = (byte) index;
            this.sectionsWithSpritesCount += flags >>> 2 & 1;
            this.sectionsWithEntities[this.sectionsWithEntitiesCount] = (byte) index;
            this.sectionsWithEntitiesCount += flags >>> 1 & 1;
        }
    }

    @Nullable
    public ByteIterator sectionsWithGeometryIterator(boolean reverse) {
        return this.sectionsWithGeometryCount == 0 ? null : new ReversibleByteArrayIterator(this.sectionsWithGeometry, this.sectionsWithGeometryCount, reverse);
    }

    @Nullable
    public ByteIterator sectionsWithSpritesIterator() {
        return this.sectionsWithSpritesCount == 0 ? null : new ByteArrayIterator(this.sectionsWithSprites, this.sectionsWithSpritesCount);
    }

    @Nullable
    public ByteIterator sectionsWithEntitiesIterator() {
        return this.sectionsWithEntitiesCount == 0 ? null : new ByteArrayIterator(this.sectionsWithEntities, this.sectionsWithEntitiesCount);
    }

    public int getSectionsWithGeometryCount() {
        return this.sectionsWithGeometryCount;
    }

    public int getSectionsWithSpritesCount() {
        return this.sectionsWithSpritesCount;
    }

    public int getSectionsWithEntitiesCount() {
        return this.sectionsWithEntitiesCount;
    }

    public int getLastVisibleFrame() {
        return this.lastVisibleFrame;
    }

    public RenderRegion getRegion() {
        return this.region;
    }

    public int size() {
        return this.size;
    }
}
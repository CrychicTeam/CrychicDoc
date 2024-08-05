package me.jellysquid.mods.sodium.client.render.chunk.data;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.function.IntFunction;
import me.jellysquid.mods.sodium.client.render.chunk.occlusion.VisibilityEncoding;
import me.jellysquid.mods.sodium.client.render.chunk.terrain.DefaultTerrainRenderPasses;
import me.jellysquid.mods.sodium.client.render.chunk.terrain.TerrainRenderPass;
import me.jellysquid.mods.sodium.client.render.texture.SpriteUtil;
import net.minecraft.client.renderer.chunk.VisibilitySet;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BuiltSectionInfo {

    public static final BuiltSectionInfo EMPTY = createEmptyData();

    public final int flags;

    public final long visibilityData;

    @Nullable
    public final BlockEntity[] globalBlockEntities;

    @Nullable
    public final BlockEntity[] culledBlockEntities;

    @Nullable
    public final TextureAtlasSprite[] animatedSprites;

    private BuiltSectionInfo(@NotNull Collection<TerrainRenderPass> blockRenderPasses, @NotNull Collection<BlockEntity> globalBlockEntities, @NotNull Collection<BlockEntity> culledBlockEntities, @NotNull Collection<TextureAtlasSprite> animatedSprites, @NotNull VisibilitySet occlusionData) {
        this.globalBlockEntities = toArray(globalBlockEntities, BlockEntity[]::new);
        this.culledBlockEntities = toArray(culledBlockEntities, BlockEntity[]::new);
        this.animatedSprites = toArray(animatedSprites, TextureAtlasSprite[]::new);
        int flags = 0;
        if (!blockRenderPasses.isEmpty()) {
            flags |= 1;
            if (blockRenderPasses.contains(DefaultTerrainRenderPasses.TRANSLUCENT)) {
                flags |= 16;
            }
        }
        if (!culledBlockEntities.isEmpty()) {
            flags |= 2;
        }
        if (!animatedSprites.isEmpty()) {
            flags |= 4;
        }
        this.flags = flags;
        this.visibilityData = VisibilityEncoding.encode(occlusionData);
    }

    private static BuiltSectionInfo createEmptyData() {
        VisibilitySet occlusionData = new VisibilitySet();
        occlusionData.add(EnumSet.allOf(Direction.class));
        BuiltSectionInfo.Builder meshInfo = new BuiltSectionInfo.Builder();
        meshInfo.setOcclusionData(occlusionData);
        return meshInfo.build();
    }

    private static <T> T[] toArray(Collection<T> collection, IntFunction<T[]> allocator) {
        return (T[]) (collection.isEmpty() ? null : collection.toArray(allocator));
    }

    public static class Builder {

        private final List<TerrainRenderPass> blockRenderPasses = new ArrayList();

        private final List<BlockEntity> globalBlockEntities = new ArrayList();

        private final List<BlockEntity> culledBlockEntities = new ArrayList();

        private final Set<TextureAtlasSprite> animatedSprites = new ObjectOpenHashSet();

        private VisibilitySet occlusionData;

        public void addRenderPass(TerrainRenderPass pass) {
            this.blockRenderPasses.add(pass);
        }

        public void setOcclusionData(VisibilitySet data) {
            this.occlusionData = data;
        }

        public void addSprite(TextureAtlasSprite sprite) {
            if (SpriteUtil.hasAnimation(sprite)) {
                this.animatedSprites.add(sprite);
            }
        }

        public void addBlockEntity(BlockEntity entity, boolean cull) {
            (cull ? this.culledBlockEntities : this.globalBlockEntities).add(entity);
        }

        public BuiltSectionInfo build() {
            return new BuiltSectionInfo(this.blockRenderPasses, this.globalBlockEntities, this.culledBlockEntities, this.animatedSprites, this.occlusionData);
        }
    }
}
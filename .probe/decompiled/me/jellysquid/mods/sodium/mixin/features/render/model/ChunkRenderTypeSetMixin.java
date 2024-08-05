package me.jellysquid.mods.sodium.mixin.features.render.model;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import java.util.BitSet;
import java.util.Iterator;
import java.util.List;
import net.minecraft.Util;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.client.ChunkRenderTypeSet;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = { ChunkRenderTypeSet.class }, remap = false)
public class ChunkRenderTypeSetMixin {

    @Shadow
    @Final
    private static RenderType[] CHUNK_RENDER_TYPES;

    private ImmutableList<RenderType> embeddium$containedTypes;

    private int mask;

    private static final int POSSIBLE_RENDER_TYPE_COMBINATIONS = 1 << CHUNK_RENDER_TYPES.length;

    private static final int MASK_ALL = POSSIBLE_RENDER_TYPE_COMBINATIONS - 1;

    private static final ChunkRenderTypeSet[] UNIVERSE = Util.make(new ChunkRenderTypeSet[POSSIBLE_RENDER_TYPE_COMBINATIONS], array -> {
        if (CHUNK_RENDER_TYPES.length > 8) {
            throw new AssertionError("This code is written assuming a small universe of chunk render types");
        } else {
            array[0] = ChunkRenderTypeSet.none();
            for (int i = 1; i < array.length - 1; i++) {
                array[i] = embeddium$construct(BitSet.valueOf(new long[] { (long) i }));
            }
            array[MASK_ALL] = ChunkRenderTypeSet.all();
        }
    });

    @Invoker("<init>")
    static ChunkRenderTypeSet embeddium$construct(BitSet bitSet) {
        throw new AssertionError();
    }

    @Inject(method = { "<init>" }, at = { @At("RETURN") })
    private void onConstruct(BitSet bits, CallbackInfo ci) {
        int mask = bits.length() > 0 ? bits.toByteArray()[0] : 0;
        this.mask = mask;
        Builder<RenderType> builder = ImmutableList.builder();
        while (mask != 0) {
            int nextId = Integer.numberOfTrailingZeros(mask);
            mask &= ~(1 << nextId);
            builder.add(CHUNK_RENDER_TYPES[nextId]);
        }
        this.embeddium$containedTypes = builder.build();
    }

    @Overwrite
    public Iterator<RenderType> iterator() {
        return this.embeddium$containedTypes.iterator();
    }

    @Overwrite
    public boolean isEmpty() {
        return this.mask == 0;
    }

    @Overwrite
    public boolean contains(RenderType renderType) {
        int id = renderType.getChunkLayerId();
        return id >= 0 && (this.mask & 1 << id) != 0;
    }

    @Overwrite
    public List<RenderType> asList() {
        return this.embeddium$containedTypes;
    }

    @Overwrite
    public static ChunkRenderTypeSet of(RenderType... types) {
        int mask = 0;
        for (RenderType renderType : types) {
            int index = renderType.getChunkLayerId();
            if (index < 0) {
                throw new IllegalArgumentException("Attempted to create chunk render type set with a non-chunk render type: " + renderType);
            }
            mask |= 1 << index;
        }
        return UNIVERSE[mask];
    }

    @Overwrite
    private static ChunkRenderTypeSet of(Iterable<RenderType> types) {
        int mask = 0;
        for (RenderType renderType : types) {
            int index = renderType.getChunkLayerId();
            if (index < 0) {
                throw new IllegalArgumentException("Attempted to create chunk render type set with a non-chunk render type: " + renderType);
            }
            mask |= 1 << index;
        }
        return UNIVERSE[mask];
    }

    @Overwrite
    public static ChunkRenderTypeSet union(ChunkRenderTypeSet... sets) {
        int mask = 0;
        for (ChunkRenderTypeSet set : sets) {
            mask |= ((ChunkRenderTypeSetMixin) set).mask;
        }
        return UNIVERSE[mask];
    }

    @Overwrite
    public static ChunkRenderTypeSet union(Iterable<ChunkRenderTypeSet> sets) {
        int mask = 0;
        for (ChunkRenderTypeSet set : sets) {
            mask |= ((ChunkRenderTypeSetMixin) set).mask;
        }
        return UNIVERSE[mask];
    }

    @Overwrite
    public static ChunkRenderTypeSet intersection(ChunkRenderTypeSet... sets) {
        int mask = MASK_ALL;
        for (ChunkRenderTypeSet set : sets) {
            mask &= ((ChunkRenderTypeSetMixin) set).mask;
        }
        return UNIVERSE[mask];
    }

    @Overwrite
    public static ChunkRenderTypeSet intersection(Iterable<ChunkRenderTypeSet> sets) {
        int mask = MASK_ALL;
        for (ChunkRenderTypeSet set : sets) {
            mask &= ((ChunkRenderTypeSetMixin) set).mask;
        }
        return UNIVERSE[mask];
    }
}
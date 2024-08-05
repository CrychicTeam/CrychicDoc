package me.jellysquid.mods.sodium.mixin.features.model;

import it.unimi.dsi.fastutil.objects.Reference2ReferenceOpenHashMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.StampedLock;
import java.util.function.Predicate;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.MultiPartBakedModel;
import net.minecraft.client.resources.model.SimpleBakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.ChunkRenderTypeSet;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.client.model.data.MultipartModelData;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ MultiPartBakedModel.class })
public class MultipartBakedModelMixin {

    @Unique
    private final Map<BlockState, BakedModel[]> stateCacheFast = new Reference2ReferenceOpenHashMap();

    @Unique
    private final StampedLock lock = new StampedLock();

    @Shadow
    @Final
    private List<Pair<Predicate<BlockState>, BakedModel>> selectors;

    @Unique
    private boolean embeddium$hasCustomRenderTypes;

    @Inject(method = { "<init>" }, at = { @At("RETURN") })
    private void checkSubModelRenderTypes(CallbackInfo ci) {
        boolean hasRenderTypes = false;
        for (Pair<Predicate<BlockState>, BakedModel> pair : this.selectors) {
            BakedModel model = (BakedModel) pair.getRight();
            if (model.getClass() == SimpleBakedModel.class) {
                hasRenderTypes = ((SimpleBakedModelAccessor) model).getBlockRenderTypes() != null;
            } else {
                hasRenderTypes = true;
            }
            if (hasRenderTypes) {
                break;
            }
        }
        this.embeddium$hasCustomRenderTypes = hasRenderTypes;
    }

    @Unique
    private BakedModel[] getModelComponents(BlockState state) {
        long readStamp = this.lock.readLock();
        BakedModel[] models;
        try {
            models = (BakedModel[]) this.stateCacheFast.get(state);
        } finally {
            this.lock.unlockRead(readStamp);
        }
        if (models == null) {
            long writeStamp = this.lock.writeLock();
            try {
                List<BakedModel> modelList = new ArrayList(this.selectors.size());
                for (Pair<Predicate<BlockState>, BakedModel> pair : this.selectors) {
                    if (((Predicate) pair.getLeft()).test(state)) {
                        modelList.add((BakedModel) pair.getRight());
                    }
                }
                models = (BakedModel[]) modelList.toArray(BakedModel[]::new);
                this.stateCacheFast.put(state, models);
            } finally {
                this.lock.unlockWrite(writeStamp);
            }
        }
        return models;
    }

    @Overwrite(remap = false)
    public List<BakedQuad> getQuads(BlockState state, Direction face, RandomSource random, ModelData modelData, RenderType renderLayer) {
        if (state == null) {
            return Collections.emptyList();
        } else {
            BakedModel[] models = this.getModelComponents(state);
            List<BakedQuad> quads = null;
            long seed = random.nextLong();
            boolean checkSubmodelTypes = this.embeddium$hasCustomRenderTypes;
            for (BakedModel model : models) {
                random.setSeed(seed);
                if (!checkSubmodelTypes || renderLayer == null || model.getRenderTypes(state, random, modelData).contains(renderLayer)) {
                    List<BakedQuad> submodelQuads = model.getQuads(state, face, random, MultipartModelData.resolve(modelData, model), renderLayer);
                    if (models.length == 1) {
                        return submodelQuads;
                    }
                    if (quads == null) {
                        quads = new ArrayList();
                    }
                    quads.addAll(submodelQuads);
                }
            }
            return quads != null ? quads : Collections.emptyList();
        }
    }

    @Overwrite(remap = false)
    public ChunkRenderTypeSet getRenderTypes(@NotNull BlockState state, @NotNull RandomSource random, @NotNull ModelData data) {
        long seed = random.nextLong();
        if (!this.embeddium$hasCustomRenderTypes) {
            return ItemBlockRenderTypes.getRenderLayers(state);
        } else {
            BakedModel[] models = this.getModelComponents(state);
            if (models.length == 0) {
                return ChunkRenderTypeSet.none();
            } else {
                ChunkRenderTypeSet[] sets = new ChunkRenderTypeSet[models.length];
                for (int i = 0; i < models.length; i++) {
                    random.setSeed(seed);
                    sets[i] = models[i].getRenderTypes(state, random, data);
                }
                return ChunkRenderTypeSet.union(sets);
            }
        }
    }
}
package com.simibubi.create.content.kinetics.waterwheel;

import com.jozufozu.flywheel.core.StitchedSprite;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.CreateClient;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.foundation.model.BakedModelHelper;
import com.simibubi.create.foundation.render.BakedModelRenderHelper;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import com.simibubi.create.foundation.render.SuperByteBufferCache;
import com.simibubi.create.foundation.utility.RegisteredObjects;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceOpenHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.registries.ForgeRegistries;

public class WaterWheelRenderer<T extends WaterWheelBlockEntity> extends KineticBlockEntityRenderer<T> {

    public static final SuperByteBufferCache.Compartment<WaterWheelModelKey> WATER_WHEEL = new SuperByteBufferCache.Compartment<>();

    public static final StitchedSprite OAK_PLANKS_TEMPLATE = new StitchedSprite(new ResourceLocation("block/oak_planks"));

    public static final StitchedSprite OAK_LOG_TEMPLATE = new StitchedSprite(new ResourceLocation("block/oak_log"));

    public static final StitchedSprite OAK_LOG_TOP_TEMPLATE = new StitchedSprite(new ResourceLocation("block/oak_log_top"));

    private static final String[] LOG_SUFFIXES = new String[] { "_log", "_stem", "_block" };

    protected final boolean large;

    public WaterWheelRenderer(BlockEntityRendererProvider.Context context, boolean large) {
        super(context);
        this.large = large;
    }

    public static <T extends WaterWheelBlockEntity> WaterWheelRenderer<T> standard(BlockEntityRendererProvider.Context context) {
        return new WaterWheelRenderer<>(context, false);
    }

    public static <T extends WaterWheelBlockEntity> WaterWheelRenderer<T> large(BlockEntityRendererProvider.Context context) {
        return new WaterWheelRenderer<>(context, true);
    }

    protected SuperByteBuffer getRotatedModel(T be, BlockState state) {
        WaterWheelModelKey key = new WaterWheelModelKey(this.large, state, be.material);
        return CreateClient.BUFFER_CACHE.get(WATER_WHEEL, key, () -> {
            BakedModel model = generateModel(key);
            BlockState state1 = key.state();
            Direction dir;
            if (key.large()) {
                dir = Direction.fromAxisAndDirection((Direction.Axis) state1.m_61143_(LargeWaterWheelBlock.AXIS), Direction.AxisDirection.POSITIVE);
            } else {
                dir = (Direction) state1.m_61143_(WaterWheelBlock.FACING);
            }
            PoseStack transform = (PoseStack) CachedBufferer.rotateToFaceVertical(dir).get();
            return BakedModelRenderHelper.standardModelRender(model, Blocks.AIR.defaultBlockState(), transform);
        });
    }

    public static BakedModel generateModel(WaterWheelModelKey key) {
        BakedModel template;
        if (key.large()) {
            boolean extension = (Boolean) key.state().m_61143_(LargeWaterWheelBlock.EXTENSION);
            if (extension) {
                template = AllPartialModels.LARGE_WATER_WHEEL_EXTENSION.get();
            } else {
                template = AllPartialModels.LARGE_WATER_WHEEL.get();
            }
        } else {
            template = AllPartialModels.WATER_WHEEL.get();
        }
        return generateModel(template, key.material());
    }

    public static BakedModel generateModel(BakedModel template, BlockState planksBlockState) {
        Block planksBlock = planksBlockState.m_60734_();
        ResourceLocation id = RegisteredObjects.getKeyOrThrow(planksBlock);
        String path = id.getPath();
        if (path.endsWith("_planks")) {
            String namespace = id.getNamespace();
            String wood = path.substring(0, path.length() - 7);
            BlockState logBlockState = getLogBlockState(namespace, wood);
            Map<TextureAtlasSprite, TextureAtlasSprite> map = new Reference2ReferenceOpenHashMap();
            map.put(OAK_PLANKS_TEMPLATE.get(), getSpriteOnSide(planksBlockState, Direction.UP));
            map.put(OAK_LOG_TEMPLATE.get(), getSpriteOnSide(logBlockState, Direction.SOUTH));
            map.put(OAK_LOG_TOP_TEMPLATE.get(), getSpriteOnSide(logBlockState, Direction.UP));
            return BakedModelHelper.generateModel(template, map::get);
        } else {
            return BakedModelHelper.generateModel(template, sprite -> null);
        }
    }

    private static BlockState getLogBlockState(String namespace, String wood) {
        for (String suffix : LOG_SUFFIXES) {
            Optional<BlockState> state = ForgeRegistries.BLOCKS.getHolder(new ResourceLocation(namespace, wood + suffix)).map(Holder::m_203334_).map(Block::m_49966_);
            if (state.isPresent()) {
                return (BlockState) state.get();
            }
        }
        return Blocks.OAK_LOG.defaultBlockState();
    }

    private static TextureAtlasSprite getSpriteOnSide(BlockState state, Direction side) {
        BakedModel model = Minecraft.getInstance().getBlockRenderer().getBlockModel(state);
        if (model == null) {
            return null;
        } else {
            RandomSource random = RandomSource.create();
            random.setSeed(42L);
            List<BakedQuad> quads = model.getQuads(state, side, random, ModelData.EMPTY, null);
            if (!quads.isEmpty()) {
                return ((BakedQuad) quads.get(0)).getSprite();
            } else {
                random.setSeed(42L);
                quads = model.getQuads(state, null, random, ModelData.EMPTY, null);
                if (!quads.isEmpty()) {
                    for (BakedQuad quad : quads) {
                        if (quad.getDirection() == side) {
                            return quad.getSprite();
                        }
                    }
                }
                return model.getParticleIcon(ModelData.EMPTY);
            }
        }
    }
}
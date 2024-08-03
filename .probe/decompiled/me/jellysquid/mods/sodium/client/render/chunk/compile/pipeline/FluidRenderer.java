package me.jellysquid.mods.sodium.client.render.chunk.compile.pipeline;

import java.util.Objects;
import me.jellysquid.mods.sodium.client.compat.ccl.SinkingVertexBuilder;
import me.jellysquid.mods.sodium.client.model.color.ColorProvider;
import me.jellysquid.mods.sodium.client.model.color.ColorProviderRegistry;
import me.jellysquid.mods.sodium.client.model.color.DefaultColorProviders;
import me.jellysquid.mods.sodium.client.model.light.LightMode;
import me.jellysquid.mods.sodium.client.model.light.LightPipeline;
import me.jellysquid.mods.sodium.client.model.light.LightPipelineProvider;
import me.jellysquid.mods.sodium.client.model.light.data.QuadLightData;
import me.jellysquid.mods.sodium.client.model.quad.ModelQuad;
import me.jellysquid.mods.sodium.client.model.quad.ModelQuadView;
import me.jellysquid.mods.sodium.client.model.quad.ModelQuadViewMutable;
import me.jellysquid.mods.sodium.client.model.quad.properties.ModelQuadFacing;
import me.jellysquid.mods.sodium.client.render.chunk.compile.ChunkBuildBuffers;
import me.jellysquid.mods.sodium.client.render.chunk.compile.ChunkBuildContext;
import me.jellysquid.mods.sodium.client.render.chunk.compile.buffers.ChunkModelBuilder;
import me.jellysquid.mods.sodium.client.render.chunk.terrain.material.DefaultMaterials;
import me.jellysquid.mods.sodium.client.render.chunk.terrain.material.Material;
import me.jellysquid.mods.sodium.client.render.chunk.vertex.builder.ChunkMeshBufferBuilder;
import me.jellysquid.mods.sodium.client.render.chunk.vertex.format.ChunkVertexEncoder;
import me.jellysquid.mods.sodium.client.util.DirectionUtil;
import me.jellysquid.mods.sodium.client.world.WorldSlice;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SupportType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.apache.commons.lang3.mutable.MutableFloat;
import org.apache.commons.lang3.mutable.MutableInt;
import org.embeddedt.embeddium.impl.render.chunk.compile.GlobalChunkBuildContext;
import org.embeddedt.embeddium.render.chunk.ChunkColorWriter;
import org.embeddedt.embeddium.render.fluid.EmbeddiumFluidSpriteCache;
import org.embeddedt.embeddium.tags.EmbeddiumTags;

public class FluidRenderer {

    private static final float EPSILON = 0.001F;

    private final BlockPos.MutableBlockPos scratchPos = new BlockPos.MutableBlockPos();

    private final MutableFloat scratchHeight = new MutableFloat(0.0F);

    private final MutableInt scratchSamples = new MutableInt();

    private final ModelQuadViewMutable quad = new ModelQuad();

    private final LightPipelineProvider lighters;

    private final QuadLightData quadLightData = new QuadLightData();

    private final int[] quadColors = new int[4];

    private final ChunkVertexEncoder.Vertex[] vertices = ChunkVertexEncoder.Vertex.uninitializedQuad();

    private final ColorProviderRegistry colorProviderRegistry;

    private final EmbeddiumFluidSpriteCache fluidSpriteCache = new EmbeddiumFluidSpriteCache();

    private final SinkingVertexBuilder fluidVertexBuilder = new SinkingVertexBuilder();

    private final ChunkColorWriter colorEncoder = ChunkColorWriter.get();

    public FluidRenderer(ColorProviderRegistry colorProviderRegistry, LightPipelineProvider lighters) {
        this.quad.setLightFace(Direction.UP);
        this.lighters = lighters;
        this.colorProviderRegistry = colorProviderRegistry;
    }

    private boolean isFluidOccluded(BlockAndTintGetter world, int x, int y, int z, Direction dir, Fluid fluid) {
        if (world.m_6425_(this.scratchPos.set(x + dir.getStepX(), y + dir.getStepY(), z + dir.getStepZ())).getType().isSame(fluid)) {
            return true;
        } else {
            BlockPos pos = this.scratchPos.set(x, y, z);
            BlockState blockState = world.m_8055_(pos);
            if (blockState.m_60815_() && blockState.m_60659_(world, pos, dir, SupportType.FULL)) {
                VoxelShape sideShape = blockState.m_60655_(world, pos, dir);
                if (sideShape == Shapes.block()) {
                    return true;
                } else {
                    return sideShape == Shapes.empty() ? false : Block.isShapeFullBlock(sideShape);
                }
            } else {
                return false;
            }
        }
    }

    private boolean isSideExposed(BlockAndTintGetter world, int x, int y, int z, Direction dir, float height) {
        BlockPos pos = this.scratchPos.set(x + dir.getStepX(), y + dir.getStepY(), z + dir.getStepZ());
        BlockState blockState = world.m_8055_(pos);
        if (blockState.m_60815_()) {
            VoxelShape shape = blockState.m_60768_(world, pos);
            if (shape == Shapes.block()) {
                return dir == Direction.UP;
            } else if (shape.isEmpty()) {
                return true;
            } else {
                VoxelShape threshold = Shapes.box(0.0, 0.0, 0.0, 1.0, (double) height, 1.0);
                return !Shapes.blockOccudes(threshold, shape, dir);
            }
        } else {
            return true;
        }
    }

    private void renderVanilla(WorldSlice world, FluidState fluidState, BlockPos blockPos, ChunkModelBuilder buffers, Material material) {
        ChunkBuildContext context = (ChunkBuildContext) Objects.requireNonNull(GlobalChunkBuildContext.get());
        context.setCaptureAdditionalSprites(true);
        this.fluidVertexBuilder.reset();
        Minecraft.getInstance().getBlockRenderer().renderLiquid(blockPos, world, this.fluidVertexBuilder, world.getBlockState(blockPos), fluidState);
        this.fluidVertexBuilder.flush(buffers, material, 0.0F, 0.0F, 0.0F);
        for (TextureAtlasSprite sprite : context.getAdditionalCapturedSprites()) {
            if (sprite != null) {
                buffers.addSprite(sprite);
            }
        }
        context.setCaptureAdditionalSprites(false);
    }

    public void render(WorldSlice world, FluidState fluidState, BlockPos blockPos, BlockPos offset, ChunkBuildBuffers buffers) {
        Material material = DefaultMaterials.forFluidState(fluidState);
        ChunkModelBuilder meshBuilder = buffers.get(material);
        if (fluidState.getType().is(EmbeddiumTags.RENDERS_WITH_VANILLA)) {
            this.renderVanilla(world, fluidState, blockPos, meshBuilder, material);
        } else {
            int posX = blockPos.m_123341_();
            int posY = blockPos.m_123342_();
            int posZ = blockPos.m_123343_();
            Fluid fluid = fluidState.getType();
            boolean sfUp = this.isFluidOccluded(world, posX, posY, posZ, Direction.UP, fluid);
            boolean sfDown = this.isFluidOccluded(world, posX, posY, posZ, Direction.DOWN, fluid) || !this.isSideExposed(world, posX, posY, posZ, Direction.DOWN, 0.8888889F);
            boolean sfNorth = this.isFluidOccluded(world, posX, posY, posZ, Direction.NORTH, fluid);
            boolean sfSouth = this.isFluidOccluded(world, posX, posY, posZ, Direction.SOUTH, fluid);
            boolean sfWest = this.isFluidOccluded(world, posX, posY, posZ, Direction.WEST, fluid);
            boolean sfEast = this.isFluidOccluded(world, posX, posY, posZ, Direction.EAST, fluid);
            if (!sfUp || !sfDown || !sfEast || !sfWest || !sfNorth || !sfSouth) {
                boolean isWater = fluidState.is(FluidTags.WATER);
                ColorProvider<FluidState> colorProvider = this.getColorProvider(fluid);
                TextureAtlasSprite[] sprites = this.fluidSpriteCache.getSprites(world, blockPos, fluidState);
                float fluidHeight = this.fluidHeight(world, fluid, blockPos, Direction.UP);
                float northWestHeight;
                float southWestHeight;
                float southEastHeight;
                float northEastHeight;
                if (fluidHeight >= 1.0F) {
                    northWestHeight = 1.0F;
                    southWestHeight = 1.0F;
                    southEastHeight = 1.0F;
                    northEastHeight = 1.0F;
                } else {
                    BlockPos.MutableBlockPos scratchPos = new BlockPos.MutableBlockPos();
                    float heightNorth = this.fluidHeight(world, fluid, scratchPos.setWithOffset(blockPos, Direction.NORTH), Direction.NORTH);
                    float heightSouth = this.fluidHeight(world, fluid, scratchPos.setWithOffset(blockPos, Direction.SOUTH), Direction.SOUTH);
                    float heightEast = this.fluidHeight(world, fluid, scratchPos.setWithOffset(blockPos, Direction.EAST), Direction.EAST);
                    float heightWest = this.fluidHeight(world, fluid, scratchPos.setWithOffset(blockPos, Direction.WEST), Direction.WEST);
                    northWestHeight = this.fluidCornerHeight(world, fluid, fluidHeight, heightNorth, heightWest, scratchPos.set(blockPos).move(Direction.NORTH).move(Direction.WEST));
                    southWestHeight = this.fluidCornerHeight(world, fluid, fluidHeight, heightSouth, heightWest, scratchPos.set(blockPos).move(Direction.SOUTH).move(Direction.WEST));
                    southEastHeight = this.fluidCornerHeight(world, fluid, fluidHeight, heightSouth, heightEast, scratchPos.set(blockPos).move(Direction.SOUTH).move(Direction.EAST));
                    northEastHeight = this.fluidCornerHeight(world, fluid, fluidHeight, heightNorth, heightEast, scratchPos.set(blockPos).move(Direction.NORTH).move(Direction.EAST));
                }
                float yOffset = sfDown ? 0.0F : 0.001F;
                ModelQuadViewMutable quad = this.quad;
                LightMode lightMode = isWater && Minecraft.useAmbientOcclusion() ? LightMode.SMOOTH : LightMode.FLAT;
                LightPipeline lighter = this.lighters.getLighter(lightMode);
                quad.setFlags(0);
                if (!sfUp && this.isSideExposed(world, posX, posY, posZ, Direction.UP, Math.min(Math.min(northWestHeight, southWestHeight), Math.min(southEastHeight, northEastHeight)))) {
                    northWestHeight -= 0.001F;
                    southWestHeight -= 0.001F;
                    southEastHeight -= 0.001F;
                    northEastHeight -= 0.001F;
                    Vec3 velocity = fluidState.getFlow(world, blockPos);
                    TextureAtlasSprite sprite;
                    ModelQuadFacing facing;
                    float u1;
                    float u2;
                    float u3;
                    float u4;
                    float v1;
                    float v2;
                    float v3;
                    float v4;
                    if (velocity.x == 0.0 && velocity.z == 0.0) {
                        sprite = sprites[0];
                        facing = ModelQuadFacing.POS_Y;
                        u1 = sprite.getU(0.0);
                        v1 = sprite.getV(0.0);
                        u2 = u1;
                        v2 = sprite.getV(16.0);
                        u3 = sprite.getU(16.0);
                        v3 = v2;
                        u4 = u3;
                        v4 = v1;
                    } else {
                        sprite = sprites[1];
                        facing = ModelQuadFacing.UNASSIGNED;
                        float dir = (float) Mth.atan2(velocity.z, velocity.x) - (float) (Math.PI / 2);
                        float sin = Mth.sin(dir) * 0.25F;
                        float cos = Mth.cos(dir) * 0.25F;
                        u1 = sprite.getU((double) (8.0F + (-cos - sin) * 16.0F));
                        v1 = sprite.getV((double) (8.0F + (-cos + sin) * 16.0F));
                        u2 = sprite.getU((double) (8.0F + (-cos + sin) * 16.0F));
                        v2 = sprite.getV((double) (8.0F + (cos + sin) * 16.0F));
                        u3 = sprite.getU((double) (8.0F + (cos + sin) * 16.0F));
                        v3 = sprite.getV((double) (8.0F + (cos - sin) * 16.0F));
                        u4 = sprite.getU((double) (8.0F + (cos - sin) * 16.0F));
                        v4 = sprite.getV((double) (8.0F + (-cos - sin) * 16.0F));
                    }
                    float uAvg = (u1 + u2 + u3 + u4) / 4.0F;
                    float vAvg = (v1 + v2 + v3 + v4) / 4.0F;
                    float s1 = (float) sprites[0].contents().width() / (sprites[0].getU1() - sprites[0].getU0());
                    float s2 = (float) sprites[0].contents().height() / (sprites[0].getV1() - sprites[0].getV0());
                    float s3 = 4.0F / Math.max(s2, s1);
                    u1 = Mth.lerp(s3, u1, uAvg);
                    u2 = Mth.lerp(s3, u2, uAvg);
                    u3 = Mth.lerp(s3, u3, uAvg);
                    u4 = Mth.lerp(s3, u4, uAvg);
                    v1 = Mth.lerp(s3, v1, vAvg);
                    v2 = Mth.lerp(s3, v2, vAvg);
                    v3 = Mth.lerp(s3, v3, vAvg);
                    v4 = Mth.lerp(s3, v4, vAvg);
                    quad.setSprite(sprite);
                    setVertex(quad, 0, 0.0F, northWestHeight, 0.0F, u1, v1);
                    setVertex(quad, 1, 0.0F, southWestHeight, 1.0F, u2, v2);
                    setVertex(quad, 2, 1.0F, southEastHeight, 1.0F, u3, v3);
                    setVertex(quad, 3, 1.0F, northEastHeight, 0.0F, u4, v4);
                    this.updateQuad(quad, world, blockPos, lighter, Direction.UP, 1.0F, colorProvider, fluidState);
                    this.writeQuad(meshBuilder, material, offset, quad, facing, false);
                    if (fluidState.shouldRenderBackwardUpFace(world, this.scratchPos.set(posX, posY + 1, posZ))) {
                        this.writeQuad(meshBuilder, material, offset, quad, ModelQuadFacing.NEG_Y, true);
                    }
                }
                if (!sfDown) {
                    TextureAtlasSprite spritex = sprites[0];
                    float minU = spritex.getU0();
                    float maxU = spritex.getU1();
                    float minV = spritex.getV0();
                    float maxV = spritex.getV1();
                    quad.setSprite(spritex);
                    setVertex(quad, 0, 0.0F, yOffset, 1.0F, minU, maxV);
                    setVertex(quad, 1, 0.0F, yOffset, 0.0F, minU, minV);
                    setVertex(quad, 2, 1.0F, yOffset, 0.0F, maxU, minV);
                    setVertex(quad, 3, 1.0F, yOffset, 1.0F, maxU, maxV);
                    this.updateQuad(quad, world, blockPos, lighter, Direction.DOWN, 1.0F, colorProvider, fluidState);
                    this.writeQuad(meshBuilder, material, offset, quad, ModelQuadFacing.NEG_Y, false);
                }
                quad.setFlags(6);
                for (Direction dir : DirectionUtil.HORIZONTAL_DIRECTIONS) {
                    float c1;
                    float c2;
                    float x1;
                    float z1;
                    float x2;
                    float z2;
                    switch(dir) {
                        case NORTH:
                            if (sfNorth) {
                                continue;
                            }
                            c1 = northWestHeight;
                            c2 = northEastHeight;
                            x1 = 0.0F;
                            x2 = 1.0F;
                            z1 = 0.001F;
                            z2 = z1;
                            break;
                        case SOUTH:
                            if (sfSouth) {
                                continue;
                            }
                            c1 = southEastHeight;
                            c2 = southWestHeight;
                            x1 = 1.0F;
                            x2 = 0.0F;
                            z1 = 0.999F;
                            z2 = z1;
                            break;
                        case WEST:
                            if (sfWest) {
                                continue;
                            }
                            c1 = southWestHeight;
                            c2 = northWestHeight;
                            x1 = 0.001F;
                            x2 = x1;
                            z1 = 1.0F;
                            z2 = 0.0F;
                            break;
                        case EAST:
                            if (!sfEast) {
                                c1 = northEastHeight;
                                c2 = southEastHeight;
                                x1 = 0.999F;
                                x2 = x1;
                                z1 = 0.0F;
                                z2 = 1.0F;
                                break;
                            }
                        default:
                            continue;
                    }
                    if (this.isSideExposed(world, posX, posY, posZ, dir, Math.max(c1, c2))) {
                        int adjX = posX + dir.getStepX();
                        int adjY = posY + dir.getStepY();
                        int adjZ = posZ + dir.getStepZ();
                        TextureAtlasSprite spritex = sprites[1];
                        boolean isOverlay = false;
                        if (sprites.length > 2) {
                            BlockPos adjPos = this.scratchPos.set(adjX, adjY, adjZ);
                            BlockState adjBlock = world.getBlockState(adjPos);
                            if (sprites[2] != null && adjBlock.shouldDisplayFluidOverlay(world, adjPos, fluidState)) {
                                spritex = sprites[2];
                                isOverlay = true;
                            }
                        }
                        float u1x = spritex.getU(0.0);
                        float u2x = spritex.getU(8.0);
                        float v1x = spritex.getV((double) ((1.0F - c1) * 16.0F * 0.5F));
                        float v2x = spritex.getV((double) ((1.0F - c2) * 16.0F * 0.5F));
                        float v3x = spritex.getV(8.0);
                        quad.setSprite(spritex);
                        setVertex(quad, 0, x2, c2, z2, u2x, v2x);
                        setVertex(quad, 1, x2, yOffset, z2, u2x, v3x);
                        setVertex(quad, 2, x1, yOffset, z1, u1x, v3x);
                        setVertex(quad, 3, x1, c1, z1, u1x, v1x);
                        float br = dir.getAxis() == Direction.Axis.Z ? 0.8F : 0.6F;
                        ModelQuadFacing facingx = ModelQuadFacing.fromDirection(dir);
                        this.updateQuad(quad, world, blockPos, lighter, dir, br, colorProvider, fluidState);
                        this.writeQuad(meshBuilder, material, offset, quad, facingx, false);
                        if (!isOverlay) {
                            this.writeQuad(meshBuilder, material, offset, quad, facingx.getOpposite(), true);
                        }
                    }
                }
            }
        }
    }

    private ColorProvider<FluidState> getColorProvider(Fluid fluid) {
        ColorProvider<FluidState> override = this.colorProviderRegistry.getColorProvider(fluid);
        return override != null ? override : DefaultColorProviders.getFluidProvider();
    }

    private void updateQuad(ModelQuadView quad, WorldSlice world, BlockPos pos, LightPipeline lighter, Direction dir, float brightness, ColorProvider<FluidState> colorProvider, FluidState fluidState) {
        QuadLightData light = this.quadLightData;
        lighter.calculate(quad, pos, light, null, dir, false);
        colorProvider.getColors(world, pos, fluidState, quad, this.quadColors);
        for (int i = 0; i < 4; i++) {
            this.quadColors[i] = this.colorEncoder.writeColor(this.quadColors[i], light.br[i] * brightness);
        }
    }

    private void writeQuad(ChunkModelBuilder builder, Material material, BlockPos offset, ModelQuadView quad, ModelQuadFacing facing, boolean flip) {
        ChunkVertexEncoder.Vertex[] vertices = this.vertices;
        for (int i = 0; i < 4; i++) {
            ChunkVertexEncoder.Vertex out = vertices[flip ? 3 - i : i];
            out.x = (float) offset.m_123341_() + quad.getX(i);
            out.y = (float) offset.m_123342_() + quad.getY(i);
            out.z = (float) offset.m_123343_() + quad.getZ(i);
            out.color = this.quadColors[i];
            out.u = quad.getTexU(i);
            out.v = quad.getTexV(i);
            out.light = this.quadLightData.lm[i];
        }
        TextureAtlasSprite sprite = quad.getSprite();
        if (sprite != null) {
            builder.addSprite(sprite);
        }
        ChunkMeshBufferBuilder vertexBuffer = builder.getVertexBuffer(facing);
        vertexBuffer.push(vertices, material);
    }

    private static void setVertex(ModelQuadViewMutable quad, int i, float x, float y, float z, float u, float v) {
        quad.setX(i, x);
        quad.setY(i, y);
        quad.setZ(i, z);
        quad.setTexU(i, u);
        quad.setTexV(i, v);
    }

    private float fluidCornerHeight(BlockAndTintGetter world, Fluid fluid, float fluidHeight, float fluidHeightX, float fluidHeightY, BlockPos blockPos) {
        if (!(fluidHeightY >= 1.0F) && !(fluidHeightX >= 1.0F)) {
            if (fluidHeightY > 0.0F || fluidHeightX > 0.0F) {
                float height = this.fluidHeight(world, fluid, blockPos, Direction.UP);
                if (height >= 1.0F) {
                    return 1.0F;
                }
                this.modifyHeight(this.scratchHeight, this.scratchSamples, height);
            }
            this.modifyHeight(this.scratchHeight, this.scratchSamples, fluidHeight);
            this.modifyHeight(this.scratchHeight, this.scratchSamples, fluidHeightY);
            this.modifyHeight(this.scratchHeight, this.scratchSamples, fluidHeightX);
            float result = this.scratchHeight.floatValue() / (float) this.scratchSamples.intValue();
            this.scratchHeight.setValue(0.0F);
            this.scratchSamples.setValue(0);
            return result;
        } else {
            return 1.0F;
        }
    }

    private void modifyHeight(MutableFloat totalHeight, MutableInt samples, float target) {
        if (target >= 0.8F) {
            totalHeight.add(target * 10.0F);
            samples.add(10);
        } else if (target >= 0.0F) {
            totalHeight.add(target);
            samples.increment();
        }
    }

    private float fluidHeight(BlockAndTintGetter world, Fluid fluid, BlockPos blockPos, Direction direction) {
        BlockState blockState = world.m_8055_(blockPos);
        FluidState fluidState = blockState.m_60819_();
        if (fluid.isSame(fluidState.getType())) {
            FluidState fluidStateUp = world.m_6425_(blockPos.above());
            return fluid.isSame(fluidStateUp.getType()) ? 1.0F : fluidState.getOwnHeight();
        } else {
            return !blockState.m_280296_() ? 0.0F : -1.0F;
        }
    }
}
package com.simibubi.create.foundation.ponder;

import com.google.common.base.Suppliers;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.content.kinetics.belt.BeltBlock;
import com.simibubi.create.content.kinetics.belt.BeltBlockEntity;
import com.simibubi.create.content.schematics.SchematicWorld;
import com.simibubi.create.foundation.blockEntity.IMultiBlockEntityContainer;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.mixin.accessor.ParticleEngineAccessor;
import com.simibubi.create.foundation.ponder.element.WorldSectionElement;
import com.simibubi.create.foundation.render.SuperRenderTypeBuffer;
import com.simibubi.create.foundation.utility.RegisteredObjects;
import com.simibubi.create.foundation.utility.worldWrappers.WrappedClientWorld;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;

public class PonderWorld extends SchematicWorld {

    public PonderScene scene;

    protected Map<BlockPos, BlockState> originalBlocks;

    protected Map<BlockPos, CompoundTag> originalBlockEntities;

    protected Map<BlockPos, Integer> blockBreakingProgressions;

    protected List<Entity> originalEntities;

    private Supplier<ClientLevel> asClientWorld = Suppliers.memoize(() -> WrappedClientWorld.of(this));

    protected PonderWorldParticles particles;

    private final Map<ResourceLocation, ParticleProvider<?>> particleProviders;

    int overrideLight;

    Selection mask;

    boolean currentlyTickingEntities;

    public PonderWorld(BlockPos anchor, Level original) {
        super(anchor, original);
        this.originalBlocks = new HashMap();
        this.originalBlockEntities = new HashMap();
        this.blockBreakingProgressions = new HashMap();
        this.originalEntities = new ArrayList();
        this.particles = new PonderWorldParticles(this);
        this.particleProviders = ((ParticleEngineAccessor) Minecraft.getInstance().particleEngine).create$getProviders();
    }

    public void createBackup() {
        this.originalBlocks.clear();
        this.originalBlockEntities.clear();
        this.blocks.forEach((k, v) -> this.originalBlocks.put(k, v));
        this.blockEntities.forEach((k, v) -> this.originalBlockEntities.put(k, v.saveWithFullMetadata()));
        this.entities.forEach(e -> EntityType.create(e.serializeNBT(), this).ifPresent(this.originalEntities::add));
    }

    public void restore() {
        this.entities.clear();
        this.blocks.clear();
        this.blockEntities.clear();
        this.blockBreakingProgressions.clear();
        this.renderedBlockEntities.clear();
        this.originalBlocks.forEach((k, v) -> this.blocks.put(k, v));
        this.originalBlockEntities.forEach((k, v) -> {
            BlockEntity blockEntity = BlockEntity.loadStatic(k, (BlockState) this.originalBlocks.get(k), v);
            this.onBEadded(blockEntity, blockEntity.getBlockPos());
            this.blockEntities.put(k, blockEntity);
            this.renderedBlockEntities.add(blockEntity);
        });
        this.originalEntities.forEach(e -> EntityType.create(e.serializeNBT(), this).ifPresent(this.entities::add));
        this.particles.clearEffects();
        this.fixControllerBlockEntities();
    }

    public void restoreBlocks(Selection selection) {
        selection.forEach(p -> {
            if (this.originalBlocks.containsKey(p)) {
                this.blocks.put(p, (BlockState) this.originalBlocks.get(p));
            }
            if (this.originalBlockEntities.containsKey(p)) {
                BlockEntity blockEntity = BlockEntity.loadStatic(p, (BlockState) this.originalBlocks.get(p), (CompoundTag) this.originalBlockEntities.get(p));
                this.onBEadded(blockEntity, blockEntity.getBlockPos());
                this.blockEntities.put(p, blockEntity);
            }
        });
        this.redraw();
    }

    private void redraw() {
        if (this.scene != null) {
            this.scene.forEach(WorldSectionElement.class, WorldSectionElement::queueRedraw);
        }
    }

    public void pushFakeLight(int light) {
        this.overrideLight = light;
    }

    public void popLight() {
        this.overrideLight = -1;
    }

    @Override
    public int getBrightness(LightLayer p_226658_1_, BlockPos p_226658_2_) {
        return this.overrideLight == -1 ? 15 : this.overrideLight;
    }

    public void setMask(Selection mask) {
        this.mask = mask;
    }

    public void clearMask() {
        this.mask = null;
    }

    @Override
    public BlockState getBlockState(BlockPos globalPos) {
        if (this.mask != null && !this.mask.test(globalPos.subtract(this.anchor))) {
            return Blocks.AIR.defaultBlockState();
        } else {
            return this.currentlyTickingEntities && globalPos.m_123342_() < 0 ? Blocks.AIR.defaultBlockState() : super.getBlockState(globalPos);
        }
    }

    @Override
    public BlockGetter getChunkForCollisions(int p_225522_1_, int p_225522_2_) {
        return this;
    }

    public void renderEntities(PoseStack ms, SuperRenderTypeBuffer buffer, Camera ari, float pt) {
        Vec3 Vector3d = ari.getPosition();
        double d0 = Vector3d.x();
        double d1 = Vector3d.y();
        double d2 = Vector3d.z();
        for (Entity entity : this.entities) {
            if (entity.tickCount == 0) {
                entity.xOld = entity.getX();
                entity.yOld = entity.getY();
                entity.zOld = entity.getZ();
            }
            this.renderEntity(entity, d0, d1, d2, pt, ms, buffer);
        }
        buffer.draw(RenderType.entitySolid(InventoryMenu.BLOCK_ATLAS));
        buffer.draw(RenderType.entityCutout(InventoryMenu.BLOCK_ATLAS));
        buffer.draw(RenderType.entityCutoutNoCull(InventoryMenu.BLOCK_ATLAS));
        buffer.draw(RenderType.entitySmoothCutout(InventoryMenu.BLOCK_ATLAS));
    }

    private void renderEntity(Entity entity, double x, double y, double z, float pt, PoseStack ms, MultiBufferSource buffer) {
        double d0 = Mth.lerp((double) pt, entity.xOld, entity.getX());
        double d1 = Mth.lerp((double) pt, entity.yOld, entity.getY());
        double d2 = Mth.lerp((double) pt, entity.zOld, entity.getZ());
        float f = Mth.lerp(pt, entity.yRotO, entity.getYRot());
        EntityRenderDispatcher renderManager = Minecraft.getInstance().getEntityRenderDispatcher();
        int light = renderManager.getRenderer(entity).getPackedLightCoords(entity, pt);
        renderManager.render(entity, d0 - x, d1 - y, d2 - z, f, pt, ms, buffer, light);
    }

    public void renderParticles(PoseStack ms, MultiBufferSource buffer, Camera ari, float pt) {
        this.particles.renderParticles(ms, buffer, ari, pt);
    }

    public void tick() {
        this.currentlyTickingEntities = true;
        this.particles.tick();
        Iterator<Entity> iterator = this.entities.iterator();
        while (iterator.hasNext()) {
            Entity entity = (Entity) iterator.next();
            entity.tickCount++;
            entity.xOld = entity.getX();
            entity.yOld = entity.getY();
            entity.zOld = entity.getZ();
            entity.tick();
            if (entity.getY() <= -0.5) {
                entity.discard();
            }
            if (!entity.isAlive()) {
                iterator.remove();
            }
        }
        this.currentlyTickingEntities = false;
    }

    @Override
    public void addParticle(ParticleOptions data, double x, double y, double z, double mx, double my, double mz) {
        this.addParticle(this.makeParticle(data, x, y, z, mx, my, mz));
    }

    @Override
    public void addAlwaysVisibleParticle(ParticleOptions data, double x, double y, double z, double mx, double my, double mz) {
        this.addParticle(data, x, y, z, mx, my, mz);
    }

    @Nullable
    private <T extends ParticleOptions> Particle makeParticle(T data, double x, double y, double z, double mx, double my, double mz) {
        ResourceLocation key = RegisteredObjects.getKeyOrThrow(data.getType());
        ParticleProvider<T> particleProvider = (ParticleProvider<T>) this.particleProviders.get(key);
        return particleProvider == null ? null : particleProvider.createParticle(data, (ClientLevel) this.asClientWorld.get(), x, y, z, mx, my, mz);
    }

    @Override
    public boolean setBlock(BlockPos pos, BlockState arg1, int arg2) {
        return super.setBlock(pos, arg1, arg2);
    }

    public void addParticle(Particle p) {
        if (p != null) {
            this.particles.addParticle(p);
        }
    }

    @Override
    protected void onBEadded(BlockEntity blockEntity, BlockPos pos) {
        super.onBEadded(blockEntity, pos);
        if (blockEntity instanceof SmartBlockEntity smartBlockEntity) {
            smartBlockEntity.markVirtual();
        }
    }

    public void fixControllerBlockEntities() {
        Iterator var1 = this.blockEntities.values().iterator();
        while (true) {
            BlockEntity blockEntity;
            label41: while (true) {
                if (!var1.hasNext()) {
                    return;
                }
                blockEntity = (BlockEntity) var1.next();
                if (!(blockEntity instanceof BeltBlockEntity beltBlockEntity)) {
                    break;
                }
                if (beltBlockEntity.isController()) {
                    BlockPos controllerPos = blockEntity.getBlockPos();
                    Iterator current = BeltBlock.getBeltChain(this, controllerPos).iterator();
                    while (true) {
                        if (!current.hasNext()) {
                            break label41;
                        }
                        BlockPos blockPos = (BlockPos) current.next();
                        if (this.m_7702_(blockPos) instanceof BeltBlockEntity belt2) {
                            belt2.setController(controllerPos);
                        }
                    }
                }
            }
            if (blockEntity instanceof IMultiBlockEntityContainer multiBlockEntity) {
                BlockPos lastKnown = multiBlockEntity.getLastKnownPos();
                BlockPos current = blockEntity.getBlockPos();
                if (lastKnown != null && current != null && !multiBlockEntity.isController() && !lastKnown.equals(current)) {
                    BlockPos newControllerPos = multiBlockEntity.getController().offset(current.subtract(lastKnown));
                    multiBlockEntity.setController(newControllerPos);
                }
            }
        }
    }

    public void setBlockBreakingProgress(BlockPos pos, int damage) {
        if (damage == 0) {
            this.blockBreakingProgressions.remove(pos);
        } else {
            this.blockBreakingProgressions.put(pos, damage - 1);
        }
    }

    public Map<BlockPos, Integer> getBlockBreakingProgressions() {
        return this.blockBreakingProgressions;
    }

    public void addBlockDestroyEffects(BlockPos pos, BlockState state) {
        VoxelShape voxelshape = state.m_60808_(this, pos);
        if (!voxelshape.isEmpty()) {
            AABB bb = voxelshape.bounds();
            double d1 = Math.min(1.0, bb.maxX - bb.minX);
            double d2 = Math.min(1.0, bb.maxY - bb.minY);
            double d3 = Math.min(1.0, bb.maxZ - bb.minZ);
            int i = Math.max(2, Mth.ceil(d1 / 0.25));
            int j = Math.max(2, Mth.ceil(d2 / 0.25));
            int k = Math.max(2, Mth.ceil(d3 / 0.25));
            for (int l = 0; l < i; l++) {
                for (int i1 = 0; i1 < j; i1++) {
                    for (int j1 = 0; j1 < k; j1++) {
                        double d4 = ((double) l + 0.5) / (double) i;
                        double d5 = ((double) i1 + 0.5) / (double) j;
                        double d6 = ((double) j1 + 0.5) / (double) k;
                        double d7 = d4 * d1 + bb.minX;
                        double d8 = d5 * d2 + bb.minY;
                        double d9 = d6 * d3 + bb.minZ;
                        this.addParticle(new BlockParticleOption(ParticleTypes.BLOCK, state), (double) pos.m_123341_() + d7, (double) pos.m_123342_() + d8, (double) pos.m_123343_() + d9, d4 - 0.5, d5 - 0.5, d6 - 0.5);
                    }
                }
            }
        }
    }

    @Override
    protected BlockState processBlockStateForPrinting(BlockState state) {
        return state;
    }

    @Override
    public boolean hasChunkAt(BlockPos pos) {
        return true;
    }

    @Override
    public boolean hasChunk(int x, int y) {
        return true;
    }

    @Override
    public boolean isLoaded(BlockPos pos) {
        return true;
    }

    @Override
    public boolean hasNearbyAlivePlayer(double p_217358_1_, double p_217358_3_, double p_217358_5_, double p_217358_7_) {
        return true;
    }
}
package com.simibubi.create.content.schematics;

import com.simibubi.create.Create;
import com.simibubi.create.foundation.utility.BBHelper;
import com.simibubi.create.foundation.utility.NBTProcessors;
import com.simibubi.create.foundation.utility.worldWrappers.WrappedWorld;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Stream;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.ticks.BlackholeTickAccess;
import net.minecraft.world.ticks.LevelTickAccess;

public class SchematicWorld extends WrappedWorld implements ServerLevelAccessor {

    protected Map<BlockPos, BlockState> blocks;

    protected Map<BlockPos, BlockEntity> blockEntities;

    protected List<BlockEntity> renderedBlockEntities;

    protected List<Entity> entities;

    protected BoundingBox bounds;

    public BlockPos anchor;

    public boolean renderMode;

    public SchematicWorld(Level original) {
        this(BlockPos.ZERO, original);
    }

    public SchematicWorld(BlockPos anchor, Level original) {
        super(original);
        this.setChunkSource(new SchematicChunkSource(this));
        this.blocks = new HashMap();
        this.blockEntities = new HashMap();
        this.bounds = new BoundingBox(BlockPos.ZERO);
        this.anchor = anchor;
        this.entities = new ArrayList();
        this.renderedBlockEntities = new ArrayList();
    }

    public Set<BlockPos> getAllPositions() {
        return this.blocks.keySet();
    }

    @Override
    public boolean addFreshEntity(Entity entityIn) {
        if (entityIn instanceof ItemFrame itemFrame) {
            itemFrame.setItem(NBTProcessors.withUnsafeNBTDiscarded(itemFrame.getItem()));
        }
        if (entityIn instanceof ArmorStand armorStand) {
            for (EquipmentSlot equipmentSlot : EquipmentSlot.values()) {
                armorStand.setItemSlot(equipmentSlot, NBTProcessors.withUnsafeNBTDiscarded(armorStand.getItemBySlot(equipmentSlot)));
            }
        }
        return this.entities.add(entityIn);
    }

    public Stream<Entity> getEntityStream() {
        return this.entities.stream();
    }

    @Override
    public BlockEntity getBlockEntity(BlockPos pos) {
        if (this.m_151570_(pos)) {
            return null;
        } else if (this.blockEntities.containsKey(pos)) {
            return (BlockEntity) this.blockEntities.get(pos);
        } else if (!this.blocks.containsKey(pos.subtract(this.anchor))) {
            return null;
        } else {
            BlockState blockState = this.getBlockState(pos);
            if (blockState.m_155947_()) {
                try {
                    BlockEntity blockEntity = ((EntityBlock) blockState.m_60734_()).newBlockEntity(pos, blockState);
                    if (blockEntity != null) {
                        this.onBEadded(blockEntity, pos);
                        this.blockEntities.put(pos, blockEntity);
                        this.renderedBlockEntities.add(blockEntity);
                    }
                    return blockEntity;
                } catch (Exception var4) {
                    Create.LOGGER.debug("Could not create BlockEntity of block " + blockState, var4);
                }
            }
            return null;
        }
    }

    protected void onBEadded(BlockEntity blockEntity, BlockPos pos) {
        blockEntity.setLevel(this);
    }

    @Override
    public BlockState getBlockState(BlockPos globalPos) {
        BlockPos pos = globalPos.subtract(this.anchor);
        if (pos.m_123342_() - this.bounds.minY() == -1 && !this.renderMode) {
            return Blocks.DIRT.defaultBlockState();
        } else {
            return this.getBounds().isInside(pos) && this.blocks.containsKey(pos) ? this.processBlockStateForPrinting((BlockState) this.blocks.get(pos)) : Blocks.AIR.defaultBlockState();
        }
    }

    public Map<BlockPos, BlockState> getBlockMap() {
        return this.blocks;
    }

    @Override
    public FluidState getFluidState(BlockPos pos) {
        return this.getBlockState(pos).m_60819_();
    }

    @Override
    public Holder<Biome> getBiome(BlockPos pos) {
        return this.world.registryAccess().m_255025_(Registries.BIOME).m_255043_(Biomes.PLAINS);
    }

    @Override
    public int getBrightness(LightLayer lightLayer, BlockPos pos) {
        return 15;
    }

    @Override
    public float getShade(Direction face, boolean hasShade) {
        return 1.0F;
    }

    @Override
    public LevelTickAccess<Block> getBlockTicks() {
        return BlackholeTickAccess.emptyLevelList();
    }

    @Override
    public LevelTickAccess<Fluid> getFluidTicks() {
        return BlackholeTickAccess.emptyLevelList();
    }

    @Override
    public List<Entity> getEntities(Entity arg0, AABB arg1, Predicate<? super Entity> arg2) {
        return Collections.emptyList();
    }

    @Override
    public <T extends Entity> List<T> getEntitiesOfClass(Class<T> arg0, AABB arg1, Predicate<? super T> arg2) {
        return Collections.emptyList();
    }

    @Override
    public List<? extends Player> players() {
        return Collections.emptyList();
    }

    @Override
    public int getSkyDarken() {
        return 0;
    }

    @Override
    public boolean isStateAtPosition(BlockPos pos, Predicate<BlockState> predicate) {
        return predicate.test(this.getBlockState(pos));
    }

    @Override
    public boolean destroyBlock(BlockPos arg0, boolean arg1) {
        return this.setBlock(arg0, Blocks.AIR.defaultBlockState(), 3);
    }

    @Override
    public boolean removeBlock(BlockPos arg0, boolean arg1) {
        return this.setBlock(arg0, Blocks.AIR.defaultBlockState(), 3);
    }

    @Override
    public boolean setBlock(BlockPos pos, BlockState arg1, int arg2) {
        pos = pos.immutable().subtract(this.anchor);
        this.bounds = BBHelper.encapsulate(this.bounds, pos);
        this.blocks.put(pos, arg1);
        if (this.blockEntities.containsKey(pos)) {
            BlockEntity blockEntity = (BlockEntity) this.blockEntities.get(pos);
            if (!blockEntity.getType().isValid(arg1)) {
                this.blockEntities.remove(pos);
                this.renderedBlockEntities.remove(blockEntity);
            }
        }
        BlockEntity blockEntity = this.getBlockEntity(pos);
        if (blockEntity != null) {
            this.blockEntities.put(pos, blockEntity);
        }
        return true;
    }

    @Override
    public void sendBlockUpdated(BlockPos pos, BlockState oldState, BlockState newState, int flags) {
    }

    public BoundingBox getBounds() {
        return this.bounds;
    }

    public Iterable<BlockEntity> getBlockEntities() {
        return this.blockEntities.values();
    }

    public Iterable<BlockEntity> getRenderedBlockEntities() {
        return this.renderedBlockEntities;
    }

    protected BlockState processBlockStateForPrinting(BlockState state) {
        if (state.m_60734_() instanceof AbstractFurnaceBlock && state.m_61138_(BlockStateProperties.LIT)) {
            state = (BlockState) state.m_61124_(BlockStateProperties.LIT, false);
        }
        return state;
    }

    @Override
    public ServerLevel getLevel() {
        if (this.world instanceof ServerLevel) {
            return (ServerLevel) this.world;
        } else {
            throw new IllegalStateException("Cannot use IServerWorld#getWorld in a client environment");
        }
    }
}
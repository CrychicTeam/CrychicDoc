package com.mna.blocks.worldgen;

import com.mna.api.blocks.IManaweaveNotifiable;
import com.mna.api.blocks.interfaces.IDontCreateBlockItem;
import com.mna.api.recipes.IManaweavePattern;
import com.mna.blocks.BlockInit;
import com.mna.blocks.tileentities.ManaweaveCacheTile;
import com.mna.blocks.tileentities.init.TileEntityInit;
import com.mna.blocks.utility.WaterloggableBlockWithOffset;
import com.mna.interop.lootr.LootrIntegration;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.fml.ModList;

public class ManaweaveCacheBlock extends WaterloggableBlockWithOffset implements EntityBlock, IManaweaveNotifiable, IDontCreateBlockItem {

    public ManaweaveCacheBlock() {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).noOcclusion().explosionResistance(200.0F).destroyTime(200.0F), false, new BlockPos(0, 1, 0), new BlockPos(0, 2, 0));
    }

    public int getLightEmission(BlockState state, BlockGetter world, BlockPos pos) {
        return 10;
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        BlockEntity tile = world.getBlockEntity(pos);
        if (tile != null && tile instanceof ManaweaveCacheTile cache && cache.isOpen() && !cache.isBuff()) {
            if (!world.isClientSide) {
                if (ModList.get().isLoaded("lootr")) {
                    LootrIntegration.openCache(cache, (ServerPlayer) player);
                } else {
                    cache.m_59640_(player);
                    player.openMenu(cache);
                }
            }
            return InteractionResult.sidedSuccess(world.isClientSide);
        }
        return InteractionResult.FAIL;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ManaweaveCacheTile(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return type == TileEntityInit.MANAWEAVE_CACHE.get() ? (lvl, pos, state1, be) -> ManaweaveCacheTile.Tick(lvl, pos, state1, (ManaweaveCacheTile) be) : null;
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState0) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public boolean notify(Level world, BlockPos pos, BlockState state, List<IManaweavePattern> patterns, @Nullable LivingEntity caster) {
        if (!world.isClientSide && caster instanceof Player player) {
            ManaweaveCacheTile te = (ManaweaveCacheTile) world.getBlockEntity(pos);
            return te.notifyPattern(player, ((IManaweavePattern) patterns.get(0)).getRegistryId());
        } else {
            return false;
        }
    }

    @Override
    public void onRemove(BlockState oldState, Level world, BlockPos pos, BlockState newState, boolean drops) {
        if (!oldState.m_60713_(newState.m_60734_())) {
            BlockEntity blockentity = world.getBlockEntity(pos);
            if (blockentity instanceof ManaweaveCacheTile && ((ManaweaveCacheTile) blockentity).isOpen() && !((ManaweaveCacheTile) blockentity).isBuff()) {
                Containers.dropContents(world, pos, (ManaweaveCacheTile) blockentity);
                world.updateNeighbourForOutputSignal(pos, this);
            }
            super.onRemove(oldState, world, pos, newState, drops);
        }
    }

    @Override
    protected void spawnDestroyParticles(Level world, Player player, BlockPos pos, BlockState state) {
        for (int i = 0; i < 20; i++) {
            world.addParticle(new BlockParticleOption(ParticleTypes.BLOCK, BlockInit.CHIMERITE_ARCANE_STONE_STRAIGHT.get().defaultBlockState()), (double) pos.m_123341_() + Math.random(), (double) pos.m_123342_() + Math.random(), (double) pos.m_123343_() + Math.random(), 0.0, 0.05 * Math.random(), 0.0);
        }
    }
}
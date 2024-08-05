package com.mna.blocks.ritual;

import com.mna.api.blocks.interfaces.IDontCreateBlockItem;
import com.mna.api.capabilities.IWorldMagic;
import com.mna.api.particles.MAParticleType;
import com.mna.api.particles.ParticleInit;
import com.mna.capabilities.worlddata.WorldMagicProvider;
import com.mna.entities.EntityInit;
import com.mna.entities.utility.DisplayReagents;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.util.LazyOptional;

public class RitualTeleportLocationBlock extends Block implements IDontCreateBlockItem {

    public RitualTeleportLocationBlock() {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).noLootTable().randomTicks().strength(3.0F, 10000.0F));
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        if (worldIn.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            LazyOptional<IWorldMagic> worldMagicContainer = worldIn.getCapability(WorldMagicProvider.MAGIC);
            if (worldMagicContainer.isPresent()) {
                IWorldMagic worldMagic = worldMagicContainer.orElse(null);
                List<ResourceLocation> reagents = worldMagic.getRitualTeleportBlockReagents(pos);
                Entity displayEntity = EntityInit.REAGENT_ENTITY.get().spawn((ServerLevel) worldIn, null, player, pos.above(), MobSpawnType.SPAWN_EGG, true, true);
                if (displayEntity != null && displayEntity instanceof DisplayReagents) {
                    ((DisplayReagents) displayEntity).setResourceLocations(reagents);
                }
            }
            return InteractionResult.SUCCESS;
        }
    }

    @Override
    public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.m_60734_() != newState.m_60734_()) {
            LazyOptional<IWorldMagic> worldMagicContainer = worldIn.getCapability(WorldMagicProvider.MAGIC);
            if (worldMagicContainer.isPresent()) {
                IWorldMagic worldMagic = worldMagicContainer.orElse(null);
                worldMagic.removeRitualTeleportLocation(pos);
            }
        }
        super.m_6810_(state, worldIn, pos, newState, isMoving);
    }

    @Override
    public void animateTick(BlockState stateIn, Level worldIn, BlockPos pos, RandomSource rand) {
        if (worldIn.isClientSide) {
            worldIn.addParticle(new MAParticleType(ParticleInit.SPARKLE_VELOCITY.get()), (double) ((float) pos.m_123341_() + rand.nextFloat()), (double) pos.above().m_123342_(), (double) ((float) pos.m_123343_() + rand.nextFloat()), 0.0, 0.1F, 0.0);
        }
    }

    public PushReaction getPistonPushReaction(BlockState state) {
        return PushReaction.BLOCK;
    }
}
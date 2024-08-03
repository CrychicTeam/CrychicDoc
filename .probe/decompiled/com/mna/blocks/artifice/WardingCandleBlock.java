package com.mna.blocks.artifice;

import com.mna.api.affinity.Affinity;
import com.mna.api.blocks.ISpellInteractibleBlock;
import com.mna.api.config.GeneralConfigValues;
import com.mna.api.particles.MAParticleType;
import com.mna.api.particles.ParticleInit;
import com.mna.api.spells.base.ISpellDefinition;
import com.mna.blocks.WaterloggableBlock;
import com.mna.capabilities.worlddata.WorldMagicProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.registries.ForgeRegistries;

public class WardingCandleBlock extends WaterloggableBlock implements ISpellInteractibleBlock<WardingCandleBlock> {

    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");

    protected static final VoxelShape SHAPE = Block.box(6.0, 0.0, 6.0, 10.0, 16.0, 10.0);

    public WardingCandleBlock() {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.SAND).noOcclusion().strength(1.0F), false);
        this.m_49959_((BlockState) this.m_49966_().m_61124_(ACTIVE, false));
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult p_225533_6_) {
        if (!worldIn.isClientSide) {
            BlockState newstate = (BlockState) state.m_61124_(ACTIVE, !(Boolean) state.m_61143_(ACTIVE));
            if ((Boolean) newstate.m_61143_(ACTIVE)) {
                if (player.m_21120_(handIn).getItem() != Items.FLINT_AND_STEEL) {
                    return InteractionResult.FAIL;
                }
                worldIn.getCapability(WorldMagicProvider.MAGIC).ifPresent(w -> w.addWardingCandleLocation(pos));
                worldIn.playSound(null, pos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F, 1.0F);
            } else {
                worldIn.getCapability(WorldMagicProvider.MAGIC).ifPresent(w -> w.removeWardingCandleLocation(pos));
                worldIn.playSound(null, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 1.0F, 1.0F);
            }
            worldIn.setBlock(pos, newstate, 1);
            worldIn.sendBlockUpdated(pos, state, newstate, 2);
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public void animateTick(BlockState stateIn, Level worldIn, BlockPos pos, RandomSource rand) {
        if ((Boolean) stateIn.m_61143_(ACTIVE)) {
            for (int i = 0; i < 2; i++) {
                worldIn.addParticle(new MAParticleType(ParticleInit.FLAME.get()).setScale(0.02F).setMaxAge((int) (20.0 + Math.random() * 20.0)).setColor(30, 172, 255), (double) ((float) pos.m_123341_() + 0.5F), (double) pos.m_123342_() + 0.93 + Math.random() * 0.05, (double) ((float) pos.m_123343_() + 0.5F), 0.0, 0.005, 0.0);
            }
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(ACTIVE);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    public int getLightEmission(BlockState state, BlockGetter world, BlockPos pos) {
        return state.m_61143_(ACTIVE) ? 15 : 5;
    }

    @Override
    public void playerDestroy(Level worldIn, Player player, BlockPos pos, BlockState state, BlockEntity te, ItemStack stack) {
        if (!worldIn.isClientSide) {
            worldIn.getCapability(WorldMagicProvider.MAGIC).ifPresent(w -> w.removeWardingCandleLocation(pos));
        }
        super.m_6240_(worldIn, player, pos, state, te, stack);
    }

    @Override
    public void wasExploded(Level worldIn, BlockPos pos, Explosion explosionIn) {
        if (!worldIn.isClientSide) {
            worldIn.getCapability(WorldMagicProvider.MAGIC).ifPresent(w -> w.removeWardingCandleLocation(pos));
        }
    }

    @Override
    public void playerWillDestroy(Level worldIn, BlockPos pos, BlockState state, Player player) {
        if (!worldIn.isClientSide) {
            worldIn.getCapability(WorldMagicProvider.MAGIC).ifPresent(w -> w.removeWardingCandleLocation(pos));
        }
        super.m_5707_(worldIn, pos, state, player);
    }

    @Override
    public boolean onHitBySpell(Level world, BlockPos pos, ISpellDefinition spell) {
        if (!world.isClientSide && spell.getAffinity().containsKey(Affinity.FIRE)) {
            BlockState existing = world.getBlockState(pos);
            if (existing.m_61138_(ACTIVE) && !(Boolean) existing.m_61143_(ACTIVE)) {
                BlockState newState = (BlockState) existing.m_61124_(ACTIVE, true);
                world.getCapability(WorldMagicProvider.MAGIC).ifPresent(w -> w.addWardingCandleLocation(pos));
                world.setBlock(pos, newState, 1);
                world.sendBlockUpdated(pos, existing, newState, 2);
                return true;
            }
        }
        return false;
    }

    public static boolean shouldEntityBeBlocked(EntityType<?> entityType) {
        ResourceLocation key = ForgeRegistries.ENTITY_TYPES.getKey(entityType);
        String regName = key.toString();
        String modName = key.getNamespace() + ":*";
        if (GeneralConfigValues.WardingCandleBlacklist.contains("*.*")) {
            return true;
        } else if (GeneralConfigValues.WardingCandleBlacklist.contains(regName) || GeneralConfigValues.WardingCandleBlacklist.contains(modName)) {
            return true;
        } else {
            return !GeneralConfigValues.WardingCandleWhitelist.contains(regName) && !GeneralConfigValues.WardingCandleWhitelist.contains(modName) ? false : false;
        }
    }

    @Override
    public boolean propagatesSkylightDown(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2) {
        return true;
    }
}
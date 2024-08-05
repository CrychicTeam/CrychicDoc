package com.mna.blocks.artifice;

import com.mna.api.sound.SFX;
import com.mna.blocks.tileentities.CoffinTile;
import com.mna.capabilities.playerdata.progression.PlayerProgressionProvider;
import com.mna.effects.EffectInit;
import com.mna.entities.boss.WitherLich;
import com.mna.factions.Factions;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.mutable.MutableBoolean;

public class CoffinBlock extends BedBlock {

    public CoffinBlock() {
        super(DyeColor.BLACK, BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).noOcclusion().strength(3.0F));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        if (worldIn.isClientSide) {
            return InteractionResult.CONSUME;
        } else {
            if (state.m_61143_(f_49440_) != BedPart.HEAD) {
                pos = pos.relative((Direction) state.m_61143_(f_54117_));
                state = worldIn.getBlockState(pos);
                if (state.m_60734_() != this) {
                    return InteractionResult.CONSUME;
                }
            }
            BlockEntity te = worldIn.getBlockEntity(pos);
            if (te != null && te instanceof CoffinTile coffin) {
                if (coffin.getRitualPlayer() != player) {
                    MutableBoolean canUse = new MutableBoolean(false);
                    player.getCapability(PlayerProgressionProvider.PROGRESSION).ifPresent(p -> canUse.setValue(p.getAlliedFaction() == Factions.UNDEAD));
                    if (!canUse.booleanValue()) {
                        player.m_213846_(Component.translatable("mna.faction.cant_use_generic"));
                        return InteractionResult.FAIL;
                    } else {
                        return super.use(state, worldIn, pos, player, handIn, hit);
                    }
                } else if (!(Boolean) state.m_61143_(f_49441_)) {
                    this.applyColdDark(player, worldIn, state, pos);
                    coffin.setRitualPlayer(null);
                    return InteractionResult.SUCCESS;
                } else {
                    return InteractionResult.FAIL;
                }
            } else {
                return InteractionResult.FAIL;
            }
        }
    }

    private void applyColdDark(Player player, Level world, BlockState coffin, BlockPos pos) {
        player.getPersistentData().putLong("coldDarkPos", pos.asLong());
        player.m_7292_(new MobEffectInstance(EffectInit.COLD_DARK.get(), 200));
        Direction dir = coffin.getBedDirection(world, pos).getOpposite();
        BlockPos destBP = pos.relative(dir.getCounterClockWise()).relative(dir, 2);
        BlockPos startBP = destBP.relative(dir, 10);
        Vec3 adjustment = Vec3.ZERO;
        switch(dir) {
            case WEST:
                adjustment = new Vec3(0.0, 0.0, -1.0);
                break;
            case EAST:
                adjustment = new Vec3(0.0, 0.0, 1.0);
                break;
            case NORTH:
                adjustment = new Vec3(1.0, 0.0, 0.0);
                break;
            case SOUTH:
                adjustment = new Vec3(-1.0, 0.0, 0.0);
        }
        Vec3 dest = new Vec3((double) ((float) destBP.m_123341_() + 0.5F), (double) destBP.m_123342_() + 0.5, (double) ((float) destBP.m_123343_() + 0.5F)).add(adjustment);
        Vec3 start = new Vec3((double) startBP.m_123341_(), (double) startBP.m_123342_(), (double) startBP.m_123343_()).add(adjustment);
        WitherLich lich = new WitherLich(world);
        lich.setFactionJoin(player, start, dest, 100);
        lich.m_6034_(start.x, start.y, start.z);
        world.m_7967_(lich);
        world.playSound(null, (double) pos.m_123341_(), (double) pos.m_123342_(), (double) pos.m_123343_(), SFX.Event.Ritual.COLD_DARK, SoundSource.PLAYERS, 1.0F, 1.0F);
    }

    @Override
    public void updateEntityAfterFallOn(BlockGetter worldIn, Entity entityIn) {
    }

    @Override
    public void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, float dist) {
        super.fallOn(level, state, pos, entity, dist * 2.0F);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CoffinTile(pos, state);
    }
}
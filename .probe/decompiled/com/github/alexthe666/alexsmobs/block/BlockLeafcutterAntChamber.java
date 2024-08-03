package com.github.alexthe666.alexsmobs.block;

import com.github.alexthe666.alexsmobs.entity.EntityLeafcutterAnt;
import com.github.alexthe666.alexsmobs.item.AMItemRegistry;
import com.github.alexthe666.alexsmobs.misc.AMPointOfInterestRegistry;
import com.github.alexthe666.alexsmobs.misc.AMTagRegistry;
import com.github.alexthe666.alexsmobs.tileentity.TileEntityLeafcutterAnthill;
import com.google.common.base.Predicates;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BeehiveBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;

public class BlockLeafcutterAntChamber extends Block {

    public static final IntegerProperty FUNGUS = IntegerProperty.create("fungus", 0, 5);

    public BlockLeafcutterAntChamber() {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.DIRT).sound(SoundType.GRAVEL).strength(1.3F).randomTicks());
        this.m_49959_((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(FUNGUS, 0));
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        int fungalLevel = (Integer) state.m_61143_(FUNGUS);
        if (fungalLevel == 5) {
            boolean shroomlight = false;
            for (BlockPos blockpos : BlockPos.betweenClosed(pos.offset(-1, -1, -1), pos.offset(1, 1, 1))) {
                if (worldIn.getBlockState(blockpos).m_60734_() == Blocks.SHROOMLIGHT) {
                    shroomlight = true;
                }
            }
            if (!shroomlight) {
                this.angerNearbyAnts(worldIn, pos);
            }
            worldIn.setBlockAndUpdate(pos, (BlockState) state.m_61124_(FUNGUS, 0));
            if (!worldIn.isClientSide) {
                if (worldIn.random.nextInt(2) == 0) {
                    Direction dir = Direction.getRandom(worldIn.random);
                    if (worldIn.getBlockState(pos.above()).m_60734_() == AMBlockRegistry.LEAFCUTTER_ANTHILL.get()) {
                        dir = Direction.DOWN;
                    }
                    BlockPos offset = pos.relative(dir);
                    if (worldIn.getBlockState(offset).m_204336_(AMTagRegistry.LEAFCUTTER_PUPA_USABLE_ON) && !worldIn.m_45527_(offset)) {
                        worldIn.setBlockAndUpdate(offset, this.m_49966_());
                    }
                }
                m_49840_(worldIn, pos, new ItemStack(AMItemRegistry.GONGYLIDIA.get()));
            }
            return InteractionResult.SUCCESS;
        } else {
            return InteractionResult.FAIL;
        }
    }

    @Override
    public void randomTick(BlockState state, ServerLevel worldIn, BlockPos pos, RandomSource random) {
        if (worldIn.isAreaLoaded(pos, 3)) {
            if (worldIn.m_45527_(pos.above())) {
                worldIn.m_46597_(pos, Blocks.DIRT.defaultBlockState());
            }
        }
    }

    @Override
    public void playerDestroy(Level worldIn, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity te, ItemStack stack) {
        super.playerDestroy(worldIn, player, pos, state, te, stack);
        this.angerNearbyAnts(worldIn, pos);
    }

    private void angerNearbyAnts(Level world, BlockPos pos) {
        List<EntityLeafcutterAnt> list = world.m_45976_(EntityLeafcutterAnt.class, new AABB(pos).inflate(20.0, 6.0, 20.0));
        Player player = null;
        List<Player> list1 = world.m_45976_(Player.class, new AABB(pos).inflate(20.0, 6.0, 20.0));
        if (!list1.isEmpty()) {
            int i = list1.size();
            player = (Player) list1.get(world.random.nextInt(i));
            if (!list.isEmpty()) {
                for (EntityLeafcutterAnt beeentity : list) {
                    if (beeentity.m_5448_() == null) {
                        beeentity.setTarget(player);
                    }
                }
            }
            if (!world.isClientSide) {
                PoiManager pointofinterestmanager = ((ServerLevel) world).getPoiManager();
                Stream<BlockPos> stream = pointofinterestmanager.findAll(poiTypeHolder -> poiTypeHolder.is(AMPointOfInterestRegistry.LEAFCUTTER_ANT_HILL.getKey()), Predicates.alwaysTrue(), pos, 50, PoiManager.Occupancy.ANY);
                for (BlockPos pos2 : (List) stream.collect(Collectors.toList())) {
                    if (world.getBlockEntity(pos2) instanceof TileEntityLeafcutterAnthill) {
                        TileEntityLeafcutterAnthill beehivetileentity = (TileEntityLeafcutterAnthill) world.getBlockEntity(pos2);
                        beehivetileentity.angerAnts(player, world.getBlockState(pos2), BeehiveBlockEntity.BeeReleaseStatus.EMERGENCY);
                    }
                }
            }
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FUNGUS);
    }
}
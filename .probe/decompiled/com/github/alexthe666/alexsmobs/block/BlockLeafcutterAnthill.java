package com.github.alexthe666.alexsmobs.block;

import com.github.alexthe666.alexsmobs.entity.EntityLeafcutterAnt;
import com.github.alexthe666.alexsmobs.entity.EntityManedWolf;
import com.github.alexthe666.alexsmobs.item.AMItemRegistry;
import com.github.alexthe666.alexsmobs.misc.AMAdvancementTriggerRegistry;
import com.github.alexthe666.alexsmobs.tileentity.AMTileEntityRegistry;
import com.github.alexthe666.alexsmobs.tileentity.TileEntityLeafcutterAnthill;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BeehiveBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;

public class BlockLeafcutterAnthill extends BaseEntityBlock {

    public BlockLeafcutterAnthill() {
        super(BlockBehaviour.Properties.of().sound(SoundType.GRAVEL).strength(0.75F));
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        if (worldIn.getBlockEntity(pos) instanceof TileEntityLeafcutterAnthill) {
            TileEntityLeafcutterAnthill hill = (TileEntityLeafcutterAnthill) worldIn.getBlockEntity(pos);
            ItemStack heldItem = player.m_21120_(handIn);
            if (heldItem.getItem() == AMItemRegistry.GONGYLIDIA.get() && hill.hasQueen()) {
                hill.releaseQueens();
                if (!player.isCreative()) {
                    heldItem.shrink(1);
                }
            }
            return InteractionResult.SUCCESS;
        } else {
            return InteractionResult.PASS;
        }
    }

    @Override
    public RenderShape getRenderShape(BlockState p_149645_1_) {
        return RenderShape.MODEL;
    }

    @Override
    public void playerWillDestroy(Level worldIn, BlockPos pos, BlockState state, Player player) {
        if (!worldIn.isClientSide && player.isCreative() && worldIn.getGameRules().getBoolean(GameRules.RULE_DOBLOCKDROPS) && worldIn.getBlockEntity(pos) instanceof TileEntityLeafcutterAnthill anthivetileentity) {
            ItemStack itemstack = new ItemStack(this);
            boolean flag = !anthivetileentity.hasNoAnts();
            if (!flag) {
                return;
            }
            if (flag) {
                CompoundTag compoundnbt = new CompoundTag();
                compoundnbt.put("Ants", anthivetileentity.getAnts());
                itemstack.addTagElement("BlockEntityTag", compoundnbt);
            }
            CompoundTag compoundnbt1 = new CompoundTag();
            itemstack.addTagElement("BlockStateTag", compoundnbt1);
            ItemEntity itementity = new ItemEntity(worldIn, (double) pos.m_123341_(), (double) pos.m_123342_(), (double) pos.m_123343_(), itemstack);
            itementity.setDefaultPickUpDelay();
            worldIn.m_7967_(itementity);
        }
        super.m_5707_(worldIn, pos, state, player);
    }

    @Override
    public void fallOn(Level worldIn, BlockState state, BlockPos pos, Entity entityIn, float fallDistance) {
        if (entityIn instanceof LivingEntity && !(entityIn instanceof EntityManedWolf)) {
            this.angerNearbyAnts(worldIn, (LivingEntity) entityIn, pos);
            if (!worldIn.isClientSide && worldIn.getBlockEntity(pos) instanceof TileEntityLeafcutterAnthill) {
                TileEntityLeafcutterAnthill beehivetileentity = (TileEntityLeafcutterAnthill) worldIn.getBlockEntity(pos);
                beehivetileentity.angerAnts((LivingEntity) entityIn, worldIn.getBlockState(pos), BeehiveBlockEntity.BeeReleaseStatus.EMERGENCY);
                if (entityIn instanceof ServerPlayer) {
                    AMAdvancementTriggerRegistry.STOMP_LEAFCUTTER_ANTHILL.trigger((ServerPlayer) entityIn);
                }
            }
        }
        super.m_142072_(worldIn, state, pos, entityIn, fallDistance);
    }

    @Override
    public void playerDestroy(Level worldIn, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity te, ItemStack stack) {
        super.m_6240_(worldIn, player, pos, state, te, stack);
        if (!worldIn.isClientSide && te instanceof TileEntityLeafcutterAnthill beehivetileentity && EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SILK_TOUCH, stack) == 0) {
            beehivetileentity.angerAnts(player, state, BeehiveBlockEntity.BeeReleaseStatus.EMERGENCY);
            worldIn.updateNeighbourForOutputSignal(pos, this);
            this.angerNearbyAnts(worldIn, pos);
        }
    }

    private void angerNearbyAnts(Level world, BlockPos pos) {
        List<EntityLeafcutterAnt> list = world.m_45976_(EntityLeafcutterAnt.class, new AABB(pos).inflate(20.0, 6.0, 20.0));
        if (!list.isEmpty()) {
            List<Player> list1 = world.m_45976_(Player.class, new AABB(pos).inflate(20.0, 6.0, 20.0));
            if (list1.isEmpty()) {
                return;
            }
            int i = list1.size();
            for (EntityLeafcutterAnt beeentity : list) {
                if (beeentity.m_5448_() == null) {
                    beeentity.setTarget((LivingEntity) list1.get(world.random.nextInt(i)));
                }
            }
        }
    }

    private void angerNearbyAnts(Level world, LivingEntity entity, BlockPos pos) {
        List<EntityLeafcutterAnt> list = world.m_45976_(EntityLeafcutterAnt.class, new AABB(pos).inflate(20.0, 6.0, 20.0));
        if (!list.isEmpty()) {
            for (EntityLeafcutterAnt beeentity : list) {
                if (beeentity.m_5448_() == null) {
                    beeentity.setTarget(entity);
                }
            }
        }
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TileEntityLeafcutterAnthill(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level0, BlockState blockState1, BlockEntityType<T> blockEntityTypeT2) {
        return level0.isClientSide ? null : m_152132_(blockEntityTypeT2, AMTileEntityRegistry.LEAFCUTTER_ANTHILL.get(), TileEntityLeafcutterAnthill::serverTick);
    }
}
package com.github.alexthe666.alexsmobs.block;

import com.github.alexthe666.alexsmobs.entity.AMEntityRegistry;
import com.github.alexthe666.alexsmobs.entity.EntityTerrapin;
import com.github.alexthe666.alexsmobs.entity.util.TerrapinTypes;
import com.github.alexthe666.alexsmobs.misc.AMTagRegistry;
import com.github.alexthe666.alexsmobs.tileentity.TileEntityTerrapinEgg;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.event.ForgeEventFactory;

public class BlockTerrapinEgg extends BaseEntityBlock {

    public static final IntegerProperty HATCH = BlockStateProperties.HATCH;

    public static final IntegerProperty EGGS = BlockStateProperties.EGGS;

    private static final VoxelShape ONE_EGG_SHAPE = Block.box(3.0, 0.0, 3.0, 12.0, 7.0, 12.0);

    private static final VoxelShape MULTI_EGG_SHAPE = Block.box(1.0, 0.0, 1.0, 15.0, 7.0, 15.0);

    public BlockTerrapinEgg() {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.SAND).strength(0.5F).sound(SoundType.METAL).randomTicks().noOcclusion());
        this.m_49959_((BlockState) ((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(HATCH, 0)).m_61124_(EGGS, 1));
    }

    public static boolean hasProperHabitat(BlockGetter reader, BlockPos blockReader) {
        return isProperHabitat(reader, blockReader.below());
    }

    @Override
    public RenderShape getRenderShape(BlockState p_149645_1_) {
        return RenderShape.MODEL;
    }

    public static boolean isProperHabitat(BlockGetter reader, BlockPos pos) {
        return reader.getBlockState(pos).m_204336_(BlockTags.SAND) || reader.getBlockState(pos).m_204336_(AMTagRegistry.CROCODILE_SPAWNS);
    }

    @Override
    public void stepOn(Level worldIn, BlockPos pos, BlockState state, Entity entityIn) {
        this.tryTrample(worldIn, pos, entityIn, 100);
        super.m_141947_(worldIn, pos, state, entityIn);
    }

    @Override
    public void fallOn(Level worldIn, BlockState state, BlockPos pos, Entity entityIn, float fallDistance) {
        if (!(entityIn instanceof Zombie)) {
            this.tryTrample(worldIn, pos, entityIn, 3);
        }
        super.m_142072_(worldIn, state, pos, entityIn, fallDistance);
    }

    private void tryTrample(Level worldIn, BlockPos pos, Entity trampler, int chances) {
        if (this.canTrample(worldIn, trampler) && !worldIn.isClientSide && worldIn.random.nextInt(chances) == 0) {
            BlockState blockstate = worldIn.getBlockState(pos);
            this.removeOneEgg(worldIn, pos, blockstate);
        }
    }

    private void removeOneEgg(Level worldIn, BlockPos pos, BlockState state) {
        worldIn.playSound(null, pos, SoundEvents.TURTLE_EGG_BREAK, SoundSource.BLOCKS, 0.7F, 0.9F + worldIn.random.nextFloat() * 0.2F);
        int i = (Integer) state.m_61143_(EGGS);
        if (i <= 1) {
            worldIn.m_46961_(pos, false);
        } else {
            worldIn.setBlock(pos, (BlockState) state.m_61124_(EGGS, i - 1), 2);
            worldIn.m_220407_(GameEvent.BLOCK_DESTROY, pos, GameEvent.Context.of(state));
            worldIn.m_46796_(2001, pos, Block.getId(state));
        }
    }

    @Override
    public void randomTick(BlockState state, ServerLevel worldIn, BlockPos pos, RandomSource random) {
        if (this.canGrow(worldIn) && hasProperHabitat(worldIn, pos)) {
            int i = (Integer) state.m_61143_(HATCH);
            if (i < 2) {
                worldIn.m_5594_(null, pos, SoundEvents.TURTLE_EGG_CRACK, SoundSource.BLOCKS, 0.7F, 0.9F + random.nextFloat() * 0.2F);
                worldIn.m_220407_(GameEvent.BLOCK_DESTROY, pos, GameEvent.Context.of(state));
                worldIn.m_7731_(pos, (BlockState) state.m_61124_(HATCH, i + 1), 2);
            } else {
                worldIn.m_5594_(null, pos, SoundEvents.TURTLE_EGG_HATCH, SoundSource.BLOCKS, 0.7F, 0.9F + random.nextFloat() * 0.2F);
                worldIn.m_220407_(GameEvent.BLOCK_DESTROY, pos, GameEvent.Context.of(state));
                worldIn.m_7471_(pos, false);
                for (int j = 0; j < state.m_61143_(EGGS); j++) {
                    worldIn.m_46796_(2001, pos, Block.getId(state));
                    EntityTerrapin turtleentity = AMEntityRegistry.TERRAPIN.get().create(worldIn);
                    turtleentity.m_146762_(-24000);
                    if (worldIn.m_7702_(pos) instanceof TileEntityTerrapinEgg eggTE) {
                        eggTE.addAttributesToOffspring(turtleentity, random);
                    }
                    turtleentity.setFromBucket(true);
                    turtleentity.m_7678_((double) pos.m_123341_() + 0.3 + (double) j * 0.2, (double) pos.m_123342_(), (double) pos.m_123343_() + 0.3, 0.0F, 0.0F);
                    worldIn.addFreshEntity(turtleentity);
                }
            }
        }
    }

    @Override
    public void onPlace(BlockState state, Level worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (hasProperHabitat(worldIn, pos) && !worldIn.isClientSide) {
            worldIn.m_46796_(2005, pos, 0);
        }
    }

    private boolean canGrow(Level worldIn) {
        float f = worldIn.m_46942_(1.0F);
        return (double) f < 0.69 && (double) f > 0.65 ? true : worldIn.random.nextInt(15) == 0;
    }

    @Override
    public void playerDestroy(Level worldIn, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity te, ItemStack stack) {
        super.m_6240_(worldIn, player, pos, state, te, stack);
        this.removeOneEgg(worldIn, pos, state);
    }

    @Override
    public boolean canBeReplaced(BlockState state, BlockPlaceContext useContext) {
        return useContext.m_43722_().getItem() == this.m_5456_() && (Integer) state.m_61143_(EGGS) < 4 || super.m_6864_(state, useContext);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState blockstate = context.m_43725_().getBlockState(context.getClickedPos());
        return blockstate.m_60734_() == this ? (BlockState) blockstate.m_61124_(EGGS, Math.min(4, (Integer) blockstate.m_61143_(EGGS) + 1)) : super.m_5573_(context);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return state.m_61143_(EGGS) > 1 ? MULTI_EGG_SHAPE : ONE_EGG_SHAPE;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(HATCH, EGGS);
    }

    private boolean canTrample(Level worldIn, Entity trampler) {
        if (trampler instanceof EntityTerrapin || trampler instanceof Bat) {
            return false;
        } else {
            return !(trampler instanceof LivingEntity) ? false : trampler instanceof Player || ForgeEventFactory.getMobGriefingEvent(worldIn, trampler);
        }
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        ItemStack pickaxe = builder.getOptionalParameter(LootContextParams.TOOL);
        BlockEntity blockentity = builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
        boolean silkTouch = false;
        if (pickaxe != null) {
            silkTouch = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SILK_TOUCH, pickaxe) > 0;
        }
        if (silkTouch && blockentity instanceof TileEntityTerrapinEgg) {
            ItemStack stack = new ItemStack(AMBlockRegistry.TERRAPIN_EGG.get());
            TileEntityTerrapinEgg egg = (TileEntityTerrapinEgg) blockentity;
            CompoundTag tag = stack.getOrCreateTagElement("BlockEntityTag");
            CompoundTag parent1 = new CompoundTag();
            CompoundTag parent2 = new CompoundTag();
            boolean flag = false;
            if (egg.parent1 != null) {
                flag = true;
                egg.parent1.writeToNBT(parent1);
            }
            if (egg.parent2 != null) {
                flag = true;
                egg.parent2.writeToNBT(parent2);
            }
            if (flag) {
                tag.put("Parent1Data", parent1);
                tag.put("Parent2Data", parent2);
            }
            return List.of(stack);
        } else {
            return List.of();
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable BlockGetter w, List<Component> list, TooltipFlag flags) {
        super.m_5871_(stack, w, list, flags);
        CompoundTag compoundtag = BlockItem.getBlockEntityData(stack);
        if (compoundtag != null && compoundtag.contains("Parent1Data") && compoundtag.contains("Parent2Data")) {
            TerrapinTypes parent1Type = TerrapinTypes.values()[Mth.clamp(compoundtag.getCompound("Parent1Data").getInt("TerrapinType"), 0, TerrapinTypes.values().length - 1)];
            TerrapinTypes parent2Type = TerrapinTypes.values()[Mth.clamp(compoundtag.getCompound("Parent2Data").getInt("TerrapinType"), 0, TerrapinTypes.values().length - 1)];
            String s1 = Component.translatable(parent1Type.getTranslationName()).getString();
            String s2 = Component.translatable(parent2Type.getTranslationName()).getString();
            list.add(Component.translatable("block.alexsmobs.terrapin_egg.desc", s1, s2).withStyle(ChatFormatting.GRAY));
        }
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState state2, boolean b) {
        if (state.m_60713_(AMBlockRegistry.TERRAPIN_EGG.get()) && (Integer) state.m_61143_(EGGS) <= 1) {
            super.m_6810_(state, level, pos, state2, b);
        }
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TileEntityTerrapinEgg(pos, state);
    }
}
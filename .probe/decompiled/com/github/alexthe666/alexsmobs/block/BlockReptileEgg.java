package com.github.alexthe666.alexsmobs.block;

import com.github.alexthe666.alexsmobs.entity.EntityCaiman;
import com.github.alexthe666.alexsmobs.entity.EntityCrocodile;
import com.github.alexthe666.alexsmobs.entity.EntityPlatypus;
import com.github.alexthe666.alexsmobs.misc.AMTagRegistry;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.registries.RegistryObject;

public class BlockReptileEgg extends Block {

    public static final IntegerProperty HATCH = BlockStateProperties.HATCH;

    public static final IntegerProperty EGGS = BlockStateProperties.EGGS;

    private static final VoxelShape ONE_EGG_SHAPE = Block.box(3.0, 0.0, 3.0, 12.0, 7.0, 12.0);

    private static final VoxelShape MULTI_EGG_SHAPE = Block.box(1.0, 0.0, 1.0, 15.0, 7.0, 15.0);

    private final RegistryObject<EntityType> births;

    public BlockReptileEgg(RegistryObject births) {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.SAND).strength(0.5F).sound(SoundType.METAL).randomTicks().noOcclusion());
        this.m_49959_((BlockState) ((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(HATCH, 0)).m_61124_(EGGS, 1));
        this.births = births;
    }

    public static boolean hasProperHabitat(BlockGetter reader, BlockPos blockReader) {
        return isProperHabitat(reader, blockReader.below());
    }

    public static boolean isProperHabitat(BlockGetter reader, BlockPos pos) {
        return reader.getBlockState(pos).m_204336_(BlockTags.SAND) || reader.getBlockState(pos).m_204336_(AMTagRegistry.CROCODILE_SPAWNS);
    }

    @Override
    public void stepOn(Level worldIn, BlockPos pos, BlockState state, Entity entityIn) {
        this.tryTrample(worldIn, pos, entityIn, 100);
        super.stepOn(worldIn, pos, state, entityIn);
    }

    @Override
    public void fallOn(Level worldIn, BlockState state, BlockPos pos, Entity entityIn, float fallDistance) {
        if (!(entityIn instanceof Zombie)) {
            this.tryTrample(worldIn, pos, entityIn, 3);
        }
        super.fallOn(worldIn, state, pos, entityIn, fallDistance);
    }

    private void tryTrample(Level worldIn, BlockPos pos, Entity trampler, int chances) {
        if (this.canTrample(worldIn, trampler) && !worldIn.isClientSide && worldIn.random.nextInt(chances) == 0) {
            AABB bb = new AABB((double) pos.m_123341_(), (double) pos.m_123342_(), (double) pos.m_123343_(), (double) (pos.m_123341_() + 1), (double) (pos.m_123342_() + 1), (double) (pos.m_123343_() + 1)).inflate(25.0, 25.0, 25.0);
            if (trampler instanceof LivingEntity) {
                for (Mob living : worldIn.m_6443_(Mob.class, bb, livingx -> livingx.m_6084_() && livingx.m_6095_() == this.births.get())) {
                    if (!(living instanceof TamableAnimal) || !((TamableAnimal) living).isTame() || !((TamableAnimal) living).isOwnedBy((LivingEntity) trampler)) {
                        living.setTarget((LivingEntity) trampler);
                    }
                }
            }
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
                    Entity fromType = this.births.get().create(worldIn);
                    if (fromType instanceof Animal animal) {
                        animal.m_146762_(-24000);
                        animal.m_21446_(pos, 20);
                    }
                    Holder<Biome> biome = worldIn.m_204166_(pos);
                    fromType.moveTo((double) pos.m_123341_() + 0.3 + (double) j * 0.2, (double) pos.m_123342_(), (double) pos.m_123343_() + 0.3, 0.0F, 0.0F);
                    if (!worldIn.f_46443_) {
                        Player closest = worldIn.m_5788_((double) ((float) pos.m_123341_() + 0.5F), (double) ((float) pos.m_123342_() + 0.5F), (double) ((float) pos.m_123343_() + 0.5F), 20.0, EntitySelector.NO_SPECTATORS);
                        if (closest != null) {
                            if (fromType instanceof TamableAnimal tamableAnimal) {
                                tamableAnimal.setTame(true);
                                tamableAnimal.setOrderedToSit(true);
                                tamableAnimal.tame(closest);
                            }
                            if (fromType instanceof EntityCrocodile crocodile) {
                                crocodile.setDesert(biome.is(AMTagRegistry.SPAWNS_DESERT_CROCODILES));
                            }
                        }
                        worldIn.addFreshEntity(fromType);
                    }
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
        return (double) f < 0.8 && (double) f > 0.65 ? true : worldIn.random.nextInt(15) == 0;
    }

    @Override
    public void playerDestroy(Level worldIn, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity te, ItemStack stack) {
        super.playerDestroy(worldIn, player, pos, state, te, stack);
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
        return blockstate.m_60734_() == this ? (BlockState) blockstate.m_61124_(EGGS, Math.min(4, (Integer) blockstate.m_61143_(EGGS) + 1)) : super.getStateForPlacement(context);
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
        if (trampler instanceof EntityCrocodile || trampler instanceof EntityCaiman || trampler instanceof EntityPlatypus || trampler instanceof Bat) {
            return false;
        } else {
            return !(trampler instanceof LivingEntity) ? false : trampler instanceof Player || ForgeEventFactory.getMobGriefingEvent(worldIn, trampler);
        }
    }
}
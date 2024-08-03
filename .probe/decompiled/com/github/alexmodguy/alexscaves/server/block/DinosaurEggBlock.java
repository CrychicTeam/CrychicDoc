package com.github.alexmodguy.alexscaves.server.block;

import com.github.alexmodguy.alexscaves.server.entity.living.DinosaurEntity;
import com.github.alexmodguy.alexscaves.server.misc.ACTagRegistry;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.registries.RegistryObject;

public class DinosaurEggBlock extends Block {

    public static final IntegerProperty HATCH = BlockStateProperties.HATCH;

    public static final BooleanProperty NEEDS_PLAYER = BooleanProperty.create("needs_player");

    private final VoxelShape voxelShape;

    private final RegistryObject<EntityType> births;

    public DinosaurEggBlock(BlockBehaviour.Properties properties, RegistryObject births, VoxelShape voxelShape) {
        super(properties);
        this.m_49959_((BlockState) ((BlockState) this.m_49966_().m_61124_(HATCH, 0)).m_61124_(NEEDS_PLAYER, false));
        this.births = births;
        this.voxelShape = voxelShape;
    }

    public DinosaurEggBlock(BlockBehaviour.Properties properties, RegistryObject births, int widthPx, int heightPx) {
        super(properties);
        this.m_49959_((BlockState) ((BlockState) this.m_49966_().m_61124_(HATCH, 0)).m_61124_(NEEDS_PLAYER, false));
        this.births = births;
        int px = (16 - widthPx) / 2;
        this.voxelShape = Block.box((double) px, 0.0, (double) px, (double) (16 - px), (double) heightPx, (double) (16 - px));
    }

    public boolean isProperHabitat(BlockGetter reader, BlockPos pos) {
        BlockState state = reader.getBlockState(pos.below());
        return state.m_280296_() && !state.m_204336_(ACTagRegistry.STOPS_DINOSAUR_EGGS);
    }

    public boolean canHatchAt(BlockGetter reader, BlockPos pos) {
        return this.isProperHabitat(reader, pos);
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

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return this.voxelShape;
    }

    private void tryTrample(Level worldIn, BlockPos pos, Entity trampler, int chances) {
        if (this.canTrample(worldIn, trampler) && !worldIn.isClientSide && worldIn.random.nextInt(chances) == 0) {
            AABB bb = new AABB((double) pos.m_123341_(), (double) pos.m_123342_(), (double) pos.m_123343_(), (double) (pos.m_123341_() + 1), (double) (pos.m_123342_() + 1), (double) (pos.m_123343_() + 1)).inflate(25.0, 25.0, 25.0);
            if (trampler instanceof LivingEntity && (!(trampler instanceof Player player) || !player.isCreative())) {
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

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos blockPos, CollisionContext context) {
        if (context instanceof EntityCollisionContext entityCollisionContext && entityCollisionContext.getEntity() instanceof DinosaurEntity) {
            return Shapes.empty();
        }
        return super.m_5939_(state, level, blockPos, context);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel worldIn, BlockPos pos, RandomSource random) {
        if (this.canGrow(worldIn, worldIn.m_8055_(pos.below())) && this.canHatchAt(worldIn, pos) && (!(Boolean) state.m_61143_(NEEDS_PLAYER) || worldIn.m_5788_((double) ((float) pos.m_123341_() + 0.5F), (double) ((float) pos.m_123342_() + 0.5F), (double) ((float) pos.m_123343_() + 0.5F), 15.0, EntitySelector.NO_SPECTATORS) != null)) {
            int i = (Integer) state.m_61143_(HATCH);
            if (i < 2) {
                worldIn.m_5594_(null, pos, SoundEvents.TURTLE_EGG_CRACK, SoundSource.BLOCKS, 0.7F, 0.9F + random.nextFloat() * 0.2F);
                worldIn.m_220407_(GameEvent.BLOCK_DESTROY, pos, GameEvent.Context.of(state));
                worldIn.m_7731_(pos, (BlockState) state.m_61124_(HATCH, i + 1), 2);
            } else {
                this.spawnDinosaurs(worldIn, pos, state);
            }
        }
    }

    public void spawnDinosaurs(Level level, BlockPos pos, BlockState state) {
        level.playSound(null, pos, SoundEvents.TURTLE_EGG_HATCH, SoundSource.BLOCKS, 0.7F, 0.9F + level.random.nextFloat() * 0.2F);
        level.m_220407_(GameEvent.BLOCK_DESTROY, pos, GameEvent.Context.of(state));
        level.removeBlock(pos, false);
        for (int j = 0; j < this.getDinosaursBornFrom(state); j++) {
            level.m_46796_(2001, pos, Block.getId(state));
            Entity fromType = this.births.get().create(level);
            if (fromType instanceof Animal animal) {
                animal.m_146762_(-24000);
            }
            fromType.moveTo((double) pos.m_123341_() + 0.3 + (double) j * 0.2, (double) pos.m_123342_(), (double) pos.m_123343_() + 0.3, 0.0F, 0.0F);
            if (!level.isClientSide) {
                Player closest = level.m_5788_((double) ((float) pos.m_123341_() + 0.5F), (double) ((float) pos.m_123342_() + 0.5F), (double) ((float) pos.m_123343_() + 0.5F), 10.0, EntitySelector.NO_SPECTATORS);
                if (closest != null && fromType instanceof DinosaurEntity dinosaur && dinosaur.tamesFromHatching()) {
                    dinosaur.m_7105_(true);
                    dinosaur.m_21839_(true);
                    dinosaur.m_21828_(closest);
                }
                level.m_7967_(fromType);
            }
        }
    }

    protected boolean canGrow(Level worldIn, BlockState stateBelow) {
        return worldIn.random.nextInt(stateBelow.m_60713_(ACBlockRegistry.FERN_THATCH.get()) ? 10 : 20) == 0;
    }

    protected int getDinosaursBornFrom(BlockState state) {
        return 1;
    }

    protected void removeOneEgg(Level worldIn, BlockPos pos, BlockState state) {
        worldIn.playSound(null, pos, SoundEvents.TURTLE_EGG_BREAK, SoundSource.BLOCKS, 0.7F, 0.9F + worldIn.random.nextFloat() * 0.2F);
        worldIn.m_46961_(pos, false);
    }

    private boolean canTrample(Level worldIn, Entity trampler) {
        if (!trampler.getType().is(ACTagRegistry.DINOSAURS)) {
            return !(trampler instanceof LivingEntity) ? false : trampler instanceof Player || ForgeEventFactory.getMobGriefingEvent(worldIn, trampler);
        } else {
            return false;
        }
    }

    @Override
    public void onPlace(BlockState state, Level worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (this.canHatchAt(worldIn, pos) && !worldIn.isClientSide) {
            worldIn.m_46796_(2005, pos, 0);
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(HATCH, NEEDS_PLAYER);
    }

    @Override
    public void playerDestroy(Level worldIn, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity te, ItemStack stack) {
        super.playerDestroy(worldIn, player, pos, state, te, stack);
        this.removeOneEgg(worldIn, pos, state);
    }
}
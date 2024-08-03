package fr.lucreeper74.createmetallurgy.content.kinetics.beltGrinder;

import com.simibubi.create.AllShapes;
import com.simibubi.create.content.kinetics.base.HorizontalKineticBlock;
import com.simibubi.create.content.kinetics.drill.DrillBlock;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import com.simibubi.create.foundation.utility.VecHelper;
import fr.lucreeper74.createmetallurgy.registries.CMBlockEntityTypes;
import fr.lucreeper74.createmetallurgy.registries.CMDamageTypes;
import java.util.Iterator;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BeltGrinderBlock extends HorizontalKineticBlock implements IBE<BeltGrinderBlockEntity> {

    public BeltGrinderBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return AllShapes.CASING_14PX.get(Direction.UP);
    }

    @Override
    public Class<BeltGrinderBlockEntity> getBlockEntityClass() {
        return BeltGrinderBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends BeltGrinderBlockEntity> getBlockEntityType() {
        return (BlockEntityType<? extends BeltGrinderBlockEntity>) CMBlockEntityTypes.BELT_GRINDER.get();
    }

    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return ((Direction) state.m_61143_(HORIZONTAL_FACING)).getAxis();
    }

    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return face.getAxis() == ((Direction) state.m_61143_(HORIZONTAL_FACING)).getAxis();
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult ray) {
        if (!player.isSpectator() && player.m_21120_(handIn).isEmpty()) {
            return ray.getDirection() != Direction.UP ? InteractionResult.PASS : this.onBlockEntityUse(worldIn, pos, be -> {
                for (int i = 0; i < be.inv.getSlots(); i++) {
                    ItemStack heldItemStack = be.inv.getStackInSlot(i);
                    if (!worldIn.isClientSide && !heldItemStack.isEmpty()) {
                        player.getInventory().placeItemBackInInventory(heldItemStack);
                    }
                }
                be.inv.clear();
                be.notifyUpdate();
                return InteractionResult.SUCCESS;
            });
        } else {
            return InteractionResult.PASS;
        }
    }

    @Override
    public void entityInside(BlockState state, Level worldIn, BlockPos pos, Entity entityIn) {
        if (!(entityIn instanceof ItemEntity)) {
            if (entityIn instanceof Player player && player.isCreative()) {
                return;
            }
            if (new AABB(pos).deflate(0.1F).intersects(entityIn.getBoundingBox())) {
                this.withBlockEntityDo(worldIn, pos, be -> {
                    float speed = Mth.abs(be.getSpeed());
                    if (be.getSpeed() != 0.0F) {
                        Level level = entityIn.level();
                        Iterator var4x = entityIn.getArmorSlots().iterator();
                        if (var4x.hasNext()) {
                            ItemStack armor = (ItemStack) var4x.next();
                            if (armor.isEmpty() || !armor.isDamageableItem() || armor.getDamageValue() >= armor.getMaxDamage()) {
                                entityIn.hurt(CMDamageTypes.grinder(level), (float) DrillBlock.getDamage(speed));
                            }
                            if (AnimationTickHolder.getTicks() % Math.round(-10.0F * speed / 32.0F + 90.0F) == 0) {
                                armor.hurt(1, entityIn.level().getRandom(), null);
                            }
                            if (!armor.isEmpty()) {
                                float pitch = speed / 256.0F + 0.8F;
                                entityIn.playSound(SoundEvents.GRINDSTONE_USE, 0.3F, entityIn.level().random.nextFloat() * 0.2F + pitch);
                                RandomSource r = level.getRandom();
                                Vec3 c = VecHelper.getCenterOf(be.m_58899_());
                                Vec3 v = c.add(VecHelper.offsetRandomly(Vec3.ZERO, r, 0.25F).multiply(1.0, 0.0, 1.0));
                                level.addParticle(ParticleTypes.CRIT, v.x, v.y + 0.4F, v.z, 0.0, 0.0, 0.0);
                            }
                        }
                    }
                });
            }
        }
    }

    @Override
    public void updateEntityAfterFallOn(BlockGetter worldIn, Entity entityIn) {
        super.m_5548_(worldIn, entityIn);
        if (entityIn instanceof ItemEntity) {
            if (!entityIn.level().isClientSide) {
                BlockPos pos = entityIn.blockPosition();
                this.withBlockEntityDo(entityIn.level(), pos, be -> {
                    if (be.getSpeed() != 0.0F) {
                        be.insertItem((ItemEntity) entityIn);
                    }
                });
            }
        }
    }
}
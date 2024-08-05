package org.violetmoon.quark.content.automation.block.be;

import java.util.List;
import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.DispenserMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.FakePlayer;
import org.jetbrains.annotations.NotNull;
import org.violetmoon.quark.content.automation.block.FeedingTroughBlock;
import org.violetmoon.quark.content.automation.module.FeedingTroughModule;
import org.violetmoon.zeta.util.MiscUtil;

public class FeedingTroughBlockEntity extends RandomizableContainerBlockEntity {

    private NonNullList<ItemStack> stacks;

    private long internalRng = 0L;

    public FeedingTroughBlockEntity(BlockPos pos, BlockState state) {
        super(FeedingTroughModule.blockEntityType, pos, state);
        this.stacks = NonNullList.withSize(9, ItemStack.EMPTY);
    }

    public void updateFoodHolder(Animal mob, Ingredient temptations, FakePlayer foodHolder) {
        for (int i = 0; i < this.getContainerSize(); i++) {
            ItemStack stack = this.m_8020_(i);
            if (temptations.test(stack) && mob.isFood(stack)) {
                Inventory inventory = foodHolder.m_150109_();
                inventory.items.set(inventory.selected, stack);
                Vec3 througPos = new Vec3((double) this.f_58858_.m_123341_(), (double) this.f_58858_.m_123342_(), (double) this.f_58858_.m_123343_()).add(0.5, -1.0, 0.5);
                Vec3 mobPosition = mob.m_20182_();
                Vec3 direction = mobPosition.subtract(througPos);
                Vec2 angles = MiscUtil.getMinecraftAnglesLossy(direction);
                Vec3 newPos = Vec3.ZERO;
                float maxDist = 5.0F;
                if (direction.lengthSqr() > (double) (maxDist * maxDist)) {
                    newPos = mobPosition.add(direction.normalize().scale((double) (-maxDist)));
                } else {
                    newPos = througPos.add(direction.normalize().scale(-1.0));
                }
                foodHolder.m_7678_(newPos.x, newPos.y, newPos.z, angles.y, angles.x);
                return;
            }
        }
    }

    public FeedingTroughBlockEntity.FeedResult tryFeedingAnimal(Animal animal) {
        for (int i = 0; i < this.getContainerSize(); i++) {
            ItemStack stack = this.m_8020_(i);
            if (animal.isFood(stack)) {
                SoundEvent soundEvent = animal.m_7866_(stack);
                if (soundEvent != null) {
                    animal.m_5496_(soundEvent, 0.5F + 0.5F * (float) this.f_58857_.random.nextInt(2), (this.f_58857_.random.nextFloat() - this.f_58857_.random.nextFloat()) * 0.2F + 1.0F);
                }
                this.addItemParticles(animal, stack, 16);
                stack.shrink(1);
                this.setChanged();
                if (this.getSpecialRand().nextDouble() < FeedingTroughModule.loveChance) {
                    List<Animal> animalsAround = this.f_58857_.m_45976_(Animal.class, new AABB(this.f_58858_).inflate(FeedingTroughModule.range));
                    if (animalsAround.size() <= FeedingTroughModule.maxAnimals) {
                        animal.setInLove(null);
                    }
                    return FeedingTroughBlockEntity.FeedResult.SECS;
                }
                return FeedingTroughBlockEntity.FeedResult.FED;
            }
        }
        return FeedingTroughBlockEntity.FeedResult.NONE;
    }

    @Override
    public void setChanged() {
        super.m_6596_();
        BlockState state = this.m_58900_();
        if (this.f_58857_ != null && state.m_60734_() instanceof FeedingTroughBlock) {
            boolean full = (Boolean) state.m_61143_(FeedingTroughBlock.FULL);
            boolean shouldBeFull = !this.isEmpty();
            if (full != shouldBeFull) {
                this.f_58857_.setBlock(this.f_58858_, (BlockState) state.m_61124_(FeedingTroughBlock.FULL, shouldBeFull), 2);
            }
        }
    }

    private void addItemParticles(Entity entity, ItemStack stack, int count) {
        for (int i = 0; i < count; i++) {
            Vec3 direction = new Vec3(((double) entity.level().random.nextFloat() - 0.5) * 0.1, Math.random() * 0.1 + 0.1, 0.0);
            direction = direction.xRot(-entity.getXRot() * (float) (Math.PI / 180.0));
            direction = direction.yRot(-entity.getYRot() * (float) (Math.PI / 180.0));
            double yVelocity = (double) (-entity.level().random.nextFloat()) * 0.6 - 0.3;
            Vec3 position = new Vec3(((double) entity.level().random.nextFloat() - 0.5) * 0.3, yVelocity, 0.6);
            Vec3 entityPos = entity.position();
            position = position.xRot(-entity.getXRot() * (float) (Math.PI / 180.0));
            position = position.yRot(-entity.getYRot() * (float) (Math.PI / 180.0));
            position = position.add(entityPos.x, entityPos.y + (double) entity.getEyeHeight(), entityPos.z);
            if (this.f_58857_ instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(new ItemParticleOption(ParticleTypes.ITEM, stack), position.x, position.y, position.z, 1, direction.x, direction.y + 0.05, direction.z, 0.0);
            } else if (this.f_58857_ != null) {
                this.f_58857_.addParticle(new ItemParticleOption(ParticleTypes.ITEM, stack), position.x, position.y, position.z, direction.x, direction.y + 0.05, direction.z);
            }
        }
    }

    private Random getSpecialRand() {
        Random specialRand = new Random(this.internalRng);
        this.internalRng = specialRand.nextLong();
        return specialRand;
    }

    @Override
    public int getContainerSize() {
        return 9;
    }

    @Override
    public boolean isEmpty() {
        for (int i = 0; i < this.getContainerSize(); i++) {
            ItemStack stack = this.m_8020_(i);
            if (!stack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @NotNull
    @Override
    protected Component getDefaultName() {
        return Component.translatable("quark.container.feeding_trough");
    }

    @Override
    public void load(@NotNull CompoundTag nbt) {
        super.m_142466_(nbt);
        this.internalRng = nbt.getLong("rng");
        this.stacks = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        if (!this.m_59631_(nbt)) {
            ContainerHelper.loadAllItems(nbt, this.stacks);
        }
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag nbt) {
        super.m_183515_(nbt);
        nbt.putLong("rng", this.internalRng);
        if (!this.m_59634_(nbt)) {
            ContainerHelper.saveAllItems(nbt, this.stacks);
        }
    }

    @NotNull
    @Override
    protected NonNullList<ItemStack> getItems() {
        return this.stacks;
    }

    @Override
    protected void setItems(@NotNull NonNullList<ItemStack> items) {
        this.stacks = items;
    }

    @NotNull
    @Override
    protected AbstractContainerMenu createMenu(int id, @NotNull Inventory playerInventory) {
        return new DispenserMenu(id, playerInventory, this) {

            @Override
            public MenuType<?> getType() {
                return FeedingTroughModule.menuType;
            }
        };
    }

    public static enum FeedResult {

        FED, SECS, NONE
    }
}
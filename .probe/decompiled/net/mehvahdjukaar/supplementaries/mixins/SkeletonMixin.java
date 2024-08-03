package net.mehvahdjukaar.supplementaries.mixins;

import net.mehvahdjukaar.supplementaries.api.IQuiverEntity;
import net.mehvahdjukaar.supplementaries.common.network.ModNetwork;
import net.mehvahdjukaar.supplementaries.common.network.SyncSkellyQuiverPacket;
import net.mehvahdjukaar.supplementaries.reg.ModRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ Skeleton.class })
public abstract class SkeletonMixin extends AbstractSkeleton implements IQuiverEntity {

    @Unique
    private ItemStack supplementaries$quiver = ItemStack.EMPTY;

    @Unique
    private float supplementaries$quiverDropChance = 0.6F;

    protected SkeletonMixin(EntityType<? extends AbstractSkeleton> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = { "dropCustomDeathLoot" }, at = { @At("TAIL") })
    protected void dropCustomDeathLoot(DamageSource damageSource, int looting, boolean hitByPlayer, CallbackInfo ci) {
        if (this.supplementaries$quiver != null && hitByPlayer) {
            ItemStack itemStack = this.supplementaries$quiver;
            if (Math.max(this.f_19796_.nextFloat() - (float) looting * 0.02F, 0.0F) < this.supplementaries$quiverDropChance) {
                this.m_19983_(itemStack);
                this.supplementaries$quiver = ItemStack.EMPTY;
            }
        }
    }

    @Inject(method = { "addAdditionalSaveData" }, at = { @At("TAIL") })
    public void addAdditionalSaveData(CompoundTag compound, CallbackInfo ci) {
        if (!this.supplementaries$quiver.isEmpty()) {
            compound.put("Quiver", this.supplementaries$quiver.save(new CompoundTag()));
            compound.putFloat("QuiverDropChance", this.supplementaries$quiverDropChance);
        }
    }

    @Inject(method = { "readAdditionalSaveData" }, at = { @At("TAIL") })
    public void readAdditionalSaveData(CompoundTag compound, CallbackInfo ci) {
        if (compound.contains("Quiver")) {
            this.supplementaries$setQuiver(ItemStack.of(compound.getCompound("Quiver")));
            this.supplementaries$quiverDropChance = compound.getFloat("QuiverDropChance");
        }
    }

    @Override
    public ItemStack supplementaries$getQuiver() {
        return this.supplementaries$quiver;
    }

    @Override
    public void supplementaries$setQuiver(ItemStack quiver) {
        this.supplementaries$quiver = quiver;
        if (!this.m_9236_().isClientSide) {
            ModNetwork.CHANNEL.sentToAllClientPlayersTrackingEntity(this, new SyncSkellyQuiverPacket(this));
        }
    }

    @Override
    public boolean wantsToPickUp(ItemStack stack) {
        return this.supplementaries$quiver == null && stack.getItem() == ModRegistry.QUIVER_ITEM.get() ? true : super.m_7243_(stack);
    }

    @Override
    public ItemStack equipItemIfPossible(ItemStack stack) {
        if (stack.getItem() == ModRegistry.QUIVER_ITEM.get()) {
            if (this.supplementaries$quiver != null) {
                this.m_19983_(this.supplementaries$quiver);
            }
            this.supplementaries$setQuiver(stack);
            this.supplementaries$quiverDropChance = 1.0F;
            return stack;
        } else {
            return super.m_255207_(stack);
        }
    }
}
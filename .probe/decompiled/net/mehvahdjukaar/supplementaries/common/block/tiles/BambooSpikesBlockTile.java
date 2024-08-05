package net.mehvahdjukaar.supplementaries.common.block.tiles;

import net.mehvahdjukaar.supplementaries.common.items.BambooSpikesTippedItem;
import net.mehvahdjukaar.supplementaries.configs.CommonConfigs;
import net.mehvahdjukaar.supplementaries.reg.ModRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class BambooSpikesBlockTile extends BlockEntity {

    public Potion potion = Potions.EMPTY;

    public int charges = 0;

    public long lastTicked = 0L;

    public static final float POTION_MULTIPLIER = 0.1F;

    public static final int MAX_CHARGES = 16;

    public BambooSpikesBlockTile(BlockPos pos, BlockState state) {
        super((BlockEntityType<?>) ModRegistry.BAMBOO_SPIKES_TILE.get(), pos, state);
    }

    public int getColor() {
        return this.hasPotion() ? PotionUtils.getColor(this.potion) : 16777215;
    }

    public boolean hasPotion() {
        return this.potion != Potions.EMPTY && this.charges != 0;
    }

    public boolean isOnCooldown(Level world) {
        return world.getGameTime() - this.lastTicked < 20L;
    }

    public boolean consumeCharge(Level world) {
        if ((Boolean) CommonConfigs.Functional.ONLY_ALLOW_HARMFUL.get() && !((MobEffectInstance) this.potion.getEffects().get(0)).getEffect().isBeneficial()) {
            return false;
        } else {
            this.lastTicked = world.getGameTime();
            this.charges--;
            this.m_6596_();
            if (this.charges <= 0) {
                this.charges = 0;
                this.potion = Potions.EMPTY;
                return true;
            } else {
                return false;
            }
        }
    }

    public void setMissingCharges(int missing) {
        this.charges = Math.max(16 - missing, 0);
    }

    public boolean tryApplyPotion(Potion newPotion) {
        if ((this.charges == 0 || this.potion == Potions.EMPTY || this.potion.equals(newPotion) && this.charges != 16) && BambooSpikesTippedItem.isPotionValid(newPotion)) {
            this.potion = newPotion;
            this.charges = 16;
            this.m_6596_();
            this.f_58857_.sendBlockUpdated(this.f_58858_, this.m_58900_(), this.m_58900_(), 3);
            return true;
        } else {
            return false;
        }
    }

    public boolean interactWithEntity(LivingEntity le, @NotNull Level world) {
        if (this.hasPotion() && !this.isOnCooldown(world)) {
            boolean used = false;
            for (MobEffectInstance effect : this.potion.getEffects()) {
                if (le.canBeAffected(effect) && !le.hasEffect(effect.getEffect())) {
                    if (effect.getEffect().isInstantenous()) {
                        float health = 0.5F;
                        effect.getEffect().applyInstantenousEffect(null, null, le, effect.getAmplifier(), (double) health);
                    } else {
                        le.addEffect(new MobEffectInstance(effect.getEffect(), (int) ((float) effect.getDuration() * 0.1F), effect.getAmplifier()));
                    }
                    used = true;
                }
            }
            if (used) {
                this.makeParticle(world);
                return this.consumeCharge(world);
            }
        }
        return false;
    }

    public void makeParticle(Level level) {
        int i = this.getColor();
        double d0 = (double) (i >> 16 & 0xFF) / 255.0;
        double d1 = (double) (i >> 8 & 0xFF) / 255.0;
        double d2 = (double) (i & 0xFF) / 255.0;
        BlockPos pos = this.m_58899_();
        level.addParticle(ParticleTypes.ENTITY_EFFECT, (double) pos.m_123341_() + 0.5 + ((double) level.random.nextFloat() - 0.5) * 0.75, (double) pos.m_123342_() + 0.5 + ((double) level.random.nextFloat() - 0.5) * 0.75, (double) pos.m_123343_() + 0.5 + ((double) level.random.nextFloat() - 0.5) * 0.75, d0, d1, d2);
    }

    public ItemStack getSpikeItem() {
        if (this.hasPotion()) {
            ItemStack stack = BambooSpikesTippedItem.makeSpikeItem(this.potion);
            stack.setDamageValue(stack.getMaxDamage() - this.charges);
            return stack;
        } else {
            return new ItemStack((ItemLike) ModRegistry.BAMBOO_SPIKES_ITEM.get());
        }
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        compound.putInt("Charges", this.charges);
        compound.putLong("LastTicked", this.lastTicked);
        ResourceLocation resourcelocation = BuiltInRegistries.POTION.getKey(this.potion);
        compound.putString("Potion", resourcelocation.toString());
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        this.charges = compound.getInt("Charges");
        this.lastTicked = compound.getLong("LastTicked");
        this.potion = PotionUtils.getPotion(compound);
    }

    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return this.m_187482_();
    }
}
package net.minecraft.world.entity.animal;

import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.Shearable;
import net.minecraft.world.entity.VariantHolder;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SuspiciousStewItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SuspiciousEffectHolder;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import org.apache.commons.lang3.tuple.Pair;

public class MushroomCow extends Cow implements Shearable, VariantHolder<MushroomCow.MushroomType> {

    private static final EntityDataAccessor<String> DATA_TYPE = SynchedEntityData.defineId(MushroomCow.class, EntityDataSerializers.STRING);

    private static final int MUTATE_CHANCE = 1024;

    @Nullable
    private MobEffect effect;

    private int effectDuration;

    @Nullable
    private UUID lastLightningBoltUUID;

    public MushroomCow(EntityType<? extends MushroomCow> entityTypeExtendsMushroomCow0, Level level1) {
        super(entityTypeExtendsMushroomCow0, level1);
    }

    @Override
    public float getWalkTargetValue(BlockPos blockPos0, LevelReader levelReader1) {
        return levelReader1.m_8055_(blockPos0.below()).m_60713_(Blocks.MYCELIUM) ? 10.0F : levelReader1.getPathfindingCostFromLightLevels(blockPos0);
    }

    public static boolean checkMushroomSpawnRules(EntityType<MushroomCow> entityTypeMushroomCow0, LevelAccessor levelAccessor1, MobSpawnType mobSpawnType2, BlockPos blockPos3, RandomSource randomSource4) {
        return levelAccessor1.m_8055_(blockPos3.below()).m_204336_(BlockTags.MOOSHROOMS_SPAWNABLE_ON) && m_186209_(levelAccessor1, blockPos3);
    }

    @Override
    public void thunderHit(ServerLevel serverLevel0, LightningBolt lightningBolt1) {
        UUID $$2 = lightningBolt1.m_20148_();
        if (!$$2.equals(this.lastLightningBoltUUID)) {
            this.setVariant(this.getVariant() == MushroomCow.MushroomType.RED ? MushroomCow.MushroomType.BROWN : MushroomCow.MushroomType.RED);
            this.lastLightningBoltUUID = $$2;
            this.m_5496_(SoundEvents.MOOSHROOM_CONVERT, 2.0F, 1.0F);
        }
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        this.f_19804_.define(DATA_TYPE, MushroomCow.MushroomType.RED.type);
    }

    @Override
    public InteractionResult mobInteract(Player player0, InteractionHand interactionHand1) {
        ItemStack $$2 = player0.m_21120_(interactionHand1);
        if ($$2.is(Items.BOWL) && !this.m_6162_()) {
            boolean $$3 = false;
            ItemStack $$4;
            if (this.effect != null) {
                $$3 = true;
                $$4 = new ItemStack(Items.SUSPICIOUS_STEW);
                SuspiciousStewItem.saveMobEffect($$4, this.effect, this.effectDuration);
                this.effect = null;
                this.effectDuration = 0;
            } else {
                $$4 = new ItemStack(Items.MUSHROOM_STEW);
            }
            ItemStack $$6 = ItemUtils.createFilledResult($$2, player0, $$4, false);
            player0.m_21008_(interactionHand1, $$6);
            SoundEvent $$7;
            if ($$3) {
                $$7 = SoundEvents.MOOSHROOM_MILK_SUSPICIOUSLY;
            } else {
                $$7 = SoundEvents.MOOSHROOM_MILK;
            }
            this.m_5496_($$7, 1.0F, 1.0F);
            return InteractionResult.sidedSuccess(this.m_9236_().isClientSide);
        } else if ($$2.is(Items.SHEARS) && this.readyForShearing()) {
            this.shear(SoundSource.PLAYERS);
            this.m_146852_(GameEvent.SHEAR, player0);
            if (!this.m_9236_().isClientSide) {
                $$2.hurtAndBreak(1, player0, p_28927_ -> p_28927_.m_21190_(interactionHand1));
            }
            return InteractionResult.sidedSuccess(this.m_9236_().isClientSide);
        } else if (this.getVariant() == MushroomCow.MushroomType.BROWN && $$2.is(ItemTags.SMALL_FLOWERS)) {
            if (this.effect != null) {
                for (int $$9 = 0; $$9 < 2; $$9++) {
                    this.m_9236_().addParticle(ParticleTypes.SMOKE, this.m_20185_() + this.f_19796_.nextDouble() / 2.0, this.m_20227_(0.5), this.m_20189_() + this.f_19796_.nextDouble() / 2.0, 0.0, this.f_19796_.nextDouble() / 5.0, 0.0);
                }
            } else {
                Optional<Pair<MobEffect, Integer>> $$10 = this.getEffectFromItemStack($$2);
                if (!$$10.isPresent()) {
                    return InteractionResult.PASS;
                }
                Pair<MobEffect, Integer> $$11 = (Pair<MobEffect, Integer>) $$10.get();
                if (!player0.getAbilities().instabuild) {
                    $$2.shrink(1);
                }
                for (int $$12 = 0; $$12 < 4; $$12++) {
                    this.m_9236_().addParticle(ParticleTypes.EFFECT, this.m_20185_() + this.f_19796_.nextDouble() / 2.0, this.m_20227_(0.5), this.m_20189_() + this.f_19796_.nextDouble() / 2.0, 0.0, this.f_19796_.nextDouble() / 5.0, 0.0);
                }
                this.effect = (MobEffect) $$11.getLeft();
                this.effectDuration = (Integer) $$11.getRight();
                this.m_5496_(SoundEvents.MOOSHROOM_EAT, 2.0F, 1.0F);
            }
            return InteractionResult.sidedSuccess(this.m_9236_().isClientSide);
        } else {
            return super.mobInteract(player0, interactionHand1);
        }
    }

    @Override
    public void shear(SoundSource soundSource0) {
        this.m_9236_().playSound(null, this, SoundEvents.MOOSHROOM_SHEAR, soundSource0, 1.0F, 1.0F);
        if (!this.m_9236_().isClientSide()) {
            Cow $$1 = EntityType.COW.create(this.m_9236_());
            if ($$1 != null) {
                ((ServerLevel) this.m_9236_()).sendParticles(ParticleTypes.EXPLOSION, this.m_20185_(), this.m_20227_(0.5), this.m_20189_(), 1, 0.0, 0.0, 0.0, 0.0);
                this.m_146870_();
                $$1.m_7678_(this.m_20185_(), this.m_20186_(), this.m_20189_(), this.m_146908_(), this.m_146909_());
                $$1.m_21153_(this.m_21223_());
                $$1.f_20883_ = this.f_20883_;
                if (this.m_8077_()) {
                    $$1.m_6593_(this.m_7770_());
                    $$1.m_20340_(this.m_20151_());
                }
                if (this.m_21532_()) {
                    $$1.m_21530_();
                }
                $$1.m_20331_(this.m_20147_());
                this.m_9236_().m_7967_($$1);
                for (int $$2 = 0; $$2 < 5; $$2++) {
                    this.m_9236_().m_7967_(new ItemEntity(this.m_9236_(), this.m_20185_(), this.m_20227_(1.0), this.m_20189_(), new ItemStack(this.getVariant().blockState.m_60734_())));
                }
            }
        }
    }

    @Override
    public boolean readyForShearing() {
        return this.m_6084_() && !this.m_6162_();
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag0) {
        super.m_7380_(compoundTag0);
        compoundTag0.putString("Type", this.getVariant().getSerializedName());
        if (this.effect != null) {
            compoundTag0.putInt("EffectId", MobEffect.getId(this.effect));
            compoundTag0.putInt("EffectDuration", this.effectDuration);
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag0) {
        super.m_7378_(compoundTag0);
        this.setVariant(MushroomCow.MushroomType.byType(compoundTag0.getString("Type")));
        if (compoundTag0.contains("EffectId", 99)) {
            this.effect = MobEffect.byId(compoundTag0.getInt("EffectId"));
        }
        if (compoundTag0.contains("EffectDuration", 99)) {
            this.effectDuration = compoundTag0.getInt("EffectDuration");
        }
    }

    private Optional<Pair<MobEffect, Integer>> getEffectFromItemStack(ItemStack itemStack0) {
        SuspiciousEffectHolder $$1 = SuspiciousEffectHolder.tryGet(itemStack0.getItem());
        return $$1 != null ? Optional.of(Pair.of($$1.getSuspiciousEffect(), $$1.getEffectDuration())) : Optional.empty();
    }

    public void setVariant(MushroomCow.MushroomType mushroomCowMushroomType0) {
        this.f_19804_.set(DATA_TYPE, mushroomCowMushroomType0.type);
    }

    public MushroomCow.MushroomType getVariant() {
        return MushroomCow.MushroomType.byType(this.f_19804_.get(DATA_TYPE));
    }

    @Nullable
    public MushroomCow getBreedOffspring(ServerLevel serverLevel0, AgeableMob ageableMob1) {
        MushroomCow $$2 = EntityType.MOOSHROOM.create(serverLevel0);
        if ($$2 != null) {
            $$2.setVariant(this.getOffspringType((MushroomCow) ageableMob1));
        }
        return $$2;
    }

    private MushroomCow.MushroomType getOffspringType(MushroomCow mushroomCow0) {
        MushroomCow.MushroomType $$1 = this.getVariant();
        MushroomCow.MushroomType $$2 = mushroomCow0.getVariant();
        MushroomCow.MushroomType $$3;
        if ($$1 == $$2 && this.f_19796_.nextInt(1024) == 0) {
            $$3 = $$1 == MushroomCow.MushroomType.BROWN ? MushroomCow.MushroomType.RED : MushroomCow.MushroomType.BROWN;
        } else {
            $$3 = this.f_19796_.nextBoolean() ? $$1 : $$2;
        }
        return $$3;
    }

    public static enum MushroomType implements StringRepresentable {

        RED("red", Blocks.RED_MUSHROOM.defaultBlockState()), BROWN("brown", Blocks.BROWN_MUSHROOM.defaultBlockState());

        public static final StringRepresentable.EnumCodec<MushroomCow.MushroomType> CODEC = StringRepresentable.fromEnum(MushroomCow.MushroomType::values);

        final String type;

        final BlockState blockState;

        private MushroomType(String p_28967_, BlockState p_28968_) {
            this.type = p_28967_;
            this.blockState = p_28968_;
        }

        public BlockState getBlockState() {
            return this.blockState;
        }

        @Override
        public String getSerializedName() {
            return this.type;
        }

        static MushroomCow.MushroomType byType(String p_28977_) {
            return (MushroomCow.MushroomType) CODEC.byName(p_28977_, RED);
        }
    }
}
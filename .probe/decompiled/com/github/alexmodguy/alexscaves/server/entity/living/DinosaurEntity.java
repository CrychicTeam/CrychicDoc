package com.github.alexmodguy.alexscaves.server.entity.living;

import com.github.alexmodguy.alexscaves.client.particle.ACParticleRegistry;
import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.github.alexmodguy.alexscaves.server.entity.ACEntityRegistry;
import com.github.alexmodguy.alexscaves.server.entity.util.LaysEggs;
import com.github.alexmodguy.alexscaves.server.item.ACItemRegistry;
import com.github.alexmodguy.alexscaves.server.level.storage.ACWorldData;
import com.github.alexmodguy.alexscaves.server.misc.ACAdvancementTriggerRegistry;
import com.github.alexmodguy.alexscaves.server.misc.ACMath;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import com.github.alexmodguy.alexscaves.server.misc.ACTagRegistry;
import com.github.alexthe666.citadel.server.entity.IDancesToJukebox;
import com.github.alexthe666.citadel.server.entity.pathfinding.raycoms.IAdvancedPathingMob;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public abstract class DinosaurEntity extends TamableAnimal implements IDancesToJukebox, LaysEggs, IAdvancedPathingMob {

    private static final EntityDataAccessor<Boolean> DANCING = SynchedEntityData.defineId(DinosaurEntity.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> HAS_EGG = SynchedEntityData.defineId(DinosaurEntity.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Integer> COMMAND = SynchedEntityData.defineId(DinosaurEntity.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Integer> ALT_SKIN = SynchedEntityData.defineId(DinosaurEntity.class, EntityDataSerializers.INT);

    public float prevDanceProgress;

    public float danceProgress;

    private BlockPos jukeboxPosition;

    private float prevSitProgress;

    private float sitProgress;

    private float prevBuryEggsProgress;

    private float buryEggsProgress;

    public boolean buryingEggs;

    public DinosaurEntity(EntityType entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.f_19804_.define(DANCING, false);
        this.f_19804_.define(HAS_EGG, false);
        this.f_19804_.define(COMMAND, 0);
        this.f_19804_.define(ALT_SKIN, 0);
    }

    public static boolean checkPrehistoricSpawnRules(EntityType<? extends Animal> type, LevelAccessor levelAccessor, MobSpawnType mobType, BlockPos pos, RandomSource randomSource) {
        return levelAccessor.m_8055_(pos.below()).m_204336_(ACTagRegistry.DINOSAURS_SPAWNABLE_ON) && levelAccessor.m_6425_(pos).isEmpty() && levelAccessor.m_6425_(pos.below()).isEmpty();
    }

    public static boolean checkPrehistoricPostBossSpawnRules(EntityType<? extends Animal> type, LevelAccessor levelAccessor, MobSpawnType mobType, BlockPos pos, RandomSource randomSource) {
        if (checkPrehistoricSpawnRules(type, levelAccessor, mobType, pos, randomSource) && levelAccessor instanceof ServerLevel serverLevel) {
            ACWorldData data = ACWorldData.get(serverLevel);
            return data != null && data.isPrimordialBossDefeatedOnce();
        } else {
            return false;
        }
    }

    @Override
    public void tick() {
        super.m_8119_();
        this.prevDanceProgress = this.danceProgress;
        this.prevSitProgress = this.sitProgress;
        this.prevBuryEggsProgress = this.buryEggsProgress;
        if (this.jukeboxPosition == null || !this.jukeboxPosition.m_203195_(this.m_20182_(), 15.0) || !this.m_9236_().getBlockState(this.jukeboxPosition).m_60713_(Blocks.JUKEBOX)) {
            this.setDancing(false);
            this.jukeboxPosition = null;
        }
        if (this.isDancing() && this.danceProgress < 5.0F) {
            this.danceProgress++;
        }
        if (!this.isDancing() && this.danceProgress > 0.0F) {
            this.danceProgress--;
        }
        if (this.isInSittingPose() && this.sitProgress < this.maxSitTicks()) {
            this.sitProgress++;
        }
        if (!this.isInSittingPose() && this.sitProgress > 0.0F) {
            this.sitProgress--;
        }
        if (this.buryingEggs && this.buryEggsProgress < 5.0F) {
            this.buryEggsProgress++;
        }
        if (!this.buryingEggs && this.buryEggsProgress > 0.0F) {
            this.buryEggsProgress--;
        }
    }

    public float maxSitTicks() {
        return 10.0F;
    }

    public boolean isDancing() {
        return this.f_19804_.get(DANCING);
    }

    @Override
    public void setDancing(boolean bool) {
        this.f_19804_.set(DANCING, bool);
    }

    @Override
    public boolean hasEgg() {
        return this.f_19804_.get(HAS_EGG);
    }

    @Override
    public void setHasEgg(boolean hasEgg) {
        this.f_19804_.set(HAS_EGG, hasEgg);
    }

    public int getCommand() {
        return this.f_19804_.get(COMMAND);
    }

    public void setCommand(int command) {
        this.f_19804_.set(COMMAND, command);
    }

    public int getAltSkin() {
        return this.f_19804_.get(ALT_SKIN);
    }

    public void setAltSkin(int skinIndex) {
        this.f_19804_.set(ALT_SKIN, skinIndex);
    }

    @Override
    public void setRecordPlayingNearby(BlockPos pos, boolean playing) {
        this.onClientPlayMusicDisc(this.m_19879_(), pos, playing);
    }

    @Override
    public void setJukeboxPos(BlockPos blockPos) {
        this.jukeboxPosition = blockPos;
    }

    public float getDanceProgress(float partialTicks) {
        return (this.prevDanceProgress + (this.danceProgress - this.prevDanceProgress) * partialTicks) * 0.2F;
    }

    public float getSitProgress(float partialTicks) {
        return (this.prevSitProgress + (this.sitProgress - this.prevSitProgress) * partialTicks) / this.maxSitTicks();
    }

    public float getBuryEggsProgress(float partialTicks) {
        return (this.prevBuryEggsProgress + (this.buryEggsProgress - this.prevBuryEggsProgress) * partialTicks) * 0.2F;
    }

    @Override
    public void travel(Vec3 vec3d) {
        if (this.isDancing() || this.isInSittingPose()) {
            if (this.m_21573_().getPath() != null) {
                this.m_21573_().stop();
            }
            vec3d = Vec3.ZERO;
        }
        super.m_7023_(vec3d);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("Command", this.getCommand());
        compound.putBoolean("Egg", this.hasEgg());
        compound.putInt("AltSkin", this.getAltSkin());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setCommand(compound.getInt("Command"));
        this.setHasEgg(compound.getBoolean("Egg"));
        int altSkin = compound.getInt("AltSkin");
        if (compound.contains("Retro") && compound.getBoolean("Retro")) {
            altSkin = 1;
        }
        this.setAltSkin(altSkin);
    }

    public boolean tamesFromHatching() {
        return false;
    }

    public boolean hasRidingMeter() {
        return false;
    }

    public float getMeterAmount() {
        return 0.0F;
    }

    protected void clampRotation(LivingEntity livingEntity, float clampRange) {
        livingEntity.setYBodyRot(this.m_146908_());
        float f = Mth.wrapDegrees(livingEntity.m_146908_() - this.m_146908_());
        float f1 = Mth.clamp(f, -clampRange, clampRange);
        livingEntity.f_19859_ += f1 - f;
        livingEntity.yBodyRotO += f1 - f;
        livingEntity.m_146922_(livingEntity.m_146908_() + f1 - f);
        livingEntity.setYHeadRot(livingEntity.m_146908_());
    }

    @Override
    public void onLayEggTick(BlockPos belowEgg, int time) {
        this.f_267362_.update(0.5F, 0.4F);
        this.m_9236_().broadcastEntityEvent(this, (byte) 77);
    }

    @Override
    public void handleEntityEvent(byte b) {
        if (b == 77) {
            this.buryingEggs = true;
            float radius = this.m_20205_() * 0.55F;
            float particleCount = (float) (5 + this.f_19796_.nextInt(5)) * radius;
            for (int i1 = 0; (float) i1 < particleCount; i1++) {
                double motionX = (double) (this.m_217043_().nextFloat() - 0.5F) * 0.7;
                double motionY = (double) this.m_217043_().nextFloat() * 0.7 + 0.8F;
                double motionZ = (double) (this.m_217043_().nextFloat() - 0.5F) * 0.7;
                float angle = (float) (Math.PI / 180.0) * (this.f_20883_ + (float) i1 / particleCount * 360.0F);
                double extraX = (double) (radius * Mth.sin((float) (Math.PI + (double) angle)));
                double extraY = 1.2F;
                double extraZ = (double) (radius * Mth.cos(angle));
                BlockPos ground = BlockPos.containing(ACMath.getGroundBelowPosition(this.m_9236_(), new Vec3((double) Mth.floor(this.m_20185_() + extraX), (double) Mth.floor(this.m_20186_() + extraY), (double) Mth.floor(this.m_20189_() + extraZ))));
                BlockState groundState = this.m_9236_().getBlockState(ground.below());
                if (groundState.m_280296_() && this.m_9236_().isClientSide) {
                    this.m_9236_().addParticle(new BlockParticleOption(ParticleTypes.BLOCK, groundState), true, this.m_20185_() + extraX, (double) ground.m_123342_(), this.m_20189_() + extraZ, motionX, motionY, motionZ);
                }
            }
        } else if (b == 78) {
            this.buryingEggs = false;
        } else if (b != 82 && b != 83) {
            super.handleEntityEvent(b);
        } else {
            ParticleOptions options;
            if (b == 82) {
                options = ACParticleRegistry.DINOSAUR_TRANSFORMATION_AMBER.get();
            } else {
                options = ACParticleRegistry.DINOSAUR_TRANSFORMATION_TECTONIC.get();
            }
            for (int i = 0; i < 15 + this.m_9236_().random.nextInt(5); i++) {
                this.m_9236_().addParticle(options, this.m_20185_(), this.m_20227_(0.5), this.m_20189_(), (double) this.m_19879_(), 0.0, 0.0);
            }
        }
    }

    public boolean onFeedMixture(ItemStack itemStack, Player player) {
        return false;
    }

    @Override
    public boolean isInSittingPose() {
        return super.isInSittingPose() && !this.m_20160_() && !this.m_20159_();
    }

    public int getAltSkinForItem(ItemStack stack) {
        if (stack.is(ACItemRegistry.AMBER_CURIOSITY.get())) {
            return 1;
        } else {
            return stack.is(ACItemRegistry.TECTONIC_SHARD.get()) ? 2 : 0;
        }
    }

    public BlockState createEggBeddingBlockState() {
        return ACBlockRegistry.FERN_THATCH.get().defaultBlockState();
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemstack = player.m_21120_(hand);
        InteractionResult interactionresult = itemstack.interactLivingEntity(player, this, hand);
        InteractionResult type = super.m_6071_(player, hand);
        if (!interactionresult.consumesAction() && !type.consumesAction()) {
            int altSkinFromItem = this.getAltSkinForItem(itemstack);
            if (altSkinFromItem > 0) {
                this.m_142075_(player, hand, itemstack);
                this.m_216990_(altSkinFromItem == 2 ? ACSoundRegistry.TECTONIC_SHARD_TRANSFORM.get() : ACSoundRegistry.AMBER_MONOLITH_SUMMON.get());
                if (!this.m_9236_().isClientSide) {
                    if (altSkinFromItem == this.getAltSkin()) {
                        this.setAltSkin(0);
                    } else {
                        this.setAltSkin(altSkinFromItem);
                    }
                    this.m_9236_().broadcastEntityEvent(this, (byte) (altSkinFromItem > 1 ? 83 : 82));
                }
                return InteractionResult.SUCCESS;
            }
            if (this.m_21824_() && this.m_21830_(player) && !this.m_6898_(itemstack)) {
                if (this.canOwnerCommand(player)) {
                    this.setCommand(this.getCommand() + 1);
                    if (this.getCommand() == 3) {
                        this.setCommand(0);
                    }
                    player.displayClientMessage(Component.translatable("entity.alexscaves.all.command_" + this.getCommand(), this.m_7755_()), true);
                    boolean sit = this.getCommand() == 1;
                    if (sit) {
                        this.m_21839_(true);
                    } else {
                        this.m_21839_(false);
                    }
                    return InteractionResult.SUCCESS;
                }
                if (this.canOwnerMount(player)) {
                    if (this.m_6095_() == ACEntityRegistry.SUBTERRANODON.get() && this.m_7310_(player)) {
                        this.m_6027_(this.m_20185_(), this.m_20186_() + (double) player.m_20206_() + 0.5, this.m_20189_());
                    }
                    if (!this.m_9236_().isClientSide && player.m_20329_(this)) {
                        return InteractionResult.CONSUME;
                    }
                    return InteractionResult.SUCCESS;
                }
            }
        }
        return type;
    }

    @Override
    public boolean startRiding(Entity entity, boolean force) {
        boolean flag = super.m_7998_(entity, force);
        if (flag && entity instanceof AbstractMinecart) {
            List<EntityType> nearbyDinosaurEntityTypes = new ArrayList();
            double advancementRange = 30.0;
            for (DinosaurEntity dinosaur : this.m_9236_().m_45976_(DinosaurEntity.class, this.m_20191_().inflate(advancementRange, advancementRange, advancementRange))) {
                if (dinosaur.m_20201_() instanceof AbstractMinecart && !nearbyDinosaurEntityTypes.contains(dinosaur.m_6095_())) {
                    nearbyDinosaurEntityTypes.add(dinosaur.m_6095_());
                }
            }
            if (nearbyDinosaurEntityTypes.size() >= 5) {
                for (Player player : this.m_9236_().m_45976_(Player.class, this.m_20191_().inflate(advancementRange))) {
                    if ((double) player.m_20270_(this) < advancementRange) {
                        ACAdvancementTriggerRegistry.DINOSAURS_MINECART.triggerForEntity(player);
                    }
                }
            }
        }
        return flag;
    }

    @Override
    public boolean isAlliedTo(Entity entityIn) {
        if (this.m_21824_()) {
            LivingEntity livingentity = this.m_269323_();
            if (entityIn == livingentity) {
                return true;
            }
            if (entityIn instanceof TamableAnimal) {
                return ((TamableAnimal) entityIn).isOwnedBy(livingentity);
            }
            if (livingentity != null) {
                return livingentity.m_7307_(entityIn);
            }
        }
        return super.isAlliedTo(entityIn);
    }

    public boolean canOwnerMount(Player player) {
        return false;
    }

    public boolean canOwnerCommand(Player ownerPlayer) {
        return false;
    }

    @Override
    public boolean stopTickingPathing() {
        return this.isInSittingPose() || this.m_20160_() || this.isDancing();
    }
}
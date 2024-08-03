package org.violetmoon.quark.content.mobs.entity;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraftforge.common.Tags;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.violetmoon.quark.base.Quark;
import org.violetmoon.quark.base.handler.QuarkSounds;
import org.violetmoon.quark.base.util.IfFlagGoal;
import org.violetmoon.quark.content.mobs.ai.ActWaryGoal;
import org.violetmoon.quark.content.mobs.ai.FavorBlockGoal;
import org.violetmoon.quark.content.mobs.ai.RunAndPoofGoal;
import org.violetmoon.quark.content.mobs.module.StonelingsModule;
import org.violetmoon.quark.content.tools.entity.rang.Pickarang;
import org.violetmoon.quark.content.world.module.GlimmeringWealdModule;
import org.violetmoon.quark.content.world.module.NewStoneTypesModule;
import org.violetmoon.zeta.util.BlockUtils;
import org.violetmoon.zeta.util.MiscUtil;

public class Stoneling extends PathfinderMob {

    public static final ResourceLocation CARRY_LOOT_TABLE = new ResourceLocation("quark", "entities/stoneling_carry");

    private static final EntityDataAccessor<ItemStack> CARRYING_ITEM = SynchedEntityData.defineId(Stoneling.class, EntityDataSerializers.ITEM_STACK);

    private static final EntityDataAccessor<Byte> VARIANT = SynchedEntityData.defineId(Stoneling.class, EntityDataSerializers.BYTE);

    private static final EntityDataAccessor<Float> HOLD_ANGLE = SynchedEntityData.defineId(Stoneling.class, EntityDataSerializers.FLOAT);

    public static final EntityDataAccessor<Boolean> HAS_LICHEN = SynchedEntityData.defineId(Stoneling.class, EntityDataSerializers.BOOLEAN);

    private static final String TAG_CARRYING_ITEM = "carryingItem";

    private static final String TAG_VARIANT = "variant";

    private static final String TAG_HAS_LICHEN = "has_lichen";

    private static final String TAG_HOLD_ANGLE = "itemAngle";

    private static final String TAG_PLAYER_MADE = "playerMade";

    private ActWaryGoal waryGoal;

    private boolean isTame;

    public Stoneling(EntityType<? extends Stoneling> type, Level worldIn) {
        super(type, worldIn);
        this.m_21441_(BlockPathTypes.DAMAGE_OTHER, 1.0F);
        this.m_21441_(BlockPathTypes.DANGER_OTHER, 1.0F);
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        this.f_19804_.define(CARRYING_ITEM, ItemStack.EMPTY);
        this.f_19804_.define(VARIANT, (byte) 0);
        this.f_19804_.define(HOLD_ANGLE, 0.0F);
        this.f_19804_.define(HAS_LICHEN, false);
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.2, 0.98F));
        this.f_21345_.addGoal(4, new FavorBlockGoal(this, 0.2, s -> s.m_204336_(Tags.Blocks.ORES_DIAMOND)));
        this.f_21345_.addGoal(3, new IfFlagGoal(new TemptGoal(this, 0.6, Ingredient.of(this.temptTag()), false), () -> StonelingsModule.enableDiamondHeart && !StonelingsModule.tamableStonelings));
        this.f_21345_.addGoal(2, new RunAndPoofGoal(this, Player.class, 4.0F, 0.5, 0.5));
        this.f_21345_.addGoal(1, this.waryGoal = new ActWaryGoal(this, 0.1, 6.0, () -> StonelingsModule.cautiousStonelings));
        this.f_21345_.addGoal(0, new IfFlagGoal(new TemptGoal(this, 0.6, Ingredient.of(this.temptTag()), false), () -> StonelingsModule.tamableStonelings));
    }

    private TagKey<Item> temptTag() {
        return Quark.ZETA.modules.isEnabled(GlimmeringWealdModule.class) ? GlimmeringWealdModule.glowShroomFeedablesTag : Tags.Items.GEMS_DIAMOND;
    }

    public static AttributeSupplier.Builder prepareAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 8.0).add(Attributes.KNOCKBACK_RESISTANCE, 1.0);
    }

    @Override
    public void tick() {
        super.m_8119_();
        if (this.f_19798_) {
            this.m_274367_(1.0F);
        } else {
            this.m_274367_(0.6F);
        }
        this.f_20884_ = this.f_19859_;
        this.f_20883_ = this.m_146908_();
    }

    public MobCategory getClassification(boolean forSpawnCount) {
        return this.isTame ? MobCategory.CREATURE : MobCategory.MONSTER;
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return !this.isTame;
    }

    @Override
    public void checkDespawn() {
        boolean wasAlive = this.m_6084_();
        super.m_6043_();
        if (!this.m_6084_() && wasAlive) {
            for (Entity passenger : this.m_146897_()) {
                if (!(passenger instanceof Player)) {
                    passenger.discard();
                }
            }
        }
    }

    @NotNull
    @Override
    public InteractionResult mobInteract(Player player, @NotNull InteractionHand hand) {
        ItemStack stack = player.m_21120_(hand);
        return !stack.isEmpty() && stack.getItem() == Items.NAME_TAG ? stack.getItem().interactLivingEntity(stack, player, this, hand) : super.m_6071_(player, hand);
    }

    @NotNull
    @Override
    public InteractionResult interactAt(@NotNull Player player, @NotNull Vec3 vec, @NotNull InteractionHand hand) {
        if (hand == InteractionHand.MAIN_HAND && this.m_6084_()) {
            ItemStack playerItem = player.m_21120_(hand);
            Vec3 pos = this.m_20182_();
            Level level = this.m_9236_();
            if (this.isPlayerMade()) {
                if (!player.m_20163_() && !playerItem.isEmpty()) {
                    Stoneling.StonelingVariant currentVariant = this.getVariant();
                    Stoneling.StonelingVariant targetVariant = null;
                    Block targetBlock = null;
                    label67: for (Stoneling.StonelingVariant variant : Stoneling.StonelingVariant.values()) {
                        for (Block block : variant.getBlocks()) {
                            if (block.asItem() == playerItem.getItem()) {
                                targetVariant = variant;
                                targetBlock = block;
                                break label67;
                            }
                        }
                    }
                    if (targetVariant != null) {
                        if (level instanceof ServerLevel serverLevel) {
                            serverLevel.sendParticles(ParticleTypes.HEART, pos.x, pos.y + (double) this.m_20206_(), pos.z, 1, 0.1, 0.1, 0.1, 0.1);
                            if (targetVariant != currentVariant) {
                                serverLevel.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, targetBlock.defaultBlockState()), pos.x, pos.y + (double) (this.m_20206_() / 2.0F), pos.z, 16, 0.1, 0.1, 0.1, 0.25);
                            }
                        }
                        if (targetVariant != currentVariant) {
                            this.m_5496_(QuarkSounds.ENTITY_STONELING_EAT, 1.0F, 1.0F);
                            this.f_19804_.set(VARIANT, targetVariant.getIndex());
                        }
                        this.m_5496_(QuarkSounds.ENTITY_STONELING_PURR, 1.0F, 1.0F + level.random.nextFloat());
                        this.m_5634_(1.0F);
                        if (!player.getAbilities().instabuild) {
                            playerItem.shrink(1);
                        }
                        return InteractionResult.sidedSuccess(level.isClientSide);
                    }
                    return InteractionResult.PASS;
                }
                ItemStack stonelingItem = this.f_19804_.get(CARRYING_ITEM);
                if (!stonelingItem.isEmpty() || !playerItem.isEmpty()) {
                    player.m_21008_(hand, stonelingItem.copy());
                    this.f_19804_.set(CARRYING_ITEM, playerItem.copy());
                    if (playerItem.isEmpty()) {
                        this.m_5496_(QuarkSounds.ENTITY_STONELING_GIVE, 1.0F, 1.0F);
                    } else {
                        this.m_5496_(QuarkSounds.ENTITY_STONELING_TAKE, 1.0F, 1.0F);
                    }
                    return InteractionResult.sidedSuccess(level.isClientSide);
                }
            } else if (StonelingsModule.tamableStonelings && playerItem.is(this.temptTag())) {
                this.m_5634_(8.0F);
                this.setPlayerMade(true);
                this.m_5496_(QuarkSounds.ENTITY_STONELING_PURR, 1.0F, 1.0F + level.random.nextFloat());
                if (!player.getAbilities().instabuild) {
                    playerItem.shrink(1);
                }
                if (level instanceof ServerLevel serverLevelx) {
                    serverLevelx.sendParticles(ParticleTypes.HEART, pos.x, pos.y + (double) this.m_20206_(), pos.z, 4, 0.1, 0.1, 0.1, 0.1);
                }
                return InteractionResult.sidedSuccess(level.isClientSide);
            }
        }
        return InteractionResult.PASS;
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, @NotNull DifficultyInstance difficulty, @NotNull MobSpawnType spawnReason, @Nullable SpawnGroupData data, @Nullable CompoundTag compound) {
        RandomSource rand = world.m_213780_();
        byte variant;
        if (data instanceof Stoneling.StonelingVariant stonelingVariant) {
            variant = stonelingVariant.getIndex();
        } else {
            variant = (byte) rand.nextInt(Stoneling.StonelingVariant.values().length);
        }
        this.f_19804_.set(VARIANT, variant);
        this.f_19804_.set(HAS_LICHEN, world.m_204166_(this.m_20097_()).is(GlimmeringWealdModule.BIOME_NAME) && rand.nextInt(5) < 3);
        this.f_19804_.set(HOLD_ANGLE, world.m_213780_().nextFloat() * 90.0F - 45.0F);
        if (!this.isTame && !world.m_5776_()) {
            List<ItemStack> items = world.m_7654_().getLootData().m_278676_(CARRY_LOOT_TABLE).getRandomItems(new LootParams.Builder(world.getLevel()).withParameter(LootContextParams.ORIGIN, this.m_20182_()).create(LootContextParamSets.CHEST));
            if (!items.isEmpty()) {
                this.f_19804_.set(CARRYING_ITEM, (ItemStack) items.get(0));
            }
        }
        return super.m_6518_(world, difficulty, spawnReason, data, compound);
    }

    @Override
    public boolean isInvulnerableTo(@NotNull DamageSource source) {
        return this.m_269291_().cactus().equals(source) || isProjectileWithoutPiercing(source) || super.m_6673_(source);
    }

    private static boolean isProjectileWithoutPiercing(DamageSource source) {
        if (!source.isIndirect()) {
            return false;
        } else {
            Entity sourceEntity = source.getDirectEntity();
            if (sourceEntity instanceof Pickarang pickarang) {
                return pickarang.getPiercingModifier() <= 0;
            } else {
                return sourceEntity instanceof AbstractArrow arrow ? arrow.getPierceLevel() <= 0 : true;
            }
        }
    }

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    @Override
    public boolean checkSpawnObstruction(LevelReader worldReader) {
        return worldReader.m_5450_(this, Shapes.create(this.m_20191_()));
    }

    @Override
    public double getPassengersRidingOffset() {
        return (double) this.m_20206_();
    }

    @Override
    public boolean isPushedByFluid() {
        return false;
    }

    @Override
    protected int decreaseAirSupply(int air) {
        return air;
    }

    @Override
    public boolean causeFallDamage(float distance, float damageMultiplier, @NotNull DamageSource source) {
        return false;
    }

    @Override
    protected void actuallyHurt(@NotNull DamageSource damageSrc, float damageAmount) {
        super.m_6475_(damageSrc, damageAmount);
        if (!this.isPlayerMade() && damageSrc.getEntity() instanceof Player) {
            this.startle();
            for (Entity entity : this.m_9236_().m_45933_(this, this.m_20191_().inflate(16.0))) {
                if (entity instanceof Stoneling) {
                    Stoneling stoneling = (Stoneling) entity;
                    if (!stoneling.isPlayerMade() && stoneling.m_21574_().hasLineOfSight(this)) {
                        this.startle();
                    }
                }
            }
        }
    }

    public boolean isStartled() {
        return this.waryGoal.isStartled();
    }

    public void startle() {
        this.waryGoal.startle();
        for (WrappedGoal task : Sets.newHashSet(this.f_21345_.getAvailableGoals())) {
            if (task.getGoal() instanceof TemptGoal) {
                this.f_21345_.removeGoal(task.getGoal());
            }
        }
    }

    @Override
    protected void dropCustomDeathLoot(@NotNull DamageSource damage, int looting, boolean wasRecentlyHit) {
        super.m_7472_(damage, looting, wasRecentlyHit);
        ItemStack stack = this.getCarryingItem();
        if (!stack.isEmpty()) {
            this.m_5552_(stack, 0.0F);
        }
    }

    public void setPlayerMade(boolean value) {
        this.isTame = value;
    }

    public ItemStack getCarryingItem() {
        return this.f_19804_.get(CARRYING_ITEM);
    }

    public Stoneling.StonelingVariant getVariant() {
        return Stoneling.StonelingVariant.byIndex(this.f_19804_.get(VARIANT));
    }

    public float getItemAngle() {
        return this.f_19804_.get(HOLD_ANGLE);
    }

    public boolean isPlayerMade() {
        return this.isTame;
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.m_7378_(compound);
        if (compound.contains("carryingItem", 10)) {
            CompoundTag itemCmp = compound.getCompound("carryingItem");
            ItemStack stack = ItemStack.of(itemCmp);
            this.f_19804_.set(CARRYING_ITEM, stack);
        }
        this.f_19804_.set(VARIANT, compound.getByte("variant"));
        this.f_19804_.set(HOLD_ANGLE, compound.getFloat("itemAngle"));
        this.f_19804_.set(HAS_LICHEN, compound.getBoolean("has_lichen"));
        this.setPlayerMade(compound.getBoolean("playerMade"));
    }

    @Override
    public boolean hasLineOfSight(Entity entityIn) {
        Vec3 pos = this.m_20182_();
        Vec3 epos = entityIn.position();
        Vec3 origin = new Vec3(pos.x, pos.y + (double) this.m_20192_(), pos.z);
        float otherEyes = entityIn.getEyeHeight();
        for (float height = 0.0F; height <= otherEyes; height += otherEyes / 8.0F) {
            if (this.m_9236_().m_45547_(new ClipContext(origin, epos.add(0.0, (double) height, 0.0), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this)).getType() == HitResult.Type.MISS) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.m_7380_(compound);
        compound.put("carryingItem", this.getCarryingItem().serializeNBT());
        compound.putByte("variant", this.getVariant().getIndex());
        compound.putFloat("itemAngle", this.getItemAngle());
        compound.putBoolean("playerMade", this.isPlayerMade());
        compound.putBoolean("has_lichen", this.f_19804_.get(HAS_LICHEN));
    }

    public static boolean spawnPredicate(EntityType<? extends Stoneling> type, ServerLevelAccessor world, MobSpawnType reason, BlockPos pos, RandomSource rand) {
        return pos.m_123342_() <= StonelingsModule.maxYLevel && (MiscUtil.validSpawnLight(world, pos, rand) || world.m_204166_(pos).is(GlimmeringWealdModule.BIOME_NAME)) && MiscUtil.validSpawnLocation(type, world, reason, pos);
    }

    @Override
    public boolean checkSpawnRules(@NotNull LevelAccessor world, @NotNull MobSpawnType reason) {
        BlockPos pos = BlockPos.containing(this.m_20182_()).below();
        BlockState state = world.m_8055_(pos);
        return !BlockUtils.isStoneBased(state, world, pos) ? false : StonelingsModule.dimensions.canSpawnHere(world) && super.checkSpawnRules(world, reason);
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource damageSourceIn) {
        return QuarkSounds.ENTITY_STONELING_CRY;
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return QuarkSounds.ENTITY_STONELING_DIE;
    }

    @Override
    public int getAmbientSoundInterval() {
        return 1200;
    }

    @Override
    public void playAmbientSound() {
        SoundEvent sound = this.getAmbientSound();
        if (sound != null) {
            this.m_5496_(sound, this.m_6121_(), 1.0F);
        }
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        if (this.m_8077_()) {
            String customName = this.m_7755_().getString();
            if (customName.equalsIgnoreCase("michael stevens") || customName.equalsIgnoreCase("vsauce")) {
                return QuarkSounds.ENTITY_STONELING_MICHAEL;
            }
        }
        return null;
    }

    @NotNull
    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public float getWalkTargetValue(@NotNull BlockPos pos, LevelReader world) {
        return 0.5F - (float) world.m_45524_(pos, 0);
    }

    public static enum StonelingVariant implements SpawnGroupData {

        STONE("stone", Blocks.COBBLESTONE, Blocks.STONE),
        ANDESITE("andesite", Blocks.ANDESITE, Blocks.POLISHED_ANDESITE),
        DIORITE("diorite", Blocks.DIORITE, Blocks.POLISHED_DIORITE),
        GRANITE("granite", Blocks.GRANITE, Blocks.POLISHED_GRANITE),
        LIMESTONE("limestone", NewStoneTypesModule.limestoneBlock, (Block) NewStoneTypesModule.polishedBlocks.get(NewStoneTypesModule.limestoneBlock)),
        CALCITE("calcite", Blocks.CALCITE),
        SHALE("shale", NewStoneTypesModule.shaleBlock, (Block) NewStoneTypesModule.polishedBlocks.get(NewStoneTypesModule.shaleBlock)),
        JASPER("jasper", NewStoneTypesModule.jasperBlock, (Block) NewStoneTypesModule.polishedBlocks.get(NewStoneTypesModule.jasperBlock)),
        DEEPSLATE("deepslate", Blocks.DEEPSLATE, Blocks.POLISHED_DEEPSLATE),
        TUFF("tuff", Blocks.TUFF, (Block) NewStoneTypesModule.polishedBlocks.get(Blocks.TUFF)),
        DRIPSTONE("dripstone", Blocks.DRIPSTONE_BLOCK, (Block) NewStoneTypesModule.polishedBlocks.get(Blocks.DRIPSTONE_BLOCK));

        private final ResourceLocation texture;

        private final List<Block> blocks;

        private StonelingVariant(String variantPath, Block... blocks) {
            this.texture = new ResourceLocation("quark", "textures/model/entity/stoneling/" + variantPath + ".png");
            this.blocks = Lists.newArrayList(blocks);
        }

        public static Stoneling.StonelingVariant byIndex(byte index) {
            Stoneling.StonelingVariant[] values = values();
            return values[Mth.clamp(index, 0, values.length - 1)];
        }

        public byte getIndex() {
            return (byte) this.ordinal();
        }

        public ResourceLocation getTexture() {
            return this.texture;
        }

        public List<Block> getBlocks() {
            return this.blocks;
        }
    }
}
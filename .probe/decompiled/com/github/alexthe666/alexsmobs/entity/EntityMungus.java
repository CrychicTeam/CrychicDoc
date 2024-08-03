package com.github.alexthe666.alexsmobs.entity;

import com.github.alexthe666.alexsmobs.AlexsMobs;
import com.github.alexthe666.alexsmobs.config.AMConfig;
import com.github.alexthe666.alexsmobs.entity.ai.AnimalAIWanderRanged;
import com.github.alexthe666.alexsmobs.entity.ai.CreatureAITargetItems;
import com.github.alexthe666.alexsmobs.entity.ai.MungusAIAlertBunfungus;
import com.github.alexthe666.alexsmobs.entity.ai.MungusAITemptMushroom;
import com.github.alexthe666.alexsmobs.item.AMItemRegistry;
import com.github.alexthe666.alexsmobs.message.MessageMungusBiomeChange;
import com.github.alexthe666.alexsmobs.misc.AMSoundRegistry;
import com.github.alexthe666.alexsmobs.misc.AMTagRegistry;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.QuartPos;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.Shearable;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.chunk.PalettedContainer;
import net.minecraft.world.level.chunk.PalettedContainerRO;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.IForgeShearable;
import net.minecraftforge.registries.ForgeRegistries;

public class EntityMungus extends Animal implements ITargetsDroppedItems, Shearable, IForgeShearable {

    protected static final EntityDataAccessor<Optional<BlockPos>> TARGETED_BLOCK_POS = SynchedEntityData.defineId(EntityMungus.class, EntityDataSerializers.OPTIONAL_BLOCK_POS);

    private static final EntityDataAccessor<Boolean> ALT_ORDER_MUSHROOMS = SynchedEntityData.defineId(EntityMungus.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> REVERTING = SynchedEntityData.defineId(EntityMungus.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Integer> MUSHROOM_COUNT = SynchedEntityData.defineId(EntityMungus.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Integer> SACK_SWELL = SynchedEntityData.defineId(EntityMungus.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Boolean> EXPLOSION_DISABLED = SynchedEntityData.defineId(EntityMungus.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Optional<BlockState>> MUSHROOM_STATE = SynchedEntityData.defineId(EntityMungus.class, EntityDataSerializers.OPTIONAL_BLOCK_STATE);

    private static final int WIDTH_BITS = Mth.ceillog2(16) - 2;

    public static final int MAX_SIZE = 1 << WIDTH_BITS + WIDTH_BITS + DimensionType.BITS_FOR_Y - 2;

    private static final int HORIZONTAL_MASK = (1 << WIDTH_BITS) - 1;

    private static final HashMap<String, String> MUSHROOM_TO_BIOME = new HashMap();

    private static final HashMap<String, String> MUSHROOM_TO_BLOCK = new HashMap();

    private static boolean initBiomeData = false;

    public float prevSwellProgress = 0.0F;

    public float swellProgress = 0.0F;

    private int beamCounter = 0;

    private int mosquitoAttackCooldown = 0;

    private boolean hasExploded;

    public int timeUntilNextEgg = this.f_19796_.nextInt(24000) + 24000;

    protected EntityMungus(EntityType<? extends Animal> type, Level worldIn) {
        super(type, worldIn);
        initBiomeData();
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 15.0).add(Attributes.MOVEMENT_SPEED, 0.25);
    }

    public static boolean canMungusSpawn(EntityType type, LevelAccessor worldIn, MobSpawnType reason, BlockPos pos, RandomSource randomIn) {
        return worldIn.m_8055_(pos.below()).m_60815_();
    }

    public static BlockState getMushroomBlockstate(Item item) {
        if (item instanceof BlockItem) {
            ResourceLocation name = ForgeRegistries.ITEMS.getKey(item);
            if (name != null && MUSHROOM_TO_BIOME.containsKey(name.toString())) {
                return ((BlockItem) item).getBlock().defaultBlockState();
            }
        }
        return null;
    }

    private static void initBiomeData() {
        if (!initBiomeData || MUSHROOM_TO_BIOME.isEmpty()) {
            initBiomeData = true;
            for (String str : AMConfig.mungusBiomeMatches) {
                String[] split = str.split("\\|");
                if (split.length >= 2) {
                    MUSHROOM_TO_BIOME.put(split[0], split[1]);
                    MUSHROOM_TO_BLOCK.put(split[0], split[2]);
                }
            }
        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return AMSoundRegistry.MUNGUS_IDLE.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return AMSoundRegistry.MUNGUS_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return AMSoundRegistry.MUNGUS_HURT.get();
    }

    @Override
    public boolean checkSpawnRules(LevelAccessor worldIn, MobSpawnType spawnReasonIn) {
        return AMEntityRegistry.rollSpawn(AMConfig.mungusSpawnRolls, this.m_217043_(), spawnReasonIn);
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(0, new BreedGoal(this, 1.0));
        this.f_21345_.addGoal(1, new FloatGoal(this));
        this.f_21345_.addGoal(2, new PanicGoal(this, 1.25));
        this.f_21345_.addGoal(3, new MungusAITemptMushroom(this, 1.0));
        this.f_21345_.addGoal(5, new EntityMungus.AITargetMushrooms());
        this.f_21345_.addGoal(6, new FollowParentGoal(this, 1.1));
        this.f_21345_.addGoal(7, new AnimalAIWanderRanged(this, 60, 1.0, 14, 7));
        this.f_21345_.addGoal(8, new LookAtPlayerGoal(this, LivingEntity.class, 15.0F));
        this.f_21345_.addGoal(8, new RandomLookAroundGoal(this));
        this.f_21346_.addGoal(1, new CreatureAITargetItems(this, false, 10));
        this.f_21346_.addGoal(2, new MungusAIAlertBunfungus(this, EntityBunfungus.class));
    }

    @Override
    public void tick() {
        super.m_8119_();
        if (!this.m_9236_().isClientSide && this.m_6084_() && !this.m_6162_() && --this.timeUntilNextEgg <= 0) {
            ItemEntity dropped = this.m_19998_(AMItemRegistry.MUNGAL_SPORES.get());
            dropped.setDefaultPickUpDelay();
            this.timeUntilNextEgg = this.f_19796_.nextInt(24000) + 24000;
        }
    }

    @Override
    public void baseTick() {
        super.m_6075_();
        this.prevSwellProgress = this.swellProgress;
        if (this.isReverting() && AMConfig.mungusBiomeTransformationType == 2) {
            this.swellProgress += 0.5F;
            if (this.swellProgress >= 10.0F) {
                try {
                    this.explode();
                } catch (Exception var2) {
                    var2.printStackTrace();
                }
                this.swellProgress = 0.0F;
                this.f_19804_.set(REVERTING, false);
            }
        } else if (this.m_6084_() && this.swellProgress > 0.0F) {
            this.swellProgress--;
        }
        if (this.f_19804_.get(EXPLOSION_DISABLED)) {
            if (this.mosquitoAttackCooldown < 0) {
                this.mosquitoAttackCooldown++;
            }
            if (this.mosquitoAttackCooldown > 200) {
                this.mosquitoAttackCooldown = 0;
                this.f_19804_.set(EXPLOSION_DISABLED, false);
            }
        }
    }

    @Override
    protected void tickDeath() {
        super.m_6153_();
        if (this.getMushroomCount() >= 5 && AMConfig.mungusBiomeTransformationType > 0 && !this.m_6162_() && !this.f_19804_.get(EXPLOSION_DISABLED)) {
            this.swellProgress++;
            if (this.f_20919_ == 19 && !this.hasExploded) {
                this.hasExploded = true;
                try {
                    this.explode();
                } catch (Exception var2) {
                    var2.printStackTrace();
                }
            }
        }
    }

    private void explode() {
        for (int i = 0; i < 5; i++) {
            float r1 = 6.0F * (this.f_19796_.nextFloat() - 0.5F);
            float r2 = 2.0F * (this.f_19796_.nextFloat() - 0.5F);
            float r3 = 6.0F * (this.f_19796_.nextFloat() - 0.5F);
            this.m_9236_().addParticle(ParticleTypes.EXPLOSION, this.m_20185_() + (double) r1, this.m_20186_() + 0.5 + (double) r2, this.m_20189_() + (double) r3, (double) (r1 * 4.0F), (double) (r2 * 4.0F), (double) (r3 * 4.0F));
        }
        if (!this.m_9236_().isClientSide) {
            ServerLevel serverLevel = (ServerLevel) this.m_9236_();
            int radius = 3;
            int j = 3 + this.m_9236_().random.nextInt(1);
            int k = 3 + this.m_9236_().random.nextInt(1);
            int l = 3 + this.m_9236_().random.nextInt(1);
            float f = (float) (j + k + l) * 0.333F + 0.5F;
            float ff = f * f;
            double ffDouble = (double) ff;
            BlockPos center = this.m_20183_();
            BlockState transformState = Blocks.MYCELIUM.defaultBlockState();
            Registry<Biome> registry = serverLevel.getServer().registryAccess().m_175515_(Registries.BIOME);
            Holder<Biome> biome = (Holder<Biome>) registry.getHolder(Biomes.MUSHROOM_FIELDS).get();
            TagKey<Block> transformMatches = AMTagRegistry.MUNGUS_REPLACE_MUSHROOM;
            if (this.getMushroomState() != null) {
                String mushroomKey = ForgeRegistries.BLOCKS.getKey(this.getMushroomState().m_60734_()).toString();
                if (MUSHROOM_TO_BLOCK.containsKey(mushroomKey)) {
                    Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation((String) MUSHROOM_TO_BLOCK.get(mushroomKey)));
                    if (block != null) {
                        transformState = block.defaultBlockState();
                        if (block == Blocks.WARPED_NYLIUM) {
                            transformMatches = AMTagRegistry.MUNGUS_REPLACE_NETHER;
                        }
                        if (block == Blocks.CRIMSON_NYLIUM) {
                            transformMatches = AMTagRegistry.MUNGUS_REPLACE_NETHER;
                        }
                    }
                }
                Holder<Biome> gottenFrom = this.getBiomeKeyFromShroom();
                if (gottenFrom != null) {
                    biome = gottenFrom;
                }
            }
            BlockState finalTransformState = transformState;
            TagKey<Block> finalTransformReplace = transformMatches;
            if (AMConfig.mungusBiomeTransformationType == 2 && !this.m_9236_().isClientSide) {
                this.transformBiome(center, biome);
            }
            this.m_146850_(GameEvent.EXPLODE);
            this.m_5496_(SoundEvents.GENERIC_EXPLODE, this.m_6121_(), this.m_6100_());
            if (!this.isReverting()) {
                BlockPos.betweenClosedStream(center.offset(-j, -k, -l), center.offset(j, k, l)).forEach(blockpos -> {
                    if (blockpos.m_123331_(center) <= ffDouble && this.m_9236_().random.nextFloat() > (float) blockpos.m_123331_(center) / ff) {
                        if (this.m_9236_().getBlockState(blockpos).m_204336_(finalTransformReplace) && !this.m_9236_().getBlockState(blockpos.above()).m_60815_()) {
                            this.m_9236_().setBlockAndUpdate(blockpos, finalTransformState);
                        }
                        if (this.m_9236_().random.nextInt(4) == 0 && this.m_9236_().getBlockState(blockpos).m_280296_() && this.m_9236_().getFluidState(blockpos.above()).isEmpty() && !this.m_9236_().getBlockState(blockpos.above()).m_60815_()) {
                            this.m_9236_().setBlockAndUpdate(blockpos.above(), this.getMushroomState());
                        }
                    }
                });
            }
        }
    }

    public void disableExplosion() {
        this.f_19804_.set(EXPLOSION_DISABLED, true);
    }

    private Holder<Biome> getBiomeKeyFromShroom() {
        Registry<Biome> registry = this.m_9236_().registryAccess().registryOrThrow(Registries.BIOME);
        BlockState state = this.getMushroomState();
        if (state == null) {
            return null;
        } else {
            ResourceLocation blockRegName = ForgeRegistries.BLOCKS.getKey(state.m_60734_());
            if (blockRegName != null && MUSHROOM_TO_BIOME.containsKey(blockRegName.toString())) {
                String str = (String) MUSHROOM_TO_BIOME.get(blockRegName.toString());
                Biome biome = (Biome) registry.getOptional(new ResourceLocation(str)).orElse(null);
                ResourceKey<Biome> resourceKey = (ResourceKey<Biome>) registry.getResourceKey(biome).orElse(null);
                return (Holder<Biome>) registry.getHolder(resourceKey).orElse(null);
            } else {
                return null;
            }
        }
    }

    private PalettedContainerRO<Holder<Biome>> getChunkBiomes(LevelChunk chunk) {
        int i = QuartPos.fromBlock(chunk.m_141937_());
        int k = i + QuartPos.fromBlock(chunk.m_141928_()) - 1;
        int l = Mth.clamp(QuartPos.fromBlock((int) this.m_20186_()), i, k);
        int j = chunk.m_151564_(QuartPos.toBlock(l));
        LevelChunkSection section = chunk.m_183278_(j);
        return section == null ? null : section.getBiomes();
    }

    private void setChunkBiomes(LevelChunk chunk, PalettedContainer<Holder<Biome>> container) {
        int i = QuartPos.fromBlock(chunk.m_141937_());
        int k = i + QuartPos.fromBlock(chunk.m_141928_()) - 1;
        int l = Mth.clamp(QuartPos.fromBlock((int) this.m_20186_()), i, k);
        int j = chunk.m_151564_(QuartPos.toBlock(l));
        LevelChunkSection section = chunk.m_183278_(j);
        if (section != null) {
            section.biomes = container;
        }
    }

    private void transformBiome(BlockPos pos, Holder<Biome> biome) {
        LevelChunk chunk = this.m_9236_().getChunkAt(pos);
        PalettedContainer<Holder<Biome>> container = this.getChunkBiomes(chunk).recreate();
        if (this.f_19804_.get(REVERTING)) {
            int lvt_4_1_ = chunk.m_7697_().getMinBlockX() >> 2;
            int yChunk = (int) this.m_20186_() >> 2;
            int lvt_5_1_ = chunk.m_7697_().getMinBlockZ() >> 2;
            ChunkGenerator chunkgenerator = ((ServerLevel) this.m_9236_()).getChunkSource().getGenerator();
            for (int k = 0; k < 4; k++) {
                for (int l = 0; l < 4; l++) {
                    for (int i1 = 0; i1 < 4; i1++) {
                        container.getAndSetUnchecked(k, l, i1, ((ServerLevel) this.m_9236_()).getUncachedNoiseBiome(lvt_4_1_ + k, yChunk + l, lvt_5_1_ + i1));
                    }
                }
            }
            this.setChunkBiomes(chunk, container);
            if (!this.m_9236_().isClientSide) {
            }
        } else {
            if (biome == null) {
                return;
            }
            if (container != null && !this.m_9236_().isClientSide) {
                for (int biomeX = 0; biomeX < 4; biomeX++) {
                    for (int biomeY = 0; biomeY < 4; biomeY++) {
                        for (int biomeZ = 0; biomeZ < 4; biomeZ++) {
                            container.getAndSetUnchecked(biomeX, biomeY, biomeZ, biome);
                        }
                    }
                }
                this.setChunkBiomes(chunk, container);
                ResourceLocation biomeKey = ForgeRegistries.BIOMES.getKey(biome.value());
                if (biomeKey != null) {
                    AlexsMobs.sendMSGToAll(new MessageMungusBiomeChange(this.m_19879_(), pos.m_123341_(), pos.m_123343_(), biomeKey.toString()));
                }
            }
        }
    }

    public boolean shouldFollowMushroom(ItemStack stack) {
        BlockState state = getMushroomBlockstate(stack.getItem());
        if (state == null || state.m_60795_()) {
            return false;
        } else {
            return this.getMushroomCount() == 0 ? true : this.getMushroomState().m_60734_() == state.m_60734_();
        }
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemstack = player.m_21120_(hand);
        InteractionResult type = super.mobInteract(player, hand);
        if (itemstack.getItem() == Items.POISONOUS_POTATO && !this.m_6162_()) {
            this.f_19804_.set(REVERTING, true);
            this.m_142075_(player, hand, itemstack);
            return InteractionResult.SUCCESS;
        } else if (this.shouldFollowMushroom(itemstack) && this.getMushroomCount() < 5) {
            this.f_19804_.set(REVERTING, false);
            BlockState state = getMushroomBlockstate(itemstack.getItem());
            this.m_146850_(GameEvent.BLOCK_PLACE);
            this.m_5496_(SoundEvents.FUNGUS_PLACE, this.m_6121_(), this.m_6100_());
            if (this.getMushroomState() != null && state != null && state.m_60734_() != this.getMushroomState().m_60734_()) {
                this.setMushroomCount(0);
            }
            this.setMushroomState(state);
            this.m_142075_(player, hand, itemstack);
            this.setMushroomCount(this.getMushroomCount() + 1);
            return InteractionResult.SUCCESS;
        } else {
            return type;
        }
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        boolean prev = super.hurt(source, amount);
        if (prev) {
            this.setBeamTarget(null);
            this.beamCounter = Math.min(this.beamCounter, -1200);
        }
        return prev;
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        this.f_19804_.define(MUSHROOM_STATE, Optional.empty());
        this.m_20088_().define(TARGETED_BLOCK_POS, Optional.empty());
        this.f_19804_.define(ALT_ORDER_MUSHROOMS, false);
        this.f_19804_.define(REVERTING, false);
        this.f_19804_.define(EXPLOSION_DISABLED, false);
        this.f_19804_.define(MUSHROOM_COUNT, 0);
        this.f_19804_.define(SACK_SWELL, 0);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        BlockState blockstate = this.getMushroomState();
        if (blockstate != null) {
            compound.put("MushroomState", NbtUtils.writeBlockState(blockstate));
        }
        compound.putInt("MushroomCount", this.getMushroomCount());
        compound.putInt("Sack", this.getSackSwell());
        compound.putInt("BeamCounter", this.beamCounter);
        compound.putBoolean("AltMush", this.f_19804_.get(ALT_ORDER_MUSHROOMS));
        if (this.getBeamTarget() != null) {
            compound.put("BeamTarget", NbtUtils.writeBlockPos(this.getBeamTarget()));
        }
        compound.putInt("EggTime", this.timeUntilNextEgg);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        BlockState blockstate = null;
        if (compound.contains("MushroomState", 10)) {
            blockstate = NbtUtils.readBlockState(this.m_9236_().m_246945_(Registries.BLOCK), compound.getCompound("MushroomState"));
            if (blockstate.m_60795_()) {
                blockstate = null;
            }
        }
        if (compound.contains("BeamTarget", 10)) {
            this.setBeamTarget(NbtUtils.readBlockPos(compound.getCompound("BeamTarget")));
        }
        this.setMushroomState(blockstate);
        this.setMushroomCount(compound.getInt("MushroomCount"));
        this.setSackSwell(compound.getInt("Sack"));
        this.beamCounter = compound.getInt("BeamCounter");
        this.f_19804_.set(ALT_ORDER_MUSHROOMS, compound.getBoolean("AltMush"));
        if (compound.contains("EggTime")) {
            this.timeUntilNextEgg = compound.getInt("EggTime");
        }
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (this.getBeamTarget() != null) {
            BlockPos t = this.getBeamTarget();
            if (this.isMushroomTarget(t) && this.hasLineOfSightMushroom(t)) {
                this.m_21563_().setLookAt((double) ((float) t.m_123341_() + 0.5F), (double) ((float) t.m_123342_() + 0.15F), (double) ((float) t.m_123343_() + 0.5F), 90.0F, 90.0F);
                this.m_21563_().tick();
                double d5 = 1.0;
                double eyeHeight = this.m_20186_() + 1.0;
                if (this.beamCounter % 20 == 0) {
                    this.m_5496_(AMSoundRegistry.MUNGUS_LASER_LOOP.get(), this.m_6100_(), this.m_6121_());
                }
                this.beamCounter++;
                double d0 = (double) ((float) t.m_123341_() + 0.5F) - this.m_20185_();
                double d1 = (double) ((float) t.m_123342_() + 0.5F) - eyeHeight;
                double d2 = (double) ((float) t.m_123343_() + 0.5F) - this.m_20189_();
                double d3 = Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
                d0 /= d3;
                d1 /= d3;
                d2 /= d3;
                double d4 = this.f_19796_.nextDouble();
                while (d4 < d3 - 0.5) {
                    d4 += 1.0 - d5 + this.f_19796_.nextDouble();
                    if (this.f_19796_.nextFloat() < 0.1F) {
                        float r1 = 0.3F * (this.f_19796_.nextFloat() - 0.5F);
                        float r2 = 0.3F * (this.f_19796_.nextFloat() - 0.5F);
                        float r3 = 0.3F * (this.f_19796_.nextFloat() - 0.5F);
                        this.m_9236_().addParticle(ParticleTypes.MYCELIUM, this.m_20185_() + d0 * d4 + (double) r1, eyeHeight + d1 * d4 + (double) r2, this.m_20189_() + d2 * d4 + (double) r3, (double) (r1 * 4.0F), (double) (r2 * 4.0F), (double) (r3 * 4.0F));
                    }
                }
                if (this.beamCounter > 200) {
                    BlockState state = this.m_9236_().getBlockState(t);
                    if (state.m_60734_() instanceof BonemealableBlock) {
                        BonemealableBlock igrowable = (BonemealableBlock) state.m_60734_();
                        boolean flag = false;
                        if (igrowable.isValidBonemealTarget(this.m_9236_(), t, state, this.m_9236_().isClientSide)) {
                            for (int i = 0; i < 5; i++) {
                                float r1 = 3.0F * (this.f_19796_.nextFloat() - 0.5F);
                                float r2 = 2.0F * (this.f_19796_.nextFloat() - 0.5F);
                                float r3 = 3.0F * (this.f_19796_.nextFloat() - 0.5F);
                                this.m_9236_().addParticle(ParticleTypes.EXPLOSION, (double) ((float) t.m_123341_() + 0.5F + r1), (double) ((float) t.m_123342_() + 0.5F + r2), (double) ((float) t.m_123343_() + 0.5F + r3), (double) (r1 * 4.0F), (double) (r2 * 4.0F), (double) (r3 * 4.0F));
                            }
                            if (!this.m_9236_().isClientSide) {
                                this.m_9236_().m_46796_(2005, t, 0);
                                igrowable.performBonemeal((ServerLevel) this.m_9236_(), this.m_9236_().random, t, state);
                                flag = this.m_9236_().getBlockState(t).m_60734_() != state.m_60734_();
                            }
                        }
                        if (!flag) {
                            int grown = 0;
                            int maxGrow = 2 + this.f_19796_.nextInt(3);
                            for (int i = 0; i < 15; i++) {
                                BlockPos pos = t.offset(this.f_19796_.nextInt(10) - 5, this.f_19796_.nextInt(4) - 2, this.f_19796_.nextInt(10) - 5);
                                if (grown < maxGrow && this.m_9236_().getBlockState(pos).m_60795_() && this.m_9236_().getBlockState(pos.below()).m_60815_()) {
                                    this.m_9236_().setBlockAndUpdate(pos, state);
                                    grown++;
                                }
                            }
                        }
                        this.m_5496_(AMSoundRegistry.MUNGUS_LASER_END.get(), this.m_6100_(), this.m_6121_());
                        if (flag) {
                            this.m_5496_(AMSoundRegistry.MUNGUS_LASER_GROW.get(), this.m_6100_(), this.m_6121_());
                        }
                        this.setBeamTarget(null);
                        this.beamCounter = -1200;
                        if (this.getMushroomCount() > 0) {
                            this.setMushroomCount(this.getMushroomCount() - 1);
                        }
                    }
                }
            } else {
                this.setBeamTarget(null);
            }
        }
        if (this.beamCounter < 0) {
            this.beamCounter++;
        }
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return stack.getItem() == AMItemRegistry.MUNGAL_SPORES.get();
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
        this.f_19804_.set(ALT_ORDER_MUSHROOMS, this.f_19796_.nextBoolean());
        this.setMushroomCount(this.f_19796_.nextInt(2));
        this.setMushroomState(this.f_19796_.nextBoolean() ? Blocks.BROWN_MUSHROOM.defaultBlockState() : Blocks.RED_MUSHROOM.defaultBlockState());
        return super.m_6518_(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    public int getMushroomCount() {
        return this.f_19804_.get(MUSHROOM_COUNT);
    }

    public void setMushroomCount(int command) {
        this.f_19804_.set(MUSHROOM_COUNT, command);
    }

    public int getSackSwell() {
        return this.f_19804_.get(SACK_SWELL);
    }

    public void setSackSwell(int command) {
        this.f_19804_.set(SACK_SWELL, command);
    }

    @Nullable
    public BlockPos getBeamTarget() {
        return (BlockPos) this.m_20088_().get(TARGETED_BLOCK_POS).orElse(null);
    }

    public void setBeamTarget(@Nullable BlockPos beamTarget) {
        this.m_20088_().set(TARGETED_BLOCK_POS, Optional.ofNullable(beamTarget));
    }

    public boolean isAltOrderMushroom() {
        return this.f_19804_.get(ALT_ORDER_MUSHROOMS);
    }

    @Nullable
    public BlockState getMushroomState() {
        return (BlockState) this.f_19804_.get(MUSHROOM_STATE).orElse(null);
    }

    public void setMushroomState(@Nullable BlockState state) {
        this.f_19804_.set(MUSHROOM_STATE, Optional.ofNullable(state));
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel p_241840_1_, AgeableMob p_241840_2_) {
        return AMEntityRegistry.MUNGUS.get().create(p_241840_1_);
    }

    public boolean isMushroomTarget(BlockPos pos) {
        return this.getMushroomState() != null ? this.m_9236_().getBlockState(pos).m_60734_() == this.getMushroomState().m_60734_() : false;
    }

    @Override
    public boolean canTargetItem(ItemStack stack) {
        return this.shouldFollowMushroom(stack) && this.getMushroomCount() < 5;
    }

    @Override
    public void onGetItem(ItemEntity e) {
        if (this.shouldFollowMushroom(e.getItem())) {
            BlockState state = getMushroomBlockstate(e.getItem().getItem());
            if (this.getMushroomState() != null && state != null && state.m_60734_() != this.getMushroomState().m_60734_()) {
                this.setMushroomCount(0);
            }
            this.m_146850_(GameEvent.BLOCK_PLACE);
            this.m_5496_(SoundEvents.FUNGUS_PLACE, this.m_6121_(), this.m_6100_());
            this.setMushroomState(state);
            this.setMushroomCount(this.getMushroomCount() + 1);
        }
    }

    private boolean hasLineOfSightMushroom(BlockPos destinationBlock) {
        Vec3 Vector3d = new Vec3(this.m_20185_(), this.m_20188_(), this.m_20189_());
        Vec3 blockVec = Vec3.atCenterOf(destinationBlock);
        BlockHitResult result = this.m_9236_().m_45547_(new ClipContext(Vector3d, blockVec, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
        return result.getBlockPos().equals(destinationBlock);
    }

    @Override
    public boolean readyForShearing() {
        return this.m_6084_() && this.getMushroomState() != null && this.getMushroomCount() > 0;
    }

    @Override
    public boolean isShearable(@Nonnull ItemStack item, Level world, BlockPos pos) {
        return this.readyForShearing();
    }

    @Override
    public void shear(SoundSource category) {
        this.m_146850_(GameEvent.ENTITY_INTERACT);
        this.m_9236_().playSound(null, this, SoundEvents.SHEEP_SHEAR, category, 1.0F, 1.0F);
        if (!this.m_9236_().isClientSide() && this.getMushroomState() != null && this.getMushroomCount() > 0) {
            this.setMushroomCount(this.getMushroomCount() - 1);
            if (this.getMushroomCount() <= 0) {
                this.setMushroomState(null);
                this.setBeamTarget(null);
                this.beamCounter = Math.min(-1200, this.beamCounter);
            }
        }
    }

    @Nonnull
    @Override
    public List<ItemStack> onSheared(@Nullable Player player, @Nonnull ItemStack item, Level world, BlockPos pos, int fortune) {
        world.playSound(null, this, SoundEvents.SHEEP_SHEAR, player == null ? SoundSource.BLOCKS : SoundSource.PLAYERS, 1.0F, 1.0F);
        if (!world.isClientSide() && this.getMushroomState() != null && this.getMushroomCount() > 0) {
            this.setMushroomCount(this.getMushroomCount() - 1);
            if (this.getMushroomCount() <= 0) {
                this.setMushroomState(null);
                this.setBeamTarget(null);
                this.beamCounter = Math.min(-1200, this.beamCounter);
            }
        }
        return Collections.emptyList();
    }

    public boolean isReverting() {
        return this.f_19804_.get(REVERTING);
    }

    public boolean isWarpedMoscoReady() {
        return this.getMushroomState() == Blocks.WARPED_FUNGUS.defaultBlockState() && this.getMushroomCount() >= 5;
    }

    class AITargetMushrooms extends Goal {

        private final int searchLength;

        protected BlockPos destinationBlock;

        protected int runDelay = 70;

        private AITargetMushrooms() {
            this.searchLength = 20;
        }

        @Override
        public boolean canContinueToUse() {
            return this.destinationBlock != null && EntityMungus.this.isMushroomTarget(this.destinationBlock.mutable()) && this.isCloseToShroom(32.0);
        }

        public boolean isCloseToShroom(double dist) {
            return this.destinationBlock == null || EntityMungus.this.m_20238_(Vec3.atCenterOf(this.destinationBlock)) < dist * dist;
        }

        @Override
        public boolean canUse() {
            if (EntityMungus.this.getBeamTarget() != null || EntityMungus.this.beamCounter < 0 || EntityMungus.this.getMushroomCount() <= 0) {
                return false;
            } else if (this.runDelay > 0) {
                this.runDelay--;
                return false;
            } else {
                this.runDelay = 70 + EntityMungus.this.f_19796_.nextInt(150);
                return this.searchForDestination();
            }
        }

        @Override
        public void start() {
        }

        @Override
        public void tick() {
            if (this.destinationBlock == null || !EntityMungus.this.isMushroomTarget(this.destinationBlock) || EntityMungus.this.beamCounter < 0) {
                this.stop();
            } else if (!EntityMungus.this.hasLineOfSightMushroom(this.destinationBlock)) {
                EntityMungus.this.m_21573_().moveTo((double) this.destinationBlock.m_123341_(), (double) this.destinationBlock.m_123342_(), (double) this.destinationBlock.m_123343_(), 1.0);
            } else {
                EntityMungus.this.setBeamTarget(this.destinationBlock);
                if (!EntityMungus.this.m_27593_()) {
                    EntityMungus.this.m_21573_().stop();
                }
            }
        }

        @Override
        public void stop() {
            EntityMungus.this.setBeamTarget(null);
        }

        protected boolean searchForDestination() {
            int lvt_1_1_ = this.searchLength;
            BlockPos lvt_3_1_ = EntityMungus.this.m_20183_();
            BlockPos.MutableBlockPos lvt_4_1_ = new BlockPos.MutableBlockPos();
            for (int lvt_5_1_ = -5; lvt_5_1_ <= 5; lvt_5_1_++) {
                for (int lvt_6_1_ = 0; lvt_6_1_ < lvt_1_1_; lvt_6_1_++) {
                    for (int lvt_7_1_ = 0; lvt_7_1_ <= lvt_6_1_; lvt_7_1_ = lvt_7_1_ > 0 ? -lvt_7_1_ : 1 - lvt_7_1_) {
                        for (int lvt_8_1_ = lvt_7_1_ < lvt_6_1_ && lvt_7_1_ > -lvt_6_1_ ? lvt_6_1_ : 0; lvt_8_1_ <= lvt_6_1_; lvt_8_1_ = lvt_8_1_ > 0 ? -lvt_8_1_ : 1 - lvt_8_1_) {
                            lvt_4_1_.setWithOffset(lvt_3_1_, lvt_7_1_, lvt_5_1_ - 1, lvt_8_1_);
                            if (this.isMushroom(EntityMungus.this.m_9236_(), lvt_4_1_)) {
                                this.destinationBlock = lvt_4_1_;
                                return true;
                            }
                        }
                    }
                }
            }
            return false;
        }

        private boolean isMushroom(Level world, BlockPos.MutableBlockPos lvt_4_1_) {
            return EntityMungus.this.isMushroomTarget(lvt_4_1_);
        }
    }
}
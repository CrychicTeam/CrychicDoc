package se.mickelus.tetra.blocks.forged.chthonic;

import java.util.Optional;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.registries.ObjectHolder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import se.mickelus.mutil.util.CastOptional;
import se.mickelus.tetra.blocks.forged.extractor.SeepingBedrockBlock;

@ParametersAreNonnullByDefault
public class FracturedBedrockTile extends BlockEntity {

    public static final TagKey<Block> extractorBreakable = BlockTags.create(new ResourceLocation("tetra", "extractor_breakable"));

    private static final Logger logger = LogManager.getLogger();

    private static final String activityKey = "actv";

    private static final String stepKey = "step";

    private static final String luckKey = "luck";

    private static final ResourceLocation[] lootTables = new ResourceLocation[] { new ResourceLocation("tetra", "extractor/tier1"), new ResourceLocation("tetra", "extractor/tier2"), new ResourceLocation("tetra", "extractor/tier3"), new ResourceLocation("tetra", "extractor/tier4") };

    @ObjectHolder(registryName = "block_entity_type", value = "tetra:fractured_bedrock")
    public static BlockEntityType<FracturedBedrockTile> type;

    private final float spawnRatio = 0.5F;

    private final int spawnYLimit = 4;

    private int activity = 0;

    private int step = 0;

    private int luck = 0;

    private MobSpawnSettings spawnInfo;

    public FracturedBedrockTile(BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    public static boolean breakBlock(Level world, BlockPos pos, BlockState blockState) {
        if (world instanceof ServerLevel serverLevel && !blockState.m_60795_() && blockState.m_204336_(extractorBreakable) && blockState.m_60800_(world, pos) > -1.0F) {
            BlockEntity tile = blockState.m_60734_() instanceof EntityBlock ? world.getBlockEntity(pos) : null;
            LootParams.Builder lootBuilder = new LootParams.Builder(serverLevel).withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(pos)).withParameter(LootContextParams.TOOL, ItemStack.EMPTY).withOptionalParameter(LootContextParams.BLOCK_ENTITY, tile).withOptionalParameter(LootContextParams.THIS_ENTITY, null);
            blockState.m_287290_(lootBuilder).forEach(itemStack -> Block.popResource(world, pos, itemStack));
            world.m_5898_(null, 2001, pos, Block.getId(world.getBlockState(pos)));
            world.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
            return true;
        }
        return false;
    }

    public void updateLuck() {
        if (this.spawnInfo == null) {
            this.spawnInfo = ((Biome) this.f_58857_.m_204166_(this.f_58858_).value()).getMobSettings();
        }
        boolean spawnBonus = this.spawnInfo.getMobs(MobCategory.MONSTER).unwrap().stream().map(spawner -> spawner.type).anyMatch(type -> EntityType.HUSK.equals(type) || EntityType.STRAY.equals(type) || EntityType.WITCH.equals(type));
        if (spawnBonus) {
            this.luck++;
        }
    }

    public void activate(int amount) {
        if (!this.f_58857_.isClientSide && this.activity <= 0) {
            this.playSound();
        }
        int preTier = this.getProjectedTier();
        this.activity += amount;
        this.m_6596_();
        if (!this.f_58857_.isClientSide && this.getProjectedTier() != preTier) {
            this.f_58857_.sendBlockUpdated(this.f_58858_, this.m_58900_(), this.m_58900_(), 3);
        }
    }

    private int getRate() {
        return 20 - Mth.clamp(this.activity / 64 * 5, 0, 15);
    }

    private int getIntensity() {
        return Math.min(1 + this.activity / 16, 4);
    }

    public int getProjectedTier() {
        return this.getTier(this.step + this.activity);
    }

    private int getTier() {
        return this.getTier(this.step);
    }

    private int getTier(int ref) {
        if (ref > 10240) {
            return 3;
        } else if (ref > 4096) {
            return 2;
        } else {
            return ref > 1024 ? 1 : 0;
        }
    }

    private int getMaxDistance() {
        switch(this.getTier()) {
            case 0:
                return 12;
            case 1:
                return 16;
            case 2:
                return 20;
            case 3:
                return 25;
            default:
                return 25;
        }
    }

    private boolean shouldDeplete() {
        return this.step >= 12288;
    }

    private Vec3 getTarget(int i) {
        int maxDistance = this.getMaxDistance();
        int steps = 32;
        double directionRotation = 90.0 * (double) (i % 4);
        double offsetRotation = 360.0 / (double) steps * (double) (i / 4) + (double) ((float) i / 8.0F);
        float pitch = (float) (-(i % (steps * 16)) / steps) * 5.0F;
        return Vec3.directionFromRotation(pitch, (float) (directionRotation + offsetRotation)).multiply((double) maxDistance, (double) (4.0F + (float) maxDistance / 2.0F), (double) maxDistance);
    }

    private boolean isBedrock(Block block) {
        return Blocks.BEDROCK.equals(block) || FracturedBedrockBlock.instance.equals(block) || SeepingBedrockBlock.instance.equals(block) || ChthonicExtractorBlock.instance.equals(block);
    }

    private boolean canReplace(BlockState blockState) {
        return blockState.m_204336_(BlockTags.REPLACEABLE);
    }

    private BlockHitResult raytrace(Level level, Vec3 origin, Vec3 target) {
        return BlockGetter.traverseBlocks(origin, target, new ClipContext(origin, target, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, null), (ctx, blockPos) -> {
            BlockState blockState = level.getBlockState(blockPos);
            Block block = blockState.m_60734_();
            if (!this.isBedrock(block) && !this.canReplace(blockState)) {
                VoxelShape voxelshape = ctx.getBlockShape(blockState, level, blockPos);
                return level.m_45558_(origin, target, blockPos, voxelshape, blockState);
            } else {
                return null;
            }
        }, ctx -> {
            Vec3 vec3 = ctx.getFrom().subtract(ctx.getTo());
            return BlockHitResult.miss(ctx.getTo(), Direction.getNearest(vec3.x, vec3.y, vec3.z), BlockPos.containing(ctx.getTo()));
        });
    }

    private BlockPos traceDown(BlockPos blockPos, int minY, Level level) {
        BlockPos.MutableBlockPos movePos = blockPos.mutable();
        while (movePos.m_123342_() > minY) {
            movePos.move(Direction.DOWN);
            BlockState blockState = level.getBlockState(movePos);
            if (this.isBedrock(blockState.m_60734_())) {
                return movePos.move(Direction.UP).immutable();
            }
            if (!blockState.m_60795_()) {
                return movePos.immutable();
            }
        }
        return movePos.immutable();
    }

    private void spawnOre(BlockPos pos) {
        ServerLevel serverWorld = (ServerLevel) this.f_58857_;
        LootTable table = serverWorld.getServer().getLootData().m_278676_(lootTables[this.getTier()]);
        LootParams context = new LootParams.Builder(serverWorld).withLuck((float) this.luck).create(LootContextParamSets.EMPTY);
        table.getRandomItems(context).stream().filter(itemStack -> !itemStack.isEmpty()).findAny().ifPresent(itemStack -> {
            if (itemStack.getItem() instanceof BlockItem blockItem) {
                BlockState newState = blockItem.getBlock().defaultBlockState();
                this.f_58857_.m_5898_(null, 2001, pos, Block.getId(newState));
                this.f_58857_.setBlock(pos, newState, 2);
            } else {
                Block.popResource(this.f_58857_, pos, itemStack);
            }
        });
    }

    private void spawnMob(BlockPos pos) {
        if (this.spawnInfo == null) {
            this.spawnInfo = ((Biome) this.f_58857_.m_204166_(pos).value()).getMobSettings();
        }
        if (!(this.m_58899_().m_123331_(pos) < 42.0) && !(this.f_58857_.getRandom().nextFloat() >= this.spawnInfo.getCreatureProbability() / 4.0F)) {
            WeightedRandomList<MobSpawnSettings.SpawnerData> spawners = this.spawnInfo.getMobs(MobCategory.MONSTER);
            Optional<MobSpawnSettings.SpawnerData> optionalSpawnerData = spawners.getRandom(this.f_58857_.getRandom());
            if (optionalSpawnerData.isPresent()) {
                MobSpawnSettings.SpawnerData mob = (MobSpawnSettings.SpawnerData) optionalSpawnerData.get();
                Vec3 spawnPos = Vec3.atBottomCenterOf(pos);
                ServerLevel serverWorld = (ServerLevel) this.f_58857_;
                if (mob.type.canSummon() && serverWorld.m_45772_(mob.type.getAABB(spawnPos.x, spawnPos.y, spawnPos.z)) && SpawnPlacements.checkSpawnRules(mob.type, serverWorld, MobSpawnType.SPAWNER, pos, serverWorld.m_213780_())) {
                    Entity entity;
                    try {
                        entity = mob.type.create(serverWorld.getLevel());
                    } catch (Exception var9) {
                        logger.warn("Failed to create mob", var9);
                        return;
                    }
                    entity.moveTo(spawnPos);
                    CastOptional.cast(entity, Mob.class).filter(e -> e.checkSpawnRules(serverWorld, MobSpawnType.SPAWNER)).filter(e -> e.checkSpawnObstruction(serverWorld)).ifPresent(e -> {
                        e.finalizeSpawn(serverWorld, serverWorld.m_6436_(e.m_20183_()), MobSpawnType.SPAWNER, null, null);
                        serverWorld.m_47205_(e);
                        serverWorld.m_45976_(Player.class, new AABB(this.m_58899_()).inflate(24.0, 8.0, 24.0)).stream().findAny().ifPresent(e::m_21335_);
                    });
                }
            }
        }
    }

    private void playSound() {
        this.f_58857_.playSound(null, this.f_58858_.below(this.f_58858_.m_123342_()), SoundEvents.BEACON_AMBIENT, SoundSource.BLOCKS, 3.0F, 0.5F);
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        if (compound.contains("actv")) {
            this.activity = compound.getInt("actv");
        }
        if (compound.contains("step")) {
            this.step = compound.getInt("step");
        }
        if (compound.contains("luck")) {
            this.luck = compound.getInt("luck");
        }
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        compound.putInt("actv", this.activity);
        compound.putInt("step", this.step);
        compound.putInt("luck", this.luck);
    }

    @Nullable
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return this.m_187482_();
    }

    public void deserializeNBT(CompoundTag nbt) {
        super.deserializeNBT(nbt);
    }

    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket packet) {
        this.load(packet.getTag());
    }

    public void tick(Level level, BlockPos pos, BlockState state) {
        if (!level.isClientSide && this.activity > 0 && level.getGameTime() % (long) this.getRate() == 0L) {
            int intensity = this.getIntensity();
            Vec3 origin = Vec3.atCenterOf(pos);
            for (int i = 0; i < intensity; i++) {
                Vec3 target = this.getTarget(this.step + i);
                BlockHitResult result = this.raytrace(level, origin, origin.add(target));
                if (result.getType() != HitResult.Type.MISS) {
                    BlockPos hitPos = result.getBlockPos();
                    BlockState hitState = level.getBlockState(hitPos);
                    breakBlock(level, hitPos, hitState);
                    int minY = level.m_141937_();
                    BlockPos spawnPos = this.traceDown(hitPos, minY, level);
                    BlockState spawnState = level.getBlockState(spawnPos);
                    if (this.canReplace(spawnState)) {
                        if (level.getRandom().nextFloat() < 0.5F) {
                            if (spawnPos.m_123342_() < minY + 4) {
                                this.spawnOre(spawnPos);
                            }
                        } else {
                            this.spawnMob(spawnPos);
                        }
                    } else {
                        breakBlock(level, spawnPos, spawnState);
                    }
                }
            }
            ((ServerLevel) level).sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, FracturedBedrockBlock.instance.m_49966_()), (double) this.f_58858_.m_123341_() + 0.5, (double) this.f_58858_.m_123342_() + 1.1, (double) this.f_58858_.m_123343_() + 0.5, 8, 0.0, level.random.nextGaussian() * 0.1, 0.0, 0.1);
            this.step += intensity;
            this.activity -= intensity;
            if (this.shouldDeplete()) {
                level.setBlock(this.m_58899_(), DepletedBedrockBlock.instance.m_49966_(), 2);
            }
        }
        if (!level.isClientSide && this.activity > 0 && this.f_58857_.getGameTime() % 80L == 0L) {
            this.playSound();
        }
    }
}
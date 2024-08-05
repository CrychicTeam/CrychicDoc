package net.mehvahdjukaar.supplementaries.mixins;

import java.util.Optional;
import net.mehvahdjukaar.supplementaries.common.entities.RedMerchantEntity;
import net.mehvahdjukaar.supplementaries.common.utils.MiscUtils;
import net.mehvahdjukaar.supplementaries.configs.CommonConfigs;
import net.mehvahdjukaar.supplementaries.reg.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraft.world.entity.npc.WanderingTraderSpawner;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.dimension.end.EndDragonFight;
import net.minecraft.world.level.storage.ServerLevelData;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ WanderingTraderSpawner.class })
public abstract class RedMerchantSpawnerMixin {

    @Shadow
    @Final
    private RandomSource random;

    @Final
    @Shadow
    private ServerLevelData serverLevelData;

    @Unique
    private int supplementaries$redSpawnDelay = 0;

    @Shadow
    protected abstract void tryToSpawnLlamaFor(ServerLevel var1, WanderingTrader var2, int var3);

    @Inject(method = { "tick" }, at = { @At("HEAD") })
    public void tick(ServerLevel serverLevel, boolean b, boolean b1, CallbackInfoReturnable<Integer> cir) {
        if (this.supplementaries$redSpawnDelay > 0) {
            this.supplementaries$redSpawnDelay--;
        }
    }

    @Inject(method = { "spawn" }, at = { @At("RETURN") })
    public void spawn(ServerLevel world, CallbackInfoReturnable<Boolean> cir) {
        if (!(Boolean) cir.getReturnValue() && this.supplementaries$redSpawnDelay == 0) {
            Player player = world.getRandomPlayer();
            if (player != null && this.random.nextInt(9) == 0) {
                BlockPos blockpos = player.m_20183_();
                double mult = supplementaries$getRedMerchantSpawnMultiplier();
                if (mult != 0.0 && (double) this.supplementaries$calculateNormalizeDifficulty(world, blockpos) * mult > (double) (this.random.nextFloat() * 90.0F)) {
                    PoiManager poiManager = world.getPoiManager();
                    Optional<BlockPos> optional = poiManager.find(h -> h.is(PoiTypes.MEETING), pos -> true, blockpos, 48, PoiManager.Occupancy.ANY);
                    BlockPos targetPos = (BlockPos) optional.orElse(blockpos);
                    BlockPos spawnPos = this.findSpawnPositionNear(world, targetPos, 48);
                    if (spawnPos != null && this.hasEnoughSpace(world, spawnPos) && !world.m_204166_(spawnPos).is(Biomes.THE_VOID)) {
                        RedMerchantEntity trader = (RedMerchantEntity) ((EntityType) ModEntities.RED_MERCHANT.get()).spawn(world, spawnPos, MobSpawnType.EVENT);
                        if (trader != null) {
                            this.serverLevelData.setWanderingTraderId(trader.m_20148_());
                            int lifetime = 25000;
                            trader.setDespawnDelay(lifetime);
                            trader.setWanderTarget(targetPos);
                            trader.m_21446_(targetPos, 16);
                            this.supplementaries$redSpawnDelay = lifetime;
                        }
                    }
                }
            }
        }
    }

    @Unique
    private static double supplementaries$getRedMerchantSpawnMultiplier() {
        return CommonConfigs.getRedMerchantSpawnMultiplier();
    }

    @Unique
    private float supplementaries$calculateNormalizeDifficulty(ServerLevel world, BlockPos pos) {
        float dragon = 1.0F;
        EndDragonFight.Data dragonData = world.getServer().getWorldData().endDragonFightData();
        if (dragonData.dragonKilled()) {
            dragon = 1.25F;
        }
        long i = 0L;
        float f = 0.0F;
        if (world.m_46805_(pos)) {
            f = world.m_46940_();
            i = world.m_46745_(pos).m_6319_();
        }
        DifficultyInstance instance = new DifficultyInstance(Difficulty.NORMAL, world.m_46468_(), i, f);
        float diff = instance.getEffectiveDifficulty();
        diff = (float) ((double) diff - 1.5);
        diff *= 4.0F;
        float scale = switch(world.m_46791_()) {
            case PEACEFUL ->
                1.0F;
            case EASY ->
                1.25F;
            case NORMAL ->
                1.5F;
            case HARD ->
                1.75F;
        };
        diff *= scale;
        diff *= dragon;
        if (MiscUtils.FESTIVITY.isChristmas()) {
            diff *= 15.0F;
        }
        return diff;
    }

    @Shadow
    protected abstract boolean hasEnoughSpace(BlockGetter var1, BlockPos var2);

    @Shadow
    protected abstract BlockPos findSpawnPositionNear(LevelReader var1, BlockPos var2, int var3);
}
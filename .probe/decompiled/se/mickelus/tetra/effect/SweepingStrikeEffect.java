package se.mickelus.tetra.effect;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.ToolAction;
import org.apache.commons.lang3.tuple.Pair;
import se.mickelus.mutil.util.CastOptional;
import se.mickelus.mutil.util.RotationHelper;
import se.mickelus.tetra.ServerScheduler;
import se.mickelus.tetra.client.particle.SweepingStrikeParticleOption;
import se.mickelus.tetra.items.modular.ItemModularHandheld;
import se.mickelus.tetra.util.ToolActionHelper;

@ParametersAreNonnullByDefault
public class SweepingStrikeEffect {

    private static final Cache<UUID, Integer> strikeCache = CacheBuilder.newBuilder().maximumSize(100L).expireAfterWrite(1L, TimeUnit.MINUTES).build();

    private static int getStrikeCounter(UUID entityId) {
        int counter = 0;
        try {
            counter = (Integer) strikeCache.get(entityId, () -> 0);
        } catch (ExecutionException var3) {
            var3.printStackTrace();
        }
        strikeCache.put(entityId, counter + 1);
        return counter;
    }

    public static void causeTruesweepEffect(Player player, ItemStack itemStack) {
        StrikingEffect.effectActionMap.stream().filter(entry -> EffectHelper.getEffectLevel(itemStack, (ItemEffect) entry.getLeft()) > 0).map(Pair::getRight).findFirst().ifPresent(tool -> {
            double lookDistance = (Double) Optional.ofNullable(player.m_21051_(ForgeMod.BLOCK_REACH.get())).map(AttributeInstance::m_22135_).orElse(4.5);
            BlockPos origin = BlockPos.containing(player.m_146892_().add(player.m_20252_(0.0F).scale(lookDistance)));
            causeEffect(player.m_9236_(), player, itemStack, origin, tool);
        });
    }

    public static void causeEffect(Level world, Player breakingPlayer, ItemStack toolStack, BlockPos origin, ToolAction tool) {
        if (!world.isClientSide) {
            boolean alternate = isAlternate(breakingPlayer);
            List<Pair<Integer, BlockPos>> targets = breakBlocksAround(world, breakingPlayer, toolStack, origin, tool, alternate);
            int minDelay = targets.stream().mapToInt(Pair::getLeft).min().orElse(0);
            int maxDelay = targets.stream().mapToInt(Pair::getLeft).max().orElse(0);
            int duration = maxDelay - minDelay;
            duration = targets.size() > 10 ? duration : duration * 2;
            duration = Math.max(4, duration);
            double distance = breakingPlayer.m_146892_().subtract(Vec3.atCenterOf(origin)).length();
            causeVfx(breakingPlayer, alternate, duration, (float) distance - 0.5F);
            if (toolStack.getItem() instanceof ItemModularHandheld item) {
                int amount = Math.max(1, targets.size() / 4);
                if (!targets.isEmpty()) {
                    item.applyUsageEffects(breakingPlayer, toolStack, (double) amount);
                }
                item.applyDamage(amount, toolStack, breakingPlayer);
            }
        }
    }

    private static void causeVfx(Player player, boolean isAlternate, int duration, float distance) {
        if (player.m_9236_() instanceof ServerLevel) {
            Vec3 viewVec = player.m_20252_(0.0F).scale((double) distance);
            float ox = -Mth.sin(player.m_146908_() * (float) Math.PI / 180.0F) * 0.5F;
            float oz = Mth.cos(player.m_146908_() * (float) Math.PI / 180.0F) * 0.5F;
            ((ServerLevel) player.m_9236_()).sendParticles(new SweepingStrikeParticleOption(duration, isAlternate, player.m_146909_(), player.m_146908_()), player.m_20185_() + viewVec.x + (double) ox, player.m_20227_(0.6) + viewVec.y, player.m_20189_() + viewVec.z + (double) oz, 0, 0.0, 0.0, 0.0, 0.0);
        }
    }

    public static List<Pair<Integer, BlockPos>> breakBlocksAround(Level world, Player breakingPlayer, ItemStack toolStack, BlockPos originPos, ToolAction tool, boolean alternate) {
        if (world.isClientSide) {
            return Collections.emptyList();
        } else {
            Vec3 playerPosition = breakingPlayer.m_146892_();
            int playerDistance = Mth.ceil(playerPosition.distanceTo(Vec3.atCenterOf(originPos)));
            Direction facing = breakingPlayer.m_6350_();
            double efficiency = (double) ((Float) CastOptional.cast(toolStack.getItem(), ItemModularHandheld.class).map(item -> item.getToolEfficiency(toolStack, tool)).map(eff -> EffectHelper.getModifiedEfficiency(breakingPlayer, toolStack, eff, null, null)).orElse(0.0F)).floatValue();
            double critMultiplier = (Double) CastOptional.cast(toolStack.getItem(), ItemModularHandheld.class).map(item -> CritEffect.rollMultiplier(breakingPlayer.m_217043_(), item, toolStack)).orElse(1.0);
            if (critMultiplier != 1.0) {
                efficiency *= critMultiplier;
                ((ServerLevel) world).sendParticles(ParticleTypes.ENCHANTED_HIT, (double) ((float) originPos.m_123341_() + 0.5F), (double) ((float) originPos.m_123342_() + 0.5F), (double) ((float) originPos.m_123343_() + 0.5F), 15, 0.2, 0.2, 0.2, 0.0);
            }
            int jankLevel = EffectHelper.getEffectLevel(toolStack, ItemEffect.janking);
            int skulkTaintLevel = EffectHelper.getEffectLevel(toolStack, ItemEffect.sculkTaint);
            int reachingLevel = EffectHelper.getEffectLevel(toolStack, ItemEffect.reaching);
            float reachingEfficiency = EffectHelper.getEffectEfficiency(toolStack, ItemEffect.reaching);
            float focusLevel = EffectHelper.getEffectEfficiency(toolStack, ItemEffect.sweepingFocus);
            boolean planarSweep = EffectHelper.getEffectLevel(toolStack, ItemEffect.planarSweep) > 0;
            float focus = focusLevel > 0.0F ? focusLevel - 1.0F : 0.0F;
            int vertical = planarSweep ? 0 : 1;
            float pitch = planarSweep ? (Math.abs(breakingPlayer.m_146909_()) > 45.0F ? (float) (-Math.PI / 2) * Math.signum(breakingPlayer.m_146909_()) : 0.0F) : breakingPlayer.m_146909_() / -180.0F * (float) Math.PI;
            boolean tryReplant = EffectHelper.tryReplant(toolStack, tool);
            List<BlockPos> positions = BlockPos.betweenClosedStream(-16, -vertical, -playerDistance - 1, 16, vertical, 32).map(BlockPos::new).sorted(Comparator.comparingInt(posx -> getPosWeight(posx, focus, playerDistance))).toList();
            List<Pair<Integer, BlockPos>> targets = new ArrayList();
            for (BlockPos pos : positions) {
                BlockPos worldPos = (BlockPos) Optional.of(pos).map(p -> RotationHelper.rotatePitch(p, pitch)).map(p -> RotationHelper.rotateDirection(p, facing)).map(originPos::m_121955_).get();
                BlockState blockState = world.getBlockState(worldPos);
                float blockHardness = blockState.m_60800_(world, worldPos);
                if (ToolActionHelper.isEffectiveOn(tool, blockState) && blockHardness >= 0.0F && (!tryReplant || isFullyGrown(blockState))) {
                    if (!ToolActionHelper.playerCanDestroyBlock(breakingPlayer, blockState, worldPos, toolStack, tool)) {
                        break;
                    }
                    double reachingFactor = getReachingFactor(playerPosition, worldPos, reachingLevel, reachingEfficiency);
                    efficiency -= Math.max(0.5, (double) blockHardness / reachingFactor + (double) (Math.abs(pos.m_123341_()) + Math.abs(pos.m_123343_())) * 0.05);
                    if (efficiency >= 0.0) {
                        targets.add(Pair.of(alternate ? -pos.m_123341_() : pos.m_123341_(), worldPos));
                    }
                } else if (!blockState.m_60795_() && !blockState.m_278721_()) {
                    efficiency -= Math.max((double) Math.abs(blockHardness), 0.5);
                }
                if (efficiency <= 0.0) {
                    break;
                }
            }
            int minDelay = targets.stream().mapToInt(Pair::getLeft).min().orElse(0);
            targets.forEach(pair -> {
                BlockPos posx = (BlockPos) pair.getRight();
                int delay = (Integer) pair.getLeft() - minDelay;
                delay = targets.size() > 10 ? delay : delay * 2;
                BlockState blockStatex = world.getBlockState(posx);
                enqueueBlockBreak(world, breakingPlayer, toolStack, posx, blockStatex, delay, tryReplant, jankLevel, skulkTaintLevel);
            });
            return targets;
        }
    }

    private static double getReachingFactor(Vec3 playerPos, BlockPos blockPos, int reachingLevel, float reachingEfficiency) {
        return reachingLevel > 0 ? (double) ReachingEffect.getMultiplier(reachingLevel, blockPos.m_203193_(playerPos), reachingEfficiency) : 1.0;
    }

    private static int getPosWeight(BlockPos pos, float focus, int backDist) {
        int x = pos.m_123341_();
        int y = pos.m_123342_();
        int z = pos.m_123343_();
        double res = Math.pow((double) Math.abs(x), 2.2) * 1.5 * (double) (1.0F - focus) + Math.pow((double) (Math.abs(y) * 4), 2.0) + Math.pow((double) z, 2.0) * (double) (1.0F + focus);
        if (x < 0) {
            res += (double) (-x);
        }
        if (z < 0) {
            res += Math.pow((double) Math.abs(x), 2.2) * (double) (1.0F - focus) + Math.pow((double) (Math.abs(y) * 4), 2.0) * 1.5 + Math.pow((double) ((float) z * 12.0F / (float) backDist), 2.0) * 2.0 * (double) (1.0F + focus);
        }
        return (int) res;
    }

    private static boolean isAlternate(Player player) {
        return getStrikeCounter(player.m_20148_()) % 2 == 0;
    }

    private static void enqueueBlockBreak(Level world, Player player, ItemStack itemStack, BlockPos pos, BlockState blockState, int delay, boolean tryReplant, int jankLevel, int skulkTaintLevel) {
        if (delay > 0) {
            ServerScheduler.schedule(delay, () -> breakBlock(world, player, itemStack, pos, blockState, tryReplant, jankLevel, skulkTaintLevel));
        } else {
            breakBlock(world, player, itemStack, pos, blockState, tryReplant, jankLevel, skulkTaintLevel);
        }
    }

    private static void breakBlock(Level world, Player player, ItemStack itemStack, BlockPos pos, BlockState blockState, boolean tryReplant, int jankLevel, int skulkTaintLevel) {
        if (EffectHelper.breakBlock(world, player, itemStack, pos, blockState, true, tryReplant)) {
            EffectHelper.sendEventToPlayer((ServerPlayer) player, 2001, pos, Block.getId(blockState));
            if (jankLevel > 0) {
                JankEffect.jankItemsDelayed((ServerLevel) world, pos, jankLevel, EffectHelper.getEffectEfficiency(itemStack, ItemEffect.janking), player);
            }
            if (skulkTaintLevel > 0) {
                SculkTaintEffect.perform((ServerLevel) world, pos, skulkTaintLevel, EffectHelper.getEffectEfficiency(itemStack, ItemEffect.sculkTaint));
            }
        }
    }

    private static boolean isFullyGrown(BlockState blockState) {
        if (blockState.m_60734_() instanceof CropBlock crop && crop.isMaxAge(blockState)) {
            return true;
        }
        return false;
    }

    private static void debugPlacement(Level world, BlockPos origin, List<BlockPos> positions, int count) {
        for (int i = 0; i < positions.size() && i < count; i++) {
            BlockPos pos = (BlockPos) positions.get(i);
            if ((float) i > (float) count * 7.0F / 8.0F) {
                enqueueDebugPlacement(world, pos, Blocks.WHITE_STAINED_GLASS.defaultBlockState(), i * 5 + 100);
            } else if ((float) i > (float) count * 6.0F / 8.0F) {
                enqueueDebugPlacement(world, pos, Blocks.LIGHT_BLUE_STAINED_GLASS.defaultBlockState(), i * 5 + 100);
            } else if ((float) i > (float) count * 5.0F / 8.0F) {
                enqueueDebugPlacement(world, pos, Blocks.BLUE_STAINED_GLASS.defaultBlockState(), i * 5 + 100);
            } else if ((float) i > (float) count * 4.0F / 8.0F) {
                enqueueDebugPlacement(world, pos, Blocks.CYAN_STAINED_GLASS.defaultBlockState(), i * 5 + 100);
            } else if ((float) i > (float) count * 3.0F / 8.0F) {
                enqueueDebugPlacement(world, pos, Blocks.LIME_STAINED_GLASS.defaultBlockState(), i * 5 + 100);
            } else if ((float) i > (float) count * 2.0F / 8.0F) {
                enqueueDebugPlacement(world, pos, Blocks.YELLOW_STAINED_GLASS.defaultBlockState(), i * 5 + 100);
            } else if ((float) i > (float) count * 1.0F / 8.0F) {
                enqueueDebugPlacement(world, pos, Blocks.ORANGE_STAINED_GLASS.defaultBlockState(), i * 5 + 100);
            } else {
                enqueueDebugPlacement(world, pos, Blocks.RED_STAINED_GLASS.defaultBlockState(), i * 5 + 100);
            }
        }
    }

    private static void enqueueDebugPlacement(Level world, BlockPos pos, BlockState blockState, int delay) {
        ServerScheduler.schedule(delay, () -> world.setBlock(pos, blockState, 3));
    }
}
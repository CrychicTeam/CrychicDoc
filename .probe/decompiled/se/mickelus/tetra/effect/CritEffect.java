package se.mickelus.tetra.effect;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import net.minecraftforge.eventbus.api.Event.Result;
import se.mickelus.mutil.util.CastOptional;
import se.mickelus.tetra.items.modular.IModularItem;
import se.mickelus.tetra.util.ToolActionHelper;

@ParametersAreNonnullByDefault
public class CritEffect {

    private static final Cache<UUID, BlockPos> critBlockCache = CacheBuilder.newBuilder().maximumSize(50L).expireAfterWrite(10L, TimeUnit.SECONDS).build();

    public static boolean critBlock(Level world, Player breakingPlayer, BlockPos pos, BlockState blockState, ItemStack itemStack, int critLevel) {
        BlockPos recentCritPos = (BlockPos) critBlockCache.getIfPresent(breakingPlayer.m_20148_());
        if (pos.equals(recentCritPos)) {
            return true;
        } else if ((double) breakingPlayer.m_217043_().nextFloat() < (double) critLevel * 0.01 && recentCritPos == null && blockState.m_60800_(world, pos) > -1.0F && itemStack.getItem().getDestroySpeed(itemStack, blockState) > 2.0F * blockState.m_60800_(world, pos) && ToolActionHelper.playerCanDestroyBlock(breakingPlayer, blockState, pos, itemStack)) {
            EffectHelper.breakBlock(world, breakingPlayer, itemStack, pos, blockState, true, false);
            itemStack.getItem().mineBlock(itemStack, world, blockState, pos, breakingPlayer);
            critBlockCache.put(breakingPlayer.m_20148_(), pos);
            CastOptional.cast(breakingPlayer, ServerPlayer.class).ifPresent(serverPlayer -> EffectHelper.sendEventToPlayer(serverPlayer, 2001, pos, Block.getId(blockState)));
            if (world instanceof ServerLevel) {
                ((ServerLevel) world).sendParticles(ParticleTypes.ENCHANTED_HIT, (double) pos.m_123341_() + 0.5, (double) pos.m_123342_() + 0.5, (double) pos.m_123343_() + 0.5, 12, (world.random.nextDouble() * 2.0 - 1.0) * 0.3, 0.3 + world.random.nextDouble() * 0.3, (world.random.nextDouble() * 2.0 - 1.0) * 0.3, 0.3);
            }
            return true;
        } else {
            return false;
        }
    }

    public static void onBlockBreak(LivingEntity entity) {
        critBlockCache.invalidate(entity.m_20148_());
    }

    public static void critEntity(CriticalHitEvent event, ItemStack itemStack, int critLevel) {
        if ((double) event.getEntity().m_217043_().nextFloat() < (double) critLevel * 0.01) {
            event.setDamageModifier(Math.max(EffectHelper.getEffectEfficiency(itemStack, ItemEffect.criticalStrike), event.getDamageModifier()));
            event.setResult(Result.ALLOW);
        }
    }

    public static double rollMultiplier(RandomSource random, IModularItem item, ItemStack itemStack) {
        int critLevel = item.getEffectLevel(itemStack, ItemEffect.criticalStrike);
        return critLevel > 0 && (double) random.nextFloat() < (double) critLevel * 0.01 ? (double) item.getEffectEfficiency(itemStack, ItemEffect.criticalStrike) : 1.0;
    }
}
package se.mickelus.tetra.effect;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundLevelEventPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.event.ForgeEventFactory;
import org.apache.commons.lang3.mutable.MutableBoolean;
import se.mickelus.tetra.items.modular.IModularItem;

@ParametersAreNonnullByDefault
public class EffectHelper {

    private static final Cache<UUID, Float> cooledAttackStrengthCache = CacheBuilder.newBuilder().maximumSize(50L).expireAfterWrite(1L, TimeUnit.SECONDS).build();

    private static final Cache<UUID, Boolean> sprintingCache = CacheBuilder.newBuilder().maximumSize(50L).expireAfterWrite(1L, TimeUnit.SECONDS).build();

    public static void setCooledAttackStrength(Player player, float strength) {
        cooledAttackStrengthCache.put(player.m_20148_(), strength);
    }

    public static float getCooledAttackStrength(Player player) {
        try {
            return (Float) cooledAttackStrengthCache.get(player.m_20148_(), () -> 0.0F);
        } catch (ExecutionException var2) {
            return 0.0F;
        }
    }

    public static void setSprinting(LivingEntity player, boolean isSprinting) {
        sprintingCache.put(player.m_20148_(), isSprinting);
    }

    public static boolean getSprinting(LivingEntity player) {
        try {
            return (Boolean) sprintingCache.get(player.m_20148_(), () -> false);
        } catch (ExecutionException var2) {
            return false;
        }
    }

    public static int getEffectLevel(ItemStack itemStack, ItemEffect effect) {
        IModularItem item = (IModularItem) itemStack.getItem();
        return item.getEffectLevel(itemStack, effect);
    }

    public static float getEffectEfficiency(ItemStack itemStack, ItemEffect effect) {
        IModularItem item = (IModularItem) itemStack.getItem();
        return item.getEffectEfficiency(itemStack, effect);
    }

    public static boolean breakBlock(Level world, Player breakingPlayer, ItemStack toolStack, BlockPos pos, BlockState blockState, boolean harvest, boolean tryReplant) {
        if (!world.isClientSide) {
            ServerLevel serverWorld = (ServerLevel) world;
            ServerPlayer serverPlayer = (ServerPlayer) breakingPlayer;
            GameType gameType = serverPlayer.gameMode.getGameModeForPlayer();
            int exp = ForgeHooks.onBlockBreakEvent(world, gameType, serverPlayer, pos);
            BlockEntity tileEntity = world.getBlockEntity(pos);
            if (exp == -1) {
                return false;
            } else {
                boolean canRemove = !toolStack.onBlockStartBreak(pos, breakingPlayer) && !breakingPlayer.blockActionRestricted(world, pos, gameType) && (!harvest || blockState.canHarvestBlock(world, pos, breakingPlayer)) && blockState.m_60734_().onDestroyedByPlayer(blockState, world, pos, breakingPlayer, harvest, world.getFluidState(pos));
                if (canRemove) {
                    blockState.m_60734_().destroy(world, pos, blockState);
                    if (tryReplant) {
                        breakAndReplant(serverWorld, pos, blockState, breakingPlayer, toolStack, harvest);
                    } else if (harvest) {
                        blockState.m_60734_().playerDestroy(world, breakingPlayer, pos, blockState, tileEntity, toolStack);
                    }
                    if (harvest && exp > 0) {
                        blockState.m_60734_().popExperience(serverWorld, pos, exp);
                    }
                    if (harvest) {
                        blockState.m_222967_(serverWorld, pos, toolStack, false);
                    }
                }
                return canRemove;
            }
        } else {
            return blockState.m_60734_().onDestroyedByPlayer(blockState, world, pos, breakingPlayer, harvest, world.getFluidState(pos));
        }
    }

    public static boolean tryReplant(ItemStack itemStack, ToolAction toolAction) {
        return toolAction == ToolActions.HOE_DIG && EnchantmentHelper.hasSilkTouch(itemStack);
    }

    private static boolean breakAndReplant(ServerLevel serverLevel, BlockPos pos, BlockState blockState, Player entity, ItemStack itemStack, boolean doDrops) {
        BlockState newBlock = blockState.m_60734_() instanceof CropBlock crop ? crop.m_49966_() : Blocks.AIR.defaultBlockState();
        MutableBoolean foundSeed = new MutableBoolean(false);
        Item seedItem = blockState.m_60734_().asItem();
        Block.getDrops(blockState, serverLevel, pos, serverLevel.m_7702_(pos), entity, itemStack).forEach(droppedStack -> {
            if (droppedStack.getItem() == seedItem && !foundSeed.getValue()) {
                droppedStack.shrink(1);
                foundSeed.setValue(true);
            }
            if (doDrops && !droppedStack.isEmpty()) {
                Block.popResource(serverLevel, pos, droppedStack);
            }
        });
        serverLevel.m_46597_(pos, newBlock);
        return foundSeed.getValue();
    }

    public static void sendEventToPlayer(ServerPlayer player, int type, BlockPos pos, int data) {
        player.connection.send(new ClientboundLevelEventPacket(type, pos, data, false));
    }

    public static void applyEnchantmentHitEffects(ItemStack itemStack, LivingEntity target, LivingEntity attacker) {
        EnchantmentHelper.getEnchantments(itemStack).forEach((enchantment, level) -> enchantment.doPostAttack(attacker, target, level));
        if (attacker != null) {
            for (ItemStack equipment : attacker.m_20158_()) {
                EnchantmentHelper.getEnchantments(equipment).forEach((enchantment, level) -> enchantment.doPostAttack(attacker, target, level));
            }
        }
        int fireAspectLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FIRE_ASPECT, itemStack);
        if (fireAspectLevel > 0) {
            target.m_20254_(fireAspectLevel * 4);
        }
    }

    public static float getModifiedEfficiency(Player player, ItemStack itemStack, float base, @Nullable BlockState blockState, @Nullable BlockPos pos) {
        float result = base;
        if (base > 1.0F) {
            int efficiencyLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.BLOCK_EFFICIENCY, itemStack);
            result = base + (float) (efficiencyLevel * efficiencyLevel + 1);
        }
        if (MobEffectUtil.hasDigSpeed(player)) {
            result *= 1.0F + (float) (MobEffectUtil.getDigSpeedAmplification(player) + 1) * 0.2F;
        }
        if (player.m_21023_(MobEffects.DIG_SLOWDOWN)) {
            switch(player.m_21124_(MobEffects.DIG_SLOWDOWN).getAmplifier()) {
                case 0:
                    result *= 0.3F;
                    break;
                case 1:
                    result *= 0.09F;
                    break;
                case 2:
                    result *= 0.0027F;
                    break;
                case 3:
                default:
                    result *= 8.1E-4F;
            }
        }
        if (player.m_204029_(FluidTags.WATER) && !EnchantmentHelper.hasAquaAffinity(player)) {
            result /= 5.0F;
        }
        if (!player.m_20096_()) {
            result /= 5.0F;
        }
        if (blockState != null) {
            result = ForgeEventFactory.getBreakSpeed(player, blockState, result, pos);
        }
        return result;
    }
}
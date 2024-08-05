package dev.xkmc.l2complements.content.enchantment.digging;

import dev.xkmc.l2complements.content.enchantment.core.CustomDescEnchantment;
import dev.xkmc.l2complements.content.enchantment.core.DiggerAndSwordEnchantment;
import dev.xkmc.l2complements.content.enchantment.core.UnobtainableEnchantment;
import dev.xkmc.l2complements.init.L2Complements;
import dev.xkmc.l2complements.init.data.LCConfig;
import dev.xkmc.l2complements.init.data.LangData;
import dev.xkmc.l2complements.init.data.TagGen;
import dev.xkmc.l2complements.init.registrate.LCEnchantments;
import dev.xkmc.l2library.init.events.GeneralEventHandler;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.Level;

public class RangeDiggingEnchantment extends UnobtainableEnchantment implements CustomDescEnchantment {

    private static final Set<UUID> BREAKER = new HashSet();

    private final BlockBreaker breaker;

    public static void execute(Player player, Runnable run) {
        synchronized (BREAKER) {
            if (!BREAKER.contains(player.m_20148_())) {
                BREAKER.add(player.m_20148_());
                try {
                    run.run();
                } catch (Exception var5) {
                    L2Complements.LOGGER.throwing(Level.ERROR, var5);
                }
                BREAKER.remove(player.m_20148_());
            }
        }
    }

    private static Direction getFace(Player player) {
        net.minecraft.world.level.Level level = player.m_9236_();
        Vec3 base = player.m_20299_(0.0F);
        Vec3 look = player.m_20154_();
        double reach = player.m_21133_(ForgeMod.BLOCK_REACH.get());
        Vec3 target = base.add(look.x * reach, look.y * reach, look.z * reach);
        BlockHitResult trace = level.m_45547_(new ClipContext(base, target, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, player));
        return trace.getDirection();
    }

    private static double hardnessFactor() {
        return LCConfig.COMMON.chainDiggingHardnessRange.get();
    }

    private static boolean canBreak(BlockPos i, net.minecraft.world.level.Level level, Player player, double hardness) {
        BlockState state = level.getBlockState(i);
        if (state.m_60795_()) {
            return false;
        } else if (!player.hasCorrectToolForDrops(state)) {
            return false;
        } else {
            float speed = state.m_60800_(player.m_9236_(), i);
            return speed < 0.0F ? false : hardness < 0.0 || (double) speed <= hardness;
        }
    }

    public RangeDiggingEnchantment(BlockBreaker breaker, Enchantment.Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot[] pApplicableSlots) {
        super(pRarity, pCategory, pApplicableSlots);
        this.breaker = breaker;
    }

    @Override
    public int getMaxLevel() {
        return this.breaker.getMaxLevel();
    }

    public List<BlockPos> getTargets(Player player, BlockPos pos, ItemStack stack, int lv) {
        net.minecraft.world.level.Level level = player.m_9236_();
        BlockState state = level.getBlockState(pos);
        double hardness = this.breaker.ignoreHardness() ? -1.0 : (double) state.m_60800_(level, pos) * hardnessFactor();
        return this.breaker.getInstance(new DiggerContext(player, getFace(player), stack, lv, pos, state)).find(level, pos, i -> !pos.equals(i) && canBreak(i, level, player, hardness));
    }

    public void onBlockBreak(ServerPlayer player, BlockPos pos, ItemStack stack, int lv) {
        List<BlockPos> blocks = this.getTargets(player, pos, stack, lv);
        execute(player, () -> {
            int max = LCConfig.COMMON.chainDiggingDelayThreshold.get();
            if (blocks.size() <= max) {
                for (BlockPos i : blocks) {
                    player.gameMode.destroyBlock(i);
                }
            } else {
                if (LCConfig.COMMON.delayDiggingRequireEnder.get() && stack.getEnchantmentLevel((Enchantment) LCEnchantments.ENDER.get()) <= 0) {
                    player.sendSystemMessage(LangData.IDS.DELAY_WARNING.get(((DiggerAndSwordEnchantment) LCEnchantments.ENDER.get()).m_44700_(1), max).withStyle(ChatFormatting.RED), true);
                    return;
                }
                GeneralEventHandler.schedulePersistent(new DelayedBlockBreaker(player, blocks)::tick);
            }
        });
    }

    @Override
    public List<Component> descFull(int lv, String key, boolean alt, boolean book) {
        return this.breaker.descFull(lv, key, alt, book);
    }

    @Override
    protected boolean checkCompatibility(Enchantment e) {
        return !ForgeRegistries.ENCHANTMENTS.tags().getTag(TagGen.DIGGER_ENCH).contains(e);
    }

    @Override
    public int getDecoColor(String s) {
        return -5263441;
    }
}
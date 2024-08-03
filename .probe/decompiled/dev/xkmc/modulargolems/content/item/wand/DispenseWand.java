package dev.xkmc.modulargolems.content.item.wand;

import com.tterrag.registrate.util.entry.ItemEntry;
import dev.xkmc.l2library.util.raytrace.RayTraceUtil;
import dev.xkmc.modulargolems.content.item.golem.GolemHolder;
import dev.xkmc.modulargolems.init.advancement.GolemTriggers;
import dev.xkmc.modulargolems.init.data.MGConfig;
import dev.xkmc.modulargolems.init.data.MGLangData;
import java.util.function.Predicate;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class DispenseWand extends BaseWandItem implements GolemInteractItem {

    private static void iter(Player player, Predicate<ItemStack> use) {
        if (!use.test(player.m_21206_())) {
            for (int i = 0; i < 36; i++) {
                if (use.test(player.getInventory().getItem(i))) {
                    return;
                }
            }
        }
    }

    public DispenseWand(Item.Properties properties, @Nullable ItemEntry<? extends BaseWandItem> base) {
        super(properties, MGLangData.WAND_SUMMON_RIGHT, MGLangData.WAND_SUMMON_SHIFT, base);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player user, InteractionHand hand) {
        ItemStack stack = user.m_21120_(hand);
        if (!level.isClientSide()) {
            boolean all = user.m_6144_();
            Vec3 pos = user.m_20182_();
            if (!all) {
                BlockHitResult result = RayTraceUtil.rayTraceBlock(level, user, (double) MGConfig.COMMON.summonDistance.get().intValue());
                if (result.getType() == HitResult.Type.BLOCK) {
                    pos = result.m_82450_();
                }
            }
            Vec3 finalPos = pos;
            int[] counter = new int[] { 0 };
            iter(user, golem -> {
                if (golem.getItem() instanceof GolemHolder<?, ?> holder && holder.summon(golem, level, finalPos, user, null)) {
                    counter[0]++;
                    return !all;
                }
                return false;
            });
            if (counter[0] > 1 && user instanceof ServerPlayer sp) {
                GolemTriggers.MAS_SUMMON.trigger(sp, counter[0]);
            }
        }
        return InteractionResultHolder.success(stack);
    }
}
package dev.xkmc.modulargolems.content.item.wand;

import com.tterrag.registrate.util.entry.ItemEntry;
import dev.xkmc.l2library.util.raytrace.RayTraceUtil;
import dev.xkmc.l2serial.util.Wrappers;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.item.card.ConfigCard;
import dev.xkmc.modulargolems.init.data.MGConfig;
import dev.xkmc.modulargolems.init.data.MGLangData;
import java.util.List;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.Nullable;

public class RetrievalWandItem extends BaseWandItem implements GolemInteractItem {

    public RetrievalWandItem(Item.Properties properties, @Nullable ItemEntry<? extends BaseWandItem> base) {
        super(properties, MGLangData.WAND_RETRIEVE_RIGHT, MGLangData.WAND_RETRIEVE_SHIFT, base);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player user, InteractionHand hand) {
        ItemStack stack = user.m_21120_(hand);
        if (user.m_6144_()) {
            EntityHitResult result = RayTraceUtil.rayTraceEntity(user, (double) MGConfig.COMMON.retrieveDistance.get().intValue(), e -> {
                if (e instanceof AbstractGolemEntity<?, ?> golemx && golemx.canModify(user)) {
                    return true;
                }
                return false;
            });
            if (result == null) {
                return InteractionResultHolder.fail(stack);
            } else {
                Entity golem = result.getEntity();
                return attemptRetrieve(level, user, (AbstractGolemEntity<?, ?>) Wrappers.cast(golem)) ? InteractionResultHolder.success(stack) : InteractionResultHolder.fail(stack);
            }
        } else {
            List<AbstractGolemEntity> list = level.getEntities(EntityTypeTest.forClass(AbstractGolemEntity.class), user.m_20191_().inflate((double) MGConfig.COMMON.retrieveRange.get().intValue()), e -> true);
            if (list.size() == 0) {
                return InteractionResultHolder.pass(stack);
            } else {
                boolean success = false;
                for (AbstractGolemEntity golem : list) {
                    success |= attemptRetrieve(level, user, golem);
                }
                return success ? InteractionResultHolder.success(stack) : InteractionResultHolder.fail(stack);
            }
        }
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player user, LivingEntity target, InteractionHand hand) {
        if (target instanceof AbstractGolemEntity<?, ?> golem) {
            return attemptRetrieve(target.m_9236_(), user, (AbstractGolemEntity<?, ?>) Wrappers.cast(golem)) ? InteractionResult.SUCCESS : InteractionResult.FAIL;
        } else {
            return InteractionResult.PASS;
        }
    }

    private static boolean attemptRetrieve(Level level, Player user, AbstractGolemEntity<?, ?> golem) {
        if (!ConfigCard.getFilter(user).test(golem)) {
            return false;
        } else if (!golem.canModify(user)) {
            return false;
        } else if (level.isClientSide()) {
            return true;
        } else {
            golem.m_19877_();
            user.getInventory().placeItemBackInInventory(golem.toItem());
            return true;
        }
    }
}
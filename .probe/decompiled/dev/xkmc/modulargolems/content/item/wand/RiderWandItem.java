package dev.xkmc.modulargolems.content.item.wand;

import com.tterrag.registrate.util.entry.ItemEntry;
import dev.xkmc.l2serial.util.Wrappers;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.entity.dog.DogGolemEntity;
import dev.xkmc.modulargolems.content.item.card.ConfigCard;
import dev.xkmc.modulargolems.init.data.MGLangData;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class RiderWandItem extends BaseWandItem implements GolemInteractItem {

    public RiderWandItem(Item.Properties properties, @Nullable ItemEntry<? extends BaseWandItem> base) {
        super(properties, MGLangData.WAND_RIDER, null, base);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player user, LivingEntity target, InteractionHand hand) {
        if (user.m_20202_() instanceof AbstractGolemEntity) {
            if (!user.m_9236_().isClientSide()) {
                user.m_8127_();
            }
            return InteractionResult.SUCCESS;
        } else if (target instanceof AbstractGolemEntity<?, ?> golem) {
            return ride(target.m_9236_(), user, (AbstractGolemEntity<?, ?>) Wrappers.cast(golem)) ? InteractionResult.SUCCESS : InteractionResult.FAIL;
        } else {
            return InteractionResult.PASS;
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player user, InteractionHand hand) {
        ItemStack stack = user.m_21120_(hand);
        if (user.m_20202_() instanceof AbstractGolemEntity) {
            if (!level.isClientSide()) {
                user.m_8127_();
            }
            return InteractionResultHolder.success(stack);
        } else {
            return InteractionResultHolder.pass(stack);
        }
    }

    private static boolean ride(Level level, Player user, AbstractGolemEntity<?, ?> golem) {
        if (!ConfigCard.getFilter(user).test(golem)) {
            return false;
        } else if (!golem.canModify(user) && !(golem.m_6688_() instanceof Player)) {
            return false;
        } else if (level.isClientSide()) {
            return true;
        } else if (golem instanceof DogGolemEntity e) {
            user.m_7998_(e, false);
            return true;
        } else {
            return true;
        }
    }
}
package dev.xkmc.modulargolems.content.capability;

import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.l2serial.serialization.SerialClass.SerialField;
import dev.xkmc.modulargolems.content.item.card.NameFilterCard;
import dev.xkmc.modulargolems.content.item.card.TargetFilterCard;
import dev.xkmc.modulargolems.init.registrate.GolemItems;
import java.util.ArrayList;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

@SerialClass
public class TargetFilterConfig {

    public static final int LINE = 18;

    @SerialField
    protected final ArrayList<ItemStack> hostileTo = new ArrayList();

    @SerialField
    protected final ArrayList<ItemStack> friendlyTo = new ArrayList();

    public boolean internalMatch(ArrayList<ItemStack> list, ItemStack stack) {
        for (ItemStack filter : list) {
            if (stack.getItem() == filter.getItem() && ItemStack.isSameItemSameTags(stack, filter)) {
                return true;
            }
        }
        return false;
    }

    public boolean aggressiveToward(LivingEntity le) {
        for (ItemStack e : this.hostileTo) {
            if (e.getItem() instanceof TargetFilterCard card && card.mayTarget(e).test(le)) {
                return true;
            }
        }
        return false;
    }

    public boolean friendlyToward(LivingEntity le) {
        for (ItemStack e : this.friendlyTo) {
            if (e.getItem() instanceof TargetFilterCard card && card.mayTarget(e).test(le)) {
                return true;
            }
        }
        return false;
    }

    public void initDefault() {
        this.resetHostile();
        this.resetFriendly();
    }

    public void resetHostile() {
        this.hostileTo.clear();
        this.hostileTo.add(GolemItems.CARD_DEF.asStack());
    }

    public void resetFriendly() {
        this.friendlyTo.clear();
        this.friendlyTo.add(NameFilterCard.getFriendly());
    }
}
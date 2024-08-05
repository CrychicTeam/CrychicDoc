package dev.xkmc.l2artifacts.content.misc;

import dev.xkmc.l2artifacts.content.search.common.ArtifactChestMenuPvd;
import dev.xkmc.l2artifacts.content.search.fitered.FilteredMenu;
import dev.xkmc.l2library.util.nbt.ItemCompoundTag;
import dev.xkmc.l2library.util.nbt.ItemListTag;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ArtifactChestItem extends Item {

    private static final String KEY_LIST = "artifact_list";

    private static final String KEY_FILTER = "filter";

    private static final String KEY_EXP = "experience";

    public static List<ItemStack> getContent(ItemStack stack) {
        ListTag list = ItemCompoundTag.of(stack).getSubList("artifact_list", 10).getOrCreate();
        List<ItemStack> ans = new ArrayList();
        for (int i = 0; i < list.size(); i++) {
            ans.add(ItemStack.of(list.getCompound(i)));
        }
        return ans;
    }

    public static void setContent(ItemStack stack, List<ItemStack> ans) {
        ItemListTag list = ItemCompoundTag.of(stack).getSubList("artifact_list", 10);
        list.clear();
        for (ItemStack e : ans) {
            list.addCompound().setTag(e.serializeNBT());
        }
    }

    public ArtifactChestItem(Item.Properties properties) {
        super(properties.stacksTo(1));
    }

    public static CompoundTag getFilter(ItemStack stack) {
        return stack.getOrCreateTag().getCompound("filter");
    }

    public static void setFilter(ItemStack stack, CompoundTag filter) {
        stack.getOrCreateTag().put("filter", filter);
    }

    public static int getExp(ItemStack stack) {
        return stack.getOrCreateTag().getInt("experience");
    }

    public static void setExp(ItemStack stack, int exp) {
        stack.getOrCreateTag().putInt("experience", exp);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.m_21120_(hand);
        if (!level.isClientSide()) {
            int slot = hand == InteractionHand.OFF_HAND ? 40 : player.getInventory().selected;
            new ArtifactChestMenuPvd(FilteredMenu::new, (ServerPlayer) player, slot, stack).open();
        }
        return InteractionResultHolder.success(stack);
    }

    @Override
    public boolean canFitInsideContainerItems() {
        return false;
    }
}
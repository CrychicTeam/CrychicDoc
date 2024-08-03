package dev.xkmc.l2artifacts.content.misc;

import dev.xkmc.l2artifacts.content.core.ArtifactSet;
import dev.xkmc.l2artifacts.content.core.RankedItem;
import dev.xkmc.l2artifacts.init.L2Artifacts;
import dev.xkmc.l2artifacts.init.data.LangData;
import dev.xkmc.l2artifacts.init.registrate.entries.SetEntry;
import dev.xkmc.l2artifacts.init.registrate.items.ArtifactItems;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.stats.Stats;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class RandomArtifactSetItem extends RankedItem {

    public RandomArtifactSetItem(Item.Properties props, int rank) {
        super(props, rank);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.m_21120_(hand);
        player.awardStat(Stats.ITEM_USED.get(this));
        if (!level.isClientSide) {
            for (ItemStack e : getRandomArtifact(stack, this.rank, player.m_217043_())) {
                player.getInventory().placeItemBackInInventory(e);
            }
        }
        if (!player.getAbilities().instabuild) {
            stack.shrink(1);
        }
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }

    public static ItemStack setList(int rank, Collection<SetEntry<?>> sets) {
        ItemStack stack = ArtifactItems.RANDOM[rank - 1].asStack();
        CompoundTag root = stack.getOrCreateTag();
        ListTag ltag = new ListTag();
        for (SetEntry<?> e : sets) {
            if (e.hasRank(rank) && e.items.length == 5) {
                ltag.add(StringTag.valueOf(((ArtifactSet) e.get()).getID()));
            }
        }
        root.put("Sets", ltag);
        return stack;
    }

    @Nullable
    private static Collection<SetEntry<?>> getList(ItemStack stack, int rank) {
        CompoundTag tag = stack.getTag();
        if (tag != null && tag.contains("Sets")) {
            ListTag ltag = tag.getList("Sets", 8);
            Map<String, SetEntry<?>> map = new HashMap();
            for (SetEntry<?> e : L2Artifacts.REGISTRATE.SET_LIST) {
                if (e.hasRank(rank) && e.items.length == 5) {
                    map.put(((ArtifactSet) e.get()).getID(), e);
                }
            }
            Collection<SetEntry<?>> list = new ArrayList();
            for (int i = 0; i < ltag.size(); i++) {
                String str = ltag.getString(i);
                if (map.containsKey(str)) {
                    list.add((SetEntry) map.get(str));
                }
            }
            if (!list.isEmpty()) {
                return list;
            }
        }
        return null;
    }

    public static List<ItemStack> getRandomArtifact(ItemStack stack, int rank, RandomSource random) {
        Collection<SetEntry<?>> list = getList(stack, rank);
        if (list == null) {
            list = L2Artifacts.REGISTRATE.SET_LIST;
        }
        List<SetEntry<?>> sets = list.stream().filter(e -> e.hasRank(rank) && e.items.length == 5).toList();
        SetEntry<?> set = (SetEntry<?>) sets.get(random.nextInt(sets.size()));
        List<ItemStack> ans = new ArrayList();
        for (int i = 0; i < 5; i++) {
            ans.add(set.getItem(i, rank));
        }
        return ans;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
        Collection<SetEntry<?>> sets = getList(stack, this.rank);
        if (sets == null) {
            list.add(LangData.LOOT_POOL_ALL.get());
        } else {
            list.add(LangData.LOOT_POOL.get());
            for (SetEntry<?> e : sets) {
                list.add(((ArtifactSet) e.get()).getDesc().withStyle(ChatFormatting.GRAY));
            }
        }
    }
}
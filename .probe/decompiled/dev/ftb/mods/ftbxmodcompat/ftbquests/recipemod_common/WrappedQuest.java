package dev.ftb.mods.ftbxmodcompat.ftbquests.recipemod_common;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import dev.ftb.mods.ftblibrary.icon.ItemIcon;
import dev.ftb.mods.ftbquests.client.ClientQuestFile;
import dev.ftb.mods.ftbquests.quest.Quest;
import dev.ftb.mods.ftbquests.quest.loot.RewardTable;
import dev.ftb.mods.ftbquests.quest.loot.WeightedReward;
import dev.ftb.mods.ftbquests.quest.reward.RandomReward;
import dev.ftb.mods.ftbquests.quest.reward.Reward;
import dev.ftb.mods.ftbquests.quest.task.ItemTask;
import dev.ftb.mods.ftbquests.quest.task.Task;
import dev.latvian.mods.itemfilters.api.ItemFiltersAPI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

public class WrappedQuest {

    public final Quest quest;

    public final List<List<ItemStack>> input;

    public final List<List<ItemStack>> output;

    public WrappedQuest(Quest q, List<Reward> rewards) {
        this.quest = q;
        this.input = new ArrayList(5);
        this.output = new ArrayList(5);
        if (this.quest.getTasks().size() == 1) {
            this.input.add(Collections.emptyList());
            this.input.add(Collections.emptyList());
            this.input.add(Collections.emptyList());
            this.input.add(Collections.emptyList());
        }
        for (Task task : this.quest.getTasks()) {
            if (task instanceof ItemTask itemTask) {
                this.input.add(Collections.singletonList(itemTask.getItemStack()));
            } else {
                Object object = task.getIcon().getIngredient();
                ItemStack stack = object instanceof ItemStack ? (ItemStack) object : ItemStack.EMPTY;
                if (!stack.isEmpty()) {
                    List<ItemStack> list = new ArrayList();
                    ItemFiltersAPI.getDisplayItemStacks(stack, list);
                    this.input.add(List.copyOf(list));
                } else if (task.getIcon() instanceof ItemIcon itemIcon) {
                    stack = itemIcon.getStack().copy();
                    stack.setHoverName(task.getTitle());
                    this.input.add(Collections.singletonList(stack));
                } else {
                    stack = new ItemStack(Items.PAINTING);
                    stack.setHoverName(task.getTitle());
                    stack.addTagElement("icon", StringTag.valueOf(task.getIcon().toString()));
                    this.input.add(Collections.singletonList(stack));
                }
            }
        }
        if (rewards.size() == 1) {
            this.output.add(Collections.emptyList());
            this.output.add(Collections.emptyList());
            this.output.add(Collections.emptyList());
            this.output.add(Collections.emptyList());
        }
        for (Reward reward : rewards) {
            Object object = reward.getIcon().getIngredient();
            ItemStack stack = ItemStack.EMPTY;
            if (object instanceof ItemStack s) {
                stack = s;
            }
            if (!stack.isEmpty()) {
                this.output.add(Collections.singletonList(stack.copy()));
            } else if (reward instanceof RandomReward r) {
                RewardTable table = r.getTable();
                if (table != null) {
                    Builder<ItemStack> builder = ImmutableList.builder();
                    if (table.shouldShowTooltip()) {
                        ItemStack unknown = new ItemStack(Items.BARRIER);
                        unknown.setHoverName(Component.literal("Unknown Reward"));
                        builder.add(unknown);
                    } else {
                        for (WeightedReward wr : table.getWeightedRewards()) {
                            if (wr.getReward().getIcon().getIngredient() instanceof ItemStack s) {
                                builder.add(s);
                            }
                        }
                    }
                    this.output.add(builder.build());
                }
            } else if (reward.getIcon() instanceof ItemIcon itemIcon) {
                stack = itemIcon.getStack().copy();
                stack.setHoverName(reward.getTitle());
                this.output.add(Collections.singletonList(stack));
            } else {
                stack = new ItemStack(Items.PAINTING);
                stack.setHoverName(reward.getTitle());
                stack.addTagElement("icon", StringTag.valueOf(reward.getIcon().toString()));
                this.output.add(Collections.singletonList(stack));
            }
        }
    }

    public List<Ingredient> inputIngredients() {
        return this.input.stream().map(items -> Ingredient.of((ItemStack[]) items.toArray(new ItemStack[0]))).toList();
    }

    public List<Ingredient> outputIngredients() {
        return this.output.stream().map(items -> Ingredient.of((ItemStack[]) items.toArray(new ItemStack[0]))).toList();
    }

    public boolean hasInput(ItemStack stack) {
        for (List<ItemStack> l : this.input) {
            for (ItemStack stack1 : l) {
                if (ItemStack.isSameItemSameTags(stack1, stack)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean hasOutput(ItemStack stack) {
        for (List<ItemStack> l : this.output) {
            for (ItemStack stack1 : l) {
                if (ItemStack.isSameItemSameTags(stack1, stack)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void openQuestGui() {
        ClientQuestFile.openGui(this.quest, true);
    }
}
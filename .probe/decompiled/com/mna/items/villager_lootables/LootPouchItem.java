package com.mna.items.villager_lootables;

import com.mna.api.tools.MATags;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;

public class LootPouchItem extends Item {

    public final ResourceLocation POUCH_ITEMS;

    private final Consumer<ItemStack> randomizer;

    public LootPouchItem(ResourceLocation itemTag, Consumer<ItemStack> randomizerFunction) {
        super(new Item.Properties());
        this.POUCH_ITEMS = itemTag;
        this.randomizer = randomizerFunction;
    }

    public LootPouchItem(ResourceLocation itemTag) {
        this(itemTag, null);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack stack = player.m_21120_(hand);
        if (player.m_9236_().isClientSide()) {
            return InteractionResultHolder.pass(stack);
        } else {
            this.addStackToPlayer(player, new ItemStack(Items.LEATHER));
            this.addStackToPlayer(player, new ItemStack(Items.STRING));
            int numTypes = 2;
            switch((int) (Math.random() * 10.0)) {
                case 6:
                case 7:
                    numTypes = 1;
                    break;
                case 8:
                case 9:
                    numTypes = 3;
            }
            for (int i = 0; i < numTypes; i++) {
                this.addStackToPlayer(player, this.getRandomLootStack());
            }
            stack.shrink(1);
            return InteractionResultHolder.consume(stack);
        }
    }

    private ItemStack getRandomLootStack() {
        List<Item> items = MATags.getItemTagContents(this.POUCH_ITEMS);
        if (items.size() == 0) {
            return ItemStack.EMPTY;
        } else {
            ItemStack stack = new ItemStack((ItemLike) items.get((int) (Math.random() * (double) items.size())));
            if (this.randomizer != null) {
                this.randomizer.accept(stack);
            }
            return stack;
        }
    }

    private void addStackToPlayer(Player player, ItemStack stack) {
        if (!player.addItem(stack)) {
            player.drop(stack, true);
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, Level world, List<Component> list, TooltipFlag flag) {
        super.appendHoverText(stack, world, list, flag);
        list.add(Component.translatable(this.m_5524_() + ".desc").withStyle(ChatFormatting.GRAY).withStyle(ChatFormatting.ITALIC));
    }
}
package com.rekindled.embers.item;

import java.util.ArrayList;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class AlchemyHintItem extends Item {

    public AlchemyHintItem(Item.Properties pProperties) {
        super(pProperties);
    }

    public static int getBlackPins(ItemStack stack) {
        return stack.hasTag() ? stack.getTag().getInt("blackPins") : 0;
    }

    public static int getWhitePins(ItemStack stack) {
        return stack.hasTag() ? stack.getTag().getInt("whitePins") : 0;
    }

    public static ArrayList<ItemStack> getAspects(ItemStack stack) {
        ArrayList<ItemStack> items = new ArrayList();
        if (stack.hasTag() && stack.getTag().contains("aspects")) {
            for (Tag nbt : stack.getTag().getList("aspects", 10)) {
                items.add(ItemStack.of((CompoundTag) nbt));
            }
        }
        return items;
    }

    public static ArrayList<ItemStack> getInputs(ItemStack stack) {
        ArrayList<ItemStack> items = new ArrayList();
        if (stack.hasTag() && stack.getTag().contains("inputs")) {
            for (Tag nbt : stack.getTag().getList("inputs", 10)) {
                items.add(ItemStack.of((CompoundTag) nbt));
            }
        }
        return items;
    }

    public static ItemStack getResult(ItemStack stack) {
        return stack.hasTag() && stack.getTag().contains("result") ? ItemStack.of(stack.getTag().getCompound("result")) : ItemStack.EMPTY;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.m_21120_(hand);
        if (!level.isClientSide) {
            return InteractionResultHolder.consume(stack);
        } else {
            int blackPins = getBlackPins(stack);
            int whitePins = getWhitePins(stack);
            String black = blackPins == 1 ? I18n.get("embers.alchemy_hint.black.one") : I18n.get("embers.alchemy_hint.black", blackPins);
            String white = whitePins == 1 ? I18n.get("embers.alchemy_hint.white.one") : I18n.get("embers.alchemy_hint.white", whitePins);
            if (blackPins != 0) {
                if (whitePins != 0) {
                    player.displayClientMessage(Component.translatable("embers.alchemy_hint", I18n.get("embers.alchemy_hint.and", black, white)), false);
                    return InteractionResultHolder.consume(stack);
                } else {
                    player.displayClientMessage(Component.translatable("embers.alchemy_hint", black), false);
                    return InteractionResultHolder.consume(stack);
                }
            } else if (whitePins != 0) {
                player.displayClientMessage(Component.translatable("embers.alchemy_hint", white), false);
                return InteractionResultHolder.consume(stack);
            } else {
                player.displayClientMessage(Component.translatable("embers.alchemy_hint", I18n.get("embers.alchemy_hint.none")), false);
                return InteractionResultHolder.consume(stack);
            }
        }
    }
}
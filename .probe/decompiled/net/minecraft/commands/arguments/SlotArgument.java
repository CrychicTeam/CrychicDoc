package net.minecraft.commands.arguments;

import com.google.common.collect.Maps;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import net.minecraft.Util;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;

public class SlotArgument implements ArgumentType<Integer> {

    private static final Collection<String> EXAMPLES = Arrays.asList("container.5", "12", "weapon");

    private static final DynamicCommandExceptionType ERROR_UNKNOWN_SLOT = new DynamicCommandExceptionType(p_111283_ -> Component.translatable("slot.unknown", p_111283_));

    private static final Map<String, Integer> SLOTS = Util.make(Maps.newHashMap(), p_111285_ -> {
        for (int $$1 = 0; $$1 < 54; $$1++) {
            p_111285_.put("container." + $$1, $$1);
        }
        for (int $$2 = 0; $$2 < 9; $$2++) {
            p_111285_.put("hotbar." + $$2, $$2);
        }
        for (int $$3 = 0; $$3 < 27; $$3++) {
            p_111285_.put("inventory." + $$3, 9 + $$3);
        }
        for (int $$4 = 0; $$4 < 27; $$4++) {
            p_111285_.put("enderchest." + $$4, 200 + $$4);
        }
        for (int $$5 = 0; $$5 < 8; $$5++) {
            p_111285_.put("villager." + $$5, 300 + $$5);
        }
        for (int $$6 = 0; $$6 < 15; $$6++) {
            p_111285_.put("horse." + $$6, 500 + $$6);
        }
        p_111285_.put("weapon", EquipmentSlot.MAINHAND.getIndex(98));
        p_111285_.put("weapon.mainhand", EquipmentSlot.MAINHAND.getIndex(98));
        p_111285_.put("weapon.offhand", EquipmentSlot.OFFHAND.getIndex(98));
        p_111285_.put("armor.head", EquipmentSlot.HEAD.getIndex(100));
        p_111285_.put("armor.chest", EquipmentSlot.CHEST.getIndex(100));
        p_111285_.put("armor.legs", EquipmentSlot.LEGS.getIndex(100));
        p_111285_.put("armor.feet", EquipmentSlot.FEET.getIndex(100));
        p_111285_.put("horse.saddle", 400);
        p_111285_.put("horse.armor", 401);
        p_111285_.put("horse.chest", 499);
    });

    public static SlotArgument slot() {
        return new SlotArgument();
    }

    public static int getSlot(CommandContext<CommandSourceStack> commandContextCommandSourceStack0, String string1) {
        return (Integer) commandContextCommandSourceStack0.getArgument(string1, Integer.class);
    }

    public Integer parse(StringReader stringReader0) throws CommandSyntaxException {
        String $$1 = stringReader0.readUnquotedString();
        if (!SLOTS.containsKey($$1)) {
            throw ERROR_UNKNOWN_SLOT.create($$1);
        } else {
            return (Integer) SLOTS.get($$1);
        }
    }

    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> commandContextS0, SuggestionsBuilder suggestionsBuilder1) {
        return SharedSuggestionProvider.suggest(SLOTS.keySet(), suggestionsBuilder1);
    }

    public Collection<String> getExamples() {
        return EXAMPLES;
    }
}
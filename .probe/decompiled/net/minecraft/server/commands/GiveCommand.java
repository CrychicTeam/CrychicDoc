package net.minecraft.server.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.Collection;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.item.ItemArgument;
import net.minecraft.commands.arguments.item.ItemInput;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;

public class GiveCommand {

    public static final int MAX_ALLOWED_ITEMSTACKS = 100;

    public static void register(CommandDispatcher<CommandSourceStack> commandDispatcherCommandSourceStack0, CommandBuildContext commandBuildContext1) {
        commandDispatcherCommandSourceStack0.register((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("give").requires(p_137777_ -> p_137777_.hasPermission(2))).then(Commands.argument("targets", EntityArgument.players()).then(((RequiredArgumentBuilder) Commands.argument("item", ItemArgument.item(commandBuildContext1)).executes(p_137784_ -> giveItem((CommandSourceStack) p_137784_.getSource(), ItemArgument.getItem(p_137784_, "item"), EntityArgument.getPlayers(p_137784_, "targets"), 1))).then(Commands.argument("count", IntegerArgumentType.integer(1)).executes(p_137775_ -> giveItem((CommandSourceStack) p_137775_.getSource(), ItemArgument.getItem(p_137775_, "item"), EntityArgument.getPlayers(p_137775_, "targets"), IntegerArgumentType.getInteger(p_137775_, "count")))))));
    }

    private static int giveItem(CommandSourceStack commandSourceStack0, ItemInput itemInput1, Collection<ServerPlayer> collectionServerPlayer2, int int3) throws CommandSyntaxException {
        int $$4 = itemInput1.getItem().getMaxStackSize();
        int $$5 = $$4 * 100;
        ItemStack $$6 = itemInput1.createItemStack(int3, false);
        if (int3 > $$5) {
            commandSourceStack0.sendFailure(Component.translatable("commands.give.failed.toomanyitems", $$5, $$6.getDisplayName()));
            return 0;
        } else {
            for (ServerPlayer $$7 : collectionServerPlayer2) {
                int $$8 = int3;
                while ($$8 > 0) {
                    int $$9 = Math.min($$4, $$8);
                    $$8 -= $$9;
                    ItemStack $$10 = itemInput1.createItemStack($$9, false);
                    boolean $$11 = $$7.m_150109_().add($$10);
                    if ($$11 && $$10.isEmpty()) {
                        $$10.setCount(1);
                        ItemEntity $$13 = $$7.m_36176_($$10, false);
                        if ($$13 != null) {
                            $$13.makeFakeItem();
                        }
                        $$7.m_9236_().playSound(null, $$7.m_20185_(), $$7.m_20186_(), $$7.m_20189_(), SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.2F, (($$7.m_217043_().nextFloat() - $$7.m_217043_().nextFloat()) * 0.7F + 1.0F) * 2.0F);
                        $$7.f_36096_.broadcastChanges();
                    } else {
                        ItemEntity $$12 = $$7.m_36176_($$10, false);
                        if ($$12 != null) {
                            $$12.setNoPickUpDelay();
                            $$12.setTarget($$7.m_20148_());
                        }
                    }
                }
            }
            if (collectionServerPlayer2.size() == 1) {
                commandSourceStack0.sendSuccess(() -> Component.translatable("commands.give.success.single", int3, $$6.getDisplayName(), ((ServerPlayer) collectionServerPlayer2.iterator().next()).m_5446_()), true);
            } else {
                commandSourceStack0.sendSuccess(() -> Component.translatable("commands.give.success.single", int3, $$6.getDisplayName(), collectionServerPlayer2.size()), true);
            }
            return collectionServerPlayer2.size();
        }
    }
}
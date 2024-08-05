package net.minecraft.server.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import java.util.Collection;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceArgument;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

public class EnchantCommand {

    private static final DynamicCommandExceptionType ERROR_NOT_LIVING_ENTITY = new DynamicCommandExceptionType(p_137029_ -> Component.translatable("commands.enchant.failed.entity", p_137029_));

    private static final DynamicCommandExceptionType ERROR_NO_ITEM = new DynamicCommandExceptionType(p_137027_ -> Component.translatable("commands.enchant.failed.itemless", p_137027_));

    private static final DynamicCommandExceptionType ERROR_INCOMPATIBLE = new DynamicCommandExceptionType(p_137020_ -> Component.translatable("commands.enchant.failed.incompatible", p_137020_));

    private static final Dynamic2CommandExceptionType ERROR_LEVEL_TOO_HIGH = new Dynamic2CommandExceptionType((p_137022_, p_137023_) -> Component.translatable("commands.enchant.failed.level", p_137022_, p_137023_));

    private static final SimpleCommandExceptionType ERROR_NOTHING_HAPPENED = new SimpleCommandExceptionType(Component.translatable("commands.enchant.failed"));

    public static void register(CommandDispatcher<CommandSourceStack> commandDispatcherCommandSourceStack0, CommandBuildContext commandBuildContext1) {
        commandDispatcherCommandSourceStack0.register((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("enchant").requires(p_137013_ -> p_137013_.hasPermission(2))).then(Commands.argument("targets", EntityArgument.entities()).then(((RequiredArgumentBuilder) Commands.argument("enchantment", ResourceArgument.resource(commandBuildContext1, Registries.ENCHANTMENT)).executes(p_248131_ -> enchant((CommandSourceStack) p_248131_.getSource(), EntityArgument.getEntities(p_248131_, "targets"), ResourceArgument.getEnchantment(p_248131_, "enchantment"), 1))).then(Commands.argument("level", IntegerArgumentType.integer(0)).executes(p_248132_ -> enchant((CommandSourceStack) p_248132_.getSource(), EntityArgument.getEntities(p_248132_, "targets"), ResourceArgument.getEnchantment(p_248132_, "enchantment"), IntegerArgumentType.getInteger(p_248132_, "level")))))));
    }

    private static int enchant(CommandSourceStack commandSourceStack0, Collection<? extends Entity> collectionExtendsEntity1, Holder<Enchantment> holderEnchantment2, int int3) throws CommandSyntaxException {
        Enchantment $$4 = holderEnchantment2.value();
        if (int3 > $$4.getMaxLevel()) {
            throw ERROR_LEVEL_TOO_HIGH.create(int3, $$4.getMaxLevel());
        } else {
            int $$5 = 0;
            for (Entity $$6 : collectionExtendsEntity1) {
                if ($$6 instanceof LivingEntity) {
                    LivingEntity $$7 = (LivingEntity) $$6;
                    ItemStack $$8 = $$7.getMainHandItem();
                    if (!$$8.isEmpty()) {
                        if ($$4.canEnchant($$8) && EnchantmentHelper.isEnchantmentCompatible(EnchantmentHelper.getEnchantments($$8).keySet(), $$4)) {
                            $$8.enchant($$4, int3);
                            $$5++;
                        } else if (collectionExtendsEntity1.size() == 1) {
                            throw ERROR_INCOMPATIBLE.create($$8.getItem().getName($$8).getString());
                        }
                    } else if (collectionExtendsEntity1.size() == 1) {
                        throw ERROR_NO_ITEM.create($$7.m_7755_().getString());
                    }
                } else if (collectionExtendsEntity1.size() == 1) {
                    throw ERROR_NOT_LIVING_ENTITY.create($$6.getName().getString());
                }
            }
            if ($$5 == 0) {
                throw ERROR_NOTHING_HAPPENED.create();
            } else {
                if (collectionExtendsEntity1.size() == 1) {
                    commandSourceStack0.sendSuccess(() -> Component.translatable("commands.enchant.success.single", $$4.getFullname(int3), ((Entity) collectionExtendsEntity1.iterator().next()).getDisplayName()), true);
                } else {
                    commandSourceStack0.sendSuccess(() -> Component.translatable("commands.enchant.success.multiple", $$4.getFullname(int3), collectionExtendsEntity1.size()), true);
                }
                return $$5;
            }
        }
    }
}
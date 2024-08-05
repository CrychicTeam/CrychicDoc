package net.minecraft.server.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
import com.mojang.brigadier.exceptions.Dynamic3CommandExceptionType;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import java.util.UUID;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceArgument;
import net.minecraft.commands.arguments.UuidArgument;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

public class AttributeCommand {

    private static final DynamicCommandExceptionType ERROR_NOT_LIVING_ENTITY = new DynamicCommandExceptionType(p_212443_ -> Component.translatable("commands.attribute.failed.entity", p_212443_));

    private static final Dynamic2CommandExceptionType ERROR_NO_SUCH_ATTRIBUTE = new Dynamic2CommandExceptionType((p_212445_, p_212446_) -> Component.translatable("commands.attribute.failed.no_attribute", p_212445_, p_212446_));

    private static final Dynamic3CommandExceptionType ERROR_NO_SUCH_MODIFIER = new Dynamic3CommandExceptionType((p_212448_, p_212449_, p_212450_) -> Component.translatable("commands.attribute.failed.no_modifier", p_212449_, p_212448_, p_212450_));

    private static final Dynamic3CommandExceptionType ERROR_MODIFIER_ALREADY_PRESENT = new Dynamic3CommandExceptionType((p_136497_, p_136498_, p_136499_) -> Component.translatable("commands.attribute.failed.modifier_already_present", p_136499_, p_136498_, p_136497_));

    public static void register(CommandDispatcher<CommandSourceStack> commandDispatcherCommandSourceStack0, CommandBuildContext commandBuildContext1) {
        commandDispatcherCommandSourceStack0.register((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("attribute").requires(p_212441_ -> p_212441_.hasPermission(2))).then(Commands.argument("target", EntityArgument.entity()).then(((RequiredArgumentBuilder) ((RequiredArgumentBuilder) Commands.argument("attribute", ResourceArgument.resource(commandBuildContext1, Registries.ATTRIBUTE)).then(((LiteralArgumentBuilder) Commands.literal("get").executes(p_248109_ -> getAttributeValue((CommandSourceStack) p_248109_.getSource(), EntityArgument.getEntity(p_248109_, "target"), ResourceArgument.getAttribute(p_248109_, "attribute"), 1.0))).then(Commands.argument("scale", DoubleArgumentType.doubleArg()).executes(p_248104_ -> getAttributeValue((CommandSourceStack) p_248104_.getSource(), EntityArgument.getEntity(p_248104_, "target"), ResourceArgument.getAttribute(p_248104_, "attribute"), DoubleArgumentType.getDouble(p_248104_, "scale")))))).then(((LiteralArgumentBuilder) Commands.literal("base").then(Commands.literal("set").then(Commands.argument("value", DoubleArgumentType.doubleArg()).executes(p_248102_ -> setAttributeBase((CommandSourceStack) p_248102_.getSource(), EntityArgument.getEntity(p_248102_, "target"), ResourceArgument.getAttribute(p_248102_, "attribute"), DoubleArgumentType.getDouble(p_248102_, "value")))))).then(((LiteralArgumentBuilder) Commands.literal("get").executes(p_248112_ -> getAttributeBase((CommandSourceStack) p_248112_.getSource(), EntityArgument.getEntity(p_248112_, "target"), ResourceArgument.getAttribute(p_248112_, "attribute"), 1.0))).then(Commands.argument("scale", DoubleArgumentType.doubleArg()).executes(p_248106_ -> getAttributeBase((CommandSourceStack) p_248106_.getSource(), EntityArgument.getEntity(p_248106_, "target"), ResourceArgument.getAttribute(p_248106_, "attribute"), DoubleArgumentType.getDouble(p_248106_, "scale"))))))).then(((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("modifier").then(Commands.literal("add").then(Commands.argument("uuid", UuidArgument.uuid()).then(Commands.argument("name", StringArgumentType.string()).then(((RequiredArgumentBuilder) ((RequiredArgumentBuilder) Commands.argument("value", DoubleArgumentType.doubleArg()).then(Commands.literal("add").executes(p_248105_ -> addModifier((CommandSourceStack) p_248105_.getSource(), EntityArgument.getEntity(p_248105_, "target"), ResourceArgument.getAttribute(p_248105_, "attribute"), UuidArgument.getUuid(p_248105_, "uuid"), StringArgumentType.getString(p_248105_, "name"), DoubleArgumentType.getDouble(p_248105_, "value"), AttributeModifier.Operation.ADDITION)))).then(Commands.literal("multiply").executes(p_248107_ -> addModifier((CommandSourceStack) p_248107_.getSource(), EntityArgument.getEntity(p_248107_, "target"), ResourceArgument.getAttribute(p_248107_, "attribute"), UuidArgument.getUuid(p_248107_, "uuid"), StringArgumentType.getString(p_248107_, "name"), DoubleArgumentType.getDouble(p_248107_, "value"), AttributeModifier.Operation.MULTIPLY_TOTAL)))).then(Commands.literal("multiply_base").executes(p_248108_ -> addModifier((CommandSourceStack) p_248108_.getSource(), EntityArgument.getEntity(p_248108_, "target"), ResourceArgument.getAttribute(p_248108_, "attribute"), UuidArgument.getUuid(p_248108_, "uuid"), StringArgumentType.getString(p_248108_, "name"), DoubleArgumentType.getDouble(p_248108_, "value"), AttributeModifier.Operation.MULTIPLY_BASE)))))))).then(Commands.literal("remove").then(Commands.argument("uuid", UuidArgument.uuid()).executes(p_248103_ -> removeModifier((CommandSourceStack) p_248103_.getSource(), EntityArgument.getEntity(p_248103_, "target"), ResourceArgument.getAttribute(p_248103_, "attribute"), UuidArgument.getUuid(p_248103_, "uuid")))))).then(Commands.literal("value").then(Commands.literal("get").then(((RequiredArgumentBuilder) Commands.argument("uuid", UuidArgument.uuid()).executes(p_248110_ -> getAttributeModifier((CommandSourceStack) p_248110_.getSource(), EntityArgument.getEntity(p_248110_, "target"), ResourceArgument.getAttribute(p_248110_, "attribute"), UuidArgument.getUuid(p_248110_, "uuid"), 1.0))).then(Commands.argument("scale", DoubleArgumentType.doubleArg()).executes(p_248111_ -> getAttributeModifier((CommandSourceStack) p_248111_.getSource(), EntityArgument.getEntity(p_248111_, "target"), ResourceArgument.getAttribute(p_248111_, "attribute"), UuidArgument.getUuid(p_248111_, "uuid"), DoubleArgumentType.getDouble(p_248111_, "scale")))))))))));
    }

    private static AttributeInstance getAttributeInstance(Entity entity0, Holder<Attribute> holderAttribute1) throws CommandSyntaxException {
        AttributeInstance $$2 = getLivingEntity(entity0).getAttributes().getInstance(holderAttribute1);
        if ($$2 == null) {
            throw ERROR_NO_SUCH_ATTRIBUTE.create(entity0.getName(), getAttributeDescription(holderAttribute1));
        } else {
            return $$2;
        }
    }

    private static LivingEntity getLivingEntity(Entity entity0) throws CommandSyntaxException {
        if (!(entity0 instanceof LivingEntity)) {
            throw ERROR_NOT_LIVING_ENTITY.create(entity0.getName());
        } else {
            return (LivingEntity) entity0;
        }
    }

    private static LivingEntity getEntityWithAttribute(Entity entity0, Holder<Attribute> holderAttribute1) throws CommandSyntaxException {
        LivingEntity $$2 = getLivingEntity(entity0);
        if (!$$2.getAttributes().hasAttribute(holderAttribute1)) {
            throw ERROR_NO_SUCH_ATTRIBUTE.create(entity0.getName(), getAttributeDescription(holderAttribute1));
        } else {
            return $$2;
        }
    }

    private static int getAttributeValue(CommandSourceStack commandSourceStack0, Entity entity1, Holder<Attribute> holderAttribute2, double double3) throws CommandSyntaxException {
        LivingEntity $$4 = getEntityWithAttribute(entity1, holderAttribute2);
        double $$5 = $$4.getAttributeValue(holderAttribute2);
        commandSourceStack0.sendSuccess(() -> Component.translatable("commands.attribute.value.get.success", getAttributeDescription(holderAttribute2), entity1.getName(), $$5), false);
        return (int) ($$5 * double3);
    }

    private static int getAttributeBase(CommandSourceStack commandSourceStack0, Entity entity1, Holder<Attribute> holderAttribute2, double double3) throws CommandSyntaxException {
        LivingEntity $$4 = getEntityWithAttribute(entity1, holderAttribute2);
        double $$5 = $$4.getAttributeBaseValue(holderAttribute2);
        commandSourceStack0.sendSuccess(() -> Component.translatable("commands.attribute.base_value.get.success", getAttributeDescription(holderAttribute2), entity1.getName(), $$5), false);
        return (int) ($$5 * double3);
    }

    private static int getAttributeModifier(CommandSourceStack commandSourceStack0, Entity entity1, Holder<Attribute> holderAttribute2, UUID uUID3, double double4) throws CommandSyntaxException {
        LivingEntity $$5 = getEntityWithAttribute(entity1, holderAttribute2);
        AttributeMap $$6 = $$5.getAttributes();
        if (!$$6.hasModifier(holderAttribute2, uUID3)) {
            throw ERROR_NO_SUCH_MODIFIER.create(entity1.getName(), getAttributeDescription(holderAttribute2), uUID3);
        } else {
            double $$7 = $$6.getModifierValue(holderAttribute2, uUID3);
            commandSourceStack0.sendSuccess(() -> Component.translatable("commands.attribute.modifier.value.get.success", uUID3, getAttributeDescription(holderAttribute2), entity1.getName(), $$7), false);
            return (int) ($$7 * double4);
        }
    }

    private static int setAttributeBase(CommandSourceStack commandSourceStack0, Entity entity1, Holder<Attribute> holderAttribute2, double double3) throws CommandSyntaxException {
        getAttributeInstance(entity1, holderAttribute2).setBaseValue(double3);
        commandSourceStack0.sendSuccess(() -> Component.translatable("commands.attribute.base_value.set.success", getAttributeDescription(holderAttribute2), entity1.getName(), double3), false);
        return 1;
    }

    private static int addModifier(CommandSourceStack commandSourceStack0, Entity entity1, Holder<Attribute> holderAttribute2, UUID uUID3, String string4, double double5, AttributeModifier.Operation attributeModifierOperation6) throws CommandSyntaxException {
        AttributeInstance $$7 = getAttributeInstance(entity1, holderAttribute2);
        AttributeModifier $$8 = new AttributeModifier(uUID3, string4, double5, attributeModifierOperation6);
        if ($$7.hasModifier($$8)) {
            throw ERROR_MODIFIER_ALREADY_PRESENT.create(entity1.getName(), getAttributeDescription(holderAttribute2), uUID3);
        } else {
            $$7.addPermanentModifier($$8);
            commandSourceStack0.sendSuccess(() -> Component.translatable("commands.attribute.modifier.add.success", uUID3, getAttributeDescription(holderAttribute2), entity1.getName()), false);
            return 1;
        }
    }

    private static int removeModifier(CommandSourceStack commandSourceStack0, Entity entity1, Holder<Attribute> holderAttribute2, UUID uUID3) throws CommandSyntaxException {
        AttributeInstance $$4 = getAttributeInstance(entity1, holderAttribute2);
        if ($$4.removePermanentModifier(uUID3)) {
            commandSourceStack0.sendSuccess(() -> Component.translatable("commands.attribute.modifier.remove.success", uUID3, getAttributeDescription(holderAttribute2), entity1.getName()), false);
            return 1;
        } else {
            throw ERROR_NO_SUCH_MODIFIER.create(entity1.getName(), getAttributeDescription(holderAttribute2), uUID3);
        }
    }

    private static Component getAttributeDescription(Holder<Attribute> holderAttribute0) {
        return Component.translatable(holderAttribute0.value().getDescriptionId());
    }
}
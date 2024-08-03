package dev.xkmc.modulargolems.content.item.equipments;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.ImmutableMultimap.Builder;
import dev.xkmc.l2library.util.math.MathHelper;
import dev.xkmc.modulargolems.init.data.MGLangData;
import java.util.EnumMap;
import java.util.List;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.function.Consumer;
import java.util.function.Supplier;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public abstract class GolemEquipmentItem extends Item {

    protected static final EnumMap<EquipmentSlot, UUID> UUID = new EnumMap(EquipmentSlot.class);

    private final EquipmentSlot slot;

    private final Supplier<EntityType<?>> type;

    private final Multimap<Attribute, AttributeModifier> defaultModifiers;

    public GolemEquipmentItem(Item.Properties properties, EquipmentSlot slot, Supplier<EntityType<?>> type, Consumer<Builder<Attribute, AttributeModifier>> attr) {
        super(properties);
        this.slot = slot;
        this.type = type;
        Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        attr.accept(builder);
        this.defaultModifiers = builder.build();
    }

    public EquipmentSlot getSlot() {
        return this.slot;
    }

    public boolean isFor(EntityType<?> type) {
        return this.type.get() == type;
    }

    public Multimap<Attribute, AttributeModifier> getGolemModifiers(ItemStack stack, @Nullable Entity entity, EquipmentSlot slot) {
        return (Multimap<Attribute, AttributeModifier>) (this.slot != slot || entity != null && this.type.get() != entity.getType() ? ImmutableMultimap.of() : this.getDefaultGolemModifiers(stack, slot));
    }

    protected Multimap<Attribute, AttributeModifier> getDefaultGolemModifiers(ItemStack stack, EquipmentSlot slot) {
        return this.defaultModifiers;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
        list.add(MGLangData.GOLEM_EQUIPMENT.get(((EntityType) this.type.get()).getDescription().copy().withStyle(ChatFormatting.GOLD)));
        Multimap<Attribute, AttributeModifier> multimap = this.getGolemModifiers(stack, null, this.slot);
        if (!multimap.isEmpty()) {
            list.add(CommonComponents.EMPTY);
            list.add(Component.translatable("item.modifiers." + this.slot.getName()).withStyle(ChatFormatting.GRAY));
            for (Entry<Attribute, AttributeModifier> entry : multimap.entries()) {
                AttributeModifier attr = (AttributeModifier) entry.getValue();
                double val = attr.getAmount();
                double disp;
                if (attr.getOperation() == AttributeModifier.Operation.MULTIPLY_BASE || attr.getOperation() == AttributeModifier.Operation.MULTIPLY_TOTAL) {
                    disp = val * 100.0;
                } else if (((Attribute) entry.getKey()).equals(Attributes.KNOCKBACK_RESISTANCE)) {
                    disp = val * 10.0;
                } else {
                    disp = val;
                }
                if (val > 0.0) {
                    list.add(Component.translatable("attribute.modifier.plus." + attr.getOperation().toValue(), ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(disp), Component.translatable(((Attribute) entry.getKey()).getDescriptionId())).withStyle(ChatFormatting.BLUE));
                } else if (val < 0.0) {
                    disp *= -1.0;
                    list.add(Component.translatable("attribute.modifier.take." + attr.getOperation().toValue(), ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(disp), Component.translatable(((Attribute) entry.getKey()).getDescriptionId())).withStyle(ChatFormatting.RED));
                }
            }
        }
    }

    static {
        for (EquipmentSlot e : EquipmentSlot.values()) {
            UUID.put(e, MathHelper.getUUIDFromString("modulargolems:metalgolem_" + e.getName()));
        }
    }
}
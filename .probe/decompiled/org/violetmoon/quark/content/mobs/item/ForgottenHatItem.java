package org.violetmoon.quark.content.mobs.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.ImmutableMultimap.Builder;
import java.util.UUID;
import java.util.function.BooleanSupplier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.common.ForgeMod;
import org.jetbrains.annotations.NotNull;
import org.violetmoon.quark.base.Quark;
import org.violetmoon.zeta.item.IZetaItem;
import org.violetmoon.zeta.item.ZetaArmorItem;
import org.violetmoon.zeta.module.ZetaModule;
import org.violetmoon.zeta.registry.CreativeTabManager;

public class ForgottenHatItem extends ZetaArmorItem implements IZetaItem {

    private static final String TEXTURE = "quark:textures/misc/forgotten_hat_worn.png";

    private final ZetaModule module;

    private Multimap<Attribute, AttributeModifier> attributes;

    public ForgottenHatItem(ZetaModule module) {
        super(ArmorMaterials.LEATHER, ArmorItem.Type.HELMET, new Item.Properties().stacksTo(1).durability(0).rarity(Rarity.RARE));
        Quark.ZETA.registry.registerItem(this, "forgotten_hat");
        this.module = module;
        CreativeTabManager.addToCreativeTabNextTo(CreativeModeTabs.TOOLS_AND_UTILITIES, this, Items.SADDLE, true);
    }

    @Override
    public ZetaModule getModule() {
        return this.module;
    }

    public IZetaItem setCondition(BooleanSupplier condition) {
        return this;
    }

    @Override
    public boolean doesConditionApply() {
        return true;
    }

    @Override
    public boolean canEquipZeta(ItemStack stack, EquipmentSlot equipmentSlot, Entity ent) {
        return equipmentSlot == EquipmentSlot.HEAD;
    }

    @Override
    public String getArmorTextureZeta(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        return "quark:textures/misc/forgotten_hat_worn.png";
    }

    @Override
    public boolean isEnchantable(@NotNull ItemStack stack) {
        return false;
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot slot) {
        if (this.attributes == null) {
            Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
            UUID uuid = UUID.fromString("2AD3F246-FEE1-4E67-B886-69FD380BB150");
            builder.put(Attributes.ARMOR, new AttributeModifier(uuid, "Armor modifier", 1.0, AttributeModifier.Operation.ADDITION));
            builder.put(Attributes.LUCK, new AttributeModifier(uuid, "Armor luck modifier", 1.0, AttributeModifier.Operation.ADDITION));
            builder.put(ForgeMod.ENTITY_REACH.get(), new AttributeModifier(uuid, "Armor entity reach modifier", 2.0, AttributeModifier.Operation.ADDITION));
            builder.put(ForgeMod.BLOCK_REACH.get(), new AttributeModifier(uuid, "Armor block reach modifier", 2.0, AttributeModifier.Operation.ADDITION));
            this.attributes = builder.build();
        }
        return slot == this.m_40402_() ? this.attributes : super.m_7167_(slot);
    }
}
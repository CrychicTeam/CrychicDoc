package com.mna.items.relic;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.ImmutableMultimap.Builder;
import com.mna.api.items.IRelic;
import com.mna.gui.containers.providers.NamedRunicMalus;
import com.mna.inventory.ItemInventoryBase;
import com.mna.items.base.ItemBagBase;
import com.mna.items.filters.ItemFilterGroup;
import com.mna.items.renderers.obj_gecko.RunicMalusRenderer;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.common.util.NonNullLazy;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public class ItemRunicMalus extends ItemBagBase implements GeoItem, IRelic {

    public static final int INVENTORY_SIZE = 6;

    private AnimatableInstanceCache animCache = GeckoLibUtil.createInstanceCache(this);

    private static final String CONTROLLER_NAME = "runicMalusController";

    private final Multimap<Attribute, AttributeModifier> defaultModifiers;

    public ItemRunicMalus() {
        super(new Item.Properties().rarity(Rarity.EPIC).fireResistant().stacksTo(1));
        Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(f_41374_, "Weapon modifier", 10.0, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(f_41375_, "Weapon modifier", -3.0, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_KNOCKBACK, new AttributeModifier(UUID.fromString("e6b1b07c-9824-4d3e-be88-24714cc3fe0c"), "Weapon modifier", 2.0, AttributeModifier.Operation.ADDITION));
        this.defaultModifiers = builder.build();
    }

    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {

            private final NonNullLazy<BlockEntityWithoutLevelRenderer> ister = NonNullLazy.of(() -> new RunicMalusRenderer(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels()));

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return this.ister.get();
            }
        });
    }

    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return false;
    }

    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return false;
    }

    @Override
    public ItemFilterGroup filterGroup() {
        return ItemFilterGroup.ANY_ENCHANTED_RUNE;
    }

    @Override
    public MenuProvider getProvider(ItemStack stack) {
        return new NamedRunicMalus(stack);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        return !world.isClientSide && this.openGuiIfModifierPressed(player.m_21120_(hand), player, world) ? new InteractionResultHolder<>(InteractionResult.SUCCESS, player.m_21120_(hand)) : new InteractionResultHolder<>(InteractionResult.PASS, player.m_21120_(hand));
    }

    @Override
    public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip.add(Component.translatable("item.mna.runic_malus.lore_1").withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable("item.mna.runic_malus.lore_2").withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal("  "));
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot equipmentSlot0) {
        return equipmentSlot0 == EquipmentSlot.MAINHAND ? this.defaultModifiers : super.m_7167_(equipmentSlot0);
    }

    public static NonNullList<ItemStack> getRunes(ItemStack itemStack) {
        return new ItemInventoryBase(itemStack, 6).getAllItems();
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.animCache;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar registrar) {
        registrar.add(new AnimationController<>(this, state -> state.setAndContinue(RawAnimation.begin().thenLoop("animation.malus.idle"))));
    }
}
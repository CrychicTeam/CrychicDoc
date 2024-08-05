package com.mna.items.relic;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.ImmutableMultimap.Builder;
import com.mna.api.items.IRelic;
import com.mna.api.items.IShowHud;
import com.mna.api.spells.ComponentApplicationResult;
import com.mna.api.spells.parts.SpellEffect;
import com.mna.api.spells.targeting.SpellContext;
import com.mna.api.spells.targeting.SpellSource;
import com.mna.api.spells.targeting.SpellTarget;
import com.mna.api.timing.DelayedEventQueue;
import com.mna.api.timing.TimedDelayedSpellEffect;
import com.mna.capabilities.playerdata.magic.PlayerMagicProvider;
import com.mna.gui.containers.providers.NamedAstroBlade;
import com.mna.inventory.ItemInventoryBase;
import com.mna.items.ItemInit;
import com.mna.items.base.ItemBagBase;
import com.mna.items.filters.ItemFilterGroup;
import com.mna.items.renderers.AstroBladeRenderer;
import com.mna.items.sorcery.ItemSpell;
import com.mna.spells.SpellCaster;
import com.mna.spells.crafting.SpellRecipe;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
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
import org.apache.commons.lang3.mutable.MutableBoolean;

public class ItemAstroBlade extends ItemBagBase implements IShowHud, IRelic {

    public static final String NBT_AS_WARDEN = "as_warden";

    public static final int INVENTORY_SIZE = 2;

    public static final int SLOT_HANDLE = 0;

    public static final int SLOT_BLADE = 1;

    private final Multimap<Attribute, AttributeModifier> defaultModifiers;

    public ItemAstroBlade() {
        super(new Item.Properties().rarity(Rarity.EPIC).fireResistant().stacksTo(1));
        Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(f_41374_, "Weapon modifier", 10.0, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(f_41375_, "Weapon modifier", -2.4F, AttributeModifier.Operation.ADDITION));
        this.defaultModifiers = builder.build();
    }

    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {

            private final NonNullLazy<BlockEntityWithoutLevelRenderer> ister = NonNullLazy.of(() -> new AstroBladeRenderer(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels()));

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
        return ItemFilterGroup.ANY_SPELL;
    }

    @Override
    public MenuProvider getProvider(ItemStack stack) {
        return new NamedAstroBlade(stack);
    }

    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
        ItemInventoryBase inv = new ItemInventoryBase(stack);
        ItemStack bladeSlot = inv.getStackInSlot(1);
        if (bladeSlot.getItem() == ItemInit.ENCHANTED_VELLUM.get()) {
            return super.onLeftClickEntity(stack, player, entity);
        } else {
            if (!bladeSlot.isEmpty() && SpellRecipe.stackContainsSpell(bladeSlot) && !player.m_9236_().isClientSide()) {
                SpellRecipe recipe = SpellRecipe.fromNBT(bladeSlot.getTag());
                if (recipe.isValid()) {
                    MutableBoolean consumed = new MutableBoolean(false);
                    player.getCapability(PlayerMagicProvider.MAGIC).ifPresent(c -> {
                        if (c.getCastingResource().hasEnoughAbsolute(player, recipe.getManaCost())) {
                            c.getCastingResource().consume(player, recipe.getManaCost());
                            consumed.setTrue();
                        }
                    });
                    if (consumed.getValue()) {
                        SpellSource source = new SpellSource(player, InteractionHand.MAIN_HAND);
                        SpellContext context = new SpellContext(player.m_9236_(), recipe);
                        recipe.iterateComponents(c -> {
                            int delay = (int) (c.getValue(com.mna.api.spells.attributes.Attribute.DELAY) * 20.0F);
                            boolean appliedComponent = false;
                            if (delay > 0) {
                                DelayedEventQueue.pushEvent(player.m_9236_(), new TimedDelayedSpellEffect(((SpellEffect) c.getPart()).getRegistryName().toString(), delay, source, new SpellTarget(entity), c, context));
                                appliedComponent = true;
                            } else if (((SpellEffect) c.getPart()).ApplyEffect(source, new SpellTarget(entity), c, context) == ComponentApplicationResult.SUCCESS) {
                                appliedComponent = true;
                            }
                            if (appliedComponent) {
                                SpellCaster.addComponentRoteProgress(player, (SpellEffect) c.getPart());
                            }
                        });
                    }
                }
            }
            return super.onLeftClickEntity(stack, player, entity);
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        if (!world.isClientSide) {
            ItemStack held = player.m_21120_(hand);
            if (this.openGuiIfModifierPressed(held, player, world)) {
                return new InteractionResultHolder<>(InteractionResult.SUCCESS, held);
            }
            ItemInventoryBase inv = new ItemInventoryBase(held);
            ItemStack hiltSlot = inv.getStackInSlot(0);
            if (hiltSlot.getItem() == ItemInit.ENCHANTED_VELLUM.get()) {
                return new InteractionResultHolder<>(InteractionResult.PASS, player.m_21120_(hand));
            }
            if (!hiltSlot.isEmpty()) {
                ItemSpell.castSpellOnUse(hiltSlot, world, player, hand, this::shouldConsumeMana);
                return new InteractionResultHolder<>(InteractionResult.SUCCESS, held);
            }
        }
        return new InteractionResultHolder<>(InteractionResult.PASS, player.m_21120_(hand));
    }

    @Override
    public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip.add(Component.translatable("item.mna.astro_blade.lore_1").withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable("item.mna.astro_blade.lore_2").withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal("  "));
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot equipmentSlot0) {
        return equipmentSlot0 == EquipmentSlot.MAINHAND ? this.defaultModifiers : super.m_7167_(equipmentSlot0);
    }

    @Override
    public int getUseDuration(ItemStack itemStack0) {
        return 99999;
    }

    @Override
    public void onUseTick(Level pLevel, LivingEntity player, ItemStack stack, int count) {
        if (player instanceof Player) {
            ItemSpell.castSpellOnChannelTick(stack, (Player) player, count, this::shouldConsumeChanneledMana);
        }
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entityLiving) {
        if (entityLiving instanceof Player) {
            ((Player) entityLiving).m_21253_();
        }
        return stack;
    }

    protected boolean shouldConsumeMana(ItemStack stack) {
        return true;
    }

    protected boolean shouldConsumeChanneledMana(Player player, ItemStack stack) {
        return true;
    }
}
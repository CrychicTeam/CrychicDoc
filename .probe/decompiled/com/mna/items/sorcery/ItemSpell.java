package com.mna.items.sorcery;

import com.mna.api.items.IShowHud;
import com.mna.api.spells.SpellCastingResult;
import com.mna.api.spells.SpellCastingResultCode;
import com.mna.api.spells.collections.Components;
import com.mna.api.spells.collections.Shapes;
import com.mna.api.spells.parts.SpellEffect;
import com.mna.effects.EffectInit;
import com.mna.gui.containers.providers.NamedSpellAdjust;
import com.mna.gui.containers.providers.NamedSpellCustomization;
import com.mna.items.ItemInit;
import com.mna.items.renderers.ItemSpellRenderer;
import com.mna.spells.SpellCaster;
import com.mna.spells.crafting.SpellRecipe;
import java.util.ArrayList;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.common.util.NonNullLazy;
import net.minecraftforge.network.NetworkHooks;

public class ItemSpell extends ItemSpellRecipe implements IShowHud {

    public ItemSpell() {
        this(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC));
    }

    public ItemSpell(Item.Properties properties) {
        super(properties);
    }

    public static final void castSpellOnChannelTick(ItemStack stack, Player player, int itemInUseCount, BiPredicate<Player, ItemStack> consumeChanneledMana) {
        if (itemInUseCount == 0 || !consumeChanneledMana.test(player, stack) || player.m_21023_(EffectInit.SILENCE.get())) {
            player.m_21253_();
        }
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level worldIn, LivingEntity entityLiving) {
        if (entityLiving instanceof Player) {
            ((Player) entityLiving).m_21253_();
        }
        return stack;
    }

    public static final InteractionResultHolder<ItemStack> castSpellOnUse(ItemStack spellStack, Level world, Player player, InteractionHand hand, Function<ItemStack, Boolean> consumeMana) {
        if (!SpellRecipe.stackContainsSpell(spellStack)) {
            return InteractionResultHolder.pass(spellStack);
        } else {
            player.m_6674_(hand);
            SpellCastingResult result = SpellCaster.PlayerCast(spellStack, player, hand, player.m_20182_().add(0.0, (double) player.m_20192_(), 0.0), player.m_20154_(), world, (Boolean) consumeMana.apply(spellStack));
            if (result.getCode() == SpellCastingResultCode.CHANNEL) {
                player.m_6672_(hand);
                if ((Boolean) consumeMana.apply(spellStack)) {
                    player.m_7292_(new MobEffectInstance(EffectInit.MANA_STUNT.get(), spellStack.getUseDuration(), 5));
                }
                return InteractionResultHolder.success(spellStack);
            } else if (result.getCode() == SpellCastingResultCode.SUCCESS) {
                setCooldown(player, spellStack);
                return InteractionResultHolder.success(spellStack);
            } else {
                setCooldown(player, spellStack);
                return InteractionResultHolder.success(spellStack);
            }
        }
    }

    public static final InteractionResult castSpellOnRightClickEntity(ItemStack spellStack, Level world, Player player, InteractionHand hand, Function<ItemStack, Boolean> consumeMana) {
        if (SpellRecipe.fromNBT(spellStack.getOrCreateTag()).isChanneled()) {
            return InteractionResult.PASS;
        } else {
            return player.getCooldowns().isOnCooldown(spellStack.getItem()) ? InteractionResult.PASS : castSpellOnUse(spellStack, world, player, hand, consumeMana).getResult();
        }
    }

    protected boolean shouldConsumeMana(ItemStack stack) {
        return true;
    }

    protected boolean consumeChanneledMana(Player player, ItemStack stack) {
        return SpellCaster.consumeChanneledMana(player, stack);
    }

    protected static final void setCooldown(Player player, ItemStack stack) {
        if (stack.getItem() instanceof ItemSpellRecipe) {
            ItemSpellRecipe item = (ItemSpellRecipe) stack.getItem();
            SpellRecipe recipe = SpellRecipe.fromNBT(item.getSpellCompound(stack, player));
            SpellCaster.setCooldown(stack, player, recipe.getCooldown(player));
        }
    }

    public static ItemStack createWTFBoomStack() {
        ItemStack spellStack = new ItemStack(ItemInit.SPELL.get());
        spellStack.setHoverName(Component.literal("L").withStyle(ChatFormatting.RED).append(Component.literal("M").withStyle(ChatFormatting.GOLD)).append(Component.literal("N").withStyle(ChatFormatting.GREEN)).append(Component.literal("O").withStyle(ChatFormatting.BLUE)).append(Component.literal("P").withStyle(ChatFormatting.LIGHT_PURPLE)));
        SpellRecipe recipe = new SpellRecipe();
        recipe.setShape(Shapes.SELF);
        recipe.addComponent(Components.LMNOP);
        recipe.writeToNBT(spellStack.getOrCreateTag());
        setCustomIcon(spellStack, 328);
        return spellStack;
    }

    @Override
    protected ArrayList<Component> getFlavorText(ItemStack stack) {
        if (SpellRecipe.stackContainsSpell(stack)) {
            SpellRecipe recipe = SpellRecipe.fromNBT(stack.getTag());
            if (recipe.isValid() && recipe.getComponents().stream().anyMatch(c -> c.getPart() == Components.LMNOP) && !recipe.isMysterious()) {
                ArrayList<Component> output = new ArrayList();
                output.add(Component.translatable("mna:components/wtfboom.warning"));
                return output;
            }
        }
        return super.getFlavorText(stack);
    }

    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {

            private final NonNullLazy<BlockEntityWithoutLevelRenderer> ister = NonNullLazy.of(() -> new ItemSpellRenderer(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels()));

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return this.ister.get();
            }
        });
    }

    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        return InteractionResult.PASS;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack itemstack = player.m_21120_(hand);
        if (itemstack.getItem() == ItemInit.SPELL.get() && getCustomIcon(itemstack) == -1) {
            if (!world.isClientSide) {
                NetworkHooks.openScreen((ServerPlayer) player, new NamedSpellCustomization());
            }
            return InteractionResultHolder.success(itemstack);
        } else if (!SpellRecipe.isMysterious(itemstack) && this.openGuiIfModifierPressed(player.m_21120_(hand), player, world)) {
            return InteractionResultHolder.success(itemstack);
        } else {
            return player.getCooldowns().isOnCooldown(this) ? InteractionResultHolder.fail(itemstack) : castSpellOnUse(itemstack, world, player, hand, this::shouldConsumeMana);
        }
    }

    @Override
    public MenuProvider getProvider(ItemStack stack) {
        return new NamedSpellAdjust();
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player playerIn, LivingEntity target, InteractionHand hand) {
        return castSpellOnRightClickEntity(stack, playerIn.m_9236_(), playerIn, hand, this::shouldConsumeMana);
    }

    @Override
    public void onUseTick(Level pLevel, LivingEntity player, ItemStack stack, int count) {
        if (player instanceof Player) {
            castSpellOnChannelTick(stack, (Player) player, count, this::consumeChanneledMana);
        }
    }

    @Override
    public void releaseUsing(ItemStack stack, Level worldIn, LivingEntity entityLiving, int timeLeft) {
        if (entityLiving instanceof Player player) {
            SpellRecipe recipe = SpellRecipe.fromNBT(this.getSpellCompound(stack, player));
            setCooldown(player, stack);
            if (this.shouldConsumeMana(stack)) {
                if (entityLiving.hasEffect(EffectInit.MANA_STUNT.get()) && entityLiving.getEffect(EffectInit.MANA_STUNT.get()).getAmplifier() == 5) {
                    entityLiving.removeEffect(EffectInit.MANA_STUNT.get());
                }
                SpellCaster.AddAffinityAndMagicXP(recipe, player, recipe.getMaxChannelTime() - timeLeft);
            }
        }
    }

    public boolean canEquip(ItemStack stack, EquipmentSlot armorType, Entity entity) {
        return armorType == EquipmentSlot.MAINHAND || armorType == EquipmentSlot.OFFHAND;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        SpellRecipe recipe = SpellRecipe.fromNBT(stack.getTag());
        int base = recipe.getMaxChannelTime();
        if (EnchantmentHelper.hasChanneling(stack)) {
            base *= 2;
        }
        return base;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }

    public boolean canContinueUsing(ItemStack oldStack, ItemStack newStack) {
        return true;
    }

    @Override
    protected Component getTranslatedDisplayName(SpellRecipe recipe) {
        if (recipe.isMysterious()) {
            return Component.translatable("item.mna.spell.mysterious");
        } else {
            MutableComponent shape = Component.translatable(recipe.getShape().getPart().getRegistryName().toString());
            StringBuilder sb = new StringBuilder();
            recipe.iterateComponents(c -> {
                if (sb.length() != 0) {
                    sb.append(", ");
                }
                sb.append(Component.translatable(((SpellEffect) c.getPart()).getRegistryName().toString()).getString());
            });
            return Component.translatable("item.mna.spell.display_name", shape.getString(), sb.toString());
        }
    }

    public static void setCustomIcon(ItemStack stack, int index) {
        stack.getOrCreateTag().putInt("icon", index);
    }

    public static int getCustomIcon(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        return tag.contains("icon") ? tag.getInt("icon") : -1;
    }

    @Override
    public boolean requiresModifierKey() {
        return true;
    }
}
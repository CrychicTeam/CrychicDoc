package com.mna.items.sorcery.bound;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.ImmutableMultimap.Builder;
import com.mna.api.spells.base.ISpellDefinition;
import com.mna.capabilities.playerdata.magic.PlayerMagicProvider;
import com.mna.interop.CuriosInterop;
import com.mna.items.ItemInit;
import com.mna.items.base.INoCreativeTab;
import com.mna.items.renderers.bound.BoundAxeItemRenderer;
import com.mna.spells.crafting.SpellRecipe;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.util.NonNullLazy;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;
import top.theillusivec4.curios.api.SlotTypePreset;

public class ItemBoundAxe extends AxeItem implements IBoundItem, INoCreativeTab {

    public AnimatableInstanceCache animCache = GeckoLibUtil.createInstanceCache(this);

    public ItemBoundAxe(float attackDamageIn, float attackSpeedIn) {
        super(Tiers.DIAMOND, attackDamageIn, attackSpeedIn, new Item.Properties());
    }

    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {

            private final NonNullLazy<BlockEntityWithoutLevelRenderer> ister = NonNullLazy.of(() -> new BoundAxeItemRenderer(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels()));

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return this.ister.get();
            }
        });
    }

    public int getDamage(ItemStack stack) {
        return 0;
    }

    @Override
    public float getPassiveManaDrain(Entity entity) {
        if (entity instanceof Player p && CuriosInterop.IsItemInCurioSlot(ItemInit.BATTLEMAGE_AMULET.get(), p, SlotTypePreset.NECKLACE)) {
            return 0.0F;
        }
        return 0.2F;
    }

    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> baseAttrs = super.getAttributeModifiers(slot, stack);
        if (slot != EquipmentSlot.MAINHAND) {
            return baseAttrs;
        } else {
            Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
            if (!stack.hasTag()) {
                return baseAttrs;
            } else {
                SpellRecipe recipe = SpellRecipe.fromNBT(stack.getTag());
                if (!recipe.isValid()) {
                    return baseAttrs;
                } else {
                    int damage = (int) recipe.getShape().getValue(com.mna.api.spells.attributes.Attribute.DAMAGE);
                    builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(f_41374_, "Weapon modifier", (double) damage, AttributeModifier.Operation.ADDITION));
                    int range = (int) recipe.getShape().getValue(com.mna.api.spells.attributes.Attribute.RANGE);
                    builder.put(ForgeMod.ENTITY_REACH.get(), new AttributeModifier(BASE_RANGE_UUID, "Weapon modifier", (double) range, AttributeModifier.Operation.ADDITION));
                    baseAttrs.forEach((a, m) -> {
                        if (a != ForgeMod.ENTITY_REACH.get() && a != Attributes.ATTACK_DAMAGE) {
                            builder.put(a, m);
                        }
                    });
                    return builder.build();
                }
            }
        }
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        SpellRecipe recipe = this.getRecipe(stack);
        if (attacker instanceof Player && recipe.isValid()) {
            Player player = (Player) attacker;
            if (!player.getCooldowns().isOnCooldown(this)) {
                player.getCapability(PlayerMagicProvider.MAGIC).ifPresent(c -> {
                    if (c.getCastingResource().hasEnoughAbsolute(player, recipe.getManaCost()) && this.affectTarget(recipe, attacker, target, InteractionHand.MAIN_HAND)) {
                        c.getCastingResource().consume(player, recipe.getManaCost());
                    }
                });
                player.getCooldowns().addCooldown(this, (int) ((float) recipe.getCooldown(player) * 0.75F));
            }
        }
        return super.m_7579_(stack, target, attacker);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        this.handleInventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);
        super.m_6883_(stack, worldIn, entityIn, itemSlot, isSelected);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player != null) {
            SpellRecipe recipe = this.getRecipe(stack);
            recipe.addItemTooltip(stack, worldIn, tooltip, mc.player);
            super.m_7373_(stack, worldIn, tooltip, flagIn);
        }
    }

    public ItemStack createFromSpell(ItemStack original, ISpellDefinition recipe) {
        return IBoundItem.super.createFromSpell(this, original, recipe);
    }

    public boolean onDroppedByPlayer(ItemStack item, Player player) {
        ItemStack restored = this.restoreItem(item);
        player.getInventory().setItem(player.getInventory().selected, restored);
        return false;
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return true;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar registrar) {
        registrar.add(new AnimationController<>(this, state -> state.setAndContinue(RawAnimation.begin().thenLoop("animation.bound_swordaxe.idle"))));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.animCache;
    }
}
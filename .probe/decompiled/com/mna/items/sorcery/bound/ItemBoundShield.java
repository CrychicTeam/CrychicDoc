package com.mna.items.sorcery.bound;

import com.mna.api.spells.base.ISpellDefinition;
import com.mna.interop.CuriosInterop;
import com.mna.items.ItemInit;
import com.mna.items.base.INoCreativeTab;
import com.mna.items.renderers.bound.BoundShieldItemRenderer;
import com.mna.spells.crafting.SpellRecipe;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.common.util.NonNullLazy;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;
import top.theillusivec4.curios.api.SlotTypePreset;

public class ItemBoundShield extends ShieldItem implements IBoundItem, INoCreativeTab {

    public AnimatableInstanceCache animCache = GeckoLibUtil.createInstanceCache(this);

    public ItemBoundShield() {
        super(new Item.Properties());
    }

    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {

            private final NonNullLazy<BlockEntityWithoutLevelRenderer> ister = NonNullLazy.of(() -> new BoundShieldItemRenderer(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels()));

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
        if (entity instanceof LivingEntity && ((LivingEntity) entity).isBlocking()) {
            return 2.0F;
        } else {
            if (entity instanceof Player p && CuriosInterop.IsItemInCurioSlot(ItemInit.WARDING_AMULET.get(), p, SlotTypePreset.NECKLACE)) {
                return 0.0F;
            }
            return 0.2F;
        }
    }

    @Override
    public void inventoryTick(ItemStack stack, Level worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        this.handleInventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);
        super.m_6883_(stack, worldIn, entityIn, itemSlot, isSelected);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BLOCK;
    }

    public boolean canPerformAction(ItemStack stack, ToolAction toolAction) {
        return toolAction == ToolActions.SHIELD_BLOCK;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player != null) {
            SpellRecipe recipe = this.getRecipe(stack);
            recipe.addItemTooltip(stack, worldIn, tooltip, mc.player);
            super.appendHoverText(stack, worldIn, tooltip, flagIn);
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
        registrar.add(new AnimationController<>(this, state -> state.setAndContinue(RawAnimation.begin().thenLoop("animation.bound_shield.idle"))));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.animCache;
    }
}
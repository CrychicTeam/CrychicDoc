package com.mna.items.sorcery.bound;

import com.mna.api.capabilities.IPlayerProgression;
import com.mna.api.spells.adjusters.SpellCastStage;
import com.mna.api.spells.base.ISpellDefinition;
import com.mna.capabilities.playerdata.magic.PlayerMagicProvider;
import com.mna.capabilities.playerdata.progression.PlayerProgressionProvider;
import com.mna.entities.EntityInit;
import com.mna.entities.sorcery.targeting.BoundBowProjectile;
import com.mna.interop.CuriosInterop;
import com.mna.items.ItemInit;
import com.mna.items.base.INoCreativeTab;
import com.mna.items.renderers.bound.BoundBowItemRenderer;
import com.mna.spells.SpellCaster;
import com.mna.spells.crafting.SpellRecipe;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.common.util.NonNullLazy;
import org.apache.commons.lang3.mutable.MutableObject;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.network.GeckoLibNetwork;
import software.bernie.geckolib.util.GeckoLibUtil;
import top.theillusivec4.curios.api.SlotTypePreset;

public class ItemBoundBow extends BowItem implements IBoundItem, INoCreativeTab {

    private static final RawAnimation DRAW = RawAnimation.begin().thenPlay("animation.bound_bow.draw").thenLoop("animation.bound_bow.draw_idle");

    private static final RawAnimation FIRE = RawAnimation.begin().thenPlay("animation.bound_bow.fire").thenLoop("animation.bound_bow.idle");

    private static final String controller_id = "bound_bow_fire";

    AnimatableInstanceCache animCache = GeckoLibUtil.createInstanceCache(this);

    public ItemBoundBow() {
        super(new Item.Properties());
        GeckoLibNetwork.registerSyncedAnimatable(this);
    }

    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {

            private final NonNullLazy<BlockEntityWithoutLevelRenderer> ister = NonNullLazy.of(() -> new BoundBowItemRenderer(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels()));

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
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 72000;
    }

    public int manaPerShot(Player player, ItemStack stack) {
        SpellRecipe recipe = this.getRecipe(stack);
        SpellCaster.applyAdjusters(stack, player, recipe, SpellCastStage.CALCULATING_MANA_COST);
        return (int) recipe.getManaCost();
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
        ItemStack itemstack = playerIn.m_21120_(handIn);
        MutableObject<InteractionResultHolder<ItemStack>> returnValue = new MutableObject(InteractionResultHolder.fail(itemstack));
        SpellRecipe recipe = SpellRecipe.fromNBT(itemstack.getTag());
        if (!this.canCastSpell(recipe, playerIn, handIn)) {
            return InteractionResultHolder.fail(itemstack);
        } else {
            playerIn.getCapability(PlayerMagicProvider.MAGIC).ifPresent(m -> {
                if (playerIn.getAbilities().instabuild || m.getCastingResource().hasEnough(playerIn, (float) this.manaPerShot(playerIn, itemstack))) {
                    playerIn.m_6672_(handIn);
                    if (worldIn instanceof ServerLevel serverLevel) {
                        this.triggerAnim(playerIn, GeoItem.getOrAssignId(itemstack, serverLevel), "bound_bow_fire", "draw");
                    }
                    returnValue.setValue(InteractionResultHolder.consume(itemstack));
                }
            });
            return (InteractionResultHolder<ItemStack>) returnValue.getValue();
        }
    }

    public static float getArrowVelocity(int charge) {
        float f = (float) charge / 20.0F;
        f = (f * f + f * 2.0F) / 3.0F;
        if (f > 1.0F) {
            f = 1.0F;
        }
        return f;
    }

    @Override
    public void releaseUsing(ItemStack stack, Level worldIn, LivingEntity entityLiving, int timeLeft) {
        if (entityLiving instanceof Player playerentity) {
            if (worldIn instanceof ServerLevel serverLevel) {
                this.triggerAnim(playerentity, GeoItem.getOrAssignId(stack, serverLevel), "bound_bow_fire", "fire");
            }
            int i = this.getUseDuration(stack) - timeLeft;
            if (i < 0) {
                return;
            }
            float f = getArrowVelocity(i);
            if (!((double) f < 0.1)) {
                playerentity.getCapability(PlayerMagicProvider.MAGIC).ifPresent(m -> {
                    int mana = this.manaPerShot(playerentity, stack);
                    if (m.getCastingResource().hasEnough(entityLiving, (float) mana)) {
                        SpellRecipe recipe = this.getRecipe(stack);
                        if (!worldIn.isClientSide) {
                            BoundBowProjectile shot = new BoundBowProjectile(EntityInit.BOUND_BOW_PROJECTILE.get(), worldIn);
                            shot.m_6034_(playerentity.m_20185_(), playerentity.m_20186_() + (double) playerentity.m_20192_(), playerentity.m_20189_());
                            shot.m_5602_(playerentity);
                            shot.m_37251_(playerentity, playerentity.m_146909_(), playerentity.m_146908_(), 0.0F, f * 3.0F, 1.0F);
                            shot.setSpell(recipe);
                            worldIn.m_7967_(shot);
                        }
                        m.getCastingResource().consume(playerentity, (float) mana);
                        IPlayerProgression progression = (IPlayerProgression) playerentity.getCapability(PlayerProgressionProvider.PROGRESSION).orElse(null);
                        if (progression != null && !recipe.canFactionCraft(progression)) {
                            this.getRecipe(stack).usedByPlayer(playerentity);
                        }
                        worldIn.playSound((Player) null, playerentity.m_20185_(), playerentity.m_20186_(), playerentity.m_20189_(), SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1.0F, 1.0F / (worldIn.random.nextFloat() * 0.4F + 1.2F) + f * 0.5F);
                        playerentity.getCooldowns().addCooldown(this, (int) ((float) recipe.getCooldown(playerentity) * 0.75F));
                    }
                });
            }
        }
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar registrar) {
        registrar.add(new AnimationController<>(this, "bound_bow_fire", 0, state -> PlayState.STOP).triggerableAnim("draw", DRAW).triggerableAnim("fire", FIRE));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.animCache;
    }
}
package com.mna.items.armor;

import com.mna.api.faction.IFaction;
import com.mna.api.items.IFactionSpecific;
import com.mna.api.items.ITieredItem;
import com.mna.api.tools.RLoc;
import com.mna.factions.Factions;
import com.mna.items.renderers.BoneArmorRenderer;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import software.bernie.geckolib.util.GeckoLibUtil;

public class BoneArmorItem extends ArmorItem implements GeoItem, ISetItem, ITieredItem<BoneArmorItem>, IFactionSpecific, IBrokenArmorReplaceable<BoneArmorItem> {

    private final AnimatableInstanceCache animCache = GeckoLibUtil.createInstanceCache(this);

    private static final ResourceLocation bone_armor_set_bonus = RLoc.create("bone_armor_set_bonus");

    public static final String bone_armor_set_bonus_key = "bone_armor_set_bonus";

    private int _tier = -1;

    public BoneArmorItem(ArmorMaterial materialIn, ArmorItem.Type slot, Item.Properties builder) {
        super(materialIn, slot, builder.rarity(Rarity.EPIC));
    }

    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {

            private GeoArmorRenderer<?> renderer;

            @NotNull
            @Override
            public HumanoidModel<?> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original) {
                if (this.renderer == null) {
                    this.renderer = new BoneArmorRenderer();
                }
                this.renderer.prepForRender(livingEntity, itemStack, equipmentSlot, original);
                return this.renderer;
            }
        });
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar registrar) {
        registrar.add(new AnimationController<>(this, state -> PlayState.STOP));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.animCache;
    }

    @Override
    public ResourceLocation getSetIdentifier() {
        return bone_armor_set_bonus;
    }

    public void onArmorTick(ItemStack stack, Level world, Player player) {
        this.usedByPlayer(player);
    }

    @Override
    public void applySetBonus(LivingEntity living, EquipmentSlot... setSlots) {
        if (living instanceof Player) {
            living.getPersistentData().putBoolean("bone_armor_set_bonus", true);
        }
    }

    @Override
    public void removeSetBonus(LivingEntity living, EquipmentSlot... setSlots) {
        if (living instanceof Player) {
            living.getPersistentData().putBoolean("bone_armor_set_bonus", false);
        }
    }

    @Override
    public int itemsForSetBonus() {
        return 4;
    }

    @Override
    public int getCachedTier() {
        return this._tier;
    }

    @Override
    public void setCachedTier(int tier) {
        this._tier = tier;
    }

    @Override
    public IFaction getFaction() {
        return Factions.UNDEAD;
    }

    @Override
    public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        ISetItem.super.addSetTooltip(tooltip);
        super.m_7373_(stack, worldIn, tooltip, flagIn);
    }

    @Override
    public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T entity, Consumer<T> onBroken) {
        return IBrokenArmorReplaceable.super.damageItem(stack, amount * 3, entity, onBroken);
    }
}
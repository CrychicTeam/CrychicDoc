package com.mna.items.armor;

import com.mna.ManaAndArtifice;
import com.mna.api.config.GeneralConfigValues;
import com.mna.api.faction.IFaction;
import com.mna.api.items.IFactionSpecific;
import com.mna.api.items.ITieredItem;
import com.mna.api.particles.MAParticleType;
import com.mna.api.particles.ParticleInit;
import com.mna.api.tools.RLoc;
import com.mna.capabilities.playerdata.magic.PlayerMagicProvider;
import com.mna.effects.EffectInit;
import com.mna.factions.Factions;
import com.mna.items.ItemInit;
import com.mna.items.base.IManaRepairable;
import com.mna.items.renderers.CouncilArmorRenderer;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
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
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import software.bernie.geckolib.util.GeckoLibUtil;

public class CouncilArmorItem extends ArmorItem implements GeoItem, ISetItem, ITieredItem<CouncilArmorItem>, IFactionSpecific, IBrokenArmorReplaceable<CouncilArmorItem>, IManaRepairable {

    private AnimatableInstanceCache animCache = GeckoLibUtil.createInstanceCache(this);

    private static final ResourceLocation council_armor_set_bonus = RLoc.create("council_armor_set_bonus");

    public static final String council_armor_set_bonus_key = "council_armor_set_bonus";

    public static final String council_armor_reflect_counters_key = "council_armor_reflect_counters";

    private static final float MANA_COST_PER_TICK = 0.75F;

    private int _tier = -1;

    public CouncilArmorItem(ArmorMaterial materialIn, ArmorItem.Type slot, Item.Properties builder) {
        super(materialIn, slot, builder.rarity(Rarity.EPIC));
    }

    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {

            private GeoArmorRenderer<?> renderer;

            @NotNull
            @Override
            public HumanoidModel<?> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original) {
                if (this.renderer == null) {
                    this.renderer = new CouncilArmorRenderer();
                }
                this.renderer.prepForRender(livingEntity, itemStack, equipmentSlot, original);
                return this.renderer;
            }
        });
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar registrar) {
        registrar.add(new AnimationController<>(this, state -> state.setAndContinue(RawAnimation.begin().thenLoop("animation.model.ambient"))));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.animCache;
    }

    @Override
    public ResourceLocation getSetIdentifier() {
        return council_armor_set_bonus;
    }

    public void onArmorTick(ItemStack stack, Level world, Player player) {
        this.usedByPlayer(player);
        if (this.isSetEquipped(player) && this == ItemInit.COUNCIL_ARMOR__CHEST.get()) {
            player.getCapability(PlayerMagicProvider.MAGIC).ifPresent(m -> {
                if (player.getAbilities().flying && !player.m_21023_(EffectInit.LEVITATION.get())) {
                    if (world.isClientSide) {
                        Vec3 look = player.m_20156_().cross(new Vec3(0.0, 1.0, 0.0));
                        float offset = (float) (Math.random() * 0.2);
                        look = look.scale((double) offset);
                        world.addParticle(new MAParticleType(ParticleInit.ARCANE.get()), player.m_20185_() + look.x, player.m_20186_(), player.m_20189_() + look.z, 0.0, -0.05F, 0.0);
                        world.addParticle(new MAParticleType(ParticleInit.ARCANE.get()), player.m_20185_() - look.x, player.m_20186_(), player.m_20189_() - look.z, 0.0, -0.05F, 0.0);
                    } else {
                        m.getCastingResource().consume(player, 0.75F);
                    }
                }
                if (!m.getCastingResource().hasEnoughAbsolute(player, 0.75F)) {
                    ManaAndArtifice.instance.proxy.setFlightEnabled(player, false);
                } else {
                    ManaAndArtifice.instance.proxy.setFlightEnabled(player, true);
                    if (!player.isCreative() && !player.isSpectator()) {
                        ManaAndArtifice.instance.proxy.setFlySpeed(player, 0.02F);
                        if (player.getAbilities().flying) {
                            player.m_6858_(false);
                        }
                    } else {
                        ManaAndArtifice.instance.proxy.setFlySpeed(player, 0.05F);
                    }
                }
            });
        }
    }

    @Override
    public void applySetBonus(LivingEntity living, EquipmentSlot... setSlots) {
        if (living instanceof Player) {
            living.getPersistentData().putBoolean("council_armor_set_bonus", true);
            ((Player) living).getCapability(PlayerMagicProvider.MAGIC).ifPresent(m -> {
                m.getCastingResource().addModifier("council_armor_set_bonus", 750.0F);
                m.getCastingResource().addRegenerationModifier("council_armor_set_bonus", -0.25F);
            });
        }
    }

    @Override
    public void removeSetBonus(LivingEntity living, EquipmentSlot... setSlots) {
        if (living instanceof Player) {
            living.getPersistentData().putBoolean("council_armor_set_bonus", false);
            ((Player) living).getCapability(PlayerMagicProvider.MAGIC).ifPresent(m -> {
                m.getCastingResource().removeModifier("council_armor_set_bonus");
                m.getCastingResource().removeRegenerationModifier("council_armor_set_bonus");
            });
            ManaAndArtifice.instance.proxy.setFlySpeed((Player) living, 0.05F);
            ManaAndArtifice.instance.proxy.setFlightEnabled((Player) living, false);
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
        return Factions.COUNCIL;
    }

    @Override
    public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        ISetItem.super.addSetTooltip(tooltip);
        super.m_7373_(stack, worldIn, tooltip, flagIn);
    }

    private static int[] getReflectCharges(Player player) {
        int[] reflections;
        if (!player.getPersistentData().contains("council_armor_reflect_counters")) {
            reflections = new int[GeneralConfigValues.SpellweaverReflectCharges];
        } else {
            reflections = player.getPersistentData().getIntArray("council_armor_reflect_counters");
        }
        if (reflections.length != GeneralConfigValues.SpellweaverReflectCharges) {
            reflections = new int[GeneralConfigValues.SpellweaverReflectCharges];
        }
        return reflections;
    }

    private static void updateReflectCharges(Player player, int[] reflections) {
        player.getPersistentData().putIntArray("council_armor_reflect_counters", reflections);
    }

    public static boolean consumeReflectCharget(Player player) {
        int[] reflections = getReflectCharges(player);
        for (int i = 0; i < reflections.length; i++) {
            if (reflections[i] <= 0) {
                reflections[i] = GeneralConfigValues.SpellweaverReflectTime;
                updateReflectCharges(player, reflections);
                return true;
            }
        }
        return false;
    }

    public static void tickReflectCharges(Player player) {
        int[] reflections = getReflectCharges(player);
        for (int i = 0; i < reflections.length; i++) {
            if (reflections[i] > 0) {
                reflections[i]--;
            }
        }
        updateReflectCharges(player, reflections);
    }

    @Override
    public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T entity, Consumer<T> onBroken) {
        return IBrokenArmorReplaceable.super.damageItem(stack, amount * 3, entity, onBroken);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        IManaRepairable.super.inventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);
    }
}
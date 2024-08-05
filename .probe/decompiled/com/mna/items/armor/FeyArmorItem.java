package com.mna.items.armor;

import com.mna.ManaAndArtifice;
import com.mna.api.capabilities.IPlayerMagic;
import com.mna.api.config.GeneralConfigValues;
import com.mna.api.faction.IFaction;
import com.mna.api.items.IFactionSpecific;
import com.mna.api.items.ITieredItem;
import com.mna.api.particles.MAParticleType;
import com.mna.api.particles.ParticleInit;
import com.mna.api.tools.RLoc;
import com.mna.capabilities.playerdata.magic.PlayerMagicProvider;
import com.mna.factions.Factions;
import com.mna.items.ItemInit;
import com.mna.items.renderers.FeyArmorRenderer;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
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

public class FeyArmorItem extends ArmorItem implements GeoItem, ISetItem, ITieredItem<FeyArmorItem>, IFactionSpecific, IBrokenArmorReplaceable<FeyArmorItem> {

    private AnimatableInstanceCache animCache = GeckoLibUtil.createInstanceCache(this);

    private static final ResourceLocation fey_armor_set_bonus = RLoc.create("fey_armor_set_bonus");

    public static final String fey_armor_set_bonus_key = "fey_armor_set_bonus";

    private static final float FLIGHT_MANA_COST_PER_TICK = 0.75F;

    private static final float HOVER_MANA_COST_PER_TICK = 0.25F;

    private int _tier = -1;

    public static LivingEntity renderEntity;

    public FeyArmorItem(ArmorMaterial materialIn, ArmorItem.Type slot, Item.Properties builder) {
        super(materialIn, slot, builder.rarity(Rarity.EPIC));
    }

    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {

            private GeoArmorRenderer<?> renderer;

            @NotNull
            @Override
            public HumanoidModel<?> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original) {
                if (this.renderer == null) {
                    this.renderer = new FeyArmorRenderer();
                }
                this.renderer.prepForRender(livingEntity, itemStack, equipmentSlot, original);
                return this.renderer;
            }
        });
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.animCache;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar registrar) {
        registrar.add(new AnimationController<>(this, state -> {
            if (renderEntity == null) {
                state.getController().transitionLength(20);
                return state.setAndContinue(RawAnimation.begin().thenLoop("animation.fey_armor.notflying"));
            } else if (!renderEntity.isFallFlying() && (!(renderEntity instanceof Player) || !((Player) renderEntity).getAbilities().flying)) {
                state.getController().transitionLength(20);
                return state.setAndContinue(RawAnimation.begin().thenLoop("animation.fey_armor.notflying"));
            } else {
                state.getController().transitionLength(0);
                return state.setAndContinue(RawAnimation.begin().thenLoop("animation.fey_armor.flying"));
            }
        }));
    }

    @Override
    public ResourceLocation getSetIdentifier() {
        return fey_armor_set_bonus;
    }

    public void onArmorTick(ItemStack stack, Level world, Player player) {
        this.usedByPlayer(player);
        if (stack.getItem() == ItemInit.FEY_ARMOR_CHEST.get() && this.isSetEquipped(player)) {
            player.getCapability(PlayerMagicProvider.MAGIC).ifPresent(m -> {
                ManaAndArtifice.instance.proxy.setFlightEnabled(player, m.getCastingResource().hasEnoughAbsolute(player, 0.25F));
                if (!player.isCreative() && !player.isSpectator()) {
                    ManaAndArtifice.instance.proxy.setFlySpeed(player, 0.02F);
                } else {
                    ManaAndArtifice.instance.proxy.setFlySpeed(player, 0.05F);
                }
                if (player.getAbilities().flying) {
                    m.getCastingResource().consume(player, 0.25F);
                    player.m_6858_(false);
                }
            });
            if (player.m_21223_() < player.m_21233_() * 0.1F && !player.getCooldowns().isOnCooldown(this)) {
                player.getCooldowns().addCooldown(this, 6000);
                player.m_7292_(new MobEffectInstance(MobEffects.REGENERATION, 3, 60));
            }
        }
    }

    @Override
    public void applySetBonus(LivingEntity living, EquipmentSlot... setSlots) {
        if (living instanceof Player) {
            living.getPersistentData().putBoolean("fey_armor_set_bonus", true);
        }
    }

    @Override
    public void removeSetBonus(LivingEntity living, EquipmentSlot... setSlots) {
        if (living instanceof Player) {
            living.getPersistentData().putBoolean("fey_armor_set_bonus", false);
            ManaAndArtifice.instance.proxy.setFlightEnabled((Player) living, false);
            ManaAndArtifice.instance.proxy.setFlySpeed((Player) living, 0.05F);
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
        return Factions.FEY;
    }

    @Override
    public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        ISetItem.super.addSetTooltip(tooltip);
        super.m_7373_(stack, worldIn, tooltip, flagIn);
    }

    public boolean elytraFlightTick(ItemStack stack, LivingEntity entity, int flightTicks) {
        if (!(entity instanceof Player)) {
            return false;
        } else {
            if (flightTicks % 100 == 0) {
                this.usedByPlayer((Player) entity);
            }
            IPlayerMagic magic = (IPlayerMagic) ((Player) entity).getCapability(PlayerMagicProvider.MAGIC).orElse(null);
            if (magic != null && magic.getCastingResource().hasEnoughAbsolute(entity, 0.75F)) {
                Vec3 look = entity.m_20154_();
                if (!entity.m_6144_()) {
                    magic.getCastingResource().consume(entity, 0.75F);
                    Vec3 motion = entity.m_20184_();
                    float maxLength = 1.75F;
                    double lookScale = 0.06;
                    Vec3 scaled_look = look.scale(lookScale);
                    motion = motion.add(scaled_look);
                    if (motion.length() > (double) maxLength) {
                        motion = motion.scale((double) maxLength / motion.length());
                    }
                    entity.m_20256_(motion);
                } else {
                    magic.getCastingResource().consume(entity, 0.5625F);
                    Vec3 motion = entity.m_20184_();
                    float minLength = 0.1F;
                    double lookScale = -0.01;
                    Vec3 scaled_look = look.scale(lookScale);
                    motion = motion.add(scaled_look);
                    if (motion.length() < (double) minLength) {
                        motion = motion.scale((double) minLength / motion.length());
                    }
                    entity.m_20256_(motion);
                }
                if (entity.m_9236_().isClientSide()) {
                    Vec3 pos = entity.m_20182_().add(look.scale(3.0));
                    for (int i = 0; i < 5; i++) {
                        entity.m_9236_().addParticle(new MAParticleType(ParticleInit.AIR_VELOCITY.get()).setScale(0.2F).setColor(10, 10, 10), pos.x - 0.5 + Math.random(), pos.y - 0.5 + Math.random(), pos.z - 0.5 + Math.random(), -look.x, -look.y, -look.z);
                    }
                }
                return true;
            } else {
                return false;
            }
        }
    }

    public boolean canElytraFly(ItemStack stack, LivingEntity entity) {
        return entity instanceof Player && !entity.m_20072_() && !entity.m_20077_() && this.isSetEquipped(entity) && ((Player) entity).getCapability(PlayerMagicProvider.MAGIC).isPresent() && ((IPlayerMagic) ((Player) entity).getCapability(PlayerMagicProvider.MAGIC).orElse(null)).getCastingResource().hasEnoughAbsolute(entity, 0.75F);
    }

    public static boolean randomReflectChance() {
        return Math.random() < GeneralConfigValues.DruidicReflectChance;
    }

    public static boolean randomTeleportChance() {
        return Math.random() < GeneralConfigValues.DruidicTeleportChance;
    }

    @Override
    public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T entity, Consumer<T> onBroken) {
        return IBrokenArmorReplaceable.super.damageItem(stack, amount * 3, entity, onBroken);
    }
}
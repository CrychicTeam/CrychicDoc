package com.mna.items.armor;

import com.mna.api.capabilities.IPlayerMagic;
import com.mna.api.config.GeneralConfigValues;
import com.mna.api.faction.IFaction;
import com.mna.api.items.IFactionSpecific;
import com.mna.api.items.ITieredItem;
import com.mna.api.particles.MAParticleType;
import com.mna.api.particles.ParticleInit;
import com.mna.api.sound.SFX;
import com.mna.api.timing.DelayedEventQueue;
import com.mna.api.timing.TimedDelayedEvent;
import com.mna.api.tools.RLoc;
import com.mna.capabilities.playerdata.magic.PlayerMagicProvider;
import com.mna.effects.EffectInit;
import com.mna.entities.utility.MAExplosion;
import com.mna.factions.Factions;
import com.mna.items.ItemInit;
import com.mna.items.renderers.DemonArmorRenderer;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import software.bernie.geckolib.util.GeckoLibUtil;

public class DemonArmorItem extends ArmorItem implements GeoItem, ISetItem, ITieredItem<DemonArmorItem>, IFactionSpecific, IBrokenArmorReplaceable<DemonArmorItem> {

    private AnimatableInstanceCache animCache = GeckoLibUtil.createInstanceCache(this);

    private static final ResourceLocation demon_armor_set_bonus = RLoc.create("demon_armor_set_bonus");

    public static final String demon_armor_set_bonus_key = "demon_armor_set_bonus";

    public static final String demon_armor_meteor_jumping_key = "demon_armor_meteor_jumping";

    public static final String demon_armor_run_speed_1 = "demon_armor_set_bonus_1";

    public static final String demon_armor_run_speed_2 = "demon_armor_set_bonus_2";

    public static final String demon_armor_run_speed_3 = "demon_armor_set_bonus_3";

    public static final AttributeModifier runSpeed_1 = new AttributeModifier("demon_armor_set_bonus_1", 0.05F, AttributeModifier.Operation.ADDITION);

    public static final AttributeModifier runSpeed_2 = new AttributeModifier("demon_armor_set_bonus_2", 0.1F, AttributeModifier.Operation.ADDITION);

    public static final AttributeModifier runSpeed_3 = new AttributeModifier("demon_armor_set_bonus_3", 0.1F, AttributeModifier.Operation.ADDITION);

    private static final float MANA_COST_PER_TICK = 0.25F;

    private static final float METEOR_JUMP_MANA_COST = 40.0F;

    private int _tier = -1;

    public DemonArmorItem(ArmorMaterial materialIn, ArmorItem.Type slot, Item.Properties builder) {
        super(materialIn, slot, builder.rarity(Rarity.EPIC));
    }

    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {

            private GeoArmorRenderer<?> renderer;

            @NotNull
            @Override
            public HumanoidModel<?> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original) {
                if (this.renderer == null) {
                    this.renderer = new DemonArmorRenderer();
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
        registrar.add(new AnimationController<>(this, state -> PlayState.STOP));
    }

    @Override
    public ResourceLocation getSetIdentifier() {
        return demon_armor_set_bonus;
    }

    public void onArmorTick(ItemStack stack, Level world, Player player) {
        this.usedByPlayer(player);
        if (stack.isDamaged() && player.m_6060_() && player.m_9236_().getGameTime() % 10L == 0L) {
            stack.setDamageValue(stack.getDamageValue() - 1);
        }
        if (this.isSetEquipped(player)) {
            if (stack.getItem() != ItemInit.DEMON_ARMOR_CHEST.get()) {
                return;
            }
            IPlayerMagic magic = (IPlayerMagic) player.getCapability(PlayerMagicProvider.MAGIC).orElse(null);
            if (magic == null) {
                return;
            }
            boolean showParticles = false;
            boolean removeModifier = true;
            if (player.m_20142_() && magic.getCastingResource().hasEnoughAbsolute(player, 0.25F)) {
                if (!player.m_21051_(Attributes.MOVEMENT_SPEED).hasModifier(runSpeed_1)) {
                    player.m_21051_(Attributes.MOVEMENT_SPEED).addTransientModifier(runSpeed_1);
                    DelayedEventQueue.pushEvent(world, new TimedDelayedEvent<>("demon_armor_set_bonus_2", 60, player, this::addDelayedRunSpeed));
                    DelayedEventQueue.pushEvent(world, new TimedDelayedEvent<>("demon_armor_set_bonus_3", 120, player, this::addDelayedRunSpeed));
                    player.playSound(SFX.Event.Artifact.DEMON_ARMOR_SPRINT_START, 1.0F, 0.8F);
                }
                magic.getCastingResource().consume(player, 0.25F);
                removeModifier = false;
                showParticles = true;
            }
            if (removeModifier) {
                player.m_21051_(Attributes.MOVEMENT_SPEED).removeModifier(runSpeed_1);
                player.m_21051_(Attributes.MOVEMENT_SPEED).removeModifier(runSpeed_2);
                player.m_21051_(Attributes.MOVEMENT_SPEED).removeModifier(runSpeed_3);
            }
            if (!player.m_20096_() && player.m_20184_().y < 0.0 && player.m_6047_() && magic.getCastingResource().hasEnough(player, 40.0F) && !player.getPersistentData().contains("demon_armor_meteor_jumping")) {
                int heightAboveGround = 0;
                int reqHeight = 5;
                for (BlockPos pos = player.m_20183_(); player.m_9236_().m_46859_(pos) && heightAboveGround < reqHeight; heightAboveGround++) {
                    pos = pos.below();
                }
                if (heightAboveGround >= reqHeight) {
                    player.getPersistentData().putBoolean("demon_armor_meteor_jumping", true);
                    player.playSound(SFX.Event.Artifact.METEOR_JUMP, 0.25F, 0.8F);
                    magic.getCastingResource().consume(player, 40.0F);
                    showParticles = true;
                }
            }
            if (player.getPersistentData().contains("demon_armor_meteor_jumping")) {
                this.handlePlayerMeteorJump(player);
                showParticles = true;
            }
            if (player.m_6060_()) {
                if (!player.m_9236_().isClientSide()) {
                    MobEffectInstance strength = player.m_21124_(EffectInit.BURNING_RAGE.get());
                    if (strength == null || strength.getDuration() < 105) {
                        player.m_7292_(new MobEffectInstance(EffectInit.BURNING_RAGE.get(), 600, 1, true, true));
                    }
                }
            } else if (!player.m_9236_().isClientSide()) {
                MobEffectInstance eff = player.m_21124_(EffectInit.BURNING_RAGE.get());
                if (eff != null && eff.isAmbient()) {
                    player.m_21195_(EffectInit.BURNING_RAGE.get());
                }
            }
            if (world.isClientSide && showParticles) {
                Vec3 motion = player.m_20184_();
                Vec3 look = player.m_20156_().cross(new Vec3(0.0, 1.0, 0.0));
                float offset = (float) (Math.random() * 0.2);
                float yOffset = 0.2F;
                look = look.scale((double) offset);
                for (int i = 0; i < 5; i++) {
                    world.addParticle(new MAParticleType(ParticleInit.FLAME.get()), player.m_20185_() + look.x + Math.random() * motion.x * 2.0, player.m_20186_() + (double) yOffset + Math.random() * motion.y * 2.0, player.m_20189_() + look.z + Math.random() * motion.z * 2.0, 0.0, 0.0, 0.0);
                    world.addParticle(new MAParticleType(ParticleInit.FLAME.get()), player.m_20185_() - look.x + Math.random() * motion.x * 2.0, player.m_20186_() + (double) yOffset + Math.random() * motion.y * 2.0, player.m_20189_() - look.z + Math.random() * motion.z * 2.0, 0.0, 0.0, 0.0);
                }
            }
        } else {
            player.m_21051_(Attributes.MOVEMENT_SPEED).removeModifier(runSpeed_1);
            player.m_21051_(Attributes.MOVEMENT_SPEED).removeModifier(runSpeed_2);
            player.m_21051_(Attributes.MOVEMENT_SPEED).removeModifier(runSpeed_3);
        }
    }

    public void addDelayedRunSpeed(String identifier, Player player) {
        if (player.m_20142_()) {
            if (identifier == "demon_armor_set_bonus_2" && !player.m_21051_(Attributes.MOVEMENT_SPEED).hasModifier(runSpeed_2)) {
                player.m_21051_(Attributes.MOVEMENT_SPEED).addTransientModifier(runSpeed_2);
                player.playSound(SFX.Event.Artifact.DEMON_ARMOR_SPRINT_START, 1.0F, 1.0F);
            } else if (identifier == "demon_armor_set_bonus_3" && !player.m_21051_(Attributes.MOVEMENT_SPEED).hasModifier(runSpeed_3)) {
                player.m_21051_(Attributes.MOVEMENT_SPEED).addTransientModifier(runSpeed_3);
                player.playSound(SFX.Event.Artifact.DEMON_ARMOR_SPRINT_START, 1.0F, 1.2F);
            }
        }
    }

    public boolean handlePlayerJump(Player player) {
        if (this.isSetEquipped(player) && player.m_20142_()) {
            float multiplier = (float) player.m_21133_(Attributes.MOVEMENT_SPEED) * 6.0F;
            player.m_5997_((double) ((float) (player.m_20184_().x * (double) multiplier)), 0.75, (double) ((float) (player.m_20184_().z * (double) multiplier)));
            return true;
        } else {
            return false;
        }
    }

    private void handlePlayerMeteorJump(Player player) {
        if (player.m_20096_()) {
            this.handlePlayerMeteorJumpImpact(player, 0.0F);
        }
        if (this.isSetEquipped(player)) {
            player.m_5997_(0.0, -0.05, 0.0);
            if (player.m_9236_().isClientSide()) {
                for (int i = 0; i < 25; i++) {
                    player.m_9236_().addParticle(new MAParticleType(ParticleInit.FLAME.get()), player.m_20185_() - 0.5 + Math.random() * 0.5, player.m_20186_() + Math.random(), player.m_20189_() - 0.5 + Math.random() * 0.5, 0.0, 0.0, 0.0);
                }
            }
        }
    }

    public boolean handlePlayerMeteorJumpImpact(Player player, float fallDistance) {
        if (!this.isSetEquipped(player)) {
            return false;
        } else {
            if (player.getPersistentData().contains("demon_armor_meteor_jumping")) {
                player.getPersistentData().remove("demon_armor_meteor_jumping");
                if (!player.m_9236_().isClientSide() && fallDistance > 5.0F) {
                    MAExplosion.make(player, (ServerLevel) player.m_9236_(), player.m_20185_(), player.m_20186_(), player.m_20189_(), Math.min(5.0F, fallDistance / 6.0F), Math.min(20.0F, fallDistance / 1.5F), true, GeneralConfigValues.MeteorJumpEnabled && ((ServerLevel) player.m_9236_()).getServer().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING) ? Explosion.BlockInteraction.DESTROY : Explosion.BlockInteraction.KEEP, player.m_269291_().explosion(player, null));
                }
            }
            return true;
        }
    }

    @Override
    public void applySetBonus(LivingEntity living, EquipmentSlot... setSlots) {
        if (living instanceof Player) {
            living.getPersistentData().putBoolean("demon_armor_set_bonus", true);
        }
    }

    @Override
    public void removeSetBonus(LivingEntity living, EquipmentSlot... setSlots) {
        if (living instanceof Player) {
            living.getPersistentData().putBoolean("demon_armor_set_bonus", false);
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
        return Factions.DEMONS;
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
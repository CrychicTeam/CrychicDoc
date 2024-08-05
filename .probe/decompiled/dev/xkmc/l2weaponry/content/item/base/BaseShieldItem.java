package dev.xkmc.l2weaponry.content.item.base;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.ImmutableMultimap.Builder;
import dev.xkmc.l2library.init.events.GeneralEventHandler;
import dev.xkmc.l2library.util.math.MathHelper;
import dev.xkmc.l2weaponry.content.capability.IShieldData;
import dev.xkmc.l2weaponry.content.capability.LWPlayerData;
import dev.xkmc.l2weaponry.init.registrate.LWItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

public class BaseShieldItem extends ShieldItem {

    public static final String KEY_LAST_DAMAGE = "last_damage";

    private static final String NAME_ATTR_MAIN = "shield_defense_mainhand";

    private static final String NAME_ATTR_OFF = "shield_defense_offhand";

    protected final int maxDefense;

    protected final double recover;

    protected final boolean lightWeight;

    private final Multimap<Attribute, AttributeModifier> mainhandModifiers;

    private final Multimap<Attribute, AttributeModifier> offhandModifiers;

    public BaseShieldItem(Item.Properties pProperties, int maxDefense, double recover, boolean lightWeight) {
        super(pProperties);
        this.maxDefense = maxDefense;
        this.recover = recover;
        this.lightWeight = lightWeight;
        Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        this.buildAttributes(EquipmentSlot.MAINHAND, builder);
        this.mainhandModifiers = builder.build();
        builder = ImmutableMultimap.builder();
        this.buildAttributes(EquipmentSlot.OFFHAND, builder);
        this.offhandModifiers = builder.build();
        LWItems.BLOCK_DECO.add(this);
    }

    protected void buildAttributes(EquipmentSlot slot, Builder<Attribute, AttributeModifier> builder) {
        if (slot == EquipmentSlot.MAINHAND) {
            builder.put((Attribute) LWItems.SHIELD_DEFENSE.get(), new AttributeModifier(MathHelper.getUUIDFromString("shield_defense_mainhand"), "shield_defense_mainhand", (double) this.maxDefense, AttributeModifier.Operation.ADDITION));
        }
        if (slot == EquipmentSlot.OFFHAND) {
            builder.put((Attribute) LWItems.SHIELD_DEFENSE.get(), new AttributeModifier(MathHelper.getUUIDFromString("shield_defense_offhand"), "shield_defense_offhand", (double) this.maxDefense, AttributeModifier.Operation.ADDITION));
        }
    }

    public void takeDamage(ItemStack stack, Player player, int amount) {
        stack.getOrCreateTag().putInt("last_damage", amount);
        if (this.lightWeight(stack)) {
            int cd = this.damageShield(player, stack, -1.0);
            if (cd > 0 && player instanceof ServerPlayer) {
                player.getCooldowns().addCooldown(this, cd);
                player.m_9236_().broadcastEntityEvent(player, (byte) 30);
            }
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
        ItemStack itemstack = pPlayer.m_21120_(pHand);
        if (!this.lightWeight(itemstack) && pHand == InteractionHand.OFF_HAND) {
            return InteractionResultHolder.pass(itemstack);
        } else {
            pPlayer.m_6672_(pHand);
            this.startUse(pPlayer);
            return InteractionResultHolder.consume(itemstack);
        }
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        ItemStack stack = pContext.getItemInHand();
        InteractionHand hand = pContext.getHand();
        Player player = pContext.getPlayer();
        if (!this.lightWeight(stack) && hand == InteractionHand.OFF_HAND) {
            return InteractionResult.PASS;
        } else if (player != null) {
            player.m_6672_(hand);
            this.startUse(player);
            return InteractionResult.CONSUME;
        } else {
            return super.m_6225_(pContext);
        }
    }

    protected void startUse(Player player) {
        LWPlayerData cap = LWPlayerData.HOLDER.get(player);
        cap.startReflectTimer();
    }

    @Override
    public void releaseUsing(ItemStack pStack, Level pLevel, LivingEntity user, int pTimeCharged) {
        if (user instanceof Player player) {
            LWPlayerData cap = LWPlayerData.HOLDER.get(player);
            cap.clearReflectTimer();
        }
    }

    public boolean lightWeight(ItemStack stack) {
        return this.lightWeight;
    }

    public double getDefenseRecover(ItemStack stack) {
        return this.recover;
    }

    public double getMaxDefense(LivingEntity player) {
        AttributeInstance attr = player.getAttribute((Attribute) LWItems.SHIELD_DEFENSE.get());
        return attr == null ? (double) this.maxDefense : attr.getValue();
    }

    @Override
    public void onUseTick(Level pLevel, LivingEntity entity, ItemStack pStack, int pRemainingUseDuration) {
        if (entity instanceof ServerPlayer player && player.m_36335_().isOnCooldown(this)) {
            player.m_5810_();
        }
    }

    public int damageShield(Player player, ItemStack stack, double v) {
        return this.damageShieldImpl(player, LWPlayerData.HOLDER.get(player), stack, v);
    }

    public int damageShieldImpl(LivingEntity player, IShieldData cap, ItemStack stack, double v) {
        CompoundTag c = stack.getOrCreateTag();
        int damage = c.getInt("last_damage");
        c.putInt("last_damage", 0);
        double defense = cap.getShieldDefense();
        double max = this.getMaxDefense(player);
        double retain = cap.popRetain();
        double light = Math.max(0.0, (double) damage - retain);
        double heavy = Math.max(0.0, (double) damage * v - retain);
        defense += v < 0.0 ? light / max : (this.lightWeight(stack) ? v : heavy / this.getMaxDefense(player));
        if (defense >= 1.0) {
            cap.setShieldDefense(1.0);
            return 100;
        } else {
            cap.setShieldDefense(defense);
            return 0;
        }
    }

    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        return (Multimap<Attribute, AttributeModifier>) (slot == EquipmentSlot.MAINHAND ? this.mainhandModifiers : (this.lightWeight(stack) && slot == EquipmentSlot.OFFHAND ? this.offhandModifiers : ImmutableMultimap.of()));
    }

    public double reflect(ItemStack stack, Player player, LivingEntity target) {
        return this.reflectImpl(stack, this.getReflectSource(player), player.m_21133_(Attributes.ATTACK_DAMAGE), LWPlayerData.HOLDER.get(player), target);
    }

    protected void onBlock(ItemStack stack, LivingEntity user, LivingEntity target) {
    }

    protected double onReflect(ItemStack stack, LivingEntity user, LivingEntity target, double original, double reflect) {
        return reflect;
    }

    protected DamageSource getReflectSource(Player player) {
        return player.m_9236_().damageSources().playerAttack(player);
    }

    public double reflectImpl(ItemStack stack, DamageSource source, double additional, IShieldData data, LivingEntity target) {
        LivingEntity user = (LivingEntity) source.getEntity();
        assert user != null;
        this.onBlock(stack, user, target);
        CompoundTag c = stack.getOrCreateTag();
        double damage = (double) c.getInt("last_damage");
        if (data.canReflect() && data.getReflectTimer() > 0) {
            double finalDamage = this.onReflect(stack, user, target, damage, damage + additional);
            GeneralEventHandler.schedule(() -> target.hurt(source, (float) finalDamage));
            return 2.0;
        } else {
            double extra = this.onReflect(stack, user, target, damage, 0.0);
            if (extra > 0.0) {
                GeneralEventHandler.schedule(() -> target.hurt(source, (float) extra));
            }
            return 0.5;
        }
    }
}
package com.mna.items.artifice;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.ImmutableMultimap.Builder;
import com.mna.api.faction.IFaction;
import com.mna.api.items.IFactionSpecific;
import com.mna.api.sound.SFX;
import com.mna.capabilities.playerdata.magic.PlayerMagicProvider;
import com.mna.enchantments.framework.EnchantmentInit;
import com.mna.factions.Factions;
import com.mna.items.ItemInit;
import java.util.ArrayList;
import java.util.Map;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class ItemHellfireTrident extends Item implements IFactionSpecific {

    public static final int THROW_THRESHOLD_TIME = 10;

    public static final float BASE_DAMAGE = 10.0F;

    public static final float SHOOT_POWER = 2.5F;

    private final int enchantability = 25;

    private final Multimap<Attribute, AttributeModifier> defaultModifiers;

    private final ArrayList<Enchantment> allowedEnchantments = new ArrayList();

    public ItemHellfireTrident() {
        super(new Item.Properties().fireResistant().stacksTo(1).rarity(Rarity.EPIC).durability(500));
        Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(f_41374_, "Tool modifier", 10.0, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(f_41375_, "Tool modifier", -1.9F, AttributeModifier.Operation.ADDITION));
        this.defaultModifiers = builder.build();
    }

    protected ArrayList<Enchantment> getAllowedEnchantments() {
        this.allowedEnchantments.clear();
        if (this.allowedEnchantments.size() == 0) {
            this.allowedEnchantments.add(Enchantments.SHARPNESS);
            this.allowedEnchantments.add(Enchantments.KNOCKBACK);
            this.allowedEnchantments.add(Enchantments.MOB_LOOTING);
            this.allowedEnchantments.add(Enchantments.SMITE);
            this.allowedEnchantments.add(Enchantments.IMPALING);
            this.allowedEnchantments.add(Enchantments.FIRE_ASPECT);
            this.allowedEnchantments.add(Enchantments.MENDING);
            this.allowedEnchantments.add(Enchantments.UNBREAKING);
            this.allowedEnchantments.add(EnchantmentInit.MANA_REPAIR.get());
            this.allowedEnchantments.add(EnchantmentInit.BEHEADING.get());
        }
        return this.allowedEnchantments;
    }

    public int getEnchantmentValue(ItemStack stack) {
        return 25;
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return true;
    }

    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(book);
        for (Enchantment ench : enchantments.keySet()) {
            if (!this.getAllowedEnchantments().contains(ench)) {
                return false;
            }
        }
        return true;
    }

    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return this.getAllowedEnchantments().contains(enchantment);
    }

    @Override
    public boolean canAttackBlock(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer) {
        return !pPlayer.isCreative();
    }

    @Override
    public void releaseUsing(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity, int pTimeCharged) {
        if (pLivingEntity instanceof Player player) {
            player.getCapability(PlayerMagicProvider.MAGIC).ifPresent(magic -> {
                int manaCost = player.m_20096_() ? 100 : 200;
                if (magic.getCastingResource().hasEnough(player, (float) manaCost)) {
                    int j = 3;
                    magic.getCastingResource().consume(player, (float) manaCost);
                    this.usedByPlayer(player);
                    float f7 = player.m_146908_();
                    float f = player.m_146909_();
                    float f1 = -Mth.sin(f7 * (float) (Math.PI / 180.0)) * Mth.cos(f * (float) (Math.PI / 180.0));
                    float f2 = -Mth.sin(f * (float) (Math.PI / 180.0));
                    float f3 = Mth.cos(f7 * (float) (Math.PI / 180.0)) * Mth.cos(f * (float) (Math.PI / 180.0));
                    float f4 = Mth.sqrt(f1 * f1 + f2 * f2 + f3 * f3);
                    float f5 = 3.0F * ((1.0F + (float) j) / 4.0F);
                    f1 *= f5 / f4;
                    f2 *= f5 / f4;
                    f3 *= f5 / f4;
                    player.m_5997_((double) f1, (double) f2, (double) f3);
                    player.startAutoSpinAttack(20);
                    if (player.m_20096_()) {
                        player.m_6478_(MoverType.SELF, new Vec3(0.0, 1.1999999F, 0.0));
                    }
                    pLevel.playSound((Player) null, player, SFX.Spell.Impact.Single.FIRE, SoundSource.PLAYERS, 1.0F, 1.0F);
                    pLevel.playSound((Player) null, player, SoundEvents.TRIDENT_RIPTIDE_3, SoundSource.PLAYERS, 1.0F, 1.0F);
                    player.getCooldowns().addCooldown(this, 20);
                    player.getPersistentData().putBoolean("hellfiretrident", true);
                }
            });
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
        ItemStack itemstack = pPlayer.m_21120_(pHand);
        if (itemstack.getDamageValue() >= itemstack.getMaxDamage() - 1) {
            return InteractionResultHolder.fail(itemstack);
        } else {
            pPlayer.m_6672_(pHand);
            return InteractionResultHolder.consume(itemstack);
        }
    }

    @Override
    public IFaction getFaction() {
        return Factions.DEMONS;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.SPEAR;
    }

    @Override
    public int getUseDuration(ItemStack pStack) {
        return 72000;
    }

    @Override
    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        pStack.hurtAndBreak(1, pAttacker, p_43414_ -> p_43414_.broadcastBreakEvent(EquipmentSlot.MAINHAND));
        return true;
    }

    @Override
    public boolean mineBlock(ItemStack pStack, Level pLevel, BlockState pState, BlockPos pPos, LivingEntity pEntityLiving) {
        if ((double) pState.m_60800_(pLevel, pPos) != 0.0) {
            pStack.hurtAndBreak(2, pEntityLiving, p_43385_ -> p_43385_.broadcastBreakEvent(EquipmentSlot.MAINHAND));
        }
        return true;
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot pEquipmentSlot) {
        return pEquipmentSlot == EquipmentSlot.MAINHAND ? this.defaultModifiers : super.getDefaultAttributeModifiers(pEquipmentSlot);
    }

    @Override
    public int getEnchantmentValue() {
        return 1;
    }

    @Override
    public boolean isValidRepairItem(ItemStack pStack, ItemStack pRepairCandidate) {
        return pRepairCandidate.getItem() == ItemInit.LIVING_FLAME.get();
    }
}
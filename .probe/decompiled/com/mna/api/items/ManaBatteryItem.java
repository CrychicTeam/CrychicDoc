package com.mna.api.items;

import com.mna.api.ManaAndArtificeMod;
import com.mna.api.capabilities.IPlayerMagic;
import com.mna.api.particles.MAParticleType;
import com.mna.api.particles.ParticleInit;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

public class ManaBatteryItem extends ChargeableItem {

    static final String KEY_MODE = "mana_crystal_fragment_mode";

    static final String KEY_MANA = "mana_crystal_fragment_mana";

    static final int MODE_INFUSE = 0;

    static final int MODE_SUPPLEMENT = 1;

    public ManaBatteryItem(Item.Properties properties, float maxMana) {
        super(properties, maxMana);
    }

    public int getMode(ItemStack stack) {
        return stack.hasTag() && stack.getTag().contains("mana_crystal_fragment_mode") ? stack.getTag().getInt("mana_crystal_fragment_mode") : 0;
    }

    protected void setMode(ItemStack stack, int mode) {
        stack.getOrCreateTag().putInt("mana_crystal_fragment_mode", mode);
    }

    @Override
    public void onUseTick(Level pLevel, LivingEntity living, ItemStack stack, int pRemainingUseDuration) {
        IPlayerMagic magic = (IPlayerMagic) living.getCapability(ManaAndArtificeMod.getMagicCapability()).orElse(null);
        if (magic != null && living instanceof Player) {
            int mana_shift_amount = 10;
            switch(this.getMode(stack)) {
                case 0:
                default:
                    if (living.m_9236_().isClientSide()) {
                        living.m_9236_().addParticle(new MAParticleType(ParticleInit.BLUE_SPARKLE_GRAVITY.get()), living.m_20185_() - 0.5 + Math.random(), living.m_20186_() + Math.random(), living.m_20189_() - 0.5 + Math.random(), 0.0, 0.1F, 0.0);
                    } else {
                        if (!(magic.getCastingResource().getAmount() > (float) mana_shift_amount)) {
                            living.stopUsingItem();
                            return;
                        }
                        if (!(this.refundMana(stack, (float) mana_shift_amount, (Player) living) > 0.0F)) {
                            living.stopUsingItem();
                            return;
                        }
                        magic.getCastingResource().setAmount(magic.getCastingResource().getAmount() - (float) mana_shift_amount);
                    }
                    break;
                case 1:
                    if (living.m_9236_().isClientSide()) {
                        living.m_9236_().addParticle(new MAParticleType(ParticleInit.BLUE_SPARKLE_GRAVITY.get()), living.m_20185_() - 0.5 + Math.random(), living.m_20186_() + Math.random(), living.m_20189_() - 0.5 + Math.random(), 0.0, 0.1F, 0.0);
                    } else if (magic.getCastingResource().getAmount() < magic.getCastingResource().getMaxAmount()) {
                        if (this.consumeMana(stack, (float) mana_shift_amount, null)) {
                            magic.getCastingResource().restore((float) mana_shift_amount);
                        } else {
                            living.stopUsingItem();
                        }
                    }
            }
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
        ItemStack stack = playerIn.m_21120_(handIn);
        if (playerIn.m_6144_()) {
            if (!worldIn.isClientSide) {
                if (this.getMode(stack) == 0) {
                    this.setMode(stack, 1);
                } else {
                    this.setMode(stack, 0);
                }
            }
            return InteractionResultHolder.success(stack);
        } else if (this.getMode(stack) == 1) {
            if (this.getMana(stack) > 0.0F) {
                playerIn.getCapability(ManaAndArtificeMod.getMagicCapability()).ifPresent(m -> {
                    if (m.getCastingResource().canRechargeFrom(stack) && m.getCastingResource().getAmount() < m.getCastingResource().getMaxAmount()) {
                        playerIn.m_6672_(handIn);
                    }
                });
            }
            return InteractionResultHolder.sidedSuccess(stack, worldIn.isClientSide);
        } else {
            return super.use(worldIn, playerIn, handIn);
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        switch(this.getMode(stack)) {
            case 0:
            default:
                tooltip.add(Component.translatable("item.mna.mana_battery.infusion").withStyle(ChatFormatting.AQUA));
                break;
            case 1:
                tooltip.add(Component.translatable("item.mna.mana_battery.supplement").withStyle(ChatFormatting.AQUA));
        }
        Minecraft mc = Minecraft.getInstance();
        if (mc != null && mc.player != null) {
            mc.player.getCapability(ManaAndArtificeMod.getMagicCapability()).ifPresent(m -> {
                if (!m.getCastingResource().canRechargeFrom(stack)) {
                    tooltip.add(Component.translatable("item.mna.mana_battery.no_supplement").withStyle(ChatFormatting.RED));
                }
            });
        }
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
    }

    @Override
    public boolean canContinueUsing(ItemStack oldStack, ItemStack newStack) {
        if (this.getMode(newStack) == 0) {
            if (this.getMana(newStack) < this.getMaxMana()) {
                return true;
            }
        } else if (this.getMode(newStack) == 1 && this.getMana(newStack) >= 0.0F) {
            return true;
        }
        return false;
    }

    @Override
    protected boolean tickCurio() {
        return false;
    }

    @Override
    protected boolean tickInventory() {
        return false;
    }

    @Override
    protected boolean tickEffect(ItemStack stack, Player player, Level world, int slot, float mana, boolean selected) {
        return false;
    }
}
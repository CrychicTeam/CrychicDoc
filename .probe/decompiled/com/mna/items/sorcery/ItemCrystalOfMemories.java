package com.mna.items.sorcery;

import com.mna.api.items.IShowHud;
import com.mna.api.items.TieredItem;
import com.mna.api.particles.MAParticleType;
import com.mna.api.particles.ParticleInit;
import com.mna.items.ItemInit;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FastColor;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;

public class ItemCrystalOfMemories extends TieredItem implements IShowHud {

    private static final String KEY_MODE = "memory_crystal_fragment_mode";

    private static final int MODE_INFUSE = 0;

    private static final int MODE_SUPPLEMENT = 1;

    private static final String KEY_STORED_XP = "stored_xp";

    private static final int MAX_XP = 20000;

    public ItemCrystalOfMemories() {
        super(new Item.Properties().durability(100).setNoRepair());
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
    }

    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return false;
    }

    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return false;
    }

    public int getEnchantmentValue(ItemStack stack) {
        return 0;
    }

    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        return InteractionResult.PASS;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.NONE;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 100;
    }

    public boolean canContinueUsing(ItemStack oldStack, ItemStack newStack) {
        return true;
    }

    private int getContainedXP(ItemStack stack) {
        return stack.hasTag() && stack.getItem() == ItemInit.CRYSTAL_OF_MEMORIES.get() ? stack.getTag().getInt("stored_xp") : 0;
    }

    private void storeContainedXP(ItemStack stack, int amount) {
        if (stack.getItem() == ItemInit.CRYSTAL_OF_MEMORIES.get()) {
            stack.getOrCreateTag().putInt("stored_xp", amount);
        }
    }

    @Override
    public void onUseTick(Level pLevel, LivingEntity living, ItemStack stack, int count) {
        if (living instanceof Player p) {
            int contained_xp = this.getContainedXP(stack);
            if (!living.m_9236_().isClientSide()) {
                switch(this.getMode(stack)) {
                    case 0:
                    default:
                        if (contained_xp == 20000 || p.totalExperience == 0) {
                            return;
                        }
                        int xp_to_store = p.experienceLevel == 0 ? 1 : p.experienceLevel;
                        if (contained_xp + xp_to_store > 20000) {
                            xp_to_store = 20000 - contained_xp;
                        }
                        p.giveExperiencePoints(-xp_to_store);
                        this.storeContainedXP(stack, Math.min(contained_xp + xp_to_store, 20000));
                        break;
                    case 1:
                        if (contained_xp == 0) {
                            return;
                        }
                        int xp_to_give = Math.max(p.experienceLevel, 1);
                        if (contained_xp < xp_to_give) {
                            xp_to_give = contained_xp;
                        }
                        p.giveExperiencePoints(xp_to_give);
                        this.storeContainedXP(stack, contained_xp - xp_to_give);
                }
            }
        }
        if (living.m_9236_().isClientSide()) {
            for (int i = 0; i < 5; i++) {
                living.m_9236_().addParticle(new MAParticleType(ParticleInit.BLUE_SPARKLE_GRAVITY.get()), living.m_20185_() - 0.5 + Math.random(), living.m_20186_() + Math.random(), living.m_20189_() - 0.5 + Math.random(), 0.0, 0.1F, 0.0);
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
        } else {
            playerIn.m_6672_(handIn);
            return InteractionResultHolder.consume(stack);
        }
    }

    public int getMode(ItemStack stack) {
        return stack.hasTag() && stack.getTag().contains("memory_crystal_fragment_mode") ? stack.getTag().getInt("memory_crystal_fragment_mode") : 0;
    }

    private void setMode(ItemStack stack, int mode) {
        stack.getOrCreateTag().putInt("memory_crystal_fragment_mode", mode);
    }

    @Override
    public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        int contained_xp = this.getContainedXP(stack);
        if (contained_xp == 20000) {
            tooltip.add(Component.translatable("item.mna.crystal_of_memories.full", stack.getMaxDamage()).withStyle(ChatFormatting.ITALIC));
        } else {
            tooltip.add(Component.translatable("item.mna.crystal_of_memories.contains", contained_xp, 20000).withStyle(ChatFormatting.ITALIC));
        }
        switch(this.getMode(stack)) {
            case 0:
            default:
                tooltip.add(Component.translatable("item.mna.crystal_of_memories.infusion"));
                break;
            case 1:
                tooltip.add(Component.translatable("item.mna.crystal_of_memories.supplement"));
        }
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return FastColor.ARGB32.color(255, 128, 64, 255);
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return true;
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        float xp = (float) this.getContainedXP(stack);
        return (int) (13.0F * (xp / 20000.0F));
    }
}
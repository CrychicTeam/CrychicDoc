package com.mna.api.items;

import com.mna.api.ManaAndArtificeMod;
import com.mna.api.capabilities.CurioItemCapability;
import com.mna.api.particles.MAParticleType;
import com.mna.api.particles.ParticleInit;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FastColor;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.extensions.IForgeItem;
import org.apache.commons.lang3.mutable.MutableBoolean;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICurio;

public abstract class ChargeableItem extends TieredItem implements IShowHud, IForgeItem, IFactionSpecific {

    private static final String KEY_NBT_MANA = "mana";

    private float max_mana;

    public ChargeableItem(Item.Properties properties, float maxMana) {
        super(properties.durability(100));
        this.max_mana = maxMana;
    }

    public boolean consumeMana(ItemStack stack, float amount, @Nullable Player player) {
        if (player != null) {
            this.usedByPlayer(player);
        }
        if (player != null && !(this instanceof ManaBatteryItem)) {
            for (ItemStack invStack : (List) player.getInventory().items.stream().filter(i -> i.getItem() instanceof ManaBatteryItem).collect(Collectors.toList())) {
                ManaBatteryItem item = (ManaBatteryItem) invStack.getItem();
                float contained = item.getMana(invStack);
                float toConsume = amount;
                if (contained < amount) {
                    toConsume = contained;
                }
                if (item.consumeMana(invStack, toConsume, player)) {
                    amount -= toConsume;
                }
                if (amount <= 0.0F) {
                    return true;
                }
            }
        }
        CompoundTag tag = stack.getTag();
        if (tag == null) {
            return false;
        } else {
            float curMana = tag.getFloat("mana");
            if (curMana < amount) {
                return false;
            } else {
                curMana -= amount;
                tag.putFloat("mana", curMana);
                return true;
            }
        }
    }

    public float refundMana(ItemStack stack, float amount, @Nullable Player player) {
        CompoundTag tag = stack.getTag();
        if (tag == null) {
            return 0.0F;
        } else {
            float curMana = tag.getFloat("mana");
            if (curMana + amount > this.max_mana) {
                amount = this.max_mana - curMana;
            }
            curMana += amount;
            tag.putFloat("mana", curMana);
            return amount;
        }
    }

    @Override
    public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T entity, Consumer<T> onBroken) {
        return 0;
    }

    @Override
    public ICapabilityProvider initCapabilities(final ItemStack stack, CompoundTag nbt) {
        return this.tickCurio() ? CurioItemCapability.createProvider(new ICurio() {

            @Override
            public void curioTick(String identifier, int index, LivingEntity livingEntity) {
                if (ChargeableItem.this.beforeCurioTick(livingEntity, index, stack)) {
                    ChargeableItem.this.internalInventoryTick(livingEntity, index, stack, false);
                }
            }

            @Override
            public ItemStack getStack() {
                return stack;
            }
        }) : super.initCapabilities(stack, nbt);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (this.tickInventory()) {
            this.internalInventoryTick(entityIn, itemSlot, stack, isSelected);
        }
    }

    protected boolean beforeCurioTick(LivingEntity entity, int index, ItemStack stack) {
        return true;
    }

    private final void internalInventoryTick(Entity entity, int slot, ItemStack stack, boolean selected) {
        if (entity instanceof Player player) {
            float mana = this.getMana(stack);
            if (mana >= this.manaPerOperation() && this.tickEffect(stack, player, player.m_9236_(), slot, mana, selected)) {
                this.consumeMana(stack, this.manaPerOperation(), player);
            }
        }
    }

    public final boolean applyEffect(ItemStack stack, Player player, Level world, int slot, float mana, boolean selected) {
        float curMana = this.getMana(stack);
        if (curMana < mana) {
            return false;
        } else if (this.tickEffect(stack, player, world, slot, curMana, selected)) {
            this.consumeMana(stack, this.manaPerOperation(), player);
            return true;
        } else {
            return false;
        }
    }

    protected abstract boolean tickEffect(ItemStack var1, Player var2, Level var3, int var4, float var5, boolean var6);

    protected float manaPerRechargeTick() {
        return 10.0F;
    }

    public final float getMana(ItemStack stack) {
        return stack.hasTag() ? stack.getTag().getFloat("mana") : 0.0F;
    }

    public final float getMaxMana() {
        return this.max_mana;
    }

    protected boolean tickCurio() {
        return true;
    }

    protected boolean tickInventory() {
        return false;
    }

    protected float manaPerOperation() {
        return 1.0F;
    }

    protected float minimumManaToStartChanneling() {
        return this.manaPerRechargeTick() * 5.0F;
    }

    protected int repairPerRechargeTick() {
        return 10;
    }

    protected boolean canChannelRepair() {
        return true;
    }

    @Override
    public boolean canContinueUsing(ItemStack oldStack, ItemStack newStack) {
        return this.canChannelRepair() && this.getMana(newStack) < this.getMaxMana();
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 9999;
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        return InteractionResult.PASS;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
        ItemStack stack = playerIn.m_21120_(handIn);
        if (stack.getItem() != this) {
            return InteractionResultHolder.fail(stack);
        } else {
            if (this.canChannelRepair() && this.getMana(stack) < this.max_mana) {
                MutableBoolean hasMana = new MutableBoolean(false);
                playerIn.getCapability(ManaAndArtificeMod.getMagicCapability()).ifPresent(m -> {
                    if (m.getCastingResource().hasEnoughAbsolute(playerIn, this.minimumManaToStartChanneling())) {
                        playerIn.m_6672_(handIn);
                    }
                });
                if (hasMana.getValue()) {
                    return InteractionResultHolder.success(playerIn.m_21120_(handIn));
                }
            }
            return InteractionResultHolder.fail(playerIn.m_21120_(handIn));
        }
    }

    @Override
    public void onUseTick(Level pLevel, LivingEntity living, ItemStack stack, int pRemainingUseDuration) {
        if (living instanceof Player) {
            if (!living.m_9236_().isClientSide()) {
                ((Player) living).getCapability(ManaAndArtificeMod.getMagicCapability()).ifPresent(m -> {
                    if (m.getCastingResource().hasEnoughAbsolute(living, this.manaPerRechargeTick()) && this.getMana(stack) < this.getMaxMana()) {
                        m.getCastingResource().consume(living, this.manaPerRechargeTick());
                        this.refundMana(stack, ((Player) living).isCreative() ? (float) (this.repairPerRechargeTick() * 100) : (float) this.repairPerRechargeTick(), (Player) living);
                    } else {
                        ((Player) living).m_5810_();
                    }
                });
            } else {
                living.m_9236_().addParticle(new MAParticleType(ParticleInit.BLUE_SPARKLE_GRAVITY.get()), living.m_20185_() - 0.5 + Math.random(), living.m_20186_() + Math.random(), living.m_20189_() - 0.5 + Math.random(), 0.0, 0.1F, 0.0);
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip.add(Component.translatable("item.mna.chargeable.mana", this.getMana(stack), this.max_mana).withStyle(ChatFormatting.AQUA));
        tooltip.add(Component.translatable("item.mna.chargeable.pedestalCharge").withStyle(ChatFormatting.AQUA));
        if (this.canChannelRepair()) {
            tooltip.add(Component.translatable("item.mna.chargeable.rightClickCharge").withStyle(ChatFormatting.AQUA));
        }
    }

    public boolean isEquippedAndHasMana(LivingEntity living, float mana, boolean autoConsume) {
        MutableBoolean valid = new MutableBoolean(false);
        CuriosApi.getCuriosHelper().findFirstCurio(living, this).ifPresent(t -> {
            if (this.getMana(t.stack()) >= mana) {
                if (autoConsume && living instanceof Player) {
                    this.consumeMana(t.stack(), mana, (Player) living);
                }
                valid.setTrue();
            }
        });
        return valid.booleanValue();
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return FastColor.ARGB32.color(255, 128, 64, 255);
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        float mana = this.getMana(stack);
        float pct = mana / this.max_mana;
        return Math.round(13.0F * pct);
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return true;
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
    }

    @Override
    public boolean isRepairable(ItemStack stack) {
        return false;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return false;
    }

    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return false;
    }
}
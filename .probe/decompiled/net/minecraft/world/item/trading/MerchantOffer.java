package net.minecraft.world.item.trading;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;

public class MerchantOffer {

    private final ItemStack baseCostA;

    private final ItemStack costB;

    private final ItemStack result;

    private int uses;

    private final int maxUses;

    private boolean rewardExp = true;

    private int specialPriceDiff;

    private int demand;

    private float priceMultiplier;

    private int xp = 1;

    public MerchantOffer(CompoundTag compoundTag0) {
        this.baseCostA = ItemStack.of(compoundTag0.getCompound("buy"));
        this.costB = ItemStack.of(compoundTag0.getCompound("buyB"));
        this.result = ItemStack.of(compoundTag0.getCompound("sell"));
        this.uses = compoundTag0.getInt("uses");
        if (compoundTag0.contains("maxUses", 99)) {
            this.maxUses = compoundTag0.getInt("maxUses");
        } else {
            this.maxUses = 4;
        }
        if (compoundTag0.contains("rewardExp", 1)) {
            this.rewardExp = compoundTag0.getBoolean("rewardExp");
        }
        if (compoundTag0.contains("xp", 3)) {
            this.xp = compoundTag0.getInt("xp");
        }
        if (compoundTag0.contains("priceMultiplier", 5)) {
            this.priceMultiplier = compoundTag0.getFloat("priceMultiplier");
        }
        this.specialPriceDiff = compoundTag0.getInt("specialPrice");
        this.demand = compoundTag0.getInt("demand");
    }

    public MerchantOffer(ItemStack itemStack0, ItemStack itemStack1, int int2, int int3, float float4) {
        this(itemStack0, ItemStack.EMPTY, itemStack1, int2, int3, float4);
    }

    public MerchantOffer(ItemStack itemStack0, ItemStack itemStack1, ItemStack itemStack2, int int3, int int4, float float5) {
        this(itemStack0, itemStack1, itemStack2, 0, int3, int4, float5);
    }

    public MerchantOffer(ItemStack itemStack0, ItemStack itemStack1, ItemStack itemStack2, int int3, int int4, int int5, float float6) {
        this(itemStack0, itemStack1, itemStack2, int3, int4, int5, float6, 0);
    }

    public MerchantOffer(ItemStack itemStack0, ItemStack itemStack1, ItemStack itemStack2, int int3, int int4, int int5, float float6, int int7) {
        this.baseCostA = itemStack0;
        this.costB = itemStack1;
        this.result = itemStack2;
        this.uses = int3;
        this.maxUses = int4;
        this.xp = int5;
        this.priceMultiplier = float6;
        this.demand = int7;
    }

    public ItemStack getBaseCostA() {
        return this.baseCostA;
    }

    public ItemStack getCostA() {
        if (this.baseCostA.isEmpty()) {
            return ItemStack.EMPTY;
        } else {
            int $$0 = this.baseCostA.getCount();
            int $$1 = Math.max(0, Mth.floor((float) ($$0 * this.demand) * this.priceMultiplier));
            return this.baseCostA.copyWithCount(Mth.clamp($$0 + $$1 + this.specialPriceDiff, 1, this.baseCostA.getItem().getMaxStackSize()));
        }
    }

    public ItemStack getCostB() {
        return this.costB;
    }

    public ItemStack getResult() {
        return this.result;
    }

    public void updateDemand() {
        this.demand = this.demand + this.uses - (this.maxUses - this.uses);
    }

    public ItemStack assemble() {
        return this.result.copy();
    }

    public int getUses() {
        return this.uses;
    }

    public void resetUses() {
        this.uses = 0;
    }

    public int getMaxUses() {
        return this.maxUses;
    }

    public void increaseUses() {
        this.uses++;
    }

    public int getDemand() {
        return this.demand;
    }

    public void addToSpecialPriceDiff(int int0) {
        this.specialPriceDiff += int0;
    }

    public void resetSpecialPriceDiff() {
        this.specialPriceDiff = 0;
    }

    public int getSpecialPriceDiff() {
        return this.specialPriceDiff;
    }

    public void setSpecialPriceDiff(int int0) {
        this.specialPriceDiff = int0;
    }

    public float getPriceMultiplier() {
        return this.priceMultiplier;
    }

    public int getXp() {
        return this.xp;
    }

    public boolean isOutOfStock() {
        return this.uses >= this.maxUses;
    }

    public void setToOutOfStock() {
        this.uses = this.maxUses;
    }

    public boolean needsRestock() {
        return this.uses > 0;
    }

    public boolean shouldRewardExp() {
        return this.rewardExp;
    }

    public CompoundTag createTag() {
        CompoundTag $$0 = new CompoundTag();
        $$0.put("buy", this.baseCostA.save(new CompoundTag()));
        $$0.put("sell", this.result.save(new CompoundTag()));
        $$0.put("buyB", this.costB.save(new CompoundTag()));
        $$0.putInt("uses", this.uses);
        $$0.putInt("maxUses", this.maxUses);
        $$0.putBoolean("rewardExp", this.rewardExp);
        $$0.putInt("xp", this.xp);
        $$0.putFloat("priceMultiplier", this.priceMultiplier);
        $$0.putInt("specialPrice", this.specialPriceDiff);
        $$0.putInt("demand", this.demand);
        return $$0;
    }

    public boolean satisfiedBy(ItemStack itemStack0, ItemStack itemStack1) {
        return this.isRequiredItem(itemStack0, this.getCostA()) && itemStack0.getCount() >= this.getCostA().getCount() && this.isRequiredItem(itemStack1, this.costB) && itemStack1.getCount() >= this.costB.getCount();
    }

    private boolean isRequiredItem(ItemStack itemStack0, ItemStack itemStack1) {
        if (itemStack1.isEmpty() && itemStack0.isEmpty()) {
            return true;
        } else {
            ItemStack $$2 = itemStack0.copy();
            if ($$2.getItem().canBeDepleted()) {
                $$2.setDamageValue($$2.getDamageValue());
            }
            return ItemStack.isSameItem($$2, itemStack1) && (!itemStack1.hasTag() || $$2.hasTag() && NbtUtils.compareNbt(itemStack1.getTag(), $$2.getTag(), false));
        }
    }

    public boolean take(ItemStack itemStack0, ItemStack itemStack1) {
        if (!this.satisfiedBy(itemStack0, itemStack1)) {
            return false;
        } else {
            itemStack0.shrink(this.getCostA().getCount());
            if (!this.getCostB().isEmpty()) {
                itemStack1.shrink(this.getCostB().getCount());
            }
            return true;
        }
    }
}
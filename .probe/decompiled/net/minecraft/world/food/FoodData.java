package net.minecraft.world.food;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;

public class FoodData {

    private int foodLevel = 20;

    private float saturationLevel;

    private float exhaustionLevel;

    private int tickTimer;

    private int lastFoodLevel = 20;

    public FoodData() {
        this.saturationLevel = 5.0F;
    }

    public void eat(int int0, float float1) {
        this.foodLevel = Math.min(int0 + this.foodLevel, 20);
        this.saturationLevel = Math.min(this.saturationLevel + (float) int0 * float1 * 2.0F, (float) this.foodLevel);
    }

    public void eat(Item item0, ItemStack itemStack1) {
        if (item0.isEdible()) {
            FoodProperties $$2 = item0.getFoodProperties();
            this.eat($$2.getNutrition(), $$2.getSaturationModifier());
        }
    }

    public void tick(Player player0) {
        Difficulty $$1 = player0.m_9236_().m_46791_();
        this.lastFoodLevel = this.foodLevel;
        if (this.exhaustionLevel > 4.0F) {
            this.exhaustionLevel -= 4.0F;
            if (this.saturationLevel > 0.0F) {
                this.saturationLevel = Math.max(this.saturationLevel - 1.0F, 0.0F);
            } else if ($$1 != Difficulty.PEACEFUL) {
                this.foodLevel = Math.max(this.foodLevel - 1, 0);
            }
        }
        boolean $$2 = player0.m_9236_().getGameRules().getBoolean(GameRules.RULE_NATURAL_REGENERATION);
        if ($$2 && this.saturationLevel > 0.0F && player0.isHurt() && this.foodLevel >= 20) {
            this.tickTimer++;
            if (this.tickTimer >= 10) {
                float $$3 = Math.min(this.saturationLevel, 6.0F);
                player0.m_5634_($$3 / 6.0F);
                this.addExhaustion($$3);
                this.tickTimer = 0;
            }
        } else if ($$2 && this.foodLevel >= 18 && player0.isHurt()) {
            this.tickTimer++;
            if (this.tickTimer >= 80) {
                player0.m_5634_(1.0F);
                this.addExhaustion(6.0F);
                this.tickTimer = 0;
            }
        } else if (this.foodLevel <= 0) {
            this.tickTimer++;
            if (this.tickTimer >= 80) {
                if (player0.m_21223_() > 10.0F || $$1 == Difficulty.HARD || player0.m_21223_() > 1.0F && $$1 == Difficulty.NORMAL) {
                    player0.hurt(player0.m_269291_().starve(), 1.0F);
                }
                this.tickTimer = 0;
            }
        } else {
            this.tickTimer = 0;
        }
    }

    public void readAdditionalSaveData(CompoundTag compoundTag0) {
        if (compoundTag0.contains("foodLevel", 99)) {
            this.foodLevel = compoundTag0.getInt("foodLevel");
            this.tickTimer = compoundTag0.getInt("foodTickTimer");
            this.saturationLevel = compoundTag0.getFloat("foodSaturationLevel");
            this.exhaustionLevel = compoundTag0.getFloat("foodExhaustionLevel");
        }
    }

    public void addAdditionalSaveData(CompoundTag compoundTag0) {
        compoundTag0.putInt("foodLevel", this.foodLevel);
        compoundTag0.putInt("foodTickTimer", this.tickTimer);
        compoundTag0.putFloat("foodSaturationLevel", this.saturationLevel);
        compoundTag0.putFloat("foodExhaustionLevel", this.exhaustionLevel);
    }

    public int getFoodLevel() {
        return this.foodLevel;
    }

    public int getLastFoodLevel() {
        return this.lastFoodLevel;
    }

    public boolean needsFood() {
        return this.foodLevel < 20;
    }

    public void addExhaustion(float float0) {
        this.exhaustionLevel = Math.min(this.exhaustionLevel + float0, 40.0F);
    }

    public float getExhaustionLevel() {
        return this.exhaustionLevel;
    }

    public float getSaturationLevel() {
        return this.saturationLevel;
    }

    public void setFoodLevel(int int0) {
        this.foodLevel = int0;
    }

    public void setSaturation(float float0) {
        this.saturationLevel = float0;
    }

    public void setExhaustion(float float0) {
        this.exhaustionLevel = float0;
    }
}
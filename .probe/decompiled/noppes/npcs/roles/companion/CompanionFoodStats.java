package noppes.npcs.roles.companion;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Difficulty;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import noppes.npcs.entity.EntityNPCInterface;

public class CompanionFoodStats {

    private int foodLevel = 20;

    private float foodSaturationLevel = 5.0F;

    private float foodExhaustionLevel;

    private int foodTimer;

    private int prevFoodLevel = 20;

    private void addStats(int p_75122_1_, float p_75122_2_) {
        this.foodLevel = Math.min(p_75122_1_ + this.foodLevel, 20);
        this.foodSaturationLevel = Math.min(this.foodSaturationLevel + (float) p_75122_1_ * p_75122_2_ * 2.0F, (float) this.foodLevel);
    }

    public void onFoodEaten(FoodProperties food, ItemStack itemstack) {
        this.addStats(food.getNutrition(), food.getSaturationModifier());
    }

    public void onUpdate(EntityNPCInterface npc) {
        Difficulty enumdifficulty = npc.m_9236_().m_46791_();
        this.prevFoodLevel = this.foodLevel;
        if (this.foodExhaustionLevel > 4.0F) {
            this.foodExhaustionLevel -= 4.0F;
            if (this.foodSaturationLevel > 0.0F) {
                this.foodSaturationLevel = Math.max(this.foodSaturationLevel - 1.0F, 0.0F);
            } else if (enumdifficulty != Difficulty.PEACEFUL) {
                this.foodLevel = Math.max(this.foodLevel - 1, 0);
            }
        }
        if (npc.m_9236_().getGameRules().getBoolean(GameRules.RULE_NATURAL_REGENERATION) && this.foodLevel >= 18 && npc.m_21223_() > 0.0F && npc.m_21223_() < npc.m_21233_()) {
            this.foodTimer++;
            if (this.foodTimer >= 80) {
                npc.m_5634_(1.0F);
                this.addExhaustion(3.0F);
                this.foodTimer = 0;
            }
        } else if (this.foodLevel <= 0) {
            this.foodTimer++;
            if (this.foodTimer >= 80) {
                if (npc.m_21223_() > 10.0F || enumdifficulty == Difficulty.HARD || npc.m_21223_() > 1.0F && enumdifficulty == Difficulty.NORMAL) {
                    npc.hurt(npc.m_269291_().starve(), 1.0F);
                }
                this.foodTimer = 0;
            }
        } else {
            this.foodTimer = 0;
        }
    }

    public void readNBT(CompoundTag compound) {
        if (compound.contains("foodLevel", 99)) {
            this.foodLevel = compound.getInt("foodLevel");
            this.foodTimer = compound.getInt("foodTickTimer");
            this.foodSaturationLevel = compound.getFloat("foodSaturationLevel");
            this.foodExhaustionLevel = compound.getFloat("foodExhaustionLevel");
        }
    }

    public void writeNBT(CompoundTag compound) {
        compound.putInt("foodLevel", this.foodLevel);
        compound.putInt("foodTickTimer", this.foodTimer);
        compound.putFloat("foodSaturationLevel", this.foodSaturationLevel);
        compound.putFloat("foodExhaustionLevel", this.foodExhaustionLevel);
    }

    public int getFoodLevel() {
        return this.foodLevel;
    }

    @OnlyIn(Dist.CLIENT)
    public int getPrevFoodLevel() {
        return this.prevFoodLevel;
    }

    public boolean needFood() {
        return this.foodLevel < 20;
    }

    public void addExhaustion(float p_75113_1_) {
        this.foodExhaustionLevel = Math.min(this.foodExhaustionLevel + p_75113_1_, 40.0F);
    }

    public float getSaturationLevel() {
        return this.foodSaturationLevel;
    }

    @OnlyIn(Dist.CLIENT)
    public void setFoodLevel(int p_75114_1_) {
        this.foodLevel = p_75114_1_;
    }

    @OnlyIn(Dist.CLIENT)
    public void setFoodSaturationLevel(float p_75119_1_) {
        this.foodSaturationLevel = p_75119_1_;
    }
}
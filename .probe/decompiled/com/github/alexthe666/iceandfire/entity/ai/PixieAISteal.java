package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.entity.EntityPixie;
import com.github.alexthe666.iceandfire.misc.IafSoundRegistry;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class PixieAISteal extends Goal {

    private final EntityPixie temptedEntity;

    private Player temptingPlayer;

    private int delayTemptCounter = 0;

    private boolean isRunning;

    public PixieAISteal(EntityPixie temptedEntityIn, double speedIn) {
        this.temptedEntity = temptedEntityIn;
    }

    @Override
    public boolean canUse() {
        if (IafConfig.pixiesStealItems && this.temptedEntity.m_21205_().isEmpty() && this.temptedEntity.stealCooldown <= 0) {
            if (this.temptedEntity.m_217043_().nextInt(200) == 0) {
                return false;
            } else if (this.temptedEntity.m_21824_()) {
                return false;
            } else if (this.delayTemptCounter > 0) {
                this.delayTemptCounter--;
                return false;
            } else {
                this.temptingPlayer = this.temptedEntity.m_9236_().m_45930_(this.temptedEntity, 10.0);
                return this.temptingPlayer != null && this.temptedEntity.m_21120_(InteractionHand.MAIN_HAND).isEmpty() && !this.temptingPlayer.getInventory().isEmpty() && !this.temptingPlayer.isCreative();
            }
        } else {
            return false;
        }
    }

    @Override
    public boolean canContinueToUse() {
        return !this.temptedEntity.m_21824_() && this.temptedEntity.m_21205_().isEmpty() && this.delayTemptCounter == 0 && this.temptedEntity.stealCooldown == 0;
    }

    @Override
    public void start() {
        this.isRunning = true;
    }

    @Override
    public void stop() {
        this.temptingPlayer = null;
        if (this.delayTemptCounter < 10) {
            this.delayTemptCounter += 10;
        }
        this.isRunning = false;
    }

    @Override
    public void tick() {
        this.temptedEntity.m_21563_().setLookAt(this.temptingPlayer, (float) (this.temptedEntity.m_8085_() + 20), (float) this.temptedEntity.m_8132_());
        ArrayList<Integer> slotlist = new ArrayList();
        if (this.temptedEntity.m_20280_(this.temptingPlayer) < 3.0 && !this.temptingPlayer.getInventory().isEmpty()) {
            for (int i = 0; i < this.temptingPlayer.getInventory().getContainerSize(); i++) {
                ItemStack targetStack = this.temptingPlayer.getInventory().getItem(i);
                if (!Inventory.isHotbarSlot(i) && !targetStack.isEmpty() && targetStack.isStackable()) {
                    slotlist.add(i);
                }
            }
            if (!slotlist.isEmpty()) {
                int slot;
                if (slotlist.size() == 1) {
                    slot = (Integer) slotlist.get(0);
                } else {
                    slot = (Integer) slotlist.get(ThreadLocalRandom.current().nextInt(slotlist.size()));
                }
                ItemStack randomItem = this.temptingPlayer.getInventory().getItem(slot);
                this.temptedEntity.m_21008_(InteractionHand.MAIN_HAND, randomItem);
                this.temptingPlayer.getInventory().removeItemNoUpdate(slot);
                this.temptedEntity.flipAI(true);
                this.temptedEntity.m_5496_(IafSoundRegistry.PIXIE_TAUNT, 1.0F, 1.0F);
                for (EntityPixie pixie : this.temptingPlayer.m_9236_().m_45976_(EntityPixie.class, this.temptedEntity.m_20191_().inflate(40.0))) {
                    pixie.stealCooldown = 1000 + pixie.m_217043_().nextInt(3000);
                }
                if (this.temptingPlayer != null) {
                    this.temptingPlayer.m_7292_(new MobEffectInstance(this.temptedEntity.negativePotions[this.temptedEntity.getColor()], 100));
                }
            } else {
                this.temptedEntity.flipAI(true);
                this.delayTemptCounter = 200;
            }
        } else {
            this.temptedEntity.m_21566_().setWantedPosition(this.temptingPlayer.m_20185_(), this.temptingPlayer.m_20186_() + 1.5, this.temptingPlayer.m_20189_(), 1.0);
        }
    }

    public boolean isRunning() {
        return this.isRunning;
    }
}
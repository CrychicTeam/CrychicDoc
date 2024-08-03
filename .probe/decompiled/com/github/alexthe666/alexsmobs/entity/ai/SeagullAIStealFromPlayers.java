package com.github.alexthe666.alexsmobs.entity.ai;

import com.github.alexthe666.alexsmobs.config.AMConfig;
import com.github.alexthe666.alexsmobs.entity.EntitySeagull;
import com.github.alexthe666.alexsmobs.misc.AMAdvancementTriggerRegistry;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;

public class SeagullAIStealFromPlayers extends Goal {

    private final EntitySeagull seagull;

    private Vec3 fleeVec = null;

    private Player target;

    private int fleeTime = 0;

    public SeagullAIStealFromPlayers(EntitySeagull entitySeagull) {
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.TARGET));
        this.seagull = entitySeagull;
    }

    @Override
    public boolean canUse() {
        long worldTime = this.seagull.m_9236_().getGameTime() % 10L;
        if ((this.seagull.m_21216_() < 100 || worldTime == 0L) && !this.seagull.isSitting() && AMConfig.seagullStealing) {
            if ((this.seagull.m_217043_().nextInt(12) == 0 || worldTime == 0L) && this.seagull.stealCooldown <= 0) {
                if (this.seagull.m_21205_().isEmpty()) {
                    Player valid = this.getClosestValidPlayer();
                    if (valid != null) {
                        this.target = valid;
                        return true;
                    }
                }
                return false;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public void start() {
        this.seagull.aiItemFlag = true;
    }

    @Override
    public void stop() {
        this.seagull.aiItemFlag = false;
        this.target = null;
        this.fleeVec = null;
        this.fleeTime = 0;
    }

    @Override
    public boolean canContinueToUse() {
        return this.target != null && !this.target.isCreative() && (this.seagull.m_21205_().isEmpty() || this.fleeTime > 0);
    }

    @Override
    public void tick() {
        this.seagull.setFlying(true);
        this.seagull.m_21566_().setWantedPosition(this.target.m_20185_(), this.target.m_20188_(), this.target.m_20189_(), 1.2F);
        if (this.seagull.m_20270_(this.target) < 2.0F && this.seagull.m_21205_().isEmpty()) {
            if (this.hasFoods(this.target)) {
                ItemStack foodStack = this.getFoodItemFrom(this.target);
                if (!foodStack.isEmpty()) {
                    ItemStack copy = foodStack.copy();
                    foodStack.shrink(1);
                    copy.setCount(1);
                    this.seagull.peck();
                    this.seagull.m_21008_(InteractionHand.MAIN_HAND, copy);
                    this.fleeTime = 60;
                    this.seagull.stealCooldown = 1500 + this.seagull.m_217043_().nextInt(1500);
                    if (this.target instanceof ServerPlayer) {
                        AMAdvancementTriggerRegistry.SEAGULL_STEAL.trigger((ServerPlayer) this.target);
                    }
                } else {
                    this.stop();
                }
            } else {
                this.stop();
            }
        }
        if (this.fleeTime > 0) {
            if (this.fleeVec == null) {
                this.fleeVec = this.seagull.getBlockInViewAway(this.target.m_20182_(), 4.0F);
            }
            if (this.fleeVec != null) {
                this.seagull.setFlying(true);
                this.seagull.m_21566_().setWantedPosition(this.fleeVec.x, this.fleeVec.y, this.fleeVec.z, 1.2F);
                if (this.seagull.m_20238_(this.fleeVec) < 5.0) {
                    this.fleeVec = this.seagull.getBlockInViewAway(this.fleeVec, 4.0F);
                }
            }
            this.fleeTime--;
        }
    }

    private Player getClosestValidPlayer() {
        List<Player> list = this.seagull.m_9236_().m_6443_(Player.class, this.seagull.m_20191_().inflate(10.0, 25.0, 10.0), EntitySelector.NO_CREATIVE_OR_SPECTATOR);
        Player closest = null;
        if (!list.isEmpty()) {
            for (Player player : list) {
                if ((closest == null || closest.m_20270_(this.seagull) > player.m_20270_(this.seagull)) && this.hasFoods(player)) {
                    closest = player;
                }
            }
        }
        return closest;
    }

    private boolean hasFoods(Player player) {
        for (int i = 0; i < 9; i++) {
            ItemStack stackIn = player.getInventory().items.get(i);
            if (stackIn.isEdible() && !this.isBlacklisted(stackIn)) {
                return true;
            }
        }
        return false;
    }

    private boolean isBlacklisted(ItemStack stack) {
        ResourceLocation loc = ForgeRegistries.ITEMS.getKey(stack.getItem());
        if (loc != null) {
            for (String str : AMConfig.seagullStealingBlacklist) {
                if (loc.toString().equals(str)) {
                    return true;
                }
            }
        }
        return false;
    }

    private ItemStack getFoodItemFrom(Player player) {
        List<ItemStack> foods = new ArrayList();
        for (int i = 0; i < 9; i++) {
            ItemStack stackIn = player.getInventory().items.get(i);
            if (stackIn.isEdible() && !this.isBlacklisted(stackIn)) {
                foods.add(stackIn);
            }
        }
        return !foods.isEmpty() ? (ItemStack) foods.get(foods.size() <= 1 ? 0 : this.seagull.m_217043_().nextInt(foods.size() - 1)) : ItemStack.EMPTY;
    }
}
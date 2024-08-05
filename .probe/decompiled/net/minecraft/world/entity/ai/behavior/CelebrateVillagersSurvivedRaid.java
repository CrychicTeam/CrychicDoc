package net.minecraft.world.entity.ai.behavior;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.entity.raid.Raid;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.FireworkRocketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class CelebrateVillagersSurvivedRaid extends Behavior<Villager> {

    @Nullable
    private Raid currentRaid;

    public CelebrateVillagersSurvivedRaid(int int0, int int1) {
        super(ImmutableMap.of(), int0, int1);
    }

    protected boolean checkExtraStartConditions(ServerLevel serverLevel0, Villager villager1) {
        BlockPos $$2 = villager1.m_20183_();
        this.currentRaid = serverLevel0.getRaidAt($$2);
        return this.currentRaid != null && this.currentRaid.isVictory() && MoveToSkySeeingSpot.hasNoBlocksAbove(serverLevel0, villager1, $$2);
    }

    protected boolean canStillUse(ServerLevel serverLevel0, Villager villager1, long long2) {
        return this.currentRaid != null && !this.currentRaid.isStopped();
    }

    protected void stop(ServerLevel serverLevel0, Villager villager1, long long2) {
        this.currentRaid = null;
        villager1.getBrain().updateActivityFromSchedule(serverLevel0.m_46468_(), serverLevel0.m_46467_());
    }

    protected void tick(ServerLevel serverLevel0, Villager villager1, long long2) {
        RandomSource $$3 = villager1.m_217043_();
        if ($$3.nextInt(100) == 0) {
            villager1.m_35310_();
        }
        if ($$3.nextInt(200) == 0 && MoveToSkySeeingSpot.hasNoBlocksAbove(serverLevel0, villager1, villager1.m_20183_())) {
            DyeColor $$4 = Util.getRandom(DyeColor.values(), $$3);
            int $$5 = $$3.nextInt(3);
            ItemStack $$6 = this.getFirework($$4, $$5);
            FireworkRocketEntity $$7 = new FireworkRocketEntity(villager1.m_9236_(), villager1, villager1.m_20185_(), villager1.m_20188_(), villager1.m_20189_(), $$6);
            villager1.m_9236_().m_7967_($$7);
        }
    }

    private ItemStack getFirework(DyeColor dyeColor0, int int1) {
        ItemStack $$2 = new ItemStack(Items.FIREWORK_ROCKET, 1);
        ItemStack $$3 = new ItemStack(Items.FIREWORK_STAR);
        CompoundTag $$4 = $$3.getOrCreateTagElement("Explosion");
        List<Integer> $$5 = Lists.newArrayList();
        $$5.add(dyeColor0.getFireworkColor());
        $$4.putIntArray("Colors", $$5);
        $$4.putByte("Type", (byte) FireworkRocketItem.Shape.BURST.getId());
        CompoundTag $$6 = $$2.getOrCreateTagElement("Fireworks");
        ListTag $$7 = new ListTag();
        CompoundTag $$8 = $$3.getTagElement("Explosion");
        if ($$8 != null) {
            $$7.add($$8);
        }
        $$6.putByte("Flight", (byte) int1);
        if (!$$7.isEmpty()) {
            $$6.put("Explosions", $$7);
        }
        return $$2;
    }
}
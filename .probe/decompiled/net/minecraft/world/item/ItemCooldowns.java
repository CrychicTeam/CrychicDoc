package net.minecraft.world.item;

import com.google.common.collect.Maps;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.util.Mth;

public class ItemCooldowns {

    private final Map<Item, ItemCooldowns.CooldownInstance> cooldowns = Maps.newHashMap();

    private int tickCount;

    public boolean isOnCooldown(Item item0) {
        return this.getCooldownPercent(item0, 0.0F) > 0.0F;
    }

    public float getCooldownPercent(Item item0, float float1) {
        ItemCooldowns.CooldownInstance $$2 = (ItemCooldowns.CooldownInstance) this.cooldowns.get(item0);
        if ($$2 != null) {
            float $$3 = (float) ($$2.endTime - $$2.startTime);
            float $$4 = (float) $$2.endTime - ((float) this.tickCount + float1);
            return Mth.clamp($$4 / $$3, 0.0F, 1.0F);
        } else {
            return 0.0F;
        }
    }

    public void tick() {
        this.tickCount++;
        if (!this.cooldowns.isEmpty()) {
            Iterator<Entry<Item, ItemCooldowns.CooldownInstance>> $$0 = this.cooldowns.entrySet().iterator();
            while ($$0.hasNext()) {
                Entry<Item, ItemCooldowns.CooldownInstance> $$1 = (Entry<Item, ItemCooldowns.CooldownInstance>) $$0.next();
                if (((ItemCooldowns.CooldownInstance) $$1.getValue()).endTime <= this.tickCount) {
                    $$0.remove();
                    this.onCooldownEnded((Item) $$1.getKey());
                }
            }
        }
    }

    public void addCooldown(Item item0, int int1) {
        this.cooldowns.put(item0, new ItemCooldowns.CooldownInstance(this.tickCount, this.tickCount + int1));
        this.onCooldownStarted(item0, int1);
    }

    public void removeCooldown(Item item0) {
        this.cooldowns.remove(item0);
        this.onCooldownEnded(item0);
    }

    protected void onCooldownStarted(Item item0, int int1) {
    }

    protected void onCooldownEnded(Item item0) {
    }

    static class CooldownInstance {

        final int startTime;

        final int endTime;

        CooldownInstance(int int0, int int1) {
            this.startTime = int0;
            this.endTime = int1;
        }
    }
}
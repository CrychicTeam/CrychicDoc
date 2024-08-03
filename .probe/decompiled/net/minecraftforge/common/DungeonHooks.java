package net.minecraftforge.common;

import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.util.random.WeightedRandom;
import net.minecraft.world.entity.EntityType;

public class DungeonHooks {

    private static ArrayList<DungeonHooks.DungeonMob> dungeonMobs = new ArrayList();

    public static float addDungeonMob(EntityType<?> type, int rarity) {
        if (rarity <= 0) {
            throw new IllegalArgumentException("Rarity must be greater then zero");
        } else {
            Iterator<DungeonHooks.DungeonMob> itr = dungeonMobs.iterator();
            while (itr.hasNext()) {
                DungeonHooks.DungeonMob mob = (DungeonHooks.DungeonMob) itr.next();
                if (type == mob.type) {
                    itr.remove();
                    rarity += mob.m_142631_().asInt();
                    break;
                }
            }
            dungeonMobs.add(new DungeonHooks.DungeonMob(rarity, type));
            return (float) rarity;
        }
    }

    public static int removeDungeonMob(EntityType<?> name) {
        for (DungeonHooks.DungeonMob mob : dungeonMobs) {
            if (name == mob.type) {
                dungeonMobs.remove(mob);
                return mob.m_142631_().asInt();
            }
        }
        return 0;
    }

    public static EntityType<?> getRandomDungeonMob(RandomSource rand) {
        DungeonHooks.DungeonMob mob = (DungeonHooks.DungeonMob) WeightedRandom.getRandomItem(rand, dungeonMobs).orElseThrow();
        return mob.type;
    }

    static {
        addDungeonMob(EntityType.SKELETON, 100);
        addDungeonMob(EntityType.ZOMBIE, 200);
        addDungeonMob(EntityType.SPIDER, 100);
    }

    public static class DungeonMob extends WeightedEntry.IntrusiveBase {

        public final EntityType<?> type;

        public DungeonMob(int weight, EntityType<?> type) {
            super(weight);
            this.type = type;
        }

        public boolean equals(Object target) {
            return target instanceof DungeonHooks.DungeonMob && this.type.equals(((DungeonHooks.DungeonMob) target).type);
        }
    }
}
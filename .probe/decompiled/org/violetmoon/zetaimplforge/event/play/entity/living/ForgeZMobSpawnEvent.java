package org.violetmoon.zetaimplforge.event.play.entity.living;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraftforge.event.entity.living.MobSpawnEvent;
import org.violetmoon.zeta.event.bus.ZResult;
import org.violetmoon.zeta.event.play.entity.living.ZMobSpawnEvent;
import org.violetmoon.zetaimplforge.ForgeZeta;

public class ForgeZMobSpawnEvent implements ZMobSpawnEvent {

    private final MobSpawnEvent e;

    public ForgeZMobSpawnEvent(MobSpawnEvent e) {
        this.e = e;
    }

    @Override
    public Mob getEntity() {
        return this.e.getEntity();
    }

    @Override
    public ServerLevelAccessor getLevel() {
        return this.e.getLevel();
    }

    @Override
    public double getX() {
        return this.e.getX();
    }

    @Override
    public double getY() {
        return this.e.getY();
    }

    @Override
    public double getZ() {
        return this.e.getZ();
    }

    @Override
    public ZResult getResult() {
        return ForgeZeta.from(this.e.getResult());
    }

    @Override
    public void setResult(ZResult value) {
        this.e.setResult(ForgeZeta.to(value));
    }

    public static class FinalizeSpawn extends ForgeZMobSpawnEvent implements ZMobSpawnEvent.CheckSpawn {

        private final MobSpawnEvent.FinalizeSpawn e;

        public FinalizeSpawn(MobSpawnEvent.FinalizeSpawn e) {
            super(e);
            this.e = e;
        }

        @Override
        public BaseSpawner getSpawner() {
            return this.e.getSpawner();
        }

        @Override
        public MobSpawnType getSpawnType() {
            return this.e.getSpawnType();
        }

        public static class Lowest extends ForgeZMobSpawnEvent.FinalizeSpawn implements ZMobSpawnEvent.CheckSpawn.Lowest {

            public Lowest(MobSpawnEvent.FinalizeSpawn e) {
                super(e);
            }
        }
    }
}
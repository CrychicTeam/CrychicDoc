package noppes.npcs.entity.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import noppes.npcs.EventHooks;
import noppes.npcs.api.CustomNPCsException;
import noppes.npcs.api.ITimers;
import noppes.npcs.controllers.IScriptBlockHandler;
import noppes.npcs.controllers.data.PlayerData;
import noppes.npcs.entity.EntityNPCInterface;

public class DataTimers implements ITimers {

    private Object parent;

    private Map<Integer, DataTimers.Timer> timers = new HashMap();

    public DataTimers(Object parent) {
        this.parent = parent;
    }

    @Override
    public void start(int id, int ticks, boolean repeat) {
        if (this.timers.containsKey(id)) {
            throw new CustomNPCsException("There is already a timer with id: " + id);
        } else {
            this.timers.put(id, new DataTimers.Timer(id, ticks, repeat));
        }
    }

    @Override
    public void forceStart(int id, int ticks, boolean repeat) {
        this.timers.put(id, new DataTimers.Timer(id, ticks, repeat));
    }

    @Override
    public boolean has(int id) {
        return this.timers.containsKey(id);
    }

    @Override
    public boolean stop(int id) {
        return this.timers.remove(id) != null;
    }

    @Override
    public void reset(int id) {
        DataTimers.Timer timer = (DataTimers.Timer) this.timers.get(id);
        if (timer == null) {
            throw new CustomNPCsException("There is no timer with id: " + id);
        } else {
            timer.ticks = 0;
        }
    }

    public void save(CompoundTag compound) {
        ListTag list = new ListTag();
        for (DataTimers.Timer timer : this.timers.values()) {
            CompoundTag c = new CompoundTag();
            c.putInt("ID", timer.id);
            c.putInt("TimerTicks", timer.id);
            c.putBoolean("Repeat", timer.repeat);
            c.putInt("Ticks", timer.ticks);
            list.add(c);
        }
        compound.put("NpcsTimers", list);
    }

    public void load(CompoundTag compound) {
        Map<Integer, DataTimers.Timer> timers = new HashMap();
        ListTag list = compound.getList("NpcsTimers", 10);
        for (int i = 0; i < list.size(); i++) {
            CompoundTag c = list.getCompound(i);
            DataTimers.Timer t = new DataTimers.Timer(c.getInt("ID"), c.getInt("TimerTicks"), c.getBoolean("Repeat"));
            t.ticks = c.getInt("Ticks");
            timers.put(t.id, t);
        }
        this.timers = timers;
    }

    public void update() {
        for (DataTimers.Timer timer : new ArrayList(this.timers.values())) {
            timer.update();
        }
    }

    @Override
    public void clear() {
        this.timers = new HashMap();
    }

    class Timer {

        public int id;

        private boolean repeat;

        private int timerTicks;

        private int ticks = 0;

        public Timer(int id, int ticks, boolean repeat) {
            this.id = id;
            this.repeat = repeat;
            this.timerTicks = ticks;
            this.ticks = ticks;
        }

        public void update() {
            if (this.ticks-- <= 0) {
                if (this.repeat) {
                    this.ticks = this.timerTicks;
                } else {
                    DataTimers.this.stop(this.id);
                }
                Object ob = DataTimers.this.parent;
                if (ob instanceof EntityNPCInterface) {
                    EventHooks.onNPCTimer((EntityNPCInterface) ob, this.id);
                } else if (ob instanceof PlayerData) {
                    EventHooks.onPlayerTimer((PlayerData) ob, this.id);
                } else {
                    EventHooks.onScriptBlockTimer((IScriptBlockHandler) ob, this.id);
                }
            }
        }
    }
}
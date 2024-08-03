package noppes.npcs.controllers.data;

import java.util.HashMap;
import java.util.Random;
import java.util.Map.Entry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;

public class Lines {

    private static final Random random = new Random();

    private int lastLine = -1;

    public HashMap<Integer, Line> lines = new HashMap();

    public CompoundTag save() {
        CompoundTag compound = new CompoundTag();
        ListTag nbttaglist = new ListTag();
        for (int slot : this.lines.keySet()) {
            Line line = (Line) this.lines.get(slot);
            CompoundTag nbttagcompound = new CompoundTag();
            nbttagcompound.putInt("Slot", slot);
            nbttagcompound.putString("Line", line.getText());
            nbttagcompound.putString("Song", line.getSound());
            nbttaglist.add(nbttagcompound);
        }
        compound.put("Lines", nbttaglist);
        return compound;
    }

    public void readNBT(CompoundTag compound) {
        ListTag nbttaglist = compound.getList("Lines", 10);
        HashMap<Integer, Line> map = new HashMap();
        for (int i = 0; i < nbttaglist.size(); i++) {
            CompoundTag nbttagcompound = nbttaglist.getCompound(i);
            Line line = new Line();
            line.setText(nbttagcompound.getString("Line"));
            line.setSound(nbttagcompound.getString("Song"));
            map.put(nbttagcompound.getInt("Slot"), line);
        }
        this.lines = map;
    }

    public Line getLine(boolean isRandom) {
        if (this.lines.isEmpty()) {
            return null;
        } else {
            if (isRandom) {
                int i = random.nextInt(this.lines.size());
                for (Entry<Integer, Line> e : this.lines.entrySet()) {
                    if (--i < 0) {
                        return ((Line) e.getValue()).copy();
                    }
                }
            }
            this.lastLine++;
            while (true) {
                this.lastLine %= 8;
                Line line = (Line) this.lines.get(this.lastLine);
                if (line != null) {
                    return line.copy();
                }
                this.lastLine++;
            }
        }
    }

    public boolean isEmpty() {
        return this.lines.isEmpty();
    }
}
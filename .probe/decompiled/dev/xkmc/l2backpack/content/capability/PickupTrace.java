package dev.xkmc.l2backpack.content.capability;

import java.util.HashSet;
import java.util.Stack;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

public class PickupTrace {

    private final HashSet<Integer> visited = new HashSet();

    private final HashSet<Integer> active = new HashSet();

    private final Stack<PickupTrace.Entry> layer = new Stack();

    public final boolean simulate;

    public final ServerLevel level;

    @Nullable
    public final ServerPlayer player;

    public PickupTrace(boolean simulate, ServerLevel level) {
        this.simulate = simulate;
        this.level = level;
        this.player = null;
    }

    public PickupTrace(boolean simulate, ServerPlayer player) {
        this.simulate = simulate;
        this.player = player;
        this.level = player.serverLevel();
    }

    public boolean push(int sig, PickupConfig mode) {
        if (this.visited.contains(sig)) {
            return false;
        } else {
            this.layer.push(new PickupTrace.Entry(sig, mode));
            this.visited.add(sig);
            this.active.add(sig);
            return true;
        }
    }

    public void pop() {
        PickupTrace.Entry ent = (PickupTrace.Entry) this.layer.pop();
        this.active.remove(ent.sig());
    }

    public PickupConfig getMode() {
        return ((PickupTrace.Entry) this.layer.peek()).mode();
    }

    private static record Entry(int sig, PickupConfig mode) {
    }
}
package net.minecraft.world.level.gameevent;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import net.minecraft.network.protocol.game.DebugPackets;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;

public class EuclideanGameEventListenerRegistry implements GameEventListenerRegistry {

    private final List<GameEventListener> listeners = Lists.newArrayList();

    private final Set<GameEventListener> listenersToRemove = Sets.newHashSet();

    private final List<GameEventListener> listenersToAdd = Lists.newArrayList();

    private boolean processing;

    private final ServerLevel level;

    private final int sectionY;

    private final EuclideanGameEventListenerRegistry.OnEmptyAction onEmptyAction;

    public EuclideanGameEventListenerRegistry(ServerLevel serverLevel0, int int1, EuclideanGameEventListenerRegistry.OnEmptyAction euclideanGameEventListenerRegistryOnEmptyAction2) {
        this.level = serverLevel0;
        this.sectionY = int1;
        this.onEmptyAction = euclideanGameEventListenerRegistryOnEmptyAction2;
    }

    @Override
    public boolean isEmpty() {
        return this.listeners.isEmpty();
    }

    @Override
    public void register(GameEventListener gameEventListener0) {
        if (this.processing) {
            this.listenersToAdd.add(gameEventListener0);
        } else {
            this.listeners.add(gameEventListener0);
        }
        DebugPackets.sendGameEventListenerInfo(this.level, gameEventListener0);
    }

    @Override
    public void unregister(GameEventListener gameEventListener0) {
        if (this.processing) {
            this.listenersToRemove.add(gameEventListener0);
        } else {
            this.listeners.remove(gameEventListener0);
        }
        if (this.listeners.isEmpty()) {
            this.onEmptyAction.apply(this.sectionY);
        }
    }

    @Override
    public boolean visitInRangeListeners(GameEvent gameEvent0, Vec3 vec1, GameEvent.Context gameEventContext2, GameEventListenerRegistry.ListenerVisitor gameEventListenerRegistryListenerVisitor3) {
        this.processing = true;
        boolean $$4 = false;
        try {
            Iterator<GameEventListener> $$5 = this.listeners.iterator();
            while ($$5.hasNext()) {
                GameEventListener $$6 = (GameEventListener) $$5.next();
                if (this.listenersToRemove.remove($$6)) {
                    $$5.remove();
                } else {
                    Optional<Vec3> $$7 = getPostableListenerPosition(this.level, vec1, $$6);
                    if ($$7.isPresent()) {
                        gameEventListenerRegistryListenerVisitor3.visit($$6, (Vec3) $$7.get());
                        $$4 = true;
                    }
                }
            }
        } finally {
            this.processing = false;
        }
        if (!this.listenersToAdd.isEmpty()) {
            this.listeners.addAll(this.listenersToAdd);
            this.listenersToAdd.clear();
        }
        if (!this.listenersToRemove.isEmpty()) {
            this.listeners.removeAll(this.listenersToRemove);
            this.listenersToRemove.clear();
        }
        return $$4;
    }

    private static Optional<Vec3> getPostableListenerPosition(ServerLevel serverLevel0, Vec3 vec1, GameEventListener gameEventListener2) {
        Optional<Vec3> $$3 = gameEventListener2.getListenerSource().getPosition(serverLevel0);
        if ($$3.isEmpty()) {
            return Optional.empty();
        } else {
            double $$4 = ((Vec3) $$3.get()).distanceToSqr(vec1);
            int $$5 = gameEventListener2.getListenerRadius() * gameEventListener2.getListenerRadius();
            return $$4 > (double) $$5 ? Optional.empty() : $$3;
        }
    }

    @FunctionalInterface
    public interface OnEmptyAction {

        void apply(int var1);
    }
}
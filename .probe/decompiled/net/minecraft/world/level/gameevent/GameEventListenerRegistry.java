package net.minecraft.world.level.gameevent;

import net.minecraft.world.phys.Vec3;

public interface GameEventListenerRegistry {

    GameEventListenerRegistry NOOP = new GameEventListenerRegistry() {

        @Override
        public boolean isEmpty() {
            return true;
        }

        @Override
        public void register(GameEventListener p_251092_) {
        }

        @Override
        public void unregister(GameEventListener p_251937_) {
        }

        @Override
        public boolean visitInRangeListeners(GameEvent p_251260_, Vec3 p_249086_, GameEvent.Context p_249012_, GameEventListenerRegistry.ListenerVisitor p_252106_) {
            return false;
        }
    };

    boolean isEmpty();

    void register(GameEventListener var1);

    void unregister(GameEventListener var1);

    boolean visitInRangeListeners(GameEvent var1, Vec3 var2, GameEvent.Context var3, GameEventListenerRegistry.ListenerVisitor var4);

    @FunctionalInterface
    public interface ListenerVisitor {

        void visit(GameEventListener var1, Vec3 var2);
    }
}
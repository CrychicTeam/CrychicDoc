package net.minecraft.world.level.gameevent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.network.protocol.game.DebugPackets;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.phys.Vec3;

public class GameEventDispatcher {

    private final ServerLevel level;

    public GameEventDispatcher(ServerLevel serverLevel0) {
        this.level = serverLevel0;
    }

    public void post(GameEvent gameEvent0, Vec3 vec1, GameEvent.Context gameEventContext2) {
        int $$3 = gameEvent0.getNotificationRadius();
        BlockPos $$4 = BlockPos.containing(vec1);
        int $$5 = SectionPos.blockToSectionCoord($$4.m_123341_() - $$3);
        int $$6 = SectionPos.blockToSectionCoord($$4.m_123342_() - $$3);
        int $$7 = SectionPos.blockToSectionCoord($$4.m_123343_() - $$3);
        int $$8 = SectionPos.blockToSectionCoord($$4.m_123341_() + $$3);
        int $$9 = SectionPos.blockToSectionCoord($$4.m_123342_() + $$3);
        int $$10 = SectionPos.blockToSectionCoord($$4.m_123343_() + $$3);
        List<GameEvent.ListenerInfo> $$11 = new ArrayList();
        GameEventListenerRegistry.ListenerVisitor $$12 = (p_251272_, p_248685_) -> {
            if (p_251272_.getDeliveryMode() == GameEventListener.DeliveryMode.BY_DISTANCE) {
                $$11.add(new GameEvent.ListenerInfo(gameEvent0, vec1, gameEventContext2, p_251272_, p_248685_));
            } else {
                p_251272_.handleGameEvent(this.level, gameEvent0, gameEventContext2, vec1);
            }
        };
        boolean $$13 = false;
        for (int $$14 = $$5; $$14 <= $$8; $$14++) {
            for (int $$15 = $$7; $$15 <= $$10; $$15++) {
                ChunkAccess $$16 = this.level.getChunkSource().getChunkNow($$14, $$15);
                if ($$16 != null) {
                    for (int $$17 = $$6; $$17 <= $$9; $$17++) {
                        $$13 |= $$16.getListenerRegistry($$17).visitInRangeListeners(gameEvent0, vec1, gameEventContext2, $$12);
                    }
                }
            }
        }
        if (!$$11.isEmpty()) {
            this.handleGameEventMessagesInQueue($$11);
        }
        if ($$13) {
            DebugPackets.sendGameEventInfo(this.level, gameEvent0, vec1);
        }
    }

    private void handleGameEventMessagesInQueue(List<GameEvent.ListenerInfo> listGameEventListenerInfo0) {
        Collections.sort(listGameEventListenerInfo0);
        for (GameEvent.ListenerInfo $$1 : listGameEventListenerInfo0) {
            GameEventListener $$2 = $$1.recipient();
            $$2.handleGameEvent(this.level, $$1.gameEvent(), $$1.context(), $$1.source());
        }
    }
}
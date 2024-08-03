package noppes.npcs.controllers;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import noppes.npcs.controllers.data.BlockData;
import noppes.npcs.entity.EntityNPCInterface;

public class MassBlockController {

    private static Queue<MassBlockController.IMassBlock> queue;

    private static MassBlockController Instance;

    public MassBlockController() {
        queue = new LinkedList();
        Instance = this;
    }

    public static void Update() {
        if (!queue.isEmpty()) {
            MassBlockController.IMassBlock imb = (MassBlockController.IMassBlock) queue.remove();
            Level level = imb.getNpc().m_9236_();
            BlockPos pos = imb.getNpc().m_20183_();
            int range = imb.getRange();
            List<BlockData> list = new ArrayList();
            for (int x = -range; x < range; x++) {
                for (int z = -range; z < range; z++) {
                    if (level.isLoaded(new BlockPos(x + pos.m_123341_(), 64, z + pos.m_123343_()))) {
                        for (int y = 0; y < range; y++) {
                            BlockPos blockPos = pos.offset(x, y - range / 2, z);
                            list.add(new BlockData(blockPos, level.getBlockState(blockPos), null));
                        }
                    }
                }
            }
            imb.processed(list);
        }
    }

    public static void Queue(MassBlockController.IMassBlock imb) {
        queue.add(imb);
    }

    public interface IMassBlock {

        EntityNPCInterface getNpc();

        int getRange();

        void processed(List<BlockData> var1);
    }
}
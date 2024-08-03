package journeymap.client.task.main;

import journeymap.client.JourneymapClient;
import journeymap.client.cartography.color.ColorManager;
import journeymap.client.data.DataCache;
import journeymap.client.log.ChatLog;
import journeymap.client.mod.ModBlockDelegate;
import journeymap.client.task.multi.MapPlayerTask;
import net.minecraft.client.Minecraft;

public class EnsureCurrentColorsTask implements IMainThreadTask {

    final boolean forceReset;

    final boolean announce;

    public EnsureCurrentColorsTask() {
        this(false, false);
    }

    public EnsureCurrentColorsTask(boolean forceReset, boolean announce) {
        this.forceReset = forceReset;
        this.announce = announce;
        if (announce) {
            ChatLog.announceI18N("jm.common.colorreset_start");
        }
    }

    @Override
    public IMainThreadTask perform(Minecraft mc, JourneymapClient jm) {
        if (this.forceReset) {
            DataCache.INSTANCE.resetBlockMetadata();
            ModBlockDelegate.INSTANCE.reset();
            ColorManager.INSTANCE.reset();
        }
        ColorManager.INSTANCE.ensureCurrent(this.forceReset);
        if (this.announce) {
            ChatLog.announceI18N("jm.common.colorreset_complete");
        }
        if (this.forceReset) {
            MapPlayerTask.forceNearbyRemap();
        }
        return null;
    }

    @Override
    public String getName() {
        return "EnsureCurrentColorsTask";
    }
}
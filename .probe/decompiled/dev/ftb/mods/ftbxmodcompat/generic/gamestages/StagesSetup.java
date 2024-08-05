package dev.ftb.mods.ftbxmodcompat.generic.gamestages;

import dev.ftb.mods.ftblibrary.integration.stages.StageHelper;
import dev.ftb.mods.ftbxmodcompat.FTBXModCompat;
import dev.ftb.mods.ftbxmodcompat.config.FTBXModConfig;

public class StagesSetup {

    public static void init() {
        FTBXModConfig.StageSelector sel = FTBXModConfig.STAGE_SELECTOR.get();
        if (sel != FTBXModConfig.StageSelector.DEFAULT && !sel.isUsable()) {
            FTBXModCompat.LOGGER.error("Stages implementation {} isn't available, falling back to default", sel);
            sel = FTBXModConfig.StageSelector.DEFAULT;
        }
        switch(sel) {
            case KUBEJS:
                StageHelper.getInstance().setProviderImpl(new KubeJSStageProvider());
                break;
            case GAMESTAGES:
                StageHelper.getInstance().setProviderImpl(new GameStagesStageProvider());
            case VANILLA:
            default:
                break;
            case DEFAULT:
                if (FTBXModCompat.isKubeJSLoaded) {
                    StageHelper.getInstance().setProviderImpl(new KubeJSStageProvider());
                } else if (FTBXModCompat.isGameStagesLoaded) {
                    StageHelper.getInstance().setProviderImpl(new GameStagesStageProvider());
                }
        }
        FTBXModCompat.LOGGER.info("Chose [{}] as the active game stages implementation", StageHelper.getInstance().getProvider().getName());
    }
}
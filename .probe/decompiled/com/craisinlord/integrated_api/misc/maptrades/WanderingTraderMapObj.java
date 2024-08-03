package com.craisinlord.integrated_api.misc.maptrades;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WanderingTraderMapObj {

    @Expose
    public final WanderingTraderMapObj.TRADE_TYPE tradeType;

    @Expose
    public final String structure;

    @Expose
    public final String mapIcon;

    @Expose
    public final String mapName;

    @Expose
    public final int emeraldsRequired;

    @Expose
    public final int tradesAllowed;

    @Expose
    public final int xpReward;

    @Expose
    public final int spawnRegionSearchRadius;

    public WanderingTraderMapObj(WanderingTraderMapObj.TRADE_TYPE tradeType, String structure, String mapIcon, String mapName, int emeraldsRequired, int tradesAllowed, int xpReward, int spawnRegionSearchRadius) {
        this.tradeType = tradeType;
        this.structure = structure;
        this.mapIcon = mapIcon;
        this.mapName = mapName;
        this.emeraldsRequired = emeraldsRequired;
        this.tradesAllowed = tradesAllowed;
        this.xpReward = xpReward;
        this.spawnRegionSearchRadius = spawnRegionSearchRadius;
    }

    public static enum TRADE_TYPE {

        @SerializedName("rare")
        RARE, @SerializedName("common")
        COMMON
    }
}
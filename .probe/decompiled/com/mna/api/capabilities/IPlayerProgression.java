package com.mna.api.capabilities;

import com.mna.api.faction.FactionDifficultyStats;
import com.mna.api.faction.IFaction;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public interface IPlayerProgression {

    String[] Tier_Health_Boost_IDs = new String[] { "e13eed85-2800-4e0f-adfe-888cc0bebe6f", "056cb5aa-0992-44fb-8dcc-432ecc160a84", "17fac1ea-b63c-424d-b30f-9721d84eacc7", "befe52c5-fd98-48fa-aaa9-8bfcb91a77ca", "71090d1d-f63a-4f39-aa69-a05599f9b0f3" };

    int getTier();

    default void setTier(int tier, @Nullable Player player) {
        this.setTier(tier, player, true);
    }

    void setTier(int var1, @Nullable Player var2, boolean var3);

    void addTierProgressionComplete(ResourceLocation var1);

    void setTierProgression(List<ResourceLocation> var1);

    float getTierProgress(Level var1);

    List<ResourceLocation> getCompletedProgressionSteps();

    int getTierMaxComplexity();

    boolean hasAlliedFaction();

    @Nullable
    IFaction getAlliedFaction();

    void setAlliedFaction(IFaction var1, @Nullable Player var2);

    FactionDifficultyStats getFactionDifficultyStats(IFaction var1);

    int getFactionStanding();

    void increaseFactionStanding(int var1);

    void setFactionStanding(int var1);

    boolean needsSync();

    void clearSyncFlag();

    void setDirty();

    boolean canBeRaided(Player var1);

    boolean canBeRaided(IFaction var1, Player var2);

    int getRelativeRaidStrength(IFaction var1, Player var2);

    double getRaidChance(IFaction var1);

    void setRaidChance(IFaction var1, double var2);

    void raidImmediate(IFaction var1);

    boolean hasForceRaid();

    void clearForceRaid();

    @Nullable
    IFaction getForceRaid();

    void incrementFactionAggro(IFaction var1, float var2, float var3);

    @Nullable
    CodexBreadcrumb popCodexBreadcrumb();

    @Nullable
    CodexBreadcrumb peekCodexBreadcrumb();

    void pushCodexBreadcrumb(CodexBreadcrumb.Type var1, String var2, int var3, String... var4);

    void clearCodexEntryHistory();

    int breadcrumbLength();

    void tickClassicRaids(Player var1);

    int getAllyCooldown();

    void summonRandomAlly(Player var1);

    void setAllyCooldown(int var1);
}
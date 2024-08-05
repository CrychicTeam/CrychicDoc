package com.mojang.realmsclient.gui;

import com.mojang.realmsclient.client.RealmsClient;
import com.mojang.realmsclient.dto.RealmsNews;
import com.mojang.realmsclient.dto.RealmsNotification;
import com.mojang.realmsclient.dto.RealmsServer;
import com.mojang.realmsclient.dto.RealmsServerPlayerLists;
import com.mojang.realmsclient.gui.task.DataFetcher;
import com.mojang.realmsclient.gui.task.RepeatedDelayStrategy;
import com.mojang.realmsclient.util.RealmsPersistence;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;
import net.minecraft.Util;

public class RealmsDataFetcher {

    public final DataFetcher dataFetcher = new DataFetcher(Util.ioPool(), TimeUnit.MILLISECONDS, Util.timeSource);

    public final DataFetcher.Task<List<RealmsNotification>> notificationsTask;

    public final DataFetcher.Task<List<RealmsServer>> serverListUpdateTask;

    public final DataFetcher.Task<RealmsServerPlayerLists> liveStatsTask;

    public final DataFetcher.Task<Integer> pendingInvitesTask;

    public final DataFetcher.Task<Boolean> trialAvailabilityTask;

    public final DataFetcher.Task<RealmsNews> newsTask;

    public final RealmsNewsManager newsManager = new RealmsNewsManager(new RealmsPersistence());

    public RealmsDataFetcher(RealmsClient realmsClient0) {
        this.serverListUpdateTask = this.dataFetcher.createTask("server list", () -> realmsClient0.listWorlds().servers, Duration.ofSeconds(60L), RepeatedDelayStrategy.CONSTANT);
        this.liveStatsTask = this.dataFetcher.createTask("live stats", realmsClient0::m_87241_, Duration.ofSeconds(10L), RepeatedDelayStrategy.CONSTANT);
        this.pendingInvitesTask = this.dataFetcher.createTask("pending invite count", realmsClient0::m_87260_, Duration.ofSeconds(10L), RepeatedDelayStrategy.exponentialBackoff(360));
        this.trialAvailabilityTask = this.dataFetcher.createTask("trial availablity", realmsClient0::m_87264_, Duration.ofSeconds(60L), RepeatedDelayStrategy.exponentialBackoff(60));
        this.newsTask = this.dataFetcher.createTask("unread news", realmsClient0::m_87263_, Duration.ofMinutes(5L), RepeatedDelayStrategy.CONSTANT);
        this.notificationsTask = this.dataFetcher.createTask("notifications", realmsClient0::m_274314_, Duration.ofMinutes(5L), RepeatedDelayStrategy.CONSTANT);
    }
}
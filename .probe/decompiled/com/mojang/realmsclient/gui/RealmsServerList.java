package com.mojang.realmsclient.gui;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mojang.realmsclient.dto.RealmsServer;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import net.minecraft.client.Minecraft;

public class RealmsServerList {

    private final Minecraft minecraft;

    private final Set<RealmsServer> removedServers = Sets.newHashSet();

    private List<RealmsServer> servers = Lists.newArrayList();

    public RealmsServerList(Minecraft minecraft0) {
        this.minecraft = minecraft0;
    }

    public List<RealmsServer> updateServersList(List<RealmsServer> listRealmsServer0) {
        List<RealmsServer> $$1 = new ArrayList(listRealmsServer0);
        $$1.sort(new RealmsServer.McoServerComparator(this.minecraft.getUser().getName()));
        boolean $$2 = $$1.removeAll(this.removedServers);
        if (!$$2) {
            this.removedServers.clear();
        }
        this.servers = $$1;
        return List.copyOf(this.servers);
    }

    public synchronized List<RealmsServer> removeItem(RealmsServer realmsServer0) {
        this.servers.remove(realmsServer0);
        this.removedServers.add(realmsServer0);
        return List.copyOf(this.servers);
    }
}
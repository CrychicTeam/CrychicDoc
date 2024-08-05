package net.minecraft.client.multiplayer;

import com.google.common.collect.Maps;
import com.mojang.logging.LogUtils;
import java.util.Map;
import java.util.Map.Entry;
import javax.annotation.Nullable;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementList;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.toasts.AdvancementToast;
import net.minecraft.client.telemetry.WorldSessionTelemetryManager;
import net.minecraft.network.protocol.game.ClientboundUpdateAdvancementsPacket;
import net.minecraft.network.protocol.game.ServerboundSeenAdvancementsPacket;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;

public class ClientAdvancements {

    private static final Logger LOGGER = LogUtils.getLogger();

    private final Minecraft minecraft;

    private final WorldSessionTelemetryManager telemetryManager;

    private final AdvancementList advancements = new AdvancementList();

    private final Map<Advancement, AdvancementProgress> progress = Maps.newHashMap();

    @Nullable
    private ClientAdvancements.Listener listener;

    @Nullable
    private Advancement selectedTab;

    public ClientAdvancements(Minecraft minecraft0, WorldSessionTelemetryManager worldSessionTelemetryManager1) {
        this.minecraft = minecraft0;
        this.telemetryManager = worldSessionTelemetryManager1;
    }

    public void update(ClientboundUpdateAdvancementsPacket clientboundUpdateAdvancementsPacket0) {
        if (clientboundUpdateAdvancementsPacket0.shouldReset()) {
            this.advancements.clear();
            this.progress.clear();
        }
        this.advancements.remove(clientboundUpdateAdvancementsPacket0.getRemoved());
        this.advancements.add(clientboundUpdateAdvancementsPacket0.getAdded());
        for (Entry<ResourceLocation, AdvancementProgress> $$1 : clientboundUpdateAdvancementsPacket0.getProgress().entrySet()) {
            Advancement $$2 = this.advancements.get((ResourceLocation) $$1.getKey());
            if ($$2 != null) {
                AdvancementProgress $$3 = (AdvancementProgress) $$1.getValue();
                $$3.update($$2.getCriteria(), $$2.getRequirements());
                this.progress.put($$2, $$3);
                if (this.listener != null) {
                    this.listener.onUpdateAdvancementProgress($$2, $$3);
                }
                if (!clientboundUpdateAdvancementsPacket0.shouldReset() && $$3.isDone()) {
                    if (this.minecraft.level != null) {
                        this.telemetryManager.onAdvancementDone(this.minecraft.level, $$2);
                    }
                    if ($$2.getDisplay() != null && $$2.getDisplay().shouldShowToast()) {
                        this.minecraft.getToasts().addToast(new AdvancementToast($$2));
                    }
                }
            } else {
                LOGGER.warn("Server informed client about progress for unknown advancement {}", $$1.getKey());
            }
        }
    }

    public AdvancementList getAdvancements() {
        return this.advancements;
    }

    public void setSelectedTab(@Nullable Advancement advancement0, boolean boolean1) {
        ClientPacketListener $$2 = this.minecraft.getConnection();
        if ($$2 != null && advancement0 != null && boolean1) {
            $$2.send(ServerboundSeenAdvancementsPacket.openedTab(advancement0));
        }
        if (this.selectedTab != advancement0) {
            this.selectedTab = advancement0;
            if (this.listener != null) {
                this.listener.onSelectedTabChanged(advancement0);
            }
        }
    }

    public void setListener(@Nullable ClientAdvancements.Listener clientAdvancementsListener0) {
        this.listener = clientAdvancementsListener0;
        this.advancements.setListener(clientAdvancementsListener0);
        if (clientAdvancementsListener0 != null) {
            for (Entry<Advancement, AdvancementProgress> $$1 : this.progress.entrySet()) {
                clientAdvancementsListener0.onUpdateAdvancementProgress((Advancement) $$1.getKey(), (AdvancementProgress) $$1.getValue());
            }
            clientAdvancementsListener0.onSelectedTabChanged(this.selectedTab);
        }
    }

    public interface Listener extends AdvancementList.Listener {

        void onUpdateAdvancementProgress(Advancement var1, AdvancementProgress var2);

        void onSelectedTabChanged(@Nullable Advancement var1);
    }
}
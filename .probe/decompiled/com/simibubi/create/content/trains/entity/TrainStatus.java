package com.simibubi.create.content.trains.entity;

import com.simibubi.create.foundation.utility.Components;
import com.simibubi.create.foundation.utility.Lang;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class TrainStatus {

    Train train;

    boolean navigation;

    boolean track;

    boolean conductor;

    List<Component> queued = new ArrayList();

    public TrainStatus(Train train) {
        this.train = train;
    }

    public void failedNavigation() {
        if (!this.navigation) {
            this.displayInformation("no_path", false);
            this.navigation = true;
        }
    }

    public void failedNavigationNoTarget(String filter) {
        if (!this.navigation) {
            this.displayInformation("no_match", false, filter);
            this.navigation = true;
        }
    }

    public void successfulNavigation() {
        if (this.navigation) {
            this.displayInformation("navigation_success", true);
            this.navigation = false;
        }
    }

    public void foundConductor() {
        if (this.conductor) {
            this.displayInformation("found_driver", true);
            this.conductor = false;
        }
    }

    public void missingConductor() {
        if (!this.conductor) {
            this.displayInformation("missing_driver", false);
            this.conductor = true;
        }
    }

    public void missingCorrectConductor() {
        if (!this.conductor) {
            this.displayInformation("opposite_driver", false);
            this.conductor = true;
        }
    }

    public void manualControls() {
        this.displayInformation("paused_for_manual", true);
    }

    public void failedMigration() {
        if (!this.track) {
            this.displayInformation("track_missing", false);
            this.track = true;
        }
    }

    public void highStress() {
        if (!this.track) {
            this.displayInformation("coupling_stress", false);
            this.track = true;
        }
    }

    public void doublePortal() {
        if (!this.track) {
            this.displayInformation("double_portal", false);
            this.track = true;
        }
    }

    public void endOfTrack() {
        if (!this.track) {
            this.displayInformation("end_of_track", false);
            this.track = true;
        }
    }

    public void crash() {
        this.displayInformation("collision", false);
    }

    public void successfulMigration() {
        if (this.track) {
            this.displayInformation("back_on_track", true);
            this.track = false;
        }
    }

    public void trackOK() {
        this.track = false;
    }

    public void tick(Level level) {
        if (!this.queued.isEmpty()) {
            LivingEntity owner = this.train.getOwner(level);
            if (owner != null) {
                if (owner instanceof Player player) {
                    player.displayClientMessage(Lang.translateDirect("train.status", this.train.name).withStyle(ChatFormatting.GOLD), false);
                    this.queued.forEach(c -> player.displayClientMessage(c, false));
                }
                this.queued.clear();
            }
        }
    }

    public void displayInformation(String key, boolean itsAGoodThing, Object... args) {
        this.queued.add(Components.literal(" - ").withStyle(ChatFormatting.GRAY).append(Lang.translateDirect("train.status." + key, args).withStyle(st -> st.withColor(itsAGoodThing ? 14019778 : 16765876))));
        if (this.queued.size() > 3) {
            this.queued.remove(0);
        }
    }

    public void newSchedule() {
        this.navigation = false;
        this.conductor = false;
    }
}
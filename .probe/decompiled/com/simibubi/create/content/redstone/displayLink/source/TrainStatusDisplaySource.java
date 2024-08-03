package com.simibubi.create.content.redstone.displayLink.source;

import com.simibubi.create.content.redstone.displayLink.DisplayLinkContext;
import com.simibubi.create.content.redstone.displayLink.target.DisplayTargetStats;
import com.simibubi.create.content.trains.entity.Train;
import com.simibubi.create.content.trains.schedule.Schedule;
import com.simibubi.create.content.trains.schedule.ScheduleRuntime;
import com.simibubi.create.content.trains.station.GlobalStation;
import com.simibubi.create.content.trains.station.StationBlockEntity;
import net.minecraft.network.chat.MutableComponent;

public class TrainStatusDisplaySource extends SingleLineDisplaySource {

    @Override
    protected MutableComponent provideLine(DisplayLinkContext context, DisplayTargetStats stats) {
        if (context.getSourceBlockEntity() instanceof StationBlockEntity observerBE) {
            GlobalStation observer = observerBE.getStation();
            if (observer == null) {
                return EMPTY_LINE;
            } else {
                Train currentTrain = observer.getPresentTrain();
                if (currentTrain == null) {
                    return EMPTY_LINE;
                } else {
                    ScheduleRuntime runtime = currentTrain.runtime;
                    Schedule schedule = runtime.getSchedule();
                    if (schedule == null) {
                        return EMPTY_LINE;
                    } else if (runtime.paused) {
                        return EMPTY_LINE;
                    } else if (runtime.state != ScheduleRuntime.State.POST_TRANSIT) {
                        return EMPTY_LINE;
                    } else {
                        return runtime.currentEntry == schedule.entries.size() - 1 && !schedule.cyclic ? EMPTY_LINE : runtime.getWaitingStatus(context.level());
                    }
                }
            }
        } else {
            return EMPTY_LINE;
        }
    }

    @Override
    protected boolean allowsLabeling(DisplayLinkContext context) {
        return false;
    }

    @Override
    protected String getTranslationKey() {
        return "train_status";
    }
}
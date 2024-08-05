package com.simibubi.create.content.redstone.displayLink.source;

import com.simibubi.create.Create;
import com.simibubi.create.content.redstone.displayLink.DisplayLinkContext;
import com.simibubi.create.content.redstone.displayLink.target.DisplayTargetStats;
import com.simibubi.create.content.trains.entity.Train;
import com.simibubi.create.content.trains.observer.TrackObserver;
import com.simibubi.create.content.trains.observer.TrackObserverBlockEntity;
import java.util.UUID;
import net.minecraft.network.chat.MutableComponent;

public class ObservedTrainNameSource extends SingleLineDisplaySource {

    @Override
    protected MutableComponent provideLine(DisplayLinkContext context, DisplayTargetStats stats) {
        if (context.getSourceBlockEntity() instanceof TrackObserverBlockEntity observerBE) {
            TrackObserver observer = observerBE.getObserver();
            if (observer == null) {
                return EMPTY_LINE;
            } else {
                UUID currentTrain = observer.getCurrentTrain();
                if (currentTrain == null) {
                    return EMPTY_LINE;
                } else {
                    Train train = (Train) Create.RAILWAYS.trains.get(currentTrain);
                    return train == null ? EMPTY_LINE : train.name.copy();
                }
            }
        } else {
            return EMPTY_LINE;
        }
    }

    @Override
    public int getPassiveRefreshTicks() {
        return 400;
    }

    @Override
    protected String getTranslationKey() {
        return "observed_train_name";
    }

    @Override
    protected boolean allowsLabeling(DisplayLinkContext context) {
        return true;
    }
}
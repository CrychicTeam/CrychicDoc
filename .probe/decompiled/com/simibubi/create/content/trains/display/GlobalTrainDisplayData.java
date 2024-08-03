package com.simibubi.create.content.trains.display;

import com.simibubi.create.Create;
import com.simibubi.create.content.trains.entity.Train;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.network.chat.MutableComponent;

public class GlobalTrainDisplayData {

    public static final Map<String, Collection<GlobalTrainDisplayData.TrainDeparturePrediction>> statusByDestination = new HashMap();

    public static boolean updateTick = false;

    public static void refresh() {
        statusByDestination.clear();
        for (Train train : Create.RAILWAYS.trains.values()) {
            if (!train.runtime.paused && train.runtime.getSchedule() != null && !train.derailed && train.graph != null) {
                for (GlobalTrainDisplayData.TrainDeparturePrediction prediction : train.runtime.submitPredictions()) {
                    ((Collection) statusByDestination.computeIfAbsent(prediction.destination, $ -> new ArrayList())).add(prediction);
                }
            }
        }
    }

    public static List<GlobalTrainDisplayData.TrainDeparturePrediction> prepare(String filter, int maxLines) {
        String regex = filter.isBlank() ? filter : "\\Q" + filter.replace("*", "\\E.*\\Q") + "\\E";
        return statusByDestination.entrySet().stream().filter(e -> ((String) e.getKey()).matches(regex)).flatMap(e -> ((Collection) e.getValue()).stream()).sorted().limit((long) maxLines).toList();
    }

    public static class TrainDeparturePrediction implements Comparable<GlobalTrainDisplayData.TrainDeparturePrediction> {

        public Train train;

        public int ticks;

        public MutableComponent scheduleTitle;

        public String destination;

        public TrainDeparturePrediction(Train train, int ticks, MutableComponent scheduleTitle, String destination) {
            this.scheduleTitle = scheduleTitle;
            this.destination = destination;
            this.train = train;
            this.ticks = ticks;
        }

        private int getCompareTicks() {
            if (this.ticks == -1) {
                return Integer.MAX_VALUE;
            } else {
                return this.ticks < 200 ? 0 : this.ticks;
            }
        }

        public int compareTo(GlobalTrainDisplayData.TrainDeparturePrediction o) {
            int compare = Integer.compare(this.getCompareTicks(), o.getCompareTicks());
            return compare == 0 ? this.train.name.getString().compareTo(o.train.name.getString()) : compare;
        }
    }
}
package net.minecraft.world.level.gameevent.vibrations;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import org.apache.commons.lang3.tuple.Pair;

public class VibrationSelector {

    public static final Codec<VibrationSelector> CODEC = RecordCodecBuilder.create(p_249445_ -> p_249445_.group(VibrationInfo.CODEC.optionalFieldOf("event").forGetter(p_251862_ -> p_251862_.currentVibrationData.map(Pair::getLeft)), Codec.LONG.fieldOf("tick").forGetter(p_251458_ -> (Long) p_251458_.currentVibrationData.map(Pair::getRight).orElse(-1L))).apply(p_249445_, VibrationSelector::new));

    private Optional<Pair<VibrationInfo, Long>> currentVibrationData;

    public VibrationSelector(Optional<VibrationInfo> optionalVibrationInfo0, long long1) {
        this.currentVibrationData = optionalVibrationInfo0.map(p_251571_ -> Pair.of(p_251571_, long1));
    }

    public VibrationSelector() {
        this.currentVibrationData = Optional.empty();
    }

    public void addCandidate(VibrationInfo vibrationInfo0, long long1) {
        if (this.shouldReplaceVibration(vibrationInfo0, long1)) {
            this.currentVibrationData = Optional.of(Pair.of(vibrationInfo0, long1));
        }
    }

    private boolean shouldReplaceVibration(VibrationInfo vibrationInfo0, long long1) {
        if (this.currentVibrationData.isEmpty()) {
            return true;
        } else {
            Pair<VibrationInfo, Long> $$2 = (Pair<VibrationInfo, Long>) this.currentVibrationData.get();
            long $$3 = (Long) $$2.getRight();
            if (long1 != $$3) {
                return false;
            } else {
                VibrationInfo $$4 = (VibrationInfo) $$2.getLeft();
                if (vibrationInfo0.distance() < $$4.distance()) {
                    return true;
                } else {
                    return vibrationInfo0.distance() > $$4.distance() ? false : VibrationSystem.getGameEventFrequency(vibrationInfo0.gameEvent()) > VibrationSystem.getGameEventFrequency($$4.gameEvent());
                }
            }
        }
    }

    public Optional<VibrationInfo> chosenCandidate(long long0) {
        if (this.currentVibrationData.isEmpty()) {
            return Optional.empty();
        } else {
            return ((Pair) this.currentVibrationData.get()).getRight() < long0 ? Optional.of((VibrationInfo) ((Pair) this.currentVibrationData.get()).getLeft()) : Optional.empty();
        }
    }

    public void startOver() {
        this.currentVibrationData = Optional.empty();
    }
}
package harmonised.pmmo.config.codecs;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;

public class VeinData implements DataSource<VeinData> {

    public Optional<Integer> chargeCap;

    public Optional<Double> chargeRate;

    public Optional<Integer> consumeAmount;

    public static final Codec<VeinData> VEIN_DATA_CODEC = RecordCodecBuilder.create(instance -> instance.group(Codec.INT.optionalFieldOf("chargeCap").forGetter(vd -> vd.chargeCap), Codec.DOUBLE.optionalFieldOf("chargeRate").forGetter(vd -> vd.chargeRate), Codec.INT.optionalFieldOf("consumeAmount").forGetter(vd -> vd.consumeAmount)).apply(instance, VeinData::new));

    public static VeinData EMPTY = new VeinData(Optional.of(0), Optional.of(0.0), Optional.empty());

    public VeinData(Optional<Integer> chargeCap, Optional<Double> chargeRate, Optional<Integer> consumeAmount) {
        this.chargeCap = chargeCap;
        this.chargeRate = chargeRate;
        this.consumeAmount = consumeAmount;
    }

    public void replaceWith(VeinData other) {
        this.chargeCap = other.chargeCap;
        this.chargeRate = other.chargeRate;
        this.consumeAmount = other.consumeAmount;
    }

    public VeinData combine(VeinData other) {
        return new VeinData(this.chargeCap.orElse(0) > other.chargeCap.orElse(0) ? this.chargeCap : other.chargeCap, this.chargeRate.orElse(0.0) > other.chargeRate.orElse(0.0) ? this.chargeRate : other.chargeRate, this.consumeAmount.orElse(0) > other.consumeAmount.orElse(0) ? this.consumeAmount : other.consumeAmount);
    }

    @Override
    public boolean isUnconfigured() {
        return (this.chargeCap.isEmpty() || (Integer) this.chargeCap.get() == 0) && (this.chargeRate.isEmpty() || (double) ((Integer) this.chargeCap.get()).intValue() == 0.0) && (this.consumeAmount.isEmpty() || (Integer) this.consumeAmount.get() == 0);
    }

    public String toString() {
        return String.format("{Rate:%1$s ,Cap:%2$s ,Consume:%3$s", this.chargeRate.isPresent() ? String.valueOf(this.chargeRate.get()) : "Empty", this.chargeCap.isPresent() ? String.valueOf(this.chargeCap.get()) : "Empty", this.consumeAmount.isPresent() ? String.valueOf(this.consumeAmount.get()) : "Empty");
    }
}
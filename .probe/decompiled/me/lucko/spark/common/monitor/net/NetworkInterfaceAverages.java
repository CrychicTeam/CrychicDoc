package me.lucko.spark.common.monitor.net;

import java.math.BigDecimal;
import me.lucko.spark.common.util.RollingAverage;

public final class NetworkInterfaceAverages {

    private final RollingAverage rxBytesPerSecond;

    private final RollingAverage txBytesPerSecond;

    private final RollingAverage rxPacketsPerSecond;

    private final RollingAverage txPacketsPerSecond;

    NetworkInterfaceAverages(int windowSize) {
        this.rxBytesPerSecond = new RollingAverage(windowSize);
        this.txBytesPerSecond = new RollingAverage(windowSize);
        this.rxPacketsPerSecond = new RollingAverage(windowSize);
        this.txPacketsPerSecond = new RollingAverage(windowSize);
    }

    void accept(NetworkInterfaceInfo info, NetworkInterfaceAverages.RateCalculator rateCalculator) {
        this.rxBytesPerSecond.add(rateCalculator.calculate(info.getReceivedBytes()));
        this.txBytesPerSecond.add(rateCalculator.calculate(info.getTransmittedBytes()));
        this.rxPacketsPerSecond.add(rateCalculator.calculate(info.getReceivedPackets()));
        this.txPacketsPerSecond.add(rateCalculator.calculate(info.getTransmittedPackets()));
    }

    public RollingAverage bytesPerSecond(Direction direction) {
        switch(direction) {
            case RECEIVE:
                return this.rxBytesPerSecond();
            case TRANSMIT:
                return this.txBytesPerSecond();
            default:
                throw new AssertionError();
        }
    }

    public RollingAverage packetsPerSecond(Direction direction) {
        switch(direction) {
            case RECEIVE:
                return this.rxPacketsPerSecond();
            case TRANSMIT:
                return this.txPacketsPerSecond();
            default:
                throw new AssertionError();
        }
    }

    public RollingAverage rxBytesPerSecond() {
        return this.rxBytesPerSecond;
    }

    public RollingAverage rxPacketsPerSecond() {
        return this.rxPacketsPerSecond;
    }

    public RollingAverage txBytesPerSecond() {
        return this.txBytesPerSecond;
    }

    public RollingAverage txPacketsPerSecond() {
        return this.txPacketsPerSecond;
    }

    interface RateCalculator {

        BigDecimal calculate(long var1);
    }
}
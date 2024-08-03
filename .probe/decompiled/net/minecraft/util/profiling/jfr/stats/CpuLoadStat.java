package net.minecraft.util.profiling.jfr.stats;

import jdk.jfr.consumer.RecordedEvent;

public record CpuLoadStat(double f_185614_, double f_185615_, double f_185616_) {

    private final double jvm;

    private final double userJvm;

    private final double system;

    public CpuLoadStat(double f_185614_, double f_185615_, double f_185616_) {
        this.jvm = f_185614_;
        this.userJvm = f_185615_;
        this.system = f_185616_;
    }

    public static CpuLoadStat from(RecordedEvent p_185623_) {
        return new CpuLoadStat((double) p_185623_.getFloat("jvmSystem"), (double) p_185623_.getFloat("jvmUser"), (double) p_185623_.getFloat("machineTotal"));
    }
}
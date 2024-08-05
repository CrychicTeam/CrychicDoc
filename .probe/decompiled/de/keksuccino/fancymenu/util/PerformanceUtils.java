package de.keksuccino.fancymenu.util;

import com.sun.management.OperatingSystemMXBean;
import java.lang.management.ManagementFactory;

public class PerformanceUtils {

    protected static final OperatingSystemMXBean OS_BEAN = (OperatingSystemMXBean) ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);

    public static double getJvmCpuUsage() {
        return OS_BEAN == null ? 0.0 : OS_BEAN.getProcessCpuLoad();
    }

    public static double getOsCpuUsage() {
        return OS_BEAN == null ? 0.0 : OS_BEAN.getCpuLoad();
    }
}
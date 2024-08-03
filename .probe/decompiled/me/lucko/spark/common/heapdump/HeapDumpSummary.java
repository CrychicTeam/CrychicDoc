package me.lucko.spark.common.heapdump;

import java.lang.management.ManagementFactory;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.management.JMX;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import me.lucko.spark.common.SparkPlatform;
import me.lucko.spark.common.command.sender.CommandSender;
import me.lucko.spark.lib.asm.Type;
import me.lucko.spark.proto.SparkHeapProtos;

public final class HeapDumpSummary {

    private static final String DIAGNOSTIC_BEAN = "com.sun.management:type=DiagnosticCommand";

    private static final Pattern OUTPUT_FORMAT = Pattern.compile("^\\s*(\\d+):\\s*(\\d+)\\s*(\\d+)\\s*([^\\s]+).*$");

    private final List<HeapDumpSummary.Entry> entries;

    private static String getRawHeapData() throws Exception {
        MBeanServer beanServer = ManagementFactory.getPlatformMBeanServer();
        ObjectName diagnosticBeanName = ObjectName.getInstance("com.sun.management:type=DiagnosticCommand");
        HeapDumpSummary.DiagnosticCommandMXBean proxy = (HeapDumpSummary.DiagnosticCommandMXBean) JMX.newMXBeanProxy(beanServer, diagnosticBeanName, HeapDumpSummary.DiagnosticCommandMXBean.class);
        return proxy.gcClassHistogram(new String[0]);
    }

    private static String typeToClassName(String type) {
        try {
            return Type.getType(type).getClassName();
        } catch (IllegalArgumentException var2) {
            return type;
        }
    }

    public static HeapDumpSummary createNew() {
        String rawOutput;
        try {
            rawOutput = getRawHeapData();
        } catch (Exception var2) {
            throw new RuntimeException("Unable to get heap dump", var2);
        }
        return new HeapDumpSummary((List<HeapDumpSummary.Entry>) Arrays.stream(rawOutput.split("\n")).map(line -> {
            Matcher matcher = OUTPUT_FORMAT.matcher(line);
            if (!matcher.matches()) {
                return null;
            } else {
                try {
                    return new HeapDumpSummary.Entry(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2)), Long.parseLong(matcher.group(3)), typeToClassName(matcher.group(4)));
                } catch (Exception var3) {
                    new IllegalArgumentException("Exception parsing line: " + line, var3).printStackTrace();
                    return null;
                }
            }
        }).filter(Objects::nonNull).collect(Collectors.toList()));
    }

    private HeapDumpSummary(List<HeapDumpSummary.Entry> entries) {
        this.entries = entries;
    }

    public SparkHeapProtos.HeapData toProto(SparkPlatform platform, CommandSender creator) {
        SparkHeapProtos.HeapMetadata.Builder metadata = SparkHeapProtos.HeapMetadata.newBuilder().setPlatformMetadata(platform.getPlugin().getPlatformInfo().toData().toProto()).setCreator(creator.toData().toProto());
        try {
            metadata.setPlatformStatistics(platform.getStatisticsProvider().getPlatformStatistics(null, true));
        } catch (Exception var8) {
            var8.printStackTrace();
        }
        try {
            metadata.setSystemStatistics(platform.getStatisticsProvider().getSystemStatistics());
        } catch (Exception var7) {
            var7.printStackTrace();
        }
        SparkHeapProtos.HeapData.Builder proto = SparkHeapProtos.HeapData.newBuilder();
        proto.setMetadata(metadata);
        for (HeapDumpSummary.Entry entry : this.entries) {
            proto.addEntries(entry.toProto());
        }
        return proto.build();
    }

    public interface DiagnosticCommandMXBean {

        String gcClassHistogram(String[] var1);
    }

    public static final class Entry {

        private final int order;

        private final int instances;

        private final long bytes;

        private final String type;

        Entry(int order, int instances, long bytes, String type) {
            this.order = order;
            this.instances = instances;
            this.bytes = bytes;
            this.type = type;
        }

        public int getOrder() {
            return this.order;
        }

        public int getInstances() {
            return this.instances;
        }

        public long getBytes() {
            return this.bytes;
        }

        public String getType() {
            return this.type;
        }

        public SparkHeapProtos.HeapEntry toProto() {
            return SparkHeapProtos.HeapEntry.newBuilder().setOrder(this.order).setInstances(this.instances).setSize(this.bytes).setType(this.type).build();
        }
    }
}
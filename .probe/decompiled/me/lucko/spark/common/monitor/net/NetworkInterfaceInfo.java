package me.lucko.spark.common.monitor.net;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import me.lucko.spark.common.monitor.LinuxProc;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class NetworkInterfaceInfo {

    public static final NetworkInterfaceInfo ZERO = new NetworkInterfaceInfo("", 0L, 0L, 0L, 0L, 0L, 0L);

    private final String name;

    private final long rxBytes;

    private final long rxPackets;

    private final long rxErrors;

    private final long txBytes;

    private final long txPackets;

    private final long txErrors;

    private static final Pattern PROC_NET_DEV_PATTERN = Pattern.compile("^\\s*(\\w+):([\\d\\s]+)$");

    public NetworkInterfaceInfo(String name, long rxBytes, long rxPackets, long rxErrors, long txBytes, long txPackets, long txErrors) {
        this.name = name;
        this.rxBytes = rxBytes;
        this.rxPackets = rxPackets;
        this.rxErrors = rxErrors;
        this.txBytes = txBytes;
        this.txPackets = txPackets;
        this.txErrors = txErrors;
    }

    public String getName() {
        return this.name;
    }

    public long getReceivedBytes() {
        return this.rxBytes;
    }

    public long getReceivedPackets() {
        return this.rxPackets;
    }

    public long getReceiveErrors() {
        return this.rxErrors;
    }

    public long getTransmittedBytes() {
        return this.txBytes;
    }

    public long getTransmittedPackets() {
        return this.txPackets;
    }

    public long getTransmitErrors() {
        return this.txErrors;
    }

    public long getBytes(Direction direction) {
        switch(direction) {
            case RECEIVE:
                return this.getReceivedBytes();
            case TRANSMIT:
                return this.getTransmittedBytes();
            default:
                throw new AssertionError();
        }
    }

    public long getPackets(Direction direction) {
        switch(direction) {
            case RECEIVE:
                return this.getReceivedPackets();
            case TRANSMIT:
                return this.getTransmittedPackets();
            default:
                throw new AssertionError();
        }
    }

    public boolean isZero() {
        return this.rxBytes == 0L && this.rxPackets == 0L && this.rxErrors == 0L && this.txBytes == 0L && this.txPackets == 0L && this.txErrors == 0L;
    }

    public NetworkInterfaceInfo subtract(NetworkInterfaceInfo other) {
        return other != ZERO && !other.isZero() ? new NetworkInterfaceInfo(this.name, this.rxBytes - other.rxBytes, this.rxPackets - other.rxPackets, this.rxErrors - other.rxErrors, this.txBytes - other.txBytes, this.txPackets - other.txPackets, this.txErrors - other.txErrors) : this;
    }

    @NonNull
    public static Map<String, NetworkInterfaceInfo> difference(Map<String, NetworkInterfaceInfo> current, Map<String, NetworkInterfaceInfo> previous) {
        if (previous != null && !previous.isEmpty()) {
            Builder<String, NetworkInterfaceInfo> builder = ImmutableMap.builder();
            for (NetworkInterfaceInfo netInf : current.values()) {
                String name = netInf.getName();
                builder.put(name, netInf.subtract((NetworkInterfaceInfo) previous.getOrDefault(name, ZERO)));
            }
            return builder.build();
        } else {
            return current;
        }
    }

    @NonNull
    public static Map<String, NetworkInterfaceInfo> pollSystem() {
        try {
            List<String> output = LinuxProc.NET_DEV.read();
            return read(output);
        } catch (Exception var1) {
            return Collections.emptyMap();
        }
    }

    @NonNull
    private static Map<String, NetworkInterfaceInfo> read(List<String> output) {
        if (output.size() < 3) {
            return Collections.emptyMap();
        } else {
            String header = (String) output.get(1);
            String[] categories = header.split("\\|");
            if (categories.length != 3) {
                return Collections.emptyMap();
            } else {
                List<String> rxFields = Arrays.asList(categories[1].trim().split("\\s+"));
                List<String> txFields = Arrays.asList(categories[2].trim().split("\\s+"));
                int rxFieldsLength = rxFields.size();
                int txFieldsLength = txFields.size();
                int fieldRxBytes = rxFields.indexOf("bytes");
                int fieldRxPackets = rxFields.indexOf("packets");
                int fieldRxErrors = rxFields.indexOf("errs");
                int fieldTxBytes = rxFieldsLength + txFields.indexOf("bytes");
                int fieldTxPackets = rxFieldsLength + txFields.indexOf("packets");
                int fieldTxErrors = rxFieldsLength + txFields.indexOf("errs");
                int expectedFields = rxFieldsLength + txFieldsLength;
                if (IntStream.of(new int[] { fieldRxBytes, fieldRxPackets, fieldRxErrors, fieldTxBytes, fieldTxPackets, fieldTxErrors }).anyMatch(i -> i == -1)) {
                    return Collections.emptyMap();
                } else {
                    Builder<String, NetworkInterfaceInfo> builder = ImmutableMap.builder();
                    for (String line : output.subList(2, output.size())) {
                        Matcher matcher = PROC_NET_DEV_PATTERN.matcher(line);
                        if (matcher.matches()) {
                            String interfaceName = matcher.group(1);
                            String[] stringValues = matcher.group(2).trim().split("\\s+");
                            if (stringValues.length == expectedFields) {
                                long[] values = Arrays.stream(stringValues).mapToLong(Long::parseLong).toArray();
                                builder.put(interfaceName, new NetworkInterfaceInfo(interfaceName, values[fieldRxBytes], values[fieldRxPackets], values[fieldRxErrors], values[fieldTxBytes], values[fieldTxPackets], values[fieldTxErrors]));
                            }
                        }
                    }
                    return builder.build();
                }
            }
        }
    }
}
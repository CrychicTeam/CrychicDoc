package me.lucko.spark.common.sampler;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import me.lucko.spark.proto.SparkSamplerProtos;

public interface ThreadGrouper {

    ThreadGrouper BY_NAME = new ThreadGrouper() {

        @Override
        public String getGroup(long threadId, String threadName) {
            return threadName;
        }

        @Override
        public String getLabel(String group) {
            return group;
        }

        @Override
        public SparkSamplerProtos.SamplerMetadata.DataAggregator.ThreadGrouper asProto() {
            return SparkSamplerProtos.SamplerMetadata.DataAggregator.ThreadGrouper.BY_NAME;
        }
    };

    ThreadGrouper BY_POOL = new ThreadGrouper() {

        private final Pattern pattern = Pattern.compile("^(.*?)[-# ]+\\d+$");

        private final Map<Long, String> cache = new ConcurrentHashMap();

        private final Map<String, Set<Long>> seen = new ConcurrentHashMap();

        @Override
        public String getGroup(long threadId, String threadName) {
            String cached = (String) this.cache.get(threadId);
            if (cached != null) {
                return cached;
            } else {
                Matcher matcher = this.pattern.matcher(threadName);
                if (!matcher.matches()) {
                    return threadName;
                } else {
                    String group = matcher.group(1).trim();
                    this.cache.put(threadId, group);
                    ((Set) this.seen.computeIfAbsent(group, g -> ConcurrentHashMap.newKeySet())).add(threadId);
                    return group;
                }
            }
        }

        @Override
        public String getLabel(String group) {
            int count = ((Set) this.seen.getOrDefault(group, Collections.emptySet())).size();
            return count == 0 ? group : group + " (x" + count + ")";
        }

        @Override
        public SparkSamplerProtos.SamplerMetadata.DataAggregator.ThreadGrouper asProto() {
            return SparkSamplerProtos.SamplerMetadata.DataAggregator.ThreadGrouper.BY_POOL;
        }
    };

    ThreadGrouper AS_ONE = new ThreadGrouper() {

        private final Set<Long> seen = ConcurrentHashMap.newKeySet();

        @Override
        public String getGroup(long threadId, String threadName) {
            this.seen.add(threadId);
            return "root";
        }

        @Override
        public String getLabel(String group) {
            return "All (x" + this.seen.size() + ")";
        }

        @Override
        public SparkSamplerProtos.SamplerMetadata.DataAggregator.ThreadGrouper asProto() {
            return SparkSamplerProtos.SamplerMetadata.DataAggregator.ThreadGrouper.AS_ONE;
        }
    };

    String getGroup(long var1, String var3);

    String getLabel(String var1);

    SparkSamplerProtos.SamplerMetadata.DataAggregator.ThreadGrouper asProto();

    static ThreadGrouper parseConfigSetting(String setting) {
        switch(setting) {
            case "as-one":
                return AS_ONE;
            case "by-name":
                return BY_NAME;
            default:
                return BY_POOL;
        }
    }
}
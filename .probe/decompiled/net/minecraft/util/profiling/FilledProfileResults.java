package net.minecraft.util.profiling;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mojang.logging.LogUtils;
import it.unimi.dsi.fastutil.objects.Object2LongMap;
import it.unimi.dsi.fastutil.objects.Object2LongMaps;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.SharedConstants;
import net.minecraft.Util;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;

public class FilledProfileResults implements ProfileResults {

    private static final Logger LOGGER = LogUtils.getLogger();

    private static final ProfilerPathEntry EMPTY = new ProfilerPathEntry() {

        @Override
        public long getDuration() {
            return 0L;
        }

        @Override
        public long getMaxDuration() {
            return 0L;
        }

        @Override
        public long getCount() {
            return 0L;
        }

        @Override
        public Object2LongMap<String> getCounters() {
            return Object2LongMaps.emptyMap();
        }
    };

    private static final Splitter SPLITTER = Splitter.on('\u001e');

    private static final Comparator<Entry<String, FilledProfileResults.CounterCollector>> COUNTER_ENTRY_COMPARATOR = Entry.comparingByValue(Comparator.comparingLong(p_18489_ -> p_18489_.totalValue)).reversed();

    private final Map<String, ? extends ProfilerPathEntry> entries;

    private final long startTimeNano;

    private final int startTimeTicks;

    private final long endTimeNano;

    private final int endTimeTicks;

    private final int tickDuration;

    public FilledProfileResults(Map<String, ? extends ProfilerPathEntry> mapStringExtendsProfilerPathEntry0, long long1, int int2, long long3, int int4) {
        this.entries = mapStringExtendsProfilerPathEntry0;
        this.startTimeNano = long1;
        this.startTimeTicks = int2;
        this.endTimeNano = long3;
        this.endTimeTicks = int4;
        this.tickDuration = int4 - int2;
    }

    private ProfilerPathEntry getEntry(String string0) {
        ProfilerPathEntry $$1 = (ProfilerPathEntry) this.entries.get(string0);
        return $$1 != null ? $$1 : EMPTY;
    }

    @Override
    public List<ResultField> getTimes(String string0) {
        String $$1 = string0;
        ProfilerPathEntry $$2 = this.getEntry("root");
        long $$3 = $$2.getDuration();
        ProfilerPathEntry $$4 = this.getEntry(string0);
        long $$5 = $$4.getDuration();
        long $$6 = $$4.getCount();
        List<ResultField> $$7 = Lists.newArrayList();
        if (!string0.isEmpty()) {
            string0 = string0 + "\u001e";
        }
        long $$8 = 0L;
        for (String $$9 : this.entries.keySet()) {
            if (isDirectChild(string0, $$9)) {
                $$8 += this.getEntry($$9).getDuration();
            }
        }
        float $$10 = (float) $$8;
        if ($$8 < $$5) {
            $$8 = $$5;
        }
        if ($$3 < $$8) {
            $$3 = $$8;
        }
        for (String $$11 : this.entries.keySet()) {
            if (isDirectChild(string0, $$11)) {
                ProfilerPathEntry $$12 = this.getEntry($$11);
                long $$13 = $$12.getDuration();
                double $$14 = (double) $$13 * 100.0 / (double) $$8;
                double $$15 = (double) $$13 * 100.0 / (double) $$3;
                String $$16 = $$11.substring(string0.length());
                $$7.add(new ResultField($$16, $$14, $$15, $$12.getCount()));
            }
        }
        if ((float) $$8 > $$10) {
            $$7.add(new ResultField("unspecified", (double) ((float) $$8 - $$10) * 100.0 / (double) $$8, (double) ((float) $$8 - $$10) * 100.0 / (double) $$3, $$6));
        }
        Collections.sort($$7);
        $$7.add(0, new ResultField($$1, 100.0, (double) $$8 * 100.0 / (double) $$3, $$6));
        return $$7;
    }

    private static boolean isDirectChild(String string0, String string1) {
        return string1.length() > string0.length() && string1.startsWith(string0) && string1.indexOf(30, string0.length() + 1) < 0;
    }

    private Map<String, FilledProfileResults.CounterCollector> getCounterValues() {
        Map<String, FilledProfileResults.CounterCollector> $$0 = Maps.newTreeMap();
        this.entries.forEach((p_18512_, p_18513_) -> {
            Object2LongMap<String> $$3 = p_18513_.getCounters();
            if (!$$3.isEmpty()) {
                List<String> $$4 = SPLITTER.splitToList(p_18512_);
                $$3.forEach((p_145944_, p_145945_) -> ((FilledProfileResults.CounterCollector) $$0.computeIfAbsent(p_145944_, p_145947_ -> new FilledProfileResults.CounterCollector())).addValue($$4.iterator(), p_145945_));
            }
        });
        return $$0;
    }

    @Override
    public long getStartTimeNano() {
        return this.startTimeNano;
    }

    @Override
    public int getStartTimeTicks() {
        return this.startTimeTicks;
    }

    @Override
    public long getEndTimeNano() {
        return this.endTimeNano;
    }

    @Override
    public int getEndTimeTicks() {
        return this.endTimeTicks;
    }

    @Override
    public boolean saveResults(Path path0) {
        Writer $$1 = null;
        boolean var4;
        try {
            Files.createDirectories(path0.getParent());
            $$1 = Files.newBufferedWriter(path0, StandardCharsets.UTF_8);
            $$1.write(this.getProfilerResults(this.m_18577_(), this.getTickDuration()));
            return true;
        } catch (Throwable var8) {
            LOGGER.error("Could not save profiler results to {}", path0, var8);
            var4 = false;
        } finally {
            IOUtils.closeQuietly($$1);
        }
        return var4;
    }

    protected String getProfilerResults(long long0, int int1) {
        StringBuilder $$2 = new StringBuilder();
        $$2.append("---- Minecraft Profiler Results ----\n");
        $$2.append("// ");
        $$2.append(getComment());
        $$2.append("\n\n");
        $$2.append("Version: ").append(SharedConstants.getCurrentVersion().getId()).append('\n');
        $$2.append("Time span: ").append(long0 / 1000000L).append(" ms\n");
        $$2.append("Tick span: ").append(int1).append(" ticks\n");
        $$2.append("// This is approximately ").append(String.format(Locale.ROOT, "%.2f", (float) int1 / ((float) long0 / 1.0E9F))).append(" ticks per second. It should be ").append(20).append(" ticks per second\n\n");
        $$2.append("--- BEGIN PROFILE DUMP ---\n\n");
        this.appendProfilerResults(0, "root", $$2);
        $$2.append("--- END PROFILE DUMP ---\n\n");
        Map<String, FilledProfileResults.CounterCollector> $$3 = this.getCounterValues();
        if (!$$3.isEmpty()) {
            $$2.append("--- BEGIN COUNTER DUMP ---\n\n");
            this.appendCounters($$3, $$2, int1);
            $$2.append("--- END COUNTER DUMP ---\n\n");
        }
        return $$2.toString();
    }

    @Override
    public String getProfilerResults() {
        StringBuilder $$0 = new StringBuilder();
        this.appendProfilerResults(0, "root", $$0);
        return $$0.toString();
    }

    private static StringBuilder indentLine(StringBuilder stringBuilder0, int int1) {
        stringBuilder0.append(String.format(Locale.ROOT, "[%02d] ", int1));
        for (int $$2 = 0; $$2 < int1; $$2++) {
            stringBuilder0.append("|   ");
        }
        return stringBuilder0;
    }

    private void appendProfilerResults(int int0, String string1, StringBuilder stringBuilder2) {
        List<ResultField> $$3 = this.getTimes(string1);
        Object2LongMap<String> $$4 = ((ProfilerPathEntry) ObjectUtils.firstNonNull(new ProfilerPathEntry[] { (ProfilerPathEntry) this.entries.get(string1), EMPTY })).getCounters();
        $$4.forEach((p_18508_, p_18509_) -> indentLine(stringBuilder2, int0).append('#').append(p_18508_).append(' ').append(p_18509_).append('/').append(p_18509_ / (long) this.tickDuration).append('\n'));
        if ($$3.size() >= 3) {
            for (int $$5 = 1; $$5 < $$3.size(); $$5++) {
                ResultField $$6 = (ResultField) $$3.get($$5);
                indentLine(stringBuilder2, int0).append($$6.name).append('(').append($$6.count).append('/').append(String.format(Locale.ROOT, "%.0f", (float) $$6.count / (float) this.tickDuration)).append(')').append(" - ").append(String.format(Locale.ROOT, "%.2f", $$6.percentage)).append("%/").append(String.format(Locale.ROOT, "%.2f", $$6.globalPercentage)).append("%\n");
                if (!"unspecified".equals($$6.name)) {
                    try {
                        this.appendProfilerResults(int0 + 1, string1 + "\u001e" + $$6.name, stringBuilder2);
                    } catch (Exception var9) {
                        stringBuilder2.append("[[ EXCEPTION ").append(var9).append(" ]]");
                    }
                }
            }
        }
    }

    private void appendCounterResults(int int0, String string1, FilledProfileResults.CounterCollector filledProfileResultsCounterCollector2, int int3, StringBuilder stringBuilder4) {
        indentLine(stringBuilder4, int0).append(string1).append(" total:").append(filledProfileResultsCounterCollector2.selfValue).append('/').append(filledProfileResultsCounterCollector2.totalValue).append(" average: ").append(filledProfileResultsCounterCollector2.selfValue / (long) int3).append('/').append(filledProfileResultsCounterCollector2.totalValue / (long) int3).append('\n');
        filledProfileResultsCounterCollector2.children.entrySet().stream().sorted(COUNTER_ENTRY_COMPARATOR).forEach(p_18474_ -> this.appendCounterResults(int0 + 1, (String) p_18474_.getKey(), (FilledProfileResults.CounterCollector) p_18474_.getValue(), int3, stringBuilder4));
    }

    private void appendCounters(Map<String, FilledProfileResults.CounterCollector> mapStringFilledProfileResultsCounterCollector0, StringBuilder stringBuilder1, int int2) {
        mapStringFilledProfileResultsCounterCollector0.forEach((p_18503_, p_18504_) -> {
            stringBuilder1.append("-- Counter: ").append(p_18503_).append(" --\n");
            this.appendCounterResults(0, "root", (FilledProfileResults.CounterCollector) p_18504_.children.get("root"), int2, stringBuilder1);
            stringBuilder1.append("\n\n");
        });
    }

    private static String getComment() {
        String[] $$0 = new String[] { "I'd Rather Be Surfing", "Shiny numbers!", "Am I not running fast enough? :(", "I'm working as hard as I can!", "Will I ever be good enough for you? :(", "Speedy. Zoooooom!", "Hello world", "40% better than a crash report.", "Now with extra numbers", "Now with less numbers", "Now with the same numbers", "You should add flames to things, it makes them go faster!", "Do you feel the need for... optimization?", "*cracks redstone whip*", "Maybe if you treated it better then it'll have more motivation to work faster! Poor server." };
        try {
            return $$0[(int) (Util.getNanos() % (long) $$0.length)];
        } catch (Throwable var2) {
            return "Witty comment unavailable :(";
        }
    }

    @Override
    public int getTickDuration() {
        return this.tickDuration;
    }

    static class CounterCollector {

        long selfValue;

        long totalValue;

        final Map<String, FilledProfileResults.CounterCollector> children = Maps.newHashMap();

        public void addValue(Iterator<String> iteratorString0, long long1) {
            this.totalValue += long1;
            if (!iteratorString0.hasNext()) {
                this.selfValue += long1;
            } else {
                ((FilledProfileResults.CounterCollector) this.children.computeIfAbsent((String) iteratorString0.next(), p_18546_ -> new FilledProfileResults.CounterCollector())).addValue(iteratorString0, long1);
            }
        }
    }
}
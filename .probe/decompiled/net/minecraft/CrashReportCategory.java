package net.minecraft;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.Locale;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.block.state.BlockState;

public class CrashReportCategory {

    private final String title;

    private final List<CrashReportCategory.Entry> entries = Lists.newArrayList();

    private StackTraceElement[] stackTrace = new StackTraceElement[0];

    public CrashReportCategory(String string0) {
        this.title = string0;
    }

    public static String formatLocation(LevelHeightAccessor levelHeightAccessor0, double double1, double double2, double double3) {
        return String.format(Locale.ROOT, "%.2f,%.2f,%.2f - %s", double1, double2, double3, formatLocation(levelHeightAccessor0, BlockPos.containing(double1, double2, double3)));
    }

    public static String formatLocation(LevelHeightAccessor levelHeightAccessor0, BlockPos blockPos1) {
        return formatLocation(levelHeightAccessor0, blockPos1.m_123341_(), blockPos1.m_123342_(), blockPos1.m_123343_());
    }

    public static String formatLocation(LevelHeightAccessor levelHeightAccessor0, int int1, int int2, int int3) {
        StringBuilder $$4 = new StringBuilder();
        try {
            $$4.append(String.format(Locale.ROOT, "World: (%d,%d,%d)", int1, int2, int3));
        } catch (Throwable var19) {
            $$4.append("(Error finding world loc)");
        }
        $$4.append(", ");
        try {
            int $$6 = SectionPos.blockToSectionCoord(int1);
            int $$7 = SectionPos.blockToSectionCoord(int2);
            int $$8 = SectionPos.blockToSectionCoord(int3);
            int $$9 = int1 & 15;
            int $$10 = int2 & 15;
            int $$11 = int3 & 15;
            int $$12 = SectionPos.sectionToBlockCoord($$6);
            int $$13 = levelHeightAccessor0.getMinBuildHeight();
            int $$14 = SectionPos.sectionToBlockCoord($$8);
            int $$15 = SectionPos.sectionToBlockCoord($$6 + 1) - 1;
            int $$16 = levelHeightAccessor0.getMaxBuildHeight() - 1;
            int $$17 = SectionPos.sectionToBlockCoord($$8 + 1) - 1;
            $$4.append(String.format(Locale.ROOT, "Section: (at %d,%d,%d in %d,%d,%d; chunk contains blocks %d,%d,%d to %d,%d,%d)", $$9, $$10, $$11, $$6, $$7, $$8, $$12, $$13, $$14, $$15, $$16, $$17));
        } catch (Throwable var18) {
            $$4.append("(Error finding chunk loc)");
        }
        $$4.append(", ");
        try {
            int $$19 = int1 >> 9;
            int $$20 = int3 >> 9;
            int $$21 = $$19 << 5;
            int $$22 = $$20 << 5;
            int $$23 = ($$19 + 1 << 5) - 1;
            int $$24 = ($$20 + 1 << 5) - 1;
            int $$25 = $$19 << 9;
            int $$26 = levelHeightAccessor0.getMinBuildHeight();
            int $$27 = $$20 << 9;
            int $$28 = ($$19 + 1 << 9) - 1;
            int $$29 = levelHeightAccessor0.getMaxBuildHeight() - 1;
            int $$30 = ($$20 + 1 << 9) - 1;
            $$4.append(String.format(Locale.ROOT, "Region: (%d,%d; contains chunks %d,%d to %d,%d, blocks %d,%d,%d to %d,%d,%d)", $$19, $$20, $$21, $$22, $$23, $$24, $$25, $$26, $$27, $$28, $$29, $$30));
        } catch (Throwable var17) {
            $$4.append("(Error finding world loc)");
        }
        return $$4.toString();
    }

    public CrashReportCategory setDetail(String string0, CrashReportDetail<String> crashReportDetailString1) {
        try {
            this.setDetail(string0, crashReportDetailString1.call());
        } catch (Throwable var4) {
            this.setDetailError(string0, var4);
        }
        return this;
    }

    public CrashReportCategory setDetail(String string0, Object object1) {
        this.entries.add(new CrashReportCategory.Entry(string0, object1));
        return this;
    }

    public void setDetailError(String string0, Throwable throwable1) {
        this.setDetail(string0, throwable1);
    }

    public int fillInStackTrace(int int0) {
        StackTraceElement[] $$1 = Thread.currentThread().getStackTrace();
        if ($$1.length <= 0) {
            return 0;
        } else {
            this.stackTrace = new StackTraceElement[$$1.length - 3 - int0];
            System.arraycopy($$1, 3 + int0, this.stackTrace, 0, this.stackTrace.length);
            return this.stackTrace.length;
        }
    }

    public boolean validateStackTrace(StackTraceElement stackTraceElement0, StackTraceElement stackTraceElement1) {
        if (this.stackTrace.length != 0 && stackTraceElement0 != null) {
            StackTraceElement $$2 = this.stackTrace[0];
            if ($$2.isNativeMethod() == stackTraceElement0.isNativeMethod() && $$2.getClassName().equals(stackTraceElement0.getClassName()) && $$2.getFileName().equals(stackTraceElement0.getFileName()) && $$2.getMethodName().equals(stackTraceElement0.getMethodName())) {
                if (stackTraceElement1 != null != this.stackTrace.length > 1) {
                    return false;
                } else if (stackTraceElement1 != null && !this.stackTrace[1].equals(stackTraceElement1)) {
                    return false;
                } else {
                    this.stackTrace[0] = stackTraceElement0;
                    return true;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public void trimStacktrace(int int0) {
        StackTraceElement[] $$1 = new StackTraceElement[this.stackTrace.length - int0];
        System.arraycopy(this.stackTrace, 0, $$1, 0, $$1.length);
        this.stackTrace = $$1;
    }

    public void getDetails(StringBuilder stringBuilder0) {
        stringBuilder0.append("-- ").append(this.title).append(" --\n");
        stringBuilder0.append("Details:");
        for (CrashReportCategory.Entry $$1 : this.entries) {
            stringBuilder0.append("\n\t");
            stringBuilder0.append($$1.getKey());
            stringBuilder0.append(": ");
            stringBuilder0.append($$1.getValue());
        }
        if (this.stackTrace != null && this.stackTrace.length > 0) {
            stringBuilder0.append("\nStacktrace:");
            for (StackTraceElement $$2 : this.stackTrace) {
                stringBuilder0.append("\n\tat ");
                stringBuilder0.append($$2);
            }
        }
    }

    public StackTraceElement[] getStacktrace() {
        return this.stackTrace;
    }

    public static void populateBlockDetails(CrashReportCategory crashReportCategory0, LevelHeightAccessor levelHeightAccessor1, BlockPos blockPos2, @Nullable BlockState blockState3) {
        if (blockState3 != null) {
            crashReportCategory0.setDetail("Block", blockState3::toString);
        }
        crashReportCategory0.setDetail("Block location", (CrashReportDetail<String>) (() -> formatLocation(levelHeightAccessor1, blockPos2)));
    }

    static class Entry {

        private final String key;

        private final String value;

        public Entry(String string0, @Nullable Object object1) {
            this.key = string0;
            if (object1 == null) {
                this.value = "~~NULL~~";
            } else if (object1 instanceof Throwable $$2) {
                this.value = "~~ERROR~~ " + $$2.getClass().getSimpleName() + ": " + $$2.getMessage();
            } else {
                this.value = object1.toString();
            }
        }

        public String getKey() {
            return this.key;
        }

        public String getValue() {
            return this.value;
        }
    }
}
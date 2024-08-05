package net.minecraft;

import com.google.common.collect.Lists;
import com.mojang.logging.LogUtils;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletionException;
import net.minecraft.util.MemoryReserve;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;

public class CrashReport {

    private static final Logger LOGGER = LogUtils.getLogger();

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.ROOT);

    private final String title;

    private final Throwable exception;

    private final List<CrashReportCategory> details = Lists.newArrayList();

    private File saveFile;

    private boolean trackingStackTrace = true;

    private StackTraceElement[] uncategorizedStackTrace = new StackTraceElement[0];

    private final SystemReport systemReport = new SystemReport();

    public CrashReport(String string0, Throwable throwable1) {
        this.title = string0;
        this.exception = throwable1;
    }

    public String getTitle() {
        return this.title;
    }

    public Throwable getException() {
        return this.exception;
    }

    public String getDetails() {
        StringBuilder $$0 = new StringBuilder();
        this.getDetails($$0);
        return $$0.toString();
    }

    public void getDetails(StringBuilder stringBuilder0) {
        if ((this.uncategorizedStackTrace == null || this.uncategorizedStackTrace.length <= 0) && !this.details.isEmpty()) {
            this.uncategorizedStackTrace = (StackTraceElement[]) ArrayUtils.subarray(((CrashReportCategory) this.details.get(0)).getStacktrace(), 0, 1);
        }
        if (this.uncategorizedStackTrace != null && this.uncategorizedStackTrace.length > 0) {
            stringBuilder0.append("-- Head --\n");
            stringBuilder0.append("Thread: ").append(Thread.currentThread().getName()).append("\n");
            stringBuilder0.append("Stacktrace:\n");
            for (StackTraceElement $$1 : this.uncategorizedStackTrace) {
                stringBuilder0.append("\t").append("at ").append($$1);
                stringBuilder0.append("\n");
            }
            stringBuilder0.append("\n");
        }
        for (CrashReportCategory $$2 : this.details) {
            $$2.getDetails(stringBuilder0);
            stringBuilder0.append("\n\n");
        }
        this.systemReport.appendToCrashReportString(stringBuilder0);
    }

    public String getExceptionMessage() {
        StringWriter $$0 = null;
        PrintWriter $$1 = null;
        Throwable $$2 = this.exception;
        if ($$2.getMessage() == null) {
            if ($$2 instanceof NullPointerException) {
                $$2 = new NullPointerException(this.title);
            } else if ($$2 instanceof StackOverflowError) {
                $$2 = new StackOverflowError(this.title);
            } else if ($$2 instanceof OutOfMemoryError) {
                $$2 = new OutOfMemoryError(this.title);
            }
            $$2.setStackTrace(this.exception.getStackTrace());
        }
        String var4;
        try {
            $$0 = new StringWriter();
            $$1 = new PrintWriter($$0);
            $$2.printStackTrace($$1);
            var4 = $$0.toString();
        } finally {
            IOUtils.closeQuietly($$0);
            IOUtils.closeQuietly($$1);
        }
        return var4;
    }

    public String getFriendlyReport() {
        StringBuilder $$0 = new StringBuilder();
        $$0.append("---- Minecraft Crash Report ----\n");
        $$0.append("// ");
        $$0.append(getErrorComment());
        $$0.append("\n\n");
        $$0.append("Time: ");
        $$0.append(DATE_TIME_FORMATTER.format(ZonedDateTime.now()));
        $$0.append("\n");
        $$0.append("Description: ");
        $$0.append(this.title);
        $$0.append("\n\n");
        $$0.append(this.getExceptionMessage());
        $$0.append("\n\nA detailed walkthrough of the error, its code path and all known details is as follows:\n");
        for (int $$1 = 0; $$1 < 87; $$1++) {
            $$0.append("-");
        }
        $$0.append("\n\n");
        this.getDetails($$0);
        return $$0.toString();
    }

    public File getSaveFile() {
        return this.saveFile;
    }

    public boolean saveToFile(File file0) {
        if (this.saveFile != null) {
            return false;
        } else {
            if (file0.getParentFile() != null) {
                file0.getParentFile().mkdirs();
            }
            Writer $$1 = null;
            boolean var4;
            try {
                $$1 = new OutputStreamWriter(new FileOutputStream(file0), StandardCharsets.UTF_8);
                $$1.write(this.getFriendlyReport());
                this.saveFile = file0;
                return true;
            } catch (Throwable var8) {
                LOGGER.error("Could not save crash report to {}", file0, var8);
                var4 = false;
            } finally {
                IOUtils.closeQuietly($$1);
            }
            return var4;
        }
    }

    public SystemReport getSystemReport() {
        return this.systemReport;
    }

    public CrashReportCategory addCategory(String string0) {
        return this.addCategory(string0, 1);
    }

    public CrashReportCategory addCategory(String string0, int int1) {
        CrashReportCategory $$2 = new CrashReportCategory(string0);
        if (this.trackingStackTrace) {
            int $$3 = $$2.fillInStackTrace(int1);
            StackTraceElement[] $$4 = this.exception.getStackTrace();
            StackTraceElement $$5 = null;
            StackTraceElement $$6 = null;
            int $$7 = $$4.length - $$3;
            if ($$7 < 0) {
                System.out.println("Negative index in crash report handler (" + $$4.length + "/" + $$3 + ")");
            }
            if ($$4 != null && 0 <= $$7 && $$7 < $$4.length) {
                $$5 = $$4[$$7];
                if ($$4.length + 1 - $$3 < $$4.length) {
                    $$6 = $$4[$$4.length + 1 - $$3];
                }
            }
            this.trackingStackTrace = $$2.validateStackTrace($$5, $$6);
            if ($$4 != null && $$4.length >= $$3 && 0 <= $$7 && $$7 < $$4.length) {
                this.uncategorizedStackTrace = new StackTraceElement[$$7];
                System.arraycopy($$4, 0, this.uncategorizedStackTrace, 0, this.uncategorizedStackTrace.length);
            } else {
                this.trackingStackTrace = false;
            }
        }
        this.details.add($$2);
        return $$2;
    }

    private static String getErrorComment() {
        String[] $$0 = new String[] { "Who set us up the TNT?", "Everything's going to plan. No, really, that was supposed to happen.", "Uh... Did I do that?", "Oops.", "Why did you do that?", "I feel sad now :(", "My bad.", "I'm sorry, Dave.", "I let you down. Sorry :(", "On the bright side, I bought you a teddy bear!", "Daisy, daisy...", "Oh - I know what I did wrong!", "Hey, that tickles! Hehehe!", "I blame Dinnerbone.", "You should try our sister game, Minceraft!", "Don't be sad. I'll do better next time, I promise!", "Don't be sad, have a hug! <3", "I just don't know what went wrong :(", "Shall we play a game?", "Quite honestly, I wouldn't worry myself about that.", "I bet Cylons wouldn't have this problem.", "Sorry :(", "Surprise! Haha. Well, this is awkward.", "Would you like a cupcake?", "Hi. I'm Minecraft, and I'm a crashaholic.", "Ooh. Shiny.", "This doesn't make any sense!", "Why is it breaking :(", "Don't do that.", "Ouch. That hurt :(", "You're mean.", "This is a token for 1 free hug. Redeem at your nearest Mojangsta: [~~HUG~~]", "There are four lights!", "But it works on my machine." };
        try {
            return $$0[(int) (Util.getNanos() % (long) $$0.length)];
        } catch (Throwable var2) {
            return "Witty comment unavailable :(";
        }
    }

    public static CrashReport forThrowable(Throwable throwable0, String string1) {
        while (throwable0 instanceof CompletionException && throwable0.getCause() != null) {
            throwable0 = throwable0.getCause();
        }
        CrashReport $$2;
        if (throwable0 instanceof ReportedException) {
            $$2 = ((ReportedException) throwable0).getReport();
        } else {
            $$2 = new CrashReport(string1, throwable0);
        }
        return $$2;
    }

    public static void preload() {
        MemoryReserve.allocate();
        new CrashReport("Don't panic!", new Throwable()).getFriendlyReport();
    }
}
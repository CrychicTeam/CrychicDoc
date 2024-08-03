package net.minecraft.util;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import com.mojang.logging.LogUtils;
import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Platform;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.Kernel32Util;
import com.sun.jna.platform.win32.Version;
import com.sun.jna.platform.win32.Win32Exception;
import com.sun.jna.platform.win32.Tlhelp32.MODULEENTRY32W;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;
import java.nio.charset.StandardCharsets;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.stream.Collectors;
import net.minecraft.CrashReportCategory;
import net.minecraft.CrashReportDetail;
import org.slf4j.Logger;

public class NativeModuleLister {

    private static final Logger LOGGER = LogUtils.getLogger();

    private static final int LANG_MASK = 65535;

    private static final int DEFAULT_LANG = 1033;

    private static final int CODEPAGE_MASK = -65536;

    private static final int DEFAULT_CODEPAGE = 78643200;

    public static List<NativeModuleLister.NativeModuleInfo> listModules() {
        if (!Platform.isWindows()) {
            return ImmutableList.of();
        } else {
            int $$0 = Kernel32.INSTANCE.GetCurrentProcessId();
            Builder<NativeModuleLister.NativeModuleInfo> $$1 = ImmutableList.builder();
            for (MODULEENTRY32W $$3 : Kernel32Util.getModules($$0)) {
                String $$4 = $$3.szModule();
                Optional<NativeModuleLister.NativeModuleVersion> $$5 = tryGetVersion($$3.szExePath());
                $$1.add(new NativeModuleLister.NativeModuleInfo($$4, $$5));
            }
            return $$1.build();
        }
    }

    private static Optional<NativeModuleLister.NativeModuleVersion> tryGetVersion(String string0) {
        try {
            IntByReference $$1 = new IntByReference();
            int $$2 = Version.INSTANCE.GetFileVersionInfoSize(string0, $$1);
            if ($$2 == 0) {
                int $$3 = Native.getLastError();
                if ($$3 != 1813 && $$3 != 1812) {
                    throw new Win32Exception($$3);
                } else {
                    return Optional.empty();
                }
            } else {
                Pointer $$4 = new Memory((long) $$2);
                if (!Version.INSTANCE.GetFileVersionInfo(string0, 0, $$2, $$4)) {
                    throw new Win32Exception(Native.getLastError());
                } else {
                    IntByReference $$5 = new IntByReference();
                    Pointer $$6 = queryVersionValue($$4, "\\VarFileInfo\\Translation", $$5);
                    int[] $$7 = $$6.getIntArray(0L, $$5.getValue() / 4);
                    OptionalInt $$8 = findLangAndCodepage($$7);
                    if (!$$8.isPresent()) {
                        return Optional.empty();
                    } else {
                        int $$9 = $$8.getAsInt();
                        int $$10 = $$9 & 65535;
                        int $$11 = ($$9 & -65536) >> 16;
                        String $$12 = queryVersionString($$4, langTableKey("FileDescription", $$10, $$11), $$5);
                        String $$13 = queryVersionString($$4, langTableKey("CompanyName", $$10, $$11), $$5);
                        String $$14 = queryVersionString($$4, langTableKey("FileVersion", $$10, $$11), $$5);
                        return Optional.of(new NativeModuleLister.NativeModuleVersion($$12, $$14, $$13));
                    }
                }
            }
        } catch (Exception var14) {
            LOGGER.info("Failed to find module info for {}", string0, var14);
            return Optional.empty();
        }
    }

    private static String langTableKey(String string0, int int1, int int2) {
        return String.format(Locale.ROOT, "\\StringFileInfo\\%04x%04x\\%s", int1, int2, string0);
    }

    private static OptionalInt findLangAndCodepage(int[] int0) {
        OptionalInt $$1 = OptionalInt.empty();
        for (int $$2 : int0) {
            if (($$2 & -65536) == 78643200 && ($$2 & 65535) == 1033) {
                return OptionalInt.of($$2);
            }
            $$1 = OptionalInt.of($$2);
        }
        return $$1;
    }

    private static Pointer queryVersionValue(Pointer pointer0, String string1, IntByReference intByReference2) {
        PointerByReference $$3 = new PointerByReference();
        if (!Version.INSTANCE.VerQueryValue(pointer0, string1, $$3, intByReference2)) {
            throw new UnsupportedOperationException("Can't get version value " + string1);
        } else {
            return $$3.getValue();
        }
    }

    private static String queryVersionString(Pointer pointer0, String string1, IntByReference intByReference2) {
        try {
            Pointer $$3 = queryVersionValue(pointer0, string1, intByReference2);
            byte[] $$4 = $$3.getByteArray(0L, (intByReference2.getValue() - 1) * 2);
            return new String($$4, StandardCharsets.UTF_16LE);
        } catch (Exception var5) {
            return "";
        }
    }

    public static void addCrashSection(CrashReportCategory crashReportCategory0) {
        crashReportCategory0.setDetail("Modules", (CrashReportDetail<String>) (() -> (String) listModules().stream().sorted(Comparator.comparing(p_184685_ -> p_184685_.name)).map(p_184668_ -> "\n\t\t" + p_184668_).collect(Collectors.joining())));
    }

    public static class NativeModuleInfo {

        public final String name;

        public final Optional<NativeModuleLister.NativeModuleVersion> version;

        public NativeModuleInfo(String string0, Optional<NativeModuleLister.NativeModuleVersion> optionalNativeModuleListerNativeModuleVersion1) {
            this.name = string0;
            this.version = optionalNativeModuleListerNativeModuleVersion1;
        }

        public String toString() {
            return (String) this.version.map(p_184696_ -> this.name + ":" + p_184696_).orElse(this.name);
        }
    }

    public static class NativeModuleVersion {

        public final String description;

        public final String version;

        public final String company;

        public NativeModuleVersion(String string0, String string1, String string2) {
            this.description = string0;
            this.version = string1;
            this.company = string2;
        }

        public String toString() {
            return this.description + ":" + this.version + ":" + this.company;
        }
    }
}
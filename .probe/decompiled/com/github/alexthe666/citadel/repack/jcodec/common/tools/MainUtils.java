package com.github.alexthe666.citadel.repack.jcodec.common.tools;

import com.github.alexthe666.citadel.repack.jcodec.common.StringUtils;
import com.github.alexthe666.citadel.repack.jcodec.common.io.IOUtils;
import com.github.alexthe666.citadel.repack.jcodec.platform.Platform;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainUtils {

    private static final String KEY_GIT_REVISION = "git.commit.id.abbrev";

    private static final String JCODEC_LOG_SINK_COLOR = "jcodec.colorPrint";

    private static final String GIT_PROPERTIES = "git.properties";

    public static boolean isColorSupported = System.console() != null || Boolean.parseBoolean(System.getProperty("jcodec.colorPrint"));

    private static Pattern flagPattern = Pattern.compile("^--([^=]+)=(.*)$");

    public static MainUtils.Cmd parseArguments(String[] args, MainUtils.Flag[] flags) {
        Map<String, String> longFlags = new HashMap();
        Map<String, String> shortFlags = new HashMap();
        Map<String, String> allLongFlags = new HashMap();
        Map<String, String> allShortFlags = new HashMap();
        List<String> outArgs = new ArrayList();
        List<Map<String, String>> argLongFlags = new ArrayList();
        List<Map<String, String>> argShortFlags = new ArrayList();
        for (int arg = 0; arg < args.length; arg++) {
            if (args[arg].startsWith("--")) {
                Matcher matcher = flagPattern.matcher(args[arg]);
                if (matcher.matches()) {
                    longFlags.put(matcher.group(1), matcher.group(2));
                } else {
                    longFlags.put(args[arg].substring(2), "true");
                }
            } else if (!args[arg].startsWith("-")) {
                allLongFlags.putAll(longFlags);
                allShortFlags.putAll(shortFlags);
                outArgs.add(args[arg]);
                argLongFlags.add(longFlags);
                argShortFlags.add(shortFlags);
                longFlags = new HashMap();
                shortFlags = new HashMap();
            } else {
                String shortName = args[arg].substring(1);
                boolean found = false;
                for (MainUtils.Flag flag : flags) {
                    if (shortName.equals(flag.getShortName())) {
                        found = true;
                        if (flag.getType() != MainUtils.FlagType.VOID) {
                            shortFlags.put(shortName, args[++arg]);
                        } else {
                            shortFlags.put(shortName, "true");
                        }
                    }
                }
                if (!found) {
                    arg++;
                }
            }
        }
        return new MainUtils.Cmd(allLongFlags, allShortFlags, (String[]) outArgs.toArray(new String[0]), (Map<String, String>[]) argLongFlags.toArray((Map[]) Array.newInstance(longFlags.getClass(), 0)), (Map<String, String>[]) argShortFlags.toArray((Map[]) Array.newInstance(shortFlags.getClass(), 0)));
    }

    public static void printHelpArgs(MainUtils.Flag[] flags, String[] args) {
        printHelpOut(System.out, "", flags, Arrays.asList(args));
    }

    public static void printHelp(MainUtils.Flag[] flags, List<String> params) {
        printHelpOut(System.out, "", flags, params);
    }

    public static void printHelpNoFlags(String... arguments) {
        printHelpOut(System.out, "", new MainUtils.Flag[0], Arrays.asList(arguments));
    }

    public static void printHelpCmdVa(String command, MainUtils.Flag[] flags, String args) {
        printHelpOut(System.out, command, flags, Collections.singletonList(args));
    }

    public static void printHelpCmd(String command, MainUtils.Flag[] flags, List<String> params) {
        printHelpOut(System.out, command, flags, params);
    }

    private static String getGitRevision() {
        InputStream is = null;
        Object properties;
        try {
            is = Thread.currentThread().getContextClassLoader().getResourceAsStream("git.properties");
            if (is != null) {
                Properties propertiesx = new Properties();
                propertiesx.load(is);
                return (String) propertiesx.get("git.commit.id.abbrev");
            }
            properties = null;
        } catch (IOException var6) {
            return null;
        } finally {
            IOUtils.closeQuietly(is);
        }
        return (String) properties;
    }

    public static void printHelpOut(PrintStream out, String command, MainUtils.Flag[] flags, List<String> params) {
        String version = MainUtils.class.getPackage().getImplementationVersion();
        String gitRevision = getGitRevision();
        if (command == null || command.isEmpty()) {
            command = "jcodec";
        }
        if (gitRevision != null || version != null) {
            out.println(command + bold((version != null ? " v." + version : "") + (gitRevision != null ? " rev. " + gitRevision : "")));
            out.println();
        }
        out.print(bold("Syntax: " + command));
        StringBuilder sample = new StringBuilder();
        StringBuilder detail = new StringBuilder();
        for (MainUtils.Flag flag : flags) {
            sample.append(" [");
            detail.append("\t");
            if (flag.getLongName() != null) {
                sample.append(bold(color("--" + flag.getLongName() + "=<value>", MainUtils.ANSIColor.MAGENTA)));
                detail.append(bold(color("--" + flag.getLongName(), MainUtils.ANSIColor.MAGENTA)));
            }
            if (flag.getShortName() != null) {
                if (flag.getLongName() != null) {
                    sample.append(" (");
                    detail.append(" (");
                }
                sample.append(bold(color("-" + flag.getShortName() + " <value>", MainUtils.ANSIColor.MAGENTA)));
                detail.append(bold(color("-" + flag.getShortName(), MainUtils.ANSIColor.MAGENTA)));
                if (flag.getLongName() != null) {
                    sample.append(")");
                    detail.append(")");
                }
            }
            sample.append("]");
            detail.append("\t\t" + flag.getDescription() + "\n");
        }
        for (String param : params) {
            if (param.charAt(0) != '?') {
                sample.append(bold(" <" + param + ">"));
            } else {
                sample.append(bold(" [" + param.substring(1) + "]"));
            }
        }
        out.println(sample);
        out.println(bold("Where:"));
        out.println(detail);
    }

    public static String bold(String str) {
        return isColorSupported ? "\u001b[1m" + str + "\u001b[0m" : str;
    }

    public static String colorString(String str, String placeholder) {
        return isColorSupported ? "\u001b[" + placeholder + "m" + str + "\u001b[0m" : str;
    }

    public static String color(String str, MainUtils.ANSIColor fg) {
        return isColorSupported ? "\u001b[" + (30 + (fg.ordinal() & 7)) + "m" + str + "\u001b[0m" : str;
    }

    public static String colorBright(String str, MainUtils.ANSIColor fg, boolean bright) {
        return isColorSupported ? "\u001b[" + (30 + (fg.ordinal() & 7)) + ";" + (bright ? 1 : 2) + "m" + str + "\u001b[0m" : str;
    }

    public static String color3(String str, MainUtils.ANSIColor fg, MainUtils.ANSIColor bg) {
        return isColorSupported ? "\u001b[" + (30 + (fg.ordinal() & 7)) + ";" + (40 + (bg.ordinal() & 7)) + ";1m" + str + "\u001b[0m" : str;
    }

    public static String color4(String str, MainUtils.ANSIColor fg, MainUtils.ANSIColor bg, boolean bright) {
        return isColorSupported ? "\u001b[" + (30 + (fg.ordinal() & 7)) + ";" + (40 + (bg.ordinal() & 7)) + ";" + (bright ? 1 : 2) + "m" + str + "\u001b[0m" : str;
    }

    public static File tildeExpand(String path) {
        if (path.startsWith("~")) {
            path = path.replaceFirst("~", System.getProperty("user.home"));
        }
        return new File(path);
    }

    public static enum ANSIColor {

        BLACK,
        RED,
        GREEN,
        BROWN,
        BLUE,
        MAGENTA,
        CYAN,
        GREY
    }

    public static class Cmd {

        public Map<String, String> longFlags;

        public Map<String, String> shortFlags;

        public String[] args;

        private Map<String, String>[] longArgFlags;

        private Map<String, String>[] shortArgFlags;

        public Cmd(Map<String, String> longFlags, Map<String, String> shortFlags, String[] args, Map<String, String>[] longArgFlags, Map<String, String>[] shortArgFlags) {
            this.args = args;
            this.longFlags = longFlags;
            this.shortFlags = shortFlags;
            this.longArgFlags = longArgFlags;
            this.shortArgFlags = shortArgFlags;
        }

        private Long getLongFlagInternal(Map<String, String> longFlags, Map<String, String> shortFlags, MainUtils.Flag flag, Long defaultValue) {
            return longFlags.containsKey(flag.getLongName()) ? new Long((String) longFlags.get(flag.getLongName())) : (shortFlags.containsKey(flag.getShortName()) ? new Long((String) shortFlags.get(flag.getShortName())) : defaultValue);
        }

        private Integer getIntegerFlagInternal(Map<String, String> longFlags, Map<String, String> shortFlags, MainUtils.Flag flag, Integer defaultValue) {
            return longFlags.containsKey(flag.getLongName()) ? new Integer((String) longFlags.get(flag.getLongName())) : (shortFlags.containsKey(flag.getShortName()) ? new Integer((String) shortFlags.get(flag.getShortName())) : defaultValue);
        }

        private Boolean getBooleanFlagInternal(Map<String, String> longFlags, Map<String, String> shortFlags, MainUtils.Flag flag, Boolean defaultValue) {
            return longFlags.containsKey(flag.getLongName()) ? !"false".equalsIgnoreCase((String) longFlags.get(flag.getLongName())) : (shortFlags.containsKey(flag.getShortName()) ? !"false".equalsIgnoreCase((String) shortFlags.get(flag.getShortName())) : defaultValue);
        }

        private Double getDoubleFlagInternal(Map<String, String> longFlags, Map<String, String> shortFlags, MainUtils.Flag flag, Double defaultValue) {
            return longFlags.containsKey(flag.getLongName()) ? new Double((String) longFlags.get(flag.getLongName())) : (shortFlags.containsKey(flag.getShortName()) ? new Double((String) shortFlags.get(flag.getShortName())) : defaultValue);
        }

        private String getStringFlagInternal(Map<String, String> longFlags, Map<String, String> shortFlags, MainUtils.Flag flag, String defaultValue) {
            return longFlags.containsKey(flag.getLongName()) ? (String) longFlags.get(flag.getLongName()) : (shortFlags.containsKey(flag.getShortName()) ? (String) shortFlags.get(flag.getShortName()) : defaultValue);
        }

        private int[] getMultiIntegerFlagInternal(Map<String, String> longFlags, Map<String, String> shortFlags, MainUtils.Flag flag, int[] defaultValue) {
            String flagValue;
            if (longFlags.containsKey(flag.getLongName())) {
                flagValue = (String) longFlags.get(flag.getLongName());
            } else {
                if (!shortFlags.containsKey(flag.getShortName())) {
                    return defaultValue;
                }
                flagValue = (String) shortFlags.get(flag.getShortName());
            }
            String[] split = StringUtils.splitS(flagValue, ",");
            int[] result = new int[split.length];
            for (int i = 0; i < split.length; i++) {
                result[i] = Integer.parseInt(split[i]);
            }
            return result;
        }

        private <T extends Enum<T>> T getEnumFlagInternal(Map<String, String> longFlags, Map<String, String> shortFlags, MainUtils.Flag flag, T defaultValue, Class<T> class1) {
            String flagValue;
            if (longFlags.containsKey(flag.getLongName())) {
                flagValue = (String) longFlags.get(flag.getLongName());
            } else {
                if (!shortFlags.containsKey(flag.getShortName())) {
                    return defaultValue;
                }
                flagValue = (String) shortFlags.get(flag.getShortName());
            }
            String strVal = flagValue.toLowerCase();
            for (T val : EnumSet.allOf(class1)) {
                if (val.name().toLowerCase().equals(strVal)) {
                    return val;
                }
            }
            return null;
        }

        public Long getLongFlagD(MainUtils.Flag flagName, Long defaultValue) {
            return this.getLongFlagInternal(this.longFlags, this.shortFlags, flagName, defaultValue);
        }

        public Long getLongFlag(MainUtils.Flag flagName) {
            return this.getLongFlagInternal(this.longFlags, this.shortFlags, flagName, null);
        }

        public Long getLongFlagID(int arg, MainUtils.Flag flagName, Long defaultValue) {
            return this.getLongFlagInternal(this.longArgFlags[arg], this.shortArgFlags[arg], flagName, defaultValue);
        }

        public Long getLongFlagI(int arg, MainUtils.Flag flagName) {
            return this.getLongFlagInternal(this.longArgFlags[arg], this.shortArgFlags[arg], flagName, null);
        }

        public Integer getIntegerFlagD(MainUtils.Flag flagName, Integer defaultValue) {
            return this.getIntegerFlagInternal(this.longFlags, this.shortFlags, flagName, defaultValue);
        }

        public Integer getIntegerFlag(MainUtils.Flag flagName) {
            return this.getIntegerFlagInternal(this.longFlags, this.shortFlags, flagName, null);
        }

        public Integer getIntegerFlagID(int arg, MainUtils.Flag flagName, Integer defaultValue) {
            return this.getIntegerFlagInternal(this.longArgFlags[arg], this.shortArgFlags[arg], flagName, defaultValue);
        }

        public Integer getIntegerFlagI(int arg, MainUtils.Flag flagName) {
            return this.getIntegerFlagInternal(this.longArgFlags[arg], this.shortArgFlags[arg], flagName, null);
        }

        public Boolean getBooleanFlagD(MainUtils.Flag flagName, Boolean defaultValue) {
            return this.getBooleanFlagInternal(this.longFlags, this.shortFlags, flagName, defaultValue);
        }

        public Boolean getBooleanFlag(MainUtils.Flag flagName) {
            return this.getBooleanFlagInternal(this.longFlags, this.shortFlags, flagName, false);
        }

        public Boolean getBooleanFlagID(int arg, MainUtils.Flag flagName, Boolean defaultValue) {
            return this.getBooleanFlagInternal(this.longArgFlags[arg], this.shortArgFlags[arg], flagName, defaultValue);
        }

        public Boolean getBooleanFlagI(int arg, MainUtils.Flag flagName) {
            return this.getBooleanFlagInternal(this.longArgFlags[arg], this.shortArgFlags[arg], flagName, false);
        }

        public Double getDoubleFlagD(MainUtils.Flag flagName, Double defaultValue) {
            return this.getDoubleFlagInternal(this.longFlags, this.shortFlags, flagName, defaultValue);
        }

        public Double getDoubleFlag(MainUtils.Flag flagName) {
            return this.getDoubleFlagInternal(this.longFlags, this.shortFlags, flagName, null);
        }

        public Double getDoubleFlagID(int arg, MainUtils.Flag flagName, Double defaultValue) {
            return this.getDoubleFlagInternal(this.longArgFlags[arg], this.shortArgFlags[arg], flagName, defaultValue);
        }

        public Double getDoubleFlagI(int arg, MainUtils.Flag flagName) {
            return this.getDoubleFlagInternal(this.longArgFlags[arg], this.shortArgFlags[arg], flagName, null);
        }

        public String getStringFlagD(MainUtils.Flag flagName, String defaultValue) {
            return this.getStringFlagInternal(this.longFlags, this.shortFlags, flagName, defaultValue);
        }

        public String getStringFlag(MainUtils.Flag flagName) {
            return this.getStringFlagInternal(this.longFlags, this.shortFlags, flagName, null);
        }

        public String getStringFlagID(int arg, MainUtils.Flag flagName, String defaultValue) {
            return this.getStringFlagInternal(this.longArgFlags[arg], this.shortArgFlags[arg], flagName, defaultValue);
        }

        public String getStringFlagI(int arg, MainUtils.Flag flagName) {
            return this.getStringFlagInternal(this.longArgFlags[arg], this.shortArgFlags[arg], flagName, null);
        }

        public int[] getMultiIntegerFlagD(MainUtils.Flag flagName, int[] defaultValue) {
            return this.getMultiIntegerFlagInternal(this.longFlags, this.shortFlags, flagName, defaultValue);
        }

        public int[] getMultiIntegerFlag(MainUtils.Flag flagName) {
            return this.getMultiIntegerFlagInternal(this.longFlags, this.shortFlags, flagName, new int[0]);
        }

        public int[] getMultiIntegerFlagID(int arg, MainUtils.Flag flagName, int[] defaultValue) {
            return this.getMultiIntegerFlagInternal(this.longArgFlags[arg], this.shortArgFlags[arg], flagName, defaultValue);
        }

        public int[] getMultiIntegerFlagI(int arg, MainUtils.Flag flagName) {
            return this.getMultiIntegerFlagInternal(this.longArgFlags[arg], this.shortArgFlags[arg], flagName, new int[0]);
        }

        public <T extends Enum<T>> T getEnumFlagD(MainUtils.Flag flagName, T defaultValue, Class<T> class1) {
            return this.getEnumFlagInternal(this.longFlags, this.shortFlags, flagName, defaultValue, class1);
        }

        public <T extends Enum<T>> T getEnumFlag(MainUtils.Flag flagName, Class<T> class1) {
            return this.getEnumFlagInternal(this.longFlags, this.shortFlags, flagName, null, class1);
        }

        public <T extends Enum<T>> T getEnumFlagID(int arg, MainUtils.Flag flagName, T defaultValue, Class<T> class1) {
            return this.getEnumFlagInternal(this.longArgFlags[arg], this.shortArgFlags[arg], flagName, defaultValue, class1);
        }

        public <T extends Enum<T>> T getEnumFlagI(int arg, MainUtils.Flag flagName, Class<T> class1) {
            return this.getEnumFlagInternal(this.longArgFlags[arg], this.shortArgFlags[arg], flagName, null, class1);
        }

        public String getArg(int i) {
            return i < this.args.length ? this.args[i] : null;
        }

        public int argsLength() {
            return this.args.length;
        }

        public void popArg() {
            this.args = Platform.copyOfRangeO(this.args, 1, this.args.length);
        }
    }

    public static class Flag {

        private String longName;

        private String shortName;

        private String description;

        private MainUtils.FlagType type;

        public Flag(String longName, String shortName, String description, MainUtils.FlagType type) {
            this.longName = longName;
            this.shortName = shortName;
            this.description = description;
            this.type = type;
        }

        public static MainUtils.Flag flag(String longName, String shortName, String description) {
            return new MainUtils.Flag(longName, shortName, description, MainUtils.FlagType.ANY);
        }

        public String getLongName() {
            return this.longName;
        }

        public String getDescription() {
            return this.description;
        }

        public String getShortName() {
            return this.shortName;
        }

        public MainUtils.FlagType getType() {
            return this.type;
        }
    }

    public static enum FlagType {

        VOID,
        STRING,
        INT,
        LONG,
        DOUBLE,
        MULT,
        ENUM,
        ANY
    }
}
package me.lucko.spark.common.command;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Arguments {

    private static final Pattern FLAG_REGEX = Pattern.compile("^--(.+)$");

    private final List<String> rawArgs;

    private final SetMultimap<String, String> parsedArgs;

    private String parsedSubCommand = null;

    public Arguments(List<String> rawArgs, boolean allowSubCommand) {
        this.rawArgs = rawArgs;
        this.parsedArgs = HashMultimap.create();
        String flag = null;
        List<String> value = null;
        for (int i = 0; i < this.rawArgs.size(); i++) {
            String arg = (String) this.rawArgs.get(i);
            Matcher matcher = FLAG_REGEX.matcher(arg);
            boolean matches = matcher.matches();
            if (i == 0 && allowSubCommand && !matches) {
                this.parsedSubCommand = arg;
            } else if (flag != null && !matches) {
                value.add(arg);
            } else {
                if (!matches) {
                    throw new Arguments.ParseException("Expected flag at position " + i + " but got '" + arg + "' instead!");
                }
                if (flag != null) {
                    this.parsedArgs.put(flag, String.join(" ", value));
                }
                flag = matcher.group(1).toLowerCase();
                value = new ArrayList();
            }
        }
        if (flag != null) {
            this.parsedArgs.put(flag, String.join(" ", value));
        }
    }

    public List<String> raw() {
        return this.rawArgs;
    }

    public String subCommand() {
        return this.parsedSubCommand;
    }

    public int intFlag(String key) {
        Iterator<String> it = this.parsedArgs.get(key).iterator();
        if (it.hasNext()) {
            try {
                return Math.abs(Integer.parseInt((String) it.next()));
            } catch (NumberFormatException var4) {
                throw new Arguments.ParseException("Invalid input for '" + key + "' argument. Please specify a number!");
            }
        } else {
            return -1;
        }
    }

    public double doubleFlag(String key) {
        Iterator<String> it = this.parsedArgs.get(key).iterator();
        if (it.hasNext()) {
            try {
                return Math.abs(Double.parseDouble((String) it.next()));
            } catch (NumberFormatException var4) {
                throw new Arguments.ParseException("Invalid input for '" + key + "' argument. Please specify a number!");
            }
        } else {
            return -1.0;
        }
    }

    public Set<String> stringFlag(String key) {
        return this.parsedArgs.get(key);
    }

    public boolean boolFlag(String key) {
        return this.parsedArgs.containsKey(key);
    }

    public static final class ParseException extends IllegalArgumentException {

        public ParseException(String s) {
            super(s);
        }
    }
}
package de.keksuccino.konkrete.json.jsonpath.internal.filter;

public enum PatternFlag {

    UNIX_LINES(1, 'd'),
    CASE_INSENSITIVE(2, 'i'),
    COMMENTS(4, 'x'),
    MULTILINE(8, 'm'),
    DOTALL(32, 's'),
    UNICODE_CASE(64, 'u'),
    UNICODE_CHARACTER_CLASS(256, 'U');

    private final int code;

    private final char flag;

    private PatternFlag(int code, char flag) {
        this.code = code;
        this.flag = flag;
    }

    public static int parseFlags(char[] flags) {
        int flagsValue = 0;
        for (char flag : flags) {
            flagsValue |= getCodeByFlag(flag);
        }
        return flagsValue;
    }

    public static String parseFlags(int flags) {
        StringBuilder builder = new StringBuilder();
        for (PatternFlag patternFlag : values()) {
            if ((patternFlag.code & flags) == patternFlag.code) {
                builder.append(patternFlag.flag);
            }
        }
        return builder.toString();
    }

    private static int getCodeByFlag(char flag) {
        for (PatternFlag patternFlag : values()) {
            if (patternFlag.flag == flag) {
                return patternFlag.code;
            }
        }
        return 0;
    }
}
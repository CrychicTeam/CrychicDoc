package de.keksuccino.konkrete.json.jsonpath.internal;

public class JsonFormatter {

    private static final String INDENT = "   ";

    private static final String NEW_LINE = System.getProperty("line.separator");

    private static final int MODE_SINGLE = 100;

    private static final int MODE_DOUBLE = 101;

    private static final int MODE_ESCAPE_SINGLE = 102;

    private static final int MODE_ESCAPE_DOUBLE = 103;

    private static final int MODE_BETWEEN = 104;

    private static void appendIndent(StringBuilder sb, int count) {
        while (count > 0) {
            sb.append("   ");
            count--;
        }
    }

    public static String prettyPrint(String input) {
        input = input.replaceAll("[\\r\\n]", "");
        StringBuilder output = new StringBuilder(input.length() * 2);
        int mode = 104;
        int depth = 0;
        for (int i = 0; i < input.length(); i++) {
            char ch = input.charAt(i);
            switch(mode) {
                case 100:
                    output.append(ch);
                    switch(ch) {
                        case '\'':
                            mode = 104;
                            continue;
                        case '\\':
                            mode = 102;
                        default:
                            continue;
                    }
                case 101:
                    output.append(ch);
                    switch(ch) {
                        case '"':
                            mode = 104;
                            continue;
                        case '\\':
                            mode = 103;
                        default:
                            continue;
                    }
                case 102:
                    output.append(ch);
                    mode = 100;
                    break;
                case 103:
                    output.append(ch);
                    mode = 101;
                    break;
                case 104:
                    switch(ch) {
                        case ' ':
                            break;
                        case '"':
                            output.append(ch);
                            mode = 101;
                            break;
                        case '\'':
                            output.append(ch);
                            mode = 100;
                            break;
                        case ',':
                            output.append(ch);
                            output.append(NEW_LINE);
                            appendIndent(output, depth);
                            break;
                        case ':':
                            output.append(" : ");
                            break;
                        case '[':
                        case '{':
                            output.append(ch);
                            output.append(NEW_LINE);
                            appendIndent(output, ++depth);
                            break;
                        case ']':
                        case '}':
                            output.append(NEW_LINE);
                            appendIndent(output, --depth);
                            output.append(ch);
                            break;
                        default:
                            output.append(ch);
                    }
            }
        }
        return output.toString();
    }
}
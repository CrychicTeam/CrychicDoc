package de.keksuccino.konkrete.json.minidev.json;

import java.io.IOException;

class JStylerObj {

    public static final JStylerObj.MPSimple MP_SIMPLE = new JStylerObj.MPSimple();

    public static final JStylerObj.MPTrue MP_TRUE = new JStylerObj.MPTrue();

    public static final JStylerObj.MPAgressive MP_AGGRESIVE = new JStylerObj.MPAgressive();

    public static final JStylerObj.EscapeLT ESCAPE_LT = new JStylerObj.EscapeLT();

    public static final JStylerObj.Escape4Web ESCAPE4Web = new JStylerObj.Escape4Web();

    public static boolean isSpace(char c) {
        return c == '\r' || c == '\n' || c == '\t' || c == ' ';
    }

    public static boolean isSpecialChar(char c) {
        return c == '\b' || c == '\f' || c == '\n';
    }

    public static boolean isSpecialOpen(char c) {
        return c == '{' || c == '[' || c == ',' || c == ':';
    }

    public static boolean isSpecialClose(char c) {
        return c == '}' || c == ']' || c == ',' || c == ':';
    }

    public static boolean isSpecial(char c) {
        return c == '{' || c == '[' || c == ',' || c == '}' || c == ']' || c == ':' || c == '\'' || c == '"';
    }

    public static boolean isUnicode(char c) {
        return c >= 0 && c <= 31 || c >= 127 && c <= 159 || c >= 8192 && c <= 8447;
    }

    public static boolean isKeyword(String s) {
        if (s.length() < 3) {
            return false;
        } else {
            char c = s.charAt(0);
            if (c == 'n') {
                return s.equals("null");
            } else if (c == 't') {
                return s.equals("true");
            } else if (c == 'f') {
                return s.equals("false");
            } else {
                return c == 'N' ? s.equals("NaN") : false;
            }
        }
    }

    private static class Escape4Web implements JStylerObj.StringProtector {

        @Override
        public void escape(String s, Appendable sb) {
            try {
                int len = s.length();
                for (int i = 0; i < len; i++) {
                    char ch = s.charAt(i);
                    switch(ch) {
                        case '\b':
                            sb.append("\\b");
                            break;
                        case '\t':
                            sb.append("\\t");
                            break;
                        case '\n':
                            sb.append("\\n");
                            break;
                        case '\f':
                            sb.append("\\f");
                            break;
                        case '\r':
                            sb.append("\\r");
                            break;
                        case '"':
                            sb.append("\\\"");
                            break;
                        case '/':
                            sb.append("\\/");
                            break;
                        case '\\':
                            sb.append("\\\\");
                            break;
                        default:
                            if ((ch < 0 || ch > 31) && (ch < 127 || ch > 159) && (ch < 8192 || ch > 8447)) {
                                sb.append(ch);
                            } else {
                                sb.append("\\u");
                                String hex = "0123456789ABCDEF";
                                sb.append(hex.charAt(ch >> '\f' & 15));
                                sb.append(hex.charAt(ch >> '\b' & 15));
                                sb.append(hex.charAt(ch >> 4 & 15));
                                sb.append(hex.charAt(ch >> 0 & 15));
                            }
                    }
                }
            } catch (IOException var7) {
                throw new RuntimeException("Impossible Error");
            }
        }
    }

    private static class EscapeLT implements JStylerObj.StringProtector {

        @Override
        public void escape(String s, Appendable out) {
            try {
                int len = s.length();
                for (int i = 0; i < len; i++) {
                    char ch = s.charAt(i);
                    switch(ch) {
                        case '\b':
                            out.append("\\b");
                            break;
                        case '\t':
                            out.append("\\t");
                            break;
                        case '\n':
                            out.append("\\n");
                            break;
                        case '\f':
                            out.append("\\f");
                            break;
                        case '\r':
                            out.append("\\r");
                            break;
                        case '"':
                            out.append("\\\"");
                            break;
                        case '\\':
                            out.append("\\\\");
                            break;
                        default:
                            if ((ch < 0 || ch > 31) && (ch < 127 || ch > 159) && (ch < 8192 || ch > 8447)) {
                                out.append(ch);
                            } else {
                                out.append("\\u");
                                String hex = "0123456789ABCDEF";
                                out.append(hex.charAt(ch >> '\f' & 15));
                                out.append(hex.charAt(ch >> '\b' & 15));
                                out.append(hex.charAt(ch >> 4 & 15));
                                out.append(hex.charAt(ch >> 0 & 15));
                            }
                    }
                }
            } catch (IOException var7) {
                throw new RuntimeException("Impossible Exception");
            }
        }
    }

    private static class MPAgressive implements JStylerObj.MustProtect {

        @Override
        public boolean mustBeProtect(String s) {
            if (s == null) {
                return false;
            } else {
                int len = s.length();
                if (len == 0) {
                    return true;
                } else if (s.trim() != s) {
                    return true;
                } else {
                    char ch = s.charAt(0);
                    if (!JStylerObj.isSpecial(ch) && !JStylerObj.isUnicode(ch)) {
                        for (int i = 1; i < len; i++) {
                            ch = s.charAt(i);
                            if (JStylerObj.isSpecialClose(ch) || JStylerObj.isUnicode(ch)) {
                                return true;
                            }
                        }
                        if (JStylerObj.isKeyword(s)) {
                            return true;
                        } else {
                            ch = s.charAt(0);
                            if ((ch < '0' || ch > '9') && ch != '-') {
                                return false;
                            } else {
                                int p;
                                for (p = 1; p < len; p++) {
                                    ch = s.charAt(p);
                                    if (ch < '0' || ch > '9') {
                                        break;
                                    }
                                }
                                if (p == len) {
                                    return true;
                                } else {
                                    if (ch == '.') {
                                        p++;
                                    }
                                    while (p < len) {
                                        ch = s.charAt(p);
                                        if (ch < '0' || ch > '9') {
                                            break;
                                        }
                                        p++;
                                    }
                                    if (p == len) {
                                        return true;
                                    } else {
                                        if (ch == 'E' || ch == 'e') {
                                            if (++p == len) {
                                                return false;
                                            }
                                            ch = s.charAt(p);
                                            if (ch == '+' || ch == '-') {
                                                ch = s.charAt(++p);
                                            }
                                        }
                                        if (p == len) {
                                            return false;
                                        } else {
                                            while (p < len) {
                                                ch = s.charAt(p);
                                                if (ch < '0' || ch > '9') {
                                                    break;
                                                }
                                                p++;
                                            }
                                            return p == len;
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        return true;
                    }
                }
            }
        }
    }

    private static class MPSimple implements JStylerObj.MustProtect {

        @Override
        public boolean mustBeProtect(String s) {
            if (s == null) {
                return false;
            } else {
                int len = s.length();
                if (len == 0) {
                    return true;
                } else if (s.trim() != s) {
                    return true;
                } else {
                    char ch = s.charAt(0);
                    if ((ch < '0' || ch > '9') && ch != '-') {
                        for (int i = 0; i < len; i++) {
                            ch = s.charAt(i);
                            if (JStylerObj.isSpace(ch)) {
                                return true;
                            }
                            if (JStylerObj.isSpecial(ch)) {
                                return true;
                            }
                            if (JStylerObj.isSpecialChar(ch)) {
                                return true;
                            }
                            if (JStylerObj.isUnicode(ch)) {
                                return true;
                            }
                        }
                        return JStylerObj.isKeyword(s);
                    } else {
                        return true;
                    }
                }
            }
        }
    }

    private static class MPTrue implements JStylerObj.MustProtect {

        @Override
        public boolean mustBeProtect(String s) {
            return true;
        }
    }

    public interface MustProtect {

        boolean mustBeProtect(String var1);
    }

    public interface StringProtector {

        void escape(String var1, Appendable var2);
    }
}
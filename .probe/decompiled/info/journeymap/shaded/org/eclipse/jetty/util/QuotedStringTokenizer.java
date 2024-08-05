package info.journeymap.shaded.org.eclipse.jetty.util;

import java.io.IOException;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

public class QuotedStringTokenizer extends StringTokenizer {

    private static final String __delim = "\t\n\r";

    private String _string;

    private String _delim = "\t\n\r";

    private boolean _returnQuotes = false;

    private boolean _returnDelimiters = false;

    private StringBuffer _token;

    private boolean _hasToken = false;

    private int _i = 0;

    private int _lastStart = 0;

    private boolean _double = true;

    private boolean _single = true;

    private static final char[] escapes = new char[32];

    public QuotedStringTokenizer(String str, String delim, boolean returnDelimiters, boolean returnQuotes) {
        super("");
        this._string = str;
        if (delim != null) {
            this._delim = delim;
        }
        this._returnDelimiters = returnDelimiters;
        this._returnQuotes = returnQuotes;
        if (this._delim.indexOf(39) < 0 && this._delim.indexOf(34) < 0) {
            this._token = new StringBuffer(this._string.length() > 1024 ? 512 : this._string.length() / 2);
        } else {
            throw new Error("Can't use quotes as delimiters: " + this._delim);
        }
    }

    public QuotedStringTokenizer(String str, String delim, boolean returnDelimiters) {
        this(str, delim, returnDelimiters, false);
    }

    public QuotedStringTokenizer(String str, String delim) {
        this(str, delim, false, false);
    }

    public QuotedStringTokenizer(String str) {
        this(str, null, false, false);
    }

    public boolean hasMoreTokens() {
        if (this._hasToken) {
            return true;
        } else {
            this._lastStart = this._i;
            int state = 0;
            boolean escape = false;
            while (this._i < this._string.length()) {
                char c = this._string.charAt(this._i++);
                switch(state) {
                    case 0:
                        if (this._delim.indexOf(c) >= 0) {
                            if (this._returnDelimiters) {
                                this._token.append(c);
                                return this._hasToken = true;
                            }
                        } else if (c == '\'' && this._single) {
                            if (this._returnQuotes) {
                                this._token.append(c);
                            }
                            state = 2;
                        } else {
                            if (c == '"' && this._double) {
                                if (this._returnQuotes) {
                                    this._token.append(c);
                                }
                                state = 3;
                                continue;
                            }
                            this._token.append(c);
                            this._hasToken = true;
                            state = 1;
                        }
                        break;
                    case 1:
                        this._hasToken = true;
                        if (this._delim.indexOf(c) >= 0) {
                            if (this._returnDelimiters) {
                                this._i--;
                            }
                            return this._hasToken;
                        }
                        if (c == '\'' && this._single) {
                            if (this._returnQuotes) {
                                this._token.append(c);
                            }
                            state = 2;
                        } else {
                            if (c == '"' && this._double) {
                                if (this._returnQuotes) {
                                    this._token.append(c);
                                }
                                state = 3;
                                break;
                            }
                            this._token.append(c);
                        }
                        break;
                    case 2:
                        this._hasToken = true;
                        if (escape) {
                            escape = false;
                            this._token.append(c);
                        } else if (c == '\'') {
                            if (this._returnQuotes) {
                                this._token.append(c);
                            }
                            state = 1;
                        } else if (c == '\\') {
                            if (this._returnQuotes) {
                                this._token.append(c);
                            }
                            escape = true;
                        } else {
                            this._token.append(c);
                        }
                        break;
                    case 3:
                        this._hasToken = true;
                        if (escape) {
                            escape = false;
                            this._token.append(c);
                        } else if (c == '"') {
                            if (this._returnQuotes) {
                                this._token.append(c);
                            }
                            state = 1;
                        } else if (c == '\\') {
                            if (this._returnQuotes) {
                                this._token.append(c);
                            }
                            escape = true;
                        } else {
                            this._token.append(c);
                        }
                }
            }
            return this._hasToken;
        }
    }

    public String nextToken() throws NoSuchElementException {
        if (this.hasMoreTokens() && this._token != null) {
            String t = this._token.toString();
            this._token.setLength(0);
            this._hasToken = false;
            return t;
        } else {
            throw new NoSuchElementException();
        }
    }

    public String nextToken(String delim) throws NoSuchElementException {
        this._delim = delim;
        this._i = this._lastStart;
        this._token.setLength(0);
        this._hasToken = false;
        return this.nextToken();
    }

    public boolean hasMoreElements() {
        return this.hasMoreTokens();
    }

    public Object nextElement() throws NoSuchElementException {
        return this.nextToken();
    }

    public int countTokens() {
        return -1;
    }

    public static String quoteIfNeeded(String s, String delim) {
        if (s == null) {
            return null;
        } else if (s.length() == 0) {
            return "\"\"";
        } else {
            for (int i = 0; i < s.length(); i++) {
                char c = s.charAt(i);
                if (c == '\\' || c == '"' || c == '\'' || Character.isWhitespace(c) || delim.indexOf(c) >= 0) {
                    StringBuffer b = new StringBuffer(s.length() + 8);
                    quote(b, s);
                    return b.toString();
                }
            }
            return s;
        }
    }

    public static String quote(String s) {
        if (s == null) {
            return null;
        } else if (s.length() == 0) {
            return "\"\"";
        } else {
            StringBuffer b = new StringBuffer(s.length() + 8);
            quote(b, s);
            return b.toString();
        }
    }

    public static void quoteOnly(Appendable buffer, String input) {
        if (input != null) {
            try {
                buffer.append('"');
                for (int i = 0; i < input.length(); i++) {
                    char c = input.charAt(i);
                    if (c == '"' || c == '\\') {
                        buffer.append('\\');
                    }
                    buffer.append(c);
                }
                buffer.append('"');
            } catch (IOException var4) {
                throw new RuntimeException(var4);
            }
        }
    }

    public static void quote(Appendable buffer, String input) {
        if (input != null) {
            try {
                buffer.append('"');
                for (int i = 0; i < input.length(); i++) {
                    char c = input.charAt(i);
                    if (c >= ' ') {
                        if (c == '"' || c == '\\') {
                            buffer.append('\\');
                        }
                        buffer.append(c);
                    } else {
                        char escape = escapes[c];
                        if (escape == '\uffff') {
                            buffer.append('\\').append('u').append('0').append('0');
                            if (c < 16) {
                                buffer.append('0');
                            }
                            buffer.append(Integer.toString(c, 16));
                        } else {
                            buffer.append('\\').append(escape);
                        }
                    }
                }
                buffer.append('"');
            } catch (IOException var5) {
                throw new RuntimeException(var5);
            }
        }
    }

    public static String unquoteOnly(String s) {
        return unquoteOnly(s, false);
    }

    public static String unquoteOnly(String s, boolean lenient) {
        if (s == null) {
            return null;
        } else if (s.length() < 2) {
            return s;
        } else {
            char first = s.charAt(0);
            char last = s.charAt(s.length() - 1);
            if (first == last && (first == '"' || first == '\'')) {
                StringBuilder b = new StringBuilder(s.length() - 2);
                boolean escape = false;
                for (int i = 1; i < s.length() - 1; i++) {
                    char c = s.charAt(i);
                    if (escape) {
                        escape = false;
                        if (lenient && !isValidEscaping(c)) {
                            b.append('\\');
                        }
                        b.append(c);
                    } else if (c == '\\') {
                        escape = true;
                    } else {
                        b.append(c);
                    }
                }
                return b.toString();
            } else {
                return s;
            }
        }
    }

    public static String unquote(String s) {
        return unquote(s, false);
    }

    public static String unquote(String s, boolean lenient) {
        if (s == null) {
            return null;
        } else if (s.length() < 2) {
            return s;
        } else {
            char first = s.charAt(0);
            char last = s.charAt(s.length() - 1);
            if (first == last && (first == '"' || first == '\'')) {
                StringBuilder b = new StringBuilder(s.length() - 2);
                boolean escape = false;
                for (int i = 1; i < s.length() - 1; i++) {
                    char c = s.charAt(i);
                    if (escape) {
                        escape = false;
                        switch(c) {
                            case '"':
                                b.append('"');
                                break;
                            case '/':
                                b.append('/');
                                break;
                            case '\\':
                                b.append('\\');
                                break;
                            case 'b':
                                b.append('\b');
                                break;
                            case 'f':
                                b.append('\f');
                                break;
                            case 'n':
                                b.append('\n');
                                break;
                            case 'r':
                                b.append('\r');
                                break;
                            case 't':
                                b.append('\t');
                                break;
                            case 'u':
                                b.append((char) ((TypeUtil.convertHexDigit((byte) s.charAt(i++)) << 24) + (TypeUtil.convertHexDigit((byte) s.charAt(i++)) << 16) + (TypeUtil.convertHexDigit((byte) s.charAt(i++)) << 8) + TypeUtil.convertHexDigit((byte) s.charAt(i++))));
                                break;
                            default:
                                if (lenient && !isValidEscaping(c)) {
                                    b.append('\\');
                                }
                                b.append(c);
                        }
                    } else if (c == '\\') {
                        escape = true;
                    } else {
                        b.append(c);
                    }
                }
                return b.toString();
            } else {
                return s;
            }
        }
    }

    private static boolean isValidEscaping(char c) {
        return c == 'n' || c == 'r' || c == 't' || c == 'f' || c == 'b' || c == '\\' || c == '/' || c == '"' || c == 'u';
    }

    public static boolean isQuoted(String s) {
        return s != null && s.length() > 0 && s.charAt(0) == '"' && s.charAt(s.length() - 1) == '"';
    }

    public boolean getDouble() {
        return this._double;
    }

    public void setDouble(boolean d) {
        this._double = d;
    }

    public boolean getSingle() {
        return this._single;
    }

    public void setSingle(boolean single) {
        this._single = single;
    }

    static {
        Arrays.fill(escapes, '\uffff');
        escapes[8] = 'b';
        escapes[9] = 't';
        escapes[10] = 'n';
        escapes[12] = 'f';
        escapes[13] = 'r';
    }
}
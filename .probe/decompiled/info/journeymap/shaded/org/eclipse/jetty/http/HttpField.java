package info.journeymap.shaded.org.eclipse.jetty.http;

import info.journeymap.shaded.org.eclipse.jetty.util.StringUtil;
import java.util.Objects;

public class HttpField {

    private static final String __zeroquality = "q=0";

    private final HttpHeader _header;

    private final String _name;

    private final String _value;

    private int hash = 0;

    public HttpField(HttpHeader header, String name, String value) {
        this._header = header;
        this._name = name;
        this._value = value;
    }

    public HttpField(HttpHeader header, String value) {
        this(header, header.asString(), value);
    }

    public HttpField(HttpHeader header, HttpHeaderValue value) {
        this(header, header.asString(), value.asString());
    }

    public HttpField(String name, String value) {
        this(HttpHeader.CACHE.get(name), name, value);
    }

    public HttpHeader getHeader() {
        return this._header;
    }

    public String getName() {
        return this._name;
    }

    public String getValue() {
        return this._value;
    }

    public int getIntValue() {
        return Integer.valueOf(this._value);
    }

    public long getLongValue() {
        return Long.valueOf(this._value);
    }

    public String[] getValues() {
        if (this._value == null) {
            return null;
        } else {
            QuotedCSV list = new QuotedCSV(false, this._value);
            return (String[]) list.getValues().toArray(new String[list.size()]);
        }
    }

    public boolean contains(String search) {
        if (search == null) {
            return this._value == null;
        } else if (search.length() == 0) {
            return false;
        } else if (this._value == null) {
            return false;
        } else if (search.equals(this._value)) {
            return true;
        } else {
            search = StringUtil.asciiToLowerCase(search);
            int state = 0;
            int match = 0;
            int param = 0;
            for (int i = 0; i < this._value.length(); i++) {
                char c = this._value.charAt(i);
                switch(state) {
                    case 0:
                        switch(c) {
                            case '\t':
                            case ' ':
                            case ',':
                                continue;
                            case '"':
                                match = 0;
                                state = 2;
                                continue;
                            case ';':
                                param = -1;
                                match = -1;
                                state = 5;
                                continue;
                            default:
                                match = Character.toLowerCase(c) == search.charAt(0) ? 1 : -1;
                                state = 1;
                                continue;
                        }
                    case 1:
                        switch(c) {
                            case ',':
                                if (match == search.length()) {
                                    return true;
                                }
                                state = 0;
                                continue;
                            case ';':
                                param = match >= 0 ? 0 : -1;
                                state = 5;
                                continue;
                            default:
                                if (match > 0) {
                                    if (match < search.length()) {
                                        match = Character.toLowerCase(c) == search.charAt(match) ? match + 1 : -1;
                                    } else if (c != ' ' && c != '\t') {
                                        match = -1;
                                    }
                                }
                                continue;
                        }
                    case 2:
                        switch(c) {
                            case '"':
                                state = 4;
                                continue;
                            case '\\':
                                state = 3;
                                continue;
                            default:
                                if (match >= 0) {
                                    if (match < search.length()) {
                                        match = Character.toLowerCase(c) == search.charAt(match) ? match + 1 : -1;
                                    } else {
                                        match = -1;
                                    }
                                }
                                continue;
                        }
                    case 3:
                        if (match >= 0) {
                            if (match < search.length()) {
                                match = Character.toLowerCase(c) == search.charAt(match) ? match + 1 : -1;
                            } else {
                                match = -1;
                            }
                        }
                        state = 2;
                        break;
                    case 4:
                        switch(c) {
                            case '\t':
                            case ' ':
                                continue;
                            case ',':
                                if (match == search.length()) {
                                    return true;
                                }
                                state = 0;
                                continue;
                            case ';':
                                state = 5;
                                continue;
                            default:
                                match = -1;
                                continue;
                        }
                    case 5:
                        switch(c) {
                            case '\t':
                            case ' ':
                                continue;
                            case ',':
                                if (param != "q=0".length() && match == search.length()) {
                                    return true;
                                }
                                param = 0;
                                state = 0;
                                continue;
                            default:
                                if (param >= 0) {
                                    if (param < "q=0".length()) {
                                        param = Character.toLowerCase(c) == "q=0".charAt(param) ? param + 1 : -1;
                                    } else if (c != '0' && c != '.') {
                                        param = -1;
                                    }
                                }
                                continue;
                        }
                    default:
                        throw new IllegalStateException();
                }
            }
            return param != "q=0".length() && match == search.length();
        }
    }

    public String toString() {
        String v = this.getValue();
        return this.getName() + ": " + (v == null ? "" : v);
    }

    public boolean isSameName(HttpField field) {
        if (field == null) {
            return false;
        } else if (field == this) {
            return true;
        } else {
            return this._header != null && this._header == field.getHeader() ? true : this._name.equalsIgnoreCase(field.getName());
        }
    }

    private int nameHashCode() {
        int h = this.hash;
        int len = this._name.length();
        if (h == 0 && len > 0) {
            for (int i = 0; i < len; i++) {
                char c = this._name.charAt(i);
                if (c >= 'a' && c <= 'z') {
                    c = (char) (c - ' ');
                }
                h = 31 * h + c;
            }
            this.hash = h;
        }
        return h;
    }

    public int hashCode() {
        int vhc = Objects.hashCode(this._value);
        return this._header == null ? vhc ^ this.nameHashCode() : vhc ^ this._header.hashCode();
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof HttpField)) {
            return false;
        } else {
            HttpField field = (HttpField) o;
            if (this._header != field.getHeader()) {
                return false;
            } else if (!this._name.equalsIgnoreCase(field.getName())) {
                return false;
            } else {
                return this._value == null && field.getValue() != null ? false : Objects.equals(this._value, field.getValue());
            }
        }
    }

    public static class IntValueHttpField extends HttpField {

        private final int _int;

        public IntValueHttpField(HttpHeader header, String name, String value, int intValue) {
            super(header, name, value);
            this._int = intValue;
        }

        public IntValueHttpField(HttpHeader header, String name, String value) {
            this(header, name, value, Integer.valueOf(value));
        }

        public IntValueHttpField(HttpHeader header, String name, int intValue) {
            this(header, name, Integer.toString(intValue), intValue);
        }

        public IntValueHttpField(HttpHeader header, int value) {
            this(header, header.asString(), value);
        }

        @Override
        public int getIntValue() {
            return this._int;
        }

        @Override
        public long getLongValue() {
            return (long) this._int;
        }
    }

    public static class LongValueHttpField extends HttpField {

        private final long _long;

        public LongValueHttpField(HttpHeader header, String name, String value, long longValue) {
            super(header, name, value);
            this._long = longValue;
        }

        public LongValueHttpField(HttpHeader header, String name, String value) {
            this(header, name, value, Long.valueOf(value));
        }

        public LongValueHttpField(HttpHeader header, String name, long value) {
            this(header, name, Long.toString(value), value);
        }

        public LongValueHttpField(HttpHeader header, long value) {
            this(header, header.asString(), value);
        }

        @Override
        public int getIntValue() {
            return (int) this._long;
        }

        @Override
        public long getLongValue() {
            return this._long;
        }
    }
}
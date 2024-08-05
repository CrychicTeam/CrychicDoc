package info.journeymap.shaded.org.eclipse.jetty.http;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class QuotedCSV implements Iterable<String> {

    protected final List<String> _values = new ArrayList();

    protected final boolean _keepQuotes;

    public QuotedCSV(String... values) {
        this(true, values);
    }

    public QuotedCSV(boolean keepQuotes, String... values) {
        this._keepQuotes = keepQuotes;
        for (String v : values) {
            this.addValue(v);
        }
    }

    public void addValue(String value) {
        StringBuffer buffer = new StringBuffer();
        int l = value.length();
        QuotedCSV.State state = QuotedCSV.State.VALUE;
        boolean quoted = false;
        boolean sloshed = false;
        int nws_length = 0;
        int last_length = 0;
        int value_length = -1;
        int param_name = -1;
        int param_value = -1;
        for (int i = 0; i <= l; i++) {
            char c = i == l ? 0 : value.charAt(i);
            if (quoted && c != 0) {
                if (sloshed) {
                    sloshed = false;
                } else {
                    switch(c) {
                        case '"':
                            quoted = false;
                            if (!this._keepQuotes) {
                                continue;
                            }
                            break;
                        case '\\':
                            sloshed = true;
                            if (!this._keepQuotes) {
                                continue;
                            }
                    }
                }
                buffer.append(c);
                nws_length = buffer.length();
            } else {
                switch(c) {
                    case '\u0000':
                    case ',':
                        if (nws_length > 0) {
                            buffer.setLength(nws_length);
                            switch(state) {
                                case VALUE:
                                    this.parsedValue(buffer);
                                    value_length = buffer.length();
                                    break;
                                case PARAM_NAME:
                                case PARAM_VALUE:
                                    this.parsedParam(buffer, value_length, param_name, param_value);
                            }
                            this._values.add(buffer.toString());
                        }
                        buffer.setLength(0);
                        last_length = 0;
                        nws_length = 0;
                        param_value = -1;
                        param_name = -1;
                        value_length = -1;
                        state = QuotedCSV.State.VALUE;
                        break;
                    case '\t':
                    case ' ':
                        if (buffer.length() > last_length) {
                            buffer.append(c);
                        }
                        break;
                    case '"':
                        quoted = true;
                        if (this._keepQuotes) {
                            if (state == QuotedCSV.State.PARAM_VALUE && param_value < 0) {
                                param_value = nws_length;
                            }
                            buffer.append(c);
                        } else if (state == QuotedCSV.State.PARAM_VALUE && param_value < 0) {
                            param_value = nws_length;
                        }
                        nws_length = buffer.length();
                        break;
                    case ';':
                        buffer.setLength(nws_length);
                        if (state == QuotedCSV.State.VALUE) {
                            this.parsedValue(buffer);
                            value_length = buffer.length();
                        } else {
                            this.parsedParam(buffer, value_length, param_name, param_value);
                        }
                        nws_length = buffer.length();
                        param_value = -1;
                        param_name = -1;
                        buffer.append(c);
                        last_length = ++nws_length;
                        state = QuotedCSV.State.PARAM_NAME;
                        break;
                    case '=':
                        switch(state) {
                            case VALUE:
                                param_name = 0;
                                value_length = 0;
                                buffer.setLength(nws_length);
                                buffer.append(c);
                                last_length = ++nws_length;
                                state = QuotedCSV.State.PARAM_VALUE;
                                continue;
                            case PARAM_NAME:
                                buffer.setLength(nws_length);
                                buffer.append(c);
                                last_length = ++nws_length;
                                state = QuotedCSV.State.PARAM_VALUE;
                                continue;
                            case PARAM_VALUE:
                                if (param_value < 0) {
                                    param_value = nws_length;
                                }
                                buffer.append(c);
                                nws_length = buffer.length();
                            default:
                                continue;
                        }
                    default:
                        switch(state) {
                            case VALUE:
                                buffer.append(c);
                                nws_length = buffer.length();
                                break;
                            case PARAM_NAME:
                                if (param_name < 0) {
                                    param_name = nws_length;
                                }
                                buffer.append(c);
                                nws_length = buffer.length();
                                break;
                            case PARAM_VALUE:
                                if (param_value < 0) {
                                    param_value = nws_length;
                                }
                                buffer.append(c);
                                nws_length = buffer.length();
                        }
                }
            }
        }
    }

    protected void parsedValue(StringBuffer buffer) {
    }

    protected void parsedParam(StringBuffer buffer, int valueLength, int paramName, int paramValue) {
    }

    public int size() {
        return this._values.size();
    }

    public boolean isEmpty() {
        return this._values.isEmpty();
    }

    public List<String> getValues() {
        return this._values;
    }

    public Iterator<String> iterator() {
        return this._values.iterator();
    }

    public static String unquote(String s) {
        int l = s.length();
        if (s != null && l != 0) {
            int i;
            for (i = 0; i < l; i++) {
                char c = s.charAt(i);
                if (c == '"') {
                    break;
                }
            }
            if (i == l) {
                return s;
            } else {
                boolean quoted = true;
                boolean sloshed = false;
                StringBuffer buffer = new StringBuffer();
                buffer.append(s, 0, i);
                i++;
                for (; i < l; i++) {
                    char c = s.charAt(i);
                    if (quoted) {
                        if (sloshed) {
                            buffer.append(c);
                            sloshed = false;
                        } else if (c == '"') {
                            quoted = false;
                        } else if (c == '\\') {
                            sloshed = true;
                        } else {
                            buffer.append(c);
                        }
                    } else if (c == '"') {
                        quoted = true;
                    } else {
                        buffer.append(c);
                    }
                }
                return buffer.toString();
            }
        } else {
            return s;
        }
    }

    private static enum State {

        VALUE, PARAM_NAME, PARAM_VALUE
    }
}
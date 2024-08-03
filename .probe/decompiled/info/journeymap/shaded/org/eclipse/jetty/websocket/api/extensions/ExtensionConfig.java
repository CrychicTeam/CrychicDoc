package info.journeymap.shaded.org.eclipse.jetty.websocket.api.extensions;

import info.journeymap.shaded.org.eclipse.jetty.websocket.api.util.QuoteUtil;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ExtensionConfig {

    private final String name;

    private final Map<String, String> parameters;

    public static ExtensionConfig parse(String parameterizedName) {
        return new ExtensionConfig(parameterizedName);
    }

    public static List<ExtensionConfig> parseEnum(Enumeration<String> valuesEnum) {
        List<ExtensionConfig> configs = new ArrayList();
        if (valuesEnum != null) {
            while (valuesEnum.hasMoreElements()) {
                Iterator<String> extTokenIter = QuoteUtil.splitAt((String) valuesEnum.nextElement(), ",");
                while (extTokenIter.hasNext()) {
                    String extToken = (String) extTokenIter.next();
                    configs.add(parse(extToken));
                }
            }
        }
        return configs;
    }

    public static List<ExtensionConfig> parseList(String... rawSecWebSocketExtensions) {
        List<ExtensionConfig> configs = new ArrayList();
        for (String rawValue : rawSecWebSocketExtensions) {
            Iterator<String> extTokenIter = QuoteUtil.splitAt(rawValue, ",");
            while (extTokenIter.hasNext()) {
                String extToken = (String) extTokenIter.next();
                configs.add(parse(extToken));
            }
        }
        return configs;
    }

    public static String toHeaderValue(List<ExtensionConfig> configs) {
        if (configs != null && !configs.isEmpty()) {
            StringBuilder parameters = new StringBuilder();
            boolean needsDelim = false;
            for (ExtensionConfig ext : configs) {
                if (needsDelim) {
                    parameters.append(", ");
                }
                parameters.append(ext.getParameterizedName());
                needsDelim = true;
            }
            return parameters.toString();
        } else {
            return null;
        }
    }

    public ExtensionConfig(ExtensionConfig copy) {
        this.name = copy.name;
        this.parameters = new HashMap();
        this.parameters.putAll(copy.parameters);
    }

    public ExtensionConfig(String parameterizedName) {
        Iterator<String> extListIter = QuoteUtil.splitAt(parameterizedName, ";");
        this.name = (String) extListIter.next();
        this.parameters = new HashMap();
        while (extListIter.hasNext()) {
            String extParam = (String) extListIter.next();
            Iterator<String> extParamIter = QuoteUtil.splitAt(extParam, "=");
            String key = ((String) extParamIter.next()).trim();
            String value = null;
            if (extParamIter.hasNext()) {
                value = (String) extParamIter.next();
            }
            this.parameters.put(key, value);
        }
    }

    public String getName() {
        return this.name;
    }

    public final int getParameter(String key, int defValue) {
        String val = (String) this.parameters.get(key);
        return val == null ? defValue : Integer.valueOf(val);
    }

    public final String getParameter(String key, String defValue) {
        String val = (String) this.parameters.get(key);
        return val == null ? defValue : val;
    }

    public final String getParameterizedName() {
        StringBuilder str = new StringBuilder();
        str.append(this.name);
        for (String param : this.parameters.keySet()) {
            str.append(';');
            str.append(param);
            String value = (String) this.parameters.get(param);
            if (value != null) {
                str.append('=');
                QuoteUtil.quoteIfNeeded(str, value, ";=");
            }
        }
        return str.toString();
    }

    public final Set<String> getParameterKeys() {
        return this.parameters.keySet();
    }

    public final Map<String, String> getParameters() {
        return this.parameters;
    }

    public final void init(ExtensionConfig other) {
        this.parameters.clear();
        this.parameters.putAll(other.parameters);
    }

    public final void setParameter(String key) {
        this.parameters.put(key, null);
    }

    public final void setParameter(String key, int value) {
        this.parameters.put(key, Integer.toString(value));
    }

    public final void setParameter(String key, String value) {
        this.parameters.put(key, value);
    }

    public String toString() {
        return this.getParameterizedName();
    }
}
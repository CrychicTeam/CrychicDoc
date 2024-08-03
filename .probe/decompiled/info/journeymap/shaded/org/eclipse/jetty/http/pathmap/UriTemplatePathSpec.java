package info.journeymap.shaded.org.eclipse.jetty.http.pathmap;

import info.journeymap.shaded.org.eclipse.jetty.util.TypeUtil;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Log;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Logger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UriTemplatePathSpec extends RegexPathSpec {

    private static final Logger LOG = Log.getLogger(UriTemplatePathSpec.class);

    private static final Pattern VARIABLE_PATTERN = Pattern.compile("\\{(.*)\\}");

    private static final String VARIABLE_RESERVED = ":/?#[]@!$&'()*+,;=";

    private static final String VARIABLE_SYMBOLS = "-._";

    private static final Set<String> FORBIDDEN_SEGMENTS = new HashSet();

    private String[] variables;

    public UriTemplatePathSpec(String rawSpec) {
        Objects.requireNonNull(rawSpec, "Path Param Spec cannot be null");
        if (!"".equals(rawSpec) && !"/".equals(rawSpec)) {
            if (rawSpec.charAt(0) != '/') {
                StringBuilder err = new StringBuilder();
                err.append("Syntax Error: path spec \"");
                err.append(rawSpec);
                err.append("\" must start with '/'");
                throw new IllegalArgumentException(err.toString());
            } else {
                for (String forbidden : FORBIDDEN_SEGMENTS) {
                    if (rawSpec.contains(forbidden)) {
                        StringBuilder err = new StringBuilder();
                        err.append("Syntax Error: segment ");
                        err.append(forbidden);
                        err.append(" is forbidden in path spec: ");
                        err.append(rawSpec);
                        throw new IllegalArgumentException(err.toString());
                    }
                }
                this.pathSpec = rawSpec;
                StringBuilder regex = new StringBuilder();
                regex.append('^');
                List<String> varNames = new ArrayList();
                String[] segments = rawSpec.substring(1).split("/");
                char[] segmentSignature = new char[segments.length];
                this.pathDepth = segments.length;
                for (int i = 0; i < segments.length; i++) {
                    String segment = segments[i];
                    Matcher mat = VARIABLE_PATTERN.matcher(segment);
                    if (mat.matches()) {
                        String variable = mat.group(1);
                        if (varNames.contains(variable)) {
                            StringBuilder err = new StringBuilder();
                            err.append("Syntax Error: variable ");
                            err.append(variable);
                            err.append(" is duplicated in path spec: ");
                            err.append(rawSpec);
                            throw new IllegalArgumentException(err.toString());
                        }
                        this.assertIsValidVariableLiteral(variable);
                        segmentSignature[i] = 'v';
                        varNames.add(variable);
                        regex.append("/([^/]+)");
                    } else {
                        if (mat.find(0)) {
                            StringBuilder err = new StringBuilder();
                            err.append("Syntax Error: variable ");
                            err.append(mat.group());
                            err.append(" must exist as entire path segment: ");
                            err.append(rawSpec);
                            throw new IllegalArgumentException(err.toString());
                        }
                        if (segment.indexOf(123) >= 0 || segment.indexOf(125) >= 0) {
                            StringBuilder err = new StringBuilder();
                            err.append("Syntax Error: invalid path segment /");
                            err.append(segment);
                            err.append("/ variable declaration incomplete: ");
                            err.append(rawSpec);
                            throw new IllegalArgumentException(err.toString());
                        }
                        if (segment.indexOf(42) >= 0) {
                            StringBuilder err = new StringBuilder();
                            err.append("Syntax Error: path segment /");
                            err.append(segment);
                            err.append("/ contains a wildcard symbol (not supported by this uri-template implementation): ");
                            err.append(rawSpec);
                            throw new IllegalArgumentException(err.toString());
                        }
                        segmentSignature[i] = 'e';
                        regex.append('/');
                        for (char c : segment.toCharArray()) {
                            if (c == '.' || c == '[' || c == ']' || c == '\\') {
                                regex.append('\\');
                            }
                            regex.append(c);
                        }
                    }
                }
                if (rawSpec.charAt(rawSpec.length() - 1) == '/') {
                    regex.append('/');
                }
                regex.append('$');
                this.pattern = Pattern.compile(regex.toString());
                int varcount = varNames.size();
                this.variables = (String[]) varNames.toArray(new String[varcount]);
                String sig = String.valueOf(segmentSignature);
                if (Pattern.matches("^e*$", sig)) {
                    this.group = PathSpecGroup.EXACT;
                } else if (Pattern.matches("^e*v+", sig)) {
                    this.group = PathSpecGroup.PREFIX_GLOB;
                } else if (Pattern.matches("^v+e+", sig)) {
                    this.group = PathSpecGroup.SUFFIX_GLOB;
                } else {
                    this.group = PathSpecGroup.MIDDLE_GLOB;
                }
            }
        } else {
            super.pathSpec = "/";
            super.pattern = Pattern.compile("^/$");
            super.pathDepth = 1;
            this.specLength = 1;
            this.variables = new String[0];
            this.group = PathSpecGroup.EXACT;
        }
    }

    private void assertIsValidVariableLiteral(String variable) {
        int len = variable.length();
        int i = 0;
        boolean valid = len > 0;
        while (valid && i < len) {
            int codepoint = variable.codePointAt(i);
            i += Character.charCount(codepoint);
            if (!this.isValidBasicLiteralCodepoint(codepoint) && !Character.isSupplementaryCodePoint(codepoint)) {
                if (codepoint == 37) {
                    if (i + 2 > len) {
                        valid = false;
                        continue;
                    }
                    codepoint = TypeUtil.convertHexDigit(variable.codePointAt(i++)) << 4;
                    codepoint |= TypeUtil.convertHexDigit(variable.codePointAt(i++));
                    if (this.isValidBasicLiteralCodepoint(codepoint)) {
                        continue;
                    }
                }
                valid = false;
            }
        }
        if (!valid) {
            StringBuilder err = new StringBuilder();
            err.append("Syntax Error: variable {");
            err.append(variable);
            err.append("} an invalid variable name: ");
            err.append(this.pathSpec);
            throw new IllegalArgumentException(err.toString());
        }
    }

    private boolean isValidBasicLiteralCodepoint(int codepoint) {
        if ((codepoint < 97 || codepoint > 122) && (codepoint < 65 || codepoint > 90) && (codepoint < 48 || codepoint > 57)) {
            if ("-._".indexOf(codepoint) >= 0) {
                return true;
            } else if (":/?#[]@!$&'()*+,;=".indexOf(codepoint) >= 0) {
                LOG.warn("Detected URI Template reserved symbol [{}] in path spec \"{}\"", (char) codepoint, this.pathSpec);
                return false;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    public Map<String, String> getPathParams(String path) {
        Matcher matcher = this.getMatcher(path);
        if (!matcher.matches()) {
            return null;
        } else if (this.group == PathSpecGroup.EXACT) {
            return Collections.emptyMap();
        } else {
            Map<String, String> ret = new HashMap();
            int groupCount = matcher.groupCount();
            for (int i = 1; i <= groupCount; i++) {
                ret.put(this.variables[i - 1], matcher.group(i));
            }
            return ret;
        }
    }

    public int getVariableCount() {
        return this.variables.length;
    }

    public String[] getVariables() {
        return this.variables;
    }

    static {
        FORBIDDEN_SEGMENTS.add("/./");
        FORBIDDEN_SEGMENTS.add("/../");
        FORBIDDEN_SEGMENTS.add("//");
    }
}
package info.journeymap.shaded.org.eclipse.jetty.http.pathmap;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexPathSpec extends PathSpec {

    protected Pattern pattern;

    protected RegexPathSpec() {
    }

    public RegexPathSpec(String regex) {
        super.pathSpec = regex;
        if (regex.startsWith("regex|")) {
            super.pathSpec = regex.substring("regex|".length());
        }
        this.pathDepth = 0;
        this.specLength = this.pathSpec.length();
        boolean inGrouping = false;
        StringBuilder signature = new StringBuilder();
        for (char c : this.pathSpec.toCharArray()) {
            switch(c) {
                case '*':
                    signature.append('g');
                    break;
                case '/':
                    if (!inGrouping) {
                        this.pathDepth++;
                    }
                    break;
                case '[':
                    inGrouping = true;
                    break;
                case ']':
                    inGrouping = false;
                    signature.append('g');
                    break;
                default:
                    if (!inGrouping && Character.isLetterOrDigit(c)) {
                        signature.append('l');
                    }
            }
        }
        this.pattern = Pattern.compile(this.pathSpec);
        String sig = signature.toString();
        if (Pattern.matches("^l*$", sig)) {
            this.group = PathSpecGroup.EXACT;
        } else if (Pattern.matches("^l*g+", sig)) {
            this.group = PathSpecGroup.PREFIX_GLOB;
        } else if (Pattern.matches("^g+l+$", sig)) {
            this.group = PathSpecGroup.SUFFIX_GLOB;
        } else {
            this.group = PathSpecGroup.MIDDLE_GLOB;
        }
    }

    public Matcher getMatcher(String path) {
        return this.pattern.matcher(path);
    }

    @Override
    public String getPathInfo(String path) {
        if (this.group == PathSpecGroup.PREFIX_GLOB) {
            Matcher matcher = this.getMatcher(path);
            if (matcher.matches() && matcher.groupCount() >= 1) {
                String pathInfo = matcher.group(1);
                if ("".equals(pathInfo)) {
                    return "/";
                }
                return pathInfo;
            }
        }
        return null;
    }

    @Override
    public String getPathMatch(String path) {
        Matcher matcher = this.getMatcher(path);
        if (matcher.matches()) {
            if (matcher.groupCount() >= 1) {
                int idx = matcher.start(1);
                if (idx > 0) {
                    if (path.charAt(idx - 1) == '/') {
                        idx--;
                    }
                    return path.substring(0, idx);
                }
            }
            return path;
        } else {
            return null;
        }
    }

    public Pattern getPattern() {
        return this.pattern;
    }

    @Override
    public String getRelativePath(String base, String path) {
        return null;
    }

    @Override
    public boolean matches(String path) {
        int idx = path.indexOf(63);
        return idx >= 0 ? this.getMatcher(path.substring(0, idx)).matches() : this.getMatcher(path).matches();
    }
}
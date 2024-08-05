package info.journeymap.shaded.org.eclipse.jetty.util.ssl;

import info.journeymap.shaded.org.eclipse.jetty.util.component.ContainerLifeCycle;
import info.journeymap.shaded.org.eclipse.jetty.util.component.Dumpable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class SslSelectionDump extends ContainerLifeCycle implements Dumpable {

    private final String type;

    private SslSelectionDump.CaptionedList enabled = new SslSelectionDump.CaptionedList("Enabled");

    private SslSelectionDump.CaptionedList disabled = new SslSelectionDump.CaptionedList("Disabled");

    public SslSelectionDump(String type, String[] supportedByJVM, String[] enabledByJVM, String[] excludedByConfig, String[] includedByConfig) {
        this.type = type;
        this.addBean(this.enabled);
        this.addBean(this.disabled);
        List<String> jvmEnabled = Arrays.asList(enabledByJVM);
        List<Pattern> excludedPatterns = (List<Pattern>) Arrays.stream(excludedByConfig).map(entry -> Pattern.compile(entry)).collect(Collectors.toList());
        List<Pattern> includedPatterns = (List<Pattern>) Arrays.stream(includedByConfig).map(entry -> Pattern.compile(entry)).collect(Collectors.toList());
        Arrays.stream(supportedByJVM).sorted(Comparator.naturalOrder()).forEach(entry -> {
            boolean isPresent = true;
            StringBuilder s = new StringBuilder();
            s.append(entry);
            if (!jvmEnabled.contains(entry)) {
                if (isPresent) {
                    s.append(" -");
                    isPresent = false;
                }
                s.append(" JreDisabled:java.security");
            }
            for (Pattern pattern : excludedPatterns) {
                Matcher m = pattern.matcher(entry);
                if (m.matches()) {
                    if (isPresent) {
                        s.append(" -");
                        isPresent = false;
                    } else {
                        s.append(",");
                    }
                    s.append(" ConfigExcluded:'").append(pattern.pattern()).append('\'');
                }
            }
            if (!includedPatterns.isEmpty()) {
                boolean isIncluded = false;
                for (Pattern patternx : includedPatterns) {
                    Matcher m = patternx.matcher(entry);
                    if (m.matches()) {
                        isIncluded = true;
                        break;
                    }
                }
                if (!isIncluded) {
                    if (isPresent) {
                        s.append(" -");
                        isPresent = false;
                    } else {
                        s.append(",");
                    }
                    s.append(" ConfigIncluded:NotSpecified");
                }
            }
            if (isPresent) {
                this.enabled.add(s.toString());
            } else {
                this.disabled.add(s.toString());
            }
        });
    }

    @Override
    public String dump() {
        return ContainerLifeCycle.dump(this);
    }

    @Override
    public void dump(Appendable out, String indent) throws IOException {
        this.dumpBeans(out, indent, new Collection[0]);
    }

    @Override
    protected void dumpThis(Appendable out) throws IOException {
        out.append(this.type).append(" Selections").append(System.lineSeparator());
    }

    private static class CaptionedList extends ArrayList<String> implements Dumpable {

        private final String caption;

        public CaptionedList(String caption) {
            this.caption = caption;
        }

        @Override
        public String dump() {
            return ContainerLifeCycle.dump(this);
        }

        @Override
        public void dump(Appendable out, String indent) throws IOException {
            out.append(this.caption);
            out.append(" (size=").append(Integer.toString(this.size())).append(")");
            out.append(System.lineSeparator());
            ContainerLifeCycle.dump(out, indent, this);
        }
    }
}
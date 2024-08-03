package com.mojang.realmsclient.util;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Lists;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import javax.annotation.Nullable;

public class TextRenderingUtils {

    private TextRenderingUtils() {
    }

    @VisibleForTesting
    protected static List<String> lineBreak(String string0) {
        return Arrays.asList(string0.split("\\n"));
    }

    public static List<TextRenderingUtils.Line> decompose(String string0, TextRenderingUtils.LineSegment... textRenderingUtilsLineSegment1) {
        return decompose(string0, Arrays.asList(textRenderingUtilsLineSegment1));
    }

    private static List<TextRenderingUtils.Line> decompose(String string0, List<TextRenderingUtils.LineSegment> listTextRenderingUtilsLineSegment1) {
        List<String> $$2 = lineBreak(string0);
        return insertLinks($$2, listTextRenderingUtilsLineSegment1);
    }

    private static List<TextRenderingUtils.Line> insertLinks(List<String> listString0, List<TextRenderingUtils.LineSegment> listTextRenderingUtilsLineSegment1) {
        int $$2 = 0;
        List<TextRenderingUtils.Line> $$3 = Lists.newArrayList();
        for (String $$4 : listString0) {
            List<TextRenderingUtils.LineSegment> $$5 = Lists.newArrayList();
            for (String $$7 : split($$4, "%link")) {
                if ("%link".equals($$7)) {
                    $$5.add((TextRenderingUtils.LineSegment) listTextRenderingUtilsLineSegment1.get($$2++));
                } else {
                    $$5.add(TextRenderingUtils.LineSegment.text($$7));
                }
            }
            $$3.add(new TextRenderingUtils.Line($$5));
        }
        return $$3;
    }

    public static List<String> split(String string0, String string1) {
        if (string1.isEmpty()) {
            throw new IllegalArgumentException("Delimiter cannot be the empty string");
        } else {
            List<String> $$2 = Lists.newArrayList();
            int $$3 = 0;
            int $$4;
            while (($$4 = string0.indexOf(string1, $$3)) != -1) {
                if ($$4 > $$3) {
                    $$2.add(string0.substring($$3, $$4));
                }
                $$2.add(string1);
                $$3 = $$4 + string1.length();
            }
            if ($$3 < string0.length()) {
                $$2.add(string0.substring($$3));
            }
            return $$2;
        }
    }

    public static class Line {

        public final List<TextRenderingUtils.LineSegment> segments;

        Line(TextRenderingUtils.LineSegment... textRenderingUtilsLineSegment0) {
            this(Arrays.asList(textRenderingUtilsLineSegment0));
        }

        Line(List<TextRenderingUtils.LineSegment> listTextRenderingUtilsLineSegment0) {
            this.segments = listTextRenderingUtilsLineSegment0;
        }

        public String toString() {
            return "Line{segments=" + this.segments + "}";
        }

        public boolean equals(Object object0) {
            if (this == object0) {
                return true;
            } else if (object0 != null && this.getClass() == object0.getClass()) {
                TextRenderingUtils.Line $$1 = (TextRenderingUtils.Line) object0;
                return Objects.equals(this.segments, $$1.segments);
            } else {
                return false;
            }
        }

        public int hashCode() {
            return Objects.hash(new Object[] { this.segments });
        }
    }

    public static class LineSegment {

        private final String fullText;

        @Nullable
        private final String linkTitle;

        @Nullable
        private final String linkUrl;

        private LineSegment(String string0) {
            this.fullText = string0;
            this.linkTitle = null;
            this.linkUrl = null;
        }

        private LineSegment(String string0, @Nullable String string1, @Nullable String string2) {
            this.fullText = string0;
            this.linkTitle = string1;
            this.linkUrl = string2;
        }

        public boolean equals(Object object0) {
            if (this == object0) {
                return true;
            } else if (object0 != null && this.getClass() == object0.getClass()) {
                TextRenderingUtils.LineSegment $$1 = (TextRenderingUtils.LineSegment) object0;
                return Objects.equals(this.fullText, $$1.fullText) && Objects.equals(this.linkTitle, $$1.linkTitle) && Objects.equals(this.linkUrl, $$1.linkUrl);
            } else {
                return false;
            }
        }

        public int hashCode() {
            return Objects.hash(new Object[] { this.fullText, this.linkTitle, this.linkUrl });
        }

        public String toString() {
            return "Segment{fullText='" + this.fullText + "', linkTitle='" + this.linkTitle + "', linkUrl='" + this.linkUrl + "'}";
        }

        public String renderedText() {
            return this.isLink() ? this.linkTitle : this.fullText;
        }

        public boolean isLink() {
            return this.linkTitle != null;
        }

        public String getLinkUrl() {
            if (!this.isLink()) {
                throw new IllegalStateException("Not a link: " + this);
            } else {
                return this.linkUrl;
            }
        }

        public static TextRenderingUtils.LineSegment link(String string0, String string1) {
            return new TextRenderingUtils.LineSegment(null, string0, string1);
        }

        @VisibleForTesting
        protected static TextRenderingUtils.LineSegment text(String string0) {
            return new TextRenderingUtils.LineSegment(string0);
        }
    }
}
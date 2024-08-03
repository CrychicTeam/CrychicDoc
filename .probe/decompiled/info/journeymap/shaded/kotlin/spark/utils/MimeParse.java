package info.journeymap.shaded.kotlin.spark.utils;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MimeParse {

    public static final String NO_MIME_TYPE = "";

    private static MimeParse.ParseResults parseMimeType(String mimeType) {
        String[] parts = mimeType.split(";");
        MimeParse.ParseResults results = new MimeParse.ParseResults();
        results.params = new HashMap();
        for (int i = 1; i < parts.length; i++) {
            String p = parts[i];
            String[] subParts = p.split("=");
            if (subParts.length == 2) {
                results.params.put(subParts[0].trim(), subParts[1].trim());
            }
        }
        String fullType = parts[0].trim();
        if (fullType.equals("*")) {
            fullType = "*/*";
        }
        int slashIndex = fullType.indexOf(47);
        if (slashIndex != -1) {
            results.type = fullType.substring(0, slashIndex);
            results.subType = fullType.substring(slashIndex + 1);
        } else {
            results.type = fullType;
            results.subType = "*";
        }
        return results;
    }

    private static MimeParse.ParseResults parseMediaRange(String range) {
        MimeParse.ParseResults results = parseMimeType(range);
        String q = (String) results.params.get("q");
        float f = toFloat(q, 1.0F);
        if (isBlank(q) || f < 0.0F || f > 1.0F) {
            results.params.put("q", "1");
        }
        return results;
    }

    private static MimeParse.FitnessAndQuality fitnessAndQualityParsed(String mimeType, Collection<MimeParse.ParseResults> parsedRanges) {
        int bestFitness = -1;
        float bestFitQ = 0.0F;
        MimeParse.ParseResults target = parseMediaRange(mimeType);
        for (MimeParse.ParseResults range : parsedRanges) {
            if ((target.type.equals(range.type) || range.type.equals("*") || target.type.equals("*")) && (target.subType.equals(range.subType) || range.subType.equals("*") || target.subType.equals("*"))) {
                for (String k : target.params.keySet()) {
                    int paramMatches = 0;
                    if (!k.equals("q") && range.params.containsKey(k) && ((String) target.params.get(k)).equals(range.params.get(k))) {
                        paramMatches++;
                    }
                    int fitness = range.type.equals(target.type) ? 100 : 0;
                    fitness += range.subType.equals(target.subType) ? 10 : 0;
                    fitness += paramMatches;
                    if (fitness > bestFitness) {
                        bestFitness = fitness;
                        bestFitQ = toFloat((String) range.params.get("q"), 0.0F);
                    }
                }
            }
        }
        return new MimeParse.FitnessAndQuality(bestFitness, bestFitQ);
    }

    public static String bestMatch(Collection<String> supported, String header) {
        List<MimeParse.ParseResults> parseResults = new LinkedList();
        List<MimeParse.FitnessAndQuality> weightedMatches = new LinkedList();
        for (String r : header.split(",")) {
            parseResults.add(parseMediaRange(r));
        }
        for (String s : supported) {
            MimeParse.FitnessAndQuality fitnessAndQuality = fitnessAndQualityParsed(s, parseResults);
            fitnessAndQuality.mimeType = s;
            weightedMatches.add(fitnessAndQuality);
        }
        Collections.sort(weightedMatches);
        MimeParse.FitnessAndQuality lastOne = (MimeParse.FitnessAndQuality) weightedMatches.get(weightedMatches.size() - 1);
        return Float.compare(lastOne.quality, 0.0F) != 0 ? lastOne.mimeType : "";
    }

    private static boolean isBlank(String s) {
        return s == null || "".equals(s.trim());
    }

    private static float toFloat(String str, float defaultValue) {
        if (str == null) {
            return defaultValue;
        } else {
            try {
                return Float.parseFloat(str);
            } catch (NumberFormatException var3) {
                return defaultValue;
            }
        }
    }

    private MimeParse() {
    }

    private static class FitnessAndQuality implements Comparable<MimeParse.FitnessAndQuality> {

        int fitness;

        float quality;

        String mimeType;

        private FitnessAndQuality(int fitness, float quality) {
            this.fitness = fitness;
            this.quality = quality;
        }

        public int compareTo(MimeParse.FitnessAndQuality o) {
            if (this.fitness == o.fitness) {
                if (this.quality == o.quality) {
                    return 0;
                } else {
                    return this.quality < o.quality ? -1 : 1;
                }
            } else {
                return this.fitness < o.fitness ? -1 : 1;
            }
        }
    }

    private static class ParseResults {

        String type;

        String subType;

        Map<String, String> params;

        private ParseResults() {
        }

        public String toString() {
            StringBuffer s = new StringBuffer("('" + this.type + "', '" + this.subType + "', {");
            for (String k : this.params.keySet()) {
                s.append("'" + k + "':'" + (String) this.params.get(k) + "',");
            }
            return s.append("})").toString();
        }
    }
}
package nikedemos.markovnames;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import noppes.npcs.CustomNpcs;

public class MarkovDictionary {

    public static final Random rng = new Random();

    private int sequenceLen = 3;

    private HashMap2D<String, String, Integer> occurrences = new HashMap2D<>();

    public MarkovDictionary(String dictionary, int seqlen) {
        try {
            this.applyDictionary(dictionary, seqlen);
        } catch (Exception var4) {
            var4.printStackTrace();
        }
    }

    public MarkovDictionary(String dictionary) {
        this(dictionary, 3);
    }

    public String getCapitalized(String str) {
        if (str != null && !str.isEmpty()) {
            char[] chars = str.toCharArray();
            chars[0] = Character.toUpperCase(chars[0]);
            return new String(chars);
        } else {
            return str;
        }
    }

    public static String readFile(String path) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, StandardCharsets.UTF_8);
    }

    public void incrementSafe(String str1, String str2) {
        if (this.occurrences.containsKeys(str1, str2)) {
            int curr = this.occurrences.get(str1, str2);
            this.occurrences.put(str1, str2, curr + 1);
        } else {
            this.occurrences.put(str1, str2, 1);
        }
    }

    public String generateWord() {
        int allEntries = 0;
        for (Entry<String, Map<String, Integer>> pair : this.occurrences.mMap.entrySet()) {
            String k = (String) pair.getKey();
            if (k.startsWith("_[") && k.endsWith("_")) {
                allEntries += this.occurrences.get(k, "_TOTAL_");
            }
        }
        int randomNumber = rng.nextInt(allEntries);
        Iterator<Entry<String, Map<String, Integer>>> it = this.occurrences.mMap.entrySet().iterator();
        StringBuilder sequence = new StringBuilder("");
        while (it.hasNext()) {
            Entry<String, Map<String, Integer>> pairx = (Entry<String, Map<String, Integer>>) it.next();
            String k = (String) pairx.getKey();
            if (k.startsWith("_[") && k.endsWith("_")) {
                int topLevelEntries = this.occurrences.get(k, "_TOTAL_");
                if (randomNumber < topLevelEntries) {
                    sequence.append(k.substring(1, this.sequenceLen + 1));
                    break;
                }
                randomNumber -= topLevelEntries;
            }
        }
        StringBuilder word = new StringBuilder("");
        word.append(sequence);
        while (sequence.charAt(sequence.length() - 1) != ']') {
            int subSize = 0;
            for (Entry<String, Integer> entry : ((Map) this.occurrences.mMap.get(sequence.toString())).entrySet()) {
                subSize += entry.getValue();
            }
            randomNumber = rng.nextInt(subSize);
            Iterator<Entry<String, Integer>> k = ((Map) this.occurrences.mMap.get(sequence.toString())).entrySet().iterator();
            String chosen = "";
            while (k.hasNext()) {
                Entry<String, Integer> entry = (Entry<String, Integer>) k.next();
                int occu = this.occurrences.get(sequence.toString(), (String) entry.getKey());
                if (randomNumber < occu) {
                    chosen = (String) entry.getKey();
                    break;
                }
                randomNumber -= occu;
            }
            word.append(chosen);
            sequence.delete(0, 1);
            sequence.append(chosen);
        }
        return this.getPost(word.substring(1, word.length() - 1));
    }

    public String getPost(String str) {
        return this.getCapitalized(str);
    }

    public void applyDictionary(String dictionaryFile, int seqLen) throws IOException {
        StringBuilder input = new StringBuilder();
        ResourceLocation resource = new ResourceLocation("customnpcs", "markovnames/" + dictionaryFile);
        Resource ir = (Resource) CustomNpcs.Server.getServerResources().resourceManager().m_213713_(resource).orElse(null);
        InputStream stream = ir.open();
        try {
            BufferedReader readIn = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
            for (String line = readIn.readLine(); line != null; line = readIn.readLine()) {
                input.append(line).append(" ");
            }
            readIn.close();
        } catch (Throwable var13) {
            if (stream != null) {
                try {
                    stream.close();
                } catch (Throwable var12) {
                    var13.addSuppressed(var12);
                }
            }
            throw var13;
        }
        if (stream != null) {
            stream.close();
        }
        if (input.length() == 0) {
            throw new RuntimeException("Resource was empty: + " + resource);
        } else {
            if (this.sequenceLen != seqLen) {
                this.sequenceLen = seqLen;
                this.occurrences.clear();
            }
            String input_str = "[" + input.toString().toLowerCase().replaceAll("[\\t\\n\\r\\s]+", "][") + "]";
            int maxCursorPos = input_str.length() - 1 - this.sequenceLen;
            for (int i = 0; i <= maxCursorPos; i++) {
                String seqCurr = input_str.substring(i, i + this.sequenceLen);
                String seqNext = input_str.substring(i + this.sequenceLen, i + this.sequenceLen + 1);
                this.incrementSafe(seqCurr, seqNext);
                StringBuilder meta = new StringBuilder("_").append(seqCurr).append("_");
                this.incrementSafe(meta.toString(), "_TOTAL_");
            }
        }
    }
}
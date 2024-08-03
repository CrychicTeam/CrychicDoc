package nikedemos.markovnames;

import java.util.HashMap;
import java.util.Map.Entry;
import nikedemos.markovnames.generators.MarkovAncientGreek;
import nikedemos.markovnames.generators.MarkovAztec;
import nikedemos.markovnames.generators.MarkovGenerator;
import nikedemos.markovnames.generators.MarkovJapanese;
import nikedemos.markovnames.generators.MarkovOldNorse;
import nikedemos.markovnames.generators.MarkovRoman;
import nikedemos.markovnames.generators.MarkovSaami;
import nikedemos.markovnames.generators.MarkovSlavic;
import nikedemos.markovnames.generators.MarkovWelsh;

public class Main {

    public static final int GENDER_RANDOM = 0;

    public static final int GENDER_MALE = 1;

    public static final int GENDER_FEMALE = 2;

    public static HashMap<String, MarkovGenerator> GENERATORS = new HashMap();

    public static void main(String[] args) {
        GENERATORS.put("ROMAN", new MarkovRoman(3));
        GENERATORS.put("JAPANESE", new MarkovJapanese(4));
        GENERATORS.put("SLAVIC", new MarkovSlavic(3));
        GENERATORS.put("WELSH", new MarkovWelsh(3));
        GENERATORS.put("SAAMI", new MarkovSaami(3));
        GENERATORS.put("OLDNORSE", new MarkovOldNorse(4));
        GENERATORS.put("ANCIENTGREEK", new MarkovAncientGreek(3));
        GENERATORS.put("AZTEC", new MarkovAztec(3));
        for (Entry<String, MarkovGenerator> pair : GENERATORS.entrySet()) {
            System.out.println("===" + (String) pair.getKey() + "===");
            for (int i = 0; i < 16; i++) {
                if (i == 0) {
                    System.out.println("GENTLEMEN-----------");
                }
                int gender = i < 8 ? 1 : 2;
                String random_name = ((MarkovGenerator) pair.getValue()).fetch(gender);
                System.out.println(random_name);
                if (i == 15) {
                    System.out.println("\n");
                } else if (i == 7) {
                    System.out.println("LADIES--------------");
                }
            }
        }
    }
}
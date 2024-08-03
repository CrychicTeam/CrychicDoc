package nikedemos.markovnames.generators;

import nikedemos.markovnames.MarkovDictionary;

public class MarkovJapanese extends MarkovGenerator {

    public MarkovDictionary markov2;

    public MarkovDictionary markov3;

    public MarkovJapanese(int seqlen) {
        this.markov = new MarkovDictionary("japanese_surnames.txt", seqlen);
        this.markov2 = new MarkovDictionary("japanese_given_male.txt", seqlen);
        this.markov3 = new MarkovDictionary("japanese_given_female.txt", seqlen);
    }

    public MarkovJapanese() {
        this(4);
    }

    @Override
    public String fetch(int gender) {
        StringBuilder name = new StringBuilder(this.markov.generateWord());
        name.append(" ");
        if (gender == 0) {
            gender = MarkovDictionary.rng.nextBoolean() ? 1 : 2;
        }
        if (gender == 2) {
            name.append(this.markov3.generateWord());
        } else {
            name.append(this.markov2.generateWord());
        }
        return name.toString();
    }
}
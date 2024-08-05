package de.keksuccino.konkrete.input;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;

public class CharacterFilter {

    private List<Character> allowed = new ArrayList();

    private List<Character> forbidden = new ArrayList();

    public boolean isAllowed(char c) {
        return !this.allowed.isEmpty() ? this.allowed.contains(c) : !this.forbidden.contains(c);
    }

    public boolean isAllowed(@Nonnull String charString) {
        return charString != null && charString.length() >= 1 ? this.isAllowed(charString.charAt(0)) : true;
    }

    public String filterForAllowedChars(String text) {
        String s = "";
        if (text == null) {
            return s;
        } else {
            for (int i = 0; i < text.length(); i++) {
                if (this.isAllowed(text.charAt(i))) {
                    s = s + text.charAt(i);
                }
            }
            return s;
        }
    }

    public void addAllowedCharacters(char... chars) {
        for (char c : chars) {
            if (!this.allowed.contains(c)) {
                this.allowed.add(c);
            }
        }
    }

    public void addAllowedCharacters(@Nonnull String... chars) {
        for (String s : chars) {
            if (s != null && s.length() >= 1 && !this.allowed.contains(s.charAt(0))) {
                this.allowed.add(s.charAt(0));
            }
        }
    }

    public void addForbiddenCharacters(char... chars) {
        for (char c : chars) {
            if (!this.forbidden.contains(c)) {
                this.forbidden.add(c);
            }
        }
    }

    public void addForbiddenCharacters(@Nonnull String... chars) {
        for (String s : chars) {
            if (s != null && s.length() >= 1 && !this.forbidden.contains(s.charAt(0))) {
                this.forbidden.add(s.charAt(0));
            }
        }
    }

    public static CharacterFilter getDoubleCharacterFiler() {
        CharacterFilter f = new CharacterFilter();
        f.addAllowedCharacters("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", ".", "-", "+");
        return f;
    }

    public static CharacterFilter getIntegerCharacterFiler() {
        CharacterFilter f = new CharacterFilter();
        f.addAllowedCharacters("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "-", "+");
        return f;
    }

    public static CharacterFilter getBasicFilenameCharacterFilter() {
        CharacterFilter f = new CharacterFilter();
        f.addAllowedCharacters("a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", ".", "_", "-");
        return f;
    }

    public static CharacterFilter getFilenameFilterWithUppercaseSupport() {
        CharacterFilter f = getBasicFilenameCharacterFilter();
        f.addAllowedCharacters("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z");
        return f;
    }

    public static CharacterFilter getUrlCharacterFilter() {
        CharacterFilter f = new CharacterFilter();
        f.addAllowedCharacters("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", ".", "-", "_", "~", "+", "#", ",", "%", "&", "=", "*", ";", ":", "@", "?", "/", "\\");
        return f;
    }
}
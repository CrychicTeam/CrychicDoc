package dev.latvian.mods.rhino;

import java.util.ArrayDeque;

public class ConsString implements CharSequence {

    private final int length;

    private CharSequence left;

    private CharSequence right;

    private boolean isFlat;

    public ConsString(CharSequence str1, CharSequence str2) {
        this.left = str1;
        this.right = str2;
        this.length = this.left.length() + this.right.length();
        this.isFlat = false;
    }

    public String toString() {
        return this.isFlat ? (String) this.left : this.flatten();
    }

    private synchronized String flatten() {
        if (!this.isFlat) {
            char[] chars = new char[this.length];
            int charPos = this.length;
            ArrayDeque<CharSequence> stack = new ArrayDeque();
            stack.addFirst(this.left);
            CharSequence next = this.right;
            do {
                if (next instanceof ConsString casted) {
                    if (!casted.isFlat) {
                        stack.addFirst(casted.left);
                        next = casted.right;
                        continue;
                    }
                    next = casted.left;
                }
                String str = (String) next;
                charPos -= str.length();
                str.getChars(0, str.length(), chars, charPos);
                next = stack.isEmpty() ? null : (CharSequence) stack.removeFirst();
            } while (next != null);
            this.left = new String(chars);
            this.right = "";
            this.isFlat = true;
        }
        return (String) this.left;
    }

    public int length() {
        return this.length;
    }

    public char charAt(int index) {
        String str = this.isFlat ? (String) this.left : this.flatten();
        return str.charAt(index);
    }

    public CharSequence subSequence(int start, int end) {
        String str = this.isFlat ? (String) this.left : this.flatten();
        return str.substring(start, end);
    }
}
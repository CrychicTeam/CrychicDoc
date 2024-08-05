package com.mojang.realmsclient;

import java.util.Locale;

public enum Unit {

    B, KB, MB, GB;

    private static final int BASE_UNIT = 1024;

    public static Unit getLargest(long p_86941_) {
        if (p_86941_ < 1024L) {
            return B;
        } else {
            try {
                int $$1 = (int) (Math.log((double) p_86941_) / Math.log(1024.0));
                String $$2 = String.valueOf("KMGTPE".charAt($$1 - 1));
                return valueOf($$2 + "B");
            } catch (Exception var4) {
                return GB;
            }
        }
    }

    public static double convertTo(long p_86943_, Unit p_86944_) {
        return p_86944_ == B ? (double) p_86943_ : (double) p_86943_ / Math.pow(1024.0, (double) p_86944_.ordinal());
    }

    public static String humanReadable(long p_86946_) {
        int $$1 = 1024;
        if (p_86946_ < 1024L) {
            return p_86946_ + " B";
        } else {
            int $$2 = (int) (Math.log((double) p_86946_) / Math.log(1024.0));
            String $$3 = "KMGTPE".charAt($$2 - 1) + "";
            return String.format(Locale.ROOT, "%.1f %sB", (double) p_86946_ / Math.pow(1024.0, (double) $$2), $$3);
        }
    }

    public static String humanReadable(long p_86948_, Unit p_86949_) {
        return String.format(Locale.ROOT, "%." + (p_86949_ == GB ? "1" : "0") + "f %s", convertTo(p_86948_, p_86949_), p_86949_.name());
    }
}
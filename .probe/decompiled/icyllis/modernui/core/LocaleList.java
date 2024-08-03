package icyllis.modernui.core;

import icyllis.modernui.annotation.NonNull;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Locale;

public final class LocaleList {

    private static final Locale[] EMPTY_LOCALES = new Locale[0];

    private static final LocaleList EMPTY_LOCALE_LIST = new LocaleList();

    private final Locale[] mLocales;

    private final String mStringRepresentation;

    public LocaleList(@NonNull Locale... list) {
        if (list.length == 0) {
            this.mLocales = EMPTY_LOCALES;
            this.mStringRepresentation = "";
        } else {
            ArrayList<Locale> ll = new ArrayList();
            HashSet<Locale> dedup = new HashSet();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < list.length; i++) {
                Locale l = list[i];
                if (l == null) {
                    throw new NullPointerException("list[" + i + "] is null");
                }
                if (dedup.add(l)) {
                    Locale clone = (Locale) l.clone();
                    ll.add(clone);
                    sb.append(clone.toLanguageTag());
                    if (i < list.length - 1) {
                        sb.append(',');
                    }
                }
            }
            this.mLocales = (Locale[]) ll.toArray(new Locale[0]);
            this.mStringRepresentation = sb.toString();
        }
    }
}
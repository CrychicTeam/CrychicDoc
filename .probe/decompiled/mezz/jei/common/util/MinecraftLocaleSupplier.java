package mezz.jei.common.util;

import java.util.Locale;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.LanguageManager;

public class MinecraftLocaleSupplier implements Supplier<Locale> {

    @Nullable
    private String cachedLocaleCode;

    @Nullable
    private Locale cachedLocale;

    public Locale get() {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft == null) {
            return Locale.getDefault();
        } else {
            LanguageManager languageManager = minecraft.getLanguageManager();
            String code = languageManager.getSelected();
            if (this.cachedLocale == null || !code.equals(this.cachedLocaleCode)) {
                this.cachedLocaleCode = code;
                String[] splitLangCode = code.split("_", 2);
                if (splitLangCode.length == 1) {
                    this.cachedLocale = new Locale(code);
                } else {
                    this.cachedLocale = new Locale(splitLangCode[0], splitLangCode[1]);
                }
            }
            return this.cachedLocale;
        }
    }
}
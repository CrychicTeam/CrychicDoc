package com.mojang.realmsclient.exception;

import com.mojang.realmsclient.client.RealmsError;
import java.util.Locale;
import javax.annotation.Nullable;
import net.minecraft.client.resources.language.I18n;

public class RealmsServiceException extends Exception {

    public final int httpResultCode;

    public final String rawResponse;

    @Nullable
    public final RealmsError realmsError;

    public RealmsServiceException(int int0, String string1, RealmsError realmsError2) {
        super(string1);
        this.httpResultCode = int0;
        this.rawResponse = string1;
        this.realmsError = realmsError2;
    }

    public RealmsServiceException(int int0, String string1) {
        super(string1);
        this.httpResultCode = int0;
        this.rawResponse = string1;
        this.realmsError = null;
    }

    public String getMessage() {
        if (this.realmsError != null) {
            String $$0 = "mco.errorMessage." + this.realmsError.getErrorCode();
            String $$1 = I18n.exists($$0) ? I18n.get($$0) : this.realmsError.getErrorMessage();
            return String.format(Locale.ROOT, "Realms service error (%d/%d) %s", this.httpResultCode, this.realmsError.getErrorCode(), $$1);
        } else {
            return String.format(Locale.ROOT, "Realms service error (%d) %s", this.httpResultCode, this.rawResponse);
        }
    }

    public int realmsErrorCodeOrDefault(int int0) {
        return this.realmsError != null ? this.realmsError.getErrorCode() : int0;
    }
}
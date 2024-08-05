package snownee.kiwi.shadowed.com.ezylang.evalex.functions.datetime;

import java.time.DateTimeException;
import java.time.ZoneId;
import lombok.Generated;
import snownee.kiwi.shadowed.com.ezylang.evalex.EvaluationException;
import snownee.kiwi.shadowed.com.ezylang.evalex.parser.Token;

public class ZoneIdConverter {

    public static ZoneId convert(Token referenceToken, String zoneIdString) throws EvaluationException {
        try {
            return ZoneId.of(zoneIdString);
        } catch (DateTimeException var3) {
            throw new EvaluationException(referenceToken, String.format("Unable to convert zone string '%s' to a zone ID: %s", referenceToken.getValue(), var3.getMessage()));
        }
    }

    @Generated
    private ZoneIdConverter() {
    }
}
package snownee.kiwi.shadowed.com.ezylang.evalex.data.conversion;

import java.time.DateTimeException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalQueries;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import snownee.kiwi.shadowed.com.ezylang.evalex.config.ExpressionConfiguration;
import snownee.kiwi.shadowed.com.ezylang.evalex.data.EvaluationValue;

public class DateTimeConverter implements ConverterIfc {

    @Override
    public EvaluationValue convert(Object object, ExpressionConfiguration configuration) {
        Instant instant;
        if (object instanceof Instant) {
            instant = (Instant) object;
        } else if (object instanceof ZonedDateTime) {
            instant = ((ZonedDateTime) object).toInstant();
        } else if (object instanceof OffsetDateTime) {
            instant = ((OffsetDateTime) object).toInstant();
        } else if (object instanceof LocalDate) {
            instant = ((LocalDate) object).atStartOfDay().atZone(configuration.getZoneId()).toInstant();
        } else if (object instanceof LocalDateTime) {
            instant = ((LocalDateTime) object).atZone(configuration.getZoneId()).toInstant();
        } else if (object instanceof Date) {
            instant = ((Date) object).toInstant();
        } else {
            if (!(object instanceof Calendar)) {
                throw this.illegalArgument(object);
            }
            instant = ((Calendar) object).toInstant();
        }
        return EvaluationValue.dateTimeValue(instant);
    }

    public Instant parseDateTime(String value, ZoneId zoneId, List<DateTimeFormatter> formatters) {
        for (DateTimeFormatter formatter : formatters) {
            try {
                return this.parseToInstant(value, zoneId, formatter);
            } catch (DateTimeException var7) {
            }
        }
        return null;
    }

    private Instant parseToInstant(String value, ZoneId zoneId, DateTimeFormatter formatter) {
        TemporalAccessor ta = formatter.parse(value);
        ZoneId parsedZoneId = (ZoneId) ta.query(TemporalQueries.zone());
        if (parsedZoneId == null) {
            LocalDate parsedDate = (LocalDate) ta.query(TemporalQueries.localDate());
            LocalTime parsedTime = (LocalTime) ta.query(TemporalQueries.localTime());
            if (parsedTime == null) {
                parsedTime = parsedDate.atStartOfDay().toLocalTime();
            }
            ta = ZonedDateTime.of(parsedDate, parsedTime, zoneId);
        }
        return Instant.from(ta);
    }

    @Override
    public boolean canConvert(Object object) {
        return object instanceof Instant || object instanceof ZonedDateTime || object instanceof OffsetDateTime || object instanceof LocalDate || object instanceof LocalDateTime || object instanceof Date || object instanceof Calendar;
    }
}
declare module "packages/java/time/$Instant" {
import {$Comparable, $Comparable$Type} from "packages/java/lang/$Comparable"
import {$TemporalField, $TemporalField$Type} from "packages/java/time/temporal/$TemporalField"
import {$Serializable, $Serializable$Type} from "packages/java/io/$Serializable"
import {$TemporalAccessor, $TemporalAccessor$Type} from "packages/java/time/temporal/$TemporalAccessor"
import {$ZoneOffset, $ZoneOffset$Type} from "packages/java/time/$ZoneOffset"
import {$Clock, $Clock$Type} from "packages/java/time/$Clock"
import {$ValueRange, $ValueRange$Type} from "packages/java/time/temporal/$ValueRange"
import {$TemporalAmount, $TemporalAmount$Type} from "packages/java/time/temporal/$TemporalAmount"
import {$TemporalQuery, $TemporalQuery$Type} from "packages/java/time/temporal/$TemporalQuery"
import {$TemporalAdjuster, $TemporalAdjuster$Type} from "packages/java/time/temporal/$TemporalAdjuster"
import {$TemporalUnit, $TemporalUnit$Type} from "packages/java/time/temporal/$TemporalUnit"
import {$Temporal, $Temporal$Type} from "packages/java/time/temporal/$Temporal"
import {$ZoneId, $ZoneId$Type} from "packages/java/time/$ZoneId"
import {$ZonedDateTime, $ZonedDateTime$Type} from "packages/java/time/$ZonedDateTime"
import {$OffsetDateTime, $OffsetDateTime$Type} from "packages/java/time/$OffsetDateTime"

export class $Instant implements $Temporal, $TemporalAdjuster, $Comparable<($Instant)>, $Serializable {
static readonly "EPOCH": $Instant
static readonly "MIN": $Instant
static readonly "MAX": $Instant


public "get"(arg0: $TemporalField$Type): integer
public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "compareTo"(arg0: $Instant$Type): integer
public "getLong"(arg0: $TemporalField$Type): long
public static "from"(arg0: $TemporalAccessor$Type): $Instant
public "query"<R>(arg0: $TemporalQuery$Type<(R)>): R
public "range"(arg0: $TemporalField$Type): $ValueRange
public "isSupported"(arg0: $TemporalField$Type): boolean
public "isSupported"(arg0: $TemporalUnit$Type): boolean
public static "parse"(arg0: charseq): $Instant
public "plus"(arg0: long, arg1: $TemporalUnit$Type): $Instant
public static "now"(): $Instant
public static "now"(arg0: $Clock$Type): $Instant
public "getEpochSecond"(): long
public "getNano"(): integer
public static "ofEpochSecond"(arg0: long): $Instant
public static "ofEpochSecond"(arg0: long, arg1: long): $Instant
public "minus"(arg0: $TemporalAmount$Type): $Instant
public "toEpochMilli"(): long
public "until"(arg0: $Temporal$Type, arg1: $TemporalUnit$Type): long
public "plusNanos"(arg0: long): $Instant
public "plusSeconds"(arg0: long): $Instant
public "plusMillis"(arg0: long): $Instant
public "minusSeconds"(arg0: long): $Instant
public "minusMillis"(arg0: long): $Instant
public "minusNanos"(arg0: long): $Instant
public "truncatedTo"(arg0: $TemporalUnit$Type): $Instant
public "adjustInto"(arg0: $Temporal$Type): $Temporal
public static "ofEpochMilli"(arg0: long): $Instant
public "atOffset"(arg0: $ZoneOffset$Type): $OffsetDateTime
public "atZone"(arg0: $ZoneId$Type): $ZonedDateTime
public "isAfter"(arg0: $Instant$Type): boolean
public "isBefore"(arg0: $Instant$Type): boolean
get "epochSecond"(): long
get "nano"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Instant$Type = ($Instant);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Instant_ = $Instant$Type;
}}
declare module "packages/java/time/zone/$ZoneOffsetTransitionRule" {
import {$ZoneOffsetTransitionRule$TimeDefinition, $ZoneOffsetTransitionRule$TimeDefinition$Type} from "packages/java/time/zone/$ZoneOffsetTransitionRule$TimeDefinition"
import {$Serializable, $Serializable$Type} from "packages/java/io/$Serializable"
import {$Month, $Month$Type} from "packages/java/time/$Month"
import {$DayOfWeek, $DayOfWeek$Type} from "packages/java/time/$DayOfWeek"
import {$ZoneOffset, $ZoneOffset$Type} from "packages/java/time/$ZoneOffset"
import {$LocalTime, $LocalTime$Type} from "packages/java/time/$LocalTime"
import {$ZoneOffsetTransition, $ZoneOffsetTransition$Type} from "packages/java/time/zone/$ZoneOffsetTransition"

export class $ZoneOffsetTransitionRule implements $Serializable {


public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public static "of"(arg0: $Month$Type, arg1: integer, arg2: $DayOfWeek$Type, arg3: $LocalTime$Type, arg4: boolean, arg5: $ZoneOffsetTransitionRule$TimeDefinition$Type, arg6: $ZoneOffset$Type, arg7: $ZoneOffset$Type, arg8: $ZoneOffset$Type): $ZoneOffsetTransitionRule
public "getMonth"(): $Month
public "getDayOfWeek"(): $DayOfWeek
public "getDayOfMonthIndicator"(): integer
public "getLocalTime"(): $LocalTime
public "isMidnightEndOfDay"(): boolean
public "getTimeDefinition"(): $ZoneOffsetTransitionRule$TimeDefinition
public "getStandardOffset"(): $ZoneOffset
public "getOffsetBefore"(): $ZoneOffset
public "getOffsetAfter"(): $ZoneOffset
public "createTransition"(arg0: integer): $ZoneOffsetTransition
get "month"(): $Month
get "dayOfWeek"(): $DayOfWeek
get "dayOfMonthIndicator"(): integer
get "localTime"(): $LocalTime
get "midnightEndOfDay"(): boolean
get "timeDefinition"(): $ZoneOffsetTransitionRule$TimeDefinition
get "standardOffset"(): $ZoneOffset
get "offsetBefore"(): $ZoneOffset
get "offsetAfter"(): $ZoneOffset
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ZoneOffsetTransitionRule$Type = ($ZoneOffsetTransitionRule);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ZoneOffsetTransitionRule_ = $ZoneOffsetTransitionRule$Type;
}}
declare module "packages/java/time/temporal/$TemporalQuery" {
import {$TemporalAccessor, $TemporalAccessor$Type} from "packages/java/time/temporal/$TemporalAccessor"

export interface $TemporalQuery<R> {

 "queryFrom"(arg0: $TemporalAccessor$Type): R

(arg0: $TemporalAccessor$Type): R
}

export namespace $TemporalQuery {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TemporalQuery$Type<R> = ($TemporalQuery<(R)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TemporalQuery_<R> = $TemporalQuery$Type<(R)>;
}}
declare module "packages/java/time/chrono/$Chronology" {
import {$Comparable, $Comparable$Type} from "packages/java/lang/$Comparable"
import {$TemporalField, $TemporalField$Type} from "packages/java/time/temporal/$TemporalField"
import {$ChronoZonedDateTime, $ChronoZonedDateTime$Type} from "packages/java/time/chrono/$ChronoZonedDateTime"
import {$Instant, $Instant$Type} from "packages/java/time/$Instant"
import {$ChronoField, $ChronoField$Type} from "packages/java/time/temporal/$ChronoField"
import {$TemporalAccessor, $TemporalAccessor$Type} from "packages/java/time/temporal/$TemporalAccessor"
import {$ZoneOffset, $ZoneOffset$Type} from "packages/java/time/$ZoneOffset"
import {$Clock, $Clock$Type} from "packages/java/time/$Clock"
import {$ValueRange, $ValueRange$Type} from "packages/java/time/temporal/$ValueRange"
import {$ChronoLocalDate, $ChronoLocalDate$Type} from "packages/java/time/chrono/$ChronoLocalDate"
import {$Locale, $Locale$Type} from "packages/java/util/$Locale"
import {$TextStyle, $TextStyle$Type} from "packages/java/time/format/$TextStyle"
import {$Era, $Era$Type} from "packages/java/time/chrono/$Era"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ZoneId, $ZoneId$Type} from "packages/java/time/$ZoneId"
import {$ChronoPeriod, $ChronoPeriod$Type} from "packages/java/time/chrono/$ChronoPeriod"
import {$ChronoLocalDateTime, $ChronoLocalDateTime$Type} from "packages/java/time/chrono/$ChronoLocalDateTime"
import {$ResolverStyle, $ResolverStyle$Type} from "packages/java/time/format/$ResolverStyle"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export interface $Chronology extends $Comparable<($Chronology)> {

 "equals"(arg0: any): boolean
 "toString"(): string
 "hashCode"(): integer
 "compareTo"(arg0: $Chronology$Type): integer
 "getId"(): string
 "range"(arg0: $ChronoField$Type): $ValueRange
 "getDisplayName"(arg0: $TextStyle$Type, arg1: $Locale$Type): string
 "date"(arg0: integer, arg1: integer, arg2: integer): $ChronoLocalDate
 "date"(arg0: $TemporalAccessor$Type): $ChronoLocalDate
 "date"(arg0: $Era$Type, arg1: integer, arg2: integer, arg3: integer): $ChronoLocalDate
 "eras"(): $List<($Era)>
 "epochSecond"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: $ZoneOffset$Type): long
 "epochSecond"(arg0: $Era$Type, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: $ZoneOffset$Type): long
 "isLeapYear"(arg0: long): boolean
 "eraOf"(arg0: integer): $Era
 "localDateTime"(arg0: $TemporalAccessor$Type): $ChronoLocalDateTime<(any)>
 "getCalendarType"(): string
 "dateEpochDay"(arg0: long): $ChronoLocalDate
 "dateNow"(): $ChronoLocalDate
 "dateNow"(arg0: $ZoneId$Type): $ChronoLocalDate
 "dateNow"(arg0: $Clock$Type): $ChronoLocalDate
 "prolepticYear"(arg0: $Era$Type, arg1: integer): integer
 "dateYearDay"(arg0: $Era$Type, arg1: integer, arg2: integer): $ChronoLocalDate
 "dateYearDay"(arg0: integer, arg1: integer): $ChronoLocalDate
 "resolveDate"(arg0: $Map$Type<($TemporalField$Type), (long)>, arg1: $ResolverStyle$Type): $ChronoLocalDate
 "zonedDateTime"(arg0: $TemporalAccessor$Type): $ChronoZonedDateTime<(any)>
 "zonedDateTime"(arg0: $Instant$Type, arg1: $ZoneId$Type): $ChronoZonedDateTime<(any)>
 "period"(arg0: integer, arg1: integer, arg2: integer): $ChronoPeriod
}

export namespace $Chronology {
function of(arg0: string): $Chronology
function from(arg0: $TemporalAccessor$Type): $Chronology
function ofLocale(arg0: $Locale$Type): $Chronology
function getAvailableChronologies(): $Set<($Chronology)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Chronology$Type = ($Chronology);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Chronology_ = $Chronology$Type;
}}
declare module "packages/java/time/chrono/$ChronoLocalDate" {
import {$Comparator, $Comparator$Type} from "packages/java/util/$Comparator"
import {$Comparable, $Comparable$Type} from "packages/java/lang/$Comparable"
import {$TemporalField, $TemporalField$Type} from "packages/java/time/temporal/$TemporalField"
import {$TemporalAccessor, $TemporalAccessor$Type} from "packages/java/time/temporal/$TemporalAccessor"
import {$ValueRange, $ValueRange$Type} from "packages/java/time/temporal/$ValueRange"
import {$Chronology, $Chronology$Type} from "packages/java/time/chrono/$Chronology"
import {$TemporalQuery, $TemporalQuery$Type} from "packages/java/time/temporal/$TemporalQuery"
import {$Era, $Era$Type} from "packages/java/time/chrono/$Era"
import {$TemporalAdjuster, $TemporalAdjuster$Type} from "packages/java/time/temporal/$TemporalAdjuster"
import {$DateTimeFormatter, $DateTimeFormatter$Type} from "packages/java/time/format/$DateTimeFormatter"
import {$TemporalUnit, $TemporalUnit$Type} from "packages/java/time/temporal/$TemporalUnit"
import {$Temporal, $Temporal$Type} from "packages/java/time/temporal/$Temporal"
import {$ChronoPeriod, $ChronoPeriod$Type} from "packages/java/time/chrono/$ChronoPeriod"
import {$LocalTime, $LocalTime$Type} from "packages/java/time/$LocalTime"
import {$ChronoLocalDateTime, $ChronoLocalDateTime$Type} from "packages/java/time/chrono/$ChronoLocalDateTime"

export interface $ChronoLocalDate extends $Temporal, $TemporalAdjuster, $Comparable<($ChronoLocalDate)> {

 "equals"(arg0: any): boolean
 "toString"(): string
 "hashCode"(): integer
 "compareTo"(arg0: $ChronoLocalDate$Type): integer
 "format"(arg0: $DateTimeFormatter$Type): string
 "query"<R>(arg0: $TemporalQuery$Type<(R)>): R
 "isSupported"(arg0: $TemporalUnit$Type): boolean
 "isSupported"(arg0: $TemporalField$Type): boolean
 "until"(arg0: $ChronoLocalDate$Type): $ChronoPeriod
 "until"(arg0: $Temporal$Type, arg1: $TemporalUnit$Type): long
 "adjustInto"(arg0: $Temporal$Type): $Temporal
 "with"(arg0: $TemporalField$Type, arg1: long): $ChronoLocalDate
 "isAfter"(arg0: $ChronoLocalDate$Type): boolean
 "isBefore"(arg0: $ChronoLocalDate$Type): boolean
 "isLeapYear"(): boolean
 "lengthOfMonth"(): integer
 "lengthOfYear"(): integer
 "toEpochDay"(): long
 "atTime"(arg0: $LocalTime$Type): $ChronoLocalDateTime<(any)>
 "isEqual"(arg0: $ChronoLocalDate$Type): boolean
 "getEra"(): $Era
 "getChronology"(): $Chronology
 "get"(arg0: $TemporalField$Type): integer
 "getLong"(arg0: $TemporalField$Type): long
 "range"(arg0: $TemporalField$Type): $ValueRange
}

export namespace $ChronoLocalDate {
function from(arg0: $TemporalAccessor$Type): $ChronoLocalDate
function timeLineOrder(): $Comparator<($ChronoLocalDate)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ChronoLocalDate$Type = ($ChronoLocalDate);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ChronoLocalDate_ = $ChronoLocalDate$Type;
}}
declare module "packages/java/time/format/$DateTimeFormatter" {
import {$FormatStyle, $FormatStyle$Type} from "packages/java/time/format/$FormatStyle"
import {$TemporalField, $TemporalField$Type} from "packages/java/time/temporal/$TemporalField"
import {$DecimalStyle, $DecimalStyle$Type} from "packages/java/time/format/$DecimalStyle"
import {$Appendable, $Appendable$Type} from "packages/java/lang/$Appendable"
import {$Format, $Format$Type} from "packages/java/text/$Format"
import {$TemporalAccessor, $TemporalAccessor$Type} from "packages/java/time/temporal/$TemporalAccessor"
import {$Chronology, $Chronology$Type} from "packages/java/time/chrono/$Chronology"
import {$Locale, $Locale$Type} from "packages/java/util/$Locale"
import {$TemporalQuery, $TemporalQuery$Type} from "packages/java/time/temporal/$TemporalQuery"
import {$Period, $Period$Type} from "packages/java/time/$Period"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$ZoneId, $ZoneId$Type} from "packages/java/time/$ZoneId"
import {$ParsePosition, $ParsePosition$Type} from "packages/java/text/$ParsePosition"
import {$ResolverStyle, $ResolverStyle$Type} from "packages/java/time/format/$ResolverStyle"

export class $DateTimeFormatter {
static readonly "ISO_LOCAL_DATE": $DateTimeFormatter
static readonly "ISO_OFFSET_DATE": $DateTimeFormatter
static readonly "ISO_DATE": $DateTimeFormatter
static readonly "ISO_LOCAL_TIME": $DateTimeFormatter
static readonly "ISO_OFFSET_TIME": $DateTimeFormatter
static readonly "ISO_TIME": $DateTimeFormatter
static readonly "ISO_LOCAL_DATE_TIME": $DateTimeFormatter
static readonly "ISO_OFFSET_DATE_TIME": $DateTimeFormatter
static readonly "ISO_ZONED_DATE_TIME": $DateTimeFormatter
static readonly "ISO_DATE_TIME": $DateTimeFormatter
static readonly "ISO_ORDINAL_DATE": $DateTimeFormatter
static readonly "ISO_WEEK_DATE": $DateTimeFormatter
static readonly "ISO_INSTANT": $DateTimeFormatter
static readonly "BASIC_ISO_DATE": $DateTimeFormatter
static readonly "RFC_1123_DATE_TIME": $DateTimeFormatter


public "toString"(): string
public "format"(arg0: $TemporalAccessor$Type): string
public "parse"(arg0: charseq, arg1: $ParsePosition$Type): $TemporalAccessor
public "parse"(arg0: charseq): $TemporalAccessor
public "parse"<T>(arg0: charseq, arg1: $TemporalQuery$Type<(T)>): T
public "getZone"(): $ZoneId
public "withZone"(arg0: $ZoneId$Type): $DateTimeFormatter
public "getChronology"(): $Chronology
public "formatTo"(arg0: $TemporalAccessor$Type, arg1: $Appendable$Type): void
public static "ofPattern"(arg0: string): $DateTimeFormatter
public static "ofPattern"(arg0: string, arg1: $Locale$Type): $DateTimeFormatter
public static "ofLocalizedDate"(arg0: $FormatStyle$Type): $DateTimeFormatter
public static "ofLocalizedTime"(arg0: $FormatStyle$Type): $DateTimeFormatter
public static "ofLocalizedDateTime"(arg0: $FormatStyle$Type): $DateTimeFormatter
public static "ofLocalizedDateTime"(arg0: $FormatStyle$Type, arg1: $FormatStyle$Type): $DateTimeFormatter
public static "parsedExcessDays"(): $TemporalQuery<($Period)>
public static "parsedLeapSecond"(): $TemporalQuery<(boolean)>
public "getLocale"(): $Locale
public "withLocale"(arg0: $Locale$Type): $DateTimeFormatter
public "localizedBy"(arg0: $Locale$Type): $DateTimeFormatter
public "getDecimalStyle"(): $DecimalStyle
public "withDecimalStyle"(arg0: $DecimalStyle$Type): $DateTimeFormatter
public "withChronology"(arg0: $Chronology$Type): $DateTimeFormatter
public "getResolverStyle"(): $ResolverStyle
public "withResolverStyle"(arg0: $ResolverStyle$Type): $DateTimeFormatter
public "getResolverFields"(): $Set<($TemporalField)>
public "withResolverFields"(...arg0: ($TemporalField$Type)[]): $DateTimeFormatter
public "withResolverFields"(arg0: $Set$Type<($TemporalField$Type)>): $DateTimeFormatter
public "parseBest"(arg0: charseq, ...arg1: ($TemporalQuery$Type<(any)>)[]): $TemporalAccessor
public "parseUnresolved"(arg0: charseq, arg1: $ParsePosition$Type): $TemporalAccessor
public "toFormat"(): $Format
public "toFormat"(arg0: $TemporalQuery$Type<(any)>): $Format
get "zone"(): $ZoneId
get "chronology"(): $Chronology
get "locale"(): $Locale
get "decimalStyle"(): $DecimalStyle
get "resolverStyle"(): $ResolverStyle
get "resolverFields"(): $Set<($TemporalField)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DateTimeFormatter$Type = ($DateTimeFormatter);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DateTimeFormatter_ = $DateTimeFormatter$Type;
}}
declare module "packages/java/time/temporal/$TemporalAdjuster" {
import {$Temporal, $Temporal$Type} from "packages/java/time/temporal/$Temporal"

export interface $TemporalAdjuster {

 "adjustInto"(arg0: $Temporal$Type): $Temporal

(arg0: $Temporal$Type): $Temporal
}

export namespace $TemporalAdjuster {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TemporalAdjuster$Type = ($TemporalAdjuster);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TemporalAdjuster_ = $TemporalAdjuster$Type;
}}
declare module "packages/java/time/zone/$ZoneRules" {
import {$Serializable, $Serializable$Type} from "packages/java/io/$Serializable"
import {$ZoneOffsetTransitionRule, $ZoneOffsetTransitionRule$Type} from "packages/java/time/zone/$ZoneOffsetTransitionRule"
import {$Instant, $Instant$Type} from "packages/java/time/$Instant"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ZoneOffset, $ZoneOffset$Type} from "packages/java/time/$ZoneOffset"
import {$LocalDateTime, $LocalDateTime$Type} from "packages/java/time/$LocalDateTime"
import {$ZoneOffsetTransition, $ZoneOffsetTransition$Type} from "packages/java/time/zone/$ZoneOffsetTransition"
import {$Duration, $Duration$Type} from "packages/java/time/$Duration"

export class $ZoneRules implements $Serializable {


public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public static "of"(arg0: $ZoneOffset$Type, arg1: $ZoneOffset$Type, arg2: $List$Type<($ZoneOffsetTransition$Type)>, arg3: $List$Type<($ZoneOffsetTransition$Type)>, arg4: $List$Type<($ZoneOffsetTransitionRule$Type)>): $ZoneRules
public static "of"(arg0: $ZoneOffset$Type): $ZoneRules
public "getOffset"(arg0: $LocalDateTime$Type): $ZoneOffset
public "getOffset"(arg0: $Instant$Type): $ZoneOffset
public "getTransition"(arg0: $LocalDateTime$Type): $ZoneOffsetTransition
public "isFixedOffset"(): boolean
public "getStandardOffset"(arg0: $Instant$Type): $ZoneOffset
public "getValidOffsets"(arg0: $LocalDateTime$Type): $List<($ZoneOffset)>
public "getDaylightSavings"(arg0: $Instant$Type): $Duration
public "isDaylightSavings"(arg0: $Instant$Type): boolean
public "isValidOffset"(arg0: $LocalDateTime$Type, arg1: $ZoneOffset$Type): boolean
public "nextTransition"(arg0: $Instant$Type): $ZoneOffsetTransition
public "previousTransition"(arg0: $Instant$Type): $ZoneOffsetTransition
public "getTransitions"(): $List<($ZoneOffsetTransition)>
public "getTransitionRules"(): $List<($ZoneOffsetTransitionRule)>
get "fixedOffset"(): boolean
get "transitions"(): $List<($ZoneOffsetTransition)>
get "transitionRules"(): $List<($ZoneOffsetTransitionRule)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ZoneRules$Type = ($ZoneRules);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ZoneRules_ = $ZoneRules$Type;
}}
declare module "packages/java/time/temporal/$Temporal" {
import {$TemporalAmount, $TemporalAmount$Type} from "packages/java/time/temporal/$TemporalAmount"
import {$TemporalQuery, $TemporalQuery$Type} from "packages/java/time/temporal/$TemporalQuery"
import {$TemporalField, $TemporalField$Type} from "packages/java/time/temporal/$TemporalField"
import {$TemporalAdjuster, $TemporalAdjuster$Type} from "packages/java/time/temporal/$TemporalAdjuster"
import {$TemporalUnit, $TemporalUnit$Type} from "packages/java/time/temporal/$TemporalUnit"
import {$TemporalAccessor, $TemporalAccessor$Type} from "packages/java/time/temporal/$TemporalAccessor"
import {$ValueRange, $ValueRange$Type} from "packages/java/time/temporal/$ValueRange"

export interface $Temporal extends $TemporalAccessor {

 "isSupported"(arg0: $TemporalUnit$Type): boolean
 "plus"(arg0: long, arg1: $TemporalUnit$Type): $Temporal
 "plus"(arg0: $TemporalAmount$Type): $Temporal
 "minus"(arg0: $TemporalAmount$Type): $Temporal
 "minus"(arg0: long, arg1: $TemporalUnit$Type): $Temporal
 "until"(arg0: $Temporal$Type, arg1: $TemporalUnit$Type): long
 "with"(arg0: $TemporalAdjuster$Type): $Temporal
 "with"(arg0: $TemporalField$Type, arg1: long): $Temporal
 "get"(arg0: $TemporalField$Type): integer
 "getLong"(arg0: $TemporalField$Type): long
 "query"<R>(arg0: $TemporalQuery$Type<(R)>): R
 "range"(arg0: $TemporalField$Type): $ValueRange
 "isSupported"(arg0: $TemporalField$Type): boolean
}

export namespace $Temporal {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Temporal$Type = ($Temporal);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Temporal_ = $Temporal$Type;
}}
declare module "packages/java/time/format/$ResolverStyle" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $ResolverStyle extends $Enum<($ResolverStyle)> {
static readonly "STRICT": $ResolverStyle
static readonly "SMART": $ResolverStyle
static readonly "LENIENT": $ResolverStyle


public static "values"(): ($ResolverStyle)[]
public static "valueOf"(arg0: string): $ResolverStyle
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ResolverStyle$Type = (("strict") | ("smart") | ("lenient")) | ($ResolverStyle);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ResolverStyle_ = $ResolverStyle$Type;
}}
declare module "packages/java/time/temporal/$ChronoField" {
import {$TemporalField, $TemporalField$Type} from "packages/java/time/temporal/$TemporalField"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$TemporalUnit, $TemporalUnit$Type} from "packages/java/time/temporal/$TemporalUnit"
import {$Temporal, $Temporal$Type} from "packages/java/time/temporal/$Temporal"
import {$TemporalAccessor, $TemporalAccessor$Type} from "packages/java/time/temporal/$TemporalAccessor"
import {$ValueRange, $ValueRange$Type} from "packages/java/time/temporal/$ValueRange"
import {$ResolverStyle, $ResolverStyle$Type} from "packages/java/time/format/$ResolverStyle"
import {$Locale, $Locale$Type} from "packages/java/util/$Locale"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $ChronoField extends $Enum<($ChronoField)> implements $TemporalField {
static readonly "NANO_OF_SECOND": $ChronoField
static readonly "NANO_OF_DAY": $ChronoField
static readonly "MICRO_OF_SECOND": $ChronoField
static readonly "MICRO_OF_DAY": $ChronoField
static readonly "MILLI_OF_SECOND": $ChronoField
static readonly "MILLI_OF_DAY": $ChronoField
static readonly "SECOND_OF_MINUTE": $ChronoField
static readonly "SECOND_OF_DAY": $ChronoField
static readonly "MINUTE_OF_HOUR": $ChronoField
static readonly "MINUTE_OF_DAY": $ChronoField
static readonly "HOUR_OF_AMPM": $ChronoField
static readonly "CLOCK_HOUR_OF_AMPM": $ChronoField
static readonly "HOUR_OF_DAY": $ChronoField
static readonly "CLOCK_HOUR_OF_DAY": $ChronoField
static readonly "AMPM_OF_DAY": $ChronoField
static readonly "DAY_OF_WEEK": $ChronoField
static readonly "ALIGNED_DAY_OF_WEEK_IN_MONTH": $ChronoField
static readonly "ALIGNED_DAY_OF_WEEK_IN_YEAR": $ChronoField
static readonly "DAY_OF_MONTH": $ChronoField
static readonly "DAY_OF_YEAR": $ChronoField
static readonly "EPOCH_DAY": $ChronoField
static readonly "ALIGNED_WEEK_OF_MONTH": $ChronoField
static readonly "ALIGNED_WEEK_OF_YEAR": $ChronoField
static readonly "MONTH_OF_YEAR": $ChronoField
static readonly "PROLEPTIC_MONTH": $ChronoField
static readonly "YEAR_OF_ERA": $ChronoField
static readonly "YEAR": $ChronoField
static readonly "ERA": $ChronoField
static readonly "INSTANT_SECONDS": $ChronoField
static readonly "OFFSET_SECONDS": $ChronoField


public "toString"(): string
public static "values"(): ($ChronoField)[]
public static "valueOf"(arg0: string): $ChronoField
public "range"(): $ValueRange
public "getDisplayName"(arg0: $Locale$Type): string
public "checkValidIntValue"(arg0: long): integer
public "isSupportedBy"(arg0: $TemporalAccessor$Type): boolean
public "isTimeBased"(): boolean
public "getFrom"(arg0: $TemporalAccessor$Type): long
public "adjustInto"<R extends $Temporal>(arg0: R, arg1: long): R
public "checkValidValue"(arg0: long): long
public "rangeRefinedBy"(arg0: $TemporalAccessor$Type): $ValueRange
public "isDateBased"(): boolean
public "getBaseUnit"(): $TemporalUnit
public "getRangeUnit"(): $TemporalUnit
public "resolve"(arg0: $Map$Type<($TemporalField$Type), (long)>, arg1: $TemporalAccessor$Type, arg2: $ResolverStyle$Type): $TemporalAccessor
get "timeBased"(): boolean
get "dateBased"(): boolean
get "baseUnit"(): $TemporalUnit
get "rangeUnit"(): $TemporalUnit
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ChronoField$Type = (("year_of_era") | ("micro_of_second") | ("year") | ("minute_of_day") | ("minute_of_hour") | ("epoch_day") | ("day_of_month") | ("hour_of_ampm") | ("offset_seconds") | ("milli_of_second") | ("nano_of_second") | ("proleptic_month") | ("era") | ("second_of_minute") | ("aligned_day_of_week_in_year") | ("micro_of_day") | ("nano_of_day") | ("hour_of_day") | ("day_of_week") | ("second_of_day") | ("day_of_year") | ("month_of_year") | ("ampm_of_day") | ("milli_of_day") | ("aligned_week_of_month") | ("instant_seconds") | ("aligned_day_of_week_in_month") | ("aligned_week_of_year") | ("clock_hour_of_day") | ("clock_hour_of_ampm")) | ($ChronoField);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ChronoField_ = $ChronoField$Type;
}}
declare module "packages/java/time/zone/$ZoneOffsetTransition" {
import {$Comparable, $Comparable$Type} from "packages/java/lang/$Comparable"
import {$Serializable, $Serializable$Type} from "packages/java/io/$Serializable"
import {$Instant, $Instant$Type} from "packages/java/time/$Instant"
import {$ZoneOffset, $ZoneOffset$Type} from "packages/java/time/$ZoneOffset"
import {$LocalDateTime, $LocalDateTime$Type} from "packages/java/time/$LocalDateTime"
import {$Duration, $Duration$Type} from "packages/java/time/$Duration"

export class $ZoneOffsetTransition implements $Comparable<($ZoneOffsetTransition)>, $Serializable {


public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "compareTo"(arg0: $ZoneOffsetTransition$Type): integer
public static "of"(arg0: $LocalDateTime$Type, arg1: $ZoneOffset$Type, arg2: $ZoneOffset$Type): $ZoneOffsetTransition
public "getDuration"(): $Duration
public "isGap"(): boolean
public "getDateTimeAfter"(): $LocalDateTime
public "toEpochSecond"(): long
public "getOffsetBefore"(): $ZoneOffset
public "getOffsetAfter"(): $ZoneOffset
public "getDateTimeBefore"(): $LocalDateTime
public "isValidOffset"(arg0: $ZoneOffset$Type): boolean
public "getInstant"(): $Instant
public "isOverlap"(): boolean
get "duration"(): $Duration
get "gap"(): boolean
get "dateTimeAfter"(): $LocalDateTime
get "offsetBefore"(): $ZoneOffset
get "offsetAfter"(): $ZoneOffset
get "dateTimeBefore"(): $LocalDateTime
get "instant"(): $Instant
get "overlap"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ZoneOffsetTransition$Type = ($ZoneOffsetTransition);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ZoneOffsetTransition_ = $ZoneOffsetTransition$Type;
}}
declare module "packages/java/time/temporal/$TemporalUnit" {
import {$Temporal, $Temporal$Type} from "packages/java/time/temporal/$Temporal"
import {$Duration, $Duration$Type} from "packages/java/time/$Duration"

export interface $TemporalUnit {

 "toString"(): string
 "between"(arg0: $Temporal$Type, arg1: $Temporal$Type): long
 "isDurationEstimated"(): boolean
 "getDuration"(): $Duration
 "addTo"<R extends $Temporal>(arg0: R, arg1: long): R
 "isSupportedBy"(arg0: $Temporal$Type): boolean
 "isTimeBased"(): boolean
 "isDateBased"(): boolean
}

export namespace $TemporalUnit {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TemporalUnit$Type = ($TemporalUnit);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TemporalUnit_ = $TemporalUnit$Type;
}}
declare module "packages/java/time/$LocalDateTime" {
import {$Comparator, $Comparator$Type} from "packages/java/util/$Comparator"
import {$TemporalField, $TemporalField$Type} from "packages/java/time/temporal/$TemporalField"
import {$Serializable, $Serializable$Type} from "packages/java/io/$Serializable"
import {$Instant, $Instant$Type} from "packages/java/time/$Instant"
import {$LocalDate, $LocalDate$Type} from "packages/java/time/$LocalDate"
import {$TemporalAccessor, $TemporalAccessor$Type} from "packages/java/time/temporal/$TemporalAccessor"
import {$ZoneOffset, $ZoneOffset$Type} from "packages/java/time/$ZoneOffset"
import {$DayOfWeek, $DayOfWeek$Type} from "packages/java/time/$DayOfWeek"
import {$Clock, $Clock$Type} from "packages/java/time/$Clock"
import {$ValueRange, $ValueRange$Type} from "packages/java/time/temporal/$ValueRange"
import {$Chronology, $Chronology$Type} from "packages/java/time/chrono/$Chronology"
import {$TemporalAmount, $TemporalAmount$Type} from "packages/java/time/temporal/$TemporalAmount"
import {$TemporalQuery, $TemporalQuery$Type} from "packages/java/time/temporal/$TemporalQuery"
import {$Month, $Month$Type} from "packages/java/time/$Month"
import {$TemporalAdjuster, $TemporalAdjuster$Type} from "packages/java/time/temporal/$TemporalAdjuster"
import {$DateTimeFormatter, $DateTimeFormatter$Type} from "packages/java/time/format/$DateTimeFormatter"
import {$TemporalUnit, $TemporalUnit$Type} from "packages/java/time/temporal/$TemporalUnit"
import {$ZoneId, $ZoneId$Type} from "packages/java/time/$ZoneId"
import {$Temporal, $Temporal$Type} from "packages/java/time/temporal/$Temporal"
import {$LocalTime, $LocalTime$Type} from "packages/java/time/$LocalTime"
import {$OffsetDateTime, $OffsetDateTime$Type} from "packages/java/time/$OffsetDateTime"
import {$ChronoLocalDateTime, $ChronoLocalDateTime$Type} from "packages/java/time/chrono/$ChronoLocalDateTime"

export class $LocalDateTime implements $Temporal, $TemporalAdjuster, $ChronoLocalDateTime<($LocalDate)>, $Serializable {
static readonly "MIN": $LocalDateTime
static readonly "MAX": $LocalDateTime


public "get"(arg0: $TemporalField$Type): integer
public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "compareTo"(arg0: $ChronoLocalDateTime$Type<(any)>): integer
public "getLong"(arg0: $TemporalField$Type): long
public "format"(arg0: $DateTimeFormatter$Type): string
public static "of"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer): $LocalDateTime
public static "of"(arg0: integer, arg1: $Month$Type, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer): $LocalDateTime
public static "of"(arg0: integer, arg1: $Month$Type, arg2: integer, arg3: integer, arg4: integer, arg5: integer): $LocalDateTime
public static "of"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer): $LocalDateTime
public static "of"(arg0: $LocalDate$Type, arg1: $LocalTime$Type): $LocalDateTime
public static "of"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer): $LocalDateTime
public static "of"(arg0: integer, arg1: $Month$Type, arg2: integer, arg3: integer, arg4: integer): $LocalDateTime
public static "from"(arg0: $TemporalAccessor$Type): $LocalDateTime
public "query"<R>(arg0: $TemporalQuery$Type<(R)>): R
public "range"(arg0: $TemporalField$Type): $ValueRange
public "isSupported"(arg0: $TemporalUnit$Type): boolean
public "isSupported"(arg0: $TemporalField$Type): boolean
public static "parse"(arg0: charseq, arg1: $DateTimeFormatter$Type): $LocalDateTime
public static "parse"(arg0: charseq): $LocalDateTime
public "plus"(arg0: long, arg1: $TemporalUnit$Type): $LocalDateTime
public "plus"(arg0: $TemporalAmount$Type): $LocalDateTime
public static "now"(): $LocalDateTime
public static "now"(arg0: $Clock$Type): $LocalDateTime
public static "now"(arg0: $ZoneId$Type): $LocalDateTime
public "getNano"(): integer
public static "ofEpochSecond"(arg0: long, arg1: integer, arg2: $ZoneOffset$Type): $LocalDateTime
public "getYear"(): integer
public "getMonthValue"(): integer
public "getDayOfMonth"(): integer
public "getHour"(): integer
public "getMinute"(): integer
public "getSecond"(): integer
public "minus"(arg0: $TemporalAmount$Type): $LocalDateTime
public "until"(arg0: $Temporal$Type, arg1: $TemporalUnit$Type): long
public "plusNanos"(arg0: long): $LocalDateTime
public "plusSeconds"(arg0: long): $LocalDateTime
public "plusDays"(arg0: long): $LocalDateTime
public "plusHours"(arg0: long): $LocalDateTime
public "plusMinutes"(arg0: long): $LocalDateTime
public "minusDays"(arg0: long): $LocalDateTime
public "minusHours"(arg0: long): $LocalDateTime
public "minusMinutes"(arg0: long): $LocalDateTime
public "minusSeconds"(arg0: long): $LocalDateTime
public "minusNanos"(arg0: long): $LocalDateTime
public "truncatedTo"(arg0: $TemporalUnit$Type): $LocalDateTime
public "adjustInto"(arg0: $Temporal$Type): $Temporal
public "with"(arg0: $TemporalField$Type, arg1: long): $LocalDateTime
public static "ofInstant"(arg0: $Instant$Type, arg1: $ZoneId$Type): $LocalDateTime
public "atOffset"(arg0: $ZoneOffset$Type): $OffsetDateTime
public "isAfter"(arg0: $ChronoLocalDateTime$Type<(any)>): boolean
public "isBefore"(arg0: $ChronoLocalDateTime$Type<(any)>): boolean
public "getMonth"(): $Month
public "getDayOfWeek"(): $DayOfWeek
public "getDayOfYear"(): integer
public "withDayOfMonth"(arg0: integer): $LocalDateTime
public "withDayOfYear"(arg0: integer): $LocalDateTime
public "plusWeeks"(arg0: long): $LocalDateTime
public "withMonth"(arg0: integer): $LocalDateTime
public "plusMonths"(arg0: long): $LocalDateTime
public "withYear"(arg0: integer): $LocalDateTime
public "plusYears"(arg0: long): $LocalDateTime
public "minusMonths"(arg0: long): $LocalDateTime
public "toLocalTime"(): $LocalTime
public "isEqual"(arg0: $ChronoLocalDateTime$Type<(any)>): boolean
public "minusYears"(arg0: long): $LocalDateTime
public "minusWeeks"(arg0: long): $LocalDateTime
public "withHour"(arg0: integer): $LocalDateTime
public "withMinute"(arg0: integer): $LocalDateTime
public "withSecond"(arg0: integer): $LocalDateTime
public "withNano"(arg0: integer): $LocalDateTime
public "toInstant"(arg0: $ZoneOffset$Type): $Instant
public "getChronology"(): $Chronology
public "toEpochSecond"(arg0: $ZoneOffset$Type): long
public static "timeLineOrder"(): $Comparator<($ChronoLocalDateTime<(any)>)>
get "nano"(): integer
get "year"(): integer
get "monthValue"(): integer
get "dayOfMonth"(): integer
get "hour"(): integer
get "minute"(): integer
get "second"(): integer
get "month"(): $Month
get "dayOfWeek"(): $DayOfWeek
get "dayOfYear"(): integer
get "chronology"(): $Chronology
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LocalDateTime$Type = ($LocalDateTime);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LocalDateTime_ = $LocalDateTime$Type;
}}
declare module "packages/java/time/$Duration" {
import {$TemporalAmount, $TemporalAmount$Type} from "packages/java/time/temporal/$TemporalAmount"
import {$Comparable, $Comparable$Type} from "packages/java/lang/$Comparable"
import {$Serializable, $Serializable$Type} from "packages/java/io/$Serializable"
import {$TemporalUnit, $TemporalUnit$Type} from "packages/java/time/temporal/$TemporalUnit"
import {$Temporal, $Temporal$Type} from "packages/java/time/temporal/$Temporal"
import {$List, $List$Type} from "packages/java/util/$List"

export class $Duration implements $TemporalAmount, $Comparable<($Duration)>, $Serializable {
static readonly "ZERO": $Duration


public "get"(arg0: $TemporalUnit$Type): long
public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "abs"(): $Duration
public "compareTo"(arg0: $Duration$Type): integer
public static "of"(arg0: long, arg1: $TemporalUnit$Type): $Duration
public static "from"(arg0: $TemporalAmount$Type): $Duration
public "toMillis"(): long
public "toNanos"(): long
public static "parse"(arg0: charseq): $Duration
public static "between"(arg0: $Temporal$Type, arg1: $Temporal$Type): $Duration
public "plus"(arg0: $Duration$Type): $Duration
public "plus"(arg0: long, arg1: $TemporalUnit$Type): $Duration
public "isZero"(): boolean
public "getNano"(): integer
public "toDays"(): long
public "toSeconds"(): long
public "isNegative"(): boolean
public "minus"(arg0: long, arg1: $TemporalUnit$Type): $Duration
public "minus"(arg0: $Duration$Type): $Duration
public "getUnits"(): $List<($TemporalUnit)>
public static "ofSeconds"(arg0: long): $Duration
public static "ofSeconds"(arg0: long, arg1: long): $Duration
public "negated"(): $Duration
public static "ofNanos"(arg0: long): $Duration
public "getSeconds"(): long
public "plusNanos"(arg0: long): $Duration
public "plusSeconds"(arg0: long): $Duration
public "plusMillis"(arg0: long): $Duration
public "multipliedBy"(arg0: long): $Duration
public "plusDays"(arg0: long): $Duration
public "plusHours"(arg0: long): $Duration
public "plusMinutes"(arg0: long): $Duration
public "toHours"(): long
public "toMinutes"(): long
public static "ofDays"(arg0: long): $Duration
public static "ofHours"(arg0: long): $Duration
public static "ofMinutes"(arg0: long): $Duration
public static "ofMillis"(arg0: long): $Duration
public "withSeconds"(arg0: long): $Duration
public "withNanos"(arg0: integer): $Duration
public "minusDays"(arg0: long): $Duration
public "minusHours"(arg0: long): $Duration
public "minusMinutes"(arg0: long): $Duration
public "minusSeconds"(arg0: long): $Duration
public "minusMillis"(arg0: long): $Duration
public "minusNanos"(arg0: long): $Duration
public "dividedBy"(arg0: long): $Duration
public "dividedBy"(arg0: $Duration$Type): long
public "addTo"(arg0: $Temporal$Type): $Temporal
public "subtractFrom"(arg0: $Temporal$Type): $Temporal
public "toDaysPart"(): long
public "toHoursPart"(): integer
public "toMinutesPart"(): integer
public "toSecondsPart"(): integer
public "toMillisPart"(): integer
public "toNanosPart"(): integer
public "truncatedTo"(arg0: $TemporalUnit$Type): $Duration
get "zero"(): boolean
get "nano"(): integer
get "negative"(): boolean
get "units"(): $List<($TemporalUnit)>
get "seconds"(): long
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Duration$Type = ($TemporalAmount$Type) | ($Duration);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Duration_ = $Duration$Type;
}}
declare module "packages/java/time/temporal/$ValueRange" {
import {$TemporalField, $TemporalField$Type} from "packages/java/time/temporal/$TemporalField"
import {$Serializable, $Serializable$Type} from "packages/java/io/$Serializable"

export class $ValueRange implements $Serializable {


public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public static "of"(arg0: long, arg1: long): $ValueRange
public static "of"(arg0: long, arg1: long, arg2: long, arg3: long): $ValueRange
public static "of"(arg0: long, arg1: long, arg2: long): $ValueRange
public "getMaximum"(): long
public "checkValidIntValue"(arg0: long, arg1: $TemporalField$Type): integer
public "checkValidValue"(arg0: long, arg1: $TemporalField$Type): long
public "isIntValue"(): boolean
public "isValidValue"(arg0: long): boolean
public "isFixed"(): boolean
public "getMinimum"(): long
public "isValidIntValue"(arg0: long): boolean
public "getLargestMinimum"(): long
public "getSmallestMaximum"(): long
get "maximum"(): long
get "intValue"(): boolean
get "fixed"(): boolean
get "minimum"(): long
get "largestMinimum"(): long
get "smallestMaximum"(): long
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ValueRange$Type = ($ValueRange);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ValueRange_ = $ValueRange$Type;
}}
declare module "packages/java/time/$Period" {
import {$TemporalAmount, $TemporalAmount$Type} from "packages/java/time/temporal/$TemporalAmount"
import {$Serializable, $Serializable$Type} from "packages/java/io/$Serializable"
import {$TemporalUnit, $TemporalUnit$Type} from "packages/java/time/temporal/$TemporalUnit"
import {$LocalDate, $LocalDate$Type} from "packages/java/time/$LocalDate"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Temporal, $Temporal$Type} from "packages/java/time/temporal/$Temporal"
import {$ChronoPeriod, $ChronoPeriod$Type} from "packages/java/time/chrono/$ChronoPeriod"
import {$ChronoLocalDate, $ChronoLocalDate$Type} from "packages/java/time/chrono/$ChronoLocalDate"

export class $Period implements $ChronoPeriod, $Serializable {
static readonly "ZERO": $Period


public "get"(arg0: $TemporalUnit$Type): long
public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public static "of"(arg0: integer, arg1: integer, arg2: integer): $Period
public static "from"(arg0: $TemporalAmount$Type): $Period
public static "parse"(arg0: charseq): $Period
public "normalized"(): $Period
public static "between"(arg0: $LocalDate$Type, arg1: $LocalDate$Type): $Period
public "isZero"(): boolean
public "getMonths"(): integer
public "isNegative"(): boolean
public "minus"(arg0: $TemporalAmount$Type): $Period
public "getUnits"(): $List<($TemporalUnit)>
public "negated"(): $Period
public "plusDays"(arg0: long): $Period
public static "ofDays"(arg0: integer): $Period
public "minusDays"(arg0: long): $Period
public "addTo"(arg0: $Temporal$Type): $Temporal
public "subtractFrom"(arg0: $Temporal$Type): $Temporal
public "plusMonths"(arg0: long): $Period
public "toTotalMonths"(): long
public "getDays"(): integer
public "plusYears"(arg0: long): $Period
public "minusMonths"(arg0: long): $Period
public "minusYears"(arg0: long): $Period
public "getYears"(): integer
public static "ofYears"(arg0: integer): $Period
public static "ofMonths"(arg0: integer): $Period
public static "ofWeeks"(arg0: integer): $Period
public "withYears"(arg0: integer): $Period
public "withMonths"(arg0: integer): $Period
public "withDays"(arg0: integer): $Period
public static "between"(arg0: $ChronoLocalDate$Type, arg1: $ChronoLocalDate$Type): $ChronoPeriod
get "zero"(): boolean
get "months"(): integer
get "negative"(): boolean
get "units"(): $List<($TemporalUnit)>
get "days"(): integer
get "years"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Period$Type = ($Period);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Period_ = $Period$Type;
}}
declare module "packages/java/time/chrono/$ChronoPeriod" {
import {$TemporalAmount, $TemporalAmount$Type} from "packages/java/time/temporal/$TemporalAmount"
import {$TemporalUnit, $TemporalUnit$Type} from "packages/java/time/temporal/$TemporalUnit"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Temporal, $Temporal$Type} from "packages/java/time/temporal/$Temporal"
import {$ChronoLocalDate, $ChronoLocalDate$Type} from "packages/java/time/chrono/$ChronoLocalDate"
import {$Chronology, $Chronology$Type} from "packages/java/time/chrono/$Chronology"

export interface $ChronoPeriod extends $TemporalAmount {

 "get"(arg0: $TemporalUnit$Type): long
 "equals"(arg0: any): boolean
 "toString"(): string
 "hashCode"(): integer
 "normalized"(): $ChronoPeriod
 "plus"(arg0: $TemporalAmount$Type): $ChronoPeriod
 "isZero"(): boolean
 "isNegative"(): boolean
 "minus"(arg0: $TemporalAmount$Type): $ChronoPeriod
 "getUnits"(): $List<($TemporalUnit)>
 "negated"(): $ChronoPeriod
 "multipliedBy"(arg0: integer): $ChronoPeriod
 "addTo"(arg0: $Temporal$Type): $Temporal
 "subtractFrom"(arg0: $Temporal$Type): $Temporal
 "getChronology"(): $Chronology
}

export namespace $ChronoPeriod {
function between(arg0: $ChronoLocalDate$Type, arg1: $ChronoLocalDate$Type): $ChronoPeriod
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ChronoPeriod$Type = ($ChronoPeriod);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ChronoPeriod_ = $ChronoPeriod$Type;
}}
declare module "packages/java/time/format/$TextStyle" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $TextStyle extends $Enum<($TextStyle)> {
static readonly "FULL": $TextStyle
static readonly "FULL_STANDALONE": $TextStyle
static readonly "SHORT": $TextStyle
static readonly "SHORT_STANDALONE": $TextStyle
static readonly "NARROW": $TextStyle
static readonly "NARROW_STANDALONE": $TextStyle


public static "values"(): ($TextStyle)[]
public static "valueOf"(arg0: string): $TextStyle
public "isStandalone"(): boolean
public "asStandalone"(): $TextStyle
public "asNormal"(): $TextStyle
get "standalone"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TextStyle$Type = (("short_standalone") | ("narrow_standalone") | ("short") | ("narrow") | ("full_standalone") | ("full")) | ($TextStyle);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TextStyle_ = $TextStyle$Type;
}}
declare module "packages/java/time/$LocalTime" {
import {$Comparable, $Comparable$Type} from "packages/java/lang/$Comparable"
import {$TemporalField, $TemporalField$Type} from "packages/java/time/temporal/$TemporalField"
import {$Serializable, $Serializable$Type} from "packages/java/io/$Serializable"
import {$Instant, $Instant$Type} from "packages/java/time/$Instant"
import {$LocalDate, $LocalDate$Type} from "packages/java/time/$LocalDate"
import {$TemporalAccessor, $TemporalAccessor$Type} from "packages/java/time/temporal/$TemporalAccessor"
import {$ZoneOffset, $ZoneOffset$Type} from "packages/java/time/$ZoneOffset"
import {$Clock, $Clock$Type} from "packages/java/time/$Clock"
import {$ValueRange, $ValueRange$Type} from "packages/java/time/temporal/$ValueRange"
import {$LocalDateTime, $LocalDateTime$Type} from "packages/java/time/$LocalDateTime"
import {$TemporalAmount, $TemporalAmount$Type} from "packages/java/time/temporal/$TemporalAmount"
import {$TemporalQuery, $TemporalQuery$Type} from "packages/java/time/temporal/$TemporalQuery"
import {$TemporalAdjuster, $TemporalAdjuster$Type} from "packages/java/time/temporal/$TemporalAdjuster"
import {$DateTimeFormatter, $DateTimeFormatter$Type} from "packages/java/time/format/$DateTimeFormatter"
import {$TemporalUnit, $TemporalUnit$Type} from "packages/java/time/temporal/$TemporalUnit"
import {$ZoneId, $ZoneId$Type} from "packages/java/time/$ZoneId"
import {$Temporal, $Temporal$Type} from "packages/java/time/temporal/$Temporal"
import {$OffsetTime, $OffsetTime$Type} from "packages/java/time/$OffsetTime"

export class $LocalTime implements $Temporal, $TemporalAdjuster, $Comparable<($LocalTime)>, $Serializable {
static readonly "MIN": $LocalTime
static readonly "MAX": $LocalTime
static readonly "MIDNIGHT": $LocalTime
static readonly "NOON": $LocalTime


public "get"(arg0: $TemporalField$Type): integer
public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "compareTo"(arg0: $LocalTime$Type): integer
public "getLong"(arg0: $TemporalField$Type): long
public "format"(arg0: $DateTimeFormatter$Type): string
public static "of"(arg0: integer, arg1: integer, arg2: integer, arg3: integer): $LocalTime
public static "of"(arg0: integer, arg1: integer, arg2: integer): $LocalTime
public static "of"(arg0: integer, arg1: integer): $LocalTime
public static "from"(arg0: $TemporalAccessor$Type): $LocalTime
public "query"<R>(arg0: $TemporalQuery$Type<(R)>): R
public "range"(arg0: $TemporalField$Type): $ValueRange
public "isSupported"(arg0: $TemporalUnit$Type): boolean
public "isSupported"(arg0: $TemporalField$Type): boolean
public static "parse"(arg0: charseq): $LocalTime
public static "parse"(arg0: charseq, arg1: $DateTimeFormatter$Type): $LocalTime
public "plus"(arg0: $TemporalAmount$Type): $LocalTime
public "plus"(arg0: long, arg1: $TemporalUnit$Type): $LocalTime
public static "now"(): $LocalTime
public static "now"(arg0: $Clock$Type): $LocalTime
public static "now"(arg0: $ZoneId$Type): $LocalTime
public "getNano"(): integer
public "getHour"(): integer
public "getMinute"(): integer
public "getSecond"(): integer
public "minus"(arg0: long, arg1: $TemporalUnit$Type): $LocalTime
public "minus"(arg0: $TemporalAmount$Type): $LocalTime
public "until"(arg0: $Temporal$Type, arg1: $TemporalUnit$Type): long
public "plusNanos"(arg0: long): $LocalTime
public "plusSeconds"(arg0: long): $LocalTime
public "plusHours"(arg0: long): $LocalTime
public "plusMinutes"(arg0: long): $LocalTime
public "minusHours"(arg0: long): $LocalTime
public "minusMinutes"(arg0: long): $LocalTime
public "minusSeconds"(arg0: long): $LocalTime
public "minusNanos"(arg0: long): $LocalTime
public "truncatedTo"(arg0: $TemporalUnit$Type): $LocalTime
public "adjustInto"(arg0: $Temporal$Type): $Temporal
public static "ofInstant"(arg0: $Instant$Type, arg1: $ZoneId$Type): $LocalTime
public "atOffset"(arg0: $ZoneOffset$Type): $OffsetTime
public "isAfter"(arg0: $LocalTime$Type): boolean
public "isBefore"(arg0: $LocalTime$Type): boolean
public "toSecondOfDay"(): integer
public "toEpochSecond"(arg0: $LocalDate$Type, arg1: $ZoneOffset$Type): long
public static "ofNanoOfDay"(arg0: long): $LocalTime
public "withHour"(arg0: integer): $LocalTime
public "withMinute"(arg0: integer): $LocalTime
public "withSecond"(arg0: integer): $LocalTime
public "withNano"(arg0: integer): $LocalTime
public "toNanoOfDay"(): long
public static "ofSecondOfDay"(arg0: long): $LocalTime
public "atDate"(arg0: $LocalDate$Type): $LocalDateTime
get "nano"(): integer
get "hour"(): integer
get "minute"(): integer
get "second"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LocalTime$Type = ($LocalTime);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LocalTime_ = $LocalTime$Type;
}}
declare module "packages/java/time/$Clock" {
import {$Instant, $Instant$Type} from "packages/java/time/$Instant"
import {$ZoneId, $ZoneId$Type} from "packages/java/time/$ZoneId"
import {$InstantSource, $InstantSource$Type} from "packages/java/time/$InstantSource"
import {$Duration, $Duration$Type} from "packages/java/time/$Duration"

export class $Clock implements $InstantSource {


public "equals"(arg0: any): boolean
public "hashCode"(): integer
public static "offset"(arg0: $Clock$Type, arg1: $Duration$Type): $Clock
public "millis"(): long
public static "system"(arg0: $ZoneId$Type): $Clock
public static "fixed"(arg0: $Instant$Type, arg1: $ZoneId$Type): $Clock
public "instant"(): $Instant
public static "systemUTC"(): $Clock
public static "systemDefaultZone"(): $Clock
public static "tickMillis"(arg0: $ZoneId$Type): $Clock
public static "tickSeconds"(arg0: $ZoneId$Type): $Clock
public static "tickMinutes"(arg0: $ZoneId$Type): $Clock
public static "tick"(arg0: $Clock$Type, arg1: $Duration$Type): $Clock
public "getZone"(): $ZoneId
public "withZone"(arg0: $ZoneId$Type): $Clock
public static "offset"(arg0: $InstantSource$Type, arg1: $Duration$Type): $InstantSource
public static "system"(): $InstantSource
public static "fixed"(arg0: $Instant$Type): $InstantSource
public static "tick"(arg0: $InstantSource$Type, arg1: $Duration$Type): $InstantSource
get "zone"(): $ZoneId
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Clock$Type = ($Clock);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Clock_ = $Clock$Type;
}}
declare module "packages/java/time/$ZoneId" {
import {$TextStyle, $TextStyle$Type} from "packages/java/time/format/$TextStyle"
import {$Serializable, $Serializable$Type} from "packages/java/io/$Serializable"
import {$ZoneRules, $ZoneRules$Type} from "packages/java/time/zone/$ZoneRules"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$TemporalAccessor, $TemporalAccessor$Type} from "packages/java/time/temporal/$TemporalAccessor"
import {$ZoneOffset, $ZoneOffset$Type} from "packages/java/time/$ZoneOffset"
import {$Map, $Map$Type} from "packages/java/util/$Map"
import {$Locale, $Locale$Type} from "packages/java/util/$Locale"

export class $ZoneId implements $Serializable {
static readonly "SHORT_IDS": $Map<(string), (string)>


public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public static "of"(arg0: string, arg1: $Map$Type<(string), (string)>): $ZoneId
public static "of"(arg0: string): $ZoneId
public static "from"(arg0: $TemporalAccessor$Type): $ZoneId
public "getId"(): string
public "normalized"(): $ZoneId
public "getDisplayName"(arg0: $TextStyle$Type, arg1: $Locale$Type): string
public static "systemDefault"(): $ZoneId
public "getRules"(): $ZoneRules
public static "getAvailableZoneIds"(): $Set<(string)>
public static "ofOffset"(arg0: string, arg1: $ZoneOffset$Type): $ZoneId
get "id"(): string
get "rules"(): $ZoneRules
get "availableZoneIds"(): $Set<(string)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ZoneId$Type = ($ZoneId);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ZoneId_ = $ZoneId$Type;
}}
declare module "packages/java/time/$OffsetDateTime" {
import {$Comparator, $Comparator$Type} from "packages/java/util/$Comparator"
import {$Comparable, $Comparable$Type} from "packages/java/lang/$Comparable"
import {$TemporalField, $TemporalField$Type} from "packages/java/time/temporal/$TemporalField"
import {$Serializable, $Serializable$Type} from "packages/java/io/$Serializable"
import {$Instant, $Instant$Type} from "packages/java/time/$Instant"
import {$LocalDate, $LocalDate$Type} from "packages/java/time/$LocalDate"
import {$ZoneOffset, $ZoneOffset$Type} from "packages/java/time/$ZoneOffset"
import {$TemporalAccessor, $TemporalAccessor$Type} from "packages/java/time/temporal/$TemporalAccessor"
import {$DayOfWeek, $DayOfWeek$Type} from "packages/java/time/$DayOfWeek"
import {$Clock, $Clock$Type} from "packages/java/time/$Clock"
import {$LocalDateTime, $LocalDateTime$Type} from "packages/java/time/$LocalDateTime"
import {$ValueRange, $ValueRange$Type} from "packages/java/time/temporal/$ValueRange"
import {$TemporalAmount, $TemporalAmount$Type} from "packages/java/time/temporal/$TemporalAmount"
import {$TemporalQuery, $TemporalQuery$Type} from "packages/java/time/temporal/$TemporalQuery"
import {$Month, $Month$Type} from "packages/java/time/$Month"
import {$TemporalAdjuster, $TemporalAdjuster$Type} from "packages/java/time/temporal/$TemporalAdjuster"
import {$DateTimeFormatter, $DateTimeFormatter$Type} from "packages/java/time/format/$DateTimeFormatter"
import {$TemporalUnit, $TemporalUnit$Type} from "packages/java/time/temporal/$TemporalUnit"
import {$ZoneId, $ZoneId$Type} from "packages/java/time/$ZoneId"
import {$Temporal, $Temporal$Type} from "packages/java/time/temporal/$Temporal"
import {$ZonedDateTime, $ZonedDateTime$Type} from "packages/java/time/$ZonedDateTime"
import {$OffsetTime, $OffsetTime$Type} from "packages/java/time/$OffsetTime"
import {$LocalTime, $LocalTime$Type} from "packages/java/time/$LocalTime"

export class $OffsetDateTime implements $Temporal, $TemporalAdjuster, $Comparable<($OffsetDateTime)>, $Serializable {
static readonly "MIN": $OffsetDateTime
static readonly "MAX": $OffsetDateTime


public "get"(arg0: $TemporalField$Type): integer
public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "compareTo"(arg0: $OffsetDateTime$Type): integer
public "getLong"(arg0: $TemporalField$Type): long
public "format"(arg0: $DateTimeFormatter$Type): string
public static "of"(arg0: $LocalDateTime$Type, arg1: $ZoneOffset$Type): $OffsetDateTime
public static "of"(arg0: $LocalDate$Type, arg1: $LocalTime$Type, arg2: $ZoneOffset$Type): $OffsetDateTime
public static "of"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: $ZoneOffset$Type): $OffsetDateTime
public static "from"(arg0: $TemporalAccessor$Type): $OffsetDateTime
public "query"<R>(arg0: $TemporalQuery$Type<(R)>): R
public "getOffset"(): $ZoneOffset
public "range"(arg0: $TemporalField$Type): $ValueRange
public "isSupported"(arg0: $TemporalUnit$Type): boolean
public "isSupported"(arg0: $TemporalField$Type): boolean
public static "parse"(arg0: charseq, arg1: $DateTimeFormatter$Type): $OffsetDateTime
public static "parse"(arg0: charseq): $OffsetDateTime
public "plus"(arg0: long, arg1: $TemporalUnit$Type): $OffsetDateTime
public "plus"(arg0: $TemporalAmount$Type): $OffsetDateTime
public static "now"(arg0: $Clock$Type): $OffsetDateTime
public static "now"(): $OffsetDateTime
public static "now"(arg0: $ZoneId$Type): $OffsetDateTime
public "getNano"(): integer
public "toInstant"(): $Instant
public "getYear"(): integer
public "getMonthValue"(): integer
public "getDayOfMonth"(): integer
public "getHour"(): integer
public "getMinute"(): integer
public "getSecond"(): integer
public "minus"(arg0: long, arg1: $TemporalUnit$Type): $OffsetDateTime
public "minus"(arg0: $TemporalAmount$Type): $OffsetDateTime
public "until"(arg0: $Temporal$Type, arg1: $TemporalUnit$Type): long
public "plusNanos"(arg0: long): $OffsetDateTime
public "plusSeconds"(arg0: long): $OffsetDateTime
public "plusDays"(arg0: long): $OffsetDateTime
public "plusHours"(arg0: long): $OffsetDateTime
public "plusMinutes"(arg0: long): $OffsetDateTime
public "minusDays"(arg0: long): $OffsetDateTime
public "minusHours"(arg0: long): $OffsetDateTime
public "minusMinutes"(arg0: long): $OffsetDateTime
public "minusSeconds"(arg0: long): $OffsetDateTime
public "minusNanos"(arg0: long): $OffsetDateTime
public "truncatedTo"(arg0: $TemporalUnit$Type): $OffsetDateTime
public "adjustInto"(arg0: $Temporal$Type): $Temporal
public "with"(arg0: $TemporalField$Type, arg1: long): $OffsetDateTime
public "with"(arg0: $TemporalAdjuster$Type): $OffsetDateTime
public static "ofInstant"(arg0: $Instant$Type, arg1: $ZoneId$Type): $OffsetDateTime
public "isAfter"(arg0: $OffsetDateTime$Type): boolean
public "isBefore"(arg0: $OffsetDateTime$Type): boolean
public "getMonth"(): $Month
public "getDayOfWeek"(): $DayOfWeek
public "getDayOfYear"(): integer
public "withDayOfMonth"(arg0: integer): $OffsetDateTime
public "withDayOfYear"(arg0: integer): $OffsetDateTime
public "plusWeeks"(arg0: long): $OffsetDateTime
public "withMonth"(arg0: integer): $OffsetDateTime
public "plusMonths"(arg0: long): $OffsetDateTime
public "withYear"(arg0: integer): $OffsetDateTime
public "plusYears"(arg0: long): $OffsetDateTime
public "minusMonths"(arg0: long): $OffsetDateTime
public "toLocalTime"(): $LocalTime
public "isEqual"(arg0: $OffsetDateTime$Type): boolean
public "minusYears"(arg0: long): $OffsetDateTime
public "minusWeeks"(arg0: long): $OffsetDateTime
public "toEpochSecond"(): long
public static "timeLineOrder"(): $Comparator<($OffsetDateTime)>
public "toLocalDateTime"(): $LocalDateTime
public "withHour"(arg0: integer): $OffsetDateTime
public "withMinute"(arg0: integer): $OffsetDateTime
public "withSecond"(arg0: integer): $OffsetDateTime
public "withNano"(arg0: integer): $OffsetDateTime
public "toLocalDate"(): $LocalDate
public "toZonedDateTime"(): $ZonedDateTime
public "withOffsetSameInstant"(arg0: $ZoneOffset$Type): $OffsetDateTime
public "withOffsetSameLocal"(arg0: $ZoneOffset$Type): $OffsetDateTime
public "atZoneSameInstant"(arg0: $ZoneId$Type): $ZonedDateTime
public "atZoneSimilarLocal"(arg0: $ZoneId$Type): $ZonedDateTime
public "toOffsetTime"(): $OffsetTime
get "offset"(): $ZoneOffset
get "nano"(): integer
get "year"(): integer
get "monthValue"(): integer
get "dayOfMonth"(): integer
get "hour"(): integer
get "minute"(): integer
get "second"(): integer
get "month"(): $Month
get "dayOfWeek"(): $DayOfWeek
get "dayOfYear"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $OffsetDateTime$Type = ($OffsetDateTime);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $OffsetDateTime_ = $OffsetDateTime$Type;
}}
declare module "packages/java/time/$LocalDate" {
import {$TemporalAccessor, $TemporalAccessor$Type} from "packages/java/time/temporal/$TemporalAccessor"
import {$DayOfWeek, $DayOfWeek$Type} from "packages/java/time/$DayOfWeek"
import {$Clock, $Clock$Type} from "packages/java/time/$Clock"
import {$ValueRange, $ValueRange$Type} from "packages/java/time/temporal/$ValueRange"
import {$LocalDateTime, $LocalDateTime$Type} from "packages/java/time/$LocalDateTime"
import {$TemporalAmount, $TemporalAmount$Type} from "packages/java/time/temporal/$TemporalAmount"
import {$TemporalAdjuster, $TemporalAdjuster$Type} from "packages/java/time/temporal/$TemporalAdjuster"
import {$DateTimeFormatter, $DateTimeFormatter$Type} from "packages/java/time/format/$DateTimeFormatter"
import {$TemporalUnit, $TemporalUnit$Type} from "packages/java/time/temporal/$TemporalUnit"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$OffsetDateTime, $OffsetDateTime$Type} from "packages/java/time/$OffsetDateTime"
import {$LocalTime, $LocalTime$Type} from "packages/java/time/$LocalTime"
import {$Comparator, $Comparator$Type} from "packages/java/util/$Comparator"
import {$TemporalField, $TemporalField$Type} from "packages/java/time/temporal/$TemporalField"
import {$Serializable, $Serializable$Type} from "packages/java/io/$Serializable"
import {$Instant, $Instant$Type} from "packages/java/time/$Instant"
import {$ZoneOffset, $ZoneOffset$Type} from "packages/java/time/$ZoneOffset"
import {$ChronoLocalDate, $ChronoLocalDate$Type} from "packages/java/time/chrono/$ChronoLocalDate"
import {$TemporalQuery, $TemporalQuery$Type} from "packages/java/time/temporal/$TemporalQuery"
import {$Month, $Month$Type} from "packages/java/time/$Month"
import {$Period, $Period$Type} from "packages/java/time/$Period"
import {$ZoneId, $ZoneId$Type} from "packages/java/time/$ZoneId"
import {$Temporal, $Temporal$Type} from "packages/java/time/temporal/$Temporal"
import {$ZonedDateTime, $ZonedDateTime$Type} from "packages/java/time/$ZonedDateTime"
import {$OffsetTime, $OffsetTime$Type} from "packages/java/time/$OffsetTime"

export class $LocalDate implements $Temporal, $TemporalAdjuster, $ChronoLocalDate, $Serializable {
static readonly "MIN": $LocalDate
static readonly "MAX": $LocalDate
static readonly "EPOCH": $LocalDate


public "get"(arg0: $TemporalField$Type): integer
public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "compareTo"(arg0: $ChronoLocalDate$Type): integer
public "getLong"(arg0: $TemporalField$Type): long
public "format"(arg0: $DateTimeFormatter$Type): string
public static "of"(arg0: integer, arg1: integer, arg2: integer): $LocalDate
public static "of"(arg0: integer, arg1: $Month$Type, arg2: integer): $LocalDate
public static "from"(arg0: $TemporalAccessor$Type): $LocalDate
public "query"<R>(arg0: $TemporalQuery$Type<(R)>): R
public "range"(arg0: $TemporalField$Type): $ValueRange
public "isSupported"(arg0: $TemporalUnit$Type): boolean
public "isSupported"(arg0: $TemporalField$Type): boolean
public static "parse"(arg0: charseq): $LocalDate
public static "parse"(arg0: charseq, arg1: $DateTimeFormatter$Type): $LocalDate
public "plus"(arg0: $TemporalAmount$Type): $LocalDate
public "plus"(arg0: long, arg1: $TemporalUnit$Type): $LocalDate
public static "now"(arg0: $ZoneId$Type): $LocalDate
public static "now"(arg0: $Clock$Type): $LocalDate
public static "now"(): $LocalDate
public "getYear"(): integer
public "getMonthValue"(): integer
public "getDayOfMonth"(): integer
public "until"(arg0: $ChronoLocalDate$Type): $Period
public "until"(arg0: $Temporal$Type, arg1: $TemporalUnit$Type): long
public "plusDays"(arg0: long): $LocalDate
public "minusDays"(arg0: long): $LocalDate
public "adjustInto"(arg0: $Temporal$Type): $Temporal
public "with"(arg0: $TemporalField$Type, arg1: long): $LocalDate
public static "ofInstant"(arg0: $Instant$Type, arg1: $ZoneId$Type): $LocalDate
public "isAfter"(arg0: $ChronoLocalDate$Type): boolean
public "isBefore"(arg0: $ChronoLocalDate$Type): boolean
public "isLeapYear"(): boolean
public static "ofEpochDay"(arg0: long): $LocalDate
public "lengthOfMonth"(): integer
public "lengthOfYear"(): integer
public "getMonth"(): $Month
public "toEpochDay"(): long
public "getDayOfWeek"(): $DayOfWeek
public "getDayOfYear"(): integer
public "withDayOfMonth"(arg0: integer): $LocalDate
public "withDayOfYear"(arg0: integer): $LocalDate
public "plusWeeks"(arg0: long): $LocalDate
public "withMonth"(arg0: integer): $LocalDate
public "plusMonths"(arg0: long): $LocalDate
public "withYear"(arg0: integer): $LocalDate
public static "ofYearDay"(arg0: integer, arg1: integer): $LocalDate
public "plusYears"(arg0: long): $LocalDate
public "minusMonths"(arg0: long): $LocalDate
public "atTime"(arg0: $OffsetTime$Type): $OffsetDateTime
public "atTime"(arg0: integer, arg1: integer, arg2: integer): $LocalDateTime
public "atTime"(arg0: integer, arg1: integer): $LocalDateTime
public "atTime"(arg0: integer, arg1: integer, arg2: integer, arg3: integer): $LocalDateTime
public "isEqual"(arg0: $ChronoLocalDate$Type): boolean
public "minusYears"(arg0: long): $LocalDate
public "minusWeeks"(arg0: long): $LocalDate
public "datesUntil"(arg0: $LocalDate$Type): $Stream<($LocalDate)>
public "datesUntil"(arg0: $LocalDate$Type, arg1: $Period$Type): $Stream<($LocalDate)>
public "atStartOfDay"(arg0: $ZoneId$Type): $ZonedDateTime
public "atStartOfDay"(): $LocalDateTime
public "toEpochSecond"(arg0: $LocalTime$Type, arg1: $ZoneOffset$Type): long
public static "timeLineOrder"(): $Comparator<($ChronoLocalDate)>
get "year"(): integer
get "monthValue"(): integer
get "dayOfMonth"(): integer
get "leapYear"(): boolean
get "month"(): $Month
get "dayOfWeek"(): $DayOfWeek
get "dayOfYear"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LocalDate$Type = ($LocalDate);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LocalDate_ = $LocalDate$Type;
}}
declare module "packages/java/time/temporal/$TemporalField" {
import {$TemporalUnit, $TemporalUnit$Type} from "packages/java/time/temporal/$TemporalUnit"
import {$Temporal, $Temporal$Type} from "packages/java/time/temporal/$Temporal"
import {$TemporalAccessor, $TemporalAccessor$Type} from "packages/java/time/temporal/$TemporalAccessor"
import {$ValueRange, $ValueRange$Type} from "packages/java/time/temporal/$ValueRange"
import {$ResolverStyle, $ResolverStyle$Type} from "packages/java/time/format/$ResolverStyle"
import {$Map, $Map$Type} from "packages/java/util/$Map"
import {$Locale, $Locale$Type} from "packages/java/util/$Locale"

export interface $TemporalField {

 "toString"(): string
 "resolve"(arg0: $Map$Type<($TemporalField$Type), (long)>, arg1: $TemporalAccessor$Type, arg2: $ResolverStyle$Type): $TemporalAccessor
 "range"(): $ValueRange
 "getDisplayName"(arg0: $Locale$Type): string
 "isSupportedBy"(arg0: $TemporalAccessor$Type): boolean
 "isTimeBased"(): boolean
 "getFrom"(arg0: $TemporalAccessor$Type): long
 "adjustInto"<R extends $Temporal>(arg0: R, arg1: long): R
 "rangeRefinedBy"(arg0: $TemporalAccessor$Type): $ValueRange
 "isDateBased"(): boolean
 "getBaseUnit"(): $TemporalUnit
 "getRangeUnit"(): $TemporalUnit
}

export namespace $TemporalField {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TemporalField$Type = ($TemporalField);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TemporalField_ = $TemporalField$Type;
}}
declare module "packages/java/time/temporal/$TemporalAccessor" {
import {$TemporalQuery, $TemporalQuery$Type} from "packages/java/time/temporal/$TemporalQuery"
import {$TemporalField, $TemporalField$Type} from "packages/java/time/temporal/$TemporalField"
import {$ValueRange, $ValueRange$Type} from "packages/java/time/temporal/$ValueRange"

export interface $TemporalAccessor {

 "get"(arg0: $TemporalField$Type): integer
 "getLong"(arg0: $TemporalField$Type): long
 "query"<R>(arg0: $TemporalQuery$Type<(R)>): R
 "range"(arg0: $TemporalField$Type): $ValueRange
 "isSupported"(arg0: $TemporalField$Type): boolean
}

export namespace $TemporalAccessor {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TemporalAccessor$Type = ($TemporalAccessor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TemporalAccessor_ = $TemporalAccessor$Type;
}}
declare module "packages/java/time/$DayOfWeek" {
import {$TemporalQuery, $TemporalQuery$Type} from "packages/java/time/temporal/$TemporalQuery"
import {$TextStyle, $TextStyle$Type} from "packages/java/time/format/$TextStyle"
import {$TemporalField, $TemporalField$Type} from "packages/java/time/temporal/$TemporalField"
import {$TemporalAdjuster, $TemporalAdjuster$Type} from "packages/java/time/temporal/$TemporalAdjuster"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$Temporal, $Temporal$Type} from "packages/java/time/temporal/$Temporal"
import {$TemporalAccessor, $TemporalAccessor$Type} from "packages/java/time/temporal/$TemporalAccessor"
import {$ValueRange, $ValueRange$Type} from "packages/java/time/temporal/$ValueRange"
import {$Locale, $Locale$Type} from "packages/java/util/$Locale"

export class $DayOfWeek extends $Enum<($DayOfWeek)> implements $TemporalAccessor, $TemporalAdjuster {
static readonly "MONDAY": $DayOfWeek
static readonly "TUESDAY": $DayOfWeek
static readonly "WEDNESDAY": $DayOfWeek
static readonly "THURSDAY": $DayOfWeek
static readonly "FRIDAY": $DayOfWeek
static readonly "SATURDAY": $DayOfWeek
static readonly "SUNDAY": $DayOfWeek


public "get"(arg0: $TemporalField$Type): integer
public static "values"(): ($DayOfWeek)[]
public "getLong"(arg0: $TemporalField$Type): long
public static "valueOf"(arg0: string): $DayOfWeek
public "getValue"(): integer
public static "of"(arg0: integer): $DayOfWeek
public static "from"(arg0: $TemporalAccessor$Type): $DayOfWeek
public "query"<R>(arg0: $TemporalQuery$Type<(R)>): R
public "range"(arg0: $TemporalField$Type): $ValueRange
public "isSupported"(arg0: $TemporalField$Type): boolean
public "plus"(arg0: long): $DayOfWeek
public "getDisplayName"(arg0: $TextStyle$Type, arg1: $Locale$Type): string
public "minus"(arg0: long): $DayOfWeek
public "adjustInto"(arg0: $Temporal$Type): $Temporal
get "value"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DayOfWeek$Type = (("sunday") | ("saturday") | ("tuesday") | ("wednesday") | ("thursday") | ("friday") | ("monday")) | ($DayOfWeek);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DayOfWeek_ = $DayOfWeek$Type;
}}
declare module "packages/java/time/$ZonedDateTime" {
import {$Comparator, $Comparator$Type} from "packages/java/util/$Comparator"
import {$TemporalField, $TemporalField$Type} from "packages/java/time/temporal/$TemporalField"
import {$Serializable, $Serializable$Type} from "packages/java/io/$Serializable"
import {$ChronoZonedDateTime, $ChronoZonedDateTime$Type} from "packages/java/time/chrono/$ChronoZonedDateTime"
import {$Instant, $Instant$Type} from "packages/java/time/$Instant"
import {$LocalDate, $LocalDate$Type} from "packages/java/time/$LocalDate"
import {$TemporalAccessor, $TemporalAccessor$Type} from "packages/java/time/temporal/$TemporalAccessor"
import {$ZoneOffset, $ZoneOffset$Type} from "packages/java/time/$ZoneOffset"
import {$DayOfWeek, $DayOfWeek$Type} from "packages/java/time/$DayOfWeek"
import {$Clock, $Clock$Type} from "packages/java/time/$Clock"
import {$LocalDateTime, $LocalDateTime$Type} from "packages/java/time/$LocalDateTime"
import {$ValueRange, $ValueRange$Type} from "packages/java/time/temporal/$ValueRange"
import {$Chronology, $Chronology$Type} from "packages/java/time/chrono/$Chronology"
import {$TemporalAmount, $TemporalAmount$Type} from "packages/java/time/temporal/$TemporalAmount"
import {$TemporalQuery, $TemporalQuery$Type} from "packages/java/time/temporal/$TemporalQuery"
import {$Month, $Month$Type} from "packages/java/time/$Month"
import {$DateTimeFormatter, $DateTimeFormatter$Type} from "packages/java/time/format/$DateTimeFormatter"
import {$TemporalUnit, $TemporalUnit$Type} from "packages/java/time/temporal/$TemporalUnit"
import {$ZoneId, $ZoneId$Type} from "packages/java/time/$ZoneId"
import {$Temporal, $Temporal$Type} from "packages/java/time/temporal/$Temporal"
import {$LocalTime, $LocalTime$Type} from "packages/java/time/$LocalTime"
import {$OffsetDateTime, $OffsetDateTime$Type} from "packages/java/time/$OffsetDateTime"

export class $ZonedDateTime implements $Temporal, $ChronoZonedDateTime<($LocalDate)>, $Serializable {


public "get"(arg0: $TemporalField$Type): integer
public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "getLong"(arg0: $TemporalField$Type): long
public "format"(arg0: $DateTimeFormatter$Type): string
public static "of"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: $ZoneId$Type): $ZonedDateTime
public static "of"(arg0: $LocalDate$Type, arg1: $LocalTime$Type, arg2: $ZoneId$Type): $ZonedDateTime
public static "of"(arg0: $LocalDateTime$Type, arg1: $ZoneId$Type): $ZonedDateTime
public static "from"(arg0: $TemporalAccessor$Type): $ZonedDateTime
public "query"<R>(arg0: $TemporalQuery$Type<(R)>): R
public "getOffset"(): $ZoneOffset
public "range"(arg0: $TemporalField$Type): $ValueRange
public "isSupported"(arg0: $TemporalUnit$Type): boolean
public "isSupported"(arg0: $TemporalField$Type): boolean
public static "parse"(arg0: charseq): $ZonedDateTime
public static "parse"(arg0: charseq, arg1: $DateTimeFormatter$Type): $ZonedDateTime
public "plus"(arg0: long, arg1: $TemporalUnit$Type): $ZonedDateTime
public static "now"(): $ZonedDateTime
public static "now"(arg0: $Clock$Type): $ZonedDateTime
public static "now"(arg0: $ZoneId$Type): $ZonedDateTime
public "getNano"(): integer
public "getYear"(): integer
public "getMonthValue"(): integer
public "getDayOfMonth"(): integer
public "getHour"(): integer
public "getMinute"(): integer
public "getSecond"(): integer
public "minus"(arg0: $TemporalAmount$Type): $ZonedDateTime
public "getZone"(): $ZoneId
public "until"(arg0: $Temporal$Type, arg1: $TemporalUnit$Type): long
public "plusNanos"(arg0: long): $ZonedDateTime
public "plusSeconds"(arg0: long): $ZonedDateTime
public "plusDays"(arg0: long): $ZonedDateTime
public "plusHours"(arg0: long): $ZonedDateTime
public "plusMinutes"(arg0: long): $ZonedDateTime
public "minusDays"(arg0: long): $ZonedDateTime
public "minusHours"(arg0: long): $ZonedDateTime
public "minusMinutes"(arg0: long): $ZonedDateTime
public "minusSeconds"(arg0: long): $ZonedDateTime
public "minusNanos"(arg0: long): $ZonedDateTime
public "truncatedTo"(arg0: $TemporalUnit$Type): $ZonedDateTime
public static "ofInstant"(arg0: $LocalDateTime$Type, arg1: $ZoneOffset$Type, arg2: $ZoneId$Type): $ZonedDateTime
public static "ofInstant"(arg0: $Instant$Type, arg1: $ZoneId$Type): $ZonedDateTime
public "getMonth"(): $Month
public "getDayOfWeek"(): $DayOfWeek
public "getDayOfYear"(): integer
public "withDayOfMonth"(arg0: integer): $ZonedDateTime
public "withDayOfYear"(arg0: integer): $ZonedDateTime
public "plusWeeks"(arg0: long): $ZonedDateTime
public "withMonth"(arg0: integer): $ZonedDateTime
public "plusMonths"(arg0: long): $ZonedDateTime
public "withYear"(arg0: integer): $ZonedDateTime
public "plusYears"(arg0: long): $ZonedDateTime
public "minusMonths"(arg0: long): $ZonedDateTime
public "toLocalTime"(): $LocalTime
public "minusYears"(arg0: long): $ZonedDateTime
public "minusWeeks"(arg0: long): $ZonedDateTime
public "toLocalDateTime"(): $LocalDateTime
public "withHour"(arg0: integer): $ZonedDateTime
public "withMinute"(arg0: integer): $ZonedDateTime
public "withSecond"(arg0: integer): $ZonedDateTime
public "withNano"(arg0: integer): $ZonedDateTime
public "toLocalDate"(): $LocalDate
public static "ofLocal"(arg0: $LocalDateTime$Type, arg1: $ZoneId$Type, arg2: $ZoneOffset$Type): $ZonedDateTime
public "toOffsetDateTime"(): $OffsetDateTime
public static "ofStrict"(arg0: $LocalDateTime$Type, arg1: $ZoneOffset$Type, arg2: $ZoneId$Type): $ZonedDateTime
public "withFixedOffsetZone"(): $ZonedDateTime
public "compareTo"(arg0: $ChronoZonedDateTime$Type<(any)>): integer
public "toInstant"(): $Instant
public "isAfter"(arg0: $ChronoZonedDateTime$Type<(any)>): boolean
public "isBefore"(arg0: $ChronoZonedDateTime$Type<(any)>): boolean
public "isEqual"(arg0: $ChronoZonedDateTime$Type<(any)>): boolean
public "getChronology"(): $Chronology
public "toEpochSecond"(): long
public static "timeLineOrder"(): $Comparator<($ChronoZonedDateTime<(any)>)>
get "offset"(): $ZoneOffset
get "nano"(): integer
get "year"(): integer
get "monthValue"(): integer
get "dayOfMonth"(): integer
get "hour"(): integer
get "minute"(): integer
get "second"(): integer
get "zone"(): $ZoneId
get "month"(): $Month
get "dayOfWeek"(): $DayOfWeek
get "dayOfYear"(): integer
get "chronology"(): $Chronology
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ZonedDateTime$Type = ($ZonedDateTime);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ZonedDateTime_ = $ZonedDateTime$Type;
}}
declare module "packages/java/time/$InstantSource" {
import {$Instant, $Instant$Type} from "packages/java/time/$Instant"
import {$ZoneId, $ZoneId$Type} from "packages/java/time/$ZoneId"
import {$Clock, $Clock$Type} from "packages/java/time/$Clock"
import {$Duration, $Duration$Type} from "packages/java/time/$Duration"

export interface $InstantSource {

 "millis"(): long
 "instant"(): $Instant
 "withZone"(arg0: $ZoneId$Type): $Clock

(arg0: $InstantSource$Type, arg1: $Duration$Type): $InstantSource
}

export namespace $InstantSource {
function offset(arg0: $InstantSource$Type, arg1: $Duration$Type): $InstantSource
function system(): $InstantSource
function fixed(arg0: $Instant$Type): $InstantSource
function tick(arg0: $InstantSource$Type, arg1: $Duration$Type): $InstantSource
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InstantSource$Type = ($InstantSource);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InstantSource_ = $InstantSource$Type;
}}
declare module "packages/java/time/zone/$ZoneOffsetTransitionRule$TimeDefinition" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$ZoneOffset, $ZoneOffset$Type} from "packages/java/time/$ZoneOffset"
import {$LocalDateTime, $LocalDateTime$Type} from "packages/java/time/$LocalDateTime"

export class $ZoneOffsetTransitionRule$TimeDefinition extends $Enum<($ZoneOffsetTransitionRule$TimeDefinition)> {
static readonly "UTC": $ZoneOffsetTransitionRule$TimeDefinition
static readonly "WALL": $ZoneOffsetTransitionRule$TimeDefinition
static readonly "STANDARD": $ZoneOffsetTransitionRule$TimeDefinition


public static "values"(): ($ZoneOffsetTransitionRule$TimeDefinition)[]
public static "valueOf"(arg0: string): $ZoneOffsetTransitionRule$TimeDefinition
public "createDateTime"(arg0: $LocalDateTime$Type, arg1: $ZoneOffset$Type, arg2: $ZoneOffset$Type): $LocalDateTime
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ZoneOffsetTransitionRule$TimeDefinition$Type = (("standard") | ("utc") | ("wall")) | ($ZoneOffsetTransitionRule$TimeDefinition);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ZoneOffsetTransitionRule$TimeDefinition_ = $ZoneOffsetTransitionRule$TimeDefinition$Type;
}}
declare module "packages/java/time/$ZoneOffset" {
import {$Comparable, $Comparable$Type} from "packages/java/lang/$Comparable"
import {$TemporalQuery, $TemporalQuery$Type} from "packages/java/time/temporal/$TemporalQuery"
import {$TemporalField, $TemporalField$Type} from "packages/java/time/temporal/$TemporalField"
import {$Serializable, $Serializable$Type} from "packages/java/io/$Serializable"
import {$TemporalAdjuster, $TemporalAdjuster$Type} from "packages/java/time/temporal/$TemporalAdjuster"
import {$ZoneRules, $ZoneRules$Type} from "packages/java/time/zone/$ZoneRules"
import {$Temporal, $Temporal$Type} from "packages/java/time/temporal/$Temporal"
import {$ZoneId, $ZoneId$Type} from "packages/java/time/$ZoneId"
import {$TemporalAccessor, $TemporalAccessor$Type} from "packages/java/time/temporal/$TemporalAccessor"
import {$ValueRange, $ValueRange$Type} from "packages/java/time/temporal/$ValueRange"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $ZoneOffset extends $ZoneId implements $TemporalAccessor, $TemporalAdjuster, $Comparable<($ZoneOffset)>, $Serializable {
static readonly "UTC": $ZoneOffset
static readonly "MIN": $ZoneOffset
static readonly "MAX": $ZoneOffset
static readonly "SHORT_IDS": $Map<(string), (string)>


public "get"(arg0: $TemporalField$Type): integer
public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "compareTo"(arg0: $ZoneOffset$Type): integer
public "getLong"(arg0: $TemporalField$Type): long
public static "of"(arg0: string): $ZoneOffset
public static "from"(arg0: $TemporalAccessor$Type): $ZoneOffset
public "getId"(): string
public "query"<R>(arg0: $TemporalQuery$Type<(R)>): R
public "range"(arg0: $TemporalField$Type): $ValueRange
public "isSupported"(arg0: $TemporalField$Type): boolean
public static "ofHours"(arg0: integer): $ZoneOffset
public "adjustInto"(arg0: $Temporal$Type): $Temporal
public "getRules"(): $ZoneRules
public "getTotalSeconds"(): integer
public static "ofHoursMinutesSeconds"(arg0: integer, arg1: integer, arg2: integer): $ZoneOffset
public static "ofTotalSeconds"(arg0: integer): $ZoneOffset
public static "ofHoursMinutes"(arg0: integer, arg1: integer): $ZoneOffset
get "id"(): string
get "rules"(): $ZoneRules
get "totalSeconds"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ZoneOffset$Type = ($ZoneOffset);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ZoneOffset_ = $ZoneOffset$Type;
}}
declare module "packages/java/time/$OffsetTime" {
import {$Comparable, $Comparable$Type} from "packages/java/lang/$Comparable"
import {$TemporalField, $TemporalField$Type} from "packages/java/time/temporal/$TemporalField"
import {$Serializable, $Serializable$Type} from "packages/java/io/$Serializable"
import {$Instant, $Instant$Type} from "packages/java/time/$Instant"
import {$LocalDate, $LocalDate$Type} from "packages/java/time/$LocalDate"
import {$ZoneOffset, $ZoneOffset$Type} from "packages/java/time/$ZoneOffset"
import {$TemporalAccessor, $TemporalAccessor$Type} from "packages/java/time/temporal/$TemporalAccessor"
import {$Clock, $Clock$Type} from "packages/java/time/$Clock"
import {$ValueRange, $ValueRange$Type} from "packages/java/time/temporal/$ValueRange"
import {$TemporalAmount, $TemporalAmount$Type} from "packages/java/time/temporal/$TemporalAmount"
import {$TemporalQuery, $TemporalQuery$Type} from "packages/java/time/temporal/$TemporalQuery"
import {$TemporalAdjuster, $TemporalAdjuster$Type} from "packages/java/time/temporal/$TemporalAdjuster"
import {$DateTimeFormatter, $DateTimeFormatter$Type} from "packages/java/time/format/$DateTimeFormatter"
import {$TemporalUnit, $TemporalUnit$Type} from "packages/java/time/temporal/$TemporalUnit"
import {$ZoneId, $ZoneId$Type} from "packages/java/time/$ZoneId"
import {$Temporal, $Temporal$Type} from "packages/java/time/temporal/$Temporal"
import {$LocalTime, $LocalTime$Type} from "packages/java/time/$LocalTime"
import {$OffsetDateTime, $OffsetDateTime$Type} from "packages/java/time/$OffsetDateTime"

export class $OffsetTime implements $Temporal, $TemporalAdjuster, $Comparable<($OffsetTime)>, $Serializable {
static readonly "MIN": $OffsetTime
static readonly "MAX": $OffsetTime


public "get"(arg0: $TemporalField$Type): integer
public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "compareTo"(arg0: $OffsetTime$Type): integer
public "getLong"(arg0: $TemporalField$Type): long
public "format"(arg0: $DateTimeFormatter$Type): string
public static "of"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: $ZoneOffset$Type): $OffsetTime
public static "of"(arg0: $LocalTime$Type, arg1: $ZoneOffset$Type): $OffsetTime
public static "from"(arg0: $TemporalAccessor$Type): $OffsetTime
public "query"<R>(arg0: $TemporalQuery$Type<(R)>): R
public "getOffset"(): $ZoneOffset
public "range"(arg0: $TemporalField$Type): $ValueRange
public "isSupported"(arg0: $TemporalField$Type): boolean
public "isSupported"(arg0: $TemporalUnit$Type): boolean
public static "parse"(arg0: charseq): $OffsetTime
public static "parse"(arg0: charseq, arg1: $DateTimeFormatter$Type): $OffsetTime
public "plus"(arg0: $TemporalAmount$Type): $OffsetTime
public "plus"(arg0: long, arg1: $TemporalUnit$Type): $OffsetTime
public static "now"(): $OffsetTime
public static "now"(arg0: $Clock$Type): $OffsetTime
public static "now"(arg0: $ZoneId$Type): $OffsetTime
public "getNano"(): integer
public "getHour"(): integer
public "getMinute"(): integer
public "getSecond"(): integer
public "minus"(arg0: long, arg1: $TemporalUnit$Type): $OffsetTime
public "until"(arg0: $Temporal$Type, arg1: $TemporalUnit$Type): long
public "plusNanos"(arg0: long): $OffsetTime
public "plusSeconds"(arg0: long): $OffsetTime
public "plusHours"(arg0: long): $OffsetTime
public "plusMinutes"(arg0: long): $OffsetTime
public "minusHours"(arg0: long): $OffsetTime
public "minusMinutes"(arg0: long): $OffsetTime
public "minusSeconds"(arg0: long): $OffsetTime
public "minusNanos"(arg0: long): $OffsetTime
public "truncatedTo"(arg0: $TemporalUnit$Type): $OffsetTime
public "adjustInto"(arg0: $Temporal$Type): $Temporal
public "with"(arg0: $TemporalField$Type, arg1: long): $OffsetTime
public static "ofInstant"(arg0: $Instant$Type, arg1: $ZoneId$Type): $OffsetTime
public "isAfter"(arg0: $OffsetTime$Type): boolean
public "isBefore"(arg0: $OffsetTime$Type): boolean
public "toLocalTime"(): $LocalTime
public "isEqual"(arg0: $OffsetTime$Type): boolean
public "toEpochSecond"(arg0: $LocalDate$Type): long
public "withHour"(arg0: integer): $OffsetTime
public "withMinute"(arg0: integer): $OffsetTime
public "withSecond"(arg0: integer): $OffsetTime
public "withNano"(arg0: integer): $OffsetTime
public "atDate"(arg0: $LocalDate$Type): $OffsetDateTime
public "withOffsetSameInstant"(arg0: $ZoneOffset$Type): $OffsetTime
public "withOffsetSameLocal"(arg0: $ZoneOffset$Type): $OffsetTime
get "offset"(): $ZoneOffset
get "nano"(): integer
get "hour"(): integer
get "minute"(): integer
get "second"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $OffsetTime$Type = ($OffsetTime);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $OffsetTime_ = $OffsetTime$Type;
}}
declare module "packages/java/time/$Month" {
import {$TemporalQuery, $TemporalQuery$Type} from "packages/java/time/temporal/$TemporalQuery"
import {$TextStyle, $TextStyle$Type} from "packages/java/time/format/$TextStyle"
import {$TemporalField, $TemporalField$Type} from "packages/java/time/temporal/$TemporalField"
import {$TemporalAdjuster, $TemporalAdjuster$Type} from "packages/java/time/temporal/$TemporalAdjuster"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$Temporal, $Temporal$Type} from "packages/java/time/temporal/$Temporal"
import {$TemporalAccessor, $TemporalAccessor$Type} from "packages/java/time/temporal/$TemporalAccessor"
import {$ValueRange, $ValueRange$Type} from "packages/java/time/temporal/$ValueRange"
import {$Locale, $Locale$Type} from "packages/java/util/$Locale"

export class $Month extends $Enum<($Month)> implements $TemporalAccessor, $TemporalAdjuster {
static readonly "JANUARY": $Month
static readonly "FEBRUARY": $Month
static readonly "MARCH": $Month
static readonly "APRIL": $Month
static readonly "MAY": $Month
static readonly "JUNE": $Month
static readonly "JULY": $Month
static readonly "AUGUST": $Month
static readonly "SEPTEMBER": $Month
static readonly "OCTOBER": $Month
static readonly "NOVEMBER": $Month
static readonly "DECEMBER": $Month


public "get"(arg0: $TemporalField$Type): integer
public "length"(arg0: boolean): integer
public static "values"(): ($Month)[]
public "getLong"(arg0: $TemporalField$Type): long
public static "valueOf"(arg0: string): $Month
public "getValue"(): integer
public static "of"(arg0: integer): $Month
public static "from"(arg0: $TemporalAccessor$Type): $Month
public "query"<R>(arg0: $TemporalQuery$Type<(R)>): R
public "range"(arg0: $TemporalField$Type): $ValueRange
public "isSupported"(arg0: $TemporalField$Type): boolean
public "minLength"(): integer
public "plus"(arg0: long): $Month
public "getDisplayName"(arg0: $TextStyle$Type, arg1: $Locale$Type): string
public "minus"(arg0: long): $Month
public "adjustInto"(arg0: $Temporal$Type): $Temporal
public "firstDayOfYear"(arg0: boolean): integer
public "maxLength"(): integer
public "firstMonthOfQuarter"(): $Month
get "value"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Month$Type = (("november") | ("june") | ("september") | ("may") | ("august") | ("january") | ("february") | ("july") | ("december") | ("october") | ("april") | ("march")) | ($Month);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Month_ = $Month$Type;
}}
declare module "packages/java/time/temporal/$TemporalAmount" {
import {$TemporalUnit, $TemporalUnit$Type} from "packages/java/time/temporal/$TemporalUnit"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Temporal, $Temporal$Type} from "packages/java/time/temporal/$Temporal"

export interface $TemporalAmount {

 "get"(arg0: $TemporalUnit$Type): long
 "getUnits"(): $List<($TemporalUnit)>
 "addTo"(arg0: $Temporal$Type): $Temporal
 "subtractFrom"(arg0: $Temporal$Type): $Temporal
}

export namespace $TemporalAmount {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TemporalAmount$Type = (string) | (number) | ($TemporalAmount);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TemporalAmount_ = $TemporalAmount$Type;
}}
declare module "packages/java/time/chrono/$ChronoLocalDateTime" {
import {$Comparator, $Comparator$Type} from "packages/java/util/$Comparator"
import {$Comparable, $Comparable$Type} from "packages/java/lang/$Comparable"
import {$TemporalField, $TemporalField$Type} from "packages/java/time/temporal/$TemporalField"
import {$ChronoZonedDateTime, $ChronoZonedDateTime$Type} from "packages/java/time/chrono/$ChronoZonedDateTime"
import {$Instant, $Instant$Type} from "packages/java/time/$Instant"
import {$TemporalAccessor, $TemporalAccessor$Type} from "packages/java/time/temporal/$TemporalAccessor"
import {$ZoneOffset, $ZoneOffset$Type} from "packages/java/time/$ZoneOffset"
import {$ChronoLocalDate, $ChronoLocalDate$Type} from "packages/java/time/chrono/$ChronoLocalDate"
import {$ValueRange, $ValueRange$Type} from "packages/java/time/temporal/$ValueRange"
import {$Chronology, $Chronology$Type} from "packages/java/time/chrono/$Chronology"
import {$TemporalAmount, $TemporalAmount$Type} from "packages/java/time/temporal/$TemporalAmount"
import {$TemporalQuery, $TemporalQuery$Type} from "packages/java/time/temporal/$TemporalQuery"
import {$TemporalAdjuster, $TemporalAdjuster$Type} from "packages/java/time/temporal/$TemporalAdjuster"
import {$DateTimeFormatter, $DateTimeFormatter$Type} from "packages/java/time/format/$DateTimeFormatter"
import {$TemporalUnit, $TemporalUnit$Type} from "packages/java/time/temporal/$TemporalUnit"
import {$Temporal, $Temporal$Type} from "packages/java/time/temporal/$Temporal"
import {$ZoneId, $ZoneId$Type} from "packages/java/time/$ZoneId"
import {$LocalTime, $LocalTime$Type} from "packages/java/time/$LocalTime"

export interface $ChronoLocalDateTime<D extends $ChronoLocalDate> extends $Temporal, $TemporalAdjuster, $Comparable<($ChronoLocalDateTime<(any)>)> {

 "equals"(arg0: any): boolean
 "toString"(): string
 "hashCode"(): integer
 "compareTo"(arg0: $ChronoLocalDateTime$Type<(any)>): integer
 "format"(arg0: $DateTimeFormatter$Type): string
 "query"<R>(arg0: $TemporalQuery$Type<(R)>): R
 "isSupported"(arg0: $TemporalField$Type): boolean
 "isSupported"(arg0: $TemporalUnit$Type): boolean
 "plus"(arg0: $TemporalAmount$Type): $ChronoLocalDateTime<(D)>
 "toInstant"(arg0: $ZoneOffset$Type): $Instant
 "minus"(arg0: long, arg1: $TemporalUnit$Type): $ChronoLocalDateTime<(D)>
 "adjustInto"(arg0: $Temporal$Type): $Temporal
 "atZone"(arg0: $ZoneId$Type): $ChronoZonedDateTime<(D)>
 "isAfter"(arg0: $ChronoLocalDateTime$Type<(any)>): boolean
 "isBefore"(arg0: $ChronoLocalDateTime$Type<(any)>): boolean
 "toLocalTime"(): $LocalTime
 "isEqual"(arg0: $ChronoLocalDateTime$Type<(any)>): boolean
 "getChronology"(): $Chronology
 "toEpochSecond"(arg0: $ZoneOffset$Type): long
 "toLocalDate"(): D
 "until"(arg0: $Temporal$Type, arg1: $TemporalUnit$Type): long
 "get"(arg0: $TemporalField$Type): integer
 "getLong"(arg0: $TemporalField$Type): long
 "range"(arg0: $TemporalField$Type): $ValueRange
}

export namespace $ChronoLocalDateTime {
function from(arg0: $TemporalAccessor$Type): $ChronoLocalDateTime<(any)>
function timeLineOrder(): $Comparator<($ChronoLocalDateTime<(any)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ChronoLocalDateTime$Type<D> = ($ChronoLocalDateTime<(D)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ChronoLocalDateTime_<D> = $ChronoLocalDateTime$Type<(D)>;
}}
declare module "packages/java/time/chrono/$ChronoZonedDateTime" {
import {$Comparator, $Comparator$Type} from "packages/java/util/$Comparator"
import {$Comparable, $Comparable$Type} from "packages/java/lang/$Comparable"
import {$TemporalField, $TemporalField$Type} from "packages/java/time/temporal/$TemporalField"
import {$Instant, $Instant$Type} from "packages/java/time/$Instant"
import {$TemporalAccessor, $TemporalAccessor$Type} from "packages/java/time/temporal/$TemporalAccessor"
import {$ZoneOffset, $ZoneOffset$Type} from "packages/java/time/$ZoneOffset"
import {$ValueRange, $ValueRange$Type} from "packages/java/time/temporal/$ValueRange"
import {$ChronoLocalDate, $ChronoLocalDate$Type} from "packages/java/time/chrono/$ChronoLocalDate"
import {$Chronology, $Chronology$Type} from "packages/java/time/chrono/$Chronology"
import {$TemporalAmount, $TemporalAmount$Type} from "packages/java/time/temporal/$TemporalAmount"
import {$TemporalQuery, $TemporalQuery$Type} from "packages/java/time/temporal/$TemporalQuery"
import {$TemporalAdjuster, $TemporalAdjuster$Type} from "packages/java/time/temporal/$TemporalAdjuster"
import {$DateTimeFormatter, $DateTimeFormatter$Type} from "packages/java/time/format/$DateTimeFormatter"
import {$TemporalUnit, $TemporalUnit$Type} from "packages/java/time/temporal/$TemporalUnit"
import {$ZoneId, $ZoneId$Type} from "packages/java/time/$ZoneId"
import {$Temporal, $Temporal$Type} from "packages/java/time/temporal/$Temporal"
import {$LocalTime, $LocalTime$Type} from "packages/java/time/$LocalTime"
import {$ChronoLocalDateTime, $ChronoLocalDateTime$Type} from "packages/java/time/chrono/$ChronoLocalDateTime"

export interface $ChronoZonedDateTime<D extends $ChronoLocalDate> extends $Temporal, $Comparable<($ChronoZonedDateTime<(any)>)> {

 "get"(arg0: $TemporalField$Type): integer
 "equals"(arg0: any): boolean
 "toString"(): string
 "hashCode"(): integer
 "compareTo"(arg0: $ChronoZonedDateTime$Type<(any)>): integer
 "getLong"(arg0: $TemporalField$Type): long
 "format"(arg0: $DateTimeFormatter$Type): string
 "query"<R>(arg0: $TemporalQuery$Type<(R)>): R
 "getOffset"(): $ZoneOffset
 "range"(arg0: $TemporalField$Type): $ValueRange
 "isSupported"(arg0: $TemporalUnit$Type): boolean
 "isSupported"(arg0: $TemporalField$Type): boolean
 "plus"(arg0: $TemporalAmount$Type): $ChronoZonedDateTime<(D)>
 "toInstant"(): $Instant
 "minus"(arg0: long, arg1: $TemporalUnit$Type): $ChronoZonedDateTime<(D)>
 "getZone"(): $ZoneId
 "with"(arg0: $TemporalAdjuster$Type): $ChronoZonedDateTime<(D)>
 "with"(arg0: $TemporalField$Type, arg1: long): $ChronoZonedDateTime<(D)>
 "isAfter"(arg0: $ChronoZonedDateTime$Type<(any)>): boolean
 "isBefore"(arg0: $ChronoZonedDateTime$Type<(any)>): boolean
 "toLocalTime"(): $LocalTime
 "isEqual"(arg0: $ChronoZonedDateTime$Type<(any)>): boolean
 "getChronology"(): $Chronology
 "toEpochSecond"(): long
 "toLocalDateTime"(): $ChronoLocalDateTime<(D)>
 "toLocalDate"(): D
 "withZoneSameInstant"(arg0: $ZoneId$Type): $ChronoZonedDateTime<(D)>
 "withZoneSameLocal"(arg0: $ZoneId$Type): $ChronoZonedDateTime<(D)>
 "withLaterOffsetAtOverlap"(): $ChronoZonedDateTime<(D)>
 "withEarlierOffsetAtOverlap"(): $ChronoZonedDateTime<(D)>
 "until"(arg0: $Temporal$Type, arg1: $TemporalUnit$Type): long
}

export namespace $ChronoZonedDateTime {
function from(arg0: $TemporalAccessor$Type): $ChronoZonedDateTime<(any)>
function timeLineOrder(): $Comparator<($ChronoZonedDateTime<(any)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ChronoZonedDateTime$Type<D> = ($ChronoZonedDateTime<(D)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ChronoZonedDateTime_<D> = $ChronoZonedDateTime$Type<(D)>;
}}
declare module "packages/java/time/format/$DecimalStyle" {
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$Locale, $Locale$Type} from "packages/java/util/$Locale"

export class $DecimalStyle {
static readonly "STANDARD": $DecimalStyle


public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public static "of"(arg0: $Locale$Type): $DecimalStyle
public static "getAvailableLocales"(): $Set<($Locale)>
public "getZeroDigit"(): character
public "getDecimalSeparator"(): character
public "getPositiveSign"(): character
public "getNegativeSign"(): character
public static "ofDefaultLocale"(): $DecimalStyle
public "withZeroDigit"(arg0: character): $DecimalStyle
public "withPositiveSign"(arg0: character): $DecimalStyle
public "withNegativeSign"(arg0: character): $DecimalStyle
public "withDecimalSeparator"(arg0: character): $DecimalStyle
get "availableLocales"(): $Set<($Locale)>
get "zeroDigit"(): character
get "decimalSeparator"(): character
get "positiveSign"(): character
get "negativeSign"(): character
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DecimalStyle$Type = ($DecimalStyle);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DecimalStyle_ = $DecimalStyle$Type;
}}
declare module "packages/java/time/chrono/$Era" {
import {$TemporalQuery, $TemporalQuery$Type} from "packages/java/time/temporal/$TemporalQuery"
import {$TextStyle, $TextStyle$Type} from "packages/java/time/format/$TextStyle"
import {$TemporalField, $TemporalField$Type} from "packages/java/time/temporal/$TemporalField"
import {$TemporalAdjuster, $TemporalAdjuster$Type} from "packages/java/time/temporal/$TemporalAdjuster"
import {$Temporal, $Temporal$Type} from "packages/java/time/temporal/$Temporal"
import {$TemporalAccessor, $TemporalAccessor$Type} from "packages/java/time/temporal/$TemporalAccessor"
import {$ValueRange, $ValueRange$Type} from "packages/java/time/temporal/$ValueRange"
import {$Locale, $Locale$Type} from "packages/java/util/$Locale"

export interface $Era extends $TemporalAccessor, $TemporalAdjuster {

 "get"(arg0: $TemporalField$Type): integer
 "getLong"(arg0: $TemporalField$Type): long
 "getValue"(): integer
 "query"<R>(arg0: $TemporalQuery$Type<(R)>): R
 "range"(arg0: $TemporalField$Type): $ValueRange
 "isSupported"(arg0: $TemporalField$Type): boolean
 "getDisplayName"(arg0: $TextStyle$Type, arg1: $Locale$Type): string
 "adjustInto"(arg0: $Temporal$Type): $Temporal

(arg0: $TemporalField$Type): integer
}

export namespace $Era {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Era$Type = ($Era);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Era_ = $Era$Type;
}}
declare module "packages/java/time/temporal/$ChronoUnit" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$TemporalUnit, $TemporalUnit$Type} from "packages/java/time/temporal/$TemporalUnit"
import {$Temporal, $Temporal$Type} from "packages/java/time/temporal/$Temporal"
import {$Duration, $Duration$Type} from "packages/java/time/$Duration"

export class $ChronoUnit extends $Enum<($ChronoUnit)> implements $TemporalUnit {
static readonly "NANOS": $ChronoUnit
static readonly "MICROS": $ChronoUnit
static readonly "MILLIS": $ChronoUnit
static readonly "SECONDS": $ChronoUnit
static readonly "MINUTES": $ChronoUnit
static readonly "HOURS": $ChronoUnit
static readonly "HALF_DAYS": $ChronoUnit
static readonly "DAYS": $ChronoUnit
static readonly "WEEKS": $ChronoUnit
static readonly "MONTHS": $ChronoUnit
static readonly "YEARS": $ChronoUnit
static readonly "DECADES": $ChronoUnit
static readonly "CENTURIES": $ChronoUnit
static readonly "MILLENNIA": $ChronoUnit
static readonly "ERAS": $ChronoUnit
static readonly "FOREVER": $ChronoUnit


public "toString"(): string
public static "values"(): ($ChronoUnit)[]
public static "valueOf"(arg0: string): $ChronoUnit
public "between"(arg0: $Temporal$Type, arg1: $Temporal$Type): long
public "isDurationEstimated"(): boolean
public "getDuration"(): $Duration
public "addTo"<R extends $Temporal>(arg0: R, arg1: long): R
public "isSupportedBy"(arg0: $Temporal$Type): boolean
public "isTimeBased"(): boolean
public "isDateBased"(): boolean
get "durationEstimated"(): boolean
get "duration"(): $Duration
get "timeBased"(): boolean
get "dateBased"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ChronoUnit$Type = (("centuries") | ("hours") | ("weeks") | ("months") | ("nanos") | ("minutes") | ("millennia") | ("years") | ("eras") | ("seconds") | ("micros") | ("days") | ("decades") | ("half_days") | ("millis") | ("forever")) | ($ChronoUnit);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ChronoUnit_ = $ChronoUnit$Type;
}}
declare module "packages/java/time/format/$FormatStyle" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $FormatStyle extends $Enum<($FormatStyle)> {
static readonly "FULL": $FormatStyle
static readonly "LONG": $FormatStyle
static readonly "MEDIUM": $FormatStyle
static readonly "SHORT": $FormatStyle


public static "values"(): ($FormatStyle)[]
public static "valueOf"(arg0: string): $FormatStyle
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FormatStyle$Type = (("short") | ("medium") | ("long") | ("full")) | ($FormatStyle);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FormatStyle_ = $FormatStyle$Type;
}}

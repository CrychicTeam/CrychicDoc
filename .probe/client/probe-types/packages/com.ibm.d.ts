declare module "packages/com/ibm/icu/util/$Currency$CurrencyUsage" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $Currency$CurrencyUsage extends $Enum<($Currency$CurrencyUsage)> {
static readonly "STANDARD": $Currency$CurrencyUsage
static readonly "CASH": $Currency$CurrencyUsage


public static "values"(): ($Currency$CurrencyUsage)[]
public static "valueOf"(arg0: string): $Currency$CurrencyUsage
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Currency$CurrencyUsage$Type = (("standard") | ("cash")) | ($Currency$CurrencyUsage);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Currency$CurrencyUsage_ = $Currency$CurrencyUsage$Type;
}}
declare module "packages/com/ibm/icu/util/$MeasureUnit$Complexity" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $MeasureUnit$Complexity extends $Enum<($MeasureUnit$Complexity)> {
static readonly "SINGLE": $MeasureUnit$Complexity
static readonly "COMPOUND": $MeasureUnit$Complexity
static readonly "MIXED": $MeasureUnit$Complexity


public static "values"(): ($MeasureUnit$Complexity)[]
public static "valueOf"(arg0: string): $MeasureUnit$Complexity
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MeasureUnit$Complexity$Type = (("single") | ("mixed") | ("compound")) | ($MeasureUnit$Complexity);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MeasureUnit$Complexity_ = $MeasureUnit$Complexity$Type;
}}
declare module "packages/com/ibm/icu/text/$UFormat" {
import {$Format, $Format$Type} from "packages/java/text/$Format"
import {$ULocale$Type, $ULocale$Type$Type} from "packages/com/ibm/icu/util/$ULocale$Type"
import {$ULocale, $ULocale$Type} from "packages/com/ibm/icu/util/$ULocale"

export class $UFormat extends $Format {

constructor()

public "getLocale"(arg0: $ULocale$Type$Type): $ULocale
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $UFormat$Type = ($UFormat);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $UFormat_ = $UFormat$Type;
}}
declare module "packages/com/ibm/icu/text/$Replaceable" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $Replaceable {

 "length"(): integer
 "getChars"(arg0: integer, arg1: integer, arg2: (character)[], arg3: integer): void
 "charAt"(arg0: integer): character
 "replace"(arg0: integer, arg1: integer, arg2: string): void
 "replace"(arg0: integer, arg1: integer, arg2: (character)[], arg3: integer, arg4: integer): void
 "copy"(arg0: integer, arg1: integer, arg2: integer): void
 "char32At"(arg0: integer): integer
 "hasMetaData"(): boolean
}

export namespace $Replaceable {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Replaceable$Type = ($Replaceable);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Replaceable_ = $Replaceable$Type;
}}
declare module "packages/com/ibm/icu/util/$MeasureUnit$MeasurePrefix" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $MeasureUnit$MeasurePrefix extends $Enum<($MeasureUnit$MeasurePrefix)> {
static readonly "YOTTA": $MeasureUnit$MeasurePrefix
static readonly "ZETTA": $MeasureUnit$MeasurePrefix
static readonly "EXA": $MeasureUnit$MeasurePrefix
static readonly "PETA": $MeasureUnit$MeasurePrefix
static readonly "TERA": $MeasureUnit$MeasurePrefix
static readonly "GIGA": $MeasureUnit$MeasurePrefix
static readonly "MEGA": $MeasureUnit$MeasurePrefix
static readonly "KILO": $MeasureUnit$MeasurePrefix
static readonly "HECTO": $MeasureUnit$MeasurePrefix
static readonly "DEKA": $MeasureUnit$MeasurePrefix
static readonly "ONE": $MeasureUnit$MeasurePrefix
static readonly "DECI": $MeasureUnit$MeasurePrefix
static readonly "CENTI": $MeasureUnit$MeasurePrefix
static readonly "MILLI": $MeasureUnit$MeasurePrefix
static readonly "MICRO": $MeasureUnit$MeasurePrefix
static readonly "NANO": $MeasureUnit$MeasurePrefix
static readonly "PICO": $MeasureUnit$MeasurePrefix
static readonly "FEMTO": $MeasureUnit$MeasurePrefix
static readonly "ATTO": $MeasureUnit$MeasurePrefix
static readonly "ZEPTO": $MeasureUnit$MeasurePrefix
static readonly "YOCTO": $MeasureUnit$MeasurePrefix
static readonly "KIBI": $MeasureUnit$MeasurePrefix
static readonly "MEBI": $MeasureUnit$MeasurePrefix
static readonly "GIBI": $MeasureUnit$MeasurePrefix
static readonly "TEBI": $MeasureUnit$MeasurePrefix
static readonly "PEBI": $MeasureUnit$MeasurePrefix
static readonly "EXBI": $MeasureUnit$MeasurePrefix
static readonly "ZEBI": $MeasureUnit$MeasurePrefix
static readonly "YOBI": $MeasureUnit$MeasurePrefix


public static "values"(): ($MeasureUnit$MeasurePrefix)[]
public static "valueOf"(arg0: string): $MeasureUnit$MeasurePrefix
public "getBase"(): integer
/**
 * 
 * @deprecated
 */
public "getIdentifier"(): string
public "getPower"(): integer
get "base"(): integer
get "identifier"(): string
get "power"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MeasureUnit$MeasurePrefix$Type = (("mega") | ("atto") | ("femto") | ("zepto") | ("nano") | ("pebi") | ("deci") | ("zebi") | ("giga") | ("tebi") | ("exa") | ("pico") | ("kilo") | ("yocto") | ("exbi") | ("yotta") | ("peta") | ("tera") | ("kibi") | ("gibi") | ("centi") | ("yobi") | ("one") | ("hecto") | ("mebi") | ("zetta") | ("micro") | ("deka") | ("milli")) | ($MeasureUnit$MeasurePrefix);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MeasureUnit$MeasurePrefix_ = $MeasureUnit$MeasurePrefix$Type;
}}
declare module "packages/com/ibm/icu/text/$DisplayContext$Type" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $DisplayContext$Type extends $Enum<($DisplayContext$Type)> {
static readonly "DIALECT_HANDLING": $DisplayContext$Type
static readonly "CAPITALIZATION": $DisplayContext$Type
static readonly "DISPLAY_LENGTH": $DisplayContext$Type
static readonly "SUBSTITUTE_HANDLING": $DisplayContext$Type


public static "values"(): ($DisplayContext$Type)[]
public static "valueOf"(arg0: string): $DisplayContext$Type
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DisplayContext$Type$Type = (("display_length") | ("dialect_handling") | ("capitalization") | ("substitute_handling")) | ($DisplayContext$Type);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DisplayContext$Type_ = $DisplayContext$Type$Type;
}}
declare module "packages/com/ibm/icu/text/$SymbolTable" {
import {$ParsePosition, $ParsePosition$Type} from "packages/java/text/$ParsePosition"
import {$UnicodeMatcher, $UnicodeMatcher$Type} from "packages/com/ibm/icu/text/$UnicodeMatcher"

export interface $SymbolTable {

 "lookup"(arg0: string): (character)[]
 "parseReference"(arg0: string, arg1: $ParsePosition$Type, arg2: integer): string
 "lookupMatcher"(arg0: integer): $UnicodeMatcher
}

export namespace $SymbolTable {
const SYMBOL_REF: character
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SymbolTable$Type = ($SymbolTable);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SymbolTable_ = $SymbolTable$Type;
}}
declare module "packages/com/ibm/icu/text/$UnicodeFilter" {
import {$Replaceable, $Replaceable$Type} from "packages/com/ibm/icu/text/$Replaceable"
import {$UnicodeSet, $UnicodeSet$Type} from "packages/com/ibm/icu/text/$UnicodeSet"
import {$UnicodeMatcher, $UnicodeMatcher$Type} from "packages/com/ibm/icu/text/$UnicodeMatcher"

export class $UnicodeFilter implements $UnicodeMatcher {


public "matches"(arg0: $Replaceable$Type, arg1: (integer)[], arg2: integer, arg3: boolean): integer
public "contains"(arg0: integer): boolean
public "toPattern"(arg0: boolean): string
public "matchesIndexValue"(arg0: integer): boolean
public "addMatchSetTo"(arg0: $UnicodeSet$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $UnicodeFilter$Type = ($UnicodeFilter);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $UnicodeFilter_ = $UnicodeFilter$Type;
}}
declare module "packages/com/ibm/icu/util/$OutputInt" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
/**
 * 
 * @deprecated
 */
export class $OutputInt {
/**
 * 
 * @deprecated
 */
 "value": integer

/**
 * 
 * @deprecated
 */
constructor()
/**
 * 
 * @deprecated
 */
constructor(arg0: integer)

/**
 * 
 * @deprecated
 */
public "toString"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $OutputInt$Type = ($OutputInt);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $OutputInt_ = $OutputInt$Type;
}}
declare module "packages/com/ibm/icu/util/$Measure" {
import {$MeasureUnit, $MeasureUnit$Type} from "packages/com/ibm/icu/util/$MeasureUnit"

export class $Measure {

constructor(arg0: number, arg1: $MeasureUnit$Type)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "getNumber"(): number
public "getUnit"(): $MeasureUnit
get "number"(): number
get "unit"(): $MeasureUnit
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Measure$Type = ($Measure);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Measure_ = $Measure$Type;
}}
declare module "packages/com/ibm/icu/text/$NumberFormat$NumberFormatFactory" {
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$NumberFormat, $NumberFormat$Type} from "packages/com/ibm/icu/text/$NumberFormat"
import {$Locale, $Locale$Type} from "packages/java/util/$Locale"
import {$ULocale, $ULocale$Type} from "packages/com/ibm/icu/util/$ULocale"

export class $NumberFormat$NumberFormatFactory {
static readonly "FORMAT_NUMBER": integer
static readonly "FORMAT_CURRENCY": integer
static readonly "FORMAT_PERCENT": integer
static readonly "FORMAT_SCIENTIFIC": integer
static readonly "FORMAT_INTEGER": integer


public "visible"(): boolean
public "createFormat"(arg0: $Locale$Type, arg1: integer): $NumberFormat
public "createFormat"(arg0: $ULocale$Type, arg1: integer): $NumberFormat
public "getSupportedLocaleNames"(): $Set<(string)>
get "supportedLocaleNames"(): $Set<(string)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NumberFormat$NumberFormatFactory$Type = ($NumberFormat$NumberFormatFactory);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NumberFormat$NumberFormatFactory_ = $NumberFormat$NumberFormatFactory$Type;
}}
declare module "packages/com/ibm/icu/util/$ULocale$Category" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $ULocale$Category extends $Enum<($ULocale$Category)> {
static readonly "DISPLAY": $ULocale$Category
static readonly "FORMAT": $ULocale$Category


public static "values"(): ($ULocale$Category)[]
public static "valueOf"(arg0: string): $ULocale$Category
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ULocale$Category$Type = (("display") | ("format")) | ($ULocale$Category);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ULocale$Category_ = $ULocale$Category$Type;
}}
declare module "packages/com/ibm/icu/text/$UnicodeSet" {
import {$Comparable, $Comparable$Type} from "packages/java/lang/$Comparable"
import {$SymbolTable, $SymbolTable$Type} from "packages/com/ibm/icu/text/$SymbolTable"
import {$UnicodeSet$EntryRange, $UnicodeSet$EntryRange$Type} from "packages/com/ibm/icu/text/$UnicodeSet$EntryRange"
import {$Freezable, $Freezable$Type} from "packages/com/ibm/icu/util/$Freezable"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Replaceable, $Replaceable$Type} from "packages/com/ibm/icu/text/$Replaceable"
import {$Iterable, $Iterable$Type} from "packages/java/lang/$Iterable"
import {$Spliterator, $Spliterator$Type} from "packages/java/util/$Spliterator"
import {$UnicodeSet$SpanCondition, $UnicodeSet$SpanCondition$Type} from "packages/com/ibm/icu/text/$UnicodeSet$SpanCondition"
import {$OutputInt, $OutputInt$Type} from "packages/com/ibm/icu/util/$OutputInt"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$UnicodeSet$XSymbolTable, $UnicodeSet$XSymbolTable$Type} from "packages/com/ibm/icu/text/$UnicodeSet$XSymbolTable"
import {$UnicodeSet$ComparisonStyle, $UnicodeSet$ComparisonStyle$Type} from "packages/com/ibm/icu/text/$UnicodeSet$ComparisonStyle"
import {$StringBuffer, $StringBuffer$Type} from "packages/java/lang/$StringBuffer"
import {$UnicodeFilter, $UnicodeFilter$Type} from "packages/com/ibm/icu/text/$UnicodeFilter"
import {$ParsePosition, $ParsePosition$Type} from "packages/java/text/$ParsePosition"
import {$Iterator, $Iterator$Type} from "packages/java/util/$Iterator"

export class $UnicodeSet extends $UnicodeFilter implements $Iterable<(string)>, $Comparable<($UnicodeSet)>, $Freezable<($UnicodeSet)> {
static readonly "EMPTY": $UnicodeSet
static readonly "ALL_CODE_POINTS": $UnicodeSet
static readonly "MIN_VALUE": integer
static readonly "MAX_VALUE": integer
static readonly "IGNORE_SPACE": integer
static readonly "CASE": integer
static readonly "CASE_INSENSITIVE": integer
static readonly "ADD_CASE_MAPPINGS": integer

constructor(arg0: string)
constructor(arg0: string, arg1: boolean)
constructor(arg0: string, arg1: integer)
constructor(arg0: string, arg1: $ParsePosition$Type, arg2: $SymbolTable$Type)
constructor(arg0: string, arg1: $ParsePosition$Type, arg2: $SymbolTable$Type, arg3: integer)
constructor()
constructor(arg0: $UnicodeSet$Type)
constructor(arg0: integer, arg1: integer)
constructor(...arg0: (integer)[])

public "add"(arg0: charseq): $UnicodeSet
public "add"(arg0: integer, arg1: integer): $UnicodeSet
public "add"(arg0: integer): $UnicodeSet
public "add"(arg0: $Iterable$Type<(any)>): $UnicodeSet
public "remove"(arg0: integer, arg1: integer): $UnicodeSet
public "remove"(arg0: charseq): $UnicodeSet
public "remove"(arg0: integer): $UnicodeSet
public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "clone"(): any
public "compareTo"(arg0: $UnicodeSet$Type, arg1: $UnicodeSet$ComparisonStyle$Type): integer
public "compareTo"(arg0: $UnicodeSet$Type): integer
public "compareTo"(arg0: $Iterable$Type<(string)>): integer
public "indexOf"(arg0: integer): integer
public static "compare"(arg0: charseq, arg1: integer): integer
public static "compare"<T extends $Comparable<(T)>>(arg0: $Iterable$Type<(T)>, arg1: $Iterable$Type<(T)>): integer
public static "compare"(arg0: integer, arg1: charseq): integer
public static "compare"<T extends $Comparable<(T)>>(arg0: $Collection$Type<(T)>, arg1: $Collection$Type<(T)>, arg2: $UnicodeSet$ComparisonStyle$Type): integer
/**
 * 
 * @deprecated
 */
public static "compare"<T extends $Comparable<(T)>>(arg0: $Iterator$Type<(T)>, arg1: $Iterator$Type<(T)>): integer
public "clear"(): $UnicodeSet
public "charAt"(arg0: integer): integer
public "isEmpty"(): boolean
public "matches"(arg0: $Replaceable$Type, arg1: (integer)[], arg2: integer, arg3: boolean): integer
public "size"(): integer
public static "toArray"(arg0: $UnicodeSet$Type): (string)[]
public "iterator"(): $Iterator<(string)>
public "contains"(arg0: charseq): boolean
public "contains"(arg0: integer): boolean
public "contains"(arg0: integer, arg1: integer): boolean
public static "from"(arg0: charseq): $UnicodeSet
public "addAll"(arg0: $UnicodeSet$Type): $UnicodeSet
public "addAll"(arg0: integer, arg1: integer): $UnicodeSet
public "addAll"<T extends charseq>(...arg0: (T)[]): $UnicodeSet
public "addAll"(arg0: $Iterable$Type<(any)>): $UnicodeSet
public "addAll"(arg0: charseq): $UnicodeSet
public "set"(arg0: integer, arg1: integer): $UnicodeSet
public "set"(arg0: $UnicodeSet$Type): $UnicodeSet
public "isFrozen"(): boolean
public "removeAll"(arg0: $UnicodeSet$Type): $UnicodeSet
public "removeAll"<T extends charseq>(arg0: $Iterable$Type<(T)>): $UnicodeSet
public "removeAll"(arg0: charseq): $UnicodeSet
public "retainAll"<T extends charseq>(arg0: $Iterable$Type<(T)>): $UnicodeSet
public "retainAll"(arg0: charseq): $UnicodeSet
public "retainAll"(arg0: $UnicodeSet$Type): $UnicodeSet
public "complement"(arg0: charseq): $UnicodeSet
public "complement"(arg0: integer, arg1: integer): $UnicodeSet
public "complement"(arg0: integer): $UnicodeSet
public "complement"(): $UnicodeSet
public "containsAll"(arg0: string): boolean
public "containsAll"<T extends charseq>(arg0: $Iterable$Type<(T)>): boolean
public "containsAll"(arg0: $UnicodeSet$Type): boolean
public "freeze"(): $UnicodeSet
public "compact"(): $UnicodeSet
/**
 * 
 * @deprecated
 */
public "applyPattern"(arg0: string, arg1: $ParsePosition$Type, arg2: $SymbolTable$Type, arg3: integer): $UnicodeSet
public "applyPattern"(arg0: string): $UnicodeSet
public "applyPattern"(arg0: string, arg1: boolean): $UnicodeSet
public "applyPattern"(arg0: string, arg1: integer): $UnicodeSet
public "toPattern"(arg0: boolean): string
public "strings"(): $Collection<(string)>
public "ranges"(): $Iterable<($UnicodeSet$EntryRange)>
public "containsNone"(arg0: charseq): boolean
public "containsNone"<T extends charseq>(arg0: $Iterable$Type<(T)>): boolean
public "containsNone"(arg0: integer, arg1: integer): boolean
public "containsNone"(arg0: $UnicodeSet$Type): boolean
/**
 * 
 * @deprecated
 */
public "matchesAt"(arg0: charseq, arg1: integer): integer
public static "resemblesPattern"(arg0: string, arg1: integer): boolean
public "_generatePattern"(arg0: $StringBuffer$Type, arg1: boolean, arg2: boolean): $StringBuffer
public "_generatePattern"(arg0: $StringBuffer$Type, arg1: boolean): $StringBuffer
public "matchesIndexValue"(arg0: integer): boolean
public "hasStrings"(): boolean
public "getRangeStart"(arg0: integer): integer
public "getRangeEnd"(arg0: integer): integer
public "complementAll"(arg0: $UnicodeSet$Type): $UnicodeSet
public "complementAll"(arg0: charseq): $UnicodeSet
public "addMatchSetTo"(arg0: $UnicodeSet$Type): void
/**
 * 
 * @deprecated
 */
public "getRegexEquivalent"(): string
public "containsSome"<T extends charseq>(arg0: $Iterable$Type<(T)>): boolean
public "containsSome"(arg0: integer, arg1: integer): boolean
public "containsSome"(arg0: charseq): boolean
public "containsSome"(arg0: $UnicodeSet$Type): boolean
public "getRangeCount"(): integer
public static "fromAll"(arg0: charseq): $UnicodeSet
public "removeAllStrings"(): $UnicodeSet
public static "addAllTo"<T>(arg0: $Iterable$Type<(T)>, arg1: (T)[]): (T)[]
public static "addAllTo"<T, U extends $Collection<(T)>>(arg0: $Iterable$Type<(T)>, arg1: U): U
public "addAllTo"<T extends $Collection<(string)>>(arg0: T): T
public "addAllTo"(arg0: (string)[]): (string)[]
public "applyPropertyAlias"(arg0: string, arg1: string): $UnicodeSet
public "applyPropertyAlias"(arg0: string, arg1: string, arg2: $SymbolTable$Type): $UnicodeSet
public "closeOver"(arg0: integer): $UnicodeSet
public "spanBack"(arg0: charseq, arg1: $UnicodeSet$SpanCondition$Type): integer
public "spanBack"(arg0: charseq, arg1: integer, arg2: $UnicodeSet$SpanCondition$Type): integer
/**
 * 
 * @deprecated
 */
public "addBridges"(arg0: $UnicodeSet$Type): $UnicodeSet
/**
 * 
 * @deprecated
 */
public "findIn"(arg0: charseq, arg1: integer, arg2: boolean): integer
/**
 * 
 * @deprecated
 */
public "stripFrom"(arg0: charseq, arg1: boolean): string
/**
 * 
 * @deprecated
 */
public static "getSingleCodePoint"(arg0: charseq): integer
/**
 * 
 * @deprecated
 */
public "findLastIn"(arg0: charseq, arg1: integer, arg2: boolean): integer
/**
 * 
 * @deprecated
 */
public "spanAndCount"(arg0: charseq, arg1: integer, arg2: $UnicodeSet$SpanCondition$Type, arg3: $OutputInt$Type): integer
public "applyIntPropertyValue"(arg0: integer, arg1: integer): $UnicodeSet
/**
 * 
 * @deprecated
 */
public static "setDefaultXSymbolTable"(arg0: $UnicodeSet$XSymbolTable$Type): void
/**
 * 
 * @deprecated
 */
public static "getDefaultXSymbolTable"(): $UnicodeSet$XSymbolTable
public "retain"(arg0: integer): $UnicodeSet
public "retain"(arg0: integer, arg1: integer): $UnicodeSet
public "retain"(arg0: charseq): $UnicodeSet
public "span"(arg0: charseq, arg1: integer, arg2: $UnicodeSet$SpanCondition$Type): integer
public "span"(arg0: charseq, arg1: $UnicodeSet$SpanCondition$Type): integer
public "spliterator"(): $Spliterator<(string)>
public "forEach"(arg0: $Consumer$Type<(any)>): void
[Symbol.iterator](): IterableIterator<string>;
get "empty"(): boolean
get "frozen"(): boolean
get "regexEquivalent"(): string
get "rangeCount"(): integer
set "defaultXSymbolTable"(value: $UnicodeSet$XSymbolTable$Type)
get "defaultXSymbolTable"(): $UnicodeSet$XSymbolTable
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $UnicodeSet$Type = ($UnicodeSet);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $UnicodeSet_ = $UnicodeSet$Type;
}}
declare module "packages/com/ibm/icu/text/$DisplayContext" {
import {$DisplayContext$Type, $DisplayContext$Type$Type} from "packages/com/ibm/icu/text/$DisplayContext$Type"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $DisplayContext extends $Enum<($DisplayContext)> {
static readonly "STANDARD_NAMES": $DisplayContext
static readonly "DIALECT_NAMES": $DisplayContext
static readonly "CAPITALIZATION_NONE": $DisplayContext
static readonly "CAPITALIZATION_FOR_MIDDLE_OF_SENTENCE": $DisplayContext
static readonly "CAPITALIZATION_FOR_BEGINNING_OF_SENTENCE": $DisplayContext
static readonly "CAPITALIZATION_FOR_UI_LIST_OR_MENU": $DisplayContext
static readonly "CAPITALIZATION_FOR_STANDALONE": $DisplayContext
static readonly "LENGTH_FULL": $DisplayContext
static readonly "LENGTH_SHORT": $DisplayContext
static readonly "SUBSTITUTE": $DisplayContext
static readonly "NO_SUBSTITUTE": $DisplayContext


public "type"(): $DisplayContext$Type
public "value"(): integer
public static "values"(): ($DisplayContext)[]
public static "valueOf"(arg0: string): $DisplayContext
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DisplayContext$Type = (("dialect_names") | ("capitalization_for_standalone") | ("capitalization_for_beginning_of_sentence") | ("length_full") | ("capitalization_none") | ("standard_names") | ("length_short") | ("capitalization_for_ui_list_or_menu") | ("substitute") | ("no_substitute") | ("capitalization_for_middle_of_sentence")) | ($DisplayContext);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DisplayContext_ = $DisplayContext$Type;
}}
declare module "packages/com/ibm/icu/util/$Currency$CurrencyStringInfo" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
/**
 * 
 * @deprecated
 */
export class $Currency$CurrencyStringInfo {

/**
 * 
 * @deprecated
 */
constructor(arg0: string, arg1: string)

/**
 * 
 * @deprecated
 */
public "getISOCode"(): string
/**
 * 
 * @deprecated
 */
public "getCurrencyString"(): string
get "iSOCode"(): string
get "currencyString"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Currency$CurrencyStringInfo$Type = ($Currency$CurrencyStringInfo);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Currency$CurrencyStringInfo_ = $Currency$CurrencyStringInfo$Type;
}}
declare module "packages/com/ibm/icu/text/$UnicodeSet$SpanCondition" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $UnicodeSet$SpanCondition extends $Enum<($UnicodeSet$SpanCondition)> {
static readonly "NOT_CONTAINED": $UnicodeSet$SpanCondition
static readonly "CONTAINED": $UnicodeSet$SpanCondition
static readonly "SIMPLE": $UnicodeSet$SpanCondition
static readonly "CONDITION_COUNT": $UnicodeSet$SpanCondition


public static "values"(): ($UnicodeSet$SpanCondition)[]
public static "valueOf"(arg0: string): $UnicodeSet$SpanCondition
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $UnicodeSet$SpanCondition$Type = (("not_contained") | ("contained") | ("condition_count") | ("simple")) | ($UnicodeSet$SpanCondition);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $UnicodeSet$SpanCondition_ = $UnicodeSet$SpanCondition$Type;
}}
declare module "packages/com/ibm/icu/util/$TimeZone" {
import {$Serializable, $Serializable$Type} from "packages/java/io/$Serializable"
import {$Date, $Date$Type} from "packages/java/util/$Date"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$Freezable, $Freezable$Type} from "packages/com/ibm/icu/util/$Freezable"
import {$Cloneable, $Cloneable$Type} from "packages/java/lang/$Cloneable"
import {$TimeZone$SystemTimeZoneType, $TimeZone$SystemTimeZoneType$Type} from "packages/com/ibm/icu/util/$TimeZone$SystemTimeZoneType"
import {$Locale, $Locale$Type} from "packages/java/util/$Locale"
import {$ULocale, $ULocale$Type} from "packages/com/ibm/icu/util/$ULocale"

export class $TimeZone implements $Serializable, $Cloneable, $Freezable<($TimeZone)> {
static readonly "TIMEZONE_ICU": integer
static readonly "TIMEZONE_JDK": integer
static readonly "SHORT": integer
static readonly "LONG": integer
static readonly "SHORT_GENERIC": integer
static readonly "LONG_GENERIC": integer
static readonly "SHORT_GMT": integer
static readonly "LONG_GMT": integer
static readonly "SHORT_COMMONLY_USED": integer
static readonly "GENERIC_LOCATION": integer
static readonly "UNKNOWN_ZONE_ID": string
static readonly "UNKNOWN_ZONE": $TimeZone
static readonly "GMT_ZONE": $TimeZone

constructor()

public "equals"(arg0: any): boolean
public "hashCode"(): integer
public "clone"(): any
public static "getDefault"(): $TimeZone
public "isFrozen"(): boolean
public "freeze"(): $TimeZone
public "getOffset"(arg0: long): integer
public "getOffset"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer): integer
public "getOffset"(arg0: long, arg1: boolean, arg2: (integer)[]): void
public "getDisplayName"(arg0: $Locale$Type): string
public "getDisplayName"(): string
public "getDisplayName"(arg0: boolean, arg1: integer): string
public "getDisplayName"(arg0: boolean, arg1: integer, arg2: $Locale$Type): string
public "getDisplayName"(arg0: $ULocale$Type): string
public "getDisplayName"(arg0: boolean, arg1: integer, arg2: $ULocale$Type): string
public static "getTimeZone"(arg0: string, arg1: integer): $TimeZone
public static "getTimeZone"(arg0: string): $TimeZone
public "getID"(): string
public "getDSTSavings"(): integer
public "hasSameRules"(arg0: $TimeZone$Type): boolean
public "getRawOffset"(): integer
public static "setDefault"(arg0: $TimeZone$Type): void
public static "getRegion"(arg0: string): string
public "inDaylightTime"(arg0: $Date$Type): boolean
public "useDaylightTime"(): boolean
public static "getAvailableIDs"(arg0: $TimeZone$SystemTimeZoneType$Type, arg1: string, arg2: integer): $Set<(string)>
public static "getAvailableIDs"(): (string)[]
public static "getAvailableIDs"(arg0: integer): (string)[]
public static "getAvailableIDs"(arg0: string): (string)[]
public "setID"(arg0: string): void
public "setRawOffset"(arg0: integer): void
public "observesDaylightTime"(): boolean
/**
 * 
 * @deprecated
 */
public static "forULocaleOrDefault"(arg0: $ULocale$Type): $TimeZone
public static "setDefaultTimeZoneType"(arg0: integer): void
public static "getDefaultTimeZoneType"(): integer
public "cloneAsThawed"(): $TimeZone
public static "getTZDataVersion"(): string
/**
 * 
 * @deprecated
 */
public static "forLocaleOrDefault"(arg0: $Locale$Type): $TimeZone
public static "countEquivalentIDs"(arg0: string): integer
public static "getEquivalentID"(arg0: string, arg1: integer): string
public static "getFrozenTimeZone"(arg0: string): $TimeZone
public static "getWindowsID"(arg0: string): string
public static "getCanonicalID"(arg0: string, arg1: (boolean)[]): string
public static "getCanonicalID"(arg0: string): string
/**
 * 
 * @deprecated
 */
public static "setICUDefault"(arg0: $TimeZone$Type): void
public static "getIDForWindowsID"(arg0: string, arg1: string): string
get "default"(): $TimeZone
get "frozen"(): boolean
get "displayName"(): string
get "iD"(): string
get "dSTSavings"(): integer
get "rawOffset"(): integer
set "default"(value: $TimeZone$Type)
get "availableIDs"(): (string)[]
set "iD"(value: string)
set "rawOffset"(value: integer)
set "defaultTimeZoneType"(value: integer)
get "defaultTimeZoneType"(): integer
get "tZDataVersion"(): string
set "iCUDefault"(value: $TimeZone$Type)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TimeZone$Type = ($TimeZone);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TimeZone_ = $TimeZone$Type;
}}
declare module "packages/com/ibm/icu/util/$ULocale$Minimize" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

/**
 * 
 * @deprecated
 */
export class $ULocale$Minimize extends $Enum<($ULocale$Minimize)> {
/**
 * 
 * @deprecated
 */
static readonly "FAVOR_SCRIPT": $ULocale$Minimize
/**
 * 
 * @deprecated
 */
static readonly "FAVOR_REGION": $ULocale$Minimize


public static "values"(): ($ULocale$Minimize)[]
public static "valueOf"(arg0: string): $ULocale$Minimize
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ULocale$Minimize$Type = (("favor_region") | ("favor_script")) | ($ULocale$Minimize);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ULocale$Minimize_ = $ULocale$Minimize$Type;
}}
declare module "packages/com/ibm/icu/util/$CurrencyAmount" {
import {$Currency, $Currency$Type} from "packages/com/ibm/icu/util/$Currency"
import {$Measure, $Measure$Type} from "packages/com/ibm/icu/util/$Measure"
import {$Currency as $Currency$0, $Currency$Type as $Currency$0$Type} from "packages/java/util/$Currency"

export class $CurrencyAmount extends $Measure {

constructor(arg0: double, arg1: $Currency$0$Type)
constructor(arg0: number, arg1: $Currency$0$Type)
constructor(arg0: double, arg1: $Currency$Type)
constructor(arg0: number, arg1: $Currency$Type)

public "getCurrency"(): $Currency
get "currency"(): $Currency
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CurrencyAmount$Type = ($CurrencyAmount);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CurrencyAmount_ = $CurrencyAmount$Type;
}}
declare module "packages/com/ibm/icu/impl/units/$MeasureUnitImpl" {
import {$MeasureUnit$Complexity, $MeasureUnit$Complexity$Type} from "packages/com/ibm/icu/util/$MeasureUnit$Complexity"
import {$MeasureUnit, $MeasureUnit$Type} from "packages/com/ibm/icu/util/$MeasureUnit"
import {$MeasureUnitImpl$MeasureUnitImplWithIndex, $MeasureUnitImpl$MeasureUnitImplWithIndex$Type} from "packages/com/ibm/icu/impl/units/$MeasureUnitImpl$MeasureUnitImplWithIndex"
import {$SingleUnitImpl, $SingleUnitImpl$Type} from "packages/com/ibm/icu/impl/units/$SingleUnitImpl"
import {$ArrayList, $ArrayList$Type} from "packages/java/util/$ArrayList"

export class $MeasureUnitImpl {

constructor()
constructor(arg0: $SingleUnitImpl$Type)

public "getComplexity"(): $MeasureUnit$Complexity
public static "forIdentifier"(arg0: string): $MeasureUnitImpl
public "getSingleUnitImpl"(): $SingleUnitImpl
public "getSingleUnits"(): $ArrayList<($SingleUnitImpl)>
public "appendSingleUnit"(arg0: $SingleUnitImpl$Type): boolean
public "takeReciprocal"(): void
public "toString"(): string
public "copy"(): $MeasureUnitImpl
public "build"(): $MeasureUnit
public "getIdentifier"(): string
public "serialize"(): void
public "setComplexity"(arg0: $MeasureUnit$Complexity$Type): void
public "copyAndSimplify"(): $MeasureUnitImpl
public static "forCurrencyCode"(arg0: string): $MeasureUnitImpl
public "applyDimensionality"(arg0: integer): void
public "extractIndividualUnitsWithIndices"(): $ArrayList<($MeasureUnitImpl$MeasureUnitImplWithIndex)>
get "complexity"(): $MeasureUnit$Complexity
get "singleUnitImpl"(): $SingleUnitImpl
get "singleUnits"(): $ArrayList<($SingleUnitImpl)>
get "identifier"(): string
set "complexity"(value: $MeasureUnit$Complexity$Type)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MeasureUnitImpl$Type = ($MeasureUnitImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MeasureUnitImpl_ = $MeasureUnitImpl$Type;
}}
declare module "packages/com/ibm/icu/util/$ULocale$AvailableType" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $ULocale$AvailableType extends $Enum<($ULocale$AvailableType)> {
static readonly "DEFAULT": $ULocale$AvailableType
static readonly "ONLY_LEGACY_ALIASES": $ULocale$AvailableType
static readonly "WITH_LEGACY_ALIASES": $ULocale$AvailableType


public static "values"(): ($ULocale$AvailableType)[]
public static "valueOf"(arg0: string): $ULocale$AvailableType
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ULocale$AvailableType$Type = (("with_legacy_aliases") | ("default") | ("only_legacy_aliases")) | ($ULocale$AvailableType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ULocale$AvailableType_ = $ULocale$AvailableType$Type;
}}
declare module "packages/com/ibm/icu/impl/$TextTrieMap$Output" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $TextTrieMap$Output {
 "matchLength": integer
 "partialMatch": boolean

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TextTrieMap$Output$Type = ($TextTrieMap$Output);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TextTrieMap$Output_ = $TextTrieMap$Output$Type;
}}
declare module "packages/com/ibm/icu/util/$TimeZone$SystemTimeZoneType" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $TimeZone$SystemTimeZoneType extends $Enum<($TimeZone$SystemTimeZoneType)> {
static readonly "ANY": $TimeZone$SystemTimeZoneType
static readonly "CANONICAL": $TimeZone$SystemTimeZoneType
static readonly "CANONICAL_LOCATION": $TimeZone$SystemTimeZoneType


public static "values"(): ($TimeZone$SystemTimeZoneType)[]
public static "valueOf"(arg0: string): $TimeZone$SystemTimeZoneType
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TimeZone$SystemTimeZoneType$Type = (("canonical") | ("any") | ("canonical_location")) | ($TimeZone$SystemTimeZoneType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TimeZone$SystemTimeZoneType_ = $TimeZone$SystemTimeZoneType$Type;
}}
declare module "packages/com/ibm/icu/util/$MeasureUnit" {
import {$Serializable, $Serializable$Type} from "packages/java/io/$Serializable"
import {$MeasureUnit$MeasurePrefix, $MeasureUnit$MeasurePrefix$Type} from "packages/com/ibm/icu/util/$MeasureUnit$MeasurePrefix"
import {$MeasureUnit$Complexity, $MeasureUnit$Complexity$Type} from "packages/com/ibm/icu/util/$MeasureUnit$Complexity"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$List, $List$Type} from "packages/java/util/$List"
import {$TimeUnit, $TimeUnit$Type} from "packages/com/ibm/icu/util/$TimeUnit"
import {$MeasureUnitImpl, $MeasureUnitImpl$Type} from "packages/com/ibm/icu/impl/units/$MeasureUnitImpl"

export class $MeasureUnit implements $Serializable {
static readonly "G_FORCE": $MeasureUnit
static readonly "METER_PER_SECOND_SQUARED": $MeasureUnit
static readonly "ARC_MINUTE": $MeasureUnit
static readonly "ARC_SECOND": $MeasureUnit
static readonly "DEGREE": $MeasureUnit
static readonly "RADIAN": $MeasureUnit
static readonly "REVOLUTION_ANGLE": $MeasureUnit
static readonly "ACRE": $MeasureUnit
static readonly "DUNAM": $MeasureUnit
static readonly "HECTARE": $MeasureUnit
static readonly "SQUARE_CENTIMETER": $MeasureUnit
static readonly "SQUARE_FOOT": $MeasureUnit
static readonly "SQUARE_INCH": $MeasureUnit
static readonly "SQUARE_KILOMETER": $MeasureUnit
static readonly "SQUARE_METER": $MeasureUnit
static readonly "SQUARE_MILE": $MeasureUnit
static readonly "SQUARE_YARD": $MeasureUnit
static readonly "ITEM": $MeasureUnit
static readonly "KARAT": $MeasureUnit
static readonly "MILLIGRAM_OFGLUCOSE_PER_DECILITER": $MeasureUnit
static readonly "MILLIGRAM_PER_DECILITER": $MeasureUnit
static readonly "MILLIMOLE_PER_LITER": $MeasureUnit
static readonly "MOLE": $MeasureUnit
static readonly "PERCENT": $MeasureUnit
static readonly "PERMILLE": $MeasureUnit
static readonly "PART_PER_MILLION": $MeasureUnit
static readonly "PERMYRIAD": $MeasureUnit
static readonly "LITER_PER_100KILOMETERS": $MeasureUnit
static readonly "LITER_PER_KILOMETER": $MeasureUnit
static readonly "MILE_PER_GALLON": $MeasureUnit
static readonly "MILE_PER_GALLON_IMPERIAL": $MeasureUnit
static readonly "BIT": $MeasureUnit
static readonly "BYTE": $MeasureUnit
static readonly "GIGABIT": $MeasureUnit
static readonly "GIGABYTE": $MeasureUnit
static readonly "KILOBIT": $MeasureUnit
static readonly "KILOBYTE": $MeasureUnit
static readonly "MEGABIT": $MeasureUnit
static readonly "MEGABYTE": $MeasureUnit
static readonly "PETABYTE": $MeasureUnit
static readonly "TERABIT": $MeasureUnit
static readonly "TERABYTE": $MeasureUnit
static readonly "CENTURY": $MeasureUnit
static readonly "DAY": $TimeUnit
static readonly "DAY_PERSON": $MeasureUnit
static readonly "DECADE": $MeasureUnit
static readonly "HOUR": $TimeUnit
static readonly "MICROSECOND": $MeasureUnit
static readonly "MILLISECOND": $MeasureUnit
static readonly "MINUTE": $TimeUnit
static readonly "MONTH": $TimeUnit
static readonly "MONTH_PERSON": $MeasureUnit
static readonly "NANOSECOND": $MeasureUnit
static readonly "SECOND": $TimeUnit
static readonly "WEEK": $TimeUnit
static readonly "WEEK_PERSON": $MeasureUnit
static readonly "YEAR": $TimeUnit
static readonly "YEAR_PERSON": $MeasureUnit
static readonly "AMPERE": $MeasureUnit
static readonly "MILLIAMPERE": $MeasureUnit
static readonly "OHM": $MeasureUnit
static readonly "VOLT": $MeasureUnit
static readonly "BRITISH_THERMAL_UNIT": $MeasureUnit
static readonly "CALORIE": $MeasureUnit
static readonly "ELECTRONVOLT": $MeasureUnit
static readonly "FOODCALORIE": $MeasureUnit
static readonly "JOULE": $MeasureUnit
static readonly "KILOCALORIE": $MeasureUnit
static readonly "KILOJOULE": $MeasureUnit
static readonly "KILOWATT_HOUR": $MeasureUnit
static readonly "THERM_US": $MeasureUnit
static readonly "KILOWATT_HOUR_PER_100_KILOMETER": $MeasureUnit
static readonly "NEWTON": $MeasureUnit
static readonly "POUND_FORCE": $MeasureUnit
static readonly "GIGAHERTZ": $MeasureUnit
static readonly "HERTZ": $MeasureUnit
static readonly "KILOHERTZ": $MeasureUnit
static readonly "MEGAHERTZ": $MeasureUnit
static readonly "DOT": $MeasureUnit
static readonly "DOT_PER_CENTIMETER": $MeasureUnit
static readonly "DOT_PER_INCH": $MeasureUnit
static readonly "EM": $MeasureUnit
static readonly "MEGAPIXEL": $MeasureUnit
static readonly "PIXEL": $MeasureUnit
static readonly "PIXEL_PER_CENTIMETER": $MeasureUnit
static readonly "PIXEL_PER_INCH": $MeasureUnit
static readonly "ASTRONOMICAL_UNIT": $MeasureUnit
static readonly "CENTIMETER": $MeasureUnit
static readonly "DECIMETER": $MeasureUnit
static readonly "EARTH_RADIUS": $MeasureUnit
static readonly "FATHOM": $MeasureUnit
static readonly "FOOT": $MeasureUnit
static readonly "FURLONG": $MeasureUnit
static readonly "INCH": $MeasureUnit
static readonly "KILOMETER": $MeasureUnit
static readonly "LIGHT_YEAR": $MeasureUnit
static readonly "METER": $MeasureUnit
static readonly "MICROMETER": $MeasureUnit
static readonly "MILE": $MeasureUnit
static readonly "MILE_SCANDINAVIAN": $MeasureUnit
static readonly "MILLIMETER": $MeasureUnit
static readonly "NANOMETER": $MeasureUnit
static readonly "NAUTICAL_MILE": $MeasureUnit
static readonly "PARSEC": $MeasureUnit
static readonly "PICOMETER": $MeasureUnit
static readonly "POINT": $MeasureUnit
static readonly "SOLAR_RADIUS": $MeasureUnit
static readonly "YARD": $MeasureUnit
static readonly "CANDELA": $MeasureUnit
static readonly "LUMEN": $MeasureUnit
static readonly "LUX": $MeasureUnit
static readonly "SOLAR_LUMINOSITY": $MeasureUnit
static readonly "CARAT": $MeasureUnit
static readonly "DALTON": $MeasureUnit
static readonly "EARTH_MASS": $MeasureUnit
static readonly "GRAIN": $MeasureUnit
static readonly "GRAM": $MeasureUnit
static readonly "KILOGRAM": $MeasureUnit
static readonly "METRIC_TON": $MeasureUnit
static readonly "MICROGRAM": $MeasureUnit
static readonly "MILLIGRAM": $MeasureUnit
static readonly "OUNCE": $MeasureUnit
static readonly "OUNCE_TROY": $MeasureUnit
static readonly "POUND": $MeasureUnit
static readonly "SOLAR_MASS": $MeasureUnit
static readonly "STONE": $MeasureUnit
static readonly "TON": $MeasureUnit
static readonly "GIGAWATT": $MeasureUnit
static readonly "HORSEPOWER": $MeasureUnit
static readonly "KILOWATT": $MeasureUnit
static readonly "MEGAWATT": $MeasureUnit
static readonly "MILLIWATT": $MeasureUnit
static readonly "WATT": $MeasureUnit
static readonly "ATMOSPHERE": $MeasureUnit
static readonly "BAR": $MeasureUnit
static readonly "HECTOPASCAL": $MeasureUnit
static readonly "INCH_HG": $MeasureUnit
static readonly "KILOPASCAL": $MeasureUnit
static readonly "MEGAPASCAL": $MeasureUnit
static readonly "MILLIBAR": $MeasureUnit
static readonly "MILLIMETER_OF_MERCURY": $MeasureUnit
static readonly "PASCAL": $MeasureUnit
static readonly "POUND_PER_SQUARE_INCH": $MeasureUnit
static readonly "KILOMETER_PER_HOUR": $MeasureUnit
static readonly "KNOT": $MeasureUnit
static readonly "METER_PER_SECOND": $MeasureUnit
static readonly "MILE_PER_HOUR": $MeasureUnit
static readonly "CELSIUS": $MeasureUnit
static readonly "FAHRENHEIT": $MeasureUnit
static readonly "GENERIC_TEMPERATURE": $MeasureUnit
static readonly "KELVIN": $MeasureUnit
static readonly "NEWTON_METER": $MeasureUnit
static readonly "POUND_FOOT": $MeasureUnit
static readonly "ACRE_FOOT": $MeasureUnit
static readonly "BARREL": $MeasureUnit
static readonly "BUSHEL": $MeasureUnit
static readonly "CENTILITER": $MeasureUnit
static readonly "CUBIC_CENTIMETER": $MeasureUnit
static readonly "CUBIC_FOOT": $MeasureUnit
static readonly "CUBIC_INCH": $MeasureUnit
static readonly "CUBIC_KILOMETER": $MeasureUnit
static readonly "CUBIC_METER": $MeasureUnit
static readonly "CUBIC_MILE": $MeasureUnit
static readonly "CUBIC_YARD": $MeasureUnit
static readonly "CUP": $MeasureUnit
static readonly "CUP_METRIC": $MeasureUnit
static readonly "DECILITER": $MeasureUnit
static readonly "DESSERT_SPOON": $MeasureUnit
static readonly "DESSERT_SPOON_IMPERIAL": $MeasureUnit
static readonly "DRAM": $MeasureUnit
static readonly "DROP": $MeasureUnit
static readonly "FLUID_OUNCE": $MeasureUnit
static readonly "FLUID_OUNCE_IMPERIAL": $MeasureUnit
static readonly "GALLON": $MeasureUnit
static readonly "GALLON_IMPERIAL": $MeasureUnit
static readonly "HECTOLITER": $MeasureUnit
static readonly "JIGGER": $MeasureUnit
static readonly "LITER": $MeasureUnit
static readonly "MEGALITER": $MeasureUnit
static readonly "MILLILITER": $MeasureUnit
static readonly "PINCH": $MeasureUnit
static readonly "PINT": $MeasureUnit
static readonly "PINT_METRIC": $MeasureUnit
static readonly "QUART": $MeasureUnit
static readonly "QUART_IMPERIAL": $MeasureUnit
static readonly "TABLESPOON": $MeasureUnit
static readonly "TEASPOON": $MeasureUnit


public "getDimensionality"(): integer
public "withDimensionality"(arg0: integer): $MeasureUnit
public "getComplexity"(): $MeasureUnit$Complexity
public static "forIdentifier"(arg0: string): $MeasureUnit
public "reciprocal"(): $MeasureUnit
public "splitToSingleUnits"(): $List<($MeasureUnit)>
/**
 * 
 * @deprecated
 */
public static "findBySubType"(arg0: string): $MeasureUnit
public static "getAvailableTypes"(): $Set<(string)>
public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "getType"(): string
public "product"(arg0: $MeasureUnit$Type): $MeasureUnit
public "withPrefix"(arg0: $MeasureUnit$MeasurePrefix$Type): $MeasureUnit
public "getIdentifier"(): string
public "getPrefix"(): $MeasureUnit$MeasurePrefix
public "getSubtype"(): string
public static "getAvailable"(): $Set<($MeasureUnit)>
public static "getAvailable"(arg0: string): $Set<($MeasureUnit)>
/**
 * 
 * @deprecated
 */
public static "fromMeasureUnitImpl"(arg0: $MeasureUnitImpl$Type): $MeasureUnit
/**
 * 
 * @deprecated
 */
public static "internalGetInstance"(arg0: string, arg1: string): $MeasureUnit
/**
 * 
 * @deprecated
 */
public "getCopyOfMeasureUnitImpl"(): $MeasureUnitImpl
get "dimensionality"(): integer
get "complexity"(): $MeasureUnit$Complexity
get "availableTypes"(): $Set<(string)>
get "type"(): string
get "identifier"(): string
get "prefix"(): $MeasureUnit$MeasurePrefix
get "subtype"(): string
get "available"(): $Set<($MeasureUnit)>
get "copyOfMeasureUnitImpl"(): $MeasureUnitImpl
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MeasureUnit$Type = ($MeasureUnit);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MeasureUnit_ = $MeasureUnit$Type;
}}
declare module "packages/com/ibm/icu/util/$Currency" {
import {$Currency$CurrencyUsage, $Currency$CurrencyUsage$Type} from "packages/com/ibm/icu/util/$Currency$CurrencyUsage"
import {$Date, $Date$Type} from "packages/java/util/$Date"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$MeasureUnit, $MeasureUnit$Type} from "packages/com/ibm/icu/util/$MeasureUnit"
import {$Currency as $Currency$0, $Currency$Type as $Currency$0$Type} from "packages/java/util/$Currency"
import {$TimeUnit, $TimeUnit$Type} from "packages/com/ibm/icu/util/$TimeUnit"
import {$ParsePosition, $ParsePosition$Type} from "packages/java/text/$ParsePosition"
import {$TextTrieMap, $TextTrieMap$Type} from "packages/com/ibm/icu/impl/$TextTrieMap"
import {$Locale, $Locale$Type} from "packages/java/util/$Locale"
import {$ULocale, $ULocale$Type} from "packages/com/ibm/icu/util/$ULocale"
import {$Currency$CurrencyStringInfo, $Currency$CurrencyStringInfo$Type} from "packages/com/ibm/icu/util/$Currency$CurrencyStringInfo"

export class $Currency extends $MeasureUnit {
static readonly "SYMBOL_NAME": integer
static readonly "LONG_NAME": integer
static readonly "PLURAL_LONG_NAME": integer
static readonly "NARROW_SYMBOL_NAME": integer
static readonly "FORMAL_SYMBOL_NAME": integer
static readonly "VARIANT_SYMBOL_NAME": integer
static readonly "G_FORCE": $MeasureUnit
static readonly "METER_PER_SECOND_SQUARED": $MeasureUnit
static readonly "ARC_MINUTE": $MeasureUnit
static readonly "ARC_SECOND": $MeasureUnit
static readonly "DEGREE": $MeasureUnit
static readonly "RADIAN": $MeasureUnit
static readonly "REVOLUTION_ANGLE": $MeasureUnit
static readonly "ACRE": $MeasureUnit
static readonly "DUNAM": $MeasureUnit
static readonly "HECTARE": $MeasureUnit
static readonly "SQUARE_CENTIMETER": $MeasureUnit
static readonly "SQUARE_FOOT": $MeasureUnit
static readonly "SQUARE_INCH": $MeasureUnit
static readonly "SQUARE_KILOMETER": $MeasureUnit
static readonly "SQUARE_METER": $MeasureUnit
static readonly "SQUARE_MILE": $MeasureUnit
static readonly "SQUARE_YARD": $MeasureUnit
static readonly "ITEM": $MeasureUnit
static readonly "KARAT": $MeasureUnit
static readonly "MILLIGRAM_OFGLUCOSE_PER_DECILITER": $MeasureUnit
static readonly "MILLIGRAM_PER_DECILITER": $MeasureUnit
static readonly "MILLIMOLE_PER_LITER": $MeasureUnit
static readonly "MOLE": $MeasureUnit
static readonly "PERCENT": $MeasureUnit
static readonly "PERMILLE": $MeasureUnit
static readonly "PART_PER_MILLION": $MeasureUnit
static readonly "PERMYRIAD": $MeasureUnit
static readonly "LITER_PER_100KILOMETERS": $MeasureUnit
static readonly "LITER_PER_KILOMETER": $MeasureUnit
static readonly "MILE_PER_GALLON": $MeasureUnit
static readonly "MILE_PER_GALLON_IMPERIAL": $MeasureUnit
static readonly "BIT": $MeasureUnit
static readonly "BYTE": $MeasureUnit
static readonly "GIGABIT": $MeasureUnit
static readonly "GIGABYTE": $MeasureUnit
static readonly "KILOBIT": $MeasureUnit
static readonly "KILOBYTE": $MeasureUnit
static readonly "MEGABIT": $MeasureUnit
static readonly "MEGABYTE": $MeasureUnit
static readonly "PETABYTE": $MeasureUnit
static readonly "TERABIT": $MeasureUnit
static readonly "TERABYTE": $MeasureUnit
static readonly "CENTURY": $MeasureUnit
static readonly "DAY": $TimeUnit
static readonly "DAY_PERSON": $MeasureUnit
static readonly "DECADE": $MeasureUnit
static readonly "HOUR": $TimeUnit
static readonly "MICROSECOND": $MeasureUnit
static readonly "MILLISECOND": $MeasureUnit
static readonly "MINUTE": $TimeUnit
static readonly "MONTH": $TimeUnit
static readonly "MONTH_PERSON": $MeasureUnit
static readonly "NANOSECOND": $MeasureUnit
static readonly "SECOND": $TimeUnit
static readonly "WEEK": $TimeUnit
static readonly "WEEK_PERSON": $MeasureUnit
static readonly "YEAR": $TimeUnit
static readonly "YEAR_PERSON": $MeasureUnit
static readonly "AMPERE": $MeasureUnit
static readonly "MILLIAMPERE": $MeasureUnit
static readonly "OHM": $MeasureUnit
static readonly "VOLT": $MeasureUnit
static readonly "BRITISH_THERMAL_UNIT": $MeasureUnit
static readonly "CALORIE": $MeasureUnit
static readonly "ELECTRONVOLT": $MeasureUnit
static readonly "FOODCALORIE": $MeasureUnit
static readonly "JOULE": $MeasureUnit
static readonly "KILOCALORIE": $MeasureUnit
static readonly "KILOJOULE": $MeasureUnit
static readonly "KILOWATT_HOUR": $MeasureUnit
static readonly "THERM_US": $MeasureUnit
static readonly "KILOWATT_HOUR_PER_100_KILOMETER": $MeasureUnit
static readonly "NEWTON": $MeasureUnit
static readonly "POUND_FORCE": $MeasureUnit
static readonly "GIGAHERTZ": $MeasureUnit
static readonly "HERTZ": $MeasureUnit
static readonly "KILOHERTZ": $MeasureUnit
static readonly "MEGAHERTZ": $MeasureUnit
static readonly "DOT": $MeasureUnit
static readonly "DOT_PER_CENTIMETER": $MeasureUnit
static readonly "DOT_PER_INCH": $MeasureUnit
static readonly "EM": $MeasureUnit
static readonly "MEGAPIXEL": $MeasureUnit
static readonly "PIXEL": $MeasureUnit
static readonly "PIXEL_PER_CENTIMETER": $MeasureUnit
static readonly "PIXEL_PER_INCH": $MeasureUnit
static readonly "ASTRONOMICAL_UNIT": $MeasureUnit
static readonly "CENTIMETER": $MeasureUnit
static readonly "DECIMETER": $MeasureUnit
static readonly "EARTH_RADIUS": $MeasureUnit
static readonly "FATHOM": $MeasureUnit
static readonly "FOOT": $MeasureUnit
static readonly "FURLONG": $MeasureUnit
static readonly "INCH": $MeasureUnit
static readonly "KILOMETER": $MeasureUnit
static readonly "LIGHT_YEAR": $MeasureUnit
static readonly "METER": $MeasureUnit
static readonly "MICROMETER": $MeasureUnit
static readonly "MILE": $MeasureUnit
static readonly "MILE_SCANDINAVIAN": $MeasureUnit
static readonly "MILLIMETER": $MeasureUnit
static readonly "NANOMETER": $MeasureUnit
static readonly "NAUTICAL_MILE": $MeasureUnit
static readonly "PARSEC": $MeasureUnit
static readonly "PICOMETER": $MeasureUnit
static readonly "POINT": $MeasureUnit
static readonly "SOLAR_RADIUS": $MeasureUnit
static readonly "YARD": $MeasureUnit
static readonly "CANDELA": $MeasureUnit
static readonly "LUMEN": $MeasureUnit
static readonly "LUX": $MeasureUnit
static readonly "SOLAR_LUMINOSITY": $MeasureUnit
static readonly "CARAT": $MeasureUnit
static readonly "DALTON": $MeasureUnit
static readonly "EARTH_MASS": $MeasureUnit
static readonly "GRAIN": $MeasureUnit
static readonly "GRAM": $MeasureUnit
static readonly "KILOGRAM": $MeasureUnit
static readonly "METRIC_TON": $MeasureUnit
static readonly "MICROGRAM": $MeasureUnit
static readonly "MILLIGRAM": $MeasureUnit
static readonly "OUNCE": $MeasureUnit
static readonly "OUNCE_TROY": $MeasureUnit
static readonly "POUND": $MeasureUnit
static readonly "SOLAR_MASS": $MeasureUnit
static readonly "STONE": $MeasureUnit
static readonly "TON": $MeasureUnit
static readonly "GIGAWATT": $MeasureUnit
static readonly "HORSEPOWER": $MeasureUnit
static readonly "KILOWATT": $MeasureUnit
static readonly "MEGAWATT": $MeasureUnit
static readonly "MILLIWATT": $MeasureUnit
static readonly "WATT": $MeasureUnit
static readonly "ATMOSPHERE": $MeasureUnit
static readonly "BAR": $MeasureUnit
static readonly "HECTOPASCAL": $MeasureUnit
static readonly "INCH_HG": $MeasureUnit
static readonly "KILOPASCAL": $MeasureUnit
static readonly "MEGAPASCAL": $MeasureUnit
static readonly "MILLIBAR": $MeasureUnit
static readonly "MILLIMETER_OF_MERCURY": $MeasureUnit
static readonly "PASCAL": $MeasureUnit
static readonly "POUND_PER_SQUARE_INCH": $MeasureUnit
static readonly "KILOMETER_PER_HOUR": $MeasureUnit
static readonly "KNOT": $MeasureUnit
static readonly "METER_PER_SECOND": $MeasureUnit
static readonly "MILE_PER_HOUR": $MeasureUnit
static readonly "CELSIUS": $MeasureUnit
static readonly "FAHRENHEIT": $MeasureUnit
static readonly "GENERIC_TEMPERATURE": $MeasureUnit
static readonly "KELVIN": $MeasureUnit
static readonly "NEWTON_METER": $MeasureUnit
static readonly "POUND_FOOT": $MeasureUnit
static readonly "ACRE_FOOT": $MeasureUnit
static readonly "BARREL": $MeasureUnit
static readonly "BUSHEL": $MeasureUnit
static readonly "CENTILITER": $MeasureUnit
static readonly "CUBIC_CENTIMETER": $MeasureUnit
static readonly "CUBIC_FOOT": $MeasureUnit
static readonly "CUBIC_INCH": $MeasureUnit
static readonly "CUBIC_KILOMETER": $MeasureUnit
static readonly "CUBIC_METER": $MeasureUnit
static readonly "CUBIC_MILE": $MeasureUnit
static readonly "CUBIC_YARD": $MeasureUnit
static readonly "CUP": $MeasureUnit
static readonly "CUP_METRIC": $MeasureUnit
static readonly "DECILITER": $MeasureUnit
static readonly "DESSERT_SPOON": $MeasureUnit
static readonly "DESSERT_SPOON_IMPERIAL": $MeasureUnit
static readonly "DRAM": $MeasureUnit
static readonly "DROP": $MeasureUnit
static readonly "FLUID_OUNCE": $MeasureUnit
static readonly "FLUID_OUNCE_IMPERIAL": $MeasureUnit
static readonly "GALLON": $MeasureUnit
static readonly "GALLON_IMPERIAL": $MeasureUnit
static readonly "HECTOLITER": $MeasureUnit
static readonly "JIGGER": $MeasureUnit
static readonly "LITER": $MeasureUnit
static readonly "MEGALITER": $MeasureUnit
static readonly "MILLILITER": $MeasureUnit
static readonly "PINCH": $MeasureUnit
static readonly "PINT": $MeasureUnit
static readonly "PINT_METRIC": $MeasureUnit
static readonly "QUART": $MeasureUnit
static readonly "QUART_IMPERIAL": $MeasureUnit
static readonly "TABLESPOON": $MeasureUnit
static readonly "TEASPOON": $MeasureUnit


public "getName"(arg0: $Locale$Type, arg1: integer, arg2: (boolean)[]): string
public "getName"(arg0: $Locale$Type, arg1: integer, arg2: string, arg3: (boolean)[]): string
public "getName"(arg0: $ULocale$Type, arg1: integer, arg2: string, arg3: (boolean)[]): string
public "getName"(arg0: $ULocale$Type, arg1: integer, arg2: (boolean)[]): string
public "toString"(): string
public static "getInstance"(arg0: $Locale$Type): $Currency
public static "getInstance"(arg0: string): $Currency
public static "getInstance"(arg0: $ULocale$Type): $Currency
/**
 * 
 * @deprecated
 */
public static "parse"(arg0: $ULocale$Type, arg1: string, arg2: integer, arg3: $ParsePosition$Type): string
public static "unregister"(arg0: any): boolean
public "getDisplayName"(arg0: $Locale$Type): string
public "getDisplayName"(): string
public static "getAvailableLocales"(): ($Locale)[]
public "getSymbol"(arg0: $Locale$Type): string
public "getSymbol"(): string
public "getSymbol"(arg0: $ULocale$Type): string
public "getCurrencyCode"(): string
public "getDefaultFractionDigits"(): integer
public "getDefaultFractionDigits"(arg0: $Currency$CurrencyUsage$Type): integer
public static "getAvailableCurrencies"(): $Set<($Currency)>
public "getNumericCode"(): integer
public static "isAvailable"(arg0: string, arg1: $Date$Type, arg2: $Date$Type): boolean
public static "getKeywordValuesForLocale"(arg0: string, arg1: $ULocale$Type, arg2: boolean): (string)[]
public static "registerInstance"(arg0: $Currency$Type, arg1: $ULocale$Type): any
public static "fromJavaCurrency"(arg0: $Currency$0$Type): $Currency
public "toJavaCurrency"(): $Currency$0
/**
 * 
 * @deprecated
 */
public static "getParsingTrie"(arg0: $ULocale$Type, arg1: integer): $TextTrieMap<($Currency$CurrencyStringInfo)>
public static "getAvailableCurrencyCodes"(arg0: $ULocale$Type, arg1: $Date$Type): (string)[]
public static "getAvailableCurrencyCodes"(arg0: $Locale$Type, arg1: $Date$Type): (string)[]
public static "getAvailableULocales"(): ($ULocale)[]
public "getRoundingIncrement"(): double
public "getRoundingIncrement"(arg0: $Currency$CurrencyUsage$Type): double
get "displayName"(): string
get "availableLocales"(): ($Locale)[]
get "symbol"(): string
get "currencyCode"(): string
get "defaultFractionDigits"(): integer
get "availableCurrencies"(): $Set<($Currency)>
get "numericCode"(): integer
get "availableULocales"(): ($ULocale)[]
get "roundingIncrement"(): double
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Currency$Type = ($Currency);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Currency_ = $Currency$Type;
}}
declare module "packages/com/ibm/icu/text/$UnicodeSet$ComparisonStyle" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $UnicodeSet$ComparisonStyle extends $Enum<($UnicodeSet$ComparisonStyle)> {
static readonly "SHORTER_FIRST": $UnicodeSet$ComparisonStyle
static readonly "LEXICOGRAPHIC": $UnicodeSet$ComparisonStyle
static readonly "LONGER_FIRST": $UnicodeSet$ComparisonStyle


public static "values"(): ($UnicodeSet$ComparisonStyle)[]
public static "valueOf"(arg0: string): $UnicodeSet$ComparisonStyle
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $UnicodeSet$ComparisonStyle$Type = (("longer_first") | ("shorter_first") | ("lexicographic")) | ($UnicodeSet$ComparisonStyle);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $UnicodeSet$ComparisonStyle_ = $UnicodeSet$ComparisonStyle$Type;
}}
declare module "packages/com/ibm/icu/impl/$TextTrieMap$ResultHandler" {
import {$Iterator, $Iterator$Type} from "packages/java/util/$Iterator"

export interface $TextTrieMap$ResultHandler<V> {

 "handlePrefixMatch"(arg0: integer, arg1: $Iterator$Type<(V)>): boolean

(arg0: integer, arg1: $Iterator$Type<(V)>): boolean
}

export namespace $TextTrieMap$ResultHandler {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TextTrieMap$ResultHandler$Type<V> = ($TextTrieMap$ResultHandler<(V)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TextTrieMap$ResultHandler_<V> = $TextTrieMap$ResultHandler$Type<(V)>;
}}
declare module "packages/com/ibm/icu/util/$TimeUnit" {
import {$MeasureUnit, $MeasureUnit$Type} from "packages/com/ibm/icu/util/$MeasureUnit"

export class $TimeUnit extends $MeasureUnit {
static readonly "G_FORCE": $MeasureUnit
static readonly "METER_PER_SECOND_SQUARED": $MeasureUnit
static readonly "ARC_MINUTE": $MeasureUnit
static readonly "ARC_SECOND": $MeasureUnit
static readonly "DEGREE": $MeasureUnit
static readonly "RADIAN": $MeasureUnit
static readonly "REVOLUTION_ANGLE": $MeasureUnit
static readonly "ACRE": $MeasureUnit
static readonly "DUNAM": $MeasureUnit
static readonly "HECTARE": $MeasureUnit
static readonly "SQUARE_CENTIMETER": $MeasureUnit
static readonly "SQUARE_FOOT": $MeasureUnit
static readonly "SQUARE_INCH": $MeasureUnit
static readonly "SQUARE_KILOMETER": $MeasureUnit
static readonly "SQUARE_METER": $MeasureUnit
static readonly "SQUARE_MILE": $MeasureUnit
static readonly "SQUARE_YARD": $MeasureUnit
static readonly "ITEM": $MeasureUnit
static readonly "KARAT": $MeasureUnit
static readonly "MILLIGRAM_OFGLUCOSE_PER_DECILITER": $MeasureUnit
static readonly "MILLIGRAM_PER_DECILITER": $MeasureUnit
static readonly "MILLIMOLE_PER_LITER": $MeasureUnit
static readonly "MOLE": $MeasureUnit
static readonly "PERCENT": $MeasureUnit
static readonly "PERMILLE": $MeasureUnit
static readonly "PART_PER_MILLION": $MeasureUnit
static readonly "PERMYRIAD": $MeasureUnit
static readonly "LITER_PER_100KILOMETERS": $MeasureUnit
static readonly "LITER_PER_KILOMETER": $MeasureUnit
static readonly "MILE_PER_GALLON": $MeasureUnit
static readonly "MILE_PER_GALLON_IMPERIAL": $MeasureUnit
static readonly "BIT": $MeasureUnit
static readonly "BYTE": $MeasureUnit
static readonly "GIGABIT": $MeasureUnit
static readonly "GIGABYTE": $MeasureUnit
static readonly "KILOBIT": $MeasureUnit
static readonly "KILOBYTE": $MeasureUnit
static readonly "MEGABIT": $MeasureUnit
static readonly "MEGABYTE": $MeasureUnit
static readonly "PETABYTE": $MeasureUnit
static readonly "TERABIT": $MeasureUnit
static readonly "TERABYTE": $MeasureUnit
static readonly "CENTURY": $MeasureUnit
static readonly "DAY": $TimeUnit
static readonly "DAY_PERSON": $MeasureUnit
static readonly "DECADE": $MeasureUnit
static readonly "HOUR": $TimeUnit
static readonly "MICROSECOND": $MeasureUnit
static readonly "MILLISECOND": $MeasureUnit
static readonly "MINUTE": $TimeUnit
static readonly "MONTH": $TimeUnit
static readonly "MONTH_PERSON": $MeasureUnit
static readonly "NANOSECOND": $MeasureUnit
static readonly "SECOND": $TimeUnit
static readonly "WEEK": $TimeUnit
static readonly "WEEK_PERSON": $MeasureUnit
static readonly "YEAR": $TimeUnit
static readonly "YEAR_PERSON": $MeasureUnit
static readonly "AMPERE": $MeasureUnit
static readonly "MILLIAMPERE": $MeasureUnit
static readonly "OHM": $MeasureUnit
static readonly "VOLT": $MeasureUnit
static readonly "BRITISH_THERMAL_UNIT": $MeasureUnit
static readonly "CALORIE": $MeasureUnit
static readonly "ELECTRONVOLT": $MeasureUnit
static readonly "FOODCALORIE": $MeasureUnit
static readonly "JOULE": $MeasureUnit
static readonly "KILOCALORIE": $MeasureUnit
static readonly "KILOJOULE": $MeasureUnit
static readonly "KILOWATT_HOUR": $MeasureUnit
static readonly "THERM_US": $MeasureUnit
static readonly "KILOWATT_HOUR_PER_100_KILOMETER": $MeasureUnit
static readonly "NEWTON": $MeasureUnit
static readonly "POUND_FORCE": $MeasureUnit
static readonly "GIGAHERTZ": $MeasureUnit
static readonly "HERTZ": $MeasureUnit
static readonly "KILOHERTZ": $MeasureUnit
static readonly "MEGAHERTZ": $MeasureUnit
static readonly "DOT": $MeasureUnit
static readonly "DOT_PER_CENTIMETER": $MeasureUnit
static readonly "DOT_PER_INCH": $MeasureUnit
static readonly "EM": $MeasureUnit
static readonly "MEGAPIXEL": $MeasureUnit
static readonly "PIXEL": $MeasureUnit
static readonly "PIXEL_PER_CENTIMETER": $MeasureUnit
static readonly "PIXEL_PER_INCH": $MeasureUnit
static readonly "ASTRONOMICAL_UNIT": $MeasureUnit
static readonly "CENTIMETER": $MeasureUnit
static readonly "DECIMETER": $MeasureUnit
static readonly "EARTH_RADIUS": $MeasureUnit
static readonly "FATHOM": $MeasureUnit
static readonly "FOOT": $MeasureUnit
static readonly "FURLONG": $MeasureUnit
static readonly "INCH": $MeasureUnit
static readonly "KILOMETER": $MeasureUnit
static readonly "LIGHT_YEAR": $MeasureUnit
static readonly "METER": $MeasureUnit
static readonly "MICROMETER": $MeasureUnit
static readonly "MILE": $MeasureUnit
static readonly "MILE_SCANDINAVIAN": $MeasureUnit
static readonly "MILLIMETER": $MeasureUnit
static readonly "NANOMETER": $MeasureUnit
static readonly "NAUTICAL_MILE": $MeasureUnit
static readonly "PARSEC": $MeasureUnit
static readonly "PICOMETER": $MeasureUnit
static readonly "POINT": $MeasureUnit
static readonly "SOLAR_RADIUS": $MeasureUnit
static readonly "YARD": $MeasureUnit
static readonly "CANDELA": $MeasureUnit
static readonly "LUMEN": $MeasureUnit
static readonly "LUX": $MeasureUnit
static readonly "SOLAR_LUMINOSITY": $MeasureUnit
static readonly "CARAT": $MeasureUnit
static readonly "DALTON": $MeasureUnit
static readonly "EARTH_MASS": $MeasureUnit
static readonly "GRAIN": $MeasureUnit
static readonly "GRAM": $MeasureUnit
static readonly "KILOGRAM": $MeasureUnit
static readonly "METRIC_TON": $MeasureUnit
static readonly "MICROGRAM": $MeasureUnit
static readonly "MILLIGRAM": $MeasureUnit
static readonly "OUNCE": $MeasureUnit
static readonly "OUNCE_TROY": $MeasureUnit
static readonly "POUND": $MeasureUnit
static readonly "SOLAR_MASS": $MeasureUnit
static readonly "STONE": $MeasureUnit
static readonly "TON": $MeasureUnit
static readonly "GIGAWATT": $MeasureUnit
static readonly "HORSEPOWER": $MeasureUnit
static readonly "KILOWATT": $MeasureUnit
static readonly "MEGAWATT": $MeasureUnit
static readonly "MILLIWATT": $MeasureUnit
static readonly "WATT": $MeasureUnit
static readonly "ATMOSPHERE": $MeasureUnit
static readonly "BAR": $MeasureUnit
static readonly "HECTOPASCAL": $MeasureUnit
static readonly "INCH_HG": $MeasureUnit
static readonly "KILOPASCAL": $MeasureUnit
static readonly "MEGAPASCAL": $MeasureUnit
static readonly "MILLIBAR": $MeasureUnit
static readonly "MILLIMETER_OF_MERCURY": $MeasureUnit
static readonly "PASCAL": $MeasureUnit
static readonly "POUND_PER_SQUARE_INCH": $MeasureUnit
static readonly "KILOMETER_PER_HOUR": $MeasureUnit
static readonly "KNOT": $MeasureUnit
static readonly "METER_PER_SECOND": $MeasureUnit
static readonly "MILE_PER_HOUR": $MeasureUnit
static readonly "CELSIUS": $MeasureUnit
static readonly "FAHRENHEIT": $MeasureUnit
static readonly "GENERIC_TEMPERATURE": $MeasureUnit
static readonly "KELVIN": $MeasureUnit
static readonly "NEWTON_METER": $MeasureUnit
static readonly "POUND_FOOT": $MeasureUnit
static readonly "ACRE_FOOT": $MeasureUnit
static readonly "BARREL": $MeasureUnit
static readonly "BUSHEL": $MeasureUnit
static readonly "CENTILITER": $MeasureUnit
static readonly "CUBIC_CENTIMETER": $MeasureUnit
static readonly "CUBIC_FOOT": $MeasureUnit
static readonly "CUBIC_INCH": $MeasureUnit
static readonly "CUBIC_KILOMETER": $MeasureUnit
static readonly "CUBIC_METER": $MeasureUnit
static readonly "CUBIC_MILE": $MeasureUnit
static readonly "CUBIC_YARD": $MeasureUnit
static readonly "CUP": $MeasureUnit
static readonly "CUP_METRIC": $MeasureUnit
static readonly "DECILITER": $MeasureUnit
static readonly "DESSERT_SPOON": $MeasureUnit
static readonly "DESSERT_SPOON_IMPERIAL": $MeasureUnit
static readonly "DRAM": $MeasureUnit
static readonly "DROP": $MeasureUnit
static readonly "FLUID_OUNCE": $MeasureUnit
static readonly "FLUID_OUNCE_IMPERIAL": $MeasureUnit
static readonly "GALLON": $MeasureUnit
static readonly "GALLON_IMPERIAL": $MeasureUnit
static readonly "HECTOLITER": $MeasureUnit
static readonly "JIGGER": $MeasureUnit
static readonly "LITER": $MeasureUnit
static readonly "MEGALITER": $MeasureUnit
static readonly "MILLILITER": $MeasureUnit
static readonly "PINCH": $MeasureUnit
static readonly "PINT": $MeasureUnit
static readonly "PINT_METRIC": $MeasureUnit
static readonly "QUART": $MeasureUnit
static readonly "QUART_IMPERIAL": $MeasureUnit
static readonly "TABLESPOON": $MeasureUnit
static readonly "TEASPOON": $MeasureUnit


public static "values"(): ($TimeUnit)[]
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TimeUnit$Type = ($TimeUnit);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TimeUnit_ = $TimeUnit$Type;
}}
declare module "packages/com/ibm/icu/math/$BigDecimal" {
import {$Comparable, $Comparable$Type} from "packages/java/lang/$Comparable"
import {$Serializable, $Serializable$Type} from "packages/java/io/$Serializable"
import {$BigInteger, $BigInteger$Type} from "packages/java/math/$BigInteger"
import {$MathContext, $MathContext$Type} from "packages/com/ibm/icu/math/$MathContext"
import {$BigDecimal as $BigDecimal$0, $BigDecimal$Type as $BigDecimal$0$Type} from "packages/java/math/$BigDecimal"

export class $BigDecimal extends number implements $Serializable, $Comparable<($BigDecimal)> {
static readonly "ZERO": $BigDecimal
static readonly "ONE": $BigDecimal
static readonly "TEN": $BigDecimal
static readonly "ROUND_CEILING": integer
static readonly "ROUND_DOWN": integer
static readonly "ROUND_FLOOR": integer
static readonly "ROUND_HALF_DOWN": integer
static readonly "ROUND_HALF_EVEN": integer
static readonly "ROUND_HALF_UP": integer
static readonly "ROUND_UNNECESSARY": integer
static readonly "ROUND_UP": integer

constructor(arg0: $BigDecimal$0$Type)
constructor(arg0: (character)[], arg1: integer, arg2: integer)
constructor(arg0: (character)[])
constructor(arg0: $BigInteger$Type, arg1: integer)
constructor(arg0: $BigInteger$Type)
constructor(arg0: long)
constructor(arg0: string)
constructor(arg0: integer)
constructor(arg0: double)

public "divideInteger"(arg0: $BigDecimal$Type, arg1: $MathContext$Type): $BigDecimal
public "divideInteger"(arg0: $BigDecimal$Type): $BigDecimal
public "add"(arg0: $BigDecimal$Type): $BigDecimal
public "add"(arg0: $BigDecimal$Type, arg1: $MathContext$Type): $BigDecimal
public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "abs"(arg0: $MathContext$Type): $BigDecimal
public "abs"(): $BigDecimal
public "pow"(arg0: $BigDecimal$Type): $BigDecimal
public "pow"(arg0: $BigDecimal$Type, arg1: $MathContext$Type): $BigDecimal
public "min"(arg0: $BigDecimal$Type): $BigDecimal
public "min"(arg0: $BigDecimal$Type, arg1: $MathContext$Type): $BigDecimal
public "max"(arg0: $BigDecimal$Type): $BigDecimal
public "max"(arg0: $BigDecimal$Type, arg1: $MathContext$Type): $BigDecimal
public "signum"(): integer
public "compareTo"(arg0: $BigDecimal$Type, arg1: $MathContext$Type): integer
public "compareTo"(arg0: $BigDecimal$Type): integer
public "intValue"(): integer
public "longValue"(): long
public "floatValue"(): float
public "doubleValue"(): double
public static "valueOf"(arg0: long, arg1: integer): $BigDecimal
public static "valueOf"(arg0: long): $BigDecimal
public static "valueOf"(arg0: double): $BigDecimal
public "scale"(): integer
public "toCharArray"(): (character)[]
public "format"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer): string
public "format"(arg0: integer, arg1: integer): string
public "multiply"(arg0: $BigDecimal$Type, arg1: $MathContext$Type): $BigDecimal
public "multiply"(arg0: $BigDecimal$Type): $BigDecimal
public "setScale"(arg0: integer, arg1: integer): $BigDecimal
public "setScale"(arg0: integer): $BigDecimal
public "negate"(arg0: $MathContext$Type): $BigDecimal
public "negate"(): $BigDecimal
public "subtract"(arg0: $BigDecimal$Type, arg1: $MathContext$Type): $BigDecimal
public "subtract"(arg0: $BigDecimal$Type): $BigDecimal
public "divide"(arg0: $BigDecimal$Type): $BigDecimal
public "divide"(arg0: $BigDecimal$Type, arg1: $MathContext$Type): $BigDecimal
public "divide"(arg0: $BigDecimal$Type, arg1: integer, arg2: integer): $BigDecimal
public "divide"(arg0: $BigDecimal$Type, arg1: integer): $BigDecimal
public "unscaledValue"(): $BigInteger
public "plus"(arg0: $MathContext$Type): $BigDecimal
public "plus"(): $BigDecimal
public "toBigInteger"(): $BigInteger
public "longValueExact"(): long
public "toBigDecimal"(): $BigDecimal$0
public "remainder"(arg0: $BigDecimal$Type): $BigDecimal
public "remainder"(arg0: $BigDecimal$Type, arg1: $MathContext$Type): $BigDecimal
public "movePointLeft"(arg0: integer): $BigDecimal
public "movePointRight"(arg0: integer): $BigDecimal
public "toBigIntegerExact"(): $BigInteger
public "intValueExact"(): integer
public "shortValueExact"(): short
public "byteValueExact"(): byte
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BigDecimal$Type = ($BigDecimal);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BigDecimal_ = $BigDecimal$Type;
}}
declare module "packages/com/ibm/icu/util/$Calendar" {
import {$Comparable, $Comparable$Type} from "packages/java/lang/$Comparable"
import {$TimeZone, $TimeZone$Type} from "packages/com/ibm/icu/util/$TimeZone"
import {$Serializable, $Serializable$Type} from "packages/java/io/$Serializable"
import {$Date, $Date$Type} from "packages/java/util/$Date"
import {$Cloneable, $Cloneable$Type} from "packages/java/lang/$Cloneable"
import {$Calendar$WeekData, $Calendar$WeekData$Type} from "packages/com/ibm/icu/util/$Calendar$WeekData"
import {$DateFormat, $DateFormat$Type} from "packages/com/ibm/icu/text/$DateFormat"
import {$ULocale$Type, $ULocale$Type$Type} from "packages/com/ibm/icu/util/$ULocale$Type"
import {$Locale, $Locale$Type} from "packages/java/util/$Locale"
import {$ULocale, $ULocale$Type} from "packages/com/ibm/icu/util/$ULocale"

export class $Calendar implements $Serializable, $Cloneable, $Comparable<($Calendar)> {
static readonly "ERA": integer
static readonly "YEAR": integer
static readonly "MONTH": integer
static readonly "WEEK_OF_YEAR": integer
static readonly "WEEK_OF_MONTH": integer
static readonly "DATE": integer
static readonly "DAY_OF_MONTH": integer
static readonly "DAY_OF_YEAR": integer
static readonly "DAY_OF_WEEK": integer
static readonly "DAY_OF_WEEK_IN_MONTH": integer
static readonly "AM_PM": integer
static readonly "HOUR": integer
static readonly "HOUR_OF_DAY": integer
static readonly "MINUTE": integer
static readonly "SECOND": integer
static readonly "MILLISECOND": integer
static readonly "ZONE_OFFSET": integer
static readonly "DST_OFFSET": integer
static readonly "YEAR_WOY": integer
static readonly "DOW_LOCAL": integer
static readonly "EXTENDED_YEAR": integer
static readonly "JULIAN_DAY": integer
static readonly "MILLISECONDS_IN_DAY": integer
static readonly "IS_LEAP_MONTH": integer
static readonly "SUNDAY": integer
static readonly "MONDAY": integer
static readonly "TUESDAY": integer
static readonly "WEDNESDAY": integer
static readonly "THURSDAY": integer
static readonly "FRIDAY": integer
static readonly "SATURDAY": integer
static readonly "JANUARY": integer
static readonly "FEBRUARY": integer
static readonly "MARCH": integer
static readonly "APRIL": integer
static readonly "MAY": integer
static readonly "JUNE": integer
static readonly "JULY": integer
static readonly "AUGUST": integer
static readonly "SEPTEMBER": integer
static readonly "OCTOBER": integer
static readonly "NOVEMBER": integer
static readonly "DECEMBER": integer
static readonly "UNDECIMBER": integer
static readonly "AM": integer
static readonly "PM": integer
/**
 * 
 * @deprecated
 */
static readonly "WEEKDAY": integer
/**
 * 
 * @deprecated
 */
static readonly "WEEKEND": integer
/**
 * 
 * @deprecated
 */
static readonly "WEEKEND_ONSET": integer
/**
 * 
 * @deprecated
 */
static readonly "WEEKEND_CEASE": integer
static readonly "WALLTIME_LAST": integer
static readonly "WALLTIME_FIRST": integer
static readonly "WALLTIME_NEXT_VALID": integer


public "getDateTimeFormat"(arg0: integer, arg1: integer, arg2: $Locale$Type): $DateFormat
public "getDateTimeFormat"(arg0: integer, arg1: integer, arg2: $ULocale$Type): $DateFormat
public "add"(arg0: integer, arg1: integer): void
public "get"(arg0: integer): integer
public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "clone"(): any
public "compareTo"(arg0: $Calendar$Type): integer
public "clear"(): void
public "clear"(arg0: integer): void
public static "getInstance"(arg0: $ULocale$Type): $Calendar
public static "getInstance"(arg0: $Locale$Type): $Calendar
public static "getInstance"(arg0: $TimeZone$Type): $Calendar
public static "getInstance"(): $Calendar
public static "getInstance"(arg0: $TimeZone$Type, arg1: $ULocale$Type): $Calendar
public static "getInstance"(arg0: $TimeZone$Type, arg1: $Locale$Type): $Calendar
public "set"(arg0: integer, arg1: integer): void
public "set"(arg0: integer, arg1: integer, arg2: integer): void
public "set"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer): void
public "set"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer): void
public "isSet"(arg0: integer): boolean
public "getType"(): string
public "before"(arg0: any): boolean
public "after"(arg0: any): boolean
public "getDisplayName"(arg0: $Locale$Type): string
public "getDisplayName"(arg0: $ULocale$Type): string
public static "getAvailableLocales"(): ($Locale)[]
public "setTimeZone"(arg0: $TimeZone$Type): void
public "getTimeZone"(): $TimeZone
public "setLenient"(arg0: boolean): void
public "isLenient"(): boolean
public "getFirstDayOfWeek"(): integer
public "getMinimalDaysInFirstWeek"(): integer
/**
 * 
 * @deprecated
 */
public static "getDateTimePattern"(arg0: $Calendar$Type, arg1: $ULocale$Type, arg2: integer): string
public "setTimeInMillis"(arg0: long): void
public "getTime"(): $Date
public "setTime"(arg0: $Date$Type): void
public "getMaximum"(arg0: integer): integer
public "getLeastMaximum"(arg0: integer): integer
public "getLocale"(arg0: $ULocale$Type$Type): $ULocale
public "getMinimum"(arg0: integer): integer
public "getTimeInMillis"(): long
public "roll"(arg0: integer, arg1: integer): void
public "roll"(arg0: integer, arg1: boolean): void
public "getGreatestMinimum"(arg0: integer): integer
public "setFirstDayOfWeek"(arg0: integer): void
public "setMinimalDaysInFirstWeek"(arg0: integer): void
public "getActualMinimum"(arg0: integer): integer
public "getActualMaximum"(arg0: integer): integer
public "setRepeatedWallTimeOption"(arg0: integer): void
public "setSkippedWallTimeOption"(arg0: integer): void
public static "getKeywordValuesForLocale"(arg0: string, arg1: $ULocale$Type, arg2: boolean): (string)[]
public "getRepeatedWallTimeOption"(): integer
/**
 * 
 * @deprecated
 */
public "getWeekendTransition"(arg0: integer): integer
public static "getWeekDataForRegion"(arg0: string): $Calendar$WeekData
public "getSkippedWallTimeOption"(): integer
public "isEquivalentTo"(arg0: $Calendar$Type): boolean
public static "getAvailableULocales"(): ($ULocale)[]
public "getFieldCount"(): integer
/**
 * 
 * @deprecated
 */
public "getRelatedYear"(): integer
/**
 * 
 * @deprecated
 */
public "setRelatedYear"(arg0: integer): void
/**
 * 
 * @deprecated
 */
public "haveDefaultCentury"(): boolean
public "fieldDifference"(arg0: $Date$Type, arg1: integer): integer
/**
 * 
 * @deprecated
 */
public "getDayOfWeekType"(arg0: integer): integer
public "isWeekend"(arg0: $Date$Type): boolean
public "isWeekend"(): boolean
public "getWeekData"(): $Calendar$WeekData
public "setWeekData"(arg0: $Calendar$WeekData$Type): $Calendar
get "instance"(): $Calendar
get "type"(): string
get "availableLocales"(): ($Locale)[]
set "timeZone"(value: $TimeZone$Type)
get "timeZone"(): $TimeZone
set "lenient"(value: boolean)
get "lenient"(): boolean
get "firstDayOfWeek"(): integer
get "minimalDaysInFirstWeek"(): integer
set "timeInMillis"(value: long)
get "time"(): $Date
set "time"(value: $Date$Type)
get "timeInMillis"(): long
set "firstDayOfWeek"(value: integer)
set "minimalDaysInFirstWeek"(value: integer)
set "repeatedWallTimeOption"(value: integer)
set "skippedWallTimeOption"(value: integer)
get "repeatedWallTimeOption"(): integer
get "skippedWallTimeOption"(): integer
get "availableULocales"(): ($ULocale)[]
get "fieldCount"(): integer
get "relatedYear"(): integer
set "relatedYear"(value: integer)
get "weekend"(): boolean
get "weekData"(): $Calendar$WeekData
set "weekData"(value: $Calendar$WeekData$Type)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Calendar$Type = ($Calendar);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Calendar_ = $Calendar$Type;
}}
declare module "packages/com/ibm/icu/util/$ULocale$Type" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $ULocale$Type {


}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ULocale$Type$Type = ($ULocale$Type);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ULocale$Type_ = $ULocale$Type$Type;
}}
declare module "packages/com/ibm/icu/text/$DateFormat" {
import {$DisplayContext$Type, $DisplayContext$Type$Type} from "packages/com/ibm/icu/text/$DisplayContext$Type"
import {$Date, $Date$Type} from "packages/java/util/$Date"
import {$Calendar, $Calendar$Type} from "packages/com/ibm/icu/util/$Calendar"
import {$DateFormat$BooleanAttribute, $DateFormat$BooleanAttribute$Type} from "packages/com/ibm/icu/text/$DateFormat$BooleanAttribute"
import {$Locale, $Locale$Type} from "packages/java/util/$Locale"
import {$ULocale, $ULocale$Type} from "packages/com/ibm/icu/util/$ULocale"
import {$FieldPosition, $FieldPosition$Type} from "packages/java/text/$FieldPosition"
import {$TimeZone, $TimeZone$Type} from "packages/com/ibm/icu/util/$TimeZone"
import {$DisplayContext, $DisplayContext$Type} from "packages/com/ibm/icu/text/$DisplayContext"
import {$UFormat, $UFormat$Type} from "packages/com/ibm/icu/text/$UFormat"
import {$List, $List$Type} from "packages/java/util/$List"
import {$StringBuffer, $StringBuffer$Type} from "packages/java/lang/$StringBuffer"
import {$ParsePosition, $ParsePosition$Type} from "packages/java/text/$ParsePosition"
import {$NumberFormat, $NumberFormat$Type} from "packages/com/ibm/icu/text/$NumberFormat"

export class $DateFormat extends $UFormat {
static readonly "ERA_FIELD": integer
static readonly "YEAR_FIELD": integer
static readonly "MONTH_FIELD": integer
static readonly "DATE_FIELD": integer
static readonly "HOUR_OF_DAY1_FIELD": integer
static readonly "HOUR_OF_DAY0_FIELD": integer
static readonly "MINUTE_FIELD": integer
static readonly "SECOND_FIELD": integer
static readonly "FRACTIONAL_SECOND_FIELD": integer
static readonly "MILLISECOND_FIELD": integer
static readonly "DAY_OF_WEEK_FIELD": integer
static readonly "DAY_OF_YEAR_FIELD": integer
static readonly "DAY_OF_WEEK_IN_MONTH_FIELD": integer
static readonly "WEEK_OF_YEAR_FIELD": integer
static readonly "WEEK_OF_MONTH_FIELD": integer
static readonly "AM_PM_FIELD": integer
static readonly "HOUR1_FIELD": integer
static readonly "HOUR0_FIELD": integer
static readonly "TIMEZONE_FIELD": integer
static readonly "YEAR_WOY_FIELD": integer
static readonly "DOW_LOCAL_FIELD": integer
static readonly "EXTENDED_YEAR_FIELD": integer
static readonly "JULIAN_DAY_FIELD": integer
static readonly "MILLISECONDS_IN_DAY_FIELD": integer
static readonly "TIMEZONE_RFC_FIELD": integer
static readonly "TIMEZONE_GENERIC_FIELD": integer
static readonly "STANDALONE_DAY_FIELD": integer
static readonly "STANDALONE_MONTH_FIELD": integer
static readonly "QUARTER_FIELD": integer
static readonly "STANDALONE_QUARTER_FIELD": integer
static readonly "TIMEZONE_SPECIAL_FIELD": integer
static readonly "YEAR_NAME_FIELD": integer
static readonly "TIMEZONE_LOCALIZED_GMT_OFFSET_FIELD": integer
static readonly "TIMEZONE_ISO_FIELD": integer
static readonly "TIMEZONE_ISO_LOCAL_FIELD": integer
static readonly "AM_PM_MIDNIGHT_NOON_FIELD": integer
static readonly "FLEXIBLE_DAY_PERIOD_FIELD": integer
/**
 * 
 * @deprecated
 */
static readonly "TIME_SEPARATOR": integer
/**
 * 
 * @deprecated
 */
static readonly "FIELD_COUNT": integer
static readonly "NONE": integer
static readonly "FULL": integer
static readonly "LONG": integer
static readonly "MEDIUM": integer
static readonly "SHORT": integer
static readonly "DEFAULT": integer
static readonly "RELATIVE": integer
static readonly "RELATIVE_FULL": integer
static readonly "RELATIVE_LONG": integer
static readonly "RELATIVE_MEDIUM": integer
static readonly "RELATIVE_SHORT": integer
static readonly "RELATIVE_DEFAULT": integer
static readonly "YEAR": string
static readonly "QUARTER": string
static readonly "ABBR_QUARTER": string
static readonly "YEAR_QUARTER": string
static readonly "YEAR_ABBR_QUARTER": string
static readonly "MONTH": string
static readonly "ABBR_MONTH": string
static readonly "NUM_MONTH": string
static readonly "YEAR_MONTH": string
static readonly "YEAR_ABBR_MONTH": string
static readonly "YEAR_NUM_MONTH": string
static readonly "DAY": string
static readonly "YEAR_MONTH_DAY": string
static readonly "YEAR_ABBR_MONTH_DAY": string
static readonly "YEAR_NUM_MONTH_DAY": string
static readonly "WEEKDAY": string
static readonly "ABBR_WEEKDAY": string
static readonly "YEAR_MONTH_WEEKDAY_DAY": string
static readonly "YEAR_ABBR_MONTH_WEEKDAY_DAY": string
static readonly "YEAR_NUM_MONTH_WEEKDAY_DAY": string
static readonly "MONTH_DAY": string
static readonly "ABBR_MONTH_DAY": string
static readonly "NUM_MONTH_DAY": string
static readonly "MONTH_WEEKDAY_DAY": string
static readonly "ABBR_MONTH_WEEKDAY_DAY": string
static readonly "NUM_MONTH_WEEKDAY_DAY": string
/**
 * 
 * @deprecated
 */
static readonly "DATE_SKELETONS": $List<(string)>
static readonly "HOUR": string
static readonly "HOUR24": string
static readonly "MINUTE": string
static readonly "HOUR_MINUTE": string
static readonly "HOUR24_MINUTE": string
static readonly "SECOND": string
static readonly "HOUR_MINUTE_SECOND": string
static readonly "HOUR24_MINUTE_SECOND": string
static readonly "MINUTE_SECOND": string
/**
 * 
 * @deprecated
 */
static readonly "TIME_SKELETONS": $List<(string)>
static readonly "LOCATION_TZ": string
static readonly "GENERIC_TZ": string
static readonly "ABBR_GENERIC_TZ": string
static readonly "SPECIFIC_TZ": string
static readonly "ABBR_SPECIFIC_TZ": string
static readonly "ABBR_UTC_TZ": string
/**
 * 
 * @deprecated
 */
static readonly "ZONE_SKELETONS": $List<(string)>
/**
 * 
 * @deprecated
 */
static readonly "STANDALONE_MONTH": string
/**
 * 
 * @deprecated
 */
static readonly "ABBR_STANDALONE_MONTH": string
/**
 * 
 * @deprecated
 */
static readonly "HOUR_MINUTE_GENERIC_TZ": string
/**
 * 
 * @deprecated
 */
static readonly "HOUR_MINUTE_TZ": string
/**
 * 
 * @deprecated
 */
static readonly "HOUR_GENERIC_TZ": string
/**
 * 
 * @deprecated
 */
static readonly "HOUR_TZ": string
/**
 * 
 * @deprecated
 */
static readonly "JP_ERA_2019_ROOT": string
/**
 * 
 * @deprecated
 */
static readonly "JP_ERA_2019_JA": string
/**
 * 
 * @deprecated
 */
static readonly "JP_ERA_2019_NARROW": string


public "setCalendarLenient"(arg0: boolean): void
public "isCalendarLenient"(): boolean
public static "getPatternInstance"(arg0: string, arg1: $Locale$Type): $DateFormat
public static "getPatternInstance"(arg0: string): $DateFormat
public static "getPatternInstance"(arg0: $Calendar$Type, arg1: string, arg2: $ULocale$Type): $DateFormat
public static "getPatternInstance"(arg0: $Calendar$Type, arg1: string, arg2: $Locale$Type): $DateFormat
public static "getPatternInstance"(arg0: string, arg1: $ULocale$Type): $DateFormat
public "equals"(arg0: any): boolean
public "hashCode"(): integer
public "clone"(): any
public "format"(arg0: any, arg1: $StringBuffer$Type, arg2: $FieldPosition$Type): $StringBuffer
public "format"(arg0: $Calendar$Type, arg1: $StringBuffer$Type, arg2: $FieldPosition$Type): $StringBuffer
public "format"(arg0: $Date$Type): string
public "format"(arg0: $Date$Type, arg1: $StringBuffer$Type, arg2: $FieldPosition$Type): $StringBuffer
public static "getInstance"(arg0: $Calendar$Type): $DateFormat
public static "getInstance"(): $DateFormat
public static "getInstance"(arg0: $Calendar$Type, arg1: $ULocale$Type): $DateFormat
public static "getInstance"(arg0: $Calendar$Type, arg1: $Locale$Type): $DateFormat
public "getContext"(arg0: $DisplayContext$Type$Type): $DisplayContext
public "parse"(arg0: string, arg1: $ParsePosition$Type): $Date
public "parse"(arg0: string): $Date
public "parse"(arg0: string, arg1: $Calendar$Type, arg2: $ParsePosition$Type): void
public static "getDateTimeInstance"(arg0: integer, arg1: integer): $DateFormat
public static "getDateTimeInstance"(): $DateFormat
public static "getDateTimeInstance"(arg0: $Calendar$Type, arg1: integer, arg2: integer): $DateFormat
public static "getDateTimeInstance"(arg0: $Calendar$Type, arg1: integer, arg2: integer, arg3: $Locale$Type): $DateFormat
public static "getDateTimeInstance"(arg0: $Calendar$Type, arg1: integer, arg2: integer, arg3: $ULocale$Type): $DateFormat
public static "getDateTimeInstance"(arg0: integer, arg1: integer, arg2: $Locale$Type): $DateFormat
public static "getDateTimeInstance"(arg0: integer, arg1: integer, arg2: $ULocale$Type): $DateFormat
public static "getAvailableLocales"(): ($Locale)[]
public "setTimeZone"(arg0: $TimeZone$Type): void
public "getTimeZone"(): $TimeZone
public "setLenient"(arg0: boolean): void
public "isLenient"(): boolean
public static "getDateInstance"(arg0: integer): $DateFormat
public static "getDateInstance"(arg0: $Calendar$Type, arg1: integer): $DateFormat
public static "getDateInstance"(arg0: $Calendar$Type, arg1: integer, arg2: $ULocale$Type): $DateFormat
public static "getDateInstance"(arg0: $Calendar$Type, arg1: integer, arg2: $Locale$Type): $DateFormat
public static "getDateInstance"(): $DateFormat
public static "getDateInstance"(arg0: integer, arg1: $ULocale$Type): $DateFormat
public static "getDateInstance"(arg0: integer, arg1: $Locale$Type): $DateFormat
public static "getTimeInstance"(arg0: integer): $DateFormat
public static "getTimeInstance"(arg0: integer, arg1: $Locale$Type): $DateFormat
public static "getTimeInstance"(arg0: $Calendar$Type, arg1: integer): $DateFormat
public static "getTimeInstance"(arg0: integer, arg1: $ULocale$Type): $DateFormat
public static "getTimeInstance"(arg0: $Calendar$Type, arg1: integer, arg2: $Locale$Type): $DateFormat
public static "getTimeInstance"(): $DateFormat
public static "getTimeInstance"(arg0: $Calendar$Type, arg1: integer, arg2: $ULocale$Type): $DateFormat
public "parseObject"(arg0: string, arg1: $ParsePosition$Type): any
public "setCalendar"(arg0: $Calendar$Type): void
public "getCalendar"(): $Calendar
public "setNumberFormat"(arg0: $NumberFormat$Type): void
public "getNumberFormat"(): $NumberFormat
public "setBooleanAttribute"(arg0: $DateFormat$BooleanAttribute$Type, arg1: boolean): $DateFormat
public static "getInstanceForSkeleton"(arg0: string): $DateFormat
public static "getInstanceForSkeleton"(arg0: string, arg1: $ULocale$Type): $DateFormat
public static "getInstanceForSkeleton"(arg0: $Calendar$Type, arg1: string, arg2: $Locale$Type): $DateFormat
public static "getInstanceForSkeleton"(arg0: $Calendar$Type, arg1: string, arg2: $ULocale$Type): $DateFormat
public static "getInstanceForSkeleton"(arg0: string, arg1: $Locale$Type): $DateFormat
public "getBooleanAttribute"(arg0: $DateFormat$BooleanAttribute$Type): boolean
public static "getAvailableULocales"(): ($ULocale)[]
public "setContext"(arg0: $DisplayContext$Type): void
set "calendarLenient"(value: boolean)
get "calendarLenient"(): boolean
get "instance"(): $DateFormat
get "dateTimeInstance"(): $DateFormat
get "availableLocales"(): ($Locale)[]
set "timeZone"(value: $TimeZone$Type)
get "timeZone"(): $TimeZone
set "lenient"(value: boolean)
get "lenient"(): boolean
get "dateInstance"(): $DateFormat
get "timeInstance"(): $DateFormat
set "calendar"(value: $Calendar$Type)
get "calendar"(): $Calendar
set "numberFormat"(value: $NumberFormat$Type)
get "numberFormat"(): $NumberFormat
get "availableULocales"(): ($ULocale)[]
set "context"(value: $DisplayContext$Type)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DateFormat$Type = ($DateFormat);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DateFormat_ = $DateFormat$Type;
}}
declare module "packages/com/ibm/icu/text/$NumberFormat" {
import {$DisplayContext$Type, $DisplayContext$Type$Type} from "packages/com/ibm/icu/text/$DisplayContext$Type"
import {$BigDecimal, $BigDecimal$Type} from "packages/com/ibm/icu/math/$BigDecimal"
import {$BigDecimal as $BigDecimal$0, $BigDecimal$Type as $BigDecimal$0$Type} from "packages/java/math/$BigDecimal"
import {$Locale, $Locale$Type} from "packages/java/util/$Locale"
import {$ULocale, $ULocale$Type} from "packages/com/ibm/icu/util/$ULocale"
import {$FieldPosition, $FieldPosition$Type} from "packages/java/text/$FieldPosition"
import {$DisplayContext, $DisplayContext$Type} from "packages/com/ibm/icu/text/$DisplayContext"
import {$Currency, $Currency$Type} from "packages/com/ibm/icu/util/$Currency"
import {$BigInteger, $BigInteger$Type} from "packages/java/math/$BigInteger"
import {$UFormat, $UFormat$Type} from "packages/com/ibm/icu/text/$UFormat"
import {$StringBuffer, $StringBuffer$Type} from "packages/java/lang/$StringBuffer"
import {$CurrencyAmount, $CurrencyAmount$Type} from "packages/com/ibm/icu/util/$CurrencyAmount"
import {$ParsePosition, $ParsePosition$Type} from "packages/java/text/$ParsePosition"
import {$NumberFormat$NumberFormatFactory, $NumberFormat$NumberFormatFactory$Type} from "packages/com/ibm/icu/text/$NumberFormat$NumberFormatFactory"

export class $NumberFormat extends $UFormat {
static readonly "NUMBERSTYLE": integer
static readonly "CURRENCYSTYLE": integer
static readonly "PERCENTSTYLE": integer
static readonly "SCIENTIFICSTYLE": integer
static readonly "INTEGERSTYLE": integer
static readonly "ISOCURRENCYSTYLE": integer
static readonly "PLURALCURRENCYSTYLE": integer
static readonly "ACCOUNTINGCURRENCYSTYLE": integer
static readonly "CASHCURRENCYSTYLE": integer
static readonly "STANDARDCURRENCYSTYLE": integer
static readonly "INTEGER_FIELD": integer
static readonly "FRACTION_FIELD": integer

constructor()

public "equals"(arg0: any): boolean
public "hashCode"(): integer
public "clone"(): any
public "format"(arg0: $BigDecimal$Type, arg1: $StringBuffer$Type, arg2: $FieldPosition$Type): $StringBuffer
public "format"(arg0: $BigDecimal$0$Type, arg1: $StringBuffer$Type, arg2: $FieldPosition$Type): $StringBuffer
public "format"(arg0: $BigInteger$Type, arg1: $StringBuffer$Type, arg2: $FieldPosition$Type): $StringBuffer
public "format"(arg0: $CurrencyAmount$Type, arg1: $StringBuffer$Type, arg2: $FieldPosition$Type): $StringBuffer
public "format"(arg0: any, arg1: $StringBuffer$Type, arg2: $FieldPosition$Type): $StringBuffer
public "format"(arg0: long): string
public "format"(arg0: $BigDecimal$0$Type): string
public "format"(arg0: double): string
public "format"(arg0: $BigInteger$Type): string
public "format"(arg0: long, arg1: $StringBuffer$Type, arg2: $FieldPosition$Type): $StringBuffer
public "format"(arg0: $BigDecimal$Type): string
public "format"(arg0: double, arg1: $StringBuffer$Type, arg2: $FieldPosition$Type): $StringBuffer
public "format"(arg0: $CurrencyAmount$Type): string
public static "getInstance"(arg0: $Locale$Type, arg1: integer): $NumberFormat
public static "getInstance"(): $NumberFormat
public static "getInstance"(arg0: $Locale$Type): $NumberFormat
public static "getInstance"(arg0: $ULocale$Type): $NumberFormat
public static "getInstance"(arg0: integer): $NumberFormat
public static "getInstance"(arg0: $ULocale$Type, arg1: integer): $NumberFormat
public "getContext"(arg0: $DisplayContext$Type$Type): $DisplayContext
public "parse"(arg0: string, arg1: $ParsePosition$Type): number
public "parse"(arg0: string): number
public static "unregister"(arg0: any): boolean
public "getRoundingMode"(): integer
public static "getAvailableLocales"(): ($Locale)[]
public "parseObject"(arg0: string, arg1: $ParsePosition$Type): any
public "getMaximumIntegerDigits"(): integer
public "getMinimumIntegerDigits"(): integer
public "getMaximumFractionDigits"(): integer
public "getMinimumFractionDigits"(): integer
public "isGroupingUsed"(): boolean
public "isParseIntegerOnly"(): boolean
public "setGroupingUsed"(arg0: boolean): void
public "setMinimumIntegerDigits"(arg0: integer): void
public "setMaximumIntegerDigits"(arg0: integer): void
public "setMaximumFractionDigits"(arg0: integer): void
public "setMinimumFractionDigits"(arg0: integer): void
public "getCurrency"(): $Currency
public "setCurrency"(arg0: $Currency$Type): void
public "setRoundingMode"(arg0: integer): void
public static "getNumberInstance"(arg0: $ULocale$Type): $NumberFormat
public static "getNumberInstance"(): $NumberFormat
public static "getNumberInstance"(arg0: $Locale$Type): $NumberFormat
public static "getPercentInstance"(): $NumberFormat
public static "getPercentInstance"(arg0: $Locale$Type): $NumberFormat
public static "getPercentInstance"(arg0: $ULocale$Type): $NumberFormat
public static "getCurrencyInstance"(arg0: $ULocale$Type): $NumberFormat
public static "getCurrencyInstance"(arg0: $Locale$Type): $NumberFormat
public static "getCurrencyInstance"(): $NumberFormat
public static "getIntegerInstance"(arg0: $ULocale$Type): $NumberFormat
public static "getIntegerInstance"(): $NumberFormat
public static "getIntegerInstance"(arg0: $Locale$Type): $NumberFormat
public "setParseIntegerOnly"(arg0: boolean): void
public static "getScientificInstance"(): $NumberFormat
public static "getScientificInstance"(arg0: $ULocale$Type): $NumberFormat
public static "getScientificInstance"(arg0: $Locale$Type): $NumberFormat
public static "registerFactory"(arg0: $NumberFormat$NumberFormatFactory$Type): any
/**
 * 
 * @deprecated
 */
public static "getPatternForStyle"(arg0: $ULocale$Type, arg1: integer): string
public static "getAvailableULocales"(): ($ULocale)[]
/**
 * 
 * @deprecated
 */
public static "getPatternForStyleAndNumberingSystem"(arg0: $ULocale$Type, arg1: string, arg2: integer): string
public "setContext"(arg0: $DisplayContext$Type): void
public "setParseStrict"(arg0: boolean): void
public "parseCurrency"(arg0: charseq, arg1: $ParsePosition$Type): $CurrencyAmount
public "isParseStrict"(): boolean
get "instance"(): $NumberFormat
get "roundingMode"(): integer
get "availableLocales"(): ($Locale)[]
get "maximumIntegerDigits"(): integer
get "minimumIntegerDigits"(): integer
get "maximumFractionDigits"(): integer
get "minimumFractionDigits"(): integer
get "groupingUsed"(): boolean
get "parseIntegerOnly"(): boolean
set "groupingUsed"(value: boolean)
set "minimumIntegerDigits"(value: integer)
set "maximumIntegerDigits"(value: integer)
set "maximumFractionDigits"(value: integer)
set "minimumFractionDigits"(value: integer)
get "currency"(): $Currency
set "currency"(value: $Currency$Type)
set "roundingMode"(value: integer)
get "numberInstance"(): $NumberFormat
get "percentInstance"(): $NumberFormat
get "currencyInstance"(): $NumberFormat
get "integerInstance"(): $NumberFormat
set "parseIntegerOnly"(value: boolean)
get "scientificInstance"(): $NumberFormat
get "availableULocales"(): ($ULocale)[]
set "context"(value: $DisplayContext$Type)
set "parseStrict"(value: boolean)
get "parseStrict"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NumberFormat$Type = ($NumberFormat);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NumberFormat_ = $NumberFormat$Type;
}}
declare module "packages/com/ibm/icu/text/$UnicodeSet$XSymbolTable" {
import {$SymbolTable, $SymbolTable$Type} from "packages/com/ibm/icu/text/$SymbolTable"
import {$UnicodeSet, $UnicodeSet$Type} from "packages/com/ibm/icu/text/$UnicodeSet"
import {$ParsePosition, $ParsePosition$Type} from "packages/java/text/$ParsePosition"
import {$UnicodeMatcher, $UnicodeMatcher$Type} from "packages/com/ibm/icu/text/$UnicodeMatcher"

export class $UnicodeSet$XSymbolTable implements $SymbolTable {

constructor()

public "lookup"(arg0: string): (character)[]
public "parseReference"(arg0: string, arg1: $ParsePosition$Type, arg2: integer): string
public "applyPropertyAlias"(arg0: string, arg1: string, arg2: $UnicodeSet$Type): boolean
public "lookupMatcher"(arg0: integer): $UnicodeMatcher
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $UnicodeSet$XSymbolTable$Type = ($UnicodeSet$XSymbolTable);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $UnicodeSet$XSymbolTable_ = $UnicodeSet$XSymbolTable$Type;
}}
declare module "packages/com/ibm/icu/util/$ULocale" {
import {$Comparable, $Comparable$Type} from "packages/java/lang/$Comparable"
import {$Serializable, $Serializable$Type} from "packages/java/io/$Serializable"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$ULocale$Category, $ULocale$Category$Type} from "packages/com/ibm/icu/util/$ULocale$Category"
import {$Iterator, $Iterator$Type} from "packages/java/util/$Iterator"
import {$ULocale$Minimize, $ULocale$Minimize$Type} from "packages/com/ibm/icu/util/$ULocale$Minimize"
import {$ULocale$Type, $ULocale$Type$Type} from "packages/com/ibm/icu/util/$ULocale$Type"
import {$ULocale$AvailableType, $ULocale$AvailableType$Type} from "packages/com/ibm/icu/util/$ULocale$AvailableType"
import {$Locale, $Locale$Type} from "packages/java/util/$Locale"

export class $ULocale implements $Serializable, $Comparable<($ULocale)> {
static readonly "ENGLISH": $ULocale
static readonly "FRENCH": $ULocale
static readonly "GERMAN": $ULocale
static readonly "ITALIAN": $ULocale
static readonly "JAPANESE": $ULocale
static readonly "KOREAN": $ULocale
static readonly "CHINESE": $ULocale
static readonly "SIMPLIFIED_CHINESE": $ULocale
static readonly "TRADITIONAL_CHINESE": $ULocale
static readonly "FRANCE": $ULocale
static readonly "GERMANY": $ULocale
static readonly "ITALY": $ULocale
static readonly "JAPAN": $ULocale
static readonly "KOREA": $ULocale
static readonly "CHINA": $ULocale
static readonly "PRC": $ULocale
static readonly "TAIWAN": $ULocale
static readonly "UK": $ULocale
static readonly "US": $ULocale
static readonly "CANADA": $ULocale
static readonly "CANADA_FRENCH": $ULocale
static readonly "ROOT": $ULocale
static "ACTUAL_LOCALE": $ULocale$Type
static "VALID_LOCALE": $ULocale$Type
static readonly "PRIVATE_USE_EXTENSION": character
static readonly "UNICODE_LOCALE_EXTENSION": character

constructor(arg0: string, arg1: string, arg2: string)
constructor(arg0: string)
constructor(arg0: string, arg1: string)

public static "getName"(arg0: string): string
public "getName"(): string
public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "clone"(): any
public "compareTo"(arg0: $ULocale$Type): integer
public static "getDefault"(): $ULocale
public static "getDefault"(arg0: $ULocale$Category$Type): $ULocale
public static "canonicalize"(arg0: string): string
public "getLanguage"(): string
public static "getLanguage"(arg0: string): string
public "getDisplayName"(arg0: $ULocale$Type): string
public static "getDisplayName"(arg0: string, arg1: $ULocale$Type): string
public static "getDisplayName"(arg0: string, arg1: string): string
public "getDisplayName"(): string
public static "getAvailableLocales"(): ($ULocale)[]
public "getUnicodeLocaleType"(arg0: string): string
public static "getCountry"(arg0: string): string
public "getCountry"(): string
public "getVariant"(): string
public static "getVariant"(arg0: string): string
public "getScript"(): string
public static "getScript"(arg0: string): string
public static "setDefault"(arg0: $ULocale$Category$Type, arg1: $ULocale$Type): void
public static "setDefault"(arg0: $ULocale$Type): void
public "getUnicodeLocaleAttributes"(): $Set<(string)>
public "getUnicodeLocaleKeys"(): $Set<(string)>
public static "getDisplayLanguage"(arg0: string, arg1: $ULocale$Type): string
public "getDisplayLanguage"(arg0: $ULocale$Type): string
public static "getDisplayLanguage"(arg0: string, arg1: string): string
public "getDisplayLanguage"(): string
public "getDisplayScript"(): string
public "getDisplayScript"(arg0: $ULocale$Type): string
public static "getDisplayScript"(arg0: string, arg1: string): string
public static "getDisplayScript"(arg0: string, arg1: $ULocale$Type): string
public static "getDisplayCountry"(arg0: string, arg1: $ULocale$Type): string
public "getDisplayCountry"(): string
public static "getDisplayCountry"(arg0: string, arg1: string): string
public "getDisplayCountry"(arg0: $ULocale$Type): string
public static "getDisplayVariant"(arg0: string, arg1: $ULocale$Type): string
public "getDisplayVariant"(): string
public "getDisplayVariant"(arg0: $ULocale$Type): string
public static "getDisplayVariant"(arg0: string, arg1: string): string
public static "getISOCountries"(): (string)[]
public static "getISOLanguages"(): (string)[]
public "getExtension"(arg0: character): string
public "getExtensionKeys"(): $Set<(character)>
public "toLanguageTag"(): string
public static "forLanguageTag"(arg0: string): $ULocale
public static "getISO3Language"(arg0: string): string
public "getISO3Language"(): string
public "getISO3Country"(): string
public static "getISO3Country"(arg0: string): string
public "isRightToLeft"(): boolean
public static "acceptLanguage"(arg0: string, arg1: ($ULocale$Type)[], arg2: (boolean)[]): $ULocale
public static "acceptLanguage"(arg0: ($ULocale$Type)[], arg1: (boolean)[]): $ULocale
public static "acceptLanguage"(arg0: string, arg1: (boolean)[]): $ULocale
public static "acceptLanguage"(arg0: ($ULocale$Type)[], arg1: ($ULocale$Type)[], arg2: (boolean)[]): $ULocale
public static "forLocale"(arg0: $Locale$Type): $ULocale
public static "createCanonical"(arg0: string): $ULocale
public static "createCanonical"(arg0: $ULocale$Type): $ULocale
public "getKeywords"(): $Iterator<(string)>
public static "getKeywords"(arg0: string): $Iterator<(string)>
public "getKeywordValue"(arg0: string): string
public static "getKeywordValue"(arg0: string, arg1: string): string
public static "setKeywordValue"(arg0: string, arg1: string, arg2: string): string
public "setKeywordValue"(arg0: string, arg1: string): $ULocale
public static "addLikelySubtags"(arg0: $ULocale$Type): $ULocale
public static "minimizeSubtags"(arg0: $ULocale$Type): $ULocale
/**
 * 
 * @deprecated
 */
public static "minimizeSubtags"(arg0: $ULocale$Type, arg1: $ULocale$Minimize$Type): $ULocale
public "getLineOrientation"(): string
public static "getDisplayKeyword"(arg0: string, arg1: string): string
public static "getDisplayKeyword"(arg0: string, arg1: $ULocale$Type): string
public static "getDisplayKeyword"(arg0: string): string
public static "toLegacyKey"(arg0: string): string
public static "toLegacyType"(arg0: string, arg1: string): string
public static "toUnicodeLocaleKey"(arg0: string): string
public "toLocale"(): $Locale
public static "getBaseName"(arg0: string): string
public "getBaseName"(): string
/**
 * 
 * @deprecated
 */
public static "getRegionForSupplementalData"(arg0: $ULocale$Type, arg1: boolean): string
/**
 * 
 * @deprecated
 */
public "getDisplayScriptInContext"(arg0: $ULocale$Type): string
/**
 * 
 * @deprecated
 */
public static "getDisplayScriptInContext"(arg0: string, arg1: $ULocale$Type): string
/**
 * 
 * @deprecated
 */
public static "getDisplayScriptInContext"(arg0: string, arg1: string): string
/**
 * 
 * @deprecated
 */
public "getDisplayScriptInContext"(): string
public static "getAvailableLocalesByType"(arg0: $ULocale$AvailableType$Type): $Collection<($ULocale)>
public "getDisplayLanguageWithDialect"(): string
public "getDisplayLanguageWithDialect"(arg0: $ULocale$Type): string
public static "getDisplayLanguageWithDialect"(arg0: string, arg1: string): string
public static "getDisplayLanguageWithDialect"(arg0: string, arg1: $ULocale$Type): string
public static "getDisplayKeywordValue"(arg0: string, arg1: string, arg2: string): string
public "getDisplayKeywordValue"(arg0: string, arg1: $ULocale$Type): string
public "getDisplayKeywordValue"(arg0: string): string
public static "getDisplayKeywordValue"(arg0: string, arg1: string, arg2: $ULocale$Type): string
public "getCharacterOrientation"(): string
public static "getDisplayNameWithDialect"(arg0: string, arg1: string): string
public "getDisplayNameWithDialect"(): string
public static "getDisplayNameWithDialect"(arg0: string, arg1: $ULocale$Type): string
public "getDisplayNameWithDialect"(arg0: $ULocale$Type): string
public static "toUnicodeLocaleType"(arg0: string, arg1: string): string
public static "getFallback"(arg0: string): string
public "getFallback"(): $ULocale
get "name"(): string
get "default"(): $ULocale
get "language"(): string
get "displayName"(): string
get "availableLocales"(): ($ULocale)[]
get "country"(): string
get "variant"(): string
get "script"(): string
set "default"(value: $ULocale$Type)
get "unicodeLocaleAttributes"(): $Set<(string)>
get "unicodeLocaleKeys"(): $Set<(string)>
get "displayLanguage"(): string
get "displayScript"(): string
get "displayCountry"(): string
get "displayVariant"(): string
get "iSOCountries"(): (string)[]
get "iSOLanguages"(): (string)[]
get "extensionKeys"(): $Set<(character)>
get "iSO3Language"(): string
get "iSO3Country"(): string
get "rightToLeft"(): boolean
get "keywords"(): $Iterator<(string)>
get "lineOrientation"(): string
get "baseName"(): string
get "displayScriptInContext"(): string
get "displayLanguageWithDialect"(): string
get "characterOrientation"(): string
get "displayNameWithDialect"(): string
get "fallback"(): $ULocale
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ULocale$Type = ($ULocale);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ULocale_ = $ULocale$Type;
}}
declare module "packages/com/ibm/icu/util/$Freezable" {
import {$Cloneable, $Cloneable$Type} from "packages/java/lang/$Cloneable"

export interface $Freezable<T> extends $Cloneable {

 "isFrozen"(): boolean
 "freeze"(): T
 "cloneAsThawed"(): T
}

export namespace $Freezable {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Freezable$Type<T> = ($Freezable<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Freezable_<T> = $Freezable$Type<(T)>;
}}
declare module "packages/com/ibm/icu/text/$DateFormat$BooleanAttribute" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $DateFormat$BooleanAttribute extends $Enum<($DateFormat$BooleanAttribute)> {
static readonly "PARSE_ALLOW_WHITESPACE": $DateFormat$BooleanAttribute
static readonly "PARSE_ALLOW_NUMERIC": $DateFormat$BooleanAttribute
static readonly "PARSE_MULTIPLE_PATTERNS_FOR_MATCH": $DateFormat$BooleanAttribute
static readonly "PARSE_PARTIAL_LITERAL_MATCH": $DateFormat$BooleanAttribute
/**
 * 
 * @deprecated
 */
static readonly "PARSE_PARTIAL_MATCH": $DateFormat$BooleanAttribute


public static "values"(): ($DateFormat$BooleanAttribute)[]
public static "valueOf"(arg0: string): $DateFormat$BooleanAttribute
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DateFormat$BooleanAttribute$Type = (("parse_partial_match") | ("parse_allow_numeric") | ("parse_multiple_patterns_for_match") | ("parse_partial_literal_match") | ("parse_allow_whitespace")) | ($DateFormat$BooleanAttribute);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DateFormat$BooleanAttribute_ = $DateFormat$BooleanAttribute$Type;
}}
declare module "packages/com/ibm/icu/impl/$TextTrieMap" {
import {$UnicodeSet, $UnicodeSet$Type} from "packages/com/ibm/icu/text/$UnicodeSet"
import {$Iterator, $Iterator$Type} from "packages/java/util/$Iterator"
import {$TextTrieMap$Output, $TextTrieMap$Output$Type} from "packages/com/ibm/icu/impl/$TextTrieMap$Output"
import {$TextTrieMap$ResultHandler, $TextTrieMap$ResultHandler$Type} from "packages/com/ibm/icu/impl/$TextTrieMap$ResultHandler"

export class $TextTrieMap<V> {

constructor(arg0: boolean)

public "get"(arg0: charseq, arg1: integer, arg2: $TextTrieMap$Output$Type): $Iterator<(V)>
public "get"(arg0: charseq, arg1: integer): $Iterator<(V)>
public "get"(arg0: string): $Iterator<(V)>
public "put"(arg0: charseq, arg1: V): $TextTrieMap<(V)>
public "find"(arg0: charseq, arg1: $TextTrieMap$ResultHandler$Type<(V)>): void
public "find"(arg0: charseq, arg1: integer, arg2: $TextTrieMap$ResultHandler$Type<(V)>): void
public "putLeadCodePoints"(arg0: $UnicodeSet$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TextTrieMap$Type<V> = ($TextTrieMap<(V)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TextTrieMap_<V> = $TextTrieMap$Type<(V)>;
}}
declare module "packages/com/ibm/icu/util/$Calendar$WeekData" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $Calendar$WeekData {
readonly "firstDayOfWeek": integer
readonly "minimalDaysInFirstWeek": integer
readonly "weekendOnset": integer
readonly "weekendOnsetMillis": integer
readonly "weekendCease": integer
readonly "weekendCeaseMillis": integer

constructor(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Calendar$WeekData$Type = ($Calendar$WeekData);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Calendar$WeekData_ = $Calendar$WeekData$Type;
}}
declare module "packages/com/ibm/icu/text/$UnicodeSet$EntryRange" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $UnicodeSet$EntryRange {
 "codepoint": integer
 "codepointEnd": integer


public "toString"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $UnicodeSet$EntryRange$Type = ($UnicodeSet$EntryRange);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $UnicodeSet$EntryRange_ = $UnicodeSet$EntryRange$Type;
}}
declare module "packages/com/ibm/icu/math/$MathContext" {
import {$Serializable, $Serializable$Type} from "packages/java/io/$Serializable"

export class $MathContext implements $Serializable {
static readonly "PLAIN": integer
static readonly "SCIENTIFIC": integer
static readonly "ENGINEERING": integer
static readonly "ROUND_CEILING": integer
static readonly "ROUND_DOWN": integer
static readonly "ROUND_FLOOR": integer
static readonly "ROUND_HALF_DOWN": integer
static readonly "ROUND_HALF_EVEN": integer
static readonly "ROUND_HALF_UP": integer
static readonly "ROUND_UNNECESSARY": integer
static readonly "ROUND_UP": integer
static readonly "DEFAULT": $MathContext

constructor(arg0: integer, arg1: integer, arg2: boolean, arg3: integer)
constructor(arg0: integer, arg1: integer, arg2: boolean)
constructor(arg0: integer, arg1: integer)
constructor(arg0: integer)

public "toString"(): string
public "getRoundingMode"(): integer
public "getDigits"(): integer
public "getForm"(): integer
public "getLostDigits"(): boolean
get "roundingMode"(): integer
get "digits"(): integer
get "form"(): integer
get "lostDigits"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MathContext$Type = ($MathContext);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MathContext_ = $MathContext$Type;
}}
declare module "packages/com/ibm/icu/impl/units/$SingleUnitImpl" {
import {$MeasureUnit$MeasurePrefix, $MeasureUnit$MeasurePrefix$Type} from "packages/com/ibm/icu/util/$MeasureUnit$MeasurePrefix"
import {$MeasureUnit, $MeasureUnit$Type} from "packages/com/ibm/icu/util/$MeasureUnit"

export class $SingleUnitImpl {

constructor()

public "getDimensionality"(): integer
public "setDimensionality"(arg0: integer): void
public "copy"(): $SingleUnitImpl
public "getIndex"(): integer
public "build"(): $MeasureUnit
public "getPrefix"(): $MeasureUnit$MeasurePrefix
public "setPrefix"(arg0: $MeasureUnit$MeasurePrefix$Type): void
public "setSimpleUnit"(arg0: integer, arg1: (string)[]): void
public "getSimpleUnitID"(): string
public "getNeutralIdentifier"(): string
get "dimensionality"(): integer
set "dimensionality"(value: integer)
get "index"(): integer
get "prefix"(): $MeasureUnit$MeasurePrefix
set "prefix"(value: $MeasureUnit$MeasurePrefix$Type)
get "simpleUnitID"(): string
get "neutralIdentifier"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SingleUnitImpl$Type = ($SingleUnitImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SingleUnitImpl_ = $SingleUnitImpl$Type;
}}
declare module "packages/com/ibm/icu/text/$UnicodeMatcher" {
import {$Replaceable, $Replaceable$Type} from "packages/com/ibm/icu/text/$Replaceable"
import {$UnicodeSet, $UnicodeSet$Type} from "packages/com/ibm/icu/text/$UnicodeSet"

export interface $UnicodeMatcher {

 "matches"(arg0: $Replaceable$Type, arg1: (integer)[], arg2: integer, arg3: boolean): integer
 "toPattern"(arg0: boolean): string
 "matchesIndexValue"(arg0: integer): boolean
 "addMatchSetTo"(arg0: $UnicodeSet$Type): void
}

export namespace $UnicodeMatcher {
const U_MISMATCH: integer
const U_PARTIAL_MATCH: integer
const U_MATCH: integer
const ETHER: character
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $UnicodeMatcher$Type = ($UnicodeMatcher);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $UnicodeMatcher_ = $UnicodeMatcher$Type;
}}
declare module "packages/com/ibm/icu/impl/units/$MeasureUnitImpl$MeasureUnitImplWithIndex" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $MeasureUnitImpl$MeasureUnitImplWithIndex {


}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MeasureUnitImpl$MeasureUnitImplWithIndex$Type = ($MeasureUnitImpl$MeasureUnitImplWithIndex);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MeasureUnitImpl$MeasureUnitImplWithIndex_ = $MeasureUnitImpl$MeasureUnitImplWithIndex$Type;
}}

declare module "packages/blue/endless/jankson/$JsonArray" {
import {$Comparator, $Comparator$Type} from "packages/java/util/$Comparator"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$ListIterator, $ListIterator$Type} from "packages/java/util/$ListIterator"
import {$JsonGrammar, $JsonGrammar$Type} from "packages/blue/endless/jankson/$JsonGrammar"
import {$JsonElement, $JsonElement$Type} from "packages/blue/endless/jankson/$JsonElement"
import {$Spliterator, $Spliterator$Type} from "packages/java/util/$Spliterator"
import {$Iterable, $Iterable$Type} from "packages/java/lang/$Iterable"
import {$Writer, $Writer$Type} from "packages/java/io/$Writer"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$UnaryOperator, $UnaryOperator$Type} from "packages/java/util/function/$UnaryOperator"
import {$Marshaller, $Marshaller$Type} from "packages/blue/endless/jankson/api/$Marshaller"
import {$IntFunction, $IntFunction$Type} from "packages/java/util/function/$IntFunction"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$Iterator, $Iterator$Type} from "packages/java/util/$Iterator"

export class $JsonArray extends $JsonElement implements $List<($JsonElement)>, $Iterable<($JsonElement)> {

constructor(arg0: $Collection$Type<(any)>, arg1: $Marshaller$Type)
constructor<T>(arg0: (T)[], arg1: $Marshaller$Type)
constructor()

public "add"(arg0: $JsonElement$Type, arg1: string): boolean
public "add"(arg0: $JsonElement$Type): boolean
public "add"(arg0: integer, arg1: $JsonElement$Type): void
public "remove"(arg0: integer): $JsonElement
public "remove"(arg0: any): boolean
public "get"<E>(arg0: $Class$Type<(E)>, arg1: integer): E
public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "clone"(): $JsonArray
public "indexOf"(arg0: any): integer
public "getBoolean"(arg0: integer, arg1: boolean): boolean
public "getByte"(arg0: integer, arg1: byte): byte
public "getShort"(arg0: integer, arg1: short): short
public "getChar"(arg0: integer, arg1: character): character
public "getInt"(arg0: integer, arg1: integer): integer
public "getLong"(arg0: integer, arg1: long): long
public "getFloat"(arg0: integer, arg1: float): float
public "getDouble"(arg0: integer, arg1: double): double
public "clear"(): void
public "lastIndexOf"(arg0: any): integer
public "isEmpty"(): boolean
public "size"(): integer
public "subList"(arg0: integer, arg1: integer): $List<($JsonElement)>
public "toArray"<T>(arg0: (T)[]): (T)[]
public "toArray"(): ($JsonElement)[]
public "iterator"(): $Iterator<($JsonElement)>
public "contains"(arg0: any): boolean
public "addAll"(arg0: $Collection$Type<(any)>): boolean
public "addAll"(arg0: integer, arg1: $Collection$Type<(any)>): boolean
public "set"(arg0: integer, arg1: $JsonElement$Type): $JsonElement
public "removeAll"(arg0: $Collection$Type<(any)>): boolean
public "retainAll"(arg0: $Collection$Type<(any)>): boolean
public "listIterator"(arg0: integer): $ListIterator<($JsonElement)>
public "listIterator"(): $ListIterator<($JsonElement)>
public "containsAll"(arg0: $Collection$Type<(any)>): boolean
public "getString"(arg0: integer, arg1: string): string
public "setComment"(arg0: integer, arg1: string): void
public "getComment"(arg0: integer): string
public "toJson"(arg0: boolean, arg1: boolean, arg2: integer): string
public "toJson"(arg0: $Writer$Type, arg1: $JsonGrammar$Type, arg2: integer): void
public "getMarshaller"(): $Marshaller
public "setMarshaller"(arg0: $Marshaller$Type): void
public static "copyOf"<E>(arg0: $Collection$Type<(any)>): $List<($JsonElement)>
public "replaceAll"(arg0: $UnaryOperator$Type<($JsonElement$Type)>): void
public static "of"<E>(arg0: $JsonElement$Type, arg1: $JsonElement$Type, arg2: $JsonElement$Type, arg3: $JsonElement$Type): $List<($JsonElement)>
public static "of"<E>(arg0: $JsonElement$Type, arg1: $JsonElement$Type, arg2: $JsonElement$Type): $List<($JsonElement)>
public static "of"<E>(arg0: $JsonElement$Type, arg1: $JsonElement$Type): $List<($JsonElement)>
public static "of"<E>(arg0: $JsonElement$Type): $List<($JsonElement)>
public static "of"<E>(): $List<($JsonElement)>
public static "of"<E>(arg0: $JsonElement$Type, arg1: $JsonElement$Type, arg2: $JsonElement$Type, arg3: $JsonElement$Type, arg4: $JsonElement$Type, arg5: $JsonElement$Type, arg6: $JsonElement$Type, arg7: $JsonElement$Type): $List<($JsonElement)>
public static "of"<E>(arg0: $JsonElement$Type, arg1: $JsonElement$Type, arg2: $JsonElement$Type, arg3: $JsonElement$Type, arg4: $JsonElement$Type, arg5: $JsonElement$Type, arg6: $JsonElement$Type): $List<($JsonElement)>
public static "of"<E>(arg0: $JsonElement$Type, arg1: $JsonElement$Type, arg2: $JsonElement$Type, arg3: $JsonElement$Type, arg4: $JsonElement$Type, arg5: $JsonElement$Type): $List<($JsonElement)>
public static "of"<E>(arg0: $JsonElement$Type, arg1: $JsonElement$Type, arg2: $JsonElement$Type, arg3: $JsonElement$Type, arg4: $JsonElement$Type): $List<($JsonElement)>
public static "of"<E>(arg0: $JsonElement$Type, arg1: $JsonElement$Type, arg2: $JsonElement$Type, arg3: $JsonElement$Type, arg4: $JsonElement$Type, arg5: $JsonElement$Type, arg6: $JsonElement$Type, arg7: $JsonElement$Type, arg8: $JsonElement$Type, arg9: $JsonElement$Type): $List<($JsonElement)>
public static "of"<E>(arg0: $JsonElement$Type, arg1: $JsonElement$Type, arg2: $JsonElement$Type, arg3: $JsonElement$Type, arg4: $JsonElement$Type, arg5: $JsonElement$Type, arg6: $JsonElement$Type, arg7: $JsonElement$Type, arg8: $JsonElement$Type): $List<($JsonElement)>
public static "of"<E>(...arg0: ($JsonElement$Type)[]): $List<($JsonElement)>
public "spliterator"(): $Spliterator<($JsonElement)>
public "sort"(arg0: $Comparator$Type<(any)>): void
public "forEach"(arg0: $Consumer$Type<(any)>): void
public "toArray"<T>(arg0: $IntFunction$Type<((T)[])>): (T)[]
public "stream"(): $Stream<($JsonElement)>
public "removeIf"(arg0: $Predicate$Type<(any)>): boolean
public "parallelStream"(): $Stream<($JsonElement)>
[Symbol.iterator](): IterableIterator<$JsonElement>;
[index: number]: $JsonElement
get "empty"(): boolean
get "marshaller"(): $Marshaller
set "marshaller"(value: $Marshaller$Type)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JsonArray$Type = ($JsonArray);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JsonArray_ = $JsonArray$Type;
}}
declare module "packages/blue/endless/jankson/$JsonGrammar" {
import {$JsonGrammar$Builder, $JsonGrammar$Builder$Type} from "packages/blue/endless/jankson/$JsonGrammar$Builder"

export class $JsonGrammar {
static readonly "JANKSON": $JsonGrammar
static readonly "JSON5": $JsonGrammar
static readonly "STRICT": $JsonGrammar
static readonly "COMPACT": $JsonGrammar

constructor()

public static "builder"(): $JsonGrammar$Builder
public "shouldOutputWhitespace"(): boolean
public "hasComments"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JsonGrammar$Type = ($JsonGrammar);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JsonGrammar_ = $JsonGrammar$Type;
}}
declare module "packages/blue/endless/jankson/api/$SyntaxError" {
import {$Exception, $Exception$Type} from "packages/java/lang/$Exception"

export class $SyntaxError extends $Exception {

constructor(arg0: string)

public "setEndParsing"(arg0: integer, arg1: integer): void
public "setStartParsing"(arg0: integer, arg1: integer): void
public "getCompleteMessage"(): string
public "getLineMessage"(): string
get "completeMessage"(): string
get "lineMessage"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SyntaxError$Type = ($SyntaxError);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SyntaxError_ = $SyntaxError$Type;
}}
declare module "packages/blue/endless/jankson/$JsonElement" {
import {$Cloneable, $Cloneable$Type} from "packages/java/lang/$Cloneable"
import {$JsonGrammar, $JsonGrammar$Type} from "packages/blue/endless/jankson/$JsonGrammar"
import {$Writer, $Writer$Type} from "packages/java/io/$Writer"

export class $JsonElement implements $Cloneable {

constructor()

public "toJson"(arg0: $JsonGrammar$Type): string
/**
 * 
 * @deprecated
 */
public "toJson"(arg0: boolean, arg1: boolean, arg2: integer): string
public "toJson"(arg0: $Writer$Type, arg1: $JsonGrammar$Type, arg2: integer): void
public "toJson"(arg0: $JsonGrammar$Type, arg1: integer): string
public "toJson"(arg0: boolean, arg1: boolean): string
public "toJson"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JsonElement$Type = ($JsonElement);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JsonElement_ = $JsonElement$Type;
}}
declare module "packages/blue/endless/jankson/$Jankson$Builder" {
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$JsonObject, $JsonObject$Type} from "packages/blue/endless/jankson/$JsonObject"
import {$DeserializerFunction, $DeserializerFunction$Type} from "packages/blue/endless/jankson/api/$DeserializerFunction"
import {$Jankson, $Jankson$Type} from "packages/blue/endless/jankson/$Jankson"
import {$Marshaller, $Marshaller$Type} from "packages/blue/endless/jankson/api/$Marshaller"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$JsonElement, $JsonElement$Type} from "packages/blue/endless/jankson/$JsonElement"
import {$BiFunction, $BiFunction$Type} from "packages/java/util/function/$BiFunction"

export class $Jankson$Builder {

constructor()

public "build"(): $Jankson
public "registerTypeFactory"<T>(arg0: $Class$Type<(T)>, arg1: $Supplier$Type<(T)>): $Jankson$Builder
public "registerDeserializer"<A, B>(arg0: $Class$Type<(A)>, arg1: $Class$Type<(B)>, arg2: $DeserializerFunction$Type<(A), (B)>): $Jankson$Builder
/**
 * 
 * @deprecated
 */
public "registerPrimitiveTypeAdapter"<T>(arg0: $Class$Type<(T)>, arg1: $Function$Type<(any), (T)>): $Jankson$Builder
public "allowBareRootObject"(): $Jankson$Builder
/**
 * 
 * @deprecated
 */
public "registerTypeAdapter"<T>(arg0: $Class$Type<(T)>, arg1: $Function$Type<($JsonObject$Type), (T)>): $Jankson$Builder
public "registerSerializer"<T>(arg0: $Class$Type<(T)>, arg1: $BiFunction$Type<(T), ($Marshaller$Type), ($JsonElement$Type)>): $Jankson$Builder
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Jankson$Builder$Type = ($Jankson$Builder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Jankson$Builder_ = $Jankson$Builder$Type;
}}
declare module "packages/blue/endless/jankson/$Jankson" {
import {$JsonObject, $JsonObject$Type} from "packages/blue/endless/jankson/$JsonObject"
import {$Jankson$Builder, $Jankson$Builder$Type} from "packages/blue/endless/jankson/$Jankson$Builder"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$File, $File$Type} from "packages/java/io/$File"
import {$Marshaller, $Marshaller$Type} from "packages/blue/endless/jankson/api/$Marshaller"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$InputStream, $InputStream$Type} from "packages/java/io/$InputStream"
import {$ParserContext, $ParserContext$Type} from "packages/blue/endless/jankson/impl/$ParserContext"
import {$JsonElement, $JsonElement$Type} from "packages/blue/endless/jankson/$JsonElement"
import {$SyntaxError, $SyntaxError$Type} from "packages/blue/endless/jankson/api/$SyntaxError"

export class $Jankson {


public "load"(arg0: $InputStream$Type): $JsonObject
public "load"(arg0: $File$Type): $JsonObject
public "load"(arg0: string): $JsonObject
public static "builder"(): $Jankson$Builder
public "push"<T>(arg0: $ParserContext$Type<(T)>, arg1: $Consumer$Type<(T)>): void
public "fromJson"<T>(arg0: string, arg1: $Class$Type<(T)>): T
public "fromJson"<T>(arg0: $JsonObject$Type, arg1: $Class$Type<(T)>): T
public "toJson"<T>(arg0: T, arg1: $Marshaller$Type): $JsonElement
public "toJson"<T>(arg0: T): $JsonElement
public "fromJsonCarefully"<T>(arg0: $JsonObject$Type, arg1: $Class$Type<(T)>): T
public "fromJsonCarefully"<T>(arg0: string, arg1: $Class$Type<(T)>): T
public "throwDelayed"(arg0: $SyntaxError$Type): void
public "loadElement"(arg0: $InputStream$Type): $JsonElement
public "loadElement"(arg0: $File$Type): $JsonElement
public "loadElement"(arg0: string): $JsonElement
public "getMarshaller"(): $Marshaller
get "marshaller"(): $Marshaller
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Jankson$Type = ($Jankson);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Jankson_ = $Jankson$Type;
}}
declare module "packages/blue/endless/jankson/api/$Marshaller" {
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$Type, $Type$Type} from "packages/java/lang/reflect/$Type"
import {$JsonElement, $JsonElement$Type} from "packages/blue/endless/jankson/$JsonElement"

export interface $Marshaller {

 "marshallCarefully"<E>(arg0: $Class$Type<(E)>, arg1: $JsonElement$Type): E
 "serialize"(arg0: any): $JsonElement
 "marshall"<E>(arg0: $Class$Type<(E)>, arg1: $JsonElement$Type): E
 "marshall"<E>(arg0: $Type$Type, arg1: $JsonElement$Type): E
}

export namespace $Marshaller {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Marshaller$Type = ($Marshaller);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Marshaller_ = $Marshaller$Type;
}}
declare module "packages/blue/endless/jankson/$JsonObject" {
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$JsonGrammar, $JsonGrammar$Type} from "packages/blue/endless/jankson/$JsonGrammar"
import {$JsonElement, $JsonElement$Type} from "packages/blue/endless/jankson/$JsonElement"
import {$Writer, $Writer$Type} from "packages/java/io/$Writer"
import {$BiFunction, $BiFunction$Type} from "packages/java/util/function/$BiFunction"
import {$BiConsumer, $BiConsumer$Type} from "packages/java/util/function/$BiConsumer"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$Marshaller, $Marshaller$Type} from "packages/blue/endless/jankson/api/$Marshaller"
import {$Map$Entry, $Map$Entry$Type} from "packages/java/util/$Map$Entry"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $JsonObject extends $JsonElement implements $Map<(string), ($JsonElement)> {

constructor()

public "remove"(arg0: any): $JsonElement
public "get"<E>(arg0: $Class$Type<(E)>, arg1: string): E
public "get"(arg0: any): $JsonElement
public "put"(arg0: string, arg1: $JsonElement$Type): $JsonElement
public "put"(arg0: string, arg1: $JsonElement$Type, arg2: string): $JsonElement
public "equals"(arg0: any): boolean
public "toString"(): string
public "values"(): $Collection<($JsonElement)>
public "hashCode"(): integer
public "getBoolean"(arg0: string, arg1: boolean): boolean
public "getByte"(arg0: string, arg1: byte): byte
public "getShort"(arg0: string, arg1: short): short
public "getChar"(arg0: string, arg1: character): character
public "getInt"(arg0: string, arg1: integer): integer
public "getLong"(arg0: string, arg1: long): long
public "getFloat"(arg0: string, arg1: float): float
public "getDouble"(arg0: string, arg1: double): double
public "clear"(): void
public "isEmpty"(): boolean
public "size"(): integer
public "entrySet"(): $Set<($Map$Entry<(string), ($JsonElement)>)>
public "putAll"(arg0: $Map$Type<(any), (any)>): void
public "containsKey"(arg0: any): boolean
public "keySet"(): $Set<(string)>
public "containsValue"(arg0: any): boolean
public "getObject"(arg0: string): $JsonObject
public "setComment"(arg0: string, arg1: string): void
public "getComment"(arg0: string): string
public "toJson"(arg0: $Writer$Type, arg1: $JsonGrammar$Type, arg2: integer): void
public "toJson"(arg0: boolean, arg1: boolean, arg2: integer): string
public "recursiveGetOrCreate"<E extends $JsonElement>(arg0: $Class$Type<(E)>, arg1: string, arg2: E, arg3: string): E
public "getDelta"(arg0: $JsonObject$Type): $JsonObject
public "putDefault"(arg0: string, arg1: $JsonElement$Type, arg2: string): $JsonElement
public "putDefault"<T>(arg0: string, arg1: T, arg2: $Class$Type<(any)>, arg3: string): T
public "putDefault"<T>(arg0: string, arg1: T, arg2: string): T
public "recursiveGet"<E>(arg0: $Class$Type<(E)>, arg1: string): E
public "getMarshaller"(): $Marshaller
public "setMarshaller"(arg0: $Marshaller$Type): void
public "remove"(arg0: any, arg1: any): boolean
public static "copyOf"<K, V>(arg0: $Map$Type<(any), (any)>): $Map<(string), ($JsonElement)>
public "replace"(arg0: string, arg1: $JsonElement$Type): $JsonElement
public "replace"(arg0: string, arg1: $JsonElement$Type, arg2: $JsonElement$Type): boolean
public "replaceAll"(arg0: $BiFunction$Type<(any), (any), (any)>): void
public static "of"<K, V>(arg0: string, arg1: $JsonElement$Type, arg2: string, arg3: $JsonElement$Type, arg4: string, arg5: $JsonElement$Type, arg6: string, arg7: $JsonElement$Type, arg8: string, arg9: $JsonElement$Type): $Map<(string), ($JsonElement)>
public static "of"<K, V>(arg0: string, arg1: $JsonElement$Type, arg2: string, arg3: $JsonElement$Type, arg4: string, arg5: $JsonElement$Type, arg6: string, arg7: $JsonElement$Type): $Map<(string), ($JsonElement)>
public static "of"<K, V>(arg0: string, arg1: $JsonElement$Type, arg2: string, arg3: $JsonElement$Type, arg4: string, arg5: $JsonElement$Type): $Map<(string), ($JsonElement)>
public static "of"<K, V>(): $Map<(string), ($JsonElement)>
public static "of"<K, V>(arg0: string, arg1: $JsonElement$Type, arg2: string, arg3: $JsonElement$Type): $Map<(string), ($JsonElement)>
public static "of"<K, V>(arg0: string, arg1: $JsonElement$Type): $Map<(string), ($JsonElement)>
public static "of"<K, V>(arg0: string, arg1: $JsonElement$Type, arg2: string, arg3: $JsonElement$Type, arg4: string, arg5: $JsonElement$Type, arg6: string, arg7: $JsonElement$Type, arg8: string, arg9: $JsonElement$Type, arg10: string, arg11: $JsonElement$Type, arg12: string, arg13: $JsonElement$Type, arg14: string, arg15: $JsonElement$Type, arg16: string, arg17: $JsonElement$Type, arg18: string, arg19: $JsonElement$Type): $Map<(string), ($JsonElement)>
public static "of"<K, V>(arg0: string, arg1: $JsonElement$Type, arg2: string, arg3: $JsonElement$Type, arg4: string, arg5: $JsonElement$Type, arg6: string, arg7: $JsonElement$Type, arg8: string, arg9: $JsonElement$Type, arg10: string, arg11: $JsonElement$Type, arg12: string, arg13: $JsonElement$Type, arg14: string, arg15: $JsonElement$Type, arg16: string, arg17: $JsonElement$Type): $Map<(string), ($JsonElement)>
public static "of"<K, V>(arg0: string, arg1: $JsonElement$Type, arg2: string, arg3: $JsonElement$Type, arg4: string, arg5: $JsonElement$Type, arg6: string, arg7: $JsonElement$Type, arg8: string, arg9: $JsonElement$Type, arg10: string, arg11: $JsonElement$Type, arg12: string, arg13: $JsonElement$Type, arg14: string, arg15: $JsonElement$Type): $Map<(string), ($JsonElement)>
public static "of"<K, V>(arg0: string, arg1: $JsonElement$Type, arg2: string, arg3: $JsonElement$Type, arg4: string, arg5: $JsonElement$Type, arg6: string, arg7: $JsonElement$Type, arg8: string, arg9: $JsonElement$Type, arg10: string, arg11: $JsonElement$Type, arg12: string, arg13: $JsonElement$Type): $Map<(string), ($JsonElement)>
public static "of"<K, V>(arg0: string, arg1: $JsonElement$Type, arg2: string, arg3: $JsonElement$Type, arg4: string, arg5: $JsonElement$Type, arg6: string, arg7: $JsonElement$Type, arg8: string, arg9: $JsonElement$Type, arg10: string, arg11: $JsonElement$Type): $Map<(string), ($JsonElement)>
public "merge"(arg0: string, arg1: $JsonElement$Type, arg2: $BiFunction$Type<(any), (any), (any)>): $JsonElement
public "putIfAbsent"(arg0: string, arg1: $JsonElement$Type): $JsonElement
public "compute"(arg0: string, arg1: $BiFunction$Type<(any), (any), (any)>): $JsonElement
public static "entry"<K, V>(arg0: string, arg1: $JsonElement$Type): $Map$Entry<(string), ($JsonElement)>
public "forEach"(arg0: $BiConsumer$Type<(any), (any)>): void
public "computeIfAbsent"(arg0: string, arg1: $Function$Type<(any), (any)>): $JsonElement
public "getOrDefault"(arg0: any, arg1: $JsonElement$Type): $JsonElement
public "computeIfPresent"(arg0: string, arg1: $BiFunction$Type<(any), (any), (any)>): $JsonElement
public static "ofEntries"<K, V>(...arg0: ($Map$Entry$Type<(any), (any)>)[]): $Map<(string), ($JsonElement)>
[index: string | number]: $JsonElement
get "empty"(): boolean
get "marshaller"(): $Marshaller
set "marshaller"(value: $Marshaller$Type)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JsonObject$Type = ($JsonObject);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JsonObject_ = $JsonObject$Type;
}}
declare module "packages/blue/endless/jankson/impl/$ParserContext" {
import {$Jankson, $Jankson$Type} from "packages/blue/endless/jankson/$Jankson"

export interface $ParserContext<T> {

 "consume"(arg0: integer, arg1: $Jankson$Type): boolean
 "eof"(): void
 "getResult"(): T
 "isComplete"(): boolean
}

export namespace $ParserContext {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ParserContext$Type<T> = ($ParserContext<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ParserContext_<T> = $ParserContext$Type<(T)>;
}}
declare module "packages/blue/endless/jankson/$JsonGrammar$Builder" {
import {$JsonGrammar, $JsonGrammar$Type} from "packages/blue/endless/jankson/$JsonGrammar"

export class $JsonGrammar$Builder {

constructor()

public "build"(): $JsonGrammar
public "printTrailingCommas"(arg0: boolean): $JsonGrammar$Builder
public "bareSpecialNumerics"(arg0: boolean): $JsonGrammar$Builder
public "printCommas"(arg0: boolean): $JsonGrammar$Builder
public "bareRootObject"(arg0: boolean): $JsonGrammar$Builder
public "printUnquotedKeys"(arg0: boolean): $JsonGrammar$Builder
public "printWhitespace"(arg0: boolean): $JsonGrammar$Builder
public "withComments"(arg0: boolean): $JsonGrammar$Builder
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JsonGrammar$Builder$Type = ($JsonGrammar$Builder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JsonGrammar$Builder_ = $JsonGrammar$Builder$Type;
}}
declare module "packages/blue/endless/jankson/api/$DeserializerFunction" {
import {$Marshaller, $Marshaller$Type} from "packages/blue/endless/jankson/api/$Marshaller"
import {$InternalDeserializerFunction, $InternalDeserializerFunction$Type} from "packages/blue/endless/jankson/impl/serializer/$InternalDeserializerFunction"

export interface $DeserializerFunction<A, B> extends $InternalDeserializerFunction<(B)> {

 "apply"(arg0: A, arg1: $Marshaller$Type): B
 "deserialize"(arg0: any, arg1: $Marshaller$Type): B

(arg0: A, arg1: $Marshaller$Type): B
}

export namespace $DeserializerFunction {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DeserializerFunction$Type<A, B> = ($DeserializerFunction<(A), (B)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DeserializerFunction_<A, B> = $DeserializerFunction$Type<(A), (B)>;
}}
declare module "packages/blue/endless/jankson/impl/serializer/$InternalDeserializerFunction" {
import {$Marshaller, $Marshaller$Type} from "packages/blue/endless/jankson/api/$Marshaller"

export interface $InternalDeserializerFunction<B> {

 "deserialize"(arg0: any, arg1: $Marshaller$Type): B

(arg0: any, arg1: $Marshaller$Type): B
}

export namespace $InternalDeserializerFunction {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InternalDeserializerFunction$Type<B> = ($InternalDeserializerFunction<(B)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InternalDeserializerFunction_<B> = $InternalDeserializerFunction$Type<(B)>;
}}

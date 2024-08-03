declare module "packages/java/util/$RandomAccess" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $RandomAccess {

}

export namespace $RandomAccess {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RandomAccess$Type = ($RandomAccess);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RandomAccess_ = $RandomAccess$Type;
}}
declare module "packages/java/util/concurrent/$ConcurrentHashMap$KeySetView" {
import {$Serializable, $Serializable$Type} from "packages/java/io/$Serializable"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$ConcurrentHashMap$CollectionView, $ConcurrentHashMap$CollectionView$Type} from "packages/java/util/concurrent/$ConcurrentHashMap$CollectionView"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Iterator, $Iterator$Type} from "packages/java/util/$Iterator"
import {$Spliterator, $Spliterator$Type} from "packages/java/util/$Spliterator"

export class $ConcurrentHashMap$KeySetView<K, V> extends $ConcurrentHashMap$CollectionView<(K), (V), (K)> implements $Set<(K)>, $Serializable {


public "add"(arg0: K): boolean
public "remove"(arg0: any): boolean
public "equals"(arg0: any): boolean
public "hashCode"(): integer
public "iterator"(): $Iterator<(K)>
public "contains"(arg0: any): boolean
public "spliterator"(): $Spliterator<(K)>
public "addAll"(arg0: $Collection$Type<(any)>): boolean
public "forEach"(arg0: $Consumer$Type<(any)>): void
public "getMappedValue"(): V
public static "copyOf"<E>(arg0: $Collection$Type<(any)>): $Set<(K)>
public "clear"(): void
public "isEmpty"(): boolean
public "size"(): integer
public "toArray"<T>(arg0: (T)[]): (T)[]
public "toArray"(): (any)[]
public static "of"<E>(arg0: K, arg1: K, arg2: K, arg3: K, arg4: K, arg5: K): $Set<(K)>
public static "of"<E>(arg0: K, arg1: K, arg2: K, arg3: K, arg4: K): $Set<(K)>
public static "of"<E>(arg0: K): $Set<(K)>
public static "of"<E>(arg0: K, arg1: K, arg2: K, arg3: K): $Set<(K)>
public static "of"<E>(arg0: K, arg1: K, arg2: K, arg3: K, arg4: K, arg5: K, arg6: K, arg7: K, arg8: K, arg9: K): $Set<(K)>
public static "of"<E>(arg0: K, arg1: K, arg2: K, arg3: K, arg4: K, arg5: K, arg6: K, arg7: K, arg8: K): $Set<(K)>
public static "of"<E>(arg0: K, arg1: K, arg2: K, arg3: K, arg4: K, arg5: K, arg6: K, arg7: K): $Set<(K)>
public static "of"<E>(arg0: K, arg1: K, arg2: K, arg3: K, arg4: K, arg5: K, arg6: K): $Set<(K)>
public static "of"<E>(...arg0: (K)[]): $Set<(K)>
public static "of"<E>(arg0: K, arg1: K, arg2: K): $Set<(K)>
public static "of"<E>(): $Set<(K)>
public static "of"<E>(arg0: K, arg1: K): $Set<(K)>
public "retainAll"(arg0: $Collection$Type<(any)>): boolean
public "containsAll"(arg0: $Collection$Type<(any)>): boolean
get "mappedValue"(): V
get "empty"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ConcurrentHashMap$KeySetView$Type<K, V> = ($ConcurrentHashMap$KeySetView<(K), (V)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ConcurrentHashMap$KeySetView_<K, V> = $ConcurrentHashMap$KeySetView$Type<(K), (V)>;
}}
declare module "packages/java/util/$ResourceBundle" {
import {$ResourceBundle$Control, $ResourceBundle$Control$Type} from "packages/java/util/$ResourceBundle$Control"
import {$Module, $Module$Type} from "packages/java/lang/$Module"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$Enumeration, $Enumeration$Type} from "packages/java/util/$Enumeration"
import {$ClassLoader, $ClassLoader$Type} from "packages/java/lang/$ClassLoader"
import {$Locale, $Locale$Type} from "packages/java/util/$Locale"

export class $ResourceBundle {

constructor()

public "containsKey"(arg0: string): boolean
public "keySet"(): $Set<(string)>
public "getObject"(arg0: string): any
public "getStringArray"(arg0: string): (string)[]
public "getString"(arg0: string): string
public "getLocale"(): $Locale
public "getKeys"(): $Enumeration<(string)>
public "getBaseBundleName"(): string
public static "getBundle"(arg0: string, arg1: $Module$Type): $ResourceBundle
public static "getBundle"(arg0: string, arg1: $Locale$Type): $ResourceBundle
public static "getBundle"(arg0: string, arg1: $ResourceBundle$Control$Type): $ResourceBundle
public static "getBundle"(arg0: string): $ResourceBundle
public static "getBundle"(arg0: string, arg1: $Locale$Type, arg2: $ClassLoader$Type, arg3: $ResourceBundle$Control$Type): $ResourceBundle
public static "getBundle"(arg0: string, arg1: $Locale$Type, arg2: $ResourceBundle$Control$Type): $ResourceBundle
public static "getBundle"(arg0: string, arg1: $Locale$Type, arg2: $ClassLoader$Type): $ResourceBundle
public static "getBundle"(arg0: string, arg1: $Locale$Type, arg2: $Module$Type): $ResourceBundle
public static "clearCache"(): void
public static "clearCache"(arg0: $ClassLoader$Type): void
get "locale"(): $Locale
get "keys"(): $Enumeration<(string)>
get "baseBundleName"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ResourceBundle$Type = ($ResourceBundle);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ResourceBundle_ = $ResourceBundle$Type;
}}
declare module "packages/java/util/zip/$ZipEntry" {
import {$FileTime, $FileTime$Type} from "packages/java/nio/file/attribute/$FileTime"
import {$Cloneable, $Cloneable$Type} from "packages/java/lang/$Cloneable"
import {$LocalDateTime, $LocalDateTime$Type} from "packages/java/time/$LocalDateTime"
import {$ZipConstants, $ZipConstants$Type} from "packages/java/util/zip/$ZipConstants"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $ZipEntry implements $ZipConstants, $Cloneable {
static readonly "STORED": integer
static readonly "DEFLATED": integer

constructor(arg0: string)
constructor(arg0: $ZipEntry$Type)

public "getName"(): string
public "toString"(): string
public "hashCode"(): integer
public "clone"(): any
public "getMethod"(): integer
public "getSize"(): long
public "isDirectory"(): boolean
public "getLastModifiedTime"(): $FileTime
public "setLastModifiedTime"(arg0: $FileTime$Type): $ZipEntry
public "getTime"(): long
public "setTime"(arg0: long): void
public "setTimeLocal"(arg0: $LocalDateTime$Type): void
public "getTimeLocal"(): $LocalDateTime
public "setLastAccessTime"(arg0: $FileTime$Type): $ZipEntry
public "getLastAccessTime"(): $FileTime
public "setCreationTime"(arg0: $FileTime$Type): $ZipEntry
public "getCreationTime"(): $FileTime
public "setSize"(arg0: long): void
public "getCompressedSize"(): long
public "setCompressedSize"(arg0: long): void
public "setCrc"(arg0: long): void
public "getCrc"(): long
public "setMethod"(arg0: integer): void
public "setExtra"(arg0: (byte)[]): void
public "getExtra"(): (byte)[]
public "setComment"(arg0: string): void
public "getComment"(): string
get "name"(): string
get "method"(): integer
get "size"(): long
get "directory"(): boolean
get "lastModifiedTime"(): $FileTime
set "lastModifiedTime"(value: $FileTime$Type)
get "time"(): long
set "time"(value: long)
set "timeLocal"(value: $LocalDateTime$Type)
get "timeLocal"(): $LocalDateTime
set "lastAccessTime"(value: $FileTime$Type)
get "lastAccessTime"(): $FileTime
set "creationTime"(value: $FileTime$Type)
get "creationTime"(): $FileTime
set "size"(value: long)
get "compressedSize"(): long
set "compressedSize"(value: long)
set "crc"(value: long)
get "crc"(): long
set "method"(value: integer)
set "extra"(value: (byte)[])
get "extra"(): (byte)[]
set "comment"(value: string)
get "comment"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ZipEntry$Type = ($ZipEntry);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ZipEntry_ = $ZipEntry$Type;
}}
declare module "packages/java/util/concurrent/$RejectedExecutionHandler" {
import {$ThreadPoolExecutor, $ThreadPoolExecutor$Type} from "packages/java/util/concurrent/$ThreadPoolExecutor"
import {$Runnable, $Runnable$Type} from "packages/java/lang/$Runnable"

export interface $RejectedExecutionHandler {

 "rejectedExecution"(arg0: $Runnable$Type, arg1: $ThreadPoolExecutor$Type): void

(arg0: $Runnable$Type, arg1: $ThreadPoolExecutor$Type): void
}

export namespace $RejectedExecutionHandler {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RejectedExecutionHandler$Type = ($RejectedExecutionHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RejectedExecutionHandler_ = $RejectedExecutionHandler$Type;
}}
declare module "packages/java/util/$Locale$Category" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $Locale$Category extends $Enum<($Locale$Category)> {
static readonly "DISPLAY": $Locale$Category
static readonly "FORMAT": $Locale$Category


public static "values"(): ($Locale$Category)[]
public static "valueOf"(arg0: string): $Locale$Category
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Locale$Category$Type = (("display") | ("format")) | ($Locale$Category);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Locale$Category_ = $Locale$Category$Type;
}}
declare module "packages/java/util/regex/$MatchResult" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $MatchResult {

 "group"(): string
 "group"(arg0: integer): string
 "start"(arg0: integer): integer
 "start"(): integer
 "end"(arg0: integer): integer
 "end"(): integer
 "groupCount"(): integer
}

export namespace $MatchResult {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MatchResult$Type = ($MatchResult);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MatchResult_ = $MatchResult$Type;
}}
declare module "packages/java/util/$PrimitiveIterator" {
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$Iterator, $Iterator$Type} from "packages/java/util/$Iterator"

export interface $PrimitiveIterator<T, T_CONS> extends $Iterator<(T)> {

 "forEachRemaining"(arg0: T_CONS): void
 "remove"(): void
 "forEachRemaining"(arg0: $Consumer$Type<(any)>): void
 "hasNext"(): boolean
 "next"(): T
}

export namespace $PrimitiveIterator {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PrimitiveIterator$Type<T, T_CONS> = ($PrimitiveIterator<(T), (T_CONS)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PrimitiveIterator_<T, T_CONS> = $PrimitiveIterator$Type<(T), (T_CONS)>;
}}
declare module "packages/java/util/$EventObject" {
import {$Serializable, $Serializable$Type} from "packages/java/io/$Serializable"

export class $EventObject implements $Serializable {

constructor(arg0: any)

public "toString"(): string
public "getSource"(): any
get "source"(): any
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EventObject$Type = ($EventObject);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EventObject_ = $EventObject$Type;
}}
declare module "packages/java/util/zip/$ZipFile" {
import {$Closeable, $Closeable$Type} from "packages/java/io/$Closeable"
import {$File, $File$Type} from "packages/java/io/$File"
import {$Enumeration, $Enumeration$Type} from "packages/java/util/$Enumeration"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$InputStream, $InputStream$Type} from "packages/java/io/$InputStream"
import {$ZipEntry, $ZipEntry$Type} from "packages/java/util/zip/$ZipEntry"
import {$ZipConstants, $ZipConstants$Type} from "packages/java/util/zip/$ZipConstants"
import {$Charset, $Charset$Type} from "packages/java/nio/charset/$Charset"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $ZipFile implements $ZipConstants, $Closeable {
static readonly "OPEN_READ": integer
static readonly "OPEN_DELETE": integer

constructor(arg0: string, arg1: $Charset$Type)
constructor(arg0: $File$Type, arg1: $Charset$Type)
constructor(arg0: string)
constructor(arg0: $File$Type, arg1: integer)
constructor(arg0: $File$Type)
constructor(arg0: $File$Type, arg1: integer, arg2: $Charset$Type)

public "getName"(): string
public "size"(): integer
public "stream"(): $Stream<(any)>
public "close"(): void
public "entries"(): $Enumeration<(any)>
public "getInputStream"(arg0: $ZipEntry$Type): $InputStream
public "getEntry"(arg0: string): $ZipEntry
public "getComment"(): string
get "name"(): string
get "comment"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ZipFile$Type = ($ZipFile);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ZipFile_ = $ZipFile$Type;
}}
declare module "packages/java/util/stream/$DoubleStream$DoubleMapMultiConsumer" {
import {$DoubleConsumer, $DoubleConsumer$Type} from "packages/java/util/function/$DoubleConsumer"

export interface $DoubleStream$DoubleMapMultiConsumer {

 "accept"(arg0: double, arg1: $DoubleConsumer$Type): void

(arg0: double, arg1: $DoubleConsumer$Type): void
}

export namespace $DoubleStream$DoubleMapMultiConsumer {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DoubleStream$DoubleMapMultiConsumer$Type = ($DoubleStream$DoubleMapMultiConsumer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DoubleStream$DoubleMapMultiConsumer_ = $DoubleStream$DoubleMapMultiConsumer$Type;
}}
declare module "packages/java/util/function/$ToDoubleBiFunction" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $ToDoubleBiFunction<T, U> {

 "applyAsDouble"(arg0: T, arg1: U): double

(arg0: T, arg1: U): double
}

export namespace $ToDoubleBiFunction {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ToDoubleBiFunction$Type<T, U> = ($ToDoubleBiFunction<(T), (U)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ToDoubleBiFunction_<T, U> = $ToDoubleBiFunction$Type<(T), (U)>;
}}
declare module "packages/java/util/concurrent/$ConcurrentHashMap$CollectionView" {
import {$Serializable, $Serializable$Type} from "packages/java/io/$Serializable"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$IntFunction, $IntFunction$Type} from "packages/java/util/function/$IntFunction"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$ConcurrentHashMap, $ConcurrentHashMap$Type} from "packages/java/util/concurrent/$ConcurrentHashMap"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$Iterator, $Iterator$Type} from "packages/java/util/$Iterator"
import {$Spliterator, $Spliterator$Type} from "packages/java/util/$Spliterator"

export class $ConcurrentHashMap$CollectionView<K, V, E> implements $Collection<(E)>, $Serializable {


public "remove"(arg0: any): boolean
public "toString"(): string
public "clear"(): void
public "isEmpty"(): boolean
public "size"(): integer
public "toArray"(): (any)[]
public "toArray"<T>(arg0: (T)[]): (T)[]
public "iterator"(): $Iterator<(E)>
public "contains"(arg0: any): boolean
public "removeAll"(arg0: $Collection$Type<(any)>): boolean
public "retainAll"(arg0: $Collection$Type<(any)>): boolean
public "containsAll"(arg0: $Collection$Type<(any)>): boolean
public "getMap"(): $ConcurrentHashMap<(K), (V)>
public "add"(arg0: E): boolean
public "equals"(arg0: any): boolean
public "hashCode"(): integer
public "toArray"<T>(arg0: $IntFunction$Type<((T)[])>): (T)[]
public "stream"(): $Stream<(E)>
public "spliterator"(): $Spliterator<(E)>
public "addAll"(arg0: $Collection$Type<(any)>): boolean
public "removeIf"(arg0: $Predicate$Type<(any)>): boolean
public "parallelStream"(): $Stream<(E)>
public "forEach"(arg0: $Consumer$Type<(any)>): void
[Symbol.iterator](): IterableIterator<E>;
get "empty"(): boolean
get "map"(): $ConcurrentHashMap<(K), (V)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ConcurrentHashMap$CollectionView$Type<K, V, E> = ($ConcurrentHashMap$CollectionView<(K), (V), (E)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ConcurrentHashMap$CollectionView_<K, V, E> = $ConcurrentHashMap$CollectionView$Type<(K), (V), (E)>;
}}
declare module "packages/java/util/jar/$Attributes" {
import {$BiConsumer, $BiConsumer$Type} from "packages/java/util/function/$BiConsumer"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Attributes$Name, $Attributes$Name$Type} from "packages/java/util/jar/$Attributes$Name"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Cloneable, $Cloneable$Type} from "packages/java/lang/$Cloneable"
import {$BiFunction, $BiFunction$Type} from "packages/java/util/function/$BiFunction"
import {$Map$Entry, $Map$Entry$Type} from "packages/java/util/$Map$Entry"
import {$Map, $Map$Type} from "packages/java/util/$Map"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $Attributes implements $Map<(any), (any)>, $Cloneable {

constructor(arg0: $Attributes$Type)
constructor(arg0: integer)
constructor()

public "remove"(arg0: any): any
public "get"(arg0: any): any
public "put"(arg0: any, arg1: any): any
public "equals"(arg0: any): boolean
public "values"(): $Collection<(any)>
public "hashCode"(): integer
public "clone"(): any
public "clear"(): void
public "getValue"(arg0: string): string
public "getValue"(arg0: $Attributes$Name$Type): string
public "isEmpty"(): boolean
public "size"(): integer
public "entrySet"(): $Set<($Map$Entry<(any), (any)>)>
public "putAll"(arg0: $Map$Type<(any), (any)>): void
public "containsKey"(arg0: any): boolean
public "keySet"(): $Set<(any)>
public "containsValue"(arg0: any): boolean
public "putValue"(arg0: string, arg1: string): string
public "remove"(arg0: any, arg1: any): boolean
public static "copyOf"<K, V>(arg0: $Map$Type<(any), (any)>): $Map<(any), (any)>
public "replace"(arg0: any, arg1: any): any
public "replace"(arg0: any, arg1: any, arg2: any): boolean
public "replaceAll"(arg0: $BiFunction$Type<(any), (any), (any)>): void
public static "of"<K, V>(arg0: any, arg1: any, arg2: any, arg3: any, arg4: any, arg5: any, arg6: any, arg7: any, arg8: any, arg9: any): $Map<(any), (any)>
public static "of"<K, V>(arg0: any, arg1: any, arg2: any, arg3: any, arg4: any, arg5: any, arg6: any, arg7: any): $Map<(any), (any)>
public static "of"<K, V>(arg0: any, arg1: any, arg2: any, arg3: any, arg4: any, arg5: any): $Map<(any), (any)>
public static "of"<K, V>(): $Map<(any), (any)>
public static "of"<K, V>(arg0: any, arg1: any, arg2: any, arg3: any): $Map<(any), (any)>
public static "of"<K, V>(arg0: any, arg1: any): $Map<(any), (any)>
public static "of"<K, V>(arg0: any, arg1: any, arg2: any, arg3: any, arg4: any, arg5: any, arg6: any, arg7: any, arg8: any, arg9: any, arg10: any, arg11: any, arg12: any, arg13: any, arg14: any, arg15: any, arg16: any, arg17: any, arg18: any, arg19: any): $Map<(any), (any)>
public static "of"<K, V>(arg0: any, arg1: any, arg2: any, arg3: any, arg4: any, arg5: any, arg6: any, arg7: any, arg8: any, arg9: any, arg10: any, arg11: any, arg12: any, arg13: any, arg14: any, arg15: any, arg16: any, arg17: any): $Map<(any), (any)>
public static "of"<K, V>(arg0: any, arg1: any, arg2: any, arg3: any, arg4: any, arg5: any, arg6: any, arg7: any, arg8: any, arg9: any, arg10: any, arg11: any, arg12: any, arg13: any, arg14: any, arg15: any): $Map<(any), (any)>
public static "of"<K, V>(arg0: any, arg1: any, arg2: any, arg3: any, arg4: any, arg5: any, arg6: any, arg7: any, arg8: any, arg9: any, arg10: any, arg11: any, arg12: any, arg13: any): $Map<(any), (any)>
public static "of"<K, V>(arg0: any, arg1: any, arg2: any, arg3: any, arg4: any, arg5: any, arg6: any, arg7: any, arg8: any, arg9: any, arg10: any, arg11: any): $Map<(any), (any)>
public "merge"(arg0: any, arg1: any, arg2: $BiFunction$Type<(any), (any), (any)>): any
public "putIfAbsent"(arg0: any, arg1: any): any
public "compute"(arg0: any, arg1: $BiFunction$Type<(any), (any), (any)>): any
public static "entry"<K, V>(arg0: any, arg1: any): $Map$Entry<(any), (any)>
public "forEach"(arg0: $BiConsumer$Type<(any), (any)>): void
public "computeIfAbsent"(arg0: any, arg1: $Function$Type<(any), (any)>): any
public "getOrDefault"(arg0: any, arg1: any): any
public "computeIfPresent"(arg0: any, arg1: $BiFunction$Type<(any), (any), (any)>): any
public static "ofEntries"<K, V>(...arg0: ($Map$Entry$Type<(any), (any)>)[]): $Map<(any), (any)>
[index: string | number]: any
get "empty"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Attributes$Type = ($Attributes);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Attributes_ = $Attributes$Type;
}}
declare module "packages/java/util/function/$ToDoubleFunction" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $ToDoubleFunction<T> {

 "applyAsDouble"(arg0: T): double

(arg0: T): double
}

export namespace $ToDoubleFunction {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ToDoubleFunction$Type<T> = ($ToDoubleFunction<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ToDoubleFunction_<T> = $ToDoubleFunction$Type<(T)>;
}}
declare module "packages/java/util/function/$BiPredicate" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $BiPredicate<T, U> {

 "test"(arg0: T, arg1: U): boolean
 "or"(arg0: $BiPredicate$Type<(any), (any)>): $BiPredicate<(T), (U)>
 "negate"(): $BiPredicate<(T), (U)>
 "and"(arg0: $BiPredicate$Type<(any), (any)>): $BiPredicate<(T), (U)>

(arg0: T, arg1: U): boolean
}

export namespace $BiPredicate {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BiPredicate$Type<T, U> = ($BiPredicate<(T), (U)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BiPredicate_<T, U> = $BiPredicate$Type<(T), (U)>;
}}
declare module "packages/java/util/stream/$Collectors" {
import {$Comparator, $Comparator$Type} from "packages/java/util/$Comparator"
import {$ConcurrentMap, $ConcurrentMap$Type} from "packages/java/util/concurrent/$ConcurrentMap"
import {$ToIntFunction, $ToIntFunction$Type} from "packages/java/util/function/$ToIntFunction"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$Collector, $Collector$Type} from "packages/java/util/stream/$Collector"
import {$LongSummaryStatistics, $LongSummaryStatistics$Type} from "packages/java/util/$LongSummaryStatistics"
import {$IntSummaryStatistics, $IntSummaryStatistics$Type} from "packages/java/util/$IntSummaryStatistics"
import {$BiFunction, $BiFunction$Type} from "packages/java/util/function/$BiFunction"
import {$BinaryOperator, $BinaryOperator$Type} from "packages/java/util/function/$BinaryOperator"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$List, $List$Type} from "packages/java/util/$List"
import {$DoubleSummaryStatistics, $DoubleSummaryStatistics$Type} from "packages/java/util/$DoubleSummaryStatistics"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$ToLongFunction, $ToLongFunction$Type} from "packages/java/util/function/$ToLongFunction"
import {$ToDoubleFunction, $ToDoubleFunction$Type} from "packages/java/util/function/$ToDoubleFunction"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $Collectors {


public static "joining"(): $Collector<(charseq), (any), (string)>
public static "joining"(arg0: charseq, arg1: charseq, arg2: charseq): $Collector<(charseq), (any), (string)>
public static "joining"(arg0: charseq): $Collector<(charseq), (any), (string)>
public static "toList"<T>(): $Collector<(T), (any), ($List<(T)>)>
public static "toMap"<T, K, U>(arg0: $Function$Type<(any), (any)>, arg1: $Function$Type<(any), (any)>): $Collector<(T), (any), ($Map<(K), (U)>)>
public static "toMap"<T, K, U>(arg0: $Function$Type<(any), (any)>, arg1: $Function$Type<(any), (any)>, arg2: $BinaryOperator$Type<(U)>): $Collector<(T), (any), ($Map<(K), (U)>)>
public static "toMap"<T, K, U, M extends $Map<(K), (U)>>(arg0: $Function$Type<(any), (any)>, arg1: $Function$Type<(any), (any)>, arg2: $BinaryOperator$Type<(U)>, arg3: $Supplier$Type<(M)>): $Collector<(T), (any), (M)>
public static "toSet"<T>(): $Collector<(T), (any), ($Set<(T)>)>
public static "mapping"<T, U, A, R>(arg0: $Function$Type<(any), (any)>, arg1: $Collector$Type<(any), (A), (R)>): $Collector<(T), (any), (R)>
public static "groupingBy"<T, K>(arg0: $Function$Type<(any), (any)>): $Collector<(T), (any), ($Map<(K), ($List<(T)>)>)>
public static "groupingBy"<T, K, A, D>(arg0: $Function$Type<(any), (any)>, arg1: $Collector$Type<(any), (A), (D)>): $Collector<(T), (any), ($Map<(K), (D)>)>
public static "groupingBy"<T, K, D, A, M extends $Map<(K), (D)>>(arg0: $Function$Type<(any), (any)>, arg1: $Supplier$Type<(M)>, arg2: $Collector$Type<(any), (A), (D)>): $Collector<(T), (any), (M)>
public static "minBy"<T>(arg0: $Comparator$Type<(any)>): $Collector<(T), (any), ($Optional<(T)>)>
public static "maxBy"<T>(arg0: $Comparator$Type<(any)>): $Collector<(T), (any), ($Optional<(T)>)>
public static "toCollection"<T, C extends $Collection<(T)>>(arg0: $Supplier$Type<(C)>): $Collector<(T), (any), (C)>
public static "summingLong"<T>(arg0: $ToLongFunction$Type<(any)>): $Collector<(T), (any), (long)>
public static "reducing"<T, U>(arg0: U, arg1: $Function$Type<(any), (any)>, arg2: $BinaryOperator$Type<(U)>): $Collector<(T), (any), (U)>
public static "reducing"<T>(arg0: $BinaryOperator$Type<(T)>): $Collector<(T), (any), ($Optional<(T)>)>
public static "reducing"<T>(arg0: T, arg1: $BinaryOperator$Type<(T)>): $Collector<(T), (any), (T)>
public static "groupingByConcurrent"<T, K, A, D>(arg0: $Function$Type<(any), (any)>, arg1: $Collector$Type<(any), (A), (D)>): $Collector<(T), (any), ($ConcurrentMap<(K), (D)>)>
public static "groupingByConcurrent"<T, K, A, D, M extends $ConcurrentMap<(K), (D)>>(arg0: $Function$Type<(any), (any)>, arg1: $Supplier$Type<(M)>, arg2: $Collector$Type<(any), (A), (D)>): $Collector<(T), (any), (M)>
public static "groupingByConcurrent"<T, K>(arg0: $Function$Type<(any), (any)>): $Collector<(T), (any), ($ConcurrentMap<(K), ($List<(T)>)>)>
public static "partitioningBy"<T>(arg0: $Predicate$Type<(any)>): $Collector<(T), (any), ($Map<(boolean), ($List<(T)>)>)>
public static "partitioningBy"<T, D, A>(arg0: $Predicate$Type<(any)>, arg1: $Collector$Type<(any), (A), (D)>): $Collector<(T), (any), ($Map<(boolean), (D)>)>
public static "collectingAndThen"<T, A, R, RR>(arg0: $Collector$Type<(T), (A), (R)>, arg1: $Function$Type<(R), (RR)>): $Collector<(T), (A), (RR)>
public static "toConcurrentMap"<T, K, U>(arg0: $Function$Type<(any), (any)>, arg1: $Function$Type<(any), (any)>): $Collector<(T), (any), ($ConcurrentMap<(K), (U)>)>
public static "toConcurrentMap"<T, K, U, M extends $ConcurrentMap<(K), (U)>>(arg0: $Function$Type<(any), (any)>, arg1: $Function$Type<(any), (any)>, arg2: $BinaryOperator$Type<(U)>, arg3: $Supplier$Type<(M)>): $Collector<(T), (any), (M)>
public static "toConcurrentMap"<T, K, U>(arg0: $Function$Type<(any), (any)>, arg1: $Function$Type<(any), (any)>, arg2: $BinaryOperator$Type<(U)>): $Collector<(T), (any), ($ConcurrentMap<(K), (U)>)>
public static "toUnmodifiableList"<T>(): $Collector<(T), (any), ($List<(T)>)>
public static "toUnmodifiableSet"<T>(): $Collector<(T), (any), ($Set<(T)>)>
public static "flatMapping"<T, U, A, R>(arg0: $Function$Type<(any), (any)>, arg1: $Collector$Type<(any), (A), (R)>): $Collector<(T), (any), (R)>
public static "filtering"<T, A, R>(arg0: $Predicate$Type<(any)>, arg1: $Collector$Type<(any), (A), (R)>): $Collector<(T), (any), (R)>
public static "counting"<T>(): $Collector<(T), (any), (long)>
public static "summingInt"<T>(arg0: $ToIntFunction$Type<(any)>): $Collector<(T), (any), (integer)>
public static "summingDouble"<T>(arg0: $ToDoubleFunction$Type<(any)>): $Collector<(T), (any), (double)>
public static "averagingInt"<T>(arg0: $ToIntFunction$Type<(any)>): $Collector<(T), (any), (double)>
public static "averagingLong"<T>(arg0: $ToLongFunction$Type<(any)>): $Collector<(T), (any), (double)>
public static "averagingDouble"<T>(arg0: $ToDoubleFunction$Type<(any)>): $Collector<(T), (any), (double)>
public static "toUnmodifiableMap"<T, K, U>(arg0: $Function$Type<(any), (any)>, arg1: $Function$Type<(any), (any)>): $Collector<(T), (any), ($Map<(K), (U)>)>
public static "toUnmodifiableMap"<T, K, U>(arg0: $Function$Type<(any), (any)>, arg1: $Function$Type<(any), (any)>, arg2: $BinaryOperator$Type<(U)>): $Collector<(T), (any), ($Map<(K), (U)>)>
public static "summarizingInt"<T>(arg0: $ToIntFunction$Type<(any)>): $Collector<(T), (any), ($IntSummaryStatistics)>
public static "summarizingLong"<T>(arg0: $ToLongFunction$Type<(any)>): $Collector<(T), (any), ($LongSummaryStatistics)>
public static "summarizingDouble"<T>(arg0: $ToDoubleFunction$Type<(any)>): $Collector<(T), (any), ($DoubleSummaryStatistics)>
public static "teeing"<T, R1, R2, R>(arg0: $Collector$Type<(any), (any), (R1)>, arg1: $Collector$Type<(any), (any), (R2)>, arg2: $BiFunction$Type<(any), (any), (R)>): $Collector<(T), (any), (R)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Collectors$Type = ($Collectors);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Collectors_ = $Collectors$Type;
}}
declare module "packages/java/util/concurrent/locks/$ReadWriteLock" {
import {$Lock, $Lock$Type} from "packages/java/util/concurrent/locks/$Lock"

export interface $ReadWriteLock {

 "readLock"(): $Lock
 "writeLock"(): $Lock
}

export namespace $ReadWriteLock {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ReadWriteLock$Type = ($ReadWriteLock);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ReadWriteLock_ = $ReadWriteLock$Type;
}}
declare module "packages/java/util/$Set" {
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$IntFunction, $IntFunction$Type} from "packages/java/util/function/$IntFunction"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$Iterator, $Iterator$Type} from "packages/java/util/$Iterator"
import {$Spliterator, $Spliterator$Type} from "packages/java/util/$Spliterator"

export interface $Set<E> extends $Collection<(E)> {

 "add"(arg0: E): boolean
 "remove"(arg0: any): boolean
 "equals"(arg0: any): boolean
 "hashCode"(): integer
 "clear"(): void
 "isEmpty"(): boolean
 "size"(): integer
 "toArray"<T>(arg0: (T)[]): (T)[]
 "toArray"(): (any)[]
 "iterator"(): $Iterator<(E)>
 "contains"(arg0: any): boolean
 "spliterator"(): $Spliterator<(E)>
 "addAll"(arg0: $Collection$Type<(any)>): boolean
 "removeAll"(arg0: $Collection$Type<(any)>): boolean
 "retainAll"(arg0: $Collection$Type<(any)>): boolean
 "containsAll"(arg0: $Collection$Type<(any)>): boolean
 "toArray"<T>(arg0: $IntFunction$Type<((T)[])>): (T)[]
 "stream"(): $Stream<(E)>
 "removeIf"(arg0: $Predicate$Type<(any)>): boolean
 "parallelStream"(): $Stream<(E)>
 "forEach"(arg0: $Consumer$Type<(any)>): void
}

export namespace $Set {
function copyOf<E>(arg0: $Collection$Type<(any)>): $Set<(E)>
function of<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E, arg5: E): $Set<(E)>
function of<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E): $Set<(E)>
function of<E>(arg0: E): $Set<(E)>
function of<E>(arg0: E, arg1: E, arg2: E, arg3: E): $Set<(E)>
function of<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E, arg5: E, arg6: E, arg7: E, arg8: E, arg9: E): $Set<(E)>
function of<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E, arg5: E, arg6: E, arg7: E, arg8: E): $Set<(E)>
function of<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E, arg5: E, arg6: E, arg7: E): $Set<(E)>
function of<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E, arg5: E, arg6: E): $Set<(E)>
function of<E>(...arg0: (E)[]): $Set<(E)>
function of<E>(arg0: E, arg1: E, arg2: E): $Set<(E)>
function of<E>(): $Set<(E)>
function of<E>(arg0: E, arg1: E): $Set<(E)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Set$Type<E> = ((E)[]) | ($Set<(E)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Set_<E> = $Set$Type<(E)>;
}}
declare module "packages/java/util/function/$IntToLongFunction" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $IntToLongFunction {

 "applyAsLong"(arg0: integer): long

(arg0: integer): long
}

export namespace $IntToLongFunction {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IntToLongFunction$Type = ($IntToLongFunction);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IntToLongFunction_ = $IntToLongFunction$Type;
}}
declare module "packages/java/util/$Locale$IsoCountryCode" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $Locale$IsoCountryCode extends $Enum<($Locale$IsoCountryCode)> {
static readonly "PART1_ALPHA2": $Locale$IsoCountryCode
static readonly "PART1_ALPHA3": $Locale$IsoCountryCode
static readonly "PART3": $Locale$IsoCountryCode


public static "values"(): ($Locale$IsoCountryCode)[]
public static "valueOf"(arg0: string): $Locale$IsoCountryCode
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Locale$IsoCountryCode$Type = (("part3") | ("part1_alpha2") | ("part1_alpha3")) | ($Locale$IsoCountryCode);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Locale$IsoCountryCode_ = $Locale$IsoCountryCode$Type;
}}
declare module "packages/java/util/function/$ToLongFunction" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $ToLongFunction<T> {

 "applyAsLong"(arg0: T): long

(arg0: T): long
}

export namespace $ToLongFunction {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ToLongFunction$Type<T> = ($ToLongFunction<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ToLongFunction_<T> = $ToLongFunction$Type<(T)>;
}}
declare module "packages/java/util/concurrent/$ForkJoinPool$ForkJoinWorkerThreadFactory" {
import {$ForkJoinWorkerThread, $ForkJoinWorkerThread$Type} from "packages/java/util/concurrent/$ForkJoinWorkerThread"
import {$ForkJoinPool, $ForkJoinPool$Type} from "packages/java/util/concurrent/$ForkJoinPool"

export interface $ForkJoinPool$ForkJoinWorkerThreadFactory {

 "newThread"(arg0: $ForkJoinPool$Type): $ForkJoinWorkerThread

(arg0: $ForkJoinPool$Type): $ForkJoinWorkerThread
}

export namespace $ForkJoinPool$ForkJoinWorkerThreadFactory {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForkJoinPool$ForkJoinWorkerThreadFactory$Type = ($ForkJoinPool$ForkJoinWorkerThreadFactory);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForkJoinPool$ForkJoinWorkerThreadFactory_ = $ForkJoinPool$ForkJoinWorkerThreadFactory$Type;
}}
declare module "packages/java/util/$StringTokenizer" {
import {$Enumeration, $Enumeration$Type} from "packages/java/util/$Enumeration"
import {$Iterator, $Iterator$Type} from "packages/java/util/$Iterator"

export class $StringTokenizer implements $Enumeration<(any)> {

constructor(arg0: string, arg1: string)
constructor(arg0: string, arg1: string, arg2: boolean)
constructor(arg0: string)

public "countTokens"(): integer
public "hasMoreElements"(): boolean
public "nextToken"(arg0: string): string
public "nextToken"(): string
public "nextElement"(): any
public "hasMoreTokens"(): boolean
public "asIterator"(): $Iterator<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StringTokenizer$Type = ($StringTokenizer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StringTokenizer_ = $StringTokenizer$Type;
}}
declare module "packages/java/util/function/$LongPredicate" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $LongPredicate {

 "test"(arg0: long): boolean
 "or"(arg0: $LongPredicate$Type): $LongPredicate
 "negate"(): $LongPredicate
 "and"(arg0: $LongPredicate$Type): $LongPredicate

(arg0: long): boolean
}

export namespace $LongPredicate {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LongPredicate$Type = ($LongPredicate);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LongPredicate_ = $LongPredicate$Type;
}}
declare module "packages/java/util/$AbstractCollection" {
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$IntFunction, $IntFunction$Type} from "packages/java/util/function/$IntFunction"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$Iterator, $Iterator$Type} from "packages/java/util/$Iterator"
import {$Spliterator, $Spliterator$Type} from "packages/java/util/$Spliterator"

export class $AbstractCollection<E> implements $Collection<(E)> {


public "add"(arg0: E): boolean
public "remove"(arg0: any): boolean
public "toString"(): string
public "clear"(): void
public "isEmpty"(): boolean
public "size"(): integer
public "toArray"(): (any)[]
public "toArray"<T>(arg0: (T)[]): (T)[]
public "iterator"(): $Iterator<(E)>
public "contains"(arg0: any): boolean
public "addAll"(arg0: $Collection$Type<(any)>): boolean
public "removeAll"(arg0: $Collection$Type<(any)>): boolean
public "retainAll"(arg0: $Collection$Type<(any)>): boolean
public "containsAll"(arg0: $Collection$Type<(any)>): boolean
public "equals"(arg0: any): boolean
public "hashCode"(): integer
public "toArray"<T>(arg0: $IntFunction$Type<((T)[])>): (T)[]
public "stream"(): $Stream<(E)>
public "spliterator"(): $Spliterator<(E)>
public "removeIf"(arg0: $Predicate$Type<(any)>): boolean
public "parallelStream"(): $Stream<(E)>
public "forEach"(arg0: $Consumer$Type<(any)>): void
[Symbol.iterator](): IterableIterator<E>;
get "empty"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AbstractCollection$Type<E> = ($AbstractCollection<(E)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AbstractCollection_<E> = $AbstractCollection$Type<(E)>;
}}
declare module "packages/java/util/concurrent/atomic/$Striped64" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $Striped64 extends number {


}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Striped64$Type = ($Striped64);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Striped64_ = $Striped64$Type;
}}
declare module "packages/java/util/function/$LongToIntFunction" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $LongToIntFunction {

 "applyAsInt"(arg0: long): integer

(arg0: long): integer
}

export namespace $LongToIntFunction {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LongToIntFunction$Type = ($LongToIntFunction);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LongToIntFunction_ = $LongToIntFunction$Type;
}}
declare module "packages/java/util/function/$ObjLongConsumer" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $ObjLongConsumer<T> {

 "accept"(arg0: T, arg1: long): void

(arg0: T, arg1: long): void
}

export namespace $ObjLongConsumer {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ObjLongConsumer$Type<T> = ($ObjLongConsumer<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ObjLongConsumer_<T> = $ObjLongConsumer$Type<(T)>;
}}
declare module "packages/java/util/$Collection" {
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$IntFunction, $IntFunction$Type} from "packages/java/util/function/$IntFunction"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$Iterator, $Iterator$Type} from "packages/java/util/$Iterator"
import {$Spliterator, $Spliterator$Type} from "packages/java/util/$Spliterator"
import {$Iterable, $Iterable$Type} from "packages/java/lang/$Iterable"

export interface $Collection<E> extends $Iterable<(E)> {

 "add"(arg0: E): boolean
 "remove"(arg0: any): boolean
 "equals"(arg0: any): boolean
 "hashCode"(): integer
 "clear"(): void
 "isEmpty"(): boolean
 "size"(): integer
 "toArray"<T>(arg0: (T)[]): (T)[]
 "toArray"<T>(arg0: $IntFunction$Type<((T)[])>): (T)[]
 "toArray"(): (any)[]
 "iterator"(): $Iterator<(E)>
 "stream"(): $Stream<(E)>
 "contains"(arg0: any): boolean
 "spliterator"(): $Spliterator<(E)>
 "addAll"(arg0: $Collection$Type<(any)>): boolean
 "removeIf"(arg0: $Predicate$Type<(any)>): boolean
 "removeAll"(arg0: $Collection$Type<(any)>): boolean
 "retainAll"(arg0: $Collection$Type<(any)>): boolean
 "containsAll"(arg0: $Collection$Type<(any)>): boolean
 "parallelStream"(): $Stream<(E)>
 "forEach"(arg0: $Consumer$Type<(any)>): void
}

export namespace $Collection {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Collection$Type<E> = ((E)[]) | ($Collection<(E)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Collection_<E> = $Collection$Type<(E)>;
}}
declare module "packages/java/util/concurrent/$AbstractExecutorService" {
import {$ExecutorService, $ExecutorService$Type} from "packages/java/util/concurrent/$ExecutorService"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Future, $Future$Type} from "packages/java/util/concurrent/$Future"
import {$Callable, $Callable$Type} from "packages/java/util/concurrent/$Callable"
import {$List, $List$Type} from "packages/java/util/$List"
import {$TimeUnit, $TimeUnit$Type} from "packages/java/util/concurrent/$TimeUnit"
import {$Runnable, $Runnable$Type} from "packages/java/lang/$Runnable"

export class $AbstractExecutorService implements $ExecutorService {

constructor()

public "submit"<T>(arg0: $Callable$Type<(T)>): $Future<(T)>
public "submit"(arg0: $Runnable$Type): $Future<(any)>
public "submit"<T>(arg0: $Runnable$Type, arg1: T): $Future<(T)>
public "invokeAny"<T>(arg0: $Collection$Type<(any)>): T
public "invokeAny"<T>(arg0: $Collection$Type<(any)>, arg1: long, arg2: $TimeUnit$Type): T
public "invokeAll"<T>(arg0: $Collection$Type<(any)>): $List<($Future<(T)>)>
public "invokeAll"<T>(arg0: $Collection$Type<(any)>, arg1: long, arg2: $TimeUnit$Type): $List<($Future<(T)>)>
public "shutdown"(): void
public "isShutdown"(): boolean
public "shutdownNow"(): $List<($Runnable)>
public "isTerminated"(): boolean
public "awaitTermination"(arg0: long, arg1: $TimeUnit$Type): boolean
public "execute"(arg0: $Runnable$Type): void
get "terminated"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AbstractExecutorService$Type = ($AbstractExecutorService);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AbstractExecutorService_ = $AbstractExecutorService$Type;
}}
declare module "packages/java/util/function/$ObjIntConsumer" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $ObjIntConsumer<T> {

 "accept"(arg0: T, arg1: integer): void

(arg0: T, arg1: integer): void
}

export namespace $ObjIntConsumer {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ObjIntConsumer$Type<T> = ($ObjIntConsumer<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ObjIntConsumer_<T> = $ObjIntConsumer$Type<(T)>;
}}
declare module "packages/java/util/$Date" {
import {$Comparable, $Comparable$Type} from "packages/java/lang/$Comparable"
import {$Serializable, $Serializable$Type} from "packages/java/io/$Serializable"
import {$Instant, $Instant$Type} from "packages/java/time/$Instant"
import {$Cloneable, $Cloneable$Type} from "packages/java/lang/$Cloneable"

export class $Date implements $Serializable, $Cloneable, $Comparable<($Date)> {

/**
 * 
 * @deprecated
 */
constructor(arg0: string)
/**
 * 
 * @deprecated
 */
constructor(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer)
/**
 * 
 * @deprecated
 */
constructor(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer)
constructor()
constructor(arg0: long)
/**
 * 
 * @deprecated
 */
constructor(arg0: integer, arg1: integer, arg2: integer)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "clone"(): any
public "compareTo"(arg0: $Date$Type): integer
public static "from"(arg0: $Instant$Type): $Date
public "before"(arg0: $Date$Type): boolean
public "after"(arg0: $Date$Type): boolean
/**
 * 
 * @deprecated
 */
public static "parse"(arg0: string): long
public "toInstant"(): $Instant
/**
 * 
 * @deprecated
 */
public static "UTC"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer): long
/**
 * 
 * @deprecated
 */
public "getYear"(): integer
public "getTime"(): long
public "setTime"(arg0: long): void
/**
 * 
 * @deprecated
 */
public "getSeconds"(): integer
/**
 * 
 * @deprecated
 */
public "getMonth"(): integer
/**
 * 
 * @deprecated
 */
public "setDate"(arg0: integer): void
/**
 * 
 * @deprecated
 */
public "setMonth"(arg0: integer): void
/**
 * 
 * @deprecated
 */
public "getHours"(): integer
/**
 * 
 * @deprecated
 */
public "setHours"(arg0: integer): void
/**
 * 
 * @deprecated
 */
public "getMinutes"(): integer
/**
 * 
 * @deprecated
 */
public "setMinutes"(arg0: integer): void
/**
 * 
 * @deprecated
 */
public "setSeconds"(arg0: integer): void
/**
 * 
 * @deprecated
 */
public "setYear"(arg0: integer): void
/**
 * 
 * @deprecated
 */
public "getDate"(): integer
/**
 * 
 * @deprecated
 */
public "getDay"(): integer
/**
 * 
 * @deprecated
 */
public "toLocaleString"(): string
/**
 * 
 * @deprecated
 */
public "toGMTString"(): string
/**
 * 
 * @deprecated
 */
public "getTimezoneOffset"(): integer
get "year"(): integer
get "time"(): long
set "time"(value: long)
get "seconds"(): integer
get "month"(): integer
set "date"(value: integer)
set "month"(value: integer)
get "hours"(): integer
set "hours"(value: integer)
get "minutes"(): integer
set "minutes"(value: integer)
set "seconds"(value: integer)
set "year"(value: integer)
get "date"(): integer
get "day"(): integer
get "timezoneOffset"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Date$Type = ($Date);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Date_ = $Date$Type;
}}
declare module "packages/java/util/concurrent/$ForkJoinPool" {
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Thread$UncaughtExceptionHandler, $Thread$UncaughtExceptionHandler$Type} from "packages/java/lang/$Thread$UncaughtExceptionHandler"
import {$Callable, $Callable$Type} from "packages/java/util/concurrent/$Callable"
import {$AbstractExecutorService, $AbstractExecutorService$Type} from "packages/java/util/concurrent/$AbstractExecutorService"
import {$TimeUnit, $TimeUnit$Type} from "packages/java/util/concurrent/$TimeUnit"
import {$Runnable, $Runnable$Type} from "packages/java/lang/$Runnable"
import {$ForkJoinTask, $ForkJoinTask$Type} from "packages/java/util/concurrent/$ForkJoinTask"
import {$ForkJoinPool$ManagedBlocker, $ForkJoinPool$ManagedBlocker$Type} from "packages/java/util/concurrent/$ForkJoinPool$ManagedBlocker"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Future, $Future$Type} from "packages/java/util/concurrent/$Future"
import {$ForkJoinPool$ForkJoinWorkerThreadFactory, $ForkJoinPool$ForkJoinWorkerThreadFactory$Type} from "packages/java/util/concurrent/$ForkJoinPool$ForkJoinWorkerThreadFactory"

export class $ForkJoinPool extends $AbstractExecutorService {
static readonly "defaultForkJoinWorkerThreadFactory": $ForkJoinPool$ForkJoinWorkerThreadFactory

constructor(arg0: integer)
constructor()
constructor(arg0: integer, arg1: $ForkJoinPool$ForkJoinWorkerThreadFactory$Type, arg2: $Thread$UncaughtExceptionHandler$Type, arg3: boolean, arg4: integer, arg5: integer, arg6: integer, arg7: $Predicate$Type<(any)>, arg8: long, arg9: $TimeUnit$Type)
constructor(arg0: integer, arg1: $ForkJoinPool$ForkJoinWorkerThreadFactory$Type, arg2: $Thread$UncaughtExceptionHandler$Type, arg3: boolean)

public "invoke"<T>(arg0: $ForkJoinTask$Type<(T)>): T
public "shutdown"(): void
public "toString"(): string
public "getFactory"(): $ForkJoinPool$ForkJoinWorkerThreadFactory
public "execute"(arg0: $Runnable$Type): void
public "execute"(arg0: $ForkJoinTask$Type<(any)>): void
public "getUncaughtExceptionHandler"(): $Thread$UncaughtExceptionHandler
public static "getCommonPoolParallelism"(): integer
public "isShutdown"(): boolean
public "submit"<T>(arg0: $Runnable$Type, arg1: T): $ForkJoinTask<(T)>
public "submit"<T>(arg0: $Callable$Type<(T)>): $ForkJoinTask<(T)>
public "submit"<T>(arg0: $ForkJoinTask$Type<(T)>): $ForkJoinTask<(T)>
public "submit"(arg0: $Runnable$Type): $ForkJoinTask<(any)>
public "invokeAny"<T>(arg0: $Collection$Type<(any)>): T
public "invokeAny"<T>(arg0: $Collection$Type<(any)>, arg1: long, arg2: $TimeUnit$Type): T
public "invokeAll"<T>(arg0: $Collection$Type<(any)>): $List<($Future<(T)>)>
public "invokeAll"<T>(arg0: $Collection$Type<(any)>, arg1: long, arg2: $TimeUnit$Type): $List<($Future<(T)>)>
public "shutdownNow"(): $List<($Runnable)>
public "isTerminated"(): boolean
public "awaitTermination"(arg0: long, arg1: $TimeUnit$Type): boolean
public "getQueuedTaskCount"(): long
public static "commonPool"(): $ForkJoinPool
public "getParallelism"(): integer
public "getPoolSize"(): integer
public "getAsyncMode"(): boolean
public "getRunningThreadCount"(): integer
public "getActiveThreadCount"(): integer
public "isQuiescent"(): boolean
public "getStealCount"(): long
public "getQueuedSubmissionCount"(): integer
public "hasQueuedSubmissions"(): boolean
public "isTerminating"(): boolean
public "awaitQuiescence"(arg0: long, arg1: $TimeUnit$Type): boolean
public static "managedBlock"(arg0: $ForkJoinPool$ManagedBlocker$Type): void
get "factory"(): $ForkJoinPool$ForkJoinWorkerThreadFactory
get "uncaughtExceptionHandler"(): $Thread$UncaughtExceptionHandler
get "commonPoolParallelism"(): integer
get "terminated"(): boolean
get "queuedTaskCount"(): long
get "parallelism"(): integer
get "poolSize"(): integer
get "asyncMode"(): boolean
get "runningThreadCount"(): integer
get "activeThreadCount"(): integer
get "quiescent"(): boolean
get "stealCount"(): long
get "queuedSubmissionCount"(): integer
get "terminating"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForkJoinPool$Type = ($ForkJoinPool);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForkJoinPool_ = $ForkJoinPool$Type;
}}
declare module "packages/java/util/concurrent/atomic/$AtomicLong" {
import {$Serializable, $Serializable$Type} from "packages/java/io/$Serializable"
import {$LongBinaryOperator, $LongBinaryOperator$Type} from "packages/java/util/function/$LongBinaryOperator"
import {$LongUnaryOperator, $LongUnaryOperator$Type} from "packages/java/util/function/$LongUnaryOperator"

export class $AtomicLong extends number implements $Serializable {

constructor()
constructor(arg0: long)

public "get"(): long
public "toString"(): string
public "intValue"(): integer
public "longValue"(): long
public "floatValue"(): float
public "doubleValue"(): double
public "set"(arg0: long): void
public "getOpaque"(): long
public "setOpaque"(arg0: long): void
public "getAcquire"(): long
public "setRelease"(arg0: long): void
public "compareAndSet"(arg0: long, arg1: long): boolean
public "compareAndExchange"(arg0: long, arg1: long): long
public "compareAndExchangeAcquire"(arg0: long, arg1: long): long
public "compareAndExchangeRelease"(arg0: long, arg1: long): long
public "weakCompareAndSetPlain"(arg0: long, arg1: long): boolean
/**
 * 
 * @deprecated
 */
public "weakCompareAndSet"(arg0: long, arg1: long): boolean
public "weakCompareAndSetAcquire"(arg0: long, arg1: long): boolean
public "weakCompareAndSetRelease"(arg0: long, arg1: long): boolean
public "getAndSet"(arg0: long): long
public "getAndAdd"(arg0: long): long
public "lazySet"(arg0: long): void
public "weakCompareAndSetVolatile"(arg0: long, arg1: long): boolean
public "getAndIncrement"(): long
public "getAndDecrement"(): long
public "incrementAndGet"(): long
public "decrementAndGet"(): long
public "addAndGet"(arg0: long): long
public "getAndUpdate"(arg0: $LongUnaryOperator$Type): long
public "updateAndGet"(arg0: $LongUnaryOperator$Type): long
public "getAndAccumulate"(arg0: long, arg1: $LongBinaryOperator$Type): long
public "accumulateAndGet"(arg0: long, arg1: $LongBinaryOperator$Type): long
public "getPlain"(): long
public "setPlain"(arg0: long): void
get "opaque"(): long
set "opaque"(value: long)
get "acquire"(): long
set "release"(value: long)
get "andIncrement"(): long
get "andDecrement"(): long
get "plain"(): long
set "plain"(value: long)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AtomicLong$Type = ($AtomicLong);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AtomicLong_ = $AtomicLong$Type;
}}
declare module "packages/java/util/function/$BiFunction" {
import {$Function, $Function$Type} from "packages/java/util/function/$Function"

export interface $BiFunction<T, U, R> {

 "apply"(arg0: T, arg1: U): R
 "andThen"<V>(arg0: $Function$Type<(any), (any)>): $BiFunction<(T), (U), (V)>

(arg0: T, arg1: U): R
}

export namespace $BiFunction {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BiFunction$Type<T, U, R> = ($BiFunction<(T), (U), (R)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BiFunction_<T, U, R> = $BiFunction$Type<(T), (U), (R)>;
}}
declare module "packages/java/util/function/$IntBinaryOperator" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $IntBinaryOperator {

 "applyAsInt"(arg0: integer, arg1: integer): integer

(arg0: integer, arg1: integer): integer
}

export namespace $IntBinaryOperator {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IntBinaryOperator$Type = ($IntBinaryOperator);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IntBinaryOperator_ = $IntBinaryOperator$Type;
}}
declare module "packages/java/util/jar/$Attributes$Name" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $Attributes$Name {
static readonly "MANIFEST_VERSION": $Attributes$Name
static readonly "SIGNATURE_VERSION": $Attributes$Name
static readonly "CONTENT_TYPE": $Attributes$Name
static readonly "CLASS_PATH": $Attributes$Name
static readonly "MAIN_CLASS": $Attributes$Name
static readonly "SEALED": $Attributes$Name
static readonly "EXTENSION_LIST": $Attributes$Name
static readonly "EXTENSION_NAME": $Attributes$Name
/**
 * 
 * @deprecated
 */
static readonly "EXTENSION_INSTALLATION": $Attributes$Name
static readonly "IMPLEMENTATION_TITLE": $Attributes$Name
static readonly "IMPLEMENTATION_VERSION": $Attributes$Name
static readonly "IMPLEMENTATION_VENDOR": $Attributes$Name
/**
 * 
 * @deprecated
 */
static readonly "IMPLEMENTATION_VENDOR_ID": $Attributes$Name
/**
 * 
 * @deprecated
 */
static readonly "IMPLEMENTATION_URL": $Attributes$Name
static readonly "SPECIFICATION_TITLE": $Attributes$Name
static readonly "SPECIFICATION_VERSION": $Attributes$Name
static readonly "SPECIFICATION_VENDOR": $Attributes$Name
static readonly "MULTI_RELEASE": $Attributes$Name

constructor(arg0: string)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Attributes$Name$Type = ($Attributes$Name);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Attributes$Name_ = $Attributes$Name$Type;
}}
declare module "packages/java/util/function/$ToIntFunction" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $ToIntFunction<T> {

 "applyAsInt"(arg0: T): integer

(arg0: T): integer
}

export namespace $ToIntFunction {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ToIntFunction$Type<T> = ($ToIntFunction<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ToIntFunction_<T> = $ToIntFunction$Type<(T)>;
}}
declare module "packages/java/util/function/$Consumer" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $Consumer<T> {

 "accept"(arg0: T): void
 "andThen"(arg0: $Consumer$Type<(any)>): $Consumer<(T)>

(arg0: T): void
}

export namespace $Consumer {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Consumer$Type<T> = ($Consumer<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Consumer_<T> = $Consumer$Type<(T)>;
}}
declare module "packages/java/util/concurrent/$ForkJoinWorkerThread" {
import {$Thread, $Thread$Type} from "packages/java/lang/$Thread"
import {$ForkJoinPool, $ForkJoinPool$Type} from "packages/java/util/concurrent/$ForkJoinPool"

export class $ForkJoinWorkerThread extends $Thread {
static readonly "MIN_PRIORITY": integer
static readonly "NORM_PRIORITY": integer
static readonly "MAX_PRIORITY": integer


public "run"(): void
public "getPool"(): $ForkJoinPool
public "getPoolIndex"(): integer
get "pool"(): $ForkJoinPool
get "poolIndex"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForkJoinWorkerThread$Type = ($ForkJoinWorkerThread);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForkJoinWorkerThread_ = $ForkJoinWorkerThread$Type;
}}
declare module "packages/java/util/concurrent/$CompletionStage" {
import {$BiConsumer, $BiConsumer$Type} from "packages/java/util/function/$BiConsumer"
import {$Throwable, $Throwable$Type} from "packages/java/lang/$Throwable"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$CompletableFuture, $CompletableFuture$Type} from "packages/java/util/concurrent/$CompletableFuture"
import {$Executor, $Executor$Type} from "packages/java/util/concurrent/$Executor"
import {$BiFunction, $BiFunction$Type} from "packages/java/util/function/$BiFunction"
import {$Runnable, $Runnable$Type} from "packages/java/lang/$Runnable"

export interface $CompletionStage<T> {

 "handle"<U>(arg0: $BiFunction$Type<(any), ($Throwable$Type), (any)>): $CompletionStage<(U)>
 "toCompletableFuture"(): $CompletableFuture<(T)>
 "exceptionallyComposeAsync"(arg0: $Function$Type<($Throwable$Type), (any)>): $CompletionStage<(T)>
 "exceptionallyComposeAsync"(arg0: $Function$Type<($Throwable$Type), (any)>, arg1: $Executor$Type): $CompletionStage<(T)>
 "exceptionallyCompose"(arg0: $Function$Type<($Throwable$Type), (any)>): $CompletionStage<(T)>
 "exceptionallyAsync"(arg0: $Function$Type<($Throwable$Type), (any)>, arg1: $Executor$Type): $CompletionStage<(T)>
 "exceptionallyAsync"(arg0: $Function$Type<($Throwable$Type), (any)>): $CompletionStage<(T)>
 "exceptionally"(arg0: $Function$Type<($Throwable$Type), (any)>): $CompletionStage<(T)>
 "whenCompleteAsync"(arg0: $BiConsumer$Type<(any), (any)>): $CompletionStage<(T)>
 "whenCompleteAsync"(arg0: $BiConsumer$Type<(any), (any)>, arg1: $Executor$Type): $CompletionStage<(T)>
 "handleAsync"<U>(arg0: $BiFunction$Type<(any), ($Throwable$Type), (any)>, arg1: $Executor$Type): $CompletionStage<(U)>
 "handleAsync"<U>(arg0: $BiFunction$Type<(any), ($Throwable$Type), (any)>): $CompletionStage<(U)>
 "thenComposeAsync"<U>(arg0: $Function$Type<(any), (any)>, arg1: $Executor$Type): $CompletionStage<(U)>
 "thenComposeAsync"<U>(arg0: $Function$Type<(any), (any)>): $CompletionStage<(U)>
 "thenCompose"<U>(arg0: $Function$Type<(any), (any)>): $CompletionStage<(U)>
 "runAfterEitherAsync"(arg0: $CompletionStage$Type<(any)>, arg1: $Runnable$Type): $CompletionStage<(void)>
 "runAfterEitherAsync"(arg0: $CompletionStage$Type<(any)>, arg1: $Runnable$Type, arg2: $Executor$Type): $CompletionStage<(void)>
 "runAfterEither"(arg0: $CompletionStage$Type<(any)>, arg1: $Runnable$Type): $CompletionStage<(void)>
 "acceptEitherAsync"(arg0: $CompletionStage$Type<(any)>, arg1: $Consumer$Type<(any)>): $CompletionStage<(void)>
 "acceptEitherAsync"(arg0: $CompletionStage$Type<(any)>, arg1: $Consumer$Type<(any)>, arg2: $Executor$Type): $CompletionStage<(void)>
 "acceptEither"(arg0: $CompletionStage$Type<(any)>, arg1: $Consumer$Type<(any)>): $CompletionStage<(void)>
 "applyToEitherAsync"<U>(arg0: $CompletionStage$Type<(any)>, arg1: $Function$Type<(any), (U)>, arg2: $Executor$Type): $CompletionStage<(U)>
 "applyToEitherAsync"<U>(arg0: $CompletionStage$Type<(any)>, arg1: $Function$Type<(any), (U)>): $CompletionStage<(U)>
 "applyToEither"<U>(arg0: $CompletionStage$Type<(any)>, arg1: $Function$Type<(any), (U)>): $CompletionStage<(U)>
 "runAfterBothAsync"(arg0: $CompletionStage$Type<(any)>, arg1: $Runnable$Type, arg2: $Executor$Type): $CompletionStage<(void)>
 "runAfterBothAsync"(arg0: $CompletionStage$Type<(any)>, arg1: $Runnable$Type): $CompletionStage<(void)>
 "runAfterBoth"(arg0: $CompletionStage$Type<(any)>, arg1: $Runnable$Type): $CompletionStage<(void)>
 "thenAcceptBothAsync"<U>(arg0: $CompletionStage$Type<(any)>, arg1: $BiConsumer$Type<(any), (any)>, arg2: $Executor$Type): $CompletionStage<(void)>
 "thenAcceptBothAsync"<U>(arg0: $CompletionStage$Type<(any)>, arg1: $BiConsumer$Type<(any), (any)>): $CompletionStage<(void)>
 "thenAcceptBoth"<U>(arg0: $CompletionStage$Type<(any)>, arg1: $BiConsumer$Type<(any), (any)>): $CompletionStage<(void)>
 "thenCombineAsync"<U, V>(arg0: $CompletionStage$Type<(any)>, arg1: $BiFunction$Type<(any), (any), (any)>, arg2: $Executor$Type): $CompletionStage<(V)>
 "thenCombineAsync"<U, V>(arg0: $CompletionStage$Type<(any)>, arg1: $BiFunction$Type<(any), (any), (any)>): $CompletionStage<(V)>
 "thenCombine"<U, V>(arg0: $CompletionStage$Type<(any)>, arg1: $BiFunction$Type<(any), (any), (any)>): $CompletionStage<(V)>
 "thenRunAsync"(arg0: $Runnable$Type): $CompletionStage<(void)>
 "thenRunAsync"(arg0: $Runnable$Type, arg1: $Executor$Type): $CompletionStage<(void)>
 "thenRun"(arg0: $Runnable$Type): $CompletionStage<(void)>
 "thenAcceptAsync"(arg0: $Consumer$Type<(any)>): $CompletionStage<(void)>
 "thenAcceptAsync"(arg0: $Consumer$Type<(any)>, arg1: $Executor$Type): $CompletionStage<(void)>
 "thenAccept"(arg0: $Consumer$Type<(any)>): $CompletionStage<(void)>
 "thenApplyAsync"<U>(arg0: $Function$Type<(any), (any)>, arg1: $Executor$Type): $CompletionStage<(U)>
 "thenApplyAsync"<U>(arg0: $Function$Type<(any), (any)>): $CompletionStage<(U)>
 "thenApply"<U>(arg0: $Function$Type<(any), (any)>): $CompletionStage<(U)>
 "whenComplete"(arg0: $BiConsumer$Type<(any), (any)>): $CompletionStage<(T)>
}

export namespace $CompletionStage {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CompletionStage$Type<T> = ($CompletionStage<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CompletionStage_<T> = $CompletionStage$Type<(T)>;
}}
declare module "packages/java/util/stream/$IntStream$Builder" {
import {$IntStream, $IntStream$Type} from "packages/java/util/stream/$IntStream"
import {$IntConsumer, $IntConsumer$Type} from "packages/java/util/function/$IntConsumer"

export interface $IntStream$Builder extends $IntConsumer {

 "add"(arg0: integer): $IntStream$Builder
 "accept"(arg0: integer): void
 "build"(): $IntStream
 "andThen"(arg0: $IntConsumer$Type): $IntConsumer
}

export namespace $IntStream$Builder {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IntStream$Builder$Type = ($IntStream$Builder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IntStream$Builder_ = $IntStream$Builder$Type;
}}
declare module "packages/java/util/jar/$Manifest" {
import {$OutputStream, $OutputStream$Type} from "packages/java/io/$OutputStream"
import {$Cloneable, $Cloneable$Type} from "packages/java/lang/$Cloneable"
import {$InputStream, $InputStream$Type} from "packages/java/io/$InputStream"
import {$Attributes, $Attributes$Type} from "packages/java/util/jar/$Attributes"
import {$Map, $Map$Type} from "packages/java/util/$Map"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $Manifest implements $Cloneable {

constructor(arg0: $Manifest$Type)
constructor(arg0: $InputStream$Type)
constructor()

public "equals"(arg0: any): boolean
public "hashCode"(): integer
public "clone"(): any
public "clear"(): void
public "write"(arg0: $OutputStream$Type): void
public "read"(arg0: $InputStream$Type): void
public "getMainAttributes"(): $Attributes
public "getEntries"(): $Map<(string), ($Attributes)>
public "getAttributes"(arg0: string): $Attributes
get "mainAttributes"(): $Attributes
get "entries"(): $Map<(string), ($Attributes)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Manifest$Type = ($Manifest);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Manifest_ = $Manifest$Type;
}}
declare module "packages/java/util/function/$DoubleConsumer" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $DoubleConsumer {

 "accept"(arg0: double): void
 "andThen"(arg0: $DoubleConsumer$Type): $DoubleConsumer

(arg0: double): void
}

export namespace $DoubleConsumer {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DoubleConsumer$Type = ($DoubleConsumer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DoubleConsumer_ = $DoubleConsumer$Type;
}}
declare module "packages/java/util/$LinkedList" {
import {$Serializable, $Serializable$Type} from "packages/java/io/$Serializable"
import {$AbstractSequentialList, $AbstractSequentialList$Type} from "packages/java/util/$AbstractSequentialList"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Cloneable, $Cloneable$Type} from "packages/java/lang/$Cloneable"
import {$Deque, $Deque$Type} from "packages/java/util/$Deque"
import {$ListIterator, $ListIterator$Type} from "packages/java/util/$ListIterator"
import {$Spliterator, $Spliterator$Type} from "packages/java/util/$Spliterator"
import {$Iterator, $Iterator$Type} from "packages/java/util/$Iterator"

export class $LinkedList<E> extends $AbstractSequentialList<(E)> implements $List<(E)>, $Deque<(E)>, $Cloneable, $Serializable {

constructor()
constructor(arg0: $Collection$Type<(any)>)

public "add"(arg0: E): boolean
public "add"(arg0: integer, arg1: E): void
public "remove"(arg0: any): boolean
public "remove"(): E
public "remove"(arg0: integer): E
public "get"(arg0: integer): E
public "clone"(): any
public "indexOf"(arg0: any): integer
public "clear"(): void
public "lastIndexOf"(arg0: any): integer
public "size"(): integer
public "toArray"(): (any)[]
public "toArray"<T>(arg0: (T)[]): (T)[]
public "contains"(arg0: any): boolean
public "spliterator"(): $Spliterator<(E)>
public "addAll"(arg0: integer, arg1: $Collection$Type<(any)>): boolean
public "addAll"(arg0: $Collection$Type<(any)>): boolean
public "set"(arg0: integer, arg1: E): E
public "poll"(): E
public "peek"(): E
public "element"(): E
public "listIterator"(arg0: integer): $ListIterator<(E)>
public "push"(arg0: E): void
public "pop"(): E
public "addFirst"(arg0: E): void
public "addLast"(arg0: E): void
public "pollFirst"(): E
public "pollLast"(): E
public "offerLast"(arg0: E): boolean
public "removeFirst"(): E
public "getFirst"(): E
public "peekFirst"(): E
public "removeFirstOccurrence"(arg0: any): boolean
public "offerFirst"(arg0: E): boolean
public "removeLast"(): E
public "getLast"(): E
public "peekLast"(): E
public "removeLastOccurrence"(arg0: any): boolean
public "offer"(arg0: E): boolean
public "descendingIterator"(): $Iterator<(E)>
public "equals"(arg0: any): boolean
public "hashCode"(): integer
public static "copyOf"<E>(arg0: $Collection$Type<(any)>): $List<(E)>
public "isEmpty"(): boolean
public "subList"(arg0: integer, arg1: integer): $List<(E)>
public "iterator"(): $Iterator<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E): $List<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E): $List<(E)>
public static "of"<E>(arg0: E, arg1: E): $List<(E)>
public static "of"<E>(arg0: E): $List<(E)>
public static "of"<E>(): $List<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E, arg5: E, arg6: E, arg7: E): $List<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E, arg5: E, arg6: E): $List<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E, arg5: E): $List<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E): $List<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E, arg5: E, arg6: E, arg7: E, arg8: E, arg9: E): $List<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E, arg5: E, arg6: E, arg7: E, arg8: E): $List<(E)>
public static "of"<E>(...arg0: (E)[]): $List<(E)>
public "removeAll"(arg0: $Collection$Type<(any)>): boolean
public "retainAll"(arg0: $Collection$Type<(any)>): boolean
public "listIterator"(): $ListIterator<(E)>
public "containsAll"(arg0: $Collection$Type<(any)>): boolean
get "first"(): E
get "last"(): E
get "empty"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LinkedList$Type<E> = ($LinkedList<(E)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LinkedList_<E> = $LinkedList$Type<(E)>;
}}
declare module "packages/java/util/stream/$Stream" {
import {$Stream$Builder, $Stream$Builder$Type} from "packages/java/util/stream/$Stream$Builder"
import {$Collector, $Collector$Type} from "packages/java/util/stream/$Collector"
import {$BiFunction, $BiFunction$Type} from "packages/java/util/function/$BiFunction"
import {$Runnable, $Runnable$Type} from "packages/java/lang/$Runnable"
import {$BinaryOperator, $BinaryOperator$Type} from "packages/java/util/function/$BinaryOperator"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$UnaryOperator, $UnaryOperator$Type} from "packages/java/util/function/$UnaryOperator"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$ToLongFunction, $ToLongFunction$Type} from "packages/java/util/function/$ToLongFunction"
import {$ToDoubleFunction, $ToDoubleFunction$Type} from "packages/java/util/function/$ToDoubleFunction"
import {$Comparator, $Comparator$Type} from "packages/java/util/$Comparator"
import {$LongStream, $LongStream$Type} from "packages/java/util/stream/$LongStream"
import {$IntStream, $IntStream$Type} from "packages/java/util/stream/$IntStream"
import {$ToIntFunction, $ToIntFunction$Type} from "packages/java/util/function/$ToIntFunction"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$Spliterator, $Spliterator$Type} from "packages/java/util/$Spliterator"
import {$DoubleStream, $DoubleStream$Type} from "packages/java/util/stream/$DoubleStream"
import {$BiConsumer, $BiConsumer$Type} from "packages/java/util/function/$BiConsumer"
import {$IntFunction, $IntFunction$Type} from "packages/java/util/function/$IntFunction"
import {$BaseStream, $BaseStream$Type} from "packages/java/util/stream/$BaseStream"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$Iterator, $Iterator$Type} from "packages/java/util/$Iterator"

export interface $Stream<T> extends $BaseStream<(T), ($Stream<(T)>)> {

 "min"(arg0: $Comparator$Type<(any)>): $Optional<(T)>
 "max"(arg0: $Comparator$Type<(any)>): $Optional<(T)>
 "toArray"(): (any)[]
 "toArray"<A>(arg0: $IntFunction$Type<((A)[])>): (A)[]
 "map"<R>(arg0: $Function$Type<(any), (any)>): $Stream<(R)>
 "collect"<R>(arg0: $Supplier$Type<(R)>, arg1: $BiConsumer$Type<(R), (any)>, arg2: $BiConsumer$Type<(R), (R)>): R
 "collect"<R, A>(arg0: $Collector$Type<(any), (A), (R)>): R
 "toList"(): $List<(T)>
 "count"(): long
 "limit"(arg0: long): $Stream<(T)>
 "filter"(arg0: $Predicate$Type<(any)>): $Stream<(T)>
 "anyMatch"(arg0: $Predicate$Type<(any)>): boolean
 "flatMap"<R>(arg0: $Function$Type<(any), (any)>): $Stream<(R)>
 "forEach"(arg0: $Consumer$Type<(any)>): void
 "findAny"(): $Optional<(T)>
 "skip"(arg0: long): $Stream<(T)>
 "peek"(arg0: $Consumer$Type<(any)>): $Stream<(T)>
 "reduce"<U>(arg0: U, arg1: $BiFunction$Type<(U), (any), (U)>, arg2: $BinaryOperator$Type<(U)>): U
 "reduce"(arg0: T, arg1: $BinaryOperator$Type<(T)>): T
 "reduce"(arg0: $BinaryOperator$Type<(T)>): $Optional<(T)>
 "distinct"(): $Stream<(T)>
 "findFirst"(): $Optional<(T)>
 "allMatch"(arg0: $Predicate$Type<(any)>): boolean
 "sorted"(): $Stream<(T)>
 "sorted"(arg0: $Comparator$Type<(any)>): $Stream<(T)>
 "forEachOrdered"(arg0: $Consumer$Type<(any)>): void
 "mapToInt"(arg0: $ToIntFunction$Type<(any)>): $IntStream
 "mapToLong"(arg0: $ToLongFunction$Type<(any)>): $LongStream
 "mapToDouble"(arg0: $ToDoubleFunction$Type<(any)>): $DoubleStream
 "flatMapToInt"(arg0: $Function$Type<(any), (any)>): $IntStream
 "flatMapToDouble"(arg0: $Function$Type<(any), (any)>): $DoubleStream
 "flatMapToLong"(arg0: $Function$Type<(any), (any)>): $LongStream
 "mapMulti"<R>(arg0: $BiConsumer$Type<(any), (any)>): $Stream<(R)>
 "mapMultiToInt"(arg0: $BiConsumer$Type<(any), (any)>): $IntStream
 "mapMultiToLong"(arg0: $BiConsumer$Type<(any), (any)>): $LongStream
 "mapMultiToDouble"(arg0: $BiConsumer$Type<(any), (any)>): $DoubleStream
 "takeWhile"(arg0: $Predicate$Type<(any)>): $Stream<(T)>
 "dropWhile"(arg0: $Predicate$Type<(any)>): $Stream<(T)>
 "noneMatch"(arg0: $Predicate$Type<(any)>): boolean
 "iterator"(): $Iterator<(T)>
 "spliterator"(): $Spliterator<(T)>
 "close"(): void
 "parallel"(): $Stream<(T)>
 "onClose"(arg0: $Runnable$Type): $Stream<(T)>
 "sequential"(): $Stream<(T)>
 "isParallel"(): boolean
 "unordered"(): $Stream<(T)>
}

export namespace $Stream {
function of<T>(...arg0: (T)[]): $Stream<(T)>
function of<T>(arg0: T): $Stream<(T)>
function builder<T>(): $Stream$Builder<(T)>
function concat<T>(arg0: $Stream$Type<(any)>, arg1: $Stream$Type<(any)>): $Stream<(T)>
function empty<T>(): $Stream<(T)>
function iterate<T>(arg0: T, arg1: $Predicate$Type<(any)>, arg2: $UnaryOperator$Type<(T)>): $Stream<(T)>
function iterate<T>(arg0: T, arg1: $UnaryOperator$Type<(T)>): $Stream<(T)>
function ofNullable<T>(arg0: T): $Stream<(T)>
function generate<T>(arg0: $Supplier$Type<(any)>): $Stream<(T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Stream$Type<T> = ($Stream<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Stream_<T> = $Stream$Type<(T)>;
}}
declare module "packages/java/util/function/$LongSupplier" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $LongSupplier {

 "getAsLong"(): long

(): long
}

export namespace $LongSupplier {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LongSupplier$Type = ($LongSupplier);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LongSupplier_ = $LongSupplier$Type;
}}
declare module "packages/java/util/concurrent/$ExecutionException" {
import {$Throwable, $Throwable$Type} from "packages/java/lang/$Throwable"
import {$Exception, $Exception$Type} from "packages/java/lang/$Exception"

export class $ExecutionException extends $Exception {

constructor(arg0: $Throwable$Type)
constructor(arg0: string, arg1: $Throwable$Type)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ExecutionException$Type = ($ExecutionException);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ExecutionException_ = $ExecutionException$Type;
}}
declare module "packages/java/util/stream/$Collector" {
import {$BiConsumer, $BiConsumer$Type} from "packages/java/util/function/$BiConsumer"
import {$BinaryOperator, $BinaryOperator$Type} from "packages/java/util/function/$BinaryOperator"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$Collector$Characteristics, $Collector$Characteristics$Type} from "packages/java/util/stream/$Collector$Characteristics"

export interface $Collector<T, A, R> {

 "characteristics"(): $Set<($Collector$Characteristics)>
 "combiner"(): $BinaryOperator<(A)>
 "supplier"(): $Supplier<(A)>
 "accumulator"(): $BiConsumer<(A), (T)>
 "finisher"(): $Function<(A), (R)>
}

export namespace $Collector {
function of<T, A, R>(arg0: $Supplier$Type<(A)>, arg1: $BiConsumer$Type<(A), (T)>, arg2: $BinaryOperator$Type<(A)>, arg3: $Function$Type<(A), (R)>, ...arg4: ($Collector$Characteristics$Type)[]): $Collector<(T), (A), (R)>
function of<T, R>(arg0: $Supplier$Type<(R)>, arg1: $BiConsumer$Type<(R), (T)>, arg2: $BinaryOperator$Type<(R)>, ...arg3: ($Collector$Characteristics$Type)[]): $Collector<(T), (R), (R)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Collector$Type<T, A, R> = ($Collector<(T), (A), (R)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Collector_<T, A, R> = $Collector$Type<(T), (A), (R)>;
}}
declare module "packages/java/util/$Locale$FilteringMode" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $Locale$FilteringMode extends $Enum<($Locale$FilteringMode)> {
static readonly "AUTOSELECT_FILTERING": $Locale$FilteringMode
static readonly "EXTENDED_FILTERING": $Locale$FilteringMode
static readonly "IGNORE_EXTENDED_RANGES": $Locale$FilteringMode
static readonly "MAP_EXTENDED_RANGES": $Locale$FilteringMode
static readonly "REJECT_EXTENDED_RANGES": $Locale$FilteringMode


public static "values"(): ($Locale$FilteringMode)[]
public static "valueOf"(arg0: string): $Locale$FilteringMode
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Locale$FilteringMode$Type = (("extended_filtering") | ("reject_extended_ranges") | ("map_extended_ranges") | ("ignore_extended_ranges") | ("autoselect_filtering")) | ($Locale$FilteringMode);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Locale$FilteringMode_ = $Locale$FilteringMode$Type;
}}
declare module "packages/java/util/$Base64$Encoder" {
import {$OutputStream, $OutputStream$Type} from "packages/java/io/$OutputStream"
import {$ByteBuffer, $ByteBuffer$Type} from "packages/java/nio/$ByteBuffer"

export class $Base64$Encoder {


public "wrap"(arg0: $OutputStream$Type): $OutputStream
public "encode"(arg0: $ByteBuffer$Type): $ByteBuffer
public "encode"(arg0: (byte)[]): (byte)[]
public "encode"(arg0: (byte)[], arg1: (byte)[]): integer
public "encodeToString"(arg0: (byte)[]): string
public "withoutPadding"(): $Base64$Encoder
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Base64$Encoder$Type = ($Base64$Encoder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Base64$Encoder_ = $Base64$Encoder$Type;
}}
declare module "packages/java/util/stream/$LongStream$LongMapMultiConsumer" {
import {$LongConsumer, $LongConsumer$Type} from "packages/java/util/function/$LongConsumer"

export interface $LongStream$LongMapMultiConsumer {

 "accept"(arg0: long, arg1: $LongConsumer$Type): void

(arg0: long, arg1: $LongConsumer$Type): void
}

export namespace $LongStream$LongMapMultiConsumer {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LongStream$LongMapMultiConsumer$Type = ($LongStream$LongMapMultiConsumer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LongStream$LongMapMultiConsumer_ = $LongStream$LongMapMultiConsumer$Type;
}}
declare module "packages/java/util/$Spliterator$OfInt" {
import {$Comparator, $Comparator$Type} from "packages/java/util/$Comparator"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$Spliterator$OfPrimitive, $Spliterator$OfPrimitive$Type} from "packages/java/util/$Spliterator$OfPrimitive"
import {$IntConsumer, $IntConsumer$Type} from "packages/java/util/function/$IntConsumer"

export interface $Spliterator$OfInt extends $Spliterator$OfPrimitive<(integer), ($IntConsumer), ($Spliterator$OfInt)> {

 "forEachRemaining"(arg0: $IntConsumer$Type): void
 "forEachRemaining"(arg0: $Consumer$Type<(any)>): void
 "tryAdvance"(arg0: $Consumer$Type<(any)>): boolean
 "tryAdvance"(arg0: $IntConsumer$Type): boolean
 "characteristics"(): integer
 "estimateSize"(): long
 "getExactSizeIfKnown"(): long
 "hasCharacteristics"(arg0: integer): boolean
 "getComparator"(): $Comparator<(any)>
}

export namespace $Spliterator$OfInt {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Spliterator$OfInt$Type = ($Spliterator$OfInt);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Spliterator$OfInt_ = $Spliterator$OfInt$Type;
}}
declare module "packages/java/util/$TreeMap" {
import {$Comparator, $Comparator$Type} from "packages/java/util/$Comparator"
import {$Serializable, $Serializable$Type} from "packages/java/io/$Serializable"
import {$NavigableSet, $NavigableSet$Type} from "packages/java/util/$NavigableSet"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$SortedMap, $SortedMap$Type} from "packages/java/util/$SortedMap"
import {$AbstractMap, $AbstractMap$Type} from "packages/java/util/$AbstractMap"
import {$BiFunction, $BiFunction$Type} from "packages/java/util/function/$BiFunction"
import {$NavigableMap, $NavigableMap$Type} from "packages/java/util/$NavigableMap"
import {$BiConsumer, $BiConsumer$Type} from "packages/java/util/function/$BiConsumer"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$Cloneable, $Cloneable$Type} from "packages/java/lang/$Cloneable"
import {$Map, $Map$Type} from "packages/java/util/$Map"
import {$Map$Entry, $Map$Entry$Type} from "packages/java/util/$Map$Entry"

export class $TreeMap<K, V> extends $AbstractMap<(K), (V)> implements $NavigableMap<(K), (V)>, $Cloneable, $Serializable {

constructor(arg0: $SortedMap$Type<(K), (any)>)
constructor(arg0: $Map$Type<(any), (any)>)
constructor(arg0: $Comparator$Type<(any)>)
constructor()

public "remove"(arg0: any): V
public "get"(arg0: any): V
public "put"(arg0: K, arg1: V): V
public "values"(): $Collection<(V)>
public "clone"(): any
public "clear"(): void
public "replace"(arg0: K, arg1: V, arg2: V): boolean
public "replace"(arg0: K, arg1: V): V
public "replaceAll"(arg0: $BiFunction$Type<(any), (any), (any)>): void
public "size"(): integer
public "merge"(arg0: K, arg1: V, arg2: $BiFunction$Type<(any), (any), (any)>): V
public "entrySet"(): $Set<($Map$Entry<(K), (V)>)>
public "putAll"(arg0: $Map$Type<(any), (any)>): void
public "putIfAbsent"(arg0: K, arg1: V): V
public "compute"(arg0: K, arg1: $BiFunction$Type<(any), (any), (any)>): V
public "forEach"(arg0: $BiConsumer$Type<(any), (any)>): void
public "containsKey"(arg0: any): boolean
public "computeIfAbsent"(arg0: K, arg1: $Function$Type<(any), (any)>): V
public "keySet"(): $Set<(K)>
public "containsValue"(arg0: any): boolean
public "computeIfPresent"(arg0: K, arg1: $BiFunction$Type<(any), (any), (any)>): V
public "comparator"(): $Comparator<(any)>
public "firstKey"(): K
public "lowerEntry"(arg0: K): $Map$Entry<(K), (V)>
public "lowerKey"(arg0: K): K
public "floorEntry"(arg0: K): $Map$Entry<(K), (V)>
public "floorKey"(arg0: K): K
public "ceilingEntry"(arg0: K): $Map$Entry<(K), (V)>
public "ceilingKey"(arg0: K): K
public "higherEntry"(arg0: K): $Map$Entry<(K), (V)>
public "higherKey"(arg0: K): K
public "firstEntry"(): $Map$Entry<(K), (V)>
public "lastEntry"(): $Map$Entry<(K), (V)>
public "pollFirstEntry"(): $Map$Entry<(K), (V)>
public "pollLastEntry"(): $Map$Entry<(K), (V)>
public "descendingMap"(): $NavigableMap<(K), (V)>
public "navigableKeySet"(): $NavigableSet<(K)>
public "descendingKeySet"(): $NavigableSet<(K)>
public "subMap"(arg0: K, arg1: K): $SortedMap<(K), (V)>
public "subMap"(arg0: K, arg1: boolean, arg2: K, arg3: boolean): $NavigableMap<(K), (V)>
public "headMap"(arg0: K, arg1: boolean): $NavigableMap<(K), (V)>
public "headMap"(arg0: K): $SortedMap<(K), (V)>
public "tailMap"(arg0: K): $SortedMap<(K), (V)>
public "tailMap"(arg0: K, arg1: boolean): $NavigableMap<(K), (V)>
public "lastKey"(): K
public static "copyOf"<K, V>(arg0: $Map$Type<(any), (any)>): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V, arg8: K, arg9: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V): $Map<(K), (V)>
public static "of"<K, V>(): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V, arg8: K, arg9: V, arg10: K, arg11: V, arg12: K, arg13: V, arg14: K, arg15: V, arg16: K, arg17: V, arg18: K, arg19: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V, arg8: K, arg9: V, arg10: K, arg11: V, arg12: K, arg13: V, arg14: K, arg15: V, arg16: K, arg17: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V, arg8: K, arg9: V, arg10: K, arg11: V, arg12: K, arg13: V, arg14: K, arg15: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V, arg8: K, arg9: V, arg10: K, arg11: V, arg12: K, arg13: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V, arg8: K, arg9: V, arg10: K, arg11: V): $Map<(K), (V)>
public static "entry"<K, V>(arg0: K, arg1: V): $Map$Entry<(K), (V)>
public static "ofEntries"<K, V>(...arg0: ($Map$Entry$Type<(any), (any)>)[]): $Map<(K), (V)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TreeMap$Type<K, V> = ($TreeMap<(K), (V)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TreeMap_<K, V> = $TreeMap$Type<(K), (V)>;
}}
declare module "packages/java/util/$PrimitiveIterator$OfLong" {
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$LongConsumer, $LongConsumer$Type} from "packages/java/util/function/$LongConsumer"
import {$PrimitiveIterator, $PrimitiveIterator$Type} from "packages/java/util/$PrimitiveIterator"

export interface $PrimitiveIterator$OfLong extends $PrimitiveIterator<(long), ($LongConsumer)> {

 "forEachRemaining"(arg0: $LongConsumer$Type): void
 "forEachRemaining"(arg0: $Consumer$Type<(any)>): void
 "nextLong"(): long
 "remove"(): void
 "hasNext"(): boolean
}

export namespace $PrimitiveIterator$OfLong {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PrimitiveIterator$OfLong$Type = ($PrimitiveIterator$OfLong);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PrimitiveIterator$OfLong_ = $PrimitiveIterator$OfLong$Type;
}}
declare module "packages/java/util/$LongSummaryStatistics" {
import {$LongConsumer, $LongConsumer$Type} from "packages/java/util/function/$LongConsumer"
import {$IntConsumer, $IntConsumer$Type} from "packages/java/util/function/$IntConsumer"

export class $LongSummaryStatistics implements $LongConsumer, $IntConsumer {

constructor()
constructor(arg0: long, arg1: long, arg2: long, arg3: long)

public "toString"(): string
public "accept"(arg0: integer): void
public "accept"(arg0: long): void
public "combine"(arg0: $LongSummaryStatistics$Type): void
public "getCount"(): long
public "getSum"(): long
public "getMin"(): long
public "getAverage"(): double
public "getMax"(): long
public "andThen"(arg0: $LongConsumer$Type): $LongConsumer
public "andThen"(arg0: $IntConsumer$Type): $IntConsumer
get "count"(): long
get "sum"(): long
get "min"(): long
get "average"(): double
get "max"(): long
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LongSummaryStatistics$Type = ($LongSummaryStatistics);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LongSummaryStatistics_ = $LongSummaryStatistics$Type;
}}
declare module "packages/java/util/concurrent/locks/$Condition" {
import {$Date, $Date$Type} from "packages/java/util/$Date"
import {$TimeUnit, $TimeUnit$Type} from "packages/java/util/concurrent/$TimeUnit"

export interface $Condition {

 "signalAll"(): void
 "awaitNanos"(arg0: long): long
 "signal"(): void
 "await"(): void
 "await"(arg0: long, arg1: $TimeUnit$Type): boolean
 "awaitUninterruptibly"(): void
 "awaitUntil"(arg0: $Date$Type): boolean
}

export namespace $Condition {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Condition$Type = ($Condition);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Condition_ = $Condition$Type;
}}
declare module "packages/java/util/concurrent/atomic/$AtomicBoolean" {
import {$Serializable, $Serializable$Type} from "packages/java/io/$Serializable"

export class $AtomicBoolean implements $Serializable {

constructor(arg0: boolean)
constructor()

public "get"(): boolean
public "toString"(): string
public "set"(arg0: boolean): void
public "getOpaque"(): boolean
public "setOpaque"(arg0: boolean): void
public "getAcquire"(): boolean
public "setRelease"(arg0: boolean): void
public "compareAndSet"(arg0: boolean, arg1: boolean): boolean
public "compareAndExchange"(arg0: boolean, arg1: boolean): boolean
public "compareAndExchangeAcquire"(arg0: boolean, arg1: boolean): boolean
public "compareAndExchangeRelease"(arg0: boolean, arg1: boolean): boolean
public "weakCompareAndSetPlain"(arg0: boolean, arg1: boolean): boolean
/**
 * 
 * @deprecated
 */
public "weakCompareAndSet"(arg0: boolean, arg1: boolean): boolean
public "weakCompareAndSetAcquire"(arg0: boolean, arg1: boolean): boolean
public "weakCompareAndSetRelease"(arg0: boolean, arg1: boolean): boolean
public "getAndSet"(arg0: boolean): boolean
public "lazySet"(arg0: boolean): void
public "weakCompareAndSetVolatile"(arg0: boolean, arg1: boolean): boolean
public "getPlain"(): boolean
public "setPlain"(arg0: boolean): void
get "opaque"(): boolean
set "opaque"(value: boolean)
get "acquire"(): boolean
set "release"(value: boolean)
get "plain"(): boolean
set "plain"(value: boolean)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AtomicBoolean$Type = ($AtomicBoolean);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AtomicBoolean_ = $AtomicBoolean$Type;
}}
declare module "packages/java/util/concurrent/$ForkJoinTask" {
import {$Throwable, $Throwable$Type} from "packages/java/lang/$Throwable"
import {$Serializable, $Serializable$Type} from "packages/java/io/$Serializable"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Callable, $Callable$Type} from "packages/java/util/concurrent/$Callable"
import {$Future, $Future$Type} from "packages/java/util/concurrent/$Future"
import {$ForkJoinPool, $ForkJoinPool$Type} from "packages/java/util/concurrent/$ForkJoinPool"
import {$TimeUnit, $TimeUnit$Type} from "packages/java/util/concurrent/$TimeUnit"
import {$Runnable, $Runnable$Type} from "packages/java/lang/$Runnable"

export class $ForkJoinTask<V> implements $Future<(V)>, $Serializable {

constructor()

public "invoke"(): V
public "get"(): V
public "get"(arg0: long, arg1: $TimeUnit$Type): V
public "join"(): V
public "getException"(): $Throwable
public "reinitialize"(): void
public static "getPool"(): $ForkJoinPool
public "complete"(arg0: V): void
public "isDone"(): boolean
public "cancel"(arg0: boolean): boolean
public static "invokeAll"(arg0: $ForkJoinTask$Type<(any)>, arg1: $ForkJoinTask$Type<(any)>): void
public static "invokeAll"<T extends $ForkJoinTask<(any)>>(arg0: $Collection$Type<(T)>): $Collection<(T)>
public static "invokeAll"(...arg0: ($ForkJoinTask$Type<(any)>)[]): void
public "quietlyComplete"(): void
public "getRawResult"(): V
public "fork"(): $ForkJoinTask<(V)>
public static "getSurplusQueuedTaskCount"(): integer
public "isCancelled"(): boolean
public "isCompletedAbnormally"(): boolean
public "isCompletedNormally"(): boolean
public "completeExceptionally"(arg0: $Throwable$Type): void
public "quietlyJoin"(): void
public "quietlyInvoke"(): void
public static "helpQuiesce"(): void
public static "inForkJoinPool"(): boolean
public "tryUnfork"(): boolean
public static "getQueuedTaskCount"(): integer
public "getForkJoinTaskTag"(): short
public "setForkJoinTaskTag"(arg0: short): short
public "compareAndSetForkJoinTaskTag"(arg0: short, arg1: short): boolean
public static "adapt"(arg0: $Runnable$Type): $ForkJoinTask<(any)>
public static "adapt"<T>(arg0: $Callable$Type<(any)>): $ForkJoinTask<(T)>
public static "adapt"<T>(arg0: $Runnable$Type, arg1: T): $ForkJoinTask<(T)>
get "exception"(): $Throwable
get "pool"(): $ForkJoinPool
get "done"(): boolean
get "rawResult"(): V
get "surplusQueuedTaskCount"(): integer
get "cancelled"(): boolean
get "completedAbnormally"(): boolean
get "completedNormally"(): boolean
get "queuedTaskCount"(): integer
get "forkJoinTaskTag"(): short
set "forkJoinTaskTag"(value: short)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForkJoinTask$Type<V> = ($ForkJoinTask<(V)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForkJoinTask_<V> = $ForkJoinTask$Type<(V)>;
}}
declare module "packages/java/util/function/$IntConsumer" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $IntConsumer {

 "accept"(arg0: integer): void
 "andThen"(arg0: $IntConsumer$Type): $IntConsumer

(arg0: integer): void
}

export namespace $IntConsumer {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IntConsumer$Type = ($IntConsumer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IntConsumer_ = $IntConsumer$Type;
}}
declare module "packages/java/util/logging/$Level" {
import {$Serializable, $Serializable$Type} from "packages/java/io/$Serializable"

export class $Level implements $Serializable {
static readonly "OFF": $Level
static readonly "SEVERE": $Level
static readonly "WARNING": $Level
static readonly "INFO": $Level
static readonly "CONFIG": $Level
static readonly "FINE": $Level
static readonly "FINER": $Level
static readonly "FINEST": $Level
static readonly "ALL": $Level


public "getName"(): string
public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "intValue"(): integer
public static "parse"(arg0: string): $Level
public "getResourceBundleName"(): string
public "getLocalizedName"(): string
get "name"(): string
get "resourceBundleName"(): string
get "localizedName"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Level$Type = ($Level);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Level_ = $Level$Type;
}}
declare module "packages/java/util/function/$Supplier" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $Supplier<T> {

 "get"(): T

(): T
}

export namespace $Supplier {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Supplier$Type<T> = ($Supplier<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Supplier_<T> = $Supplier$Type<(T)>;
}}
declare module "packages/java/util/$Properties" {
import {$OutputStream, $OutputStream$Type} from "packages/java/io/$OutputStream"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Hashtable, $Hashtable$Type} from "packages/java/util/$Hashtable"
import {$Writer, $Writer$Type} from "packages/java/io/$Writer"
import {$BiFunction, $BiFunction$Type} from "packages/java/util/function/$BiFunction"
import {$Charset, $Charset$Type} from "packages/java/nio/charset/$Charset"
import {$BiConsumer, $BiConsumer$Type} from "packages/java/util/function/$BiConsumer"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Enumeration, $Enumeration$Type} from "packages/java/util/$Enumeration"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$InputStream, $InputStream$Type} from "packages/java/io/$InputStream"
import {$PrintWriter, $PrintWriter$Type} from "packages/java/io/$PrintWriter"
import {$PrintStream, $PrintStream$Type} from "packages/java/io/$PrintStream"
import {$Reader, $Reader$Type} from "packages/java/io/$Reader"
import {$Map$Entry, $Map$Entry$Type} from "packages/java/util/$Map$Entry"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $Properties extends $Hashtable<(any), (any)> {

constructor(arg0: $Properties$Type)
constructor(arg0: integer)
constructor()

public "remove"(arg0: any): any
public "remove"(arg0: any, arg1: any): boolean
public "get"(arg0: any): any
public "put"(arg0: any, arg1: any): any
public "getProperty"(arg0: string): string
public "getProperty"(arg0: string, arg1: string): string
public "equals"(arg0: any): boolean
public "toString"(): string
public "values"(): $Collection<(any)>
public "hashCode"(): integer
public "clone"(): any
public "load"(arg0: $Reader$Type): void
public "load"(arg0: $InputStream$Type): void
public "store"(arg0: $OutputStream$Type, arg1: string): void
public "store"(arg0: $Writer$Type, arg1: string): void
public "clear"(): void
public "isEmpty"(): boolean
public "replace"(arg0: any, arg1: any, arg2: any): boolean
public "replace"(arg0: any, arg1: any): any
public "replaceAll"(arg0: $BiFunction$Type<(any), (any), (any)>): void
public "size"(): integer
public "contains"(arg0: any): boolean
public "list"(arg0: $PrintStream$Type): void
public "list"(arg0: $PrintWriter$Type): void
public "elements"(): $Enumeration<(any)>
public "merge"(arg0: any, arg1: any, arg2: $BiFunction$Type<(any), (any), (any)>): any
public "entrySet"(): $Set<($Map$Entry<(any), (any)>)>
public "putAll"(arg0: $Map$Type<(any), (any)>): void
public "putIfAbsent"(arg0: any, arg1: any): any
public "compute"(arg0: any, arg1: $BiFunction$Type<(any), (any), (any)>): any
public "setProperty"(arg0: string, arg1: string): any
public "forEach"(arg0: $BiConsumer$Type<(any), (any)>): void
public "containsKey"(arg0: any): boolean
public "computeIfAbsent"(arg0: any, arg1: $Function$Type<(any), (any)>): any
public "storeToXML"(arg0: $OutputStream$Type, arg1: string, arg2: string): void
public "storeToXML"(arg0: $OutputStream$Type, arg1: string): void
public "storeToXML"(arg0: $OutputStream$Type, arg1: string, arg2: $Charset$Type): void
public "keys"(): $Enumeration<(any)>
public "keySet"(): $Set<(any)>
public "containsValue"(arg0: any): boolean
public "getOrDefault"(arg0: any, arg1: any): any
public "computeIfPresent"(arg0: any, arg1: $BiFunction$Type<(any), (any), (any)>): any
/**
 * 
 * @deprecated
 */
public "save"(arg0: $OutputStream$Type, arg1: string): void
public "loadFromXML"(arg0: $InputStream$Type): void
public "propertyNames"(): $Enumeration<(any)>
public "stringPropertyNames"(): $Set<(string)>
public static "copyOf"<K, V>(arg0: $Map$Type<(any), (any)>): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V, arg8: K, arg9: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V): $Map<(K), (V)>
public static "of"<K, V>(): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V, arg8: K, arg9: V, arg10: K, arg11: V, arg12: K, arg13: V, arg14: K, arg15: V, arg16: K, arg17: V, arg18: K, arg19: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V, arg8: K, arg9: V, arg10: K, arg11: V, arg12: K, arg13: V, arg14: K, arg15: V, arg16: K, arg17: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V, arg8: K, arg9: V, arg10: K, arg11: V, arg12: K, arg13: V, arg14: K, arg15: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V, arg8: K, arg9: V, arg10: K, arg11: V, arg12: K, arg13: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V, arg8: K, arg9: V, arg10: K, arg11: V): $Map<(K), (V)>
public static "entry"<K, V>(arg0: K, arg1: V): $Map$Entry<(K), (V)>
public static "ofEntries"<K, V>(...arg0: ($Map$Entry$Type<(any), (any)>)[]): $Map<(K), (V)>
get "empty"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Properties$Type = ($Properties);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Properties_ = $Properties$Type;
}}
declare module "packages/java/util/function/$LongUnaryOperator" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $LongUnaryOperator {

 "applyAsLong"(arg0: long): long
 "compose"(arg0: $LongUnaryOperator$Type): $LongUnaryOperator
 "andThen"(arg0: $LongUnaryOperator$Type): $LongUnaryOperator

(): $LongUnaryOperator
}

export namespace $LongUnaryOperator {
function identity(): $LongUnaryOperator
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LongUnaryOperator$Type = ($LongUnaryOperator);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LongUnaryOperator_ = $LongUnaryOperator$Type;
}}
declare module "packages/java/util/concurrent/$BlockingQueue" {
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$IntFunction, $IntFunction$Type} from "packages/java/util/function/$IntFunction"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$TimeUnit, $TimeUnit$Type} from "packages/java/util/concurrent/$TimeUnit"
import {$Iterator, $Iterator$Type} from "packages/java/util/$Iterator"
import {$Spliterator, $Spliterator$Type} from "packages/java/util/$Spliterator"
import {$Queue, $Queue$Type} from "packages/java/util/$Queue"

export interface $BlockingQueue<E> extends $Queue<(E)> {

 "add"(arg0: E): boolean
 "remove"(arg0: any): boolean
 "put"(arg0: E): void
 "contains"(arg0: any): boolean
 "poll"(arg0: long, arg1: $TimeUnit$Type): E
 "offer"(arg0: E): boolean
 "offer"(arg0: E, arg1: long, arg2: $TimeUnit$Type): boolean
 "take"(): E
 "remainingCapacity"(): integer
 "drainTo"(arg0: $Collection$Type<(any)>): integer
 "drainTo"(arg0: $Collection$Type<(any)>, arg1: integer): integer
 "remove"(): E
 "poll"(): E
 "peek"(): E
 "element"(): E
 "equals"(arg0: any): boolean
 "hashCode"(): integer
 "clear"(): void
 "isEmpty"(): boolean
 "size"(): integer
 "toArray"<T>(arg0: (T)[]): (T)[]
 "toArray"<T>(arg0: $IntFunction$Type<((T)[])>): (T)[]
 "toArray"(): (any)[]
 "iterator"(): $Iterator<(E)>
 "stream"(): $Stream<(E)>
 "spliterator"(): $Spliterator<(E)>
 "addAll"(arg0: $Collection$Type<(any)>): boolean
 "removeIf"(arg0: $Predicate$Type<(any)>): boolean
 "removeAll"(arg0: $Collection$Type<(any)>): boolean
 "retainAll"(arg0: $Collection$Type<(any)>): boolean
 "containsAll"(arg0: $Collection$Type<(any)>): boolean
 "parallelStream"(): $Stream<(E)>
 "forEach"(arg0: $Consumer$Type<(any)>): void
}

export namespace $BlockingQueue {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockingQueue$Type<E> = ($BlockingQueue<(E)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockingQueue_<E> = $BlockingQueue$Type<(E)>;
}}
declare module "packages/java/util/stream/$Stream$Builder" {
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"

export interface $Stream$Builder<T> extends $Consumer<(T)> {

 "add"(arg0: T): $Stream$Builder<(T)>
 "accept"(arg0: T): void
 "build"(): $Stream<(T)>
 "andThen"(arg0: $Consumer$Type<(any)>): $Consumer<(T)>
}

export namespace $Stream$Builder {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Stream$Builder$Type<T> = ($Stream$Builder<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Stream$Builder_<T> = $Stream$Builder$Type<(T)>;
}}
declare module "packages/java/util/concurrent/$ScheduledExecutorService" {
import {$ExecutorService, $ExecutorService$Type} from "packages/java/util/concurrent/$ExecutorService"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Callable, $Callable$Type} from "packages/java/util/concurrent/$Callable"
import {$Future, $Future$Type} from "packages/java/util/concurrent/$Future"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ScheduledFuture, $ScheduledFuture$Type} from "packages/java/util/concurrent/$ScheduledFuture"
import {$TimeUnit, $TimeUnit$Type} from "packages/java/util/concurrent/$TimeUnit"
import {$Runnable, $Runnable$Type} from "packages/java/lang/$Runnable"

export interface $ScheduledExecutorService extends $ExecutorService {

 "scheduleAtFixedRate"(arg0: $Runnable$Type, arg1: long, arg2: long, arg3: $TimeUnit$Type): $ScheduledFuture<(any)>
 "schedule"(arg0: $Runnable$Type, arg1: long, arg2: $TimeUnit$Type): $ScheduledFuture<(any)>
 "schedule"<V>(arg0: $Callable$Type<(V)>, arg1: long, arg2: $TimeUnit$Type): $ScheduledFuture<(V)>
 "scheduleWithFixedDelay"(arg0: $Runnable$Type, arg1: long, arg2: long, arg3: $TimeUnit$Type): $ScheduledFuture<(any)>
 "shutdown"(): void
 "isShutdown"(): boolean
 "submit"(arg0: $Runnable$Type): $Future<(any)>
 "submit"<T>(arg0: $Runnable$Type, arg1: T): $Future<(T)>
 "submit"<T>(arg0: $Callable$Type<(T)>): $Future<(T)>
 "invokeAny"<T>(arg0: $Collection$Type<(any)>, arg1: long, arg2: $TimeUnit$Type): T
 "invokeAny"<T>(arg0: $Collection$Type<(any)>): T
 "invokeAll"<T>(arg0: $Collection$Type<(any)>): $List<($Future<(T)>)>
 "invokeAll"<T>(arg0: $Collection$Type<(any)>, arg1: long, arg2: $TimeUnit$Type): $List<($Future<(T)>)>
 "shutdownNow"(): $List<($Runnable)>
 "isTerminated"(): boolean
 "awaitTermination"(arg0: long, arg1: $TimeUnit$Type): boolean
 "execute"(arg0: $Runnable$Type): void
}

export namespace $ScheduledExecutorService {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ScheduledExecutorService$Type = ($ScheduledExecutorService);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ScheduledExecutorService_ = $ScheduledExecutorService$Type;
}}
declare module "packages/java/util/function/$DoubleUnaryOperator" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $DoubleUnaryOperator {

 "applyAsDouble"(arg0: double): double
 "compose"(arg0: $DoubleUnaryOperator$Type): $DoubleUnaryOperator
 "andThen"(arg0: $DoubleUnaryOperator$Type): $DoubleUnaryOperator

(): $DoubleUnaryOperator
}

export namespace $DoubleUnaryOperator {
function identity(): $DoubleUnaryOperator
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DoubleUnaryOperator$Type = ($DoubleUnaryOperator);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DoubleUnaryOperator_ = $DoubleUnaryOperator$Type;
}}
declare module "packages/java/util/$BitSet" {
import {$Serializable, $Serializable$Type} from "packages/java/io/$Serializable"
import {$IntStream, $IntStream$Type} from "packages/java/util/stream/$IntStream"
import {$LongBuffer, $LongBuffer$Type} from "packages/java/nio/$LongBuffer"
import {$Cloneable, $Cloneable$Type} from "packages/java/lang/$Cloneable"
import {$ByteBuffer, $ByteBuffer$Type} from "packages/java/nio/$ByteBuffer"

export class $BitSet implements $Cloneable, $Serializable {

constructor(arg0: integer)
constructor()

public "get"(arg0: integer, arg1: integer): $BitSet
public "get"(arg0: integer): boolean
public "equals"(arg0: any): boolean
public "length"(): integer
public "toString"(): string
public "hashCode"(): integer
public "clone"(): any
public static "valueOf"(arg0: (long)[]): $BitSet
public static "valueOf"(arg0: $ByteBuffer$Type): $BitSet
public static "valueOf"(arg0: $LongBuffer$Type): $BitSet
public static "valueOf"(arg0: (byte)[]): $BitSet
public "clear"(): void
public "clear"(arg0: integer, arg1: integer): void
public "clear"(arg0: integer): void
public "isEmpty"(): boolean
public "size"(): integer
public "stream"(): $IntStream
public "set"(arg0: integer, arg1: integer): void
public "set"(arg0: integer): void
public "set"(arg0: integer, arg1: boolean): void
public "set"(arg0: integer, arg1: integer, arg2: boolean): void
public "toByteArray"(): (byte)[]
public "flip"(arg0: integer): void
public "flip"(arg0: integer, arg1: integer): void
public "nextClearBit"(arg0: integer): integer
public "or"(arg0: $BitSet$Type): void
public "and"(arg0: $BitSet$Type): void
public "xor"(arg0: $BitSet$Type): void
public "andNot"(arg0: $BitSet$Type): void
public "cardinality"(): integer
public "nextSetBit"(arg0: integer): integer
public "toLongArray"(): (long)[]
public "previousSetBit"(arg0: integer): integer
public "previousClearBit"(arg0: integer): integer
public "intersects"(arg0: $BitSet$Type): boolean
get "empty"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BitSet$Type = ($BitSet);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BitSet_ = $BitSet$Type;
}}
declare module "packages/java/util/$TreeSet" {
import {$Comparator, $Comparator$Type} from "packages/java/util/$Comparator"
import {$Serializable, $Serializable$Type} from "packages/java/io/$Serializable"
import {$NavigableSet, $NavigableSet$Type} from "packages/java/util/$NavigableSet"
import {$SortedSet, $SortedSet$Type} from "packages/java/util/$SortedSet"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Cloneable, $Cloneable$Type} from "packages/java/lang/$Cloneable"
import {$AbstractSet, $AbstractSet$Type} from "packages/java/util/$AbstractSet"
import {$Iterator, $Iterator$Type} from "packages/java/util/$Iterator"
import {$Spliterator, $Spliterator$Type} from "packages/java/util/$Spliterator"

export class $TreeSet<E> extends $AbstractSet<(E)> implements $NavigableSet<(E)>, $Cloneable, $Serializable {

constructor(arg0: $SortedSet$Type<(E)>)
constructor(arg0: $Collection$Type<(any)>)
constructor(arg0: $Comparator$Type<(any)>)
constructor()

public "add"(arg0: E): boolean
public "remove"(arg0: any): boolean
public "clone"(): any
public "floor"(arg0: E): E
public "clear"(): void
public "isEmpty"(): boolean
public "size"(): integer
public "iterator"(): $Iterator<(E)>
public "contains"(arg0: any): boolean
public "last"(): E
public "spliterator"(): $Spliterator<(E)>
public "addAll"(arg0: $Collection$Type<(any)>): boolean
public "first"(): E
public "lower"(arg0: E): E
public "comparator"(): $Comparator<(any)>
public "pollFirst"(): E
public "pollLast"(): E
public "descendingIterator"(): $Iterator<(E)>
public "ceiling"(arg0: E): E
public "higher"(arg0: E): E
public "descendingSet"(): $NavigableSet<(E)>
public "subSet"(arg0: E, arg1: boolean, arg2: E, arg3: boolean): $NavigableSet<(E)>
public "subSet"(arg0: E, arg1: E): $SortedSet<(E)>
public "headSet"(arg0: E): $SortedSet<(E)>
public "headSet"(arg0: E, arg1: boolean): $NavigableSet<(E)>
public "tailSet"(arg0: E, arg1: boolean): $NavigableSet<(E)>
public "tailSet"(arg0: E): $SortedSet<(E)>
public static "copyOf"<E>(arg0: $Collection$Type<(any)>): $Set<(E)>
public "toArray"<T>(arg0: (T)[]): (T)[]
public "toArray"(): (any)[]
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E, arg5: E): $Set<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E): $Set<(E)>
public static "of"<E>(arg0: E): $Set<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E): $Set<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E, arg5: E, arg6: E, arg7: E, arg8: E, arg9: E): $Set<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E, arg5: E, arg6: E, arg7: E, arg8: E): $Set<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E, arg5: E, arg6: E, arg7: E): $Set<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E, arg5: E, arg6: E): $Set<(E)>
public static "of"<E>(...arg0: (E)[]): $Set<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E): $Set<(E)>
public static "of"<E>(): $Set<(E)>
public static "of"<E>(arg0: E, arg1: E): $Set<(E)>
public "retainAll"(arg0: $Collection$Type<(any)>): boolean
public "containsAll"(arg0: $Collection$Type<(any)>): boolean
get "empty"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TreeSet$Type<E> = ($TreeSet<(E)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TreeSet_<E> = $TreeSet$Type<(E)>;
}}
declare module "packages/java/util/$LinkedHashMap" {
import {$BiConsumer, $BiConsumer$Type} from "packages/java/util/function/$BiConsumer"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$HashMap, $HashMap$Type} from "packages/java/util/$HashMap"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$BiFunction, $BiFunction$Type} from "packages/java/util/function/$BiFunction"
import {$Map, $Map$Type} from "packages/java/util/$Map"
import {$Map$Entry, $Map$Entry$Type} from "packages/java/util/$Map$Entry"

export class $LinkedHashMap<K, V> extends $HashMap<(K), (V)> implements $Map<(K), (V)> {

constructor(arg0: integer, arg1: float, arg2: boolean)
constructor()
constructor(arg0: integer)
constructor(arg0: integer, arg1: float)
constructor(arg0: $Map$Type<(any), (any)>)

public "get"(arg0: any): V
public "values"(): $Collection<(V)>
public "clear"(): void
public "replaceAll"(arg0: $BiFunction$Type<(any), (any), (any)>): void
public "entrySet"(): $Set<($Map$Entry<(K), (V)>)>
public "forEach"(arg0: $BiConsumer$Type<(any), (any)>): void
public "keySet"(): $Set<(K)>
public "containsValue"(arg0: any): boolean
public "getOrDefault"(arg0: any, arg1: V): V
public "remove"(arg0: any): V
public "remove"(arg0: any, arg1: any): boolean
public "put"(arg0: K, arg1: V): V
public "equals"(arg0: any): boolean
public "hashCode"(): integer
public static "copyOf"<K, V>(arg0: $Map$Type<(any), (any)>): $Map<(K), (V)>
public "isEmpty"(): boolean
public "replace"(arg0: K, arg1: V): V
public "replace"(arg0: K, arg1: V, arg2: V): boolean
public "size"(): integer
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V, arg8: K, arg9: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V): $Map<(K), (V)>
public static "of"<K, V>(): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V, arg8: K, arg9: V, arg10: K, arg11: V, arg12: K, arg13: V, arg14: K, arg15: V, arg16: K, arg17: V, arg18: K, arg19: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V, arg8: K, arg9: V, arg10: K, arg11: V, arg12: K, arg13: V, arg14: K, arg15: V, arg16: K, arg17: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V, arg8: K, arg9: V, arg10: K, arg11: V, arg12: K, arg13: V, arg14: K, arg15: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V, arg8: K, arg9: V, arg10: K, arg11: V, arg12: K, arg13: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V, arg8: K, arg9: V, arg10: K, arg11: V): $Map<(K), (V)>
public "merge"(arg0: K, arg1: V, arg2: $BiFunction$Type<(any), (any), (any)>): V
public "putAll"(arg0: $Map$Type<(any), (any)>): void
public "putIfAbsent"(arg0: K, arg1: V): V
public "compute"(arg0: K, arg1: $BiFunction$Type<(any), (any), (any)>): V
public static "entry"<K, V>(arg0: K, arg1: V): $Map$Entry<(K), (V)>
public "containsKey"(arg0: any): boolean
public "computeIfAbsent"(arg0: K, arg1: $Function$Type<(any), (any)>): V
public "computeIfPresent"(arg0: K, arg1: $BiFunction$Type<(any), (any), (any)>): V
public static "ofEntries"<K, V>(...arg0: ($Map$Entry$Type<(any), (any)>)[]): $Map<(K), (V)>
get "empty"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LinkedHashMap$Type<K, V> = ($LinkedHashMap<(K), (V)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LinkedHashMap_<K, V> = $LinkedHashMap$Type<(K), (V)>;
}}
declare module "packages/java/util/jar/$JarFile" {
import {$ZipFile, $ZipFile$Type} from "packages/java/util/zip/$ZipFile"
import {$File, $File$Type} from "packages/java/io/$File"
import {$Runtime$Version, $Runtime$Version$Type} from "packages/java/lang/$Runtime$Version"
import {$Enumeration, $Enumeration$Type} from "packages/java/util/$Enumeration"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$InputStream, $InputStream$Type} from "packages/java/io/$InputStream"
import {$JarEntry, $JarEntry$Type} from "packages/java/util/jar/$JarEntry"
import {$Manifest, $Manifest$Type} from "packages/java/util/jar/$Manifest"
import {$ZipEntry, $ZipEntry$Type} from "packages/java/util/zip/$ZipEntry"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $JarFile extends $ZipFile {
static readonly "MANIFEST_NAME": string
static readonly "OPEN_READ": integer
static readonly "OPEN_DELETE": integer

constructor(arg0: string, arg1: boolean)
constructor(arg0: $File$Type, arg1: boolean, arg2: integer, arg3: $Runtime$Version$Type)
constructor(arg0: $File$Type, arg1: boolean)
constructor(arg0: $File$Type, arg1: boolean, arg2: integer)
constructor(arg0: $File$Type)
constructor(arg0: string)

public "stream"(): $Stream<($JarEntry)>
public "entries"(): $Enumeration<($JarEntry)>
public "getInputStream"(arg0: $ZipEntry$Type): $InputStream
public "getManifest"(): $Manifest
public "getEntry"(arg0: string): $ZipEntry
public "getJarEntry"(arg0: string): $JarEntry
public "isMultiRelease"(): boolean
public static "baseVersion"(): $Runtime$Version
public static "runtimeVersion"(): $Runtime$Version
public "getVersion"(): $Runtime$Version
public "versionedStream"(): $Stream<($JarEntry)>
get "manifest"(): $Manifest
get "multiRelease"(): boolean
get "version"(): $Runtime$Version
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JarFile$Type = ($JarFile);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JarFile_ = $JarFile$Type;
}}
declare module "packages/java/util/stream/$LongStream" {
import {$ObjLongConsumer, $ObjLongConsumer$Type} from "packages/java/util/function/$ObjLongConsumer"
import {$LongFunction, $LongFunction$Type} from "packages/java/util/function/$LongFunction"
import {$LongConsumer, $LongConsumer$Type} from "packages/java/util/function/$LongConsumer"
import {$IntStream, $IntStream$Type} from "packages/java/util/stream/$IntStream"
import {$OptionalDouble, $OptionalDouble$Type} from "packages/java/util/$OptionalDouble"
import {$LongBinaryOperator, $LongBinaryOperator$Type} from "packages/java/util/function/$LongBinaryOperator"
import {$LongStream$Builder, $LongStream$Builder$Type} from "packages/java/util/stream/$LongStream$Builder"
import {$LongSummaryStatistics, $LongSummaryStatistics$Type} from "packages/java/util/$LongSummaryStatistics"
import {$LongUnaryOperator, $LongUnaryOperator$Type} from "packages/java/util/function/$LongUnaryOperator"
import {$Spliterator$OfLong, $Spliterator$OfLong$Type} from "packages/java/util/$Spliterator$OfLong"
import {$LongToIntFunction, $LongToIntFunction$Type} from "packages/java/util/function/$LongToIntFunction"
import {$DoubleStream, $DoubleStream$Type} from "packages/java/util/stream/$DoubleStream"
import {$Runnable, $Runnable$Type} from "packages/java/lang/$Runnable"
import {$BiConsumer, $BiConsumer$Type} from "packages/java/util/function/$BiConsumer"
import {$LongToDoubleFunction, $LongToDoubleFunction$Type} from "packages/java/util/function/$LongToDoubleFunction"
import {$LongPredicate, $LongPredicate$Type} from "packages/java/util/function/$LongPredicate"
import {$OptionalLong, $OptionalLong$Type} from "packages/java/util/$OptionalLong"
import {$BaseStream, $BaseStream$Type} from "packages/java/util/stream/$BaseStream"
import {$LongStream$LongMapMultiConsumer, $LongStream$LongMapMultiConsumer$Type} from "packages/java/util/stream/$LongStream$LongMapMultiConsumer"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$LongSupplier, $LongSupplier$Type} from "packages/java/util/function/$LongSupplier"

export interface $LongStream extends $BaseStream<(long), ($LongStream)> {

 "min"(): $OptionalLong
 "max"(): $OptionalLong
 "toArray"(): (long)[]
 "map"(arg0: $LongUnaryOperator$Type): $LongStream
 "collect"<R>(arg0: $Supplier$Type<(R)>, arg1: $ObjLongConsumer$Type<(R)>, arg2: $BiConsumer$Type<(R), (R)>): R
 "count"(): long
 "limit"(arg0: long): $LongStream
 "spliterator"(): $Spliterator$OfLong
 "filter"(arg0: $LongPredicate$Type): $LongStream
 "anyMatch"(arg0: $LongPredicate$Type): boolean
 "flatMap"(arg0: $LongFunction$Type<(any)>): $LongStream
 "forEach"(arg0: $LongConsumer$Type): void
 "findAny"(): $OptionalLong
 "skip"(arg0: long): $LongStream
 "peek"(arg0: $LongConsumer$Type): $LongStream
 "sum"(): long
 "reduce"(arg0: $LongBinaryOperator$Type): $OptionalLong
 "reduce"(arg0: long, arg1: $LongBinaryOperator$Type): long
 "distinct"(): $LongStream
 "findFirst"(): $OptionalLong
 "allMatch"(arg0: $LongPredicate$Type): boolean
 "parallel"(): $LongStream
 "mapToObj"<U>(arg0: $LongFunction$Type<(any)>): $Stream<(U)>
 "sorted"(): $LongStream
 "forEachOrdered"(arg0: $LongConsumer$Type): void
 "mapToInt"(arg0: $LongToIntFunction$Type): $IntStream
 "mapToDouble"(arg0: $LongToDoubleFunction$Type): $DoubleStream
 "mapMulti"(arg0: $LongStream$LongMapMultiConsumer$Type): $LongStream
 "takeWhile"(arg0: $LongPredicate$Type): $LongStream
 "dropWhile"(arg0: $LongPredicate$Type): $LongStream
 "noneMatch"(arg0: $LongPredicate$Type): boolean
 "boxed"(): $Stream<(long)>
 "asDoubleStream"(): $DoubleStream
 "average"(): $OptionalDouble
 "summaryStatistics"(): $LongSummaryStatistics
 "close"(): void
 "onClose"(arg0: $Runnable$Type): $LongStream
 "isParallel"(): boolean
 "unordered"(): $LongStream
}

export namespace $LongStream {
function of(arg0: long): $LongStream
function of(...arg0: (long)[]): $LongStream
function builder(): $LongStream$Builder
function concat(arg0: $LongStream$Type, arg1: $LongStream$Type): $LongStream
function empty(): $LongStream
function range(arg0: long, arg1: long): $LongStream
function iterate(arg0: long, arg1: $LongUnaryOperator$Type): $LongStream
function iterate(arg0: long, arg1: $LongPredicate$Type, arg2: $LongUnaryOperator$Type): $LongStream
function generate(arg0: $LongSupplier$Type): $LongStream
function rangeClosed(arg0: long, arg1: long): $LongStream
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LongStream$Type = ($LongStream);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LongStream_ = $LongStream$Type;
}}
declare module "packages/java/util/$OptionalDouble" {
import {$Throwable, $Throwable$Type} from "packages/java/lang/$Throwable"
import {$DoubleConsumer, $DoubleConsumer$Type} from "packages/java/util/function/$DoubleConsumer"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$DoubleSupplier, $DoubleSupplier$Type} from "packages/java/util/function/$DoubleSupplier"
import {$DoubleStream, $DoubleStream$Type} from "packages/java/util/stream/$DoubleStream"
import {$Runnable, $Runnable$Type} from "packages/java/lang/$Runnable"

export class $OptionalDouble {


public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "isEmpty"(): boolean
public "stream"(): $DoubleStream
public static "of"(arg0: double): $OptionalDouble
public static "empty"(): $OptionalDouble
public "isPresent"(): boolean
public "orElse"(arg0: double): double
public "orElseThrow"<X extends $Throwable>(arg0: $Supplier$Type<(any)>): double
public "orElseThrow"(): double
public "ifPresent"(arg0: $DoubleConsumer$Type): void
public "ifPresentOrElse"(arg0: $DoubleConsumer$Type, arg1: $Runnable$Type): void
public "orElseGet"(arg0: $DoubleSupplier$Type): double
public "getAsDouble"(): double
get "present"(): boolean
get "asDouble"(): double
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $OptionalDouble$Type = ($OptionalDouble);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $OptionalDouble_ = $OptionalDouble$Type;
}}
declare module "packages/java/util/$Spliterators$AbstractSpliterator" {
import {$Comparator, $Comparator$Type} from "packages/java/util/$Comparator"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$Spliterator, $Spliterator$Type} from "packages/java/util/$Spliterator"

export class $Spliterators$AbstractSpliterator<T> implements $Spliterator<(T)> {


public "characteristics"(): integer
public "trySplit"(): $Spliterator<(T)>
public "estimateSize"(): long
public "forEachRemaining"(arg0: $Consumer$Type<(any)>): void
public "tryAdvance"(arg0: $Consumer$Type<(any)>): boolean
public "getExactSizeIfKnown"(): long
public "hasCharacteristics"(arg0: integer): boolean
public "getComparator"(): $Comparator<(any)>
get "exactSizeIfKnown"(): long
get "comparator"(): $Comparator<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Spliterators$AbstractSpliterator$Type<T> = ($Spliterators$AbstractSpliterator<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Spliterators$AbstractSpliterator_<T> = $Spliterators$AbstractSpliterator$Type<(T)>;
}}
declare module "packages/java/util/$List" {
import {$Comparator, $Comparator$Type} from "packages/java/util/$Comparator"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$UnaryOperator, $UnaryOperator$Type} from "packages/java/util/function/$UnaryOperator"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$IntFunction, $IntFunction$Type} from "packages/java/util/function/$IntFunction"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$ListIterator, $ListIterator$Type} from "packages/java/util/$ListIterator"
import {$Iterator, $Iterator$Type} from "packages/java/util/$Iterator"
import {$Spliterator, $Spliterator$Type} from "packages/java/util/$Spliterator"

export interface $List<E> extends $Collection<(E)> {

 "add"(arg0: integer, arg1: E): void
 "add"(arg0: E): boolean
 "remove"(arg0: any): boolean
 "remove"(arg0: integer): E
 "get"(arg0: integer): E
 "equals"(arg0: any): boolean
 "hashCode"(): integer
 "indexOf"(arg0: any): integer
 "clear"(): void
 "lastIndexOf"(arg0: any): integer
 "isEmpty"(): boolean
 "replaceAll"(arg0: $UnaryOperator$Type<(E)>): void
 "size"(): integer
 "subList"(arg0: integer, arg1: integer): $List<(E)>
 "toArray"(): (any)[]
 "toArray"<T>(arg0: (T)[]): (T)[]
 "iterator"(): $Iterator<(E)>
 "contains"(arg0: any): boolean
 "spliterator"(): $Spliterator<(E)>
 "addAll"(arg0: integer, arg1: $Collection$Type<(any)>): boolean
 "addAll"(arg0: $Collection$Type<(any)>): boolean
 "set"(arg0: integer, arg1: E): E
 "sort"(arg0: $Comparator$Type<(any)>): void
 "removeAll"(arg0: $Collection$Type<(any)>): boolean
 "retainAll"(arg0: $Collection$Type<(any)>): boolean
 "listIterator"(arg0: integer): $ListIterator<(E)>
 "listIterator"(): $ListIterator<(E)>
 "containsAll"(arg0: $Collection$Type<(any)>): boolean
 "toArray"<T>(arg0: $IntFunction$Type<((T)[])>): (T)[]
 "stream"(): $Stream<(E)>
 "removeIf"(arg0: $Predicate$Type<(any)>): boolean
 "parallelStream"(): $Stream<(E)>
 "forEach"(arg0: $Consumer$Type<(any)>): void
}

export namespace $List {
function copyOf<E>(arg0: $Collection$Type<(any)>): $List<(E)>
function of<E>(arg0: E, arg1: E, arg2: E, arg3: E): $List<(E)>
function of<E>(arg0: E, arg1: E, arg2: E): $List<(E)>
function of<E>(arg0: E, arg1: E): $List<(E)>
function of<E>(arg0: E): $List<(E)>
function of<E>(): $List<(E)>
function of<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E, arg5: E, arg6: E, arg7: E): $List<(E)>
function of<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E, arg5: E, arg6: E): $List<(E)>
function of<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E, arg5: E): $List<(E)>
function of<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E): $List<(E)>
function of<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E, arg5: E, arg6: E, arg7: E, arg8: E, arg9: E): $List<(E)>
function of<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E, arg5: E, arg6: E, arg7: E, arg8: E): $List<(E)>
function of<E>(...arg0: (E)[]): $List<(E)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $List$Type<E> = ((E)[]) | ($List<(E)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $List_<E> = $List$Type<(E)>;
}}
declare module "packages/java/util/function/$ObjDoubleConsumer" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $ObjDoubleConsumer<T> {

 "accept"(arg0: T, arg1: double): void

(arg0: T, arg1: double): void
}

export namespace $ObjDoubleConsumer {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ObjDoubleConsumer$Type<T> = ($ObjDoubleConsumer<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ObjDoubleConsumer_<T> = $ObjDoubleConsumer$Type<(T)>;
}}
declare module "packages/java/util/$EnumMap" {
import {$Serializable, $Serializable$Type} from "packages/java/io/$Serializable"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$Cloneable, $Cloneable$Type} from "packages/java/lang/$Cloneable"
import {$AbstractMap, $AbstractMap$Type} from "packages/java/util/$AbstractMap"
import {$Map, $Map$Type} from "packages/java/util/$Map"
import {$Map$Entry, $Map$Entry$Type} from "packages/java/util/$Map$Entry"

export class $EnumMap<K extends $Enum<(K)>, V> extends $AbstractMap<(K), (V)> implements $Serializable, $Cloneable {

constructor(arg0: $Class$Type<(K)>)
constructor(arg0: $Map$Type<(K), (any)>)
constructor(arg0: $EnumMap$Type<(K), (any)>)

public "remove"(arg0: any): V
public "get"(arg0: any): V
public "put"(arg0: K, arg1: V): V
public "equals"(arg0: any): boolean
public "values"(): $Collection<(V)>
public "hashCode"(): integer
public "clear"(): void
public "size"(): integer
public "entrySet"(): $Set<($Map$Entry<(K), (V)>)>
public "putAll"(arg0: $Map$Type<(any), (any)>): void
public "containsKey"(arg0: any): boolean
public "keySet"(): $Set<(K)>
public "containsValue"(arg0: any): boolean
public static "copyOf"<K, V>(arg0: $Map$Type<(any), (any)>): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V, arg8: K, arg9: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V): $Map<(K), (V)>
public static "of"<K, V>(): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V, arg8: K, arg9: V, arg10: K, arg11: V, arg12: K, arg13: V, arg14: K, arg15: V, arg16: K, arg17: V, arg18: K, arg19: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V, arg8: K, arg9: V, arg10: K, arg11: V, arg12: K, arg13: V, arg14: K, arg15: V, arg16: K, arg17: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V, arg8: K, arg9: V, arg10: K, arg11: V, arg12: K, arg13: V, arg14: K, arg15: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V, arg8: K, arg9: V, arg10: K, arg11: V, arg12: K, arg13: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V, arg8: K, arg9: V, arg10: K, arg11: V): $Map<(K), (V)>
public static "entry"<K, V>(arg0: K, arg1: V): $Map$Entry<(K), (V)>
public static "ofEntries"<K, V>(...arg0: ($Map$Entry$Type<(any), (any)>)[]): $Map<(K), (V)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EnumMap$Type<K, V> = ($EnumMap<(K), (V)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EnumMap_<K, V> = $EnumMap$Type<(K), (V)>;
}}
declare module "packages/java/util/$NoSuchElementException" {
import {$Throwable, $Throwable$Type} from "packages/java/lang/$Throwable"
import {$RuntimeException, $RuntimeException$Type} from "packages/java/lang/$RuntimeException"

export class $NoSuchElementException extends $RuntimeException {

constructor(arg0: string)
constructor(arg0: $Throwable$Type)
constructor(arg0: string, arg1: $Throwable$Type)
constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NoSuchElementException$Type = ($NoSuchElementException);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NoSuchElementException_ = $NoSuchElementException$Type;
}}
declare module "packages/java/util/function/$IntPredicate" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $IntPredicate {

 "test"(arg0: integer): boolean
 "or"(arg0: $IntPredicate$Type): $IntPredicate
 "negate"(): $IntPredicate
 "and"(arg0: $IntPredicate$Type): $IntPredicate

(arg0: integer): boolean
}

export namespace $IntPredicate {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IntPredicate$Type = ($IntPredicate);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IntPredicate_ = $IntPredicate$Type;
}}
declare module "packages/java/util/function/$IntToDoubleFunction" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $IntToDoubleFunction {

 "applyAsDouble"(arg0: integer): double

(arg0: integer): double
}

export namespace $IntToDoubleFunction {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IntToDoubleFunction$Type = ($IntToDoubleFunction);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IntToDoubleFunction_ = $IntToDoubleFunction$Type;
}}
declare module "packages/java/util/$SortedMap" {
import {$Comparator, $Comparator$Type} from "packages/java/util/$Comparator"
import {$BiConsumer, $BiConsumer$Type} from "packages/java/util/function/$BiConsumer"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$BiFunction, $BiFunction$Type} from "packages/java/util/function/$BiFunction"
import {$Map$Entry, $Map$Entry$Type} from "packages/java/util/$Map$Entry"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export interface $SortedMap<K, V> extends $Map<(K), (V)> {

 "values"(): $Collection<(V)>
 "entrySet"(): $Set<($Map$Entry<(K), (V)>)>
 "keySet"(): $Set<(K)>
 "comparator"(): $Comparator<(any)>
 "firstKey"(): K
 "subMap"(arg0: K, arg1: K): $SortedMap<(K), (V)>
 "headMap"(arg0: K): $SortedMap<(K), (V)>
 "tailMap"(arg0: K): $SortedMap<(K), (V)>
 "lastKey"(): K
 "remove"(arg0: any): V
 "remove"(arg0: any, arg1: any): boolean
 "get"(arg0: any): V
 "put"(arg0: K, arg1: V): V
 "equals"(arg0: any): boolean
 "hashCode"(): integer
 "clear"(): void
 "isEmpty"(): boolean
 "replace"(arg0: K, arg1: V): V
 "replace"(arg0: K, arg1: V, arg2: V): boolean
 "replaceAll"(arg0: $BiFunction$Type<(any), (any), (any)>): void
 "size"(): integer
 "merge"(arg0: K, arg1: V, arg2: $BiFunction$Type<(any), (any), (any)>): V
 "putAll"(arg0: $Map$Type<(any), (any)>): void
 "putIfAbsent"(arg0: K, arg1: V): V
 "compute"(arg0: K, arg1: $BiFunction$Type<(any), (any), (any)>): V
 "forEach"(arg0: $BiConsumer$Type<(any), (any)>): void
 "containsKey"(arg0: any): boolean
 "computeIfAbsent"(arg0: K, arg1: $Function$Type<(any), (any)>): V
 "containsValue"(arg0: any): boolean
 "getOrDefault"(arg0: any, arg1: V): V
 "computeIfPresent"(arg0: K, arg1: $BiFunction$Type<(any), (any), (any)>): V
}

export namespace $SortedMap {
function copyOf<K, V>(arg0: $Map$Type<(any), (any)>): $Map<(K), (V)>
function of<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V, arg8: K, arg9: V): $Map<(K), (V)>
function of<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V): $Map<(K), (V)>
function of<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V): $Map<(K), (V)>
function of<K, V>(): $Map<(K), (V)>
function of<K, V>(arg0: K, arg1: V, arg2: K, arg3: V): $Map<(K), (V)>
function of<K, V>(arg0: K, arg1: V): $Map<(K), (V)>
function of<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V, arg8: K, arg9: V, arg10: K, arg11: V, arg12: K, arg13: V, arg14: K, arg15: V, arg16: K, arg17: V, arg18: K, arg19: V): $Map<(K), (V)>
function of<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V, arg8: K, arg9: V, arg10: K, arg11: V, arg12: K, arg13: V, arg14: K, arg15: V, arg16: K, arg17: V): $Map<(K), (V)>
function of<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V, arg8: K, arg9: V, arg10: K, arg11: V, arg12: K, arg13: V, arg14: K, arg15: V): $Map<(K), (V)>
function of<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V, arg8: K, arg9: V, arg10: K, arg11: V, arg12: K, arg13: V): $Map<(K), (V)>
function of<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V, arg8: K, arg9: V, arg10: K, arg11: V): $Map<(K), (V)>
function entry<K, V>(arg0: K, arg1: V): $Map$Entry<(K), (V)>
function ofEntries<K, V>(...arg0: ($Map$Entry$Type<(any), (any)>)[]): $Map<(K), (V)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SortedMap$Type<K, V> = ($SortedMap<(K), (V)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SortedMap_<K, V> = $SortedMap$Type<(K), (V)>;
}}
declare module "packages/java/util/function/$LongConsumer" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $LongConsumer {

 "accept"(arg0: long): void
 "andThen"(arg0: $LongConsumer$Type): $LongConsumer

(arg0: long): void
}

export namespace $LongConsumer {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LongConsumer$Type = ($LongConsumer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LongConsumer_ = $LongConsumer$Type;
}}
declare module "packages/java/util/$Spliterator" {
import {$Comparator, $Comparator$Type} from "packages/java/util/$Comparator"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"

export interface $Spliterator<T> {

 "forEachRemaining"(arg0: $Consumer$Type<(any)>): void
 "characteristics"(): integer
 "tryAdvance"(arg0: $Consumer$Type<(any)>): boolean
 "trySplit"(): $Spliterator<(T)>
 "estimateSize"(): long
 "getExactSizeIfKnown"(): long
 "hasCharacteristics"(arg0: integer): boolean
 "getComparator"(): $Comparator<(any)>
}

export namespace $Spliterator {
const ORDERED: integer
const DISTINCT: integer
const SORTED: integer
const SIZED: integer
const NONNULL: integer
const IMMUTABLE: integer
const CONCURRENT: integer
const SUBSIZED: integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Spliterator$Type<T> = ($Spliterator<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Spliterator_<T> = $Spliterator$Type<(T)>;
}}
declare module "packages/java/util/concurrent/$Executor" {
import {$Runnable, $Runnable$Type} from "packages/java/lang/$Runnable"

export interface $Executor {

 "execute"(arg0: $Runnable$Type): void

(arg0: $Runnable$Type): void
}

export namespace $Executor {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Executor$Type = ($Executor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Executor_ = $Executor$Type;
}}
declare module "packages/java/util/$Stack" {
import {$Vector, $Vector$Type} from "packages/java/util/$Vector"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$List, $List$Type} from "packages/java/util/$List"

export class $Stack<E> extends $Vector<(E)> {

constructor()

public "empty"(): boolean
public "peek"(): E
public "search"(arg0: any): integer
public "push"(arg0: E): E
public "pop"(): E
public static "copyOf"<E>(arg0: $Collection$Type<(any)>): $List<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E): $List<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E): $List<(E)>
public static "of"<E>(arg0: E, arg1: E): $List<(E)>
public static "of"<E>(arg0: E): $List<(E)>
public static "of"<E>(): $List<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E, arg5: E, arg6: E, arg7: E): $List<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E, arg5: E, arg6: E): $List<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E, arg5: E): $List<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E): $List<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E, arg5: E, arg6: E, arg7: E, arg8: E, arg9: E): $List<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E, arg5: E, arg6: E, arg7: E, arg8: E): $List<(E)>
public static "of"<E>(...arg0: (E)[]): $List<(E)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Stack$Type<E> = ($Stack<(E)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Stack_<E> = $Stack$Type<(E)>;
}}
declare module "packages/java/util/concurrent/$ConcurrentHashMap" {
import {$ConcurrentMap, $ConcurrentMap$Type} from "packages/java/util/concurrent/$ConcurrentMap"
import {$Serializable, $Serializable$Type} from "packages/java/io/$Serializable"
import {$ToIntFunction, $ToIntFunction$Type} from "packages/java/util/function/$ToIntFunction"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$LongBinaryOperator, $LongBinaryOperator$Type} from "packages/java/util/function/$LongBinaryOperator"
import {$ConcurrentHashMap$KeySetView, $ConcurrentHashMap$KeySetView$Type} from "packages/java/util/concurrent/$ConcurrentHashMap$KeySetView"
import {$AbstractMap, $AbstractMap$Type} from "packages/java/util/$AbstractMap"
import {$IntBinaryOperator, $IntBinaryOperator$Type} from "packages/java/util/function/$IntBinaryOperator"
import {$BiFunction, $BiFunction$Type} from "packages/java/util/function/$BiFunction"
import {$ToIntBiFunction, $ToIntBiFunction$Type} from "packages/java/util/function/$ToIntBiFunction"
import {$BiConsumer, $BiConsumer$Type} from "packages/java/util/function/$BiConsumer"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$Enumeration, $Enumeration$Type} from "packages/java/util/$Enumeration"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$DoubleBinaryOperator, $DoubleBinaryOperator$Type} from "packages/java/util/function/$DoubleBinaryOperator"
import {$ToLongBiFunction, $ToLongBiFunction$Type} from "packages/java/util/function/$ToLongBiFunction"
import {$ToLongFunction, $ToLongFunction$Type} from "packages/java/util/function/$ToLongFunction"
import {$ToDoubleFunction, $ToDoubleFunction$Type} from "packages/java/util/function/$ToDoubleFunction"
import {$Map, $Map$Type} from "packages/java/util/$Map"
import {$Map$Entry, $Map$Entry$Type} from "packages/java/util/$Map$Entry"
import {$ToDoubleBiFunction, $ToDoubleBiFunction$Type} from "packages/java/util/function/$ToDoubleBiFunction"

export class $ConcurrentHashMap<K, V> extends $AbstractMap<(K), (V)> implements $ConcurrentMap<(K), (V)>, $Serializable {

constructor(arg0: integer)
constructor(arg0: $Map$Type<(any), (any)>)
constructor(arg0: integer, arg1: float)
constructor(arg0: integer, arg1: float, arg2: integer)
constructor()

public "remove"(arg0: any): V
public "remove"(arg0: any, arg1: any): boolean
public "get"(arg0: any): V
public "put"(arg0: K, arg1: V): V
public "equals"(arg0: any): boolean
public "toString"(): string
public "values"(): $Collection<(V)>
public "hashCode"(): integer
public "clear"(): void
public "isEmpty"(): boolean
public "replace"(arg0: K, arg1: V): V
public "replace"(arg0: K, arg1: V, arg2: V): boolean
public "replaceAll"(arg0: $BiFunction$Type<(any), (any), (any)>): void
public "size"(): integer
public "contains"(arg0: any): boolean
public "elements"(): $Enumeration<(V)>
public "merge"(arg0: K, arg1: V, arg2: $BiFunction$Type<(any), (any), (any)>): V
public "entrySet"(): $Set<($Map$Entry<(K), (V)>)>
public "putAll"(arg0: $Map$Type<(any), (any)>): void
public "putIfAbsent"(arg0: K, arg1: V): V
public "compute"(arg0: K, arg1: $BiFunction$Type<(any), (any), (any)>): V
public "forEach"(arg0: long, arg1: $BiConsumer$Type<(any), (any)>): void
public "forEach"<U>(arg0: long, arg1: $BiFunction$Type<(any), (any), (any)>, arg2: $Consumer$Type<(any)>): void
public "forEach"(arg0: $BiConsumer$Type<(any), (any)>): void
public "containsKey"(arg0: any): boolean
public "computeIfAbsent"(arg0: K, arg1: $Function$Type<(any), (any)>): V
public "keys"(): $Enumeration<(K)>
public "keySet"(arg0: V): $ConcurrentHashMap$KeySetView<(K), (V)>
public "containsValue"(arg0: any): boolean
public "getOrDefault"(arg0: any, arg1: V): V
public "computeIfPresent"(arg0: K, arg1: $BiFunction$Type<(any), (any), (any)>): V
public "mappingCount"(): long
public static "newKeySet"<K>(arg0: integer): $ConcurrentHashMap$KeySetView<(K), (boolean)>
public static "newKeySet"<K>(): $ConcurrentHashMap$KeySetView<(K), (boolean)>
public "search"<U>(arg0: long, arg1: $BiFunction$Type<(any), (any), (any)>): U
public "reduce"<U>(arg0: long, arg1: $BiFunction$Type<(any), (any), (any)>, arg2: $BiFunction$Type<(any), (any), (any)>): U
public "reduceToDouble"(arg0: long, arg1: $ToDoubleBiFunction$Type<(any), (any)>, arg2: double, arg3: $DoubleBinaryOperator$Type): double
public "reduceToLong"(arg0: long, arg1: $ToLongBiFunction$Type<(any), (any)>, arg2: long, arg3: $LongBinaryOperator$Type): long
public "reduceToInt"(arg0: long, arg1: $ToIntBiFunction$Type<(any), (any)>, arg2: integer, arg3: $IntBinaryOperator$Type): integer
public "forEachKey"(arg0: long, arg1: $Consumer$Type<(any)>): void
public "forEachKey"<U>(arg0: long, arg1: $Function$Type<(any), (any)>, arg2: $Consumer$Type<(any)>): void
public "searchKeys"<U>(arg0: long, arg1: $Function$Type<(any), (any)>): U
public "reduceKeys"(arg0: long, arg1: $BiFunction$Type<(any), (any), (any)>): K
public "reduceKeys"<U>(arg0: long, arg1: $Function$Type<(any), (any)>, arg2: $BiFunction$Type<(any), (any), (any)>): U
public "reduceKeysToDouble"(arg0: long, arg1: $ToDoubleFunction$Type<(any)>, arg2: double, arg3: $DoubleBinaryOperator$Type): double
public "reduceKeysToLong"(arg0: long, arg1: $ToLongFunction$Type<(any)>, arg2: long, arg3: $LongBinaryOperator$Type): long
public "reduceKeysToInt"(arg0: long, arg1: $ToIntFunction$Type<(any)>, arg2: integer, arg3: $IntBinaryOperator$Type): integer
public "forEachValue"<U>(arg0: long, arg1: $Function$Type<(any), (any)>, arg2: $Consumer$Type<(any)>): void
public "forEachValue"(arg0: long, arg1: $Consumer$Type<(any)>): void
public "searchValues"<U>(arg0: long, arg1: $Function$Type<(any), (any)>): U
public "reduceValues"(arg0: long, arg1: $BiFunction$Type<(any), (any), (any)>): V
public "reduceValues"<U>(arg0: long, arg1: $Function$Type<(any), (any)>, arg2: $BiFunction$Type<(any), (any), (any)>): U
public "reduceValuesToDouble"(arg0: long, arg1: $ToDoubleFunction$Type<(any)>, arg2: double, arg3: $DoubleBinaryOperator$Type): double
public "reduceValuesToLong"(arg0: long, arg1: $ToLongFunction$Type<(any)>, arg2: long, arg3: $LongBinaryOperator$Type): long
public "reduceValuesToInt"(arg0: long, arg1: $ToIntFunction$Type<(any)>, arg2: integer, arg3: $IntBinaryOperator$Type): integer
public "forEachEntry"(arg0: long, arg1: $Consumer$Type<(any)>): void
public "forEachEntry"<U>(arg0: long, arg1: $Function$Type<($Map$Entry$Type<(K), (V)>), (any)>, arg2: $Consumer$Type<(any)>): void
public "searchEntries"<U>(arg0: long, arg1: $Function$Type<($Map$Entry$Type<(K), (V)>), (any)>): U
public "reduceEntries"(arg0: long, arg1: $BiFunction$Type<($Map$Entry$Type<(K), (V)>), ($Map$Entry$Type<(K), (V)>), (any)>): $Map$Entry<(K), (V)>
public "reduceEntries"<U>(arg0: long, arg1: $Function$Type<($Map$Entry$Type<(K), (V)>), (any)>, arg2: $BiFunction$Type<(any), (any), (any)>): U
public "reduceEntriesToDouble"(arg0: long, arg1: $ToDoubleFunction$Type<($Map$Entry$Type<(K), (V)>)>, arg2: double, arg3: $DoubleBinaryOperator$Type): double
public "reduceEntriesToLong"(arg0: long, arg1: $ToLongFunction$Type<($Map$Entry$Type<(K), (V)>)>, arg2: long, arg3: $LongBinaryOperator$Type): long
public "reduceEntriesToInt"(arg0: long, arg1: $ToIntFunction$Type<($Map$Entry$Type<(K), (V)>)>, arg2: integer, arg3: $IntBinaryOperator$Type): integer
public static "copyOf"<K, V>(arg0: $Map$Type<(any), (any)>): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V, arg8: K, arg9: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V): $Map<(K), (V)>
public static "of"<K, V>(): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V, arg8: K, arg9: V, arg10: K, arg11: V, arg12: K, arg13: V, arg14: K, arg15: V, arg16: K, arg17: V, arg18: K, arg19: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V, arg8: K, arg9: V, arg10: K, arg11: V, arg12: K, arg13: V, arg14: K, arg15: V, arg16: K, arg17: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V, arg8: K, arg9: V, arg10: K, arg11: V, arg12: K, arg13: V, arg14: K, arg15: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V, arg8: K, arg9: V, arg10: K, arg11: V, arg12: K, arg13: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V, arg8: K, arg9: V, arg10: K, arg11: V): $Map<(K), (V)>
public static "entry"<K, V>(arg0: K, arg1: V): $Map$Entry<(K), (V)>
public static "ofEntries"<K, V>(...arg0: ($Map$Entry$Type<(any), (any)>)[]): $Map<(K), (V)>
get "empty"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ConcurrentHashMap$Type<K, V> = ($ConcurrentHashMap<(K), (V)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ConcurrentHashMap_<K, V> = $ConcurrentHashMap$Type<(K), (V)>;
}}
declare module "packages/java/util/$EnumSet" {
import {$Serializable, $Serializable$Type} from "packages/java/io/$Serializable"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$Cloneable, $Cloneable$Type} from "packages/java/lang/$Cloneable"
import {$AbstractSet, $AbstractSet$Type} from "packages/java/util/$AbstractSet"
import {$Iterator, $Iterator$Type} from "packages/java/util/$Iterator"

export class $EnumSet<E extends $Enum<(E)>> extends $AbstractSet<(E)> implements $Cloneable, $Serializable {


public "clone"(): $EnumSet<(E)>
public static "copyOf"<E extends $Enum<(E)>>(arg0: $Collection$Type<(E)>): $EnumSet<(E)>
public static "copyOf"<E extends $Enum<(E)>>(arg0: $EnumSet$Type<(E)>): $EnumSet<(E)>
public static "of"<E extends $Enum<(E)>>(arg0: E, arg1: E, arg2: E): $EnumSet<(E)>
public static "of"<E extends $Enum<(E)>>(arg0: E, arg1: E): $EnumSet<(E)>
public static "of"<E extends $Enum<(E)>>(arg0: E, arg1: E, arg2: E, arg3: E): $EnumSet<(E)>
public static "of"<E extends $Enum<(E)>>(arg0: E, ...arg1: (E)[]): $EnumSet<(E)>
public static "of"<E extends $Enum<(E)>>(arg0: E): $EnumSet<(E)>
public static "of"<E extends $Enum<(E)>>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E): $EnumSet<(E)>
public static "noneOf"<E extends $Enum<(E)>>(arg0: $Class$Type<(E)>): $EnumSet<(E)>
public static "range"<E extends $Enum<(E)>>(arg0: E, arg1: E): $EnumSet<(E)>
public static "allOf"<E extends $Enum<(E)>>(arg0: $Class$Type<(E)>): $EnumSet<(E)>
public static "complementOf"<E extends $Enum<(E)>>(arg0: $EnumSet$Type<(E)>): $EnumSet<(E)>
public "add"(arg0: E): boolean
public "remove"(arg0: any): boolean
public "clear"(): void
public "isEmpty"(): boolean
public "size"(): integer
public "toArray"<T>(arg0: (T)[]): (T)[]
public "toArray"(): (any)[]
public "iterator"(): $Iterator<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E, arg5: E): $Set<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E): $Set<(E)>
public static "of"<E>(arg0: E): $Set<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E): $Set<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E, arg5: E, arg6: E, arg7: E, arg8: E, arg9: E): $Set<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E, arg5: E, arg6: E, arg7: E, arg8: E): $Set<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E, arg5: E, arg6: E, arg7: E): $Set<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E, arg5: E, arg6: E): $Set<(E)>
public static "of"<E>(...arg0: (E)[]): $Set<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E): $Set<(E)>
public static "of"<E>(): $Set<(E)>
public static "of"<E>(arg0: E, arg1: E): $Set<(E)>
public "contains"(arg0: any): boolean
public "addAll"(arg0: $Collection$Type<(any)>): boolean
public "retainAll"(arg0: $Collection$Type<(any)>): boolean
public "containsAll"(arg0: $Collection$Type<(any)>): boolean
get "empty"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EnumSet$Type<E> = ($EnumSet<(E)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EnumSet_<E> = $EnumSet$Type<(E)>;
}}
declare module "packages/java/util/concurrent/$ExecutorService" {
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Future, $Future$Type} from "packages/java/util/concurrent/$Future"
import {$Callable, $Callable$Type} from "packages/java/util/concurrent/$Callable"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Executor, $Executor$Type} from "packages/java/util/concurrent/$Executor"
import {$TimeUnit, $TimeUnit$Type} from "packages/java/util/concurrent/$TimeUnit"
import {$Runnable, $Runnable$Type} from "packages/java/lang/$Runnable"

export interface $ExecutorService extends $Executor {

 "shutdown"(): void
 "isShutdown"(): boolean
 "submit"(arg0: $Runnable$Type): $Future<(any)>
 "submit"<T>(arg0: $Runnable$Type, arg1: T): $Future<(T)>
 "submit"<T>(arg0: $Callable$Type<(T)>): $Future<(T)>
 "invokeAny"<T>(arg0: $Collection$Type<(any)>, arg1: long, arg2: $TimeUnit$Type): T
 "invokeAny"<T>(arg0: $Collection$Type<(any)>): T
 "invokeAll"<T>(arg0: $Collection$Type<(any)>): $List<($Future<(T)>)>
 "invokeAll"<T>(arg0: $Collection$Type<(any)>, arg1: long, arg2: $TimeUnit$Type): $List<($Future<(T)>)>
 "shutdownNow"(): $List<($Runnable)>
 "isTerminated"(): boolean
 "awaitTermination"(arg0: long, arg1: $TimeUnit$Type): boolean
 "execute"(arg0: $Runnable$Type): void
}

export namespace $ExecutorService {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ExecutorService$Type = ($ExecutorService);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ExecutorService_ = $ExecutorService$Type;
}}
declare module "packages/java/util/$Spliterator$OfPrimitive" {
import {$Comparator, $Comparator$Type} from "packages/java/util/$Comparator"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$Spliterator, $Spliterator$Type} from "packages/java/util/$Spliterator"

export interface $Spliterator$OfPrimitive<T, T_CONS, T_SPLITR extends $Spliterator$OfPrimitive<(T), (T_CONS), (T_SPLITR)>> extends $Spliterator<(T)> {

 "forEachRemaining"(arg0: T_CONS): void
 "tryAdvance"(arg0: T_CONS): boolean
 "forEachRemaining"(arg0: $Consumer$Type<(any)>): void
 "characteristics"(): integer
 "tryAdvance"(arg0: $Consumer$Type<(any)>): boolean
 "estimateSize"(): long
 "getExactSizeIfKnown"(): long
 "hasCharacteristics"(arg0: integer): boolean
 "getComparator"(): $Comparator<(any)>
}

export namespace $Spliterator$OfPrimitive {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Spliterator$OfPrimitive$Type<T, T_CONS, T_SPLITR> = ($Spliterator$OfPrimitive<(T), (T_CONS), (T_SPLITR)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Spliterator$OfPrimitive_<T, T_CONS, T_SPLITR> = $Spliterator$OfPrimitive$Type<(T), (T_CONS), (T_SPLITR)>;
}}
declare module "packages/java/util/function/$DoubleToLongFunction" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $DoubleToLongFunction {

 "applyAsLong"(arg0: double): long

(arg0: double): long
}

export namespace $DoubleToLongFunction {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DoubleToLongFunction$Type = ($DoubleToLongFunction);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DoubleToLongFunction_ = $DoubleToLongFunction$Type;
}}
declare module "packages/java/util/regex/$Matcher" {
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Pattern, $Pattern$Type} from "packages/java/util/regex/$Pattern"
import {$StringBuffer, $StringBuffer$Type} from "packages/java/lang/$StringBuffer"
import {$StringBuilder, $StringBuilder$Type} from "packages/java/lang/$StringBuilder"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$MatchResult, $MatchResult$Type} from "packages/java/util/regex/$MatchResult"

export class $Matcher implements $MatchResult {


public "group"(arg0: integer): string
public "group"(): string
public "group"(arg0: string): string
public "toString"(): string
public "matches"(): boolean
public "replaceFirst"(arg0: string): string
public "replaceFirst"(arg0: $Function$Type<($MatchResult$Type), (string)>): string
public "replaceAll"(arg0: string): string
public "replaceAll"(arg0: $Function$Type<($MatchResult$Type), (string)>): string
public "start"(): integer
public "start"(arg0: string): integer
public "start"(arg0: integer): integer
public "end"(arg0: string): integer
public "end"(): integer
public "end"(arg0: integer): integer
public "find"(): boolean
public "find"(arg0: integer): boolean
public "reset"(): $Matcher
public "reset"(arg0: charseq): $Matcher
public "results"(): $Stream<($MatchResult)>
public "pattern"(): $Pattern
public "region"(arg0: integer, arg1: integer): $Matcher
public "groupCount"(): integer
public "toMatchResult"(): $MatchResult
public "appendReplacement"(arg0: $StringBuffer$Type, arg1: string): $Matcher
public "appendReplacement"(arg0: $StringBuilder$Type, arg1: string): $Matcher
public "appendTail"(arg0: $StringBuilder$Type): $StringBuilder
public "appendTail"(arg0: $StringBuffer$Type): $StringBuffer
public "regionStart"(): integer
public "regionEnd"(): integer
public "hitEnd"(): boolean
public "requireEnd"(): boolean
public "usePattern"(arg0: $Pattern$Type): $Matcher
public "lookingAt"(): boolean
public static "quoteReplacement"(arg0: string): string
public "hasTransparentBounds"(): boolean
public "useTransparentBounds"(arg0: boolean): $Matcher
public "hasAnchoringBounds"(): boolean
public "useAnchoringBounds"(arg0: boolean): $Matcher
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Matcher$Type = ($Matcher);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Matcher_ = $Matcher$Type;
}}
declare module "packages/java/util/$Map" {
import {$BiConsumer, $BiConsumer$Type} from "packages/java/util/function/$BiConsumer"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$BiFunction, $BiFunction$Type} from "packages/java/util/function/$BiFunction"
import {$Map$Entry, $Map$Entry$Type} from "packages/java/util/$Map$Entry"

export interface $Map<K, V> {

 "remove"(arg0: any): V
 "remove"(arg0: any, arg1: any): boolean
 "get"(arg0: any): V
 "put"(arg0: K, arg1: V): V
 "equals"(arg0: any): boolean
 "values"(): $Collection<(V)>
 "hashCode"(): integer
 "clear"(): void
 "isEmpty"(): boolean
 "replace"(arg0: K, arg1: V): V
 "replace"(arg0: K, arg1: V, arg2: V): boolean
 "replaceAll"(arg0: $BiFunction$Type<(any), (any), (any)>): void
 "size"(): integer
 "merge"(arg0: K, arg1: V, arg2: $BiFunction$Type<(any), (any), (any)>): V
 "entrySet"(): $Set<($Map$Entry<(K), (V)>)>
 "putAll"(arg0: $Map$Type<(any), (any)>): void
 "putIfAbsent"(arg0: K, arg1: V): V
 "compute"(arg0: K, arg1: $BiFunction$Type<(any), (any), (any)>): V
 "forEach"(arg0: $BiConsumer$Type<(any), (any)>): void
 "containsKey"(arg0: any): boolean
 "computeIfAbsent"(arg0: K, arg1: $Function$Type<(any), (any)>): V
 "keySet"(): $Set<(K)>
 "containsValue"(arg0: any): boolean
 "getOrDefault"(arg0: any, arg1: V): V
 "computeIfPresent"(arg0: K, arg1: $BiFunction$Type<(any), (any), (any)>): V
}

export namespace $Map {
function copyOf<K, V>(arg0: $Map$Type<(any), (any)>): $Map<(K), (V)>
function of<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V, arg8: K, arg9: V): $Map<(K), (V)>
function of<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V): $Map<(K), (V)>
function of<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V): $Map<(K), (V)>
function of<K, V>(): $Map<(K), (V)>
function of<K, V>(arg0: K, arg1: V, arg2: K, arg3: V): $Map<(K), (V)>
function of<K, V>(arg0: K, arg1: V): $Map<(K), (V)>
function of<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V, arg8: K, arg9: V, arg10: K, arg11: V, arg12: K, arg13: V, arg14: K, arg15: V, arg16: K, arg17: V, arg18: K, arg19: V): $Map<(K), (V)>
function of<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V, arg8: K, arg9: V, arg10: K, arg11: V, arg12: K, arg13: V, arg14: K, arg15: V, arg16: K, arg17: V): $Map<(K), (V)>
function of<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V, arg8: K, arg9: V, arg10: K, arg11: V, arg12: K, arg13: V, arg14: K, arg15: V): $Map<(K), (V)>
function of<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V, arg8: K, arg9: V, arg10: K, arg11: V, arg12: K, arg13: V): $Map<(K), (V)>
function of<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V, arg8: K, arg9: V, arg10: K, arg11: V): $Map<(K), (V)>
function entry<K, V>(arg0: K, arg1: V): $Map$Entry<(K), (V)>
function ofEntries<K, V>(...arg0: ($Map$Entry$Type<(any), (any)>)[]): $Map<(K), (V)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Map$Type<K, V> = ({[key: string]: V}) | ($Map<(K), (V)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Map_<K, V> = $Map$Type<(K), (V)>;
}}
declare module "packages/java/util/logging/$Handler" {
import {$Filter, $Filter$Type} from "packages/java/util/logging/$Filter"
import {$ErrorManager, $ErrorManager$Type} from "packages/java/util/logging/$ErrorManager"
import {$LogRecord, $LogRecord$Type} from "packages/java/util/logging/$LogRecord"
import {$Formatter, $Formatter$Type} from "packages/java/util/logging/$Formatter"
import {$Level, $Level$Type} from "packages/java/util/logging/$Level"

export class $Handler {


public "flush"(): void
public "close"(): void
public "getEncoding"(): string
public "publish"(arg0: $LogRecord$Type): void
public "isLoggable"(arg0: $LogRecord$Type): boolean
public "getFilter"(): $Filter
public "getLevel"(): $Level
public "setFormatter"(arg0: $Formatter$Type): void
public "getFormatter"(): $Formatter
public "setEncoding"(arg0: string): void
public "setFilter"(arg0: $Filter$Type): void
public "setErrorManager"(arg0: $ErrorManager$Type): void
public "getErrorManager"(): $ErrorManager
public "setLevel"(arg0: $Level$Type): void
get "encoding"(): string
get "filter"(): $Filter
get "level"(): $Level
set "formatter"(value: $Formatter$Type)
get "formatter"(): $Formatter
set "encoding"(value: string)
set "filter"(value: $Filter$Type)
set "errorManager"(value: $ErrorManager$Type)
get "errorManager"(): $ErrorManager
set "level"(value: $Level$Type)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Handler$Type = ($Handler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Handler_ = $Handler$Type;
}}
declare module "packages/java/util/regex/$Pattern" {
import {$Matcher, $Matcher$Type} from "packages/java/util/regex/$Matcher"
import {$Serializable, $Serializable$Type} from "packages/java/io/$Serializable"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"

export class $Pattern implements $Serializable {
static readonly "UNIX_LINES": integer
static readonly "CASE_INSENSITIVE": integer
static readonly "COMMENTS": integer
static readonly "MULTILINE": integer
static readonly "LITERAL": integer
static readonly "DOTALL": integer
static readonly "UNICODE_CASE": integer
static readonly "CANON_EQ": integer
static readonly "UNICODE_CHARACTER_CLASS": integer


public "toString"(): string
public "flags"(): integer
public static "matches"(arg0: string, arg1: charseq): boolean
public static "compile"(arg0: string): $Pattern
public static "compile"(arg0: string, arg1: integer): $Pattern
public "matcher"(arg0: charseq): $Matcher
public "split"(arg0: charseq, arg1: integer): (string)[]
public "split"(arg0: charseq): (string)[]
public static "quote"(arg0: string): string
public "pattern"(): string
public "asPredicate"(): $Predicate<(string)>
public "asMatchPredicate"(): $Predicate<(string)>
public "splitAsStream"(arg0: charseq): $Stream<(string)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Pattern$Type = ($Pattern);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Pattern_ = $Pattern$Type;
}}
declare module "packages/java/util/concurrent/$ConcurrentLinkedQueue" {
import {$Serializable, $Serializable$Type} from "packages/java/io/$Serializable"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$AbstractQueue, $AbstractQueue$Type} from "packages/java/util/$AbstractQueue"
import {$Iterator, $Iterator$Type} from "packages/java/util/$Iterator"
import {$Spliterator, $Spliterator$Type} from "packages/java/util/$Spliterator"
import {$Queue, $Queue$Type} from "packages/java/util/$Queue"

export class $ConcurrentLinkedQueue<E> extends $AbstractQueue<(E)> implements $Queue<(E)>, $Serializable {

constructor()
constructor(arg0: $Collection$Type<(any)>)

public "add"(arg0: E): boolean
public "remove"(arg0: any): boolean
public "toString"(): string
public "clear"(): void
public "isEmpty"(): boolean
public "size"(): integer
public "toArray"(): (any)[]
public "toArray"<T>(arg0: (T)[]): (T)[]
public "iterator"(): $Iterator<(E)>
public "contains"(arg0: any): boolean
public "spliterator"(): $Spliterator<(E)>
public "addAll"(arg0: $Collection$Type<(any)>): boolean
public "forEach"(arg0: $Consumer$Type<(any)>): void
public "poll"(): E
public "peek"(): E
public "removeIf"(arg0: $Predicate$Type<(any)>): boolean
public "removeAll"(arg0: $Collection$Type<(any)>): boolean
public "retainAll"(arg0: $Collection$Type<(any)>): boolean
public "offer"(arg0: E): boolean
public "remove"(): E
public "element"(): E
public "equals"(arg0: any): boolean
public "hashCode"(): integer
public "containsAll"(arg0: $Collection$Type<(any)>): boolean
get "empty"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ConcurrentLinkedQueue$Type<E> = ($ConcurrentLinkedQueue<(E)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ConcurrentLinkedQueue_<E> = $ConcurrentLinkedQueue$Type<(E)>;
}}
declare module "packages/java/util/stream/$BaseStream" {
import {$AutoCloseable, $AutoCloseable$Type} from "packages/java/lang/$AutoCloseable"
import {$Iterator, $Iterator$Type} from "packages/java/util/$Iterator"
import {$Spliterator, $Spliterator$Type} from "packages/java/util/$Spliterator"
import {$Runnable, $Runnable$Type} from "packages/java/lang/$Runnable"

export interface $BaseStream<T, S extends $BaseStream<(T), (S)>> extends $AutoCloseable {

 "iterator"(): $Iterator<(T)>
 "spliterator"(): $Spliterator<(T)>
 "close"(): void
 "parallel"(): S
 "onClose"(arg0: $Runnable$Type): S
 "sequential"(): S
 "isParallel"(): boolean
 "unordered"(): S
}

export namespace $BaseStream {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BaseStream$Type<T, S> = ($BaseStream<(T), (S)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BaseStream_<T, S> = $BaseStream$Type<(T), (S)>;
}}
declare module "packages/java/util/concurrent/$ForkJoinPool$ManagedBlocker" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $ForkJoinPool$ManagedBlocker {

 "block"(): boolean
 "isReleasable"(): boolean
}

export namespace $ForkJoinPool$ManagedBlocker {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForkJoinPool$ManagedBlocker$Type = ($ForkJoinPool$ManagedBlocker);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForkJoinPool$ManagedBlocker_ = $ForkJoinPool$ManagedBlocker$Type;
}}
declare module "packages/java/util/$DoubleSummaryStatistics" {
import {$DoubleConsumer, $DoubleConsumer$Type} from "packages/java/util/function/$DoubleConsumer"

export class $DoubleSummaryStatistics implements $DoubleConsumer {

constructor()
constructor(arg0: long, arg1: double, arg2: double, arg3: double)

public "toString"(): string
public "accept"(arg0: double): void
public "combine"(arg0: $DoubleSummaryStatistics$Type): void
public "getCount"(): long
public "getSum"(): double
public "getMin"(): double
public "getAverage"(): double
public "getMax"(): double
public "andThen"(arg0: $DoubleConsumer$Type): $DoubleConsumer
get "count"(): long
get "sum"(): double
get "min"(): double
get "average"(): double
get "max"(): double
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DoubleSummaryStatistics$Type = ($DoubleSummaryStatistics);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DoubleSummaryStatistics_ = $DoubleSummaryStatistics$Type;
}}
declare module "packages/java/util/$SortedSet" {
import {$Comparator, $Comparator$Type} from "packages/java/util/$Comparator"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$IntFunction, $IntFunction$Type} from "packages/java/util/function/$IntFunction"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$Spliterator, $Spliterator$Type} from "packages/java/util/$Spliterator"
import {$Iterator, $Iterator$Type} from "packages/java/util/$Iterator"

export interface $SortedSet<E> extends $Set<(E)> {

 "last"(): E
 "spliterator"(): $Spliterator<(E)>
 "first"(): E
 "comparator"(): $Comparator<(any)>
 "subSet"(arg0: E, arg1: E): $SortedSet<(E)>
 "headSet"(arg0: E): $SortedSet<(E)>
 "tailSet"(arg0: E): $SortedSet<(E)>
 "add"(arg0: E): boolean
 "remove"(arg0: any): boolean
 "equals"(arg0: any): boolean
 "hashCode"(): integer
 "clear"(): void
 "isEmpty"(): boolean
 "size"(): integer
 "toArray"<T>(arg0: (T)[]): (T)[]
 "toArray"(): (any)[]
 "iterator"(): $Iterator<(E)>
 "contains"(arg0: any): boolean
 "addAll"(arg0: $Collection$Type<(any)>): boolean
 "removeAll"(arg0: $Collection$Type<(any)>): boolean
 "retainAll"(arg0: $Collection$Type<(any)>): boolean
 "containsAll"(arg0: $Collection$Type<(any)>): boolean
 "toArray"<T>(arg0: $IntFunction$Type<((T)[])>): (T)[]
 "stream"(): $Stream<(E)>
 "removeIf"(arg0: $Predicate$Type<(any)>): boolean
 "parallelStream"(): $Stream<(E)>
 "forEach"(arg0: $Consumer$Type<(any)>): void
}

export namespace $SortedSet {
function copyOf<E>(arg0: $Collection$Type<(any)>): $Set<(E)>
function of<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E, arg5: E): $Set<(E)>
function of<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E): $Set<(E)>
function of<E>(arg0: E): $Set<(E)>
function of<E>(arg0: E, arg1: E, arg2: E, arg3: E): $Set<(E)>
function of<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E, arg5: E, arg6: E, arg7: E, arg8: E, arg9: E): $Set<(E)>
function of<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E, arg5: E, arg6: E, arg7: E, arg8: E): $Set<(E)>
function of<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E, arg5: E, arg6: E, arg7: E): $Set<(E)>
function of<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E, arg5: E, arg6: E): $Set<(E)>
function of<E>(...arg0: (E)[]): $Set<(E)>
function of<E>(arg0: E, arg1: E, arg2: E): $Set<(E)>
function of<E>(): $Set<(E)>
function of<E>(arg0: E, arg1: E): $Set<(E)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SortedSet$Type<E> = ($SortedSet<(E)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SortedSet_<E> = $SortedSet$Type<(E)>;
}}
declare module "packages/java/util/$Deque" {
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$IntFunction, $IntFunction$Type} from "packages/java/util/function/$IntFunction"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$Iterator, $Iterator$Type} from "packages/java/util/$Iterator"
import {$Spliterator, $Spliterator$Type} from "packages/java/util/$Spliterator"
import {$Queue, $Queue$Type} from "packages/java/util/$Queue"

export interface $Deque<E> extends $Queue<(E)> {

 "add"(arg0: E): boolean
 "remove"(): E
 "remove"(arg0: any): boolean
 "size"(): integer
 "iterator"(): $Iterator<(E)>
 "contains"(arg0: any): boolean
 "addAll"(arg0: $Collection$Type<(any)>): boolean
 "poll"(): E
 "peek"(): E
 "element"(): E
 "push"(arg0: E): void
 "pop"(): E
 "addFirst"(arg0: E): void
 "addLast"(arg0: E): void
 "pollFirst"(): E
 "pollLast"(): E
 "offerLast"(arg0: E): boolean
 "removeFirst"(): E
 "getFirst"(): E
 "peekFirst"(): E
 "removeFirstOccurrence"(arg0: any): boolean
 "offerFirst"(arg0: E): boolean
 "removeLast"(): E
 "getLast"(): E
 "peekLast"(): E
 "removeLastOccurrence"(arg0: any): boolean
 "offer"(arg0: E): boolean
 "descendingIterator"(): $Iterator<(E)>
 "equals"(arg0: any): boolean
 "hashCode"(): integer
 "clear"(): void
 "isEmpty"(): boolean
 "toArray"<T>(arg0: (T)[]): (T)[]
 "toArray"<T>(arg0: $IntFunction$Type<((T)[])>): (T)[]
 "toArray"(): (any)[]
 "stream"(): $Stream<(E)>
 "spliterator"(): $Spliterator<(E)>
 "removeIf"(arg0: $Predicate$Type<(any)>): boolean
 "removeAll"(arg0: $Collection$Type<(any)>): boolean
 "retainAll"(arg0: $Collection$Type<(any)>): boolean
 "containsAll"(arg0: $Collection$Type<(any)>): boolean
 "parallelStream"(): $Stream<(E)>
 "forEach"(arg0: $Consumer$Type<(any)>): void
}

export namespace $Deque {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Deque$Type<E> = ($Deque<(E)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Deque_<E> = $Deque$Type<(E)>;
}}
declare module "packages/java/util/function/$LongToDoubleFunction" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $LongToDoubleFunction {

 "applyAsDouble"(arg0: long): double

(arg0: long): double
}

export namespace $LongToDoubleFunction {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LongToDoubleFunction$Type = ($LongToDoubleFunction);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LongToDoubleFunction_ = $LongToDoubleFunction$Type;
}}
declare module "packages/java/util/$IdentityHashMap" {
import {$BiConsumer, $BiConsumer$Type} from "packages/java/util/function/$BiConsumer"
import {$Serializable, $Serializable$Type} from "packages/java/io/$Serializable"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Cloneable, $Cloneable$Type} from "packages/java/lang/$Cloneable"
import {$AbstractMap, $AbstractMap$Type} from "packages/java/util/$AbstractMap"
import {$BiFunction, $BiFunction$Type} from "packages/java/util/function/$BiFunction"
import {$Map, $Map$Type} from "packages/java/util/$Map"
import {$Map$Entry, $Map$Entry$Type} from "packages/java/util/$Map$Entry"

export class $IdentityHashMap<K, V> extends $AbstractMap<(K), (V)> implements $Map<(K), (V)>, $Serializable, $Cloneable {

constructor(arg0: integer)
constructor()
constructor(arg0: $Map$Type<(any), (any)>)

public "remove"(arg0: any): V
public "get"(arg0: any): V
public "put"(arg0: K, arg1: V): V
public "equals"(arg0: any): boolean
public "values"(): $Collection<(V)>
public "hashCode"(): integer
public "clone"(): any
public "clear"(): void
public "isEmpty"(): boolean
public "replaceAll"(arg0: $BiFunction$Type<(any), (any), (any)>): void
public "size"(): integer
public "entrySet"(): $Set<($Map$Entry<(K), (V)>)>
public "putAll"(arg0: $Map$Type<(any), (any)>): void
public "forEach"(arg0: $BiConsumer$Type<(any), (any)>): void
public "containsKey"(arg0: any): boolean
public "keySet"(): $Set<(K)>
public "containsValue"(arg0: any): boolean
public static "copyOf"<K, V>(arg0: $Map$Type<(any), (any)>): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V, arg8: K, arg9: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V): $Map<(K), (V)>
public static "of"<K, V>(): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V, arg8: K, arg9: V, arg10: K, arg11: V, arg12: K, arg13: V, arg14: K, arg15: V, arg16: K, arg17: V, arg18: K, arg19: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V, arg8: K, arg9: V, arg10: K, arg11: V, arg12: K, arg13: V, arg14: K, arg15: V, arg16: K, arg17: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V, arg8: K, arg9: V, arg10: K, arg11: V, arg12: K, arg13: V, arg14: K, arg15: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V, arg8: K, arg9: V, arg10: K, arg11: V, arg12: K, arg13: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V, arg8: K, arg9: V, arg10: K, arg11: V): $Map<(K), (V)>
public static "entry"<K, V>(arg0: K, arg1: V): $Map$Entry<(K), (V)>
public static "ofEntries"<K, V>(...arg0: ($Map$Entry$Type<(any), (any)>)[]): $Map<(K), (V)>
get "empty"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IdentityHashMap$Type<K, V> = ($IdentityHashMap<(K), (V)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IdentityHashMap_<K, V> = $IdentityHashMap$Type<(K), (V)>;
}}
declare module "packages/java/util/$OptionalLong" {
import {$LongStream, $LongStream$Type} from "packages/java/util/stream/$LongStream"
import {$Throwable, $Throwable$Type} from "packages/java/lang/$Throwable"
import {$LongConsumer, $LongConsumer$Type} from "packages/java/util/function/$LongConsumer"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$LongSupplier, $LongSupplier$Type} from "packages/java/util/function/$LongSupplier"
import {$Runnable, $Runnable$Type} from "packages/java/lang/$Runnable"

export class $OptionalLong {


public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "isEmpty"(): boolean
public "stream"(): $LongStream
public static "of"(arg0: long): $OptionalLong
public static "empty"(): $OptionalLong
public "isPresent"(): boolean
public "orElse"(arg0: long): long
public "orElseThrow"<X extends $Throwable>(arg0: $Supplier$Type<(any)>): long
public "orElseThrow"(): long
public "ifPresent"(arg0: $LongConsumer$Type): void
public "ifPresentOrElse"(arg0: $LongConsumer$Type, arg1: $Runnable$Type): void
public "orElseGet"(arg0: $LongSupplier$Type): long
public "getAsLong"(): long
get "present"(): boolean
get "asLong"(): long
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $OptionalLong$Type = ($OptionalLong);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $OptionalLong_ = $OptionalLong$Type;
}}
declare module "packages/java/util/$Map$Entry" {
import {$Comparator, $Comparator$Type} from "packages/java/util/$Comparator"
import {$Comparable, $Comparable$Type} from "packages/java/lang/$Comparable"

export interface $Map$Entry<K, V> {

 "equals"(arg0: any): boolean
 "hashCode"(): integer
 "getValue"(): V
 "getKey"(): K
 "setValue"(arg0: V): V
}

export namespace $Map$Entry {
function copyOf<K, V>(arg0: $Map$Entry$Type<(any), (any)>): $Map$Entry<(K), (V)>
function comparingByKey<K, V>(arg0: $Comparator$Type<(any)>): $Comparator<($Map$Entry<(K), (V)>)>
function comparingByKey<K, V>(): $Comparator<($Map$Entry<(K), (V)>)>
function comparingByValue<K, V>(): $Comparator<($Map$Entry<(K), (V)>)>
function comparingByValue<K, V>(arg0: $Comparator$Type<(any)>): $Comparator<($Map$Entry<(K), (V)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Map$Entry$Type<K, V> = ($Map$Entry<(K), (V)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Map$Entry_<K, V> = $Map$Entry$Type<(K), (V)>;
}}
declare module "packages/java/util/$AbstractList" {
import {$Comparator, $Comparator$Type} from "packages/java/util/$Comparator"
import {$UnaryOperator, $UnaryOperator$Type} from "packages/java/util/function/$UnaryOperator"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$AbstractCollection, $AbstractCollection$Type} from "packages/java/util/$AbstractCollection"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ListIterator, $ListIterator$Type} from "packages/java/util/$ListIterator"
import {$Iterator, $Iterator$Type} from "packages/java/util/$Iterator"
import {$Spliterator, $Spliterator$Type} from "packages/java/util/$Spliterator"

export class $AbstractList<E> extends $AbstractCollection<(E)> implements $List<(E)> {


public "add"(arg0: integer, arg1: E): void
public "add"(arg0: E): boolean
public "remove"(arg0: integer): E
public "get"(arg0: integer): E
public "equals"(arg0: any): boolean
public "hashCode"(): integer
public "indexOf"(arg0: any): integer
public "clear"(): void
public "lastIndexOf"(arg0: any): integer
public "subList"(arg0: integer, arg1: integer): $List<(E)>
public "iterator"(): $Iterator<(E)>
public "addAll"(arg0: integer, arg1: $Collection$Type<(any)>): boolean
public "set"(arg0: integer, arg1: E): E
public "listIterator"(): $ListIterator<(E)>
public "listIterator"(arg0: integer): $ListIterator<(E)>
public "remove"(arg0: any): boolean
public static "copyOf"<E>(arg0: $Collection$Type<(any)>): $List<(E)>
public "isEmpty"(): boolean
public "replaceAll"(arg0: $UnaryOperator$Type<(E)>): void
public "size"(): integer
public "toArray"(): (any)[]
public "toArray"<T>(arg0: (T)[]): (T)[]
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E): $List<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E): $List<(E)>
public static "of"<E>(arg0: E, arg1: E): $List<(E)>
public static "of"<E>(arg0: E): $List<(E)>
public static "of"<E>(): $List<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E, arg5: E, arg6: E, arg7: E): $List<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E, arg5: E, arg6: E): $List<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E, arg5: E): $List<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E): $List<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E, arg5: E, arg6: E, arg7: E, arg8: E, arg9: E): $List<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E, arg5: E, arg6: E, arg7: E, arg8: E): $List<(E)>
public static "of"<E>(...arg0: (E)[]): $List<(E)>
public "contains"(arg0: any): boolean
public "spliterator"(): $Spliterator<(E)>
public "addAll"(arg0: $Collection$Type<(any)>): boolean
public "sort"(arg0: $Comparator$Type<(any)>): void
public "removeAll"(arg0: $Collection$Type<(any)>): boolean
public "retainAll"(arg0: $Collection$Type<(any)>): boolean
public "containsAll"(arg0: $Collection$Type<(any)>): boolean
[index: number]: E
get "empty"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AbstractList$Type<E> = ($AbstractList<(E)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AbstractList_<E> = $AbstractList$Type<(E)>;
}}
declare module "packages/java/util/function/$Predicate" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $Predicate<T> {

 "test"(arg0: T): boolean
 "or"(arg0: $Predicate$Type<(any)>): $Predicate<(T)>
 "negate"(): $Predicate<(T)>
 "and"(arg0: $Predicate$Type<(any)>): $Predicate<(T)>

(arg0: T): boolean
}

export namespace $Predicate {
function not<T>(arg0: $Predicate$Type<(any)>): $Predicate<(T)>
function isEqual<T>(arg0: any): $Predicate<(T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Predicate$Type<T> = ($Predicate<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Predicate_<T> = $Predicate$Type<(T)>;
}}
declare module "packages/java/util/function/$IntSupplier" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $IntSupplier {

 "getAsInt"(): integer

(): integer
}

export namespace $IntSupplier {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IntSupplier$Type = ($IntSupplier);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IntSupplier_ = $IntSupplier$Type;
}}
declare module "packages/java/util/$AbstractQueue" {
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$AbstractCollection, $AbstractCollection$Type} from "packages/java/util/$AbstractCollection"
import {$Queue, $Queue$Type} from "packages/java/util/$Queue"

export class $AbstractQueue<E> extends $AbstractCollection<(E)> implements $Queue<(E)> {


public "add"(arg0: E): boolean
public "remove"(): E
public "clear"(): void
public "addAll"(arg0: $Collection$Type<(any)>): boolean
public "element"(): E
public "poll"(): E
public "peek"(): E
public "offer"(arg0: E): boolean
public "equals"(arg0: any): boolean
public "hashCode"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AbstractQueue$Type<E> = ($AbstractQueue<(E)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AbstractQueue_<E> = $AbstractQueue$Type<(E)>;
}}
declare module "packages/java/util/stream/$DoubleStream" {
import {$ObjDoubleConsumer, $ObjDoubleConsumer$Type} from "packages/java/util/function/$ObjDoubleConsumer"
import {$DoubleStream$Builder, $DoubleStream$Builder$Type} from "packages/java/util/stream/$DoubleStream$Builder"
import {$LongStream, $LongStream$Type} from "packages/java/util/stream/$LongStream"
import {$OptionalDouble, $OptionalDouble$Type} from "packages/java/util/$OptionalDouble"
import {$IntStream, $IntStream$Type} from "packages/java/util/stream/$IntStream"
import {$DoubleConsumer, $DoubleConsumer$Type} from "packages/java/util/function/$DoubleConsumer"
import {$DoubleUnaryOperator, $DoubleUnaryOperator$Type} from "packages/java/util/function/$DoubleUnaryOperator"
import {$Runnable, $Runnable$Type} from "packages/java/lang/$Runnable"
import {$BiConsumer, $BiConsumer$Type} from "packages/java/util/function/$BiConsumer"
import {$DoubleFunction, $DoubleFunction$Type} from "packages/java/util/function/$DoubleFunction"
import {$DoubleToIntFunction, $DoubleToIntFunction$Type} from "packages/java/util/function/$DoubleToIntFunction"
import {$DoubleToLongFunction, $DoubleToLongFunction$Type} from "packages/java/util/function/$DoubleToLongFunction"
import {$DoubleStream$DoubleMapMultiConsumer, $DoubleStream$DoubleMapMultiConsumer$Type} from "packages/java/util/stream/$DoubleStream$DoubleMapMultiConsumer"
import {$DoubleBinaryOperator, $DoubleBinaryOperator$Type} from "packages/java/util/function/$DoubleBinaryOperator"
import {$BaseStream, $BaseStream$Type} from "packages/java/util/stream/$BaseStream"
import {$DoubleSummaryStatistics, $DoubleSummaryStatistics$Type} from "packages/java/util/$DoubleSummaryStatistics"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$PrimitiveIterator$OfDouble, $PrimitiveIterator$OfDouble$Type} from "packages/java/util/$PrimitiveIterator$OfDouble"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$DoubleSupplier, $DoubleSupplier$Type} from "packages/java/util/function/$DoubleSupplier"
import {$DoublePredicate, $DoublePredicate$Type} from "packages/java/util/function/$DoublePredicate"

export interface $DoubleStream extends $BaseStream<(double), ($DoubleStream)> {

 "min"(): $OptionalDouble
 "max"(): $OptionalDouble
 "toArray"(): (double)[]
 "iterator"(): $PrimitiveIterator$OfDouble
 "map"(arg0: $DoubleUnaryOperator$Type): $DoubleStream
 "collect"<R>(arg0: $Supplier$Type<(R)>, arg1: $ObjDoubleConsumer$Type<(R)>, arg2: $BiConsumer$Type<(R), (R)>): R
 "count"(): long
 "limit"(arg0: long): $DoubleStream
 "filter"(arg0: $DoublePredicate$Type): $DoubleStream
 "anyMatch"(arg0: $DoublePredicate$Type): boolean
 "flatMap"(arg0: $DoubleFunction$Type<(any)>): $DoubleStream
 "forEach"(arg0: $DoubleConsumer$Type): void
 "findAny"(): $OptionalDouble
 "skip"(arg0: long): $DoubleStream
 "peek"(arg0: $DoubleConsumer$Type): $DoubleStream
 "sum"(): double
 "reduce"(arg0: double, arg1: $DoubleBinaryOperator$Type): double
 "reduce"(arg0: $DoubleBinaryOperator$Type): $OptionalDouble
 "distinct"(): $DoubleStream
 "findFirst"(): $OptionalDouble
 "allMatch"(arg0: $DoublePredicate$Type): boolean
 "mapToObj"<U>(arg0: $DoubleFunction$Type<(any)>): $Stream<(U)>
 "sorted"(): $DoubleStream
 "forEachOrdered"(arg0: $DoubleConsumer$Type): void
 "mapToInt"(arg0: $DoubleToIntFunction$Type): $IntStream
 "mapToLong"(arg0: $DoubleToLongFunction$Type): $LongStream
 "mapMulti"(arg0: $DoubleStream$DoubleMapMultiConsumer$Type): $DoubleStream
 "takeWhile"(arg0: $DoublePredicate$Type): $DoubleStream
 "dropWhile"(arg0: $DoublePredicate$Type): $DoubleStream
 "noneMatch"(arg0: $DoublePredicate$Type): boolean
 "boxed"(): $Stream<(double)>
 "average"(): $OptionalDouble
 "summaryStatistics"(): $DoubleSummaryStatistics
 "close"(): void
 "onClose"(arg0: $Runnable$Type): $DoubleStream
 "isParallel"(): boolean
 "unordered"(): $DoubleStream
}

export namespace $DoubleStream {
function of(...arg0: (double)[]): $DoubleStream
function of(arg0: double): $DoubleStream
function builder(): $DoubleStream$Builder
function concat(arg0: $DoubleStream$Type, arg1: $DoubleStream$Type): $DoubleStream
function empty(): $DoubleStream
function iterate(arg0: double, arg1: $DoublePredicate$Type, arg2: $DoubleUnaryOperator$Type): $DoubleStream
function iterate(arg0: double, arg1: $DoubleUnaryOperator$Type): $DoubleStream
function generate(arg0: $DoubleSupplier$Type): $DoubleStream
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DoubleStream$Type = ($DoubleStream);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DoubleStream_ = $DoubleStream$Type;
}}
declare module "packages/java/util/$EventListener" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $EventListener {

}

export namespace $EventListener {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EventListener$Type = ($EventListener);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EventListener_ = $EventListener$Type;
}}
declare module "packages/java/util/$OptionalInt" {
import {$Throwable, $Throwable$Type} from "packages/java/lang/$Throwable"
import {$IntStream, $IntStream$Type} from "packages/java/util/stream/$IntStream"
import {$IntSupplier, $IntSupplier$Type} from "packages/java/util/function/$IntSupplier"
import {$IntConsumer, $IntConsumer$Type} from "packages/java/util/function/$IntConsumer"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$Runnable, $Runnable$Type} from "packages/java/lang/$Runnable"

export class $OptionalInt {


public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "isEmpty"(): boolean
public "stream"(): $IntStream
public static "of"(arg0: integer): $OptionalInt
public static "empty"(): $OptionalInt
public "isPresent"(): boolean
public "orElse"(arg0: integer): integer
public "orElseThrow"<X extends $Throwable>(arg0: $Supplier$Type<(any)>): integer
public "orElseThrow"(): integer
public "ifPresent"(arg0: $IntConsumer$Type): void
public "ifPresentOrElse"(arg0: $IntConsumer$Type, arg1: $Runnable$Type): void
public "orElseGet"(arg0: $IntSupplier$Type): integer
public "getAsInt"(): integer
get "present"(): boolean
get "asInt"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $OptionalInt$Type = ($OptionalInt);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $OptionalInt_ = $OptionalInt$Type;
}}
declare module "packages/java/util/function/$DoubleToIntFunction" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $DoubleToIntFunction {

 "applyAsInt"(arg0: double): integer

(arg0: double): integer
}

export namespace $DoubleToIntFunction {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DoubleToIntFunction$Type = ($DoubleToIntFunction);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DoubleToIntFunction_ = $DoubleToIntFunction$Type;
}}
declare module "packages/java/util/logging/$Filter" {
import {$LogRecord, $LogRecord$Type} from "packages/java/util/logging/$LogRecord"

export interface $Filter {

 "isLoggable"(arg0: $LogRecord$Type): boolean

(arg0: $LogRecord$Type): boolean
}

export namespace $Filter {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Filter$Type = ($Filter);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Filter_ = $Filter$Type;
}}
declare module "packages/java/util/$Hashtable" {
import {$BiConsumer, $BiConsumer$Type} from "packages/java/util/function/$BiConsumer"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Serializable, $Serializable$Type} from "packages/java/io/$Serializable"
import {$Enumeration, $Enumeration$Type} from "packages/java/util/$Enumeration"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Cloneable, $Cloneable$Type} from "packages/java/lang/$Cloneable"
import {$BiFunction, $BiFunction$Type} from "packages/java/util/function/$BiFunction"
import {$Dictionary, $Dictionary$Type} from "packages/java/util/$Dictionary"
import {$Map, $Map$Type} from "packages/java/util/$Map"
import {$Map$Entry, $Map$Entry$Type} from "packages/java/util/$Map$Entry"

export class $Hashtable<K, V> extends $Dictionary<(K), (V)> implements $Map<(K), (V)>, $Cloneable, $Serializable {

constructor(arg0: $Map$Type<(any), (any)>)
constructor()
constructor(arg0: integer)
constructor(arg0: integer, arg1: float)

public "remove"(arg0: any): V
public "remove"(arg0: any, arg1: any): boolean
public "get"(arg0: any): V
public "put"(arg0: K, arg1: V): V
public "equals"(arg0: any): boolean
public "toString"(): string
public "values"(): $Collection<(V)>
public "hashCode"(): integer
public "clone"(): any
public "clear"(): void
public "isEmpty"(): boolean
public "replace"(arg0: K, arg1: V): V
public "replace"(arg0: K, arg1: V, arg2: V): boolean
public "replaceAll"(arg0: $BiFunction$Type<(any), (any), (any)>): void
public "size"(): integer
public "contains"(arg0: any): boolean
public "elements"(): $Enumeration<(V)>
public "merge"(arg0: K, arg1: V, arg2: $BiFunction$Type<(any), (any), (any)>): V
public "entrySet"(): $Set<($Map$Entry<(K), (V)>)>
public "putAll"(arg0: $Map$Type<(any), (any)>): void
public "putIfAbsent"(arg0: K, arg1: V): V
public "compute"(arg0: K, arg1: $BiFunction$Type<(any), (any), (any)>): V
public "forEach"(arg0: $BiConsumer$Type<(any), (any)>): void
public "containsKey"(arg0: any): boolean
public "computeIfAbsent"(arg0: K, arg1: $Function$Type<(any), (any)>): V
public "keys"(): $Enumeration<(K)>
public "keySet"(): $Set<(K)>
public "containsValue"(arg0: any): boolean
public "getOrDefault"(arg0: any, arg1: V): V
public "computeIfPresent"(arg0: K, arg1: $BiFunction$Type<(any), (any), (any)>): V
public static "copyOf"<K, V>(arg0: $Map$Type<(any), (any)>): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V, arg8: K, arg9: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V): $Map<(K), (V)>
public static "of"<K, V>(): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V, arg8: K, arg9: V, arg10: K, arg11: V, arg12: K, arg13: V, arg14: K, arg15: V, arg16: K, arg17: V, arg18: K, arg19: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V, arg8: K, arg9: V, arg10: K, arg11: V, arg12: K, arg13: V, arg14: K, arg15: V, arg16: K, arg17: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V, arg8: K, arg9: V, arg10: K, arg11: V, arg12: K, arg13: V, arg14: K, arg15: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V, arg8: K, arg9: V, arg10: K, arg11: V, arg12: K, arg13: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V, arg8: K, arg9: V, arg10: K, arg11: V): $Map<(K), (V)>
public static "entry"<K, V>(arg0: K, arg1: V): $Map$Entry<(K), (V)>
public static "ofEntries"<K, V>(...arg0: ($Map$Entry$Type<(any), (any)>)[]): $Map<(K), (V)>
[index: string | number]: V
get "empty"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Hashtable$Type<K, V> = ($Hashtable<(K), (V)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Hashtable_<K, V> = $Hashtable$Type<(K), (V)>;
}}
declare module "packages/java/util/$Comparator" {
import {$Comparable, $Comparable$Type} from "packages/java/lang/$Comparable"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$ToIntFunction, $ToIntFunction$Type} from "packages/java/util/function/$ToIntFunction"
import {$ToLongFunction, $ToLongFunction$Type} from "packages/java/util/function/$ToLongFunction"
import {$ToDoubleFunction, $ToDoubleFunction$Type} from "packages/java/util/function/$ToDoubleFunction"

export interface $Comparator<T> {

 "equals"(arg0: any): boolean
 "compare"(arg0: T, arg1: T): integer
 "thenComparing"(arg0: $Comparator$Type<(any)>): $Comparator<(T)>
 "thenComparing"<U>(arg0: $Function$Type<(any), (any)>, arg1: $Comparator$Type<(any)>): $Comparator<(T)>
 "thenComparing"<U extends $Comparable<(any)>>(arg0: $Function$Type<(any), (any)>): $Comparator<(T)>
 "reversed"(): $Comparator<(T)>
 "thenComparingInt"(arg0: $ToIntFunction$Type<(any)>): $Comparator<(T)>
 "thenComparingLong"(arg0: $ToLongFunction$Type<(any)>): $Comparator<(T)>
 "thenComparingDouble"(arg0: $ToDoubleFunction$Type<(any)>): $Comparator<(T)>
}

export namespace $Comparator {
function reverseOrder<T>(): $Comparator<(T)>
function comparing<T, U>(arg0: $Function$Type<(any), (any)>): $Comparator<(T)>
function comparing<T, U>(arg0: $Function$Type<(any), (any)>, arg1: $Comparator$Type<(any)>): $Comparator<(T)>
function comparingInt<T>(arg0: $ToIntFunction$Type<(any)>): $Comparator<(T)>
function comparingLong<T>(arg0: $ToLongFunction$Type<(any)>): $Comparator<(T)>
function comparingDouble<T>(arg0: $ToDoubleFunction$Type<(any)>): $Comparator<(T)>
function naturalOrder<T>(): $Comparator<(T)>
function nullsFirst<T>(arg0: $Comparator$Type<(any)>): $Comparator<(T)>
function nullsLast<T>(arg0: $Comparator$Type<(any)>): $Comparator<(T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Comparator$Type<T> = ($Comparator<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Comparator_<T> = $Comparator$Type<(T)>;
}}
declare module "packages/java/util/concurrent/$Future" {
import {$TimeUnit, $TimeUnit$Type} from "packages/java/util/concurrent/$TimeUnit"

export interface $Future<V> {

 "get"(arg0: long, arg1: $TimeUnit$Type): V
 "get"(): V
 "isDone"(): boolean
 "cancel"(arg0: boolean): boolean
 "isCancelled"(): boolean
}

export namespace $Future {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Future$Type<V> = ($Future<(V)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Future_<V> = $Future$Type<(V)>;
}}
declare module "packages/java/util/function/$ToLongBiFunction" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $ToLongBiFunction<T, U> {

 "applyAsLong"(arg0: T, arg1: U): long

(arg0: T, arg1: U): long
}

export namespace $ToLongBiFunction {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ToLongBiFunction$Type<T, U> = ($ToLongBiFunction<(T), (U)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ToLongBiFunction_<T, U> = $ToLongBiFunction$Type<(T), (U)>;
}}
declare module "packages/java/util/concurrent/atomic/$LongAdder" {
import {$Serializable, $Serializable$Type} from "packages/java/io/$Serializable"
import {$Striped64, $Striped64$Type} from "packages/java/util/concurrent/atomic/$Striped64"

export class $LongAdder extends $Striped64 implements $Serializable {

constructor()

public "add"(arg0: long): void
public "toString"(): string
public "intValue"(): integer
public "longValue"(): long
public "floatValue"(): float
public "doubleValue"(): double
public "increment"(): void
public "reset"(): void
public "sum"(): long
public "decrement"(): void
public "sumThenReset"(): long
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LongAdder$Type = ($LongAdder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LongAdder_ = $LongAdder$Type;
}}
declare module "packages/java/util/function/$LongBinaryOperator" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $LongBinaryOperator {

 "applyAsLong"(arg0: long, arg1: long): long

(arg0: long, arg1: long): long
}

export namespace $LongBinaryOperator {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LongBinaryOperator$Type = ($LongBinaryOperator);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LongBinaryOperator_ = $LongBinaryOperator$Type;
}}
declare module "packages/java/util/$WeakHashMap" {
import {$BiConsumer, $BiConsumer$Type} from "packages/java/util/function/$BiConsumer"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$AbstractMap, $AbstractMap$Type} from "packages/java/util/$AbstractMap"
import {$BiFunction, $BiFunction$Type} from "packages/java/util/function/$BiFunction"
import {$Map, $Map$Type} from "packages/java/util/$Map"
import {$Map$Entry, $Map$Entry$Type} from "packages/java/util/$Map$Entry"

export class $WeakHashMap<K, V> extends $AbstractMap<(K), (V)> implements $Map<(K), (V)> {

constructor(arg0: $Map$Type<(any), (any)>)
constructor(arg0: integer)
constructor(arg0: integer, arg1: float)
constructor()

public "remove"(arg0: any): V
public "get"(arg0: any): V
public "put"(arg0: K, arg1: V): V
public "values"(): $Collection<(V)>
public "clear"(): void
public "isEmpty"(): boolean
public "replaceAll"(arg0: $BiFunction$Type<(any), (any), (any)>): void
public "size"(): integer
public "entrySet"(): $Set<($Map$Entry<(K), (V)>)>
public "putAll"(arg0: $Map$Type<(any), (any)>): void
public "forEach"(arg0: $BiConsumer$Type<(any), (any)>): void
public "containsKey"(arg0: any): boolean
public "keySet"(): $Set<(K)>
public "containsValue"(arg0: any): boolean
public "equals"(arg0: any): boolean
public "hashCode"(): integer
public static "copyOf"<K, V>(arg0: $Map$Type<(any), (any)>): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V, arg8: K, arg9: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V): $Map<(K), (V)>
public static "of"<K, V>(): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V, arg8: K, arg9: V, arg10: K, arg11: V, arg12: K, arg13: V, arg14: K, arg15: V, arg16: K, arg17: V, arg18: K, arg19: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V, arg8: K, arg9: V, arg10: K, arg11: V, arg12: K, arg13: V, arg14: K, arg15: V, arg16: K, arg17: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V, arg8: K, arg9: V, arg10: K, arg11: V, arg12: K, arg13: V, arg14: K, arg15: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V, arg8: K, arg9: V, arg10: K, arg11: V, arg12: K, arg13: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V, arg8: K, arg9: V, arg10: K, arg11: V): $Map<(K), (V)>
public static "entry"<K, V>(arg0: K, arg1: V): $Map$Entry<(K), (V)>
public static "ofEntries"<K, V>(...arg0: ($Map$Entry$Type<(any), (any)>)[]): $Map<(K), (V)>
get "empty"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WeakHashMap$Type<K, V> = ($WeakHashMap<(K), (V)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WeakHashMap_<K, V> = $WeakHashMap$Type<(K), (V)>;
}}
declare module "packages/java/util/$NavigableSet" {
import {$Comparator, $Comparator$Type} from "packages/java/util/$Comparator"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$SortedSet, $SortedSet$Type} from "packages/java/util/$SortedSet"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$IntFunction, $IntFunction$Type} from "packages/java/util/function/$IntFunction"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$Iterator, $Iterator$Type} from "packages/java/util/$Iterator"
import {$Spliterator, $Spliterator$Type} from "packages/java/util/$Spliterator"

export interface $NavigableSet<E> extends $SortedSet<(E)> {

 "floor"(arg0: E): E
 "iterator"(): $Iterator<(E)>
 "lower"(arg0: E): E
 "pollFirst"(): E
 "pollLast"(): E
 "descendingIterator"(): $Iterator<(E)>
 "ceiling"(arg0: E): E
 "higher"(arg0: E): E
 "descendingSet"(): $NavigableSet<(E)>
 "subSet"(arg0: E, arg1: E): $SortedSet<(E)>
 "subSet"(arg0: E, arg1: boolean, arg2: E, arg3: boolean): $NavigableSet<(E)>
 "headSet"(arg0: E): $SortedSet<(E)>
 "headSet"(arg0: E, arg1: boolean): $NavigableSet<(E)>
 "tailSet"(arg0: E): $SortedSet<(E)>
 "tailSet"(arg0: E, arg1: boolean): $NavigableSet<(E)>
 "last"(): E
 "spliterator"(): $Spliterator<(E)>
 "first"(): E
 "comparator"(): $Comparator<(any)>
 "add"(arg0: E): boolean
 "remove"(arg0: any): boolean
 "equals"(arg0: any): boolean
 "hashCode"(): integer
 "clear"(): void
 "isEmpty"(): boolean
 "size"(): integer
 "toArray"<T>(arg0: (T)[]): (T)[]
 "toArray"(): (any)[]
 "contains"(arg0: any): boolean
 "addAll"(arg0: $Collection$Type<(any)>): boolean
 "removeAll"(arg0: $Collection$Type<(any)>): boolean
 "retainAll"(arg0: $Collection$Type<(any)>): boolean
 "containsAll"(arg0: $Collection$Type<(any)>): boolean
 "toArray"<T>(arg0: $IntFunction$Type<((T)[])>): (T)[]
 "stream"(): $Stream<(E)>
 "removeIf"(arg0: $Predicate$Type<(any)>): boolean
 "parallelStream"(): $Stream<(E)>
 "forEach"(arg0: $Consumer$Type<(any)>): void
}

export namespace $NavigableSet {
function copyOf<E>(arg0: $Collection$Type<(any)>): $Set<(E)>
function of<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E, arg5: E): $Set<(E)>
function of<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E): $Set<(E)>
function of<E>(arg0: E): $Set<(E)>
function of<E>(arg0: E, arg1: E, arg2: E, arg3: E): $Set<(E)>
function of<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E, arg5: E, arg6: E, arg7: E, arg8: E, arg9: E): $Set<(E)>
function of<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E, arg5: E, arg6: E, arg7: E, arg8: E): $Set<(E)>
function of<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E, arg5: E, arg6: E, arg7: E): $Set<(E)>
function of<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E, arg5: E, arg6: E): $Set<(E)>
function of<E>(...arg0: (E)[]): $Set<(E)>
function of<E>(arg0: E, arg1: E, arg2: E): $Set<(E)>
function of<E>(): $Set<(E)>
function of<E>(arg0: E, arg1: E): $Set<(E)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NavigableSet$Type<E> = ($NavigableSet<(E)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NavigableSet_<E> = $NavigableSet$Type<(E)>;
}}
declare module "packages/java/util/$Random" {
import {$LongStream, $LongStream$Type} from "packages/java/util/stream/$LongStream"
import {$Serializable, $Serializable$Type} from "packages/java/io/$Serializable"
import {$RandomGenerator, $RandomGenerator$Type} from "packages/java/util/random/$RandomGenerator"
import {$IntStream, $IntStream$Type} from "packages/java/util/stream/$IntStream"
import {$DoubleStream, $DoubleStream$Type} from "packages/java/util/stream/$DoubleStream"

export class $Random implements $RandomGenerator, $Serializable {

constructor()
constructor(arg0: long)

public "nextDouble"(): double
public "nextInt"(): integer
public "nextInt"(arg0: integer): integer
public "nextBytes"(arg0: (byte)[]): void
public "setSeed"(arg0: long): void
public "ints"(arg0: long): $IntStream
public "ints"(): $IntStream
public "ints"(arg0: long, arg1: integer, arg2: integer): $IntStream
public "ints"(arg0: integer, arg1: integer): $IntStream
public "longs"(arg0: long): $LongStream
public "longs"(arg0: long, arg1: long): $LongStream
public "longs"(arg0: long, arg1: long, arg2: long): $LongStream
public "longs"(): $LongStream
public "doubles"(arg0: long): $DoubleStream
public "doubles"(arg0: long, arg1: double, arg2: double): $DoubleStream
public "doubles"(arg0: double, arg1: double): $DoubleStream
public "doubles"(): $DoubleStream
public "nextLong"(): long
public "nextBoolean"(): boolean
public "nextFloat"(): float
public "nextGaussian"(): double
public static "getDefault"(): $RandomGenerator
public static "of"(arg0: string): $RandomGenerator
public "nextDouble"(arg0: double): double
public "nextDouble"(arg0: double, arg1: double): double
public "nextInt"(arg0: integer, arg1: integer): integer
public "nextLong"(arg0: long, arg1: long): long
public "nextLong"(arg0: long): long
public "nextFloat"(arg0: float): float
public "nextFloat"(arg0: float, arg1: float): float
public "nextGaussian"(arg0: double, arg1: double): double
public "isDeprecated"(): boolean
public "nextExponential"(): double
set "seed"(value: long)
get "default"(): $RandomGenerator
get "deprecated"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Random$Type = ($Random);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Random_ = $Random$Type;
}}
declare module "packages/java/util/concurrent/$Callable" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $Callable<V> {

 "call"(): V

(): V
}

export namespace $Callable {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Callable$Type<V> = ($Callable<(V)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Callable_<V> = $Callable$Type<(V)>;
}}
declare module "packages/java/util/stream/$DoubleStream$Builder" {
import {$DoubleConsumer, $DoubleConsumer$Type} from "packages/java/util/function/$DoubleConsumer"
import {$DoubleStream, $DoubleStream$Type} from "packages/java/util/stream/$DoubleStream"

export interface $DoubleStream$Builder extends $DoubleConsumer {

 "add"(arg0: double): $DoubleStream$Builder
 "accept"(arg0: double): void
 "build"(): $DoubleStream
 "andThen"(arg0: $DoubleConsumer$Type): $DoubleConsumer
}

export namespace $DoubleStream$Builder {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DoubleStream$Builder$Type = ($DoubleStream$Builder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DoubleStream$Builder_ = $DoubleStream$Builder$Type;
}}
declare module "packages/java/util/stream/$LongStream$Builder" {
import {$LongStream, $LongStream$Type} from "packages/java/util/stream/$LongStream"
import {$LongConsumer, $LongConsumer$Type} from "packages/java/util/function/$LongConsumer"

export interface $LongStream$Builder extends $LongConsumer {

 "add"(arg0: long): $LongStream$Builder
 "accept"(arg0: long): void
 "build"(): $LongStream
 "andThen"(arg0: $LongConsumer$Type): $LongConsumer
}

export namespace $LongStream$Builder {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LongStream$Builder$Type = ($LongStream$Builder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LongStream$Builder_ = $LongStream$Builder$Type;
}}
declare module "packages/java/util/$Vector" {
import {$Comparator, $Comparator$Type} from "packages/java/util/$Comparator"
import {$Serializable, $Serializable$Type} from "packages/java/io/$Serializable"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$AbstractList, $AbstractList$Type} from "packages/java/util/$AbstractList"
import {$ListIterator, $ListIterator$Type} from "packages/java/util/$ListIterator"
import {$Spliterator, $Spliterator$Type} from "packages/java/util/$Spliterator"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$UnaryOperator, $UnaryOperator$Type} from "packages/java/util/function/$UnaryOperator"
import {$Enumeration, $Enumeration$Type} from "packages/java/util/$Enumeration"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Cloneable, $Cloneable$Type} from "packages/java/lang/$Cloneable"
import {$RandomAccess, $RandomAccess$Type} from "packages/java/util/$RandomAccess"
import {$Iterator, $Iterator$Type} from "packages/java/util/$Iterator"

export class $Vector<E> extends $AbstractList<(E)> implements $List<(E)>, $RandomAccess, $Cloneable, $Serializable {

constructor(arg0: $Collection$Type<(any)>)
constructor()
constructor(arg0: integer)
constructor(arg0: integer, arg1: integer)

public "add"(arg0: E): boolean
public "add"(arg0: integer, arg1: E): void
public "remove"(arg0: integer): E
public "remove"(arg0: any): boolean
public "get"(arg0: integer): E
public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "clone"(): any
public "indexOf"(arg0: any): integer
public "indexOf"(arg0: any, arg1: integer): integer
public "clear"(): void
public "lastIndexOf"(arg0: any, arg1: integer): integer
public "lastIndexOf"(arg0: any): integer
public "isEmpty"(): boolean
public "replaceAll"(arg0: $UnaryOperator$Type<(E)>): void
public "size"(): integer
public "subList"(arg0: integer, arg1: integer): $List<(E)>
public "toArray"<T>(arg0: (T)[]): (T)[]
public "toArray"(): (any)[]
public "iterator"(): $Iterator<(E)>
public "contains"(arg0: any): boolean
public "elements"(): $Enumeration<(E)>
public "spliterator"(): $Spliterator<(E)>
public "addAll"(arg0: integer, arg1: $Collection$Type<(any)>): boolean
public "addAll"(arg0: $Collection$Type<(any)>): boolean
public "set"(arg0: integer, arg1: E): E
public "forEach"(arg0: $Consumer$Type<(any)>): void
public "capacity"(): integer
public "ensureCapacity"(arg0: integer): void
public "trimToSize"(): void
public "elementAt"(arg0: integer): E
public "removeIf"(arg0: $Predicate$Type<(any)>): boolean
public "sort"(arg0: $Comparator$Type<(any)>): void
public "removeAll"(arg0: $Collection$Type<(any)>): boolean
public "retainAll"(arg0: $Collection$Type<(any)>): boolean
public "listIterator"(): $ListIterator<(E)>
public "listIterator"(arg0: integer): $ListIterator<(E)>
public "containsAll"(arg0: $Collection$Type<(any)>): boolean
public "setSize"(arg0: integer): void
public "copyInto"(arg0: (any)[]): void
public "removeElementAt"(arg0: integer): void
public "removeElement"(arg0: any): boolean
public "insertElementAt"(arg0: E, arg1: integer): void
public "removeAllElements"(): void
public "firstElement"(): E
public "lastElement"(): E
public "setElementAt"(arg0: E, arg1: integer): void
public "addElement"(arg0: E): void
public static "copyOf"<E>(arg0: $Collection$Type<(any)>): $List<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E): $List<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E): $List<(E)>
public static "of"<E>(arg0: E, arg1: E): $List<(E)>
public static "of"<E>(arg0: E): $List<(E)>
public static "of"<E>(): $List<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E, arg5: E, arg6: E, arg7: E): $List<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E, arg5: E, arg6: E): $List<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E, arg5: E): $List<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E): $List<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E, arg5: E, arg6: E, arg7: E, arg8: E, arg9: E): $List<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E, arg5: E, arg6: E, arg7: E, arg8: E): $List<(E)>
public static "of"<E>(...arg0: (E)[]): $List<(E)>
get "empty"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Vector$Type<E> = ($Vector<(E)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Vector_<E> = $Vector$Type<(E)>;
}}
declare module "packages/java/util/function/$DoubleBinaryOperator" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $DoubleBinaryOperator {

 "applyAsDouble"(arg0: double, arg1: double): double

(arg0: double, arg1: double): double
}

export namespace $DoubleBinaryOperator {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DoubleBinaryOperator$Type = ($DoubleBinaryOperator);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DoubleBinaryOperator_ = $DoubleBinaryOperator$Type;
}}
declare module "packages/java/util/function/$UnaryOperator" {
import {$Function, $Function$Type} from "packages/java/util/function/$Function"

export interface $UnaryOperator<T> extends $Function<(T), (T)> {

 "apply"(arg0: T): T
 "compose"<V>(arg0: $Function$Type<(any), (any)>): $Function<(V), (T)>
 "andThen"<V>(arg0: $Function$Type<(any), (any)>): $Function<(T), (V)>

(): $UnaryOperator<(T)>
}

export namespace $UnaryOperator {
function identity<T>(): $UnaryOperator<(T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $UnaryOperator$Type<T> = ($UnaryOperator<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $UnaryOperator_<T> = $UnaryOperator$Type<(T)>;
}}
declare module "packages/java/util/function/$DoubleFunction" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $DoubleFunction<R> {

 "apply"(arg0: double): R

(arg0: double): R
}

export namespace $DoubleFunction {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DoubleFunction$Type<R> = ($DoubleFunction<(R)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DoubleFunction_<R> = $DoubleFunction$Type<(R)>;
}}
declare module "packages/java/util/function/$Function" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $Function<T, R> {

 "apply"(arg0: T): R
 "compose"<V>(arg0: $Function$Type<(any), (any)>): $Function<(V), (R)>
 "andThen"<V>(arg0: $Function$Type<(any), (any)>): $Function<(T), (V)>

(arg0: T): R
}

export namespace $Function {
function identity<T>(): $Function<(T), (T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Function$Type<T, R> = ($Function<(T), (R)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Function_<T, R> = $Function$Type<(T), (R)>;
}}
declare module "packages/java/util/logging/$Logger" {
import {$Filter, $Filter$Type} from "packages/java/util/logging/$Filter"
import {$Throwable, $Throwable$Type} from "packages/java/lang/$Throwable"
import {$LogRecord, $LogRecord$Type} from "packages/java/util/logging/$LogRecord"
import {$Handler, $Handler$Type} from "packages/java/util/logging/$Handler"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$Level, $Level$Type} from "packages/java/util/logging/$Level"
import {$ResourceBundle, $ResourceBundle$Type} from "packages/java/util/$ResourceBundle"

export class $Logger {
static readonly "GLOBAL_LOGGER_NAME": string
/**
 * 
 * @deprecated
 */
static readonly "global": $Logger


public "getName"(): string
public "log"(arg0: $Level$Type, arg1: string, arg2: $Throwable$Type): void
public "log"(arg0: $Level$Type, arg1: $Throwable$Type, arg2: $Supplier$Type<(string)>): void
public "log"(arg0: $Level$Type, arg1: string, arg2: any): void
public "log"(arg0: $Level$Type, arg1: string, arg2: (any)[]): void
public "log"(arg0: $LogRecord$Type): void
public "log"(arg0: $Level$Type, arg1: $Supplier$Type<(string)>): void
public "log"(arg0: $Level$Type, arg1: string): void
public "info"(arg0: string): void
public "info"(arg0: $Supplier$Type<(string)>): void
public "getParent"(): $Logger
public static "getLogger"(arg0: string, arg1: string): $Logger
public static "getLogger"(arg0: string): $Logger
public "setParent"(arg0: $Logger$Type): void
public "isLoggable"(arg0: $Level$Type): boolean
public "getFilter"(): $Filter
public "warning"(arg0: string): void
public "warning"(arg0: $Supplier$Type<(string)>): void
public "config"(arg0: string): void
public "config"(arg0: $Supplier$Type<(string)>): void
public "getLevel"(): $Level
public "setFilter"(arg0: $Filter$Type): void
public "setLevel"(arg0: $Level$Type): void
public "getResourceBundleName"(): string
public "addHandler"(arg0: $Handler$Type): void
public "getHandlers"(): ($Handler)[]
public "removeHandler"(arg0: $Handler$Type): void
public "setUseParentHandlers"(arg0: boolean): void
public static "getAnonymousLogger"(arg0: string): $Logger
public static "getAnonymousLogger"(): $Logger
public "getUseParentHandlers"(): boolean
public "setResourceBundle"(arg0: $ResourceBundle$Type): void
public "logp"(arg0: $Level$Type, arg1: string, arg2: string, arg3: string, arg4: any): void
public "logp"(arg0: $Level$Type, arg1: string, arg2: string, arg3: $Throwable$Type, arg4: $Supplier$Type<(string)>): void
public "logp"(arg0: $Level$Type, arg1: string, arg2: string, arg3: string, arg4: $Throwable$Type): void
public "logp"(arg0: $Level$Type, arg1: string, arg2: string, arg3: string): void
public "logp"(arg0: $Level$Type, arg1: string, arg2: string, arg3: string, arg4: (any)[]): void
public "logp"(arg0: $Level$Type, arg1: string, arg2: string, arg3: $Supplier$Type<(string)>): void
public "getResourceBundle"(): $ResourceBundle
public static "getGlobal"(): $Logger
public "logrb"(arg0: $Level$Type, arg1: string, arg2: string, arg3: $ResourceBundle$Type, arg4: string, ...arg5: (any)[]): void
public "logrb"(arg0: $Level$Type, arg1: $ResourceBundle$Type, arg2: string, ...arg3: (any)[]): void
/**
 * 
 * @deprecated
 */
public "logrb"(arg0: $Level$Type, arg1: string, arg2: string, arg3: string, arg4: string, arg5: $Throwable$Type): void
public "logrb"(arg0: $Level$Type, arg1: string, arg2: string, arg3: $ResourceBundle$Type, arg4: string, arg5: $Throwable$Type): void
public "logrb"(arg0: $Level$Type, arg1: $ResourceBundle$Type, arg2: string, arg3: $Throwable$Type): void
/**
 * 
 * @deprecated
 */
public "logrb"(arg0: $Level$Type, arg1: string, arg2: string, arg3: string, arg4: string): void
/**
 * 
 * @deprecated
 */
public "logrb"(arg0: $Level$Type, arg1: string, arg2: string, arg3: string, arg4: string, arg5: any): void
/**
 * 
 * @deprecated
 */
public "logrb"(arg0: $Level$Type, arg1: string, arg2: string, arg3: string, arg4: string, arg5: (any)[]): void
public "entering"(arg0: string, arg1: string, arg2: any): void
public "entering"(arg0: string, arg1: string): void
public "entering"(arg0: string, arg1: string, arg2: (any)[]): void
public "exiting"(arg0: string, arg1: string, arg2: any): void
public "exiting"(arg0: string, arg1: string): void
public "throwing"(arg0: string, arg1: string, arg2: $Throwable$Type): void
public "severe"(arg0: $Supplier$Type<(string)>): void
public "severe"(arg0: string): void
public "fine"(arg0: string): void
public "fine"(arg0: $Supplier$Type<(string)>): void
public "finer"(arg0: $Supplier$Type<(string)>): void
public "finer"(arg0: string): void
public "finest"(arg0: string): void
public "finest"(arg0: $Supplier$Type<(string)>): void
get "name"(): string
get "parent"(): $Logger
set "parent"(value: $Logger$Type)
get "filter"(): $Filter
get "level"(): $Level
set "filter"(value: $Filter$Type)
set "level"(value: $Level$Type)
get "resourceBundleName"(): string
get "handlers"(): ($Handler)[]
set "useParentHandlers"(value: boolean)
get "anonymousLogger"(): $Logger
get "useParentHandlers"(): boolean
set "resourceBundle"(value: $ResourceBundle$Type)
get "resourceBundle"(): $ResourceBundle
get "global"(): $Logger
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Logger$Type = ($Logger);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Logger_ = $Logger$Type;
}}
declare module "packages/java/util/$LinkedHashSet" {
import {$Serializable, $Serializable$Type} from "packages/java/io/$Serializable"
import {$HashSet, $HashSet$Type} from "packages/java/util/$HashSet"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Cloneable, $Cloneable$Type} from "packages/java/lang/$Cloneable"
import {$Spliterator, $Spliterator$Type} from "packages/java/util/$Spliterator"
import {$Iterator, $Iterator$Type} from "packages/java/util/$Iterator"

export class $LinkedHashSet<E> extends $HashSet<(E)> implements $Set<(E)>, $Cloneable, $Serializable {

constructor(arg0: $Collection$Type<(any)>)
constructor()
constructor(arg0: integer)
constructor(arg0: integer, arg1: float)

public "spliterator"(): $Spliterator<(E)>
public "add"(arg0: E): boolean
public "remove"(arg0: any): boolean
public "equals"(arg0: any): boolean
public "hashCode"(): integer
public static "copyOf"<E>(arg0: $Collection$Type<(any)>): $Set<(E)>
public "clear"(): void
public "isEmpty"(): boolean
public "size"(): integer
public "toArray"<T>(arg0: (T)[]): (T)[]
public "toArray"(): (any)[]
public "iterator"(): $Iterator<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E, arg5: E): $Set<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E): $Set<(E)>
public static "of"<E>(arg0: E): $Set<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E): $Set<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E, arg5: E, arg6: E, arg7: E, arg8: E, arg9: E): $Set<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E, arg5: E, arg6: E, arg7: E, arg8: E): $Set<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E, arg5: E, arg6: E, arg7: E): $Set<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E, arg5: E, arg6: E): $Set<(E)>
public static "of"<E>(...arg0: (E)[]): $Set<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E): $Set<(E)>
public static "of"<E>(): $Set<(E)>
public static "of"<E>(arg0: E, arg1: E): $Set<(E)>
public "contains"(arg0: any): boolean
public "addAll"(arg0: $Collection$Type<(any)>): boolean
public "removeAll"(arg0: $Collection$Type<(any)>): boolean
public "retainAll"(arg0: $Collection$Type<(any)>): boolean
public "containsAll"(arg0: $Collection$Type<(any)>): boolean
get "empty"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LinkedHashSet$Type<E> = ($LinkedHashSet<(E)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LinkedHashSet_<E> = $LinkedHashSet$Type<(E)>;
}}
declare module "packages/java/util/$IntSummaryStatistics" {
import {$IntConsumer, $IntConsumer$Type} from "packages/java/util/function/$IntConsumer"

export class $IntSummaryStatistics implements $IntConsumer {

constructor()
constructor(arg0: long, arg1: integer, arg2: integer, arg3: long)

public "toString"(): string
public "accept"(arg0: integer): void
public "combine"(arg0: $IntSummaryStatistics$Type): void
public "getCount"(): long
public "getSum"(): long
public "getMin"(): integer
public "getAverage"(): double
public "getMax"(): integer
public "andThen"(arg0: $IntConsumer$Type): $IntConsumer
get "count"(): long
get "sum"(): long
get "min"(): integer
get "average"(): double
get "max"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IntSummaryStatistics$Type = ($IntSummaryStatistics);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IntSummaryStatistics_ = $IntSummaryStatistics$Type;
}}
declare module "packages/java/util/concurrent/atomic/$AtomicReference" {
import {$BinaryOperator, $BinaryOperator$Type} from "packages/java/util/function/$BinaryOperator"
import {$Serializable, $Serializable$Type} from "packages/java/io/$Serializable"
import {$UnaryOperator, $UnaryOperator$Type} from "packages/java/util/function/$UnaryOperator"

export class $AtomicReference<V> implements $Serializable {

constructor(arg0: V)
constructor()

public "get"(): V
public "toString"(): string
public "set"(arg0: V): void
public "getOpaque"(): V
public "setOpaque"(arg0: V): void
public "getAcquire"(): V
public "setRelease"(arg0: V): void
public "compareAndSet"(arg0: V, arg1: V): boolean
public "compareAndExchange"(arg0: V, arg1: V): V
public "compareAndExchangeAcquire"(arg0: V, arg1: V): V
public "compareAndExchangeRelease"(arg0: V, arg1: V): V
public "weakCompareAndSetPlain"(arg0: V, arg1: V): boolean
/**
 * 
 * @deprecated
 */
public "weakCompareAndSet"(arg0: V, arg1: V): boolean
public "weakCompareAndSetAcquire"(arg0: V, arg1: V): boolean
public "weakCompareAndSetRelease"(arg0: V, arg1: V): boolean
public "getAndSet"(arg0: V): V
public "lazySet"(arg0: V): void
public "weakCompareAndSetVolatile"(arg0: V, arg1: V): boolean
public "getAndUpdate"(arg0: $UnaryOperator$Type<(V)>): V
public "updateAndGet"(arg0: $UnaryOperator$Type<(V)>): V
public "getAndAccumulate"(arg0: V, arg1: $BinaryOperator$Type<(V)>): V
public "accumulateAndGet"(arg0: V, arg1: $BinaryOperator$Type<(V)>): V
public "getPlain"(): V
public "setPlain"(arg0: V): void
get "opaque"(): V
set "opaque"(value: V)
get "acquire"(): V
set "release"(value: V)
get "plain"(): V
set "plain"(value: V)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AtomicReference$Type<V> = ($AtomicReference<(V)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AtomicReference_<V> = $AtomicReference$Type<(V)>;
}}
declare module "packages/java/util/logging/$LogRecord" {
import {$Throwable, $Throwable$Type} from "packages/java/lang/$Throwable"
import {$Serializable, $Serializable$Type} from "packages/java/io/$Serializable"
import {$Instant, $Instant$Type} from "packages/java/time/$Instant"
import {$Level, $Level$Type} from "packages/java/util/logging/$Level"
import {$ResourceBundle, $ResourceBundle$Type} from "packages/java/util/$ResourceBundle"

export class $LogRecord implements $Serializable {

constructor(arg0: $Level$Type, arg1: string)

public "getMessage"(): string
public "getParameters"(): (any)[]
public "setInstant"(arg0: $Instant$Type): void
public "getMillis"(): long
public "getLevel"(): $Level
public "setLevel"(arg0: $Level$Type): void
public "getResourceBundleName"(): string
public "getLoggerName"(): string
public "setLoggerName"(arg0: string): void
public "setResourceBundleName"(arg0: string): void
public "setResourceBundle"(arg0: $ResourceBundle$Type): void
public "setParameters"(arg0: (any)[]): void
public "setThrown"(arg0: $Throwable$Type): void
public "setSourceClassName"(arg0: string): void
public "setSourceMethodName"(arg0: string): void
public "getResourceBundle"(): $ResourceBundle
/**
 * 
 * @deprecated
 */
public "setMillis"(arg0: long): void
public "getInstant"(): $Instant
public "getThrown"(): $Throwable
public "setMessage"(arg0: string): void
public "getSequenceNumber"(): long
public "setSequenceNumber"(arg0: long): void
public "getSourceClassName"(): string
public "getSourceMethodName"(): string
/**
 * 
 * @deprecated
 */
public "getThreadID"(): integer
/**
 * 
 * @deprecated
 */
public "setThreadID"(arg0: integer): void
public "getLongThreadID"(): long
public "setLongThreadID"(arg0: long): $LogRecord
get "message"(): string
get "parameters"(): (any)[]
set "instant"(value: $Instant$Type)
get "millis"(): long
get "level"(): $Level
set "level"(value: $Level$Type)
get "resourceBundleName"(): string
get "loggerName"(): string
set "loggerName"(value: string)
set "resourceBundleName"(value: string)
set "resourceBundle"(value: $ResourceBundle$Type)
set "parameters"(value: (any)[])
set "thrown"(value: $Throwable$Type)
set "sourceClassName"(value: string)
set "sourceMethodName"(value: string)
get "resourceBundle"(): $ResourceBundle
set "millis"(value: long)
get "instant"(): $Instant
get "thrown"(): $Throwable
set "message"(value: string)
get "sequenceNumber"(): long
set "sequenceNumber"(value: long)
get "sourceClassName"(): string
get "sourceMethodName"(): string
get "threadID"(): integer
set "threadID"(value: integer)
get "longThreadID"(): long
set "longThreadID"(value: long)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LogRecord$Type = ($LogRecord);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LogRecord_ = $LogRecord$Type;
}}
declare module "packages/java/util/$UUID" {
import {$Comparable, $Comparable$Type} from "packages/java/lang/$Comparable"
import {$Serializable, $Serializable$Type} from "packages/java/io/$Serializable"

export class $UUID implements $Serializable, $Comparable<($UUID)> {

constructor(arg0: long, arg1: long)

public "equals"(arg0: any): boolean
public "toString"(): string
public "version"(): integer
public "hashCode"(): integer
public "compareTo"(arg0: $UUID$Type): integer
public "timestamp"(): long
public "node"(): long
public "variant"(): integer
public static "randomUUID"(): $UUID
public static "nameUUIDFromBytes"(arg0: (byte)[]): $UUID
public "getLeastSignificantBits"(): long
public "getMostSignificantBits"(): long
public "clockSequence"(): integer
public static "fromString"(arg0: string): $UUID
get "leastSignificantBits"(): long
get "mostSignificantBits"(): long
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $UUID$Type = (string) | ($UUID);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $UUID_ = $UUID$Type;
}}
declare module "packages/java/util/function/$BiConsumer" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $BiConsumer<T, U> {

 "accept"(arg0: T, arg1: U): void
 "andThen"(arg0: $BiConsumer$Type<(any), (any)>): $BiConsumer<(T), (U)>

(arg0: T, arg1: U): void
}

export namespace $BiConsumer {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BiConsumer$Type<T, U> = ($BiConsumer<(T), (U)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BiConsumer_<T, U> = $BiConsumer$Type<(T), (U)>;
}}
declare module "packages/java/util/$NavigableMap" {
import {$Comparator, $Comparator$Type} from "packages/java/util/$Comparator"
import {$BiConsumer, $BiConsumer$Type} from "packages/java/util/function/$BiConsumer"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$NavigableSet, $NavigableSet$Type} from "packages/java/util/$NavigableSet"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$SortedMap, $SortedMap$Type} from "packages/java/util/$SortedMap"
import {$BiFunction, $BiFunction$Type} from "packages/java/util/function/$BiFunction"
import {$Map$Entry, $Map$Entry$Type} from "packages/java/util/$Map$Entry"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export interface $NavigableMap<K, V> extends $SortedMap<(K), (V)> {

 "lowerEntry"(arg0: K): $Map$Entry<(K), (V)>
 "lowerKey"(arg0: K): K
 "floorEntry"(arg0: K): $Map$Entry<(K), (V)>
 "floorKey"(arg0: K): K
 "ceilingEntry"(arg0: K): $Map$Entry<(K), (V)>
 "ceilingKey"(arg0: K): K
 "higherEntry"(arg0: K): $Map$Entry<(K), (V)>
 "higherKey"(arg0: K): K
 "firstEntry"(): $Map$Entry<(K), (V)>
 "lastEntry"(): $Map$Entry<(K), (V)>
 "pollFirstEntry"(): $Map$Entry<(K), (V)>
 "pollLastEntry"(): $Map$Entry<(K), (V)>
 "descendingMap"(): $NavigableMap<(K), (V)>
 "navigableKeySet"(): $NavigableSet<(K)>
 "descendingKeySet"(): $NavigableSet<(K)>
 "subMap"(arg0: K, arg1: boolean, arg2: K, arg3: boolean): $NavigableMap<(K), (V)>
 "subMap"(arg0: K, arg1: K): $SortedMap<(K), (V)>
 "headMap"(arg0: K): $SortedMap<(K), (V)>
 "headMap"(arg0: K, arg1: boolean): $NavigableMap<(K), (V)>
 "tailMap"(arg0: K, arg1: boolean): $NavigableMap<(K), (V)>
 "tailMap"(arg0: K): $SortedMap<(K), (V)>
 "values"(): $Collection<(V)>
 "entrySet"(): $Set<($Map$Entry<(K), (V)>)>
 "keySet"(): $Set<(K)>
 "comparator"(): $Comparator<(any)>
 "firstKey"(): K
 "lastKey"(): K
 "remove"(arg0: any): V
 "remove"(arg0: any, arg1: any): boolean
 "get"(arg0: any): V
 "put"(arg0: K, arg1: V): V
 "equals"(arg0: any): boolean
 "hashCode"(): integer
 "clear"(): void
 "isEmpty"(): boolean
 "replace"(arg0: K, arg1: V): V
 "replace"(arg0: K, arg1: V, arg2: V): boolean
 "replaceAll"(arg0: $BiFunction$Type<(any), (any), (any)>): void
 "size"(): integer
 "merge"(arg0: K, arg1: V, arg2: $BiFunction$Type<(any), (any), (any)>): V
 "putAll"(arg0: $Map$Type<(any), (any)>): void
 "putIfAbsent"(arg0: K, arg1: V): V
 "compute"(arg0: K, arg1: $BiFunction$Type<(any), (any), (any)>): V
 "forEach"(arg0: $BiConsumer$Type<(any), (any)>): void
 "containsKey"(arg0: any): boolean
 "computeIfAbsent"(arg0: K, arg1: $Function$Type<(any), (any)>): V
 "containsValue"(arg0: any): boolean
 "getOrDefault"(arg0: any, arg1: V): V
 "computeIfPresent"(arg0: K, arg1: $BiFunction$Type<(any), (any), (any)>): V
}

export namespace $NavigableMap {
function copyOf<K, V>(arg0: $Map$Type<(any), (any)>): $Map<(K), (V)>
function of<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V, arg8: K, arg9: V): $Map<(K), (V)>
function of<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V): $Map<(K), (V)>
function of<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V): $Map<(K), (V)>
function of<K, V>(): $Map<(K), (V)>
function of<K, V>(arg0: K, arg1: V, arg2: K, arg3: V): $Map<(K), (V)>
function of<K, V>(arg0: K, arg1: V): $Map<(K), (V)>
function of<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V, arg8: K, arg9: V, arg10: K, arg11: V, arg12: K, arg13: V, arg14: K, arg15: V, arg16: K, arg17: V, arg18: K, arg19: V): $Map<(K), (V)>
function of<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V, arg8: K, arg9: V, arg10: K, arg11: V, arg12: K, arg13: V, arg14: K, arg15: V, arg16: K, arg17: V): $Map<(K), (V)>
function of<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V, arg8: K, arg9: V, arg10: K, arg11: V, arg12: K, arg13: V, arg14: K, arg15: V): $Map<(K), (V)>
function of<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V, arg8: K, arg9: V, arg10: K, arg11: V, arg12: K, arg13: V): $Map<(K), (V)>
function of<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V, arg8: K, arg9: V, arg10: K, arg11: V): $Map<(K), (V)>
function entry<K, V>(arg0: K, arg1: V): $Map$Entry<(K), (V)>
function ofEntries<K, V>(...arg0: ($Map$Entry$Type<(any), (any)>)[]): $Map<(K), (V)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NavigableMap$Type<K, V> = ($NavigableMap<(K), (V)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NavigableMap_<K, V> = $NavigableMap$Type<(K), (V)>;
}}
declare module "packages/java/util/concurrent/$ConcurrentLinkedDeque" {
import {$Serializable, $Serializable$Type} from "packages/java/io/$Serializable"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$AbstractCollection, $AbstractCollection$Type} from "packages/java/util/$AbstractCollection"
import {$Deque, $Deque$Type} from "packages/java/util/$Deque"
import {$Iterator, $Iterator$Type} from "packages/java/util/$Iterator"
import {$Spliterator, $Spliterator$Type} from "packages/java/util/$Spliterator"

export class $ConcurrentLinkedDeque<E> extends $AbstractCollection<(E)> implements $Deque<(E)>, $Serializable {

constructor()
constructor(arg0: $Collection$Type<(any)>)

public "add"(arg0: E): boolean
public "remove"(): E
public "remove"(arg0: any): boolean
public "toString"(): string
public "clear"(): void
public "isEmpty"(): boolean
public "size"(): integer
public "toArray"<T>(arg0: (T)[]): (T)[]
public "toArray"(): (any)[]
public "iterator"(): $Iterator<(E)>
public "contains"(arg0: any): boolean
public "spliterator"(): $Spliterator<(E)>
public "addAll"(arg0: $Collection$Type<(any)>): boolean
public "forEach"(arg0: $Consumer$Type<(any)>): void
public "poll"(): E
public "peek"(): E
public "removeIf"(arg0: $Predicate$Type<(any)>): boolean
public "element"(): E
public "removeAll"(arg0: $Collection$Type<(any)>): boolean
public "retainAll"(arg0: $Collection$Type<(any)>): boolean
public "push"(arg0: E): void
public "pop"(): E
public "addFirst"(arg0: E): void
public "addLast"(arg0: E): void
public "pollFirst"(): E
public "pollLast"(): E
public "offerLast"(arg0: E): boolean
public "removeFirst"(): E
public "getFirst"(): E
public "peekFirst"(): E
public "removeFirstOccurrence"(arg0: any): boolean
public "offerFirst"(arg0: E): boolean
public "removeLast"(): E
public "getLast"(): E
public "peekLast"(): E
public "removeLastOccurrence"(arg0: any): boolean
public "offer"(arg0: E): boolean
public "descendingIterator"(): $Iterator<(E)>
public "equals"(arg0: any): boolean
public "hashCode"(): integer
get "empty"(): boolean
get "first"(): E
get "last"(): E
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ConcurrentLinkedDeque$Type<E> = ($ConcurrentLinkedDeque<(E)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ConcurrentLinkedDeque_<E> = $ConcurrentLinkedDeque$Type<(E)>;
}}
declare module "packages/java/util/$PrimitiveIterator$OfDouble" {
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$DoubleConsumer, $DoubleConsumer$Type} from "packages/java/util/function/$DoubleConsumer"
import {$PrimitiveIterator, $PrimitiveIterator$Type} from "packages/java/util/$PrimitiveIterator"

export interface $PrimitiveIterator$OfDouble extends $PrimitiveIterator<(double), ($DoubleConsumer)> {

 "forEachRemaining"(arg0: $DoubleConsumer$Type): void
 "forEachRemaining"(arg0: $Consumer$Type<(any)>): void
 "nextDouble"(): double
 "remove"(): void
 "hasNext"(): boolean
}

export namespace $PrimitiveIterator$OfDouble {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PrimitiveIterator$OfDouble$Type = ($PrimitiveIterator$OfDouble);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PrimitiveIterator$OfDouble_ = $PrimitiveIterator$OfDouble$Type;
}}
declare module "packages/java/util/concurrent/$ConcurrentMap" {
import {$BiConsumer, $BiConsumer$Type} from "packages/java/util/function/$BiConsumer"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$BiFunction, $BiFunction$Type} from "packages/java/util/function/$BiFunction"
import {$Map, $Map$Type} from "packages/java/util/$Map"
import {$Map$Entry, $Map$Entry$Type} from "packages/java/util/$Map$Entry"

export interface $ConcurrentMap<K, V> extends $Map<(K), (V)> {

 "remove"(arg0: any, arg1: any): boolean
 "replace"(arg0: K, arg1: V, arg2: V): boolean
 "replace"(arg0: K, arg1: V): V
 "replaceAll"(arg0: $BiFunction$Type<(any), (any), (any)>): void
 "merge"(arg0: K, arg1: V, arg2: $BiFunction$Type<(any), (any), (any)>): V
 "putIfAbsent"(arg0: K, arg1: V): V
 "compute"(arg0: K, arg1: $BiFunction$Type<(any), (any), (any)>): V
 "forEach"(arg0: $BiConsumer$Type<(any), (any)>): void
 "computeIfAbsent"(arg0: K, arg1: $Function$Type<(any), (any)>): V
 "getOrDefault"(arg0: any, arg1: V): V
 "computeIfPresent"(arg0: K, arg1: $BiFunction$Type<(any), (any), (any)>): V
 "remove"(arg0: any): V
 "get"(arg0: any): V
 "put"(arg0: K, arg1: V): V
 "equals"(arg0: any): boolean
 "values"(): $Collection<(V)>
 "hashCode"(): integer
 "clear"(): void
 "isEmpty"(): boolean
 "size"(): integer
 "entrySet"(): $Set<($Map$Entry<(K), (V)>)>
 "putAll"(arg0: $Map$Type<(any), (any)>): void
 "containsKey"(arg0: any): boolean
 "keySet"(): $Set<(K)>
 "containsValue"(arg0: any): boolean
}

export namespace $ConcurrentMap {
function copyOf<K, V>(arg0: $Map$Type<(any), (any)>): $Map<(K), (V)>
function of<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V, arg8: K, arg9: V): $Map<(K), (V)>
function of<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V): $Map<(K), (V)>
function of<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V): $Map<(K), (V)>
function of<K, V>(): $Map<(K), (V)>
function of<K, V>(arg0: K, arg1: V, arg2: K, arg3: V): $Map<(K), (V)>
function of<K, V>(arg0: K, arg1: V): $Map<(K), (V)>
function of<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V, arg8: K, arg9: V, arg10: K, arg11: V, arg12: K, arg13: V, arg14: K, arg15: V, arg16: K, arg17: V, arg18: K, arg19: V): $Map<(K), (V)>
function of<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V, arg8: K, arg9: V, arg10: K, arg11: V, arg12: K, arg13: V, arg14: K, arg15: V, arg16: K, arg17: V): $Map<(K), (V)>
function of<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V, arg8: K, arg9: V, arg10: K, arg11: V, arg12: K, arg13: V, arg14: K, arg15: V): $Map<(K), (V)>
function of<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V, arg8: K, arg9: V, arg10: K, arg11: V, arg12: K, arg13: V): $Map<(K), (V)>
function of<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V, arg8: K, arg9: V, arg10: K, arg11: V): $Map<(K), (V)>
function entry<K, V>(arg0: K, arg1: V): $Map$Entry<(K), (V)>
function ofEntries<K, V>(...arg0: ($Map$Entry$Type<(any), (any)>)[]): $Map<(K), (V)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ConcurrentMap$Type<K, V> = ($ConcurrentMap<(K), (V)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ConcurrentMap_<K, V> = $ConcurrentMap$Type<(K), (V)>;
}}
declare module "packages/java/util/$Enumeration" {
import {$Iterator, $Iterator$Type} from "packages/java/util/$Iterator"

export interface $Enumeration<E> {

 "asIterator"(): $Iterator<(E)>
 "hasMoreElements"(): boolean
 "nextElement"(): E
}

export namespace $Enumeration {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Enumeration$Type<E> = ($Enumeration<(E)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Enumeration_<E> = $Enumeration$Type<(E)>;
}}
declare module "packages/java/util/$Calendar" {
import {$Comparable, $Comparable$Type} from "packages/java/lang/$Comparable"
import {$Serializable, $Serializable$Type} from "packages/java/io/$Serializable"
import {$Date, $Date$Type} from "packages/java/util/$Date"
import {$Instant, $Instant$Type} from "packages/java/time/$Instant"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$Cloneable, $Cloneable$Type} from "packages/java/lang/$Cloneable"
import {$TimeZone, $TimeZone$Type} from "packages/java/util/$TimeZone"
import {$Locale, $Locale$Type} from "packages/java/util/$Locale"
import {$Map, $Map$Type} from "packages/java/util/$Map"

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
static readonly "FIELD_COUNT": integer
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
static readonly "ALL_STYLES": integer
static readonly "SHORT": integer
static readonly "LONG": integer
static readonly "NARROW_FORMAT": integer
static readonly "NARROW_STANDALONE": integer
static readonly "SHORT_FORMAT": integer
static readonly "LONG_FORMAT": integer
static readonly "SHORT_STANDALONE": integer
static readonly "LONG_STANDALONE": integer


public "add"(arg0: integer, arg1: integer): void
public "get"(arg0: integer): integer
public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "clone"(): any
public "compareTo"(arg0: $Calendar$Type): integer
public "clear"(arg0: integer): void
public "clear"(): void
public static "getInstance"(): $Calendar
public static "getInstance"(arg0: $Locale$Type): $Calendar
public static "getInstance"(arg0: $TimeZone$Type): $Calendar
public static "getInstance"(arg0: $TimeZone$Type, arg1: $Locale$Type): $Calendar
public "set"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer): void
public "set"(arg0: integer, arg1: integer, arg2: integer): void
public "set"(arg0: integer, arg1: integer): void
public "set"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer): void
public "isSet"(arg0: integer): boolean
public "before"(arg0: any): boolean
public "after"(arg0: any): boolean
public "getDisplayName"(arg0: integer, arg1: integer, arg2: $Locale$Type): string
public "toInstant"(): $Instant
public static "getAvailableLocales"(): ($Locale)[]
public "setTimeZone"(arg0: $TimeZone$Type): void
public "getTimeZone"(): $TimeZone
public "setLenient"(arg0: boolean): void
public "isLenient"(): boolean
public "getFirstDayOfWeek"(): integer
public "getMinimalDaysInFirstWeek"(): integer
public "setTimeInMillis"(arg0: long): void
public "getTime"(): $Date
public "setTime"(arg0: $Date$Type): void
public "isWeekDateSupported"(): boolean
public "getWeekYear"(): integer
public "getMaximum"(arg0: integer): integer
public "getLeastMaximum"(arg0: integer): integer
public "getDisplayNames"(arg0: integer, arg1: integer, arg2: $Locale$Type): $Map<(string), (integer)>
public "getCalendarType"(): string
public "getMinimum"(arg0: integer): integer
public "getTimeInMillis"(): long
public "roll"(arg0: integer, arg1: integer): void
public "roll"(arg0: integer, arg1: boolean): void
public "getGreatestMinimum"(arg0: integer): integer
public static "getAvailableCalendarTypes"(): $Set<(string)>
public "setFirstDayOfWeek"(arg0: integer): void
public "setMinimalDaysInFirstWeek"(arg0: integer): void
public "setWeekDate"(arg0: integer, arg1: integer, arg2: integer): void
public "getWeeksInWeekYear"(): integer
public "getActualMinimum"(arg0: integer): integer
public "getActualMaximum"(arg0: integer): integer
get "instance"(): $Calendar
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
get "weekDateSupported"(): boolean
get "weekYear"(): integer
get "calendarType"(): string
get "timeInMillis"(): long
get "availableCalendarTypes"(): $Set<(string)>
set "firstDayOfWeek"(value: integer)
set "minimalDaysInFirstWeek"(value: integer)
get "weeksInWeekYear"(): integer
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
declare module "packages/java/util/$HashSet" {
import {$Serializable, $Serializable$Type} from "packages/java/io/$Serializable"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Cloneable, $Cloneable$Type} from "packages/java/lang/$Cloneable"
import {$AbstractSet, $AbstractSet$Type} from "packages/java/util/$AbstractSet"
import {$Iterator, $Iterator$Type} from "packages/java/util/$Iterator"
import {$Spliterator, $Spliterator$Type} from "packages/java/util/$Spliterator"

export class $HashSet<E> extends $AbstractSet<(E)> implements $Set<(E)>, $Cloneable, $Serializable {

constructor(arg0: integer)
constructor(arg0: integer, arg1: float)
constructor(arg0: $Collection$Type<(any)>)
constructor()

public "add"(arg0: E): boolean
public "remove"(arg0: any): boolean
public "clone"(): any
public "clear"(): void
public "isEmpty"(): boolean
public "size"(): integer
public "toArray"<T>(arg0: (T)[]): (T)[]
public "toArray"(): (any)[]
public "iterator"(): $Iterator<(E)>
public "contains"(arg0: any): boolean
public "spliterator"(): $Spliterator<(E)>
public "equals"(arg0: any): boolean
public "hashCode"(): integer
public static "copyOf"<E>(arg0: $Collection$Type<(any)>): $Set<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E, arg5: E): $Set<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E): $Set<(E)>
public static "of"<E>(arg0: E): $Set<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E): $Set<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E, arg5: E, arg6: E, arg7: E, arg8: E, arg9: E): $Set<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E, arg5: E, arg6: E, arg7: E, arg8: E): $Set<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E, arg5: E, arg6: E, arg7: E): $Set<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E, arg5: E, arg6: E): $Set<(E)>
public static "of"<E>(...arg0: (E)[]): $Set<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E): $Set<(E)>
public static "of"<E>(): $Set<(E)>
public static "of"<E>(arg0: E, arg1: E): $Set<(E)>
public "addAll"(arg0: $Collection$Type<(any)>): boolean
public "removeAll"(arg0: $Collection$Type<(any)>): boolean
public "retainAll"(arg0: $Collection$Type<(any)>): boolean
public "containsAll"(arg0: $Collection$Type<(any)>): boolean
get "empty"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $HashSet$Type<E> = ($HashSet<(E)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $HashSet_<E> = $HashSet$Type<(E)>;
}}
declare module "packages/java/util/function/$DoublePredicate" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $DoublePredicate {

 "test"(arg0: double): boolean
 "or"(arg0: $DoublePredicate$Type): $DoublePredicate
 "negate"(): $DoublePredicate
 "and"(arg0: $DoublePredicate$Type): $DoublePredicate

(arg0: double): boolean
}

export namespace $DoublePredicate {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DoublePredicate$Type = ($DoublePredicate);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DoublePredicate_ = $DoublePredicate$Type;
}}
declare module "packages/java/util/concurrent/$ScheduledFuture" {
import {$Future, $Future$Type} from "packages/java/util/concurrent/$Future"
import {$TimeUnit, $TimeUnit$Type} from "packages/java/util/concurrent/$TimeUnit"
import {$Delayed, $Delayed$Type} from "packages/java/util/concurrent/$Delayed"

export interface $ScheduledFuture<V> extends $Delayed, $Future<(V)> {

 "getDelay"(arg0: $TimeUnit$Type): long
 "get"(arg0: long, arg1: $TimeUnit$Type): V
 "get"(): V
 "isDone"(): boolean
 "cancel"(arg0: boolean): boolean
 "isCancelled"(): boolean
 "compareTo"(arg0: $Delayed$Type): integer
}

export namespace $ScheduledFuture {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ScheduledFuture$Type<V> = ($ScheduledFuture<(V)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ScheduledFuture_<V> = $ScheduledFuture$Type<(V)>;
}}
declare module "packages/java/util/concurrent/$ThreadFactory" {
import {$Thread, $Thread$Type} from "packages/java/lang/$Thread"
import {$Runnable, $Runnable$Type} from "packages/java/lang/$Runnable"

export interface $ThreadFactory {

 "newThread"(arg0: $Runnable$Type): $Thread

(arg0: $Runnable$Type): $Thread
}

export namespace $ThreadFactory {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ThreadFactory$Type = ($ThreadFactory);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ThreadFactory_ = $ThreadFactory$Type;
}}
declare module "packages/java/util/$Locale" {
import {$Locale$FilteringMode, $Locale$FilteringMode$Type} from "packages/java/util/$Locale$FilteringMode"
import {$Serializable, $Serializable$Type} from "packages/java/io/$Serializable"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$Locale$IsoCountryCode, $Locale$IsoCountryCode$Type} from "packages/java/util/$Locale$IsoCountryCode"
import {$Locale$Category, $Locale$Category$Type} from "packages/java/util/$Locale$Category"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Cloneable, $Cloneable$Type} from "packages/java/lang/$Cloneable"
import {$Locale$LanguageRange, $Locale$LanguageRange$Type} from "packages/java/util/$Locale$LanguageRange"

export class $Locale implements $Cloneable, $Serializable {
static readonly "ENGLISH": $Locale
static readonly "FRENCH": $Locale
static readonly "GERMAN": $Locale
static readonly "ITALIAN": $Locale
static readonly "JAPANESE": $Locale
static readonly "KOREAN": $Locale
static readonly "CHINESE": $Locale
static readonly "SIMPLIFIED_CHINESE": $Locale
static readonly "TRADITIONAL_CHINESE": $Locale
static readonly "FRANCE": $Locale
static readonly "GERMANY": $Locale
static readonly "ITALY": $Locale
static readonly "JAPAN": $Locale
static readonly "KOREA": $Locale
static readonly "UK": $Locale
static readonly "US": $Locale
static readonly "CANADA": $Locale
static readonly "CANADA_FRENCH": $Locale
static readonly "ROOT": $Locale
static readonly "CHINA": $Locale
static readonly "PRC": $Locale
static readonly "TAIWAN": $Locale
static readonly "PRIVATE_USE_EXTENSION": character
static readonly "UNICODE_LOCALE_EXTENSION": character

constructor(arg0: string)
constructor(arg0: string, arg1: string, arg2: string)
constructor(arg0: string, arg1: string)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "clone"(): any
public static "getDefault"(arg0: $Locale$Category$Type): $Locale
public static "getDefault"(): $Locale
public static "lookup"(arg0: $List$Type<($Locale$LanguageRange$Type)>, arg1: $Collection$Type<($Locale$Type)>): $Locale
public static "filter"(arg0: $List$Type<($Locale$LanguageRange$Type)>, arg1: $Collection$Type<($Locale$Type)>): $List<($Locale)>
public static "filter"(arg0: $List$Type<($Locale$LanguageRange$Type)>, arg1: $Collection$Type<($Locale$Type)>, arg2: $Locale$FilteringMode$Type): $List<($Locale)>
public "getLanguage"(): string
public "getDisplayName"(): string
public "getDisplayName"(arg0: $Locale$Type): string
public static "getAvailableLocales"(): ($Locale)[]
public "getUnicodeLocaleType"(arg0: string): string
public "getCountry"(): string
public "stripExtensions"(): $Locale
public "hasExtensions"(): boolean
public "getVariant"(): string
public "getScript"(): string
public static "setDefault"(arg0: $Locale$Category$Type, arg1: $Locale$Type): void
public static "setDefault"(arg0: $Locale$Type): void
public "getUnicodeLocaleAttributes"(): $Set<(string)>
public "getUnicodeLocaleKeys"(): $Set<(string)>
public "getDisplayLanguage"(): string
public "getDisplayLanguage"(arg0: $Locale$Type): string
public "getDisplayScript"(arg0: $Locale$Type): string
public "getDisplayScript"(): string
public "getDisplayCountry"(): string
public "getDisplayCountry"(arg0: $Locale$Type): string
public "getDisplayVariant"(): string
public "getDisplayVariant"(arg0: $Locale$Type): string
public static "filterTags"(arg0: $List$Type<($Locale$LanguageRange$Type)>, arg1: $Collection$Type<(string)>): $List<(string)>
public static "filterTags"(arg0: $List$Type<($Locale$LanguageRange$Type)>, arg1: $Collection$Type<(string)>, arg2: $Locale$FilteringMode$Type): $List<(string)>
public static "lookupTag"(arg0: $List$Type<($Locale$LanguageRange$Type)>, arg1: $Collection$Type<(string)>): string
public static "getISOCountries"(arg0: $Locale$IsoCountryCode$Type): $Set<(string)>
public static "getISOCountries"(): (string)[]
public static "getISOLanguages"(): (string)[]
public "getExtension"(arg0: character): string
public "getExtensionKeys"(): $Set<(character)>
public "toLanguageTag"(): string
public static "forLanguageTag"(arg0: string): $Locale
public "getISO3Language"(): string
public "getISO3Country"(): string
get "default"(): $Locale
get "language"(): string
get "displayName"(): string
get "availableLocales"(): ($Locale)[]
get "country"(): string
get "variant"(): string
get "script"(): string
set "default"(value: $Locale$Type)
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
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Locale$Type = ($Locale);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Locale_ = $Locale$Type;
}}
declare module "packages/java/util/concurrent/locks/$ReentrantLock" {
import {$Condition, $Condition$Type} from "packages/java/util/concurrent/locks/$Condition"
import {$Serializable, $Serializable$Type} from "packages/java/io/$Serializable"
import {$Thread, $Thread$Type} from "packages/java/lang/$Thread"
import {$Lock, $Lock$Type} from "packages/java/util/concurrent/locks/$Lock"
import {$TimeUnit, $TimeUnit$Type} from "packages/java/util/concurrent/$TimeUnit"

export class $ReentrantLock implements $Lock, $Serializable {

constructor()
constructor(arg0: boolean)

public "lock"(): void
public "toString"(): string
public "lockInterruptibly"(): void
public "tryLock"(): boolean
public "tryLock"(arg0: long, arg1: $TimeUnit$Type): boolean
public "newCondition"(): $Condition
public "getHoldCount"(): integer
public "isLocked"(): boolean
public "hasQueuedThreads"(): boolean
public "getQueueLength"(): integer
public "hasWaiters"(arg0: $Condition$Type): boolean
public "getWaitQueueLength"(arg0: $Condition$Type): integer
public "unlock"(): void
public "isHeldByCurrentThread"(): boolean
public "isFair"(): boolean
public "hasQueuedThread"(arg0: $Thread$Type): boolean
get "holdCount"(): integer
get "locked"(): boolean
get "queueLength"(): integer
get "heldByCurrentThread"(): boolean
get "fair"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ReentrantLock$Type = ($ReentrantLock);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ReentrantLock_ = $ReentrantLock$Type;
}}
declare module "packages/java/util/$ArrayList" {
import {$Comparator, $Comparator$Type} from "packages/java/util/$Comparator"
import {$Serializable, $Serializable$Type} from "packages/java/io/$Serializable"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$AbstractList, $AbstractList$Type} from "packages/java/util/$AbstractList"
import {$ListIterator, $ListIterator$Type} from "packages/java/util/$ListIterator"
import {$Spliterator, $Spliterator$Type} from "packages/java/util/$Spliterator"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$UnaryOperator, $UnaryOperator$Type} from "packages/java/util/function/$UnaryOperator"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Cloneable, $Cloneable$Type} from "packages/java/lang/$Cloneable"
import {$RandomAccess, $RandomAccess$Type} from "packages/java/util/$RandomAccess"
import {$Iterator, $Iterator$Type} from "packages/java/util/$Iterator"

export class $ArrayList<E> extends $AbstractList<(E)> implements $List<(E)>, $RandomAccess, $Cloneable, $Serializable {

constructor(arg0: $Collection$Type<(any)>)
constructor()
constructor(arg0: integer)

public "add"(arg0: E): boolean
public "add"(arg0: integer, arg1: E): void
public "remove"(arg0: any): boolean
public "remove"(arg0: integer): E
public "get"(arg0: integer): E
public "equals"(arg0: any): boolean
public "hashCode"(): integer
public "clone"(): any
public "indexOf"(arg0: any): integer
public "clear"(): void
public "lastIndexOf"(arg0: any): integer
public "isEmpty"(): boolean
public "replaceAll"(arg0: $UnaryOperator$Type<(E)>): void
public "size"(): integer
public "subList"(arg0: integer, arg1: integer): $List<(E)>
public "toArray"<T>(arg0: (T)[]): (T)[]
public "toArray"(): (any)[]
public "iterator"(): $Iterator<(E)>
public "contains"(arg0: any): boolean
public "spliterator"(): $Spliterator<(E)>
public "addAll"(arg0: $Collection$Type<(any)>): boolean
public "addAll"(arg0: integer, arg1: $Collection$Type<(any)>): boolean
public "set"(arg0: integer, arg1: E): E
public "forEach"(arg0: $Consumer$Type<(any)>): void
public "ensureCapacity"(arg0: integer): void
public "trimToSize"(): void
public "removeIf"(arg0: $Predicate$Type<(any)>): boolean
public "sort"(arg0: $Comparator$Type<(any)>): void
public "removeAll"(arg0: $Collection$Type<(any)>): boolean
public "retainAll"(arg0: $Collection$Type<(any)>): boolean
public "listIterator"(): $ListIterator<(E)>
public "listIterator"(arg0: integer): $ListIterator<(E)>
public static "copyOf"<E>(arg0: $Collection$Type<(any)>): $List<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E): $List<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E): $List<(E)>
public static "of"<E>(arg0: E, arg1: E): $List<(E)>
public static "of"<E>(arg0: E): $List<(E)>
public static "of"<E>(): $List<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E, arg5: E, arg6: E, arg7: E): $List<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E, arg5: E, arg6: E): $List<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E, arg5: E): $List<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E): $List<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E, arg5: E, arg6: E, arg7: E, arg8: E, arg9: E): $List<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E, arg5: E, arg6: E, arg7: E, arg8: E): $List<(E)>
public static "of"<E>(...arg0: (E)[]): $List<(E)>
public "containsAll"(arg0: $Collection$Type<(any)>): boolean
get "empty"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ArrayList$Type<E> = ($ArrayList<(E)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ArrayList_<E> = $ArrayList$Type<(E)>;
}}
declare module "packages/java/util/zip/$ZipConstants" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export interface $ZipConstants {

}

export namespace $ZipConstants {
const LOCSIG: long
const EXTSIG: long
const CENSIG: long
const ENDSIG: long
const LOCHDR: integer
const EXTHDR: integer
const CENHDR: integer
const ENDHDR: integer
const LOCVER: integer
const LOCFLG: integer
const LOCHOW: integer
const LOCTIM: integer
const LOCCRC: integer
const LOCSIZ: integer
const LOCLEN: integer
const LOCNAM: integer
const LOCEXT: integer
const EXTCRC: integer
const EXTSIZ: integer
const EXTLEN: integer
const CENVEM: integer
const CENVER: integer
const CENFLG: integer
const CENHOW: integer
const CENTIM: integer
const CENCRC: integer
const CENSIZ: integer
const CENLEN: integer
const CENNAM: integer
const CENEXT: integer
const CENCOM: integer
const CENDSK: integer
const CENATT: integer
const CENATX: integer
const CENOFF: integer
const ENDSUB: integer
const ENDTOT: integer
const ENDSIZ: integer
const ENDOFF: integer
const ENDCOM: integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ZipConstants$Type = ($ZipConstants);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ZipConstants_ = $ZipConstants$Type;
}}
declare module "packages/java/util/$Spliterator$OfLong" {
import {$Comparator, $Comparator$Type} from "packages/java/util/$Comparator"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$LongConsumer, $LongConsumer$Type} from "packages/java/util/function/$LongConsumer"
import {$Spliterator$OfPrimitive, $Spliterator$OfPrimitive$Type} from "packages/java/util/$Spliterator$OfPrimitive"

export interface $Spliterator$OfLong extends $Spliterator$OfPrimitive<(long), ($LongConsumer), ($Spliterator$OfLong)> {

 "forEachRemaining"(arg0: $LongConsumer$Type): void
 "forEachRemaining"(arg0: $Consumer$Type<(any)>): void
 "tryAdvance"(arg0: $Consumer$Type<(any)>): boolean
 "tryAdvance"(arg0: $LongConsumer$Type): boolean
 "characteristics"(): integer
 "estimateSize"(): long
 "getExactSizeIfKnown"(): long
 "hasCharacteristics"(arg0: integer): boolean
 "getComparator"(): $Comparator<(any)>
}

export namespace $Spliterator$OfLong {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Spliterator$OfLong$Type = ($Spliterator$OfLong);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Spliterator$OfLong_ = $Spliterator$OfLong$Type;
}}
declare module "packages/java/util/zip/$Deflater" {
import {$ByteBuffer, $ByteBuffer$Type} from "packages/java/nio/$ByteBuffer"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $Deflater {
static readonly "DEFLATED": integer
static readonly "NO_COMPRESSION": integer
static readonly "BEST_SPEED": integer
static readonly "BEST_COMPRESSION": integer
static readonly "DEFAULT_COMPRESSION": integer
static readonly "FILTERED": integer
static readonly "HUFFMAN_ONLY": integer
static readonly "DEFAULT_STRATEGY": integer
static readonly "NO_FLUSH": integer
static readonly "SYNC_FLUSH": integer
static readonly "FULL_FLUSH": integer

constructor()
constructor(arg0: integer)
constructor(arg0: integer, arg1: boolean)

public "end"(): void
public "reset"(): void
public "finish"(): void
public "setLevel"(arg0: integer): void
public "setInput"(arg0: (byte)[]): void
public "setInput"(arg0: (byte)[], arg1: integer, arg2: integer): void
public "setInput"(arg0: $ByteBuffer$Type): void
public "setDictionary"(arg0: (byte)[], arg1: integer, arg2: integer): void
public "setDictionary"(arg0: $ByteBuffer$Type): void
public "setDictionary"(arg0: (byte)[]): void
public "finished"(): boolean
public "getAdler"(): integer
public "getBytesRead"(): long
public "getBytesWritten"(): long
public "needsInput"(): boolean
public "getTotalIn"(): integer
public "getTotalOut"(): integer
public "deflate"(arg0: $ByteBuffer$Type, arg1: integer): integer
public "deflate"(arg0: (byte)[], arg1: integer, arg2: integer): integer
public "deflate"(arg0: (byte)[]): integer
public "deflate"(arg0: $ByteBuffer$Type): integer
public "deflate"(arg0: (byte)[], arg1: integer, arg2: integer, arg3: integer): integer
public "setStrategy"(arg0: integer): void
set "level"(value: integer)
set "input"(value: (byte)[])
set "input"(value: $ByteBuffer$Type)
set "dictionary"(value: $ByteBuffer$Type)
set "dictionary"(value: (byte)[])
get "adler"(): integer
get "bytesRead"(): long
get "bytesWritten"(): long
get "totalIn"(): integer
get "totalOut"(): integer
set "strategy"(value: integer)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Deflater$Type = ($Deflater);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Deflater_ = $Deflater$Type;
}}
declare module "packages/java/util/$ResourceBundle$Control" {
import {$List, $List$Type} from "packages/java/util/$List"
import {$ClassLoader, $ClassLoader$Type} from "packages/java/lang/$ClassLoader"
import {$ResourceBundle, $ResourceBundle$Type} from "packages/java/util/$ResourceBundle"
import {$Locale, $Locale$Type} from "packages/java/util/$Locale"

export class $ResourceBundle$Control {
static readonly "FORMAT_DEFAULT": $List<(string)>
static readonly "FORMAT_CLASS": $List<(string)>
static readonly "FORMAT_PROPERTIES": $List<(string)>
static readonly "TTL_DONT_CACHE": long
static readonly "TTL_NO_EXPIRATION_CONTROL": long


public static "getControl"(arg0: $List$Type<(string)>): $ResourceBundle$Control
public "getFormats"(arg0: string): $List<(string)>
public "getCandidateLocales"(arg0: string, arg1: $Locale$Type): $List<($Locale)>
public "getFallbackLocale"(arg0: string, arg1: $Locale$Type): $Locale
public "newBundle"(arg0: string, arg1: $Locale$Type, arg2: string, arg3: $ClassLoader$Type, arg4: boolean): $ResourceBundle
public "needsReload"(arg0: string, arg1: $Locale$Type, arg2: string, arg3: $ClassLoader$Type, arg4: $ResourceBundle$Type, arg5: long): boolean
public "getTimeToLive"(arg0: string, arg1: $Locale$Type): long
public "toBundleName"(arg0: string, arg1: $Locale$Type): string
public "toResourceName"(arg0: string, arg1: string): string
public static "getNoFallbackControl"(arg0: $List$Type<(string)>): $ResourceBundle$Control
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ResourceBundle$Control$Type = ($ResourceBundle$Control);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ResourceBundle$Control_ = $ResourceBundle$Control$Type;
}}
declare module "packages/java/util/concurrent/$TimeUnit" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$ChronoUnit, $ChronoUnit$Type} from "packages/java/time/temporal/$ChronoUnit"
import {$Thread, $Thread$Type} from "packages/java/lang/$Thread"
import {$Duration, $Duration$Type} from "packages/java/time/$Duration"

export class $TimeUnit extends $Enum<($TimeUnit)> {
static readonly "NANOSECONDS": $TimeUnit
static readonly "MICROSECONDS": $TimeUnit
static readonly "MILLISECONDS": $TimeUnit
static readonly "SECONDS": $TimeUnit
static readonly "MINUTES": $TimeUnit
static readonly "HOURS": $TimeUnit
static readonly "DAYS": $TimeUnit


public static "values"(): ($TimeUnit)[]
public "convert"(arg0: $Duration$Type): long
public "convert"(arg0: long, arg1: $TimeUnit$Type): long
public static "valueOf"(arg0: string): $TimeUnit
public static "of"(arg0: $ChronoUnit$Type): $TimeUnit
public "sleep"(arg0: long): void
public "toMillis"(arg0: long): long
public "toNanos"(arg0: long): long
public "toDays"(arg0: long): long
public "toSeconds"(arg0: long): long
public "toHours"(arg0: long): long
public "toMinutes"(arg0: long): long
public "toMicros"(arg0: long): long
public "timedWait"(arg0: any, arg1: long): void
public "timedJoin"(arg0: $Thread$Type, arg1: long): void
public "toChronoUnit"(): $ChronoUnit
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TimeUnit$Type = (("milliseconds") | ("seconds") | ("hours") | ("minutes") | ("days") | ("nanoseconds") | ("microseconds")) | ($TimeUnit);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TimeUnit_ = $TimeUnit$Type;
}}
declare module "packages/java/util/$AbstractMap" {
import {$BiConsumer, $BiConsumer$Type} from "packages/java/util/function/$BiConsumer"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$BiFunction, $BiFunction$Type} from "packages/java/util/function/$BiFunction"
import {$Map$Entry, $Map$Entry$Type} from "packages/java/util/$Map$Entry"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $AbstractMap<K, V> implements $Map<(K), (V)> {


public "remove"(arg0: any): V
public "get"(arg0: any): V
public "put"(arg0: K, arg1: V): V
public "equals"(arg0: any): boolean
public "toString"(): string
public "values"(): $Collection<(V)>
public "hashCode"(): integer
public "clear"(): void
public "isEmpty"(): boolean
public "size"(): integer
public "entrySet"(): $Set<($Map$Entry<(K), (V)>)>
public "putAll"(arg0: $Map$Type<(any), (any)>): void
public "containsKey"(arg0: any): boolean
public "keySet"(): $Set<(K)>
public "containsValue"(arg0: any): boolean
public "remove"(arg0: any, arg1: any): boolean
public static "copyOf"<K, V>(arg0: $Map$Type<(any), (any)>): $Map<(K), (V)>
public "replace"(arg0: K, arg1: V): V
public "replace"(arg0: K, arg1: V, arg2: V): boolean
public "replaceAll"(arg0: $BiFunction$Type<(any), (any), (any)>): void
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V, arg8: K, arg9: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V): $Map<(K), (V)>
public static "of"<K, V>(): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V, arg8: K, arg9: V, arg10: K, arg11: V, arg12: K, arg13: V, arg14: K, arg15: V, arg16: K, arg17: V, arg18: K, arg19: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V, arg8: K, arg9: V, arg10: K, arg11: V, arg12: K, arg13: V, arg14: K, arg15: V, arg16: K, arg17: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V, arg8: K, arg9: V, arg10: K, arg11: V, arg12: K, arg13: V, arg14: K, arg15: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V, arg8: K, arg9: V, arg10: K, arg11: V, arg12: K, arg13: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V, arg8: K, arg9: V, arg10: K, arg11: V): $Map<(K), (V)>
public "merge"(arg0: K, arg1: V, arg2: $BiFunction$Type<(any), (any), (any)>): V
public "putIfAbsent"(arg0: K, arg1: V): V
public "compute"(arg0: K, arg1: $BiFunction$Type<(any), (any), (any)>): V
public static "entry"<K, V>(arg0: K, arg1: V): $Map$Entry<(K), (V)>
public "forEach"(arg0: $BiConsumer$Type<(any), (any)>): void
public "computeIfAbsent"(arg0: K, arg1: $Function$Type<(any), (any)>): V
public "getOrDefault"(arg0: any, arg1: V): V
public "computeIfPresent"(arg0: K, arg1: $BiFunction$Type<(any), (any), (any)>): V
public static "ofEntries"<K, V>(...arg0: ($Map$Entry$Type<(any), (any)>)[]): $Map<(K), (V)>
[index: string | number]: V
get "empty"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AbstractMap$Type<K, V> = ($AbstractMap<(K), (V)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AbstractMap_<K, V> = $AbstractMap$Type<(K), (V)>;
}}
declare module "packages/java/util/$TimeZone" {
import {$Serializable, $Serializable$Type} from "packages/java/io/$Serializable"
import {$Date, $Date$Type} from "packages/java/util/$Date"
import {$ZoneId, $ZoneId$Type} from "packages/java/time/$ZoneId"
import {$Cloneable, $Cloneable$Type} from "packages/java/lang/$Cloneable"
import {$Locale, $Locale$Type} from "packages/java/util/$Locale"

export class $TimeZone implements $Serializable, $Cloneable {
static readonly "SHORT": integer
static readonly "LONG": integer

constructor()

public "clone"(): any
public static "getDefault"(): $TimeZone
public "getOffset"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer): integer
public "getOffset"(arg0: long): integer
public "getDisplayName"(): string
public "getDisplayName"(arg0: boolean, arg1: integer): string
public "getDisplayName"(arg0: boolean, arg1: integer, arg2: $Locale$Type): string
public "getDisplayName"(arg0: $Locale$Type): string
public static "getTimeZone"(arg0: $ZoneId$Type): $TimeZone
public static "getTimeZone"(arg0: string): $TimeZone
public "getID"(): string
public "getDSTSavings"(): integer
public "hasSameRules"(arg0: $TimeZone$Type): boolean
public "toZoneId"(): $ZoneId
public "getRawOffset"(): integer
public static "setDefault"(arg0: $TimeZone$Type): void
public "inDaylightTime"(arg0: $Date$Type): boolean
public "useDaylightTime"(): boolean
public static "getAvailableIDs"(): (string)[]
public static "getAvailableIDs"(arg0: integer): (string)[]
public "setID"(arg0: string): void
public "setRawOffset"(arg0: integer): void
public "observesDaylightTime"(): boolean
get "default"(): $TimeZone
get "displayName"(): string
get "iD"(): string
get "dSTSavings"(): integer
get "rawOffset"(): integer
set "default"(value: $TimeZone$Type)
get "availableIDs"(): (string)[]
set "iD"(value: string)
set "rawOffset"(value: integer)
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
declare module "packages/java/util/jar/$JarEntry" {
import {$Certificate, $Certificate$Type} from "packages/java/security/cert/$Certificate"
import {$CodeSigner, $CodeSigner$Type} from "packages/java/security/$CodeSigner"
import {$Attributes, $Attributes$Type} from "packages/java/util/jar/$Attributes"
import {$ZipEntry, $ZipEntry$Type} from "packages/java/util/zip/$ZipEntry"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $JarEntry extends $ZipEntry {
static readonly "STORED": integer
static readonly "DEFLATED": integer

constructor(arg0: $JarEntry$Type)
constructor(arg0: $ZipEntry$Type)
constructor(arg0: string)

public "getCertificates"(): ($Certificate)[]
public "getRealName"(): string
public "getAttributes"(): $Attributes
public "getCodeSigners"(): ($CodeSigner)[]
get "certificates"(): ($Certificate)[]
get "realName"(): string
get "attributes"(): $Attributes
get "codeSigners"(): ($CodeSigner)[]
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JarEntry$Type = ($JarEntry);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JarEntry_ = $JarEntry$Type;
}}
declare module "packages/java/util/function/$DoubleSupplier" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $DoubleSupplier {

 "getAsDouble"(): double

(): double
}

export namespace $DoubleSupplier {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DoubleSupplier$Type = ($DoubleSupplier);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DoubleSupplier_ = $DoubleSupplier$Type;
}}
declare module "packages/java/util/$Queue" {
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$IntFunction, $IntFunction$Type} from "packages/java/util/function/$IntFunction"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$Iterator, $Iterator$Type} from "packages/java/util/$Iterator"
import {$Spliterator, $Spliterator$Type} from "packages/java/util/$Spliterator"

export interface $Queue<E> extends $Collection<(E)> {

 "add"(arg0: E): boolean
 "remove"(): E
 "poll"(): E
 "peek"(): E
 "element"(): E
 "offer"(arg0: E): boolean
 "remove"(arg0: any): boolean
 "equals"(arg0: any): boolean
 "hashCode"(): integer
 "clear"(): void
 "isEmpty"(): boolean
 "size"(): integer
 "toArray"<T>(arg0: (T)[]): (T)[]
 "toArray"<T>(arg0: $IntFunction$Type<((T)[])>): (T)[]
 "toArray"(): (any)[]
 "iterator"(): $Iterator<(E)>
 "stream"(): $Stream<(E)>
 "contains"(arg0: any): boolean
 "spliterator"(): $Spliterator<(E)>
 "addAll"(arg0: $Collection$Type<(any)>): boolean
 "removeIf"(arg0: $Predicate$Type<(any)>): boolean
 "removeAll"(arg0: $Collection$Type<(any)>): boolean
 "retainAll"(arg0: $Collection$Type<(any)>): boolean
 "containsAll"(arg0: $Collection$Type<(any)>): boolean
 "parallelStream"(): $Stream<(E)>
 "forEach"(arg0: $Consumer$Type<(any)>): void
}

export namespace $Queue {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Queue$Type<E> = ($Queue<(E)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Queue_<E> = $Queue$Type<(E)>;
}}
declare module "packages/java/util/$AbstractMap$SimpleImmutableEntry" {
import {$Comparator, $Comparator$Type} from "packages/java/util/$Comparator"
import {$Comparable, $Comparable$Type} from "packages/java/lang/$Comparable"
import {$Serializable, $Serializable$Type} from "packages/java/io/$Serializable"
import {$Map$Entry, $Map$Entry$Type} from "packages/java/util/$Map$Entry"

export class $AbstractMap$SimpleImmutableEntry<K, V> implements $Map$Entry<(K), (V)>, $Serializable {

constructor(arg0: K, arg1: V)
constructor(arg0: $Map$Entry$Type<(any), (any)>)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "getValue"(): V
public "getKey"(): K
public "setValue"(arg0: V): V
public static "copyOf"<K, V>(arg0: $Map$Entry$Type<(any), (any)>): $Map$Entry<(K), (V)>
public static "comparingByKey"<K, V>(arg0: $Comparator$Type<(any)>): $Comparator<($Map$Entry<(K), (V)>)>
public static "comparingByKey"<K extends $Comparable<(any)>, V>(): $Comparator<($Map$Entry<(K), (V)>)>
public static "comparingByValue"<K, V extends $Comparable<(any)>>(): $Comparator<($Map$Entry<(K), (V)>)>
public static "comparingByValue"<K, V>(arg0: $Comparator$Type<(any)>): $Comparator<($Map$Entry<(K), (V)>)>
get "value"(): V
get "key"(): K
set "value"(value: V)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AbstractMap$SimpleImmutableEntry$Type<K, V> = ($AbstractMap$SimpleImmutableEntry<(K), (V)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AbstractMap$SimpleImmutableEntry_<K, V> = $AbstractMap$SimpleImmutableEntry$Type<(K), (V)>;
}}
declare module "packages/java/util/$Iterator" {
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"

export interface $Iterator<E> {

 "remove"(): void
 "forEachRemaining"(arg0: $Consumer$Type<(any)>): void
 "hasNext"(): boolean
 "next"(): E
}

export namespace $Iterator {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Iterator$Type<E> = ($Iterator<(E)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Iterator_<E> = $Iterator$Type<(E)>;
}}
declare module "packages/java/util/concurrent/$Delayed" {
import {$Comparable, $Comparable$Type} from "packages/java/lang/$Comparable"
import {$TimeUnit, $TimeUnit$Type} from "packages/java/util/concurrent/$TimeUnit"

export interface $Delayed extends $Comparable<($Delayed)> {

 "getDelay"(arg0: $TimeUnit$Type): long
 "compareTo"(arg0: $Delayed$Type): integer
}

export namespace $Delayed {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Delayed$Type = ($Delayed);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Delayed_ = $Delayed$Type;
}}
declare module "packages/java/util/zip/$ZipInputStream" {
import {$InputStream, $InputStream$Type} from "packages/java/io/$InputStream"
import {$InflaterInputStream, $InflaterInputStream$Type} from "packages/java/util/zip/$InflaterInputStream"
import {$ZipEntry, $ZipEntry$Type} from "packages/java/util/zip/$ZipEntry"
import {$ZipConstants, $ZipConstants$Type} from "packages/java/util/zip/$ZipConstants"
import {$Charset, $Charset$Type} from "packages/java/nio/charset/$Charset"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $ZipInputStream extends $InflaterInputStream implements $ZipConstants {

constructor(arg0: $InputStream$Type, arg1: $Charset$Type)
constructor(arg0: $InputStream$Type)

public "read"(arg0: (byte)[], arg1: integer, arg2: integer): integer
public "close"(): void
public "skip"(arg0: long): long
public "available"(): integer
public "getNextEntry"(): $ZipEntry
public "closeEntry"(): void
get "nextEntry"(): $ZipEntry
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ZipInputStream$Type = ($ZipInputStream);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ZipInputStream_ = $ZipInputStream$Type;
}}
declare module "packages/java/util/$HashMap" {
import {$BiConsumer, $BiConsumer$Type} from "packages/java/util/function/$BiConsumer"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Serializable, $Serializable$Type} from "packages/java/io/$Serializable"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Cloneable, $Cloneable$Type} from "packages/java/lang/$Cloneable"
import {$AbstractMap, $AbstractMap$Type} from "packages/java/util/$AbstractMap"
import {$BiFunction, $BiFunction$Type} from "packages/java/util/function/$BiFunction"
import {$Map, $Map$Type} from "packages/java/util/$Map"
import {$Map$Entry, $Map$Entry$Type} from "packages/java/util/$Map$Entry"

export class $HashMap<K, V> extends $AbstractMap<(K), (V)> implements $Map<(K), (V)>, $Cloneable, $Serializable {

constructor(arg0: integer)
constructor(arg0: integer, arg1: float)
constructor(arg0: $Map$Type<(any), (any)>)
constructor()

public "remove"(arg0: any, arg1: any): boolean
public "remove"(arg0: any): V
public "get"(arg0: any): V
public "put"(arg0: K, arg1: V): V
public "values"(): $Collection<(V)>
public "clone"(): any
public "clear"(): void
public "isEmpty"(): boolean
public "replace"(arg0: K, arg1: V): V
public "replace"(arg0: K, arg1: V, arg2: V): boolean
public "replaceAll"(arg0: $BiFunction$Type<(any), (any), (any)>): void
public "size"(): integer
public "merge"(arg0: K, arg1: V, arg2: $BiFunction$Type<(any), (any), (any)>): V
public "entrySet"(): $Set<($Map$Entry<(K), (V)>)>
public "putAll"(arg0: $Map$Type<(any), (any)>): void
public "putIfAbsent"(arg0: K, arg1: V): V
public "compute"(arg0: K, arg1: $BiFunction$Type<(any), (any), (any)>): V
public "forEach"(arg0: $BiConsumer$Type<(any), (any)>): void
public "containsKey"(arg0: any): boolean
public "computeIfAbsent"(arg0: K, arg1: $Function$Type<(any), (any)>): V
public "keySet"(): $Set<(K)>
public "containsValue"(arg0: any): boolean
public "getOrDefault"(arg0: any, arg1: V): V
public "computeIfPresent"(arg0: K, arg1: $BiFunction$Type<(any), (any), (any)>): V
public "equals"(arg0: any): boolean
public "hashCode"(): integer
public static "copyOf"<K, V>(arg0: $Map$Type<(any), (any)>): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V, arg8: K, arg9: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V): $Map<(K), (V)>
public static "of"<K, V>(): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V, arg8: K, arg9: V, arg10: K, arg11: V, arg12: K, arg13: V, arg14: K, arg15: V, arg16: K, arg17: V, arg18: K, arg19: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V, arg8: K, arg9: V, arg10: K, arg11: V, arg12: K, arg13: V, arg14: K, arg15: V, arg16: K, arg17: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V, arg8: K, arg9: V, arg10: K, arg11: V, arg12: K, arg13: V, arg14: K, arg15: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V, arg8: K, arg9: V, arg10: K, arg11: V, arg12: K, arg13: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V, arg8: K, arg9: V, arg10: K, arg11: V): $Map<(K), (V)>
public static "entry"<K, V>(arg0: K, arg1: V): $Map$Entry<(K), (V)>
public static "ofEntries"<K, V>(...arg0: ($Map$Entry$Type<(any), (any)>)[]): $Map<(K), (V)>
get "empty"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $HashMap$Type<K, V> = ($HashMap<(K), (V)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $HashMap_<K, V> = $HashMap$Type<(K), (V)>;
}}
declare module "packages/java/util/function/$BooleanSupplier" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $BooleanSupplier {

 "getAsBoolean"(): boolean

(): boolean
}

export namespace $BooleanSupplier {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BooleanSupplier$Type = ($BooleanSupplier);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BooleanSupplier_ = $BooleanSupplier$Type;
}}
declare module "packages/java/util/$ListIterator" {
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$Iterator, $Iterator$Type} from "packages/java/util/$Iterator"

export interface $ListIterator<E> extends $Iterator<(E)> {

 "add"(arg0: E): void
 "remove"(): void
 "hasNext"(): boolean
 "next"(): E
 "set"(arg0: E): void
 "nextIndex"(): integer
 "previousIndex"(): integer
 "hasPrevious"(): boolean
 "previous"(): E
 "forEachRemaining"(arg0: $Consumer$Type<(any)>): void
}

export namespace $ListIterator {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ListIterator$Type<E> = ($ListIterator<(E)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ListIterator_<E> = $ListIterator$Type<(E)>;
}}
declare module "packages/java/util/logging/$ErrorManager" {
import {$Exception, $Exception$Type} from "packages/java/lang/$Exception"

export class $ErrorManager {
static readonly "GENERIC_FAILURE": integer
static readonly "WRITE_FAILURE": integer
static readonly "FLUSH_FAILURE": integer
static readonly "CLOSE_FAILURE": integer
static readonly "OPEN_FAILURE": integer
static readonly "FORMAT_FAILURE": integer

constructor()

public "error"(arg0: string, arg1: $Exception$Type, arg2: integer): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ErrorManager$Type = ($ErrorManager);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ErrorManager_ = $ErrorManager$Type;
}}
declare module "packages/java/util/concurrent/$ThreadPoolExecutor" {
import {$RejectedExecutionHandler, $RejectedExecutionHandler$Type} from "packages/java/util/concurrent/$RejectedExecutionHandler"
import {$List, $List$Type} from "packages/java/util/$List"
import {$AbstractExecutorService, $AbstractExecutorService$Type} from "packages/java/util/concurrent/$AbstractExecutorService"
import {$BlockingQueue, $BlockingQueue$Type} from "packages/java/util/concurrent/$BlockingQueue"
import {$ThreadFactory, $ThreadFactory$Type} from "packages/java/util/concurrent/$ThreadFactory"
import {$TimeUnit, $TimeUnit$Type} from "packages/java/util/concurrent/$TimeUnit"
import {$Runnable, $Runnable$Type} from "packages/java/lang/$Runnable"

export class $ThreadPoolExecutor extends $AbstractExecutorService {

constructor(arg0: integer, arg1: integer, arg2: long, arg3: $TimeUnit$Type, arg4: $BlockingQueue$Type<($Runnable$Type)>, arg5: $RejectedExecutionHandler$Type)
constructor(arg0: integer, arg1: integer, arg2: long, arg3: $TimeUnit$Type, arg4: $BlockingQueue$Type<($Runnable$Type)>)
constructor(arg0: integer, arg1: integer, arg2: long, arg3: $TimeUnit$Type, arg4: $BlockingQueue$Type<($Runnable$Type)>, arg5: $ThreadFactory$Type, arg6: $RejectedExecutionHandler$Type)
constructor(arg0: integer, arg1: integer, arg2: long, arg3: $TimeUnit$Type, arg4: $BlockingQueue$Type<($Runnable$Type)>, arg5: $ThreadFactory$Type)

public "remove"(arg0: $Runnable$Type): boolean
public "shutdown"(): void
public "toString"(): string
public "execute"(arg0: $Runnable$Type): void
public "getQueue"(): $BlockingQueue<($Runnable)>
public "isShutdown"(): boolean
public "shutdownNow"(): $List<($Runnable)>
public "isTerminated"(): boolean
public "awaitTermination"(arg0: long, arg1: $TimeUnit$Type): boolean
public "getPoolSize"(): integer
public "isTerminating"(): boolean
public "purge"(): void
public "allowCoreThreadTimeOut"(arg0: boolean): void
public "allowsCoreThreadTimeOut"(): boolean
public "setThreadFactory"(arg0: $ThreadFactory$Type): void
public "getThreadFactory"(): $ThreadFactory
public "setRejectedExecutionHandler"(arg0: $RejectedExecutionHandler$Type): void
public "getRejectedExecutionHandler"(): $RejectedExecutionHandler
public "setCorePoolSize"(arg0: integer): void
public "getCorePoolSize"(): integer
public "prestartCoreThread"(): boolean
public "prestartAllCoreThreads"(): integer
public "setMaximumPoolSize"(arg0: integer): void
public "getMaximumPoolSize"(): integer
public "setKeepAliveTime"(arg0: long, arg1: $TimeUnit$Type): void
public "getKeepAliveTime"(arg0: $TimeUnit$Type): long
public "getActiveCount"(): integer
public "getLargestPoolSize"(): integer
public "getTaskCount"(): long
public "getCompletedTaskCount"(): long
get "queue"(): $BlockingQueue<($Runnable)>
get "terminated"(): boolean
get "poolSize"(): integer
get "terminating"(): boolean
set "threadFactory"(value: $ThreadFactory$Type)
get "threadFactory"(): $ThreadFactory
set "rejectedExecutionHandler"(value: $RejectedExecutionHandler$Type)
get "rejectedExecutionHandler"(): $RejectedExecutionHandler
set "corePoolSize"(value: integer)
get "corePoolSize"(): integer
set "maximumPoolSize"(value: integer)
get "maximumPoolSize"(): integer
get "activeCount"(): integer
get "largestPoolSize"(): integer
get "taskCount"(): long
get "completedTaskCount"(): long
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ThreadPoolExecutor$Type = ($ThreadPoolExecutor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ThreadPoolExecutor_ = $ThreadPoolExecutor$Type;
}}
declare module "packages/java/util/$Dictionary" {
import {$Enumeration, $Enumeration$Type} from "packages/java/util/$Enumeration"

export class $Dictionary<K, V> {

constructor()

public "remove"(arg0: any): V
public "get"(arg0: any): V
public "put"(arg0: K, arg1: V): V
public "isEmpty"(): boolean
public "size"(): integer
public "elements"(): $Enumeration<(V)>
public "keys"(): $Enumeration<(K)>
get "empty"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Dictionary$Type<K, V> = ($Dictionary<(K), (V)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Dictionary_<K, V> = $Dictionary$Type<(K), (V)>;
}}
declare module "packages/java/util/$Currency" {
import {$Serializable, $Serializable$Type} from "packages/java/io/$Serializable"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$Locale, $Locale$Type} from "packages/java/util/$Locale"

export class $Currency implements $Serializable {


public "toString"(): string
public static "getInstance"(arg0: string): $Currency
public static "getInstance"(arg0: $Locale$Type): $Currency
public "getDisplayName"(arg0: $Locale$Type): string
public "getDisplayName"(): string
public "getSymbol"(): string
public "getSymbol"(arg0: $Locale$Type): string
public "getCurrencyCode"(): string
public "getDefaultFractionDigits"(): integer
public static "getAvailableCurrencies"(): $Set<($Currency)>
public "getNumericCode"(): integer
public "getNumericCodeAsString"(): string
get "displayName"(): string
get "symbol"(): string
get "currencyCode"(): string
get "defaultFractionDigits"(): integer
get "availableCurrencies"(): $Set<($Currency)>
get "numericCode"(): integer
get "numericCodeAsString"(): string
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
declare module "packages/java/util/function/$IntUnaryOperator" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $IntUnaryOperator {

 "applyAsInt"(arg0: integer): integer
 "compose"(arg0: $IntUnaryOperator$Type): $IntUnaryOperator
 "andThen"(arg0: $IntUnaryOperator$Type): $IntUnaryOperator

(): $IntUnaryOperator
}

export namespace $IntUnaryOperator {
function identity(): $IntUnaryOperator
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IntUnaryOperator$Type = ($IntUnaryOperator);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IntUnaryOperator_ = $IntUnaryOperator$Type;
}}
declare module "packages/java/util/stream/$Collector$Characteristics" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $Collector$Characteristics extends $Enum<($Collector$Characteristics)> {
static readonly "CONCURRENT": $Collector$Characteristics
static readonly "UNORDERED": $Collector$Characteristics
static readonly "IDENTITY_FINISH": $Collector$Characteristics


public static "values"(): ($Collector$Characteristics)[]
public static "valueOf"(arg0: string): $Collector$Characteristics
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Collector$Characteristics$Type = (("unordered") | ("identity_finish") | ("concurrent")) | ($Collector$Characteristics);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Collector$Characteristics_ = $Collector$Characteristics$Type;
}}
declare module "packages/java/util/zip/$Inflater" {
import {$ByteBuffer, $ByteBuffer$Type} from "packages/java/nio/$ByteBuffer"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $Inflater {

constructor(arg0: boolean)
constructor()

public "inflate"(arg0: (byte)[]): integer
public "inflate"(arg0: $ByteBuffer$Type): integer
public "inflate"(arg0: (byte)[], arg1: integer, arg2: integer): integer
public "end"(): void
public "reset"(): void
public "setInput"(arg0: (byte)[]): void
public "setInput"(arg0: $ByteBuffer$Type): void
public "setInput"(arg0: (byte)[], arg1: integer, arg2: integer): void
public "setDictionary"(arg0: $ByteBuffer$Type): void
public "setDictionary"(arg0: (byte)[]): void
public "setDictionary"(arg0: (byte)[], arg1: integer, arg2: integer): void
public "finished"(): boolean
public "getAdler"(): integer
public "getBytesRead"(): long
public "getBytesWritten"(): long
public "getRemaining"(): integer
public "needsInput"(): boolean
public "needsDictionary"(): boolean
public "getTotalIn"(): integer
public "getTotalOut"(): integer
set "input"(value: (byte)[])
set "input"(value: $ByteBuffer$Type)
set "dictionary"(value: $ByteBuffer$Type)
set "dictionary"(value: (byte)[])
get "adler"(): integer
get "bytesRead"(): long
get "bytesWritten"(): long
get "remaining"(): integer
get "totalIn"(): integer
get "totalOut"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Inflater$Type = ($Inflater);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Inflater_ = $Inflater$Type;
}}
declare module "packages/java/util/$Optional" {
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Throwable, $Throwable$Type} from "packages/java/lang/$Throwable"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$Runnable, $Runnable$Type} from "packages/java/lang/$Runnable"

export class $Optional<T> {


public "get"(): T
public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "isEmpty"(): boolean
public "map"<U>(arg0: $Function$Type<(any), (any)>): $Optional<(U)>
public "stream"(): $Stream<(T)>
public static "of"<T>(arg0: T): $Optional<(T)>
public "filter"(arg0: $Predicate$Type<(any)>): $Optional<(T)>
public static "empty"<T>(): $Optional<(T)>
public "flatMap"<U>(arg0: $Function$Type<(any), (any)>): $Optional<(U)>
public "isPresent"(): boolean
public "orElse"(arg0: T): T
public "orElseThrow"<X extends $Throwable>(arg0: $Supplier$Type<(any)>): T
public "orElseThrow"(): T
public static "ofNullable"<T>(arg0: T): $Optional<(T)>
public "ifPresent"(arg0: $Consumer$Type<(any)>): void
public "ifPresentOrElse"(arg0: $Consumer$Type<(any)>, arg1: $Runnable$Type): void
public "or"(arg0: $Supplier$Type<(any)>): $Optional<(T)>
public "orElseGet"(arg0: $Supplier$Type<(any)>): T
get "present"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Optional$Type<T> = ($Optional<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Optional_<T> = $Optional$Type<(T)>;
}}
declare module "packages/java/util/function/$BinaryOperator" {
import {$Comparator, $Comparator$Type} from "packages/java/util/$Comparator"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$BiFunction, $BiFunction$Type} from "packages/java/util/function/$BiFunction"

export interface $BinaryOperator<T> extends $BiFunction<(T), (T), (T)> {

 "apply"(arg0: T, arg1: T): T
 "andThen"<V>(arg0: $Function$Type<(any), (any)>): $BiFunction<(T), (T), (V)>

(arg0: $Comparator$Type<(any)>): $BinaryOperator<(T)>
}

export namespace $BinaryOperator {
function minBy<T>(arg0: $Comparator$Type<(any)>): $BinaryOperator<(T)>
function maxBy<T>(arg0: $Comparator$Type<(any)>): $BinaryOperator<(T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BinaryOperator$Type<T> = ($BinaryOperator<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BinaryOperator_<T> = $BinaryOperator$Type<(T)>;
}}
declare module "packages/java/util/stream/$IntStream" {
import {$ObjIntConsumer, $ObjIntConsumer$Type} from "packages/java/util/function/$ObjIntConsumer"
import {$LongStream, $LongStream$Type} from "packages/java/util/stream/$LongStream"
import {$OptionalDouble, $OptionalDouble$Type} from "packages/java/util/$OptionalDouble"
import {$Spliterator$OfInt, $Spliterator$OfInt$Type} from "packages/java/util/$Spliterator$OfInt"
import {$IntSupplier, $IntSupplier$Type} from "packages/java/util/function/$IntSupplier"
import {$IntBinaryOperator, $IntBinaryOperator$Type} from "packages/java/util/function/$IntBinaryOperator"
import {$DoubleStream, $DoubleStream$Type} from "packages/java/util/stream/$DoubleStream"
import {$IntSummaryStatistics, $IntSummaryStatistics$Type} from "packages/java/util/$IntSummaryStatistics"
import {$Runnable, $Runnable$Type} from "packages/java/lang/$Runnable"
import {$IntUnaryOperator, $IntUnaryOperator$Type} from "packages/java/util/function/$IntUnaryOperator"
import {$IntStream$IntMapMultiConsumer, $IntStream$IntMapMultiConsumer$Type} from "packages/java/util/stream/$IntStream$IntMapMultiConsumer"
import {$IntStream$Builder, $IntStream$Builder$Type} from "packages/java/util/stream/$IntStream$Builder"
import {$BiConsumer, $BiConsumer$Type} from "packages/java/util/function/$BiConsumer"
import {$IntToDoubleFunction, $IntToDoubleFunction$Type} from "packages/java/util/function/$IntToDoubleFunction"
import {$IntPredicate, $IntPredicate$Type} from "packages/java/util/function/$IntPredicate"
import {$IntFunction, $IntFunction$Type} from "packages/java/util/function/$IntFunction"
import {$BaseStream, $BaseStream$Type} from "packages/java/util/stream/$BaseStream"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$IntConsumer, $IntConsumer$Type} from "packages/java/util/function/$IntConsumer"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$IntToLongFunction, $IntToLongFunction$Type} from "packages/java/util/function/$IntToLongFunction"
import {$OptionalInt, $OptionalInt$Type} from "packages/java/util/$OptionalInt"

export interface $IntStream extends $BaseStream<(integer), ($IntStream)> {

 "min"(): $OptionalInt
 "max"(): $OptionalInt
 "toArray"(): (integer)[]
 "map"(arg0: $IntUnaryOperator$Type): $IntStream
 "collect"<R>(arg0: $Supplier$Type<(R)>, arg1: $ObjIntConsumer$Type<(R)>, arg2: $BiConsumer$Type<(R), (R)>): R
 "count"(): long
 "limit"(arg0: long): $IntStream
 "spliterator"(): $Spliterator$OfInt
 "filter"(arg0: $IntPredicate$Type): $IntStream
 "anyMatch"(arg0: $IntPredicate$Type): boolean
 "flatMap"(arg0: $IntFunction$Type<(any)>): $IntStream
 "forEach"(arg0: $IntConsumer$Type): void
 "findAny"(): $OptionalInt
 "skip"(arg0: long): $IntStream
 "peek"(arg0: $IntConsumer$Type): $IntStream
 "sum"(): integer
 "reduce"(arg0: integer, arg1: $IntBinaryOperator$Type): integer
 "reduce"(arg0: $IntBinaryOperator$Type): $OptionalInt
 "distinct"(): $IntStream
 "findFirst"(): $OptionalInt
 "allMatch"(arg0: $IntPredicate$Type): boolean
 "parallel"(): $IntStream
 "mapToObj"<U>(arg0: $IntFunction$Type<(any)>): $Stream<(U)>
 "sorted"(): $IntStream
 "forEachOrdered"(arg0: $IntConsumer$Type): void
 "mapToLong"(arg0: $IntToLongFunction$Type): $LongStream
 "mapToDouble"(arg0: $IntToDoubleFunction$Type): $DoubleStream
 "mapMulti"(arg0: $IntStream$IntMapMultiConsumer$Type): $IntStream
 "takeWhile"(arg0: $IntPredicate$Type): $IntStream
 "dropWhile"(arg0: $IntPredicate$Type): $IntStream
 "noneMatch"(arg0: $IntPredicate$Type): boolean
 "boxed"(): $Stream<(integer)>
 "asLongStream"(): $LongStream
 "asDoubleStream"(): $DoubleStream
 "average"(): $OptionalDouble
 "summaryStatistics"(): $IntSummaryStatistics
 "close"(): void
 "onClose"(arg0: $Runnable$Type): $IntStream
 "isParallel"(): boolean
 "unordered"(): $IntStream
}

export namespace $IntStream {
function of(arg0: integer): $IntStream
function of(...arg0: (integer)[]): $IntStream
function builder(): $IntStream$Builder
function concat(arg0: $IntStream$Type, arg1: $IntStream$Type): $IntStream
function empty(): $IntStream
function range(arg0: integer, arg1: integer): $IntStream
function iterate(arg0: integer, arg1: $IntPredicate$Type, arg2: $IntUnaryOperator$Type): $IntStream
function iterate(arg0: integer, arg1: $IntUnaryOperator$Type): $IntStream
function generate(arg0: $IntSupplier$Type): $IntStream
function rangeClosed(arg0: integer, arg1: integer): $IntStream
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IntStream$Type = ($IntStream);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IntStream_ = $IntStream$Type;
}}
declare module "packages/java/util/concurrent/$CompletableFuture" {
import {$BiConsumer, $BiConsumer$Type} from "packages/java/util/function/$BiConsumer"
import {$Throwable, $Throwable$Type} from "packages/java/lang/$Throwable"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$CompletionStage, $CompletionStage$Type} from "packages/java/util/concurrent/$CompletionStage"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$Future, $Future$Type} from "packages/java/util/concurrent/$Future"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$Executor, $Executor$Type} from "packages/java/util/concurrent/$Executor"
import {$TimeUnit, $TimeUnit$Type} from "packages/java/util/concurrent/$TimeUnit"
import {$Runnable, $Runnable$Type} from "packages/java/lang/$Runnable"
import {$BiFunction, $BiFunction$Type} from "packages/java/util/function/$BiFunction"

export class $CompletableFuture<T> implements $Future<(T)>, $CompletionStage<(T)> {

constructor()

public "get"(arg0: long, arg1: $TimeUnit$Type): T
public "get"(): T
public "toString"(): string
public "join"(): T
public "copy"(): $CompletableFuture<(T)>
public "complete"(arg0: T): boolean
public static "allOf"(...arg0: ($CompletableFuture$Type<(any)>)[]): $CompletableFuture<(void)>
public "isDone"(): boolean
public "cancel"(arg0: boolean): boolean
public "isCancelled"(): boolean
public "completeExceptionally"(arg0: $Throwable$Type): boolean
public "newIncompleteFuture"<U>(): $CompletableFuture<(U)>
public "toCompletableFuture"(): $CompletableFuture<(T)>
public "completeAsync"(arg0: $Supplier$Type<(any)>, arg1: $Executor$Type): $CompletableFuture<(T)>
public "completeAsync"(arg0: $Supplier$Type<(any)>): $CompletableFuture<(T)>
public "exceptionallyComposeAsync"(arg0: $Function$Type<($Throwable$Type), (any)>): $CompletableFuture<(T)>
public "exceptionallyComposeAsync"(arg0: $Function$Type<($Throwable$Type), (any)>, arg1: $Executor$Type): $CompletableFuture<(T)>
public "exceptionallyCompose"(arg0: $Function$Type<($Throwable$Type), (any)>): $CompletableFuture<(T)>
public "exceptionallyAsync"(arg0: $Function$Type<($Throwable$Type), (any)>, arg1: $Executor$Type): $CompletableFuture<(T)>
public "exceptionallyAsync"(arg0: $Function$Type<($Throwable$Type), (any)>): $CompletableFuture<(T)>
public "exceptionally"(arg0: $Function$Type<($Throwable$Type), (any)>): $CompletableFuture<(T)>
public "whenCompleteAsync"(arg0: $BiConsumer$Type<(any), (any)>, arg1: $Executor$Type): $CompletableFuture<(T)>
public "whenCompleteAsync"(arg0: $BiConsumer$Type<(any), (any)>): $CompletableFuture<(T)>
public "thenComposeAsync"<U>(arg0: $Function$Type<(any), (any)>): $CompletableFuture<(U)>
public "thenCompose"<U>(arg0: $Function$Type<(any), (any)>): $CompletableFuture<(U)>
public "acceptEitherAsync"(arg0: $CompletionStage$Type<(any)>, arg1: $Consumer$Type<(any)>, arg2: $Executor$Type): $CompletableFuture<(void)>
public "acceptEitherAsync"(arg0: $CompletionStage$Type<(any)>, arg1: $Consumer$Type<(any)>): $CompletableFuture<(void)>
public "acceptEither"(arg0: $CompletionStage$Type<(any)>, arg1: $Consumer$Type<(any)>): $CompletableFuture<(void)>
public "applyToEitherAsync"<U>(arg0: $CompletionStage$Type<(any)>, arg1: $Function$Type<(any), (U)>, arg2: $Executor$Type): $CompletableFuture<(U)>
public "applyToEither"<U>(arg0: $CompletionStage$Type<(any)>, arg1: $Function$Type<(any), (U)>): $CompletableFuture<(U)>
public "runAfterBothAsync"(arg0: $CompletionStage$Type<(any)>, arg1: $Runnable$Type, arg2: $Executor$Type): $CompletableFuture<(void)>
public "thenAcceptBothAsync"<U>(arg0: $CompletionStage$Type<(any)>, arg1: $BiConsumer$Type<(any), (any)>, arg2: $Executor$Type): $CompletableFuture<(void)>
public "thenCombine"<U, V>(arg0: $CompletionStage$Type<(any)>, arg1: $BiFunction$Type<(any), (any), (any)>): $CompletableFuture<(V)>
public "thenRunAsync"(arg0: $Runnable$Type, arg1: $Executor$Type): $CompletableFuture<(void)>
public "thenRunAsync"(arg0: $Runnable$Type): $CompletableFuture<(void)>
public "thenAcceptAsync"(arg0: $Consumer$Type<(any)>): $CompletableFuture<(void)>
public "thenApply"<U>(arg0: $Function$Type<(any), (any)>): $CompletableFuture<(U)>
public static "supplyAsync"<U>(arg0: $Supplier$Type<(U)>): $CompletableFuture<(U)>
public static "supplyAsync"<U>(arg0: $Supplier$Type<(U)>, arg1: $Executor$Type): $CompletableFuture<(U)>
public "whenComplete"(arg0: $BiConsumer$Type<(any), (any)>): $CompletableFuture<(T)>
public static "runAsync"(arg0: $Runnable$Type, arg1: $Executor$Type): $CompletableFuture<(void)>
public static "runAsync"(arg0: $Runnable$Type): $CompletableFuture<(void)>
public static "completedFuture"<U>(arg0: U): $CompletableFuture<(U)>
public "getNow"(arg0: T): T
public static "anyOf"(...arg0: ($CompletableFuture$Type<(any)>)[]): $CompletableFuture<(any)>
public "isCompletedExceptionally"(): boolean
public "obtrudeValue"(arg0: T): void
public "obtrudeException"(arg0: $Throwable$Type): void
public "getNumberOfDependents"(): integer
public "minimalCompletionStage"(): $CompletionStage<(T)>
public "orTimeout"(arg0: long, arg1: $TimeUnit$Type): $CompletableFuture<(T)>
public "completeOnTimeout"(arg0: T, arg1: long, arg2: $TimeUnit$Type): $CompletableFuture<(T)>
public static "delayedExecutor"(arg0: long, arg1: $TimeUnit$Type): $Executor
public static "delayedExecutor"(arg0: long, arg1: $TimeUnit$Type, arg2: $Executor$Type): $Executor
public static "completedStage"<U>(arg0: U): $CompletionStage<(U)>
public static "failedFuture"<U>(arg0: $Throwable$Type): $CompletableFuture<(U)>
public static "failedStage"<U>(arg0: $Throwable$Type): $CompletionStage<(U)>
public "defaultExecutor"(): $Executor
get "done"(): boolean
get "cancelled"(): boolean
get "completedExceptionally"(): boolean
get "numberOfDependents"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CompletableFuture$Type<T> = ($CompletableFuture<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CompletableFuture_<T> = $CompletableFuture$Type<(T)>;
}}
declare module "packages/java/util/$PrimitiveIterator$OfInt" {
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$IntConsumer, $IntConsumer$Type} from "packages/java/util/function/$IntConsumer"
import {$PrimitiveIterator, $PrimitiveIterator$Type} from "packages/java/util/$PrimitiveIterator"

export interface $PrimitiveIterator$OfInt extends $PrimitiveIterator<(integer), ($IntConsumer)> {

 "forEachRemaining"(arg0: $IntConsumer$Type): void
 "forEachRemaining"(arg0: $Consumer$Type<(any)>): void
 "nextInt"(): integer
 "remove"(): void
 "hasNext"(): boolean
}

export namespace $PrimitiveIterator$OfInt {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PrimitiveIterator$OfInt$Type = ($PrimitiveIterator$OfInt);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PrimitiveIterator$OfInt_ = $PrimitiveIterator$OfInt$Type;
}}
declare module "packages/java/util/function/$ToIntBiFunction" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $ToIntBiFunction<T, U> {

 "applyAsInt"(arg0: T, arg1: U): integer

(arg0: T, arg1: U): integer
}

export namespace $ToIntBiFunction {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ToIntBiFunction$Type<T, U> = ($ToIntBiFunction<(T), (U)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ToIntBiFunction_<T, U> = $ToIntBiFunction$Type<(T), (U)>;
}}
declare module "packages/java/util/zip/$InflaterInputStream" {
import {$Inflater, $Inflater$Type} from "packages/java/util/zip/$Inflater"
import {$InputStream, $InputStream$Type} from "packages/java/io/$InputStream"
import {$FilterInputStream, $FilterInputStream$Type} from "packages/java/io/$FilterInputStream"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $InflaterInputStream extends $FilterInputStream {

constructor(arg0: $InputStream$Type, arg1: $Inflater$Type)
constructor(arg0: $InputStream$Type, arg1: $Inflater$Type, arg2: integer)
constructor(arg0: $InputStream$Type)

public "read"(): integer
public "read"(arg0: (byte)[], arg1: integer, arg2: integer): integer
public "close"(): void
public "mark"(arg0: integer): void
public "skip"(arg0: long): long
public "available"(): integer
public "markSupported"(): boolean
public "reset"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InflaterInputStream$Type = ($InflaterInputStream);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InflaterInputStream_ = $InflaterInputStream$Type;
}}
declare module "packages/java/util/concurrent/locks/$Lock" {
import {$Condition, $Condition$Type} from "packages/java/util/concurrent/locks/$Condition"
import {$TimeUnit, $TimeUnit$Type} from "packages/java/util/concurrent/$TimeUnit"

export interface $Lock {

 "lock"(): void
 "lockInterruptibly"(): void
 "tryLock"(arg0: long, arg1: $TimeUnit$Type): boolean
 "tryLock"(): boolean
 "newCondition"(): $Condition
 "unlock"(): void
}

export namespace $Lock {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Lock$Type = ($Lock);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Lock_ = $Lock$Type;
}}
declare module "packages/java/util/$Locale$LanguageRange" {
import {$List, $List$Type} from "packages/java/util/$List"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $Locale$LanguageRange {
static readonly "MAX_WEIGHT": double
static readonly "MIN_WEIGHT": double

constructor(arg0: string)
constructor(arg0: string, arg1: double)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public static "parse"(arg0: string): $List<($Locale$LanguageRange)>
public static "parse"(arg0: string, arg1: $Map$Type<(string), ($List$Type<(string)>)>): $List<($Locale$LanguageRange)>
public "getRange"(): string
public "getWeight"(): double
public static "mapEquivalents"(arg0: $List$Type<($Locale$LanguageRange$Type)>, arg1: $Map$Type<(string), ($List$Type<(string)>)>): $List<($Locale$LanguageRange)>
get "range"(): string
get "weight"(): double
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Locale$LanguageRange$Type = ($Locale$LanguageRange);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Locale$LanguageRange_ = $Locale$LanguageRange$Type;
}}
declare module "packages/java/util/logging/$Formatter" {
import {$LogRecord, $LogRecord$Type} from "packages/java/util/logging/$LogRecord"
import {$Handler, $Handler$Type} from "packages/java/util/logging/$Handler"

export class $Formatter {


public "format"(arg0: $LogRecord$Type): string
public "formatMessage"(arg0: $LogRecord$Type): string
public "getHead"(arg0: $Handler$Type): string
public "getTail"(arg0: $Handler$Type): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Formatter$Type = ($Formatter);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Formatter_ = $Formatter$Type;
}}
declare module "packages/java/util/concurrent/atomic/$AtomicReferenceArray" {
import {$BinaryOperator, $BinaryOperator$Type} from "packages/java/util/function/$BinaryOperator"
import {$Serializable, $Serializable$Type} from "packages/java/io/$Serializable"
import {$UnaryOperator, $UnaryOperator$Type} from "packages/java/util/function/$UnaryOperator"

export class $AtomicReferenceArray<E> implements $Serializable {

constructor(arg0: integer)
constructor(arg0: (E)[])

public "get"(arg0: integer): E
public "length"(): integer
public "toString"(): string
public "set"(arg0: integer, arg1: E): void
public "getOpaque"(arg0: integer): E
public "setOpaque"(arg0: integer, arg1: E): void
public "getAcquire"(arg0: integer): E
public "setRelease"(arg0: integer, arg1: E): void
public "compareAndSet"(arg0: integer, arg1: E, arg2: E): boolean
public "compareAndExchange"(arg0: integer, arg1: E, arg2: E): E
public "compareAndExchangeAcquire"(arg0: integer, arg1: E, arg2: E): E
public "compareAndExchangeRelease"(arg0: integer, arg1: E, arg2: E): E
public "weakCompareAndSetPlain"(arg0: integer, arg1: E, arg2: E): boolean
/**
 * 
 * @deprecated
 */
public "weakCompareAndSet"(arg0: integer, arg1: E, arg2: E): boolean
public "weakCompareAndSetAcquire"(arg0: integer, arg1: E, arg2: E): boolean
public "weakCompareAndSetRelease"(arg0: integer, arg1: E, arg2: E): boolean
public "getAndSet"(arg0: integer, arg1: E): E
public "lazySet"(arg0: integer, arg1: E): void
public "weakCompareAndSetVolatile"(arg0: integer, arg1: E, arg2: E): boolean
public "getAndUpdate"(arg0: integer, arg1: $UnaryOperator$Type<(E)>): E
public "updateAndGet"(arg0: integer, arg1: $UnaryOperator$Type<(E)>): E
public "getAndAccumulate"(arg0: integer, arg1: E, arg2: $BinaryOperator$Type<(E)>): E
public "accumulateAndGet"(arg0: integer, arg1: E, arg2: $BinaryOperator$Type<(E)>): E
public "getPlain"(arg0: integer): E
public "setPlain"(arg0: integer, arg1: E): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AtomicReferenceArray$Type<E> = ($AtomicReferenceArray<(E)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AtomicReferenceArray_<E> = $AtomicReferenceArray$Type<(E)>;
}}
declare module "packages/java/util/$AbstractSequentialList" {
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$List, $List$Type} from "packages/java/util/$List"
import {$AbstractList, $AbstractList$Type} from "packages/java/util/$AbstractList"
import {$ListIterator, $ListIterator$Type} from "packages/java/util/$ListIterator"
import {$Iterator, $Iterator$Type} from "packages/java/util/$Iterator"

export class $AbstractSequentialList<E> extends $AbstractList<(E)> {


public "add"(arg0: integer, arg1: E): void
public "remove"(arg0: integer): E
public "get"(arg0: integer): E
public "iterator"(): $Iterator<(E)>
public "addAll"(arg0: integer, arg1: $Collection$Type<(any)>): boolean
public "set"(arg0: integer, arg1: E): E
public "listIterator"(arg0: integer): $ListIterator<(E)>
public "remove"(arg0: any): boolean
public static "copyOf"<E>(arg0: $Collection$Type<(any)>): $List<(E)>
public "isEmpty"(): boolean
public "size"(): integer
public "toArray"(): (any)[]
public "toArray"<T>(arg0: (T)[]): (T)[]
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E): $List<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E): $List<(E)>
public static "of"<E>(arg0: E, arg1: E): $List<(E)>
public static "of"<E>(arg0: E): $List<(E)>
public static "of"<E>(): $List<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E, arg5: E, arg6: E, arg7: E): $List<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E, arg5: E, arg6: E): $List<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E, arg5: E): $List<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E): $List<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E, arg5: E, arg6: E, arg7: E, arg8: E, arg9: E): $List<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E, arg5: E, arg6: E, arg7: E, arg8: E): $List<(E)>
public static "of"<E>(...arg0: (E)[]): $List<(E)>
public "contains"(arg0: any): boolean
public "addAll"(arg0: $Collection$Type<(any)>): boolean
public "removeAll"(arg0: $Collection$Type<(any)>): boolean
public "retainAll"(arg0: $Collection$Type<(any)>): boolean
public "containsAll"(arg0: $Collection$Type<(any)>): boolean
get "empty"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AbstractSequentialList$Type<E> = ($AbstractSequentialList<(E)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AbstractSequentialList_<E> = $AbstractSequentialList$Type<(E)>;
}}
declare module "packages/java/util/$Spliterator$OfDouble" {
import {$Comparator, $Comparator$Type} from "packages/java/util/$Comparator"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$Spliterator$OfPrimitive, $Spliterator$OfPrimitive$Type} from "packages/java/util/$Spliterator$OfPrimitive"
import {$DoubleConsumer, $DoubleConsumer$Type} from "packages/java/util/function/$DoubleConsumer"

export interface $Spliterator$OfDouble extends $Spliterator$OfPrimitive<(double), ($DoubleConsumer), ($Spliterator$OfDouble)> {

 "forEachRemaining"(arg0: $DoubleConsumer$Type): void
 "forEachRemaining"(arg0: $Consumer$Type<(any)>): void
 "tryAdvance"(arg0: $Consumer$Type<(any)>): boolean
 "tryAdvance"(arg0: $DoubleConsumer$Type): boolean
 "characteristics"(): integer
 "estimateSize"(): long
 "getExactSizeIfKnown"(): long
 "hasCharacteristics"(arg0: integer): boolean
 "getComparator"(): $Comparator<(any)>
}

export namespace $Spliterator$OfDouble {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Spliterator$OfDouble$Type = ($Spliterator$OfDouble);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Spliterator$OfDouble_ = $Spliterator$OfDouble$Type;
}}
declare module "packages/java/util/stream/$IntStream$IntMapMultiConsumer" {
import {$IntConsumer, $IntConsumer$Type} from "packages/java/util/function/$IntConsumer"

export interface $IntStream$IntMapMultiConsumer {

 "accept"(arg0: integer, arg1: $IntConsumer$Type): void

(arg0: integer, arg1: $IntConsumer$Type): void
}

export namespace $IntStream$IntMapMultiConsumer {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IntStream$IntMapMultiConsumer$Type = ($IntStream$IntMapMultiConsumer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IntStream$IntMapMultiConsumer_ = $IntStream$IntMapMultiConsumer$Type;
}}
declare module "packages/java/util/$AbstractSet" {
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$AbstractCollection, $AbstractCollection$Type} from "packages/java/util/$AbstractCollection"
import {$Iterator, $Iterator$Type} from "packages/java/util/$Iterator"
import {$Spliterator, $Spliterator$Type} from "packages/java/util/$Spliterator"

export class $AbstractSet<E> extends $AbstractCollection<(E)> implements $Set<(E)> {


public "equals"(arg0: any): boolean
public "hashCode"(): integer
public "removeAll"(arg0: $Collection$Type<(any)>): boolean
public "add"(arg0: E): boolean
public "remove"(arg0: any): boolean
public static "copyOf"<E>(arg0: $Collection$Type<(any)>): $Set<(E)>
public "clear"(): void
public "isEmpty"(): boolean
public "size"(): integer
public "toArray"<T>(arg0: (T)[]): (T)[]
public "toArray"(): (any)[]
public "iterator"(): $Iterator<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E, arg5: E): $Set<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E): $Set<(E)>
public static "of"<E>(arg0: E): $Set<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E): $Set<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E, arg5: E, arg6: E, arg7: E, arg8: E, arg9: E): $Set<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E, arg5: E, arg6: E, arg7: E, arg8: E): $Set<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E, arg5: E, arg6: E, arg7: E): $Set<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E, arg5: E, arg6: E): $Set<(E)>
public static "of"<E>(...arg0: (E)[]): $Set<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E): $Set<(E)>
public static "of"<E>(): $Set<(E)>
public static "of"<E>(arg0: E, arg1: E): $Set<(E)>
public "contains"(arg0: any): boolean
public "spliterator"(): $Spliterator<(E)>
public "addAll"(arg0: $Collection$Type<(any)>): boolean
public "retainAll"(arg0: $Collection$Type<(any)>): boolean
public "containsAll"(arg0: $Collection$Type<(any)>): boolean
get "empty"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AbstractSet$Type<E> = ($AbstractSet<(E)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AbstractSet_<E> = $AbstractSet$Type<(E)>;
}}
declare module "packages/java/util/function/$LongFunction" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $LongFunction<R> {

 "apply"(arg0: long): R

(arg0: long): R
}

export namespace $LongFunction {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LongFunction$Type<R> = ($LongFunction<(R)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LongFunction_<R> = $LongFunction$Type<(R)>;
}}
declare module "packages/java/util/random/$RandomGenerator" {
import {$LongStream, $LongStream$Type} from "packages/java/util/stream/$LongStream"
import {$IntStream, $IntStream$Type} from "packages/java/util/stream/$IntStream"
import {$DoubleStream, $DoubleStream$Type} from "packages/java/util/stream/$DoubleStream"

export interface $RandomGenerator {

 "nextDouble"(arg0: double): double
 "nextDouble"(): double
 "nextDouble"(arg0: double, arg1: double): double
 "nextInt"(arg0: integer): integer
 "nextInt"(arg0: integer, arg1: integer): integer
 "nextInt"(): integer
 "nextBytes"(arg0: (byte)[]): void
 "ints"(arg0: integer, arg1: integer): $IntStream
 "ints"(): $IntStream
 "ints"(arg0: long): $IntStream
 "ints"(arg0: long, arg1: integer, arg2: integer): $IntStream
 "longs"(arg0: long, arg1: long, arg2: long): $LongStream
 "longs"(arg0: long, arg1: long): $LongStream
 "longs"(): $LongStream
 "longs"(arg0: long): $LongStream
 "doubles"(): $DoubleStream
 "doubles"(arg0: long, arg1: double, arg2: double): $DoubleStream
 "doubles"(arg0: double, arg1: double): $DoubleStream
 "doubles"(arg0: long): $DoubleStream
 "nextLong"(arg0: long, arg1: long): long
 "nextLong"(): long
 "nextLong"(arg0: long): long
 "nextBoolean"(): boolean
 "nextFloat"(): float
 "nextFloat"(arg0: float): float
 "nextFloat"(arg0: float, arg1: float): float
 "nextGaussian"(arg0: double, arg1: double): double
 "nextGaussian"(): double
 "isDeprecated"(): boolean
 "nextExponential"(): double

(): $RandomGenerator
}

export namespace $RandomGenerator {
function getDefault(): $RandomGenerator
function of(arg0: string): $RandomGenerator
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RandomGenerator$Type = ($RandomGenerator);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RandomGenerator_ = $RandomGenerator$Type;
}}
declare module "packages/java/util/$ArrayDeque" {
import {$Serializable, $Serializable$Type} from "packages/java/io/$Serializable"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$AbstractCollection, $AbstractCollection$Type} from "packages/java/util/$AbstractCollection"
import {$Cloneable, $Cloneable$Type} from "packages/java/lang/$Cloneable"
import {$Deque, $Deque$Type} from "packages/java/util/$Deque"
import {$Iterator, $Iterator$Type} from "packages/java/util/$Iterator"
import {$Spliterator, $Spliterator$Type} from "packages/java/util/$Spliterator"

export class $ArrayDeque<E> extends $AbstractCollection<(E)> implements $Deque<(E)>, $Cloneable, $Serializable {

constructor()
constructor(arg0: $Collection$Type<(any)>)
constructor(arg0: integer)

public "add"(arg0: E): boolean
public "remove"(): E
public "remove"(arg0: any): boolean
public "clone"(): $ArrayDeque<(E)>
public "clear"(): void
public "isEmpty"(): boolean
public "size"(): integer
public "toArray"<T>(arg0: (T)[]): (T)[]
public "toArray"(): (any)[]
public "iterator"(): $Iterator<(E)>
public "contains"(arg0: any): boolean
public "spliterator"(): $Spliterator<(E)>
public "addAll"(arg0: $Collection$Type<(any)>): boolean
public "forEach"(arg0: $Consumer$Type<(any)>): void
public "poll"(): E
public "peek"(): E
public "removeIf"(arg0: $Predicate$Type<(any)>): boolean
public "element"(): E
public "removeAll"(arg0: $Collection$Type<(any)>): boolean
public "retainAll"(arg0: $Collection$Type<(any)>): boolean
public "push"(arg0: E): void
public "pop"(): E
public "addFirst"(arg0: E): void
public "addLast"(arg0: E): void
public "pollFirst"(): E
public "pollLast"(): E
public "offerLast"(arg0: E): boolean
public "removeFirst"(): E
public "getFirst"(): E
public "peekFirst"(): E
public "removeFirstOccurrence"(arg0: any): boolean
public "offerFirst"(arg0: E): boolean
public "removeLast"(): E
public "getLast"(): E
public "peekLast"(): E
public "removeLastOccurrence"(arg0: any): boolean
public "offer"(arg0: E): boolean
public "descendingIterator"(): $Iterator<(E)>
public "equals"(arg0: any): boolean
public "hashCode"(): integer
get "empty"(): boolean
get "first"(): E
get "last"(): E
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ArrayDeque$Type<E> = ($ArrayDeque<(E)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ArrayDeque_<E> = $ArrayDeque$Type<(E)>;
}}
declare module "packages/java/util/concurrent/atomic/$AtomicInteger" {
import {$Serializable, $Serializable$Type} from "packages/java/io/$Serializable"
import {$IntBinaryOperator, $IntBinaryOperator$Type} from "packages/java/util/function/$IntBinaryOperator"
import {$IntUnaryOperator, $IntUnaryOperator$Type} from "packages/java/util/function/$IntUnaryOperator"

export class $AtomicInteger extends number implements $Serializable {

constructor(arg0: integer)
constructor()

public "get"(): integer
public "toString"(): string
public "intValue"(): integer
public "longValue"(): long
public "floatValue"(): float
public "doubleValue"(): double
public "set"(arg0: integer): void
public "getOpaque"(): integer
public "setOpaque"(arg0: integer): void
public "getAcquire"(): integer
public "setRelease"(arg0: integer): void
public "compareAndSet"(arg0: integer, arg1: integer): boolean
public "compareAndExchange"(arg0: integer, arg1: integer): integer
public "compareAndExchangeAcquire"(arg0: integer, arg1: integer): integer
public "compareAndExchangeRelease"(arg0: integer, arg1: integer): integer
public "weakCompareAndSetPlain"(arg0: integer, arg1: integer): boolean
/**
 * 
 * @deprecated
 */
public "weakCompareAndSet"(arg0: integer, arg1: integer): boolean
public "weakCompareAndSetAcquire"(arg0: integer, arg1: integer): boolean
public "weakCompareAndSetRelease"(arg0: integer, arg1: integer): boolean
public "getAndSet"(arg0: integer): integer
public "getAndAdd"(arg0: integer): integer
public "lazySet"(arg0: integer): void
public "weakCompareAndSetVolatile"(arg0: integer, arg1: integer): boolean
public "getAndIncrement"(): integer
public "getAndDecrement"(): integer
public "incrementAndGet"(): integer
public "decrementAndGet"(): integer
public "addAndGet"(arg0: integer): integer
public "getAndUpdate"(arg0: $IntUnaryOperator$Type): integer
public "updateAndGet"(arg0: $IntUnaryOperator$Type): integer
public "getAndAccumulate"(arg0: integer, arg1: $IntBinaryOperator$Type): integer
public "accumulateAndGet"(arg0: integer, arg1: $IntBinaryOperator$Type): integer
public "getPlain"(): integer
public "setPlain"(arg0: integer): void
get "opaque"(): integer
set "opaque"(value: integer)
get "acquire"(): integer
set "release"(value: integer)
get "andIncrement"(): integer
get "andDecrement"(): integer
get "plain"(): integer
set "plain"(value: integer)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AtomicInteger$Type = ($AtomicInteger);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AtomicInteger_ = $AtomicInteger$Type;
}}
declare module "packages/java/util/function/$IntFunction" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $IntFunction<R> {

 "apply"(arg0: integer): R

(arg0: integer): R
}

export namespace $IntFunction {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IntFunction$Type<R> = ($IntFunction<(R)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IntFunction_<R> = $IntFunction$Type<(R)>;
}}

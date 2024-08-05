declare module "packages/javax/script/$ScriptContext" {
import {$Bindings, $Bindings$Type} from "packages/javax/script/$Bindings"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Writer, $Writer$Type} from "packages/java/io/$Writer"
import {$Reader, $Reader$Type} from "packages/java/io/$Reader"

export interface $ScriptContext {

 "setAttribute"(arg0: string, arg1: any, arg2: integer): void
 "getAttribute"(arg0: string, arg1: integer): any
 "getAttribute"(arg0: string): any
 "getAttributesScope"(arg0: string): integer
 "removeAttribute"(arg0: string, arg1: integer): any
 "getWriter"(): $Writer
 "getErrorWriter"(): $Writer
 "getScopes"(): $List<(integer)>
 "setErrorWriter"(arg0: $Writer$Type): void
 "setWriter"(arg0: $Writer$Type): void
 "getReader"(): $Reader
 "setReader"(arg0: $Reader$Type): void
 "getBindings"(arg0: integer): $Bindings
 "setBindings"(arg0: $Bindings$Type, arg1: integer): void
}

export namespace $ScriptContext {
const ENGINE_SCOPE: integer
const GLOBAL_SCOPE: integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ScriptContext$Type = ($ScriptContext);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ScriptContext_ = $ScriptContext$Type;
}}
declare module "packages/javax/script/$Bindings" {
import {$BiConsumer, $BiConsumer$Type} from "packages/java/util/function/$BiConsumer"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$BiFunction, $BiFunction$Type} from "packages/java/util/function/$BiFunction"
import {$Map, $Map$Type} from "packages/java/util/$Map"
import {$Map$Entry, $Map$Entry$Type} from "packages/java/util/$Map$Entry"

export interface $Bindings extends $Map<(string), (any)> {

 "remove"(arg0: any): any
 "get"(arg0: any): any
 "put"(arg0: string, arg1: any): any
 "putAll"(arg0: $Map$Type<(any), (any)>): void
 "containsKey"(arg0: any): boolean
 "remove"(arg0: any, arg1: any): boolean
 "equals"(arg0: any): boolean
 "values"(): $Collection<(any)>
 "hashCode"(): integer
 "clear"(): void
 "isEmpty"(): boolean
 "replace"(arg0: string, arg1: any): any
 "replace"(arg0: string, arg1: any, arg2: any): boolean
 "replaceAll"(arg0: $BiFunction$Type<(any), (any), (any)>): void
 "size"(): integer
 "merge"(arg0: string, arg1: any, arg2: $BiFunction$Type<(any), (any), (any)>): any
 "entrySet"(): $Set<($Map$Entry<(string), (any)>)>
 "putIfAbsent"(arg0: string, arg1: any): any
 "compute"(arg0: string, arg1: $BiFunction$Type<(any), (any), (any)>): any
 "forEach"(arg0: $BiConsumer$Type<(any), (any)>): void
 "computeIfAbsent"(arg0: string, arg1: $Function$Type<(any), (any)>): any
 "keySet"(): $Set<(string)>
 "containsValue"(arg0: any): boolean
 "getOrDefault"(arg0: any, arg1: any): any
 "computeIfPresent"(arg0: string, arg1: $BiFunction$Type<(any), (any), (any)>): any
}

export namespace $Bindings {
function copyOf<K, V>(arg0: $Map$Type<(any), (any)>): $Map<(string), (any)>
function of<K, V>(arg0: string, arg1: any, arg2: string, arg3: any, arg4: string, arg5: any, arg6: string, arg7: any, arg8: string, arg9: any): $Map<(string), (any)>
function of<K, V>(arg0: string, arg1: any, arg2: string, arg3: any, arg4: string, arg5: any, arg6: string, arg7: any): $Map<(string), (any)>
function of<K, V>(arg0: string, arg1: any, arg2: string, arg3: any, arg4: string, arg5: any): $Map<(string), (any)>
function of<K, V>(): $Map<(string), (any)>
function of<K, V>(arg0: string, arg1: any, arg2: string, arg3: any): $Map<(string), (any)>
function of<K, V>(arg0: string, arg1: any): $Map<(string), (any)>
function of<K, V>(arg0: string, arg1: any, arg2: string, arg3: any, arg4: string, arg5: any, arg6: string, arg7: any, arg8: string, arg9: any, arg10: string, arg11: any, arg12: string, arg13: any, arg14: string, arg15: any, arg16: string, arg17: any, arg18: string, arg19: any): $Map<(string), (any)>
function of<K, V>(arg0: string, arg1: any, arg2: string, arg3: any, arg4: string, arg5: any, arg6: string, arg7: any, arg8: string, arg9: any, arg10: string, arg11: any, arg12: string, arg13: any, arg14: string, arg15: any, arg16: string, arg17: any): $Map<(string), (any)>
function of<K, V>(arg0: string, arg1: any, arg2: string, arg3: any, arg4: string, arg5: any, arg6: string, arg7: any, arg8: string, arg9: any, arg10: string, arg11: any, arg12: string, arg13: any, arg14: string, arg15: any): $Map<(string), (any)>
function of<K, V>(arg0: string, arg1: any, arg2: string, arg3: any, arg4: string, arg5: any, arg6: string, arg7: any, arg8: string, arg9: any, arg10: string, arg11: any, arg12: string, arg13: any): $Map<(string), (any)>
function of<K, V>(arg0: string, arg1: any, arg2: string, arg3: any, arg4: string, arg5: any, arg6: string, arg7: any, arg8: string, arg9: any, arg10: string, arg11: any): $Map<(string), (any)>
function entry<K, V>(arg0: string, arg1: any): $Map$Entry<(string), (any)>
function ofEntries<K, V>(...arg0: ($Map$Entry$Type<(any), (any)>)[]): $Map<(string), (any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Bindings$Type = ($Bindings);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Bindings_ = $Bindings$Type;
}}
declare module "packages/javax/script/$ScriptEngineFactory" {
import {$ScriptEngine, $ScriptEngine$Type} from "packages/javax/script/$ScriptEngine"
import {$List, $List$Type} from "packages/java/util/$List"

export interface $ScriptEngineFactory {

 "getExtensions"(): $List<(string)>
 "getEngineName"(): string
 "getParameter"(arg0: string): any
 "getMethodCallSyntax"(arg0: string, arg1: string, ...arg2: (string)[]): string
 "getScriptEngine"(): $ScriptEngine
 "getEngineVersion"(): string
 "getLanguageName"(): string
 "getNames"(): $List<(string)>
 "getProgram"(...arg0: (string)[]): string
 "getMimeTypes"(): $List<(string)>
 "getOutputStatement"(arg0: string): string
 "getLanguageVersion"(): string
}

export namespace $ScriptEngineFactory {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ScriptEngineFactory$Type = ($ScriptEngineFactory);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ScriptEngineFactory_ = $ScriptEngineFactory$Type;
}}
declare module "packages/javax/script/$ScriptEngine" {
import {$ScriptContext, $ScriptContext$Type} from "packages/javax/script/$ScriptContext"
import {$Bindings, $Bindings$Type} from "packages/javax/script/$Bindings"
import {$ScriptEngineFactory, $ScriptEngineFactory$Type} from "packages/javax/script/$ScriptEngineFactory"
import {$Reader, $Reader$Type} from "packages/java/io/$Reader"

export interface $ScriptEngine {

 "get"(arg0: string): any
 "put"(arg0: string, arg1: any): void
 "getFactory"(): $ScriptEngineFactory
 "getContext"(): $ScriptContext
 "eval"(arg0: string): any
 "eval"(arg0: $Reader$Type, arg1: $ScriptContext$Type): any
 "eval"(arg0: string, arg1: $ScriptContext$Type): any
 "eval"(arg0: $Reader$Type): any
 "eval"(arg0: string, arg1: $Bindings$Type): any
 "eval"(arg0: $Reader$Type, arg1: $Bindings$Type): any
 "createBindings"(): $Bindings
 "setContext"(arg0: $ScriptContext$Type): void
 "getBindings"(arg0: integer): $Bindings
 "setBindings"(arg0: $Bindings$Type, arg1: integer): void
}

export namespace $ScriptEngine {
const ARGV: string
const FILENAME: string
const ENGINE: string
const ENGINE_VERSION: string
const NAME: string
const LANGUAGE: string
const LANGUAGE_VERSION: string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ScriptEngine$Type = ($ScriptEngine);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ScriptEngine_ = $ScriptEngine$Type;
}}

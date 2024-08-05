declare module "packages/moe/wolfgirl/probejs/lang/java/base/$ClassPathProvider" {
import {$ClassPath, $ClassPath$Type} from "packages/moe/wolfgirl/probejs/lang/java/clazz/$ClassPath"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"

export interface $ClassPathProvider {

 "getClassPaths"(): $Collection<($ClassPath)>

(): $Collection<($ClassPath)>
}

export namespace $ClassPathProvider {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClassPathProvider$Type = ($ClassPathProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClassPathProvider_ = $ClassPathProvider$Type;
}}
declare module "packages/moe/wolfgirl/probejs/$ProbeJS" {
import {$Gson, $Gson$Type} from "packages/com/google/gson/$Gson"
import {$Logger, $Logger$Type} from "packages/org/apache/logging/log4j/$Logger"

export class $ProbeJS {
static readonly "MOD_ID": string
static readonly "LOGGER": $Logger
static readonly "GSON": $Gson
static readonly "GSON_WRITER": $Gson

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ProbeJS$Type = ($ProbeJS);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ProbeJS_ = $ProbeJS$Type;
}}
declare module "packages/moe/wolfgirl/probejs/lang/typescript/code/type/js/$JSTypeOfType" {
import {$ClassPath, $ClassPath$Type} from "packages/moe/wolfgirl/probejs/lang/java/clazz/$ClassPath"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Declaration, $Declaration$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/$Declaration"
import {$BaseType$FormatType, $BaseType$FormatType$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/code/type/$BaseType$FormatType"
import {$BaseType, $BaseType$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/code/type/$BaseType"

export class $JSTypeOfType extends $BaseType {
readonly "inner": $BaseType

constructor(inner: $BaseType$Type)

public "format"(declaration: $Declaration$Type, input: $BaseType$FormatType$Type): $List<(string)>
public "getUsedClassPaths"(): $Collection<($ClassPath)>
get "usedClassPaths"(): $Collection<($ClassPath)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JSTypeOfType$Type = ($JSTypeOfType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JSTypeOfType_ = $JSTypeOfType$Type;
}}
declare module "packages/moe/wolfgirl/probejs/lang/transpiler/members/$Constructor" {
import {$Converter, $Converter$Type} from "packages/moe/wolfgirl/probejs/lang/transpiler/members/$Converter"
import {$ConstructorDecl, $ConstructorDecl$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/code/member/$ConstructorDecl"
import {$TypeConverter, $TypeConverter$Type} from "packages/moe/wolfgirl/probejs/lang/transpiler/$TypeConverter"
import {$ConstructorInfo, $ConstructorInfo$Type} from "packages/moe/wolfgirl/probejs/lang/java/clazz/members/$ConstructorInfo"

export class $Constructor extends $Converter<($ConstructorInfo), ($ConstructorDecl)> {

constructor(converter: $TypeConverter$Type)

public "transpile"(input: $ConstructorInfo$Type): $ConstructorDecl
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Constructor$Type = ($Constructor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Constructor_ = $Constructor$Type;
}}
declare module "packages/moe/wolfgirl/probejs/lang/transpiler/$TypeConverter" {
import {$TypeDescriptor, $TypeDescriptor$Type} from "packages/moe/wolfgirl/probejs/lang/java/type/$TypeDescriptor"
import {$ClassPath, $ClassPath$Type} from "packages/moe/wolfgirl/probejs/lang/java/clazz/$ClassPath"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$TypeDescJS, $TypeDescJS$Type} from "packages/dev/latvian/mods/kubejs/typings/desc/$TypeDescJS"
import {$DescriptionContext, $DescriptionContext$Type} from "packages/dev/latvian/mods/kubejs/typings/desc/$DescriptionContext"
import {$ScriptManager, $ScriptManager$Type} from "packages/dev/latvian/mods/kubejs/script/$ScriptManager"
import {$BaseType, $BaseType$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/code/type/$BaseType"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $TypeConverter {
static readonly "PROBEJS_PREFIX": string
static readonly "PROBEJS": $DescriptionContext
readonly "predefinedTypes": $Map<($ClassPath), ($BaseType)>
readonly "scriptManager": $ScriptManager

constructor(manager: $ScriptManager$Type)

public "convertType"(typeDesc: $TypeDescJS$Type): $BaseType
public "convertType"(baseType: $BaseType$Type): $BaseType
public "convertType"(descriptor: $TypeDescriptor$Type): $BaseType
public "addType"(clazz: $Class$Type<(any)>, type: $BaseType$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TypeConverter$Type = ($TypeConverter);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TypeConverter_ = $TypeConverter$Type;
}}
declare module "packages/moe/wolfgirl/probejs/lang/java/clazz/$ClassPath" {
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Class, $Class$Type} from "packages/java/lang/$Class"

export class $ClassPath extends $Record {

constructor(className: string)
constructor(parts: $List$Type<(string)>)
constructor(clazz: $Class$Type<(any)>)

public "getName"(): string
public "equals"(o: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "getPackage"(): $List<(string)>
public "parts"(): $List<(string)>
public "getClassPath"(): string
public "getClassPathJava"(): string
public "getConcatenatedPackage"(sep: string): string
public "getTypeScriptPath"(): string
public "getGenerics"(): $List<(string)>
public "makePath"(base: $Path$Type): $Path
public "getDirPath"(base: $Path$Type): $Path
public "getConcatenated"(sep: string): string
get "name"(): string
get "package"(): $List<(string)>
get "classPath"(): string
get "classPathJava"(): string
get "typeScriptPath"(): string
get "generics"(): $List<(string)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClassPath$Type = ($ClassPath);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClassPath_ = $ClassPath$Type;
}}
declare module "packages/moe/wolfgirl/probejs/lang/snippet/$SnippetDump" {
import {$Snippet, $Snippet$Type} from "packages/moe/wolfgirl/probejs/lang/snippet/$Snippet"
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$List, $List$Type} from "packages/java/util/$List"

export class $SnippetDump {
 "snippets": $List<($Snippet)>

constructor()

public "writeTo"(path: $Path$Type): void
public "snippet"(name: string): $Snippet
public "fromDocs"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SnippetDump$Type = ($SnippetDump);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SnippetDump_ = $SnippetDump$Type;
}}
declare module "packages/moe/wolfgirl/probejs/lang/typescript/code/type/$Types" {
import {$ClassPath, $ClassPath$Type} from "packages/moe/wolfgirl/probejs/lang/java/clazz/$ClassPath"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$TSClassType, $TSClassType$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/code/type/$TSClassType"
import {$Declaration, $Declaration$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/$Declaration"
import {$BiFunction, $BiFunction$Type} from "packages/java/util/function/$BiFunction"
import {$BaseType$FormatType, $BaseType$FormatType$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/code/type/$BaseType$FormatType"
import {$JSObjectType$Builder, $JSObjectType$Builder$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/code/type/js/$JSObjectType$Builder"
import {$JSJoinedType$Intersection, $JSJoinedType$Intersection$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/code/type/js/$JSJoinedType$Intersection"
import {$JSArrayType, $JSArrayType$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/code/type/js/$JSArrayType"
import {$TSParamType, $TSParamType$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/code/type/$TSParamType"
import {$JSPrimitiveType, $JSPrimitiveType$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/code/type/js/$JSPrimitiveType"
import {$TSVariableType, $TSVariableType$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/code/type/$TSVariableType"
import {$JSTypeOfType, $JSTypeOfType$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/code/type/js/$JSTypeOfType"
import {$BaseType, $BaseType$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/code/type/$BaseType"
import {$JSLambdaType$Builder, $JSLambdaType$Builder$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/code/type/js/$JSLambdaType$Builder"

export interface $Types {

}

export namespace $Types {
const ANY: $JSPrimitiveType
const BOOLEAN: $JSPrimitiveType
const NUMBER: $JSPrimitiveType
const STRING: $JSPrimitiveType
const NEVER: $JSPrimitiveType
const UNKNOWN: $JSPrimitiveType
const VOID: $JSPrimitiveType
const THIS: $JSPrimitiveType
const OBJECT: $JSPrimitiveType
const NULL: $JSPrimitiveType
function type(classPath: $ClassPath$Type): $TSClassType
function type(clazz: $Class$Type<(any)>): $TSClassType
function generic(symbol: string, extendOn: $BaseType$Type): $TSVariableType
function generic(symbol: string): $TSVariableType
function lambda(): $JSLambdaType$Builder
function object(): $JSObjectType$Builder
function or(...types: ($BaseType$Type)[]): $BaseType
function literal(content: any): $JSPrimitiveType
function and(...types: ($BaseType$Type)[]): $JSJoinedType$Intersection
function primitive(type: string): $JSPrimitiveType
function custom(formatter: $BiFunction$Type<($Declaration$Type), ($BaseType$FormatType$Type), (string)>, ...imports: ($ClassPath$Type)[]): $BaseType
function arrayOf(...types: ($BaseType$Type)[]): $JSArrayType
function parameterized(base: $BaseType$Type, ...params: ($BaseType$Type)[]): $TSParamType
function typeOf(clazz: $Class$Type<(any)>): $JSTypeOfType
function typeOf(classPath: $ClassPath$Type): $JSTypeOfType
function typeOf(classType: $BaseType$Type): $JSTypeOfType
function ignoreContext(type: $BaseType$Type, formatType: $BaseType$FormatType$Type): $BaseType
function typeMaybeGeneric(clazz: $Class$Type<(any)>): $BaseType
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Types$Type = ($Types);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Types_ = $Types$Type;
}}
declare module "packages/moe/wolfgirl/probejs/lang/typescript/code/member/$ParamDecl" {
import {$List, $List$Type} from "packages/java/util/$List"
import {$Declaration, $Declaration$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/$Declaration"
import {$BaseType, $BaseType$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/code/type/$BaseType"

export class $ParamDecl {
 "name": string
 "type": $BaseType
 "varArg": boolean
 "optional": boolean

constructor(name: string, type: $BaseType$Type, varArg: boolean, optional: boolean)

public "equals"(obj: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "format"(index: integer, declaration: $Declaration$Type): string
public static "formatParams"(params: $List$Type<($ParamDecl$Type)>, declaration: $Declaration$Type): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ParamDecl$Type = ($ParamDecl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ParamDecl_ = $ParamDecl$Type;
}}
declare module "packages/moe/wolfgirl/probejs/plugin/$BuiltinProbeJSPlugin" {
import {$ProbeJSPlugin, $ProbeJSPlugin$Type} from "packages/moe/wolfgirl/probejs/plugin/$ProbeJSPlugin"
import {$ClassPath, $ClassPath$Type} from "packages/moe/wolfgirl/probejs/lang/java/clazz/$ClassPath"
import {$BindingsEvent, $BindingsEvent$Type} from "packages/dev/latvian/mods/kubejs/script/$BindingsEvent"
import {$SnippetDump, $SnippetDump$Type} from "packages/moe/wolfgirl/probejs/lang/snippet/$SnippetDump"
import {$TypeConverter, $TypeConverter$Type} from "packages/moe/wolfgirl/probejs/lang/transpiler/$TypeConverter"
import {$ClassFilter, $ClassFilter$Type} from "packages/dev/latvian/mods/kubejs/util/$ClassFilter"
import {$ScriptType, $ScriptType$Type} from "packages/dev/latvian/mods/kubejs/script/$ScriptType"
import {$ScriptDump, $ScriptDump$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/$ScriptDump"
import {$Map, $Map$Type} from "packages/java/util/$Map"
import {$TypeScriptFile, $TypeScriptFile$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/$TypeScriptFile"
import {$Transpiler, $Transpiler$Type} from "packages/moe/wolfgirl/probejs/lang/transpiler/$Transpiler"

export class $BuiltinProbeJSPlugin extends $ProbeJSPlugin {

constructor()

public "registerBindings"(event: $BindingsEvent$Type): void
public "registerEvents"(): void
public "registerClasses"(type: $ScriptType$Type, filter: $ClassFilter$Type): void
public "modifyClasses"(scriptDump: $ScriptDump$Type, globalClasses: $Map$Type<($ClassPath$Type), ($TypeScriptFile$Type)>): void
public "addGlobals"(scriptDump: $ScriptDump$Type): void
public "assignType"(scriptDump: $ScriptDump$Type): void
public "addVSCodeSnippets"(dump: $SnippetDump$Type): void
public "addPredefinedTypes"(converter: $TypeConverter$Type): void
public "denyTypes"(transpiler: $Transpiler$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BuiltinProbeJSPlugin$Type = ($BuiltinProbeJSPlugin);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BuiltinProbeJSPlugin_ = $BuiltinProbeJSPlugin$Type;
}}
declare module "packages/moe/wolfgirl/probejs/lang/typescript/code/ts/$Wrapped" {
import {$ClassPath, $ClassPath$Type} from "packages/moe/wolfgirl/probejs/lang/java/clazz/$ClassPath"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Code, $Code$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/code/$Code"
import {$Declaration, $Declaration$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/$Declaration"
import {$CommentableCode, $CommentableCode$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/code/member/$CommentableCode"

export class $Wrapped extends $CommentableCode {
readonly "codes": $List<($Code)>
readonly "comments": $List<(string)>

constructor()

public "formatRaw"(declaration: $Declaration$Type): $List<(string)>
public "isEmpty"(): boolean
public "merge"(other: $Wrapped$Type): void
public "addCode"(inner: $Code$Type): void
public "getUsedClassPaths"(): $Collection<($ClassPath)>
get "empty"(): boolean
get "usedClassPaths"(): $Collection<($ClassPath)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Wrapped$Type = ($Wrapped);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Wrapped_ = $Wrapped$Type;
}}
declare module "packages/moe/wolfgirl/probejs/docs/$Snippets" {
import {$ProbeJSPlugin, $ProbeJSPlugin$Type} from "packages/moe/wolfgirl/probejs/plugin/$ProbeJSPlugin"
import {$SnippetDump, $SnippetDump$Type} from "packages/moe/wolfgirl/probejs/lang/snippet/$SnippetDump"

export class $Snippets extends $ProbeJSPlugin {

constructor()

public "addVSCodeSnippets"(dump: $SnippetDump$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Snippets$Type = ($Snippets);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Snippets_ = $Snippets$Type;
}}
declare module "packages/moe/wolfgirl/probejs/lang/typescript/code/member/$CommentableCode" {
import {$List, $List$Type} from "packages/java/util/$List"
import {$Code, $Code$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/code/$Code"
import {$Declaration, $Declaration$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/$Declaration"

export class $CommentableCode extends $Code {
readonly "comments": $List<(string)>

constructor()

public "formatRaw"(declaration: $Declaration$Type): $List<(string)>
public "linebreak"(): void
public "addCommentAtStart"(...comments: (string)[]): void
public "formatComments"(): $List<(string)>
public "format"(declaration: $Declaration$Type): $List<(string)>
public "newline"(...comments: (string)[]): void
public "addComment"(...comments: (string)[]): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CommentableCode$Type = ($CommentableCode);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CommentableCode_ = $CommentableCode$Type;
}}
declare module "packages/moe/wolfgirl/probejs/lang/typescript/code/ts/$MethodBuilder" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $MethodBuilder {

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MethodBuilder$Type = ($MethodBuilder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MethodBuilder_ = $MethodBuilder$Type;
}}
declare module "packages/moe/wolfgirl/probejs/lang/java/type/$TypeDescriptor" {
import {$Annotation, $Annotation$Type} from "packages/java/lang/annotation/$Annotation"
import {$AnnotationHolder, $AnnotationHolder$Type} from "packages/moe/wolfgirl/probejs/lang/java/base/$AnnotationHolder"
import {$ClassPath, $ClassPath$Type} from "packages/moe/wolfgirl/probejs/lang/java/clazz/$ClassPath"
import {$ClassProvider, $ClassProvider$Type} from "packages/moe/wolfgirl/probejs/lang/java/base/$ClassProvider"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$ClassPathProvider, $ClassPathProvider$Type} from "packages/moe/wolfgirl/probejs/lang/java/base/$ClassPathProvider"

export class $TypeDescriptor extends $AnnotationHolder implements $ClassPathProvider, $ClassProvider {

constructor(annotations: ($Annotation$Type)[])

public "stream"(): $Stream<($TypeDescriptor)>
public "getClasses"(): $Collection<($Class<(any)>)>
public "getClassPaths"(): $Collection<($ClassPath)>
get "classes"(): $Collection<($Class<(any)>)>
get "classPaths"(): $Collection<($ClassPath)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TypeDescriptor$Type = ($TypeDescriptor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TypeDescriptor_ = $TypeDescriptor$Type;
}}
declare module "packages/moe/wolfgirl/probejs/lang/typescript/code/type/js/$JSPrimitiveType" {
import {$ClassPath, $ClassPath$Type} from "packages/moe/wolfgirl/probejs/lang/java/clazz/$ClassPath"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Declaration, $Declaration$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/$Declaration"
import {$BaseType$FormatType, $BaseType$FormatType$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/code/type/$BaseType$FormatType"
import {$BaseType, $BaseType$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/code/type/$BaseType"

export class $JSPrimitiveType extends $BaseType {
readonly "content": string

constructor(content: string)

public "equals"(o: any): boolean
public "hashCode"(): integer
public "format"(declaration: $Declaration$Type, input: $BaseType$FormatType$Type): $List<(string)>
public "getUsedClassPaths"(): $Collection<($ClassPath)>
get "usedClassPaths"(): $Collection<($ClassPath)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JSPrimitiveType$Type = ($JSPrimitiveType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JSPrimitiveType_ = $JSPrimitiveType$Type;
}}
declare module "packages/moe/wolfgirl/probejs/lang/java/base/$AnnotationHolder" {
import {$Annotation, $Annotation$Type} from "packages/java/lang/annotation/$Annotation"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$List, $List$Type} from "packages/java/util/$List"

export class $AnnotationHolder {

constructor(annotations: ($Annotation$Type)[])

public "getAnnotation"<T extends $Annotation>(type: $Class$Type<(T)>): T
public "getAnnotations"<T extends $Annotation>(type: $Class$Type<(T)>): $List<(T)>
public "getAnnotations"(): ($Annotation)[]
public "hasAnnotation"(annotation: $Class$Type<(any)>): boolean
get "annotations"(): ($Annotation)[]
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AnnotationHolder$Type = ($AnnotationHolder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AnnotationHolder_ = $AnnotationHolder$Type;
}}
declare module "packages/moe/wolfgirl/probejs/lang/linter/$Linter" {
import {$LintingWarning, $LintingWarning$Type} from "packages/moe/wolfgirl/probejs/lang/linter/$LintingWarning"
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$Linter$RuleFactory, $Linter$RuleFactory$Type} from "packages/moe/wolfgirl/probejs/lang/linter/$Linter$RuleFactory"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$Rule, $Rule$Type} from "packages/moe/wolfgirl/probejs/lang/linter/rules/$Rule"

export class $Linter {
static readonly "SERVER_SCRIPT": $Supplier<($Linter)>
static readonly "CLIENT_SCRIPT": $Supplier<($Linter)>
static readonly "STARTUP_SCRIPT": $Supplier<($Linter)>
static readonly "RULES": ($Linter$RuleFactory)[]
readonly "scriptPath": $Path
readonly "rules": $List<($Rule)>

constructor(scriptPath: $Path$Type)

public static "defaultLint"(report: $Consumer$Type<($Component$Type)>): void
public "lint"(): $List<($LintingWarning)>
public "exclude"(...rule: ($Class$Type<(any)>)[]): $Linter
public "defaultRules"(): $Linter
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Linter$Type = ($Linter);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Linter_ = $Linter$Type;
}}
declare module "packages/moe/wolfgirl/probejs/events/$SnippetGenerationEventJS" {
import {$Snippet, $Snippet$Type} from "packages/moe/wolfgirl/probejs/lang/snippet/$Snippet"
import {$EventJS, $EventJS$Type} from "packages/dev/latvian/mods/kubejs/event/$EventJS"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$SnippetDump, $SnippetDump$Type} from "packages/moe/wolfgirl/probejs/lang/snippet/$SnippetDump"

export class $SnippetGenerationEventJS extends $EventJS {

constructor(dump: $SnippetDump$Type)

public "create"(name: string, handler: $Consumer$Type<($Snippet$Type)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SnippetGenerationEventJS$Type = ($SnippetGenerationEventJS);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SnippetGenerationEventJS_ = $SnippetGenerationEventJS$Type;
}}
declare module "packages/moe/wolfgirl/probejs/lang/java/base/$TypeVariableHolder" {
import {$Annotation, $Annotation$Type} from "packages/java/lang/annotation/$Annotation"
import {$AnnotationHolder, $AnnotationHolder$Type} from "packages/moe/wolfgirl/probejs/lang/java/base/$AnnotationHolder"
import {$List, $List$Type} from "packages/java/util/$List"
import {$VariableType, $VariableType$Type} from "packages/moe/wolfgirl/probejs/lang/java/type/impl/$VariableType"
import {$TypeVariable, $TypeVariable$Type} from "packages/java/lang/reflect/$TypeVariable"

export class $TypeVariableHolder extends $AnnotationHolder {
readonly "variableTypes": $List<($VariableType)>

constructor(variables: ($TypeVariable$Type<(any)>)[], annotations: ($Annotation$Type)[])

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TypeVariableHolder$Type = ($TypeVariableHolder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TypeVariableHolder_ = $TypeVariableHolder$Type;
}}
declare module "packages/moe/wolfgirl/probejs/utils/$DocUtils" {
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$ParamDecl, $ParamDecl$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/code/member/$ParamDecl"
import {$BaseType, $BaseType$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/code/type/$BaseType"
import {$TypeScriptFile, $TypeScriptFile$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/$TypeScriptFile"
import {$MethodDecl, $MethodDecl$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/code/member/$MethodDecl"

export class $DocUtils {

constructor()

public static "replaceParamType"(file: $TypeScriptFile$Type, test: $Predicate$Type<($MethodDecl$Type)>, index: integer, toReplace: $BaseType$Type): void
public static "applyParam"(file: $TypeScriptFile$Type, test: $Predicate$Type<($MethodDecl$Type)>, index: integer, effect: $Consumer$Type<($ParamDecl$Type)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DocUtils$Type = ($DocUtils);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DocUtils_ = $DocUtils$Type;
}}
declare module "packages/moe/wolfgirl/probejs/docs/$Primitives" {
import {$ProbeJSPlugin, $ProbeJSPlugin$Type} from "packages/moe/wolfgirl/probejs/plugin/$ProbeJSPlugin"
import {$TypeConverter, $TypeConverter$Type} from "packages/moe/wolfgirl/probejs/lang/transpiler/$TypeConverter"
import {$JSPrimitiveType, $JSPrimitiveType$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/code/type/js/$JSPrimitiveType"
import {$ScriptDump, $ScriptDump$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/$ScriptDump"

export class $Primitives extends $ProbeJSPlugin {
static readonly "LONG": $JSPrimitiveType
static readonly "INTEGER": $JSPrimitiveType
static readonly "SHORT": $JSPrimitiveType
static readonly "BYTE": $JSPrimitiveType
static readonly "DOUBLE": $JSPrimitiveType
static readonly "FLOAT": $JSPrimitiveType
static readonly "CHARACTER": $JSPrimitiveType
static readonly "CHAR_SEQUENCE": $JSPrimitiveType

constructor()

public "addGlobals"(scriptDump: $ScriptDump$Type): void
public "addPredefinedTypes"(converter: $TypeConverter$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Primitives$Type = ($Primitives);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Primitives_ = $Primitives$Type;
}}
declare module "packages/moe/wolfgirl/probejs/lang/typescript/code/type/$CustomType" {
import {$ClassPath, $ClassPath$Type} from "packages/moe/wolfgirl/probejs/lang/java/clazz/$ClassPath"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Declaration, $Declaration$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/$Declaration"
import {$BiFunction, $BiFunction$Type} from "packages/java/util/function/$BiFunction"
import {$BaseType$FormatType, $BaseType$FormatType$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/code/type/$BaseType$FormatType"
import {$BaseType, $BaseType$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/code/type/$BaseType"

export class $CustomType extends $BaseType {

constructor(formatter: $BiFunction$Type<($Declaration$Type), ($BaseType$FormatType$Type), (string)>, imports: ($ClassPath$Type)[])

public "format"(declaration: $Declaration$Type, input: $BaseType$FormatType$Type): $List<(string)>
public "getUsedClassPaths"(): $Collection<($ClassPath)>
get "usedClassPaths"(): $Collection<($ClassPath)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CustomType$Type = ($CustomType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CustomType_ = $CustomType$Type;
}}
declare module "packages/moe/wolfgirl/probejs/lang/transformer/$KubeJSScript" {
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$List, $List$Type} from "packages/java/util/$List"

export class $KubeJSScript {
readonly "exportedSymbols": $Set<(string)>
readonly "lines": $List<(string)>

constructor(lines: $List$Type<(string)>)

public "transform"(): (string)[]
public "processRequire"(): void
public "processExport"(): void
public "wrapScope"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $KubeJSScript$Type = ($KubeJSScript);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $KubeJSScript_ = $KubeJSScript$Type;
}}
declare module "packages/moe/wolfgirl/probejs/lang/typescript/code/ts/$MethodDeclaration$Builder" {
import {$MethodDeclaration, $MethodDeclaration$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/code/ts/$MethodDeclaration"
import {$List, $List$Type} from "packages/java/util/$List"
import {$TSVariableType, $TSVariableType$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/code/type/$TSVariableType"
import {$ParamDecl, $ParamDecl$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/code/member/$ParamDecl"
import {$BaseType, $BaseType$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/code/type/$BaseType"

export class $MethodDeclaration$Builder {
readonly "name": string
readonly "variableTypes": $List<($TSVariableType)>
readonly "params": $List<($ParamDecl)>
 "returnType": $BaseType

constructor(name: string)

public "returnType"(type: $BaseType$Type): $MethodDeclaration$Builder
public "param"(symbol: string, type: $BaseType$Type): $MethodDeclaration$Builder
public "param"(symbol: string, type: $BaseType$Type, isOptional: boolean): $MethodDeclaration$Builder
public "param"(symbol: string, type: $BaseType$Type, isOptional: boolean, isVarArg: boolean): $MethodDeclaration$Builder
public "build"(): $MethodDeclaration
public "variable"(...symbols: (string)[]): $MethodDeclaration$Builder
public "variable"(...variableType: ($TSVariableType$Type)[]): $MethodDeclaration$Builder
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MethodDeclaration$Builder$Type = ($MethodDeclaration$Builder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MethodDeclaration$Builder_ = $MethodDeclaration$Builder$Type;
}}
declare module "packages/moe/wolfgirl/probejs/lang/snippet/$Snippet" {
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Registry, $Registry$Type} from "packages/net/minecraft/core/$Registry"
import {$ResourceKey, $ResourceKey$Type} from "packages/net/minecraft/resources/$ResourceKey"
import {$Variable, $Variable$Type} from "packages/moe/wolfgirl/probejs/lang/snippet/parts/$Variable"

export class $Snippet {
readonly "name": string

constructor(name: string)

public "tabStop"(enumeration: integer): $Snippet
public "tabStop"(): $Snippet
public "tabStop"(enumeration: integer, defaultValue: string): $Snippet
public "compile"(): $JsonObject
public "prefix"(prefix: string): $Snippet
public "literal"(content: string): $Snippet
public "description"(description: string): $Snippet
public "newline"(): $Snippet
public "getPrefixes"(): $List<(string)>
public "variable"(variable: $Variable$Type): $Snippet
public "registry"<T>(registry: $ResourceKey$Type<($Registry$Type<(T)>)>): $Snippet
public "choices"(enumeration: integer, choices: $Collection$Type<(string)>): $Snippet
public "choices"(choices: $Collection$Type<(string)>): $Snippet
get "prefixes"(): $List<(string)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Snippet$Type = ($Snippet);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Snippet_ = $Snippet$Type;
}}
declare module "packages/moe/wolfgirl/probejs/docs/assignments/$EnumTypes" {
import {$ProbeJSPlugin, $ProbeJSPlugin$Type} from "packages/moe/wolfgirl/probejs/plugin/$ProbeJSPlugin"
import {$ScriptDump, $ScriptDump$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/$ScriptDump"

export class $EnumTypes extends $ProbeJSPlugin {

constructor()

public "assignType"(scriptDump: $ScriptDump$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EnumTypes$Type = ($EnumTypes);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EnumTypes_ = $EnumTypes$Type;
}}
declare module "packages/moe/wolfgirl/probejs/docs/assignments/$WorldTypes" {
import {$ProbeJSPlugin, $ProbeJSPlugin$Type} from "packages/moe/wolfgirl/probejs/plugin/$ProbeJSPlugin"
import {$ScriptDump, $ScriptDump$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/$ScriptDump"

export class $WorldTypes extends $ProbeJSPlugin {

constructor()

public "assignType"(scriptDump: $ScriptDump$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WorldTypes$Type = ($WorldTypes);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WorldTypes_ = $WorldTypes$Type;
}}
declare module "packages/moe/wolfgirl/probejs/docs/events/$TagEvents" {
import {$ProbeJSPlugin, $ProbeJSPlugin$Type} from "packages/moe/wolfgirl/probejs/plugin/$ProbeJSPlugin"
import {$ClassPath, $ClassPath$Type} from "packages/moe/wolfgirl/probejs/lang/java/clazz/$ClassPath"
import {$ScriptDump, $ScriptDump$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/$ScriptDump"
import {$Map, $Map$Type} from "packages/java/util/$Map"
import {$TypeScriptFile, $TypeScriptFile$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/$TypeScriptFile"

export class $TagEvents extends $ProbeJSPlugin {
static readonly "TAG_EVENT": $ClassPath
static readonly "TAG_WRAPPER": $ClassPath

constructor()

public "modifyClasses"(scriptDump: $ScriptDump$Type, globalClasses: $Map$Type<($ClassPath$Type), ($TypeScriptFile$Type)>): void
public "addGlobals"(scriptDump: $ScriptDump$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TagEvents$Type = ($TagEvents);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TagEvents_ = $TagEvents$Type;
}}
declare module "packages/moe/wolfgirl/probejs/lang/transpiler/transformation/$InjectArray" {
import {$FieldDecl, $FieldDecl$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/code/member/$FieldDecl"
import {$ConstructorDecl, $ConstructorDecl$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/code/member/$ConstructorDecl"
import {$FieldInfo, $FieldInfo$Type} from "packages/moe/wolfgirl/probejs/lang/java/clazz/members/$FieldInfo"
import {$ConstructorInfo, $ConstructorInfo$Type} from "packages/moe/wolfgirl/probejs/lang/java/clazz/members/$ConstructorInfo"
import {$Clazz, $Clazz$Type} from "packages/moe/wolfgirl/probejs/lang/java/clazz/$Clazz"
import {$ClassDecl, $ClassDecl$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/code/member/$ClassDecl"
import {$MethodInfo, $MethodInfo$Type} from "packages/moe/wolfgirl/probejs/lang/java/clazz/members/$MethodInfo"
import {$ClassTransformer, $ClassTransformer$Type} from "packages/moe/wolfgirl/probejs/lang/transpiler/transformation/$ClassTransformer"
import {$MethodDecl, $MethodDecl$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/code/member/$MethodDecl"

export class $InjectArray implements $ClassTransformer {

constructor()

public "transform"(clazz: $Clazz$Type, classDecl: $ClassDecl$Type): void
public static "transformMethods"(methodInfo: $MethodInfo$Type, methodDecl: $MethodDecl$Type): void
public static "transformClass"(clazz: $Clazz$Type, classDecl: $ClassDecl$Type): void
public "transformField"(fieldInfo: $FieldInfo$Type, fieldDecl: $FieldDecl$Type): void
public static "transformFields"(fieldInfo: $FieldInfo$Type, fieldDecl: $FieldDecl$Type): void
public "transformMethod"(methodInfo: $MethodInfo$Type, methodDecl: $MethodDecl$Type): void
public "transformConstructor"(constructorInfo: $ConstructorInfo$Type, constructorDecl: $ConstructorDecl$Type): void
public static "transformConstructors"(constructorInfo: $ConstructorInfo$Type, constructorDecl: $ConstructorDecl$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InjectArray$Type = ($InjectArray);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InjectArray_ = $InjectArray$Type;
}}
declare module "packages/moe/wolfgirl/probejs/lang/typescript/code/type/$ContextShield" {
import {$ClassPath, $ClassPath$Type} from "packages/moe/wolfgirl/probejs/lang/java/clazz/$ClassPath"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Declaration, $Declaration$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/$Declaration"
import {$BaseType$FormatType, $BaseType$FormatType$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/code/type/$BaseType$FormatType"
import {$BaseType, $BaseType$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/code/type/$BaseType"

export class $ContextShield extends $BaseType {

constructor(inner: $BaseType$Type, formatType: $BaseType$FormatType$Type)

public "format"(declaration: $Declaration$Type, input: $BaseType$FormatType$Type): $List<(string)>
public "getUsedClassPaths"(): $Collection<($ClassPath)>
get "usedClassPaths"(): $Collection<($ClassPath)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ContextShield$Type = ($ContextShield);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ContextShield_ = $ContextShield$Type;
}}
declare module "packages/moe/wolfgirl/probejs/$ProbeConfig" {
import {$ProbeConfig$ConfigEntry, $ProbeConfig$ConfigEntry$Type} from "packages/moe/wolfgirl/probejs/$ProbeConfig$ConfigEntry"

export class $ProbeConfig {
static "INSTANCE": $ProbeConfig
 "enabled": $ProbeConfig$ConfigEntry<(boolean)>
 "enableDecompiler": $ProbeConfig$ConfigEntry<(boolean)>
 "aggressive": $ProbeConfig$ConfigEntry<(boolean)>
 "interactive": $ProbeConfig$ConfigEntry<(boolean)>
 "interactivePort": $ProbeConfig$ConfigEntry<(integer)>
 "modHash": $ProbeConfig$ConfigEntry<(long)>
 "registryHash": $ProbeConfig$ConfigEntry<(long)>
 "isolatedScopes": $ProbeConfig$ConfigEntry<(boolean)>

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ProbeConfig$Type = ($ProbeConfig);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ProbeConfig_ = $ProbeConfig$Type;
}}
declare module "packages/moe/wolfgirl/probejs/lang/typescript/$Declaration" {
import {$ClassPath, $ClassPath$Type} from "packages/moe/wolfgirl/probejs/lang/java/clazz/$ClassPath"
import {$Reference, $Reference$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/$Reference"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $Declaration {
static readonly "INPUT_TEMPLATE": string
readonly "references": $Map<($ClassPath), ($Reference)>

constructor()

public "addClass"(path: $ClassPath$Type): void
public "getSymbol"(path: $ClassPath$Type, input: boolean): string
public "getSymbol"(path: $ClassPath$Type): string
public "exclude"(name: string): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Declaration$Type = ($Declaration);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Declaration_ = $Declaration$Type;
}}
declare module "packages/moe/wolfgirl/probejs/lang/java/clazz/members/$ParamInfo" {
import {$TypeDescriptor, $TypeDescriptor$Type} from "packages/moe/wolfgirl/probejs/lang/java/type/$TypeDescriptor"
import {$AnnotationHolder, $AnnotationHolder$Type} from "packages/moe/wolfgirl/probejs/lang/java/base/$AnnotationHolder"
import {$ClassPath, $ClassPath$Type} from "packages/moe/wolfgirl/probejs/lang/java/clazz/$ClassPath"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Parameter, $Parameter$Type} from "packages/java/lang/reflect/$Parameter"
import {$ClassPathProvider, $ClassPathProvider$Type} from "packages/moe/wolfgirl/probejs/lang/java/base/$ClassPathProvider"

export class $ParamInfo extends $AnnotationHolder implements $ClassPathProvider {
 "name": string
 "type": $TypeDescriptor
readonly "varArgs": boolean

constructor(parameter: $Parameter$Type)

public "getClassPaths"(): $Collection<($ClassPath)>
get "classPaths"(): $Collection<($ClassPath)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ParamInfo$Type = ($ParamInfo);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ParamInfo_ = $ParamInfo$Type;
}}
declare module "packages/moe/wolfgirl/probejs/lang/transpiler/transformation/$InjectBeans" {
import {$FieldDecl, $FieldDecl$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/code/member/$FieldDecl"
import {$ConstructorDecl, $ConstructorDecl$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/code/member/$ConstructorDecl"
import {$FieldInfo, $FieldInfo$Type} from "packages/moe/wolfgirl/probejs/lang/java/clazz/members/$FieldInfo"
import {$ConstructorInfo, $ConstructorInfo$Type} from "packages/moe/wolfgirl/probejs/lang/java/clazz/members/$ConstructorInfo"
import {$Clazz, $Clazz$Type} from "packages/moe/wolfgirl/probejs/lang/java/clazz/$Clazz"
import {$ClassDecl, $ClassDecl$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/code/member/$ClassDecl"
import {$MethodInfo, $MethodInfo$Type} from "packages/moe/wolfgirl/probejs/lang/java/clazz/members/$MethodInfo"
import {$ClassTransformer, $ClassTransformer$Type} from "packages/moe/wolfgirl/probejs/lang/transpiler/transformation/$ClassTransformer"
import {$MethodDecl, $MethodDecl$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/code/member/$MethodDecl"

export class $InjectBeans implements $ClassTransformer {

constructor()

public "transform"(clazz: $Clazz$Type, classDecl: $ClassDecl$Type): void
public static "transformMethods"(methodInfo: $MethodInfo$Type, methodDecl: $MethodDecl$Type): void
public static "transformClass"(clazz: $Clazz$Type, classDecl: $ClassDecl$Type): void
public "transformField"(fieldInfo: $FieldInfo$Type, fieldDecl: $FieldDecl$Type): void
public static "transformFields"(fieldInfo: $FieldInfo$Type, fieldDecl: $FieldDecl$Type): void
public "transformMethod"(methodInfo: $MethodInfo$Type, methodDecl: $MethodDecl$Type): void
public "transformConstructor"(constructorInfo: $ConstructorInfo$Type, constructorDecl: $ConstructorDecl$Type): void
public static "transformConstructors"(constructorInfo: $ConstructorInfo$Type, constructorDecl: $ConstructorDecl$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InjectBeans$Type = ($InjectBeans);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InjectBeans_ = $InjectBeans$Type;
}}
declare module "packages/moe/wolfgirl/probejs/lang/typescript/$Reference" {
import {$ClassPath, $ClassPath$Type} from "packages/moe/wolfgirl/probejs/lang/java/clazz/$ClassPath"
import {$Record, $Record$Type} from "packages/java/lang/$Record"

export class $Reference extends $Record {

constructor(classPath: $ClassPath$Type, original: string, input: string)

public "equals"(o: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "original"(): string
public "classPath"(): $ClassPath
public "input"(): string
public "getImport"(): string
get "import"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Reference$Type = ($Reference);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Reference_ = $Reference$Type;
}}
declare module "packages/moe/wolfgirl/probejs/lang/typescript/code/type/$TSClassType" {
import {$ClassPath, $ClassPath$Type} from "packages/moe/wolfgirl/probejs/lang/java/clazz/$ClassPath"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Declaration, $Declaration$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/$Declaration"
import {$BaseType$FormatType, $BaseType$FormatType$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/code/type/$BaseType$FormatType"
import {$BaseType, $BaseType$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/code/type/$BaseType"

export class $TSClassType extends $BaseType {
 "classPath": $ClassPath

constructor(classPath: $ClassPath$Type)

public "format"(declaration: $Declaration$Type, input: $BaseType$FormatType$Type): $List<(string)>
public "getUsedClassPaths"(): $Collection<($ClassPath)>
get "usedClassPaths"(): $Collection<($ClassPath)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TSClassType$Type = ($TSClassType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TSClassType_ = $TSClassType$Type;
}}
declare module "packages/moe/wolfgirl/probejs/utils/$NameUtils" {
import {$Pattern, $Pattern$Type} from "packages/java/util/regex/$Pattern"
import {$Set, $Set$Type} from "packages/java/util/$Set"

export class $NameUtils {
static readonly "KEYWORDS": $Set<(string)>
static readonly "JS_IDENTIFIER_MATCH": $Pattern
static readonly "MATCH_IMPORT": $Pattern
static readonly "MATCH_CONST_REQUIRE": $Pattern
static readonly "MATCH_ANY_REQUIRE": $Pattern

constructor()

public static "finalComponentToTitle"(resourceLocation: string): string
public static "resourceLocationToPath"(resourceLocation: string): (string)[]
public static "asCamelCase"(words: (string)[]): string
public static "rlToTitle"(s: string): string
public static "getCapitalized"(s: string): string
public static "extractAlphabets"(input: string): (string)[]
public static "isNameSafe"(s: string): boolean
public static "firstLower"(word: string): string
public static "snakeToTitle"(s: string): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NameUtils$Type = ($NameUtils);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NameUtils_ = $NameUtils$Type;
}}
declare module "packages/moe/wolfgirl/probejs/lang/typescript/code/member/$InterfaceDecl" {
import {$FieldDecl, $FieldDecl$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/code/member/$FieldDecl"
import {$ConstructorDecl, $ConstructorDecl$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/code/member/$ConstructorDecl"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Code, $Code$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/code/$Code"
import {$TSVariableType, $TSVariableType$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/code/type/$TSVariableType"
import {$Declaration, $Declaration$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/$Declaration"
import {$ClassDecl, $ClassDecl$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/code/member/$ClassDecl"
import {$BaseType, $BaseType$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/code/type/$BaseType"
import {$MethodDecl, $MethodDecl$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/code/member/$MethodDecl"

export class $InterfaceDecl extends $ClassDecl {
readonly "name": string
 "superClass": $BaseType
readonly "interfaces": $List<($BaseType)>
readonly "variableTypes": $List<($TSVariableType)>
 "isAbstract": boolean
 "isNative": boolean
readonly "fields": $List<($FieldDecl)>
readonly "constructors": $List<($ConstructorDecl)>
readonly "methods": $List<($MethodDecl)>
readonly "bodyCode": $List<($Code)>
readonly "comments": $List<(string)>

constructor(name: string, superClass: $BaseType$Type, interfaces: $List$Type<($BaseType$Type)>, variableTypes: $List$Type<($TSVariableType$Type)>)

public "formatRaw"(declaration: $Declaration$Type): $List<(string)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InterfaceDecl$Type = ($InterfaceDecl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InterfaceDecl_ = $InterfaceDecl$Type;
}}
declare module "packages/moe/wolfgirl/probejs/lang/transpiler/members/$Field" {
import {$Converter, $Converter$Type} from "packages/moe/wolfgirl/probejs/lang/transpiler/members/$Converter"
import {$FieldDecl, $FieldDecl$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/code/member/$FieldDecl"
import {$TypeConverter, $TypeConverter$Type} from "packages/moe/wolfgirl/probejs/lang/transpiler/$TypeConverter"
import {$FieldInfo, $FieldInfo$Type} from "packages/moe/wolfgirl/probejs/lang/java/clazz/members/$FieldInfo"

export class $Field extends $Converter<($FieldInfo), ($FieldDecl)> {

constructor(converter: $TypeConverter$Type)

public "transpile"(input: $FieldInfo$Type): $FieldDecl
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Field$Type = ($Field);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Field_ = $Field$Type;
}}
declare module "packages/moe/wolfgirl/probejs/events/$ScriptEventJS" {
import {$EventJS, $EventJS$Type} from "packages/dev/latvian/mods/kubejs/event/$EventJS"
import {$TypeConverter, $TypeConverter$Type} from "packages/moe/wolfgirl/probejs/lang/transpiler/$TypeConverter"
import {$ScriptType, $ScriptType$Type} from "packages/dev/latvian/mods/kubejs/script/$ScriptType"
import {$ScriptDump, $ScriptDump$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/$ScriptDump"

export class $ScriptEventJS extends $EventJS {

constructor(dump: $ScriptDump$Type)

public "getScriptType"(): $ScriptType
public "getTypeConverter"(): $TypeConverter
get "scriptType"(): $ScriptType
get "typeConverter"(): $TypeConverter
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ScriptEventJS$Type = ($ScriptEventJS);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ScriptEventJS_ = $ScriptEventJS$Type;
}}
declare module "packages/moe/wolfgirl/probejs/lang/java/clazz/$Clazz$ClassType" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $Clazz$ClassType extends $Enum<($Clazz$ClassType)> {
static readonly "INTERFACE": $Clazz$ClassType
static readonly "ENUM": $Clazz$ClassType
static readonly "RECORD": $Clazz$ClassType
static readonly "CLASS": $Clazz$ClassType


public static "values"(): ($Clazz$ClassType)[]
public static "valueOf"(name: string): $Clazz$ClassType
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Clazz$ClassType$Type = (("record") | ("interface") | ("class") | ("enum")) | ($Clazz$ClassType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Clazz$ClassType_ = $Clazz$ClassType$Type;
}}
declare module "packages/moe/wolfgirl/probejs/lang/typescript/code/member/clazz/$MethodBuilder" {
import {$List, $List$Type} from "packages/java/util/$List"
import {$TSVariableType, $TSVariableType$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/code/type/$TSVariableType"
import {$ParamDecl, $ParamDecl$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/code/member/$ParamDecl"
import {$ConstructorBuilder, $ConstructorBuilder$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/code/member/clazz/$ConstructorBuilder"
import {$BaseType, $BaseType$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/code/type/$BaseType"
import {$MethodDecl, $MethodDecl$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/code/member/$MethodDecl"

export class $MethodBuilder extends $ConstructorBuilder {
readonly "name": string
 "returnType": $BaseType
 "isAbstract": boolean
 "isStatic": boolean
readonly "variableTypes": $List<($TSVariableType)>
readonly "params": $List<($ParamDecl)>

constructor(name: string)

public "abstractMethod"(): $MethodBuilder
public "staticMethod"(): $MethodBuilder
public "buildAsMethod"(): $MethodDecl
public "returnType"(type: $BaseType$Type): $MethodBuilder
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MethodBuilder$Type = ($MethodBuilder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MethodBuilder_ = $MethodBuilder$Type;
}}
declare module "packages/moe/wolfgirl/probejs/docs/assignments/$RecipeTypes" {
import {$ProbeJSPlugin, $ProbeJSPlugin$Type} from "packages/moe/wolfgirl/probejs/plugin/$ProbeJSPlugin"
import {$ScriptDump, $ScriptDump$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/$ScriptDump"

export class $RecipeTypes extends $ProbeJSPlugin {

constructor()

public "assignType"(scriptDump: $ScriptDump$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecipeTypes$Type = ($RecipeTypes);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecipeTypes_ = $RecipeTypes$Type;
}}
declare module "packages/moe/wolfgirl/probejs/docs/$RegistryTypes" {
import {$ProbeJSPlugin, $ProbeJSPlugin$Type} from "packages/moe/wolfgirl/probejs/plugin/$ProbeJSPlugin"
import {$ClassPath, $ClassPath$Type} from "packages/moe/wolfgirl/probejs/lang/java/clazz/$ClassPath"
import {$SnippetDump, $SnippetDump$Type} from "packages/moe/wolfgirl/probejs/lang/snippet/$SnippetDump"
import {$ScriptDump, $ScriptDump$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/$ScriptDump"
import {$Map, $Map$Type} from "packages/java/util/$Map"
import {$TypeScriptFile, $TypeScriptFile$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/$TypeScriptFile"

export class $RegistryTypes extends $ProbeJSPlugin {
static readonly "LITERAL_FIELD": string
static readonly "TAG_FIELD": string
static readonly "OF_TYPE_DECL": string

constructor()

public "modifyClasses"(scriptDump: $ScriptDump$Type, globalClasses: $Map$Type<($ClassPath$Type), ($TypeScriptFile$Type)>): void
public "addGlobals"(scriptDump: $ScriptDump$Type): void
public "assignType"(scriptDump: $ScriptDump$Type): void
public "addVSCodeSnippets"(dump: $SnippetDump$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RegistryTypes$Type = ($RegistryTypes);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RegistryTypes_ = $RegistryTypes$Type;
}}
declare module "packages/moe/wolfgirl/probejs/lang/typescript/code/type/$BaseType" {
import {$TSArrayType, $TSArrayType$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/code/type/$TSArrayType"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Code, $Code$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/code/$Code"
import {$Declaration, $Declaration$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/$Declaration"
import {$BaseType$FormatType, $BaseType$FormatType$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/code/type/$BaseType$FormatType"

export class $BaseType extends $Code {

constructor()

public "format"(declaration: $Declaration$Type, input: $BaseType$FormatType$Type): $List<(string)>
public "format"(declaration: $Declaration$Type): $List<(string)>
public "line"(declaration: $Declaration$Type, input: $BaseType$FormatType$Type): string
public "asArray"(): $TSArrayType
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BaseType$Type = ($BaseType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BaseType_ = $BaseType$Type;
}}
declare module "packages/moe/wolfgirl/probejs/lang/decompiler/remapper/$RemappedClass" {
import {$InputStream, $InputStream$Type} from "packages/java/io/$InputStream"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $RemappedClass {
readonly "realName": string
readonly "remappedName": string
readonly "remapped": boolean
 "fields": $Map<(string), (string)>
 "emptyMethods": $Map<(string), (string)>
 "methods": $Map<(string), (string)>


public "toString"(): string
public "descriptorString"(): string
public static "loadFrom"(stream: $InputStream$Type): $Map<(string), ($RemappedClass)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RemappedClass$Type = ($RemappedClass);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RemappedClass_ = $RemappedClass$Type;
}}
declare module "packages/moe/wolfgirl/probejs/$ProbePaths" {
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"

export class $ProbePaths {
static "PROBE": $Path
static "WORKSPACE_SETTINGS": $Path
static "SETTINGS_JSON": $Path
static "DECOMPILED": $Path

constructor()

public static "init"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ProbePaths$Type = ($ProbePaths);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ProbePaths_ = $ProbePaths$Type;
}}
declare module "packages/moe/wolfgirl/probejs/lang/snippet/parts/$Literal" {
import {$SnippetPart, $SnippetPart$Type} from "packages/moe/wolfgirl/probejs/lang/snippet/parts/$SnippetPart"

export class $Literal implements $SnippetPart {

constructor(content: string)

public "format"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Literal$Type = ($Literal);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Literal_ = $Literal$Type;
}}
declare module "packages/moe/wolfgirl/probejs/lang/transpiler/members/$Converter" {
import {$TypeConverter, $TypeConverter$Type} from "packages/moe/wolfgirl/probejs/lang/transpiler/$TypeConverter"

export class $Converter<T, C> {

constructor(converter: $TypeConverter$Type)

public "transpile"(input: T): C
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Converter$Type<T, C> = ($Converter<(T), (C)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Converter_<T, C> = $Converter$Type<(T), (C)>;
}}
declare module "packages/moe/wolfgirl/probejs/events/$TypeAssignmentEventJS" {
import {$EventJS, $EventJS$Type} from "packages/dev/latvian/mods/kubejs/event/$EventJS"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$ScriptDump, $ScriptDump$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/$ScriptDump"
import {$BaseType, $BaseType$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/code/type/$BaseType"

export class $TypeAssignmentEventJS extends $EventJS {

constructor(scriptDump: $ScriptDump$Type)

public "assignType"(clazz: $Class$Type<(any)>, baseType: $BaseType$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TypeAssignmentEventJS$Type = ($TypeAssignmentEventJS);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TypeAssignmentEventJS_ = $TypeAssignmentEventJS$Type;
}}
declare module "packages/moe/wolfgirl/probejs/plugin/$ProbeJSPlugin" {
import {$ClassPath, $ClassPath$Type} from "packages/moe/wolfgirl/probejs/lang/java/clazz/$ClassPath"
import {$KubeJSPlugin, $KubeJSPlugin$Type} from "packages/dev/latvian/mods/kubejs/$KubeJSPlugin"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$SnippetDump, $SnippetDump$Type} from "packages/moe/wolfgirl/probejs/lang/snippet/$SnippetDump"
import {$TypeConverter, $TypeConverter$Type} from "packages/moe/wolfgirl/probejs/lang/transpiler/$TypeConverter"
import {$ScriptDump, $ScriptDump$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/$ScriptDump"
import {$Pair, $Pair$Type} from "packages/com/mojang/datafixers/util/$Pair"
import {$Map, $Map$Type} from "packages/java/util/$Map"
import {$TypeScriptFile, $TypeScriptFile$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/$TypeScriptFile"
import {$Transpiler, $Transpiler$Type} from "packages/moe/wolfgirl/probejs/lang/transpiler/$Transpiler"

export class $ProbeJSPlugin extends $KubeJSPlugin {

constructor()

public "modifyClasses"(scriptDump: $ScriptDump$Type, globalClasses: $Map$Type<($ClassPath$Type), ($TypeScriptFile$Type)>): void
public "addGlobals"(scriptDump: $ScriptDump$Type): void
public "disableEventDumps"(dump: $ScriptDump$Type): $Set<($Pair<(string), (string)>)>
public "assignType"(scriptDump: $ScriptDump$Type): void
public "addVSCodeSnippets"(dump: $SnippetDump$Type): void
public "addPredefinedTypes"(converter: $TypeConverter$Type): void
public "denyTypes"(transpiler: $Transpiler$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ProbeJSPlugin$Type = ($ProbeJSPlugin);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ProbeJSPlugin_ = $ProbeJSPlugin$Type;
}}
declare module "packages/moe/wolfgirl/probejs/lang/java/base/$ClassProvider" {
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Class, $Class$Type} from "packages/java/lang/$Class"

export interface $ClassProvider {

 "getClasses"(): $Collection<($Class<(any)>)>

(): $Collection<($Class<(any)>)>
}

export namespace $ClassProvider {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClassProvider$Type = ($ClassProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClassProvider_ = $ClassProvider$Type;
}}
declare module "packages/moe/wolfgirl/probejs/lang/snippet/parts/$Enumerable" {
import {$SnippetPart, $SnippetPart$Type} from "packages/moe/wolfgirl/probejs/lang/snippet/parts/$SnippetPart"

export class $Enumerable implements $SnippetPart {
 "enumeration": integer

constructor()

public "format"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Enumerable$Type = ($Enumerable);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Enumerable_ = $Enumerable$Type;
}}
declare module "packages/moe/wolfgirl/probejs/lang/java/clazz/members/$FieldInfo" {
import {$TypeDescriptor, $TypeDescriptor$Type} from "packages/moe/wolfgirl/probejs/lang/java/type/$TypeDescriptor"
import {$AnnotationHolder, $AnnotationHolder$Type} from "packages/moe/wolfgirl/probejs/lang/java/base/$AnnotationHolder"
import {$ClassPath, $ClassPath$Type} from "packages/moe/wolfgirl/probejs/lang/java/clazz/$ClassPath"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$FieldInfo$FieldAttributes, $FieldInfo$FieldAttributes$Type} from "packages/moe/wolfgirl/probejs/lang/java/clazz/members/$FieldInfo$FieldAttributes"
import {$JavaMembers$FieldInfo, $JavaMembers$FieldInfo$Type} from "packages/dev/latvian/mods/rhino/$JavaMembers$FieldInfo"
import {$ClassPathProvider, $ClassPathProvider$Type} from "packages/moe/wolfgirl/probejs/lang/java/base/$ClassPathProvider"

export class $FieldInfo extends $AnnotationHolder implements $ClassPathProvider {
readonly "name": string
readonly "type": $TypeDescriptor
readonly "attributes": $FieldInfo$FieldAttributes

constructor(field: $JavaMembers$FieldInfo$Type)

public "getClassPaths"(): $Collection<($ClassPath)>
get "classPaths"(): $Collection<($ClassPath)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FieldInfo$Type = ($FieldInfo);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FieldInfo_ = $FieldInfo$Type;
}}
declare module "packages/moe/wolfgirl/probejs/$GameEvents" {
import {$ClientPlayerNetworkEvent$LoggingIn, $ClientPlayerNetworkEvent$LoggingIn$Type} from "packages/net/minecraftforge/client/event/$ClientPlayerNetworkEvent$LoggingIn"
import {$RegisterClientCommandsEvent, $RegisterClientCommandsEvent$Type} from "packages/net/minecraftforge/client/event/$RegisterClientCommandsEvent"

export class $GameEvents {

constructor()

public static "playerJoined"(event: $ClientPlayerNetworkEvent$LoggingIn$Type): void
public static "registerCommand"(event: $RegisterClientCommandsEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GameEvents$Type = ($GameEvents);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GameEvents_ = $GameEvents$Type;
}}
declare module "packages/moe/wolfgirl/probejs/lang/decompiler/$ProbeDecompilerLogger" {
import {$Throwable, $Throwable$Type} from "packages/java/lang/$Throwable"
import {$IFernflowerLogger, $IFernflowerLogger$Type} from "packages/org/jetbrains/java/decompiler/main/extern/$IFernflowerLogger"
import {$IFernflowerLogger$Severity, $IFernflowerLogger$Severity$Type} from "packages/org/jetbrains/java/decompiler/main/extern/$IFernflowerLogger$Severity"

export class $ProbeDecompilerLogger extends $IFernflowerLogger {
static readonly "NO_OP": $IFernflowerLogger

constructor()

public "writeMessage"(message: string, severity: $IFernflowerLogger$Severity$Type, t: $Throwable$Type): void
public "writeMessage"(message: string, severity: $IFernflowerLogger$Severity$Type): void
public "startProcessingClass"(className: string): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ProbeDecompilerLogger$Type = ($ProbeDecompilerLogger);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ProbeDecompilerLogger_ = $ProbeDecompilerLogger$Type;
}}
declare module "packages/moe/wolfgirl/probejs/lang/typescript/code/ts/$MethodDeclaration" {
import {$ClassPath, $ClassPath$Type} from "packages/moe/wolfgirl/probejs/lang/java/clazz/$ClassPath"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$List, $List$Type} from "packages/java/util/$List"
import {$TSVariableType, $TSVariableType$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/code/type/$TSVariableType"
import {$ParamDecl, $ParamDecl$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/code/member/$ParamDecl"
import {$Declaration, $Declaration$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/$Declaration"
import {$CommentableCode, $CommentableCode$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/code/member/$CommentableCode"
import {$BaseType, $BaseType$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/code/type/$BaseType"

export class $MethodDeclaration extends $CommentableCode {
 "name": string
readonly "variableTypes": $List<($TSVariableType)>
readonly "params": $List<($ParamDecl)>
 "returnType": $BaseType
readonly "comments": $List<(string)>

constructor(name: string, variableTypes: $List$Type<($TSVariableType$Type)>, params: $List$Type<($ParamDecl$Type)>, returnType: $BaseType$Type)

public "formatRaw"(declaration: $Declaration$Type): $List<(string)>
public "getUsedClassPaths"(): $Collection<($ClassPath)>
get "usedClassPaths"(): $Collection<($ClassPath)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MethodDeclaration$Type = ($MethodDeclaration);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MethodDeclaration_ = $MethodDeclaration$Type;
}}
declare module "packages/moe/wolfgirl/probejs/lang/typescript/code/member/$MethodDecl" {
import {$ClassPath, $ClassPath$Type} from "packages/moe/wolfgirl/probejs/lang/java/clazz/$ClassPath"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$List, $List$Type} from "packages/java/util/$List"
import {$TSVariableType, $TSVariableType$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/code/type/$TSVariableType"
import {$ParamDecl, $ParamDecl$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/code/member/$ParamDecl"
import {$Declaration, $Declaration$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/$Declaration"
import {$CommentableCode, $CommentableCode$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/code/member/$CommentableCode"
import {$BaseType, $BaseType$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/code/type/$BaseType"

export class $MethodDecl extends $CommentableCode {
 "name": string
 "isAbstract": boolean
 "isStatic": boolean
 "isInterface": boolean
 "variableTypes": $List<($TSVariableType)>
 "params": $List<($ParamDecl)>
 "returnType": $BaseType
 "content": string
readonly "comments": $List<(string)>

constructor(name: string, variableTypes: $List$Type<($TSVariableType$Type)>, params: $List$Type<($ParamDecl$Type)>, returnType: $BaseType$Type)

public "formatRaw"(declaration: $Declaration$Type): $List<(string)>
public "getUsedClassPaths"(): $Collection<($ClassPath)>
get "usedClassPaths"(): $Collection<($ClassPath)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MethodDecl$Type = ($MethodDecl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MethodDecl_ = $MethodDecl$Type;
}}
declare module "packages/moe/wolfgirl/probejs/lang/java/type/impl/$ClassType" {
import {$TypeDescriptor, $TypeDescriptor$Type} from "packages/moe/wolfgirl/probejs/lang/java/type/$TypeDescriptor"
import {$ClassPath, $ClassPath$Type} from "packages/moe/wolfgirl/probejs/lang/java/clazz/$ClassPath"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$Type, $Type$Type} from "packages/java/lang/reflect/$Type"
import {$AnnotatedType, $AnnotatedType$Type} from "packages/java/lang/reflect/$AnnotatedType"

export class $ClassType extends $TypeDescriptor {
readonly "classPath": $ClassPath
readonly "clazz": $Class<(any)>

constructor(type: $AnnotatedType$Type)
constructor(type: $Type$Type)

public "hashCode"(): integer
public "stream"(): $Stream<($TypeDescriptor)>
public "getClasses"(): $Collection<($Class<(any)>)>
public "getClassPaths"(): $Collection<($ClassPath)>
get "classes"(): $Collection<($Class<(any)>)>
get "classPaths"(): $Collection<($ClassPath)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClassType$Type = ($ClassType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClassType_ = $ClassType$Type;
}}
declare module "packages/moe/wolfgirl/probejs/lang/typescript/code/member/$FieldDecl" {
import {$ClassPath, $ClassPath$Type} from "packages/moe/wolfgirl/probejs/lang/java/clazz/$ClassPath"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Declaration, $Declaration$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/$Declaration"
import {$CommentableCode, $CommentableCode$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/code/member/$CommentableCode"
import {$BaseType, $BaseType$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/code/type/$BaseType"

export class $FieldDecl extends $CommentableCode {
 "isFinal": boolean
 "isStatic": boolean
 "name": string
 "type": $BaseType
readonly "comments": $List<(string)>

constructor(name: string, type: $BaseType$Type)

public "formatRaw"(declaration: $Declaration$Type): $List<(string)>
public "getUsedClassPaths"(): $Collection<($ClassPath)>
get "usedClassPaths"(): $Collection<($ClassPath)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FieldDecl$Type = ($FieldDecl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FieldDecl_ = $FieldDecl$Type;
}}
declare module "packages/moe/wolfgirl/probejs/lang/decompiler/remapper/$ProbeRemapper" {
import {$IIdentifierRenamer, $IIdentifierRenamer$Type} from "packages/org/jetbrains/java/decompiler/main/extern/$IIdentifierRenamer"
import {$RemappedClass, $RemappedClass$Type} from "packages/moe/wolfgirl/probejs/lang/decompiler/remapper/$RemappedClass"
import {$IIdentifierRenamer$Type, $IIdentifierRenamer$Type$Type} from "packages/org/jetbrains/java/decompiler/main/extern/$IIdentifierRenamer$Type"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $ProbeRemapper implements $IIdentifierRenamer {
readonly "srgClasses": $Map<(string), (string)>
readonly "srgMethods": $Map<(string), (string)>
readonly "srgFields": $Map<(string), (string)>

constructor()

public "loadFromRemapped"(remapped: $Map$Type<(string), ($RemappedClass$Type)>): void
public "getNextClassName"(fullName: string, shortName: string): string
public "toBeRenamed"(elementType: $IIdentifierRenamer$Type$Type, className: string, element: string, descriptor: string): boolean
public "getNextMethodName"(className: string, method: string, descriptor: string): string
public "getNextFieldName"(className: string, field: string, descriptor: string): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ProbeRemapper$Type = ($ProbeRemapper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ProbeRemapper_ = $ProbeRemapper$Type;
}}
declare module "packages/moe/wolfgirl/probejs/lang/typescript/code/type/$TSParamType" {
import {$ClassPath, $ClassPath$Type} from "packages/moe/wolfgirl/probejs/lang/java/clazz/$ClassPath"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Declaration, $Declaration$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/$Declaration"
import {$BaseType$FormatType, $BaseType$FormatType$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/code/type/$BaseType$FormatType"
import {$BaseType, $BaseType$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/code/type/$BaseType"

export class $TSParamType extends $BaseType {
 "baseType": $BaseType
readonly "params": $List<($BaseType)>

constructor(baseType: $BaseType$Type, params: $List$Type<($BaseType$Type)>)

public "format"(declaration: $Declaration$Type, input: $BaseType$FormatType$Type): $List<(string)>
public "getUsedClassPaths"(): $Collection<($ClassPath)>
get "usedClassPaths"(): $Collection<($ClassPath)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TSParamType$Type = ($TSParamType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TSParamType_ = $TSParamType$Type;
}}
declare module "packages/moe/wolfgirl/probejs/lang/linter/rules/$Rule" {
import {$LintingWarning, $LintingWarning$Type} from "packages/moe/wolfgirl/probejs/lang/linter/$LintingWarning"
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$List, $List$Type} from "packages/java/util/$List"

export class $Rule {

constructor()

public "lint"(basePath: $Path$Type): $List<($LintingWarning)>
public "acceptFile"(path: $Path$Type, content: $List$Type<(string)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Rule$Type = ($Rule);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Rule_ = $Rule$Type;
}}
declare module "packages/moe/wolfgirl/probejs/events/$ProbeEvents" {
import {$EventHandler, $EventHandler$Type} from "packages/dev/latvian/mods/kubejs/event/$EventHandler"
import {$EventGroup, $EventGroup$Type} from "packages/dev/latvian/mods/kubejs/event/$EventGroup"

export interface $ProbeEvents {

}

export namespace $ProbeEvents {
const GROUP: $EventGroup
const ASSIGN_TYPE: $EventHandler
const MODIFY_DOC: $EventHandler
const SNIPPETS: $EventHandler
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ProbeEvents$Type = ($ProbeEvents);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ProbeEvents_ = $ProbeEvents$Type;
}}
declare module "packages/moe/wolfgirl/probejs/lang/transpiler/transformation/$InjectAnnotation" {
import {$Param, $Param$Type} from "packages/dev/latvian/mods/kubejs/typings/$Param"
import {$CommentableCode, $CommentableCode$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/code/member/$CommentableCode"
import {$FieldInfo, $FieldInfo$Type} from "packages/moe/wolfgirl/probejs/lang/java/clazz/members/$FieldInfo"
import {$ConstructorInfo, $ConstructorInfo$Type} from "packages/moe/wolfgirl/probejs/lang/java/clazz/members/$ConstructorInfo"
import {$ClassDecl, $ClassDecl$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/code/member/$ClassDecl"
import {$MethodInfo, $MethodInfo$Type} from "packages/moe/wolfgirl/probejs/lang/java/clazz/members/$MethodInfo"
import {$ClassTransformer, $ClassTransformer$Type} from "packages/moe/wolfgirl/probejs/lang/transpiler/transformation/$ClassTransformer"
import {$MethodDecl, $MethodDecl$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/code/member/$MethodDecl"
import {$AnnotationHolder, $AnnotationHolder$Type} from "packages/moe/wolfgirl/probejs/lang/java/base/$AnnotationHolder"
import {$FieldDecl, $FieldDecl$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/code/member/$FieldDecl"
import {$ConstructorDecl, $ConstructorDecl$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/code/member/$ConstructorDecl"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Clazz, $Clazz$Type} from "packages/moe/wolfgirl/probejs/lang/java/clazz/$Clazz"

export class $InjectAnnotation implements $ClassTransformer {

constructor()

public "transform"(clazz: $Clazz$Type, classDecl: $ClassDecl$Type): void
public "applyInfo"(info: $AnnotationHolder$Type, decl: $CommentableCode$Type): $List<($Param)>
public "transformField"(fieldInfo: $FieldInfo$Type, decl: $FieldDecl$Type): void
public "transformMethod"(methodInfo: $MethodInfo$Type, decl: $MethodDecl$Type): void
public "transformConstructor"(constructorInfo: $ConstructorInfo$Type, decl: $ConstructorDecl$Type): void
public static "transformMethods"(methodInfo: $MethodInfo$Type, methodDecl: $MethodDecl$Type): void
public static "transformClass"(clazz: $Clazz$Type, classDecl: $ClassDecl$Type): void
public static "transformFields"(fieldInfo: $FieldInfo$Type, fieldDecl: $FieldDecl$Type): void
public static "transformConstructors"(constructorInfo: $ConstructorInfo$Type, constructorDecl: $ConstructorDecl$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InjectAnnotation$Type = ($InjectAnnotation);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InjectAnnotation_ = $InjectAnnotation$Type;
}}
declare module "packages/moe/wolfgirl/probejs/docs/events/$RegistryEvents" {
import {$ProbeJSPlugin, $ProbeJSPlugin$Type} from "packages/moe/wolfgirl/probejs/plugin/$ProbeJSPlugin"
import {$ClassPath, $ClassPath$Type} from "packages/moe/wolfgirl/probejs/lang/java/clazz/$ClassPath"
import {$ScriptDump, $ScriptDump$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/$ScriptDump"
import {$Map, $Map$Type} from "packages/java/util/$Map"
import {$TypeScriptFile, $TypeScriptFile$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/$TypeScriptFile"

export class $RegistryEvents extends $ProbeJSPlugin {

constructor()

public "modifyClasses"(scriptDump: $ScriptDump$Type, globalClasses: $Map$Type<($ClassPath$Type), ($TypeScriptFile$Type)>): void
public "addGlobals"(scriptDump: $ScriptDump$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RegistryEvents$Type = ($RegistryEvents);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RegistryEvents_ = $RegistryEvents$Type;
}}
declare module "packages/moe/wolfgirl/probejs/lang/java/type/impl/$VariableType" {
import {$TypeDescriptor, $TypeDescriptor$Type} from "packages/moe/wolfgirl/probejs/lang/java/type/$TypeDescriptor"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$AnnotatedTypeVariable, $AnnotatedTypeVariable$Type} from "packages/java/lang/reflect/$AnnotatedTypeVariable"
import {$TypeVariable, $TypeVariable$Type} from "packages/java/lang/reflect/$TypeVariable"

export class $VariableType extends $TypeDescriptor {
 "symbol": string
 "descriptors": $List<($TypeDescriptor)>

constructor(typeVariable: $TypeVariable$Type<(any)>, checkBounds: boolean)
constructor(typeVariable: $AnnotatedTypeVariable$Type, checkBounds: boolean)
constructor(typeVariable: $TypeVariable$Type<(any)>)
constructor(typeVariable: $AnnotatedTypeVariable$Type)

public "stream"(): $Stream<($TypeDescriptor)>
public "getSymbol"(): string
public "getDescriptors"(): $List<($TypeDescriptor)>
get "symbol"(): string
get "descriptors"(): $List<($TypeDescriptor)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $VariableType$Type = ($VariableType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $VariableType_ = $VariableType$Type;
}}
declare module "packages/moe/wolfgirl/probejs/lang/typescript/code/type/$TSVariableType" {
import {$ClassPath, $ClassPath$Type} from "packages/moe/wolfgirl/probejs/lang/java/clazz/$ClassPath"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Declaration, $Declaration$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/$Declaration"
import {$BaseType$FormatType, $BaseType$FormatType$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/code/type/$BaseType$FormatType"
import {$BaseType, $BaseType$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/code/type/$BaseType"

export class $TSVariableType extends $BaseType {
readonly "symbol": string
 "extendsType": $BaseType

constructor(symbol: string, extendsType: $BaseType$Type)

public "format"(declaration: $Declaration$Type, input: $BaseType$FormatType$Type): $List<(string)>
public "getUsedClassPaths"(): $Collection<($ClassPath)>
get "usedClassPaths"(): $Collection<($ClassPath)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TSVariableType$Type = ($TSVariableType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TSVariableType_ = $TSVariableType$Type;
}}
declare module "packages/moe/wolfgirl/probejs/lang/transpiler/$ClassTranspiler" {
import {$Converter, $Converter$Type} from "packages/moe/wolfgirl/probejs/lang/transpiler/members/$Converter"
import {$TypeConverter, $TypeConverter$Type} from "packages/moe/wolfgirl/probejs/lang/transpiler/$TypeConverter"
import {$Clazz, $Clazz$Type} from "packages/moe/wolfgirl/probejs/lang/java/clazz/$Clazz"
import {$ClassDecl, $ClassDecl$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/code/member/$ClassDecl"

export class $ClassTranspiler extends $Converter<($Clazz), ($ClassDecl)> {

constructor(converter: $TypeConverter$Type)

public "transpile"(input: $Clazz$Type): $ClassDecl
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClassTranspiler$Type = ($ClassTranspiler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClassTranspiler_ = $ClassTranspiler$Type;
}}
declare module "packages/moe/wolfgirl/probejs/lang/java/clazz/$Clazz$ClassAttribute" {
import {$Clazz$ClassType, $Clazz$ClassType$Type} from "packages/moe/wolfgirl/probejs/lang/java/clazz/$Clazz$ClassType"
import {$Class, $Class$Type} from "packages/java/lang/$Class"

export class $Clazz$ClassAttribute {
readonly "type": $Clazz$ClassType
readonly "isAbstract": boolean
readonly "isInterface": boolean
readonly "raw": $Class<(any)>

constructor(clazz: $Class$Type<(any)>)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Clazz$ClassAttribute$Type = ($Clazz$ClassAttribute);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Clazz$ClassAttribute_ = $Clazz$ClassAttribute$Type;
}}
declare module "packages/moe/wolfgirl/probejs/lang/typescript/code/ts/$Statements" {
import {$ClassDecl$Builder, $ClassDecl$Builder$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/code/member/$ClassDecl$Builder"
import {$MethodDeclaration$Builder, $MethodDeclaration$Builder$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/code/ts/$MethodDeclaration$Builder"

export interface $Statements {

}

export namespace $Statements {
function clazz(name: string): $ClassDecl$Builder
function method(name: string): $MethodDeclaration$Builder
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Statements$Type = ($Statements);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Statements_ = $Statements$Type;
}}
declare module "packages/moe/wolfgirl/probejs/lang/snippet/parts/$TabStop" {
import {$Enumerable, $Enumerable$Type} from "packages/moe/wolfgirl/probejs/lang/snippet/parts/$Enumerable"

export class $TabStop extends $Enumerable {
readonly "content": string
 "enumeration": integer

constructor(content: string)

public "format"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TabStop$Type = ($TabStop);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TabStop_ = $TabStop$Type;
}}
declare module "packages/moe/wolfgirl/probejs/lang/transpiler/transformation/$ClassTransformer" {
import {$FieldDecl, $FieldDecl$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/code/member/$FieldDecl"
import {$ConstructorDecl, $ConstructorDecl$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/code/member/$ConstructorDecl"
import {$FieldInfo, $FieldInfo$Type} from "packages/moe/wolfgirl/probejs/lang/java/clazz/members/$FieldInfo"
import {$ConstructorInfo, $ConstructorInfo$Type} from "packages/moe/wolfgirl/probejs/lang/java/clazz/members/$ConstructorInfo"
import {$Clazz, $Clazz$Type} from "packages/moe/wolfgirl/probejs/lang/java/clazz/$Clazz"
import {$ClassDecl, $ClassDecl$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/code/member/$ClassDecl"
import {$MethodInfo, $MethodInfo$Type} from "packages/moe/wolfgirl/probejs/lang/java/clazz/members/$MethodInfo"
import {$MethodDecl, $MethodDecl$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/code/member/$MethodDecl"

export interface $ClassTransformer {

 "transform"(clazz: $Clazz$Type, classDecl: $ClassDecl$Type): void
 "transformField"(fieldInfo: $FieldInfo$Type, fieldDecl: $FieldDecl$Type): void
 "transformMethod"(methodInfo: $MethodInfo$Type, methodDecl: $MethodDecl$Type): void
 "transformConstructor"(constructorInfo: $ConstructorInfo$Type, constructorDecl: $ConstructorDecl$Type): void

(clazz: $Clazz$Type, classDecl: $ClassDecl$Type): void
}

export namespace $ClassTransformer {
const CLASS_TRANSFORMERS: ($ClassTransformer)[]
function transformMethods(methodInfo: $MethodInfo$Type, methodDecl: $MethodDecl$Type): void
function transformClass(clazz: $Clazz$Type, classDecl: $ClassDecl$Type): void
function transformFields(fieldInfo: $FieldInfo$Type, fieldDecl: $FieldDecl$Type): void
function transformConstructors(constructorInfo: $ConstructorInfo$Type, constructorDecl: $ConstructorDecl$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClassTransformer$Type = ($ClassTransformer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClassTransformer_ = $ClassTransformer$Type;
}}
declare module "packages/moe/wolfgirl/probejs/lang/linter/rules/$RespectPriority" {
import {$LintingWarning, $LintingWarning$Type} from "packages/moe/wolfgirl/probejs/lang/linter/$LintingWarning"
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Rule, $Rule$Type} from "packages/moe/wolfgirl/probejs/lang/linter/rules/$Rule"

export class $RespectPriority extends $Rule {

constructor()

public "lint"(basePath: $Path$Type): $List<($LintingWarning)>
public "acceptFile"(path: $Path$Type, content: $List$Type<(string)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RespectPriority$Type = ($RespectPriority);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RespectPriority_ = $RespectPriority$Type;
}}
declare module "packages/moe/wolfgirl/probejs/utils/$RemapperUtils" {
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$JavaMembers$MethodInfo, $JavaMembers$MethodInfo$Type} from "packages/dev/latvian/mods/rhino/$JavaMembers$MethodInfo"
import {$Constructor, $Constructor$Type} from "packages/java/lang/reflect/$Constructor"
import {$JavaMembers$FieldInfo, $JavaMembers$FieldInfo$Type} from "packages/dev/latvian/mods/rhino/$JavaMembers$FieldInfo"

export class $RemapperUtils {

constructor()

public static "getFields"(from: $Class$Type<(any)>): $Collection<($JavaMembers$FieldInfo)>
public static "getMethods"(from: $Class$Type<(any)>): $Collection<($JavaMembers$MethodInfo)>
public static "getConstructors"(from: $Class$Type<(any)>): $Collection<($Constructor<(any)>)>
public static "getRemappedClassName"(clazz: $Class$Type<(any)>): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RemapperUtils$Type = ($RemapperUtils);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RemapperUtils_ = $RemapperUtils$Type;
}}
declare module "packages/moe/wolfgirl/probejs/lang/typescript/code/ts/$ReexportDeclaration" {
import {$VariableDeclaration, $VariableDeclaration$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/code/ts/$VariableDeclaration"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Declaration, $Declaration$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/$Declaration"
import {$BaseType, $BaseType$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/code/type/$BaseType"

export class $ReexportDeclaration extends $VariableDeclaration {
 "symbol": string
 "type": $BaseType
readonly "comments": $List<(string)>

constructor(symbol: string, type: $BaseType$Type)

public "formatRaw"(declaration: $Declaration$Type): $List<(string)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ReexportDeclaration$Type = ($ReexportDeclaration);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ReexportDeclaration_ = $ReexportDeclaration$Type;
}}
declare module "packages/moe/wolfgirl/probejs/lang/typescript/code/type/js/$JSArrayType" {
import {$ClassPath, $ClassPath$Type} from "packages/moe/wolfgirl/probejs/lang/java/clazz/$ClassPath"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Declaration, $Declaration$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/$Declaration"
import {$BaseType$FormatType, $BaseType$FormatType$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/code/type/$BaseType$FormatType"
import {$BaseType, $BaseType$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/code/type/$BaseType"

export class $JSArrayType extends $BaseType {
readonly "components": $List<($BaseType)>

constructor(components: $List$Type<($BaseType$Type)>)

public "format"(declaration: $Declaration$Type, input: $BaseType$FormatType$Type): $List<(string)>
public "getUsedClassPaths"(): $Collection<($ClassPath)>
get "usedClassPaths"(): $Collection<($ClassPath)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JSArrayType$Type = ($JSArrayType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JSArrayType_ = $JSArrayType$Type;
}}
declare module "packages/moe/wolfgirl/probejs/lang/java/clazz/members/$ConstructorInfo" {
import {$ClassPath, $ClassPath$Type} from "packages/moe/wolfgirl/probejs/lang/java/clazz/$ClassPath"
import {$ParamInfo, $ParamInfo$Type} from "packages/moe/wolfgirl/probejs/lang/java/clazz/members/$ParamInfo"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$List, $List$Type} from "packages/java/util/$List"
import {$VariableType, $VariableType$Type} from "packages/moe/wolfgirl/probejs/lang/java/type/impl/$VariableType"
import {$Constructor, $Constructor$Type} from "packages/java/lang/reflect/$Constructor"
import {$ClassPathProvider, $ClassPathProvider$Type} from "packages/moe/wolfgirl/probejs/lang/java/base/$ClassPathProvider"
import {$TypeVariableHolder, $TypeVariableHolder$Type} from "packages/moe/wolfgirl/probejs/lang/java/base/$TypeVariableHolder"

export class $ConstructorInfo extends $TypeVariableHolder implements $ClassPathProvider {
readonly "params": $List<($ParamInfo)>
readonly "variableTypes": $List<($VariableType)>

constructor(arg0: $Constructor$Type<(any)>)

public "getClassPaths"(): $Collection<($ClassPath)>
get "classPaths"(): $Collection<($ClassPath)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ConstructorInfo$Type = ($ConstructorInfo);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ConstructorInfo_ = $ConstructorInfo$Type;
}}
declare module "packages/moe/wolfgirl/probejs/lang/typescript/code/member/$ClassDecl" {
import {$FieldDecl, $FieldDecl$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/code/member/$FieldDecl"
import {$ConstructorDecl, $ConstructorDecl$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/code/member/$ConstructorDecl"
import {$ClassPath, $ClassPath$Type} from "packages/moe/wolfgirl/probejs/lang/java/clazz/$ClassPath"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Code, $Code$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/code/$Code"
import {$TSVariableType, $TSVariableType$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/code/type/$TSVariableType"
import {$Declaration, $Declaration$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/$Declaration"
import {$CommentableCode, $CommentableCode$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/code/member/$CommentableCode"
import {$BaseType, $BaseType$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/code/type/$BaseType"
import {$MethodDecl, $MethodDecl$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/code/member/$MethodDecl"

export class $ClassDecl extends $CommentableCode {
readonly "name": string
 "superClass": $BaseType
readonly "interfaces": $List<($BaseType)>
readonly "variableTypes": $List<($TSVariableType)>
 "isAbstract": boolean
 "isNative": boolean
readonly "fields": $List<($FieldDecl)>
readonly "constructors": $List<($ConstructorDecl)>
readonly "methods": $List<($MethodDecl)>
readonly "bodyCode": $List<($Code)>
readonly "comments": $List<(string)>

constructor(name: string, superClass: $BaseType$Type, interfaces: $List$Type<($BaseType$Type)>, variableTypes: $List$Type<($TSVariableType$Type)>)

public "formatRaw"(declaration: $Declaration$Type): $List<(string)>
public "getUsedClassPaths"(): $Collection<($ClassPath)>
get "usedClassPaths"(): $Collection<($ClassPath)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClassDecl$Type = ($ClassDecl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClassDecl_ = $ClassDecl$Type;
}}
declare module "packages/moe/wolfgirl/probejs/utils/$FileUtils" {
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"

export class $FileUtils {

constructor()

public static "forEachFile"(basePath: $Path$Type, callback: $Consumer$Type<($Path$Type)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FileUtils$Type = ($FileUtils);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FileUtils_ = $FileUtils$Type;
}}
declare module "packages/moe/wolfgirl/probejs/lang/java/clazz/members/$MethodInfo" {
import {$TypeDescriptor, $TypeDescriptor$Type} from "packages/moe/wolfgirl/probejs/lang/java/type/$TypeDescriptor"
import {$ClassPath, $ClassPath$Type} from "packages/moe/wolfgirl/probejs/lang/java/clazz/$ClassPath"
import {$ParamInfo, $ParamInfo$Type} from "packages/moe/wolfgirl/probejs/lang/java/clazz/members/$ParamInfo"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$VariableType, $VariableType$Type} from "packages/moe/wolfgirl/probejs/lang/java/type/impl/$VariableType"
import {$TypeVariable, $TypeVariable$Type} from "packages/java/lang/reflect/$TypeVariable"
import {$ClassPathProvider, $ClassPathProvider$Type} from "packages/moe/wolfgirl/probejs/lang/java/base/$ClassPathProvider"
import {$List, $List$Type} from "packages/java/util/$List"
import {$JavaMembers$MethodInfo, $JavaMembers$MethodInfo$Type} from "packages/dev/latvian/mods/rhino/$JavaMembers$MethodInfo"
import {$Type, $Type$Type} from "packages/java/lang/reflect/$Type"
import {$MethodInfo$MethodAttributes, $MethodInfo$MethodAttributes$Type} from "packages/moe/wolfgirl/probejs/lang/java/clazz/members/$MethodInfo$MethodAttributes"
import {$Map, $Map$Type} from "packages/java/util/$Map"
import {$TypeVariableHolder, $TypeVariableHolder$Type} from "packages/moe/wolfgirl/probejs/lang/java/base/$TypeVariableHolder"

export class $MethodInfo extends $TypeVariableHolder implements $ClassPathProvider {
readonly "name": string
readonly "params": $List<($ParamInfo)>
 "returnType": $TypeDescriptor
readonly "attributes": $MethodInfo$MethodAttributes
readonly "variableTypes": $List<($VariableType)>

constructor(methodInfo: $JavaMembers$MethodInfo$Type, remapper: $Map$Type<($TypeVariable$Type<(any)>), ($Type$Type)>)

public "getClassPaths"(): $Collection<($ClassPath)>
get "classPaths"(): $Collection<($ClassPath)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MethodInfo$Type = ($MethodInfo);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MethodInfo_ = $MethodInfo$Type;
}}
declare module "packages/moe/wolfgirl/probejs/lang/transpiler/members/$Method" {
import {$Converter, $Converter$Type} from "packages/moe/wolfgirl/probejs/lang/transpiler/members/$Converter"
import {$TypeConverter, $TypeConverter$Type} from "packages/moe/wolfgirl/probejs/lang/transpiler/$TypeConverter"
import {$MethodInfo, $MethodInfo$Type} from "packages/moe/wolfgirl/probejs/lang/java/clazz/members/$MethodInfo"
import {$MethodDecl, $MethodDecl$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/code/member/$MethodDecl"

export class $Method extends $Converter<($MethodInfo), ($MethodDecl)> {

constructor(converter: $TypeConverter$Type)

public "transpile"(input: $MethodInfo$Type): $MethodDecl
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Method$Type = ($Method);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Method_ = $Method$Type;
}}
declare module "packages/moe/wolfgirl/probejs/lang/typescript/code/type/$TSArrayType" {
import {$ClassPath, $ClassPath$Type} from "packages/moe/wolfgirl/probejs/lang/java/clazz/$ClassPath"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Declaration, $Declaration$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/$Declaration"
import {$BaseType$FormatType, $BaseType$FormatType$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/code/type/$BaseType$FormatType"
import {$BaseType, $BaseType$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/code/type/$BaseType"

export class $TSArrayType extends $BaseType {
 "component": $BaseType

constructor(component: $BaseType$Type)

public "format"(declaration: $Declaration$Type, input: $BaseType$FormatType$Type): $List<(string)>
public "getUsedClassPaths"(): $Collection<($ClassPath)>
get "usedClassPaths"(): $Collection<($ClassPath)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TSArrayType$Type = ($TSArrayType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TSArrayType_ = $TSArrayType$Type;
}}
declare module "packages/moe/wolfgirl/probejs/lang/snippet/parts/$Choice" {
import {$Enumerable, $Enumerable$Type} from "packages/moe/wolfgirl/probejs/lang/snippet/parts/$Enumerable"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"

export class $Choice extends $Enumerable {
readonly "choices": $Collection<(string)>
 "enumeration": integer

constructor(choices: $Collection$Type<(string)>)

public "format"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Choice$Type = ($Choice);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Choice_ = $Choice$Type;
}}
declare module "packages/moe/wolfgirl/probejs/lang/typescript/code/type/js/$JSLambdaType" {
import {$ClassPath, $ClassPath$Type} from "packages/moe/wolfgirl/probejs/lang/java/clazz/$ClassPath"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ParamDecl, $ParamDecl$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/code/member/$ParamDecl"
import {$Declaration, $Declaration$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/$Declaration"
import {$BaseType$FormatType, $BaseType$FormatType$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/code/type/$BaseType$FormatType"
import {$BaseType, $BaseType$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/code/type/$BaseType"
import {$MethodDecl, $MethodDecl$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/code/member/$MethodDecl"

export class $JSLambdaType extends $BaseType {
readonly "params": $List<($ParamDecl)>
readonly "returnType": $BaseType

constructor(params: $List$Type<($ParamDecl$Type)>, returnType: $BaseType$Type)

public "formatWithName"(name: string, declaration: $Declaration$Type, input: $BaseType$FormatType$Type): string
public "asMethod"(methodName: string): $MethodDecl
public "format"(declaration: $Declaration$Type, input: $BaseType$FormatType$Type): $List<(string)>
public "getUsedClassPaths"(): $Collection<($ClassPath)>
get "usedClassPaths"(): $Collection<($ClassPath)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JSLambdaType$Type = ($JSLambdaType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JSLambdaType_ = $JSLambdaType$Type;
}}
declare module "packages/moe/wolfgirl/probejs/lang/typescript/code/type/js/$JSLambdaType$Builder" {
import {$JSLambdaType, $JSLambdaType$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/code/type/js/$JSLambdaType"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ParamDecl, $ParamDecl$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/code/member/$ParamDecl"
import {$BaseType, $BaseType$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/code/type/$BaseType"

export class $JSLambdaType$Builder {
readonly "params": $List<($ParamDecl)>
 "returnType": $BaseType
 "arrowFunction": boolean

constructor()

public "returnType"(type: $BaseType$Type): $JSLambdaType$Builder
public "method"(): $JSLambdaType$Builder
public "param"(symbol: string, type: $BaseType$Type, isOptional: boolean, isVarArg: boolean): $JSLambdaType$Builder
public "param"(symbol: string, type: $BaseType$Type, isOptional: boolean): $JSLambdaType$Builder
public "param"(symbol: string, type: $BaseType$Type): $JSLambdaType$Builder
public "build"(): $JSLambdaType
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JSLambdaType$Builder$Type = ($JSLambdaType$Builder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JSLambdaType$Builder_ = $JSLambdaType$Builder$Type;
}}
declare module "packages/moe/wolfgirl/probejs/docs/$ParamFix" {
import {$ProbeJSPlugin, $ProbeJSPlugin$Type} from "packages/moe/wolfgirl/probejs/plugin/$ProbeJSPlugin"
import {$ClassPath, $ClassPath$Type} from "packages/moe/wolfgirl/probejs/lang/java/clazz/$ClassPath"
import {$ScriptDump, $ScriptDump$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/$ScriptDump"
import {$Map, $Map$Type} from "packages/java/util/$Map"
import {$TypeScriptFile, $TypeScriptFile$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/$TypeScriptFile"

export class $ParamFix extends $ProbeJSPlugin {

constructor()

public "modifyClasses"(scriptDump: $ScriptDump$Type, globalClasses: $Map$Type<($ClassPath$Type), ($TypeScriptFile$Type)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ParamFix$Type = ($ParamFix);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ParamFix_ = $ParamFix$Type;
}}
declare module "packages/moe/wolfgirl/probejs/lang/typescript/$ScriptDump" {
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$ClassPath, $ClassPath$Type} from "packages/moe/wolfgirl/probejs/lang/java/clazz/$ClassPath"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$ScriptType, $ScriptType$Type} from "packages/dev/latvian/mods/kubejs/script/$ScriptType"
import {$Code, $Code$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/code/$Code"
import {$ScriptManager, $ScriptManager$Type} from "packages/dev/latvian/mods/kubejs/script/$ScriptManager"
import {$Wrapped$Global, $Wrapped$Global$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/code/ts/$Wrapped$Global"
import {$Transpiler, $Transpiler$Type} from "packages/moe/wolfgirl/probejs/lang/transpiler/$Transpiler"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$Pair, $Pair$Type} from "packages/com/mojang/datafixers/util/$Pair"
import {$Clazz, $Clazz$Type} from "packages/moe/wolfgirl/probejs/lang/java/clazz/$Clazz"
import {$BaseType, $BaseType$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/code/type/$BaseType"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $ScriptDump {
static readonly "SERVER_DUMP": $Supplier<($ScriptDump)>
static readonly "CLIENT_DUMP": $Supplier<($ScriptDump)>
static readonly "STARTUP_DUMP": $Supplier<($ScriptDump)>
readonly "scriptType": $ScriptType
readonly "manager": $ScriptManager
readonly "basePath": $Path
readonly "scriptPath": $Path
readonly "globals": $Map<(string), ($Pair<($Collection<(string)>), ($Wrapped$Global)>)>
readonly "transpiler": $Transpiler
readonly "recordedClasses": $Set<($Clazz)>
 "dumped": integer
 "total": integer

constructor(manager: $ScriptManager$Type, basePath: $Path$Type, scriptPath: $Path$Type, scriptPredicate: $Predicate$Type<($Clazz$Type)>)

public "getGlobalFolder"(): $Path
public "getTypeFolder"(): $Path
public "dumpClasses"(): void
public "ensurePath"(path: string): $Path
public "ensurePath"(path: string, script: boolean): $Path
public "getPackageFolder"(): $Path
public "addGlobal"(identifier: string, excludedNames: $Collection$Type<(string)>, ...content: ($Code$Type)[]): void
public "addGlobal"(identifier: string, ...content: ($Code$Type)[]): void
public "dumpGlobal"(): void
public "dumpJSConfig"(): void
public "getSource"(): $Path
public "assignType"(classPath: $Class$Type<(any)>, type: $BaseType$Type): void
public "assignType"(classPath: $ClassPath$Type, type: $BaseType$Type): void
public "retrieveClasses"(): $Set<($Class<(any)>)>
public "removeClasses"(): void
public "acceptClasses"(classes: $Collection$Type<($Clazz$Type)>): void
public "dump"(): void
get "globalFolder"(): $Path
get "typeFolder"(): $Path
get "packageFolder"(): $Path
get "source"(): $Path
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ScriptDump$Type = ($ScriptDump);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ScriptDump_ = $ScriptDump$Type;
}}
declare module "packages/moe/wolfgirl/probejs/$GlobalStates" {
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"

export class $GlobalStates {
static readonly "KNOWN_EVENTS": $Set<($Class<(any)>)>
static readonly "MIXIN_LANG_KEYS": $Set<(string)>
static readonly "RECIPE_IDS": $Set<(string)>
static readonly "LOOT_TABLES": $Set<(string)>
static readonly "LANG_KEYS": $Supplier<($Set<(string)>)>
static readonly "RAW_TEXTURES": $Supplier<($Set<(string)>)>
static readonly "TEXTURES": $Supplier<($Set<(string)>)>
static readonly "MODS": $Supplier<($Set<(string)>)>

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GlobalStates$Type = ($GlobalStates);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GlobalStates_ = $GlobalStates$Type;
}}
declare module "packages/moe/wolfgirl/probejs/lang/linter/$LintingWarning$Level" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$Color, $Color$Type} from "packages/dev/latvian/mods/rhino/mod/util/color/$Color"

export class $LintingWarning$Level extends $Enum<($LintingWarning$Level)> {
static readonly "INFO": $LintingWarning$Level
static readonly "WARNING": $LintingWarning$Level
static readonly "ERROR": $LintingWarning$Level
readonly "color": $Color


public static "values"(): ($LintingWarning$Level)[]
public static "valueOf"(name: string): $LintingWarning$Level
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LintingWarning$Level$Type = (("warning") | ("error") | ("info")) | ($LintingWarning$Level);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LintingWarning$Level_ = $LintingWarning$Level$Type;
}}
declare module "packages/moe/wolfgirl/probejs/lang/java/clazz/members/$MethodInfo$MethodAttributes" {
import {$Method, $Method$Type} from "packages/java/lang/reflect/$Method"

export class $MethodInfo$MethodAttributes {
readonly "isStatic": boolean
readonly "isDefault": boolean
readonly "isAbstract": boolean

constructor(method: $Method$Type)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MethodInfo$MethodAttributes$Type = ($MethodInfo$MethodAttributes);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MethodInfo$MethodAttributes_ = $MethodInfo$MethodAttributes$Type;
}}
declare module "packages/moe/wolfgirl/probejs/docs/$SpecialTypes" {
import {$ProbeJSPlugin, $ProbeJSPlugin$Type} from "packages/moe/wolfgirl/probejs/plugin/$ProbeJSPlugin"
import {$SnippetDump, $SnippetDump$Type} from "packages/moe/wolfgirl/probejs/lang/snippet/$SnippetDump"
import {$ScriptDump, $ScriptDump$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/$ScriptDump"

export class $SpecialTypes extends $ProbeJSPlugin {

constructor()

public "addGlobals"(scriptDump: $ScriptDump$Type): void
public "addVSCodeSnippets"(dump: $SnippetDump$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SpecialTypes$Type = ($SpecialTypes);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SpecialTypes_ = $SpecialTypes$Type;
}}
declare module "packages/moe/wolfgirl/probejs/lang/transpiler/$Transpiler" {
import {$ClassPath, $ClassPath$Type} from "packages/moe/wolfgirl/probejs/lang/java/clazz/$ClassPath"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$TypeConverter, $TypeConverter$Type} from "packages/moe/wolfgirl/probejs/lang/transpiler/$TypeConverter"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$ScriptManager, $ScriptManager$Type} from "packages/dev/latvian/mods/kubejs/script/$ScriptManager"
import {$Clazz, $Clazz$Type} from "packages/moe/wolfgirl/probejs/lang/java/clazz/$Clazz"
import {$Map, $Map$Type} from "packages/java/util/$Map"
import {$TypeScriptFile, $TypeScriptFile$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/$TypeScriptFile"

export class $Transpiler {
readonly "typeConverter": $TypeConverter
readonly "rejectedClasses": $Set<($ClassPath)>

constructor(manager: $ScriptManager$Type)

public "reject"(clazz: $Class$Type<(any)>): void
public "dump"(clazzes: $Collection$Type<($Clazz$Type)>): $Map<($ClassPath), ($TypeScriptFile)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Transpiler$Type = ($Transpiler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Transpiler_ = $Transpiler$Type;
}}
declare module "packages/moe/wolfgirl/probejs/lang/typescript/code/type/js/$JSJoinedType" {
import {$ClassPath, $ClassPath$Type} from "packages/moe/wolfgirl/probejs/lang/java/clazz/$ClassPath"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Declaration, $Declaration$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/$Declaration"
import {$BaseType$FormatType, $BaseType$FormatType$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/code/type/$BaseType$FormatType"
import {$BaseType, $BaseType$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/code/type/$BaseType"

export class $JSJoinedType extends $BaseType {
readonly "delimiter": string
readonly "types": $List<($BaseType)>


public "format"(declaration: $Declaration$Type, input: $BaseType$FormatType$Type): $List<(string)>
public "getUsedClassPaths"(): $Collection<($ClassPath)>
get "usedClassPaths"(): $Collection<($ClassPath)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JSJoinedType$Type = ($JSJoinedType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JSJoinedType_ = $JSJoinedType$Type;
}}
declare module "packages/moe/wolfgirl/probejs/lang/decompiler/parser/$ParsedDocument" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $ParsedDocument {

constructor(content: string)

public "getCode"(): string
public "isMixinClass"(): boolean
public "getParamTransformations"(): void
get "code"(): string
get "mixinClass"(): boolean
get "paramTransformations"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ParsedDocument$Type = ($ParsedDocument);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ParsedDocument_ = $ParsedDocument$Type;
}}
declare module "packages/moe/wolfgirl/probejs/utils/$Require" {
import {$Context, $Context$Type} from "packages/dev/latvian/mods/rhino/$Context"
import {$BaseFunction, $BaseFunction$Type} from "packages/dev/latvian/mods/rhino/$BaseFunction"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$ScriptManager, $ScriptManager$Type} from "packages/dev/latvian/mods/kubejs/script/$ScriptManager"
import {$Scriptable, $Scriptable$Type} from "packages/dev/latvian/mods/rhino/$Scriptable"

export class $Require extends $BaseFunction {
static readonly "EMPTY": integer
static readonly "READONLY": integer
static readonly "DONTENUM": integer
static readonly "PERMANENT": integer
static readonly "UNINITIALIZED_CONST": integer
static readonly "CONST": integer

constructor(manager: $ScriptManager$Type)

public "call"(cx: $Context$Type, scope: $Scriptable$Type, thisObj: $Scriptable$Type, args: (any)[]): any
public "get"(arg0: $Context$Type, arg1: integer, arg2: $Scriptable$Type): any
public "put"(arg0: $Context$Type, arg1: integer, arg2: $Scriptable$Type, arg3: any): void
public "delete"(arg0: $Context$Type, arg1: integer): void
public "getDefaultValue"(arg0: $Context$Type, arg1: $Class$Type<(any)>): any
public "has"(arg0: $Context$Type, arg1: integer, arg2: $Scriptable$Type): boolean
public "setParentScope"(arg0: $Scriptable$Type): void
public "getParentScope"(): $Scriptable
public "getAllIds"(cx: $Context$Type): (any)[]
public "getIds"(arg0: $Context$Type): (any)[]
public "getPrototype"(arg0: $Context$Type): $Scriptable
public "setPrototype"(arg0: $Scriptable$Type): void
set "parentScope"(value: $Scriptable$Type)
get "parentScope"(): $Scriptable
set "prototype"(value: $Scriptable$Type)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Require$Type = ($Require);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Require_ = $Require$Type;
}}
declare module "packages/moe/wolfgirl/probejs/lang/decompiler/$ProbeDecompiler" {
import {$File, $File$Type} from "packages/java/io/$File"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Fernflower, $Fernflower$Type} from "packages/org/jetbrains/java/decompiler/main/$Fernflower"
import {$ProbeFileSaver, $ProbeFileSaver$Type} from "packages/moe/wolfgirl/probejs/lang/decompiler/$ProbeFileSaver"

export class $ProbeDecompiler {
readonly "engine": $Fernflower
readonly "resultSaver": $ProbeFileSaver

constructor()

public "addSource"(source: $File$Type): void
public "fromMods"(): void
public "decompileContext"(): void
public static "findModFiles"(): $List<($File)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ProbeDecompiler$Type = ($ProbeDecompiler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ProbeDecompiler_ = $ProbeDecompiler$Type;
}}
declare module "packages/moe/wolfgirl/probejs/lang/typescript/code/member/clazz/$ConstructorBuilder" {
import {$ConstructorDecl, $ConstructorDecl$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/code/member/$ConstructorDecl"
import {$List, $List$Type} from "packages/java/util/$List"
import {$TSVariableType, $TSVariableType$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/code/type/$TSVariableType"
import {$ParamDecl, $ParamDecl$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/code/member/$ParamDecl"
import {$BaseType, $BaseType$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/code/type/$BaseType"

export class $ConstructorBuilder {
readonly "variableTypes": $List<($TSVariableType)>
readonly "params": $List<($ParamDecl)>

constructor()

public "buildAsConstructor"(): $ConstructorDecl
public "param"(symbol: string, type: $BaseType$Type, isOptional: boolean, isVarArg: boolean): $ConstructorBuilder
public "param"(symbol: string, type: $BaseType$Type): $ConstructorBuilder
public "param"(symbol: string, type: $BaseType$Type, isOptional: boolean): $ConstructorBuilder
public "typeVariables"(...symbols: (string)[]): $ConstructorBuilder
public "typeVariables"(...variableTypes: ($TSVariableType$Type)[]): $ConstructorBuilder
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ConstructorBuilder$Type = ($ConstructorBuilder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ConstructorBuilder_ = $ConstructorBuilder$Type;
}}
declare module "packages/moe/wolfgirl/probejs/lang/java/type/impl/$ArrayType" {
import {$TypeDescriptor, $TypeDescriptor$Type} from "packages/moe/wolfgirl/probejs/lang/java/type/$TypeDescriptor"
import {$ClassPath, $ClassPath$Type} from "packages/moe/wolfgirl/probejs/lang/java/clazz/$ClassPath"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$AnnotatedArrayType, $AnnotatedArrayType$Type} from "packages/java/lang/reflect/$AnnotatedArrayType"
import {$GenericArrayType, $GenericArrayType$Type} from "packages/java/lang/reflect/$GenericArrayType"

export class $ArrayType extends $TypeDescriptor {
 "component": $TypeDescriptor

constructor(arrayType: $TypeDescriptor$Type)
constructor(arrayType: $GenericArrayType$Type)
constructor(arrayType: $AnnotatedArrayType$Type)

public "hashCode"(): integer
public "stream"(): $Stream<($TypeDescriptor)>
public "getClassPaths"(): $Collection<($ClassPath)>
get "classPaths"(): $Collection<($ClassPath)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ArrayType$Type = ($ArrayType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ArrayType_ = $ArrayType$Type;
}}
declare module "packages/moe/wolfgirl/probejs/utils/$JsonUtils" {
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$JsonArray, $JsonArray$Type} from "packages/com/google/gson/$JsonArray"

export class $JsonUtils {

constructor()

public static "parseObject"(obj: any): $JsonElement
public static "mergeJsonRecursively"(first: $JsonElement$Type, second: $JsonElement$Type): $JsonElement
public static "stripSussyJson5Stuffs"(jsonc: string): string
public static "deserializeObject"(jsonElement: $JsonElement$Type): any
public static "asStringArray"(array: $Collection$Type<(string)>): $JsonArray
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JsonUtils$Type = ($JsonUtils);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JsonUtils_ = $JsonUtils$Type;
}}
declare module "packages/moe/wolfgirl/probejs/lang/java/type/$TypeAdapter" {
import {$TypeDescriptor, $TypeDescriptor$Type} from "packages/moe/wolfgirl/probejs/lang/java/type/$TypeDescriptor"
import {$Type, $Type$Type} from "packages/java/lang/reflect/$Type"
import {$AnnotatedType, $AnnotatedType$Type} from "packages/java/lang/reflect/$AnnotatedType"

export class $TypeAdapter {

constructor()

public static "getTypeDescription"(type: $Type$Type): $TypeDescriptor
public static "getTypeDescription"(type: $Type$Type, recursive: boolean): $TypeDescriptor
public static "getTypeDescription"(type: $AnnotatedType$Type): $TypeDescriptor
public static "getTypeDescription"(type: $AnnotatedType$Type, recursive: boolean): $TypeDescriptor
public static "consolidateType"(arg0: $TypeDescriptor$Type, symbol: string, replacement: $TypeDescriptor$Type): $TypeDescriptor
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TypeAdapter$Type = ($TypeAdapter);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TypeAdapter_ = $TypeAdapter$Type;
}}
declare module "packages/moe/wolfgirl/probejs/lang/java/type/impl/$WildcardType" {
import {$TypeDescriptor, $TypeDescriptor$Type} from "packages/moe/wolfgirl/probejs/lang/java/type/$TypeDescriptor"
import {$AnnotatedWildcardType, $AnnotatedWildcardType$Type} from "packages/java/lang/reflect/$AnnotatedWildcardType"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$Either, $Either$Type} from "packages/com/mojang/datafixers/util/$Either"
import {$WildcardType as $WildcardType$0, $WildcardType$Type as $WildcardType$0$Type} from "packages/java/lang/reflect/$WildcardType"

export class $WildcardType extends $TypeDescriptor {
 "bound": $Optional<($Either<($TypeDescriptor), ($TypeDescriptor)>)>

constructor(wildcardType: $AnnotatedWildcardType$Type, checkBound: boolean)
constructor(wildcardType: $WildcardType$0$Type, checkBound: boolean)

public "stream"(): $Stream<($TypeDescriptor)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WildcardType$Type = ($WildcardType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WildcardType_ = $WildcardType$Type;
}}
declare module "packages/moe/wolfgirl/probejs/utils/$GameUtils" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $GameUtils {

constructor()

public static "registryHash"(): long
public static "modHash"(): long
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GameUtils$Type = ($GameUtils);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GameUtils_ = $GameUtils$Type;
}}
declare module "packages/moe/wolfgirl/probejs/docs/$Bindings" {
import {$ProbeJSPlugin, $ProbeJSPlugin$Type} from "packages/moe/wolfgirl/probejs/plugin/$ProbeJSPlugin"
import {$ScriptDump, $ScriptDump$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/$ScriptDump"

export class $Bindings extends $ProbeJSPlugin {

constructor()

public "addGlobals"(scriptDump: $ScriptDump$Type): void
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
declare module "packages/moe/wolfgirl/probejs/lang/java/clazz/$Clazz" {
import {$TypeDescriptor, $TypeDescriptor$Type} from "packages/moe/wolfgirl/probejs/lang/java/type/$TypeDescriptor"
import {$ClassPath, $ClassPath$Type} from "packages/moe/wolfgirl/probejs/lang/java/clazz/$ClassPath"
import {$Clazz$ClassAttribute, $Clazz$ClassAttribute$Type} from "packages/moe/wolfgirl/probejs/lang/java/clazz/$Clazz$ClassAttribute"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$FieldInfo, $FieldInfo$Type} from "packages/moe/wolfgirl/probejs/lang/java/clazz/members/$FieldInfo"
import {$VariableType, $VariableType$Type} from "packages/moe/wolfgirl/probejs/lang/java/type/impl/$VariableType"
import {$ConstructorInfo, $ConstructorInfo$Type} from "packages/moe/wolfgirl/probejs/lang/java/clazz/members/$ConstructorInfo"
import {$MethodInfo, $MethodInfo$Type} from "packages/moe/wolfgirl/probejs/lang/java/clazz/members/$MethodInfo"
import {$ClassPathProvider, $ClassPathProvider$Type} from "packages/moe/wolfgirl/probejs/lang/java/base/$ClassPathProvider"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$List, $List$Type} from "packages/java/util/$List"
import {$TypeVariableHolder, $TypeVariableHolder$Type} from "packages/moe/wolfgirl/probejs/lang/java/base/$TypeVariableHolder"

export class $Clazz extends $TypeVariableHolder implements $ClassPathProvider {
readonly "classPath": $ClassPath
readonly "constructors": $List<($ConstructorInfo)>
readonly "fields": $List<($FieldInfo)>
readonly "methods": $List<($MethodInfo)>
readonly "superClass": $TypeDescriptor
readonly "interfaces": $List<($TypeDescriptor)>
readonly "attribute": $Clazz$ClassAttribute
readonly "variableTypes": $List<($VariableType)>

constructor(clazz: $Class$Type<(any)>)

public "equals"(o: any): boolean
public "hashCode"(): integer
public "getUsedClasses"(): $Set<($ClassPath)>
public "getClassPaths"(): $Collection<($ClassPath)>
get "usedClasses"(): $Set<($ClassPath)>
get "classPaths"(): $Collection<($ClassPath)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Clazz$Type = ($Clazz);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Clazz_ = $Clazz$Type;
}}
declare module "packages/moe/wolfgirl/probejs/lang/typescript/code/type/$BaseType$FormatType" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $BaseType$FormatType extends $Enum<($BaseType$FormatType)> {
static readonly "INPUT": $BaseType$FormatType
static readonly "RETURN": $BaseType$FormatType
static readonly "VARIABLE": $BaseType$FormatType


public static "values"(): ($BaseType$FormatType)[]
public static "valueOf"(name: string): $BaseType$FormatType
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BaseType$FormatType$Type = (("input") | ("variable") | ("return")) | ($BaseType$FormatType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BaseType$FormatType_ = $BaseType$FormatType$Type;
}}
declare module "packages/moe/wolfgirl/probejs/lang/typescript/code/$Code" {
import {$ClassPath, $ClassPath$Type} from "packages/moe/wolfgirl/probejs/lang/java/clazz/$ClassPath"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Declaration, $Declaration$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/$Declaration"

export class $Code {

constructor()

public "format"(declaration: $Declaration$Type): $List<(string)>
public "line"(declaration: $Declaration$Type): string
public "getUsedClassPaths"(): $Collection<($ClassPath)>
get "usedClassPaths"(): $Collection<($ClassPath)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Code$Type = ($Code);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Code_ = $Code$Type;
}}
declare module "packages/moe/wolfgirl/probejs/lang/decompiler/remapper/$RemappedType" {
import {$RemappedClass, $RemappedClass$Type} from "packages/moe/wolfgirl/probejs/lang/decompiler/remapper/$RemappedClass"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"

export class $RemappedType {
readonly "parent": $RemappedClass
readonly "array": integer
 "realClass": $Optional<($Class<(any)>)>
 "descriptorString": string

constructor(parent: $RemappedClass$Type, array: integer)

public "equals"(obj: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "descriptorString"(): string
public "isRemapped"(): boolean
get "remapped"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RemappedType$Type = ($RemappedType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RemappedType_ = $RemappedType$Type;
}}
declare module "packages/moe/wolfgirl/probejs/docs/$ForgeEventDoc" {
import {$ProbeJSPlugin, $ProbeJSPlugin$Type} from "packages/moe/wolfgirl/probejs/plugin/$ProbeJSPlugin"
import {$ClassPath, $ClassPath$Type} from "packages/moe/wolfgirl/probejs/lang/java/clazz/$ClassPath"
import {$ScriptDump, $ScriptDump$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/$ScriptDump"
import {$Map, $Map$Type} from "packages/java/util/$Map"
import {$TypeScriptFile, $TypeScriptFile$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/$TypeScriptFile"

export class $ForgeEventDoc extends $ProbeJSPlugin {

constructor()

public "modifyClasses"(scriptDump: $ScriptDump$Type, globalClasses: $Map$Type<($ClassPath$Type), ($TypeScriptFile$Type)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForgeEventDoc$Type = ($ForgeEventDoc);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForgeEventDoc_ = $ForgeEventDoc$Type;
}}
declare module "packages/moe/wolfgirl/probejs/lang/java/type/impl/$ParamType" {
import {$TypeDescriptor, $TypeDescriptor$Type} from "packages/moe/wolfgirl/probejs/lang/java/type/$TypeDescriptor"
import {$Annotation, $Annotation$Type} from "packages/java/lang/annotation/$Annotation"
import {$ParameterizedType, $ParameterizedType$Type} from "packages/java/lang/reflect/$ParameterizedType"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$AnnotatedParameterizedType, $AnnotatedParameterizedType$Type} from "packages/java/lang/reflect/$AnnotatedParameterizedType"

export class $ParamType extends $TypeDescriptor {
 "base": $TypeDescriptor
readonly "params": $List<($TypeDescriptor)>

constructor(annotations: ($Annotation$Type)[], base: $TypeDescriptor$Type, params: $List$Type<($TypeDescriptor$Type)>)
constructor(parameterizedType: $ParameterizedType$Type)
constructor(annotatedType: $AnnotatedParameterizedType$Type)

public "hashCode"(): integer
public "stream"(): $Stream<($TypeDescriptor)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ParamType$Type = ($ParamType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ParamType_ = $ParamType$Type;
}}
declare module "packages/moe/wolfgirl/probejs/lang/transpiler/members/$Param" {
import {$Converter, $Converter$Type} from "packages/moe/wolfgirl/probejs/lang/transpiler/members/$Converter"
import {$ParamInfo, $ParamInfo$Type} from "packages/moe/wolfgirl/probejs/lang/java/clazz/members/$ParamInfo"
import {$TypeConverter, $TypeConverter$Type} from "packages/moe/wolfgirl/probejs/lang/transpiler/$TypeConverter"
import {$ParamDecl, $ParamDecl$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/code/member/$ParamDecl"

export class $Param extends $Converter<($ParamInfo), ($ParamDecl)> {

constructor(converter: $TypeConverter$Type)

public "transpile"(input: $ParamInfo$Type): $ParamDecl
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Param$Type = ($Param);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Param_ = $Param$Type;
}}
declare module "packages/moe/wolfgirl/probejs/lang/linter/$Linter$RuleFactory" {
import {$Rule, $Rule$Type} from "packages/moe/wolfgirl/probejs/lang/linter/rules/$Rule"

export interface $Linter$RuleFactory {

 "get"(): $Rule

(): $Rule
}

export namespace $Linter$RuleFactory {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Linter$RuleFactory$Type = ($Linter$RuleFactory);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Linter$RuleFactory_ = $Linter$RuleFactory$Type;
}}
declare module "packages/moe/wolfgirl/probejs/lang/typescript/code/member/$TypeDecl" {
import {$ClassPath, $ClassPath$Type} from "packages/moe/wolfgirl/probejs/lang/java/clazz/$ClassPath"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Declaration, $Declaration$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/$Declaration"
import {$CommentableCode, $CommentableCode$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/code/member/$CommentableCode"
import {$BaseType, $BaseType$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/code/type/$BaseType"

export class $TypeDecl extends $CommentableCode {
 "type": $BaseType
readonly "symbol": string
readonly "comments": $List<(string)>

constructor(symbol: string, type: $BaseType$Type)

public "formatRaw"(declaration: $Declaration$Type): $List<(string)>
public "getUsedClassPaths"(): $Collection<($ClassPath)>
get "usedClassPaths"(): $Collection<($ClassPath)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TypeDecl$Type = ($TypeDecl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TypeDecl_ = $TypeDecl$Type;
}}
declare module "packages/moe/wolfgirl/probejs/lang/typescript/code/member/$ConstructorDecl" {
import {$ClassPath, $ClassPath$Type} from "packages/moe/wolfgirl/probejs/lang/java/clazz/$ClassPath"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$List, $List$Type} from "packages/java/util/$List"
import {$TSVariableType, $TSVariableType$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/code/type/$TSVariableType"
import {$ParamDecl, $ParamDecl$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/code/member/$ParamDecl"
import {$Declaration, $Declaration$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/$Declaration"
import {$CommentableCode, $CommentableCode$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/code/member/$CommentableCode"

export class $ConstructorDecl extends $CommentableCode {
readonly "variableTypes": $List<($TSVariableType)>
readonly "params": $List<($ParamDecl)>
 "content": string
readonly "comments": $List<(string)>

constructor(variableTypes: $List$Type<($TSVariableType$Type)>, params: $List$Type<($ParamDecl$Type)>)

public "formatRaw"(declaration: $Declaration$Type): $List<(string)>
public "getUsedClassPaths"(): $Collection<($ClassPath)>
get "usedClassPaths"(): $Collection<($ClassPath)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ConstructorDecl$Type = ($ConstructorDecl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ConstructorDecl_ = $ConstructorDecl$Type;
}}
declare module "packages/moe/wolfgirl/probejs/lang/typescript/code/type/js/$JSJoinedType$Intersection" {
import {$List, $List$Type} from "packages/java/util/$List"
import {$JSJoinedType, $JSJoinedType$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/code/type/js/$JSJoinedType"
import {$BaseType, $BaseType$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/code/type/$BaseType"

export class $JSJoinedType$Intersection extends $JSJoinedType {
readonly "delimiter": string
readonly "types": $List<($BaseType)>

constructor(types: $List$Type<($BaseType$Type)>)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JSJoinedType$Intersection$Type = ($JSJoinedType$Intersection);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JSJoinedType$Intersection_ = $JSJoinedType$Intersection$Type;
}}
declare module "packages/moe/wolfgirl/probejs/lang/linter/rules/$NoNamespacePollution" {
import {$LintingWarning, $LintingWarning$Type} from "packages/moe/wolfgirl/probejs/lang/linter/$LintingWarning"
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Rule, $Rule$Type} from "packages/moe/wolfgirl/probejs/lang/linter/rules/$Rule"

export class $NoNamespacePollution extends $Rule {

constructor()

public "lint"(basePath: $Path$Type): $List<($LintingWarning)>
public "acceptFile"(path: $Path$Type, content: $List$Type<(string)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NoNamespacePollution$Type = ($NoNamespacePollution);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NoNamespacePollution_ = $NoNamespacePollution$Type;
}}
declare module "packages/moe/wolfgirl/probejs/$ProbeConfig$ConfigEntry" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $ProbeConfig$ConfigEntry<T> {
readonly "name": string
readonly "defaultValue": T


public "get"(): T
public "set"(value: T): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ProbeConfig$ConfigEntry$Type<T> = ($ProbeConfig$ConfigEntry<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ProbeConfig$ConfigEntry_<T> = $ProbeConfig$ConfigEntry$Type<(T)>;
}}
declare module "packages/moe/wolfgirl/probejs/lang/typescript/code/type/js/$JSObjectType$Builder" {
import {$JSObjectType, $JSObjectType$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/code/type/js/$JSObjectType"
import {$BaseType, $BaseType$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/code/type/$BaseType"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $JSObjectType$Builder {
readonly "members": $Map<(string), ($BaseType)>

constructor()

public "member"(name: string, type: $BaseType$Type): $JSObjectType$Builder
public "build"(): $JSObjectType
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JSObjectType$Builder$Type = ($JSObjectType$Builder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JSObjectType$Builder_ = $JSObjectType$Builder$Type;
}}
declare module "packages/moe/wolfgirl/probejs/lang/snippet/parts/$SnippetPart" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $SnippetPart {

 "format"(): string

(): string
}

export namespace $SnippetPart {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SnippetPart$Type = ($SnippetPart);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SnippetPart_ = $SnippetPart$Type;
}}
declare module "packages/moe/wolfgirl/probejs/features/bridge/$WebSocketServer" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $WebSocketServer {

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WebSocketServer$Type = ($WebSocketServer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WebSocketServer_ = $WebSocketServer$Type;
}}
declare module "packages/moe/wolfgirl/probejs/lang/typescript/code/ts/$VariableDeclaration" {
import {$ClassPath, $ClassPath$Type} from "packages/moe/wolfgirl/probejs/lang/java/clazz/$ClassPath"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Declaration, $Declaration$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/$Declaration"
import {$CommentableCode, $CommentableCode$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/code/member/$CommentableCode"
import {$BaseType, $BaseType$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/code/type/$BaseType"

export class $VariableDeclaration extends $CommentableCode {
 "symbol": string
 "type": $BaseType
readonly "comments": $List<(string)>

constructor(symbol: string, type: $BaseType$Type)

public "formatRaw"(declaration: $Declaration$Type): $List<(string)>
public "getUsedClassPaths"(): $Collection<($ClassPath)>
get "usedClassPaths"(): $Collection<($ClassPath)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $VariableDeclaration$Type = ($VariableDeclaration);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $VariableDeclaration_ = $VariableDeclaration$Type;
}}
declare module "packages/moe/wolfgirl/probejs/events/$TypingModificationEventJS" {
import {$ClassPath, $ClassPath$Type} from "packages/moe/wolfgirl/probejs/lang/java/clazz/$ClassPath"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$ScriptEventJS, $ScriptEventJS$Type} from "packages/moe/wolfgirl/probejs/events/$ScriptEventJS"
import {$ScriptDump, $ScriptDump$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/$ScriptDump"
import {$Map, $Map$Type} from "packages/java/util/$Map"
import {$TypeScriptFile, $TypeScriptFile$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/$TypeScriptFile"

export class $TypingModificationEventJS extends $ScriptEventJS {

constructor(dump: $ScriptDump$Type, files: $Map$Type<($ClassPath$Type), ($TypeScriptFile$Type)>)

public "modify"(clazz: $Class$Type<(any)>, file: $Consumer$Type<($TypeScriptFile$Type)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TypingModificationEventJS$Type = ($TypingModificationEventJS);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TypingModificationEventJS_ = $TypingModificationEventJS$Type;
}}
declare module "packages/moe/wolfgirl/probejs/docs/$ProbeBuiltinDocs" {
import {$ProbeJSPlugin, $ProbeJSPlugin$Type} from "packages/moe/wolfgirl/probejs/plugin/$ProbeJSPlugin"
import {$ClassPath, $ClassPath$Type} from "packages/moe/wolfgirl/probejs/lang/java/clazz/$ClassPath"
import {$SnippetDump, $SnippetDump$Type} from "packages/moe/wolfgirl/probejs/lang/snippet/$SnippetDump"
import {$TypeConverter, $TypeConverter$Type} from "packages/moe/wolfgirl/probejs/lang/transpiler/$TypeConverter"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$ScriptDump, $ScriptDump$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/$ScriptDump"
import {$Map, $Map$Type} from "packages/java/util/$Map"
import {$TypeScriptFile, $TypeScriptFile$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/$TypeScriptFile"
import {$Transpiler, $Transpiler$Type} from "packages/moe/wolfgirl/probejs/lang/transpiler/$Transpiler"

export class $ProbeBuiltinDocs extends $ProbeJSPlugin {
static readonly "INSTANCE": $ProbeBuiltinDocs
static readonly "BUILTIN_DOCS": $List<($Supplier<($ProbeJSPlugin)>)>

constructor()

public "modifyClasses"(scriptDump: $ScriptDump$Type, globalClasses: $Map$Type<($ClassPath$Type), ($TypeScriptFile$Type)>): void
public "addGlobals"(scriptDump: $ScriptDump$Type): void
public "assignType"(scriptDump: $ScriptDump$Type): void
public "addVSCodeSnippets"(dump: $SnippetDump$Type): void
public "addPredefinedTypes"(converter: $TypeConverter$Type): void
public "denyTypes"(transpiler: $Transpiler$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ProbeBuiltinDocs$Type = ($ProbeBuiltinDocs);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ProbeBuiltinDocs_ = $ProbeBuiltinDocs$Type;
}}
declare module "packages/moe/wolfgirl/probejs/lang/linter/$LintingWarning" {
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$LintingWarning$Level, $LintingWarning$Level$Type} from "packages/moe/wolfgirl/probejs/lang/linter/$LintingWarning$Level"

export class $LintingWarning extends $Record {

constructor(file: $Path$Type, level: $LintingWarning$Level$Type, line: integer, column: integer, message: string)

public "equals"(o: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "line"(): integer
public "file"(): $Path
public "message"(): string
public "level"(): $LintingWarning$Level
public "column"(): integer
public "defaultFormatting"(relativeBase: $Path$Type): $Component
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LintingWarning$Type = ($LintingWarning);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LintingWarning_ = $LintingWarning$Type;
}}
declare module "packages/moe/wolfgirl/probejs/lang/java/clazz/members/$FieldInfo$FieldAttributes" {
import {$Field, $Field$Type} from "packages/java/lang/reflect/$Field"

export class $FieldInfo$FieldAttributes {
readonly "isFinal": boolean
readonly "isStatic": boolean

constructor(field: $Field$Type)

public "getStaticValue"(): any
get "staticValue"(): any
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FieldInfo$FieldAttributes$Type = ($FieldInfo$FieldAttributes);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FieldInfo$FieldAttributes_ = $FieldInfo$FieldAttributes$Type;
}}
declare module "packages/moe/wolfgirl/probejs/lang/typescript/code/member/$ClassDecl$Builder" {
import {$MethodBuilder, $MethodBuilder$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/code/member/clazz/$MethodBuilder"
import {$FieldDecl, $FieldDecl$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/code/member/$FieldDecl"
import {$ConstructorDecl, $ConstructorDecl$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/code/member/$ConstructorDecl"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$List, $List$Type} from "packages/java/util/$List"
import {$TSVariableType, $TSVariableType$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/code/type/$TSVariableType"
import {$ConstructorBuilder, $ConstructorBuilder$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/code/member/clazz/$ConstructorBuilder"
import {$ClassDecl, $ClassDecl$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/code/member/$ClassDecl"
import {$BaseType, $BaseType$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/code/type/$BaseType"
import {$MethodDecl, $MethodDecl$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/code/member/$MethodDecl"

export class $ClassDecl$Builder {
readonly "name": string
 "superClass": $BaseType
readonly "interfaces": $List<($BaseType)>
readonly "variableTypes": $List<($TSVariableType)>
 "isAbstract": boolean
 "isInterface": boolean
readonly "fields": $List<($FieldDecl)>
readonly "constructors": $List<($ConstructorDecl)>
readonly "methods": $List<($MethodDecl)>

constructor(name: string)

public "abstractClass"(): $ClassDecl$Builder
public "method"(name: string, method: $Consumer$Type<($MethodBuilder$Type)>): $ClassDecl$Builder
public "interfaces"(...interfaces: ($BaseType$Type)[]): $ClassDecl$Builder
public "field"(symbol: string, baseType: $BaseType$Type, isStatic: boolean): $ClassDecl$Builder
public "field"(symbol: string, baseType: $BaseType$Type, isStatic: boolean, isFinal: boolean): $ClassDecl$Builder
public "field"(symbol: string, baseType: $BaseType$Type): $ClassDecl$Builder
public "superClass"(superClass: $BaseType$Type): $ClassDecl$Builder
public "ctor"(arg0: $Consumer$Type<($ConstructorBuilder$Type)>): $ClassDecl$Builder
public "build"(): $ClassDecl
public "interfaceClass"(): $ClassDecl$Builder
public "typeVariables"(...symbols: (string)[]): $ClassDecl$Builder
public "typeVariables"(...variableTypes: ($TSVariableType$Type)[]): $ClassDecl$Builder
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClassDecl$Builder$Type = ($ClassDecl$Builder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClassDecl$Builder_ = $ClassDecl$Builder$Type;
}}
declare module "packages/moe/wolfgirl/probejs/lang/decompiler/$ProbeFileSaver" {
import {$ClassPath, $ClassPath$Type} from "packages/moe/wolfgirl/probejs/lang/java/clazz/$ClassPath"
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$IResultSaver, $IResultSaver$Type} from "packages/org/jetbrains/java/decompiler/main/extern/$IResultSaver"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$Manifest, $Manifest$Type} from "packages/java/util/jar/$Manifest"
import {$ParsedDocument, $ParsedDocument$Type} from "packages/moe/wolfgirl/probejs/lang/decompiler/parser/$ParsedDocument"
import {$Runnable, $Runnable$Type} from "packages/java/lang/$Runnable"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $ProbeFileSaver implements $IResultSaver {
readonly "result": $Map<($ClassPath), ($ParsedDocument)>
 "classCount": integer

constructor()

public "getClasses"(): $Set<($Class<(any)>)>
public "writeTo"(base: $Path$Type): void
public "callback"(callback: $Runnable$Type): $ProbeFileSaver
public "copyFile"(source: string, path: string, entryName: string): void
public "copyEntry"(source: string, path: string, archiveName: string, entry: string): void
public "saveDirEntry"(path: string, archiveName: string, entryName: string): void
public "saveClassEntry"(path: string, archiveName: string, qualifiedName: string, entryName: string, content: string): void
public "createArchive"(path: string, archiveName: string, manifest: $Manifest$Type): void
public "closeArchive"(path: string, archiveName: string): void
public "saveClassFile"(path: string, qualifiedName: string, entryName: string, content: string, mapping: (integer)[]): void
public "saveFolder"(path: string): void
public "close"(): void
public "saveClassEntry"(arg0: string, arg1: string, arg2: string, arg3: string, arg4: string, arg5: (integer)[]): void
public "getCodeLineData"(arg0: (integer)[]): (byte)[]
get "classes"(): $Set<($Class<(any)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ProbeFileSaver$Type = ($ProbeFileSaver);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ProbeFileSaver_ = $ProbeFileSaver$Type;
}}
declare module "packages/moe/wolfgirl/probejs/lang/snippet/parts/$Variable" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$SnippetPart, $SnippetPart$Type} from "packages/moe/wolfgirl/probejs/lang/snippet/parts/$SnippetPart"

export class $Variable extends $Enum<($Variable)> implements $SnippetPart {
static readonly "LINE_COMMENT": $Variable
static readonly "BLOCK_COMMENT_END": $Variable
static readonly "BLOCK_COMMENT_START": $Variable
static readonly "UUID": $Variable
static readonly "RANDOM_HEX": $Variable
static readonly "RANDOM": $Variable
static readonly "CURRENT_TIMEZONE_OFFSET": $Variable
static readonly "CURRENT_SECONDS_UNIX": $Variable
static readonly "CURRENT_SECOND": $Variable
static readonly "CURRENT_MINUTE": $Variable
static readonly "CURRENT_HOUR": $Variable
static readonly "CURRENT_DAY_NAME_SHORT": $Variable
static readonly "CURRENT_DAY_NAME": $Variable
static readonly "CURRENT_DATE": $Variable
static readonly "CURRENT_MONTH_NAME_SHORT": $Variable
static readonly "CURRENT_MONTH_NAME": $Variable
static readonly "CURRENT_MONTH": $Variable
static readonly "CURRENT_YEAR_SHORT": $Variable
static readonly "CURRENT_YEAR": $Variable
static readonly "CURSOR_NUMBER": $Variable
static readonly "CURSOR_INDEX": $Variable
static readonly "WORKSPACE_FOLDER": $Variable
static readonly "WORKSPACE_NAME": $Variable
static readonly "CLIPBOARD": $Variable
static readonly "RELATIVE_FILEPATH": $Variable
static readonly "TM_FILEPATH": $Variable
static readonly "TM_DIRECTORY": $Variable
static readonly "TM_FILENAME_BASE": $Variable
static readonly "TM_FILENAME": $Variable
static readonly "TM_LINE_NUMBER": $Variable
static readonly "TM_LINE_INDEX": $Variable
static readonly "TM_CURRENT_WORD": $Variable
static readonly "TM_CURRENT_LINE": $Variable
static readonly "TM_SELECTED_TEXT": $Variable


public static "values"(): ($Variable)[]
public static "valueOf"(name: string): $Variable
public "format"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Variable$Type = (("cursor_index") | ("current_day_name_short") | ("tm_current_line") | ("block_comment_end") | ("current_month_name_short") | ("uuid") | ("clipboard") | ("random") | ("tm_line_index") | ("current_year_short") | ("current_year") | ("current_second") | ("line_comment") | ("workspace_folder") | ("workspace_name") | ("tm_filename_base") | ("tm_current_word") | ("current_hour") | ("tm_filepath") | ("tm_directory") | ("current_timezone_offset") | ("tm_selected_text") | ("relative_filepath") | ("current_date") | ("tm_filename") | ("current_seconds_unix") | ("current_minute") | ("random_hex") | ("current_month") | ("tm_line_number") | ("block_comment_start") | ("current_day_name") | ("current_month_name") | ("cursor_number")) | ($Variable);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Variable_ = $Variable$Type;
}}
declare module "packages/moe/wolfgirl/probejs/docs/assignments/$JavaPrimitives" {
import {$ProbeJSPlugin, $ProbeJSPlugin$Type} from "packages/moe/wolfgirl/probejs/plugin/$ProbeJSPlugin"
import {$ScriptDump, $ScriptDump$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/$ScriptDump"

export class $JavaPrimitives extends $ProbeJSPlugin {

constructor()

public "assignType"(scriptDump: $ScriptDump$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JavaPrimitives$Type = ($JavaPrimitives);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JavaPrimitives_ = $JavaPrimitives$Type;
}}
declare module "packages/moe/wolfgirl/probejs/docs/events/$RecipeEvents" {
import {$ProbeJSPlugin, $ProbeJSPlugin$Type} from "packages/moe/wolfgirl/probejs/plugin/$ProbeJSPlugin"
import {$ClassPath, $ClassPath$Type} from "packages/moe/wolfgirl/probejs/lang/java/clazz/$ClassPath"
import {$ScriptDump, $ScriptDump$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/$ScriptDump"
import {$Map, $Map$Type} from "packages/java/util/$Map"
import {$TypeScriptFile, $TypeScriptFile$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/$TypeScriptFile"

export class $RecipeEvents extends $ProbeJSPlugin {
static readonly "SHORTCUTS": $Map<(string), (string)>
static readonly "DOCUMENTED_RECIPES": $ClassPath

constructor()

public "modifyClasses"(scriptDump: $ScriptDump$Type, globalClasses: $Map$Type<($ClassPath$Type), ($TypeScriptFile$Type)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecipeEvents$Type = ($RecipeEvents);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecipeEvents_ = $RecipeEvents$Type;
}}
declare module "packages/moe/wolfgirl/probejs/$ProbeDump" {
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$ScriptDump, $ScriptDump$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/$ScriptDump"

export class $ProbeDump {
static readonly "SNIPPET_PATH": $Path
static readonly "CLASS_CACHE": $Path

constructor()

public "trigger"(p: $Consumer$Type<($Component$Type)>): void
public "cleanup"(p: $Consumer$Type<($Component$Type)>): void
public "defaultScripts"(): void
public "addScript"(dump: $ScriptDump$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ProbeDump$Type = ($ProbeDump);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ProbeDump_ = $ProbeDump$Type;
}}
declare module "packages/moe/wolfgirl/probejs/lang/typescript/code/ts/$Wrapped$Global" {
import {$List, $List$Type} from "packages/java/util/$List"
import {$Code, $Code$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/code/$Code"
import {$Declaration, $Declaration$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/$Declaration"
import {$Wrapped, $Wrapped$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/code/ts/$Wrapped"

export class $Wrapped$Global extends $Wrapped {
readonly "codes": $List<($Code)>
readonly "comments": $List<(string)>

constructor()

public "formatRaw"(declaration: $Declaration$Type): $List<(string)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Wrapped$Global$Type = ($Wrapped$Global);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Wrapped$Global_ = $Wrapped$Global$Type;
}}
declare module "packages/moe/wolfgirl/probejs/docs/$Events" {
import {$ProbeJSPlugin, $ProbeJSPlugin$Type} from "packages/moe/wolfgirl/probejs/plugin/$ProbeJSPlugin"
import {$ScriptDump, $ScriptDump$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/$ScriptDump"

export class $Events extends $ProbeJSPlugin {

constructor()

public "addGlobals"(scriptDump: $ScriptDump$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Events$Type = ($Events);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Events_ = $Events$Type;
}}
declare module "packages/moe/wolfgirl/probejs/lang/typescript/code/type/js/$JSObjectType" {
import {$ClassPath, $ClassPath$Type} from "packages/moe/wolfgirl/probejs/lang/java/clazz/$ClassPath"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Declaration, $Declaration$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/$Declaration"
import {$BaseType$FormatType, $BaseType$FormatType$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/code/type/$BaseType$FormatType"
import {$BaseType, $BaseType$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/code/type/$BaseType"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $JSObjectType extends $BaseType {
readonly "members": $Map<(string), ($BaseType)>

constructor(members: $Map$Type<(string), ($BaseType$Type)>)

public "format"(declaration: $Declaration$Type, input: $BaseType$FormatType$Type): $List<(string)>
public "getUsedClassPaths"(): $Collection<($ClassPath)>
get "usedClassPaths"(): $Collection<($ClassPath)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JSObjectType$Type = ($JSObjectType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JSObjectType_ = $JSObjectType$Type;
}}
declare module "packages/moe/wolfgirl/probejs/lang/typescript/$TypeScriptFile" {
import {$ClassPath, $ClassPath$Type} from "packages/moe/wolfgirl/probejs/lang/java/clazz/$ClassPath"
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$Code, $Code$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/code/$Code"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$Declaration, $Declaration$Type} from "packages/moe/wolfgirl/probejs/lang/typescript/$Declaration"
import {$BufferedWriter, $BufferedWriter$Type} from "packages/java/io/$BufferedWriter"

export class $TypeScriptFile {
readonly "declaration": $Declaration
readonly "codeList": $List<($Code)>
readonly "classPath": $ClassPath

constructor(self: $ClassPath$Type)

public "writeAsModule"(writer: $BufferedWriter$Type): void
public "findCode"<T extends $Code>(type: $Class$Type<(T)>): $Optional<(T)>
public "excludeSymbol"(name: string): void
public "format"(): string
public "write"(writeTo: $Path$Type): void
public "write"(writer: $BufferedWriter$Type): void
public "addCode"(code: $Code$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TypeScriptFile$Type = ($TypeScriptFile);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TypeScriptFile_ = $TypeScriptFile$Type;
}}

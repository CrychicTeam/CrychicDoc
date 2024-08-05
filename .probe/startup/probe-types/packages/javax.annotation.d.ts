declare module "packages/javax/annotation/processing/$Filer" {
import {$Element, $Element$Type} from "packages/javax/lang/model/element/$Element"
import {$FileObject, $FileObject$Type} from "packages/javax/tools/$FileObject"
import {$JavaFileObject, $JavaFileObject$Type} from "packages/javax/tools/$JavaFileObject"
import {$JavaFileManager$Location, $JavaFileManager$Location$Type} from "packages/javax/tools/$JavaFileManager$Location"

export interface $Filer {

 "createResource"(arg0: $JavaFileManager$Location$Type, arg1: charseq, arg2: charseq, ...arg3: ($Element$Type)[]): $FileObject
 "getResource"(arg0: $JavaFileManager$Location$Type, arg1: charseq, arg2: charseq): $FileObject
 "createClassFile"(arg0: charseq, ...arg1: ($Element$Type)[]): $JavaFileObject
 "createSourceFile"(arg0: charseq, ...arg1: ($Element$Type)[]): $JavaFileObject
}

export namespace $Filer {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Filer$Type = ($Filer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Filer_ = $Filer$Type;
}}
declare module "packages/javax/annotation/processing/$RoundEnvironment" {
import {$TypeElement, $TypeElement$Type} from "packages/javax/lang/model/element/$TypeElement"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$Class, $Class$Type} from "packages/java/lang/$Class"

export interface $RoundEnvironment {

 "getElementsAnnotatedWithAny"(...arg0: ($TypeElement$Type)[]): $Set<(any)>
 "getElementsAnnotatedWithAny"(arg0: $Set$Type<($Class$Type<(any)>)>): $Set<(any)>
 "getRootElements"(): $Set<(any)>
 "errorRaised"(): boolean
 "processingOver"(): boolean
 "getElementsAnnotatedWith"(arg0: $Class$Type<(any)>): $Set<(any)>
 "getElementsAnnotatedWith"(arg0: $TypeElement$Type): $Set<(any)>
}

export namespace $RoundEnvironment {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RoundEnvironment$Type = ($RoundEnvironment);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RoundEnvironment_ = $RoundEnvironment$Type;
}}
declare module "packages/javax/annotation/processing/$Messager" {
import {$AnnotationMirror, $AnnotationMirror$Type} from "packages/javax/lang/model/element/$AnnotationMirror"
import {$AnnotationValue, $AnnotationValue$Type} from "packages/javax/lang/model/element/$AnnotationValue"
import {$Element, $Element$Type} from "packages/javax/lang/model/element/$Element"
import {$Diagnostic$Kind, $Diagnostic$Kind$Type} from "packages/javax/tools/$Diagnostic$Kind"

export interface $Messager {

 "printMessage"(arg0: $Diagnostic$Kind$Type, arg1: charseq, arg2: $Element$Type, arg3: $AnnotationMirror$Type, arg4: $AnnotationValue$Type): void
 "printMessage"(arg0: $Diagnostic$Kind$Type, arg1: charseq, arg2: $Element$Type, arg3: $AnnotationMirror$Type): void
 "printMessage"(arg0: $Diagnostic$Kind$Type, arg1: charseq, arg2: $Element$Type): void
 "printMessage"(arg0: $Diagnostic$Kind$Type, arg1: charseq): void
}

export namespace $Messager {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Messager$Type = ($Messager);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Messager_ = $Messager$Type;
}}
declare module "packages/javax/annotation/processing/$AbstractProcessor" {
import {$SourceVersion, $SourceVersion$Type} from "packages/javax/lang/model/$SourceVersion"
import {$AnnotationMirror, $AnnotationMirror$Type} from "packages/javax/lang/model/element/$AnnotationMirror"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$Element, $Element$Type} from "packages/javax/lang/model/element/$Element"
import {$RoundEnvironment, $RoundEnvironment$Type} from "packages/javax/annotation/processing/$RoundEnvironment"
import {$Processor, $Processor$Type} from "packages/javax/annotation/processing/$Processor"
import {$ProcessingEnvironment, $ProcessingEnvironment$Type} from "packages/javax/annotation/processing/$ProcessingEnvironment"
import {$Iterable, $Iterable$Type} from "packages/java/lang/$Iterable"
import {$ExecutableElement, $ExecutableElement$Type} from "packages/javax/lang/model/element/$ExecutableElement"

export class $AbstractProcessor implements $Processor {


public "init"(arg0: $ProcessingEnvironment$Type): void
public "process"(arg0: $Set$Type<(any)>, arg1: $RoundEnvironment$Type): boolean
public "getSupportedAnnotationTypes"(): $Set<(string)>
public "getSupportedSourceVersion"(): $SourceVersion
public "getSupportedOptions"(): $Set<(string)>
public "getCompletions"(arg0: $Element$Type, arg1: $AnnotationMirror$Type, arg2: $ExecutableElement$Type, arg3: string): $Iterable<(any)>
get "supportedAnnotationTypes"(): $Set<(string)>
get "supportedSourceVersion"(): $SourceVersion
get "supportedOptions"(): $Set<(string)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AbstractProcessor$Type = ($AbstractProcessor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AbstractProcessor_ = $AbstractProcessor$Type;
}}
declare module "packages/javax/annotation/processing/$ProcessingEnvironment" {
import {$SourceVersion, $SourceVersion$Type} from "packages/javax/lang/model/$SourceVersion"
import {$Filer, $Filer$Type} from "packages/javax/annotation/processing/$Filer"
import {$Types, $Types$Type} from "packages/javax/lang/model/util/$Types"
import {$Messager, $Messager$Type} from "packages/javax/annotation/processing/$Messager"
import {$Elements, $Elements$Type} from "packages/javax/lang/model/util/$Elements"
import {$Locale, $Locale$Type} from "packages/java/util/$Locale"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export interface $ProcessingEnvironment {

 "getFiler"(): $Filer
 "getElementUtils"(): $Elements
 "getLocale"(): $Locale
 "getMessager"(): $Messager
 "getOptions"(): $Map<(string), (string)>
 "getSourceVersion"(): $SourceVersion
 "getTypeUtils"(): $Types
 "isPreviewEnabled"(): boolean
}

export namespace $ProcessingEnvironment {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ProcessingEnvironment$Type = ($ProcessingEnvironment);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ProcessingEnvironment_ = $ProcessingEnvironment$Type;
}}
declare module "packages/javax/annotation/processing/$Processor" {
import {$SourceVersion, $SourceVersion$Type} from "packages/javax/lang/model/$SourceVersion"
import {$AnnotationMirror, $AnnotationMirror$Type} from "packages/javax/lang/model/element/$AnnotationMirror"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$Element, $Element$Type} from "packages/javax/lang/model/element/$Element"
import {$RoundEnvironment, $RoundEnvironment$Type} from "packages/javax/annotation/processing/$RoundEnvironment"
import {$ProcessingEnvironment, $ProcessingEnvironment$Type} from "packages/javax/annotation/processing/$ProcessingEnvironment"
import {$Iterable, $Iterable$Type} from "packages/java/lang/$Iterable"
import {$ExecutableElement, $ExecutableElement$Type} from "packages/javax/lang/model/element/$ExecutableElement"

export interface $Processor {

 "init"(arg0: $ProcessingEnvironment$Type): void
 "process"(arg0: $Set$Type<(any)>, arg1: $RoundEnvironment$Type): boolean
 "getSupportedAnnotationTypes"(): $Set<(string)>
 "getSupportedSourceVersion"(): $SourceVersion
 "getSupportedOptions"(): $Set<(string)>
 "getCompletions"(arg0: $Element$Type, arg1: $AnnotationMirror$Type, arg2: $ExecutableElement$Type, arg3: string): $Iterable<(any)>
}

export namespace $Processor {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Processor$Type = ($Processor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Processor_ = $Processor$Type;
}}

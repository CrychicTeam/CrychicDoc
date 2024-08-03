declare module "packages/ca/fxco/memoryleakfix/config/$RemapTarget" {
import {$Annotation, $Annotation$Type} from "packages/java/lang/annotation/$Annotation"
import {$Remap, $Remap$Type} from "packages/ca/fxco/memoryleakfix/config/$Remap"
import {$Class, $Class$Type} from "packages/java/lang/$Class"

export interface $RemapTarget extends $Annotation {

 "value"(): $Remap
 "target"(): $Remap
 "equals"(arg0: any): boolean
 "toString"(): string
 "hashCode"(): integer
 "annotationType"(): $Class<(any)>
}

export namespace $RemapTarget {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RemapTarget$Type = ($RemapTarget);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RemapTarget_ = $RemapTarget$Type;
}}
declare module "packages/ca/fxco/memoryleakfix/mixinextras/sugar/impl/$SugarMixinTransformer" {
import {$IMixinInfo, $IMixinInfo$Type} from "packages/org/spongepowered/asm/mixin/extensibility/$IMixinInfo"
import {$ClassNode, $ClassNode$Type} from "packages/org/objectweb/asm/tree/$ClassNode"
import {$MixinTransformer, $MixinTransformer$Type} from "packages/ca/fxco/memoryleakfix/mixinextras/transformer/$MixinTransformer"

export class $SugarMixinTransformer implements $MixinTransformer {

constructor()

public "transform"(mixinInfo: $IMixinInfo$Type, mixinNode: $ClassNode$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SugarMixinTransformer$Type = ($SugarMixinTransformer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SugarMixinTransformer_ = $SugarMixinTransformer$Type;
}}
declare module "packages/ca/fxco/memoryleakfix/$MemoryLeakFixBootstrap" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $MemoryLeakFixBootstrap {

constructor()

public static "init"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MemoryLeakFixBootstrap$Type = ($MemoryLeakFixBootstrap);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MemoryLeakFixBootstrap_ = $MemoryLeakFixBootstrap$Type;
}}
declare module "packages/ca/fxco/memoryleakfix/mixinextras/service/$Versioned" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $Versioned<T> {


}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Versioned$Type<T> = ($Versioned<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Versioned_<T> = $Versioned$Type<(T)>;
}}
declare module "packages/ca/fxco/memoryleakfix/mixinextras/injector/$WrapWithConditionInjectionInfo" {
import {$AnnotationNode, $AnnotationNode$Type} from "packages/org/objectweb/asm/tree/$AnnotationNode"
import {$MixinExtrasInjectionInfo, $MixinExtrasInjectionInfo$Type} from "packages/ca/fxco/memoryleakfix/mixinextras/injector/$MixinExtrasInjectionInfo"
import {$MethodNode, $MethodNode$Type} from "packages/org/objectweb/asm/tree/$MethodNode"
import {$MixinTargetContext, $MixinTargetContext$Type} from "packages/org/spongepowered/asm/mixin/transformer/$MixinTargetContext"

export class $WrapWithConditionInjectionInfo extends $MixinExtrasInjectionInfo {
static readonly "DEFAULT_PREFIX": string

constructor(mixin: $MixinTargetContext$Type, method: $MethodNode$Type, annotation: $AnnotationNode$Type)

public "prepare"(): void
public "getMethod"(): $MethodNode
public "getAnnotationNode"(): $AnnotationNode
get "method"(): $MethodNode
get "annotationNode"(): $AnnotationNode
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WrapWithConditionInjectionInfo$Type = ($WrapWithConditionInjectionInfo);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WrapWithConditionInjectionInfo_ = $WrapWithConditionInjectionInfo$Type;
}}
declare module "packages/ca/fxco/memoryleakfix/mixinextras/lib/apache/commons/tuple/$Pair" {
import {$Comparator, $Comparator$Type} from "packages/java/util/$Comparator"
import {$Comparable, $Comparable$Type} from "packages/java/lang/$Comparable"
import {$Serializable, $Serializable$Type} from "packages/java/io/$Serializable"
import {$Map$Entry, $Map$Entry$Type} from "packages/java/util/$Map$Entry"

export class $Pair<L, R> implements $Serializable, $Comparable<($Pair<(L), (R)>)>, $Map$Entry<(L), (R)> {

constructor()

public "equals"(obj: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "compareTo"(other: $Pair$Type<(L), (R)>): integer
public "getValue"(): R
public static "of"<L, R>(left: L, right: R): $Pair<(L), (R)>
public "getKey"(): L
public "getRight"(): R
public "getLeft"(): L
public static "copyOf"<K, V>(arg0: $Map$Entry$Type<(any), (any)>): $Map$Entry<(L), (R)>
public "setValue"(arg0: R): R
public static "comparingByKey"<K, V>(arg0: $Comparator$Type<(any)>): $Comparator<($Map$Entry<(L), (R)>)>
public static "comparingByKey"<K extends $Comparable<(any)>, V>(): $Comparator<($Map$Entry<(L), (R)>)>
public static "comparingByValue"<K, V extends $Comparable<(any)>>(): $Comparator<($Map$Entry<(L), (R)>)>
public static "comparingByValue"<K, V>(arg0: $Comparator$Type<(any)>): $Comparator<($Map$Entry<(L), (R)>)>
get "value"(): R
get "key"(): L
get "right"(): R
get "left"(): L
set "value"(value: R)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Pair$Type<L, R> = ($Pair<(L), (R)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Pair_<L, R> = $Pair$Type<(L), (R)>;
}}
declare module "packages/ca/fxco/memoryleakfix/mixinextras/utils/$MixinInternals" {
import {$ClassNode, $ClassNode$Type} from "packages/org/objectweb/asm/tree/$ClassNode"
import {$Target, $Target$Type} from "packages/org/spongepowered/asm/mixin/injection/struct/$Target"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$Pair, $Pair$Type} from "packages/ca/fxco/memoryleakfix/mixinextras/lib/apache/commons/tuple/$Pair"
import {$Injector, $Injector$Type} from "packages/org/spongepowered/asm/mixin/injection/code/$Injector"
import {$InjectionNodes$InjectionNode, $InjectionNodes$InjectionNode$Type} from "packages/org/spongepowered/asm/mixin/injection/struct/$InjectionNodes$InjectionNode"
import {$Extensions, $Extensions$Type} from "packages/org/spongepowered/asm/mixin/transformer/ext/$Extensions"
import {$IMixinInfo, $IMixinInfo$Type} from "packages/org/spongepowered/asm/mixin/extensibility/$IMixinInfo"
import {$List, $List$Type} from "packages/java/util/$List"
import {$InjectionInfo, $InjectionInfo$Type} from "packages/org/spongepowered/asm/mixin/injection/struct/$InjectionInfo"
import {$ITargetClassContext, $ITargetClassContext$Type} from "packages/org/spongepowered/asm/mixin/transformer/ext/$ITargetClassContext"
import {$IExtension, $IExtension$Type} from "packages/org/spongepowered/asm/mixin/transformer/ext/$IExtension"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $MixinInternals {

constructor()

public static "getExtensions"(): $Extensions
public static "registerExtension"(extension: $IExtension$Type): void
public static "registerExtension"(extension: $IExtension$Type, isPriority: boolean): void
public static "unregisterExtension"(extension: $IExtension$Type): void
public static "getMixinsFor"(context: $ITargetClassContext$Type): $List<($Pair<($IMixinInfo), ($ClassNode)>)>
public static "getTargets"(info: $InjectionInfo$Type): $Map<($Target), ($List<($InjectionNodes$InjectionNode)>)>
public static "registerInjector"(annotationType: string, type: $Class$Type<(any)>): void
public static "unregisterInjector"(annotationType: string): void
public static "getDecorations"(node: $InjectionNodes$InjectionNode$Type): $Map<(string), (any)>
public static "getInjector"(info: $InjectionInfo$Type): $Injector
public static "registerClassInfo"(classNode: $ClassNode$Type): void
get "extensions"(): $Extensions
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MixinInternals$Type = ($MixinInternals);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MixinInternals_ = $MixinInternals$Type;
}}
declare module "packages/ca/fxco/memoryleakfix/mixinextras/sugar/impl/$SugarParameter" {
import {$AnnotationNode, $AnnotationNode$Type} from "packages/org/objectweb/asm/tree/$AnnotationNode"
import {$Type, $Type$Type} from "packages/org/objectweb/asm/$Type"

export class $SugarParameter {
readonly "sugar": $AnnotationNode
readonly "type": $Type
readonly "genericType": $Type
readonly "lvtIndex": integer
readonly "paramIndex": integer


}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SugarParameter$Type = ($SugarParameter);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SugarParameter_ = $SugarParameter$Type;
}}
declare module "packages/ca/fxco/memoryleakfix/mixinextras/versions/$MixinVersionImpl_v0_8" {
import {$AnnotationNode, $AnnotationNode$Type} from "packages/org/objectweb/asm/tree/$AnnotationNode"
import {$LocalVariableDiscriminator$Context, $LocalVariableDiscriminator$Context$Type} from "packages/org/spongepowered/asm/mixin/injection/modify/$LocalVariableDiscriminator$Context"
import {$Target, $Target$Type} from "packages/org/spongepowered/asm/mixin/injection/struct/$Target"
import {$IMixinContext, $IMixinContext$Type} from "packages/org/spongepowered/asm/mixin/refmap/$IMixinContext"
import {$InjectionInfo, $InjectionInfo$Type} from "packages/org/spongepowered/asm/mixin/injection/struct/$InjectionInfo"
import {$RuntimeException, $RuntimeException$Type} from "packages/java/lang/$RuntimeException"
import {$Type, $Type$Type} from "packages/org/objectweb/asm/$Type"
import {$AbstractInsnNode, $AbstractInsnNode$Type} from "packages/org/objectweb/asm/tree/$AbstractInsnNode"
import {$MixinVersion, $MixinVersion$Type} from "packages/ca/fxco/memoryleakfix/mixinextras/versions/$MixinVersion"

export class $MixinVersionImpl_v0_8 extends $MixinVersion {

constructor()

public "getAnnotation"(info: $InjectionInfo$Type): $AnnotationNode
public "makeInvalidInjectionException"(info: $InjectionInfo$Type, message: string): $RuntimeException
public "preInject"(info: $InjectionInfo$Type): void
public "makeLvtContext"(info: $InjectionInfo$Type, returnType: $Type$Type, argsOnly: boolean, target: $Target$Type, node: $AbstractInsnNode$Type): $LocalVariableDiscriminator$Context
public "getMixin"(info: $InjectionInfo$Type): $IMixinContext
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MixinVersionImpl_v0_8$Type = ($MixinVersionImpl_v0_8);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MixinVersionImpl_v0_8_ = $MixinVersionImpl_v0_8$Type;
}}
declare module "packages/ca/fxco/memoryleakfix/mixinextras/utils/$Blackboard" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $Blackboard {

constructor()

public static "get"<T>(key: string): T
public static "put"(key: string, value: any): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Blackboard$Type = ($Blackboard);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Blackboard_ = $Blackboard$Type;
}}
declare module "packages/ca/fxco/memoryleakfix/mixinextras/sugar/impl/$SugarPostProcessingExtension" {
import {$ClassNode, $ClassNode$Type} from "packages/org/objectweb/asm/tree/$ClassNode"
import {$MixinEnvironment, $MixinEnvironment$Type} from "packages/org/spongepowered/asm/mixin/$MixinEnvironment"
import {$ITargetClassContext, $ITargetClassContext$Type} from "packages/org/spongepowered/asm/mixin/transformer/ext/$ITargetClassContext"
import {$IExtension, $IExtension$Type} from "packages/org/spongepowered/asm/mixin/transformer/ext/$IExtension"

export class $SugarPostProcessingExtension implements $IExtension {

constructor()

public "export"(env: $MixinEnvironment$Type, name: string, force: boolean, classNode: $ClassNode$Type): void
public "postApply"(context: $ITargetClassContext$Type): void
public "preApply"(context: $ITargetClassContext$Type): void
public "checkActive"(environment: $MixinEnvironment$Type): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SugarPostProcessingExtension$Type = ($SugarPostProcessingExtension);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SugarPostProcessingExtension_ = $SugarPostProcessingExtension$Type;
}}
declare module "packages/ca/fxco/memoryleakfix/mixinextras/service/$MixinExtrasService" {
import {$MixinExtrasServiceImpl, $MixinExtrasServiceImpl$Type} from "packages/ca/fxco/memoryleakfix/mixinextras/service/$MixinExtrasServiceImpl"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$IExtension, $IExtension$Type} from "packages/org/spongepowered/asm/mixin/transformer/ext/$IExtension"

export interface $MixinExtrasService {

 "initialize"(): void
 "getVersion"(): integer
 "takeControlFrom"(arg0: any): void
 "offerExtension"(arg0: integer, arg1: $IExtension$Type): void
 "offerPackage"(arg0: integer, arg1: string): void
 "concedeTo"(arg0: any, arg1: boolean): void
 "offerInjector"(arg0: integer, arg1: $Class$Type<(any)>): void
 "shouldReplace"(arg0: any): boolean
}

export namespace $MixinExtrasService {
function getInstance(): $MixinExtrasServiceImpl
function setup(): void
function getFrom(serviceImpl: any): $MixinExtrasService
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MixinExtrasService$Type = ($MixinExtrasService);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MixinExtrasService_ = $MixinExtrasService$Type;
}}
declare module "packages/ca/fxco/memoryleakfix/mixinextras/wrapper/factory/$FactoryRedirectWrapperInjectionInfo" {
import {$AnnotationNode, $AnnotationNode$Type} from "packages/org/objectweb/asm/tree/$AnnotationNode"
import {$MethodNode, $MethodNode$Type} from "packages/org/objectweb/asm/tree/$MethodNode"
import {$WrapperInjectionInfo, $WrapperInjectionInfo$Type} from "packages/ca/fxco/memoryleakfix/mixinextras/wrapper/$WrapperInjectionInfo"
import {$LateApplyingInjectorInfo, $LateApplyingInjectorInfo$Type} from "packages/ca/fxco/memoryleakfix/mixinextras/injector/$LateApplyingInjectorInfo"
import {$MixinTargetContext, $MixinTargetContext$Type} from "packages/org/spongepowered/asm/mixin/transformer/$MixinTargetContext"

export class $FactoryRedirectWrapperInjectionInfo extends $WrapperInjectionInfo {
static readonly "DEFAULT_PREFIX": string

constructor(mixin: $MixinTargetContext$Type, method: $MethodNode$Type, annotation: $AnnotationNode$Type)

public static "wrap"(inner: any, outer: $LateApplyingInjectorInfo$Type): boolean
public "getMethod"(): $MethodNode
public "getAnnotationNode"(): $AnnotationNode
get "method"(): $MethodNode
get "annotationNode"(): $AnnotationNode
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FactoryRedirectWrapperInjectionInfo$Type = ($FactoryRedirectWrapperInjectionInfo);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FactoryRedirectWrapperInjectionInfo_ = $FactoryRedirectWrapperInjectionInfo$Type;
}}
declare module "packages/ca/fxco/memoryleakfix/config/$MinecraftRequirement" {
import {$Annotation, $Annotation$Type} from "packages/java/lang/annotation/$Annotation"
import {$VersionRange, $VersionRange$Type} from "packages/ca/fxco/memoryleakfix/config/$VersionRange"
import {$Class, $Class$Type} from "packages/java/lang/$Class"

export interface $MinecraftRequirement extends $Annotation {

 "value"(): ($VersionRange)[]
 "equals"(arg0: any): boolean
 "toString"(): string
 "hashCode"(): integer
 "annotationType"(): $Class<(any)>
}

export namespace $MinecraftRequirement {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MinecraftRequirement$Type = ($MinecraftRequirement);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MinecraftRequirement_ = $MinecraftRequirement$Type;
}}
declare module "packages/ca/fxco/memoryleakfix/config/$VersionRange" {
import {$Annotation, $Annotation$Type} from "packages/java/lang/annotation/$Annotation"
import {$Class, $Class$Type} from "packages/java/lang/$Class"

export interface $VersionRange extends $Annotation {

 "minVersion"(): string
 "maxVersion"(): string
 "equals"(arg0: any): boolean
 "toString"(): string
 "hashCode"(): integer
 "annotationType"(): $Class<(any)>
}

export namespace $VersionRange {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $VersionRange$Type = ($VersionRange);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $VersionRange_ = $VersionRange$Type;
}}
declare module "packages/ca/fxco/memoryleakfix/config/$Remap" {
import {$Annotation, $Annotation$Type} from "packages/java/lang/annotation/$Annotation"
import {$MinecraftRequirement, $MinecraftRequirement$Type} from "packages/ca/fxco/memoryleakfix/config/$MinecraftRequirement"
import {$Class, $Class$Type} from "packages/java/lang/$Class"

export interface $Remap extends $Annotation {

 "mcp"(): (string)[]
 "forge"(): (string)[]
 "fabric"(): (string)[]
 "mcVersions"(): $MinecraftRequirement
 "excludeDev"(): boolean
 "equals"(arg0: any): boolean
 "toString"(): string
 "hashCode"(): integer
 "annotationType"(): $Class<(any)>
}

export namespace $Remap {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Remap$Type = ($Remap);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Remap_ = $Remap$Type;
}}
declare module "packages/ca/fxco/memoryleakfix/mixinextras/sugar/$Local" {
import {$Annotation, $Annotation$Type} from "packages/java/lang/annotation/$Annotation"
import {$Class, $Class$Type} from "packages/java/lang/$Class"

export interface $Local extends $Annotation {

 "index"(): integer
 "name"(): (string)[]
 "print"(): boolean
 "ordinal"(): integer
 "argsOnly"(): boolean
 "equals"(arg0: any): boolean
 "toString"(): string
 "hashCode"(): integer
 "annotationType"(): $Class<(any)>
}

export namespace $Local {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Local$Type = ($Local);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Local_ = $Local$Type;
}}
declare module "packages/ca/fxco/memoryleakfix/mixinextras/wrapper/factory/$FactoryRedirectWrapperImpl" {
import {$InjectorWrapperImpl, $InjectorWrapperImpl$Type} from "packages/ca/fxco/memoryleakfix/mixinextras/wrapper/$InjectorWrapperImpl"

export class $FactoryRedirectWrapperImpl extends $InjectorWrapperImpl {


}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FactoryRedirectWrapperImpl$Type = ($FactoryRedirectWrapperImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FactoryRedirectWrapperImpl_ = $FactoryRedirectWrapperImpl$Type;
}}
declare module "packages/ca/fxco/memoryleakfix/forge/$MemoryLeakFixExpectPlatformImpl" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $MemoryLeakFixExpectPlatformImpl {

constructor()

public static "compareMinecraftToVersion"(version: string): integer
public static "getMappingType"(): string
public static "isDevEnvironment"(): boolean
public static "isModLoaded"(id: string): boolean
get "mappingType"(): string
get "devEnvironment"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MemoryLeakFixExpectPlatformImpl$Type = ($MemoryLeakFixExpectPlatformImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MemoryLeakFixExpectPlatformImpl_ = $MemoryLeakFixExpectPlatformImpl$Type;
}}
declare module "packages/ca/fxco/memoryleakfix/mixinextras/sugar/ref/$LocalDoubleRef" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $LocalDoubleRef {

 "get"(): double
 "set"(arg0: double): void
}

export namespace $LocalDoubleRef {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LocalDoubleRef$Type = ($LocalDoubleRef);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LocalDoubleRef_ = $LocalDoubleRef$Type;
}}
declare module "packages/ca/fxco/memoryleakfix/mixinextras/ap/$MixinExtrasAP" {
import {$SourceVersion, $SourceVersion$Type} from "packages/javax/lang/model/$SourceVersion"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$RoundEnvironment, $RoundEnvironment$Type} from "packages/javax/annotation/processing/$RoundEnvironment"
import {$AbstractProcessor, $AbstractProcessor$Type} from "packages/javax/annotation/processing/$AbstractProcessor"

export class $MixinExtrasAP extends $AbstractProcessor {

constructor()

public "process"(annotations: $Set$Type<(any)>, roundEnv: $RoundEnvironment$Type): boolean
public "getSupportedSourceVersion"(): $SourceVersion
get "supportedSourceVersion"(): $SourceVersion
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MixinExtrasAP$Type = ($MixinExtrasAP);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MixinExtrasAP_ = $MixinExtrasAP$Type;
}}
declare module "packages/ca/fxco/memoryleakfix/mixinextras/utils/$ClassGenUtils" {
import {$ClassNode, $ClassNode$Type} from "packages/org/objectweb/asm/tree/$ClassNode"
import {$MethodHandles$Lookup, $MethodHandles$Lookup$Type} from "packages/java/lang/invoke/$MethodHandles$Lookup"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $ClassGenUtils {

constructor()

public static "defineClass"(node: $ClassNode$Type, scope: $MethodHandles$Lookup$Type): void
public static "getDefinitions"(): $Map<(string), ((byte)[])>
get "definitions"(): $Map<(string), ((byte)[])>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClassGenUtils$Type = ($ClassGenUtils);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClassGenUtils_ = $ClassGenUtils$Type;
}}
declare module "packages/ca/fxco/memoryleakfix/config/$MemoryLeakFixMixinConfigPlugin" {
import {$IMixinInfo, $IMixinInfo$Type} from "packages/org/spongepowered/asm/mixin/extensibility/$IMixinInfo"
import {$ClassNode, $ClassNode$Type} from "packages/org/objectweb/asm/tree/$ClassNode"
import {$IMixinConfigPlugin, $IMixinConfigPlugin$Type} from "packages/org/spongepowered/asm/mixin/extensibility/$IMixinConfigPlugin"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$List, $List$Type} from "packages/java/util/$List"

export class $MemoryLeakFixMixinConfigPlugin implements $IMixinConfigPlugin {

constructor()

public "onLoad"(mixinPackage: string): void
public static "runCustomMixinClassNodeAnnotations"(className: string, classNode: $ClassNode$Type): void
public "postApply"(targetClassName: string, targetClass: $ClassNode$Type, mixinClassName: string, mixinInfo: $IMixinInfo$Type): void
public "getMixins"(): $List<(string)>
public "getRefMapperConfig"(): string
public "shouldApplyMixin"(targetClassName: string, mixinClassName: string): boolean
public "preApply"(targetClassName: string, targetClass: $ClassNode$Type, mixinClassName: string, mixinInfo: $IMixinInfo$Type): void
public "acceptTargets"(myTargets: $Set$Type<(string)>, otherTargets: $Set$Type<(string)>): void
get "mixins"(): $List<(string)>
get "refMapperConfig"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MemoryLeakFixMixinConfigPlugin$Type = ($MemoryLeakFixMixinConfigPlugin);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MemoryLeakFixMixinConfigPlugin_ = $MemoryLeakFixMixinConfigPlugin$Type;
}}
declare module "packages/ca/fxco/memoryleakfix/mixinextras/sugar/impl/$SugarApplicationException" {
import {$Throwable, $Throwable$Type} from "packages/java/lang/$Throwable"
import {$MixinException, $MixinException$Type} from "packages/org/spongepowered/asm/mixin/throwables/$MixinException"

export class $SugarApplicationException extends $MixinException {

constructor(message: string)
constructor(message: string, cause: $Throwable$Type)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SugarApplicationException$Type = ($SugarApplicationException);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SugarApplicationException_ = $SugarApplicationException$Type;
}}
declare module "packages/ca/fxco/memoryleakfix/mixinextras/sugar/ref/$LocalFloatRef" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $LocalFloatRef {

 "get"(): float
 "set"(arg0: float): void
}

export namespace $LocalFloatRef {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LocalFloatRef$Type = ($LocalFloatRef);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LocalFloatRef_ = $LocalFloatRef$Type;
}}
declare module "packages/ca/fxco/memoryleakfix/mixinextras/sugar/ref/$LocalShortRef" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $LocalShortRef {

 "get"(): short
 "set"(arg0: short): void
}

export namespace $LocalShortRef {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LocalShortRef$Type = ($LocalShortRef);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LocalShortRef_ = $LocalShortRef$Type;
}}
declare module "packages/ca/fxco/memoryleakfix/mixinextras/sugar/impl/ref/$LocalRefClassGenerator" {
import {$Type, $Type$Type} from "packages/org/objectweb/asm/$Type"

export class $LocalRefClassGenerator {

constructor()

public static "getForType"(type: $Type$Type): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LocalRefClassGenerator$Type = ($LocalRefClassGenerator);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LocalRefClassGenerator_ = $LocalRefClassGenerator$Type;
}}
declare module "packages/ca/fxco/memoryleakfix/mixinextras/sugar/ref/$LocalCharRef" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $LocalCharRef {

 "get"(): character
 "set"(arg0: character): void
}

export namespace $LocalCharRef {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LocalCharRef$Type = ($LocalCharRef);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LocalCharRef_ = $LocalCharRef$Type;
}}
declare module "packages/ca/fxco/memoryleakfix/mixinextras/injector/$MixinExtrasInjectionInfo" {
import {$AnnotationNode, $AnnotationNode$Type} from "packages/org/objectweb/asm/tree/$AnnotationNode"
import {$MethodNode, $MethodNode$Type} from "packages/org/objectweb/asm/tree/$MethodNode"
import {$InjectionInfo, $InjectionInfo$Type} from "packages/org/spongepowered/asm/mixin/injection/struct/$InjectionInfo"

export class $MixinExtrasInjectionInfo extends $InjectionInfo {
static readonly "DEFAULT_PREFIX": string


public "getSliceId"(id: string): string
public "getMethod"(): $MethodNode
public "getAnnotationNode"(): $AnnotationNode
get "method"(): $MethodNode
get "annotationNode"(): $AnnotationNode
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MixinExtrasInjectionInfo$Type = ($MixinExtrasInjectionInfo);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MixinExtrasInjectionInfo_ = $MixinExtrasInjectionInfo$Type;
}}
declare module "packages/ca/fxco/memoryleakfix/mixinextras/utils/$Decorations" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $Decorations {
static readonly "PERSISTENT": string
static readonly "POPPED_OPERATION": string
static readonly "LOCAL_REF_MAP": string
static readonly "NEW_IS_DUPED": string
static readonly "NEW_ARG_TYPES": string
static readonly "WRAPPED": string

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Decorations$Type = ($Decorations);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Decorations_ = $Decorations$Type;
}}
declare module "packages/ca/fxco/memoryleakfix/mixinextras/utils/$ProxyUtils" {
import {$Class, $Class$Type} from "packages/java/lang/$Class"

export class $ProxyUtils {

constructor()

public static "getProxy"<T>(impl: any, interfaceClass: $Class$Type<(T)>): T
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ProxyUtils$Type = ($ProxyUtils);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ProxyUtils_ = $ProxyUtils$Type;
}}
declare module "packages/ca/fxco/memoryleakfix/mixinextras/injector/wrapoperation/$Operation" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $Operation<R> {

 "call"(...arg0: (any)[]): R

(...arg0: (any)[]): R
}

export namespace $Operation {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Operation$Type<R> = ($Operation<(R)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Operation_<R> = $Operation$Type<(R)>;
}}
declare module "packages/ca/fxco/memoryleakfix/mixinextras/sugar/ref/$LocalRef" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $LocalRef<T> {

 "get"(): T
 "set"(arg0: T): void
}

export namespace $LocalRef {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LocalRef$Type<T> = ($LocalRef<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LocalRef_<T> = $LocalRef$Type<(T)>;
}}
declare module "packages/ca/fxco/memoryleakfix/mixinextras/lib/apache/commons/$ObjectUtils$Null" {
import {$Serializable, $Serializable$Type} from "packages/java/io/$Serializable"

export class $ObjectUtils$Null implements $Serializable {


}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ObjectUtils$Null$Type = ($ObjectUtils$Null);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ObjectUtils$Null_ = $ObjectUtils$Null$Type;
}}
declare module "packages/ca/fxco/memoryleakfix/mixinextras/injector/wrapoperation/$WrapOperationRuntime" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $WrapOperationRuntime {

constructor()

public static "checkArgumentCount"(args: (any)[], expectedArgumentCount: integer, expectedTypes: string): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WrapOperationRuntime$Type = ($WrapOperationRuntime);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WrapOperationRuntime_ = $WrapOperationRuntime$Type;
}}
declare module "packages/ca/fxco/memoryleakfix/mixinextras/sugar/impl/$SugarWrapper" {
import {$Annotation, $Annotation$Type} from "packages/java/lang/annotation/$Annotation"
import {$Class, $Class$Type} from "packages/java/lang/$Class"

export interface $SugarWrapper extends $Annotation {

 "equals"(arg0: any): boolean
 "toString"(): string
 "hashCode"(): integer
 "annotationType"(): $Class<(any)>
}

export namespace $SugarWrapper {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SugarWrapper$Type = ($SugarWrapper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SugarWrapper_ = $SugarWrapper$Type;
}}
declare module "packages/ca/fxco/memoryleakfix/mixinextras/utils/$GenericParamParser" {
import {$List, $List$Type} from "packages/java/util/$List"
import {$SignatureVisitor, $SignatureVisitor$Type} from "packages/org/objectweb/asm/signature/$SignatureVisitor"
import {$Type, $Type$Type} from "packages/org/objectweb/asm/$Type"

export class $GenericParamParser extends $SignatureVisitor {
static readonly "EXTENDS": character
static readonly "SUPER": character
static readonly "INSTANCEOF": character


public "visitParameterType"(): $SignatureVisitor
public static "getParameterGenerics"(desc: string, signature: string): $List<($Type)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GenericParamParser$Type = ($GenericParamParser);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GenericParamParser_ = $GenericParamParser$Type;
}}
declare module "packages/ca/fxco/memoryleakfix/mixinextras/injector/wrapoperation/$WrapOperationInjector" {
import {$InjectionInfo, $InjectionInfo$Type} from "packages/org/spongepowered/asm/mixin/injection/struct/$InjectionInfo"
import {$Injector, $Injector$Type} from "packages/org/spongepowered/asm/mixin/injection/code/$Injector"

export class $WrapOperationInjector extends $Injector {

constructor(info: $InjectionInfo$Type)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WrapOperationInjector$Type = ($WrapOperationInjector);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WrapOperationInjector_ = $WrapOperationInjector$Type;
}}
declare module "packages/ca/fxco/memoryleakfix/mixinextras/wrapper/$InjectorWrapperImpl" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $InjectorWrapperImpl {


public "usesGranularInject"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InjectorWrapperImpl$Type = ($InjectorWrapperImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InjectorWrapperImpl_ = $InjectorWrapperImpl$Type;
}}
declare module "packages/ca/fxco/memoryleakfix/mixinextras/injector/wrapoperation/$IncorrectArgumentCountException" {
import {$RuntimeException, $RuntimeException$Type} from "packages/java/lang/$RuntimeException"

export class $IncorrectArgumentCountException extends $RuntimeException {


}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IncorrectArgumentCountException$Type = ($IncorrectArgumentCountException);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IncorrectArgumentCountException_ = $IncorrectArgumentCountException$Type;
}}
declare module "packages/ca/fxco/memoryleakfix/forge/$MemoryLeakFixForge" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $MemoryLeakFixForge {

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MemoryLeakFixForge$Type = ($MemoryLeakFixForge);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MemoryLeakFixForge_ = $MemoryLeakFixForge$Type;
}}
declare module "packages/ca/fxco/memoryleakfix/mixinextras/sugar/impl/$SugarWrapperImpl" {
import {$InjectorWrapperImpl, $InjectorWrapperImpl$Type} from "packages/ca/fxco/memoryleakfix/mixinextras/wrapper/$InjectorWrapperImpl"

export class $SugarWrapperImpl extends $InjectorWrapperImpl {


}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SugarWrapperImpl$Type = ($SugarWrapperImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SugarWrapperImpl_ = $SugarWrapperImpl$Type;
}}
declare module "packages/ca/fxco/memoryleakfix/mixinextras/ap/$StdoutMessager" {
import {$AnnotationMirror, $AnnotationMirror$Type} from "packages/javax/lang/model/element/$AnnotationMirror"
import {$AnnotationValue, $AnnotationValue$Type} from "packages/javax/lang/model/element/$AnnotationValue"
import {$Element, $Element$Type} from "packages/javax/lang/model/element/$Element"
import {$Messager, $Messager$Type} from "packages/javax/annotation/processing/$Messager"
import {$Diagnostic$Kind, $Diagnostic$Kind$Type} from "packages/javax/tools/$Diagnostic$Kind"

export class $StdoutMessager implements $Messager {

constructor()

public "printMessage"(kind: $Diagnostic$Kind$Type, msg: charseq, e: $Element$Type, a: $AnnotationMirror$Type, v: $AnnotationValue$Type): void
public "printMessage"(kind: $Diagnostic$Kind$Type, msg: charseq, e: $Element$Type, a: $AnnotationMirror$Type): void
public "printMessage"(kind: $Diagnostic$Kind$Type, msg: charseq, e: $Element$Type): void
public "printMessage"(kind: $Diagnostic$Kind$Type, msg: charseq): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StdoutMessager$Type = ($StdoutMessager);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StdoutMessager_ = $StdoutMessager$Type;
}}
declare module "packages/ca/fxco/memoryleakfix/mixinextras/transformer/$MixinTransformer" {
import {$IMixinInfo, $IMixinInfo$Type} from "packages/org/spongepowered/asm/mixin/extensibility/$IMixinInfo"
import {$ClassNode, $ClassNode$Type} from "packages/org/objectweb/asm/tree/$ClassNode"

export interface $MixinTransformer {

 "transform"(arg0: $IMixinInfo$Type, arg1: $ClassNode$Type): void

(arg0: $IMixinInfo$Type, arg1: $ClassNode$Type): void
}

export namespace $MixinTransformer {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MixinTransformer$Type = ($MixinTransformer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MixinTransformer_ = $MixinTransformer$Type;
}}
declare module "packages/ca/fxco/memoryleakfix/mixinextras/sugar/ref/$LocalByteRef" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $LocalByteRef {

 "get"(): byte
 "set"(arg0: byte): void
}

export namespace $LocalByteRef {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LocalByteRef$Type = ($LocalByteRef);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LocalByteRef_ = $LocalByteRef$Type;
}}
declare module "packages/ca/fxco/memoryleakfix/mixinextras/lib/apache/commons/$ObjectUtils" {
import {$ObjectUtils$Null, $ObjectUtils$Null$Type} from "packages/ca/fxco/memoryleakfix/mixinextras/lib/apache/commons/$ObjectUtils$Null"

export class $ObjectUtils {
static readonly "NULL": $ObjectUtils$Null


/**
 * 
 * @deprecated
 */
public static "equals"(object1: any, object2: any): boolean
public "toString"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ObjectUtils$Type = ($ObjectUtils);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ObjectUtils_ = $ObjectUtils$Type;
}}
declare module "packages/ca/fxco/memoryleakfix/mixinextras/utils/$InjectorUtils" {
import {$Target, $Target$Type} from "packages/org/spongepowered/asm/mixin/injection/struct/$Target"
import {$List, $List$Type} from "packages/java/util/$List"
import {$InjectionNodes$InjectionNode, $InjectionNodes$InjectionNode$Type} from "packages/org/spongepowered/asm/mixin/injection/struct/$InjectionNodes$InjectionNode"
import {$AbstractInsnNode, $AbstractInsnNode$Type} from "packages/org/objectweb/asm/tree/$AbstractInsnNode"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $InjectorUtils {

constructor()

public static "isDupedFactoryRedirect"(node: $InjectionNodes$InjectionNode$Type): boolean
public static "findFactoryRedirectThrowString"(target: $Target$Type, start: $AbstractInsnNode$Type): $AbstractInsnNode
public static "isDynamicInstanceofRedirect"(node: $InjectionNodes$InjectionNode$Type): boolean
public static "isDupedNew"(node: $InjectionNodes$InjectionNode$Type): boolean
public static "isVirtualRedirect"(node: $InjectionNodes$InjectionNode$Type): boolean
public static "checkForDupedNews"(targets: $Map$Type<($Target$Type), ($List$Type<($InjectionNodes$InjectionNode$Type)>)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InjectorUtils$Type = ($InjectorUtils);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InjectorUtils_ = $InjectorUtils$Type;
}}
declare module "packages/ca/fxco/memoryleakfix/config/$SilentClassNotFound" {
import {$Annotation, $Annotation$Type} from "packages/java/lang/annotation/$Annotation"
import {$Class, $Class$Type} from "packages/java/lang/$Class"

export interface $SilentClassNotFound extends $Annotation {

 "equals"(arg0: any): boolean
 "toString"(): string
 "hashCode"(): integer
 "annotationType"(): $Class<(any)>
}

export namespace $SilentClassNotFound {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SilentClassNotFound$Type = ($SilentClassNotFound);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SilentClassNotFound_ = $SilentClassNotFound$Type;
}}
declare module "packages/ca/fxco/memoryleakfix/mixinextras/wrapper/factory/$FactoryRedirectWrapperMixinTransformer" {
import {$IMixinInfo, $IMixinInfo$Type} from "packages/org/spongepowered/asm/mixin/extensibility/$IMixinInfo"
import {$ClassNode, $ClassNode$Type} from "packages/org/objectweb/asm/tree/$ClassNode"
import {$MixinTransformer, $MixinTransformer$Type} from "packages/ca/fxco/memoryleakfix/mixinextras/transformer/$MixinTransformer"

export class $FactoryRedirectWrapperMixinTransformer implements $MixinTransformer {

constructor()

public "transform"(mixinInfo: $IMixinInfo$Type, mixinNode: $ClassNode$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FactoryRedirectWrapperMixinTransformer$Type = ($FactoryRedirectWrapperMixinTransformer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FactoryRedirectWrapperMixinTransformer_ = $FactoryRedirectWrapperMixinTransformer$Type;
}}
declare module "packages/ca/fxco/memoryleakfix/mixinextras/sugar/impl/ref/$LocalRefRuntime" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $LocalRefRuntime {

constructor()

public static "checkState"(state: byte): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LocalRefRuntime$Type = ($LocalRefRuntime);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LocalRefRuntime_ = $LocalRefRuntime$Type;
}}
declare module "packages/ca/fxco/memoryleakfix/mixinextras/injector/$ModifyReceiverInjectionInfo" {
import {$AnnotationNode, $AnnotationNode$Type} from "packages/org/objectweb/asm/tree/$AnnotationNode"
import {$MixinExtrasInjectionInfo, $MixinExtrasInjectionInfo$Type} from "packages/ca/fxco/memoryleakfix/mixinextras/injector/$MixinExtrasInjectionInfo"
import {$MethodNode, $MethodNode$Type} from "packages/org/objectweb/asm/tree/$MethodNode"
import {$MixinTargetContext, $MixinTargetContext$Type} from "packages/org/spongepowered/asm/mixin/transformer/$MixinTargetContext"

export class $ModifyReceiverInjectionInfo extends $MixinExtrasInjectionInfo {
static readonly "DEFAULT_PREFIX": string

constructor(mixin: $MixinTargetContext$Type, method: $MethodNode$Type, annotation: $AnnotationNode$Type)

public "getMethod"(): $MethodNode
public "getAnnotationNode"(): $AnnotationNode
get "method"(): $MethodNode
get "annotationNode"(): $AnnotationNode
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModifyReceiverInjectionInfo$Type = ($ModifyReceiverInjectionInfo);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModifyReceiverInjectionInfo_ = $ModifyReceiverInjectionInfo$Type;
}}
declare module "packages/ca/fxco/memoryleakfix/mixinextras/service/$MixinExtrasVersion" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $MixinExtrasVersion extends $Enum<($MixinExtrasVersion)> {
static readonly "V0_2_0_BETA_1": $MixinExtrasVersion
static readonly "V0_2_0_BETA_2": $MixinExtrasVersion
static readonly "V0_2_0_BETA_3": $MixinExtrasVersion
static readonly "V0_2_0_BETA_4": $MixinExtrasVersion
static readonly "V0_2_0_BETA_5": $MixinExtrasVersion
static readonly "V0_2_0_BETA_6": $MixinExtrasVersion
static readonly "V0_2_0_BETA_7": $MixinExtrasVersion
static readonly "V0_2_0_BETA_8": $MixinExtrasVersion
static readonly "V0_2_0_BETA_9": $MixinExtrasVersion
static readonly "V0_3_2": $MixinExtrasVersion
static readonly "LATEST": $MixinExtrasVersion


public "toString"(): string
public static "values"(): ($MixinExtrasVersion)[]
public static "valueOf"(name: string): $MixinExtrasVersion
public "getNumber"(): integer
get "number"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MixinExtrasVersion$Type = (("v0_2_0_beta_6") | ("v0_2_0_beta_7") | ("v0_2_0_beta_8") | ("v0_2_0_beta_9") | ("v0_2_0_beta_1") | ("v0_2_0_beta_2") | ("v0_2_0_beta_3") | ("v0_3_2") | ("v0_2_0_beta_4") | ("v0_2_0_beta_5")) | ($MixinExtrasVersion);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MixinExtrasVersion_ = $MixinExtrasVersion$Type;
}}
declare module "packages/ca/fxco/memoryleakfix/mixinextras/injector/$ModifyExpressionValue" {
import {$Annotation, $Annotation$Type} from "packages/java/lang/annotation/$Annotation"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$At, $At$Type} from "packages/org/spongepowered/asm/mixin/injection/$At"
import {$Slice, $Slice$Type} from "packages/org/spongepowered/asm/mixin/injection/$Slice"

export interface $ModifyExpressionValue extends $Annotation {

 "method"(): (string)[]
 "allow"(): integer
 "at"(): ($At)[]
 "slice"(): ($Slice)[]
 "expect"(): integer
 "require"(): integer
 "remap"(): boolean
 "equals"(arg0: any): boolean
 "toString"(): string
 "hashCode"(): integer
 "annotationType"(): $Class<(any)>
}

export namespace $ModifyExpressionValue {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModifyExpressionValue$Type = ($ModifyExpressionValue);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModifyExpressionValue_ = $ModifyExpressionValue$Type;
}}
declare module "packages/ca/fxco/memoryleakfix/mixinextras/sugar/impl/$SugarInjector" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $SugarInjector {


}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SugarInjector$Type = ($SugarInjector);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SugarInjector_ = $SugarInjector$Type;
}}
declare module "packages/ca/fxco/memoryleakfix/mixinextras/injector/$ModifyReceiver" {
import {$Annotation, $Annotation$Type} from "packages/java/lang/annotation/$Annotation"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$At, $At$Type} from "packages/org/spongepowered/asm/mixin/injection/$At"
import {$Slice, $Slice$Type} from "packages/org/spongepowered/asm/mixin/injection/$Slice"

export interface $ModifyReceiver extends $Annotation {

 "method"(): (string)[]
 "allow"(): integer
 "at"(): ($At)[]
 "slice"(): ($Slice)[]
 "expect"(): integer
 "require"(): integer
 "remap"(): boolean
 "equals"(arg0: any): boolean
 "toString"(): string
 "hashCode"(): integer
 "annotationType"(): $Class<(any)>
}

export namespace $ModifyReceiver {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModifyReceiver$Type = ($ModifyReceiver);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModifyReceiver_ = $ModifyReceiver$Type;
}}
declare module "packages/ca/fxco/memoryleakfix/mixinextras/sugar/$Share" {
import {$Annotation, $Annotation$Type} from "packages/java/lang/annotation/$Annotation"
import {$Class, $Class$Type} from "packages/java/lang/$Class"

export interface $Share extends $Annotation {

 "value"(): string
 "equals"(arg0: any): boolean
 "toString"(): string
 "hashCode"(): integer
 "annotationType"(): $Class<(any)>
}

export namespace $Share {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Share$Type = ($Share);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Share_ = $Share$Type;
}}
declare module "packages/ca/fxco/memoryleakfix/utils/$MixinInternals" {
import {$IMixinInfo, $IMixinInfo$Type} from "packages/org/spongepowered/asm/mixin/extensibility/$IMixinInfo"
import {$ClassNode, $ClassNode$Type} from "packages/org/objectweb/asm/tree/$ClassNode"
import {$Pair, $Pair$Type} from "packages/org/apache/commons/lang3/tuple/$Pair"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ITargetClassContext, $ITargetClassContext$Type} from "packages/org/spongepowered/asm/mixin/transformer/ext/$ITargetClassContext"
import {$IExtension, $IExtension$Type} from "packages/org/spongepowered/asm/mixin/transformer/ext/$IExtension"

export class $MixinInternals {

constructor()

public static "registerExtension"(extension: $IExtension$Type): void
public static "getMixinsFor"(context: $ITargetClassContext$Type): $List<($Pair<($IMixinInfo), ($ClassNode)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MixinInternals$Type = ($MixinInternals);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MixinInternals_ = $MixinInternals$Type;
}}
declare module "packages/ca/fxco/memoryleakfix/mixinextras/injector/wrapoperation/$WrapOperationApplicatorExtension" {
import {$ClassNode, $ClassNode$Type} from "packages/org/objectweb/asm/tree/$ClassNode"
import {$MixinEnvironment, $MixinEnvironment$Type} from "packages/org/spongepowered/asm/mixin/$MixinEnvironment"
import {$ITargetClassContext, $ITargetClassContext$Type} from "packages/org/spongepowered/asm/mixin/transformer/ext/$ITargetClassContext"
import {$IExtension, $IExtension$Type} from "packages/org/spongepowered/asm/mixin/transformer/ext/$IExtension"

export class $WrapOperationApplicatorExtension implements $IExtension {

constructor()

public "export"(env: $MixinEnvironment$Type, name: string, force: boolean, classNode: $ClassNode$Type): void
public "postApply"(context: $ITargetClassContext$Type): void
public "preApply"(context: $ITargetClassContext$Type): void
public "checkActive"(environment: $MixinEnvironment$Type): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WrapOperationApplicatorExtension$Type = ($WrapOperationApplicatorExtension);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WrapOperationApplicatorExtension_ = $WrapOperationApplicatorExtension$Type;
}}
declare module "packages/ca/fxco/memoryleakfix/mixinextras/injector/wrapoperation/$WrapOperation" {
import {$Annotation, $Annotation$Type} from "packages/java/lang/annotation/$Annotation"
import {$Constant, $Constant$Type} from "packages/org/spongepowered/asm/mixin/injection/$Constant"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$At, $At$Type} from "packages/org/spongepowered/asm/mixin/injection/$At"
import {$Slice, $Slice$Type} from "packages/org/spongepowered/asm/mixin/injection/$Slice"

export interface $WrapOperation extends $Annotation {

 "method"(): (string)[]
 "constant"(): ($Constant)[]
 "allow"(): integer
 "at"(): ($At)[]
 "slice"(): ($Slice)[]
 "expect"(): integer
 "require"(): integer
 "remap"(): boolean
 "equals"(arg0: any): boolean
 "toString"(): string
 "hashCode"(): integer
 "annotationType"(): $Class<(any)>
}

export namespace $WrapOperation {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WrapOperation$Type = ($WrapOperation);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WrapOperation_ = $WrapOperation$Type;
}}
declare module "packages/ca/fxco/memoryleakfix/mixinextras/sugar/$SugarBridge" {
import {$Annotation, $Annotation$Type} from "packages/java/lang/annotation/$Annotation"
import {$Class, $Class$Type} from "packages/java/lang/$Class"

export interface $SugarBridge extends $Annotation {

 "equals"(arg0: any): boolean
 "toString"(): string
 "hashCode"(): integer
 "annotationType"(): $Class<(any)>
}

export namespace $SugarBridge {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SugarBridge$Type = ($SugarBridge);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SugarBridge_ = $SugarBridge$Type;
}}
declare module "packages/ca/fxco/memoryleakfix/mixinextras/utils/$UniquenessHelper" {
import {$ClassNode, $ClassNode$Type} from "packages/org/objectweb/asm/tree/$ClassNode"

export class $UniquenessHelper {

constructor()

public static "getUniqueMethodName"(classNode: $ClassNode$Type, name: string): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $UniquenessHelper$Type = ($UniquenessHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $UniquenessHelper_ = $UniquenessHelper$Type;
}}
declare module "packages/ca/fxco/memoryleakfix/mixinextras/sugar/ref/$LocalLongRef" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $LocalLongRef {

 "get"(): long
 "set"(arg0: long): void
}

export namespace $LocalLongRef {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LocalLongRef$Type = ($LocalLongRef);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LocalLongRef_ = $LocalLongRef$Type;
}}
declare module "packages/ca/fxco/memoryleakfix/mixinextras/sugar/impl/$SingleIterationList" {
import {$Comparator, $Comparator$Type} from "packages/java/util/$Comparator"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$UnaryOperator, $UnaryOperator$Type} from "packages/java/util/function/$UnaryOperator"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$IntFunction, $IntFunction$Type} from "packages/java/util/function/$IntFunction"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$ListIterator, $ListIterator$Type} from "packages/java/util/$ListIterator"
import {$Iterator, $Iterator$Type} from "packages/java/util/$Iterator"
import {$Spliterator, $Spliterator$Type} from "packages/java/util/$Spliterator"

export class $SingleIterationList<T> implements $List<(T)> {

constructor(delegate: $List$Type<(T)>, allowedIteration: integer)

public "add"(t: T): boolean
public "add"(index: integer, element: T): void
public "remove"(index: integer): T
public "remove"(o: any): boolean
public "get"(index: integer): T
public "indexOf"(o: any): integer
public "clear"(): void
public "lastIndexOf"(o: any): integer
public "isEmpty"(): boolean
public "size"(): integer
public "subList"(fromIndex: integer, toIndex: integer): $List<(T)>
public "toArray"<T1>(a: (T1)[]): (T1)[]
public "toArray"(): (any)[]
public "iterator"(): $Iterator<(T)>
public "contains"(o: any): boolean
public "addAll"(index: integer, c: $Collection$Type<(any)>): boolean
public "addAll"(c: $Collection$Type<(any)>): boolean
public "set"(index: integer, element: T): T
public "removeAll"(c: $Collection$Type<(any)>): boolean
public "retainAll"(c: $Collection$Type<(any)>): boolean
public "listIterator"(index: integer): $ListIterator<(T)>
public "listIterator"(): $ListIterator<(T)>
public "containsAll"(c: $Collection$Type<(any)>): boolean
public "equals"(arg0: any): boolean
public "hashCode"(): integer
public static "copyOf"<E>(arg0: $Collection$Type<(any)>): $List<(T)>
public "replaceAll"(arg0: $UnaryOperator$Type<(T)>): void
public static "of"<E>(arg0: T, arg1: T, arg2: T, arg3: T): $List<(T)>
public static "of"<E>(arg0: T, arg1: T, arg2: T): $List<(T)>
public static "of"<E>(arg0: T, arg1: T): $List<(T)>
public static "of"<E>(arg0: T): $List<(T)>
public static "of"<E>(): $List<(T)>
public static "of"<E>(arg0: T, arg1: T, arg2: T, arg3: T, arg4: T, arg5: T, arg6: T, arg7: T): $List<(T)>
public static "of"<E>(arg0: T, arg1: T, arg2: T, arg3: T, arg4: T, arg5: T, arg6: T): $List<(T)>
public static "of"<E>(arg0: T, arg1: T, arg2: T, arg3: T, arg4: T, arg5: T): $List<(T)>
public static "of"<E>(arg0: T, arg1: T, arg2: T, arg3: T, arg4: T): $List<(T)>
public static "of"<E>(arg0: T, arg1: T, arg2: T, arg3: T, arg4: T, arg5: T, arg6: T, arg7: T, arg8: T, arg9: T): $List<(T)>
public static "of"<E>(arg0: T, arg1: T, arg2: T, arg3: T, arg4: T, arg5: T, arg6: T, arg7: T, arg8: T): $List<(T)>
public static "of"<E>(...arg0: (T)[]): $List<(T)>
public "spliterator"(): $Spliterator<(T)>
public "sort"(arg0: $Comparator$Type<(any)>): void
public "toArray"<T>(arg0: $IntFunction$Type<((T)[])>): (T)[]
public "stream"(): $Stream<(T)>
public "removeIf"(arg0: $Predicate$Type<(any)>): boolean
public "parallelStream"(): $Stream<(T)>
public "forEach"(arg0: $Consumer$Type<(any)>): void
[Symbol.iterator](): IterableIterator<T>;
[index: number]: T
get "empty"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SingleIterationList$Type<T> = ($SingleIterationList<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SingleIterationList_<T> = $SingleIterationList$Type<(T)>;
}}
declare module "packages/ca/fxco/memoryleakfix/mixinextras/lib/apache/commons/builder/$CompareToBuilder" {
import {$Comparator, $Comparator$Type} from "packages/java/util/$Comparator"

export class $CompareToBuilder {

constructor()

public "append"(lhs: (long)[], rhs: (long)[]): $CompareToBuilder
public "append"(lhs: (integer)[], rhs: (integer)[]): $CompareToBuilder
public "append"(lhs: (short)[], rhs: (short)[]): $CompareToBuilder
public "append"(lhs: (any)[], rhs: (any)[], comparator: $Comparator$Type<(any)>): $CompareToBuilder
public "append"(lhs: boolean, rhs: boolean): $CompareToBuilder
public "append"(lhs: (boolean)[], rhs: (boolean)[]): $CompareToBuilder
public "append"(lhs: (float)[], rhs: (float)[]): $CompareToBuilder
public "append"(lhs: (double)[], rhs: (double)[]): $CompareToBuilder
public "append"(lhs: (byte)[], rhs: (byte)[]): $CompareToBuilder
public "append"(lhs: (character)[], rhs: (character)[]): $CompareToBuilder
public "append"(lhs: short, rhs: short): $CompareToBuilder
public "append"(lhs: integer, rhs: integer): $CompareToBuilder
public "append"(lhs: long, rhs: long): $CompareToBuilder
public "append"(lhs: any, rhs: any, comparator: $Comparator$Type<(any)>): $CompareToBuilder
public "append"(lhs: any, rhs: any): $CompareToBuilder
public "append"(lhs: float, rhs: float): $CompareToBuilder
public "append"(lhs: double, rhs: double): $CompareToBuilder
public "append"(lhs: byte, rhs: byte): $CompareToBuilder
public "append"(lhs: character, rhs: character): $CompareToBuilder
public "toComparison"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CompareToBuilder$Type = ($CompareToBuilder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CompareToBuilder_ = $CompareToBuilder$Type;
}}
declare module "packages/ca/fxco/memoryleakfix/mixinextras/versions/$MixinVersionImpl_v0_8_4" {
import {$LocalVariableDiscriminator$Context, $LocalVariableDiscriminator$Context$Type} from "packages/org/spongepowered/asm/mixin/injection/modify/$LocalVariableDiscriminator$Context"
import {$Target, $Target$Type} from "packages/org/spongepowered/asm/mixin/injection/struct/$Target"
import {$InjectionInfo, $InjectionInfo$Type} from "packages/org/spongepowered/asm/mixin/injection/struct/$InjectionInfo"
import {$RuntimeException, $RuntimeException$Type} from "packages/java/lang/$RuntimeException"
import {$Type, $Type$Type} from "packages/org/objectweb/asm/$Type"
import {$AbstractInsnNode, $AbstractInsnNode$Type} from "packages/org/objectweb/asm/tree/$AbstractInsnNode"
import {$MixinVersionImpl_v0_8_3, $MixinVersionImpl_v0_8_3$Type} from "packages/ca/fxco/memoryleakfix/mixinextras/versions/$MixinVersionImpl_v0_8_3"

export class $MixinVersionImpl_v0_8_4 extends $MixinVersionImpl_v0_8_3 {

constructor()

public "makeInvalidInjectionException"(info: $InjectionInfo$Type, message: string): $RuntimeException
public "makeLvtContext"(info: $InjectionInfo$Type, returnType: $Type$Type, argsOnly: boolean, target: $Target$Type, node: $AbstractInsnNode$Type): $LocalVariableDiscriminator$Context
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MixinVersionImpl_v0_8_4$Type = ($MixinVersionImpl_v0_8_4);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MixinVersionImpl_v0_8_4_ = $MixinVersionImpl_v0_8_4$Type;
}}
declare module "packages/ca/fxco/memoryleakfix/mixinextras/versions/$MixinVersionImpl_v0_8_3" {
import {$AnnotationNode, $AnnotationNode$Type} from "packages/org/objectweb/asm/tree/$AnnotationNode"
import {$IMixinContext, $IMixinContext$Type} from "packages/org/spongepowered/asm/mixin/refmap/$IMixinContext"
import {$MixinVersionImpl_v0_8, $MixinVersionImpl_v0_8$Type} from "packages/ca/fxco/memoryleakfix/mixinextras/versions/$MixinVersionImpl_v0_8"
import {$InjectionInfo, $InjectionInfo$Type} from "packages/org/spongepowered/asm/mixin/injection/struct/$InjectionInfo"

export class $MixinVersionImpl_v0_8_3 extends $MixinVersionImpl_v0_8 {

constructor()

public "getAnnotation"(info: $InjectionInfo$Type): $AnnotationNode
public "preInject"(info: $InjectionInfo$Type): void
public "getMixin"(info: $InjectionInfo$Type): $IMixinContext
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MixinVersionImpl_v0_8_3$Type = ($MixinVersionImpl_v0_8_3);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MixinVersionImpl_v0_8_3_ = $MixinVersionImpl_v0_8_3$Type;
}}
declare module "packages/ca/fxco/memoryleakfix/mixinextras/sugar/ref/$LocalBooleanRef" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $LocalBooleanRef {

 "get"(): boolean
 "set"(arg0: boolean): void
}

export namespace $LocalBooleanRef {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LocalBooleanRef$Type = ($LocalBooleanRef);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LocalBooleanRef_ = $LocalBooleanRef$Type;
}}
declare module "packages/ca/fxco/memoryleakfix/mixinextras/transformer/$MixinTransformerExtension" {
import {$ClassNode, $ClassNode$Type} from "packages/org/objectweb/asm/tree/$ClassNode"
import {$MixinEnvironment, $MixinEnvironment$Type} from "packages/org/spongepowered/asm/mixin/$MixinEnvironment"
import {$ITargetClassContext, $ITargetClassContext$Type} from "packages/org/spongepowered/asm/mixin/transformer/ext/$ITargetClassContext"
import {$IExtension, $IExtension$Type} from "packages/org/spongepowered/asm/mixin/transformer/ext/$IExtension"

export class $MixinTransformerExtension implements $IExtension {

constructor()

public "export"(env: $MixinEnvironment$Type, name: string, force: boolean, classNode: $ClassNode$Type): void
public "postApply"(context: $ITargetClassContext$Type): void
public "preApply"(context: $ITargetClassContext$Type): void
public "checkActive"(environment: $MixinEnvironment$Type): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MixinTransformerExtension$Type = ($MixinTransformerExtension);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MixinTransformerExtension_ = $MixinTransformerExtension$Type;
}}
declare module "packages/ca/fxco/memoryleakfix/mixinextras/sugar/ref/$LocalIntRef" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $LocalIntRef {

 "get"(): integer
 "set"(arg0: integer): void
}

export namespace $LocalIntRef {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LocalIntRef$Type = ($LocalIntRef);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LocalIntRef_ = $LocalIntRef$Type;
}}
declare module "packages/ca/fxco/memoryleakfix/mixinextras/wrapper/$WrapperInjectionInfo" {
import {$AnnotationNode, $AnnotationNode$Type} from "packages/org/objectweb/asm/tree/$AnnotationNode"
import {$MixinExtrasInjectionInfo, $MixinExtrasInjectionInfo$Type} from "packages/ca/fxco/memoryleakfix/mixinextras/injector/$MixinExtrasInjectionInfo"
import {$MethodNode, $MethodNode$Type} from "packages/org/objectweb/asm/tree/$MethodNode"
import {$Target, $Target$Type} from "packages/org/spongepowered/asm/mixin/injection/struct/$Target"
import {$List, $List$Type} from "packages/java/util/$List"
import {$LateApplyingInjectorInfo, $LateApplyingInjectorInfo$Type} from "packages/ca/fxco/memoryleakfix/mixinextras/injector/$LateApplyingInjectorInfo"
import {$Map, $Map$Type} from "packages/java/util/$Map"
import {$InjectionNodes$InjectionNode, $InjectionNodes$InjectionNode$Type} from "packages/org/spongepowered/asm/mixin/injection/struct/$InjectionNodes$InjectionNode"

export class $WrapperInjectionInfo extends $MixinExtrasInjectionInfo implements $LateApplyingInjectorInfo {
static readonly "DEFAULT_PREFIX": string


public "wrap"(outer: $LateApplyingInjectorInfo$Type): void
public "prepare"(): void
public "isValid"(): boolean
public "inject"(): void
public "addCallbackInvocation"(handler: $MethodNode$Type): void
public "preInject"(): void
public "postInject"(): void
public "lateInject"(): void
public "latePostInject"(): void
public "getTargetMap"(): $Map<($Target), ($List<($InjectionNodes$InjectionNode)>)>
public static "wrap"(inner: any, outer: $LateApplyingInjectorInfo$Type): boolean
/**
 * 
 * @deprecated
 */
public "lateApply"(): void
public "getMethod"(): $MethodNode
public "getAnnotationNode"(): $AnnotationNode
get "valid"(): boolean
get "targetMap"(): $Map<($Target), ($List<($InjectionNodes$InjectionNode)>)>
get "method"(): $MethodNode
get "annotationNode"(): $AnnotationNode
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WrapperInjectionInfo$Type = ($WrapperInjectionInfo);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WrapperInjectionInfo_ = $WrapperInjectionInfo$Type;
}}
declare module "packages/ca/fxco/memoryleakfix/mixinextras/lib/apache/commons/$ArrayUtils" {
import {$Class, $Class$Type} from "packages/java/lang/$Class"

export class $ArrayUtils {
static readonly "EMPTY_OBJECT_ARRAY": (any)[]
static readonly "EMPTY_CLASS_ARRAY": ($Class<(any)>)[]
static readonly "EMPTY_STRING_ARRAY": (string)[]
static readonly "EMPTY_LONG_ARRAY": (long)[]
static readonly "EMPTY_LONG_OBJECT_ARRAY": (long)[]
static readonly "EMPTY_INT_ARRAY": (integer)[]
static readonly "EMPTY_INTEGER_OBJECT_ARRAY": (integer)[]
static readonly "EMPTY_SHORT_ARRAY": (short)[]
static readonly "EMPTY_SHORT_OBJECT_ARRAY": (short)[]
static readonly "EMPTY_BYTE_ARRAY": (byte)[]
static readonly "EMPTY_BYTE_OBJECT_ARRAY": (byte)[]
static readonly "EMPTY_DOUBLE_ARRAY": (double)[]
static readonly "EMPTY_DOUBLE_OBJECT_ARRAY": (double)[]
static readonly "EMPTY_FLOAT_ARRAY": (float)[]
static readonly "EMPTY_FLOAT_OBJECT_ARRAY": (float)[]
static readonly "EMPTY_BOOLEAN_ARRAY": (boolean)[]
static readonly "EMPTY_BOOLEAN_OBJECT_ARRAY": (boolean)[]
static readonly "EMPTY_CHAR_ARRAY": (character)[]
static readonly "EMPTY_CHARACTER_OBJECT_ARRAY": (character)[]


public static "add"<T>(array: (T)[], index: integer, element: T): (T)[]
public static "add"<T>(array: (T)[], element: T): (T)[]
public static "remove"(array: (integer)[], index: integer): (integer)[]
public static "remove"<T>(array: (T)[], index: integer): (T)[]
public static "clone"<T>(array: (T)[]): (T)[]
public static "clone"(array: (integer)[]): (integer)[]
public static "getLength"(array: any): integer
public static "addAll"<T>(array1: (T)[], ...array2: (T)[]): (T)[]
public static "addAll"(array1: (integer)[], ...array2: (integer)[]): (integer)[]
public static "subarray"<T>(array: (T)[], startIndexInclusive: integer, endIndexExclusive: integer): (T)[]
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ArrayUtils$Type = ($ArrayUtils);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ArrayUtils_ = $ArrayUtils$Type;
}}
declare module "packages/ca/fxco/memoryleakfix/$MemoryLeakFixExpectPlatform" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $MemoryLeakFixExpectPlatform {

constructor()

public static "compareMinecraftToVersion"(version: string): integer
public static "getMappingType"(): string
public static "isDevEnvironment"(): boolean
public static "isModLoaded"(id: string): boolean
get "mappingType"(): string
get "devEnvironment"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MemoryLeakFixExpectPlatform$Type = ($MemoryLeakFixExpectPlatform);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MemoryLeakFixExpectPlatform_ = $MemoryLeakFixExpectPlatform$Type;
}}
declare module "packages/ca/fxco/memoryleakfix/mixinextras/injector/wrapoperation/$WrapOperationInjectionInfo" {
import {$AnnotationNode, $AnnotationNode$Type} from "packages/org/objectweb/asm/tree/$AnnotationNode"
import {$MixinExtrasInjectionInfo, $MixinExtrasInjectionInfo$Type} from "packages/ca/fxco/memoryleakfix/mixinextras/injector/$MixinExtrasInjectionInfo"
import {$MethodNode, $MethodNode$Type} from "packages/org/objectweb/asm/tree/$MethodNode"
import {$LateApplyingInjectorInfo, $LateApplyingInjectorInfo$Type} from "packages/ca/fxco/memoryleakfix/mixinextras/injector/$LateApplyingInjectorInfo"
import {$MixinTargetContext, $MixinTargetContext$Type} from "packages/org/spongepowered/asm/mixin/transformer/$MixinTargetContext"

export class $WrapOperationInjectionInfo extends $MixinExtrasInjectionInfo implements $LateApplyingInjectorInfo {
static readonly "DEFAULT_PREFIX": string

constructor(mixin: $MixinTargetContext$Type, method: $MethodNode$Type, annotation: $AnnotationNode$Type)

public "wrap"(outer: $LateApplyingInjectorInfo$Type): void
public "prepare"(): void
public "inject"(): void
public "postInject"(): void
public "lateInject"(): void
public "latePostInject"(): void
public static "wrap"(inner: any, outer: $LateApplyingInjectorInfo$Type): boolean
/**
 * 
 * @deprecated
 */
public "lateApply"(): void
public "getMethod"(): $MethodNode
public "getAnnotationNode"(): $AnnotationNode
get "method"(): $MethodNode
get "annotationNode"(): $AnnotationNode
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WrapOperationInjectionInfo$Type = ($WrapOperationInjectionInfo);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WrapOperationInjectionInfo_ = $WrapOperationInjectionInfo$Type;
}}
declare module "packages/ca/fxco/memoryleakfix/mixinextras/utils/$InternalField" {
import {$Class, $Class$Type} from "packages/java/lang/$Class"

export interface $InternalField<O, T> {

 "get"(arg0: O): T
 "set"(arg0: O, arg1: T): void
}

export namespace $InternalField {
function of<O, T>(clazz: string, name: string): $InternalField<(O), (T)>
function of<O, T>(clazz: $Class$Type<(any)>, name: string): $InternalField<(O), (T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InternalField$Type<O, T> = ($InternalField<(O), (T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InternalField_<O, T> = $InternalField$Type<(O), (T)>;
}}
declare module "packages/ca/fxco/memoryleakfix/mixinextras/injector/$ModifyReceiverInjector" {
import {$InjectionInfo, $InjectionInfo$Type} from "packages/org/spongepowered/asm/mixin/injection/struct/$InjectionInfo"
import {$Injector, $Injector$Type} from "packages/org/spongepowered/asm/mixin/injection/code/$Injector"

export class $ModifyReceiverInjector extends $Injector {

constructor(info: $InjectionInfo$Type)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModifyReceiverInjector$Type = ($ModifyReceiverInjector);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModifyReceiverInjector_ = $ModifyReceiverInjector$Type;
}}
declare module "packages/ca/fxco/memoryleakfix/mixinextras/$MixinExtrasBootstrap" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $MixinExtrasBootstrap {

constructor()

public static "init"(): void
/**
 * 
 * @deprecated
 */
public static "getVersion"(): string
get "version"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MixinExtrasBootstrap$Type = ($MixinExtrasBootstrap);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MixinExtrasBootstrap_ = $MixinExtrasBootstrap$Type;
}}
declare module "packages/ca/fxco/memoryleakfix/mixinextras/sugar/impl/ref/$LocalRefUtils" {
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$Type, $Type$Type} from "packages/org/objectweb/asm/$Type"
import {$Runnable, $Runnable$Type} from "packages/java/lang/$Runnable"
import {$InsnList, $InsnList$Type} from "packages/org/objectweb/asm/tree/$InsnList"

export class $LocalRefUtils {

constructor()

public static "getTargetType"(type: $Type$Type, generic: $Type$Type): $Type
public static "generateInitialization"(insns: $InsnList$Type, innerType: $Type$Type): void
public static "generateUnwrapping"(insns: $InsnList$Type, innerType: $Type$Type, load: $Runnable$Type): void
public static "getInterfaceFor"(type: $Type$Type): $Class<(any)>
public static "generateNew"(insns: $InsnList$Type, innerType: $Type$Type): void
public static "generateDisposal"(insns: $InsnList$Type, innerType: $Type$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LocalRefUtils$Type = ($LocalRefUtils);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LocalRefUtils_ = $LocalRefUtils$Type;
}}
declare module "packages/ca/fxco/memoryleakfix/mixinextras/utils/$InternalConstructor" {
import {$Class, $Class$Type} from "packages/java/lang/$Class"

export interface $InternalConstructor<T> {

 "newInstance"(...arg0: (any)[]): T

(clazz: string, ...argTypes: ($Class$Type<(any)>)[]): $InternalConstructor<(T)>
}

export namespace $InternalConstructor {
function of<T>(clazz: string, ...argTypes: ($Class$Type<(any)>)[]): $InternalConstructor<(T)>
function of<T>(clazz: $Class$Type<(any)>, ...argTypes: ($Class$Type<(any)>)[]): $InternalConstructor<(T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InternalConstructor$Type<T> = ($InternalConstructor<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InternalConstructor_<T> = $InternalConstructor$Type<(T)>;
}}
declare module "packages/ca/fxco/memoryleakfix/mixinextras/sugar/impl/$SugarWrapperInjectionInfo" {
import {$AnnotationNode, $AnnotationNode$Type} from "packages/org/objectweb/asm/tree/$AnnotationNode"
import {$MethodNode, $MethodNode$Type} from "packages/org/objectweb/asm/tree/$MethodNode"
import {$WrapperInjectionInfo, $WrapperInjectionInfo$Type} from "packages/ca/fxco/memoryleakfix/mixinextras/wrapper/$WrapperInjectionInfo"
import {$LateApplyingInjectorInfo, $LateApplyingInjectorInfo$Type} from "packages/ca/fxco/memoryleakfix/mixinextras/injector/$LateApplyingInjectorInfo"
import {$MixinTargetContext, $MixinTargetContext$Type} from "packages/org/spongepowered/asm/mixin/transformer/$MixinTargetContext"

export class $SugarWrapperInjectionInfo extends $WrapperInjectionInfo {
static readonly "DEFAULT_PREFIX": string

constructor(mixin: $MixinTargetContext$Type, method: $MethodNode$Type, annotation: $AnnotationNode$Type)

public static "wrap"(inner: any, outer: $LateApplyingInjectorInfo$Type): boolean
public "getMethod"(): $MethodNode
public "getAnnotationNode"(): $AnnotationNode
get "method"(): $MethodNode
get "annotationNode"(): $AnnotationNode
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SugarWrapperInjectionInfo$Type = ($SugarWrapperInjectionInfo);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SugarWrapperInjectionInfo_ = $SugarWrapperInjectionInfo$Type;
}}
declare module "packages/ca/fxco/memoryleakfix/mixinextras/injector/$StackExtension" {
import {$Target, $Target$Type} from "packages/org/spongepowered/asm/mixin/injection/struct/$Target"
import {$Type, $Type$Type} from "packages/org/objectweb/asm/$Type"

export class $StackExtension {

constructor(target: $Target$Type)

public "receiver"(isStatic: boolean): void
public "extra"(size: integer): void
public "capturedArgs"(argTypes: ($Type$Type)[], argCount: integer): void
public "ensureAtLeast"(size: integer): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StackExtension$Type = ($StackExtension);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StackExtension_ = $StackExtension$Type;
}}
declare module "packages/ca/fxco/memoryleakfix/mixinextras/utils/$InternalMethod" {
import {$Class, $Class$Type} from "packages/java/lang/$Class"

export interface $InternalMethod<O, R> {

 "call"(arg0: O, ...arg1: (any)[]): R

(clazz: string, name: string, ...argTypes: ($Class$Type<(any)>)[]): $InternalMethod<(O), (R)>
}

export namespace $InternalMethod {
function of<O, R>(clazz: string, name: string, ...argTypes: ($Class$Type<(any)>)[]): $InternalMethod<(O), (R)>
function of<O, R>(clazz: $Class$Type<(any)>, name: string, ...argTypes: ($Class$Type<(any)>)[]): $InternalMethod<(O), (R)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InternalMethod$Type<O, R> = ($InternalMethod<(O), (R)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InternalMethod_<O, R> = $InternalMethod$Type<(O), (R)>;
}}
declare module "packages/ca/fxco/memoryleakfix/extensions/$ExtendBrain" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $ExtendBrain {

 "memoryLeakFix$clearMemories"(): void

(): void
}

export namespace $ExtendBrain {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ExtendBrain$Type = ($ExtendBrain);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ExtendBrain_ = $ExtendBrain$Type;
}}
declare module "packages/ca/fxco/memoryleakfix/mixinextras/service/$ServiceInitializationExtension" {
import {$ClassNode, $ClassNode$Type} from "packages/org/objectweb/asm/tree/$ClassNode"
import {$MixinExtrasService, $MixinExtrasService$Type} from "packages/ca/fxco/memoryleakfix/mixinextras/service/$MixinExtrasService"
import {$MixinEnvironment, $MixinEnvironment$Type} from "packages/org/spongepowered/asm/mixin/$MixinEnvironment"
import {$ITargetClassContext, $ITargetClassContext$Type} from "packages/org/spongepowered/asm/mixin/transformer/ext/$ITargetClassContext"
import {$IExtension, $IExtension$Type} from "packages/org/spongepowered/asm/mixin/transformer/ext/$IExtension"

export class $ServiceInitializationExtension implements $IExtension {

constructor(service: $MixinExtrasService$Type)

public "export"(env: $MixinEnvironment$Type, name: string, force: boolean, classNode: $ClassNode$Type): void
public "postApply"(context: $ITargetClassContext$Type): void
public "preApply"(context: $ITargetClassContext$Type): void
public "checkActive"(environment: $MixinEnvironment$Type): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ServiceInitializationExtension$Type = ($ServiceInitializationExtension);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ServiceInitializationExtension_ = $ServiceInitializationExtension$Type;
}}
declare module "packages/ca/fxco/memoryleakfix/mixinextras/versions/$MixinVersion" {
import {$AnnotationNode, $AnnotationNode$Type} from "packages/org/objectweb/asm/tree/$AnnotationNode"
import {$LocalVariableDiscriminator$Context, $LocalVariableDiscriminator$Context$Type} from "packages/org/spongepowered/asm/mixin/injection/modify/$LocalVariableDiscriminator$Context"
import {$Target, $Target$Type} from "packages/org/spongepowered/asm/mixin/injection/struct/$Target"
import {$IMixinContext, $IMixinContext$Type} from "packages/org/spongepowered/asm/mixin/refmap/$IMixinContext"
import {$InjectionInfo, $InjectionInfo$Type} from "packages/org/spongepowered/asm/mixin/injection/struct/$InjectionInfo"
import {$RuntimeException, $RuntimeException$Type} from "packages/java/lang/$RuntimeException"
import {$Type, $Type$Type} from "packages/org/objectweb/asm/$Type"
import {$AbstractInsnNode, $AbstractInsnNode$Type} from "packages/org/objectweb/asm/tree/$AbstractInsnNode"

export class $MixinVersion {

constructor()

public static "getInstance"(): $MixinVersion
public "getAnnotation"(arg0: $InjectionInfo$Type): $AnnotationNode
public "makeInvalidInjectionException"(arg0: $InjectionInfo$Type, arg1: string): $RuntimeException
public "preInject"(arg0: $InjectionInfo$Type): void
public "makeLvtContext"(arg0: $InjectionInfo$Type, arg1: $Type$Type, arg2: boolean, arg3: $Target$Type, arg4: $AbstractInsnNode$Type): $LocalVariableDiscriminator$Context
public "getMixin"(arg0: $InjectionInfo$Type): $IMixinContext
get "instance"(): $MixinVersion
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MixinVersion$Type = ($MixinVersion);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MixinVersion_ = $MixinVersion$Type;
}}
declare module "packages/ca/fxco/memoryleakfix/mixinextras/lib/apache/commons/tuple/$ImmutablePair" {
import {$Comparator, $Comparator$Type} from "packages/java/util/$Comparator"
import {$Comparable, $Comparable$Type} from "packages/java/lang/$Comparable"
import {$Pair, $Pair$Type} from "packages/ca/fxco/memoryleakfix/mixinextras/lib/apache/commons/tuple/$Pair"
import {$Map$Entry, $Map$Entry$Type} from "packages/java/util/$Map$Entry"

export class $ImmutablePair<L, R> extends $Pair<(L), (R)> {
readonly "left": L
readonly "right": R

constructor(left: L, right: R)

public "setValue"(value: R): R
public "getRight"(): R
public "getLeft"(): L
public static "copyOf"<K, V>(arg0: $Map$Entry$Type<(any), (any)>): $Map$Entry<(K), (V)>
public static "comparingByKey"<K, V>(arg0: $Comparator$Type<(any)>): $Comparator<($Map$Entry<(K), (V)>)>
public static "comparingByKey"<K extends $Comparable<(any)>, V>(): $Comparator<($Map$Entry<(K), (V)>)>
public static "comparingByValue"<K, V extends $Comparable<(any)>>(): $Comparator<($Map$Entry<(K), (V)>)>
public static "comparingByValue"<K, V>(arg0: $Comparator$Type<(any)>): $Comparator<($Map$Entry<(K), (V)>)>
set "value"(value: R)
get "right"(): R
get "left"(): L
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ImmutablePair$Type<L, R> = ($ImmutablePair<(L), (R)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ImmutablePair_<L, R> = $ImmutablePair$Type<(L), (R)>;
}}
declare module "packages/ca/fxco/memoryleakfix/mixinextras/lib/apache/commons/$StringUtils" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $StringUtils {


public static "isEmpty"(cs: charseq): boolean
public static "replace"(text: string, searchString: string, replacement: string, max: integer): string
public static "repeat"(ch: character, repeat: integer): string
public static "substringBeforeLast"(str: string, separator: string): string
public static "removeEnd"(str: string, remove: string): string
public static "replaceOnce"(text: string, searchString: string, replacement: string): string
public static "removeStart"(str: string, remove: string): string
public static "substringAfterLast"(str: string, separator: string): string
public static "substringAfter"(str: string, separator: string): string
public static "substringBefore"(str: string, separator: string): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StringUtils$Type = ($StringUtils);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StringUtils_ = $StringUtils$Type;
}}
declare module "packages/ca/fxco/memoryleakfix/mixinextras/utils/$ASMUtils" {
import {$AnnotationNode, $AnnotationNode$Type} from "packages/org/objectweb/asm/tree/$AnnotationNode"
import {$ClassNode, $ClassNode$Type} from "packages/org/objectweb/asm/tree/$ClassNode"
import {$MethodInsnNode, $MethodInsnNode$Type} from "packages/org/objectweb/asm/tree/$MethodInsnNode"
import {$MethodNode, $MethodNode$Type} from "packages/org/objectweb/asm/tree/$MethodNode"
import {$Target, $Target$Type} from "packages/org/spongepowered/asm/mixin/injection/struct/$Target"
import {$Type, $Type$Type} from "packages/org/objectweb/asm/$Type"
import {$TypeInsnNode, $TypeInsnNode$Type} from "packages/org/objectweb/asm/tree/$TypeInsnNode"

export class $ASMUtils {

constructor()

public static "isPrimitive"(type: $Type$Type): boolean
public static "getDummyOpcodeForType"(type: $Type$Type): integer
public static "annotationToString"(annotation: $AnnotationNode$Type): string
public static "typeToString"(type: $Type$Type): string
public static "getInvokeInstruction"(owner: $ClassNode$Type, method: $MethodNode$Type): $MethodInsnNode
public static "findInitNodeFor"(target: $Target$Type, newNode: $TypeInsnNode$Type): $MethodInsnNode
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ASMUtils$Type = ($ASMUtils);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ASMUtils_ = $ASMUtils$Type;
}}
declare module "packages/ca/fxco/memoryleakfix/mixinextras/injector/$ModifyReturnValueInjectionInfo" {
import {$AnnotationNode, $AnnotationNode$Type} from "packages/org/objectweb/asm/tree/$AnnotationNode"
import {$MixinExtrasInjectionInfo, $MixinExtrasInjectionInfo$Type} from "packages/ca/fxco/memoryleakfix/mixinextras/injector/$MixinExtrasInjectionInfo"
import {$MethodNode, $MethodNode$Type} from "packages/org/objectweb/asm/tree/$MethodNode"
import {$MixinTargetContext, $MixinTargetContext$Type} from "packages/org/spongepowered/asm/mixin/transformer/$MixinTargetContext"

export class $ModifyReturnValueInjectionInfo extends $MixinExtrasInjectionInfo {
static readonly "DEFAULT_PREFIX": string

constructor(mixin: $MixinTargetContext$Type, method: $MethodNode$Type, annotation: $AnnotationNode$Type)

public "getMethod"(): $MethodNode
public "getAnnotationNode"(): $AnnotationNode
get "method"(): $MethodNode
get "annotationNode"(): $AnnotationNode
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModifyReturnValueInjectionInfo$Type = ($ModifyReturnValueInjectionInfo);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModifyReturnValueInjectionInfo_ = $ModifyReturnValueInjectionInfo$Type;
}}
declare module "packages/ca/fxco/memoryleakfix/mixinextras/service/$MixinExtrasServiceImpl" {
import {$MixinExtrasVersion, $MixinExtrasVersion$Type} from "packages/ca/fxco/memoryleakfix/mixinextras/service/$MixinExtrasVersion"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$MixinExtrasService, $MixinExtrasService$Type} from "packages/ca/fxco/memoryleakfix/mixinextras/service/$MixinExtrasService"
import {$Type, $Type$Type} from "packages/org/objectweb/asm/$Type"
import {$IExtension, $IExtension$Type} from "packages/org/spongepowered/asm/mixin/transformer/ext/$IExtension"

export class $MixinExtrasServiceImpl implements $MixinExtrasService {

constructor()

public "toString"(): string
public "initialize"(): void
public "getVersion"(): integer
public "getAllClassNamesAtLeast"(ourName: string, minVersion: $MixinExtrasVersion$Type): $Set<(string)>
public "takeControlFrom"(olderService: any): void
public "offerExtension"(version: integer, extension: $IExtension$Type): void
public "offerPackage"(version: integer, packageName: string): void
public "concedeTo"(newerService: any, wasActive: boolean): void
public "offerInjector"(version: integer, injector: $Class$Type<(any)>): void
public "shouldReplace"(otherService: any): boolean
public "changePackage"(ourType: $Class$Type<(any)>, theirReference: $Type$Type, ourReference: $Class$Type<(any)>): $Type
public "getAllClassNames"(ourName: string): $Set<(string)>
public "isClassOwned"(name: string): boolean
public static "getInstance"(): $MixinExtrasServiceImpl
public static "setup"(): void
public static "getFrom"(serviceImpl: any): $MixinExtrasService
get "version"(): integer
get "instance"(): $MixinExtrasServiceImpl
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MixinExtrasServiceImpl$Type = ($MixinExtrasServiceImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MixinExtrasServiceImpl_ = $MixinExtrasServiceImpl$Type;
}}
declare module "packages/ca/fxco/memoryleakfix/extensions/$ExtendDrowned" {
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$PathNavigation, $PathNavigation$Type} from "packages/net/minecraft/world/entity/ai/navigation/$PathNavigation"

export interface $ExtendDrowned {

 "memoryLeakFix$onRemoveNavigation"(arg0: $Set$Type<($PathNavigation$Type)>): void

(arg0: $Set$Type<($PathNavigation$Type)>): void
}

export namespace $ExtendDrowned {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ExtendDrowned$Type = ($ExtendDrowned);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ExtendDrowned_ = $ExtendDrowned$Type;
}}
declare module "packages/ca/fxco/memoryleakfix/mixinextras/sugar/impl/handlers/$HandlerInfo" {
import {$BiConsumer, $BiConsumer$Type} from "packages/java/util/function/$BiConsumer"
import {$ClassNode, $ClassNode$Type} from "packages/org/objectweb/asm/tree/$ClassNode"
import {$MethodNode, $MethodNode$Type} from "packages/org/objectweb/asm/tree/$MethodNode"
import {$ArrayList, $ArrayList$Type} from "packages/java/util/$ArrayList"
import {$Type, $Type$Type} from "packages/org/objectweb/asm/$Type"
import {$SugarParameter, $SugarParameter$Type} from "packages/ca/fxco/memoryleakfix/mixinextras/sugar/impl/$SugarParameter"
import {$Runnable, $Runnable$Type} from "packages/java/lang/$Runnable"
import {$InsnList, $InsnList$Type} from "packages/org/objectweb/asm/tree/$InsnList"

export class $HandlerInfo {

constructor()

public "transformHandler"(targetClass: $ClassNode$Type, handler: $MethodNode$Type): void
public "transformGenerics"(generics: $ArrayList$Type<($Type$Type)>): void
public "wrapParameter"(param: $SugarParameter$Type, type: $Type$Type, generic: $Type$Type, unwrap: $BiConsumer$Type<($InsnList$Type), ($Runnable$Type)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $HandlerInfo$Type = ($HandlerInfo);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $HandlerInfo_ = $HandlerInfo$Type;
}}
declare module "packages/ca/fxco/memoryleakfix/mixinextras/utils/$MixinExtrasLogger" {
import {$Throwable, $Throwable$Type} from "packages/java/lang/$Throwable"

export interface $MixinExtrasLogger {

 "info"(arg0: string, ...arg1: (any)[]): void
 "debug"(arg0: string, ...arg1: (any)[]): void
 "error"(arg0: string, arg1: $Throwable$Type): void
 "warn"(arg0: string, ...arg1: (any)[]): void
}

export namespace $MixinExtrasLogger {
function get(name: string): $MixinExtrasLogger
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MixinExtrasLogger$Type = ($MixinExtrasLogger);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MixinExtrasLogger_ = $MixinExtrasLogger$Type;
}}
declare module "packages/ca/fxco/memoryleakfix/mixinextras/utils/$CompatibilityHelper" {
import {$AnnotationNode, $AnnotationNode$Type} from "packages/org/objectweb/asm/tree/$AnnotationNode"
import {$LocalVariableDiscriminator$Context, $LocalVariableDiscriminator$Context$Type} from "packages/org/spongepowered/asm/mixin/injection/modify/$LocalVariableDiscriminator$Context"
import {$Target, $Target$Type} from "packages/org/spongepowered/asm/mixin/injection/struct/$Target"
import {$IMixinContext, $IMixinContext$Type} from "packages/org/spongepowered/asm/mixin/refmap/$IMixinContext"
import {$InjectionInfo, $InjectionInfo$Type} from "packages/org/spongepowered/asm/mixin/injection/struct/$InjectionInfo"
import {$RuntimeException, $RuntimeException$Type} from "packages/java/lang/$RuntimeException"
import {$Type, $Type$Type} from "packages/org/objectweb/asm/$Type"
import {$AbstractInsnNode, $AbstractInsnNode$Type} from "packages/org/objectweb/asm/tree/$AbstractInsnNode"

export class $CompatibilityHelper {

constructor()

public static "getAnnotation"(info: $InjectionInfo$Type): $AnnotationNode
public static "makeInvalidInjectionException"(info: $InjectionInfo$Type, message: string): $RuntimeException
public static "preInject"(info: $InjectionInfo$Type): void
public static "makeLvtContext"(info: $InjectionInfo$Type, returnType: $Type$Type, argsOnly: boolean, target: $Target$Type, node: $AbstractInsnNode$Type): $LocalVariableDiscriminator$Context
public static "getMixin"(info: $InjectionInfo$Type): $IMixinContext
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CompatibilityHelper$Type = ($CompatibilityHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CompatibilityHelper_ = $CompatibilityHelper$Type;
}}
declare module "packages/ca/fxco/memoryleakfix/config/mixinExtension/$UnMixinExtension" {
import {$ClassNode, $ClassNode$Type} from "packages/org/objectweb/asm/tree/$ClassNode"
import {$MixinEnvironment, $MixinEnvironment$Type} from "packages/org/spongepowered/asm/mixin/$MixinEnvironment"
import {$ITargetClassContext, $ITargetClassContext$Type} from "packages/org/spongepowered/asm/mixin/transformer/ext/$ITargetClassContext"
import {$IExtension, $IExtension$Type} from "packages/org/spongepowered/asm/mixin/transformer/ext/$IExtension"

export class $UnMixinExtension implements $IExtension {

constructor()

public "export"(env: $MixinEnvironment$Type, name: string, force: boolean, classNode: $ClassNode$Type): void
public "postApply"(context: $ITargetClassContext$Type): void
public "preApply"(context: $ITargetClassContext$Type): void
public "checkActive"(environment: $MixinEnvironment$Type): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $UnMixinExtension$Type = ($UnMixinExtension);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $UnMixinExtension_ = $UnMixinExtension$Type;
}}
declare module "packages/ca/fxco/memoryleakfix/mixinextras/injector/$WrapWithCondition" {
import {$Annotation, $Annotation$Type} from "packages/java/lang/annotation/$Annotation"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$At, $At$Type} from "packages/org/spongepowered/asm/mixin/injection/$At"
import {$Slice, $Slice$Type} from "packages/org/spongepowered/asm/mixin/injection/$Slice"

export interface $WrapWithCondition extends $Annotation {

 "method"(): (string)[]
 "allow"(): integer
 "at"(): ($At)[]
 "slice"(): ($Slice)[]
 "expect"(): integer
 "require"(): integer
 "remap"(): boolean
 "equals"(arg0: any): boolean
 "toString"(): string
 "hashCode"(): integer
 "annotationType"(): $Class<(any)>
}

export namespace $WrapWithCondition {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WrapWithCondition$Type = ($WrapWithCondition);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WrapWithCondition_ = $WrapWithCondition$Type;
}}
declare module "packages/ca/fxco/memoryleakfix/$MemoryLeakFix" {
import {$Logger, $Logger$Type} from "packages/org/apache/logging/log4j/$Logger"

export class $MemoryLeakFix {
static readonly "MOD_ID": string
static readonly "LOGGER": $Logger

constructor()

public static "init"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MemoryLeakFix$Type = ($MemoryLeakFix);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MemoryLeakFix_ = $MemoryLeakFix$Type;
}}
declare module "packages/ca/fxco/memoryleakfix/mixinextras/injector/$WrapWithConditionInjector" {
import {$InjectionInfo, $InjectionInfo$Type} from "packages/org/spongepowered/asm/mixin/injection/struct/$InjectionInfo"
import {$Injector, $Injector$Type} from "packages/org/spongepowered/asm/mixin/injection/code/$Injector"

export class $WrapWithConditionInjector extends $Injector {

constructor(info: $InjectionInfo$Type)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WrapWithConditionInjector$Type = ($WrapWithConditionInjector);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WrapWithConditionInjector_ = $WrapWithConditionInjector$Type;
}}
declare module "packages/ca/fxco/memoryleakfix/mixinextras/injector/$ModifyExpressionValueInjector" {
import {$InjectionInfo, $InjectionInfo$Type} from "packages/org/spongepowered/asm/mixin/injection/struct/$InjectionInfo"
import {$Injector, $Injector$Type} from "packages/org/spongepowered/asm/mixin/injection/code/$Injector"

export class $ModifyExpressionValueInjector extends $Injector {

constructor(info: $InjectionInfo$Type)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModifyExpressionValueInjector$Type = ($ModifyExpressionValueInjector);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModifyExpressionValueInjector_ = $ModifyExpressionValueInjector$Type;
}}
declare module "packages/ca/fxco/memoryleakfix/mixinextras/wrapper/factory/$FactoryRedirectWrapper" {
import {$Annotation, $Annotation$Type} from "packages/java/lang/annotation/$Annotation"
import {$Class, $Class$Type} from "packages/java/lang/$Class"

export interface $FactoryRedirectWrapper extends $Annotation {

 "equals"(arg0: any): boolean
 "toString"(): string
 "hashCode"(): integer
 "annotationType"(): $Class<(any)>
}

export namespace $FactoryRedirectWrapper {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FactoryRedirectWrapper$Type = ($FactoryRedirectWrapper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FactoryRedirectWrapper_ = $FactoryRedirectWrapper$Type;
}}
declare module "packages/ca/fxco/memoryleakfix/mixinextras/injector/$LateApplyingInjectorInfo" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $LateApplyingInjectorInfo {

 "wrap"(arg0: $LateApplyingInjectorInfo$Type): void
 "lateInject"(): void
 "latePostInject"(): void
/**
 * 
 * @deprecated
 */
 "lateApply"(): void
}

export namespace $LateApplyingInjectorInfo {
function wrap(inner: any, outer: $LateApplyingInjectorInfo$Type): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LateApplyingInjectorInfo$Type = ($LateApplyingInjectorInfo);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LateApplyingInjectorInfo_ = $LateApplyingInjectorInfo$Type;
}}
declare module "packages/ca/fxco/memoryleakfix/mixinextras/utils/$TargetDecorations" {
import {$Target, $Target$Type} from "packages/org/spongepowered/asm/mixin/injection/struct/$Target"
import {$UnaryOperator, $UnaryOperator$Type} from "packages/java/util/function/$UnaryOperator"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"

export class $TargetDecorations {

constructor()

public static "remove"(target: $Target$Type, key: string): void
public static "get"<T>(target: $Target$Type, key: string): T
public static "put"(target: $Target$Type, key: string, value: any): void
public static "has"(target: $Target$Type, key: string): boolean
public static "modify"<T>(target: $Target$Type, key: string, operator: $UnaryOperator$Type<(T)>): void
public static "getOrPut"<T>(target: $Target$Type, key: string, supplier: $Supplier$Type<(T)>): T
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TargetDecorations$Type = ($TargetDecorations);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TargetDecorations_ = $TargetDecorations$Type;
}}
declare module "packages/ca/fxco/memoryleakfix/mixinextras/injector/$ModifyExpressionValueInjectionInfo" {
import {$AnnotationNode, $AnnotationNode$Type} from "packages/org/objectweb/asm/tree/$AnnotationNode"
import {$MixinExtrasInjectionInfo, $MixinExtrasInjectionInfo$Type} from "packages/ca/fxco/memoryleakfix/mixinextras/injector/$MixinExtrasInjectionInfo"
import {$MethodNode, $MethodNode$Type} from "packages/org/objectweb/asm/tree/$MethodNode"
import {$MixinTargetContext, $MixinTargetContext$Type} from "packages/org/spongepowered/asm/mixin/transformer/$MixinTargetContext"

export class $ModifyExpressionValueInjectionInfo extends $MixinExtrasInjectionInfo {
static readonly "DEFAULT_PREFIX": string

constructor(mixin: $MixinTargetContext$Type, method: $MethodNode$Type, annotation: $AnnotationNode$Type)

public "prepare"(): void
public "getMethod"(): $MethodNode
public "getAnnotationNode"(): $AnnotationNode
get "method"(): $MethodNode
get "annotationNode"(): $AnnotationNode
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModifyExpressionValueInjectionInfo$Type = ($ModifyExpressionValueInjectionInfo);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModifyExpressionValueInjectionInfo_ = $ModifyExpressionValueInjectionInfo$Type;
}}
declare module "packages/ca/fxco/memoryleakfix/mixinextras/sugar/impl/ref/generated/$GeneratedImplDummy" {
import {$MethodHandles$Lookup, $MethodHandles$Lookup$Type} from "packages/java/lang/invoke/$MethodHandles$Lookup"

export class $GeneratedImplDummy {

constructor()

public static "getLookup"(): $MethodHandles$Lookup
get "lookup"(): $MethodHandles$Lookup
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GeneratedImplDummy$Type = ($GeneratedImplDummy);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GeneratedImplDummy_ = $GeneratedImplDummy$Type;
}}
declare module "packages/ca/fxco/memoryleakfix/mixinextras/injector/$ModifyReturnValueInjector" {
import {$InjectionInfo, $InjectionInfo$Type} from "packages/org/spongepowered/asm/mixin/injection/struct/$InjectionInfo"
import {$Injector, $Injector$Type} from "packages/org/spongepowered/asm/mixin/injection/code/$Injector"

export class $ModifyReturnValueInjector extends $Injector {

constructor(info: $InjectionInfo$Type)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModifyReturnValueInjector$Type = ($ModifyReturnValueInjector);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModifyReturnValueInjector_ = $ModifyReturnValueInjector$Type;
}}
declare module "packages/ca/fxco/memoryleakfix/mixinextras/injector/$ModifyReturnValue" {
import {$Annotation, $Annotation$Type} from "packages/java/lang/annotation/$Annotation"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$At, $At$Type} from "packages/org/spongepowered/asm/mixin/injection/$At"
import {$Slice, $Slice$Type} from "packages/org/spongepowered/asm/mixin/injection/$Slice"

export interface $ModifyReturnValue extends $Annotation {

 "method"(): (string)[]
 "allow"(): integer
 "at"(): ($At)[]
 "slice"(): ($Slice)[]
 "expect"(): integer
 "require"(): integer
 "remap"(): boolean
 "equals"(arg0: any): boolean
 "toString"(): string
 "hashCode"(): integer
 "annotationType"(): $Class<(any)>
}

export namespace $ModifyReturnValue {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModifyReturnValue$Type = ($ModifyReturnValue);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModifyReturnValue_ = $ModifyReturnValue$Type;
}}
declare module "packages/ca/fxco/memoryleakfix/config/$Remaps" {
import {$Annotation, $Annotation$Type} from "packages/java/lang/annotation/$Annotation"
import {$Remap, $Remap$Type} from "packages/ca/fxco/memoryleakfix/config/$Remap"
import {$Class, $Class$Type} from "packages/java/lang/$Class"

export interface $Remaps extends $Annotation {

 "value"(): ($Remap)[]
 "equals"(arg0: any): boolean
 "toString"(): string
 "hashCode"(): integer
 "annotationType"(): $Class<(any)>
}

export namespace $Remaps {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Remaps$Type = ($Remaps);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Remaps_ = $Remaps$Type;
}}

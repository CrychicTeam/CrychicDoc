declare module "packages/org/spongepowered/asm/logging/$Level" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $Level extends $Enum<($Level)> {
static readonly "FATAL": $Level
static readonly "ERROR": $Level
static readonly "WARN": $Level
static readonly "INFO": $Level
static readonly "DEBUG": $Level
static readonly "TRACE": $Level


public static "values"(): ($Level)[]
public static "valueOf"(arg0: string): $Level
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Level$Type = (("warn") | ("trace") | ("debug") | ("error") | ("fatal") | ("info")) | ($Level);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Level_ = $Level$Type;
}}
declare module "packages/org/spongepowered/asm/service/$ISyntheticClassInfo" {
import {$IMixinInfo, $IMixinInfo$Type} from "packages/org/spongepowered/asm/mixin/extensibility/$IMixinInfo"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export interface $ISyntheticClassInfo {

 "getName"(): string
 "getClassName"(): string
 "isLoaded"(): boolean
 "getMixin"(): $IMixinInfo
}

export namespace $ISyntheticClassInfo {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ISyntheticClassInfo$Type = ($ISyntheticClassInfo);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ISyntheticClassInfo_ = $ISyntheticClassInfo$Type;
}}
declare module "packages/org/spongepowered/asm/mixin/injection/$At$Shift" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $At$Shift extends $Enum<($At$Shift)> {
static readonly "NONE": $At$Shift
static readonly "BEFORE": $At$Shift
static readonly "AFTER": $At$Shift
static readonly "BY": $At$Shift


public static "values"(): ($At$Shift)[]
public static "valueOf"(arg0: string): $At$Shift
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $At$Shift$Type = (("before") | ("by") | ("none") | ("after")) | ($At$Shift);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $At$Shift_ = $At$Shift$Type;
}}
declare module "packages/org/spongepowered/asm/util/$ITokenProvider" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export interface $ITokenProvider {

 "getToken"(arg0: string): integer

(arg0: string): integer
}

export namespace $ITokenProvider {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ITokenProvider$Type = ($ITokenProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ITokenProvider_ = $ITokenProvider$Type;
}}
declare module "packages/org/spongepowered/asm/mixin/injection/code/$ISliceContext" {
import {$AnnotationNode, $AnnotationNode$Type} from "packages/org/objectweb/asm/tree/$AnnotationNode"
import {$MethodNode, $MethodNode$Type} from "packages/org/objectweb/asm/tree/$MethodNode"
import {$MethodSlice, $MethodSlice$Type} from "packages/org/spongepowered/asm/mixin/injection/code/$MethodSlice"
import {$IAnnotationHandle, $IAnnotationHandle$Type} from "packages/org/spongepowered/asm/util/asm/$IAnnotationHandle"
import {$IMixinContext, $IMixinContext$Type} from "packages/org/spongepowered/asm/mixin/refmap/$IMixinContext"
import {$IInjectionPointContext, $IInjectionPointContext$Type} from "packages/org/spongepowered/asm/mixin/injection/$IInjectionPointContext"
import {$ISelectorContext, $ISelectorContext$Type} from "packages/org/spongepowered/asm/mixin/injection/selectors/$ISelectorContext"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export interface $ISliceContext extends $IInjectionPointContext {

 "getSlice"(arg0: string): $MethodSlice
 "getMethod"(): $MethodNode
 "getAnnotationNode"(): $AnnotationNode
 "addMessage"(arg0: string, ...arg1: (any)[]): void
 "getAnnotation"(): $IAnnotationHandle
 "getParent"(): $ISelectorContext
 "remap"(arg0: string): string
 "getMixin"(): $IMixinContext
 "getSelectorAnnotation"(): $IAnnotationHandle
 "getSelectorCoordinate"(arg0: boolean): string
}

export namespace $ISliceContext {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ISliceContext$Type = ($ISliceContext);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ISliceContext_ = $ISliceContext$Type;
}}
declare module "packages/org/spongepowered/asm/logging/$ILogger" {
import {$Throwable, $Throwable$Type} from "packages/java/lang/$Throwable"
import {$Level, $Level$Type} from "packages/org/spongepowered/asm/logging/$Level"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export interface $ILogger {

 "log"(arg0: $Level$Type, arg1: string, ...arg2: (any)[]): void
 "log"(arg0: $Level$Type, arg1: string, arg2: $Throwable$Type): void
 "info"(arg0: string, ...arg1: (any)[]): void
 "info"(arg0: string, arg1: $Throwable$Type): void
 "trace"(arg0: string, ...arg1: (any)[]): void
 "trace"(arg0: string, arg1: $Throwable$Type): void
 "debug"(arg0: string, arg1: $Throwable$Type): void
 "debug"(arg0: string, ...arg1: (any)[]): void
 "getId"(): string
 "getType"(): string
 "error"(arg0: string, ...arg1: (any)[]): void
 "error"(arg0: string, arg1: $Throwable$Type): void
 "warn"(arg0: string, ...arg1: (any)[]): void
 "warn"(arg0: string, arg1: $Throwable$Type): void
 "throwing"<T extends $Throwable>(arg0: T): T
 "fatal"(arg0: string, arg1: $Throwable$Type): void
 "fatal"(arg0: string, ...arg1: (any)[]): void
 "catching"(arg0: $Level$Type, arg1: $Throwable$Type): void
 "catching"(arg0: $Throwable$Type): void
}

export namespace $ILogger {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ILogger$Type = ($ILogger);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ILogger_ = $ILogger$Type;
}}
declare module "packages/org/spongepowered/asm/util/perf/$Profiler$Section" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $Profiler$Section {


public "getName"(): string
public "toString"(): string
public "next"(arg0: string): $Profiler$Section
public "end"(): $Profiler$Section
public "getCount"(): integer
public "getTime"(): long
public "getSeconds"(): double
public "getTotalSeconds"(): double
public "isRoot"(): boolean
public "getInfo"(): string
public "getTimes"(): (long)[]
public "isFine"(): boolean
public "setInfo"(arg0: string): void
public "getBaseName"(): string
public "getTotalTime"(): long
public "getTotalCount"(): integer
public "getAverageTime"(): double
public "getTotalAverageTime"(): double
get "name"(): string
get "count"(): integer
get "time"(): long
get "seconds"(): double
get "totalSeconds"(): double
get "root"(): boolean
get "info"(): string
get "times"(): (long)[]
get "fine"(): boolean
set "info"(value: string)
get "baseName"(): string
get "totalTime"(): long
get "totalCount"(): integer
get "averageTime"(): double
get "totalAverageTime"(): double
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Profiler$Section$Type = ($Profiler$Section);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Profiler$Section_ = $Profiler$Section$Type;
}}
declare module "packages/org/spongepowered/asm/mixin/transformer/$ClassInfo$FrameData" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $ClassInfo$FrameData {
readonly "index": integer
readonly "type": integer
readonly "locals": integer
readonly "size": integer


public "toString"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClassInfo$FrameData$Type = ($ClassInfo$FrameData);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClassInfo$FrameData_ = $ClassInfo$FrameData$Type;
}}
declare module "packages/org/spongepowered/asm/mixin/struct/$SpecialMethodInfo" {
import {$AnnotationNode, $AnnotationNode$Type} from "packages/org/objectweb/asm/tree/$AnnotationNode"
import {$ClassNode, $ClassNode$Type} from "packages/org/objectweb/asm/tree/$ClassNode"
import {$MethodNode, $MethodNode$Type} from "packages/org/objectweb/asm/tree/$MethodNode"
import {$AnnotatedMethodInfo, $AnnotatedMethodInfo$Type} from "packages/org/spongepowered/asm/mixin/struct/$AnnotatedMethodInfo"
import {$ClassInfo, $ClassInfo$Type} from "packages/org/spongepowered/asm/mixin/transformer/$ClassInfo"
import {$MixinTargetContext, $MixinTargetContext$Type} from "packages/org/spongepowered/asm/mixin/transformer/$MixinTargetContext"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $SpecialMethodInfo extends $AnnotatedMethodInfo {

constructor(arg0: $MixinTargetContext$Type, arg1: $MethodNode$Type, arg2: $AnnotationNode$Type)

public "getMethodName"(): string
public "getClassInfo"(): $ClassInfo
public "getClassNode"(): $ClassNode
get "methodName"(): string
get "classInfo"(): $ClassInfo
get "classNode"(): $ClassNode
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SpecialMethodInfo$Type = ($SpecialMethodInfo);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SpecialMethodInfo_ = $SpecialMethodInfo$Type;
}}
declare module "packages/org/spongepowered/asm/mixin/injection/struct/$InjectorGroupInfo$Map" {
import {$AnnotationNode, $AnnotationNode$Type} from "packages/org/objectweb/asm/tree/$AnnotationNode"
import {$InjectorGroupInfo, $InjectorGroupInfo$Type} from "packages/org/spongepowered/asm/mixin/injection/struct/$InjectorGroupInfo"
import {$MethodNode, $MethodNode$Type} from "packages/org/objectweb/asm/tree/$MethodNode"
import {$HashMap, $HashMap$Type} from "packages/java/util/$HashMap"
import {$Map, $Map$Type} from "packages/java/util/$Map"
import {$Map$Entry, $Map$Entry$Type} from "packages/java/util/$Map$Entry"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $InjectorGroupInfo$Map extends $HashMap<(string), ($InjectorGroupInfo)> {

constructor()

public "get"(arg0: any): $InjectorGroupInfo
public "forName"(arg0: string): $InjectorGroupInfo
public "parseGroup"(arg0: $AnnotationNode$Type, arg1: string): $InjectorGroupInfo
public "parseGroup"(arg0: $MethodNode$Type, arg1: string): $InjectorGroupInfo
public "validateAll"(): void
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
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InjectorGroupInfo$Map$Type = ($InjectorGroupInfo$Map);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InjectorGroupInfo$Map_ = $InjectorGroupInfo$Map$Type;
}}
declare module "packages/org/spongepowered/asm/mixin/injection/struct/$Target" {
import {$Comparable, $Comparable$Type} from "packages/java/lang/$Comparable"
import {$ClassNode, $ClassNode$Type} from "packages/org/objectweb/asm/tree/$ClassNode"
import {$Target$Extension, $Target$Extension$Type} from "packages/org/spongepowered/asm/mixin/injection/struct/$Target$Extension"
import {$MethodNode, $MethodNode$Type} from "packages/org/objectweb/asm/tree/$MethodNode"
import {$LabelNode, $LabelNode$Type} from "packages/org/objectweb/asm/tree/$LabelNode"
import {$Spliterator, $Spliterator$Type} from "packages/java/util/$Spliterator"
import {$Iterable, $Iterable$Type} from "packages/java/lang/$Iterable"
import {$InsnList, $InsnList$Type} from "packages/org/objectweb/asm/tree/$InsnList"
import {$AbstractInsnNode, $AbstractInsnNode$Type} from "packages/org/objectweb/asm/tree/$AbstractInsnNode"
import {$InjectionNodes$InjectionNode, $InjectionNodes$InjectionNode$Type} from "packages/org/spongepowered/asm/mixin/injection/struct/$InjectionNodes$InjectionNode"
import {$MethodInsnNode, $MethodInsnNode$Type} from "packages/org/objectweb/asm/tree/$MethodInsnNode"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$Bytecode$DelegateInitialiser, $Bytecode$DelegateInitialiser$Type} from "packages/org/spongepowered/asm/util/$Bytecode$DelegateInitialiser"
import {$Type, $Type$Type} from "packages/org/objectweb/asm/$Type"
import {$Iterator, $Iterator$Type} from "packages/java/util/$Iterator"
import {$TypeInsnNode, $TypeInsnNode$Type} from "packages/org/objectweb/asm/tree/$TypeInsnNode"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $Target implements $Comparable<($Target)>, $Iterable<($AbstractInsnNode)> {
readonly "classNode": $ClassNode
readonly "method": $MethodNode
readonly "insns": $InsnList
readonly "isStatic": boolean
readonly "isCtor": boolean
readonly "arguments": ($Type)[]
readonly "returnType": $Type

constructor(arg0: $ClassNode$Type, arg1: $MethodNode$Type)

public "get"(arg0: integer): $AbstractInsnNode
public "toString"(): string
public "compareTo"(arg0: $Target$Type): integer
public "indexOf"(arg0: $AbstractInsnNode$Type): integer
public "indexOf"(arg0: $InjectionNodes$InjectionNode$Type): integer
public "iterator"(): $Iterator<($AbstractInsnNode)>
public "replaceNode"(arg0: $AbstractInsnNode$Type, arg1: $InsnList$Type): void
public "replaceNode"(arg0: $AbstractInsnNode$Type, arg1: $AbstractInsnNode$Type, arg2: $InsnList$Type): void
public "replaceNode"(arg0: $AbstractInsnNode$Type, arg1: $AbstractInsnNode$Type): void
public "removeNode"(arg0: $AbstractInsnNode$Type): void
public "getMaxLocals"(): integer
public "getMaxStack"(): integer
public "findDelegateInitNode"(): $Bytecode$DelegateInitialiser
public "getCallbackInfoClass"(): string
public "allocateLocal"(): integer
public "getCurrentMaxStack"(): integer
public "insertBefore"(arg0: $AbstractInsnNode$Type, arg1: $InsnList$Type): void
public "insertBefore"(arg0: $InjectionNodes$InjectionNode$Type, arg1: $InsnList$Type): void
public "addInjectionNode"(arg0: $AbstractInsnNode$Type): $InjectionNodes$InjectionNode
public "generateArgMap"(arg0: ($Type$Type)[], arg1: integer): (integer)[]
public "addLocalVariable"(arg0: integer, arg1: string, arg2: string, arg3: $LabelNode$Type, arg4: $LabelNode$Type): void
public "addLocalVariable"(arg0: integer, arg1: string, arg2: string): void
public "extendLocals"(): $Target$Extension
public "extendStack"(): $Target$Extension
public "getInjectionNode"(arg0: $AbstractInsnNode$Type): $InjectionNodes$InjectionNode
public "getArgIndices"(): (integer)[]
public "allocateLocals"(arg0: integer): integer
public "wrapNode"(arg0: $AbstractInsnNode$Type, arg1: $AbstractInsnNode$Type, arg2: $InsnList$Type, arg3: $InsnList$Type): void
public "getCallbackDescriptor"(arg0: boolean, arg1: ($Type$Type)[], arg2: ($Type$Type)[], arg3: integer, arg4: integer): string
public "getCallbackDescriptor"(arg0: ($Type$Type)[], arg1: ($Type$Type)[]): string
public "getCurrentMaxLocals"(): integer
public "getSimpleCallbackDescriptor"(): string
public "findInitNodeFor"(arg0: $TypeInsnNode$Type): $MethodInsnNode
public "spliterator"(): $Spliterator<($AbstractInsnNode)>
public "forEach"(arg0: $Consumer$Type<(any)>): void
[Symbol.iterator](): IterableIterator<$AbstractInsnNode>;
get "maxLocals"(): integer
get "maxStack"(): integer
get "callbackInfoClass"(): string
get "currentMaxStack"(): integer
get "argIndices"(): (integer)[]
get "currentMaxLocals"(): integer
get "simpleCallbackDescriptor"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Target$Type = ($Target);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Target_ = $Target$Type;
}}
declare module "packages/org/spongepowered/asm/mixin/injection/struct/$InjectionPointData" {
import {$AnnotationNode, $AnnotationNode$Type} from "packages/org/objectweb/asm/tree/$AnnotationNode"
import {$MethodNode, $MethodNode$Type} from "packages/org/objectweb/asm/tree/$MethodNode"
import {$IMixinContext, $IMixinContext$Type} from "packages/org/spongepowered/asm/mixin/refmap/$IMixinContext"
import {$IInjectionPointContext, $IInjectionPointContext$Type} from "packages/org/spongepowered/asm/mixin/injection/$IInjectionPointContext"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ITargetSelector, $ITargetSelector$Type} from "packages/org/spongepowered/asm/mixin/injection/selectors/$ITargetSelector"
import {$LocalVariableDiscriminator, $LocalVariableDiscriminator$Type} from "packages/org/spongepowered/asm/mixin/injection/modify/$LocalVariableDiscriminator"
import {$Type, $Type$Type} from "packages/org/objectweb/asm/$Type"
import {$InjectionPoint$Selector, $InjectionPoint$Selector$Type} from "packages/org/spongepowered/asm/mixin/injection/$InjectionPoint$Selector"
import {$IMessageSink, $IMessageSink$Type} from "packages/org/spongepowered/asm/util/$IMessageSink"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $InjectionPointData {

constructor(arg0: $IInjectionPointContext$Type, arg1: string, arg2: $List$Type<(string)>, arg3: string, arg4: string, arg5: integer, arg6: integer, arg7: string)

public "get"(arg0: string, arg1: integer): integer
public "get"(arg0: string, arg1: boolean): boolean
public "get"(arg0: string, arg1: string): string
public "get"(arg0: string): $ITargetSelector
public "toString"(): string
public "getMethod"(): $MethodNode
public "getParent"(): $AnnotationNode
public "getContext"(): $IInjectionPointContext
public "getId"(): string
public "getType"(): string
public "getTarget"(): $ITargetSelector
public static "parseType"(arg0: string): string
public "getOpcode"(arg0: integer): integer
public "getOpcode"(): integer
public "getOpcode"(arg0: integer, ...arg1: (integer)[]): integer
public "getDescription"(): string
public "getOrdinal"(): integer
public "getAt"(): string
public "getSelector"(): $InjectionPoint$Selector
public "getMixin"(): $IMixinContext
public "getLocalVariableDiscriminator"(): $LocalVariableDiscriminator
public "getSlice"(): string
public "getMessageSink"(): $IMessageSink
public "getMethodReturnType"(): $Type
get "method"(): $MethodNode
get "parent"(): $AnnotationNode
get "context"(): $IInjectionPointContext
get "id"(): string
get "type"(): string
get "target"(): $ITargetSelector
get "opcode"(): integer
get "description"(): string
get "ordinal"(): integer
get "at"(): string
get "selector"(): $InjectionPoint$Selector
get "mixin"(): $IMixinContext
get "localVariableDiscriminator"(): $LocalVariableDiscriminator
get "slice"(): string
get "messageSink"(): $IMessageSink
get "methodReturnType"(): $Type
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InjectionPointData$Type = ($InjectionPointData);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InjectionPointData_ = $InjectionPointData$Type;
}}
declare module "packages/org/spongepowered/asm/util/perf/$Profiler" {
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$PrettyPrinter, $PrettyPrinter$Type} from "packages/org/spongepowered/asm/util/$PrettyPrinter"
import {$Profiler$Section, $Profiler$Section$Type} from "packages/org/spongepowered/asm/util/perf/$Profiler$Section"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $Profiler {
static readonly "ROOT": integer
static readonly "FINE": integer

constructor(arg0: string)

public "get"(arg0: string): $Profiler$Section
public "toString"(): string
public "begin"(arg0: string): $Profiler$Section
public "begin"(arg0: integer, ...arg1: (string)[]): $Profiler$Section
public "begin"(...arg0: (string)[]): $Profiler$Section
public "begin"(arg0: integer, arg1: string): $Profiler$Section
public "mark"(arg0: string): void
public "reset"(): void
public static "getProfiler"(arg0: string): $Profiler
public static "setActive"(arg0: boolean): void
public "printSummary"(): void
public "getSections"(): $Collection<($Profiler$Section)>
public static "getProfilers"(): $Collection<($Profiler)>
public static "printAuditSummary"(): void
public "printer"(arg0: boolean, arg1: boolean): $PrettyPrinter
set "active"(value: boolean)
get "sections"(): $Collection<($Profiler$Section)>
get "profilers"(): $Collection<($Profiler)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Profiler$Type = ($Profiler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Profiler_ = $Profiler$Type;
}}
declare module "packages/org/spongepowered/asm/mixin/injection/selectors/$ITargetSelector" {
import {$ElementNode, $ElementNode$Type} from "packages/org/spongepowered/asm/mixin/injection/selectors/$ElementNode"
import {$MatchResult, $MatchResult$Type} from "packages/org/spongepowered/asm/mixin/injection/selectors/$MatchResult"
import {$ISelectorContext, $ISelectorContext$Type} from "packages/org/spongepowered/asm/mixin/injection/selectors/$ISelectorContext"
import {$ITargetSelector$Configure, $ITargetSelector$Configure$Type} from "packages/org/spongepowered/asm/mixin/injection/selectors/$ITargetSelector$Configure"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export interface $ITargetSelector {

 "next"(): $ITargetSelector
 "validate"(): $ITargetSelector
 "match"<TNode>(arg0: $ElementNode$Type<(TNode)>): $MatchResult
 "attach"(arg0: $ISelectorContext$Type): $ITargetSelector
 "configure"(arg0: $ITargetSelector$Configure$Type, ...arg1: (string)[]): $ITargetSelector
 "getMaxMatchCount"(): integer
 "getMinMatchCount"(): integer
}

export namespace $ITargetSelector {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ITargetSelector$Type = ($ITargetSelector);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ITargetSelector_ = $ITargetSelector$Type;
}}
declare module "packages/org/spongepowered/asm/mixin/transformer/$ClassInfo$Method" {
import {$MethodNode, $MethodNode$Type} from "packages/org/objectweb/asm/tree/$MethodNode"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ClassInfo, $ClassInfo$Type} from "packages/org/spongepowered/asm/mixin/transformer/$ClassInfo"
import {$ClassInfo$Member, $ClassInfo$Member$Type} from "packages/org/spongepowered/asm/mixin/transformer/$ClassInfo$Member"
import {$ClassInfo$FrameData, $ClassInfo$FrameData$Type} from "packages/org/spongepowered/asm/mixin/transformer/$ClassInfo$FrameData"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $ClassInfo$Method extends $ClassInfo$Member {

constructor(arg0: $ClassInfo$Type, arg1: string, arg2: string)
constructor(arg0: $ClassInfo$Type, arg1: string, arg2: string, arg3: integer)
constructor(arg0: $ClassInfo$Type, arg1: string, arg2: string, arg3: integer, arg4: boolean)
constructor(arg0: $ClassInfo$Type, arg1: $ClassInfo$Member$Type)
constructor(arg0: $ClassInfo$Type, arg1: $MethodNode$Type)
constructor(arg0: $ClassInfo$Type, arg1: $MethodNode$Type, arg2: boolean)

public "equals"(arg0: any): boolean
public "renameTo"(arg0: string): string
public "getOwner"(): $ClassInfo
public "isConformed"(): boolean
public "conform"(arg0: string): string
public "getFrames"(): $List<($ClassInfo$FrameData)>
public "isAccessor"(): boolean
get "owner"(): $ClassInfo
get "conformed"(): boolean
get "frames"(): $List<($ClassInfo$FrameData)>
get "accessor"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClassInfo$Method$Type = ($ClassInfo$Method);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClassInfo$Method_ = $ClassInfo$Method$Type;
}}
declare module "packages/org/spongepowered/asm/mixin/refmap/$IReferenceMapper" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export interface $IReferenceMapper {

 "getContext"(): string
 "isDefault"(): boolean
 "getStatus"(): string
 "setContext"(arg0: string): void
 "remap"(arg0: string, arg1: string): string
 "getResourceName"(): string
 "remapWithContext"(arg0: string, arg1: string, arg2: string): string
}

export namespace $IReferenceMapper {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IReferenceMapper$Type = ($IReferenceMapper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IReferenceMapper_ = $IReferenceMapper$Type;
}}
declare module "packages/org/spongepowered/asm/mixin/injection/modify/$LocalVariableDiscriminator$Context" {
import {$Target, $Target$Type} from "packages/org/spongepowered/asm/mixin/injection/struct/$Target"
import {$PrettyPrinter$IPrettyPrintable, $PrettyPrinter$IPrettyPrintable$Type} from "packages/org/spongepowered/asm/util/$PrettyPrinter$IPrettyPrintable"
import {$InjectionInfo, $InjectionInfo$Type} from "packages/org/spongepowered/asm/mixin/injection/struct/$InjectionInfo"
import {$PrettyPrinter, $PrettyPrinter$Type} from "packages/org/spongepowered/asm/util/$PrettyPrinter"
import {$Type, $Type$Type} from "packages/org/objectweb/asm/$Type"
import {$AbstractInsnNode, $AbstractInsnNode$Type} from "packages/org/objectweb/asm/tree/$AbstractInsnNode"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $LocalVariableDiscriminator$Context implements $PrettyPrinter$IPrettyPrintable {

constructor(arg0: $InjectionInfo$Type, arg1: $Type$Type, arg2: boolean, arg3: $Target$Type, arg4: $AbstractInsnNode$Type)

public "print"(arg0: $PrettyPrinter$Type): void
public "getCandidateCount"(): integer
get "candidateCount"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LocalVariableDiscriminator$Context$Type = ($LocalVariableDiscriminator$Context);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LocalVariableDiscriminator$Context_ = $LocalVariableDiscriminator$Context$Type;
}}
declare module "packages/org/spongepowered/asm/mixin/injection/$At" {
import {$Annotation, $Annotation$Type} from "packages/java/lang/annotation/$Annotation"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$Desc, $Desc$Type} from "packages/org/spongepowered/asm/mixin/injection/$Desc"
import {$At$Shift, $At$Shift$Type} from "packages/org/spongepowered/asm/mixin/injection/$At$Shift"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export interface $At extends $Annotation {

 "value"(): string
 "target"(): string
 "args"(): (string)[]
 "id"(): string
 "ordinal"(): integer
 "desc"(): $Desc
 "shift"(): $At$Shift
 "slice"(): string
 "opcode"(): integer
 "by"(): integer
 "remap"(): boolean
 "equals"(arg0: any): boolean
 "toString"(): string
 "hashCode"(): integer
 "annotationType"(): $Class<(any)>
}

export namespace $At {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $At$Type = ($At);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $At_ = $At$Type;
}}
declare module "packages/org/spongepowered/asm/mixin/struct/$SourceMap$File" {
import {$ClassNode, $ClassNode$Type} from "packages/org/objectweb/asm/tree/$ClassNode"
import {$MethodNode, $MethodNode$Type} from "packages/org/objectweb/asm/tree/$MethodNode"
import {$StringBuilder, $StringBuilder$Type} from "packages/java/lang/$StringBuilder"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $SourceMap$File {
readonly "id": integer
readonly "lineOffset": integer
readonly "size": integer
readonly "sourceFileName": string
readonly "sourceFilePath": string

constructor(arg0: integer, arg1: integer, arg2: integer, arg3: string)
constructor(arg0: integer, arg1: integer, arg2: integer, arg3: string, arg4: string)

public "applyOffset"(arg0: $MethodNode$Type): void
public "applyOffset"(arg0: $ClassNode$Type): void
public "appendLines"(arg0: $StringBuilder$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SourceMap$File$Type = ($SourceMap$File);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SourceMap$File_ = $SourceMap$File$Type;
}}
declare module "packages/org/spongepowered/asm/mixin/injection/$IInjectionPointContext" {
import {$AnnotationNode, $AnnotationNode$Type} from "packages/org/objectweb/asm/tree/$AnnotationNode"
import {$MethodNode, $MethodNode$Type} from "packages/org/objectweb/asm/tree/$MethodNode"
import {$IAnnotationHandle, $IAnnotationHandle$Type} from "packages/org/spongepowered/asm/util/asm/$IAnnotationHandle"
import {$IMixinContext, $IMixinContext$Type} from "packages/org/spongepowered/asm/mixin/refmap/$IMixinContext"
import {$ISelectorContext, $ISelectorContext$Type} from "packages/org/spongepowered/asm/mixin/injection/selectors/$ISelectorContext"
import {$IMessageSink, $IMessageSink$Type} from "packages/org/spongepowered/asm/util/$IMessageSink"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export interface $IInjectionPointContext extends $IMessageSink, $ISelectorContext {

 "getMethod"(): $MethodNode
 "getAnnotationNode"(): $AnnotationNode
 "addMessage"(arg0: string, ...arg1: (any)[]): void
 "getAnnotation"(): $IAnnotationHandle
 "getParent"(): $ISelectorContext
 "remap"(arg0: string): string
 "getMixin"(): $IMixinContext
 "getSelectorAnnotation"(): $IAnnotationHandle
 "getSelectorCoordinate"(arg0: boolean): string
}

export namespace $IInjectionPointContext {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IInjectionPointContext$Type = ($IInjectionPointContext);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IInjectionPointContext_ = $IInjectionPointContext$Type;
}}
declare module "packages/org/spongepowered/asm/mixin/transformer/$ClassInfo$SearchType" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $ClassInfo$SearchType extends $Enum<($ClassInfo$SearchType)> {
static readonly "ALL_CLASSES": $ClassInfo$SearchType
static readonly "SUPER_CLASSES_ONLY": $ClassInfo$SearchType


public static "values"(): ($ClassInfo$SearchType)[]
public static "valueOf"(arg0: string): $ClassInfo$SearchType
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClassInfo$SearchType$Type = (("super_classes_only") | ("all_classes")) | ($ClassInfo$SearchType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClassInfo$SearchType_ = $ClassInfo$SearchType$Type;
}}
declare module "packages/org/spongepowered/asm/mixin/injection/$Slice" {
import {$Annotation, $Annotation$Type} from "packages/java/lang/annotation/$Annotation"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$At, $At$Type} from "packages/org/spongepowered/asm/mixin/injection/$At"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export interface $Slice extends $Annotation {

 "to"(): $At
 "from"(): $At
 "id"(): string
 "equals"(arg0: any): boolean
 "toString"(): string
 "hashCode"(): integer
 "annotationType"(): $Class<(any)>
}

export namespace $Slice {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Slice$Type = ($Slice);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Slice_ = $Slice$Type;
}}
declare module "packages/org/spongepowered/asm/mixin/connect/$IMixinConnector" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export interface $IMixinConnector {

 "connect"(): void

(): void
}

export namespace $IMixinConnector {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IMixinConnector$Type = ($IMixinConnector);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IMixinConnector_ = $IMixinConnector$Type;
}}
declare module "packages/org/spongepowered/asm/mixin/extensibility/$IMixinInfo" {
import {$ClassNode, $ClassNode$Type} from "packages/org/objectweb/asm/tree/$ClassNode"
import {$List, $List$Type} from "packages/java/util/$List"
import {$IMixinConfig, $IMixinConfig$Type} from "packages/org/spongepowered/asm/mixin/extensibility/$IMixinConfig"
import {$MixinEnvironment$Phase, $MixinEnvironment$Phase$Type} from "packages/org/spongepowered/asm/mixin/$MixinEnvironment$Phase"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export interface $IMixinInfo {

 "getName"(): string
 "getPriority"(): integer
 "getClassName"(): string
 "getClassBytes"(): (byte)[]
 "getPhase"(): $MixinEnvironment$Phase
 "getClassNode"(arg0: integer): $ClassNode
 "getConfig"(): $IMixinConfig
 "getTargetClasses"(): $List<(string)>
 "getClassRef"(): string
 "isDetachedSuper"(): boolean
}

export namespace $IMixinInfo {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IMixinInfo$Type = ($IMixinInfo);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IMixinInfo_ = $IMixinInfo$Type;
}}
declare module "packages/org/spongepowered/asm/mixin/injection/selectors/$ISelectorContext" {
import {$IAnnotationHandle, $IAnnotationHandle$Type} from "packages/org/spongepowered/asm/util/asm/$IAnnotationHandle"
import {$IMixinContext, $IMixinContext$Type} from "packages/org/spongepowered/asm/mixin/refmap/$IMixinContext"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export interface $ISelectorContext {

 "getMethod"(): any
 "getAnnotation"(): $IAnnotationHandle
 "getParent"(): $ISelectorContext
 "remap"(arg0: string): string
 "getMixin"(): $IMixinContext
 "getSelectorAnnotation"(): $IAnnotationHandle
 "getSelectorCoordinate"(arg0: boolean): string
}

export namespace $ISelectorContext {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ISelectorContext$Type = ($ISelectorContext);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ISelectorContext_ = $ISelectorContext$Type;
}}
declare module "packages/org/spongepowered/asm/mixin/injection/selectors/$ITargetSelector$Configure" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $ITargetSelector$Configure extends $Enum<($ITargetSelector$Configure)> {
static readonly "SELECT_MEMBER": $ITargetSelector$Configure
static readonly "SELECT_INSTRUCTION": $ITargetSelector$Configure
static readonly "MOVE": $ITargetSelector$Configure
static readonly "ORPHAN": $ITargetSelector$Configure
static readonly "TRANSFORM": $ITargetSelector$Configure
static readonly "PERMISSIVE": $ITargetSelector$Configure
static readonly "CLEAR_LIMITS": $ITargetSelector$Configure


public static "values"(): ($ITargetSelector$Configure)[]
public static "valueOf"(arg0: string): $ITargetSelector$Configure
public "checkArgs"(...arg0: (string)[]): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ITargetSelector$Configure$Type = (("move") | ("transform") | ("select_member") | ("clear_limits") | ("permissive") | ("select_instruction") | ("orphan")) | ($ITargetSelector$Configure);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ITargetSelector$Configure_ = $ITargetSelector$Configure$Type;
}}
declare module "packages/org/spongepowered/asm/mixin/$MixinEnvironment$CompatibilityLevel" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $MixinEnvironment$CompatibilityLevel extends $Enum<($MixinEnvironment$CompatibilityLevel)> {
static readonly "JAVA_6": $MixinEnvironment$CompatibilityLevel
static readonly "JAVA_7": $MixinEnvironment$CompatibilityLevel
static readonly "JAVA_8": $MixinEnvironment$CompatibilityLevel
static readonly "JAVA_9": $MixinEnvironment$CompatibilityLevel
static readonly "JAVA_10": $MixinEnvironment$CompatibilityLevel
static readonly "JAVA_11": $MixinEnvironment$CompatibilityLevel
static readonly "JAVA_12": $MixinEnvironment$CompatibilityLevel
static readonly "JAVA_13": $MixinEnvironment$CompatibilityLevel
static readonly "JAVA_14": $MixinEnvironment$CompatibilityLevel
static readonly "JAVA_15": $MixinEnvironment$CompatibilityLevel
static readonly "JAVA_16": $MixinEnvironment$CompatibilityLevel
static readonly "JAVA_17": $MixinEnvironment$CompatibilityLevel
static readonly "JAVA_18": $MixinEnvironment$CompatibilityLevel
static "DEFAULT": $MixinEnvironment$CompatibilityLevel
static "MAX_SUPPORTED": $MixinEnvironment$CompatibilityLevel


public static "values"(): ($MixinEnvironment$CompatibilityLevel)[]
public static "valueOf"(arg0: string): $MixinEnvironment$CompatibilityLevel
public "canSupport"(arg0: $MixinEnvironment$CompatibilityLevel$Type): boolean
public "isLessThan"(arg0: $MixinEnvironment$CompatibilityLevel$Type): boolean
public "canElevateTo"(arg0: $MixinEnvironment$CompatibilityLevel$Type): boolean
public "getClassVersion"(): integer
/**
 * 
 * @deprecated
 */
public "classVersion"(): integer
public static "requiredFor"(arg0: integer): $MixinEnvironment$CompatibilityLevel
public "isAtLeast"(arg0: $MixinEnvironment$CompatibilityLevel$Type): boolean
public "supports"(arg0: integer): boolean
public "getClassMajorVersion"(): integer
public "getLanguageFeatures"(): integer
/**
 * 
 * @deprecated
 */
public "supportsMethodsInInterfaces"(): boolean
get "classMajorVersion"(): integer
get "languageFeatures"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MixinEnvironment$CompatibilityLevel$Type = (("java_18") | ("java_16") | ("java_17") | ("java_14") | ("java_15") | ("java_9") | ("java_8") | ("java_7") | ("java_6") | ("java_12") | ("java_13") | ("java_10") | ("java_11")) | ($MixinEnvironment$CompatibilityLevel);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MixinEnvironment$CompatibilityLevel_ = $MixinEnvironment$CompatibilityLevel$Type;
}}
declare module "packages/org/spongepowered/asm/mixin/injection/struct/$InjectionInfo" {
import {$AnnotationNode, $AnnotationNode$Type} from "packages/org/objectweb/asm/tree/$AnnotationNode"
import {$IMixinInfo, $IMixinInfo$Type} from "packages/org/spongepowered/asm/mixin/extensibility/$IMixinInfo"
import {$MethodNode, $MethodNode$Type} from "packages/org/objectweb/asm/tree/$MethodNode"
import {$Target, $Target$Type} from "packages/org/spongepowered/asm/mixin/injection/struct/$Target"
import {$MethodSlice, $MethodSlice$Type} from "packages/org/spongepowered/asm/mixin/injection/code/$MethodSlice"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$ISliceContext, $ISliceContext$Type} from "packages/org/spongepowered/asm/mixin/injection/code/$ISliceContext"
import {$SpecialMethodInfo, $SpecialMethodInfo$Type} from "packages/org/spongepowered/asm/mixin/struct/$SpecialMethodInfo"
import {$MixinTargetContext, $MixinTargetContext$Type} from "packages/org/spongepowered/asm/mixin/transformer/$MixinTargetContext"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $InjectionInfo extends $SpecialMethodInfo implements $ISliceContext {
static readonly "DEFAULT_PREFIX": string


public "toString"(): string
public static "register"(arg0: $Class$Type<(any)>): void
public "prepare"(): void
public static "parse"(arg0: $MixinTargetContext$Type, arg1: $MethodNode$Type): $InjectionInfo
public "addMethod"(arg0: integer, arg1: string, arg2: string): $MethodNode
public "isValid"(): boolean
public "getInjectedCallbackCount"(): integer
public static "getInjectorAnnotation"(arg0: $IMixinInfo$Type, arg1: $MethodNode$Type): $AnnotationNode
public "addMessage"(arg0: string, ...arg1: (any)[]): void
public "inject"(): void
public "addCallbackInvocation"(arg0: $MethodNode$Type): void
public static "getRegisteredAnnotations"(): $Set<($Class<(any)>)>
public "preInject"(): void
public "postInject"(): void
public static "getInjectorPrefix"(arg0: $AnnotationNode$Type): string
public "getSliceId"(arg0: string): string
public "notifyInjected"(arg0: $Target$Type): void
public "getTargetCount"(): integer
public "getSlice"(arg0: string): $MethodSlice
public "getMethod"(): $MethodNode
public "getAnnotationNode"(): $AnnotationNode
get "valid"(): boolean
get "injectedCallbackCount"(): integer
get "registeredAnnotations"(): $Set<($Class<(any)>)>
get "targetCount"(): integer
get "method"(): $MethodNode
get "annotationNode"(): $AnnotationNode
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InjectionInfo$Type = ($InjectionInfo);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InjectionInfo_ = $InjectionInfo$Type;
}}
declare module "packages/org/spongepowered/asm/mixin/extensibility/$IEnvironmentTokenProvider" {
import {$MixinEnvironment, $MixinEnvironment$Type} from "packages/org/spongepowered/asm/mixin/$MixinEnvironment"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export interface $IEnvironmentTokenProvider {

 "getPriority"(): integer
 "getToken"(arg0: string, arg1: $MixinEnvironment$Type): integer
}

export namespace $IEnvironmentTokenProvider {
const DEFAULT_PRIORITY: integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IEnvironmentTokenProvider$Type = ($IEnvironmentTokenProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IEnvironmentTokenProvider_ = $IEnvironmentTokenProvider$Type;
}}
declare module "packages/org/spongepowered/asm/mixin/injection/code/$InjectorTarget" {
import {$MethodNode, $MethodNode$Type} from "packages/org/objectweb/asm/tree/$MethodNode"
import {$Target, $Target$Type} from "packages/org/spongepowered/asm/mixin/injection/struct/$Target"
import {$ITargetSelector, $ITargetSelector$Type} from "packages/org/spongepowered/asm/mixin/injection/selectors/$ITargetSelector"
import {$ISliceContext, $ISliceContext$Type} from "packages/org/spongepowered/asm/mixin/injection/code/$ISliceContext"
import {$InjectionPoint, $InjectionPoint$Type} from "packages/org/spongepowered/asm/mixin/injection/$InjectionPoint"
import {$InsnList, $InsnList$Type} from "packages/org/objectweb/asm/tree/$InsnList"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $InjectorTarget {

constructor(arg0: $ISliceContext$Type, arg1: $Target$Type, arg2: $ITargetSelector$Type)

public "toString"(): string
public "getMethod"(): $MethodNode
public "getTarget"(): $Target
public "dispose"(): void
public "getMergedBy"(): string
public "isMerged"(): boolean
public "getMergedPriority"(): integer
public "getSelector"(): $ITargetSelector
public "getSlice"(arg0: $InjectionPoint$Type): $InsnList
public "getSlice"(arg0: string): $InsnList
get "method"(): $MethodNode
get "target"(): $Target
get "mergedBy"(): string
get "merged"(): boolean
get "mergedPriority"(): integer
get "selector"(): $ITargetSelector
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InjectorTarget$Type = ($InjectorTarget);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InjectorTarget_ = $InjectorTarget$Type;
}}
declare module "packages/org/spongepowered/asm/mixin/injection/$Constant" {
import {$Annotation, $Annotation$Type} from "packages/java/lang/annotation/$Annotation"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$Constant$Condition, $Constant$Condition$Type} from "packages/org/spongepowered/asm/mixin/injection/$Constant$Condition"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export interface $Constant extends $Annotation {

 "log"(): boolean
 "intValue"(): integer
 "longValue"(): long
 "floatValue"(): float
 "doubleValue"(): double
 "ordinal"(): integer
 "slice"(): string
 "classValue"(): $Class<(any)>
 "stringValue"(): string
 "nullValue"(): boolean
 "expandZeroConditions"(): ($Constant$Condition)[]
 "equals"(arg0: any): boolean
 "toString"(): string
 "hashCode"(): integer
 "annotationType"(): $Class<(any)>
}

export namespace $Constant {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Constant$Type = ($Constant);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Constant_ = $Constant$Type;
}}
declare module "packages/org/spongepowered/asm/mixin/$MixinEnvironment$Phase" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $MixinEnvironment$Phase {
static readonly "PREINIT": $MixinEnvironment$Phase
static readonly "INIT": $MixinEnvironment$Phase
static readonly "DEFAULT": $MixinEnvironment$Phase


public static "forName"(arg0: string): $MixinEnvironment$Phase
public "toString"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MixinEnvironment$Phase$Type = ($MixinEnvironment$Phase);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MixinEnvironment$Phase_ = $MixinEnvironment$Phase$Type;
}}
declare module "packages/org/spongepowered/asm/mixin/injection/selectors/$ElementNode" {
import {$ElementNode$NodeType, $ElementNode$NodeType$Type} from "packages/org/spongepowered/asm/mixin/injection/selectors/$ElementNode$NodeType"
import {$ClassNode, $ClassNode$Type} from "packages/org/objectweb/asm/tree/$ClassNode"
import {$MethodNode, $MethodNode$Type} from "packages/org/objectweb/asm/tree/$MethodNode"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Iterable, $Iterable$Type} from "packages/java/lang/$Iterable"
import {$FieldNode, $FieldNode$Type} from "packages/org/objectweb/asm/tree/$FieldNode"
import {$InsnList, $InsnList$Type} from "packages/org/objectweb/asm/tree/$InsnList"
import {$AbstractInsnNode, $AbstractInsnNode$Type} from "packages/org/objectweb/asm/tree/$AbstractInsnNode"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $ElementNode<TNode> {

constructor()

public "getName"(): string
public "get"(): TNode
public "toString"(): string
public static "of"(arg0: $ClassNode$Type, arg1: $FieldNode$Type): $ElementNode<($FieldNode)>
public static "of"(arg0: $ClassNode$Type, arg1: $MethodNode$Type): $ElementNode<($MethodNode)>
public static "of"<TNode extends $AbstractInsnNode>(arg0: TNode): $ElementNode<(TNode)>
public static "of"<TNode>(arg0: $ClassNode$Type, arg1: TNode): $ElementNode<(TNode)>
public "getMethod"(): $MethodNode
public "getField"(): $FieldNode
public "getType"(): $ElementNode$NodeType
public "getSignature"(): string
public "isField"(): boolean
public "getOwner"(): string
public static "methodList"(arg0: $ClassNode$Type): $List<($ElementNode<($MethodNode)>)>
public "getDesc"(): string
public static "listOf"<TNode>(arg0: $ClassNode$Type, arg1: $List$Type<(TNode)>): $List<($ElementNode<(TNode)>)>
public static "insnList"(arg0: $InsnList$Type): $Iterable<($ElementNode<($AbstractInsnNode)>)>
public "getDelegateDesc"(): string
public "getImplDesc"(): string
public "getSyntheticName"(): string
public static "dynamicInsnList"(arg0: $InsnList$Type): $Iterable<($ElementNode<($AbstractInsnNode)>)>
public "getInsn"(): $AbstractInsnNode
public static "fieldList"(arg0: $ClassNode$Type): $List<($ElementNode<($FieldNode)>)>
get "name"(): string
get "method"(): $MethodNode
get "field"(): $FieldNode
get "type"(): $ElementNode$NodeType
get "signature"(): string
get "field"(): boolean
get "owner"(): string
get "desc"(): string
get "delegateDesc"(): string
get "implDesc"(): string
get "syntheticName"(): string
get "insn"(): $AbstractInsnNode
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ElementNode$Type<TNode> = ($ElementNode<(TNode)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ElementNode_<TNode> = $ElementNode$Type<(TNode)>;
}}
declare module "packages/org/spongepowered/asm/mixin/transformer/ext/$ITargetClassContext" {
import {$ClassNode, $ClassNode$Type} from "packages/org/objectweb/asm/tree/$ClassNode"
import {$ClassInfo, $ClassInfo$Type} from "packages/org/spongepowered/asm/mixin/transformer/$ClassInfo"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export interface $ITargetClassContext {

 "getClassInfo"(): $ClassInfo
 "getClassNode"(): $ClassNode
}

export namespace $ITargetClassContext {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ITargetClassContext$Type = ($ITargetClassContext);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ITargetClassContext_ = $ITargetClassContext$Type;
}}
declare module "packages/org/spongepowered/asm/mixin/extensibility/$IActivityContext" {
import {$IActivityContext$IActivity, $IActivityContext$IActivity$Type} from "packages/org/spongepowered/asm/mixin/extensibility/$IActivityContext$IActivity"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export interface $IActivityContext {

 "toString"(arg0: string): string
 "clear"(): void
 "begin"(arg0: string, ...arg1: (any)[]): $IActivityContext$IActivity
 "begin"(arg0: string): $IActivityContext$IActivity
}

export namespace $IActivityContext {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IActivityContext$Type = ($IActivityContext);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IActivityContext_ = $IActivityContext$Type;
}}
declare module "packages/org/spongepowered/asm/obfuscation/$RemapperChain" {
import {$IRemapper, $IRemapper$Type} from "packages/org/spongepowered/asm/mixin/extensibility/$IRemapper"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $RemapperChain implements $IRemapper {

constructor()

public "add"(arg0: $IRemapper$Type): $RemapperChain
public "toString"(): string
public "map"(arg0: string): string
public "unmap"(arg0: string): string
public "mapFieldName"(arg0: string, arg1: string, arg2: string): string
public "mapDesc"(arg0: string): string
public "unmapDesc"(arg0: string): string
public "mapMethodName"(arg0: string, arg1: string, arg2: string): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RemapperChain$Type = ($RemapperChain);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RemapperChain_ = $RemapperChain$Type;
}}
declare module "packages/org/spongepowered/asm/mixin/transformer/ext/$IExtensionRegistry" {
import {$ISyntheticClassRegistry, $ISyntheticClassRegistry$Type} from "packages/org/spongepowered/asm/service/$ISyntheticClassRegistry"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$IExtension, $IExtension$Type} from "packages/org/spongepowered/asm/mixin/transformer/ext/$IExtension"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export interface $IExtensionRegistry {

 "getExtensions"(): $List<($IExtension)>
 "getExtension"<T extends $IExtension>(arg0: $Class$Type<(any)>): T
 "getActiveExtensions"(): $List<($IExtension)>
 "getSyntheticClassRegistry"(): $ISyntheticClassRegistry
}

export namespace $IExtensionRegistry {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IExtensionRegistry$Type = ($IExtensionRegistry);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IExtensionRegistry_ = $IExtensionRegistry$Type;
}}
declare module "packages/org/spongepowered/asm/mixin/extensibility/$IActivityContext$IActivity" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export interface $IActivityContext$IActivity {

 "append"(arg0: string): void
 "append"(arg0: string, ...arg1: (any)[]): void
 "next"(arg0: string, ...arg1: (any)[]): void
 "next"(arg0: string): void
 "end"(): void
}

export namespace $IActivityContext$IActivity {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IActivityContext$IActivity$Type = ($IActivityContext$IActivity);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IActivityContext$IActivity_ = $IActivityContext$IActivity$Type;
}}
declare module "packages/org/spongepowered/asm/mixin/$MixinEnvironment$Side" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $MixinEnvironment$Side extends $Enum<($MixinEnvironment$Side)> {
static readonly "UNKNOWN": $MixinEnvironment$Side
static readonly "CLIENT": $MixinEnvironment$Side
static readonly "SERVER": $MixinEnvironment$Side


public static "values"(): ($MixinEnvironment$Side)[]
public static "valueOf"(arg0: string): $MixinEnvironment$Side
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MixinEnvironment$Side$Type = (("server") | ("client") | ("unknown")) | ($MixinEnvironment$Side);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MixinEnvironment$Side_ = $MixinEnvironment$Side$Type;
}}
declare module "packages/org/spongepowered/asm/mixin/injection/callback/$Cancellable" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export interface $Cancellable {

 "cancel"(): void
 "isCancelled"(): boolean
 "isCancellable"(): boolean
}

export namespace $Cancellable {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Cancellable$Type = ($Cancellable);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Cancellable_ = $Cancellable$Type;
}}
declare module "packages/org/spongepowered/asm/mixin/injection/struct/$InjectionNodes$InjectionNode" {
import {$Comparable, $Comparable$Type} from "packages/java/lang/$Comparable"
import {$AbstractInsnNode, $AbstractInsnNode$Type} from "packages/org/objectweb/asm/tree/$AbstractInsnNode"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $InjectionNodes$InjectionNode implements $Comparable<($InjectionNodes$InjectionNode)> {

constructor(arg0: $AbstractInsnNode$Type)

public "remove"(): $InjectionNodes$InjectionNode
public "toString"(): string
public "compareTo"(arg0: $InjectionNodes$InjectionNode$Type): integer
public "replace"(arg0: $AbstractInsnNode$Type): $InjectionNodes$InjectionNode
public "matches"(arg0: $AbstractInsnNode$Type): boolean
public "getId"(): integer
public "getDecoration"<V>(arg0: string): V
public "decorate"<V>(arg0: string, arg1: V): $InjectionNodes$InjectionNode
public "hasDecoration"(arg0: string): boolean
public "isReplaced"(): boolean
public "getOriginalTarget"(): $AbstractInsnNode
public "isRemoved"(): boolean
public "getCurrentTarget"(): $AbstractInsnNode
get "id"(): integer
get "replaced"(): boolean
get "originalTarget"(): $AbstractInsnNode
get "removed"(): boolean
get "currentTarget"(): $AbstractInsnNode
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InjectionNodes$InjectionNode$Type = ($InjectionNodes$InjectionNode);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InjectionNodes$InjectionNode_ = $InjectionNodes$InjectionNode$Type;
}}
declare module "packages/org/spongepowered/asm/service/$ITransformer" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export interface $ITransformer {

 "getName"(): string
 "isDelegationExcluded"(): boolean
}

export namespace $ITransformer {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ITransformer$Type = ($ITransformer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ITransformer_ = $ITransformer$Type;
}}
declare module "packages/org/spongepowered/asm/mixin/refmap/$IMixinContext" {
import {$Extensions, $Extensions$Type} from "packages/org/spongepowered/asm/mixin/transformer/ext/$Extensions"
import {$IMixinInfo, $IMixinInfo$Type} from "packages/org/spongepowered/asm/mixin/extensibility/$IMixinInfo"
import {$MixinEnvironment$Option, $MixinEnvironment$Option$Type} from "packages/org/spongepowered/asm/mixin/$MixinEnvironment$Option"
import {$IReferenceMapper, $IReferenceMapper$Type} from "packages/org/spongepowered/asm/mixin/refmap/$IReferenceMapper"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export interface $IMixinContext {

 "getPriority"(): integer
 "getClassName"(): string
 "getExtensions"(): $Extensions
 "getReferenceMapper"(): $IReferenceMapper
 "getOption"(arg0: $MixinEnvironment$Option$Type): boolean
 "getMixin"(): $IMixinInfo
 "getClassRef"(): string
 "getTargetClassRef"(): string
}

export namespace $IMixinContext {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IMixinContext$Type = ($IMixinContext);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IMixinContext_ = $IMixinContext$Type;
}}
declare module "packages/org/spongepowered/asm/util/$PrettyPrinter$IPrettyPrintable" {
import {$PrettyPrinter, $PrettyPrinter$Type} from "packages/org/spongepowered/asm/util/$PrettyPrinter"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export interface $PrettyPrinter$IPrettyPrintable {

 "print"(arg0: $PrettyPrinter$Type): void

(arg0: $PrettyPrinter$Type): void
}

export namespace $PrettyPrinter$IPrettyPrintable {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PrettyPrinter$IPrettyPrintable$Type = ($PrettyPrinter$IPrettyPrintable);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PrettyPrinter$IPrettyPrintable_ = $PrettyPrinter$IPrettyPrintable$Type;
}}
declare module "packages/org/spongepowered/asm/mixin/injection/$Desc" {
import {$Annotation, $Annotation$Type} from "packages/java/lang/annotation/$Annotation"
import {$Class, $Class$Type} from "packages/java/lang/$Class"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export interface $Desc extends $Annotation {

 "value"(): string
 "min"(): integer
 "max"(): integer
 "ret"(): $Class<(any)>
 "args"(): ($Class<(any)>)[]
 "id"(): string
 "owner"(): $Class<(any)>
 "equals"(arg0: any): boolean
 "toString"(): string
 "hashCode"(): integer
 "annotationType"(): $Class<(any)>
}

export namespace $Desc {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Desc$Type = ($Desc);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Desc_ = $Desc$Type;
}}
declare module "packages/org/spongepowered/asm/util/$Bytecode$DelegateInitialiser" {
import {$MethodInsnNode, $MethodInsnNode$Type} from "packages/org/objectweb/asm/tree/$MethodInsnNode"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $Bytecode$DelegateInitialiser {
static readonly "NONE": $Bytecode$DelegateInitialiser
readonly "insn": $MethodInsnNode
readonly "isSuper": boolean
readonly "isPresent": boolean


public "toString"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Bytecode$DelegateInitialiser$Type = ($Bytecode$DelegateInitialiser);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Bytecode$DelegateInitialiser_ = $Bytecode$DelegateInitialiser$Type;
}}
declare module "packages/org/spongepowered/asm/mixin/struct/$AnnotatedMethodInfo" {
import {$AnnotationNode, $AnnotationNode$Type} from "packages/org/objectweb/asm/tree/$AnnotationNode"
import {$MethodNode, $MethodNode$Type} from "packages/org/objectweb/asm/tree/$MethodNode"
import {$IMixinContext, $IMixinContext$Type} from "packages/org/spongepowered/asm/mixin/refmap/$IMixinContext"
import {$IAnnotationHandle, $IAnnotationHandle$Type} from "packages/org/spongepowered/asm/util/asm/$IAnnotationHandle"
import {$IInjectionPointContext, $IInjectionPointContext$Type} from "packages/org/spongepowered/asm/mixin/injection/$IInjectionPointContext"
import {$ISelectorContext, $ISelectorContext$Type} from "packages/org/spongepowered/asm/mixin/injection/selectors/$ISelectorContext"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $AnnotatedMethodInfo implements $IInjectionPointContext {

constructor(arg0: $IMixinContext$Type, arg1: $MethodNode$Type, arg2: $AnnotationNode$Type)

public "getAnnotation"(): $IAnnotationHandle
public "getParent"(): $ISelectorContext
public "getMethodName"(): string
public "addMessage"(arg0: string, ...arg1: (any)[]): void
public "remap"(arg0: string): string
public "getMixin"(): $IMixinContext
public "getSelectorAnnotation"(): $IAnnotationHandle
public "getSelectorCoordinate"(arg0: boolean): string
public "getAnnotationNode"(): $AnnotationNode
get "annotation"(): $IAnnotationHandle
get "parent"(): $ISelectorContext
get "methodName"(): string
get "mixin"(): $IMixinContext
get "selectorAnnotation"(): $IAnnotationHandle
get "annotationNode"(): $AnnotationNode
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AnnotatedMethodInfo$Type = ($AnnotatedMethodInfo);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AnnotatedMethodInfo_ = $AnnotatedMethodInfo$Type;
}}
declare module "packages/org/spongepowered/asm/mixin/transformer/$TargetClassContext" {
import {$ClassNode, $ClassNode$Type} from "packages/org/objectweb/asm/tree/$ClassNode"
import {$ClassInfo, $ClassInfo$Type} from "packages/org/spongepowered/asm/mixin/transformer/$ClassInfo"
import {$ITargetClassContext, $ITargetClassContext$Type} from "packages/org/spongepowered/asm/mixin/transformer/ext/$ITargetClassContext"
import {$ClassContext, $ClassContext$Type} from "packages/org/spongepowered/asm/mixin/transformer/$ClassContext"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $TargetClassContext extends $ClassContext implements $ITargetClassContext {


public "toString"(): string
public "getClassInfo"(): $ClassInfo
public "getClassNode"(): $ClassNode
get "classInfo"(): $ClassInfo
get "classNode"(): $ClassNode
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TargetClassContext$Type = ($TargetClassContext);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TargetClassContext_ = $TargetClassContext$Type;
}}
declare module "packages/org/spongepowered/asm/mixin/extensibility/$IMixinErrorHandler" {
import {$IMixinErrorHandler$ErrorAction, $IMixinErrorHandler$ErrorAction$Type} from "packages/org/spongepowered/asm/mixin/extensibility/$IMixinErrorHandler$ErrorAction"
import {$IMixinInfo, $IMixinInfo$Type} from "packages/org/spongepowered/asm/mixin/extensibility/$IMixinInfo"
import {$Throwable, $Throwable$Type} from "packages/java/lang/$Throwable"
import {$IMixinConfig, $IMixinConfig$Type} from "packages/org/spongepowered/asm/mixin/extensibility/$IMixinConfig"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export interface $IMixinErrorHandler {

 "onPrepareError"(arg0: $IMixinConfig$Type, arg1: $Throwable$Type, arg2: $IMixinInfo$Type, arg3: $IMixinErrorHandler$ErrorAction$Type): $IMixinErrorHandler$ErrorAction
 "onApplyError"(arg0: string, arg1: $Throwable$Type, arg2: $IMixinInfo$Type, arg3: $IMixinErrorHandler$ErrorAction$Type): $IMixinErrorHandler$ErrorAction
}

export namespace $IMixinErrorHandler {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IMixinErrorHandler$Type = ($IMixinErrorHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IMixinErrorHandler_ = $IMixinErrorHandler$Type;
}}
declare module "packages/org/spongepowered/asm/mixin/injection/$Constant$Condition" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $Constant$Condition extends $Enum<($Constant$Condition)> {
static readonly "LESS_THAN_ZERO": $Constant$Condition
static readonly "LESS_THAN_OR_EQUAL_TO_ZERO": $Constant$Condition
static readonly "GREATER_THAN_OR_EQUAL_TO_ZERO": $Constant$Condition
static readonly "GREATER_THAN_ZERO": $Constant$Condition


public static "values"(): ($Constant$Condition)[]
public static "valueOf"(arg0: string): $Constant$Condition
public "getOpcodes"(): (integer)[]
public "getEquivalentCondition"(): $Constant$Condition
get "opcodes"(): (integer)[]
get "equivalentCondition"(): $Constant$Condition
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Constant$Condition$Type = (("greater_than_zero") | ("less_than_or_equal_to_zero") | ("less_than_zero") | ("greater_than_or_equal_to_zero")) | ($Constant$Condition);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Constant$Condition_ = $Constant$Condition$Type;
}}
declare module "packages/org/spongepowered/asm/mixin/injection/code/$InsnListReadOnly" {
import {$ListIterator, $ListIterator$Type} from "packages/java/util/$ListIterator"
import {$InsnList, $InsnList$Type} from "packages/org/objectweb/asm/tree/$InsnList"
import {$AbstractInsnNode, $AbstractInsnNode$Type} from "packages/org/objectweb/asm/tree/$AbstractInsnNode"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $InsnListReadOnly extends $InsnList {

constructor(arg0: $InsnList$Type)

public "add"(arg0: $AbstractInsnNode$Type): void
public "add"(arg0: $InsnList$Type): void
public "remove"(arg0: $AbstractInsnNode$Type): void
public "get"(arg0: integer): $AbstractInsnNode
public "indexOf"(arg0: $AbstractInsnNode$Type): integer
public "insert"(arg0: $AbstractInsnNode$Type, arg1: $AbstractInsnNode$Type): void
public "insert"(arg0: $AbstractInsnNode$Type, arg1: $InsnList$Type): void
public "insert"(arg0: $AbstractInsnNode$Type): void
public "insert"(arg0: $InsnList$Type): void
public "size"(): integer
public "toArray"(): ($AbstractInsnNode)[]
public "iterator"(arg0: integer): $ListIterator<($AbstractInsnNode)>
public "iterator"(): $ListIterator<($AbstractInsnNode)>
public "contains"(arg0: $AbstractInsnNode$Type): boolean
public "set"(arg0: $AbstractInsnNode$Type, arg1: $AbstractInsnNode$Type): void
public "getFirst"(): $AbstractInsnNode
public "getLast"(): $AbstractInsnNode
public "resetLabels"(): void
public "insertBefore"(arg0: $AbstractInsnNode$Type, arg1: $InsnList$Type): void
public "insertBefore"(arg0: $AbstractInsnNode$Type, arg1: $AbstractInsnNode$Type): void
get "first"(): $AbstractInsnNode
get "last"(): $AbstractInsnNode
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InsnListReadOnly$Type = ($InsnListReadOnly);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InsnListReadOnly_ = $InsnListReadOnly$Type;
}}
declare module "packages/org/spongepowered/asm/mixin/injection/selectors/$MatchResult" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $MatchResult extends $Enum<($MatchResult)> {
static readonly "NONE": $MatchResult
static readonly "WEAK": $MatchResult
static readonly "MATCH": $MatchResult
static readonly "EXACT_MATCH": $MatchResult


public static "values"(): ($MatchResult)[]
public static "valueOf"(arg0: string): $MatchResult
public "isExactMatch"(): boolean
public "isMatch"(): boolean
public "isAtLeast"(arg0: $MatchResult$Type): boolean
get "exactMatch"(): boolean
get "match"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MatchResult$Type = (("exact_match") | ("match") | ("none") | ("weak")) | ($MatchResult);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MatchResult_ = $MatchResult$Type;
}}
declare module "packages/org/spongepowered/asm/mixin/transformer/$ClassContext" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $ClassContext {


}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClassContext$Type = ($ClassContext);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClassContext_ = $ClassContext$Type;
}}
declare module "packages/org/spongepowered/asm/mixin/transformer/ext/$IExtension" {
import {$ClassNode, $ClassNode$Type} from "packages/org/objectweb/asm/tree/$ClassNode"
import {$MixinEnvironment, $MixinEnvironment$Type} from "packages/org/spongepowered/asm/mixin/$MixinEnvironment"
import {$ITargetClassContext, $ITargetClassContext$Type} from "packages/org/spongepowered/asm/mixin/transformer/ext/$ITargetClassContext"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export interface $IExtension {

 "export"(arg0: $MixinEnvironment$Type, arg1: string, arg2: boolean, arg3: $ClassNode$Type): void
 "postApply"(arg0: $ITargetClassContext$Type): void
 "preApply"(arg0: $ITargetClassContext$Type): void
 "checkActive"(arg0: $MixinEnvironment$Type): boolean
}

export namespace $IExtension {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IExtension$Type = ($IExtension);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IExtension_ = $IExtension$Type;
}}
declare module "packages/org/spongepowered/asm/util/asm/$IAnnotationHandle" {
import {$List, $List$Type} from "packages/java/util/$List"
import {$Type, $Type$Type} from "packages/org/objectweb/asm/$Type"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export interface $IAnnotationHandle {

 "getBoolean"(arg0: string, arg1: boolean): boolean
 "getValue"<T>(): T
 "getValue"<T>(arg0: string): T
 "getValue"<T>(arg0: string, arg1: T): T
 "getAnnotation"(arg0: string): $IAnnotationHandle
 "exists"(): boolean
 "getDesc"(): string
 "getList"<T>(): $List<(T)>
 "getList"<T>(arg0: string): $List<(T)>
 "getAnnotationList"(arg0: string): $List<($IAnnotationHandle)>
 "getTypeList"(arg0: string): $List<($Type)>
 "getTypeValue"(arg0: string): $Type
}

export namespace $IAnnotationHandle {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IAnnotationHandle$Type = ($IAnnotationHandle);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IAnnotationHandle_ = $IAnnotationHandle$Type;
}}
declare module "packages/org/spongepowered/asm/mixin/transformer/$ClassInfo$Field" {
import {$ClassInfo, $ClassInfo$Type} from "packages/org/spongepowered/asm/mixin/transformer/$ClassInfo"
import {$ClassInfo$Member, $ClassInfo$Member$Type} from "packages/org/spongepowered/asm/mixin/transformer/$ClassInfo$Member"
import {$FieldNode, $FieldNode$Type} from "packages/org/objectweb/asm/tree/$FieldNode"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $ClassInfo$Field extends $ClassInfo$Member {

constructor(arg0: $ClassInfo$Type, arg1: string, arg2: string, arg3: integer, arg4: boolean)
constructor(arg0: $ClassInfo$Type, arg1: string, arg2: string, arg3: integer)
constructor(arg0: $ClassInfo$Type, arg1: $ClassInfo$Member$Type)
constructor(arg0: $ClassInfo$Type, arg1: $FieldNode$Type)
constructor(arg0: $ClassInfo$Type, arg1: $FieldNode$Type, arg2: boolean)

public "equals"(arg0: any): boolean
public "getOwner"(): $ClassInfo
get "owner"(): $ClassInfo
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClassInfo$Field$Type = ($ClassInfo$Field);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClassInfo$Field_ = $ClassInfo$Field$Type;
}}
declare module "packages/org/spongepowered/asm/mixin/injection/code/$MethodSlice" {
import {$AnnotationNode, $AnnotationNode$Type} from "packages/org/objectweb/asm/tree/$AnnotationNode"
import {$MethodNode, $MethodNode$Type} from "packages/org/objectweb/asm/tree/$MethodNode"
import {$InsnListReadOnly, $InsnListReadOnly$Type} from "packages/org/spongepowered/asm/mixin/injection/code/$InsnListReadOnly"
import {$ISliceContext, $ISliceContext$Type} from "packages/org/spongepowered/asm/mixin/injection/code/$ISliceContext"
import {$Slice, $Slice$Type} from "packages/org/spongepowered/asm/mixin/injection/$Slice"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $MethodSlice {


public "toString"(): string
public "getId"(): string
public static "parse"(arg0: $ISliceContext$Type, arg1: $AnnotationNode$Type): $MethodSlice
public static "parse"(arg0: $ISliceContext$Type, arg1: $Slice$Type): $MethodSlice
public "getSlice"(arg0: $MethodNode$Type): $InsnListReadOnly
get "id"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MethodSlice$Type = ($MethodSlice);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MethodSlice_ = $MethodSlice$Type;
}}
declare module "packages/org/spongepowered/asm/mixin/transformer/$IMixinTransformer" {
import {$ClassNode, $ClassNode$Type} from "packages/org/objectweb/asm/tree/$ClassNode"
import {$IExtensionRegistry, $IExtensionRegistry$Type} from "packages/org/spongepowered/asm/mixin/transformer/ext/$IExtensionRegistry"
import {$List, $List$Type} from "packages/java/util/$List"
import {$MixinEnvironment, $MixinEnvironment$Type} from "packages/org/spongepowered/asm/mixin/$MixinEnvironment"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export interface $IMixinTransformer {

 "audit"(arg0: $MixinEnvironment$Type): void
 "reload"(arg0: string, arg1: $ClassNode$Type): $List<(string)>
 "getExtensions"(): $IExtensionRegistry
 "transformClass"(arg0: $MixinEnvironment$Type, arg1: string, arg2: $ClassNode$Type): boolean
 "transformClass"(arg0: $MixinEnvironment$Type, arg1: string, arg2: (byte)[]): (byte)[]
 "generateClass"(arg0: $MixinEnvironment$Type, arg1: string, arg2: $ClassNode$Type): boolean
 "generateClass"(arg0: $MixinEnvironment$Type, arg1: string): (byte)[]
 "computeFramesForClass"(arg0: $MixinEnvironment$Type, arg1: string, arg2: $ClassNode$Type): boolean
 "transformClassBytes"(arg0: string, arg1: string, arg2: (byte)[]): (byte)[]
}

export namespace $IMixinTransformer {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IMixinTransformer$Type = ($IMixinTransformer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IMixinTransformer_ = $IMixinTransformer$Type;
}}
declare module "packages/org/spongepowered/asm/mixin/transformer/ext/$Extensions" {
import {$ClassNode, $ClassNode$Type} from "packages/org/objectweb/asm/tree/$ClassNode"
import {$ISyntheticClassRegistry, $ISyntheticClassRegistry$Type} from "packages/org/spongepowered/asm/service/$ISyntheticClassRegistry"
import {$IClassGenerator, $IClassGenerator$Type} from "packages/org/spongepowered/asm/mixin/transformer/ext/$IClassGenerator"
import {$IExtensionRegistry, $IExtensionRegistry$Type} from "packages/org/spongepowered/asm/mixin/transformer/ext/$IExtensionRegistry"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$MixinEnvironment, $MixinEnvironment$Type} from "packages/org/spongepowered/asm/mixin/$MixinEnvironment"
import {$ITargetClassContext, $ITargetClassContext$Type} from "packages/org/spongepowered/asm/mixin/transformer/ext/$ITargetClassContext"
import {$IExtension, $IExtension$Type} from "packages/org/spongepowered/asm/mixin/transformer/ext/$IExtension"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $Extensions implements $IExtensionRegistry {

constructor(arg0: $ISyntheticClassRegistry$Type)

public "add"(arg0: $IClassGenerator$Type): void
public "add"(arg0: $IExtension$Type): void
public "export"(arg0: $MixinEnvironment$Type, arg1: string, arg2: boolean, arg3: $ClassNode$Type): void
public "getExtensions"(): $List<($IExtension)>
public "getExtension"<T extends $IExtension>(arg0: $Class$Type<(any)>): T
public "getActiveExtensions"(): $List<($IExtension)>
public "getGenerator"<T extends $IClassGenerator>(arg0: $Class$Type<(any)>): T
public "postApply"(arg0: $ITargetClassContext$Type): void
public "select"(arg0: $MixinEnvironment$Type): void
public "getSyntheticClassRegistry"(): $ISyntheticClassRegistry
public "preApply"(arg0: $ITargetClassContext$Type): void
public "getGenerators"(): $List<($IClassGenerator)>
get "extensions"(): $List<($IExtension)>
get "activeExtensions"(): $List<($IExtension)>
get "syntheticClassRegistry"(): $ISyntheticClassRegistry
get "generators"(): $List<($IClassGenerator)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Extensions$Type = ($Extensions);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Extensions_ = $Extensions$Type;
}}
declare module "packages/org/spongepowered/asm/mixin/transformer/$ClassInfo$Traversal" {
import {$ClassInfo$SearchType, $ClassInfo$SearchType$Type} from "packages/org/spongepowered/asm/mixin/transformer/$ClassInfo$SearchType"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $ClassInfo$Traversal extends $Enum<($ClassInfo$Traversal)> {
static readonly "NONE": $ClassInfo$Traversal
static readonly "ALL": $ClassInfo$Traversal
static readonly "IMMEDIATE": $ClassInfo$Traversal
static readonly "SUPER": $ClassInfo$Traversal


public static "values"(): ($ClassInfo$Traversal)[]
public static "valueOf"(arg0: string): $ClassInfo$Traversal
public "next"(): $ClassInfo$Traversal
public "getSearchType"(): $ClassInfo$SearchType
public "canTraverse"(): boolean
get "searchType"(): $ClassInfo$SearchType
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClassInfo$Traversal$Type = (("all") | ("super") | ("immediate") | ("none")) | ($ClassInfo$Traversal);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClassInfo$Traversal_ = $ClassInfo$Traversal$Type;
}}
declare module "packages/org/spongepowered/asm/mixin/injection/callback/$CallbackInfoReturnable" {
import {$CallbackInfo, $CallbackInfo$Type} from "packages/org/spongepowered/asm/mixin/injection/callback/$CallbackInfo"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $CallbackInfoReturnable<R> extends $CallbackInfo {

constructor(arg0: string, arg1: boolean, arg2: float)
constructor(arg0: string, arg1: boolean, arg2: integer)
constructor(arg0: string, arg1: boolean, arg2: long)
constructor(arg0: string, arg1: boolean, arg2: short)
constructor(arg0: string, arg1: boolean, arg2: boolean)
constructor(arg0: string, arg1: boolean)
constructor(arg0: string, arg1: boolean, arg2: R)
constructor(arg0: string, arg1: boolean, arg2: byte)
constructor(arg0: string, arg1: boolean, arg2: character)
constructor(arg0: string, arg1: boolean, arg2: double)

public "getReturnValue"(): R
public "getReturnValueZ"(): boolean
public "getReturnValueJ"(): long
public "setReturnValue"(arg0: R): void
public "getReturnValueI"(): integer
public "getReturnValueC"(): character
public "getReturnValueD"(): double
public "getReturnValueF"(): float
public "getReturnValueB"(): byte
public "getReturnValueS"(): short
get "returnValue"(): R
get "returnValueZ"(): boolean
get "returnValueJ"(): long
set "returnValue"(value: R)
get "returnValueI"(): integer
get "returnValueC"(): character
get "returnValueD"(): double
get "returnValueF"(): float
get "returnValueB"(): byte
get "returnValueS"(): short
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CallbackInfoReturnable$Type<R> = ($CallbackInfoReturnable<(R)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CallbackInfoReturnable_<R> = $CallbackInfoReturnable$Type<(R)>;
}}
declare module "packages/org/spongepowered/asm/mixin/$MixinEnvironment" {
import {$RemapperChain, $RemapperChain$Type} from "packages/org/spongepowered/asm/obfuscation/$RemapperChain"
import {$ITransformer, $ITransformer$Type} from "packages/org/spongepowered/asm/service/$ITransformer"
import {$MixinEnvironment$Side, $MixinEnvironment$Side$Type} from "packages/org/spongepowered/asm/mixin/$MixinEnvironment$Side"
import {$Profiler, $Profiler$Type} from "packages/org/spongepowered/asm/util/perf/$Profiler"
import {$IMixinTransformer, $IMixinTransformer$Type} from "packages/org/spongepowered/asm/mixin/transformer/$IMixinTransformer"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$MixinEnvironment$Option, $MixinEnvironment$Option$Type} from "packages/org/spongepowered/asm/mixin/$MixinEnvironment$Option"
import {$List, $List$Type} from "packages/java/util/$List"
import {$MixinEnvironment$CompatibilityLevel, $MixinEnvironment$CompatibilityLevel$Type} from "packages/org/spongepowered/asm/mixin/$MixinEnvironment$CompatibilityLevel"
import {$IEnvironmentTokenProvider, $IEnvironmentTokenProvider$Type} from "packages/org/spongepowered/asm/mixin/extensibility/$IEnvironmentTokenProvider"
import {$ITokenProvider, $ITokenProvider$Type} from "packages/org/spongepowered/asm/util/$ITokenProvider"
import {$MixinEnvironment$Phase, $MixinEnvironment$Phase$Type} from "packages/org/spongepowered/asm/mixin/$MixinEnvironment$Phase"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $MixinEnvironment implements $ITokenProvider {


public "toString"(): string
public static "init"(arg0: $MixinEnvironment$Phase$Type): void
public "audit"(): void
public "getVersion"(): string
public "getToken"(arg0: string): integer
public "getPhase"(): $MixinEnvironment$Phase
/**
 * 
 * @deprecated
 */
public "getMixinConfigs"(): $List<(string)>
/**
 * 
 * @deprecated
 */
public "addConfiguration"(arg0: string): $MixinEnvironment
public "getRemappers"(): $RemapperChain
public "getSide"(): $MixinEnvironment$Side
public "setSide"(arg0: $MixinEnvironment$Side$Type): $MixinEnvironment
public "getOptionValue"(arg0: $MixinEnvironment$Option$Type): string
/**
 * 
 * @deprecated
 */
public static "getProfiler"(): $Profiler
public static "getEnvironment"(arg0: $MixinEnvironment$Phase$Type): $MixinEnvironment
public static "getDefaultEnvironment"(): $MixinEnvironment
public static "getCompatibilityLevel"(): $MixinEnvironment$CompatibilityLevel
public "registerTokenProviderClass"(arg0: string): $MixinEnvironment
public "setActiveTransformer"(arg0: $IMixinTransformer$Type): void
public "getObfuscationContext"(): string
/**
 * 
 * @deprecated
 */
public static "setCompatibilityLevel"(arg0: $MixinEnvironment$CompatibilityLevel$Type): void
/**
 * 
 * @deprecated
 */
public "getErrorHandlerClasses"(): $Set<(string)>
public "registerTokenProvider"(arg0: $IEnvironmentTokenProvider$Type): $MixinEnvironment
public "getActiveTransformer"(): any
public "setObfuscationContext"(arg0: string): void
public "getRefmapObfuscationContext"(): string
/**
 * 
 * @deprecated
 */
public "getTransformers"(): $List<($ITransformer)>
public "getOption"<E extends $Enum<(E)>>(arg0: $MixinEnvironment$Option$Type, arg1: E): E
public "getOption"(arg0: $MixinEnvironment$Option$Type): boolean
public "setOption"(arg0: $MixinEnvironment$Option$Type, arg1: boolean): void
/**
 * 
 * @deprecated
 */
public "addTransformerExclusion"(arg0: string): void
public static "getMinCompatibilityLevel"(): $MixinEnvironment$CompatibilityLevel
public static "getCurrentEnvironment"(): $MixinEnvironment
get "version"(): string
get "phase"(): $MixinEnvironment$Phase
get "mixinConfigs"(): $List<(string)>
get "remappers"(): $RemapperChain
get "side"(): $MixinEnvironment$Side
set "side"(value: $MixinEnvironment$Side$Type)
get "profiler"(): $Profiler
get "defaultEnvironment"(): $MixinEnvironment
get "compatibilityLevel"(): $MixinEnvironment$CompatibilityLevel
set "activeTransformer"(value: $IMixinTransformer$Type)
get "obfuscationContext"(): string
set "compatibilityLevel"(value: $MixinEnvironment$CompatibilityLevel$Type)
get "errorHandlerClasses"(): $Set<(string)>
get "activeTransformer"(): any
set "obfuscationContext"(value: string)
get "refmapObfuscationContext"(): string
get "transformers"(): $List<($ITransformer)>
get "minCompatibilityLevel"(): $MixinEnvironment$CompatibilityLevel
get "currentEnvironment"(): $MixinEnvironment
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MixinEnvironment$Type = ($MixinEnvironment);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MixinEnvironment_ = $MixinEnvironment$Type;
}}
declare module "packages/org/spongepowered/asm/mixin/injection/struct/$Target$Extension" {
import {$Type, $Type$Type} from "packages/org/objectweb/asm/$Type"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $Target$Extension {


public "add"(arg0: ($Type$Type)[]): $Target$Extension
public "add"(arg0: integer): $Target$Extension
public "add"(): $Target$Extension
public "get"(): integer
public "apply"(): void
public "set"(arg0: integer): $Target$Extension
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Target$Extension$Type = ($Target$Extension);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Target$Extension_ = $Target$Extension$Type;
}}
declare module "packages/org/spongepowered/asm/mixin/transformer/$ClassInfo" {
import {$ClassInfo$TypeLookup, $ClassInfo$TypeLookup$Type} from "packages/org/spongepowered/asm/mixin/transformer/$ClassInfo$TypeLookup"
import {$ClassInfo$SearchType, $ClassInfo$SearchType$Type} from "packages/org/spongepowered/asm/mixin/transformer/$ClassInfo$SearchType"
import {$MethodNode, $MethodNode$Type} from "packages/org/objectweb/asm/tree/$MethodNode"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$ClassSignature, $ClassSignature$Type} from "packages/org/spongepowered/asm/util/$ClassSignature"
import {$IMixinInfo, $IMixinInfo$Type} from "packages/org/spongepowered/asm/mixin/extensibility/$IMixinInfo"
import {$MethodInsnNode, $MethodInsnNode$Type} from "packages/org/objectweb/asm/tree/$MethodInsnNode"
import {$ClassInfo$Traversal, $ClassInfo$Traversal$Type} from "packages/org/spongepowered/asm/mixin/transformer/$ClassInfo$Traversal"
import {$FieldInsnNode, $FieldInsnNode$Type} from "packages/org/objectweb/asm/tree/$FieldInsnNode"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$ClassInfo$Method, $ClassInfo$Method$Type} from "packages/org/spongepowered/asm/mixin/transformer/$ClassInfo$Method"
import {$Type, $Type$Type} from "packages/org/objectweb/asm/$Type"
import {$ClassInfo$Field, $ClassInfo$Field$Type} from "packages/org/spongepowered/asm/mixin/transformer/$ClassInfo$Field"
import {$FieldNode, $FieldNode$Type} from "packages/org/objectweb/asm/tree/$FieldNode"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $ClassInfo {
static readonly "INCLUDE_PRIVATE": integer
static readonly "INCLUDE_STATIC": integer
static readonly "INCLUDE_ALL": integer
static readonly "INCLUDE_INITIALISERS": integer


public "getName"(): string
public static "forName"(arg0: string): $ClassInfo
public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "isInterface"(): boolean
public "getInterfaces"(): $Set<(string)>
public "getSimpleName"(): string
public "getNestHost"(): string
public "isSynthetic"(): boolean
public "getMethods"(): $Set<($ClassInfo$Method)>
public "getNestMembers"(): $Set<(string)>
public "isPublic"(): boolean
public "isProtected"(): boolean
public "getType"(): $Type
public "getSignature"(): $ClassSignature
public "isAbstract"(): boolean
public "isPrivate"(): boolean
public "getClassName"(): string
public "getAccess"(): integer
public "getSuperName"(): string
public static "getCommonSuperClass"(arg0: string, arg1: string): $ClassInfo
public static "getCommonSuperClass"(arg0: $Type$Type, arg1: $Type$Type): $ClassInfo
public static "forType"(arg0: $Type$Type, arg1: $ClassInfo$TypeLookup$Type): $ClassInfo
public "getSuperClass"(): $ClassInfo
public "isMixin"(): boolean
public "isLoadable"(): boolean
public "resolveNestHost"(): $ClassInfo
public "findMethod"(arg0: $MethodInsnNode$Type, arg1: integer): $ClassInfo$Method
public "findMethod"(arg0: $MethodNode$Type, arg1: integer): $ClassInfo$Method
public "findMethod"(arg0: string, arg1: string, arg2: integer): $ClassInfo$Method
public "findMethod"(arg0: $MethodNode$Type): $ClassInfo$Method
public "findMethod"(arg0: $MethodInsnNode$Type): $ClassInfo$Method
public "hasMixinInHierarchy"(): boolean
public "hasMixinTargetInHierarchy"(): boolean
public static "getCommonSuperClassOrInterface"(arg0: $ClassInfo$Type, arg1: $ClassInfo$Type): $ClassInfo
public static "getCommonSuperClassOrInterface"(arg0: string, arg1: string): $ClassInfo
public static "getCommonSuperClassOrInterface"(arg0: $Type$Type, arg1: $Type$Type): $ClassInfo
public "findFieldInHierarchy"(arg0: $FieldNode$Type, arg1: $ClassInfo$SearchType$Type): $ClassInfo$Field
public "findFieldInHierarchy"(arg0: string, arg1: string, arg2: $ClassInfo$SearchType$Type, arg3: $ClassInfo$Traversal$Type, arg4: integer): $ClassInfo$Field
public "findFieldInHierarchy"(arg0: $FieldNode$Type, arg1: $ClassInfo$SearchType$Type, arg2: integer): $ClassInfo$Field
public "findFieldInHierarchy"(arg0: $FieldInsnNode$Type, arg1: $ClassInfo$SearchType$Type): $ClassInfo$Field
public "findFieldInHierarchy"(arg0: $FieldInsnNode$Type, arg1: $ClassInfo$SearchType$Type, arg2: integer): $ClassInfo$Field
public "findFieldInHierarchy"(arg0: string, arg1: string, arg2: $ClassInfo$SearchType$Type): $ClassInfo$Field
public "findFieldInHierarchy"(arg0: string, arg1: string, arg2: $ClassInfo$SearchType$Type, arg3: $ClassInfo$Traversal$Type): $ClassInfo$Field
public "getOuterClass"(): $ClassInfo
public "isInner"(): boolean
public "isProbablyStatic"(): boolean
public "isReallyPublic"(): boolean
public "getOuterName"(): string
public "findSuperClass"(arg0: string, arg1: $ClassInfo$Traversal$Type): $ClassInfo
public "findSuperClass"(arg0: string, arg1: $ClassInfo$Traversal$Type, arg2: boolean): $ClassInfo
public "findSuperClass"(arg0: string): $ClassInfo
public static "forDescriptor"(arg0: string, arg1: $ClassInfo$TypeLookup$Type): $ClassInfo
public "getAppliedMixins"(): $Set<($IMixinInfo)>
public static "fromCache"(arg0: $Type$Type, arg1: $ClassInfo$TypeLookup$Type): $ClassInfo
public static "fromCache"(arg0: string): $ClassInfo
public "getInterfaceMethods"(arg0: boolean): $Set<($ClassInfo$Method)>
public "findMethodInHierarchy"(arg0: string, arg1: string, arg2: $ClassInfo$SearchType$Type, arg3: $ClassInfo$Traversal$Type, arg4: integer): $ClassInfo$Method
public "findMethodInHierarchy"(arg0: $MethodNode$Type, arg1: $ClassInfo$SearchType$Type, arg2: $ClassInfo$Traversal$Type): $ClassInfo$Method
public "findMethodInHierarchy"(arg0: $MethodNode$Type, arg1: $ClassInfo$SearchType$Type, arg2: integer): $ClassInfo$Method
public "findMethodInHierarchy"(arg0: string, arg1: string, arg2: $ClassInfo$SearchType$Type, arg3: $ClassInfo$Traversal$Type): $ClassInfo$Method
public "findMethodInHierarchy"(arg0: $MethodInsnNode$Type, arg1: $ClassInfo$SearchType$Type, arg2: integer): $ClassInfo$Method
public "findMethodInHierarchy"(arg0: string, arg1: string, arg2: $ClassInfo$SearchType$Type): $ClassInfo$Method
public "findMethodInHierarchy"(arg0: $MethodInsnNode$Type, arg1: $ClassInfo$SearchType$Type): $ClassInfo$Method
public "findMethodInHierarchy"(arg0: $MethodNode$Type, arg1: $ClassInfo$SearchType$Type, arg2: $ClassInfo$Traversal$Type, arg3: integer): $ClassInfo$Method
public "findMethodInHierarchy"(arg0: $MethodNode$Type, arg1: $ClassInfo$SearchType$Type): $ClassInfo$Method
public "findField"(arg0: $FieldNode$Type): $ClassInfo$Field
public "findField"(arg0: $FieldInsnNode$Type, arg1: integer): $ClassInfo$Field
public "findField"(arg0: string, arg1: string, arg2: integer): $ClassInfo$Field
public "hasSuperClass"(arg0: $Class$Type<(any)>, arg1: $ClassInfo$Traversal$Type, arg2: boolean): boolean
public "hasSuperClass"(arg0: $Class$Type<(any)>, arg1: $ClassInfo$Traversal$Type): boolean
public "hasSuperClass"(arg0: $Class$Type<(any)>): boolean
public "hasSuperClass"(arg0: $ClassInfo$Type, arg1: $ClassInfo$Traversal$Type, arg2: boolean): boolean
public "hasSuperClass"(arg0: $ClassInfo$Type): boolean
public "hasSuperClass"(arg0: $ClassInfo$Type, arg1: $ClassInfo$Traversal$Type): boolean
public "hasSuperClass"(arg0: string, arg1: $ClassInfo$Traversal$Type, arg2: boolean): boolean
public "hasSuperClass"(arg0: string, arg1: $ClassInfo$Traversal$Type): boolean
public "hasSuperClass"(arg0: string): boolean
get "name"(): string
get "interface"(): boolean
get "interfaces"(): $Set<(string)>
get "simpleName"(): string
get "nestHost"(): string
get "synthetic"(): boolean
get "methods"(): $Set<($ClassInfo$Method)>
get "nestMembers"(): $Set<(string)>
get "public"(): boolean
get "protected"(): boolean
get "type"(): $Type
get "signature"(): $ClassSignature
get "abstract"(): boolean
get "private"(): boolean
get "className"(): string
get "access"(): integer
get "superName"(): string
get "superClass"(): $ClassInfo
get "mixin"(): boolean
get "loadable"(): boolean
get "outerClass"(): $ClassInfo
get "inner"(): boolean
get "probablyStatic"(): boolean
get "reallyPublic"(): boolean
get "outerName"(): string
get "appliedMixins"(): $Set<($IMixinInfo)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClassInfo$Type = ($ClassInfo);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClassInfo_ = $ClassInfo$Type;
}}
declare module "packages/org/spongepowered/asm/mixin/transformer/ext/$IClassGenerator" {
import {$ClassNode, $ClassNode$Type} from "packages/org/objectweb/asm/tree/$ClassNode"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export interface $IClassGenerator {

 "getName"(): string
 "generate"(arg0: string, arg1: $ClassNode$Type): boolean
}

export namespace $IClassGenerator {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IClassGenerator$Type = ($IClassGenerator);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IClassGenerator_ = $IClassGenerator$Type;
}}
declare module "packages/org/spongepowered/asm/mixin/transformer/$ClassInfo$TypeLookup" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $ClassInfo$TypeLookup extends $Enum<($ClassInfo$TypeLookup)> {
static readonly "DECLARED_TYPE": $ClassInfo$TypeLookup
static readonly "ELEMENT_TYPE": $ClassInfo$TypeLookup


public static "values"(): ($ClassInfo$TypeLookup)[]
public static "valueOf"(arg0: string): $ClassInfo$TypeLookup
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClassInfo$TypeLookup$Type = (("element_type") | ("declared_type")) | ($ClassInfo$TypeLookup);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClassInfo$TypeLookup_ = $ClassInfo$TypeLookup$Type;
}}
declare module "packages/org/spongepowered/asm/util/$IMessageSink" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export interface $IMessageSink {

 "addMessage"(arg0: string, ...arg1: (any)[]): void

(arg0: string, ...arg1: (any)[]): void
}

export namespace $IMessageSink {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IMessageSink$Type = ($IMessageSink);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IMessageSink_ = $IMessageSink$Type;
}}
declare module "packages/org/spongepowered/asm/mixin/injection/struct/$InjectorGroupInfo" {
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$InjectionInfo, $InjectionInfo$Type} from "packages/org/spongepowered/asm/mixin/injection/struct/$InjectionInfo"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $InjectorGroupInfo {

constructor(arg0: string)

public "getName"(): string
public "add"(arg0: $InjectionInfo$Type): $InjectorGroupInfo
public "toString"(): string
public "validate"(): $InjectorGroupInfo
public "isDefault"(): boolean
public "getMembers"(): $Collection<($InjectionInfo)>
public "setMaxAllowed"(arg0: integer): void
public "setMinRequired"(arg0: integer): void
public "getMinRequired"(): integer
public "getMaxAllowed"(): integer
get "name"(): string
get "default"(): boolean
get "members"(): $Collection<($InjectionInfo)>
set "maxAllowed"(value: integer)
set "minRequired"(value: integer)
get "minRequired"(): integer
get "maxAllowed"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InjectorGroupInfo$Type = ($InjectorGroupInfo);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InjectorGroupInfo_ = $InjectorGroupInfo$Type;
}}
declare module "packages/org/spongepowered/asm/mixin/injection/points/$BeforeInvoke" {
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$InjectionPoint, $InjectionPoint$Type} from "packages/org/spongepowered/asm/mixin/injection/$InjectionPoint"
import {$InsnList, $InsnList$Type} from "packages/org/objectweb/asm/tree/$InsnList"
import {$InjectionPointData, $InjectionPointData$Type} from "packages/org/spongepowered/asm/mixin/injection/struct/$InjectionPointData"
import {$AbstractInsnNode, $AbstractInsnNode$Type} from "packages/org/objectweb/asm/tree/$AbstractInsnNode"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $BeforeInvoke extends $InjectionPoint {
static readonly "DEFAULT_ALLOWED_SHIFT_BY": integer
static readonly "MAX_ALLOWED_SHIFT_BY": integer

constructor(arg0: $InjectionPointData$Type)

public "find"(arg0: string, arg1: $InsnList$Type, arg2: $Collection$Type<($AbstractInsnNode$Type)>): boolean
public "setLogging"(arg0: boolean): $BeforeInvoke
set "logging"(value: boolean)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BeforeInvoke$Type = ($BeforeInvoke);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BeforeInvoke_ = $BeforeInvoke$Type;
}}
declare module "packages/org/spongepowered/asm/mixin/injection/code/$Injector" {
import {$Target, $Target$Type} from "packages/org/spongepowered/asm/mixin/injection/struct/$Target"
import {$InjectionInfo, $InjectionInfo$Type} from "packages/org/spongepowered/asm/mixin/injection/struct/$InjectionInfo"
import {$List, $List$Type} from "packages/java/util/$List"
import {$InjectorTarget, $InjectorTarget$Type} from "packages/org/spongepowered/asm/mixin/injection/code/$InjectorTarget"
import {$Type, $Type$Type} from "packages/org/objectweb/asm/$Type"
import {$InjectionPoint, $InjectionPoint$Type} from "packages/org/spongepowered/asm/mixin/injection/$InjectionPoint"
import {$InjectionNodes$InjectionNode, $InjectionNodes$InjectionNode$Type} from "packages/org/spongepowered/asm/mixin/injection/struct/$InjectionNodes$InjectionNode"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $Injector {

constructor(arg0: $InjectionInfo$Type, arg1: string)

public "toString"(): string
public "find"(arg0: $InjectorTarget$Type, arg1: $List$Type<($InjectionPoint$Type)>): $List<($InjectionNodes$InjectionNode)>
public "inject"(arg0: $Target$Type, arg1: $List$Type<($InjectionNodes$InjectionNode$Type)>): void
public "preInject"(arg0: $Target$Type, arg1: $List$Type<($InjectionNodes$InjectionNode$Type)>): void
public static "canCoerce"(arg0: string, arg1: string): boolean
public static "canCoerce"(arg0: $Type$Type, arg1: $Type$Type): boolean
public static "canCoerce"(arg0: character, arg1: character): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Injector$Type = ($Injector);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Injector_ = $Injector$Type;
}}
declare module "packages/org/spongepowered/asm/service/$ISyntheticClassRegistry" {
import {$ISyntheticClassInfo, $ISyntheticClassInfo$Type} from "packages/org/spongepowered/asm/service/$ISyntheticClassInfo"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export interface $ISyntheticClassRegistry {

 "findSyntheticClass"(arg0: string): $ISyntheticClassInfo

(arg0: string): $ISyntheticClassInfo
}

export namespace $ISyntheticClassRegistry {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ISyntheticClassRegistry$Type = ($ISyntheticClassRegistry);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ISyntheticClassRegistry_ = $ISyntheticClassRegistry$Type;
}}
declare module "packages/org/spongepowered/asm/mixin/extensibility/$IMixinErrorHandler$ErrorAction" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$Level, $Level$Type} from "packages/org/spongepowered/asm/logging/$Level"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $IMixinErrorHandler$ErrorAction extends $Enum<($IMixinErrorHandler$ErrorAction)> {
static readonly "NONE": $IMixinErrorHandler$ErrorAction
static readonly "WARN": $IMixinErrorHandler$ErrorAction
static readonly "ERROR": $IMixinErrorHandler$ErrorAction
readonly "logLevel": $Level


public static "values"(): ($IMixinErrorHandler$ErrorAction)[]
public static "valueOf"(arg0: string): $IMixinErrorHandler$ErrorAction
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IMixinErrorHandler$ErrorAction$Type = (("warn") | ("none") | ("error")) | ($IMixinErrorHandler$ErrorAction);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IMixinErrorHandler$ErrorAction_ = $IMixinErrorHandler$ErrorAction$Type;
}}
declare module "packages/org/spongepowered/asm/mixin/injection/callback/$CallbackInfo" {
import {$Cancellable, $Cancellable$Type} from "packages/org/spongepowered/asm/mixin/injection/callback/$Cancellable"
import {$Type, $Type$Type} from "packages/org/objectweb/asm/$Type"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $CallbackInfo implements $Cancellable {

constructor(arg0: string, arg1: boolean)

public "toString"(): string
public "getId"(): string
public "cancel"(): void
public "isCancelled"(): boolean
public "isCancellable"(): boolean
public static "getCallInfoClassName"(arg0: $Type$Type): string
get "id"(): string
get "cancelled"(): boolean
get "cancellable"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CallbackInfo$Type = ($CallbackInfo);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CallbackInfo_ = $CallbackInfo$Type;
}}
declare module "packages/org/spongepowered/asm/mixin/transformer/$MixinTargetContext" {
import {$ClassNode, $ClassNode$Type} from "packages/org/objectweb/asm/tree/$ClassNode"
import {$SourceMap$File, $SourceMap$File$Type} from "packages/org/spongepowered/asm/mixin/struct/$SourceMap$File"
import {$MethodNode, $MethodNode$Type} from "packages/org/objectweb/asm/tree/$MethodNode"
import {$Target, $Target$Type} from "packages/org/spongepowered/asm/mixin/injection/struct/$Target"
import {$IMixinContext, $IMixinContext$Type} from "packages/org/spongepowered/asm/mixin/refmap/$IMixinContext"
import {$MixinEnvironment, $MixinEnvironment$Type} from "packages/org/spongepowered/asm/mixin/$MixinEnvironment"
import {$InjectorGroupInfo$Map, $InjectorGroupInfo$Map$Type} from "packages/org/spongepowered/asm/mixin/injection/struct/$InjectorGroupInfo$Map"
import {$ClassSignature, $ClassSignature$Type} from "packages/org/spongepowered/asm/util/$ClassSignature"
import {$Extensions, $Extensions$Type} from "packages/org/spongepowered/asm/mixin/transformer/ext/$Extensions"
import {$IMixinInfo, $IMixinInfo$Type} from "packages/org/spongepowered/asm/mixin/extensibility/$IMixinInfo"
import {$TargetClassContext, $TargetClassContext$Type} from "packages/org/spongepowered/asm/mixin/transformer/$TargetClassContext"
import {$MixinEnvironment$Option, $MixinEnvironment$Option$Type} from "packages/org/spongepowered/asm/mixin/$MixinEnvironment$Option"
import {$IReferenceMapper, $IReferenceMapper$Type} from "packages/org/spongepowered/asm/mixin/refmap/$IReferenceMapper"
import {$ClassInfo, $ClassInfo$Type} from "packages/org/spongepowered/asm/mixin/transformer/$ClassInfo"
import {$ClassContext, $ClassContext$Type} from "packages/org/spongepowered/asm/mixin/transformer/$ClassContext"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $MixinTargetContext extends $ClassContext implements $IMixinContext {


public "toString"(): string
public "getPriority"(): integer
public "getSignature"(): $ClassSignature
public "getTarget"(): $TargetClassContext
public "getClassName"(): string
public "getExtensions"(): $Extensions
public "getClassInfo"(): $ClassInfo
public "getEnvironment"(): $MixinEnvironment
public "getClassNode"(): $ClassNode
public "getMinRequiredClassVersion"(): integer
public "getReferenceMapper"(): $IReferenceMapper
public "getMaxShiftByValue"(): integer
public "getOption"(arg0: $MixinEnvironment$Option$Type): boolean
public "getMixin"(): $IMixinInfo
public "getClassRef"(): string
public "getTargetMethod"(arg0: $MethodNode$Type): $Target
public "getTargetClassRef"(): string
public "getTargetClassInfo"(): $ClassInfo
public "getStratum"(): $SourceMap$File
public "getTargetClassNode"(): $ClassNode
public "getInjectorGroups"(): $InjectorGroupInfo$Map
public "getDefaultRequiredInjections"(): integer
public "getDefaultInjectorGroup"(): string
public "requireOverwriteAnnotations"(): boolean
get "priority"(): integer
get "signature"(): $ClassSignature
get "target"(): $TargetClassContext
get "className"(): string
get "extensions"(): $Extensions
get "classInfo"(): $ClassInfo
get "environment"(): $MixinEnvironment
get "classNode"(): $ClassNode
get "minRequiredClassVersion"(): integer
get "referenceMapper"(): $IReferenceMapper
get "maxShiftByValue"(): integer
get "mixin"(): $IMixinInfo
get "classRef"(): string
get "targetClassRef"(): string
get "targetClassInfo"(): $ClassInfo
get "stratum"(): $SourceMap$File
get "targetClassNode"(): $ClassNode
get "injectorGroups"(): $InjectorGroupInfo$Map
get "defaultRequiredInjections"(): integer
get "defaultInjectorGroup"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MixinTargetContext$Type = ($MixinTargetContext);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MixinTargetContext_ = $MixinTargetContext$Type;
}}
declare module "packages/org/spongepowered/asm/mixin/extensibility/$IRemapper" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export interface $IRemapper {

 "map"(arg0: string): string
 "unmap"(arg0: string): string
 "mapFieldName"(arg0: string, arg1: string, arg2: string): string
 "mapDesc"(arg0: string): string
 "unmapDesc"(arg0: string): string
 "mapMethodName"(arg0: string, arg1: string, arg2: string): string
}

export namespace $IRemapper {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IRemapper$Type = ($IRemapper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IRemapper_ = $IRemapper$Type;
}}
declare module "packages/org/spongepowered/asm/util/$ClassSignature" {
import {$ClassNode, $ClassNode$Type} from "packages/org/objectweb/asm/tree/$ClassNode"
import {$SignatureVisitor, $SignatureVisitor$Type} from "packages/org/objectweb/asm/signature/$SignatureVisitor"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $ClassSignature {


public "toString"(): string
public static "of"(arg0: string): $ClassSignature
public static "of"(arg0: $ClassNode$Type): $ClassSignature
public "merge"(arg0: $ClassSignature$Type): void
public "getSuperClass"(): string
public "addInterface"(arg0: string): void
public "wake"(): $ClassSignature
public static "ofLazy"(arg0: $ClassNode$Type): $ClassSignature
public "getRemapper"(): $SignatureVisitor
get "superClass"(): string
get "remapper"(): $SignatureVisitor
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClassSignature$Type = ($ClassSignature);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClassSignature_ = $ClassSignature$Type;
}}
declare module "packages/org/spongepowered/asm/mixin/extensibility/$IMixinConfigPlugin" {
import {$IMixinInfo, $IMixinInfo$Type} from "packages/org/spongepowered/asm/mixin/extensibility/$IMixinInfo"
import {$ClassNode, $ClassNode$Type} from "packages/org/objectweb/asm/tree/$ClassNode"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$List, $List$Type} from "packages/java/util/$List"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export interface $IMixinConfigPlugin {

 "onLoad"(arg0: string): void
 "postApply"(arg0: string, arg1: $ClassNode$Type, arg2: string, arg3: $IMixinInfo$Type): void
 "getMixins"(): $List<(string)>
 "getRefMapperConfig"(): string
 "shouldApplyMixin"(arg0: string, arg1: string): boolean
 "preApply"(arg0: string, arg1: $ClassNode$Type, arg2: string, arg3: $IMixinInfo$Type): void
 "acceptTargets"(arg0: $Set$Type<(string)>, arg1: $Set$Type<(string)>): void
}

export namespace $IMixinConfigPlugin {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IMixinConfigPlugin$Type = ($IMixinConfigPlugin);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IMixinConfigPlugin_ = $IMixinConfigPlugin$Type;
}}
declare module "packages/org/spongepowered/asm/mixin/injection/$InjectionPoint$Selector" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $InjectionPoint$Selector extends $Enum<($InjectionPoint$Selector)> {
static readonly "FIRST": $InjectionPoint$Selector
static readonly "LAST": $InjectionPoint$Selector
static readonly "ONE": $InjectionPoint$Selector
static readonly "DEFAULT": $InjectionPoint$Selector


public static "values"(): ($InjectionPoint$Selector)[]
public static "valueOf"(arg0: string): $InjectionPoint$Selector
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InjectionPoint$Selector$Type = (("last") | ("one") | ("first")) | ($InjectionPoint$Selector);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InjectionPoint$Selector_ = $InjectionPoint$Selector$Type;
}}
declare module "packages/org/spongepowered/asm/mixin/extensibility/$IMixinConfig" {
import {$IMixinConfigPlugin, $IMixinConfigPlugin$Type} from "packages/org/spongepowered/asm/mixin/extensibility/$IMixinConfigPlugin"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$MixinEnvironment, $MixinEnvironment$Type} from "packages/org/spongepowered/asm/mixin/$MixinEnvironment"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export interface $IMixinConfig {

 "getName"(): string
 "getPriority"(): integer
 "getEnvironment"(): $MixinEnvironment
 "isRequired"(): boolean
 "getPlugin"(): $IMixinConfigPlugin
 "getDecoration"<V>(arg0: string): V
 "decorate"<V>(arg0: string, arg1: V): void
 "getMixinPackage"(): string
 "hasDecoration"(arg0: string): boolean
 "getTargets"(): $Set<(string)>
}

export namespace $IMixinConfig {
const DEFAULT_PRIORITY: integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IMixinConfig$Type = ($IMixinConfig);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IMixinConfig_ = $IMixinConfig$Type;
}}
declare module "packages/org/spongepowered/asm/mixin/$MixinEnvironment$Option" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $MixinEnvironment$Option extends $Enum<($MixinEnvironment$Option)> {
static readonly "DEBUG_ALL": $MixinEnvironment$Option
static readonly "DEBUG_EXPORT": $MixinEnvironment$Option
static readonly "DEBUG_EXPORT_FILTER": $MixinEnvironment$Option
static readonly "DEBUG_EXPORT_DECOMPILE": $MixinEnvironment$Option
static readonly "DEBUG_EXPORT_DECOMPILE_THREADED": $MixinEnvironment$Option
static readonly "DEBUG_EXPORT_DECOMPILE_MERGESIGNATURES": $MixinEnvironment$Option
static readonly "DEBUG_VERIFY": $MixinEnvironment$Option
static readonly "DEBUG_VERBOSE": $MixinEnvironment$Option
static readonly "DEBUG_INJECTORS": $MixinEnvironment$Option
static readonly "DEBUG_STRICT": $MixinEnvironment$Option
static readonly "DEBUG_UNIQUE": $MixinEnvironment$Option
static readonly "DEBUG_TARGETS": $MixinEnvironment$Option
static readonly "DEBUG_PROFILER": $MixinEnvironment$Option
static readonly "DUMP_TARGET_ON_FAILURE": $MixinEnvironment$Option
static readonly "CHECK_ALL": $MixinEnvironment$Option
static readonly "CHECK_IMPLEMENTS": $MixinEnvironment$Option
static readonly "CHECK_IMPLEMENTS_STRICT": $MixinEnvironment$Option
static readonly "IGNORE_CONSTRAINTS": $MixinEnvironment$Option
static readonly "HOT_SWAP": $MixinEnvironment$Option
static readonly "ENVIRONMENT": $MixinEnvironment$Option
static readonly "OBFUSCATION_TYPE": $MixinEnvironment$Option
static readonly "DISABLE_REFMAP": $MixinEnvironment$Option
static readonly "REFMAP_REMAP": $MixinEnvironment$Option
static readonly "REFMAP_REMAP_RESOURCE": $MixinEnvironment$Option
static readonly "REFMAP_REMAP_SOURCE_ENV": $MixinEnvironment$Option
static readonly "REFMAP_REMAP_ALLOW_PERMISSIVE": $MixinEnvironment$Option
static readonly "IGNORE_REQUIRED": $MixinEnvironment$Option
static readonly "DEFAULT_COMPATIBILITY_LEVEL": $MixinEnvironment$Option
static readonly "SHIFT_BY_VIOLATION_BEHAVIOUR": $MixinEnvironment$Option
static readonly "INITIALISER_INJECTION_MODE": $MixinEnvironment$Option


public "toString"(): string
public static "values"(): ($MixinEnvironment$Option)[]
public static "valueOf"(arg0: string): $MixinEnvironment$Option
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MixinEnvironment$Option$Type = (("disable_refmap") | ("initialiser_injection_mode") | ("check_implements") | ("refmap_remap_source_env") | ("debug_all") | ("debug_verify") | ("dump_target_on_failure") | ("debug_export_filter") | ("debug_unique") | ("ignore_constraints") | ("debug_strict") | ("debug_export") | ("debug_export_decompile_threaded") | ("debug_profiler") | ("debug_export_decompile_mergesignatures") | ("obfuscation_type") | ("refmap_remap_resource") | ("debug_targets") | ("refmap_remap") | ("debug_export_decompile") | ("check_all") | ("debug_injectors") | ("refmap_remap_allow_permissive") | ("check_implements_strict") | ("debug_verbose") | ("environment") | ("hot_swap") | ("default_compatibility_level") | ("ignore_required") | ("shift_by_violation_behaviour")) | ($MixinEnvironment$Option);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MixinEnvironment$Option_ = $MixinEnvironment$Option$Type;
}}
declare module "packages/org/spongepowered/asm/mixin/injection/$InjectionPoint$RestrictTargetLevel" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $InjectionPoint$RestrictTargetLevel extends $Enum<($InjectionPoint$RestrictTargetLevel)> {
static readonly "METHODS_ONLY": $InjectionPoint$RestrictTargetLevel
static readonly "CONSTRUCTORS_AFTER_DELEGATE": $InjectionPoint$RestrictTargetLevel
static readonly "ALLOW_ALL": $InjectionPoint$RestrictTargetLevel


public static "values"(): ($InjectionPoint$RestrictTargetLevel)[]
public static "valueOf"(arg0: string): $InjectionPoint$RestrictTargetLevel
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InjectionPoint$RestrictTargetLevel$Type = (("methods_only") | ("allow_all") | ("constructors_after_delegate")) | ($InjectionPoint$RestrictTargetLevel);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InjectionPoint$RestrictTargetLevel_ = $InjectionPoint$RestrictTargetLevel$Type;
}}
declare module "packages/org/spongepowered/asm/mixin/injection/selectors/$ElementNode$NodeType" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $ElementNode$NodeType extends $Enum<($ElementNode$NodeType)> {
static readonly "UNDEFINED": $ElementNode$NodeType
static readonly "METHOD": $ElementNode$NodeType
static readonly "FIELD": $ElementNode$NodeType
static readonly "METHOD_INSN": $ElementNode$NodeType
static readonly "FIELD_INSN": $ElementNode$NodeType
static readonly "INVOKEDYNAMIC_INSN": $ElementNode$NodeType
readonly "hasMethod": boolean
readonly "hasField": boolean
readonly "hasInsn": boolean


public static "values"(): ($ElementNode$NodeType)[]
public static "valueOf"(arg0: string): $ElementNode$NodeType
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ElementNode$NodeType$Type = (("invokedynamic_insn") | ("method") | ("field") | ("method_insn") | ("field_insn") | ("undefined")) | ($ElementNode$NodeType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ElementNode$NodeType_ = $ElementNode$NodeType$Type;
}}
declare module "packages/org/spongepowered/asm/mixin/injection/modify/$LocalVariableDiscriminator" {
import {$LocalVariableDiscriminator$Context, $LocalVariableDiscriminator$Context$Type} from "packages/org/spongepowered/asm/mixin/injection/modify/$LocalVariableDiscriminator$Context"
import {$AnnotationNode, $AnnotationNode$Type} from "packages/org/objectweb/asm/tree/$AnnotationNode"
import {$Set, $Set$Type} from "packages/java/util/$Set"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $LocalVariableDiscriminator {

constructor(arg0: boolean, arg1: integer, arg2: integer, arg3: $Set$Type<(string)>, arg4: boolean)

public "toString"(arg0: $LocalVariableDiscriminator$Context$Type): string
public "toString"(): string
public "getIndex"(): integer
public static "parse"(arg0: $AnnotationNode$Type): $LocalVariableDiscriminator
public "getOrdinal"(): integer
public "printLVT"(): boolean
public "findLocal"(arg0: $LocalVariableDiscriminator$Context$Type): integer
public "hasNames"(): boolean
public "isArgsOnly"(): boolean
public "getNames"(): $Set<(string)>
get "index"(): integer
get "ordinal"(): integer
get "argsOnly"(): boolean
get "names"(): $Set<(string)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LocalVariableDiscriminator$Type = ($LocalVariableDiscriminator);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LocalVariableDiscriminator_ = $LocalVariableDiscriminator$Type;
}}
declare module "packages/org/spongepowered/asm/util/$PrettyPrinter" {
import {$Throwable, $Throwable$Type} from "packages/java/lang/$Throwable"
import {$PrettyPrinter$IPrettyPrintable, $PrettyPrinter$IPrettyPrintable$Type} from "packages/org/spongepowered/asm/util/$PrettyPrinter$IPrettyPrintable"
import {$StackTraceElement, $StackTraceElement$Type} from "packages/java/lang/$StackTraceElement"
import {$ILogger, $ILogger$Type} from "packages/org/spongepowered/asm/logging/$ILogger"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Level, $Level$Type} from "packages/org/spongepowered/asm/logging/$Level"
import {$PrintStream, $PrintStream$Type} from "packages/java/io/$PrintStream"
import {$Map, $Map$Type} from "packages/java/util/$Map"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $PrettyPrinter {

constructor()
constructor(arg0: integer)

public "addIndexed"(arg0: (any)[]): $PrettyPrinter
public "addWithIndices"(arg0: $Collection$Type<(any)>): $PrettyPrinter
public "add"(arg0: $Throwable$Type): $PrettyPrinter
public "add"(arg0: $PrettyPrinter$IPrettyPrintable$Type): $PrettyPrinter
public "add"(arg0: any): $PrettyPrinter
public "add"(arg0: any, arg1: integer): $PrettyPrinter
public "add"(arg0: $Map$Type<(any), (any)>): $PrettyPrinter
public "add"(arg0: ($StackTraceElement$Type)[], arg1: integer): $PrettyPrinter
public "add"(arg0: $Throwable$Type, arg1: integer): $PrettyPrinter
public "add"(): $PrettyPrinter
public "add"(arg0: string): $PrettyPrinter
public "add"(arg0: string, ...arg1: (any)[]): $PrettyPrinter
public "add"(arg0: (any)[]): $PrettyPrinter
public "add"(arg0: (any)[], arg1: string): $PrettyPrinter
public "log"(arg0: $Level$Type): $PrettyPrinter
public "log"(arg0: $ILogger$Type, arg1: $Level$Type): $PrettyPrinter
public "log"(arg0: $ILogger$Type): $PrettyPrinter
public "trace"(arg0: $ILogger$Type): $PrettyPrinter
public "trace"(arg0: $ILogger$Type, arg1: $Level$Type): $PrettyPrinter
public "trace"(arg0: $PrintStream$Type): $PrettyPrinter
public "trace"(arg0: string): $PrettyPrinter
public "trace"(arg0: string, arg1: $Level$Type): $PrettyPrinter
public "trace"(): $PrettyPrinter
public "trace"(arg0: $Level$Type): $PrettyPrinter
public "trace"(arg0: $PrintStream$Type, arg1: $ILogger$Type, arg2: $Level$Type): $PrettyPrinter
public "trace"(arg0: $PrintStream$Type, arg1: $ILogger$Type): $PrettyPrinter
public "trace"(arg0: $PrintStream$Type, arg1: string, arg2: $Level$Type): $PrettyPrinter
public "trace"(arg0: $PrintStream$Type, arg1: $Level$Type): $PrettyPrinter
public "trace"(arg0: $PrintStream$Type, arg1: string): $PrettyPrinter
public static "dumpStack"(): void
public "print"(): $PrettyPrinter
public "print"(arg0: $PrintStream$Type): $PrettyPrinter
public static "print"(arg0: $Throwable$Type): void
public "table"(): $PrettyPrinter
public "table"(...arg0: (string)[]): $PrettyPrinter
public "table"(...arg0: (any)[]): $PrettyPrinter
public "th"(): $PrettyPrinter
public "tr"(...arg0: (any)[]): $PrettyPrinter
public "hr"(): $PrettyPrinter
public "hr"(arg0: character): $PrettyPrinter
public "centre"(): $PrettyPrinter
public "kv"(arg0: string, arg1: string, ...arg2: (any)[]): $PrettyPrinter
public "kv"(arg0: string, arg1: any): $PrettyPrinter
public "kvWidth"(arg0: integer): $PrettyPrinter
public "addWrapped"(arg0: string, ...arg1: (any)[]): $PrettyPrinter
public "addWrapped"(arg0: integer, arg1: string, ...arg2: (any)[]): $PrettyPrinter
public "wrapTo"(arg0: integer): $PrettyPrinter
public "wrapTo"(): integer
public "spacing"(arg0: integer): $PrettyPrinter
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PrettyPrinter$Type = ($PrettyPrinter);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PrettyPrinter_ = $PrettyPrinter$Type;
}}
declare module "packages/org/spongepowered/asm/mixin/throwables/$MixinException" {
import {$Throwable, $Throwable$Type} from "packages/java/lang/$Throwable"
import {$RuntimeException, $RuntimeException$Type} from "packages/java/lang/$RuntimeException"
import {$IActivityContext, $IActivityContext$Type} from "packages/org/spongepowered/asm/mixin/extensibility/$IActivityContext"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $MixinException extends $RuntimeException {

constructor(arg0: string, arg1: $Throwable$Type, arg2: $IActivityContext$Type)
constructor(arg0: string, arg1: $Throwable$Type)
constructor(arg0: $Throwable$Type, arg1: $IActivityContext$Type)
constructor(arg0: string)
constructor(arg0: string, arg1: $IActivityContext$Type)
constructor(arg0: $Throwable$Type)

public "getMessage"(): string
public "prepend"(arg0: $IActivityContext$Type): void
get "message"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MixinException$Type = ($MixinException);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MixinException_ = $MixinException$Type;
}}
declare module "packages/org/spongepowered/asm/mixin/transformer/$ClassInfo$Member" {
import {$ClassInfo, $ClassInfo$Type} from "packages/org/spongepowered/asm/mixin/transformer/$ClassInfo"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $ClassInfo$Member {


public "getName"(): string
public "equals"(arg0: any): boolean
public "equals"(arg0: string, arg1: string): boolean
public "toString"(): string
public "hashCode"(): integer
public "isStatic"(): boolean
public "isSynthetic"(): boolean
public "isFinal"(): boolean
public "isAbstract"(): boolean
public "isPrivate"(): boolean
public "renameTo"(arg0: string): string
public "getOwner"(): $ClassInfo
public "getAccess"(): integer
public "getDesc"(): string
public "getOriginalName"(): string
public "matchesFlags"(arg0: integer): boolean
public "isInjected"(): boolean
public "isDecoratedFinal"(): boolean
public "isRemapped"(): boolean
public "getOriginalDesc"(): string
public "remapTo"(arg0: string): string
public "setUnique"(arg0: boolean): void
public "isUnique"(): boolean
public "isDecoratedMutable"(): boolean
public "isRenamed"(): boolean
public "getImplementor"(): $ClassInfo
get "name"(): string
get "static"(): boolean
get "synthetic"(): boolean
get "final"(): boolean
get "abstract"(): boolean
get "private"(): boolean
get "owner"(): $ClassInfo
get "access"(): integer
get "desc"(): string
get "originalName"(): string
get "injected"(): boolean
get "decoratedFinal"(): boolean
get "remapped"(): boolean
get "originalDesc"(): string
set "unique"(value: boolean)
get "unique"(): boolean
get "decoratedMutable"(): boolean
get "renamed"(): boolean
get "implementor"(): $ClassInfo
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClassInfo$Member$Type = ($ClassInfo$Member);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClassInfo$Member_ = $ClassInfo$Member$Type;
}}
declare module "packages/org/spongepowered/asm/mixin/injection/$InjectionPoint" {
import {$MethodNode, $MethodNode$Type} from "packages/org/objectweb/asm/tree/$MethodNode"
import {$IMixinContext, $IMixinContext$Type} from "packages/org/spongepowered/asm/mixin/refmap/$IMixinContext"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$InjectionPoint$RestrictTargetLevel, $InjectionPoint$RestrictTargetLevel$Type} from "packages/org/spongepowered/asm/mixin/injection/$InjectionPoint$RestrictTargetLevel"
import {$At, $At$Type} from "packages/org/spongepowered/asm/mixin/injection/$At"
import {$InsnList, $InsnList$Type} from "packages/org/objectweb/asm/tree/$InsnList"
import {$IMessageSink, $IMessageSink$Type} from "packages/org/spongepowered/asm/util/$IMessageSink"
import {$AbstractInsnNode, $AbstractInsnNode$Type} from "packages/org/objectweb/asm/tree/$AbstractInsnNode"
import {$AnnotationNode, $AnnotationNode$Type} from "packages/org/objectweb/asm/tree/$AnnotationNode"
import {$IInjectionPointContext, $IInjectionPointContext$Type} from "packages/org/spongepowered/asm/mixin/injection/$IInjectionPointContext"
import {$List, $List$Type} from "packages/java/util/$List"
import {$InjectionPoint$Selector, $InjectionPoint$Selector$Type} from "packages/org/spongepowered/asm/mixin/injection/$InjectionPoint$Selector"
import {$At$Shift, $At$Shift$Type} from "packages/org/spongepowered/asm/mixin/injection/$At$Shift"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $InjectionPoint {
static readonly "DEFAULT_ALLOWED_SHIFT_BY": integer
static readonly "MAX_ALLOWED_SHIFT_BY": integer

constructor(arg0: string, arg1: $InjectionPoint$Selector$Type, arg2: string, arg3: $IMessageSink$Type)
constructor(arg0: string, arg1: $InjectionPoint$Selector$Type, arg2: string)

public "toString"(): string
/**
 * 
 * @deprecated
 */
public static "register"(arg0: $Class$Type<(any)>): void
public static "register"(arg0: $Class$Type<(any)>, arg1: string): void
public "find"(arg0: string, arg1: $InsnList$Type, arg2: $Collection$Type<($AbstractInsnNode$Type)>): boolean
public "getId"(): string
public static "shift"(arg0: $InjectionPoint$Type, arg1: integer): $InjectionPoint
public static "before"(arg0: $InjectionPoint$Type): $InjectionPoint
public static "after"(arg0: $InjectionPoint$Type): $InjectionPoint
public static "parse"(arg0: $IInjectionPointContext$Type, arg1: $AnnotationNode$Type): $InjectionPoint
public static "parse"(arg0: $IMixinContext$Type, arg1: $MethodNode$Type, arg2: $AnnotationNode$Type, arg3: $AnnotationNode$Type): $InjectionPoint
public static "parse"(arg0: $IMixinContext$Type, arg1: $MethodNode$Type, arg2: $AnnotationNode$Type, arg3: $At$Type): $InjectionPoint
public static "parse"(arg0: $IMixinContext$Type, arg1: $MethodNode$Type, arg2: $AnnotationNode$Type, arg3: string, arg4: $At$Shift$Type, arg5: integer, arg6: $List$Type<(string)>, arg7: string, arg8: string, arg9: integer, arg10: integer, arg11: string): $InjectionPoint
public static "parse"(arg0: $IInjectionPointContext$Type, arg1: string, arg2: $At$Shift$Type, arg3: integer, arg4: $List$Type<(string)>, arg5: string, arg6: string, arg7: integer, arg8: integer, arg9: string): $InjectionPoint
public static "parse"(arg0: $IMixinContext$Type, arg1: $MethodNode$Type, arg2: $AnnotationNode$Type, arg3: $List$Type<($AnnotationNode$Type)>): $List<($InjectionPoint)>
public static "parse"(arg0: $IInjectionPointContext$Type, arg1: $List$Type<($AnnotationNode$Type)>): $List<($InjectionPoint)>
public static "parse"(arg0: $IInjectionPointContext$Type, arg1: $At$Type): $InjectionPoint
public static "or"(...arg0: ($InjectionPoint$Type)[]): $InjectionPoint
public static "and"(...arg0: ($InjectionPoint$Type)[]): $InjectionPoint
public "getTargetRestriction"(arg0: $IInjectionPointContext$Type): $InjectionPoint$RestrictTargetLevel
public "getSelector"(): $InjectionPoint$Selector
public "getSlice"(): string
public "checkPriority"(arg0: integer, arg1: integer): boolean
get "id"(): string
get "selector"(): $InjectionPoint$Selector
get "slice"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InjectionPoint$Type = ($InjectionPoint);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InjectionPoint_ = $InjectionPoint$Type;
}}

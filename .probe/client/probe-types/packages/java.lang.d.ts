declare module "packages/java/lang/reflect/$GenericArrayType" {
import {$Type, $Type$Type} from "packages/java/lang/reflect/$Type"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export interface $GenericArrayType extends $Type {

 "getGenericComponentType"(): $Type
 "getTypeName"(): string

(): $Type
}

export namespace $GenericArrayType {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GenericArrayType$Type = ($GenericArrayType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GenericArrayType_ = $GenericArrayType$Type;
}}
declare module "packages/java/lang/management/$PlatformManagedObject" {
import {$ObjectName, $ObjectName$Type} from "packages/javax/management/$ObjectName"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export interface $PlatformManagedObject {

 "getObjectName"(): $ObjectName

(): $ObjectName
}

export namespace $PlatformManagedObject {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlatformManagedObject$Type = ($PlatformManagedObject);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlatformManagedObject_ = $PlatformManagedObject$Type;
}}
declare module "packages/java/lang/ref/$WeakReference" {
import {$Reference, $Reference$Type} from "packages/java/lang/ref/$Reference"
import {$ReferenceQueue, $ReferenceQueue$Type} from "packages/java/lang/ref/$ReferenceQueue"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $WeakReference<T> extends $Reference<(T)> {

constructor(arg0: T)
constructor(arg0: T, arg1: $ReferenceQueue$Type<(any)>)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WeakReference$Type<T> = ($WeakReference<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WeakReference_<T> = $WeakReference$Type<(T)>;
}}
declare module "packages/java/lang/$Module" {
import {$ModuleDescriptor, $ModuleDescriptor$Type} from "packages/java/lang/module/$ModuleDescriptor"
import {$Annotation, $Annotation$Type} from "packages/java/lang/annotation/$Annotation"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$ClassLoader, $ClassLoader$Type} from "packages/java/lang/$ClassLoader"
import {$InputStream, $InputStream$Type} from "packages/java/io/$InputStream"
import {$AnnotatedElement, $AnnotatedElement$Type} from "packages/java/lang/reflect/$AnnotatedElement"
import {$ModuleLayer, $ModuleLayer$Type} from "packages/java/lang/$ModuleLayer"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $Module implements $AnnotatedElement {


public "getName"(): string
public "toString"(): string
public "addReads"(arg0: $Module$Type): $Module
public "addExports"(arg0: string, arg1: $Module$Type): $Module
public "addOpens"(arg0: string, arg1: $Module$Type): $Module
public "addUses"(arg0: $Class$Type<(any)>): $Module
public "getClassLoader"(): $ClassLoader
public "getDescriptor"(): $ModuleDescriptor
public "isNamed"(): boolean
public "getResourceAsStream"(arg0: string): $InputStream
public "isOpen"(arg0: string): boolean
public "isOpen"(arg0: string, arg1: $Module$Type): boolean
public "getAnnotation"<T extends $Annotation>(arg0: $Class$Type<(T)>): T
public "getAnnotations"(): ($Annotation)[]
public "getDeclaredAnnotations"(): ($Annotation)[]
public "getPackages"(): $Set<(string)>
public "canRead"(arg0: $Module$Type): boolean
public "canUse"(arg0: $Class$Type<(any)>): boolean
public "getLayer"(): $ModuleLayer
public "isExported"(arg0: string, arg1: $Module$Type): boolean
public "isExported"(arg0: string): boolean
public "isAnnotationPresent"(arg0: $Class$Type<(any)>): boolean
public "getAnnotationsByType"<T extends $Annotation>(arg0: $Class$Type<(T)>): (T)[]
public "getDeclaredAnnotation"<T extends $Annotation>(arg0: $Class$Type<(T)>): T
public "getDeclaredAnnotationsByType"<T extends $Annotation>(arg0: $Class$Type<(T)>): (T)[]
get "name"(): string
get "classLoader"(): $ClassLoader
get "descriptor"(): $ModuleDescriptor
get "named"(): boolean
get "annotations"(): ($Annotation)[]
get "declaredAnnotations"(): ($Annotation)[]
get "packages"(): $Set<(string)>
get "layer"(): $ModuleLayer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Module$Type = ($Module);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Module_ = $Module$Type;
}}
declare module "packages/java/lang/ref/$ReferenceQueue" {
import {$Reference, $Reference$Type} from "packages/java/lang/ref/$Reference"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $ReferenceQueue<T> {

constructor()

public "remove"(arg0: long): $Reference<(any)>
public "remove"(): $Reference<(any)>
public "poll"(): $Reference<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ReferenceQueue$Type<T> = ($ReferenceQueue<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ReferenceQueue_<T> = $ReferenceQueue$Type<(T)>;
}}
declare module "packages/java/lang/module/$ModuleDescriptor$Version" {
import {$Comparable, $Comparable$Type} from "packages/java/lang/$Comparable"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $ModuleDescriptor$Version implements $Comparable<($ModuleDescriptor$Version)> {


public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "compareTo"(arg0: $ModuleDescriptor$Version$Type): integer
public static "parse"(arg0: string): $ModuleDescriptor$Version
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModuleDescriptor$Version$Type = ($ModuleDescriptor$Version);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModuleDescriptor$Version_ = $ModuleDescriptor$Version$Type;
}}
declare module "packages/java/lang/$AutoCloseable" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $AutoCloseable {

 "close"(): void

(): void
}

export namespace $AutoCloseable {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AutoCloseable$Type = ($AutoCloseable);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AutoCloseable_ = $AutoCloseable$Type;
}}
declare module "packages/java/lang/module/$ModuleDescriptor" {
import {$ModuleDescriptor$Provides, $ModuleDescriptor$Provides$Type} from "packages/java/lang/module/$ModuleDescriptor$Provides"
import {$Comparable, $Comparable$Type} from "packages/java/lang/$Comparable"
import {$ModuleDescriptor$Builder, $ModuleDescriptor$Builder$Type} from "packages/java/lang/module/$ModuleDescriptor$Builder"
import {$ModuleDescriptor$Opens, $ModuleDescriptor$Opens$Type} from "packages/java/lang/module/$ModuleDescriptor$Opens"
import {$ByteBuffer, $ByteBuffer$Type} from "packages/java/nio/$ByteBuffer"
import {$ModuleDescriptor$Version, $ModuleDescriptor$Version$Type} from "packages/java/lang/module/$ModuleDescriptor$Version"
import {$ModuleDescriptor$Requires, $ModuleDescriptor$Requires$Type} from "packages/java/lang/module/$ModuleDescriptor$Requires"
import {$ModuleDescriptor$Exports, $ModuleDescriptor$Exports$Type} from "packages/java/lang/module/$ModuleDescriptor$Exports"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$InputStream, $InputStream$Type} from "packages/java/io/$InputStream"
import {$ModuleDescriptor$Modifier, $ModuleDescriptor$Modifier$Type} from "packages/java/lang/module/$ModuleDescriptor$Modifier"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $ModuleDescriptor implements $Comparable<($ModuleDescriptor)> {


public "modifiers"(): $Set<($ModuleDescriptor$Modifier)>
public "name"(): string
public "equals"(arg0: any): boolean
public "toString"(): string
public "version"(): $Optional<($ModuleDescriptor$Version)>
public "hashCode"(): integer
public "compareTo"(arg0: $ModuleDescriptor$Type): integer
public "packages"(): $Set<(string)>
public "isOpen"(): boolean
public static "read"(arg0: $InputStream$Type, arg1: $Supplier$Type<($Set$Type<(string)>)>): $ModuleDescriptor
public static "read"(arg0: $InputStream$Type): $ModuleDescriptor
public static "read"(arg0: $ByteBuffer$Type, arg1: $Supplier$Type<($Set$Type<(string)>)>): $ModuleDescriptor
public static "read"(arg0: $ByteBuffer$Type): $ModuleDescriptor
public "exports"(): $Set<($ModuleDescriptor$Exports)>
public "opens"(): $Set<($ModuleDescriptor$Opens)>
public "isAutomatic"(): boolean
public "uses"(): $Set<(string)>
public "provides"(): $Set<($ModuleDescriptor$Provides)>
public "requires"(): $Set<($ModuleDescriptor$Requires)>
public "mainClass"(): $Optional<(string)>
public "toNameAndVersion"(): string
public "rawVersion"(): $Optional<(string)>
public static "newModule"(arg0: string, arg1: $Set$Type<($ModuleDescriptor$Modifier$Type)>): $ModuleDescriptor$Builder
public static "newModule"(arg0: string): $ModuleDescriptor$Builder
public static "newOpenModule"(arg0: string): $ModuleDescriptor$Builder
public static "newAutomaticModule"(arg0: string): $ModuleDescriptor$Builder
get "open"(): boolean
get "automatic"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModuleDescriptor$Type = ($ModuleDescriptor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModuleDescriptor_ = $ModuleDescriptor$Type;
}}
declare module "packages/java/lang/reflect/$Member" {
import {$Class, $Class$Type} from "packages/java/lang/$Class"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export interface $Member {

 "getName"(): string
 "getModifiers"(): integer
 "isSynthetic"(): boolean
 "getDeclaringClass"(): $Class<(any)>
}

export namespace $Member {
const PUBLIC: integer
const DECLARED: integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Member$Type = ($Member);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Member_ = $Member$Type;
}}
declare module "packages/java/lang/$Error" {
import {$Throwable, $Throwable$Type} from "packages/java/lang/$Throwable"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $Error extends $Throwable {

constructor(arg0: $Throwable$Type)
constructor(arg0: string, arg1: $Throwable$Type)
constructor(arg0: string)
constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Error$Type = ($Error);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Error_ = $Error$Type;
}}
declare module "packages/java/lang/invoke/$VarHandle$VarHandleDesc" {
import {$DynamicConstantDesc, $DynamicConstantDesc$Type} from "packages/java/lang/constant/$DynamicConstantDesc"
import {$VarHandle, $VarHandle$Type} from "packages/java/lang/invoke/$VarHandle"
import {$ClassDesc, $ClassDesc$Type} from "packages/java/lang/constant/$ClassDesc"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $VarHandle$VarHandleDesc extends $DynamicConstantDesc<($VarHandle)> {


public "toString"(): string
public static "ofField"(arg0: $ClassDesc$Type, arg1: string, arg2: $ClassDesc$Type): $VarHandle$VarHandleDesc
public "varType"(): $ClassDesc
public static "ofStaticField"(arg0: $ClassDesc$Type, arg1: string, arg2: $ClassDesc$Type): $VarHandle$VarHandleDesc
public static "ofArray"(arg0: $ClassDesc$Type): $VarHandle$VarHandleDesc
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $VarHandle$VarHandleDesc$Type = ($VarHandle$VarHandleDesc);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $VarHandle$VarHandleDesc_ = $VarHandle$VarHandleDesc$Type;
}}
declare module "packages/java/lang/constant/$DirectMethodHandleDesc" {
import {$MethodHandles$Lookup, $MethodHandles$Lookup$Type} from "packages/java/lang/invoke/$MethodHandles$Lookup"
import {$DirectMethodHandleDesc$Kind, $DirectMethodHandleDesc$Kind$Type} from "packages/java/lang/constant/$DirectMethodHandleDesc$Kind"
import {$ClassDesc, $ClassDesc$Type} from "packages/java/lang/constant/$ClassDesc"
import {$MethodTypeDesc, $MethodTypeDesc$Type} from "packages/java/lang/constant/$MethodTypeDesc"
import {$MethodHandleDesc, $MethodHandleDesc$Type} from "packages/java/lang/constant/$MethodHandleDesc"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export interface $DirectMethodHandleDesc extends $MethodHandleDesc {

 "methodName"(): string
 "refKind"(): integer
 "kind"(): $DirectMethodHandleDesc$Kind
 "owner"(): $ClassDesc
 "lookupDescriptor"(): string
 "isOwnerInterface"(): boolean
 "equals"(arg0: any): boolean
 "asType"(arg0: $MethodTypeDesc$Type): $MethodHandleDesc
 "invocationType"(): $MethodTypeDesc
 "resolveConstantDesc"(arg0: $MethodHandles$Lookup$Type): any
}

export namespace $DirectMethodHandleDesc {
function of(arg0: $DirectMethodHandleDesc$Kind$Type, arg1: $ClassDesc$Type, arg2: string, arg3: string): $DirectMethodHandleDesc
function ofField(arg0: $DirectMethodHandleDesc$Kind$Type, arg1: $ClassDesc$Type, arg2: string, arg3: $ClassDesc$Type): $DirectMethodHandleDesc
function ofMethod(arg0: $DirectMethodHandleDesc$Kind$Type, arg1: $ClassDesc$Type, arg2: string, arg3: $MethodTypeDesc$Type): $DirectMethodHandleDesc
function ofConstructor(arg0: $ClassDesc$Type, ...arg1: ($ClassDesc$Type)[]): $DirectMethodHandleDesc
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DirectMethodHandleDesc$Type = ($DirectMethodHandleDesc);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DirectMethodHandleDesc_ = $DirectMethodHandleDesc$Type;
}}
declare module "packages/java/lang/$Cloneable" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export interface $Cloneable {

}

export namespace $Cloneable {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Cloneable$Type = ($Cloneable);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Cloneable_ = $Cloneable$Type;
}}
declare module "packages/java/lang/$Comparable" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $Comparable<T> {

 "compareTo"(arg0: T): integer

(arg0: T): integer
}

export namespace $Comparable {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Comparable$Type<T> = ($Comparable<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Comparable_<T> = $Comparable$Type<(T)>;
}}
declare module "packages/java/lang/reflect/$TypeVariable" {
import {$Annotation, $Annotation$Type} from "packages/java/lang/annotation/$Annotation"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$Type, $Type$Type} from "packages/java/lang/reflect/$Type"
import {$AnnotatedType, $AnnotatedType$Type} from "packages/java/lang/reflect/$AnnotatedType"
import {$AnnotatedElement, $AnnotatedElement$Type} from "packages/java/lang/reflect/$AnnotatedElement"
import {$GenericDeclaration, $GenericDeclaration$Type} from "packages/java/lang/reflect/$GenericDeclaration"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export interface $TypeVariable<D extends $GenericDeclaration> extends $Type, $AnnotatedElement {

 "getName"(): string
 "getBounds"(): ($Type)[]
 "getGenericDeclaration"(): D
 "getAnnotatedBounds"(): ($AnnotatedType)[]
 "getTypeName"(): string
 "isAnnotationPresent"(arg0: $Class$Type<(any)>): boolean
 "getAnnotation"<T extends $Annotation>(arg0: $Class$Type<(T)>): T
 "getAnnotationsByType"<T extends $Annotation>(arg0: $Class$Type<(T)>): (T)[]
 "getAnnotations"(): ($Annotation)[]
 "getDeclaredAnnotation"<T extends $Annotation>(arg0: $Class$Type<(T)>): T
 "getDeclaredAnnotationsByType"<T extends $Annotation>(arg0: $Class$Type<(T)>): (T)[]
 "getDeclaredAnnotations"(): ($Annotation)[]
}

export namespace $TypeVariable {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TypeVariable$Type<D> = ($TypeVariable<(D)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TypeVariable_<D> = $TypeVariable$Type<(D)>;
}}
declare module "packages/java/lang/$Runtime$Version" {
import {$Comparable, $Comparable$Type} from "packages/java/lang/$Comparable"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $Runtime$Version implements $Comparable<($Runtime$Version)> {


public "equals"(arg0: any): boolean
public "toString"(): string
public "version"(): $List<(integer)>
public "hashCode"(): integer
public "compareTo"(arg0: $Runtime$Version$Type): integer
public "update"(): integer
/**
 * 
 * @deprecated
 */
public "security"(): integer
public "pre"(): $Optional<(string)>
public "build"(): $Optional<(integer)>
public "optional"(): $Optional<(string)>
/**
 * 
 * @deprecated
 */
public "major"(): integer
/**
 * 
 * @deprecated
 */
public "minor"(): integer
public static "parse"(arg0: string): $Runtime$Version
public "feature"(): integer
public "interim"(): integer
public "equalsIgnoreOptional"(arg0: any): boolean
public "patch"(): integer
public "compareToIgnoreOptional"(arg0: $Runtime$Version$Type): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Runtime$Version$Type = ($Runtime$Version);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Runtime$Version_ = $Runtime$Version$Type;
}}
declare module "packages/java/lang/reflect/$AnnotatedParameterizedType" {
import {$Annotation, $Annotation$Type} from "packages/java/lang/annotation/$Annotation"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$Type, $Type$Type} from "packages/java/lang/reflect/$Type"
import {$AnnotatedType, $AnnotatedType$Type} from "packages/java/lang/reflect/$AnnotatedType"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export interface $AnnotatedParameterizedType extends $AnnotatedType {

 "getAnnotatedOwnerType"(): $AnnotatedType
 "getAnnotatedActualTypeArguments"(): ($AnnotatedType)[]
 "getAnnotation"<T extends $Annotation>(arg0: $Class$Type<(T)>): T
 "getAnnotations"(): ($Annotation)[]
 "getDeclaredAnnotations"(): ($Annotation)[]
 "getType"(): $Type
 "isAnnotationPresent"(arg0: $Class$Type<(any)>): boolean
 "getAnnotationsByType"<T extends $Annotation>(arg0: $Class$Type<(T)>): (T)[]
 "getDeclaredAnnotation"<T extends $Annotation>(arg0: $Class$Type<(T)>): T
 "getDeclaredAnnotationsByType"<T extends $Annotation>(arg0: $Class$Type<(T)>): (T)[]
}

export namespace $AnnotatedParameterizedType {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AnnotatedParameterizedType$Type = ($AnnotatedParameterizedType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AnnotatedParameterizedType_ = $AnnotatedParameterizedType$Type;
}}
declare module "packages/java/lang/reflect/$WildcardType" {
import {$Type, $Type$Type} from "packages/java/lang/reflect/$Type"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export interface $WildcardType extends $Type {

 "getUpperBounds"(): ($Type)[]
 "getLowerBounds"(): ($Type)[]
 "getTypeName"(): string
}

export namespace $WildcardType {
const probejs$$marker: never
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
declare module "packages/java/lang/invoke/$MethodType" {
import {$Serializable, $Serializable$Type} from "packages/java/io/$Serializable"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$MethodTypeDesc, $MethodTypeDesc$Type} from "packages/java/lang/constant/$MethodTypeDesc"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ClassLoader, $ClassLoader$Type} from "packages/java/lang/$ClassLoader"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$Constable, $Constable$Type} from "packages/java/lang/constant/$Constable"
import {$TypeDescriptor$OfMethod, $TypeDescriptor$OfMethod$Type} from "packages/java/lang/invoke/$TypeDescriptor$OfMethod"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $MethodType implements $Constable, $TypeDescriptor$OfMethod<($Class<(any)>), ($MethodType)>, $Serializable {


public "returnType"(): $Class<(any)>
public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "wrap"(): $MethodType
public "describeConstable"(): $Optional<($MethodTypeDesc)>
public "descriptorString"(): string
public "parameterType"(arg0: integer): $Class<(any)>
public "insertParameterTypes"(arg0: integer, arg1: $List$Type<($Class$Type<(any)>)>): $MethodType
public "insertParameterTypes"(arg0: integer, ...arg1: ($Class$Type<(any)>)[]): $MethodType
public "changeReturnType"(arg0: $Class$Type<(any)>): $MethodType
public static "methodType"(arg0: $Class$Type<(any)>, arg1: ($Class$Type<(any)>)[]): $MethodType
public static "methodType"(arg0: $Class$Type<(any)>, arg1: $MethodType$Type): $MethodType
public static "methodType"(arg0: $Class$Type<(any)>, arg1: $Class$Type<(any)>, ...arg2: ($Class$Type<(any)>)[]): $MethodType
public static "methodType"(arg0: $Class$Type<(any)>, arg1: $List$Type<($Class$Type<(any)>)>): $MethodType
public static "methodType"(arg0: $Class$Type<(any)>, arg1: $Class$Type<(any)>): $MethodType
public static "methodType"(arg0: $Class$Type<(any)>): $MethodType
public "appendParameterTypes"(arg0: $List$Type<($Class$Type<(any)>)>): $MethodType
public "appendParameterTypes"(...arg0: ($Class$Type<(any)>)[]): $MethodType
public "parameterCount"(): integer
public static "genericMethodType"(arg0: integer, arg1: boolean): $MethodType
public static "genericMethodType"(arg0: integer): $MethodType
public "lastParameterType"(): $Class<(any)>
public "parameterList"(): $List<($Class<(any)>)>
public "erase"(): $MethodType
public "toMethodDescriptorString"(): string
public "parameterArray"(): ($Class<(any)>)[]
public "hasPrimitives"(): boolean
public "unwrap"(): $MethodType
public "changeParameterType"(arg0: integer, arg1: $Class$Type<(any)>): $MethodType
public "hasWrappers"(): boolean
public "generic"(): $MethodType
public static "fromMethodDescriptorString"(arg0: string, arg1: $ClassLoader$Type): $MethodType
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MethodType$Type = ($MethodType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MethodType_ = $MethodType$Type;
}}
declare module "packages/java/lang/module/$ModuleDescriptor$Provides" {
import {$Comparable, $Comparable$Type} from "packages/java/lang/$Comparable"
import {$List, $List$Type} from "packages/java/util/$List"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $ModuleDescriptor$Provides implements $Comparable<($ModuleDescriptor$Provides)> {


public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "compareTo"(arg0: $ModuleDescriptor$Provides$Type): integer
public "service"(): string
public "providers"(): $List<(string)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModuleDescriptor$Provides$Type = ($ModuleDescriptor$Provides);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModuleDescriptor$Provides_ = $ModuleDescriptor$Provides$Type;
}}
declare module "packages/java/lang/reflect/$AnnotatedWildcardType" {
import {$Annotation, $Annotation$Type} from "packages/java/lang/annotation/$Annotation"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$Type, $Type$Type} from "packages/java/lang/reflect/$Type"
import {$AnnotatedType, $AnnotatedType$Type} from "packages/java/lang/reflect/$AnnotatedType"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export interface $AnnotatedWildcardType extends $AnnotatedType {

 "getAnnotatedOwnerType"(): $AnnotatedType
 "getAnnotatedLowerBounds"(): ($AnnotatedType)[]
 "getAnnotatedUpperBounds"(): ($AnnotatedType)[]
 "getAnnotation"<T extends $Annotation>(arg0: $Class$Type<(T)>): T
 "getAnnotations"(): ($Annotation)[]
 "getDeclaredAnnotations"(): ($Annotation)[]
 "getType"(): $Type
 "isAnnotationPresent"(arg0: $Class$Type<(any)>): boolean
 "getAnnotationsByType"<T extends $Annotation>(arg0: $Class$Type<(T)>): (T)[]
 "getDeclaredAnnotation"<T extends $Annotation>(arg0: $Class$Type<(T)>): T
 "getDeclaredAnnotationsByType"<T extends $Annotation>(arg0: $Class$Type<(T)>): (T)[]
}

export namespace $AnnotatedWildcardType {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AnnotatedWildcardType$Type = ($AnnotatedWildcardType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AnnotatedWildcardType_ = $AnnotatedWildcardType$Type;
}}
declare module "packages/java/lang/module/$ModuleReader" {
import {$Closeable, $Closeable$Type} from "packages/java/io/$Closeable"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$InputStream, $InputStream$Type} from "packages/java/io/$InputStream"
import {$URI, $URI$Type} from "packages/java/net/$URI"
import {$ByteBuffer, $ByteBuffer$Type} from "packages/java/nio/$ByteBuffer"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export interface $ModuleReader extends $Closeable {

 "list"(): $Stream<(string)>
 "find"(arg0: string): $Optional<($URI)>
 "read"(arg0: string): $Optional<($ByteBuffer)>
 "close"(): void
 "open"(arg0: string): $Optional<($InputStream)>
 "release"(arg0: $ByteBuffer$Type): void
}

export namespace $ModuleReader {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModuleReader$Type = ($ModuleReader);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModuleReader_ = $ModuleReader$Type;
}}
declare module "packages/java/lang/module/$ModuleDescriptor$Requires$Modifier" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $ModuleDescriptor$Requires$Modifier extends $Enum<($ModuleDescriptor$Requires$Modifier)> {
static readonly "TRANSITIVE": $ModuleDescriptor$Requires$Modifier
static readonly "STATIC": $ModuleDescriptor$Requires$Modifier
static readonly "SYNTHETIC": $ModuleDescriptor$Requires$Modifier
static readonly "MANDATED": $ModuleDescriptor$Requires$Modifier


public static "values"(): ($ModuleDescriptor$Requires$Modifier)[]
public static "valueOf"(arg0: string): $ModuleDescriptor$Requires$Modifier
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModuleDescriptor$Requires$Modifier$Type = (("synthetic") | ("static") | ("mandated") | ("transitive")) | ($ModuleDescriptor$Requires$Modifier);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModuleDescriptor$Requires$Modifier_ = $ModuleDescriptor$Requires$Modifier$Type;
}}
declare module "packages/java/lang/module/$ModuleDescriptor$Exports$Modifier" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $ModuleDescriptor$Exports$Modifier extends $Enum<($ModuleDescriptor$Exports$Modifier)> {
static readonly "SYNTHETIC": $ModuleDescriptor$Exports$Modifier
static readonly "MANDATED": $ModuleDescriptor$Exports$Modifier


public static "values"(): ($ModuleDescriptor$Exports$Modifier)[]
public static "valueOf"(arg0: string): $ModuleDescriptor$Exports$Modifier
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModuleDescriptor$Exports$Modifier$Type = (("synthetic") | ("mandated")) | ($ModuleDescriptor$Exports$Modifier);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModuleDescriptor$Exports$Modifier_ = $ModuleDescriptor$Exports$Modifier$Type;
}}
declare module "packages/java/lang/module/$ModuleFinder" {
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$ModuleReference, $ModuleReference$Type} from "packages/java/lang/module/$ModuleReference"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export interface $ModuleFinder {

 "find"(arg0: string): $Optional<($ModuleReference)>
 "findAll"(): $Set<($ModuleReference)>
}

export namespace $ModuleFinder {
function of(...arg0: ($Path$Type)[]): $ModuleFinder
function ofSystem(): $ModuleFinder
function compose(...arg0: ($ModuleFinder$Type)[]): $ModuleFinder
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModuleFinder$Type = ($ModuleFinder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModuleFinder_ = $ModuleFinder$Type;
}}
declare module "packages/java/lang/reflect/$AnnotatedTypeVariable" {
import {$Annotation, $Annotation$Type} from "packages/java/lang/annotation/$Annotation"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$Type, $Type$Type} from "packages/java/lang/reflect/$Type"
import {$AnnotatedType, $AnnotatedType$Type} from "packages/java/lang/reflect/$AnnotatedType"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export interface $AnnotatedTypeVariable extends $AnnotatedType {

 "getAnnotatedOwnerType"(): $AnnotatedType
 "getAnnotatedBounds"(): ($AnnotatedType)[]
 "getAnnotation"<T extends $Annotation>(arg0: $Class$Type<(T)>): T
 "getAnnotations"(): ($Annotation)[]
 "getDeclaredAnnotations"(): ($Annotation)[]
 "getType"(): $Type
 "isAnnotationPresent"(arg0: $Class$Type<(any)>): boolean
 "getAnnotationsByType"<T extends $Annotation>(arg0: $Class$Type<(T)>): (T)[]
 "getDeclaredAnnotation"<T extends $Annotation>(arg0: $Class$Type<(T)>): T
 "getDeclaredAnnotationsByType"<T extends $Annotation>(arg0: $Class$Type<(T)>): (T)[]
}

export namespace $AnnotatedTypeVariable {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AnnotatedTypeVariable$Type = ($AnnotatedTypeVariable);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AnnotatedTypeVariable_ = $AnnotatedTypeVariable$Type;
}}
declare module "packages/java/lang/$AbstractStringBuilder" {
import {$IntStream, $IntStream$Type} from "packages/java/util/stream/$IntStream"
import {$StringBuffer, $StringBuffer$Type} from "packages/java/lang/$StringBuffer"
import {$Appendable, $Appendable$Type} from "packages/java/lang/$Appendable"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $AbstractStringBuilder implements $Appendable, charseq {


public "length"(): integer
public "toString"(): string
public "append"(arg0: charseq): $AbstractStringBuilder
public "append"(arg0: charseq, arg1: integer, arg2: integer): $AbstractStringBuilder
public "append"(arg0: (character)[]): $AbstractStringBuilder
public "append"(arg0: any): $AbstractStringBuilder
public "append"(arg0: $StringBuffer$Type): $AbstractStringBuilder
public "append"(arg0: string): $AbstractStringBuilder
public "append"(arg0: integer): $AbstractStringBuilder
public "append"(arg0: long): $AbstractStringBuilder
public "append"(arg0: float): $AbstractStringBuilder
public "append"(arg0: double): $AbstractStringBuilder
public "append"(arg0: character): $AbstractStringBuilder
public "append"(arg0: boolean): $AbstractStringBuilder
public "append"(arg0: (character)[], arg1: integer, arg2: integer): $AbstractStringBuilder
public "getChars"(arg0: integer, arg1: integer, arg2: (character)[], arg3: integer): void
public "indexOf"(arg0: string): integer
public "indexOf"(arg0: string, arg1: integer): integer
public "insert"(arg0: integer, arg1: string): $AbstractStringBuilder
public "insert"(arg0: integer, arg1: charseq): $AbstractStringBuilder
public "insert"(arg0: integer, arg1: (character)[]): $AbstractStringBuilder
public "insert"(arg0: integer, arg1: any): $AbstractStringBuilder
public "insert"(arg0: integer, arg1: (character)[], arg2: integer, arg3: integer): $AbstractStringBuilder
public "insert"(arg0: integer, arg1: boolean): $AbstractStringBuilder
public "insert"(arg0: integer, arg1: float): $AbstractStringBuilder
public "insert"(arg0: integer, arg1: double): $AbstractStringBuilder
public "insert"(arg0: integer, arg1: long): $AbstractStringBuilder
public "insert"(arg0: integer, arg1: charseq, arg2: integer, arg3: integer): $AbstractStringBuilder
public "insert"(arg0: integer, arg1: integer): $AbstractStringBuilder
public "insert"(arg0: integer, arg1: character): $AbstractStringBuilder
public "charAt"(arg0: integer): character
public "codePointAt"(arg0: integer): integer
public "codePointBefore"(arg0: integer): integer
public "codePointCount"(arg0: integer, arg1: integer): integer
public "offsetByCodePoints"(arg0: integer, arg1: integer): integer
public "lastIndexOf"(arg0: string): integer
public "lastIndexOf"(arg0: string, arg1: integer): integer
public "substring"(arg0: integer): string
public "substring"(arg0: integer, arg1: integer): string
public "replace"(arg0: integer, arg1: integer, arg2: string): $AbstractStringBuilder
public "codePoints"(): $IntStream
public "subSequence"(arg0: integer, arg1: integer): charseq
public "chars"(): $IntStream
public "delete"(arg0: integer, arg1: integer): $AbstractStringBuilder
public "setLength"(arg0: integer): void
public "capacity"(): integer
public "ensureCapacity"(arg0: integer): void
public "trimToSize"(): void
public "setCharAt"(arg0: integer, arg1: character): void
public "appendCodePoint"(arg0: integer): $AbstractStringBuilder
public "deleteCharAt"(arg0: integer): $AbstractStringBuilder
public "reverse"(): $AbstractStringBuilder
public static "compare"(arg0: charseq, arg1: charseq): integer
public "isEmpty"(): boolean
get "empty"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AbstractStringBuilder$Type = ($AbstractStringBuilder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AbstractStringBuilder_ = $AbstractStringBuilder$Type;
}}
declare module "packages/java/lang/reflect/$Parameter" {
import {$Annotation, $Annotation$Type} from "packages/java/lang/annotation/$Annotation"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$Type, $Type$Type} from "packages/java/lang/reflect/$Type"
import {$AnnotatedType, $AnnotatedType$Type} from "packages/java/lang/reflect/$AnnotatedType"
import {$AnnotatedElement, $AnnotatedElement$Type} from "packages/java/lang/reflect/$AnnotatedElement"
import {$Executable, $Executable$Type} from "packages/java/lang/reflect/$Executable"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $Parameter implements $AnnotatedElement {


public "getName"(): string
public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "getModifiers"(): integer
public "isSynthetic"(): boolean
public "getAnnotation"<T extends $Annotation>(arg0: $Class$Type<(T)>): T
public "getAnnotationsByType"<T extends $Annotation>(arg0: $Class$Type<(T)>): (T)[]
public "getAnnotations"(): ($Annotation)[]
public "getDeclaredAnnotation"<T extends $Annotation>(arg0: $Class$Type<(T)>): T
public "getDeclaredAnnotationsByType"<T extends $Annotation>(arg0: $Class$Type<(T)>): (T)[]
public "getDeclaredAnnotations"(): ($Annotation)[]
public "getType"(): $Class<(any)>
public "getAnnotatedType"(): $AnnotatedType
public "getParameterizedType"(): $Type
public "isVarArgs"(): boolean
public "isNamePresent"(): boolean
public "getDeclaringExecutable"(): $Executable
public "isImplicit"(): boolean
public "isAnnotationPresent"(arg0: $Class$Type<(any)>): boolean
get "name"(): string
get "modifiers"(): integer
get "synthetic"(): boolean
get "annotations"(): ($Annotation)[]
get "declaredAnnotations"(): ($Annotation)[]
get "type"(): $Class<(any)>
get "annotatedType"(): $AnnotatedType
get "parameterizedType"(): $Type
get "varArgs"(): boolean
get "namePresent"(): boolean
get "declaringExecutable"(): $Executable
get "implicit"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Parameter$Type = ($Parameter);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Parameter_ = $Parameter$Type;
}}
declare module "packages/java/lang/$Exception" {
import {$Throwable, $Throwable$Type} from "packages/java/lang/$Throwable"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $Exception extends $Throwable {

constructor(arg0: $Throwable$Type)
constructor(arg0: string, arg1: $Throwable$Type)
constructor(arg0: string)
constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Exception$Type = ($Exception);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Exception_ = $Exception$Type;
}}
declare module "packages/java/lang/$Thread$UncaughtExceptionHandler" {
import {$Throwable, $Throwable$Type} from "packages/java/lang/$Throwable"
import {$Thread, $Thread$Type} from "packages/java/lang/$Thread"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export interface $Thread$UncaughtExceptionHandler {

 "uncaughtException"(arg0: $Thread$Type, arg1: $Throwable$Type): void

(arg0: $Thread$Type, arg1: $Throwable$Type): void
}

export namespace $Thread$UncaughtExceptionHandler {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Thread$UncaughtExceptionHandler$Type = ($Thread$UncaughtExceptionHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Thread$UncaughtExceptionHandler_ = $Thread$UncaughtExceptionHandler$Type;
}}
declare module "packages/java/lang/$ThreadGroup" {
import {$Throwable, $Throwable$Type} from "packages/java/lang/$Throwable"
import {$Thread, $Thread$Type} from "packages/java/lang/$Thread"
import {$Thread$UncaughtExceptionHandler, $Thread$UncaughtExceptionHandler$Type} from "packages/java/lang/$Thread$UncaughtExceptionHandler"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $ThreadGroup implements $Thread$UncaughtExceptionHandler {

constructor(arg0: string)
constructor(arg0: $ThreadGroup$Type, arg1: string)

public "getName"(): string
public "toString"(): string
public "list"(): void
public "getParent"(): $ThreadGroup
/**
 * 
 * @deprecated
 */
public "checkAccess"(): void
/**
 * 
 * @deprecated
 */
public "setDaemon"(arg0: boolean): void
/**
 * 
 * @deprecated
 */
public "isDaemon"(): boolean
/**
 * 
 * @deprecated
 */
public "resume"(): void
public "interrupt"(): void
public "getMaxPriority"(): integer
public "activeCount"(): integer
public "enumerate"(arg0: ($ThreadGroup$Type)[], arg1: boolean): integer
public "enumerate"(arg0: ($ThreadGroup$Type)[]): integer
public "enumerate"(arg0: ($Thread$Type)[]): integer
public "enumerate"(arg0: ($Thread$Type)[], arg1: boolean): integer
public "uncaughtException"(arg0: $Thread$Type, arg1: $Throwable$Type): void
/**
 * 
 * @deprecated
 */
public "stop"(): void
/**
 * 
 * @deprecated
 */
public "suspend"(): void
public "setMaxPriority"(arg0: integer): void
public "activeGroupCount"(): integer
/**
 * 
 * @deprecated
 */
public "destroy"(): void
/**
 * 
 * @deprecated
 */
public "isDestroyed"(): boolean
public "parentOf"(arg0: $ThreadGroup$Type): boolean
/**
 * 
 * @deprecated
 */
public "allowThreadSuspension"(arg0: boolean): boolean
get "name"(): string
get "parent"(): $ThreadGroup
set "daemon"(value: boolean)
get "daemon"(): boolean
get "maxPriority"(): integer
set "maxPriority"(value: integer)
get "destroyed"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ThreadGroup$Type = ($ThreadGroup);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ThreadGroup_ = $ThreadGroup$Type;
}}
declare module "packages/java/lang/reflect/$RecordComponent" {
import {$Annotation, $Annotation$Type} from "packages/java/lang/annotation/$Annotation"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$Type, $Type$Type} from "packages/java/lang/reflect/$Type"
import {$Method, $Method$Type} from "packages/java/lang/reflect/$Method"
import {$AnnotatedType, $AnnotatedType$Type} from "packages/java/lang/reflect/$AnnotatedType"
import {$AnnotatedElement, $AnnotatedElement$Type} from "packages/java/lang/reflect/$AnnotatedElement"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $RecordComponent implements $AnnotatedElement {


public "getName"(): string
public "toString"(): string
public "getAnnotation"<T extends $Annotation>(arg0: $Class$Type<(T)>): T
public "getAnnotations"(): ($Annotation)[]
public "getDeclaredAnnotations"(): ($Annotation)[]
public "getGenericSignature"(): string
public "getGenericType"(): $Type
public "getType"(): $Class<(any)>
public "getAnnotatedType"(): $AnnotatedType
public "getDeclaringRecord"(): $Class<(any)>
public "getAccessor"(): $Method
public "isAnnotationPresent"(arg0: $Class$Type<(any)>): boolean
public "getAnnotationsByType"<T extends $Annotation>(arg0: $Class$Type<(T)>): (T)[]
public "getDeclaredAnnotation"<T extends $Annotation>(arg0: $Class$Type<(T)>): T
public "getDeclaredAnnotationsByType"<T extends $Annotation>(arg0: $Class$Type<(T)>): (T)[]
get "name"(): string
get "annotations"(): ($Annotation)[]
get "declaredAnnotations"(): ($Annotation)[]
get "genericSignature"(): string
get "genericType"(): $Type
get "type"(): $Class<(any)>
get "annotatedType"(): $AnnotatedType
get "declaringRecord"(): $Class<(any)>
get "accessor"(): $Method
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecordComponent$Type = ($RecordComponent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecordComponent_ = $RecordComponent$Type;
}}
declare module "packages/java/lang/$NamedPackage" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $NamedPackage {


}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NamedPackage$Type = ($NamedPackage);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NamedPackage_ = $NamedPackage$Type;
}}
declare module "packages/java/lang/instrument/$ClassFileTransformer" {
import {$Module, $Module$Type} from "packages/java/lang/$Module"
import {$ClassLoader, $ClassLoader$Type} from "packages/java/lang/$ClassLoader"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$ProtectionDomain, $ProtectionDomain$Type} from "packages/java/security/$ProtectionDomain"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export interface $ClassFileTransformer {

 "transform"(arg0: $ClassLoader$Type, arg1: string, arg2: $Class$Type<(any)>, arg3: $ProtectionDomain$Type, arg4: (byte)[]): (byte)[]
 "transform"(arg0: $Module$Type, arg1: $ClassLoader$Type, arg2: string, arg3: $Class$Type<(any)>, arg4: $ProtectionDomain$Type, arg5: (byte)[]): (byte)[]
}

export namespace $ClassFileTransformer {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClassFileTransformer$Type = ($ClassFileTransformer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClassFileTransformer_ = $ClassFileTransformer$Type;
}}
declare module "packages/java/lang/$Math" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $Math {
static readonly "E": double
static readonly "PI": double


public static "abs"(arg0: integer): integer
public static "abs"(arg0: long): long
public static "abs"(arg0: float): float
public static "abs"(arg0: double): double
public static "sin"(arg0: double): double
public static "cos"(arg0: double): double
public static "tan"(arg0: double): double
public static "atan2"(arg0: double, arg1: double): double
public static "sqrt"(arg0: double): double
public static "log"(arg0: double): double
public static "log10"(arg0: double): double
public static "pow"(arg0: double, arg1: double): double
public static "exp"(arg0: double): double
public static "min"(arg0: integer, arg1: integer): integer
public static "min"(arg0: float, arg1: float): float
public static "min"(arg0: long, arg1: long): long
public static "min"(arg0: double, arg1: double): double
public static "max"(arg0: integer, arg1: integer): integer
public static "max"(arg0: float, arg1: float): float
public static "max"(arg0: long, arg1: long): long
public static "max"(arg0: double, arg1: double): double
public static "floor"(arg0: double): double
public static "ceil"(arg0: double): double
public static "rint"(arg0: double): double
public static "addExact"(arg0: integer, arg1: integer): integer
public static "addExact"(arg0: long, arg1: long): long
public static "decrementExact"(arg0: long): long
public static "decrementExact"(arg0: integer): integer
public static "incrementExact"(arg0: integer): integer
public static "incrementExact"(arg0: long): long
public static "multiplyExact"(arg0: integer, arg1: integer): integer
public static "multiplyExact"(arg0: long, arg1: long): long
public static "multiplyExact"(arg0: long, arg1: integer): long
public static "multiplyHigh"(arg0: long, arg1: long): long
public static "negateExact"(arg0: long): long
public static "negateExact"(arg0: integer): integer
public static "subtractExact"(arg0: integer, arg1: integer): integer
public static "subtractExact"(arg0: long, arg1: long): long
public static "fma"(arg0: double, arg1: double, arg2: double): double
public static "fma"(arg0: float, arg1: float, arg2: float): float
public static "copySign"(arg0: float, arg1: float): float
public static "copySign"(arg0: double, arg1: double): double
public static "signum"(arg0: float): float
public static "signum"(arg0: double): double
public static "scalb"(arg0: double, arg1: integer): double
public static "scalb"(arg0: float, arg1: integer): float
public static "getExponent"(arg0: float): integer
public static "getExponent"(arg0: double): integer
public static "floorMod"(arg0: long, arg1: integer): integer
public static "floorMod"(arg0: integer, arg1: integer): integer
public static "floorMod"(arg0: long, arg1: long): long
public static "asin"(arg0: double): double
public static "acos"(arg0: double): double
public static "atan"(arg0: double): double
public static "cbrt"(arg0: double): double
public static "IEEEremainder"(arg0: double, arg1: double): double
public static "floorDiv"(arg0: long, arg1: long): long
public static "floorDiv"(arg0: integer, arg1: integer): integer
public static "floorDiv"(arg0: long, arg1: integer): long
public static "sinh"(arg0: double): double
public static "cosh"(arg0: double): double
public static "tanh"(arg0: double): double
public static "hypot"(arg0: double, arg1: double): double
public static "expm1"(arg0: double): double
public static "log1p"(arg0: double): double
public static "toRadians"(arg0: double): double
public static "toDegrees"(arg0: double): double
public static "round"(arg0: double): long
public static "round"(arg0: float): integer
public static "random"(): double
public static "toIntExact"(arg0: long): integer
public static "multiplyFull"(arg0: integer, arg1: integer): long
public static "absExact"(arg0: integer): integer
public static "absExact"(arg0: long): long
public static "ulp"(arg0: double): double
public static "ulp"(arg0: float): float
public static "nextAfter"(arg0: double, arg1: double): double
public static "nextAfter"(arg0: float, arg1: double): float
public static "nextUp"(arg0: double): double
public static "nextUp"(arg0: float): float
public static "nextDown"(arg0: double): double
public static "nextDown"(arg0: float): float
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Math$Type = ($Math);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Math_ = $Math$Type;
}}
declare module "packages/java/lang/$Runnable" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $Runnable {

 "run"(): void

(): void
}

export namespace $Runnable {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Runnable$Type = ($Runnable);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Runnable_ = $Runnable$Type;
}}
declare module "packages/java/lang/constant/$MethodHandleDesc" {
import {$MethodHandles$Lookup, $MethodHandles$Lookup$Type} from "packages/java/lang/invoke/$MethodHandles$Lookup"
import {$DirectMethodHandleDesc$Kind, $DirectMethodHandleDesc$Kind$Type} from "packages/java/lang/constant/$DirectMethodHandleDesc$Kind"
import {$ClassDesc, $ClassDesc$Type} from "packages/java/lang/constant/$ClassDesc"
import {$MethodTypeDesc, $MethodTypeDesc$Type} from "packages/java/lang/constant/$MethodTypeDesc"
import {$ConstantDesc, $ConstantDesc$Type} from "packages/java/lang/constant/$ConstantDesc"
import {$DirectMethodHandleDesc, $DirectMethodHandleDesc$Type} from "packages/java/lang/constant/$DirectMethodHandleDesc"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export interface $MethodHandleDesc extends $ConstantDesc {

 "equals"(arg0: any): boolean
 "asType"(arg0: $MethodTypeDesc$Type): $MethodHandleDesc
 "invocationType"(): $MethodTypeDesc
 "resolveConstantDesc"(arg0: $MethodHandles$Lookup$Type): any
}

export namespace $MethodHandleDesc {
function of(arg0: $DirectMethodHandleDesc$Kind$Type, arg1: $ClassDesc$Type, arg2: string, arg3: string): $DirectMethodHandleDesc
function ofField(arg0: $DirectMethodHandleDesc$Kind$Type, arg1: $ClassDesc$Type, arg2: string, arg3: $ClassDesc$Type): $DirectMethodHandleDesc
function ofMethod(arg0: $DirectMethodHandleDesc$Kind$Type, arg1: $ClassDesc$Type, arg2: string, arg3: $MethodTypeDesc$Type): $DirectMethodHandleDesc
function ofConstructor(arg0: $ClassDesc$Type, ...arg1: ($ClassDesc$Type)[]): $DirectMethodHandleDesc
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MethodHandleDesc$Type = ($MethodHandleDesc);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MethodHandleDesc_ = $MethodHandleDesc$Type;
}}
declare module "packages/java/lang/module/$ModuleDescriptor$Modifier" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $ModuleDescriptor$Modifier extends $Enum<($ModuleDescriptor$Modifier)> {
static readonly "OPEN": $ModuleDescriptor$Modifier
static readonly "AUTOMATIC": $ModuleDescriptor$Modifier
static readonly "SYNTHETIC": $ModuleDescriptor$Modifier
static readonly "MANDATED": $ModuleDescriptor$Modifier


public static "values"(): ($ModuleDescriptor$Modifier)[]
public static "valueOf"(arg0: string): $ModuleDescriptor$Modifier
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModuleDescriptor$Modifier$Type = (("synthetic") | ("automatic") | ("mandated") | ("open")) | ($ModuleDescriptor$Modifier);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModuleDescriptor$Modifier_ = $ModuleDescriptor$Modifier$Type;
}}
declare module "packages/java/lang/reflect/$GenericDeclaration" {
import {$Annotation, $Annotation$Type} from "packages/java/lang/annotation/$Annotation"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$AnnotatedElement, $AnnotatedElement$Type} from "packages/java/lang/reflect/$AnnotatedElement"
import {$TypeVariable, $TypeVariable$Type} from "packages/java/lang/reflect/$TypeVariable"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export interface $GenericDeclaration extends $AnnotatedElement {

 "getTypeParameters"(): ($TypeVariable<(any)>)[]
 "isAnnotationPresent"(arg0: $Class$Type<(any)>): boolean
 "getAnnotation"<T extends $Annotation>(arg0: $Class$Type<(T)>): T
 "getAnnotationsByType"<T extends $Annotation>(arg0: $Class$Type<(T)>): (T)[]
 "getAnnotations"(): ($Annotation)[]
 "getDeclaredAnnotation"<T extends $Annotation>(arg0: $Class$Type<(T)>): T
 "getDeclaredAnnotationsByType"<T extends $Annotation>(arg0: $Class$Type<(T)>): (T)[]
 "getDeclaredAnnotations"(): ($Annotation)[]
}

export namespace $GenericDeclaration {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GenericDeclaration$Type = ($GenericDeclaration);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GenericDeclaration_ = $GenericDeclaration$Type;
}}
declare module "packages/java/lang/management/$ThreadMXBean" {
import {$ThreadInfo, $ThreadInfo$Type} from "packages/java/lang/management/$ThreadInfo"
import {$PlatformManagedObject, $PlatformManagedObject$Type} from "packages/java/lang/management/$PlatformManagedObject"
import {$ObjectName, $ObjectName$Type} from "packages/javax/management/$ObjectName"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export interface $ThreadMXBean extends $PlatformManagedObject {

 "getThreadCount"(): integer
 "getTotalStartedThreadCount"(): long
 "getAllThreadIds"(): (long)[]
 "getThreadInfo"(arg0: (long)[]): ($ThreadInfo)[]
 "getThreadInfo"(arg0: long, arg1: integer): $ThreadInfo
 "getThreadInfo"(arg0: (long)[], arg1: integer): ($ThreadInfo)[]
 "getThreadInfo"(arg0: (long)[], arg1: boolean, arg2: boolean, arg3: integer): ($ThreadInfo)[]
 "getThreadInfo"(arg0: (long)[], arg1: boolean, arg2: boolean): ($ThreadInfo)[]
 "getThreadInfo"(arg0: long): $ThreadInfo
 "setThreadContentionMonitoringEnabled"(arg0: boolean): void
 "getCurrentThreadCpuTime"(): long
 "getCurrentThreadUserTime"(): long
 "getThreadCpuTime"(arg0: long): long
 "getThreadUserTime"(arg0: long): long
 "isThreadCpuTimeSupported"(): boolean
 "setThreadCpuTimeEnabled"(arg0: boolean): void
 "findMonitorDeadlockedThreads"(): (long)[]
 "resetPeakThreadCount"(): void
 "findDeadlockedThreads"(): (long)[]
 "dumpAllThreads"(arg0: boolean, arg1: boolean): ($ThreadInfo)[]
 "dumpAllThreads"(arg0: boolean, arg1: boolean, arg2: integer): ($ThreadInfo)[]
 "isThreadContentionMonitoringSupported"(): boolean
 "isCurrentThreadCpuTimeSupported"(): boolean
 "isObjectMonitorUsageSupported"(): boolean
 "isSynchronizerUsageSupported"(): boolean
 "isThreadContentionMonitoringEnabled"(): boolean
 "isThreadCpuTimeEnabled"(): boolean
 "getPeakThreadCount"(): integer
 "getDaemonThreadCount"(): integer
 "getObjectName"(): $ObjectName
}

export namespace $ThreadMXBean {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ThreadMXBean$Type = ($ThreadMXBean);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ThreadMXBean_ = $ThreadMXBean$Type;
}}
declare module "packages/java/lang/reflect/$Executable" {
import {$Annotation, $Annotation$Type} from "packages/java/lang/annotation/$Annotation"
import {$Member, $Member$Type} from "packages/java/lang/reflect/$Member"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$Type, $Type$Type} from "packages/java/lang/reflect/$Type"
import {$AnnotatedType, $AnnotatedType$Type} from "packages/java/lang/reflect/$AnnotatedType"
import {$Parameter, $Parameter$Type} from "packages/java/lang/reflect/$Parameter"
import {$TypeVariable, $TypeVariable$Type} from "packages/java/lang/reflect/$TypeVariable"
import {$GenericDeclaration, $GenericDeclaration$Type} from "packages/java/lang/reflect/$GenericDeclaration"
import {$AccessibleObject, $AccessibleObject$Type} from "packages/java/lang/reflect/$AccessibleObject"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $Executable extends $AccessibleObject implements $Member, $GenericDeclaration {


public "getName"(): string
public "getModifiers"(): integer
public "getTypeParameters"(): ($TypeVariable<(any)>)[]
public "getParameterTypes"(): ($Class<(any)>)[]
public "toGenericString"(): string
public "isSynthetic"(): boolean
public "getDeclaringClass"(): $Class<(any)>
public "getAnnotation"<T extends $Annotation>(arg0: $Class$Type<(T)>): T
public "getAnnotationsByType"<T extends $Annotation>(arg0: $Class$Type<(T)>): (T)[]
public "getDeclaredAnnotations"(): ($Annotation)[]
public "isVarArgs"(): boolean
public "getAnnotatedParameterTypes"(): ($AnnotatedType)[]
public "getParameterCount"(): integer
public "getParameterAnnotations"(): (($Annotation)[])[]
public "getGenericParameterTypes"(): ($Type)[]
public "getGenericExceptionTypes"(): ($Type)[]
public "getExceptionTypes"(): ($Class<(any)>)[]
public "getAnnotatedReturnType"(): $AnnotatedType
public "getParameters"(): ($Parameter)[]
public "getAnnotatedReceiverType"(): $AnnotatedType
public "getAnnotatedExceptionTypes"(): ($AnnotatedType)[]
get "name"(): string
get "modifiers"(): integer
get "typeParameters"(): ($TypeVariable<(any)>)[]
get "parameterTypes"(): ($Class<(any)>)[]
get "synthetic"(): boolean
get "declaringClass"(): $Class<(any)>
get "declaredAnnotations"(): ($Annotation)[]
get "varArgs"(): boolean
get "annotatedParameterTypes"(): ($AnnotatedType)[]
get "parameterCount"(): integer
get "parameterAnnotations"(): (($Annotation)[])[]
get "genericParameterTypes"(): ($Type)[]
get "genericExceptionTypes"(): ($Type)[]
get "exceptionTypes"(): ($Class<(any)>)[]
get "annotatedReturnType"(): $AnnotatedType
get "parameters"(): ($Parameter)[]
get "annotatedReceiverType"(): $AnnotatedType
get "annotatedExceptionTypes"(): ($AnnotatedType)[]
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Executable$Type = ($Executable);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Executable_ = $Executable$Type;
}}
declare module "packages/java/lang/$ThreadLocal" {
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $ThreadLocal<T> {

constructor()

public "remove"(): void
public "get"(): T
public "set"(arg0: T): void
public static "withInitial"<S>(arg0: $Supplier$Type<(any)>): $ThreadLocal<(S)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ThreadLocal$Type<T> = ($ThreadLocal<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ThreadLocal_<T> = $ThreadLocal$Type<(T)>;
}}
declare module "packages/java/lang/instrument/$ClassDefinition" {
import {$Class, $Class$Type} from "packages/java/lang/$Class"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $ClassDefinition {

constructor(arg0: $Class$Type<(any)>, arg1: (byte)[])

public "getDefinitionClass"(): $Class<(any)>
public "getDefinitionClassFile"(): (byte)[]
get "definitionClass"(): $Class<(any)>
get "definitionClassFile"(): (byte)[]
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClassDefinition$Type = ($ClassDefinition);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClassDefinition_ = $ClassDefinition$Type;
}}
declare module "packages/java/lang/$IndexOutOfBoundsException" {
import {$RuntimeException, $RuntimeException$Type} from "packages/java/lang/$RuntimeException"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $IndexOutOfBoundsException extends $RuntimeException {

constructor(arg0: long)
constructor(arg0: integer)
constructor(arg0: string)
constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IndexOutOfBoundsException$Type = ($IndexOutOfBoundsException);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IndexOutOfBoundsException_ = $IndexOutOfBoundsException$Type;
}}
declare module "packages/java/lang/invoke/$VarHandle$AccessMode" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $VarHandle$AccessMode extends $Enum<($VarHandle$AccessMode)> {
static readonly "GET": $VarHandle$AccessMode
static readonly "SET": $VarHandle$AccessMode
static readonly "GET_VOLATILE": $VarHandle$AccessMode
static readonly "SET_VOLATILE": $VarHandle$AccessMode
static readonly "GET_ACQUIRE": $VarHandle$AccessMode
static readonly "SET_RELEASE": $VarHandle$AccessMode
static readonly "GET_OPAQUE": $VarHandle$AccessMode
static readonly "SET_OPAQUE": $VarHandle$AccessMode
static readonly "COMPARE_AND_SET": $VarHandle$AccessMode
static readonly "COMPARE_AND_EXCHANGE": $VarHandle$AccessMode
static readonly "COMPARE_AND_EXCHANGE_ACQUIRE": $VarHandle$AccessMode
static readonly "COMPARE_AND_EXCHANGE_RELEASE": $VarHandle$AccessMode
static readonly "WEAK_COMPARE_AND_SET_PLAIN": $VarHandle$AccessMode
static readonly "WEAK_COMPARE_AND_SET": $VarHandle$AccessMode
static readonly "WEAK_COMPARE_AND_SET_ACQUIRE": $VarHandle$AccessMode
static readonly "WEAK_COMPARE_AND_SET_RELEASE": $VarHandle$AccessMode
static readonly "GET_AND_SET": $VarHandle$AccessMode
static readonly "GET_AND_SET_ACQUIRE": $VarHandle$AccessMode
static readonly "GET_AND_SET_RELEASE": $VarHandle$AccessMode
static readonly "GET_AND_ADD": $VarHandle$AccessMode
static readonly "GET_AND_ADD_ACQUIRE": $VarHandle$AccessMode
static readonly "GET_AND_ADD_RELEASE": $VarHandle$AccessMode
static readonly "GET_AND_BITWISE_OR": $VarHandle$AccessMode
static readonly "GET_AND_BITWISE_OR_RELEASE": $VarHandle$AccessMode
static readonly "GET_AND_BITWISE_OR_ACQUIRE": $VarHandle$AccessMode
static readonly "GET_AND_BITWISE_AND": $VarHandle$AccessMode
static readonly "GET_AND_BITWISE_AND_RELEASE": $VarHandle$AccessMode
static readonly "GET_AND_BITWISE_AND_ACQUIRE": $VarHandle$AccessMode
static readonly "GET_AND_BITWISE_XOR": $VarHandle$AccessMode
static readonly "GET_AND_BITWISE_XOR_RELEASE": $VarHandle$AccessMode
static readonly "GET_AND_BITWISE_XOR_ACQUIRE": $VarHandle$AccessMode


public static "values"(): ($VarHandle$AccessMode)[]
public "methodName"(): string
public static "valueOf"(arg0: string): $VarHandle$AccessMode
public static "valueFromMethodName"(arg0: string): $VarHandle$AccessMode
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $VarHandle$AccessMode$Type = (("weak_compare_and_set_acquire") | ("get_acquire") | ("get_and_bitwise_xor_release") | ("get_and_set_acquire") | ("get_and_set_release") | ("get_and_bitwise_or_release") | ("compare_and_exchange_release") | ("get") | ("get_and_add_acquire") | ("get_and_bitwise_and_acquire") | ("get_and_bitwise_or") | ("set") | ("set_release") | ("compare_and_exchange_acquire") | ("compare_and_exchange") | ("set_opaque") | ("get_and_bitwise_or_acquire") | ("get_opaque") | ("compare_and_set") | ("get_volatile") | ("set_volatile") | ("weak_compare_and_set") | ("get_and_bitwise_xor_acquire") | ("get_and_bitwise_and_release") | ("get_and_bitwise_and") | ("weak_compare_and_set_release") | ("get_and_add_release") | ("weak_compare_and_set_plain") | ("get_and_set") | ("get_and_add") | ("get_and_bitwise_xor")) | ($VarHandle$AccessMode);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $VarHandle$AccessMode_ = $VarHandle$AccessMode$Type;
}}
declare module "packages/java/lang/management/$MonitorInfo" {
import {$LockInfo, $LockInfo$Type} from "packages/java/lang/management/$LockInfo"
import {$StackTraceElement, $StackTraceElement$Type} from "packages/java/lang/$StackTraceElement"
import {$CompositeData, $CompositeData$Type} from "packages/javax/management/openmbean/$CompositeData"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $MonitorInfo extends $LockInfo {

constructor(arg0: string, arg1: integer, arg2: integer, arg3: $StackTraceElement$Type)

public static "from"(arg0: $CompositeData$Type): $MonitorInfo
public "getLockedStackDepth"(): integer
public "getLockedStackFrame"(): $StackTraceElement
get "lockedStackDepth"(): integer
get "lockedStackFrame"(): $StackTraceElement
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MonitorInfo$Type = ($MonitorInfo);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MonitorInfo_ = $MonitorInfo$Type;
}}
declare module "packages/java/lang/$Package" {
import {$Annotation, $Annotation$Type} from "packages/java/lang/annotation/$Annotation"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$NamedPackage, $NamedPackage$Type} from "packages/java/lang/$NamedPackage"
import {$URL, $URL$Type} from "packages/java/net/$URL"
import {$AnnotatedElement, $AnnotatedElement$Type} from "packages/java/lang/reflect/$AnnotatedElement"

export class $Package extends $NamedPackage implements $AnnotatedElement {


public "getName"(): string
public "toString"(): string
public "hashCode"(): integer
public "isAnnotationPresent"(arg0: $Class$Type<(any)>): boolean
/**
 * 
 * @deprecated
 */
public static "getPackage"(arg0: string): $Package
public "getAnnotation"<A extends $Annotation>(arg0: $Class$Type<(A)>): A
public "getAnnotationsByType"<A extends $Annotation>(arg0: $Class$Type<(A)>): (A)[]
public "getAnnotations"(): ($Annotation)[]
public "getDeclaredAnnotation"<A extends $Annotation>(arg0: $Class$Type<(A)>): A
public "getDeclaredAnnotationsByType"<A extends $Annotation>(arg0: $Class$Type<(A)>): (A)[]
public "getDeclaredAnnotations"(): ($Annotation)[]
public "isSealed"(arg0: $URL$Type): boolean
public "isSealed"(): boolean
public static "getPackages"(): ($Package)[]
public "getSpecificationTitle"(): string
public "getSpecificationVersion"(): string
public "getSpecificationVendor"(): string
public "getImplementationTitle"(): string
public "getImplementationVersion"(): string
public "getImplementationVendor"(): string
public "isCompatibleWith"(arg0: string): boolean
get "name"(): string
get "annotations"(): ($Annotation)[]
get "declaredAnnotations"(): ($Annotation)[]
get "sealed"(): boolean
get "packages"(): ($Package)[]
get "specificationTitle"(): string
get "specificationVersion"(): string
get "specificationVendor"(): string
get "implementationTitle"(): string
get "implementationVersion"(): string
get "implementationVendor"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Package$Type = ($Package);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Package_ = $Package$Type;
}}
declare module "packages/java/lang/$Iterable" {
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$Iterator, $Iterator$Type} from "packages/java/util/$Iterator"
import {$Spliterator, $Spliterator$Type} from "packages/java/util/$Spliterator"

export interface $Iterable<T> {

 "iterator"(): $Iterator<(T)>
 "spliterator"(): $Spliterator<(T)>
 "forEach"(arg0: $Consumer$Type<(any)>): void

(): $Iterator<(T)>
}

export namespace $Iterable {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Iterable$Type<T> = ((T)[]) | ($Iterable<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Iterable_<T> = $Iterable$Type<(T)>;
}}
declare module "packages/java/lang/$Throwable" {
import {$Serializable, $Serializable$Type} from "packages/java/io/$Serializable"
import {$StackTraceElement, $StackTraceElement$Type} from "packages/java/lang/$StackTraceElement"
import {$PrintWriter, $PrintWriter$Type} from "packages/java/io/$PrintWriter"
import {$PrintStream, $PrintStream$Type} from "packages/java/io/$PrintStream"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $Throwable implements $Serializable {

constructor(arg0: $Throwable$Type)
constructor(arg0: string, arg1: $Throwable$Type)
constructor(arg0: string)
constructor()

public "printStackTrace"(): void
public "printStackTrace"(arg0: $PrintWriter$Type): void
public "printStackTrace"(arg0: $PrintStream$Type): void
public "getStackTrace"(): ($StackTraceElement)[]
public "fillInStackTrace"(): $Throwable
public "getCause"(): $Throwable
public "initCause"(arg0: $Throwable$Type): $Throwable
public "toString"(): string
public "getMessage"(): string
public "getSuppressed"(): ($Throwable)[]
public "getLocalizedMessage"(): string
public "setStackTrace"(arg0: ($StackTraceElement$Type)[]): void
public "addSuppressed"(arg0: $Throwable$Type): void
get "stackTrace"(): ($StackTraceElement)[]
get "cause"(): $Throwable
get "message"(): string
get "suppressed"(): ($Throwable)[]
get "localizedMessage"(): string
set "stackTrace"(value: ($StackTraceElement$Type)[])
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Throwable$Type = ($Throwable);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Throwable_ = $Throwable$Type;
}}
declare module "packages/java/lang/module/$ModuleDescriptor$Opens$Modifier" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $ModuleDescriptor$Opens$Modifier extends $Enum<($ModuleDescriptor$Opens$Modifier)> {
static readonly "SYNTHETIC": $ModuleDescriptor$Opens$Modifier
static readonly "MANDATED": $ModuleDescriptor$Opens$Modifier


public static "values"(): ($ModuleDescriptor$Opens$Modifier)[]
public static "valueOf"(arg0: string): $ModuleDescriptor$Opens$Modifier
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModuleDescriptor$Opens$Modifier$Type = (("synthetic") | ("mandated")) | ($ModuleDescriptor$Opens$Modifier);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModuleDescriptor$Opens$Modifier_ = $ModuleDescriptor$Opens$Modifier$Type;
}}
declare module "packages/java/lang/reflect/$InvocationHandler" {
import {$Method, $Method$Type} from "packages/java/lang/reflect/$Method"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export interface $InvocationHandler {

 "invoke"(arg0: any, arg1: $Method$Type, arg2: (any)[]): any

(arg0: any, arg1: $Method$Type, arg2: (any)[]): any
}

export namespace $InvocationHandler {
function invokeDefault(arg0: any, arg1: $Method$Type, ...arg2: (any)[]): any
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InvocationHandler$Type = ($InvocationHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InvocationHandler_ = $InvocationHandler$Type;
}}
declare module "packages/java/lang/module/$ModuleDescriptor$Requires" {
import {$ModuleDescriptor$Version, $ModuleDescriptor$Version$Type} from "packages/java/lang/module/$ModuleDescriptor$Version"
import {$Comparable, $Comparable$Type} from "packages/java/lang/$Comparable"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$ModuleDescriptor$Requires$Modifier, $ModuleDescriptor$Requires$Modifier$Type} from "packages/java/lang/module/$ModuleDescriptor$Requires$Modifier"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $ModuleDescriptor$Requires implements $Comparable<($ModuleDescriptor$Requires)> {


public "modifiers"(): $Set<($ModuleDescriptor$Requires$Modifier)>
public "name"(): string
public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "compareTo"(arg0: $ModuleDescriptor$Requires$Type): integer
public "rawCompiledVersion"(): $Optional<(string)>
public "compiledVersion"(): $Optional<($ModuleDescriptor$Version)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModuleDescriptor$Requires$Type = ($ModuleDescriptor$Requires);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModuleDescriptor$Requires_ = $ModuleDescriptor$Requires$Type;
}}
declare module "packages/java/lang/reflect/$Constructor" {
import {$Annotation, $Annotation$Type} from "packages/java/lang/annotation/$Annotation"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$Type, $Type$Type} from "packages/java/lang/reflect/$Type"
import {$AnnotatedType, $AnnotatedType$Type} from "packages/java/lang/reflect/$AnnotatedType"
import {$Executable, $Executable$Type} from "packages/java/lang/reflect/$Executable"
import {$TypeVariable, $TypeVariable$Type} from "packages/java/lang/reflect/$TypeVariable"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $Constructor<T> extends $Executable {


public "getName"(): string
public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "getModifiers"(): integer
public "getTypeParameters"(): ($TypeVariable<($Constructor<(T)>)>)[]
public "newInstance"(...arg0: (any)[]): T
public "getParameterTypes"(): ($Class<(any)>)[]
public "toGenericString"(): string
public "isSynthetic"(): boolean
public "getDeclaringClass"(): $Class<(T)>
public "getAnnotation"<T extends $Annotation>(arg0: $Class$Type<(T)>): T
public "getDeclaredAnnotations"(): ($Annotation)[]
public "setAccessible"(arg0: boolean): void
public "isVarArgs"(): boolean
public "getParameterCount"(): integer
public "getParameterAnnotations"(): (($Annotation)[])[]
public "getGenericParameterTypes"(): ($Type)[]
public "getGenericExceptionTypes"(): ($Type)[]
public "getExceptionTypes"(): ($Class<(any)>)[]
public "getAnnotatedReturnType"(): $AnnotatedType
public "getAnnotatedReceiverType"(): $AnnotatedType
get "name"(): string
get "modifiers"(): integer
get "typeParameters"(): ($TypeVariable<($Constructor<(T)>)>)[]
get "parameterTypes"(): ($Class<(any)>)[]
get "synthetic"(): boolean
get "declaringClass"(): $Class<(T)>
get "declaredAnnotations"(): ($Annotation)[]
set "accessible"(value: boolean)
get "varArgs"(): boolean
get "parameterCount"(): integer
get "parameterAnnotations"(): (($Annotation)[])[]
get "genericParameterTypes"(): ($Type)[]
get "genericExceptionTypes"(): ($Type)[]
get "exceptionTypes"(): ($Class<(any)>)[]
get "annotatedReturnType"(): $AnnotatedType
get "annotatedReceiverType"(): $AnnotatedType
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Constructor$Type<T> = ($Constructor<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Constructor_<T> = $Constructor$Type<(T)>;
}}
declare module "packages/java/lang/$NullPointerException" {
import {$Throwable, $Throwable$Type} from "packages/java/lang/$Throwable"
import {$RuntimeException, $RuntimeException$Type} from "packages/java/lang/$RuntimeException"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $NullPointerException extends $RuntimeException {

constructor()
constructor(arg0: string)

public "fillInStackTrace"(): $Throwable
public "getMessage"(): string
get "message"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NullPointerException$Type = ($NullPointerException);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NullPointerException_ = $NullPointerException$Type;
}}
declare module "packages/java/lang/$CharSequence" {
import {$IntStream, $IntStream$Type} from "packages/java/util/stream/$IntStream"

export interface $CharSequence {

 "length"(): integer
 "toString"(): string
 "charAt"(arg0: integer): character
 "isEmpty"(): boolean
 "codePoints"(): $IntStream
 "subSequence"(arg0: integer, arg1: integer): charseq
 "chars"(): $IntStream
}

export namespace $CharSequence {
function compare(arg0: charseq, arg1: charseq): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CharSequence$Type = ($CharSequence);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CharSequence_ = $CharSequence$Type;
}}
declare module "packages/java/lang/invoke/$TypeDescriptor$OfMethod" {
import {$List, $List$Type} from "packages/java/util/$List"
import {$TypeDescriptor, $TypeDescriptor$Type} from "packages/java/lang/invoke/$TypeDescriptor"
import {$TypeDescriptor$OfField, $TypeDescriptor$OfField$Type} from "packages/java/lang/invoke/$TypeDescriptor$OfField"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export interface $TypeDescriptor$OfMethod<F extends $TypeDescriptor$OfField<(F)>, M extends $TypeDescriptor$OfMethod<(F), (M)>> extends $TypeDescriptor {

 "returnType"(): F
 "parameterType"(arg0: integer): F
 "insertParameterTypes"(arg0: integer, ...arg1: (F)[]): M
 "changeReturnType"(arg0: F): M
 "dropParameterTypes"(arg0: integer, arg1: integer): M
 "parameterCount"(): integer
 "parameterList"(): $List<(F)>
 "parameterArray"(): (F)[]
 "changeParameterType"(arg0: integer, arg1: F): M
 "descriptorString"(): string
}

export namespace $TypeDescriptor$OfMethod {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TypeDescriptor$OfMethod$Type<F, M> = ($TypeDescriptor$OfMethod<(F), (M)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TypeDescriptor$OfMethod_<F, M> = $TypeDescriptor$OfMethod$Type<(F), (M)>;
}}
declare module "packages/java/lang/reflect/$AccessibleObject" {
import {$Annotation, $Annotation$Type} from "packages/java/lang/annotation/$Annotation"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$AnnotatedElement, $AnnotatedElement$Type} from "packages/java/lang/reflect/$AnnotatedElement"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $AccessibleObject implements $AnnotatedElement {


public "isAnnotationPresent"(arg0: $Class$Type<(any)>): boolean
public "getAnnotation"<T extends $Annotation>(arg0: $Class$Type<(T)>): T
public "getAnnotationsByType"<T extends $Annotation>(arg0: $Class$Type<(T)>): (T)[]
public "getAnnotations"(): ($Annotation)[]
public "getDeclaredAnnotation"<T extends $Annotation>(arg0: $Class$Type<(T)>): T
public "getDeclaredAnnotationsByType"<T extends $Annotation>(arg0: $Class$Type<(T)>): (T)[]
public "getDeclaredAnnotations"(): ($Annotation)[]
public "setAccessible"(arg0: boolean): void
public static "setAccessible"(arg0: ($AccessibleObject$Type)[], arg1: boolean): void
public "trySetAccessible"(): boolean
/**
 * 
 * @deprecated
 */
public "isAccessible"(): boolean
public "canAccess"(arg0: any): boolean
get "annotations"(): ($Annotation)[]
get "declaredAnnotations"(): ($Annotation)[]
set "accessible"(value: boolean)
get "accessible"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AccessibleObject$Type = ($AccessibleObject);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AccessibleObject_ = $AccessibleObject$Type;
}}
declare module "packages/java/lang/$ModuleLayer$Controller" {
import {$Module, $Module$Type} from "packages/java/lang/$Module"
import {$ModuleLayer, $ModuleLayer$Type} from "packages/java/lang/$ModuleLayer"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $ModuleLayer$Controller {


public "addReads"(arg0: $Module$Type, arg1: $Module$Type): $ModuleLayer$Controller
public "addExports"(arg0: $Module$Type, arg1: string, arg2: $Module$Type): $ModuleLayer$Controller
public "addOpens"(arg0: $Module$Type, arg1: string, arg2: $Module$Type): $ModuleLayer$Controller
public "layer"(): $ModuleLayer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModuleLayer$Controller$Type = ($ModuleLayer$Controller);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModuleLayer$Controller_ = $ModuleLayer$Controller$Type;
}}
declare module "packages/java/lang/module/$ModuleDescriptor$Opens" {
import {$Comparable, $Comparable$Type} from "packages/java/lang/$Comparable"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$ModuleDescriptor$Opens$Modifier, $ModuleDescriptor$Opens$Modifier$Type} from "packages/java/lang/module/$ModuleDescriptor$Opens$Modifier"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $ModuleDescriptor$Opens implements $Comparable<($ModuleDescriptor$Opens)> {


public "modifiers"(): $Set<($ModuleDescriptor$Opens$Modifier)>
public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "compareTo"(arg0: $ModuleDescriptor$Opens$Type): integer
public "source"(): string
public "isQualified"(): boolean
public "targets"(): $Set<(string)>
get "qualified"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModuleDescriptor$Opens$Type = ($ModuleDescriptor$Opens);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModuleDescriptor$Opens_ = $ModuleDescriptor$Opens$Type;
}}
declare module "packages/java/lang/constant/$DirectMethodHandleDesc$Kind" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $DirectMethodHandleDesc$Kind extends $Enum<($DirectMethodHandleDesc$Kind)> {
static readonly "STATIC": $DirectMethodHandleDesc$Kind
static readonly "INTERFACE_STATIC": $DirectMethodHandleDesc$Kind
static readonly "VIRTUAL": $DirectMethodHandleDesc$Kind
static readonly "INTERFACE_VIRTUAL": $DirectMethodHandleDesc$Kind
static readonly "SPECIAL": $DirectMethodHandleDesc$Kind
static readonly "INTERFACE_SPECIAL": $DirectMethodHandleDesc$Kind
static readonly "CONSTRUCTOR": $DirectMethodHandleDesc$Kind
static readonly "GETTER": $DirectMethodHandleDesc$Kind
static readonly "SETTER": $DirectMethodHandleDesc$Kind
static readonly "STATIC_GETTER": $DirectMethodHandleDesc$Kind
static readonly "STATIC_SETTER": $DirectMethodHandleDesc$Kind
readonly "refKind": integer
readonly "isInterface": boolean


public static "values"(): ($DirectMethodHandleDesc$Kind)[]
public static "valueOf"(arg0: integer, arg1: boolean): $DirectMethodHandleDesc$Kind
public static "valueOf"(arg0: string): $DirectMethodHandleDesc$Kind
public static "valueOf"(arg0: integer): $DirectMethodHandleDesc$Kind
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DirectMethodHandleDesc$Kind$Type = (("special") | ("interface_static") | ("virtual") | ("static_setter") | ("static") | ("getter") | ("static_getter") | ("interface_virtual") | ("constructor") | ("setter") | ("interface_special")) | ($DirectMethodHandleDesc$Kind);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DirectMethodHandleDesc$Kind_ = $DirectMethodHandleDesc$Kind$Type;
}}
declare module "packages/java/lang/$ClassLoader" {
import {$Package, $Package$Type} from "packages/java/lang/$Package"
import {$Module, $Module$Type} from "packages/java/lang/$Module"
import {$Enumeration, $Enumeration$Type} from "packages/java/util/$Enumeration"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$InputStream, $InputStream$Type} from "packages/java/io/$InputStream"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$URL, $URL$Type} from "packages/java/net/$URL"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $ClassLoader {


public "getName"(): string
public "loadClass"(arg0: string): $Class<(any)>
public static "getPlatformClassLoader"(): $ClassLoader
public static "getSystemClassLoader"(): $ClassLoader
public static "getSystemResourceAsStream"(arg0: string): $InputStream
public "getResourceAsStream"(arg0: string): $InputStream
public static "getSystemResource"(arg0: string): $URL
public "getResource"(arg0: string): $URL
public "getResources"(arg0: string): $Enumeration<($URL)>
public "getDefinedPackage"(arg0: string): $Package
public "resources"(arg0: string): $Stream<($URL)>
public "isRegisteredAsParallelCapable"(): boolean
public static "getSystemResources"(arg0: string): $Enumeration<($URL)>
public "getParent"(): $ClassLoader
public "getUnnamedModule"(): $Module
public "getDefinedPackages"(): ($Package)[]
public "setDefaultAssertionStatus"(arg0: boolean): void
public "setPackageAssertionStatus"(arg0: string, arg1: boolean): void
public "setClassAssertionStatus"(arg0: string, arg1: boolean): void
public "clearAssertionStatus"(): void
get "name"(): string
get "platformClassLoader"(): $ClassLoader
get "systemClassLoader"(): $ClassLoader
get "registeredAsParallelCapable"(): boolean
get "parent"(): $ClassLoader
get "unnamedModule"(): $Module
get "definedPackages"(): ($Package)[]
set "defaultAssertionStatus"(value: boolean)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClassLoader$Type = ($ClassLoader);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClassLoader_ = $ClassLoader$Type;
}}
declare module "packages/java/lang/module/$ResolvedModule" {
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$Configuration, $Configuration$Type} from "packages/java/lang/module/$Configuration"
import {$ModuleReference, $ModuleReference$Type} from "packages/java/lang/module/$ModuleReference"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $ResolvedModule {


public "name"(): string
public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "reads"(): $Set<($ResolvedModule)>
public "reference"(): $ModuleReference
public "configuration"(): $Configuration
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ResolvedModule$Type = ($ResolvedModule);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ResolvedModule_ = $ResolvedModule$Type;
}}
declare module "packages/java/lang/invoke/$TypeDescriptor$OfField" {
import {$TypeDescriptor, $TypeDescriptor$Type} from "packages/java/lang/invoke/$TypeDescriptor"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export interface $TypeDescriptor$OfField<F extends $TypeDescriptor$OfField<(F)>> extends $TypeDescriptor {

 "isArray"(): boolean
 "isPrimitive"(): boolean
 "componentType"(): F
 "arrayType"(): F
 "descriptorString"(): string
}

export namespace $TypeDescriptor$OfField {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TypeDescriptor$OfField$Type<F> = ($TypeDescriptor$OfField<(F)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TypeDescriptor$OfField_<F> = $TypeDescriptor$OfField$Type<(F)>;
}}
declare module "packages/java/lang/$StackTraceElement" {
import {$Serializable, $Serializable$Type} from "packages/java/io/$Serializable"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $StackTraceElement implements $Serializable {

constructor(arg0: string, arg1: string, arg2: string, arg3: string, arg4: string, arg5: string, arg6: integer)
constructor(arg0: string, arg1: string, arg2: string, arg3: integer)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "isNativeMethod"(): boolean
public "getFileName"(): string
public "getLineNumber"(): integer
public "getModuleName"(): string
public "getModuleVersion"(): string
public "getClassLoaderName"(): string
public "getClassName"(): string
public "getMethodName"(): string
get "nativeMethod"(): boolean
get "fileName"(): string
get "lineNumber"(): integer
get "moduleName"(): string
get "moduleVersion"(): string
get "classLoaderName"(): string
get "className"(): string
get "methodName"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StackTraceElement$Type = ($StackTraceElement);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StackTraceElement_ = $StackTraceElement$Type;
}}
declare module "packages/java/lang/$Readable" {
import {$CharBuffer, $CharBuffer$Type} from "packages/java/nio/$CharBuffer"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export interface $Readable {

 "read"(arg0: $CharBuffer$Type): integer

(arg0: $CharBuffer$Type): integer
}

export namespace $Readable {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Readable$Type = ($Readable);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Readable_ = $Readable$Type;
}}
declare module "packages/java/lang/reflect/$AnnotatedElement" {
import {$Annotation, $Annotation$Type} from "packages/java/lang/annotation/$Annotation"
import {$Class, $Class$Type} from "packages/java/lang/$Class"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export interface $AnnotatedElement {

 "isAnnotationPresent"(arg0: $Class$Type<(any)>): boolean
 "getAnnotation"<T extends $Annotation>(arg0: $Class$Type<(T)>): T
 "getAnnotationsByType"<T extends $Annotation>(arg0: $Class$Type<(T)>): (T)[]
 "getAnnotations"(): ($Annotation)[]
 "getDeclaredAnnotation"<T extends $Annotation>(arg0: $Class$Type<(T)>): T
 "getDeclaredAnnotationsByType"<T extends $Annotation>(arg0: $Class$Type<(T)>): (T)[]
 "getDeclaredAnnotations"(): ($Annotation)[]
}

export namespace $AnnotatedElement {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AnnotatedElement$Type = ($AnnotatedElement);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AnnotatedElement_ = $AnnotatedElement$Type;
}}
declare module "packages/java/lang/$Character$Subset" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $Character$Subset {


public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Character$Subset$Type = ($Character$Subset);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Character$Subset_ = $Character$Subset$Type;
}}
declare module "packages/java/lang/module/$Configuration" {
import {$ResolvedModule, $ResolvedModule$Type} from "packages/java/lang/module/$ResolvedModule"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$ModuleFinder, $ModuleFinder$Type} from "packages/java/lang/module/$ModuleFinder"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $Configuration {


public "toString"(): string
public static "empty"(): $Configuration
public "resolve"(arg0: $ModuleFinder$Type, arg1: $ModuleFinder$Type, arg2: $Collection$Type<(string)>): $Configuration
public static "resolve"(arg0: $ModuleFinder$Type, arg1: $List$Type<($Configuration$Type)>, arg2: $ModuleFinder$Type, arg3: $Collection$Type<(string)>): $Configuration
public "modules"(): $Set<($ResolvedModule)>
public "parents"(): $List<($Configuration)>
public "findModule"(arg0: string): $Optional<($ResolvedModule)>
public static "resolveAndBind"(arg0: $ModuleFinder$Type, arg1: $List$Type<($Configuration$Type)>, arg2: $ModuleFinder$Type, arg3: $Collection$Type<(string)>): $Configuration
public "resolveAndBind"(arg0: $ModuleFinder$Type, arg1: $ModuleFinder$Type, arg2: $Collection$Type<(string)>): $Configuration
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Configuration$Type = ($Configuration);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Configuration_ = $Configuration$Type;
}}
declare module "packages/java/lang/invoke/$MethodHandles$Lookup$ClassOption" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $MethodHandles$Lookup$ClassOption extends $Enum<($MethodHandles$Lookup$ClassOption)> {
static readonly "NESTMATE": $MethodHandles$Lookup$ClassOption
static readonly "STRONG": $MethodHandles$Lookup$ClassOption


public static "values"(): ($MethodHandles$Lookup$ClassOption)[]
public static "valueOf"(arg0: string): $MethodHandles$Lookup$ClassOption
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MethodHandles$Lookup$ClassOption$Type = (("strong") | ("nestmate")) | ($MethodHandles$Lookup$ClassOption);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MethodHandles$Lookup$ClassOption_ = $MethodHandles$Lookup$ClassOption$Type;
}}
declare module "packages/java/lang/constant/$Constable" {
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export interface $Constable {

 "describeConstable"(): $Optional<(any)>

(): $Optional<(any)>
}

export namespace $Constable {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Constable$Type = ($Constable);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Constable_ = $Constable$Type;
}}
declare module "packages/java/lang/annotation/$ElementType" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $ElementType extends $Enum<($ElementType)> {
static readonly "TYPE": $ElementType
static readonly "FIELD": $ElementType
static readonly "METHOD": $ElementType
static readonly "PARAMETER": $ElementType
static readonly "CONSTRUCTOR": $ElementType
static readonly "LOCAL_VARIABLE": $ElementType
static readonly "ANNOTATION_TYPE": $ElementType
static readonly "PACKAGE": $ElementType
static readonly "TYPE_PARAMETER": $ElementType
static readonly "TYPE_USE": $ElementType
static readonly "MODULE": $ElementType
static readonly "RECORD_COMPONENT": $ElementType


public static "values"(): ($ElementType)[]
public static "valueOf"(arg0: string): $ElementType
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ElementType$Type = (("local_variable") | ("package") | ("record_component") | ("field") | ("method") | ("parameter") | ("type_use") | ("module") | ("constructor") | ("annotation_type") | ("type_parameter") | ("type")) | ($ElementType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ElementType_ = $ElementType$Type;
}}
declare module "packages/java/lang/constant/$ClassDesc" {
import {$MethodHandles$Lookup, $MethodHandles$Lookup$Type} from "packages/java/lang/invoke/$MethodHandles$Lookup"
import {$ConstantDesc, $ConstantDesc$Type} from "packages/java/lang/constant/$ConstantDesc"
import {$TypeDescriptor$OfField, $TypeDescriptor$OfField$Type} from "packages/java/lang/invoke/$TypeDescriptor$OfField"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export interface $ClassDesc extends $ConstantDesc, $TypeDescriptor$OfField<($ClassDesc)> {

 "equals"(arg0: any): boolean
 "isArray"(): boolean
 "isPrimitive"(): boolean
 "packageName"(): string
 "descriptorString"(): string
 "arrayType"(arg0: integer): $ClassDesc
 "displayName"(): string
 "nested"(arg0: string): $ClassDesc
 "nested"(arg0: string, ...arg1: (string)[]): $ClassDesc
 "isClassOrInterface"(): boolean
 "resolveConstantDesc"(arg0: $MethodHandles$Lookup$Type): any
}

export namespace $ClassDesc {
function of(arg0: string): $ClassDesc
function of(arg0: string, arg1: string): $ClassDesc
function ofDescriptor(arg0: string): $ClassDesc
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClassDesc$Type = ($ClassDesc);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClassDesc_ = $ClassDesc$Type;
}}
declare module "packages/java/lang/management/$ThreadInfo" {
import {$LockInfo, $LockInfo$Type} from "packages/java/lang/management/$LockInfo"
import {$StackTraceElement, $StackTraceElement$Type} from "packages/java/lang/$StackTraceElement"
import {$Thread$State, $Thread$State$Type} from "packages/java/lang/$Thread$State"
import {$MonitorInfo, $MonitorInfo$Type} from "packages/java/lang/management/$MonitorInfo"
import {$CompositeData, $CompositeData$Type} from "packages/javax/management/openmbean/$CompositeData"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $ThreadInfo {


public "getStackTrace"(): ($StackTraceElement)[]
public "toString"(): string
public static "from"(arg0: $CompositeData$Type): $ThreadInfo
public "isDaemon"(): boolean
public "getPriority"(): integer
public "getThreadId"(): long
public "getThreadName"(): string
public "getThreadState"(): $Thread$State
public "getLockName"(): string
public "getLockOwnerName"(): string
public "getLockOwnerId"(): long
public "isSuspended"(): boolean
public "isInNative"(): boolean
public "getLockInfo"(): $LockInfo
public "getLockedSynchronizers"(): ($LockInfo)[]
public "getBlockedTime"(): long
public "getBlockedCount"(): long
public "getWaitedTime"(): long
public "getWaitedCount"(): long
public "getLockedMonitors"(): ($MonitorInfo)[]
get "stackTrace"(): ($StackTraceElement)[]
get "daemon"(): boolean
get "priority"(): integer
get "threadId"(): long
get "threadName"(): string
get "threadState"(): $Thread$State
get "lockName"(): string
get "lockOwnerName"(): string
get "lockOwnerId"(): long
get "suspended"(): boolean
get "inNative"(): boolean
get "lockInfo"(): $LockInfo
get "lockedSynchronizers"(): ($LockInfo)[]
get "blockedTime"(): long
get "blockedCount"(): long
get "waitedTime"(): long
get "waitedCount"(): long
get "lockedMonitors"(): ($MonitorInfo)[]
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ThreadInfo$Type = ($ThreadInfo);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ThreadInfo_ = $ThreadInfo$Type;
}}
declare module "packages/java/lang/reflect/$Method" {
import {$Annotation, $Annotation$Type} from "packages/java/lang/annotation/$Annotation"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$Type, $Type$Type} from "packages/java/lang/reflect/$Type"
import {$AnnotatedType, $AnnotatedType$Type} from "packages/java/lang/reflect/$AnnotatedType"
import {$Executable, $Executable$Type} from "packages/java/lang/reflect/$Executable"
import {$TypeVariable, $TypeVariable$Type} from "packages/java/lang/reflect/$TypeVariable"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $Method extends $Executable {


public "invoke"(arg0: any, ...arg1: (any)[]): any
public "getName"(): string
public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "getModifiers"(): integer
public "getTypeParameters"(): ($TypeVariable<($Method)>)[]
public "getReturnType"(): $Class<(any)>
public "getParameterTypes"(): ($Class<(any)>)[]
public "toGenericString"(): string
public "isSynthetic"(): boolean
public "getDeclaringClass"(): $Class<(any)>
public "getAnnotation"<T extends $Annotation>(arg0: $Class$Type<(T)>): T
public "getDeclaredAnnotations"(): ($Annotation)[]
public "setAccessible"(arg0: boolean): void
public "isVarArgs"(): boolean
public "getParameterCount"(): integer
public "getParameterAnnotations"(): (($Annotation)[])[]
public "getGenericParameterTypes"(): ($Type)[]
public "getGenericExceptionTypes"(): ($Type)[]
public "isDefault"(): boolean
public "getGenericReturnType"(): $Type
public "getExceptionTypes"(): ($Class<(any)>)[]
public "isBridge"(): boolean
public "getDefaultValue"(): any
public "getAnnotatedReturnType"(): $AnnotatedType
get "name"(): string
get "modifiers"(): integer
get "typeParameters"(): ($TypeVariable<($Method)>)[]
get "returnType"(): $Class<(any)>
get "parameterTypes"(): ($Class<(any)>)[]
get "synthetic"(): boolean
get "declaringClass"(): $Class<(any)>
get "declaredAnnotations"(): ($Annotation)[]
set "accessible"(value: boolean)
get "varArgs"(): boolean
get "parameterCount"(): integer
get "parameterAnnotations"(): (($Annotation)[])[]
get "genericParameterTypes"(): ($Type)[]
get "genericExceptionTypes"(): ($Type)[]
get "default"(): boolean
get "genericReturnType"(): $Type
get "exceptionTypes"(): ($Class<(any)>)[]
get "bridge"(): boolean
get "defaultValue"(): any
get "annotatedReturnType"(): $AnnotatedType
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
declare module "packages/java/lang/$LinkageError" {
import {$Throwable, $Throwable$Type} from "packages/java/lang/$Throwable"
import {$Error, $Error$Type} from "packages/java/lang/$Error"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $LinkageError extends $Error {

constructor()
constructor(arg0: string)
constructor(arg0: string, arg1: $Throwable$Type)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LinkageError$Type = ($LinkageError);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LinkageError_ = $LinkageError$Type;
}}
declare module "packages/java/lang/instrument/$Instrumentation" {
import {$Module, $Module$Type} from "packages/java/lang/$Module"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$ClassLoader, $ClassLoader$Type} from "packages/java/lang/$ClassLoader"
import {$List, $List$Type} from "packages/java/util/$List"
import {$JarFile, $JarFile$Type} from "packages/java/util/jar/$JarFile"
import {$ClassFileTransformer, $ClassFileTransformer$Type} from "packages/java/lang/instrument/$ClassFileTransformer"
import {$ClassDefinition, $ClassDefinition$Type} from "packages/java/lang/instrument/$ClassDefinition"
import {$Map, $Map$Type} from "packages/java/util/$Map"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export interface $Instrumentation {

 "addTransformer"(arg0: $ClassFileTransformer$Type, arg1: boolean): void
 "addTransformer"(arg0: $ClassFileTransformer$Type): void
 "retransformClasses"(...arg0: ($Class$Type<(any)>)[]): void
 "removeTransformer"(arg0: $ClassFileTransformer$Type): boolean
 "isRetransformClassesSupported"(): boolean
 "isRedefineClassesSupported"(): boolean
 "redefineClasses"(...arg0: ($ClassDefinition$Type)[]): void
 "isModifiableClass"(arg0: $Class$Type<(any)>): boolean
 "getAllLoadedClasses"(): ($Class<(any)>)[]
 "getInitiatedClasses"(arg0: $ClassLoader$Type): ($Class<(any)>)[]
 "getObjectSize"(arg0: any): long
 "appendToBootstrapClassLoaderSearch"(arg0: $JarFile$Type): void
 "appendToSystemClassLoaderSearch"(arg0: $JarFile$Type): void
 "isNativeMethodPrefixSupported"(): boolean
 "setNativeMethodPrefix"(arg0: $ClassFileTransformer$Type, arg1: string): void
 "redefineModule"(arg0: $Module$Type, arg1: $Set$Type<($Module$Type)>, arg2: $Map$Type<(string), ($Set$Type<($Module$Type)>)>, arg3: $Map$Type<(string), ($Set$Type<($Module$Type)>)>, arg4: $Set$Type<($Class$Type<(any)>)>, arg5: $Map$Type<($Class$Type<(any)>), ($List$Type<($Class$Type<(any)>)>)>): void
 "isModifiableModule"(arg0: $Module$Type): boolean
}

export namespace $Instrumentation {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Instrumentation$Type = ($Instrumentation);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Instrumentation_ = $Instrumentation$Type;
}}
declare module "packages/java/lang/reflect/$AnnotatedArrayType" {
import {$Annotation, $Annotation$Type} from "packages/java/lang/annotation/$Annotation"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$Type, $Type$Type} from "packages/java/lang/reflect/$Type"
import {$AnnotatedType, $AnnotatedType$Type} from "packages/java/lang/reflect/$AnnotatedType"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export interface $AnnotatedArrayType extends $AnnotatedType {

 "getAnnotatedOwnerType"(): $AnnotatedType
 "getAnnotatedGenericComponentType"(): $AnnotatedType
 "getAnnotation"<T extends $Annotation>(arg0: $Class$Type<(T)>): T
 "getAnnotations"(): ($Annotation)[]
 "getDeclaredAnnotations"(): ($Annotation)[]
 "getType"(): $Type
 "isAnnotationPresent"(arg0: $Class$Type<(any)>): boolean
 "getAnnotationsByType"<T extends $Annotation>(arg0: $Class$Type<(T)>): (T)[]
 "getDeclaredAnnotation"<T extends $Annotation>(arg0: $Class$Type<(T)>): T
 "getDeclaredAnnotationsByType"<T extends $Annotation>(arg0: $Class$Type<(T)>): (T)[]
}

export namespace $AnnotatedArrayType {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AnnotatedArrayType$Type = ($AnnotatedArrayType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AnnotatedArrayType_ = $AnnotatedArrayType$Type;
}}
declare module "packages/java/lang/management/$MemoryUsage" {
import {$CompositeData, $CompositeData$Type} from "packages/javax/management/openmbean/$CompositeData"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $MemoryUsage {

constructor(arg0: long, arg1: long, arg2: long, arg3: long)

public "toString"(): string
public static "from"(arg0: $CompositeData$Type): $MemoryUsage
public "getMax"(): long
public "getCommitted"(): long
public "getUsed"(): long
public "getInit"(): long
get "max"(): long
get "committed"(): long
get "used"(): long
get "init"(): long
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MemoryUsage$Type = ($MemoryUsage);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MemoryUsage_ = $MemoryUsage$Type;
}}
declare module "packages/java/lang/$IllegalStateException" {
import {$Throwable, $Throwable$Type} from "packages/java/lang/$Throwable"
import {$RuntimeException, $RuntimeException$Type} from "packages/java/lang/$RuntimeException"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $IllegalStateException extends $RuntimeException {

constructor(arg0: $Throwable$Type)
constructor(arg0: string, arg1: $Throwable$Type)
constructor(arg0: string)
constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IllegalStateException$Type = ($IllegalStateException);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IllegalStateException_ = $IllegalStateException$Type;
}}
declare module "packages/java/lang/$StringBuilder" {
import {$Comparable, $Comparable$Type} from "packages/java/lang/$Comparable"
import {$Serializable, $Serializable$Type} from "packages/java/io/$Serializable"
import {$AbstractStringBuilder, $AbstractStringBuilder$Type} from "packages/java/lang/$AbstractStringBuilder"

export class $StringBuilder extends $AbstractStringBuilder implements $Serializable, $Comparable<($StringBuilder)>, charseq {

constructor(arg0: charseq)
constructor(arg0: string)
constructor(arg0: integer)
constructor()

public "toString"(): string
public "compareTo"(arg0: $StringBuilder$Type): integer
public "indexOf"(arg0: string): integer
public "indexOf"(arg0: string, arg1: integer): integer
public "insert"(arg0: integer, arg1: long): $StringBuilder
public "insert"(arg0: integer, arg1: integer): $StringBuilder
public "insert"(arg0: integer, arg1: charseq, arg2: integer, arg3: integer): $StringBuilder
public "insert"(arg0: integer, arg1: charseq): $StringBuilder
public "insert"(arg0: integer, arg1: (character)[]): $StringBuilder
public "insert"(arg0: integer, arg1: boolean): $StringBuilder
public "insert"(arg0: integer, arg1: character): $StringBuilder
public "lastIndexOf"(arg0: string, arg1: integer): integer
public "lastIndexOf"(arg0: string): integer
public "appendCodePoint"(arg0: integer): $StringBuilder
public "reverse"(): $StringBuilder
public static "compare"(arg0: charseq, arg1: charseq): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StringBuilder$Type = ($StringBuilder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StringBuilder_ = $StringBuilder$Type;
}}
declare module "packages/java/lang/management/$LockInfo" {
import {$CompositeData, $CompositeData$Type} from "packages/javax/management/openmbean/$CompositeData"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $LockInfo {

constructor(arg0: string, arg1: integer)

public "toString"(): string
public static "from"(arg0: $CompositeData$Type): $LockInfo
public "getClassName"(): string
public "getIdentityHashCode"(): integer
get "className"(): string
get "identityHashCode"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LockInfo$Type = ($LockInfo);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LockInfo_ = $LockInfo$Type;
}}
declare module "packages/java/lang/$Record" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $Record {


public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Record$Type = ($Record);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Record_ = $Record$Type;
}}
declare module "packages/java/lang/invoke/$MethodHandle" {
import {$MethodType, $MethodType$Type} from "packages/java/lang/invoke/$MethodType"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$Constable, $Constable$Type} from "packages/java/lang/constant/$Constable"
import {$MethodHandleDesc, $MethodHandleDesc$Type} from "packages/java/lang/constant/$MethodHandleDesc"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $MethodHandle implements $Constable {


public "invoke"(...arg0: (any)[]): any
public "invokeExact"(...arg0: (any)[]): any
public "type"(): $MethodType
public "toString"(): string
public "describeConstable"(): $Optional<($MethodHandleDesc)>
public "asType"(arg0: $MethodType$Type): $MethodHandle
public "invokeWithArguments"(...arg0: (any)[]): any
public "invokeWithArguments"(arg0: $List$Type<(any)>): any
public "asSpreader"(arg0: integer, arg1: $Class$Type<(any)>, arg2: integer): $MethodHandle
public "asSpreader"(arg0: $Class$Type<(any)>, arg1: integer): $MethodHandle
public "isVarargsCollector"(): boolean
public "asVarargsCollector"(arg0: $Class$Type<(any)>): $MethodHandle
public "asCollector"(arg0: $Class$Type<(any)>, arg1: integer): $MethodHandle
public "asCollector"(arg0: integer, arg1: $Class$Type<(any)>, arg2: integer): $MethodHandle
public "withVarargs"(arg0: boolean): $MethodHandle
public "asFixedArity"(): $MethodHandle
public "bindTo"(arg0: any): $MethodHandle
get "varargsCollector"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MethodHandle$Type = ($MethodHandle);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MethodHandle_ = $MethodHandle$Type;
}}
declare module "packages/java/lang/$ClassCastException" {
import {$RuntimeException, $RuntimeException$Type} from "packages/java/lang/$RuntimeException"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $ClassCastException extends $RuntimeException {

constructor()
constructor(arg0: string)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClassCastException$Type = ($ClassCastException);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClassCastException_ = $ClassCastException$Type;
}}
declare module "packages/java/lang/$Appendable" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $Appendable {

 "append"(arg0: charseq): $Appendable
 "append"(arg0: charseq, arg1: integer, arg2: integer): $Appendable
 "append"(arg0: character): $Appendable
}

export namespace $Appendable {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Appendable$Type = ($Appendable);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Appendable_ = $Appendable$Type;
}}
declare module "packages/java/lang/constant/$DynamicConstantDesc" {
import {$MethodHandles$Lookup, $MethodHandles$Lookup$Type} from "packages/java/lang/invoke/$MethodHandles$Lookup"
import {$ClassDesc, $ClassDesc$Type} from "packages/java/lang/constant/$ClassDesc"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ConstantDesc, $ConstantDesc$Type} from "packages/java/lang/constant/$ConstantDesc"
import {$DirectMethodHandleDesc, $DirectMethodHandleDesc$Type} from "packages/java/lang/constant/$DirectMethodHandleDesc"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $DynamicConstantDesc<T> implements $ConstantDesc {


public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public static "of"<T>(arg0: $DirectMethodHandleDesc$Type, ...arg1: ($ConstantDesc$Type)[]): $DynamicConstantDesc<(T)>
public static "of"<T>(arg0: $DirectMethodHandleDesc$Type): $DynamicConstantDesc<(T)>
public "resolveConstantDesc"(arg0: $MethodHandles$Lookup$Type): T
public "bootstrapMethod"(): $DirectMethodHandleDesc
public static "ofNamed"<T>(arg0: $DirectMethodHandleDesc$Type, arg1: string, arg2: $ClassDesc$Type, ...arg3: ($ConstantDesc$Type)[]): $DynamicConstantDesc<(T)>
public "constantName"(): string
public "constantType"(): $ClassDesc
public "bootstrapArgs"(): ($ConstantDesc)[]
public static "ofCanonical"<T>(arg0: $DirectMethodHandleDesc$Type, arg1: string, arg2: $ClassDesc$Type, arg3: ($ConstantDesc$Type)[]): $ConstantDesc
public "bootstrapArgsList"(): $List<($ConstantDesc)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DynamicConstantDesc$Type<T> = ($DynamicConstantDesc<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DynamicConstantDesc_<T> = $DynamicConstantDesc$Type<(T)>;
}}
declare module "packages/java/lang/module/$ModuleDescriptor$Exports" {
import {$Comparable, $Comparable$Type} from "packages/java/lang/$Comparable"
import {$ModuleDescriptor$Exports$Modifier, $ModuleDescriptor$Exports$Modifier$Type} from "packages/java/lang/module/$ModuleDescriptor$Exports$Modifier"
import {$Set, $Set$Type} from "packages/java/util/$Set"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $ModuleDescriptor$Exports implements $Comparable<($ModuleDescriptor$Exports)> {


public "modifiers"(): $Set<($ModuleDescriptor$Exports$Modifier)>
public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "compareTo"(arg0: $ModuleDescriptor$Exports$Type): integer
public "source"(): string
public "isQualified"(): boolean
public "targets"(): $Set<(string)>
get "qualified"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModuleDescriptor$Exports$Type = ($ModuleDescriptor$Exports);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModuleDescriptor$Exports_ = $ModuleDescriptor$Exports$Type;
}}
declare module "packages/java/lang/management/$MemoryManagerMXBean" {
import {$PlatformManagedObject, $PlatformManagedObject$Type} from "packages/java/lang/management/$PlatformManagedObject"
import {$ObjectName, $ObjectName$Type} from "packages/javax/management/$ObjectName"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export interface $MemoryManagerMXBean extends $PlatformManagedObject {

 "getName"(): string
 "isValid"(): boolean
 "getMemoryPoolNames"(): (string)[]
 "getObjectName"(): $ObjectName
}

export namespace $MemoryManagerMXBean {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MemoryManagerMXBean$Type = ($MemoryManagerMXBean);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MemoryManagerMXBean_ = $MemoryManagerMXBean$Type;
}}
declare module "packages/java/lang/reflect/$AnnotatedType" {
import {$Annotation, $Annotation$Type} from "packages/java/lang/annotation/$Annotation"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$Type, $Type$Type} from "packages/java/lang/reflect/$Type"
import {$AnnotatedElement, $AnnotatedElement$Type} from "packages/java/lang/reflect/$AnnotatedElement"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export interface $AnnotatedType extends $AnnotatedElement {

 "getAnnotation"<T extends $Annotation>(arg0: $Class$Type<(T)>): T
 "getAnnotations"(): ($Annotation)[]
 "getDeclaredAnnotations"(): ($Annotation)[]
 "getType"(): $Type
 "getAnnotatedOwnerType"(): $AnnotatedType
 "isAnnotationPresent"(arg0: $Class$Type<(any)>): boolean
 "getAnnotationsByType"<T extends $Annotation>(arg0: $Class$Type<(T)>): (T)[]
 "getDeclaredAnnotation"<T extends $Annotation>(arg0: $Class$Type<(T)>): T
 "getDeclaredAnnotationsByType"<T extends $Annotation>(arg0: $Class$Type<(T)>): (T)[]
}

export namespace $AnnotatedType {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AnnotatedType$Type = ($AnnotatedType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AnnotatedType_ = $AnnotatedType$Type;
}}
declare module "packages/java/lang/reflect/$ParameterizedType" {
import {$Type, $Type$Type} from "packages/java/lang/reflect/$Type"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export interface $ParameterizedType extends $Type {

 "getRawType"(): $Type
 "getActualTypeArguments"(): ($Type)[]
 "getOwnerType"(): $Type
 "getTypeName"(): string
}

export namespace $ParameterizedType {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ParameterizedType$Type = ($ParameterizedType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ParameterizedType_ = $ParameterizedType$Type;
}}
declare module "packages/java/lang/invoke/$TypeDescriptor" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export interface $TypeDescriptor {

 "descriptorString"(): string

(): string
}

export namespace $TypeDescriptor {
const probejs$$marker: never
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
declare module "packages/java/lang/management/$GarbageCollectorMXBean" {
import {$MemoryManagerMXBean, $MemoryManagerMXBean$Type} from "packages/java/lang/management/$MemoryManagerMXBean"
import {$ObjectName, $ObjectName$Type} from "packages/javax/management/$ObjectName"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export interface $GarbageCollectorMXBean extends $MemoryManagerMXBean {

 "getCollectionCount"(): long
 "getCollectionTime"(): long
 "getName"(): string
 "isValid"(): boolean
 "getMemoryPoolNames"(): (string)[]
 "getObjectName"(): $ObjectName
}

export namespace $GarbageCollectorMXBean {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GarbageCollectorMXBean$Type = ($GarbageCollectorMXBean);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GarbageCollectorMXBean_ = $GarbageCollectorMXBean$Type;
}}
declare module "packages/java/lang/module/$ModuleDescriptor$Builder" {
import {$ModuleDescriptor$Version, $ModuleDescriptor$Version$Type} from "packages/java/lang/module/$ModuleDescriptor$Version"
import {$ModuleDescriptor$Provides, $ModuleDescriptor$Provides$Type} from "packages/java/lang/module/$ModuleDescriptor$Provides"
import {$ModuleDescriptor, $ModuleDescriptor$Type} from "packages/java/lang/module/$ModuleDescriptor"
import {$ModuleDescriptor$Exports$Modifier, $ModuleDescriptor$Exports$Modifier$Type} from "packages/java/lang/module/$ModuleDescriptor$Exports$Modifier"
import {$ModuleDescriptor$Requires, $ModuleDescriptor$Requires$Type} from "packages/java/lang/module/$ModuleDescriptor$Requires"
import {$ModuleDescriptor$Exports, $ModuleDescriptor$Exports$Type} from "packages/java/lang/module/$ModuleDescriptor$Exports"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$ModuleDescriptor$Opens$Modifier, $ModuleDescriptor$Opens$Modifier$Type} from "packages/java/lang/module/$ModuleDescriptor$Opens$Modifier"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ModuleDescriptor$Requires$Modifier, $ModuleDescriptor$Requires$Modifier$Type} from "packages/java/lang/module/$ModuleDescriptor$Requires$Modifier"
import {$ModuleDescriptor$Opens, $ModuleDescriptor$Opens$Type} from "packages/java/lang/module/$ModuleDescriptor$Opens"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $ModuleDescriptor$Builder {


public "version"(arg0: string): $ModuleDescriptor$Builder
public "version"(arg0: $ModuleDescriptor$Version$Type): $ModuleDescriptor$Builder
public "packages"(arg0: $Set$Type<(string)>): $ModuleDescriptor$Builder
public "exports"(arg0: string): $ModuleDescriptor$Builder
public "exports"(arg0: $Set$Type<($ModuleDescriptor$Exports$Modifier$Type)>, arg1: string, arg2: $Set$Type<(string)>): $ModuleDescriptor$Builder
public "exports"(arg0: string, arg1: $Set$Type<(string)>): $ModuleDescriptor$Builder
public "exports"(arg0: $Set$Type<($ModuleDescriptor$Exports$Modifier$Type)>, arg1: string): $ModuleDescriptor$Builder
public "exports"(arg0: $ModuleDescriptor$Exports$Type): $ModuleDescriptor$Builder
public "opens"(arg0: string): $ModuleDescriptor$Builder
public "opens"(arg0: $Set$Type<($ModuleDescriptor$Opens$Modifier$Type)>, arg1: string, arg2: $Set$Type<(string)>): $ModuleDescriptor$Builder
public "opens"(arg0: string, arg1: $Set$Type<(string)>): $ModuleDescriptor$Builder
public "opens"(arg0: $ModuleDescriptor$Opens$Type): $ModuleDescriptor$Builder
public "opens"(arg0: $Set$Type<($ModuleDescriptor$Opens$Modifier$Type)>, arg1: string): $ModuleDescriptor$Builder
public "uses"(arg0: string): $ModuleDescriptor$Builder
public "provides"(arg0: $ModuleDescriptor$Provides$Type): $ModuleDescriptor$Builder
public "provides"(arg0: string, arg1: $List$Type<(string)>): $ModuleDescriptor$Builder
public "build"(): $ModuleDescriptor
public "requires"(arg0: $ModuleDescriptor$Requires$Type): $ModuleDescriptor$Builder
public "requires"(arg0: $Set$Type<($ModuleDescriptor$Requires$Modifier$Type)>, arg1: string, arg2: $ModuleDescriptor$Version$Type): $ModuleDescriptor$Builder
public "requires"(arg0: string): $ModuleDescriptor$Builder
public "requires"(arg0: $Set$Type<($ModuleDescriptor$Requires$Modifier$Type)>, arg1: string): $ModuleDescriptor$Builder
public "mainClass"(arg0: string): $ModuleDescriptor$Builder
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModuleDescriptor$Builder$Type = ($ModuleDescriptor$Builder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModuleDescriptor$Builder_ = $ModuleDescriptor$Builder$Type;
}}
declare module "packages/java/lang/constant/$MethodTypeDesc" {
import {$MethodHandles$Lookup, $MethodHandles$Lookup$Type} from "packages/java/lang/invoke/$MethodHandles$Lookup"
import {$ClassDesc, $ClassDesc$Type} from "packages/java/lang/constant/$ClassDesc"
import {$List, $List$Type} from "packages/java/util/$List"
import {$TypeDescriptor$OfMethod, $TypeDescriptor$OfMethod$Type} from "packages/java/lang/invoke/$TypeDescriptor$OfMethod"
import {$ConstantDesc, $ConstantDesc$Type} from "packages/java/lang/constant/$ConstantDesc"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export interface $MethodTypeDesc extends $ConstantDesc, $TypeDescriptor$OfMethod<($ClassDesc), ($MethodTypeDesc)> {

 "returnType"(): $ClassDesc
 "equals"(arg0: any): boolean
 "descriptorString"(): string
 "insertParameterTypes"(arg0: integer, ...arg1: ($ClassDesc$Type)[]): $MethodTypeDesc
 "changeReturnType"(arg0: $ClassDesc$Type): $MethodTypeDesc
 "parameterCount"(): integer
 "parameterList"(): $List<($ClassDesc)>
 "parameterArray"(): ($ClassDesc)[]
 "changeParameterType"(arg0: integer, arg1: $ClassDesc$Type): $MethodTypeDesc
 "displayDescriptor"(): string
 "resolveConstantDesc"(arg0: $MethodHandles$Lookup$Type): any
}

export namespace $MethodTypeDesc {
function of(arg0: $ClassDesc$Type, ...arg1: ($ClassDesc$Type)[]): $MethodTypeDesc
function ofDescriptor(arg0: string): $MethodTypeDesc
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MethodTypeDesc$Type = ($MethodTypeDesc);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MethodTypeDesc_ = $MethodTypeDesc$Type;
}}
declare module "packages/java/lang/ref/$Cleaner$Cleanable" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export interface $Cleaner$Cleanable {

 "clean"(): void

(): void
}

export namespace $Cleaner$Cleanable {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Cleaner$Cleanable$Type = ($Cleaner$Cleanable);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Cleaner$Cleanable_ = $Cleaner$Cleanable$Type;
}}
declare module "packages/java/lang/$Thread" {
import {$StackTraceElement, $StackTraceElement$Type} from "packages/java/lang/$StackTraceElement"
import {$Thread$State, $Thread$State$Type} from "packages/java/lang/$Thread$State"
import {$ClassLoader, $ClassLoader$Type} from "packages/java/lang/$ClassLoader"
import {$Thread$UncaughtExceptionHandler, $Thread$UncaughtExceptionHandler$Type} from "packages/java/lang/$Thread$UncaughtExceptionHandler"
import {$Runnable, $Runnable$Type} from "packages/java/lang/$Runnable"
import {$ThreadGroup, $ThreadGroup$Type} from "packages/java/lang/$ThreadGroup"
import {$Map, $Map$Type} from "packages/java/util/$Map"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $Thread implements $Runnable {
static readonly "MIN_PRIORITY": integer
static readonly "NORM_PRIORITY": integer
static readonly "MAX_PRIORITY": integer

constructor(arg0: $ThreadGroup$Type, arg1: $Runnable$Type)
constructor(arg0: string)
constructor(arg0: $Runnable$Type)
constructor()
constructor(arg0: $ThreadGroup$Type, arg1: $Runnable$Type, arg2: string, arg3: long, arg4: boolean)
constructor(arg0: $ThreadGroup$Type, arg1: $Runnable$Type, arg2: string, arg3: long)
constructor(arg0: $ThreadGroup$Type, arg1: $Runnable$Type, arg2: string)
constructor(arg0: $Runnable$Type, arg1: string)
constructor(arg0: $ThreadGroup$Type, arg1: string)

public "getName"(): string
public "getStackTrace"(): ($StackTraceElement)[]
public "run"(): void
public "toString"(): string
public static "currentThread"(): $Thread
public static "onSpinWait"(): void
public "join"(arg0: long): void
public "join"(): void
public "join"(arg0: long, arg1: integer): void
public "start"(): void
public "getThreadGroup"(): $ThreadGroup
public "setContextClassLoader"(arg0: $ClassLoader$Type): void
public static "holdsLock"(arg0: any): boolean
/**
 * 
 * @deprecated
 */
public "checkAccess"(): void
public static "dumpStack"(): void
public "setPriority"(arg0: integer): void
public "setDaemon"(arg0: boolean): void
public static "sleep"(arg0: long, arg1: integer): void
public static "sleep"(arg0: long): void
public "isDaemon"(): boolean
public "getPriority"(): integer
public "getContextClassLoader"(): $ClassLoader
/**
 * 
 * @deprecated
 */
public "resume"(): void
public static "interrupted"(): boolean
public "interrupt"(): void
public static "activeCount"(): integer
public static "enumerate"(arg0: ($Thread$Type)[]): integer
public "isAlive"(): boolean
public static "setDefaultUncaughtExceptionHandler"(arg0: $Thread$UncaughtExceptionHandler$Type): void
public "getUncaughtExceptionHandler"(): $Thread$UncaughtExceptionHandler
public static "yield"(): void
/**
 * 
 * @deprecated
 */
public "stop"(): void
public "isInterrupted"(): boolean
/**
 * 
 * @deprecated
 */
public "suspend"(): void
public "setName"(arg0: string): void
/**
 * 
 * @deprecated
 */
public "countStackFrames"(): integer
public static "getAllStackTraces"(): $Map<($Thread), (($StackTraceElement)[])>
public "getId"(): long
public "getState"(): $Thread$State
public static "getDefaultUncaughtExceptionHandler"(): $Thread$UncaughtExceptionHandler
public "setUncaughtExceptionHandler"(arg0: $Thread$UncaughtExceptionHandler$Type): void
get "name"(): string
get "stackTrace"(): ($StackTraceElement)[]
get "threadGroup"(): $ThreadGroup
set "contextClassLoader"(value: $ClassLoader$Type)
set "priority"(value: integer)
set "daemon"(value: boolean)
get "daemon"(): boolean
get "priority"(): integer
get "contextClassLoader"(): $ClassLoader
get "alive"(): boolean
set "defaultUncaughtExceptionHandler"(value: $Thread$UncaughtExceptionHandler$Type)
get "uncaughtExceptionHandler"(): $Thread$UncaughtExceptionHandler
set "name"(value: string)
get "allStackTraces"(): $Map<($Thread), (($StackTraceElement)[])>
get "id"(): long
get "state"(): $Thread$State
get "defaultUncaughtExceptionHandler"(): $Thread$UncaughtExceptionHandler
set "uncaughtExceptionHandler"(value: $Thread$UncaughtExceptionHandler$Type)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Thread$Type = ($Thread);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Thread_ = $Thread$Type;
}}
declare module "packages/java/lang/module/$ModuleReference" {
import {$ModuleDescriptor, $ModuleDescriptor$Type} from "packages/java/lang/module/$ModuleDescriptor"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$URI, $URI$Type} from "packages/java/net/$URI"
import {$ModuleReader, $ModuleReader$Type} from "packages/java/lang/module/$ModuleReader"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $ModuleReference {


public "descriptor"(): $ModuleDescriptor
public "location"(): $Optional<($URI)>
public "open"(): $ModuleReader
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModuleReference$Type = ($ModuleReference);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModuleReference_ = $ModuleReference$Type;
}}
declare module "packages/java/lang/$Enum$EnumDesc" {
import {$DynamicConstantDesc, $DynamicConstantDesc$Type} from "packages/java/lang/constant/$DynamicConstantDesc"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$ClassDesc, $ClassDesc$Type} from "packages/java/lang/constant/$ClassDesc"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $Enum$EnumDesc<E extends $Enum<(E)>> extends $DynamicConstantDesc<(E)> {


public "toString"(): string
public static "of"<E extends $Enum<(E)>>(arg0: $ClassDesc$Type, arg1: string): $Enum$EnumDesc<(E)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Enum$EnumDesc$Type<E> = ($Enum$EnumDesc<(E)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Enum$EnumDesc_<E> = $Enum$EnumDesc$Type<(E)>;
}}
declare module "packages/java/lang/constant/$ConstantDesc" {
import {$MethodHandles$Lookup, $MethodHandles$Lookup$Type} from "packages/java/lang/invoke/$MethodHandles$Lookup"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export interface $ConstantDesc {

 "resolveConstantDesc"(arg0: $MethodHandles$Lookup$Type): any

(arg0: $MethodHandles$Lookup$Type): any
}

export namespace $ConstantDesc {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ConstantDesc$Type = ($ConstantDesc);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ConstantDesc_ = $ConstantDesc$Type;
}}
declare module "packages/java/lang/$Enum" {
import {$Comparable, $Comparable$Type} from "packages/java/lang/$Comparable"
import {$Enum$EnumDesc, $Enum$EnumDesc$Type} from "packages/java/lang/$Enum$EnumDesc"
import {$Serializable, $Serializable$Type} from "packages/java/io/$Serializable"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$Constable, $Constable$Type} from "packages/java/lang/constant/$Constable"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $Enum<E extends $Enum<(E)>> implements $Constable, $Comparable<(E)>, $Serializable {


public "name"(): string
public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "compareTo"(arg0: E): integer
public static "valueOf"<T extends $Enum<(T)>>(arg0: $Class$Type<(T)>, arg1: string): T
public "describeConstable"(): $Optional<($Enum$EnumDesc<(E)>)>
public "getDeclaringClass"(): $Class<(E)>
public "ordinal"(): integer
get "declaringClass"(): $Class<(E)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Enum$Type<E> = ($Enum<(E)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Enum_<E> = $Enum$Type<(E)>;
}}
declare module "packages/java/lang/invoke/$MethodHandles$Lookup" {
import {$VarHandle, $VarHandle$Type} from "packages/java/lang/invoke/$VarHandle"
import {$MethodHandle, $MethodHandle$Type} from "packages/java/lang/invoke/$MethodHandle"
import {$MethodHandles$Lookup$ClassOption, $MethodHandles$Lookup$ClassOption$Type} from "packages/java/lang/invoke/$MethodHandles$Lookup$ClassOption"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$MethodType, $MethodType$Type} from "packages/java/lang/invoke/$MethodType"
import {$Field, $Field$Type} from "packages/java/lang/reflect/$Field"
import {$Method, $Method$Type} from "packages/java/lang/reflect/$Method"
import {$Constructor, $Constructor$Type} from "packages/java/lang/reflect/$Constructor"
import {$MethodHandleInfo, $MethodHandleInfo$Type} from "packages/java/lang/invoke/$MethodHandleInfo"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $MethodHandles$Lookup {
static readonly "PUBLIC": integer
static readonly "PRIVATE": integer
static readonly "PROTECTED": integer
static readonly "PACKAGE": integer
static readonly "MODULE": integer
static readonly "UNCONDITIONAL": integer
static readonly "ORIGINAL": integer


public "toString"(): string
public "findClass"(arg0: string): $Class<(any)>
public "defineClass"(arg0: (byte)[]): $Class<(any)>
public "in"(arg0: $Class$Type<(any)>): $MethodHandles$Lookup
public "ensureInitialized"(arg0: $Class$Type<(any)>): $Class<(any)>
public "revealDirect"(arg0: $MethodHandle$Type): $MethodHandleInfo
public "lookupClass"(): $Class<(any)>
public "previousLookupClass"(): $Class<(any)>
public "findVirtual"(arg0: $Class$Type<(any)>, arg1: string, arg2: $MethodType$Type): $MethodHandle
public "findStatic"(arg0: $Class$Type<(any)>, arg1: string, arg2: $MethodType$Type): $MethodHandle
public "lookupModes"(): integer
public "hasFullPrivilegeAccess"(): boolean
public "accessClass"(arg0: $Class$Type<(any)>): $Class<(any)>
public "dropLookupMode"(arg0: integer): $MethodHandles$Lookup
public "defineHiddenClass"(arg0: (byte)[], arg1: boolean, ...arg2: ($MethodHandles$Lookup$ClassOption$Type)[]): $MethodHandles$Lookup
public "defineHiddenClassWithClassData"(arg0: (byte)[], arg1: any, arg2: boolean, ...arg3: ($MethodHandles$Lookup$ClassOption$Type)[]): $MethodHandles$Lookup
public "findConstructor"(arg0: $Class$Type<(any)>, arg1: $MethodType$Type): $MethodHandle
public "findSpecial"(arg0: $Class$Type<(any)>, arg1: string, arg2: $MethodType$Type, arg3: $Class$Type<(any)>): $MethodHandle
public "findGetter"(arg0: $Class$Type<(any)>, arg1: string, arg2: $Class$Type<(any)>): $MethodHandle
public "findSetter"(arg0: $Class$Type<(any)>, arg1: string, arg2: $Class$Type<(any)>): $MethodHandle
public "findVarHandle"(arg0: $Class$Type<(any)>, arg1: string, arg2: $Class$Type<(any)>): $VarHandle
public "findStaticGetter"(arg0: $Class$Type<(any)>, arg1: string, arg2: $Class$Type<(any)>): $MethodHandle
public "findStaticSetter"(arg0: $Class$Type<(any)>, arg1: string, arg2: $Class$Type<(any)>): $MethodHandle
public "findStaticVarHandle"(arg0: $Class$Type<(any)>, arg1: string, arg2: $Class$Type<(any)>): $VarHandle
public "bind"(arg0: any, arg1: string, arg2: $MethodType$Type): $MethodHandle
public "unreflect"(arg0: $Method$Type): $MethodHandle
public "unreflectSpecial"(arg0: $Method$Type, arg1: $Class$Type<(any)>): $MethodHandle
public "unreflectConstructor"(arg0: $Constructor$Type<(any)>): $MethodHandle
public "unreflectGetter"(arg0: $Field$Type): $MethodHandle
public "unreflectSetter"(arg0: $Field$Type): $MethodHandle
public "unreflectVarHandle"(arg0: $Field$Type): $VarHandle
/**
 * 
 * @deprecated
 */
public "hasPrivateAccess"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MethodHandles$Lookup$Type = ($MethodHandles$Lookup);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MethodHandles$Lookup_ = $MethodHandles$Lookup$Type;
}}
declare module "packages/java/lang/$ModuleLayer" {
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Module, $Module$Type} from "packages/java/lang/$Module"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$ModuleLayer$Controller, $ModuleLayer$Controller$Type} from "packages/java/lang/$ModuleLayer$Controller"
import {$Configuration, $Configuration$Type} from "packages/java/lang/module/$Configuration"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ClassLoader, $ClassLoader$Type} from "packages/java/lang/$ClassLoader"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $ModuleLayer {


public "toString"(): string
public static "empty"(): $ModuleLayer
public static "boot"(): $ModuleLayer
public "modules"(): $Set<($Module)>
public "configuration"(): $Configuration
public "parents"(): $List<($ModuleLayer)>
public "findModule"(arg0: string): $Optional<($Module)>
public "defineModules"(arg0: $Configuration$Type, arg1: $Function$Type<(string), ($ClassLoader$Type)>): $ModuleLayer
public static "defineModules"(arg0: $Configuration$Type, arg1: $List$Type<($ModuleLayer$Type)>, arg2: $Function$Type<(string), ($ClassLoader$Type)>): $ModuleLayer$Controller
public static "defineModulesWithOneLoader"(arg0: $Configuration$Type, arg1: $List$Type<($ModuleLayer$Type)>, arg2: $ClassLoader$Type): $ModuleLayer$Controller
public "defineModulesWithOneLoader"(arg0: $Configuration$Type, arg1: $ClassLoader$Type): $ModuleLayer
public static "defineModulesWithManyLoaders"(arg0: $Configuration$Type, arg1: $List$Type<($ModuleLayer$Type)>, arg2: $ClassLoader$Type): $ModuleLayer$Controller
public "defineModulesWithManyLoaders"(arg0: $Configuration$Type, arg1: $ClassLoader$Type): $ModuleLayer
public "findLoader"(arg0: string): $ClassLoader
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModuleLayer$Type = ($ModuleLayer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModuleLayer_ = $ModuleLayer$Type;
}}
declare module "packages/java/lang/invoke/$MethodHandleInfo" {
import {$Member, $Member$Type} from "packages/java/lang/reflect/$Member"
import {$MethodHandles$Lookup, $MethodHandles$Lookup$Type} from "packages/java/lang/invoke/$MethodHandles$Lookup"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$MethodType, $MethodType$Type} from "packages/java/lang/invoke/$MethodType"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export interface $MethodHandleInfo {

 "getName"(): string
 "getModifiers"(): integer
 "getDeclaringClass"(): $Class<(any)>
 "isVarArgs"(): boolean
 "getReferenceKind"(): integer
 "getMethodType"(): $MethodType
 "reflectAs"<T extends $Member>(arg0: $Class$Type<(T)>, arg1: $MethodHandles$Lookup$Type): T
}

export namespace $MethodHandleInfo {
const REF_getField: integer
const REF_getStatic: integer
const REF_putField: integer
const REF_putStatic: integer
const REF_invokeVirtual: integer
const REF_invokeStatic: integer
const REF_invokeSpecial: integer
const REF_newInvokeSpecial: integer
const REF_invokeInterface: integer
function toString(arg0: integer, arg1: $Class$Type<(any)>, arg2: string, arg3: $MethodType$Type): string
function referenceKindToString(arg0: integer): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MethodHandleInfo$Type = ($MethodHandleInfo);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MethodHandleInfo_ = $MethodHandleInfo$Type;
}}
declare module "packages/java/lang/reflect/$Type" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export interface $Type {

 "getTypeName"(): string
}

export namespace $Type {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Type$Type = ($Type);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Type_ = $Type$Type;
}}
declare module "packages/java/lang/$StringBuffer" {
import {$Comparable, $Comparable$Type} from "packages/java/lang/$Comparable"
import {$Serializable, $Serializable$Type} from "packages/java/io/$Serializable"
import {$AbstractStringBuilder, $AbstractStringBuilder$Type} from "packages/java/lang/$AbstractStringBuilder"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $StringBuffer extends $AbstractStringBuilder implements $Serializable, $Comparable<($StringBuffer)>, charseq {

constructor(arg0: charseq)
constructor(arg0: string)
constructor(arg0: integer)
constructor()

public "length"(): integer
public "toString"(): string
public "append"(arg0: integer): $StringBuffer
public "append"(arg0: long): $StringBuffer
public "append"(arg0: float): $StringBuffer
public "append"(arg0: (character)[], arg1: integer, arg2: integer): $StringBuffer
public "append"(arg0: boolean): $StringBuffer
public "append"(arg0: character): $StringBuffer
public "getChars"(arg0: integer, arg1: integer, arg2: (character)[], arg3: integer): void
public "compareTo"(arg0: $StringBuffer$Type): integer
public "indexOf"(arg0: string): integer
public "indexOf"(arg0: string, arg1: integer): integer
public "charAt"(arg0: integer): character
public "codePointAt"(arg0: integer): integer
public "codePointBefore"(arg0: integer): integer
public "codePointCount"(arg0: integer, arg1: integer): integer
public "offsetByCodePoints"(arg0: integer, arg1: integer): integer
public "lastIndexOf"(arg0: string, arg1: integer): integer
public "lastIndexOf"(arg0: string): integer
public "substring"(arg0: integer, arg1: integer): string
public "substring"(arg0: integer): string
public "subSequence"(arg0: integer, arg1: integer): charseq
public "setLength"(arg0: integer): void
public "capacity"(): integer
public "ensureCapacity"(arg0: integer): void
public "trimToSize"(): void
public "setCharAt"(arg0: integer, arg1: character): void
public "deleteCharAt"(arg0: integer): $StringBuffer
public "reverse"(): $StringBuffer
public static "compare"(arg0: charseq, arg1: charseq): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StringBuffer$Type = ($StringBuffer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StringBuffer_ = $StringBuffer$Type;
}}
declare module "packages/java/lang/$Thread$State" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $Thread$State extends $Enum<($Thread$State)> {
static readonly "NEW": $Thread$State
static readonly "RUNNABLE": $Thread$State
static readonly "BLOCKED": $Thread$State
static readonly "WAITING": $Thread$State
static readonly "TIMED_WAITING": $Thread$State
static readonly "TERMINATED": $Thread$State


public static "values"(): ($Thread$State)[]
public static "valueOf"(arg0: string): $Thread$State
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Thread$State$Type = (("new") | ("runnable") | ("blocked") | ("waiting") | ("terminated") | ("timed_waiting")) | ($Thread$State);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Thread$State_ = $Thread$State$Type;
}}
declare module "packages/java/lang/$RuntimeException" {
import {$Throwable, $Throwable$Type} from "packages/java/lang/$Throwable"
import {$Exception, $Exception$Type} from "packages/java/lang/$Exception"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $RuntimeException extends $Exception {

constructor(arg0: $Throwable$Type)
constructor(arg0: string, arg1: $Throwable$Type)
constructor(arg0: string)
constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RuntimeException$Type = ($RuntimeException);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RuntimeException_ = $RuntimeException$Type;
}}
declare module "packages/java/lang/reflect/$Field" {
import {$Annotation, $Annotation$Type} from "packages/java/lang/annotation/$Annotation"
import {$Member, $Member$Type} from "packages/java/lang/reflect/$Member"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$Type, $Type$Type} from "packages/java/lang/reflect/$Type"
import {$AnnotatedType, $AnnotatedType$Type} from "packages/java/lang/reflect/$AnnotatedType"
import {$AccessibleObject, $AccessibleObject$Type} from "packages/java/lang/reflect/$AccessibleObject"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $Field extends $AccessibleObject implements $Member {


public "getName"(): string
public "get"(arg0: any): any
public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "getModifiers"(): integer
public "getBoolean"(arg0: any): boolean
public "getByte"(arg0: any): byte
public "getShort"(arg0: any): short
public "getChar"(arg0: any): character
public "getInt"(arg0: any): integer
public "getLong"(arg0: any): long
public "getFloat"(arg0: any): float
public "getDouble"(arg0: any): double
public "toGenericString"(): string
public "isSynthetic"(): boolean
public "getDeclaringClass"(): $Class<(any)>
public "getAnnotation"<T extends $Annotation>(arg0: $Class$Type<(T)>): T
public "getAnnotationsByType"<T extends $Annotation>(arg0: $Class$Type<(T)>): (T)[]
public "getDeclaredAnnotations"(): ($Annotation)[]
public "set"(arg0: any, arg1: any): void
public "setAccessible"(arg0: boolean): void
public "getGenericType"(): $Type
public "getType"(): $Class<(any)>
public "setBoolean"(arg0: any, arg1: boolean): void
public "setByte"(arg0: any, arg1: byte): void
public "setChar"(arg0: any, arg1: character): void
public "setShort"(arg0: any, arg1: short): void
public "setInt"(arg0: any, arg1: integer): void
public "setLong"(arg0: any, arg1: long): void
public "setFloat"(arg0: any, arg1: float): void
public "setDouble"(arg0: any, arg1: double): void
public "isEnumConstant"(): boolean
public "getAnnotatedType"(): $AnnotatedType
get "name"(): string
get "modifiers"(): integer
get "synthetic"(): boolean
get "declaringClass"(): $Class<(any)>
get "declaredAnnotations"(): ($Annotation)[]
set "accessible"(value: boolean)
get "genericType"(): $Type
get "type"(): $Class<(any)>
get "enumConstant"(): boolean
get "annotatedType"(): $AnnotatedType
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
declare module "packages/java/lang/invoke/$VarHandle" {
import {$MethodHandle, $MethodHandle$Type} from "packages/java/lang/invoke/$MethodHandle"
import {$VarHandle$VarHandleDesc, $VarHandle$VarHandleDesc$Type} from "packages/java/lang/invoke/$VarHandle$VarHandleDesc"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$List, $List$Type} from "packages/java/util/$List"
import {$MethodType, $MethodType$Type} from "packages/java/lang/invoke/$MethodType"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$Constable, $Constable$Type} from "packages/java/lang/constant/$Constable"
import {$VarHandle$AccessMode, $VarHandle$AccessMode$Type} from "packages/java/lang/invoke/$VarHandle$AccessMode"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $VarHandle implements $Constable {


public "get"(...arg0: (any)[]): any
public "toString"(): string
public static "storeStoreFence"(): void
public static "fullFence"(): void
public "describeConstable"(): $Optional<($VarHandle$VarHandleDesc)>
public "set"(...arg0: (any)[]): void
public "varType"(): $Class<(any)>
public "coordinateTypes"(): $List<($Class<(any)>)>
public "accessModeType"(arg0: $VarHandle$AccessMode$Type): $MethodType
public "isAccessModeSupported"(arg0: $VarHandle$AccessMode$Type): boolean
public static "loadLoadFence"(): void
public "hasInvokeExactBehavior"(): boolean
public "getVolatile"(...arg0: (any)[]): any
public "setVolatile"(...arg0: (any)[]): void
public "getOpaque"(...arg0: (any)[]): any
public "setOpaque"(...arg0: (any)[]): void
public "getAcquire"(...arg0: (any)[]): any
public "setRelease"(...arg0: (any)[]): void
public "compareAndSet"(...arg0: (any)[]): boolean
public "compareAndExchange"(...arg0: (any)[]): any
public "compareAndExchangeAcquire"(...arg0: (any)[]): any
public "compareAndExchangeRelease"(...arg0: (any)[]): any
public "weakCompareAndSetPlain"(...arg0: (any)[]): boolean
public "weakCompareAndSet"(...arg0: (any)[]): boolean
public "weakCompareAndSetAcquire"(...arg0: (any)[]): boolean
public "weakCompareAndSetRelease"(...arg0: (any)[]): boolean
public "getAndSet"(...arg0: (any)[]): any
public "getAndSetAcquire"(...arg0: (any)[]): any
public "getAndSetRelease"(...arg0: (any)[]): any
public "getAndAdd"(...arg0: (any)[]): any
public "getAndAddAcquire"(...arg0: (any)[]): any
public "getAndAddRelease"(...arg0: (any)[]): any
public "getAndBitwiseOr"(...arg0: (any)[]): any
public "getAndBitwiseOrAcquire"(...arg0: (any)[]): any
public "getAndBitwiseOrRelease"(...arg0: (any)[]): any
public "getAndBitwiseAnd"(...arg0: (any)[]): any
public "getAndBitwiseAndAcquire"(...arg0: (any)[]): any
public "getAndBitwiseAndRelease"(...arg0: (any)[]): any
public "getAndBitwiseXor"(...arg0: (any)[]): any
public "getAndBitwiseXorAcquire"(...arg0: (any)[]): any
public "getAndBitwiseXorRelease"(...arg0: (any)[]): any
public "withInvokeExactBehavior"(): $VarHandle
public "withInvokeBehavior"(): $VarHandle
public "toMethodHandle"(arg0: $VarHandle$AccessMode$Type): $MethodHandle
public static "acquireFence"(): void
public static "releaseFence"(): void
set "volatile"(value: (any)[])
set "opaque"(value: (any)[])
set "release"(value: (any)[])
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $VarHandle$Type = ($VarHandle);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $VarHandle_ = $VarHandle$Type;
}}
declare module "packages/java/lang/$IllegalArgumentException" {
import {$Throwable, $Throwable$Type} from "packages/java/lang/$Throwable"
import {$RuntimeException, $RuntimeException$Type} from "packages/java/lang/$RuntimeException"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $IllegalArgumentException extends $RuntimeException {

constructor(arg0: $Throwable$Type)
constructor(arg0: string, arg1: $Throwable$Type)
constructor(arg0: string)
constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IllegalArgumentException$Type = ($IllegalArgumentException);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IllegalArgumentException_ = $IllegalArgumentException$Type;
}}
declare module "packages/java/lang/annotation/$Annotation" {
import {$Class, $Class$Type} from "packages/java/lang/$Class"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export interface $Annotation {

 "equals"(arg0: any): boolean
 "toString"(): string
 "hashCode"(): integer
 "annotationType"(): $Class<(any)>
}

export namespace $Annotation {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Annotation$Type = ($Annotation);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Annotation_ = $Annotation$Type;
}}
declare module "packages/java/lang/$Class" {
import {$Package, $Package$Type} from "packages/java/lang/$Package"
import {$Serializable, $Serializable$Type} from "packages/java/io/$Serializable"
import {$Module, $Module$Type} from "packages/java/lang/$Module"
import {$RecordComponent, $RecordComponent$Type} from "packages/java/lang/reflect/$RecordComponent"
import {$ClassDesc, $ClassDesc$Type} from "packages/java/lang/constant/$ClassDesc"
import {$ClassLoader, $ClassLoader$Type} from "packages/java/lang/$ClassLoader"
import {$Field, $Field$Type} from "packages/java/lang/reflect/$Field"
import {$Method, $Method$Type} from "packages/java/lang/reflect/$Method"
import {$Constructor, $Constructor$Type} from "packages/java/lang/reflect/$Constructor"
import {$ProtectionDomain, $ProtectionDomain$Type} from "packages/java/security/$ProtectionDomain"
import {$TypeVariable, $TypeVariable$Type} from "packages/java/lang/reflect/$TypeVariable"
import {$TypeDescriptor$OfField, $TypeDescriptor$OfField$Type} from "packages/java/lang/invoke/$TypeDescriptor$OfField"
import {$GenericDeclaration, $GenericDeclaration$Type} from "packages/java/lang/reflect/$GenericDeclaration"
import {$Annotation, $Annotation$Type} from "packages/java/lang/annotation/$Annotation"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$InputStream, $InputStream$Type} from "packages/java/io/$InputStream"
import {$Type, $Type$Type} from "packages/java/lang/reflect/$Type"
import {$URL, $URL$Type} from "packages/java/net/$URL"
import {$Constable, $Constable$Type} from "packages/java/lang/constant/$Constable"
import {$AnnotatedType, $AnnotatedType$Type} from "packages/java/lang/reflect/$AnnotatedType"
import {$AnnotatedElement, $AnnotatedElement$Type} from "packages/java/lang/reflect/$AnnotatedElement"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $Class<T> implements $Serializable, $GenericDeclaration, $Type, $AnnotatedElement, $TypeDescriptor$OfField<($Class<(any)>)>, $Constable {


public "getName"(): string
public static "forName"(arg0: string, arg1: boolean, arg2: $ClassLoader$Type): $Class<(any)>
public static "forName"(arg0: string): $Class<(any)>
public static "forName"(arg0: $Module$Type, arg1: string): $Class<(any)>
public "toString"(): string
public "getModule"(): $Module
public "getProtectionDomain"(): $ProtectionDomain
public "isAssignableFrom"(arg0: $Class$Type<(any)>): boolean
public "isInstance"(arg0: any): boolean
public "getModifiers"(): integer
public "isInterface"(): boolean
public "isArray"(): boolean
public "isPrimitive"(): boolean
public "isHidden"(): boolean
public "getSuperclass"(): $Class<(any)>
public "cast"(arg0: any): T
public "describeConstable"(): $Optional<($ClassDesc)>
public "getComponentType"(): $Class<(any)>
public "isAnnotation"(): boolean
public "isEnum"(): boolean
public "isRecord"(): boolean
public "getTypeParameters"(): ($TypeVariable<($Class<(T)>)>)[]
public "getClassLoader"(): $ClassLoader
/**
 * 
 * @deprecated
 */
public "newInstance"(): T
public "getInterfaces"(): ($Class<(any)>)[]
public "getEnclosingClass"(): $Class<(any)>
public "getSimpleName"(): string
public "getCanonicalName"(): string
public "getResourceAsStream"(arg0: string): $InputStream
public "getResource"(arg0: string): $URL
public "getPackageName"(): string
public "desiredAssertionStatus"(): boolean
public "getMethod"(arg0: string, ...arg1: ($Class$Type<(any)>)[]): $Method
public "isAnnotationPresent"(arg0: $Class$Type<(any)>): boolean
public "getNestHost"(): $Class<(any)>
public "descriptorString"(): string
public "getPermittedSubclasses"(): ($Class<(any)>)[]
public "toGenericString"(): string
public "isSynthetic"(): boolean
public "getGenericSuperclass"(): $Type
public "getPackage"(): $Package
public "getGenericInterfaces"(): ($Type)[]
public "getSigners"(): (any)[]
public "getEnclosingMethod"(): $Method
public "getEnclosingConstructor"(): $Constructor<(any)>
public "getDeclaringClass"(): $Class<(any)>
public "getTypeName"(): string
public "isAnonymousClass"(): boolean
public "isLocalClass"(): boolean
public "isMemberClass"(): boolean
public "getClasses"(): ($Class<(any)>)[]
public "getFields"(): ($Field)[]
public "getMethods"(): ($Method)[]
public "getConstructors"(): ($Constructor<(any)>)[]
public "getField"(arg0: string): $Field
public "getConstructor"(...arg0: ($Class$Type<(any)>)[]): $Constructor<(T)>
public "getDeclaredClasses"(): ($Class<(any)>)[]
public "getDeclaredFields"(): ($Field)[]
public "getRecordComponents"(): ($RecordComponent)[]
public "getDeclaredMethods"(): ($Method)[]
public "getDeclaredConstructors"(): ($Constructor<(any)>)[]
public "getDeclaredField"(arg0: string): $Field
public "getDeclaredMethod"(arg0: string, ...arg1: ($Class$Type<(any)>)[]): $Method
public "getDeclaredConstructor"(...arg0: ($Class$Type<(any)>)[]): $Constructor<(T)>
public "getEnumConstants"(): (T)[]
public "asSubclass"<U>(arg0: $Class$Type<(U)>): $Class<(any)>
public "getAnnotation"<A extends $Annotation>(arg0: $Class$Type<(A)>): A
public "getAnnotationsByType"<A extends $Annotation>(arg0: $Class$Type<(A)>): (A)[]
public "getAnnotations"(): ($Annotation)[]
public "getDeclaredAnnotation"<A extends $Annotation>(arg0: $Class$Type<(A)>): A
public "getDeclaredAnnotationsByType"<A extends $Annotation>(arg0: $Class$Type<(A)>): (A)[]
public "getDeclaredAnnotations"(): ($Annotation)[]
public "getAnnotatedSuperclass"(): $AnnotatedType
public "getAnnotatedInterfaces"(): ($AnnotatedType)[]
public "isNestmateOf"(arg0: $Class$Type<(any)>): boolean
public "getNestMembers"(): ($Class<(any)>)[]
public "isSealed"(): boolean
get "name"(): string
get "module"(): $Module
get "protectionDomain"(): $ProtectionDomain
get "modifiers"(): integer
get "interface"(): boolean
get "array"(): boolean
get "primitive"(): boolean
get "hidden"(): boolean
get "superclass"(): $Class<(any)>
get "componentType"(): $Class<(any)>
get "annotation"(): boolean
get "enum"(): boolean
get "record"(): boolean
get "typeParameters"(): ($TypeVariable<($Class<(T)>)>)[]
get "classLoader"(): $ClassLoader
get "interfaces"(): ($Class<(any)>)[]
get "enclosingClass"(): $Class<(any)>
get "simpleName"(): string
get "canonicalName"(): string
get "packageName"(): string
get "nestHost"(): $Class<(any)>
get "permittedSubclasses"(): ($Class<(any)>)[]
get "synthetic"(): boolean
get "genericSuperclass"(): $Type
get "package"(): $Package
get "genericInterfaces"(): ($Type)[]
get "signers"(): (any)[]
get "enclosingMethod"(): $Method
get "enclosingConstructor"(): $Constructor<(any)>
get "declaringClass"(): $Class<(any)>
get "typeName"(): string
get "anonymousClass"(): boolean
get "localClass"(): boolean
get "memberClass"(): boolean
get "classes"(): ($Class<(any)>)[]
get "fields"(): ($Field)[]
get "methods"(): ($Method)[]
get "constructors"(): ($Constructor<(any)>)[]
get "declaredClasses"(): ($Class<(any)>)[]
get "declaredFields"(): ($Field)[]
get "recordComponents"(): ($RecordComponent)[]
get "declaredMethods"(): ($Method)[]
get "declaredConstructors"(): ($Constructor<(any)>)[]
get "enumConstants"(): (T)[]
get "annotations"(): ($Annotation)[]
get "declaredAnnotations"(): ($Annotation)[]
get "annotatedSuperclass"(): $AnnotatedType
get "annotatedInterfaces"(): ($AnnotatedType)[]
get "nestMembers"(): ($Class<(any)>)[]
get "sealed"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Class$Type<T> = ($Class<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Class_<T> = $Class$Type<(T)>;
}}
declare module "packages/java/lang/ref/$Reference" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $Reference<T> {


public "get"(): T
public "clear"(): void
public static "reachabilityFence"(arg0: any): void
public "enqueue"(): boolean
public "refersTo"(arg0: T): boolean
/**
 * 
 * @deprecated
 */
public "isEnqueued"(): boolean
get "enqueued"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Reference$Type<T> = ($Reference<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Reference_<T> = $Reference$Type<(T)>;
}}

declare module "packages/jdk/jfr/consumer/$RecordedMethod" {
import {$RecordedClass, $RecordedClass$Type} from "packages/jdk/jfr/consumer/$RecordedClass"
import {$RecordedObject, $RecordedObject$Type} from "packages/jdk/jfr/consumer/$RecordedObject"

export class $RecordedMethod extends $RecordedObject {


public "getName"(): string
public "getModifiers"(): integer
public "isHidden"(): boolean
public "getDescriptor"(): string
public "getType"(): $RecordedClass
get "name"(): string
get "modifiers"(): integer
get "hidden"(): boolean
get "descriptor"(): string
get "type"(): $RecordedClass
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecordedMethod$Type = ($RecordedMethod);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecordedMethod_ = $RecordedMethod$Type;
}}
declare module "packages/jdk/jfr/consumer/$RecordedObject" {
import {$RecordedThread, $RecordedThread$Type} from "packages/jdk/jfr/consumer/$RecordedThread"
import {$Instant, $Instant$Type} from "packages/java/time/$Instant"
import {$RecordedClass, $RecordedClass$Type} from "packages/jdk/jfr/consumer/$RecordedClass"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ValueDescriptor, $ValueDescriptor$Type} from "packages/jdk/jfr/$ValueDescriptor"
import {$Duration, $Duration$Type} from "packages/java/time/$Duration"

export class $RecordedObject {


public "toString"(): string
public "getClass"(arg0: string): $RecordedClass
public "getBoolean"(arg0: string): boolean
public "getByte"(arg0: string): byte
public "getShort"(arg0: string): short
public "getChar"(arg0: string): character
public "getInt"(arg0: string): integer
public "getLong"(arg0: string): long
public "getFloat"(arg0: string): float
public "getDouble"(arg0: string): double
public "getValue"<T>(arg0: string): T
public "getFields"(): $List<($ValueDescriptor)>
public "getString"(arg0: string): string
public "getDuration"(arg0: string): $Duration
public "getInstant"(arg0: string): $Instant
public "hasField"(arg0: string): boolean
public "getThread"(arg0: string): $RecordedThread
get "fields"(): $List<($ValueDescriptor)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecordedObject$Type = ($RecordedObject);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecordedObject_ = $RecordedObject$Type;
}}
declare module "packages/jdk/jfr/$SettingDescriptor" {
import {$Annotation, $Annotation$Type} from "packages/java/lang/annotation/$Annotation"
import {$AnnotationElement, $AnnotationElement$Type} from "packages/jdk/jfr/$AnnotationElement"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$List, $List$Type} from "packages/java/util/$List"

export class $SettingDescriptor {


public "getName"(): string
public "getTypeName"(): string
public "getAnnotation"<A extends $Annotation>(arg0: $Class$Type<(A)>): A
public "getDefaultValue"(): string
public "getContentType"(): string
public "getDescription"(): string
public "getTypeId"(): long
public "getLabel"(): string
public "getAnnotationElements"(): $List<($AnnotationElement)>
get "name"(): string
get "typeName"(): string
get "defaultValue"(): string
get "contentType"(): string
get "description"(): string
get "typeId"(): long
get "label"(): string
get "annotationElements"(): $List<($AnnotationElement)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SettingDescriptor$Type = ($SettingDescriptor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SettingDescriptor_ = $SettingDescriptor$Type;
}}
declare module "packages/jdk/jfr/consumer/$RecordedEvent" {
import {$RecordedThread, $RecordedThread$Type} from "packages/jdk/jfr/consumer/$RecordedThread"
import {$Instant, $Instant$Type} from "packages/java/time/$Instant"
import {$EventType, $EventType$Type} from "packages/jdk/jfr/$EventType"
import {$RecordedStackTrace, $RecordedStackTrace$Type} from "packages/jdk/jfr/consumer/$RecordedStackTrace"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ValueDescriptor, $ValueDescriptor$Type} from "packages/jdk/jfr/$ValueDescriptor"
import {$RecordedObject, $RecordedObject$Type} from "packages/jdk/jfr/consumer/$RecordedObject"
import {$Duration, $Duration$Type} from "packages/java/time/$Duration"

export class $RecordedEvent extends $RecordedObject {


public "getStackTrace"(): $RecordedStackTrace
public "getFields"(): $List<($ValueDescriptor)>
public "getDuration"(): $Duration
public "getEventType"(): $EventType
public "getStartTime"(): $Instant
public "getThread"(): $RecordedThread
public "getEndTime"(): $Instant
get "stackTrace"(): $RecordedStackTrace
get "fields"(): $List<($ValueDescriptor)>
get "duration"(): $Duration
get "eventType"(): $EventType
get "startTime"(): $Instant
get "thread"(): $RecordedThread
get "endTime"(): $Instant
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecordedEvent$Type = ($RecordedEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecordedEvent_ = $RecordedEvent$Type;
}}
declare module "packages/jdk/jfr/consumer/$RecordedThread" {
import {$RecordedThreadGroup, $RecordedThreadGroup$Type} from "packages/jdk/jfr/consumer/$RecordedThreadGroup"
import {$RecordedObject, $RecordedObject$Type} from "packages/jdk/jfr/consumer/$RecordedObject"

export class $RecordedThread extends $RecordedObject {


public "getThreadGroup"(): $RecordedThreadGroup
public "getId"(): long
public "getOSName"(): string
public "getOSThreadId"(): long
public "getJavaName"(): string
public "getJavaThreadId"(): long
get "threadGroup"(): $RecordedThreadGroup
get "id"(): long
get "oSName"(): string
get "oSThreadId"(): long
get "javaName"(): string
get "javaThreadId"(): long
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecordedThread$Type = ($RecordedThread);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecordedThread_ = $RecordedThread$Type;
}}
declare module "packages/jdk/jfr/$EventType" {
import {$Annotation, $Annotation$Type} from "packages/java/lang/annotation/$Annotation"
import {$AnnotationElement, $AnnotationElement$Type} from "packages/jdk/jfr/$AnnotationElement"
import {$SettingDescriptor, $SettingDescriptor$Type} from "packages/jdk/jfr/$SettingDescriptor"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ValueDescriptor, $ValueDescriptor$Type} from "packages/jdk/jfr/$ValueDescriptor"
import {$Class, $Class$Type} from "packages/java/lang/$Class"

export class $EventType {


public "getName"(): string
public "getFields"(): $List<($ValueDescriptor)>
public "getField"(arg0: string): $ValueDescriptor
public "getAnnotation"<A extends $Annotation>(arg0: $Class$Type<(A)>): A
public "getId"(): long
public "isEnabled"(): boolean
public "getDescription"(): string
public "getCategoryNames"(): $List<(string)>
public static "getEventType"(arg0: $Class$Type<(any)>): $EventType
public "getSettingDescriptors"(): $List<($SettingDescriptor)>
public "getLabel"(): string
public "getAnnotationElements"(): $List<($AnnotationElement)>
get "name"(): string
get "fields"(): $List<($ValueDescriptor)>
get "id"(): long
get "enabled"(): boolean
get "description"(): string
get "categoryNames"(): $List<(string)>
get "settingDescriptors"(): $List<($SettingDescriptor)>
get "label"(): string
get "annotationElements"(): $List<($AnnotationElement)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EventType$Type = ($EventType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EventType_ = $EventType$Type;
}}
declare module "packages/jdk/jfr/consumer/$RecordedClass" {
import {$RecordedClassLoader, $RecordedClassLoader$Type} from "packages/jdk/jfr/consumer/$RecordedClassLoader"
import {$RecordedObject, $RecordedObject$Type} from "packages/jdk/jfr/consumer/$RecordedObject"

export class $RecordedClass extends $RecordedObject {


public "getName"(): string
public "getModifiers"(): integer
public "getClassLoader"(): $RecordedClassLoader
public "getId"(): long
get "name"(): string
get "modifiers"(): integer
get "classLoader"(): $RecordedClassLoader
get "id"(): long
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecordedClass$Type = ($RecordedClass);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecordedClass_ = $RecordedClass$Type;
}}
declare module "packages/jdk/jfr/consumer/$RecordedFrame" {
import {$RecordedMethod, $RecordedMethod$Type} from "packages/jdk/jfr/consumer/$RecordedMethod"
import {$RecordedObject, $RecordedObject$Type} from "packages/jdk/jfr/consumer/$RecordedObject"

export class $RecordedFrame extends $RecordedObject {


public "getMethod"(): $RecordedMethod
public "getType"(): string
public "getLineNumber"(): integer
public "isJavaFrame"(): boolean
public "getBytecodeIndex"(): integer
get "method"(): $RecordedMethod
get "type"(): string
get "lineNumber"(): integer
get "javaFrame"(): boolean
get "bytecodeIndex"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecordedFrame$Type = ($RecordedFrame);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecordedFrame_ = $RecordedFrame$Type;
}}
declare module "packages/jdk/jfr/$ValueDescriptor" {
import {$AnnotationElement, $AnnotationElement$Type} from "packages/jdk/jfr/$AnnotationElement"
import {$Annotation, $Annotation$Type} from "packages/java/lang/annotation/$Annotation"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$List, $List$Type} from "packages/java/util/$List"

export class $ValueDescriptor {

constructor(arg0: $Class$Type<(any)>, arg1: string, arg2: $List$Type<($AnnotationElement$Type)>)
constructor(arg0: $Class$Type<(any)>, arg1: string)

public "getName"(): string
public "isArray"(): boolean
public "getTypeName"(): string
public "getFields"(): $List<($ValueDescriptor)>
public "getAnnotation"<A extends $Annotation>(arg0: $Class$Type<(A)>): A
public "getContentType"(): string
public "getDescription"(): string
public "getTypeId"(): long
public "getLabel"(): string
public "getAnnotationElements"(): $List<($AnnotationElement)>
get "name"(): string
get "array"(): boolean
get "typeName"(): string
get "fields"(): $List<($ValueDescriptor)>
get "contentType"(): string
get "description"(): string
get "typeId"(): long
get "label"(): string
get "annotationElements"(): $List<($AnnotationElement)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ValueDescriptor$Type = ($ValueDescriptor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ValueDescriptor_ = $ValueDescriptor$Type;
}}
declare module "packages/jdk/jfr/$AnnotationElement" {
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ValueDescriptor, $ValueDescriptor$Type} from "packages/jdk/jfr/$ValueDescriptor"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $AnnotationElement {

constructor(arg0: $Class$Type<(any)>)
constructor(arg0: $Class$Type<(any)>, arg1: any)
constructor(arg0: $Class$Type<(any)>, arg1: $Map$Type<(string), (any)>)

public "getValue"(arg0: string): any
public "getTypeName"(): string
public "getAnnotation"<A>(arg0: $Class$Type<(any)>): A
public "hasValue"(arg0: string): boolean
public "getTypeId"(): long
public "getAnnotationElements"(): $List<($AnnotationElement)>
public "getValueDescriptors"(): $List<($ValueDescriptor)>
public "getValues"(): $List<(any)>
get "typeName"(): string
get "typeId"(): long
get "annotationElements"(): $List<($AnnotationElement)>
get "valueDescriptors"(): $List<($ValueDescriptor)>
get "values"(): $List<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AnnotationElement$Type = ($AnnotationElement);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AnnotationElement_ = $AnnotationElement$Type;
}}
declare module "packages/jdk/jfr/consumer/$RecordedStackTrace" {
import {$RecordedFrame, $RecordedFrame$Type} from "packages/jdk/jfr/consumer/$RecordedFrame"
import {$List, $List$Type} from "packages/java/util/$List"
import {$RecordedObject, $RecordedObject$Type} from "packages/jdk/jfr/consumer/$RecordedObject"

export class $RecordedStackTrace extends $RecordedObject {


public "getFrames"(): $List<($RecordedFrame)>
public "isTruncated"(): boolean
get "frames"(): $List<($RecordedFrame)>
get "truncated"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecordedStackTrace$Type = ($RecordedStackTrace);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecordedStackTrace_ = $RecordedStackTrace$Type;
}}
declare module "packages/jdk/jfr/$Event" {
import {$Event as $Event$0, $Event$Type as $Event$0$Type} from "packages/jdk/internal/event/$Event"

export class $Event extends $Event$0 {


public "end"(): void
public "begin"(): void
public "set"(arg0: integer, arg1: any): void
public "commit"(): void
public "isEnabled"(): boolean
public "shouldCommit"(): boolean
get "enabled"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Event$Type = ($Event);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Event_ = $Event$Type;
}}
declare module "packages/jdk/jfr/consumer/$RecordedClassLoader" {
import {$RecordedClass, $RecordedClass$Type} from "packages/jdk/jfr/consumer/$RecordedClass"
import {$RecordedObject, $RecordedObject$Type} from "packages/jdk/jfr/consumer/$RecordedObject"

export class $RecordedClassLoader extends $RecordedObject {


public "getName"(): string
public "getId"(): long
public "getType"(): $RecordedClass
get "name"(): string
get "id"(): long
get "type"(): $RecordedClass
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecordedClassLoader$Type = ($RecordedClassLoader);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecordedClassLoader_ = $RecordedClassLoader$Type;
}}
declare module "packages/jdk/jfr/consumer/$RecordedThreadGroup" {
import {$RecordedObject, $RecordedObject$Type} from "packages/jdk/jfr/consumer/$RecordedObject"

export class $RecordedThreadGroup extends $RecordedObject {


public "getName"(): string
public "getParent"(): $RecordedThreadGroup
get "name"(): string
get "parent"(): $RecordedThreadGroup
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecordedThreadGroup$Type = ($RecordedThreadGroup);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecordedThreadGroup_ = $RecordedThreadGroup$Type;
}}

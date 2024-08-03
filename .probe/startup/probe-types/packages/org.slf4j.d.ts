declare module "packages/org/slf4j/spi/$LoggingEventBuilder" {
import {$Throwable, $Throwable$Type} from "packages/java/lang/$Throwable"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$Marker, $Marker$Type} from "packages/org/slf4j/$Marker"

export interface $LoggingEventBuilder {

 "log"(): void
 "log"(arg0: string): void
 "log"(arg0: string, arg1: any): void
 "log"(arg0: $Supplier$Type<(string)>): void
 "log"(arg0: string, ...arg1: (any)[]): void
 "log"(arg0: string, arg1: any, arg2: any): void
 "setCause"(arg0: $Throwable$Type): $LoggingEventBuilder
 "setMessage"(arg0: $Supplier$Type<(string)>): $LoggingEventBuilder
 "setMessage"(arg0: string): $LoggingEventBuilder
 "addMarker"(arg0: $Marker$Type): $LoggingEventBuilder
 "addArgument"(arg0: $Supplier$Type<(any)>): $LoggingEventBuilder
 "addArgument"(arg0: any): $LoggingEventBuilder
 "addKeyValue"(arg0: string, arg1: any): $LoggingEventBuilder
 "addKeyValue"(arg0: string, arg1: $Supplier$Type<(any)>): $LoggingEventBuilder
}

export namespace $LoggingEventBuilder {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LoggingEventBuilder$Type = ($LoggingEventBuilder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LoggingEventBuilder_ = $LoggingEventBuilder$Type;
}}
declare module "packages/org/slf4j/$Logger" {
import {$Throwable, $Throwable$Type} from "packages/java/lang/$Throwable"
import {$Level, $Level$Type} from "packages/org/slf4j/event/$Level"
import {$LoggingEventBuilder, $LoggingEventBuilder$Type} from "packages/org/slf4j/spi/$LoggingEventBuilder"
import {$Marker, $Marker$Type} from "packages/org/slf4j/$Marker"

export interface $Logger {

 "getName"(): string
 "info"(arg0: string, arg1: any, arg2: any): void
 "info"(arg0: string, ...arg1: (any)[]): void
 "info"(arg0: string, arg1: $Throwable$Type): void
 "info"(arg0: string, arg1: any): void
 "info"(arg0: string): void
 "info"(arg0: $Marker$Type, arg1: string, arg2: $Throwable$Type): void
 "info"(arg0: $Marker$Type, arg1: string, ...arg2: (any)[]): void
 "info"(arg0: $Marker$Type, arg1: string, arg2: any, arg3: any): void
 "info"(arg0: $Marker$Type, arg1: string, arg2: any): void
 "info"(arg0: $Marker$Type, arg1: string): void
 "trace"(arg0: $Marker$Type, arg1: string, arg2: $Throwable$Type): void
 "trace"(arg0: string, arg1: any): void
 "trace"(arg0: string, ...arg1: (any)[]): void
 "trace"(arg0: string): void
 "trace"(arg0: string, arg1: $Throwable$Type): void
 "trace"(arg0: $Marker$Type, arg1: string, arg2: any): void
 "trace"(arg0: $Marker$Type, arg1: string, arg2: any, arg3: any): void
 "trace"(arg0: $Marker$Type, arg1: string): void
 "trace"(arg0: $Marker$Type, arg1: string, ...arg2: (any)[]): void
 "trace"(arg0: string, arg1: any, arg2: any): void
 "debug"(arg0: $Marker$Type, arg1: string, arg2: any): void
 "debug"(arg0: $Marker$Type, arg1: string, arg2: any, arg3: any): void
 "debug"(arg0: $Marker$Type, arg1: string, ...arg2: (any)[]): void
 "debug"(arg0: string, ...arg1: (any)[]): void
 "debug"(arg0: $Marker$Type, arg1: string, arg2: $Throwable$Type): void
 "debug"(arg0: string): void
 "debug"(arg0: string, arg1: any): void
 "debug"(arg0: string, arg1: any, arg2: any): void
 "debug"(arg0: string, arg1: $Throwable$Type): void
 "debug"(arg0: $Marker$Type, arg1: string): void
 "error"(arg0: $Marker$Type, arg1: string, arg2: any, arg3: any): void
 "error"(arg0: string): void
 "error"(arg0: $Marker$Type, arg1: string, arg2: $Throwable$Type): void
 "error"(arg0: $Marker$Type, arg1: string, ...arg2: (any)[]): void
 "error"(arg0: $Marker$Type, arg1: string, arg2: any): void
 "error"(arg0: $Marker$Type, arg1: string): void
 "error"(arg0: string, arg1: $Throwable$Type): void
 "error"(arg0: string, arg1: any, arg2: any): void
 "error"(arg0: string, ...arg1: (any)[]): void
 "error"(arg0: string, arg1: any): void
 "warn"(arg0: string, arg1: $Throwable$Type): void
 "warn"(arg0: string, arg1: any, arg2: any): void
 "warn"(arg0: string, ...arg1: (any)[]): void
 "warn"(arg0: $Marker$Type, arg1: string, arg2: any): void
 "warn"(arg0: $Marker$Type, arg1: string): void
 "warn"(arg0: $Marker$Type, arg1: string, arg2: $Throwable$Type): void
 "warn"(arg0: $Marker$Type, arg1: string, ...arg2: (any)[]): void
 "warn"(arg0: $Marker$Type, arg1: string, arg2: any, arg3: any): void
 "warn"(arg0: string): void
 "warn"(arg0: string, arg1: any): void
 "isTraceEnabled"(arg0: $Marker$Type): boolean
 "isTraceEnabled"(): boolean
 "isDebugEnabled"(arg0: $Marker$Type): boolean
 "isDebugEnabled"(): boolean
 "isInfoEnabled"(arg0: $Marker$Type): boolean
 "isInfoEnabled"(): boolean
 "isErrorEnabled"(arg0: $Marker$Type): boolean
 "isErrorEnabled"(): boolean
 "isWarnEnabled"(arg0: $Marker$Type): boolean
 "isWarnEnabled"(): boolean
 "atTrace"(): $LoggingEventBuilder
 "atDebug"(): $LoggingEventBuilder
 "atInfo"(): $LoggingEventBuilder
 "atWarn"(): $LoggingEventBuilder
 "atError"(): $LoggingEventBuilder
 "atLevel"(arg0: $Level$Type): $LoggingEventBuilder
 "isEnabledForLevel"(arg0: $Level$Type): boolean
 "makeLoggingEventBuilder"(arg0: $Level$Type): $LoggingEventBuilder
}

export namespace $Logger {
const ROOT_LOGGER_NAME: string
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
declare module "packages/org/slf4j/event/$Level" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $Level extends $Enum<($Level)> {
static readonly "ERROR": $Level
static readonly "WARN": $Level
static readonly "INFO": $Level
static readonly "DEBUG": $Level
static readonly "TRACE": $Level


public "toString"(): string
public static "values"(): ($Level)[]
public static "valueOf"(arg0: string): $Level
public "toInt"(): integer
public static "intToLevel"(arg0: integer): $Level
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Level$Type = (("warn") | ("trace") | ("debug") | ("error") | ("info")) | ($Level);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Level_ = $Level$Type;
}}
declare module "packages/org/slf4j/$Marker" {
import {$Serializable, $Serializable$Type} from "packages/java/io/$Serializable"
import {$Iterator, $Iterator$Type} from "packages/java/util/$Iterator"

export interface $Marker extends $Serializable {

 "getName"(): string
 "add"(arg0: $Marker$Type): void
 "remove"(arg0: $Marker$Type): boolean
 "equals"(arg0: any): boolean
 "hashCode"(): integer
 "iterator"(): $Iterator<($Marker)>
 "contains"(arg0: string): boolean
 "contains"(arg0: $Marker$Type): boolean
/**
 * 
 * @deprecated
 */
 "hasChildren"(): boolean
 "hasReferences"(): boolean
}

export namespace $Marker {
const ANY_MARKER: string
const ANY_NON_NULL_MARKER: string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Marker$Type = ($Marker);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Marker_ = $Marker$Type;
}}

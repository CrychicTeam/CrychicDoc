declare module "packages/com/sun/management/$GarbageCollectionNotificationInfo" {
import {$CompositeType, $CompositeType$Type} from "packages/javax/management/openmbean/$CompositeType"
import {$GcInfo, $GcInfo$Type} from "packages/com/sun/management/$GcInfo"
import {$CompositeData, $CompositeData$Type} from "packages/javax/management/openmbean/$CompositeData"
import {$CompositeDataView, $CompositeDataView$Type} from "packages/javax/management/openmbean/$CompositeDataView"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $GarbageCollectionNotificationInfo implements $CompositeDataView {
static readonly "GARBAGE_COLLECTION_NOTIFICATION": string

constructor(arg0: string, arg1: string, arg2: string, arg3: $GcInfo$Type)

public static "from"(arg0: $CompositeData$Type): $GarbageCollectionNotificationInfo
public "toCompositeData"(arg0: $CompositeType$Type): $CompositeData
public "getGcInfo"(): $GcInfo
public "getGcName"(): string
public "getGcAction"(): string
public "getGcCause"(): string
get "gcInfo"(): $GcInfo
get "gcName"(): string
get "gcAction"(): string
get "gcCause"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GarbageCollectionNotificationInfo$Type = ($GarbageCollectionNotificationInfo);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GarbageCollectionNotificationInfo_ = $GarbageCollectionNotificationInfo$Type;
}}
declare module "packages/com/sun/management/$GcInfo" {
import {$CompositeType, $CompositeType$Type} from "packages/javax/management/openmbean/$CompositeType"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$CompositeData, $CompositeData$Type} from "packages/javax/management/openmbean/$CompositeData"
import {$CompositeDataView, $CompositeDataView$Type} from "packages/javax/management/openmbean/$CompositeDataView"
import {$MemoryUsage, $MemoryUsage$Type} from "packages/java/lang/management/$MemoryUsage"
import {$Map, $Map$Type} from "packages/java/util/$Map"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $GcInfo implements $CompositeData, $CompositeDataView {


public "get"(arg0: string): any
public "equals"(arg0: any): boolean
public "toString"(): string
public "values"(): $Collection<(any)>
public "hashCode"(): integer
public static "from"(arg0: $CompositeData$Type): $GcInfo
public "containsKey"(arg0: string): boolean
public "getId"(): long
public "containsValue"(arg0: any): boolean
public "getAll"(arg0: (string)[]): (any)[]
public "getDuration"(): long
public "getCompositeType"(): $CompositeType
public "getStartTime"(): long
public "toCompositeData"(arg0: $CompositeType$Type): $CompositeData
public "getEndTime"(): long
public "getMemoryUsageBeforeGc"(): $Map<(string), ($MemoryUsage)>
public "getMemoryUsageAfterGc"(): $Map<(string), ($MemoryUsage)>
get "id"(): long
get "duration"(): long
get "compositeType"(): $CompositeType
get "startTime"(): long
get "endTime"(): long
get "memoryUsageBeforeGc"(): $Map<(string), ($MemoryUsage)>
get "memoryUsageAfterGc"(): $Map<(string), ($MemoryUsage)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GcInfo$Type = ($GcInfo);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GcInfo_ = $GcInfo$Type;
}}

declare module "packages/javax/management/$ObjectInstance" {
import {$Serializable, $Serializable$Type} from "packages/java/io/$Serializable"
import {$ObjectName, $ObjectName$Type} from "packages/javax/management/$ObjectName"

export class $ObjectInstance implements $Serializable {

constructor(arg0: string, arg1: string)
constructor(arg0: $ObjectName$Type, arg1: string)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "getClassName"(): string
public "getObjectName"(): $ObjectName
get "className"(): string
get "objectName"(): $ObjectName
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ObjectInstance$Type = ($ObjectInstance);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ObjectInstance_ = $ObjectInstance$Type;
}}
declare module "packages/javax/management/$ObjectName" {
import {$Comparable, $Comparable$Type} from "packages/java/lang/$Comparable"
import {$MBeanServer, $MBeanServer$Type} from "packages/javax/management/$MBeanServer"
import {$Hashtable, $Hashtable$Type} from "packages/java/util/$Hashtable"
import {$QueryExp, $QueryExp$Type} from "packages/javax/management/$QueryExp"

export class $ObjectName implements $Comparable<($ObjectName)>, $QueryExp {
static readonly "WILDCARD": $ObjectName

constructor(arg0: string)
constructor(arg0: string, arg1: string, arg2: string)
constructor(arg0: string, arg1: $Hashtable$Type<(string), (string)>)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "compareTo"(arg0: $ObjectName$Type): integer
public "apply"(arg0: $ObjectName$Type): boolean
public "getCanonicalName"(): string
public static "getInstance"(arg0: string): $ObjectName
public static "getInstance"(arg0: string, arg1: string, arg2: string): $ObjectName
public static "getInstance"(arg0: string, arg1: $Hashtable$Type<(string), (string)>): $ObjectName
public static "getInstance"(arg0: $ObjectName$Type): $ObjectName
public static "quote"(arg0: string): string
public "getDomain"(): string
public "isPropertyListPattern"(): boolean
public "getKeyPropertyList"(): $Hashtable<(string), (string)>
public "getKeyPropertyListString"(): string
public "isPattern"(): boolean
public "isDomainPattern"(): boolean
public "isPropertyValuePattern"(arg0: string): boolean
public "isPropertyValuePattern"(): boolean
public "isPropertyPattern"(): boolean
public "getCanonicalKeyPropertyListString"(): string
public "getKeyProperty"(arg0: string): string
public static "unquote"(arg0: string): string
public "setMBeanServer"(arg0: $MBeanServer$Type): void
get "canonicalName"(): string
get "domain"(): string
get "propertyListPattern"(): boolean
get "keyPropertyList"(): $Hashtable<(string), (string)>
get "keyPropertyListString"(): string
get "pattern"(): boolean
get "domainPattern"(): boolean
get "propertyValuePattern"(): boolean
get "propertyPattern"(): boolean
get "canonicalKeyPropertyListString"(): string
set "mBeanServer"(value: $MBeanServer$Type)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ObjectName$Type = ($ObjectName);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ObjectName_ = $ObjectName$Type;
}}
declare module "packages/javax/management/$NotificationFilter" {
import {$Notification, $Notification$Type} from "packages/javax/management/$Notification"
import {$Serializable, $Serializable$Type} from "packages/java/io/$Serializable"

export interface $NotificationFilter extends $Serializable {

 "isNotificationEnabled"(arg0: $Notification$Type): boolean

(arg0: $Notification$Type): boolean
}

export namespace $NotificationFilter {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NotificationFilter$Type = ($NotificationFilter);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NotificationFilter_ = $NotificationFilter$Type;
}}
declare module "packages/javax/management/$QueryExp" {
import {$Serializable, $Serializable$Type} from "packages/java/io/$Serializable"
import {$MBeanServer, $MBeanServer$Type} from "packages/javax/management/$MBeanServer"
import {$ObjectName, $ObjectName$Type} from "packages/javax/management/$ObjectName"

export interface $QueryExp extends $Serializable {

 "apply"(arg0: $ObjectName$Type): boolean
 "setMBeanServer"(arg0: $MBeanServer$Type): void
}

export namespace $QueryExp {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $QueryExp$Type = ($QueryExp);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $QueryExp_ = $QueryExp$Type;
}}
declare module "packages/javax/management/$MBeanServerConnection" {
import {$AttributeList, $AttributeList$Type} from "packages/javax/management/$AttributeList"
import {$Attribute, $Attribute$Type} from "packages/javax/management/$Attribute"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$NotificationListener, $NotificationListener$Type} from "packages/javax/management/$NotificationListener"
import {$MBeanInfo, $MBeanInfo$Type} from "packages/javax/management/$MBeanInfo"
import {$QueryExp, $QueryExp$Type} from "packages/javax/management/$QueryExp"
import {$ObjectInstance, $ObjectInstance$Type} from "packages/javax/management/$ObjectInstance"
import {$NotificationFilter, $NotificationFilter$Type} from "packages/javax/management/$NotificationFilter"
import {$ObjectName, $ObjectName$Type} from "packages/javax/management/$ObjectName"

export interface $MBeanServerConnection {

 "invoke"(arg0: $ObjectName$Type, arg1: string, arg2: (any)[], arg3: (string)[]): any
 "isRegistered"(arg0: $ObjectName$Type): boolean
 "getAttributes"(arg0: $ObjectName$Type, arg1: (string)[]): $AttributeList
 "setAttribute"(arg0: $ObjectName$Type, arg1: $Attribute$Type): void
 "getAttribute"(arg0: $ObjectName$Type, arg1: string): any
 "createMBean"(arg0: string, arg1: $ObjectName$Type): $ObjectInstance
 "createMBean"(arg0: string, arg1: $ObjectName$Type, arg2: $ObjectName$Type): $ObjectInstance
 "createMBean"(arg0: string, arg1: $ObjectName$Type, arg2: $ObjectName$Type, arg3: (any)[], arg4: (string)[]): $ObjectInstance
 "createMBean"(arg0: string, arg1: $ObjectName$Type, arg2: (any)[], arg3: (string)[]): $ObjectInstance
 "getObjectInstance"(arg0: $ObjectName$Type): $ObjectInstance
 "queryMBeans"(arg0: $ObjectName$Type, arg1: $QueryExp$Type): $Set<($ObjectInstance)>
 "getMBeanCount"(): integer
 "getDefaultDomain"(): string
 "getDomains"(): (string)[]
 "queryNames"(arg0: $ObjectName$Type, arg1: $QueryExp$Type): $Set<($ObjectName)>
 "getMBeanInfo"(arg0: $ObjectName$Type): $MBeanInfo
 "addNotificationListener"(arg0: $ObjectName$Type, arg1: $ObjectName$Type, arg2: $NotificationFilter$Type, arg3: any): void
 "addNotificationListener"(arg0: $ObjectName$Type, arg1: $NotificationListener$Type, arg2: $NotificationFilter$Type, arg3: any): void
 "removeNotificationListener"(arg0: $ObjectName$Type, arg1: $NotificationListener$Type): void
 "removeNotificationListener"(arg0: $ObjectName$Type, arg1: $ObjectName$Type, arg2: $NotificationFilter$Type, arg3: any): void
 "removeNotificationListener"(arg0: $ObjectName$Type, arg1: $NotificationListener$Type, arg2: $NotificationFilter$Type, arg3: any): void
 "removeNotificationListener"(arg0: $ObjectName$Type, arg1: $ObjectName$Type): void
 "setAttributes"(arg0: $ObjectName$Type, arg1: $AttributeList$Type): $AttributeList
 "unregisterMBean"(arg0: $ObjectName$Type): void
 "isInstanceOf"(arg0: $ObjectName$Type, arg1: string): boolean
}

export namespace $MBeanServerConnection {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MBeanServerConnection$Type = ($MBeanServerConnection);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MBeanServerConnection_ = $MBeanServerConnection$Type;
}}
declare module "packages/javax/management/openmbean/$OpenType" {
import {$Serializable, $Serializable$Type} from "packages/java/io/$Serializable"
import {$List, $List$Type} from "packages/java/util/$List"

export class $OpenType<T> implements $Serializable {
static readonly "ALLOWED_CLASSNAMES_LIST": $List<(string)>
/**
 * 
 * @deprecated
 */
static readonly "ALLOWED_CLASSNAMES": (string)[]


public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "isArray"(): boolean
public "getTypeName"(): string
public "getClassName"(): string
public "getDescription"(): string
public "isValue"(arg0: any): boolean
get "array"(): boolean
get "typeName"(): string
get "className"(): string
get "description"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $OpenType$Type<T> = ($OpenType<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $OpenType_<T> = $OpenType$Type<(T)>;
}}
declare module "packages/javax/management/$MBeanOperationInfo" {
import {$MBeanFeatureInfo, $MBeanFeatureInfo$Type} from "packages/javax/management/$MBeanFeatureInfo"
import {$Descriptor, $Descriptor$Type} from "packages/javax/management/$Descriptor"
import {$Cloneable, $Cloneable$Type} from "packages/java/lang/$Cloneable"
import {$Method, $Method$Type} from "packages/java/lang/reflect/$Method"
import {$MBeanParameterInfo, $MBeanParameterInfo$Type} from "packages/javax/management/$MBeanParameterInfo"

export class $MBeanOperationInfo extends $MBeanFeatureInfo implements $Cloneable {
static readonly "INFO": integer
static readonly "ACTION": integer
static readonly "ACTION_INFO": integer
static readonly "UNKNOWN": integer

constructor(arg0: string, arg1: string, arg2: ($MBeanParameterInfo$Type)[], arg3: string, arg4: integer, arg5: $Descriptor$Type)
constructor(arg0: string, arg1: string, arg2: ($MBeanParameterInfo$Type)[], arg3: string, arg4: integer)
constructor(arg0: string, arg1: $Method$Type)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "clone"(): any
public "getReturnType"(): string
public "getSignature"(): ($MBeanParameterInfo)[]
public "getImpact"(): integer
get "returnType"(): string
get "signature"(): ($MBeanParameterInfo)[]
get "impact"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MBeanOperationInfo$Type = ($MBeanOperationInfo);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MBeanOperationInfo_ = $MBeanOperationInfo$Type;
}}
declare module "packages/javax/management/openmbean/$CompositeType" {
import {$OpenType, $OpenType$Type} from "packages/javax/management/openmbean/$OpenType"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$List, $List$Type} from "packages/java/util/$List"
import {$CompositeData, $CompositeData$Type} from "packages/javax/management/openmbean/$CompositeData"

export class $CompositeType extends $OpenType<($CompositeData)> {
static readonly "ALLOWED_CLASSNAMES_LIST": $List<(string)>
/**
 * 
 * @deprecated
 */
static readonly "ALLOWED_CLASSNAMES": (string)[]

constructor(arg0: string, arg1: string, arg2: (string)[], arg3: (string)[], arg4: ($OpenType$Type<(any)>)[])

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "containsKey"(arg0: string): boolean
public "keySet"(): $Set<(string)>
public "getType"(arg0: string): $OpenType<(any)>
public "getDescription"(arg0: string): string
public "isValue"(arg0: any): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CompositeType$Type = ($CompositeType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CompositeType_ = $CompositeType$Type;
}}
declare module "packages/javax/management/loading/$ClassLoaderRepository" {
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$ClassLoader, $ClassLoader$Type} from "packages/java/lang/$ClassLoader"

export interface $ClassLoaderRepository {

 "loadClass"(arg0: string): $Class<(any)>
 "loadClassWithout"(arg0: $ClassLoader$Type, arg1: string): $Class<(any)>
 "loadClassBefore"(arg0: $ClassLoader$Type, arg1: string): $Class<(any)>
}

export namespace $ClassLoaderRepository {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClassLoaderRepository$Type = ($ClassLoaderRepository);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClassLoaderRepository_ = $ClassLoaderRepository$Type;
}}
declare module "packages/javax/management/$DescriptorRead" {
import {$Descriptor, $Descriptor$Type} from "packages/javax/management/$Descriptor"

export interface $DescriptorRead {

 "getDescriptor"(): $Descriptor

(): $Descriptor
}

export namespace $DescriptorRead {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DescriptorRead$Type = ($DescriptorRead);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DescriptorRead_ = $DescriptorRead$Type;
}}
declare module "packages/javax/management/$MBeanFeatureInfo" {
import {$Serializable, $Serializable$Type} from "packages/java/io/$Serializable"
import {$DescriptorRead, $DescriptorRead$Type} from "packages/javax/management/$DescriptorRead"
import {$Descriptor, $Descriptor$Type} from "packages/javax/management/$Descriptor"

export class $MBeanFeatureInfo implements $Serializable, $DescriptorRead {

constructor(arg0: string, arg1: string)
constructor(arg0: string, arg1: string, arg2: $Descriptor$Type)

public "getName"(): string
public "equals"(arg0: any): boolean
public "hashCode"(): integer
public "getDescriptor"(): $Descriptor
public "getDescription"(): string
get "name"(): string
get "descriptor"(): $Descriptor
get "description"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MBeanFeatureInfo$Type = ($MBeanFeatureInfo);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MBeanFeatureInfo_ = $MBeanFeatureInfo$Type;
}}
declare module "packages/javax/management/$MBeanServer" {
import {$MBeanServerConnection, $MBeanServerConnection$Type} from "packages/javax/management/$MBeanServerConnection"
import {$AttributeList, $AttributeList$Type} from "packages/javax/management/$AttributeList"
import {$Attribute, $Attribute$Type} from "packages/javax/management/$Attribute"
import {$ClassLoaderRepository, $ClassLoaderRepository$Type} from "packages/javax/management/loading/$ClassLoaderRepository"
import {$ClassLoader, $ClassLoader$Type} from "packages/java/lang/$ClassLoader"
import {$ObjectInputStream, $ObjectInputStream$Type} from "packages/java/io/$ObjectInputStream"
import {$NotificationFilter, $NotificationFilter$Type} from "packages/javax/management/$NotificationFilter"
import {$ObjectName, $ObjectName$Type} from "packages/javax/management/$ObjectName"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$NotificationListener, $NotificationListener$Type} from "packages/javax/management/$NotificationListener"
import {$MBeanInfo, $MBeanInfo$Type} from "packages/javax/management/$MBeanInfo"
import {$QueryExp, $QueryExp$Type} from "packages/javax/management/$QueryExp"
import {$ObjectInstance, $ObjectInstance$Type} from "packages/javax/management/$ObjectInstance"

export interface $MBeanServer extends $MBeanServerConnection {

 "invoke"(arg0: $ObjectName$Type, arg1: string, arg2: (any)[], arg3: (string)[]): any
 "getClassLoader"(arg0: $ObjectName$Type): $ClassLoader
 "isRegistered"(arg0: $ObjectName$Type): boolean
 "getAttributes"(arg0: $ObjectName$Type, arg1: (string)[]): $AttributeList
 "setAttribute"(arg0: $ObjectName$Type, arg1: $Attribute$Type): void
 "getAttribute"(arg0: $ObjectName$Type, arg1: string): any
 "createMBean"(arg0: string, arg1: $ObjectName$Type): $ObjectInstance
 "createMBean"(arg0: string, arg1: $ObjectName$Type, arg2: $ObjectName$Type, arg3: (any)[], arg4: (string)[]): $ObjectInstance
 "createMBean"(arg0: string, arg1: $ObjectName$Type, arg2: $ObjectName$Type): $ObjectInstance
 "createMBean"(arg0: string, arg1: $ObjectName$Type, arg2: (any)[], arg3: (string)[]): $ObjectInstance
 "getObjectInstance"(arg0: $ObjectName$Type): $ObjectInstance
 "queryMBeans"(arg0: $ObjectName$Type, arg1: $QueryExp$Type): $Set<($ObjectInstance)>
 "getMBeanCount"(): integer
 "getDefaultDomain"(): string
 "getDomains"(): (string)[]
/**
 * 
 * @deprecated
 */
 "deserialize"(arg0: $ObjectName$Type, arg1: (byte)[]): $ObjectInputStream
/**
 * 
 * @deprecated
 */
 "deserialize"(arg0: string, arg1: (byte)[]): $ObjectInputStream
/**
 * 
 * @deprecated
 */
 "deserialize"(arg0: string, arg1: $ObjectName$Type, arg2: (byte)[]): $ObjectInputStream
 "getClassLoaderFor"(arg0: $ObjectName$Type): $ClassLoader
 "getClassLoaderRepository"(): $ClassLoaderRepository
 "queryNames"(arg0: $ObjectName$Type, arg1: $QueryExp$Type): $Set<($ObjectName)>
 "registerMBean"(arg0: any, arg1: $ObjectName$Type): $ObjectInstance
 "getMBeanInfo"(arg0: $ObjectName$Type): $MBeanInfo
 "addNotificationListener"(arg0: $ObjectName$Type, arg1: $NotificationListener$Type, arg2: $NotificationFilter$Type, arg3: any): void
 "addNotificationListener"(arg0: $ObjectName$Type, arg1: $ObjectName$Type, arg2: $NotificationFilter$Type, arg3: any): void
 "removeNotificationListener"(arg0: $ObjectName$Type, arg1: $NotificationListener$Type): void
 "removeNotificationListener"(arg0: $ObjectName$Type, arg1: $ObjectName$Type, arg2: $NotificationFilter$Type, arg3: any): void
 "removeNotificationListener"(arg0: $ObjectName$Type, arg1: $ObjectName$Type): void
 "removeNotificationListener"(arg0: $ObjectName$Type, arg1: $NotificationListener$Type, arg2: $NotificationFilter$Type, arg3: any): void
 "setAttributes"(arg0: $ObjectName$Type, arg1: $AttributeList$Type): $AttributeList
 "unregisterMBean"(arg0: $ObjectName$Type): void
 "instantiate"(arg0: string, arg1: $ObjectName$Type): any
 "instantiate"(arg0: string, arg1: (any)[], arg2: (string)[]): any
 "instantiate"(arg0: string, arg1: $ObjectName$Type, arg2: (any)[], arg3: (string)[]): any
 "instantiate"(arg0: string): any
 "isInstanceOf"(arg0: $ObjectName$Type, arg1: string): boolean
}

export namespace $MBeanServer {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MBeanServer$Type = ($MBeanServer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MBeanServer_ = $MBeanServer$Type;
}}
declare module "packages/javax/management/$MBeanInfo" {
import {$Serializable, $Serializable$Type} from "packages/java/io/$Serializable"
import {$DescriptorRead, $DescriptorRead$Type} from "packages/javax/management/$DescriptorRead"
import {$Descriptor, $Descriptor$Type} from "packages/javax/management/$Descriptor"
import {$MBeanNotificationInfo, $MBeanNotificationInfo$Type} from "packages/javax/management/$MBeanNotificationInfo"
import {$Cloneable, $Cloneable$Type} from "packages/java/lang/$Cloneable"
import {$MBeanAttributeInfo, $MBeanAttributeInfo$Type} from "packages/javax/management/$MBeanAttributeInfo"
import {$MBeanOperationInfo, $MBeanOperationInfo$Type} from "packages/javax/management/$MBeanOperationInfo"
import {$MBeanConstructorInfo, $MBeanConstructorInfo$Type} from "packages/javax/management/$MBeanConstructorInfo"

export class $MBeanInfo implements $Cloneable, $Serializable, $DescriptorRead {

constructor(arg0: string, arg1: string, arg2: ($MBeanAttributeInfo$Type)[], arg3: ($MBeanConstructorInfo$Type)[], arg4: ($MBeanOperationInfo$Type)[], arg5: ($MBeanNotificationInfo$Type)[])
constructor(arg0: string, arg1: string, arg2: ($MBeanAttributeInfo$Type)[], arg3: ($MBeanConstructorInfo$Type)[], arg4: ($MBeanOperationInfo$Type)[], arg5: ($MBeanNotificationInfo$Type)[], arg6: $Descriptor$Type)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "clone"(): any
public "getDescriptor"(): $Descriptor
public "getConstructors"(): ($MBeanConstructorInfo)[]
public "getAttributes"(): ($MBeanAttributeInfo)[]
public "getClassName"(): string
public "getDescription"(): string
public "getOperations"(): ($MBeanOperationInfo)[]
public "getNotifications"(): ($MBeanNotificationInfo)[]
get "descriptor"(): $Descriptor
get "constructors"(): ($MBeanConstructorInfo)[]
get "attributes"(): ($MBeanAttributeInfo)[]
get "className"(): string
get "description"(): string
get "operations"(): ($MBeanOperationInfo)[]
get "notifications"(): ($MBeanNotificationInfo)[]
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MBeanInfo$Type = ($MBeanInfo);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MBeanInfo_ = $MBeanInfo$Type;
}}
declare module "packages/javax/management/$DynamicMBean" {
import {$AttributeList, $AttributeList$Type} from "packages/javax/management/$AttributeList"
import {$Attribute, $Attribute$Type} from "packages/javax/management/$Attribute"
import {$MBeanInfo, $MBeanInfo$Type} from "packages/javax/management/$MBeanInfo"

export interface $DynamicMBean {

 "invoke"(arg0: string, arg1: (any)[], arg2: (string)[]): any
 "getAttributes"(arg0: (string)[]): $AttributeList
 "setAttribute"(arg0: $Attribute$Type): void
 "getAttribute"(arg0: string): any
 "getMBeanInfo"(): $MBeanInfo
 "setAttributes"(arg0: $AttributeList$Type): $AttributeList
}

export namespace $DynamicMBean {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DynamicMBean$Type = ($DynamicMBean);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DynamicMBean_ = $DynamicMBean$Type;
}}
declare module "packages/javax/management/$Notification" {
import {$EventObject, $EventObject$Type} from "packages/java/util/$EventObject"

export class $Notification extends $EventObject {

constructor(arg0: string, arg1: any, arg2: long, arg3: long, arg4: string)
constructor(arg0: string, arg1: any, arg2: long, arg3: long)
constructor(arg0: string, arg1: any, arg2: long, arg3: string)
constructor(arg0: string, arg1: any, arg2: long)

public "toString"(): string
public "getMessage"(): string
public "getType"(): string
public "getUserData"(): any
public "setUserData"(arg0: any): void
public "setSource"(arg0: any): void
public "getSequenceNumber"(): long
public "setSequenceNumber"(arg0: long): void
public "getTimeStamp"(): long
public "setTimeStamp"(arg0: long): void
get "message"(): string
get "type"(): string
get "userData"(): any
set "userData"(value: any)
set "source"(value: any)
get "sequenceNumber"(): long
set "sequenceNumber"(value: long)
get "timeStamp"(): long
set "timeStamp"(value: long)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Notification$Type = ($Notification);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Notification_ = $Notification$Type;
}}
declare module "packages/javax/management/$MBeanConstructorInfo" {
import {$MBeanFeatureInfo, $MBeanFeatureInfo$Type} from "packages/javax/management/$MBeanFeatureInfo"
import {$Descriptor, $Descriptor$Type} from "packages/javax/management/$Descriptor"
import {$Cloneable, $Cloneable$Type} from "packages/java/lang/$Cloneable"
import {$Constructor, $Constructor$Type} from "packages/java/lang/reflect/$Constructor"
import {$MBeanParameterInfo, $MBeanParameterInfo$Type} from "packages/javax/management/$MBeanParameterInfo"

export class $MBeanConstructorInfo extends $MBeanFeatureInfo implements $Cloneable {

constructor(arg0: string, arg1: string, arg2: ($MBeanParameterInfo$Type)[], arg3: $Descriptor$Type)
constructor(arg0: string, arg1: string, arg2: ($MBeanParameterInfo$Type)[])
constructor(arg0: string, arg1: $Constructor$Type<(any)>)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "clone"(): any
public "getSignature"(): ($MBeanParameterInfo)[]
get "signature"(): ($MBeanParameterInfo)[]
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MBeanConstructorInfo$Type = ($MBeanConstructorInfo);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MBeanConstructorInfo_ = $MBeanConstructorInfo$Type;
}}
declare module "packages/javax/management/$Attribute" {
import {$Serializable, $Serializable$Type} from "packages/java/io/$Serializable"

export class $Attribute implements $Serializable {

constructor(arg0: string, arg1: any)

public "getName"(): string
public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "getValue"(): any
get "name"(): string
get "value"(): any
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Attribute$Type = ($Attribute);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Attribute_ = $Attribute$Type;
}}
declare module "packages/javax/management/openmbean/$CompositeData" {
import {$CompositeType, $CompositeType$Type} from "packages/javax/management/openmbean/$CompositeType"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"

export interface $CompositeData {

 "get"(arg0: string): any
 "equals"(arg0: any): boolean
 "toString"(): string
 "values"(): $Collection<(any)>
 "hashCode"(): integer
 "containsKey"(arg0: string): boolean
 "containsValue"(arg0: any): boolean
 "getAll"(arg0: (string)[]): (any)[]
 "getCompositeType"(): $CompositeType
}

export namespace $CompositeData {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CompositeData$Type = ($CompositeData);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CompositeData_ = $CompositeData$Type;
}}
declare module "packages/javax/management/$NotificationListener" {
import {$Notification, $Notification$Type} from "packages/javax/management/$Notification"
import {$EventListener, $EventListener$Type} from "packages/java/util/$EventListener"

export interface $NotificationListener extends $EventListener {

 "handleNotification"(arg0: $Notification$Type, arg1: any): void

(arg0: $Notification$Type, arg1: any): void
}

export namespace $NotificationListener {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NotificationListener$Type = ($NotificationListener);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NotificationListener_ = $NotificationListener$Type;
}}
declare module "packages/javax/management/$MBeanAttributeInfo" {
import {$MBeanFeatureInfo, $MBeanFeatureInfo$Type} from "packages/javax/management/$MBeanFeatureInfo"
import {$Descriptor, $Descriptor$Type} from "packages/javax/management/$Descriptor"
import {$Cloneable, $Cloneable$Type} from "packages/java/lang/$Cloneable"
import {$Method, $Method$Type} from "packages/java/lang/reflect/$Method"

export class $MBeanAttributeInfo extends $MBeanFeatureInfo implements $Cloneable {

constructor(arg0: string, arg1: string, arg2: $Method$Type, arg3: $Method$Type)
constructor(arg0: string, arg1: string, arg2: string, arg3: boolean, arg4: boolean, arg5: boolean, arg6: $Descriptor$Type)
constructor(arg0: string, arg1: string, arg2: string, arg3: boolean, arg4: boolean, arg5: boolean)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "clone"(): any
public "getType"(): string
public "isReadable"(): boolean
public "isWritable"(): boolean
public "isIs"(): boolean
get "type"(): string
get "readable"(): boolean
get "writable"(): boolean
get "is"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MBeanAttributeInfo$Type = ($MBeanAttributeInfo);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MBeanAttributeInfo_ = $MBeanAttributeInfo$Type;
}}
declare module "packages/javax/management/$AttributeList" {
import {$Attribute, $Attribute$Type} from "packages/javax/management/$Attribute"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ArrayList, $ArrayList$Type} from "packages/java/util/$ArrayList"

export class $AttributeList extends $ArrayList<(any)> {

constructor(arg0: $List$Type<($Attribute$Type)>)
constructor(arg0: $AttributeList$Type)
constructor(arg0: integer)
constructor()

public "add"(arg0: integer, arg1: any): void
public "add"(arg0: integer, arg1: $Attribute$Type): void
public "add"(arg0: any): boolean
public "add"(arg0: $Attribute$Type): void
public "addAll"(arg0: $Collection$Type<(any)>): boolean
public "addAll"(arg0: integer, arg1: $Collection$Type<(any)>): boolean
public "addAll"(arg0: integer, arg1: $AttributeList$Type): boolean
public "addAll"(arg0: $AttributeList$Type): boolean
public "set"(arg0: integer, arg1: any): any
public "set"(arg0: integer, arg1: $Attribute$Type): void
public "asList"(): $List<($Attribute)>
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
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AttributeList$Type = ($AttributeList);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AttributeList_ = $AttributeList$Type;
}}
declare module "packages/javax/management/$MBeanParameterInfo" {
import {$MBeanFeatureInfo, $MBeanFeatureInfo$Type} from "packages/javax/management/$MBeanFeatureInfo"
import {$Descriptor, $Descriptor$Type} from "packages/javax/management/$Descriptor"
import {$Cloneable, $Cloneable$Type} from "packages/java/lang/$Cloneable"

export class $MBeanParameterInfo extends $MBeanFeatureInfo implements $Cloneable {

constructor(arg0: string, arg1: string, arg2: string)
constructor(arg0: string, arg1: string, arg2: string, arg3: $Descriptor$Type)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "clone"(): any
public "getType"(): string
get "type"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MBeanParameterInfo$Type = ($MBeanParameterInfo);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MBeanParameterInfo_ = $MBeanParameterInfo$Type;
}}
declare module "packages/javax/management/$Descriptor" {
import {$Serializable, $Serializable$Type} from "packages/java/io/$Serializable"
import {$Cloneable, $Cloneable$Type} from "packages/java/lang/$Cloneable"

export interface $Descriptor extends $Serializable, $Cloneable {

 "equals"(arg0: any): boolean
 "hashCode"(): integer
 "clone"(): any
 "getFields"(): (string)[]
 "setFields"(arg0: (string)[], arg1: (any)[]): void
 "isValid"(): boolean
 "getFieldNames"(): (string)[]
 "getFieldValues"(...arg0: (string)[]): (any)[]
 "setField"(arg0: string, arg1: any): void
 "removeField"(arg0: string): void
 "getFieldValue"(arg0: string): any
}

export namespace $Descriptor {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Descriptor$Type = ($Descriptor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Descriptor_ = $Descriptor$Type;
}}
declare module "packages/javax/management/openmbean/$CompositeDataView" {
import {$CompositeType, $CompositeType$Type} from "packages/javax/management/openmbean/$CompositeType"
import {$CompositeData, $CompositeData$Type} from "packages/javax/management/openmbean/$CompositeData"

export interface $CompositeDataView {

 "toCompositeData"(arg0: $CompositeType$Type): $CompositeData

(arg0: $CompositeType$Type): $CompositeData
}

export namespace $CompositeDataView {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CompositeDataView$Type = ($CompositeDataView);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CompositeDataView_ = $CompositeDataView$Type;
}}
declare module "packages/javax/management/$MBeanNotificationInfo" {
import {$MBeanFeatureInfo, $MBeanFeatureInfo$Type} from "packages/javax/management/$MBeanFeatureInfo"
import {$Descriptor, $Descriptor$Type} from "packages/javax/management/$Descriptor"
import {$Cloneable, $Cloneable$Type} from "packages/java/lang/$Cloneable"

export class $MBeanNotificationInfo extends $MBeanFeatureInfo implements $Cloneable {

constructor(arg0: (string)[], arg1: string, arg2: string)
constructor(arg0: (string)[], arg1: string, arg2: string, arg3: $Descriptor$Type)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "clone"(): any
public "getNotifTypes"(): (string)[]
get "notifTypes"(): (string)[]
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MBeanNotificationInfo$Type = ($MBeanNotificationInfo);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MBeanNotificationInfo_ = $MBeanNotificationInfo$Type;
}}

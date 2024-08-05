declare module "packages/java/beans/$PropertyEditor" {
import {$PropertyChangeListener, $PropertyChangeListener$Type} from "packages/java/beans/$PropertyChangeListener"
import {$Rectangle, $Rectangle$Type} from "packages/java/awt/$Rectangle"
import {$Graphics, $Graphics$Type} from "packages/java/awt/$Graphics"
import {$Component, $Component$Type} from "packages/java/awt/$Component"

export interface $PropertyEditor {

 "getValue"(): any
 "setValue"(arg0: any): void
 "addPropertyChangeListener"(arg0: $PropertyChangeListener$Type): void
 "removePropertyChangeListener"(arg0: $PropertyChangeListener$Type): void
 "getTags"(): (string)[]
 "isPaintable"(): boolean
 "paintValue"(arg0: $Graphics$Type, arg1: $Rectangle$Type): void
 "getJavaInitializationString"(): string
 "getAsText"(): string
 "setAsText"(arg0: string): void
 "getCustomEditor"(): $Component
 "supportsCustomEditor"(): boolean
}

export namespace $PropertyEditor {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PropertyEditor$Type = ($PropertyEditor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PropertyEditor_ = $PropertyEditor$Type;
}}
declare module "packages/java/beans/$VetoableChangeListener" {
import {$EventListener, $EventListener$Type} from "packages/java/util/$EventListener"
import {$PropertyChangeEvent, $PropertyChangeEvent$Type} from "packages/java/beans/$PropertyChangeEvent"

export interface $VetoableChangeListener extends $EventListener {

 "vetoableChange"(arg0: $PropertyChangeEvent$Type): void

(arg0: $PropertyChangeEvent$Type): void
}

export namespace $VetoableChangeListener {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $VetoableChangeListener$Type = ($VetoableChangeListener);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $VetoableChangeListener_ = $VetoableChangeListener$Type;
}}
declare module "packages/java/beans/$PropertyChangeListener" {
import {$EventListener, $EventListener$Type} from "packages/java/util/$EventListener"
import {$PropertyChangeEvent, $PropertyChangeEvent$Type} from "packages/java/beans/$PropertyChangeEvent"

export interface $PropertyChangeListener extends $EventListener {

 "propertyChange"(arg0: $PropertyChangeEvent$Type): void

(arg0: $PropertyChangeEvent$Type): void
}

export namespace $PropertyChangeListener {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PropertyChangeListener$Type = ($PropertyChangeListener);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PropertyChangeListener_ = $PropertyChangeListener$Type;
}}
declare module "packages/java/beans/$FeatureDescriptor" {
import {$Enumeration, $Enumeration$Type} from "packages/java/util/$Enumeration"

export class $FeatureDescriptor {

constructor()

public "getName"(): string
public "toString"(): string
public "isHidden"(): boolean
public "getValue"(arg0: string): any
public "setName"(arg0: string): void
public "setValue"(arg0: string, arg1: any): void
public "getDisplayName"(): string
public "attributeNames"(): $Enumeration<(string)>
public "setDisplayName"(arg0: string): void
public "isExpert"(): boolean
public "setExpert"(arg0: boolean): void
public "isPreferred"(): boolean
public "setPreferred"(arg0: boolean): void
public "getShortDescription"(): string
public "setShortDescription"(arg0: string): void
public "setHidden"(arg0: boolean): void
get "name"(): string
get "hidden"(): boolean
set "name"(value: string)
get "displayName"(): string
set "displayName"(value: string)
get "expert"(): boolean
set "expert"(value: boolean)
get "preferred"(): boolean
set "preferred"(value: boolean)
get "shortDescription"(): string
set "shortDescription"(value: string)
set "hidden"(value: boolean)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FeatureDescriptor$Type = ($FeatureDescriptor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FeatureDescriptor_ = $FeatureDescriptor$Type;
}}
declare module "packages/java/beans/$PropertyChangeEvent" {
import {$EventObject, $EventObject$Type} from "packages/java/util/$EventObject"

export class $PropertyChangeEvent extends $EventObject {

constructor(arg0: any, arg1: string, arg2: any, arg3: any)

public "toString"(): string
public "getOldValue"(): any
public "getNewValue"(): any
public "getPropagationId"(): any
public "setPropagationId"(arg0: any): void
public "getPropertyName"(): string
get "oldValue"(): any
get "newValue"(): any
get "propagationId"(): any
set "propagationId"(value: any)
get "propertyName"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PropertyChangeEvent$Type = ($PropertyChangeEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PropertyChangeEvent_ = $PropertyChangeEvent$Type;
}}
declare module "packages/java/beans/$PropertyDescriptor" {
import {$FeatureDescriptor, $FeatureDescriptor$Type} from "packages/java/beans/$FeatureDescriptor"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$Method, $Method$Type} from "packages/java/lang/reflect/$Method"
import {$PropertyEditor, $PropertyEditor$Type} from "packages/java/beans/$PropertyEditor"

export class $PropertyDescriptor extends $FeatureDescriptor {

constructor(arg0: string, arg1: $Class$Type<(any)>)
constructor(arg0: string, arg1: $Class$Type<(any)>, arg2: string, arg3: string)
constructor(arg0: string, arg1: $Method$Type, arg2: $Method$Type)

public "equals"(arg0: any): boolean
public "hashCode"(): integer
public "isBound"(): boolean
public "getWriteMethod"(): $Method
public "setReadMethod"(arg0: $Method$Type): void
public "setWriteMethod"(arg0: $Method$Type): void
public "isConstrained"(): boolean
public "setConstrained"(arg0: boolean): void
public "setBound"(arg0: boolean): void
public "getPropertyEditorClass"(): $Class<(any)>
public "setPropertyEditorClass"(arg0: $Class$Type<(any)>): void
public "createPropertyEditor"(arg0: any): $PropertyEditor
public "getReadMethod"(): $Method
public "getPropertyType"(): $Class<(any)>
get "bound"(): boolean
get "writeMethod"(): $Method
set "readMethod"(value: $Method$Type)
set "writeMethod"(value: $Method$Type)
get "constrained"(): boolean
set "constrained"(value: boolean)
set "bound"(value: boolean)
get "propertyEditorClass"(): $Class<(any)>
set "propertyEditorClass"(value: $Class$Type<(any)>)
get "readMethod"(): $Method
get "propertyType"(): $Class<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PropertyDescriptor$Type = ($PropertyDescriptor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PropertyDescriptor_ = $PropertyDescriptor$Type;
}}

declare module "packages/javax/print/event/$PrintServiceAttributeListener" {
import {$PrintServiceAttributeEvent, $PrintServiceAttributeEvent$Type} from "packages/javax/print/event/$PrintServiceAttributeEvent"

export interface $PrintServiceAttributeListener {

 "attributeUpdate"(arg0: $PrintServiceAttributeEvent$Type): void

(arg0: $PrintServiceAttributeEvent$Type): void
}

export namespace $PrintServiceAttributeListener {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PrintServiceAttributeListener$Type = ($PrintServiceAttributeListener);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PrintServiceAttributeListener_ = $PrintServiceAttributeListener$Type;
}}
declare module "packages/javax/print/$PrintService" {
import {$DocPrintJob, $DocPrintJob$Type} from "packages/javax/print/$DocPrintJob"
import {$PrintServiceAttributeListener, $PrintServiceAttributeListener$Type} from "packages/javax/print/event/$PrintServiceAttributeListener"
import {$PrintServiceAttribute, $PrintServiceAttribute$Type} from "packages/javax/print/attribute/$PrintServiceAttribute"
import {$DocFlavor, $DocFlavor$Type} from "packages/javax/print/$DocFlavor"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$PrintServiceAttributeSet, $PrintServiceAttributeSet$Type} from "packages/javax/print/attribute/$PrintServiceAttributeSet"
import {$AttributeSet, $AttributeSet$Type} from "packages/javax/print/attribute/$AttributeSet"
import {$Attribute, $Attribute$Type} from "packages/javax/print/attribute/$Attribute"
import {$ServiceUIFactory, $ServiceUIFactory$Type} from "packages/javax/print/$ServiceUIFactory"

export interface $PrintService {

 "getName"(): string
 "equals"(arg0: any): boolean
 "hashCode"(): integer
 "getAttributes"(): $PrintServiceAttributeSet
 "getAttribute"<T extends $PrintServiceAttribute>(arg0: $Class$Type<(T)>): T
 "createPrintJob"(): $DocPrintJob
 "addPrintServiceAttributeListener"(arg0: $PrintServiceAttributeListener$Type): void
 "removePrintServiceAttributeListener"(arg0: $PrintServiceAttributeListener$Type): void
 "getSupportedDocFlavors"(): ($DocFlavor)[]
 "isDocFlavorSupported"(arg0: $DocFlavor$Type): boolean
 "getSupportedAttributeCategories"(): ($Class<(any)>)[]
 "isAttributeCategorySupported"(arg0: $Class$Type<(any)>): boolean
 "getDefaultAttributeValue"(arg0: $Class$Type<(any)>): any
 "getSupportedAttributeValues"(arg0: $Class$Type<(any)>, arg1: $DocFlavor$Type, arg2: $AttributeSet$Type): any
 "isAttributeValueSupported"(arg0: $Attribute$Type, arg1: $DocFlavor$Type, arg2: $AttributeSet$Type): boolean
 "getUnsupportedAttributes"(arg0: $DocFlavor$Type, arg1: $AttributeSet$Type): $AttributeSet
 "getServiceUIFactory"(): $ServiceUIFactory
}

export namespace $PrintService {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PrintService$Type = ($PrintService);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PrintService_ = $PrintService$Type;
}}
declare module "packages/javax/print/attribute/$PrintRequestAttributeSet" {
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$AttributeSet, $AttributeSet$Type} from "packages/javax/print/attribute/$AttributeSet"
import {$Attribute, $Attribute$Type} from "packages/javax/print/attribute/$Attribute"

export interface $PrintRequestAttributeSet extends $AttributeSet {

 "add"(arg0: $Attribute$Type): boolean
 "addAll"(arg0: $AttributeSet$Type): boolean
 "remove"(arg0: $Class$Type<(any)>): boolean
 "remove"(arg0: $Attribute$Type): boolean
 "get"(arg0: $Class$Type<(any)>): $Attribute
 "equals"(arg0: any): boolean
 "hashCode"(): integer
 "clear"(): void
 "isEmpty"(): boolean
 "size"(): integer
 "toArray"(): ($Attribute)[]
 "containsKey"(arg0: $Class$Type<(any)>): boolean
 "containsValue"(arg0: $Attribute$Type): boolean
}

export namespace $PrintRequestAttributeSet {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PrintRequestAttributeSet$Type = ($PrintRequestAttributeSet);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PrintRequestAttributeSet_ = $PrintRequestAttributeSet$Type;
}}
declare module "packages/javax/print/attribute/$Attribute" {
import {$Serializable, $Serializable$Type} from "packages/java/io/$Serializable"
import {$Class, $Class$Type} from "packages/java/lang/$Class"

export interface $Attribute extends $Serializable {

 "getName"(): string
 "getCategory"(): $Class<(any)>
}

export namespace $Attribute {
const probejs$$marker: never
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
declare module "packages/javax/print/$DocFlavor" {
import {$Serializable, $Serializable$Type} from "packages/java/io/$Serializable"
import {$Cloneable, $Cloneable$Type} from "packages/java/lang/$Cloneable"

export class $DocFlavor implements $Serializable, $Cloneable {
static readonly "hostEncoding": string

constructor(arg0: string, arg1: string)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "getParameter"(arg0: string): string
public "getMimeType"(): string
public "getMediaType"(): string
public "getMediaSubtype"(): string
public "getRepresentationClassName"(): string
get "mimeType"(): string
get "mediaType"(): string
get "mediaSubtype"(): string
get "representationClassName"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DocFlavor$Type = ($DocFlavor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DocFlavor_ = $DocFlavor$Type;
}}
declare module "packages/javax/print/event/$PrintJobAttributeEvent" {
import {$DocPrintJob, $DocPrintJob$Type} from "packages/javax/print/$DocPrintJob"
import {$PrintEvent, $PrintEvent$Type} from "packages/javax/print/event/$PrintEvent"
import {$PrintJobAttributeSet, $PrintJobAttributeSet$Type} from "packages/javax/print/attribute/$PrintJobAttributeSet"

export class $PrintJobAttributeEvent extends $PrintEvent {

constructor(arg0: $DocPrintJob$Type, arg1: $PrintJobAttributeSet$Type)

public "getAttributes"(): $PrintJobAttributeSet
public "getPrintJob"(): $DocPrintJob
get "attributes"(): $PrintJobAttributeSet
get "printJob"(): $DocPrintJob
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PrintJobAttributeEvent$Type = ($PrintJobAttributeEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PrintJobAttributeEvent_ = $PrintJobAttributeEvent$Type;
}}
declare module "packages/javax/print/event/$PrintJobListener" {
import {$PrintJobEvent, $PrintJobEvent$Type} from "packages/javax/print/event/$PrintJobEvent"

export interface $PrintJobListener {

 "printDataTransferCompleted"(arg0: $PrintJobEvent$Type): void
 "printJobCompleted"(arg0: $PrintJobEvent$Type): void
 "printJobFailed"(arg0: $PrintJobEvent$Type): void
 "printJobCanceled"(arg0: $PrintJobEvent$Type): void
 "printJobNoMoreEvents"(arg0: $PrintJobEvent$Type): void
 "printJobRequiresAttention"(arg0: $PrintJobEvent$Type): void
}

export namespace $PrintJobListener {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PrintJobListener$Type = ($PrintJobListener);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PrintJobListener_ = $PrintJobListener$Type;
}}
declare module "packages/javax/print/attribute/$AttributeSet" {
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$Attribute, $Attribute$Type} from "packages/javax/print/attribute/$Attribute"

export interface $AttributeSet {

 "add"(arg0: $Attribute$Type): boolean
 "remove"(arg0: $Class$Type<(any)>): boolean
 "remove"(arg0: $Attribute$Type): boolean
 "get"(arg0: $Class$Type<(any)>): $Attribute
 "equals"(arg0: any): boolean
 "hashCode"(): integer
 "clear"(): void
 "isEmpty"(): boolean
 "size"(): integer
 "toArray"(): ($Attribute)[]
 "addAll"(arg0: $AttributeSet$Type): boolean
 "containsKey"(arg0: $Class$Type<(any)>): boolean
 "containsValue"(arg0: $Attribute$Type): boolean
}

export namespace $AttributeSet {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AttributeSet$Type = ($AttributeSet);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AttributeSet_ = $AttributeSet$Type;
}}
declare module "packages/javax/print/event/$PrintJobEvent" {
import {$DocPrintJob, $DocPrintJob$Type} from "packages/javax/print/$DocPrintJob"
import {$PrintEvent, $PrintEvent$Type} from "packages/javax/print/event/$PrintEvent"

export class $PrintJobEvent extends $PrintEvent {
static readonly "JOB_CANCELED": integer
static readonly "JOB_COMPLETE": integer
static readonly "JOB_FAILED": integer
static readonly "REQUIRES_ATTENTION": integer
static readonly "NO_MORE_EVENTS": integer
static readonly "DATA_TRANSFER_COMPLETE": integer

constructor(arg0: $DocPrintJob$Type, arg1: integer)

public "getPrintJob"(): $DocPrintJob
public "getPrintEventType"(): integer
get "printJob"(): $DocPrintJob
get "printEventType"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PrintJobEvent$Type = ($PrintJobEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PrintJobEvent_ = $PrintJobEvent$Type;
}}
declare module "packages/javax/print/attribute/$PrintServiceAttributeSet" {
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$AttributeSet, $AttributeSet$Type} from "packages/javax/print/attribute/$AttributeSet"
import {$Attribute, $Attribute$Type} from "packages/javax/print/attribute/$Attribute"

export interface $PrintServiceAttributeSet extends $AttributeSet {

 "add"(arg0: $Attribute$Type): boolean
 "addAll"(arg0: $AttributeSet$Type): boolean
 "remove"(arg0: $Class$Type<(any)>): boolean
 "remove"(arg0: $Attribute$Type): boolean
 "get"(arg0: $Class$Type<(any)>): $Attribute
 "equals"(arg0: any): boolean
 "hashCode"(): integer
 "clear"(): void
 "isEmpty"(): boolean
 "size"(): integer
 "toArray"(): ($Attribute)[]
 "containsKey"(arg0: $Class$Type<(any)>): boolean
 "containsValue"(arg0: $Attribute$Type): boolean
}

export namespace $PrintServiceAttributeSet {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PrintServiceAttributeSet$Type = ($PrintServiceAttributeSet);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PrintServiceAttributeSet_ = $PrintServiceAttributeSet$Type;
}}
declare module "packages/javax/print/event/$PrintEvent" {
import {$EventObject, $EventObject$Type} from "packages/java/util/$EventObject"

export class $PrintEvent extends $EventObject {

constructor(arg0: any)

public "toString"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PrintEvent$Type = ($PrintEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PrintEvent_ = $PrintEvent$Type;
}}
declare module "packages/javax/print/$Doc" {
import {$DocFlavor, $DocFlavor$Type} from "packages/javax/print/$DocFlavor"
import {$DocAttributeSet, $DocAttributeSet$Type} from "packages/javax/print/attribute/$DocAttributeSet"
import {$InputStream, $InputStream$Type} from "packages/java/io/$InputStream"
import {$Reader, $Reader$Type} from "packages/java/io/$Reader"

export interface $Doc {

 "getAttributes"(): $DocAttributeSet
 "getReaderForText"(): $Reader
 "getDocFlavor"(): $DocFlavor
 "getPrintData"(): any
 "getStreamForBytes"(): $InputStream
}

export namespace $Doc {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Doc$Type = ($Doc);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Doc_ = $Doc$Type;
}}
declare module "packages/javax/print/$ServiceUIFactory" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $ServiceUIFactory {
static readonly "JCOMPONENT_UI": string
static readonly "PANEL_UI": string
static readonly "DIALOG_UI": string
static readonly "JDIALOG_UI": string
static readonly "ABOUT_UIROLE": integer
static readonly "ADMIN_UIROLE": integer
static readonly "MAIN_UIROLE": integer
static readonly "RESERVED_UIROLE": integer


public "getUIClassNamesForRole"(arg0: integer): (string)[]
public "getUI"(arg0: integer, arg1: string): any
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ServiceUIFactory$Type = ($ServiceUIFactory);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ServiceUIFactory_ = $ServiceUIFactory$Type;
}}
declare module "packages/javax/print/event/$PrintServiceAttributeEvent" {
import {$PrintEvent, $PrintEvent$Type} from "packages/javax/print/event/$PrintEvent"
import {$PrintService, $PrintService$Type} from "packages/javax/print/$PrintService"
import {$PrintServiceAttributeSet, $PrintServiceAttributeSet$Type} from "packages/javax/print/attribute/$PrintServiceAttributeSet"

export class $PrintServiceAttributeEvent extends $PrintEvent {

constructor(arg0: $PrintService$Type, arg1: $PrintServiceAttributeSet$Type)

public "getAttributes"(): $PrintServiceAttributeSet
public "getPrintService"(): $PrintService
get "attributes"(): $PrintServiceAttributeSet
get "printService"(): $PrintService
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PrintServiceAttributeEvent$Type = ($PrintServiceAttributeEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PrintServiceAttributeEvent_ = $PrintServiceAttributeEvent$Type;
}}
declare module "packages/javax/print/$DocPrintJob" {
import {$PrintJobAttributeListener, $PrintJobAttributeListener$Type} from "packages/javax/print/event/$PrintJobAttributeListener"
import {$PrintJobListener, $PrintJobListener$Type} from "packages/javax/print/event/$PrintJobListener"
import {$Doc, $Doc$Type} from "packages/javax/print/$Doc"
import {$PrintRequestAttributeSet, $PrintRequestAttributeSet$Type} from "packages/javax/print/attribute/$PrintRequestAttributeSet"
import {$PrintService, $PrintService$Type} from "packages/javax/print/$PrintService"
import {$PrintJobAttributeSet, $PrintJobAttributeSet$Type} from "packages/javax/print/attribute/$PrintJobAttributeSet"

export interface $DocPrintJob {

 "print"(arg0: $Doc$Type, arg1: $PrintRequestAttributeSet$Type): void
 "getAttributes"(): $PrintJobAttributeSet
 "getPrintService"(): $PrintService
 "addPrintJobListener"(arg0: $PrintJobListener$Type): void
 "removePrintJobListener"(arg0: $PrintJobListener$Type): void
 "addPrintJobAttributeListener"(arg0: $PrintJobAttributeListener$Type, arg1: $PrintJobAttributeSet$Type): void
 "removePrintJobAttributeListener"(arg0: $PrintJobAttributeListener$Type): void
}

export namespace $DocPrintJob {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DocPrintJob$Type = ($DocPrintJob);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DocPrintJob_ = $DocPrintJob$Type;
}}
declare module "packages/javax/print/attribute/$DocAttributeSet" {
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$AttributeSet, $AttributeSet$Type} from "packages/javax/print/attribute/$AttributeSet"
import {$Attribute, $Attribute$Type} from "packages/javax/print/attribute/$Attribute"

export interface $DocAttributeSet extends $AttributeSet {

 "add"(arg0: $Attribute$Type): boolean
 "addAll"(arg0: $AttributeSet$Type): boolean
 "remove"(arg0: $Class$Type<(any)>): boolean
 "remove"(arg0: $Attribute$Type): boolean
 "get"(arg0: $Class$Type<(any)>): $Attribute
 "equals"(arg0: any): boolean
 "hashCode"(): integer
 "clear"(): void
 "isEmpty"(): boolean
 "size"(): integer
 "toArray"(): ($Attribute)[]
 "containsKey"(arg0: $Class$Type<(any)>): boolean
 "containsValue"(arg0: $Attribute$Type): boolean
}

export namespace $DocAttributeSet {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DocAttributeSet$Type = ($DocAttributeSet);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DocAttributeSet_ = $DocAttributeSet$Type;
}}
declare module "packages/javax/print/event/$PrintJobAttributeListener" {
import {$PrintJobAttributeEvent, $PrintJobAttributeEvent$Type} from "packages/javax/print/event/$PrintJobAttributeEvent"

export interface $PrintJobAttributeListener {

 "attributeUpdate"(arg0: $PrintJobAttributeEvent$Type): void

(arg0: $PrintJobAttributeEvent$Type): void
}

export namespace $PrintJobAttributeListener {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PrintJobAttributeListener$Type = ($PrintJobAttributeListener);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PrintJobAttributeListener_ = $PrintJobAttributeListener$Type;
}}
declare module "packages/javax/print/attribute/$PrintJobAttributeSet" {
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$AttributeSet, $AttributeSet$Type} from "packages/javax/print/attribute/$AttributeSet"
import {$Attribute, $Attribute$Type} from "packages/javax/print/attribute/$Attribute"

export interface $PrintJobAttributeSet extends $AttributeSet {

 "add"(arg0: $Attribute$Type): boolean
 "addAll"(arg0: $AttributeSet$Type): boolean
 "remove"(arg0: $Class$Type<(any)>): boolean
 "remove"(arg0: $Attribute$Type): boolean
 "get"(arg0: $Class$Type<(any)>): $Attribute
 "equals"(arg0: any): boolean
 "hashCode"(): integer
 "clear"(): void
 "isEmpty"(): boolean
 "size"(): integer
 "toArray"(): ($Attribute)[]
 "containsKey"(arg0: $Class$Type<(any)>): boolean
 "containsValue"(arg0: $Attribute$Type): boolean
}

export namespace $PrintJobAttributeSet {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PrintJobAttributeSet$Type = ($PrintJobAttributeSet);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PrintJobAttributeSet_ = $PrintJobAttributeSet$Type;
}}
declare module "packages/javax/print/attribute/$PrintServiceAttribute" {
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$Attribute, $Attribute$Type} from "packages/javax/print/attribute/$Attribute"

export interface $PrintServiceAttribute extends $Attribute {

 "getName"(): string
 "getCategory"(): $Class<(any)>
}

export namespace $PrintServiceAttribute {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PrintServiceAttribute$Type = ($PrintServiceAttribute);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PrintServiceAttribute_ = $PrintServiceAttribute$Type;
}}

declare module "packages/javax/xml/stream/$Location" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $Location {

 "getLineNumber"(): integer
 "getSystemId"(): string
 "getPublicId"(): string
 "getCharacterOffset"(): integer
 "getColumnNumber"(): integer
}

export namespace $Location {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Location$Type = ($Location);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Location_ = $Location$Type;
}}
declare module "packages/javax/xml/stream/$XMLStreamReader" {
import {$QName, $QName$Type} from "packages/javax/xml/namespace/$QName"
import {$NamespaceContext, $NamespaceContext$Type} from "packages/javax/xml/namespace/$NamespaceContext"
import {$Location, $Location$Type} from "packages/javax/xml/stream/$Location"
import {$XMLStreamConstants, $XMLStreamConstants$Type} from "packages/javax/xml/stream/$XMLStreamConstants"

export interface $XMLStreamReader extends $XMLStreamConstants {

 "getName"(): $QName
 "getProperty"(arg0: string): any
 "hasNext"(): boolean
 "next"(): integer
 "getLocation"(): $Location
 "close"(): void
 "getEncoding"(): string
 "nextTag"(): integer
 "getText"(): string
 "isStandalone"(): boolean
 "getVersion"(): string
 "getTextLength"(): integer
 "getAttributeCount"(): integer
 "getAttributeValue"(arg0: string, arg1: string): string
 "getAttributeValue"(arg0: integer): string
 "isCharacters"(): boolean
 "getTextCharacters"(arg0: integer, arg1: (character)[], arg2: integer, arg3: integer): integer
 "getTextCharacters"(): (character)[]
 "getTextStart"(): integer
 "isWhiteSpace"(): boolean
 "hasName"(): boolean
 "isEndElement"(): boolean
 "isStartElement"(): boolean
 "getAttributeNamespace"(arg0: integer): string
 "getAttributePrefix"(arg0: integer): string
 "getElementText"(): string
 "getNamespacePrefix"(arg0: integer): string
 "hasText"(): boolean
 "isAttributeSpecified"(arg0: integer): boolean
 "getPrefix"(): string
 "getAttributeLocalName"(arg0: integer): string
 "getCharacterEncodingScheme"(): string
 "getNamespaceContext"(): $NamespaceContext
 "getPITarget"(): string
 "getPIData"(): string
 "standaloneSet"(): boolean
 "getLocalName"(): string
 "getNamespaceURI"(arg0: integer): string
 "getNamespaceURI"(arg0: string): string
 "getNamespaceURI"(): string
 "getAttributeName"(arg0: integer): $QName
 "getAttributeType"(arg0: integer): string
 "getEventType"(): integer
 "getNamespaceCount"(): integer
 "require"(arg0: integer, arg1: string, arg2: string): void
}

export namespace $XMLStreamReader {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $XMLStreamReader$Type = ($XMLStreamReader);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $XMLStreamReader_ = $XMLStreamReader$Type;
}}
declare module "packages/javax/xml/namespace/$NamespaceContext" {
import {$Iterator, $Iterator$Type} from "packages/java/util/$Iterator"

export interface $NamespaceContext {

 "getPrefix"(arg0: string): string
 "getPrefixes"(arg0: string): $Iterator<(string)>
 "getNamespaceURI"(arg0: string): string
}

export namespace $NamespaceContext {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NamespaceContext$Type = ($NamespaceContext);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NamespaceContext_ = $NamespaceContext$Type;
}}
declare module "packages/javax/xml/stream/$XMLStreamConstants" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $XMLStreamConstants {

}

export namespace $XMLStreamConstants {
const START_ELEMENT: integer
const END_ELEMENT: integer
const PROCESSING_INSTRUCTION: integer
const CHARACTERS: integer
const COMMENT: integer
const SPACE: integer
const START_DOCUMENT: integer
const END_DOCUMENT: integer
const ENTITY_REFERENCE: integer
const ATTRIBUTE: integer
const DTD: integer
const CDATA: integer
const NAMESPACE: integer
const NOTATION_DECLARATION: integer
const ENTITY_DECLARATION: integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $XMLStreamConstants$Type = ($XMLStreamConstants);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $XMLStreamConstants_ = $XMLStreamConstants$Type;
}}
declare module "packages/javax/xml/namespace/$QName" {
import {$Serializable, $Serializable$Type} from "packages/java/io/$Serializable"

export class $QName implements $Serializable {

constructor(arg0: string)
constructor(arg0: string, arg1: string, arg2: string)
constructor(arg0: string, arg1: string)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public static "valueOf"(arg0: string): $QName
public "getPrefix"(): string
public "getNamespaceURI"(): string
public "getLocalPart"(): string
get "prefix"(): string
get "namespaceURI"(): string
get "localPart"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $QName$Type = ($QName);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $QName_ = $QName$Type;
}}
declare module "packages/javax/xml/transform/$Source" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $Source {

 "isEmpty"(): boolean
 "getSystemId"(): string
 "setSystemId"(arg0: string): void
}

export namespace $Source {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Source$Type = ($Source);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Source_ = $Source$Type;
}}
declare module "packages/javax/xml/transform/$Result" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $Result {

 "getSystemId"(): string
 "setSystemId"(arg0: string): void
}

export namespace $Result {
const PI_DISABLE_OUTPUT_ESCAPING: string
const PI_ENABLE_OUTPUT_ESCAPING: string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Result$Type = ($Result);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Result_ = $Result$Type;
}}

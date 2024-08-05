declare module "packages/javax/tools/$JavaFileManager$Location" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $JavaFileManager$Location {

 "isOutputLocation"(): boolean
 "getName"(): string
 "isModuleOrientedLocation"(): boolean
}

export namespace $JavaFileManager$Location {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JavaFileManager$Location$Type = ($JavaFileManager$Location);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JavaFileManager$Location_ = $JavaFileManager$Location$Type;
}}
declare module "packages/javax/tools/$JavaFileObject" {
import {$NestingKind, $NestingKind$Type} from "packages/javax/lang/model/element/$NestingKind"
import {$Modifier, $Modifier$Type} from "packages/javax/lang/model/element/$Modifier"
import {$OutputStream, $OutputStream$Type} from "packages/java/io/$OutputStream"
import {$InputStream, $InputStream$Type} from "packages/java/io/$InputStream"
import {$URI, $URI$Type} from "packages/java/net/$URI"
import {$FileObject, $FileObject$Type} from "packages/javax/tools/$FileObject"
import {$Writer, $Writer$Type} from "packages/java/io/$Writer"
import {$JavaFileObject$Kind, $JavaFileObject$Kind$Type} from "packages/javax/tools/$JavaFileObject$Kind"
import {$Reader, $Reader$Type} from "packages/java/io/$Reader"

export interface $JavaFileObject extends $FileObject {

 "isNameCompatible"(arg0: string, arg1: $JavaFileObject$Kind$Type): boolean
 "getAccessLevel"(): $Modifier
 "getKind"(): $JavaFileObject$Kind
 "getNestingKind"(): $NestingKind
 "getCharContent"(arg0: boolean): charseq
 "openReader"(arg0: boolean): $Reader
 "openWriter"(): $Writer
 "getName"(): string
 "delete"(): boolean
 "toUri"(): $URI
 "getLastModified"(): long
 "openInputStream"(): $InputStream
 "openOutputStream"(): $OutputStream
}

export namespace $JavaFileObject {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JavaFileObject$Type = ($JavaFileObject);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JavaFileObject_ = $JavaFileObject$Type;
}}
declare module "packages/javax/tools/$Diagnostic$Kind" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $Diagnostic$Kind extends $Enum<($Diagnostic$Kind)> {
static readonly "ERROR": $Diagnostic$Kind
static readonly "WARNING": $Diagnostic$Kind
static readonly "MANDATORY_WARNING": $Diagnostic$Kind
static readonly "NOTE": $Diagnostic$Kind
static readonly "OTHER": $Diagnostic$Kind


public static "values"(): ($Diagnostic$Kind)[]
public static "valueOf"(arg0: string): $Diagnostic$Kind
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Diagnostic$Kind$Type = (("note") | ("other") | ("mandatory_warning") | ("warning") | ("error")) | ($Diagnostic$Kind);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Diagnostic$Kind_ = $Diagnostic$Kind$Type;
}}
declare module "packages/javax/tools/$FileObject" {
import {$OutputStream, $OutputStream$Type} from "packages/java/io/$OutputStream"
import {$InputStream, $InputStream$Type} from "packages/java/io/$InputStream"
import {$URI, $URI$Type} from "packages/java/net/$URI"
import {$Writer, $Writer$Type} from "packages/java/io/$Writer"
import {$Reader, $Reader$Type} from "packages/java/io/$Reader"

export interface $FileObject {

 "getCharContent"(arg0: boolean): charseq
 "openReader"(arg0: boolean): $Reader
 "openWriter"(): $Writer
 "getName"(): string
 "delete"(): boolean
 "toUri"(): $URI
 "getLastModified"(): long
 "openInputStream"(): $InputStream
 "openOutputStream"(): $OutputStream
}

export namespace $FileObject {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FileObject$Type = ($FileObject);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FileObject_ = $FileObject$Type;
}}
declare module "packages/javax/tools/$JavaFileObject$Kind" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $JavaFileObject$Kind extends $Enum<($JavaFileObject$Kind)> {
static readonly "SOURCE": $JavaFileObject$Kind
static readonly "CLASS": $JavaFileObject$Kind
static readonly "HTML": $JavaFileObject$Kind
static readonly "OTHER": $JavaFileObject$Kind
readonly "extension": string


public static "values"(): ($JavaFileObject$Kind)[]
public static "valueOf"(arg0: string): $JavaFileObject$Kind
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JavaFileObject$Kind$Type = (("other") | ("html") | ("source") | ("class")) | ($JavaFileObject$Kind);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JavaFileObject$Kind_ = $JavaFileObject$Kind$Type;
}}

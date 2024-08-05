declare module "packages/cpw/mods/jarhandling/impl/$SimpleJarMetadata" {
import {$ModuleDescriptor, $ModuleDescriptor$Type} from "packages/java/lang/module/$ModuleDescriptor"
import {$SecureJar, $SecureJar$Type} from "packages/cpw/mods/jarhandling/$SecureJar"
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$SecureJar$Provider, $SecureJar$Provider$Type} from "packages/cpw/mods/jarhandling/$SecureJar$Provider"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$List, $List$Type} from "packages/java/util/$List"
import {$JarMetadata, $JarMetadata$Type} from "packages/cpw/mods/jarhandling/$JarMetadata"

export class $SimpleJarMetadata extends $Record implements $JarMetadata {

constructor(name: string, version: string, pkgs: $Set$Type<(string)>, providers: $List$Type<($SecureJar$Provider$Type)>)

public "name"(): string
public "equals"(arg0: any): boolean
public "toString"(): string
public "version"(): string
public "hashCode"(): integer
public "pkgs"(): $Set<(string)>
public "descriptor"(): $ModuleDescriptor
public "providers"(): $List<($SecureJar$Provider)>
public static "from"(arg0: $SecureJar$Type, ...arg1: ($Path$Type)[]): $JarMetadata
public static "fromFileName"(arg0: $Path$Type, arg1: $Set$Type<(string)>, arg2: $List$Type<($SecureJar$Provider$Type)>): $SimpleJarMetadata
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SimpleJarMetadata$Type = ($SimpleJarMetadata);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SimpleJarMetadata_ = $SimpleJarMetadata$Type;
}}
declare module "packages/cpw/mods/jarhandling/$SecureJar$ModuleDataProvider" {
import {$ModuleDescriptor, $ModuleDescriptor$Type} from "packages/java/lang/module/$ModuleDescriptor"
import {$CodeSigner, $CodeSigner$Type} from "packages/java/security/$CodeSigner"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$InputStream, $InputStream$Type} from "packages/java/io/$InputStream"
import {$URI, $URI$Type} from "packages/java/net/$URI"
import {$Manifest, $Manifest$Type} from "packages/java/util/jar/$Manifest"

export interface $SecureJar$ModuleDataProvider {

 "name"(): string
 "descriptor"(): $ModuleDescriptor
 "uri"(): $URI
 "open"(arg0: string): $Optional<($InputStream)>
 "getManifest"(): $Manifest
 "verifyAndGetSigners"(arg0: string, arg1: (byte)[]): ($CodeSigner)[]
 "findFile"(arg0: string): $Optional<($URI)>
}

export namespace $SecureJar$ModuleDataProvider {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SecureJar$ModuleDataProvider$Type = ($SecureJar$ModuleDataProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SecureJar$ModuleDataProvider_ = $SecureJar$ModuleDataProvider$Type;
}}
declare module "packages/cpw/mods/jarhandling/$SecureJar" {
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$BiPredicate, $BiPredicate$Type} from "packages/java/util/function/$BiPredicate"
import {$CodeSigner, $CodeSigner$Type} from "packages/java/security/$CodeSigner"
import {$Attributes, $Attributes$Type} from "packages/java/util/jar/$Attributes"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$SecureJar$Provider, $SecureJar$Provider$Type} from "packages/cpw/mods/jarhandling/$SecureJar$Provider"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$List, $List$Type} from "packages/java/util/$List"
import {$SecureJar$ModuleDataProvider, $SecureJar$ModuleDataProvider$Type} from "packages/cpw/mods/jarhandling/$SecureJar$ModuleDataProvider"
import {$SecureJar$Status, $SecureJar$Status$Type} from "packages/cpw/mods/jarhandling/$SecureJar$Status"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$Manifest, $Manifest$Type} from "packages/java/util/jar/$Manifest"
import {$JarMetadata, $JarMetadata$Type} from "packages/cpw/mods/jarhandling/$JarMetadata"

export interface $SecureJar {

 "name"(): string
 "getPackages"(): $Set<(string)>
 "getPath"(arg0: string, ...arg1: (string)[]): $Path
 "getProviders"(): $List<($SecureJar$Provider)>
 "getTrustedManifestEntries"(arg0: string): $Attributes
 "getManifestSigners"(): ($CodeSigner)[]
 "getPrimaryPath"(): $Path
 "verifyPath"(arg0: $Path$Type): $SecureJar$Status
 "moduleDataProvider"(): $SecureJar$ModuleDataProvider
 "hasSecurityData"(): boolean
 "getRootPath"(): $Path
 "getFileStatus"(arg0: string): $SecureJar$Status
}

export namespace $SecureJar {
function from(...arg0: ($Path$Type)[]): $SecureJar
function from(arg0: $Supplier$Type<($Manifest$Type)>, arg1: $Function$Type<($SecureJar$Type), ($JarMetadata$Type)>, ...arg2: ($Path$Type)[]): $SecureJar
function from(arg0: $Supplier$Type<($Manifest$Type)>, arg1: $Function$Type<($SecureJar$Type), ($JarMetadata$Type)>, arg2: $BiPredicate$Type<(string), (string)>, ...arg3: ($Path$Type)[]): $SecureJar
function from(arg0: $BiPredicate$Type<(string), (string)>, ...arg1: ($Path$Type)[]): $SecureJar
function from(arg0: $Function$Type<($SecureJar$Type), ($JarMetadata$Type)>, ...arg1: ($Path$Type)[]): $SecureJar
function from(arg0: $Function$Type<($SecureJar$Type), ($JarMetadata$Type)>, arg1: $BiPredicate$Type<(string), (string)>, ...arg2: ($Path$Type)[]): $SecureJar
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SecureJar$Type = ($SecureJar);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SecureJar_ = $SecureJar$Type;
}}
declare module "packages/cpw/mods/jarhandling/$SecureJar$Provider" {
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$BiPredicate, $BiPredicate$Type} from "packages/java/util/function/$BiPredicate"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$List, $List$Type} from "packages/java/util/$List"

export class $SecureJar$Provider extends $Record {

constructor(serviceName: string, providers: $List$Type<(string)>)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "providers"(): $List<(string)>
public "serviceName"(): string
public static "fromPath"(arg0: $Path$Type, arg1: $BiPredicate$Type<(string), (string)>): $SecureJar$Provider
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SecureJar$Provider$Type = ($SecureJar$Provider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SecureJar$Provider_ = $SecureJar$Provider$Type;
}}
declare module "packages/cpw/mods/jarhandling/$SecureJar$Status" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $SecureJar$Status extends $Enum<($SecureJar$Status)> {
static readonly "NONE": $SecureJar$Status
static readonly "INVALID": $SecureJar$Status
static readonly "UNVERIFIED": $SecureJar$Status
static readonly "VERIFIED": $SecureJar$Status


public static "values"(): ($SecureJar$Status)[]
public static "valueOf"(arg0: string): $SecureJar$Status
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SecureJar$Status$Type = (("invalid") | ("verified") | ("unverified") | ("none")) | ($SecureJar$Status);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SecureJar$Status_ = $SecureJar$Status$Type;
}}
declare module "packages/cpw/mods/jarhandling/$JarMetadata" {
import {$SecureJar, $SecureJar$Type} from "packages/cpw/mods/jarhandling/$SecureJar"
import {$ModuleDescriptor, $ModuleDescriptor$Type} from "packages/java/lang/module/$ModuleDescriptor"
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$Pattern, $Pattern$Type} from "packages/java/util/regex/$Pattern"
import {$SecureJar$Provider, $SecureJar$Provider$Type} from "packages/cpw/mods/jarhandling/$SecureJar$Provider"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$SimpleJarMetadata, $SimpleJarMetadata$Type} from "packages/cpw/mods/jarhandling/impl/$SimpleJarMetadata"
import {$List, $List$Type} from "packages/java/util/$List"

export interface $JarMetadata {

 "name"(): string
 "version"(): string
 "descriptor"(): $ModuleDescriptor
}

export namespace $JarMetadata {
const DASH_VERSION: $Pattern
const NON_ALPHANUM: $Pattern
const REPEATING_DOTS: $Pattern
const LEADING_DOTS: $Pattern
const TRAILING_DOTS: $Pattern
const MODULE_VERSION: $Pattern
const NUMBERLIKE_PARTS: $Pattern
const ILLEGAL_KEYWORDS: $List<(string)>
const KEYWORD_PARTS: $Pattern
function from(arg0: $SecureJar$Type, ...arg1: ($Path$Type)[]): $JarMetadata
function fromFileName(arg0: $Path$Type, arg1: $Set$Type<(string)>, arg2: $List$Type<($SecureJar$Provider$Type)>): $SimpleJarMetadata
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JarMetadata$Type = ($JarMetadata);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JarMetadata_ = $JarMetadata$Type;
}}

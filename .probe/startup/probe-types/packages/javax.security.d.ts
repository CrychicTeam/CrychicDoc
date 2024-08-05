declare module "packages/javax/security/auth/$Destroyable" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $Destroyable {

 "destroy"(): void
 "isDestroyed"(): boolean
}

export namespace $Destroyable {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Destroyable$Type = ($Destroyable);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Destroyable_ = $Destroyable$Type;
}}
declare module "packages/javax/security/auth/x500/$X500Principal" {
import {$Serializable, $Serializable$Type} from "packages/java/io/$Serializable"
import {$Subject, $Subject$Type} from "packages/javax/security/auth/$Subject"
import {$InputStream, $InputStream$Type} from "packages/java/io/$InputStream"
import {$Principal, $Principal$Type} from "packages/java/security/$Principal"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $X500Principal implements $Principal, $Serializable {
static readonly "RFC1779": string
static readonly "RFC2253": string
static readonly "CANONICAL": string

constructor(arg0: $InputStream$Type)
constructor(arg0: string)
constructor(arg0: string, arg1: $Map$Type<(string), (string)>)
constructor(arg0: (byte)[])

public "getName"(): string
public "getName"(arg0: string): string
public "getName"(arg0: string, arg1: $Map$Type<(string), (string)>): string
public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "getEncoded"(): (byte)[]
public "implies"(arg0: $Subject$Type): boolean
get "name"(): string
get "encoded"(): (byte)[]
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $X500Principal$Type = ($X500Principal);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $X500Principal_ = $X500Principal$Type;
}}
declare module "packages/javax/security/cert/$Certificate" {
import {$PublicKey, $PublicKey$Type} from "packages/java/security/$PublicKey"

/**
 * 
 * @deprecated
 */
export class $Certificate {

constructor()

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "getEncoded"(): (byte)[]
public "verify"(arg0: $PublicKey$Type, arg1: string): void
public "verify"(arg0: $PublicKey$Type): void
public "getPublicKey"(): $PublicKey
get "encoded"(): (byte)[]
get "publicKey"(): $PublicKey
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Certificate$Type = ($Certificate);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Certificate_ = $Certificate$Type;
}}
declare module "packages/javax/security/cert/$X509Certificate" {
import {$Certificate, $Certificate$Type} from "packages/javax/security/cert/$Certificate"
import {$Date, $Date$Type} from "packages/java/util/$Date"
import {$BigInteger, $BigInteger$Type} from "packages/java/math/$BigInteger"
import {$InputStream, $InputStream$Type} from "packages/java/io/$InputStream"
import {$Principal, $Principal$Type} from "packages/java/security/$Principal"

/**
 * 
 * @deprecated
 */
export class $X509Certificate extends $Certificate {

constructor()

public static "getInstance"(arg0: $InputStream$Type): $X509Certificate
public static "getInstance"(arg0: (byte)[]): $X509Certificate
public "getVersion"(): integer
public "getSerialNumber"(): $BigInteger
public "getIssuerDN"(): $Principal
public "getSigAlgName"(): string
public "getSigAlgParams"(): (byte)[]
public "checkValidity"(arg0: $Date$Type): void
public "checkValidity"(): void
public "getSubjectDN"(): $Principal
public "getNotBefore"(): $Date
public "getNotAfter"(): $Date
public "getSigAlgOID"(): string
get "version"(): integer
get "serialNumber"(): $BigInteger
get "issuerDN"(): $Principal
get "sigAlgName"(): string
get "sigAlgParams"(): (byte)[]
get "subjectDN"(): $Principal
get "notBefore"(): $Date
get "notAfter"(): $Date
get "sigAlgOID"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $X509Certificate$Type = ($X509Certificate);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $X509Certificate_ = $X509Certificate$Type;
}}
declare module "packages/javax/security/auth/$Subject" {
import {$Serializable, $Serializable$Type} from "packages/java/io/$Serializable"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$PrivilegedAction, $PrivilegedAction$Type} from "packages/java/security/$PrivilegedAction"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$Principal, $Principal$Type} from "packages/java/security/$Principal"
import {$AccessControlContext, $AccessControlContext$Type} from "packages/java/security/$AccessControlContext"
import {$PrivilegedExceptionAction, $PrivilegedExceptionAction$Type} from "packages/java/security/$PrivilegedExceptionAction"

export class $Subject implements $Serializable {

constructor()
constructor(arg0: boolean, arg1: $Set$Type<(any)>, arg2: $Set$Type<(any)>, arg3: $Set$Type<(any)>)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "setReadOnly"(): void
public "getPrincipals"<T extends $Principal>(arg0: $Class$Type<(T)>): $Set<(T)>
public "getPrincipals"(): $Set<($Principal)>
public "isReadOnly"(): boolean
/**
 * 
 * @deprecated
 */
public static "getSubject"(arg0: $AccessControlContext$Type): $Subject
public static "doAs"<T>(arg0: $Subject$Type, arg1: $PrivilegedExceptionAction$Type<(T)>): T
public static "doAs"<T>(arg0: $Subject$Type, arg1: $PrivilegedAction$Type<(T)>): T
/**
 * 
 * @deprecated
 */
public static "doAsPrivileged"<T>(arg0: $Subject$Type, arg1: $PrivilegedExceptionAction$Type<(T)>, arg2: $AccessControlContext$Type): T
/**
 * 
 * @deprecated
 */
public static "doAsPrivileged"<T>(arg0: $Subject$Type, arg1: $PrivilegedAction$Type<(T)>, arg2: $AccessControlContext$Type): T
public "getPublicCredentials"(): $Set<(any)>
public "getPublicCredentials"<T>(arg0: $Class$Type<(T)>): $Set<(T)>
public "getPrivateCredentials"(): $Set<(any)>
public "getPrivateCredentials"<T>(arg0: $Class$Type<(T)>): $Set<(T)>
get "principals"(): $Set<($Principal)>
get "readOnly"(): boolean
get "publicCredentials"(): $Set<(any)>
get "privateCredentials"(): $Set<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Subject$Type = ($Subject);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Subject_ = $Subject$Type;
}}

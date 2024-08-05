declare module "packages/java/security/$SecureRandom" {
import {$RandomGenerator, $RandomGenerator$Type} from "packages/java/util/random/$RandomGenerator"
import {$Random, $Random$Type} from "packages/java/util/$Random"
import {$SecureRandomParameters, $SecureRandomParameters$Type} from "packages/java/security/$SecureRandomParameters"
import {$Provider, $Provider$Type} from "packages/java/security/$Provider"

export class $SecureRandom extends $Random {

constructor(arg0: (byte)[])
constructor()

public "toString"(): string
public static "getInstance"(arg0: string, arg1: $SecureRandomParameters$Type, arg2: $Provider$Type): $SecureRandom
public static "getInstance"(arg0: string, arg1: $SecureRandomParameters$Type, arg2: string): $SecureRandom
public static "getInstance"(arg0: string, arg1: $SecureRandomParameters$Type): $SecureRandom
public static "getInstance"(arg0: string, arg1: $Provider$Type): $SecureRandom
public static "getInstance"(arg0: string, arg1: string): $SecureRandom
public static "getInstance"(arg0: string): $SecureRandom
public "getParameters"(): $SecureRandomParameters
public "nextBytes"(arg0: (byte)[], arg1: $SecureRandomParameters$Type): void
public "nextBytes"(arg0: (byte)[]): void
public "getProvider"(): $Provider
public "setSeed"(arg0: long): void
public "setSeed"(arg0: (byte)[]): void
public static "getSeed"(arg0: integer): (byte)[]
public "getAlgorithm"(): string
public "generateSeed"(arg0: integer): (byte)[]
public static "getInstanceStrong"(): $SecureRandom
public "reseed"(arg0: $SecureRandomParameters$Type): void
public "reseed"(): void
public static "getDefault"(): $RandomGenerator
public static "of"(arg0: string): $RandomGenerator
get "parameters"(): $SecureRandomParameters
get "provider"(): $Provider
set "seed"(value: long)
set "seed"(value: (byte)[])
get "algorithm"(): string
get "instanceStrong"(): $SecureRandom
get "default"(): $RandomGenerator
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SecureRandom$Type = ($SecureRandom);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SecureRandom_ = $SecureRandom$Type;
}}
declare module "packages/java/security/$CryptoPrimitive" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $CryptoPrimitive extends $Enum<($CryptoPrimitive)> {
static readonly "MESSAGE_DIGEST": $CryptoPrimitive
static readonly "SECURE_RANDOM": $CryptoPrimitive
static readonly "BLOCK_CIPHER": $CryptoPrimitive
static readonly "STREAM_CIPHER": $CryptoPrimitive
static readonly "MAC": $CryptoPrimitive
static readonly "KEY_WRAP": $CryptoPrimitive
static readonly "PUBLIC_KEY_ENCRYPTION": $CryptoPrimitive
static readonly "SIGNATURE": $CryptoPrimitive
static readonly "KEY_ENCAPSULATION": $CryptoPrimitive
static readonly "KEY_AGREEMENT": $CryptoPrimitive


public static "values"(): ($CryptoPrimitive)[]
public static "valueOf"(arg0: string): $CryptoPrimitive
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CryptoPrimitive$Type = (("public_key_encryption") | ("key_encapsulation") | ("message_digest") | ("signature") | ("key_agreement") | ("block_cipher") | ("key_wrap") | ("secure_random") | ("stream_cipher") | ("mac")) | ($CryptoPrimitive);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CryptoPrimitive_ = $CryptoPrimitive$Type;
}}
declare module "packages/java/security/$GeneralSecurityException" {
import {$Throwable, $Throwable$Type} from "packages/java/lang/$Throwable"
import {$Exception, $Exception$Type} from "packages/java/lang/$Exception"

export class $GeneralSecurityException extends $Exception {

constructor(arg0: $Throwable$Type)
constructor(arg0: string, arg1: $Throwable$Type)
constructor(arg0: string)
constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GeneralSecurityException$Type = ($GeneralSecurityException);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GeneralSecurityException_ = $GeneralSecurityException$Type;
}}
declare module "packages/java/security/$KeyPairGenerator" {
import {$KeyPairGeneratorSpi, $KeyPairGeneratorSpi$Type} from "packages/java/security/$KeyPairGeneratorSpi"
import {$SecureRandom, $SecureRandom$Type} from "packages/java/security/$SecureRandom"
import {$KeyPair, $KeyPair$Type} from "packages/java/security/$KeyPair"
import {$Provider, $Provider$Type} from "packages/java/security/$Provider"
import {$AlgorithmParameterSpec, $AlgorithmParameterSpec$Type} from "packages/java/security/spec/$AlgorithmParameterSpec"

export class $KeyPairGenerator extends $KeyPairGeneratorSpi {


public static "getInstance"(arg0: string, arg1: $Provider$Type): $KeyPairGenerator
public static "getInstance"(arg0: string, arg1: string): $KeyPairGenerator
public static "getInstance"(arg0: string): $KeyPairGenerator
public "initialize"(arg0: $AlgorithmParameterSpec$Type): void
public "initialize"(arg0: integer, arg1: $SecureRandom$Type): void
public "initialize"(arg0: $AlgorithmParameterSpec$Type, arg1: $SecureRandom$Type): void
public "initialize"(arg0: integer): void
public "getProvider"(): $Provider
public "getAlgorithm"(): string
public "generateKeyPair"(): $KeyPair
public "genKeyPair"(): $KeyPair
get "provider"(): $Provider
get "algorithm"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $KeyPairGenerator$Type = ($KeyPairGenerator);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $KeyPairGenerator_ = $KeyPairGenerator$Type;
}}
declare module "packages/java/security/cert/$CertPath" {
import {$Serializable, $Serializable$Type} from "packages/java/io/$Serializable"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Iterator, $Iterator$Type} from "packages/java/util/$Iterator"

export class $CertPath implements $Serializable {


public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "getCertificates"(): $List<(any)>
public "getType"(): string
public "getEncoded"(arg0: string): (byte)[]
public "getEncoded"(): (byte)[]
public "getEncodings"(): $Iterator<(string)>
get "certificates"(): $List<(any)>
get "type"(): string
get "encoded"(): (byte)[]
get "encodings"(): $Iterator<(string)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CertPath$Type = ($CertPath);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CertPath_ = $CertPath$Type;
}}
declare module "packages/java/security/$SecureRandomParameters" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $SecureRandomParameters {

}

export namespace $SecureRandomParameters {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SecureRandomParameters$Type = ($SecureRandomParameters);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SecureRandomParameters_ = $SecureRandomParameters$Type;
}}
declare module "packages/java/security/$AlgorithmParameters" {
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$Provider, $Provider$Type} from "packages/java/security/$Provider"
import {$AlgorithmParameterSpec, $AlgorithmParameterSpec$Type} from "packages/java/security/spec/$AlgorithmParameterSpec"

export class $AlgorithmParameters {


public "toString"(): string
public static "getInstance"(arg0: string, arg1: $Provider$Type): $AlgorithmParameters
public static "getInstance"(arg0: string, arg1: string): $AlgorithmParameters
public static "getInstance"(arg0: string): $AlgorithmParameters
public "init"(arg0: (byte)[]): void
public "init"(arg0: (byte)[], arg1: string): void
public "init"(arg0: $AlgorithmParameterSpec$Type): void
public "getEncoded"(): (byte)[]
public "getEncoded"(arg0: string): (byte)[]
public "getProvider"(): $Provider
public "getAlgorithm"(): string
public "getParameterSpec"<T extends $AlgorithmParameterSpec>(arg0: $Class$Type<(T)>): T
get "encoded"(): (byte)[]
get "provider"(): $Provider
get "algorithm"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AlgorithmParameters$Type = ($AlgorithmParameters);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AlgorithmParameters_ = $AlgorithmParameters$Type;
}}
declare module "packages/java/security/$DomainCombiner" {
import {$ProtectionDomain, $ProtectionDomain$Type} from "packages/java/security/$ProtectionDomain"

/**
 * 
 * @deprecated
 */
export interface $DomainCombiner {

 "combine"(arg0: ($ProtectionDomain$Type)[], arg1: ($ProtectionDomain$Type)[]): ($ProtectionDomain)[]

(arg0: ($ProtectionDomain$Type)[], arg1: ($ProtectionDomain$Type)[]): ($ProtectionDomain)[]
}

export namespace $DomainCombiner {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DomainCombiner$Type = ($DomainCombiner);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DomainCombiner_ = $DomainCombiner$Type;
}}
declare module "packages/java/security/spec/$KeySpec" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $KeySpec {

}

export namespace $KeySpec {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $KeySpec$Type = ($KeySpec);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $KeySpec_ = $KeySpec$Type;
}}
declare module "packages/java/security/cert/$X509Extension" {
import {$Set, $Set$Type} from "packages/java/util/$Set"

export interface $X509Extension {

 "getExtensionValue"(arg0: string): (byte)[]
 "getCriticalExtensionOIDs"(): $Set<(string)>
 "hasUnsupportedCriticalExtension"(): boolean
 "getNonCriticalExtensionOIDs"(): $Set<(string)>
}

export namespace $X509Extension {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $X509Extension$Type = ($X509Extension);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $X509Extension_ = $X509Extension$Type;
}}
declare module "packages/java/security/$KeyStore$ProtectionParameter" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $KeyStore$ProtectionParameter {

}

export namespace $KeyStore$ProtectionParameter {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $KeyStore$ProtectionParameter$Type = ($KeyStore$ProtectionParameter);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $KeyStore$ProtectionParameter_ = $KeyStore$ProtectionParameter$Type;
}}
declare module "packages/java/security/$KeyStore$LoadStoreParameter" {
import {$KeyStore$ProtectionParameter, $KeyStore$ProtectionParameter$Type} from "packages/java/security/$KeyStore$ProtectionParameter"

export interface $KeyStore$LoadStoreParameter {

 "getProtectionParameter"(): $KeyStore$ProtectionParameter

(): $KeyStore$ProtectionParameter
}

export namespace $KeyStore$LoadStoreParameter {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $KeyStore$LoadStoreParameter$Type = ($KeyStore$LoadStoreParameter);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $KeyStore$LoadStoreParameter_ = $KeyStore$LoadStoreParameter$Type;
}}
declare module "packages/java/security/$PublicKey" {
import {$Key, $Key$Type} from "packages/java/security/$Key"

export interface $PublicKey extends $Key {

 "getEncoded"(): (byte)[]
 "getFormat"(): string
 "getAlgorithm"(): string
}

export namespace $PublicKey {
const serialVersionUID: long
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PublicKey$Type = ($PublicKey);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PublicKey_ = $PublicKey$Type;
}}
declare module "packages/java/security/$Provider$Service" {
import {$List, $List$Type} from "packages/java/util/$List"
import {$Provider, $Provider$Type} from "packages/java/security/$Provider"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $Provider$Service {

constructor(arg0: $Provider$Type, arg1: string, arg2: string, arg3: string, arg4: $List$Type<(string)>, arg5: $Map$Type<(string), (string)>)

public "toString"(): string
public "newInstance"(arg0: any): any
public "getType"(): string
public "getClassName"(): string
public "getAttribute"(arg0: string): string
public "getProvider"(): $Provider
public "getAlgorithm"(): string
public "supportsParameter"(arg0: any): boolean
get "type"(): string
get "className"(): string
get "provider"(): $Provider
get "algorithm"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Provider$Service$Type = ($Provider$Service);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Provider$Service_ = $Provider$Service$Type;
}}
declare module "packages/java/security/$BasicPermission" {
import {$Serializable, $Serializable$Type} from "packages/java/io/$Serializable"
import {$Permission, $Permission$Type} from "packages/java/security/$Permission"
import {$PermissionCollection, $PermissionCollection$Type} from "packages/java/security/$PermissionCollection"

export class $BasicPermission extends $Permission implements $Serializable {

constructor(arg0: string, arg1: string)
constructor(arg0: string)

public "equals"(arg0: any): boolean
public "hashCode"(): integer
public "implies"(arg0: $Permission$Type): boolean
public "getActions"(): string
public "newPermissionCollection"(): $PermissionCollection
get "actions"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BasicPermission$Type = ($BasicPermission);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BasicPermission_ = $BasicPermission$Type;
}}
declare module "packages/java/security/$KeyStore$Entry" {
import {$KeyStore$Entry$Attribute, $KeyStore$Entry$Attribute$Type} from "packages/java/security/$KeyStore$Entry$Attribute"
import {$Set, $Set$Type} from "packages/java/util/$Set"

export interface $KeyStore$Entry {

 "getAttributes"(): $Set<($KeyStore$Entry$Attribute)>
}

export namespace $KeyStore$Entry {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $KeyStore$Entry$Type = ($KeyStore$Entry);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $KeyStore$Entry_ = $KeyStore$Entry$Type;
}}
declare module "packages/java/security/$MessageDigest" {
import {$ByteBuffer, $ByteBuffer$Type} from "packages/java/nio/$ByteBuffer"
import {$Provider, $Provider$Type} from "packages/java/security/$Provider"
import {$MessageDigestSpi, $MessageDigestSpi$Type} from "packages/java/security/$MessageDigestSpi"

export class $MessageDigest extends $MessageDigestSpi {


public "toString"(): string
public "clone"(): any
public "update"(arg0: (byte)[]): void
public "update"(arg0: (byte)[], arg1: integer, arg2: integer): void
public "update"(arg0: $ByteBuffer$Type): void
public "update"(arg0: byte): void
public static "getInstance"(arg0: string, arg1: string): $MessageDigest
public static "getInstance"(arg0: string, arg1: $Provider$Type): $MessageDigest
public static "getInstance"(arg0: string): $MessageDigest
public "reset"(): void
public "digest"(): (byte)[]
public "digest"(arg0: (byte)[], arg1: integer, arg2: integer): integer
public "digest"(arg0: (byte)[]): (byte)[]
public "getProvider"(): $Provider
public static "isEqual"(arg0: (byte)[], arg1: (byte)[]): boolean
public "getAlgorithm"(): string
public "getDigestLength"(): integer
get "provider"(): $Provider
get "algorithm"(): string
get "digestLength"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MessageDigest$Type = ($MessageDigest);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MessageDigest_ = $MessageDigest$Type;
}}
declare module "packages/java/security/$KeyFactory" {
import {$Key, $Key$Type} from "packages/java/security/$Key"
import {$PublicKey, $PublicKey$Type} from "packages/java/security/$PublicKey"
import {$PrivateKey, $PrivateKey$Type} from "packages/java/security/$PrivateKey"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$Provider, $Provider$Type} from "packages/java/security/$Provider"
import {$KeySpec, $KeySpec$Type} from "packages/java/security/spec/$KeySpec"

export class $KeyFactory {


public static "getInstance"(arg0: string, arg1: string): $KeyFactory
public static "getInstance"(arg0: string, arg1: $Provider$Type): $KeyFactory
public static "getInstance"(arg0: string): $KeyFactory
public "getProvider"(): $Provider
public "getAlgorithm"(): string
public "generatePublic"(arg0: $KeySpec$Type): $PublicKey
public "generatePrivate"(arg0: $KeySpec$Type): $PrivateKey
public "getKeySpec"<T extends $KeySpec>(arg0: $Key$Type, arg1: $Class$Type<(T)>): T
public "translateKey"(arg0: $Key$Type): $Key
get "provider"(): $Provider
get "algorithm"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $KeyFactory$Type = ($KeyFactory);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $KeyFactory_ = $KeyFactory$Type;
}}
declare module "packages/java/security/$CodeSource" {
import {$Certificate, $Certificate$Type} from "packages/java/security/cert/$Certificate"
import {$Serializable, $Serializable$Type} from "packages/java/io/$Serializable"
import {$CodeSigner, $CodeSigner$Type} from "packages/java/security/$CodeSigner"
import {$URL, $URL$Type} from "packages/java/net/$URL"

export class $CodeSource implements $Serializable {

constructor(arg0: $URL$Type, arg1: ($Certificate$Type)[])
constructor(arg0: $URL$Type, arg1: ($CodeSigner$Type)[])

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "getLocation"(): $URL
public "getCertificates"(): ($Certificate)[]
public "implies"(arg0: $CodeSource$Type): boolean
public "getCodeSigners"(): ($CodeSigner)[]
get "location"(): $URL
get "certificates"(): ($Certificate)[]
get "codeSigners"(): ($CodeSigner)[]
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CodeSource$Type = ($CodeSource);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CodeSource_ = $CodeSource$Type;
}}
declare module "packages/java/security/$ProtectionDomain" {
import {$CodeSource, $CodeSource$Type} from "packages/java/security/$CodeSource"
import {$ClassLoader, $ClassLoader$Type} from "packages/java/lang/$ClassLoader"
import {$PermissionCollection, $PermissionCollection$Type} from "packages/java/security/$PermissionCollection"
import {$Permission, $Permission$Type} from "packages/java/security/$Permission"
import {$Principal, $Principal$Type} from "packages/java/security/$Principal"

export class $ProtectionDomain {

constructor(arg0: $CodeSource$Type, arg1: $PermissionCollection$Type)
constructor(arg0: $CodeSource$Type, arg1: $PermissionCollection$Type, arg2: $ClassLoader$Type, arg3: ($Principal$Type)[])

public "toString"(): string
public "getClassLoader"(): $ClassLoader
public "getCodeSource"(): $CodeSource
public "implies"(arg0: $Permission$Type): boolean
public "getPermissions"(): $PermissionCollection
public "getPrincipals"(): ($Principal)[]
public "staticPermissionsOnly"(): boolean
get "classLoader"(): $ClassLoader
get "codeSource"(): $CodeSource
get "permissions"(): $PermissionCollection
get "principals"(): ($Principal)[]
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ProtectionDomain$Type = ($ProtectionDomain);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ProtectionDomain_ = $ProtectionDomain$Type;
}}
declare module "packages/java/security/$PrivateKey" {
import {$Key, $Key$Type} from "packages/java/security/$Key"
import {$Destroyable, $Destroyable$Type} from "packages/javax/security/auth/$Destroyable"

export interface $PrivateKey extends $Key, $Destroyable {

 "getEncoded"(): (byte)[]
 "getFormat"(): string
 "getAlgorithm"(): string
 "destroy"(): void
 "isDestroyed"(): boolean
}

export namespace $PrivateKey {
const serialVersionUID: long
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PrivateKey$Type = ($PrivateKey);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PrivateKey_ = $PrivateKey$Type;
}}
declare module "packages/java/security/$CodeSigner" {
import {$Serializable, $Serializable$Type} from "packages/java/io/$Serializable"
import {$CertPath, $CertPath$Type} from "packages/java/security/cert/$CertPath"
import {$Timestamp, $Timestamp$Type} from "packages/java/security/$Timestamp"

export class $CodeSigner implements $Serializable {

constructor(arg0: $CertPath$Type, arg1: $Timestamp$Type)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "getSignerCertPath"(): $CertPath
public "getTimestamp"(): $Timestamp
get "signerCertPath"(): $CertPath
get "timestamp"(): $Timestamp
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CodeSigner$Type = ($CodeSigner);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CodeSigner_ = $CodeSigner$Type;
}}
declare module "packages/java/security/$Key" {
import {$Serializable, $Serializable$Type} from "packages/java/io/$Serializable"

export interface $Key extends $Serializable {

 "getEncoded"(): (byte)[]
 "getFormat"(): string
 "getAlgorithm"(): string
}

export namespace $Key {
const serialVersionUID: long
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Key$Type = ($Key);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Key_ = $Key$Type;
}}
declare module "packages/java/security/$Signature" {
import {$Certificate, $Certificate$Type} from "packages/java/security/cert/$Certificate"
import {$SecureRandom, $SecureRandom$Type} from "packages/java/security/$SecureRandom"
import {$AlgorithmParameters, $AlgorithmParameters$Type} from "packages/java/security/$AlgorithmParameters"
import {$PublicKey, $PublicKey$Type} from "packages/java/security/$PublicKey"
import {$PrivateKey, $PrivateKey$Type} from "packages/java/security/$PrivateKey"
import {$ByteBuffer, $ByteBuffer$Type} from "packages/java/nio/$ByteBuffer"
import {$Provider, $Provider$Type} from "packages/java/security/$Provider"
import {$SignatureSpi, $SignatureSpi$Type} from "packages/java/security/$SignatureSpi"
import {$AlgorithmParameterSpec, $AlgorithmParameterSpec$Type} from "packages/java/security/spec/$AlgorithmParameterSpec"

export class $Signature extends $SignatureSpi {


public "toString"(): string
public "clone"(): any
public "update"(arg0: (byte)[]): void
public "update"(arg0: (byte)[], arg1: integer, arg2: integer): void
public "update"(arg0: byte): void
public "update"(arg0: $ByteBuffer$Type): void
public static "getInstance"(arg0: string): $Signature
public static "getInstance"(arg0: string, arg1: string): $Signature
public static "getInstance"(arg0: string, arg1: $Provider$Type): $Signature
public "getParameters"(): $AlgorithmParameters
public "sign"(arg0: (byte)[], arg1: integer, arg2: integer): integer
public "sign"(): (byte)[]
public "verify"(arg0: (byte)[]): boolean
public "verify"(arg0: (byte)[], arg1: integer, arg2: integer): boolean
public "getProvider"(): $Provider
public "getAlgorithm"(): string
/**
 * 
 * @deprecated
 */
public "setParameter"(arg0: string, arg1: any): void
public "setParameter"(arg0: $AlgorithmParameterSpec$Type): void
/**
 * 
 * @deprecated
 */
public "getParameter"(arg0: string): any
public "initVerify"(arg0: $PublicKey$Type): void
public "initVerify"(arg0: $Certificate$Type): void
public "initSign"(arg0: $PrivateKey$Type): void
public "initSign"(arg0: $PrivateKey$Type, arg1: $SecureRandom$Type): void
get "parameters"(): $AlgorithmParameters
get "provider"(): $Provider
get "algorithm"(): string
set "parameter"(value: $AlgorithmParameterSpec$Type)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Signature$Type = ($Signature);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Signature_ = $Signature$Type;
}}
declare module "packages/java/security/$Permission" {
import {$Serializable, $Serializable$Type} from "packages/java/io/$Serializable"
import {$Guard, $Guard$Type} from "packages/java/security/$Guard"
import {$PermissionCollection, $PermissionCollection$Type} from "packages/java/security/$PermissionCollection"

export class $Permission implements $Guard, $Serializable {

constructor(arg0: string)

public "getName"(): string
public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "implies"(arg0: $Permission$Type): boolean
public "getActions"(): string
public "newPermissionCollection"(): $PermissionCollection
public "checkGuard"(arg0: any): void
get "name"(): string
get "actions"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Permission$Type = ($Permission);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Permission_ = $Permission$Type;
}}
declare module "packages/java/security/$KeyStore$Entry$Attribute" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $KeyStore$Entry$Attribute {

 "getName"(): string
 "getValue"(): string
}

export namespace $KeyStore$Entry$Attribute {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $KeyStore$Entry$Attribute$Type = ($KeyStore$Entry$Attribute);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $KeyStore$Entry$Attribute_ = $KeyStore$Entry$Attribute$Type;
}}
declare module "packages/java/security/$KeyStore" {
import {$Key, $Key$Type} from "packages/java/security/$Key"
import {$Certificate, $Certificate$Type} from "packages/java/security/cert/$Certificate"
import {$KeyStore$ProtectionParameter, $KeyStore$ProtectionParameter$Type} from "packages/java/security/$KeyStore$ProtectionParameter"
import {$Date, $Date$Type} from "packages/java/util/$Date"
import {$OutputStream, $OutputStream$Type} from "packages/java/io/$OutputStream"
import {$KeyStore$LoadStoreParameter, $KeyStore$LoadStoreParameter$Type} from "packages/java/security/$KeyStore$LoadStoreParameter"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$File, $File$Type} from "packages/java/io/$File"
import {$Enumeration, $Enumeration$Type} from "packages/java/util/$Enumeration"
import {$InputStream, $InputStream$Type} from "packages/java/io/$InputStream"
import {$KeyStore$Entry, $KeyStore$Entry$Type} from "packages/java/security/$KeyStore$Entry"
import {$Provider, $Provider$Type} from "packages/java/security/$Provider"

export class $KeyStore {


public "load"(arg0: $KeyStore$LoadStoreParameter$Type): void
public "load"(arg0: $InputStream$Type, arg1: (character)[]): void
public "store"(arg0: $OutputStream$Type, arg1: (character)[]): void
public "store"(arg0: $KeyStore$LoadStoreParameter$Type): void
public "size"(): integer
public "getKey"(arg0: string, arg1: (character)[]): $Key
public static "getInstance"(arg0: $File$Type, arg1: (character)[]): $KeyStore
public static "getInstance"(arg0: string, arg1: string): $KeyStore
public static "getInstance"(arg0: string): $KeyStore
public static "getInstance"(arg0: $File$Type, arg1: $KeyStore$LoadStoreParameter$Type): $KeyStore
public static "getInstance"(arg0: string, arg1: $Provider$Type): $KeyStore
public "getType"(): string
public "aliases"(): $Enumeration<(string)>
public "getEntry"(arg0: string, arg1: $KeyStore$ProtectionParameter$Type): $KeyStore$Entry
public "getProvider"(): $Provider
public "deleteEntry"(arg0: string): void
public "setEntry"(arg0: string, arg1: $KeyStore$Entry$Type, arg2: $KeyStore$ProtectionParameter$Type): void
public "getCertificateChain"(arg0: string): ($Certificate)[]
public "getCertificate"(arg0: string): $Certificate
public static "getDefaultType"(): string
public "getCreationDate"(arg0: string): $Date
public "setKeyEntry"(arg0: string, arg1: $Key$Type, arg2: (character)[], arg3: ($Certificate$Type)[]): void
public "setKeyEntry"(arg0: string, arg1: (byte)[], arg2: ($Certificate$Type)[]): void
public "setCertificateEntry"(arg0: string, arg1: $Certificate$Type): void
public "containsAlias"(arg0: string): boolean
public "isKeyEntry"(arg0: string): boolean
public "isCertificateEntry"(arg0: string): boolean
public "getCertificateAlias"(arg0: $Certificate$Type): string
public "entryInstanceOf"(arg0: string, arg1: $Class$Type<(any)>): boolean
get "type"(): string
get "provider"(): $Provider
get "defaultType"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $KeyStore$Type = ($KeyStore);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $KeyStore_ = $KeyStore$Type;
}}
declare module "packages/java/security/$Provider" {
import {$BiConsumer, $BiConsumer$Type} from "packages/java/util/function/$BiConsumer"
import {$Provider$Service, $Provider$Service$Type} from "packages/java/security/$Provider$Service"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Enumeration, $Enumeration$Type} from "packages/java/util/$Enumeration"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$InputStream, $InputStream$Type} from "packages/java/io/$InputStream"
import {$Properties, $Properties$Type} from "packages/java/util/$Properties"
import {$BiFunction, $BiFunction$Type} from "packages/java/util/function/$BiFunction"
import {$Map$Entry, $Map$Entry$Type} from "packages/java/util/$Map$Entry"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $Provider extends $Properties {


public "getName"(): string
public "remove"(arg0: any): any
public "remove"(arg0: any, arg1: any): boolean
public "get"(arg0: any): any
public "put"(arg0: any, arg1: any): any
public "getProperty"(arg0: string): string
public "toString"(): string
public "values"(): $Collection<(any)>
public "load"(arg0: $InputStream$Type): void
public "clear"(): void
public "replace"(arg0: any, arg1: any): any
public "replace"(arg0: any, arg1: any, arg2: any): boolean
public "replaceAll"(arg0: $BiFunction$Type<(any), (any), (any)>): void
public "elements"(): $Enumeration<(any)>
public "merge"(arg0: any, arg1: any, arg2: $BiFunction$Type<(any), (any), (any)>): any
public "entrySet"(): $Set<($Map$Entry<(any), (any)>)>
public "putAll"(arg0: $Map$Type<(any), (any)>): void
public "putIfAbsent"(arg0: any, arg1: any): any
public "compute"(arg0: any, arg1: $BiFunction$Type<(any), (any), (any)>): any
public "forEach"(arg0: $BiConsumer$Type<(any), (any)>): void
public "computeIfAbsent"(arg0: any, arg1: $Function$Type<(any), (any)>): any
public "keys"(): $Enumeration<(any)>
public "keySet"(): $Set<(any)>
public "getOrDefault"(arg0: any, arg1: any): any
public "computeIfPresent"(arg0: any, arg1: $BiFunction$Type<(any), (any), (any)>): any
/**
 * 
 * @deprecated
 */
public "getVersion"(): double
public "getService"(arg0: string, arg1: string): $Provider$Service
public "getServices"(): $Set<($Provider$Service)>
public "configure"(arg0: string): $Provider
public "isConfigured"(): boolean
public "getVersionStr"(): string
public "getInfo"(): string
public static "copyOf"<K, V>(arg0: $Map$Type<(any), (any)>): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V, arg8: K, arg9: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V): $Map<(K), (V)>
public static "of"<K, V>(): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V, arg8: K, arg9: V, arg10: K, arg11: V, arg12: K, arg13: V, arg14: K, arg15: V, arg16: K, arg17: V, arg18: K, arg19: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V, arg8: K, arg9: V, arg10: K, arg11: V, arg12: K, arg13: V, arg14: K, arg15: V, arg16: K, arg17: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V, arg8: K, arg9: V, arg10: K, arg11: V, arg12: K, arg13: V, arg14: K, arg15: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V, arg8: K, arg9: V, arg10: K, arg11: V, arg12: K, arg13: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V, arg8: K, arg9: V, arg10: K, arg11: V): $Map<(K), (V)>
public static "entry"<K, V>(arg0: K, arg1: V): $Map$Entry<(K), (V)>
public static "ofEntries"<K, V>(...arg0: ($Map$Entry$Type<(any), (any)>)[]): $Map<(K), (V)>
get "name"(): string
get "version"(): double
get "services"(): $Set<($Provider$Service)>
get "configured"(): boolean
get "versionStr"(): string
get "info"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Provider$Type = ($Provider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Provider_ = $Provider$Type;
}}
declare module "packages/java/security/$Timestamp" {
import {$Serializable, $Serializable$Type} from "packages/java/io/$Serializable"
import {$Date, $Date$Type} from "packages/java/util/$Date"
import {$CertPath, $CertPath$Type} from "packages/java/security/cert/$CertPath"

export class $Timestamp implements $Serializable {

constructor(arg0: $Date$Type, arg1: $CertPath$Type)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "getSignerCertPath"(): $CertPath
public "getTimestamp"(): $Date
get "signerCertPath"(): $CertPath
get "timestamp"(): $Date
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Timestamp$Type = ($Timestamp);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Timestamp_ = $Timestamp$Type;
}}
declare module "packages/java/security/$AccessControlContext" {
import {$DomainCombiner, $DomainCombiner$Type} from "packages/java/security/$DomainCombiner"
import {$Permission, $Permission$Type} from "packages/java/security/$Permission"
import {$ProtectionDomain, $ProtectionDomain$Type} from "packages/java/security/$ProtectionDomain"

/**
 * 
 * @deprecated
 */
export class $AccessControlContext {

constructor(arg0: $AccessControlContext$Type, arg1: $DomainCombiner$Type)
constructor(arg0: ($ProtectionDomain$Type)[])

public "equals"(arg0: any): boolean
public "hashCode"(): integer
public "checkPermission"(arg0: $Permission$Type): void
public "getDomainCombiner"(): $DomainCombiner
get "domainCombiner"(): $DomainCombiner
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AccessControlContext$Type = ($AccessControlContext);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AccessControlContext_ = $AccessControlContext$Type;
}}
declare module "packages/java/security/$PermissionCollection" {
import {$Serializable, $Serializable$Type} from "packages/java/io/$Serializable"
import {$Enumeration, $Enumeration$Type} from "packages/java/util/$Enumeration"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$Permission, $Permission$Type} from "packages/java/security/$Permission"

export class $PermissionCollection implements $Serializable {

constructor()

public "add"(arg0: $Permission$Type): void
public "toString"(): string
public "elements"(): $Enumeration<($Permission)>
public "setReadOnly"(): void
public "implies"(arg0: $Permission$Type): boolean
public "isReadOnly"(): boolean
public "elementsAsStream"(): $Stream<($Permission)>
get "readOnly"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PermissionCollection$Type = ($PermissionCollection);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PermissionCollection_ = $PermissionCollection$Type;
}}
declare module "packages/java/security/$KeyPairGeneratorSpi" {
import {$SecureRandom, $SecureRandom$Type} from "packages/java/security/$SecureRandom"
import {$KeyPair, $KeyPair$Type} from "packages/java/security/$KeyPair"
import {$AlgorithmParameterSpec, $AlgorithmParameterSpec$Type} from "packages/java/security/spec/$AlgorithmParameterSpec"

export class $KeyPairGeneratorSpi {

constructor()

public "initialize"(arg0: $AlgorithmParameterSpec$Type, arg1: $SecureRandom$Type): void
public "initialize"(arg0: integer, arg1: $SecureRandom$Type): void
public "generateKeyPair"(): $KeyPair
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $KeyPairGeneratorSpi$Type = ($KeyPairGeneratorSpi);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $KeyPairGeneratorSpi_ = $KeyPairGeneratorSpi$Type;
}}
declare module "packages/java/security/$KeyPair" {
import {$Serializable, $Serializable$Type} from "packages/java/io/$Serializable"
import {$PublicKey, $PublicKey$Type} from "packages/java/security/$PublicKey"
import {$PrivateKey, $PrivateKey$Type} from "packages/java/security/$PrivateKey"

export class $KeyPair implements $Serializable {

constructor(arg0: $PublicKey$Type, arg1: $PrivateKey$Type)

public "getPrivate"(): $PrivateKey
public "getPublic"(): $PublicKey
get "private"(): $PrivateKey
get "public"(): $PublicKey
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $KeyPair$Type = ($KeyPair);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $KeyPair_ = $KeyPair$Type;
}}
declare module "packages/java/security/$AlgorithmConstraints" {
import {$Key, $Key$Type} from "packages/java/security/$Key"
import {$AlgorithmParameters, $AlgorithmParameters$Type} from "packages/java/security/$AlgorithmParameters"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$CryptoPrimitive, $CryptoPrimitive$Type} from "packages/java/security/$CryptoPrimitive"

export interface $AlgorithmConstraints {

 "permits"(arg0: $Set$Type<($CryptoPrimitive$Type)>, arg1: string, arg2: $AlgorithmParameters$Type): boolean
 "permits"(arg0: $Set$Type<($CryptoPrimitive$Type)>, arg1: $Key$Type): boolean
 "permits"(arg0: $Set$Type<($CryptoPrimitive$Type)>, arg1: string, arg2: $Key$Type, arg3: $AlgorithmParameters$Type): boolean
}

export namespace $AlgorithmConstraints {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AlgorithmConstraints$Type = ($AlgorithmConstraints);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AlgorithmConstraints_ = $AlgorithmConstraints$Type;
}}
declare module "packages/java/security/cert/$Certificate" {
import {$Serializable, $Serializable$Type} from "packages/java/io/$Serializable"
import {$PublicKey, $PublicKey$Type} from "packages/java/security/$PublicKey"
import {$Provider, $Provider$Type} from "packages/java/security/$Provider"

export class $Certificate implements $Serializable {


public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "getType"(): string
public "getEncoded"(): (byte)[]
public "verify"(arg0: $PublicKey$Type): void
public "verify"(arg0: $PublicKey$Type, arg1: string): void
public "verify"(arg0: $PublicKey$Type, arg1: $Provider$Type): void
public "getPublicKey"(): $PublicKey
get "type"(): string
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
declare module "packages/java/security/$SignatureSpi" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $SignatureSpi {

constructor()

public "clone"(): any
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SignatureSpi$Type = ($SignatureSpi);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SignatureSpi_ = $SignatureSpi$Type;
}}
declare module "packages/java/security/spec/$AlgorithmParameterSpec" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $AlgorithmParameterSpec {

}

export namespace $AlgorithmParameterSpec {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AlgorithmParameterSpec$Type = ($AlgorithmParameterSpec);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AlgorithmParameterSpec_ = $AlgorithmParameterSpec$Type;
}}
declare module "packages/java/security/$PrivilegedExceptionAction" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $PrivilegedExceptionAction<T> {

 "run"(): T

(): T
}

export namespace $PrivilegedExceptionAction {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PrivilegedExceptionAction$Type<T> = ($PrivilegedExceptionAction<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PrivilegedExceptionAction_<T> = $PrivilegedExceptionAction$Type<(T)>;
}}
declare module "packages/java/security/$MessageDigestSpi" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $MessageDigestSpi {

constructor()

public "clone"(): any
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MessageDigestSpi$Type = ($MessageDigestSpi);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MessageDigestSpi_ = $MessageDigestSpi$Type;
}}
declare module "packages/java/security/$Guard" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $Guard {

 "checkGuard"(arg0: any): void

(arg0: any): void
}

export namespace $Guard {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Guard$Type = ($Guard);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Guard_ = $Guard$Type;
}}
declare module "packages/java/security/cert/$X509Certificate" {
import {$Certificate, $Certificate$Type} from "packages/java/security/cert/$Certificate"
import {$PublicKey, $PublicKey$Type} from "packages/java/security/$PublicKey"
import {$Date, $Date$Type} from "packages/java/util/$Date"
import {$X509Extension, $X509Extension$Type} from "packages/java/security/cert/$X509Extension"
import {$BigInteger, $BigInteger$Type} from "packages/java/math/$BigInteger"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$X500Principal, $X500Principal$Type} from "packages/javax/security/auth/x500/$X500Principal"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Principal, $Principal$Type} from "packages/java/security/$Principal"
import {$Provider, $Provider$Type} from "packages/java/security/$Provider"

export class $X509Certificate extends $Certificate implements $X509Extension {


public "getSignature"(): (byte)[]
public "getBasicConstraints"(): integer
public "verify"(arg0: $PublicKey$Type, arg1: $Provider$Type): void
public "getIssuerX500Principal"(): $X500Principal
public "getSubjectX500Principal"(): $X500Principal
public "getVersion"(): integer
public "getSerialNumber"(): $BigInteger
/**
 * 
 * @deprecated
 */
public "getIssuerDN"(): $Principal
public "getTBSCertificate"(): (byte)[]
public "getExtendedKeyUsage"(): $List<(string)>
public "getSubjectAlternativeNames"(): $Collection<($List<(any)>)>
public "getIssuerAlternativeNames"(): $Collection<($List<(any)>)>
public "getSigAlgName"(): string
public "getSigAlgParams"(): (byte)[]
public "checkValidity"(arg0: $Date$Type): void
public "checkValidity"(): void
/**
 * 
 * @deprecated
 */
public "getSubjectDN"(): $Principal
public "getNotBefore"(): $Date
public "getNotAfter"(): $Date
public "getSigAlgOID"(): string
public "getIssuerUniqueID"(): (boolean)[]
public "getSubjectUniqueID"(): (boolean)[]
public "getKeyUsage"(): (boolean)[]
public "getExtensionValue"(arg0: string): (byte)[]
public "getCriticalExtensionOIDs"(): $Set<(string)>
public "hasUnsupportedCriticalExtension"(): boolean
public "getNonCriticalExtensionOIDs"(): $Set<(string)>
get "signature"(): (byte)[]
get "basicConstraints"(): integer
get "issuerX500Principal"(): $X500Principal
get "subjectX500Principal"(): $X500Principal
get "version"(): integer
get "serialNumber"(): $BigInteger
get "issuerDN"(): $Principal
get "tBSCertificate"(): (byte)[]
get "extendedKeyUsage"(): $List<(string)>
get "subjectAlternativeNames"(): $Collection<($List<(any)>)>
get "issuerAlternativeNames"(): $Collection<($List<(any)>)>
get "sigAlgName"(): string
get "sigAlgParams"(): (byte)[]
get "subjectDN"(): $Principal
get "notBefore"(): $Date
get "notAfter"(): $Date
get "sigAlgOID"(): string
get "issuerUniqueID"(): (boolean)[]
get "subjectUniqueID"(): (boolean)[]
get "keyUsage"(): (boolean)[]
get "criticalExtensionOIDs"(): $Set<(string)>
get "nonCriticalExtensionOIDs"(): $Set<(string)>
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
declare module "packages/java/security/$PrivilegedAction" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $PrivilegedAction<T> {

 "run"(): T

(): T
}

export namespace $PrivilegedAction {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PrivilegedAction$Type<T> = ($PrivilegedAction<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PrivilegedAction_<T> = $PrivilegedAction$Type<(T)>;
}}
declare module "packages/java/security/$Principal" {
import {$Subject, $Subject$Type} from "packages/javax/security/auth/$Subject"

export interface $Principal {

 "getName"(): string
 "equals"(arg0: any): boolean
 "toString"(): string
 "hashCode"(): integer
 "implies"(arg0: $Subject$Type): boolean
}

export namespace $Principal {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Principal$Type = ($Principal);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Principal_ = $Principal$Type;
}}

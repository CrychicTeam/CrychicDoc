declare module "packages/java/net/$Proxy" {
import {$SocketAddress, $SocketAddress$Type} from "packages/java/net/$SocketAddress"
import {$Proxy$Type, $Proxy$Type$Type} from "packages/java/net/$Proxy$Type"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $Proxy {
static readonly "NO_PROXY": $Proxy

constructor(arg0: $Proxy$Type$Type, arg1: $SocketAddress$Type)

public "type"(): $Proxy$Type
public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "address"(): $SocketAddress
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Proxy$Type = ($Proxy);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Proxy_ = $Proxy$Type;
}}
declare module "packages/java/net/$SocketAddress" {
import {$Serializable, $Serializable$Type} from "packages/java/io/$Serializable"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $SocketAddress implements $Serializable {

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SocketAddress$Type = ($SocketAddress);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SocketAddress_ = $SocketAddress$Type;
}}
declare module "packages/java/net/$DatagramSocketImplFactory" {
import {$DatagramSocketImpl, $DatagramSocketImpl$Type} from "packages/java/net/$DatagramSocketImpl"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export interface $DatagramSocketImplFactory {

 "createDatagramSocketImpl"(): $DatagramSocketImpl

(): $DatagramSocketImpl
}

export namespace $DatagramSocketImplFactory {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DatagramSocketImplFactory$Type = ($DatagramSocketImplFactory);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DatagramSocketImplFactory_ = $DatagramSocketImplFactory$Type;
}}
declare module "packages/java/net/$HttpCookie" {
import {$List, $List$Type} from "packages/java/util/$List"
import {$Cloneable, $Cloneable$Type} from "packages/java/lang/$Cloneable"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $HttpCookie implements $Cloneable {

constructor(arg0: string, arg1: string)

public "getName"(): string
public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "clone"(): any
public "getValue"(): string
public "setValue"(arg0: string): void
public "getPath"(): string
public static "parse"(arg0: string): $List<($HttpCookie)>
public "setComment"(arg0: string): void
public "getComment"(): string
public "getVersion"(): integer
public "setVersion"(arg0: integer): void
public "getDomain"(): string
public "isHttpOnly"(): boolean
public "setPath"(arg0: string): void
public "setSecure"(arg0: boolean): void
public "getSecure"(): boolean
public "hasExpired"(): boolean
public "setMaxAge"(arg0: long): void
public "getMaxAge"(): long
public "getPortlist"(): string
public "setCommentURL"(arg0: string): void
public "getCommentURL"(): string
public "setDiscard"(arg0: boolean): void
public "getDiscard"(): boolean
public "setPortlist"(arg0: string): void
public "setDomain"(arg0: string): void
public "setHttpOnly"(arg0: boolean): void
public static "domainMatches"(arg0: string, arg1: string): boolean
get "name"(): string
get "value"(): string
set "value"(value: string)
get "path"(): string
set "comment"(value: string)
get "comment"(): string
get "version"(): integer
set "version"(value: integer)
get "domain"(): string
get "httpOnly"(): boolean
set "path"(value: string)
set "secure"(value: boolean)
get "secure"(): boolean
set "maxAge"(value: long)
get "maxAge"(): long
get "portlist"(): string
set "commentURL"(value: string)
get "commentURL"(): string
set "discard"(value: boolean)
get "discard"(): boolean
set "portlist"(value: string)
set "domain"(value: string)
set "httpOnly"(value: boolean)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $HttpCookie$Type = ($HttpCookie);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $HttpCookie_ = $HttpCookie$Type;
}}
declare module "packages/java/net/$NetworkInterface" {
import {$Enumeration, $Enumeration$Type} from "packages/java/util/$Enumeration"
import {$List, $List$Type} from "packages/java/util/$List"
import {$InterfaceAddress, $InterfaceAddress$Type} from "packages/java/net/$InterfaceAddress"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$InetAddress, $InetAddress$Type} from "packages/java/net/$InetAddress"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $NetworkInterface {


public "getName"(): string
public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "getParent"(): $NetworkInterface
public static "getByName"(arg0: string): $NetworkInterface
public "getIndex"(): integer
public static "networkInterfaces"(): $Stream<($NetworkInterface)>
public "inetAddresses"(): $Stream<($InetAddress)>
public "getInetAddresses"(): $Enumeration<($InetAddress)>
public static "getNetworkInterfaces"(): $Enumeration<($NetworkInterface)>
public "getInterfaceAddresses"(): $List<($InterfaceAddress)>
public "getSubInterfaces"(): $Enumeration<($NetworkInterface)>
public "subInterfaces"(): $Stream<($NetworkInterface)>
public "getDisplayName"(): string
public static "getByIndex"(arg0: integer): $NetworkInterface
public static "getByInetAddress"(arg0: $InetAddress$Type): $NetworkInterface
public "isUp"(): boolean
public "isLoopback"(): boolean
public "isPointToPoint"(): boolean
public "supportsMulticast"(): boolean
public "getHardwareAddress"(): (byte)[]
public "getMTU"(): integer
public "isVirtual"(): boolean
get "name"(): string
get "parent"(): $NetworkInterface
get "index"(): integer
get "interfaceAddresses"(): $List<($InterfaceAddress)>
get "displayName"(): string
get "up"(): boolean
get "loopback"(): boolean
get "pointToPoint"(): boolean
get "hardwareAddress"(): (byte)[]
get "mTU"(): integer
get "virtual"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NetworkInterface$Type = ($NetworkInterface);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NetworkInterface_ = $NetworkInterface$Type;
}}
declare module "packages/java/net/$Authenticator$RequestorType" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $Authenticator$RequestorType extends $Enum<($Authenticator$RequestorType)> {
static readonly "PROXY": $Authenticator$RequestorType
static readonly "SERVER": $Authenticator$RequestorType


public static "values"(): ($Authenticator$RequestorType)[]
public static "valueOf"(arg0: string): $Authenticator$RequestorType
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Authenticator$RequestorType$Type = (("proxy") | ("server")) | ($Authenticator$RequestorType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Authenticator$RequestorType_ = $Authenticator$RequestorType$Type;
}}
declare module "packages/java/net/$Proxy$Type" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $Proxy$Type extends $Enum<($Proxy$Type)> {
static readonly "DIRECT": $Proxy$Type
static readonly "HTTP": $Proxy$Type
static readonly "SOCKS": $Proxy$Type


public static "values"(): ($Proxy$Type)[]
public static "valueOf"(arg0: string): $Proxy$Type
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Proxy$Type$Type = (("socks") | ("direct") | ("http")) | ($Proxy$Type);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Proxy$Type_ = $Proxy$Type$Type;
}}
declare module "packages/java/net/$URLConnection" {
import {$FileNameMap, $FileNameMap$Type} from "packages/java/net/$FileNameMap"
import {$ContentHandlerFactory, $ContentHandlerFactory$Type} from "packages/java/net/$ContentHandlerFactory"
import {$OutputStream, $OutputStream$Type} from "packages/java/io/$OutputStream"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$List, $List$Type} from "packages/java/util/$List"
import {$InputStream, $InputStream$Type} from "packages/java/io/$InputStream"
import {$Permission, $Permission$Type} from "packages/java/security/$Permission"
import {$URL, $URL$Type} from "packages/java/net/$URL"
import {$Map, $Map$Type} from "packages/java/util/$Map"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $URLConnection {


public "toString"(): string
public "connect"(): void
public "getInputStream"(): $InputStream
public "getContent"(arg0: ($Class$Type<(any)>)[]): any
public "getContent"(): any
public "getPermission"(): $Permission
public "getDate"(): long
public "setUseCaches"(arg0: boolean): void
public "getLastModified"(): long
public "getContentLength"(): integer
public "getURL"(): $URL
public "setRequestProperty"(arg0: string, arg1: string): void
public "getUseCaches"(): boolean
public "getContentLengthLong"(): long
public static "guessContentTypeFromStream"(arg0: $InputStream$Type): string
public static "guessContentTypeFromName"(arg0: string): string
public "getHeaderField"(arg0: integer): string
public "getHeaderField"(arg0: string): string
public "getRequestProperty"(arg0: string): string
public "addRequestProperty"(arg0: string, arg1: string): void
public "getRequestProperties"(): $Map<(string), ($List<(string)>)>
public "setAllowUserInteraction"(arg0: boolean): void
public "getAllowUserInteraction"(): boolean
public "setIfModifiedSince"(arg0: long): void
public "setDefaultUseCaches"(arg0: boolean): void
public static "setDefaultUseCaches"(arg0: string, arg1: boolean): void
public static "getDefaultUseCaches"(arg0: string): boolean
public "getDefaultUseCaches"(): boolean
public "getContentType"(): string
public "getHeaderFieldLong"(arg0: string, arg1: long): long
public "getHeaderFieldDate"(arg0: string, arg1: long): long
public static "getFileNameMap"(): $FileNameMap
public static "setFileNameMap"(arg0: $FileNameMap$Type): void
public "setConnectTimeout"(arg0: integer): void
public "getConnectTimeout"(): integer
public "setReadTimeout"(arg0: integer): void
public "getReadTimeout"(): integer
public "getContentEncoding"(): string
public "getExpiration"(): long
public "getHeaderFields"(): $Map<(string), ($List<(string)>)>
public "getHeaderFieldInt"(arg0: string, arg1: integer): integer
public "getHeaderFieldKey"(arg0: integer): string
public "getOutputStream"(): $OutputStream
public "setDoInput"(arg0: boolean): void
public "getDoInput"(): boolean
public "setDoOutput"(arg0: boolean): void
public "getDoOutput"(): boolean
public static "setDefaultAllowUserInteraction"(arg0: boolean): void
public static "getDefaultAllowUserInteraction"(): boolean
public "getIfModifiedSince"(): long
/**
 * 
 * @deprecated
 */
public static "setDefaultRequestProperty"(arg0: string, arg1: string): void
/**
 * 
 * @deprecated
 */
public static "getDefaultRequestProperty"(arg0: string): string
public static "setContentHandlerFactory"(arg0: $ContentHandlerFactory$Type): void
get "inputStream"(): $InputStream
get "content"(): any
get "permission"(): $Permission
get "date"(): long
set "useCaches"(value: boolean)
get "lastModified"(): long
get "contentLength"(): integer
get "uRL"(): $URL
get "useCaches"(): boolean
get "contentLengthLong"(): long
get "requestProperties"(): $Map<(string), ($List<(string)>)>
set "allowUserInteraction"(value: boolean)
get "allowUserInteraction"(): boolean
set "ifModifiedSince"(value: long)
set "defaultUseCaches"(value: boolean)
get "defaultUseCaches"(): boolean
get "contentType"(): string
get "fileNameMap"(): $FileNameMap
set "fileNameMap"(value: $FileNameMap$Type)
set "connectTimeout"(value: integer)
get "connectTimeout"(): integer
set "readTimeout"(value: integer)
get "readTimeout"(): integer
get "contentEncoding"(): string
get "expiration"(): long
get "headerFields"(): $Map<(string), ($List<(string)>)>
get "outputStream"(): $OutputStream
set "doInput"(value: boolean)
get "doInput"(): boolean
set "doOutput"(value: boolean)
get "doOutput"(): boolean
set "defaultAllowUserInteraction"(value: boolean)
get "defaultAllowUserInteraction"(): boolean
get "ifModifiedSince"(): long
set "contentHandlerFactory"(value: $ContentHandlerFactory$Type)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $URLConnection$Type = ($URLConnection);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $URLConnection_ = $URLConnection$Type;
}}
declare module "packages/java/net/$PasswordAuthentication" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $PasswordAuthentication {

constructor(arg0: string, arg1: (character)[])

public "getPassword"(): (character)[]
public "getUserName"(): string
get "password"(): (character)[]
get "userName"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PasswordAuthentication$Type = ($PasswordAuthentication);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PasswordAuthentication_ = $PasswordAuthentication$Type;
}}
declare module "packages/java/net/$InetAddress" {
import {$Serializable, $Serializable$Type} from "packages/java/io/$Serializable"
import {$NetworkInterface, $NetworkInterface$Type} from "packages/java/net/$NetworkInterface"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $InetAddress implements $Serializable {


public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "getHostAddress"(): string
public "getAddress"(): (byte)[]
public static "getByName"(arg0: string): $InetAddress
public "getHostName"(): string
public "isLinkLocalAddress"(): boolean
public "isMulticastAddress"(): boolean
public "isAnyLocalAddress"(): boolean
public "isLoopbackAddress"(): boolean
public "isSiteLocalAddress"(): boolean
public "isMCGlobal"(): boolean
public "isMCNodeLocal"(): boolean
public "isMCLinkLocal"(): boolean
public "isMCSiteLocal"(): boolean
public "isMCOrgLocal"(): boolean
public "isReachable"(arg0: integer): boolean
public "isReachable"(arg0: $NetworkInterface$Type, arg1: integer, arg2: integer): boolean
public static "getAllByName"(arg0: string): ($InetAddress)[]
public static "getByAddress"(arg0: string, arg1: (byte)[]): $InetAddress
public static "getByAddress"(arg0: (byte)[]): $InetAddress
public "getCanonicalHostName"(): string
public static "getLoopbackAddress"(): $InetAddress
public static "getLocalHost"(): $InetAddress
get "hostAddress"(): string
get "address"(): (byte)[]
get "hostName"(): string
get "linkLocalAddress"(): boolean
get "multicastAddress"(): boolean
get "anyLocalAddress"(): boolean
get "loopbackAddress"(): boolean
get "siteLocalAddress"(): boolean
get "mCGlobal"(): boolean
get "mCNodeLocal"(): boolean
get "mCLinkLocal"(): boolean
get "mCSiteLocal"(): boolean
get "mCOrgLocal"(): boolean
get "canonicalHostName"(): string
get "loopbackAddress"(): $InetAddress
get "localHost"(): $InetAddress
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InetAddress$Type = ($InetAddress);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InetAddress_ = $InetAddress$Type;
}}
declare module "packages/java/net/$URLStreamHandlerFactory" {
import {$URLStreamHandler, $URLStreamHandler$Type} from "packages/java/net/$URLStreamHandler"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export interface $URLStreamHandlerFactory {

 "createURLStreamHandler"(arg0: string): $URLStreamHandler

(arg0: string): $URLStreamHandler
}

export namespace $URLStreamHandlerFactory {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $URLStreamHandlerFactory$Type = ($URLStreamHandlerFactory);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $URLStreamHandlerFactory_ = $URLStreamHandlerFactory$Type;
}}
declare module "packages/java/net/$ContentHandlerFactory" {
import {$ContentHandler, $ContentHandler$Type} from "packages/java/net/$ContentHandler"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export interface $ContentHandlerFactory {

 "createContentHandler"(arg0: string): $ContentHandler

(arg0: string): $ContentHandler
}

export namespace $ContentHandlerFactory {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ContentHandlerFactory$Type = ($ContentHandlerFactory);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ContentHandlerFactory_ = $ContentHandlerFactory$Type;
}}
declare module "packages/java/net/$InterfaceAddress" {
import {$InetAddress, $InetAddress$Type} from "packages/java/net/$InetAddress"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $InterfaceAddress {


public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "getAddress"(): $InetAddress
public "getBroadcast"(): $InetAddress
public "getNetworkPrefixLength"(): short
get "address"(): $InetAddress
get "broadcast"(): $InetAddress
get "networkPrefixLength"(): short
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InterfaceAddress$Type = ($InterfaceAddress);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InterfaceAddress_ = $InterfaceAddress$Type;
}}
declare module "packages/java/net/$DatagramSocketImpl" {
import {$SocketOptions, $SocketOptions$Type} from "packages/java/net/$SocketOptions"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $DatagramSocketImpl implements $SocketOptions {

constructor()

public "getOption"(arg0: integer): any
public "setOption"(arg0: integer, arg1: any): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DatagramSocketImpl$Type = ($DatagramSocketImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DatagramSocketImpl_ = $DatagramSocketImpl$Type;
}}
declare module "packages/java/net/$SocketImpl" {
import {$SocketOptions, $SocketOptions$Type} from "packages/java/net/$SocketOptions"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $SocketImpl implements $SocketOptions {

constructor()

public "toString"(): string
public "getOption"(arg0: integer): any
public "setOption"(arg0: integer, arg1: any): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SocketImpl$Type = ($SocketImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SocketImpl_ = $SocketImpl$Type;
}}
declare module "packages/java/net/$URLStreamHandler" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $URLStreamHandler {

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $URLStreamHandler$Type = ($URLStreamHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $URLStreamHandler_ = $URLStreamHandler$Type;
}}
declare module "packages/java/net/$SocketOption" {
import {$Class, $Class$Type} from "packages/java/lang/$Class"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export interface $SocketOption<T> {

 "name"(): string
 "type"(): $Class<(T)>
}

export namespace $SocketOption {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SocketOption$Type<T> = ($SocketOption<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SocketOption_<T> = $SocketOption$Type<(T)>;
}}
declare module "packages/java/net/$ServerSocket" {
import {$SocketAddress, $SocketAddress$Type} from "packages/java/net/$SocketAddress"
import {$SocketImplFactory, $SocketImplFactory$Type} from "packages/java/net/$SocketImplFactory"
import {$Closeable, $Closeable$Type} from "packages/java/io/$Closeable"
import {$ServerSocketChannel, $ServerSocketChannel$Type} from "packages/java/nio/channels/$ServerSocketChannel"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$Socket, $Socket$Type} from "packages/java/net/$Socket"
import {$InetAddress, $InetAddress$Type} from "packages/java/net/$InetAddress"
import {$SocketOption, $SocketOption$Type} from "packages/java/net/$SocketOption"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $ServerSocket implements $Closeable {

constructor(arg0: integer, arg1: integer, arg2: $InetAddress$Type)
constructor(arg0: integer, arg1: integer)
constructor(arg0: integer)
constructor()

public "toString"(): string
public "accept"(): $Socket
public "close"(): void
public "bind"(arg0: $SocketAddress$Type, arg1: integer): void
public "bind"(arg0: $SocketAddress$Type): void
public "getChannel"(): $ServerSocketChannel
public "isClosed"(): boolean
public "isBound"(): boolean
public "getInetAddress"(): $InetAddress
public "getLocalPort"(): integer
public "supportedOptions"(): $Set<($SocketOption<(any)>)>
public "getLocalSocketAddress"(): $SocketAddress
public "getReuseAddress"(): boolean
public "getReceiveBufferSize"(): integer
public "setReceiveBufferSize"(arg0: integer): void
public "setPerformancePreferences"(arg0: integer, arg1: integer, arg2: integer): void
public "getSoTimeout"(): integer
public "setSoTimeout"(arg0: integer): void
public "setReuseAddress"(arg0: boolean): void
public "getOption"<T>(arg0: $SocketOption$Type<(T)>): T
public "setOption"<T>(arg0: $SocketOption$Type<(T)>, arg1: T): $ServerSocket
/**
 * 
 * @deprecated
 */
public static "setSocketFactory"(arg0: $SocketImplFactory$Type): void
get "channel"(): $ServerSocketChannel
get "closed"(): boolean
get "bound"(): boolean
get "inetAddress"(): $InetAddress
get "localPort"(): integer
get "localSocketAddress"(): $SocketAddress
get "reuseAddress"(): boolean
get "receiveBufferSize"(): integer
set "receiveBufferSize"(value: integer)
get "soTimeout"(): integer
set "soTimeout"(value: integer)
set "reuseAddress"(value: boolean)
set "socketFactory"(value: $SocketImplFactory$Type)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ServerSocket$Type = ($ServerSocket);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ServerSocket_ = $ServerSocket$Type;
}}
declare module "packages/java/net/$HttpURLConnection" {
import {$Authenticator, $Authenticator$Type} from "packages/java/net/$Authenticator"
import {$InputStream, $InputStream$Type} from "packages/java/io/$InputStream"
import {$Permission, $Permission$Type} from "packages/java/security/$Permission"
import {$URLConnection, $URLConnection$Type} from "packages/java/net/$URLConnection"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $HttpURLConnection extends $URLConnection {
static readonly "HTTP_OK": integer
static readonly "HTTP_CREATED": integer
static readonly "HTTP_ACCEPTED": integer
static readonly "HTTP_NOT_AUTHORITATIVE": integer
static readonly "HTTP_NO_CONTENT": integer
static readonly "HTTP_RESET": integer
static readonly "HTTP_PARTIAL": integer
static readonly "HTTP_MULT_CHOICE": integer
static readonly "HTTP_MOVED_PERM": integer
static readonly "HTTP_MOVED_TEMP": integer
static readonly "HTTP_SEE_OTHER": integer
static readonly "HTTP_NOT_MODIFIED": integer
static readonly "HTTP_USE_PROXY": integer
static readonly "HTTP_BAD_REQUEST": integer
static readonly "HTTP_UNAUTHORIZED": integer
static readonly "HTTP_PAYMENT_REQUIRED": integer
static readonly "HTTP_FORBIDDEN": integer
static readonly "HTTP_NOT_FOUND": integer
static readonly "HTTP_BAD_METHOD": integer
static readonly "HTTP_NOT_ACCEPTABLE": integer
static readonly "HTTP_PROXY_AUTH": integer
static readonly "HTTP_CLIENT_TIMEOUT": integer
static readonly "HTTP_CONFLICT": integer
static readonly "HTTP_GONE": integer
static readonly "HTTP_LENGTH_REQUIRED": integer
static readonly "HTTP_PRECON_FAILED": integer
static readonly "HTTP_ENTITY_TOO_LARGE": integer
static readonly "HTTP_REQ_TOO_LONG": integer
static readonly "HTTP_UNSUPPORTED_TYPE": integer
/**
 * 
 * @deprecated
 */
static readonly "HTTP_SERVER_ERROR": integer
static readonly "HTTP_INTERNAL_ERROR": integer
static readonly "HTTP_NOT_IMPLEMENTED": integer
static readonly "HTTP_BAD_GATEWAY": integer
static readonly "HTTP_UNAVAILABLE": integer
static readonly "HTTP_GATEWAY_TIMEOUT": integer
static readonly "HTTP_VERSION": integer


public "getErrorStream"(): $InputStream
public "getPermission"(): $Permission
public "setRequestMethod"(arg0: string): void
public "getResponseCode"(): integer
public "getHeaderField"(arg0: integer): string
public "getHeaderFieldDate"(arg0: string, arg1: long): long
public "getHeaderFieldKey"(arg0: integer): string
public "setAuthenticator"(arg0: $Authenticator$Type): void
public "setFixedLengthStreamingMode"(arg0: integer): void
public "setFixedLengthStreamingMode"(arg0: long): void
public "setChunkedStreamingMode"(arg0: integer): void
public static "setFollowRedirects"(arg0: boolean): void
public static "getFollowRedirects"(): boolean
public "setInstanceFollowRedirects"(arg0: boolean): void
public "getInstanceFollowRedirects"(): boolean
public "getRequestMethod"(): string
public "getResponseMessage"(): string
public "disconnect"(): void
public "usingProxy"(): boolean
get "errorStream"(): $InputStream
get "permission"(): $Permission
set "requestMethod"(value: string)
get "responseCode"(): integer
set "authenticator"(value: $Authenticator$Type)
set "fixedLengthStreamingMode"(value: integer)
set "fixedLengthStreamingMode"(value: long)
set "chunkedStreamingMode"(value: integer)
set "followRedirects"(value: boolean)
get "followRedirects"(): boolean
set "instanceFollowRedirects"(value: boolean)
get "instanceFollowRedirects"(): boolean
get "requestMethod"(): string
get "responseMessage"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $HttpURLConnection$Type = ($HttpURLConnection);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $HttpURLConnection_ = $HttpURLConnection$Type;
}}
declare module "packages/java/net/$SocketImplFactory" {
import {$SocketImpl, $SocketImpl$Type} from "packages/java/net/$SocketImpl"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export interface $SocketImplFactory {

 "createSocketImpl"(): $SocketImpl

(): $SocketImpl
}

export namespace $SocketImplFactory {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SocketImplFactory$Type = ($SocketImplFactory);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SocketImplFactory_ = $SocketImplFactory$Type;
}}
declare module "packages/java/net/$ContentHandler" {
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$URLConnection, $URLConnection$Type} from "packages/java/net/$URLConnection"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $ContentHandler {

constructor()

public "getContent"(arg0: $URLConnection$Type): any
public "getContent"(arg0: $URLConnection$Type, arg1: ($Class$Type<(any)>)[]): any
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ContentHandler$Type = ($ContentHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ContentHandler_ = $ContentHandler$Type;
}}
declare module "packages/java/net/$Socket" {
import {$Proxy, $Proxy$Type} from "packages/java/net/$Proxy"
import {$SocketAddress, $SocketAddress$Type} from "packages/java/net/$SocketAddress"
import {$SocketImplFactory, $SocketImplFactory$Type} from "packages/java/net/$SocketImplFactory"
import {$Closeable, $Closeable$Type} from "packages/java/io/$Closeable"
import {$OutputStream, $OutputStream$Type} from "packages/java/io/$OutputStream"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$InputStream, $InputStream$Type} from "packages/java/io/$InputStream"
import {$SocketChannel, $SocketChannel$Type} from "packages/java/nio/channels/$SocketChannel"
import {$InetAddress, $InetAddress$Type} from "packages/java/net/$InetAddress"
import {$SocketOption, $SocketOption$Type} from "packages/java/net/$SocketOption"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $Socket implements $Closeable {

constructor(arg0: $InetAddress$Type, arg1: integer)
constructor(arg0: string, arg1: integer, arg2: $InetAddress$Type, arg3: integer)
constructor(arg0: $InetAddress$Type, arg1: integer, arg2: $InetAddress$Type, arg3: integer)
/**
 * 
 * @deprecated
 */
constructor(arg0: string, arg1: integer, arg2: boolean)
/**
 * 
 * @deprecated
 */
constructor(arg0: $InetAddress$Type, arg1: integer, arg2: boolean)
constructor()
constructor(arg0: $Proxy$Type)
constructor(arg0: string, arg1: integer)

public "toString"(): string
public "connect"(arg0: $SocketAddress$Type): void
public "connect"(arg0: $SocketAddress$Type, arg1: integer): void
public "close"(): void
public "getPort"(): integer
public "getInputStream"(): $InputStream
public "bind"(arg0: $SocketAddress$Type): void
public "getChannel"(): $SocketChannel
public "getOutputStream"(): $OutputStream
public "isClosed"(): boolean
public "getLocalAddress"(): $InetAddress
public "isConnected"(): boolean
public "isBound"(): boolean
public "getInetAddress"(): $InetAddress
public "getLocalPort"(): integer
public "isInputShutdown"(): boolean
public "isOutputShutdown"(): boolean
public "sendUrgentData"(arg0: integer): void
public "shutdownInput"(): void
public "shutdownOutput"(): void
public "supportedOptions"(): $Set<($SocketOption<(any)>)>
public "getRemoteSocketAddress"(): $SocketAddress
public "getLocalSocketAddress"(): $SocketAddress
public "getTcpNoDelay"(): boolean
public "getOOBInline"(): boolean
public "getKeepAlive"(): boolean
public "getReuseAddress"(): boolean
/**
 * 
 * @deprecated
 */
public static "setSocketImplFactory"(arg0: $SocketImplFactory$Type): void
public "getReceiveBufferSize"(): integer
public "setReceiveBufferSize"(arg0: integer): void
public "setPerformancePreferences"(arg0: integer, arg1: integer, arg2: integer): void
public "getSendBufferSize"(): integer
public "getSoLinger"(): integer
public "setTrafficClass"(arg0: integer): void
public "setOOBInline"(arg0: boolean): void
public "getSoTimeout"(): integer
public "setTcpNoDelay"(arg0: boolean): void
public "setSoTimeout"(arg0: integer): void
public "setKeepAlive"(arg0: boolean): void
public "setSoLinger"(arg0: boolean, arg1: integer): void
public "getTrafficClass"(): integer
public "setReuseAddress"(arg0: boolean): void
public "setSendBufferSize"(arg0: integer): void
public "getOption"<T>(arg0: $SocketOption$Type<(T)>): T
public "setOption"<T>(arg0: $SocketOption$Type<(T)>, arg1: T): $Socket
get "port"(): integer
get "inputStream"(): $InputStream
get "channel"(): $SocketChannel
get "outputStream"(): $OutputStream
get "closed"(): boolean
get "localAddress"(): $InetAddress
get "connected"(): boolean
get "bound"(): boolean
get "inetAddress"(): $InetAddress
get "localPort"(): integer
get "inputShutdown"(): boolean
get "outputShutdown"(): boolean
get "remoteSocketAddress"(): $SocketAddress
get "localSocketAddress"(): $SocketAddress
get "tcpNoDelay"(): boolean
get "oOBInline"(): boolean
get "keepAlive"(): boolean
get "reuseAddress"(): boolean
set "socketImplFactory"(value: $SocketImplFactory$Type)
get "receiveBufferSize"(): integer
set "receiveBufferSize"(value: integer)
get "sendBufferSize"(): integer
get "soLinger"(): integer
set "trafficClass"(value: integer)
set "oOBInline"(value: boolean)
get "soTimeout"(): integer
set "tcpNoDelay"(value: boolean)
set "soTimeout"(value: integer)
set "keepAlive"(value: boolean)
get "trafficClass"(): integer
set "reuseAddress"(value: boolean)
set "sendBufferSize"(value: integer)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Socket$Type = ($Socket);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Socket_ = $Socket$Type;
}}
declare module "packages/java/net/$InetSocketAddress" {
import {$SocketAddress, $SocketAddress$Type} from "packages/java/net/$SocketAddress"
import {$InetAddress, $InetAddress$Type} from "packages/java/net/$InetAddress"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $InetSocketAddress extends $SocketAddress {

constructor(arg0: $InetAddress$Type, arg1: integer)
constructor(arg0: integer)
constructor(arg0: string, arg1: integer)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "getAddress"(): $InetAddress
public "getPort"(): integer
public "isUnresolved"(): boolean
public "getHostName"(): string
public static "createUnresolved"(arg0: string, arg1: integer): $InetSocketAddress
public "getHostString"(): string
get "address"(): $InetAddress
get "port"(): integer
get "unresolved"(): boolean
get "hostName"(): string
get "hostString"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InetSocketAddress$Type = ($InetSocketAddress);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InetSocketAddress_ = $InetSocketAddress$Type;
}}
declare module "packages/java/net/$DatagramPacket" {
import {$SocketAddress, $SocketAddress$Type} from "packages/java/net/$SocketAddress"
import {$InetAddress, $InetAddress$Type} from "packages/java/net/$InetAddress"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $DatagramPacket {

constructor(arg0: (byte)[], arg1: integer, arg2: $SocketAddress$Type)
constructor(arg0: (byte)[], arg1: integer, arg2: $InetAddress$Type, arg3: integer)
constructor(arg0: (byte)[], arg1: integer, arg2: integer, arg3: $SocketAddress$Type)
constructor(arg0: (byte)[], arg1: integer, arg2: integer)
constructor(arg0: (byte)[], arg1: integer)
constructor(arg0: (byte)[], arg1: integer, arg2: integer, arg3: $InetAddress$Type, arg4: integer)

public "getLength"(): integer
public "setLength"(arg0: integer): void
public "getAddress"(): $InetAddress
public "getPort"(): integer
public "getOffset"(): integer
public "getData"(): (byte)[]
public "setSocketAddress"(arg0: $SocketAddress$Type): void
public "setData"(arg0: (byte)[], arg1: integer, arg2: integer): void
public "setData"(arg0: (byte)[]): void
public "setPort"(arg0: integer): void
public "getSocketAddress"(): $SocketAddress
public "setAddress"(arg0: $InetAddress$Type): void
get "length"(): integer
set "length"(value: integer)
get "address"(): $InetAddress
get "port"(): integer
get "offset"(): integer
get "data"(): (byte)[]
set "socketAddress"(value: $SocketAddress$Type)
set "data"(value: (byte)[])
set "port"(value: integer)
get "socketAddress"(): $SocketAddress
set "address"(value: $InetAddress$Type)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DatagramPacket$Type = ($DatagramPacket);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DatagramPacket_ = $DatagramPacket$Type;
}}
declare module "packages/java/net/$DatagramSocket" {
import {$SocketAddress, $SocketAddress$Type} from "packages/java/net/$SocketAddress"
import {$DatagramChannel, $DatagramChannel$Type} from "packages/java/nio/channels/$DatagramChannel"
import {$Closeable, $Closeable$Type} from "packages/java/io/$Closeable"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$DatagramSocketImplFactory, $DatagramSocketImplFactory$Type} from "packages/java/net/$DatagramSocketImplFactory"
import {$NetworkInterface, $NetworkInterface$Type} from "packages/java/net/$NetworkInterface"
import {$InetAddress, $InetAddress$Type} from "packages/java/net/$InetAddress"
import {$SocketOption, $SocketOption$Type} from "packages/java/net/$SocketOption"
import {$DatagramPacket, $DatagramPacket$Type} from "packages/java/net/$DatagramPacket"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $DatagramSocket implements $Closeable {

constructor(arg0: integer, arg1: $InetAddress$Type)
constructor(arg0: $SocketAddress$Type)
constructor()
constructor(arg0: integer)

public "connect"(arg0: $SocketAddress$Type): void
public "connect"(arg0: $InetAddress$Type, arg1: integer): void
public "close"(): void
public "getPort"(): integer
public "bind"(arg0: $SocketAddress$Type): void
public "getChannel"(): $DatagramChannel
public "getBroadcast"(): boolean
public "setBroadcast"(arg0: boolean): void
public "joinGroup"(arg0: $SocketAddress$Type, arg1: $NetworkInterface$Type): void
/**
 * 
 * @deprecated
 */
public static "setDatagramSocketImplFactory"(arg0: $DatagramSocketImplFactory$Type): void
public "isClosed"(): boolean
public "disconnect"(): void
public "getLocalAddress"(): $InetAddress
public "isConnected"(): boolean
public "isBound"(): boolean
public "getInetAddress"(): $InetAddress
public "getLocalPort"(): integer
public "supportedOptions"(): $Set<($SocketOption<(any)>)>
public "getRemoteSocketAddress"(): $SocketAddress
public "getLocalSocketAddress"(): $SocketAddress
public "getReuseAddress"(): boolean
public "receive"(arg0: $DatagramPacket$Type): void
public "send"(arg0: $DatagramPacket$Type): void
public "getReceiveBufferSize"(): integer
public "setReceiveBufferSize"(arg0: integer): void
public "getSendBufferSize"(): integer
public "setTrafficClass"(arg0: integer): void
public "getSoTimeout"(): integer
public "setSoTimeout"(arg0: integer): void
public "getTrafficClass"(): integer
public "setReuseAddress"(arg0: boolean): void
public "setSendBufferSize"(arg0: integer): void
public "getOption"<T>(arg0: $SocketOption$Type<(T)>): T
public "setOption"<T>(arg0: $SocketOption$Type<(T)>, arg1: T): $DatagramSocket
public "leaveGroup"(arg0: $SocketAddress$Type, arg1: $NetworkInterface$Type): void
get "port"(): integer
get "channel"(): $DatagramChannel
get "broadcast"(): boolean
set "broadcast"(value: boolean)
set "datagramSocketImplFactory"(value: $DatagramSocketImplFactory$Type)
get "closed"(): boolean
get "localAddress"(): $InetAddress
get "connected"(): boolean
get "bound"(): boolean
get "inetAddress"(): $InetAddress
get "localPort"(): integer
get "remoteSocketAddress"(): $SocketAddress
get "localSocketAddress"(): $SocketAddress
get "reuseAddress"(): boolean
get "receiveBufferSize"(): integer
set "receiveBufferSize"(value: integer)
get "sendBufferSize"(): integer
set "trafficClass"(value: integer)
get "soTimeout"(): integer
set "soTimeout"(value: integer)
get "trafficClass"(): integer
set "reuseAddress"(value: boolean)
set "sendBufferSize"(value: integer)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DatagramSocket$Type = ($DatagramSocket);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DatagramSocket_ = $DatagramSocket$Type;
}}
declare module "packages/java/net/$URI" {
import {$Comparable, $Comparable$Type} from "packages/java/lang/$Comparable"
import {$Serializable, $Serializable$Type} from "packages/java/io/$Serializable"
import {$URL, $URL$Type} from "packages/java/net/$URL"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $URI implements $Comparable<($URI)>, $Serializable {

constructor(arg0: string, arg1: string, arg2: string, arg3: string, arg4: string)
constructor(arg0: string, arg1: string, arg2: string, arg3: string)
constructor(arg0: string, arg1: string, arg2: string)
constructor(arg0: string)
constructor(arg0: string, arg1: string, arg2: string, arg3: integer, arg4: string, arg5: string, arg6: string)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "compareTo"(arg0: $URI$Type): integer
public "isAbsolute"(): boolean
public "resolve"(arg0: string): $URI
public "resolve"(arg0: $URI$Type): $URI
public "normalize"(): $URI
public "getScheme"(): string
public "isOpaque"(): boolean
public "getRawFragment"(): string
public "getRawQuery"(): string
public "getRawPath"(): string
public "getHost"(): string
public "getPort"(): integer
public static "create"(arg0: string): $URI
public "getAuthority"(): string
public "getQuery"(): string
public "getPath"(): string
public "getUserInfo"(): string
public "toURL"(): $URL
public "getRawAuthority"(): string
public "relativize"(arg0: $URI$Type): $URI
public "getRawSchemeSpecificPart"(): string
public "parseServerAuthority"(): $URI
public "getSchemeSpecificPart"(): string
public "getRawUserInfo"(): string
public "getFragment"(): string
public "toASCIIString"(): string
get "absolute"(): boolean
get "scheme"(): string
get "opaque"(): boolean
get "rawFragment"(): string
get "rawQuery"(): string
get "rawPath"(): string
get "host"(): string
get "port"(): integer
get "authority"(): string
get "query"(): string
get "path"(): string
get "userInfo"(): string
get "rawAuthority"(): string
get "rawSchemeSpecificPart"(): string
get "schemeSpecificPart"(): string
get "rawUserInfo"(): string
get "fragment"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $URI$Type = ($URI);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $URI_ = $URI$Type;
}}
declare module "packages/java/net/$URL" {
import {$Proxy, $Proxy$Type} from "packages/java/net/$Proxy"
import {$URLStreamHandler, $URLStreamHandler$Type} from "packages/java/net/$URLStreamHandler"
import {$Serializable, $Serializable$Type} from "packages/java/io/$Serializable"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$InputStream, $InputStream$Type} from "packages/java/io/$InputStream"
import {$URI, $URI$Type} from "packages/java/net/$URI"
import {$URLConnection, $URLConnection$Type} from "packages/java/net/$URLConnection"
import {$URLStreamHandlerFactory, $URLStreamHandlerFactory$Type} from "packages/java/net/$URLStreamHandlerFactory"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $URL implements $Serializable {

constructor(arg0: $URL$Type, arg1: string, arg2: $URLStreamHandler$Type)
constructor(arg0: $URL$Type, arg1: string)
constructor(arg0: string)
constructor(arg0: string, arg1: string, arg2: integer, arg3: string)
constructor(arg0: string, arg1: string, arg2: string)
constructor(arg0: string, arg1: string, arg2: integer, arg3: string, arg4: $URLStreamHandler$Type)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "openStream"(): $InputStream
public "getHost"(): string
public "getPort"(): integer
public "getDefaultPort"(): integer
public "sameFile"(arg0: $URL$Type): boolean
public "toExternalForm"(): string
public "openConnection"(): $URLConnection
public "openConnection"(arg0: $Proxy$Type): $URLConnection
public "getContent"(): any
public "getContent"(arg0: ($Class$Type<(any)>)[]): any
public "getProtocol"(): string
public "getAuthority"(): string
public "getFile"(): string
public "getRef"(): string
public "getQuery"(): string
public "getPath"(): string
public "getUserInfo"(): string
public "toURI"(): $URI
public static "setURLStreamHandlerFactory"(arg0: $URLStreamHandlerFactory$Type): void
get "host"(): string
get "port"(): integer
get "defaultPort"(): integer
get "content"(): any
get "protocol"(): string
get "authority"(): string
get "file"(): string
get "ref"(): string
get "query"(): string
get "path"(): string
get "userInfo"(): string
set "uRLStreamHandlerFactory"(value: $URLStreamHandlerFactory$Type)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $URL$Type = ($URL);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $URL_ = $URL$Type;
}}
declare module "packages/java/net/$FileNameMap" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export interface $FileNameMap {

 "getContentTypeFor"(arg0: string): string

(arg0: string): string
}

export namespace $FileNameMap {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FileNameMap$Type = ($FileNameMap);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FileNameMap_ = $FileNameMap$Type;
}}
declare module "packages/java/net/$SocketOptions" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export interface $SocketOptions {

 "getOption"(arg0: integer): any
 "setOption"(arg0: integer, arg1: any): void
}

export namespace $SocketOptions {
const TCP_NODELAY: integer
const SO_BINDADDR: integer
const SO_REUSEADDR: integer
const SO_REUSEPORT: integer
const SO_BROADCAST: integer
const IP_MULTICAST_IF: integer
const IP_MULTICAST_IF2: integer
const IP_MULTICAST_LOOP: integer
const IP_TOS: integer
const SO_LINGER: integer
const SO_TIMEOUT: integer
const SO_SNDBUF: integer
const SO_RCVBUF: integer
const SO_KEEPALIVE: integer
const SO_OOBINLINE: integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SocketOptions$Type = ($SocketOptions);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SocketOptions_ = $SocketOptions$Type;
}}
declare module "packages/java/net/$ProtocolFamily" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export interface $ProtocolFamily {

 "name"(): string

(): string
}

export namespace $ProtocolFamily {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ProtocolFamily$Type = ($ProtocolFamily);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ProtocolFamily_ = $ProtocolFamily$Type;
}}
declare module "packages/java/net/$Authenticator" {
import {$PasswordAuthentication, $PasswordAuthentication$Type} from "packages/java/net/$PasswordAuthentication"
import {$Authenticator$RequestorType, $Authenticator$RequestorType$Type} from "packages/java/net/$Authenticator$RequestorType"
import {$URL, $URL$Type} from "packages/java/net/$URL"
import {$InetAddress, $InetAddress$Type} from "packages/java/net/$InetAddress"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $Authenticator {

constructor()

public static "getDefault"(): $Authenticator
public static "setDefault"(arg0: $Authenticator$Type): void
public static "requestPasswordAuthentication"(arg0: $Authenticator$Type, arg1: string, arg2: $InetAddress$Type, arg3: integer, arg4: string, arg5: string, arg6: string, arg7: $URL$Type, arg8: $Authenticator$RequestorType$Type): $PasswordAuthentication
public static "requestPasswordAuthentication"(arg0: $InetAddress$Type, arg1: integer, arg2: string, arg3: string, arg4: string): $PasswordAuthentication
public static "requestPasswordAuthentication"(arg0: string, arg1: $InetAddress$Type, arg2: integer, arg3: string, arg4: string, arg5: string): $PasswordAuthentication
public static "requestPasswordAuthentication"(arg0: string, arg1: $InetAddress$Type, arg2: integer, arg3: string, arg4: string, arg5: string, arg6: $URL$Type, arg7: $Authenticator$RequestorType$Type): $PasswordAuthentication
public "requestPasswordAuthenticationInstance"(arg0: string, arg1: $InetAddress$Type, arg2: integer, arg3: string, arg4: string, arg5: string, arg6: $URL$Type, arg7: $Authenticator$RequestorType$Type): $PasswordAuthentication
get "default"(): $Authenticator
set "default"(value: $Authenticator$Type)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Authenticator$Type = ($Authenticator);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Authenticator_ = $Authenticator$Type;
}}

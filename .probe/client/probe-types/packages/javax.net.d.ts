declare module "packages/javax/net/$ServerSocketFactory" {
import {$ServerSocket, $ServerSocket$Type} from "packages/java/net/$ServerSocket"
import {$InetAddress, $InetAddress$Type} from "packages/java/net/$InetAddress"

export class $ServerSocketFactory {


public static "getDefault"(): $ServerSocketFactory
public "createServerSocket"(arg0: integer, arg1: integer): $ServerSocket
public "createServerSocket"(arg0: integer, arg1: integer, arg2: $InetAddress$Type): $ServerSocket
public "createServerSocket"(arg0: integer): $ServerSocket
public "createServerSocket"(): $ServerSocket
get "default"(): $ServerSocketFactory
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ServerSocketFactory$Type = ($ServerSocketFactory);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ServerSocketFactory_ = $ServerSocketFactory$Type;
}}
declare module "packages/javax/net/ssl/$X509ExtendedKeyManager" {
import {$X509Certificate, $X509Certificate$Type} from "packages/java/security/cert/$X509Certificate"
import {$PrivateKey, $PrivateKey$Type} from "packages/java/security/$PrivateKey"
import {$SSLEngine, $SSLEngine$Type} from "packages/javax/net/ssl/$SSLEngine"
import {$Principal, $Principal$Type} from "packages/java/security/$Principal"
import {$Socket, $Socket$Type} from "packages/java/net/$Socket"
import {$X509KeyManager, $X509KeyManager$Type} from "packages/javax/net/ssl/$X509KeyManager"

export class $X509ExtendedKeyManager implements $X509KeyManager {


public "chooseEngineClientAlias"(arg0: (string)[], arg1: ($Principal$Type)[], arg2: $SSLEngine$Type): string
public "chooseEngineServerAlias"(arg0: string, arg1: ($Principal$Type)[], arg2: $SSLEngine$Type): string
public "getCertificateChain"(arg0: string): ($X509Certificate)[]
public "getPrivateKey"(arg0: string): $PrivateKey
public "getClientAliases"(arg0: string, arg1: ($Principal$Type)[]): (string)[]
public "chooseClientAlias"(arg0: (string)[], arg1: ($Principal$Type)[], arg2: $Socket$Type): string
public "getServerAliases"(arg0: string, arg1: ($Principal$Type)[]): (string)[]
public "chooseServerAlias"(arg0: string, arg1: ($Principal$Type)[], arg2: $Socket$Type): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $X509ExtendedKeyManager$Type = ($X509ExtendedKeyManager);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $X509ExtendedKeyManager_ = $X509ExtendedKeyManager$Type;
}}
declare module "packages/javax/net/ssl/$SSLSocketFactory" {
import {$SocketFactory, $SocketFactory$Type} from "packages/javax/net/$SocketFactory"
import {$InputStream, $InputStream$Type} from "packages/java/io/$InputStream"
import {$Socket, $Socket$Type} from "packages/java/net/$Socket"

export class $SSLSocketFactory extends $SocketFactory {

constructor()

public static "getDefault"(): $SocketFactory
public "getDefaultCipherSuites"(): (string)[]
public "getSupportedCipherSuites"(): (string)[]
public "createSocket"(arg0: $Socket$Type, arg1: $InputStream$Type, arg2: boolean): $Socket
public "createSocket"(arg0: $Socket$Type, arg1: string, arg2: integer, arg3: boolean): $Socket
get "default"(): $SocketFactory
get "defaultCipherSuites"(): (string)[]
get "supportedCipherSuites"(): (string)[]
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SSLSocketFactory$Type = ($SSLSocketFactory);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SSLSocketFactory_ = $SSLSocketFactory$Type;
}}
declare module "packages/javax/net/ssl/$SSLEngine" {
import {$SSLEngineResult, $SSLEngineResult$Type} from "packages/javax/net/ssl/$SSLEngineResult"
import {$SSLSession, $SSLSession$Type} from "packages/javax/net/ssl/$SSLSession"
import {$List, $List$Type} from "packages/java/util/$List"
import {$SSLParameters, $SSLParameters$Type} from "packages/javax/net/ssl/$SSLParameters"
import {$SSLEngineResult$HandshakeStatus, $SSLEngineResult$HandshakeStatus$Type} from "packages/javax/net/ssl/$SSLEngineResult$HandshakeStatus"
import {$ByteBuffer, $ByteBuffer$Type} from "packages/java/nio/$ByteBuffer"
import {$Runnable, $Runnable$Type} from "packages/java/lang/$Runnable"
import {$BiFunction, $BiFunction$Type} from "packages/java/util/function/$BiFunction"

export class $SSLEngine {


public "wrap"(arg0: ($ByteBuffer$Type)[], arg1: $ByteBuffer$Type): $SSLEngineResult
public "wrap"(arg0: $ByteBuffer$Type, arg1: $ByteBuffer$Type): $SSLEngineResult
public "wrap"(arg0: ($ByteBuffer$Type)[], arg1: integer, arg2: integer, arg3: $ByteBuffer$Type): $SSLEngineResult
public "unwrap"(arg0: $ByteBuffer$Type, arg1: $ByteBuffer$Type): $SSLEngineResult
public "unwrap"(arg0: $ByteBuffer$Type, arg1: ($ByteBuffer$Type)[], arg2: integer, arg3: integer): $SSLEngineResult
public "unwrap"(arg0: $ByteBuffer$Type, arg1: ($ByteBuffer$Type)[]): $SSLEngineResult
public "getDelegatedTask"(): $Runnable
public "isInboundDone"(): boolean
public "beginHandshake"(): void
public "getSupportedCipherSuites"(): (string)[]
public "getSSLParameters"(): $SSLParameters
public "getSupportedProtocols"(): (string)[]
public "setEnabledProtocols"(arg0: (string)[]): void
public "setEnabledCipherSuites"(arg0: (string)[]): void
public "setSSLParameters"(arg0: $SSLParameters$Type): void
public "setUseClientMode"(arg0: boolean): void
public "closeInbound"(): void
public "closeOutbound"(): void
public "isOutboundDone"(): boolean
public "getEnabledCipherSuites"(): (string)[]
public "getEnabledProtocols"(): (string)[]
public "getUseClientMode"(): boolean
public "setNeedClientAuth"(arg0: boolean): void
public "getNeedClientAuth"(): boolean
public "setWantClientAuth"(arg0: boolean): void
public "getWantClientAuth"(): boolean
public "setEnableSessionCreation"(arg0: boolean): void
public "getEnableSessionCreation"(): boolean
public "getApplicationProtocol"(): string
public "getHandshakeApplicationProtocol"(): string
public "setHandshakeApplicationProtocolSelector"(arg0: $BiFunction$Type<($SSLEngine$Type), ($List$Type<(string)>), (string)>): void
public "getHandshakeApplicationProtocolSelector"(): $BiFunction<($SSLEngine), ($List<(string)>), (string)>
public "getHandshakeStatus"(): $SSLEngineResult$HandshakeStatus
public "getSession"(): $SSLSession
public "getPeerHost"(): string
public "getPeerPort"(): integer
public "getHandshakeSession"(): $SSLSession
get "delegatedTask"(): $Runnable
get "inboundDone"(): boolean
get "supportedCipherSuites"(): (string)[]
get "sSLParameters"(): $SSLParameters
get "supportedProtocols"(): (string)[]
set "enabledProtocols"(value: (string)[])
set "enabledCipherSuites"(value: (string)[])
set "sSLParameters"(value: $SSLParameters$Type)
set "useClientMode"(value: boolean)
get "outboundDone"(): boolean
get "enabledCipherSuites"(): (string)[]
get "enabledProtocols"(): (string)[]
get "useClientMode"(): boolean
set "needClientAuth"(value: boolean)
get "needClientAuth"(): boolean
set "wantClientAuth"(value: boolean)
get "wantClientAuth"(): boolean
set "enableSessionCreation"(value: boolean)
get "enableSessionCreation"(): boolean
get "applicationProtocol"(): string
get "handshakeApplicationProtocol"(): string
set "handshakeApplicationProtocolSelector"(value: $BiFunction$Type<($SSLEngine$Type), ($List$Type<(string)>), (string)>)
get "handshakeApplicationProtocolSelector"(): $BiFunction<($SSLEngine), ($List<(string)>), (string)>
get "handshakeStatus"(): $SSLEngineResult$HandshakeStatus
get "session"(): $SSLSession
get "peerHost"(): string
get "peerPort"(): integer
get "handshakeSession"(): $SSLSession
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SSLEngine$Type = ($SSLEngine);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SSLEngine_ = $SSLEngine$Type;
}}
declare module "packages/javax/net/ssl/$SNIServerName" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $SNIServerName {


public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "getType"(): integer
public "getEncoded"(): (byte)[]
get "type"(): integer
get "encoded"(): (byte)[]
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SNIServerName$Type = ($SNIServerName);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SNIServerName_ = $SNIServerName$Type;
}}
declare module "packages/javax/net/ssl/$TrustManager" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $TrustManager {

}

export namespace $TrustManager {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TrustManager$Type = ($TrustManager);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TrustManager_ = $TrustManager$Type;
}}
declare module "packages/javax/net/ssl/$SSLEngineResult$HandshakeStatus" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $SSLEngineResult$HandshakeStatus extends $Enum<($SSLEngineResult$HandshakeStatus)> {
static readonly "NOT_HANDSHAKING": $SSLEngineResult$HandshakeStatus
static readonly "FINISHED": $SSLEngineResult$HandshakeStatus
static readonly "NEED_TASK": $SSLEngineResult$HandshakeStatus
static readonly "NEED_WRAP": $SSLEngineResult$HandshakeStatus
static readonly "NEED_UNWRAP": $SSLEngineResult$HandshakeStatus
static readonly "NEED_UNWRAP_AGAIN": $SSLEngineResult$HandshakeStatus


public static "values"(): ($SSLEngineResult$HandshakeStatus)[]
public static "valueOf"(arg0: string): $SSLEngineResult$HandshakeStatus
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SSLEngineResult$HandshakeStatus$Type = (("need_unwrap_again") | ("need_task") | ("need_wrap") | ("finished") | ("not_handshaking") | ("need_unwrap")) | ($SSLEngineResult$HandshakeStatus);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SSLEngineResult$HandshakeStatus_ = $SSLEngineResult$HandshakeStatus$Type;
}}
declare module "packages/javax/net/ssl/$SSLSessionContext" {
import {$SSLSession, $SSLSession$Type} from "packages/javax/net/ssl/$SSLSession"
import {$Enumeration, $Enumeration$Type} from "packages/java/util/$Enumeration"

export interface $SSLSessionContext {

 "getSession"(arg0: (byte)[]): $SSLSession
 "setSessionTimeout"(arg0: integer): void
 "getIds"(): $Enumeration<((byte)[])>
 "getSessionTimeout"(): integer
 "setSessionCacheSize"(arg0: integer): void
 "getSessionCacheSize"(): integer
}

export namespace $SSLSessionContext {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SSLSessionContext$Type = ($SSLSessionContext);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SSLSessionContext_ = $SSLSessionContext$Type;
}}
declare module "packages/javax/net/ssl/$HandshakeCompletedListener" {
import {$HandshakeCompletedEvent, $HandshakeCompletedEvent$Type} from "packages/javax/net/ssl/$HandshakeCompletedEvent"
import {$EventListener, $EventListener$Type} from "packages/java/util/$EventListener"

export interface $HandshakeCompletedListener extends $EventListener {

 "handshakeCompleted"(arg0: $HandshakeCompletedEvent$Type): void

(arg0: $HandshakeCompletedEvent$Type): void
}

export namespace $HandshakeCompletedListener {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $HandshakeCompletedListener$Type = ($HandshakeCompletedListener);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $HandshakeCompletedListener_ = $HandshakeCompletedListener$Type;
}}
declare module "packages/javax/net/ssl/$SSLSocket" {
import {$SSLSession, $SSLSession$Type} from "packages/javax/net/ssl/$SSLSession"
import {$List, $List$Type} from "packages/java/util/$List"
import {$SSLParameters, $SSLParameters$Type} from "packages/javax/net/ssl/$SSLParameters"
import {$Socket, $Socket$Type} from "packages/java/net/$Socket"
import {$HandshakeCompletedListener, $HandshakeCompletedListener$Type} from "packages/javax/net/ssl/$HandshakeCompletedListener"
import {$BiFunction, $BiFunction$Type} from "packages/java/util/function/$BiFunction"

export class $SSLSocket extends $Socket {


public "getSupportedCipherSuites"(): (string)[]
public "getSSLParameters"(): $SSLParameters
public "getSupportedProtocols"(): (string)[]
public "setEnabledProtocols"(arg0: (string)[]): void
public "setEnabledCipherSuites"(arg0: (string)[]): void
public "addHandshakeCompletedListener"(arg0: $HandshakeCompletedListener$Type): void
public "setSSLParameters"(arg0: $SSLParameters$Type): void
public "startHandshake"(): void
public "removeHandshakeCompletedListener"(arg0: $HandshakeCompletedListener$Type): void
public "setUseClientMode"(arg0: boolean): void
public "getEnabledCipherSuites"(): (string)[]
public "getEnabledProtocols"(): (string)[]
public "getUseClientMode"(): boolean
public "setNeedClientAuth"(arg0: boolean): void
public "getNeedClientAuth"(): boolean
public "setWantClientAuth"(arg0: boolean): void
public "getWantClientAuth"(): boolean
public "setEnableSessionCreation"(arg0: boolean): void
public "getEnableSessionCreation"(): boolean
public "getApplicationProtocol"(): string
public "getHandshakeApplicationProtocol"(): string
public "setHandshakeApplicationProtocolSelector"(arg0: $BiFunction$Type<($SSLSocket$Type), ($List$Type<(string)>), (string)>): void
public "getHandshakeApplicationProtocolSelector"(): $BiFunction<($SSLSocket), ($List<(string)>), (string)>
public "getSession"(): $SSLSession
public "getHandshakeSession"(): $SSLSession
get "supportedCipherSuites"(): (string)[]
get "sSLParameters"(): $SSLParameters
get "supportedProtocols"(): (string)[]
set "enabledProtocols"(value: (string)[])
set "enabledCipherSuites"(value: (string)[])
set "sSLParameters"(value: $SSLParameters$Type)
set "useClientMode"(value: boolean)
get "enabledCipherSuites"(): (string)[]
get "enabledProtocols"(): (string)[]
get "useClientMode"(): boolean
set "needClientAuth"(value: boolean)
get "needClientAuth"(): boolean
set "wantClientAuth"(value: boolean)
get "wantClientAuth"(): boolean
set "enableSessionCreation"(value: boolean)
get "enableSessionCreation"(): boolean
get "applicationProtocol"(): string
get "handshakeApplicationProtocol"(): string
set "handshakeApplicationProtocolSelector"(value: $BiFunction$Type<($SSLSocket$Type), ($List$Type<(string)>), (string)>)
get "handshakeApplicationProtocolSelector"(): $BiFunction<($SSLSocket), ($List<(string)>), (string)>
get "session"(): $SSLSession
get "handshakeSession"(): $SSLSession
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SSLSocket$Type = ($SSLSocket);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SSLSocket_ = $SSLSocket$Type;
}}
declare module "packages/javax/net/ssl/$KeyManager" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $KeyManager {

}

export namespace $KeyManager {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $KeyManager$Type = ($KeyManager);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $KeyManager_ = $KeyManager$Type;
}}
declare module "packages/javax/net/ssl/$SSLContext" {
import {$SecureRandom, $SecureRandom$Type} from "packages/java/security/$SecureRandom"
import {$SSLSessionContext, $SSLSessionContext$Type} from "packages/javax/net/ssl/$SSLSessionContext"
import {$SSLSocketFactory, $SSLSocketFactory$Type} from "packages/javax/net/ssl/$SSLSocketFactory"
import {$SSLEngine, $SSLEngine$Type} from "packages/javax/net/ssl/$SSLEngine"
import {$SSLServerSocketFactory, $SSLServerSocketFactory$Type} from "packages/javax/net/ssl/$SSLServerSocketFactory"
import {$SSLParameters, $SSLParameters$Type} from "packages/javax/net/ssl/$SSLParameters"
import {$Provider, $Provider$Type} from "packages/java/security/$Provider"
import {$TrustManager, $TrustManager$Type} from "packages/javax/net/ssl/$TrustManager"
import {$KeyManager, $KeyManager$Type} from "packages/javax/net/ssl/$KeyManager"

export class $SSLContext {


public static "getDefault"(): $SSLContext
public static "getInstance"(arg0: string, arg1: $Provider$Type): $SSLContext
public static "getInstance"(arg0: string, arg1: string): $SSLContext
public static "getInstance"(arg0: string): $SSLContext
public "init"(arg0: ($KeyManager$Type)[], arg1: ($TrustManager$Type)[], arg2: $SecureRandom$Type): void
public "getProtocol"(): string
public "getProvider"(): $Provider
public static "setDefault"(arg0: $SSLContext$Type): void
public "createSSLEngine"(arg0: string, arg1: integer): $SSLEngine
public "createSSLEngine"(): $SSLEngine
public "getServerSessionContext"(): $SSLSessionContext
public "getClientSessionContext"(): $SSLSessionContext
public "getDefaultSSLParameters"(): $SSLParameters
public "getSupportedSSLParameters"(): $SSLParameters
public "getSocketFactory"(): $SSLSocketFactory
public "getServerSocketFactory"(): $SSLServerSocketFactory
get "default"(): $SSLContext
get "protocol"(): string
get "provider"(): $Provider
set "default"(value: $SSLContext$Type)
get "serverSessionContext"(): $SSLSessionContext
get "clientSessionContext"(): $SSLSessionContext
get "defaultSSLParameters"(): $SSLParameters
get "supportedSSLParameters"(): $SSLParameters
get "socketFactory"(): $SSLSocketFactory
get "serverSocketFactory"(): $SSLServerSocketFactory
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SSLContext$Type = ($SSLContext);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SSLContext_ = $SSLContext$Type;
}}
declare module "packages/javax/net/$SocketFactory" {
import {$Socket, $Socket$Type} from "packages/java/net/$Socket"
import {$InetAddress, $InetAddress$Type} from "packages/java/net/$InetAddress"

export class $SocketFactory {


public static "getDefault"(): $SocketFactory
public "createSocket"(arg0: $InetAddress$Type, arg1: integer): $Socket
public "createSocket"(arg0: string, arg1: integer, arg2: $InetAddress$Type, arg3: integer): $Socket
public "createSocket"(arg0: $InetAddress$Type, arg1: integer, arg2: $InetAddress$Type, arg3: integer): $Socket
public "createSocket"(arg0: string, arg1: integer): $Socket
public "createSocket"(): $Socket
get "default"(): $SocketFactory
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SocketFactory$Type = ($SocketFactory);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SocketFactory_ = $SocketFactory$Type;
}}
declare module "packages/javax/net/ssl/$SSLServerSocket" {
import {$ServerSocket, $ServerSocket$Type} from "packages/java/net/$ServerSocket"
import {$SSLParameters, $SSLParameters$Type} from "packages/javax/net/ssl/$SSLParameters"

export class $SSLServerSocket extends $ServerSocket {


public "getSupportedCipherSuites"(): (string)[]
public "getSSLParameters"(): $SSLParameters
public "getSupportedProtocols"(): (string)[]
public "setEnabledProtocols"(arg0: (string)[]): void
public "setEnabledCipherSuites"(arg0: (string)[]): void
public "setSSLParameters"(arg0: $SSLParameters$Type): void
public "setUseClientMode"(arg0: boolean): void
public "getEnabledCipherSuites"(): (string)[]
public "getEnabledProtocols"(): (string)[]
public "getUseClientMode"(): boolean
public "setNeedClientAuth"(arg0: boolean): void
public "getNeedClientAuth"(): boolean
public "setWantClientAuth"(arg0: boolean): void
public "getWantClientAuth"(): boolean
public "setEnableSessionCreation"(arg0: boolean): void
public "getEnableSessionCreation"(): boolean
get "supportedCipherSuites"(): (string)[]
get "sSLParameters"(): $SSLParameters
get "supportedProtocols"(): (string)[]
set "enabledProtocols"(value: (string)[])
set "enabledCipherSuites"(value: (string)[])
set "sSLParameters"(value: $SSLParameters$Type)
set "useClientMode"(value: boolean)
get "enabledCipherSuites"(): (string)[]
get "enabledProtocols"(): (string)[]
get "useClientMode"(): boolean
set "needClientAuth"(value: boolean)
get "needClientAuth"(): boolean
set "wantClientAuth"(value: boolean)
get "wantClientAuth"(): boolean
set "enableSessionCreation"(value: boolean)
get "enableSessionCreation"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SSLServerSocket$Type = ($SSLServerSocket);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SSLServerSocket_ = $SSLServerSocket$Type;
}}
declare module "packages/javax/net/ssl/$SSLParameters" {
import {$SNIServerName, $SNIServerName$Type} from "packages/javax/net/ssl/$SNIServerName"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$List, $List$Type} from "packages/java/util/$List"
import {$SNIMatcher, $SNIMatcher$Type} from "packages/javax/net/ssl/$SNIMatcher"
import {$AlgorithmConstraints, $AlgorithmConstraints$Type} from "packages/java/security/$AlgorithmConstraints"

export class $SSLParameters {

constructor(arg0: (string)[], arg1: (string)[])
constructor(arg0: (string)[])
constructor()

public "setCipherSuites"(arg0: (string)[]): void
public "setProtocols"(arg0: (string)[]): void
public "getAlgorithmConstraints"(): $AlgorithmConstraints
public "getProtocols"(): (string)[]
public "getCipherSuites"(): (string)[]
public "setEndpointIdentificationAlgorithm"(arg0: string): void
public "setServerNames"(arg0: $List$Type<($SNIServerName$Type)>): void
public "setNeedClientAuth"(arg0: boolean): void
public "getNeedClientAuth"(): boolean
public "setWantClientAuth"(arg0: boolean): void
public "getWantClientAuth"(): boolean
public "setMaximumPacketSize"(arg0: integer): void
public "getEndpointIdentificationAlgorithm"(): string
public "setAlgorithmConstraints"(arg0: $AlgorithmConstraints$Type): void
public "setSNIMatchers"(arg0: $Collection$Type<($SNIMatcher$Type)>): void
public "setApplicationProtocols"(arg0: (string)[]): void
public "setUseCipherSuitesOrder"(arg0: boolean): void
public "setEnableRetransmissions"(arg0: boolean): void
public "getServerNames"(): $List<($SNIServerName)>
public "getSNIMatchers"(): $Collection<($SNIMatcher)>
public "getApplicationProtocols"(): (string)[]
public "getUseCipherSuitesOrder"(): boolean
public "getEnableRetransmissions"(): boolean
public "getMaximumPacketSize"(): integer
set "cipherSuites"(value: (string)[])
set "protocols"(value: (string)[])
get "algorithmConstraints"(): $AlgorithmConstraints
get "protocols"(): (string)[]
get "cipherSuites"(): (string)[]
set "endpointIdentificationAlgorithm"(value: string)
set "serverNames"(value: $List$Type<($SNIServerName$Type)>)
set "needClientAuth"(value: boolean)
get "needClientAuth"(): boolean
set "wantClientAuth"(value: boolean)
get "wantClientAuth"(): boolean
set "maximumPacketSize"(value: integer)
get "endpointIdentificationAlgorithm"(): string
set "algorithmConstraints"(value: $AlgorithmConstraints$Type)
set "sNIMatchers"(value: $Collection$Type<($SNIMatcher$Type)>)
set "applicationProtocols"(value: (string)[])
set "useCipherSuitesOrder"(value: boolean)
set "enableRetransmissions"(value: boolean)
get "serverNames"(): $List<($SNIServerName)>
get "sNIMatchers"(): $Collection<($SNIMatcher)>
get "applicationProtocols"(): (string)[]
get "useCipherSuitesOrder"(): boolean
get "enableRetransmissions"(): boolean
get "maximumPacketSize"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SSLParameters$Type = ($SSLParameters);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SSLParameters_ = $SSLParameters$Type;
}}
declare module "packages/javax/net/ssl/$SSLSession" {
import {$Certificate, $Certificate$Type} from "packages/java/security/cert/$Certificate"
import {$X509Certificate, $X509Certificate$Type} from "packages/javax/security/cert/$X509Certificate"
import {$SSLSessionContext, $SSLSessionContext$Type} from "packages/javax/net/ssl/$SSLSessionContext"
import {$Principal, $Principal$Type} from "packages/java/security/$Principal"

export interface $SSLSession {

 "getValue"(arg0: string): any
 "getId"(): (byte)[]
 "getProtocol"(): string
 "isValid"(): boolean
 "putValue"(arg0: string, arg1: any): void
 "getCreationTime"(): long
 "invalidate"(): void
 "getCipherSuite"(): string
 "getLocalCertificates"(): ($Certificate)[]
 "getPeerPrincipal"(): $Principal
 "getLocalPrincipal"(): $Principal
 "getPeerCertificates"(): ($Certificate)[]
 "getPeerHost"(): string
 "getPeerPort"(): integer
 "getSessionContext"(): $SSLSessionContext
 "getLastAccessedTime"(): long
 "removeValue"(arg0: string): void
 "getValueNames"(): (string)[]
 "getPacketBufferSize"(): integer
 "getApplicationBufferSize"(): integer
/**
 * 
 * @deprecated
 */
 "getPeerCertificateChain"(): ($X509Certificate)[]
}

export namespace $SSLSession {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SSLSession$Type = ($SSLSession);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SSLSession_ = $SSLSession$Type;
}}
declare module "packages/javax/net/ssl/$SSLServerSocketFactory" {
import {$ServerSocketFactory, $ServerSocketFactory$Type} from "packages/javax/net/$ServerSocketFactory"

export class $SSLServerSocketFactory extends $ServerSocketFactory {


public static "getDefault"(): $ServerSocketFactory
public "getDefaultCipherSuites"(): (string)[]
public "getSupportedCipherSuites"(): (string)[]
get "default"(): $ServerSocketFactory
get "defaultCipherSuites"(): (string)[]
get "supportedCipherSuites"(): (string)[]
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SSLServerSocketFactory$Type = ($SSLServerSocketFactory);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SSLServerSocketFactory_ = $SSLServerSocketFactory$Type;
}}
declare module "packages/javax/net/ssl/$X509KeyManager" {
import {$X509Certificate, $X509Certificate$Type} from "packages/java/security/cert/$X509Certificate"
import {$PrivateKey, $PrivateKey$Type} from "packages/java/security/$PrivateKey"
import {$Principal, $Principal$Type} from "packages/java/security/$Principal"
import {$Socket, $Socket$Type} from "packages/java/net/$Socket"
import {$KeyManager, $KeyManager$Type} from "packages/javax/net/ssl/$KeyManager"

export interface $X509KeyManager extends $KeyManager {

 "getCertificateChain"(arg0: string): ($X509Certificate)[]
 "getPrivateKey"(arg0: string): $PrivateKey
 "getClientAliases"(arg0: string, arg1: ($Principal$Type)[]): (string)[]
 "chooseClientAlias"(arg0: (string)[], arg1: ($Principal$Type)[], arg2: $Socket$Type): string
 "getServerAliases"(arg0: string, arg1: ($Principal$Type)[]): (string)[]
 "chooseServerAlias"(arg0: string, arg1: ($Principal$Type)[], arg2: $Socket$Type): string
}

export namespace $X509KeyManager {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $X509KeyManager$Type = ($X509KeyManager);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $X509KeyManager_ = $X509KeyManager$Type;
}}
declare module "packages/javax/net/ssl/$SNIMatcher" {
import {$SNIServerName, $SNIServerName$Type} from "packages/javax/net/ssl/$SNIServerName"

export class $SNIMatcher {


public "matches"(arg0: $SNIServerName$Type): boolean
public "getType"(): integer
get "type"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SNIMatcher$Type = ($SNIMatcher);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SNIMatcher_ = $SNIMatcher$Type;
}}
declare module "packages/javax/net/ssl/$HandshakeCompletedEvent" {
import {$Certificate, $Certificate$Type} from "packages/java/security/cert/$Certificate"
import {$X509Certificate, $X509Certificate$Type} from "packages/javax/security/cert/$X509Certificate"
import {$SSLSession, $SSLSession$Type} from "packages/javax/net/ssl/$SSLSession"
import {$SSLSocket, $SSLSocket$Type} from "packages/javax/net/ssl/$SSLSocket"
import {$Principal, $Principal$Type} from "packages/java/security/$Principal"
import {$EventObject, $EventObject$Type} from "packages/java/util/$EventObject"

export class $HandshakeCompletedEvent extends $EventObject {

constructor(arg0: $SSLSocket$Type, arg1: $SSLSession$Type)

public "getCipherSuite"(): string
public "getLocalCertificates"(): ($Certificate)[]
public "getPeerPrincipal"(): $Principal
public "getLocalPrincipal"(): $Principal
public "getPeerCertificates"(): ($Certificate)[]
public "getSocket"(): $SSLSocket
public "getSession"(): $SSLSession
/**
 * 
 * @deprecated
 */
public "getPeerCertificateChain"(): ($X509Certificate)[]
get "cipherSuite"(): string
get "localCertificates"(): ($Certificate)[]
get "peerPrincipal"(): $Principal
get "localPrincipal"(): $Principal
get "peerCertificates"(): ($Certificate)[]
get "socket"(): $SSLSocket
get "session"(): $SSLSession
get "peerCertificateChain"(): ($X509Certificate)[]
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $HandshakeCompletedEvent$Type = ($HandshakeCompletedEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $HandshakeCompletedEvent_ = $HandshakeCompletedEvent$Type;
}}
declare module "packages/javax/net/ssl/$SSLEngineResult" {
import {$SSLEngineResult$Status, $SSLEngineResult$Status$Type} from "packages/javax/net/ssl/$SSLEngineResult$Status"
import {$SSLEngineResult$HandshakeStatus, $SSLEngineResult$HandshakeStatus$Type} from "packages/javax/net/ssl/$SSLEngineResult$HandshakeStatus"

export class $SSLEngineResult {

constructor(arg0: $SSLEngineResult$Status$Type, arg1: $SSLEngineResult$HandshakeStatus$Type, arg2: integer, arg3: integer)
constructor(arg0: $SSLEngineResult$Status$Type, arg1: $SSLEngineResult$HandshakeStatus$Type, arg2: integer, arg3: integer, arg4: long)

public "toString"(): string
public "getHandshakeStatus"(): $SSLEngineResult$HandshakeStatus
public "sequenceNumber"(): long
public "getStatus"(): $SSLEngineResult$Status
public "bytesProduced"(): integer
public "bytesConsumed"(): integer
get "handshakeStatus"(): $SSLEngineResult$HandshakeStatus
get "status"(): $SSLEngineResult$Status
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SSLEngineResult$Type = ($SSLEngineResult);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SSLEngineResult_ = $SSLEngineResult$Type;
}}
declare module "packages/javax/net/ssl/$SSLEngineResult$Status" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $SSLEngineResult$Status extends $Enum<($SSLEngineResult$Status)> {
static readonly "BUFFER_UNDERFLOW": $SSLEngineResult$Status
static readonly "BUFFER_OVERFLOW": $SSLEngineResult$Status
static readonly "OK": $SSLEngineResult$Status
static readonly "CLOSED": $SSLEngineResult$Status


public static "values"(): ($SSLEngineResult$Status)[]
public static "valueOf"(arg0: string): $SSLEngineResult$Status
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SSLEngineResult$Status$Type = (("buffer_underflow") | ("closed") | ("ok") | ("buffer_overflow")) | ($SSLEngineResult$Status);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SSLEngineResult$Status_ = $SSLEngineResult$Status$Type;
}}

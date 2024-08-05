declare module "packages/java/nio/file/$CopyOption" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export interface $CopyOption {

}

export namespace $CopyOption {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CopyOption$Type = ($CopyOption);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CopyOption_ = $CopyOption$Type;
}}
declare module "packages/java/nio/channels/spi/$AbstractInterruptibleChannel" {
import {$Channel, $Channel$Type} from "packages/java/nio/channels/$Channel"
import {$InterruptibleChannel, $InterruptibleChannel$Type} from "packages/java/nio/channels/$InterruptibleChannel"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $AbstractInterruptibleChannel implements $Channel, $InterruptibleChannel {


public "isOpen"(): boolean
public "close"(): void
get "open"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AbstractInterruptibleChannel$Type = ($AbstractInterruptibleChannel);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AbstractInterruptibleChannel_ = $AbstractInterruptibleChannel$Type;
}}
declare module "packages/java/nio/file/$WatchEvent$Modifier" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export interface $WatchEvent$Modifier {

 "name"(): string

(): string
}

export namespace $WatchEvent$Modifier {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WatchEvent$Modifier$Type = ($WatchEvent$Modifier);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WatchEvent$Modifier_ = $WatchEvent$Modifier$Type;
}}
declare module "packages/java/nio/file/attribute/$GroupPrincipal" {
import {$Subject, $Subject$Type} from "packages/javax/security/auth/$Subject"
import {$UserPrincipal, $UserPrincipal$Type} from "packages/java/nio/file/attribute/$UserPrincipal"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export interface $GroupPrincipal extends $UserPrincipal {

 "getName"(): string
 "equals"(arg0: any): boolean
 "toString"(): string
 "hashCode"(): integer
 "implies"(arg0: $Subject$Type): boolean
}

export namespace $GroupPrincipal {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GroupPrincipal$Type = ($GroupPrincipal);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GroupPrincipal_ = $GroupPrincipal$Type;
}}
declare module "packages/java/nio/charset/$CodingErrorAction" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $CodingErrorAction {
static readonly "IGNORE": $CodingErrorAction
static readonly "REPLACE": $CodingErrorAction
static readonly "REPORT": $CodingErrorAction


public "toString"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CodingErrorAction$Type = ($CodingErrorAction);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CodingErrorAction_ = $CodingErrorAction$Type;
}}
declare module "packages/java/nio/channels/$MulticastChannel" {
import {$SocketAddress, $SocketAddress$Type} from "packages/java/net/$SocketAddress"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$NetworkChannel, $NetworkChannel$Type} from "packages/java/nio/channels/$NetworkChannel"
import {$NetworkInterface, $NetworkInterface$Type} from "packages/java/net/$NetworkInterface"
import {$InetAddress, $InetAddress$Type} from "packages/java/net/$InetAddress"
import {$SocketOption, $SocketOption$Type} from "packages/java/net/$SocketOption"
import {$MembershipKey, $MembershipKey$Type} from "packages/java/nio/channels/$MembershipKey"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export interface $MulticastChannel extends $NetworkChannel {

 "join"(arg0: $InetAddress$Type, arg1: $NetworkInterface$Type): $MembershipKey
 "join"(arg0: $InetAddress$Type, arg1: $NetworkInterface$Type, arg2: $InetAddress$Type): $MembershipKey
 "close"(): void
 "bind"(arg0: $SocketAddress$Type): $NetworkChannel
 "getLocalAddress"(): $SocketAddress
 "supportedOptions"(): $Set<($SocketOption<(any)>)>
 "getOption"<T>(arg0: $SocketOption$Type<(T)>): T
 "setOption"<T>(arg0: $SocketOption$Type<(T)>, arg1: T): $NetworkChannel
 "isOpen"(): boolean
}

export namespace $MulticastChannel {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MulticastChannel$Type = ($MulticastChannel);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MulticastChannel_ = $MulticastChannel$Type;
}}
declare module "packages/java/nio/$ShortBuffer" {
import {$Comparable, $Comparable$Type} from "packages/java/lang/$Comparable"
import {$Buffer, $Buffer$Type} from "packages/java/nio/$Buffer"
import {$ByteOrder, $ByteOrder$Type} from "packages/java/nio/$ByteOrder"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $ShortBuffer extends $Buffer implements $Comparable<($ShortBuffer)> {


public "get"(): short
public "get"(arg0: integer, arg1: (short)[]): $ShortBuffer
public "get"(arg0: integer): short
public "get"(arg0: integer, arg1: (short)[], arg2: integer, arg3: integer): $ShortBuffer
public "get"(arg0: (short)[], arg1: integer, arg2: integer): $ShortBuffer
public "get"(arg0: (short)[]): $ShortBuffer
public "put"(arg0: (short)[]): $ShortBuffer
public "put"(arg0: integer, arg1: (short)[], arg2: integer, arg3: integer): $ShortBuffer
public "put"(arg0: integer, arg1: (short)[]): $ShortBuffer
public "put"(arg0: (short)[], arg1: integer, arg2: integer): $ShortBuffer
public "put"(arg0: integer, arg1: $ShortBuffer$Type, arg2: integer, arg3: integer): $ShortBuffer
public "put"(arg0: $ShortBuffer$Type): $ShortBuffer
public "put"(arg0: integer, arg1: short): $ShortBuffer
public "put"(arg0: short): $ShortBuffer
public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "compareTo"(arg0: $ShortBuffer$Type): integer
public static "wrap"(arg0: (short)[]): $ShortBuffer
public static "wrap"(arg0: (short)[], arg1: integer, arg2: integer): $ShortBuffer
public "position"(arg0: integer): $ShortBuffer
public "limit"(arg0: integer): $ShortBuffer
public "isDirect"(): boolean
public "hasArray"(): boolean
public "array"(): (short)[]
public "arrayOffset"(): integer
public "duplicate"(): $ShortBuffer
public "order"(): $ByteOrder
public "mismatch"(arg0: $ShortBuffer$Type): integer
public static "allocate"(arg0: integer): $ShortBuffer
public "asReadOnlyBuffer"(): $ShortBuffer
public "compact"(): $ShortBuffer
get "direct"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ShortBuffer$Type = ($ShortBuffer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ShortBuffer_ = $ShortBuffer$Type;
}}
declare module "packages/java/nio/channels/$SocketChannel" {
import {$SocketAddress, $SocketAddress$Type} from "packages/java/net/$SocketAddress"
import {$ByteChannel, $ByteChannel$Type} from "packages/java/nio/channels/$ByteChannel"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$GatheringByteChannel, $GatheringByteChannel$Type} from "packages/java/nio/channels/$GatheringByteChannel"
import {$NetworkChannel, $NetworkChannel$Type} from "packages/java/nio/channels/$NetworkChannel"
import {$ProtocolFamily, $ProtocolFamily$Type} from "packages/java/net/$ProtocolFamily"
import {$Socket, $Socket$Type} from "packages/java/net/$Socket"
import {$ByteBuffer, $ByteBuffer$Type} from "packages/java/nio/$ByteBuffer"
import {$SocketOption, $SocketOption$Type} from "packages/java/net/$SocketOption"
import {$ScatteringByteChannel, $ScatteringByteChannel$Type} from "packages/java/nio/channels/$ScatteringByteChannel"
import {$AbstractSelectableChannel, $AbstractSelectableChannel$Type} from "packages/java/nio/channels/spi/$AbstractSelectableChannel"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $SocketChannel extends $AbstractSelectableChannel implements $ByteChannel, $ScatteringByteChannel, $GatheringByteChannel, $NetworkChannel {


public "write"(arg0: ($ByteBuffer$Type)[], arg1: integer, arg2: integer): long
public "write"(arg0: ($ByteBuffer$Type)[]): long
public "write"(arg0: $ByteBuffer$Type): integer
public "read"(arg0: ($ByteBuffer$Type)[], arg1: integer, arg2: integer): long
public "read"(arg0: $ByteBuffer$Type): integer
public "read"(arg0: ($ByteBuffer$Type)[]): long
public "connect"(arg0: $SocketAddress$Type): boolean
public static "open"(arg0: $ProtocolFamily$Type): $SocketChannel
public static "open"(): $SocketChannel
public static "open"(arg0: $SocketAddress$Type): $SocketChannel
public "validOps"(): integer
public "getLocalAddress"(): $SocketAddress
public "isConnected"(): boolean
public "shutdownInput"(): $SocketChannel
public "shutdownOutput"(): $SocketChannel
public "isConnectionPending"(): boolean
public "finishConnect"(): boolean
public "getRemoteAddress"(): $SocketAddress
public "socket"(): $Socket
public "setOption"<T>(arg0: $SocketOption$Type<(T)>, arg1: T): $SocketChannel
public "supportedOptions"(): $Set<($SocketOption<(any)>)>
public "getOption"<T>(arg0: $SocketOption$Type<(T)>): T
public "isOpen"(): boolean
public "close"(): void
get "localAddress"(): $SocketAddress
get "connected"(): boolean
get "connectionPending"(): boolean
get "remoteAddress"(): $SocketAddress
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SocketChannel$Type = ($SocketChannel);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SocketChannel_ = $SocketChannel$Type;
}}
declare module "packages/java/nio/channels/$GatheringByteChannel" {
import {$WritableByteChannel, $WritableByteChannel$Type} from "packages/java/nio/channels/$WritableByteChannel"
import {$ByteBuffer, $ByteBuffer$Type} from "packages/java/nio/$ByteBuffer"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export interface $GatheringByteChannel extends $WritableByteChannel {

 "write"(arg0: ($ByteBuffer$Type)[], arg1: integer, arg2: integer): long
 "write"(arg0: ($ByteBuffer$Type)[]): long
 "write"(arg0: $ByteBuffer$Type): integer
 "isOpen"(): boolean
 "close"(): void
}

export namespace $GatheringByteChannel {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GatheringByteChannel$Type = ($GatheringByteChannel);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GatheringByteChannel_ = $GatheringByteChannel$Type;
}}
declare module "packages/java/nio/channels/$AsynchronousFileChannel" {
import {$ExecutorService, $ExecutorService$Type} from "packages/java/util/concurrent/$ExecutorService"
import {$AsynchronousChannel, $AsynchronousChannel$Type} from "packages/java/nio/channels/$AsynchronousChannel"
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$CompletionHandler, $CompletionHandler$Type} from "packages/java/nio/channels/$CompletionHandler"
import {$OpenOption, $OpenOption$Type} from "packages/java/nio/file/$OpenOption"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$Future, $Future$Type} from "packages/java/util/concurrent/$Future"
import {$ByteBuffer, $ByteBuffer$Type} from "packages/java/nio/$ByteBuffer"
import {$FileLock, $FileLock$Type} from "packages/java/nio/channels/$FileLock"
import {$FileAttribute, $FileAttribute$Type} from "packages/java/nio/file/attribute/$FileAttribute"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $AsynchronousFileChannel implements $AsynchronousChannel {


public "lock"(arg0: long, arg1: long, arg2: boolean): $Future<($FileLock)>
public "lock"<A>(arg0: A, arg1: $CompletionHandler$Type<($FileLock$Type), (any)>): void
public "lock"<A>(arg0: long, arg1: long, arg2: boolean, arg3: A, arg4: $CompletionHandler$Type<($FileLock$Type), (any)>): void
public "lock"(): $Future<($FileLock)>
public "size"(): long
public "write"(arg0: $ByteBuffer$Type, arg1: long): $Future<(integer)>
public "write"<A>(arg0: $ByteBuffer$Type, arg1: long, arg2: A, arg3: $CompletionHandler$Type<(integer), (any)>): void
public "read"(arg0: $ByteBuffer$Type, arg1: long): $Future<(integer)>
public "read"<A>(arg0: $ByteBuffer$Type, arg1: long, arg2: A, arg3: $CompletionHandler$Type<(integer), (any)>): void
public static "open"(arg0: $Path$Type, arg1: $Set$Type<(any)>, arg2: $ExecutorService$Type, ...arg3: ($FileAttribute$Type<(any)>)[]): $AsynchronousFileChannel
public static "open"(arg0: $Path$Type, ...arg1: ($OpenOption$Type)[]): $AsynchronousFileChannel
public "tryLock"(): $FileLock
public "tryLock"(arg0: long, arg1: long, arg2: boolean): $FileLock
public "force"(arg0: boolean): void
public "truncate"(arg0: long): $AsynchronousFileChannel
public "close"(): void
public "isOpen"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AsynchronousFileChannel$Type = ($AsynchronousFileChannel);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AsynchronousFileChannel_ = $AsynchronousFileChannel$Type;
}}
declare module "packages/java/nio/file/$OpenOption" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export interface $OpenOption {

}

export namespace $OpenOption {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $OpenOption$Type = ($OpenOption);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $OpenOption_ = $OpenOption$Type;
}}
declare module "packages/java/nio/file/$AccessMode" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $AccessMode extends $Enum<($AccessMode)> {
static readonly "READ": $AccessMode
static readonly "WRITE": $AccessMode
static readonly "EXECUTE": $AccessMode


public static "values"(): ($AccessMode)[]
public static "valueOf"(arg0: string): $AccessMode
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AccessMode$Type = (("read") | ("write") | ("execute")) | ($AccessMode);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AccessMode_ = $AccessMode$Type;
}}
declare module "packages/java/nio/channels/$ScatteringByteChannel" {
import {$ReadableByteChannel, $ReadableByteChannel$Type} from "packages/java/nio/channels/$ReadableByteChannel"
import {$ByteBuffer, $ByteBuffer$Type} from "packages/java/nio/$ByteBuffer"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export interface $ScatteringByteChannel extends $ReadableByteChannel {

 "read"(arg0: ($ByteBuffer$Type)[], arg1: integer, arg2: integer): long
 "read"(arg0: ($ByteBuffer$Type)[]): long
 "read"(arg0: $ByteBuffer$Type): integer
 "isOpen"(): boolean
 "close"(): void
}

export namespace $ScatteringByteChannel {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ScatteringByteChannel$Type = ($ScatteringByteChannel);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ScatteringByteChannel_ = $ScatteringByteChannel$Type;
}}
declare module "packages/java/nio/$Buffer" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $Buffer {


public "clear"(): $Buffer
public "position"(): integer
public "position"(arg0: integer): $Buffer
public "limit"(arg0: integer): $Buffer
public "limit"(): integer
public "remaining"(): integer
public "isDirect"(): boolean
public "hasArray"(): boolean
public "array"(): any
public "arrayOffset"(): integer
public "capacity"(): integer
public "mark"(): $Buffer
public "reset"(): $Buffer
public "flip"(): $Buffer
public "rewind"(): $Buffer
public "hasRemaining"(): boolean
public "isReadOnly"(): boolean
public "slice"(arg0: integer, arg1: integer): $Buffer
public "slice"(): $Buffer
public "duplicate"(): $Buffer
get "direct"(): boolean
get "readOnly"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Buffer$Type = ($Buffer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Buffer_ = $Buffer$Type;
}}
declare module "packages/java/nio/file/attribute/$UserPrincipal" {
import {$Subject, $Subject$Type} from "packages/javax/security/auth/$Subject"
import {$Principal, $Principal$Type} from "packages/java/security/$Principal"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export interface $UserPrincipal extends $Principal {

 "getName"(): string
 "equals"(arg0: any): boolean
 "toString"(): string
 "hashCode"(): integer
 "implies"(arg0: $Subject$Type): boolean
}

export namespace $UserPrincipal {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $UserPrincipal$Type = ($UserPrincipal);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $UserPrincipal_ = $UserPrincipal$Type;
}}
declare module "packages/java/nio/$MappedByteBuffer" {
import {$ByteBuffer, $ByteBuffer$Type} from "packages/java/nio/$ByteBuffer"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $MappedByteBuffer extends $ByteBuffer {


public "load"(): $MappedByteBuffer
public "mark"(): $MappedByteBuffer
public "rewind"(): $MappedByteBuffer
public "duplicate"(): $MappedByteBuffer
public "compact"(): $MappedByteBuffer
public "isLoaded"(): boolean
public "force"(): $MappedByteBuffer
public "force"(arg0: integer, arg1: integer): $MappedByteBuffer
get "loaded"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MappedByteBuffer$Type = ($MappedByteBuffer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MappedByteBuffer_ = $MappedByteBuffer$Type;
}}
declare module "packages/java/nio/file/attribute/$UserPrincipalLookupService" {
import {$UserPrincipal, $UserPrincipal$Type} from "packages/java/nio/file/attribute/$UserPrincipal"
import {$GroupPrincipal, $GroupPrincipal$Type} from "packages/java/nio/file/attribute/$GroupPrincipal"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $UserPrincipalLookupService {


public "lookupPrincipalByGroupName"(arg0: string): $GroupPrincipal
public "lookupPrincipalByName"(arg0: string): $UserPrincipal
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $UserPrincipalLookupService$Type = ($UserPrincipalLookupService);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $UserPrincipalLookupService_ = $UserPrincipalLookupService$Type;
}}
declare module "packages/java/nio/file/$WatchService" {
import {$WatchKey, $WatchKey$Type} from "packages/java/nio/file/$WatchKey"
import {$Closeable, $Closeable$Type} from "packages/java/io/$Closeable"
import {$TimeUnit, $TimeUnit$Type} from "packages/java/util/concurrent/$TimeUnit"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export interface $WatchService extends $Closeable {

 "poll"(arg0: long, arg1: $TimeUnit$Type): $WatchKey
 "poll"(): $WatchKey
 "close"(): void
 "take"(): $WatchKey
}

export namespace $WatchService {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WatchService$Type = ($WatchService);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WatchService_ = $WatchService$Type;
}}
declare module "packages/java/nio/file/attribute/$FileAttributeView" {
import {$AttributeView, $AttributeView$Type} from "packages/java/nio/file/attribute/$AttributeView"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export interface $FileAttributeView extends $AttributeView {

 "name"(): string

(): string
}

export namespace $FileAttributeView {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FileAttributeView$Type = ($FileAttributeView);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FileAttributeView_ = $FileAttributeView$Type;
}}
declare module "packages/java/nio/file/$DirectoryStream$Filter" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export interface $DirectoryStream$Filter<T> {

 "accept"(arg0: T): boolean

(arg0: T): boolean
}

export namespace $DirectoryStream$Filter {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DirectoryStream$Filter$Type<T> = ($DirectoryStream$Filter<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DirectoryStream$Filter_<T> = $DirectoryStream$Filter$Type<(T)>;
}}
declare module "packages/java/nio/channels/spi/$AbstractSelector" {
import {$SelectorProvider, $SelectorProvider$Type} from "packages/java/nio/channels/spi/$SelectorProvider"
import {$Selector, $Selector$Type} from "packages/java/nio/channels/$Selector"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $AbstractSelector extends $Selector {


public "isOpen"(): boolean
public "provider"(): $SelectorProvider
public "close"(): void
get "open"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AbstractSelector$Type = ($AbstractSelector);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AbstractSelector_ = $AbstractSelector$Type;
}}
declare module "packages/java/nio/file/attribute/$AttributeView" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export interface $AttributeView {

 "name"(): string

(): string
}

export namespace $AttributeView {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AttributeView$Type = ($AttributeView);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AttributeView_ = $AttributeView$Type;
}}
declare module "packages/java/nio/charset/$Charset" {
import {$Comparable, $Comparable$Type} from "packages/java/lang/$Comparable"
import {$CharsetEncoder, $CharsetEncoder$Type} from "packages/java/nio/charset/$CharsetEncoder"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$SortedMap, $SortedMap$Type} from "packages/java/util/$SortedMap"
import {$CharsetDecoder, $CharsetDecoder$Type} from "packages/java/nio/charset/$CharsetDecoder"
import {$ByteBuffer, $ByteBuffer$Type} from "packages/java/nio/$ByteBuffer"
import {$CharBuffer, $CharBuffer$Type} from "packages/java/nio/$CharBuffer"
import {$Locale, $Locale$Type} from "packages/java/util/$Locale"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $Charset implements $Comparable<($Charset)> {


public "name"(): string
public static "forName"(arg0: string): $Charset
public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "compareTo"(arg0: $Charset$Type): integer
public "newDecoder"(): $CharsetDecoder
public "decode"(arg0: $ByteBuffer$Type): $CharBuffer
public "newEncoder"(): $CharsetEncoder
public "encode"(arg0: string): $ByteBuffer
public "encode"(arg0: $CharBuffer$Type): $ByteBuffer
public static "defaultCharset"(): $Charset
public "canEncode"(): boolean
public "contains"(arg0: $Charset$Type): boolean
public "isRegistered"(): boolean
public "aliases"(): $Set<(string)>
public static "isSupported"(arg0: string): boolean
public static "availableCharsets"(): $SortedMap<(string), ($Charset)>
public "displayName"(): string
public "displayName"(arg0: $Locale$Type): string
get "registered"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Charset$Type = ($Charset);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Charset_ = $Charset$Type;
}}
declare module "packages/java/nio/channels/$SelectionKey" {
import {$Selector, $Selector$Type} from "packages/java/nio/channels/$Selector"
import {$SelectableChannel, $SelectableChannel$Type} from "packages/java/nio/channels/$SelectableChannel"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $SelectionKey {
static readonly "OP_READ": integer
static readonly "OP_WRITE": integer
static readonly "OP_CONNECT": integer
static readonly "OP_ACCEPT": integer


public "attachment"(): any
public "attach"(arg0: any): any
public "channel"(): $SelectableChannel
public "isReadable"(): boolean
public "isWritable"(): boolean
public "isValid"(): boolean
public "cancel"(): void
public "selector"(): $Selector
public "interestOps"(): integer
public "interestOps"(arg0: integer): $SelectionKey
public "interestOpsOr"(arg0: integer): integer
public "interestOpsAnd"(arg0: integer): integer
public "isConnectable"(): boolean
public "isAcceptable"(): boolean
public "readyOps"(): integer
get "readable"(): boolean
get "writable"(): boolean
get "valid"(): boolean
get "connectable"(): boolean
get "acceptable"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SelectionKey$Type = ($SelectionKey);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SelectionKey_ = $SelectionKey$Type;
}}
declare module "packages/java/nio/channels/$Pipe" {
import {$Pipe$SourceChannel, $Pipe$SourceChannel$Type} from "packages/java/nio/channels/$Pipe$SourceChannel"
import {$Pipe$SinkChannel, $Pipe$SinkChannel$Type} from "packages/java/nio/channels/$Pipe$SinkChannel"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $Pipe {


public "source"(): $Pipe$SourceChannel
public static "open"(): $Pipe
public "sink"(): $Pipe$SinkChannel
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Pipe$Type = ($Pipe);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Pipe_ = $Pipe$Type;
}}
declare module "packages/java/nio/$CharBuffer" {
import {$Comparable, $Comparable$Type} from "packages/java/lang/$Comparable"
import {$IntStream, $IntStream$Type} from "packages/java/util/stream/$IntStream"
import {$Buffer, $Buffer$Type} from "packages/java/nio/$Buffer"
import {$Appendable, $Appendable$Type} from "packages/java/lang/$Appendable"
import {$ByteOrder, $ByteOrder$Type} from "packages/java/nio/$ByteOrder"
import {$Readable, $Readable$Type} from "packages/java/lang/$Readable"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $CharBuffer extends $Buffer implements $Comparable<($CharBuffer)>, $Appendable, charseq, $Readable {


public "get"(): character
public "get"(arg0: integer): character
public "get"(arg0: (character)[], arg1: integer, arg2: integer): $CharBuffer
public "get"(arg0: (character)[]): $CharBuffer
public "get"(arg0: integer, arg1: (character)[]): $CharBuffer
public "get"(arg0: integer, arg1: (character)[], arg2: integer, arg3: integer): $CharBuffer
public "put"(arg0: $CharBuffer$Type): $CharBuffer
public "put"(arg0: string, arg1: integer, arg2: integer): $CharBuffer
public "put"(arg0: integer, arg1: (character)[]): $CharBuffer
public "put"(arg0: integer, arg1: character): $CharBuffer
public "put"(arg0: integer, arg1: (character)[], arg2: integer, arg3: integer): $CharBuffer
public "put"(arg0: (character)[]): $CharBuffer
public "put"(arg0: (character)[], arg1: integer, arg2: integer): $CharBuffer
public "put"(arg0: integer, arg1: $CharBuffer$Type, arg2: integer, arg3: integer): $CharBuffer
public "put"(arg0: character): $CharBuffer
public "put"(arg0: string): $CharBuffer
public "equals"(arg0: any): boolean
public "length"(): integer
public "toString"(): string
public "append"(arg0: charseq, arg1: integer, arg2: integer): $CharBuffer
public "append"(arg0: character): $CharBuffer
public "hashCode"(): integer
public "compareTo"(arg0: $CharBuffer$Type): integer
public "clear"(): $CharBuffer
public static "wrap"(arg0: (character)[], arg1: integer, arg2: integer): $CharBuffer
public static "wrap"(arg0: (character)[]): $CharBuffer
public static "wrap"(arg0: charseq): $CharBuffer
public static "wrap"(arg0: charseq, arg1: integer, arg2: integer): $CharBuffer
public "charAt"(arg0: integer): character
public "isEmpty"(): boolean
public "limit"(arg0: integer): $CharBuffer
public "chars"(): $IntStream
public "isDirect"(): boolean
public "hasArray"(): boolean
public "array"(): (character)[]
public "arrayOffset"(): integer
public "read"(arg0: $CharBuffer$Type): integer
public "reset"(): $CharBuffer
public "flip"(): $CharBuffer
public "slice"(): $CharBuffer
public "slice"(arg0: integer, arg1: integer): $CharBuffer
public "duplicate"(): $CharBuffer
public "order"(): $ByteOrder
public "mismatch"(arg0: $CharBuffer$Type): integer
public static "allocate"(arg0: integer): $CharBuffer
public "asReadOnlyBuffer"(): $CharBuffer
public "compact"(): $CharBuffer
public static "compare"(arg0: charseq, arg1: charseq): integer
public "codePoints"(): $IntStream
get "empty"(): boolean
get "direct"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CharBuffer$Type = ($CharBuffer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CharBuffer_ = $CharBuffer$Type;
}}
declare module "packages/java/nio/channels/$SeekableByteChannel" {
import {$ByteChannel, $ByteChannel$Type} from "packages/java/nio/channels/$ByteChannel"
import {$ByteBuffer, $ByteBuffer$Type} from "packages/java/nio/$ByteBuffer"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export interface $SeekableByteChannel extends $ByteChannel {

 "position"(): long
 "position"(arg0: long): $SeekableByteChannel
 "size"(): long
 "write"(arg0: $ByteBuffer$Type): integer
 "read"(arg0: $ByteBuffer$Type): integer
 "truncate"(arg0: long): $SeekableByteChannel
 "isOpen"(): boolean
 "close"(): void
}

export namespace $SeekableByteChannel {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SeekableByteChannel$Type = ($SeekableByteChannel);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SeekableByteChannel_ = $SeekableByteChannel$Type;
}}
declare module "packages/java/nio/$ByteOrder" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $ByteOrder {
static readonly "BIG_ENDIAN": $ByteOrder
static readonly "LITTLE_ENDIAN": $ByteOrder


public "toString"(): string
public static "nativeOrder"(): $ByteOrder
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ByteOrder$Type = ($ByteOrder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ByteOrder_ = $ByteOrder$Type;
}}
declare module "packages/java/nio/file/$DirectoryStream" {
import {$Closeable, $Closeable$Type} from "packages/java/io/$Closeable"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$Iterator, $Iterator$Type} from "packages/java/util/$Iterator"
import {$Spliterator, $Spliterator$Type} from "packages/java/util/$Spliterator"
import {$Iterable, $Iterable$Type} from "packages/java/lang/$Iterable"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export interface $DirectoryStream<T> extends $Closeable, $Iterable<(T)> {

 "iterator"(): $Iterator<(T)>
 "close"(): void
 "spliterator"(): $Spliterator<(T)>
 "forEach"(arg0: $Consumer$Type<(any)>): void
}

export namespace $DirectoryStream {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DirectoryStream$Type<T> = ($DirectoryStream<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DirectoryStream_<T> = $DirectoryStream$Type<(T)>;
}}
declare module "packages/java/nio/file/$WatchKey" {
import {$List, $List$Type} from "packages/java/util/$List"
import {$WatchEvent, $WatchEvent$Type} from "packages/java/nio/file/$WatchEvent"
import {$Watchable, $Watchable$Type} from "packages/java/nio/file/$Watchable"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export interface $WatchKey {

 "reset"(): boolean
 "isValid"(): boolean
 "cancel"(): void
 "watchable"(): $Watchable
 "pollEvents"(): $List<($WatchEvent<(any)>)>
}

export namespace $WatchKey {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WatchKey$Type = ($WatchKey);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WatchKey_ = $WatchKey$Type;
}}
declare module "packages/java/nio/file/$Watchable" {
import {$WatchKey, $WatchKey$Type} from "packages/java/nio/file/$WatchKey"
import {$WatchEvent$Kind, $WatchEvent$Kind$Type} from "packages/java/nio/file/$WatchEvent$Kind"
import {$WatchService, $WatchService$Type} from "packages/java/nio/file/$WatchService"
import {$WatchEvent$Modifier, $WatchEvent$Modifier$Type} from "packages/java/nio/file/$WatchEvent$Modifier"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export interface $Watchable {

 "register"(arg0: $WatchService$Type, arg1: ($WatchEvent$Kind$Type<(any)>)[], ...arg2: ($WatchEvent$Modifier$Type)[]): $WatchKey
 "register"(arg0: $WatchService$Type, ...arg1: ($WatchEvent$Kind$Type<(any)>)[]): $WatchKey
}

export namespace $Watchable {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Watchable$Type = ($Watchable);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Watchable_ = $Watchable$Type;
}}
declare module "packages/java/nio/channels/$InterruptibleChannel" {
import {$Channel, $Channel$Type} from "packages/java/nio/channels/$Channel"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export interface $InterruptibleChannel extends $Channel {

 "close"(): void
 "isOpen"(): boolean
}

export namespace $InterruptibleChannel {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InterruptibleChannel$Type = ($InterruptibleChannel);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InterruptibleChannel_ = $InterruptibleChannel$Type;
}}
declare module "packages/java/nio/file/$Path" {
import {$Comparable, $Comparable$Type} from "packages/java/lang/$Comparable"
import {$FileSystem, $FileSystem$Type} from "packages/java/nio/file/$FileSystem"
import {$WatchEvent$Kind, $WatchEvent$Kind$Type} from "packages/java/nio/file/$WatchEvent$Kind"
import {$WatchService, $WatchService$Type} from "packages/java/nio/file/$WatchService"
import {$WatchEvent$Modifier, $WatchEvent$Modifier$Type} from "packages/java/nio/file/$WatchEvent$Modifier"
import {$Spliterator, $Spliterator$Type} from "packages/java/util/$Spliterator"
import {$Iterable, $Iterable$Type} from "packages/java/lang/$Iterable"
import {$Watchable, $Watchable$Type} from "packages/java/nio/file/$Watchable"
import {$WatchKey, $WatchKey$Type} from "packages/java/nio/file/$WatchKey"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$File, $File$Type} from "packages/java/io/$File"
import {$URI, $URI$Type} from "packages/java/net/$URI"
import {$Iterator, $Iterator$Type} from "packages/java/util/$Iterator"
import {$LinkOption, $LinkOption$Type} from "packages/java/nio/file/$LinkOption"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export interface $Path extends $Comparable<($Path)>, $Iterable<($Path)>, $Watchable {

 "getName"(arg0: integer): $Path
 "equals"(arg0: any): boolean
 "toString"(): string
 "hashCode"(): integer
 "compareTo"(arg0: $Path$Type): integer
 "startsWith"(arg0: string): boolean
 "startsWith"(arg0: $Path$Type): boolean
 "iterator"(): $Iterator<($Path)>
 "endsWith"(arg0: string): boolean
 "endsWith"(arg0: $Path$Type): boolean
 "register"(arg0: $WatchService$Type, ...arg1: ($WatchEvent$Kind$Type<(any)>)[]): $WatchKey
 "register"(arg0: $WatchService$Type, arg1: ($WatchEvent$Kind$Type<(any)>)[], ...arg2: ($WatchEvent$Modifier$Type)[]): $WatchKey
 "isAbsolute"(): boolean
 "resolve"(arg0: $Path$Type): $Path
 "resolve"(arg0: string): $Path
 "getParent"(): $Path
 "getRoot"(): $Path
 "normalize"(): $Path
 "toRealPath"(...arg0: ($LinkOption$Type)[]): $Path
 "toFile"(): $File
 "getFileName"(): $Path
 "getFileSystem"(): $FileSystem
 "relativize"(arg0: $Path$Type): $Path
 "getNameCount"(): integer
 "toAbsolutePath"(): $Path
 "resolveSibling"(arg0: $Path$Type): $Path
 "resolveSibling"(arg0: string): $Path
 "subpath"(arg0: integer, arg1: integer): $Path
 "toUri"(): $URI
 "spliterator"(): $Spliterator<($Path)>
 "forEach"(arg0: $Consumer$Type<(any)>): void
}

export namespace $Path {
function of(arg0: string, ...arg1: (string)[]): $Path
function of(arg0: $URI$Type): $Path
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Path$Type = (string) | ($Path);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Path_ = $Path$Type;
}}
declare module "packages/java/nio/file/attribute/$BasicFileAttributes" {
import {$FileTime, $FileTime$Type} from "packages/java/nio/file/attribute/$FileTime"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export interface $BasicFileAttributes {

 "size"(): long
 "isDirectory"(): boolean
 "isRegularFile"(): boolean
 "isSymbolicLink"(): boolean
 "lastModifiedTime"(): $FileTime
 "lastAccessTime"(): $FileTime
 "creationTime"(): $FileTime
 "isOther"(): boolean
 "fileKey"(): any
}

export namespace $BasicFileAttributes {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BasicFileAttributes$Type = ($BasicFileAttributes);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BasicFileAttributes_ = $BasicFileAttributes$Type;
}}
declare module "packages/java/nio/file/attribute/$BasicFileAttributeView" {
import {$FileTime, $FileTime$Type} from "packages/java/nio/file/attribute/$FileTime"
import {$FileAttributeView, $FileAttributeView$Type} from "packages/java/nio/file/attribute/$FileAttributeView"
import {$BasicFileAttributes, $BasicFileAttributes$Type} from "packages/java/nio/file/attribute/$BasicFileAttributes"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export interface $BasicFileAttributeView extends $FileAttributeView {

 "name"(): string
 "readAttributes"(): $BasicFileAttributes
 "setTimes"(arg0: $FileTime$Type, arg1: $FileTime$Type, arg2: $FileTime$Type): void
}

export namespace $BasicFileAttributeView {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BasicFileAttributeView$Type = ($BasicFileAttributeView);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BasicFileAttributeView_ = $BasicFileAttributeView$Type;
}}
declare module "packages/java/nio/$DoubleBuffer" {
import {$Comparable, $Comparable$Type} from "packages/java/lang/$Comparable"
import {$Buffer, $Buffer$Type} from "packages/java/nio/$Buffer"
import {$ByteOrder, $ByteOrder$Type} from "packages/java/nio/$ByteOrder"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $DoubleBuffer extends $Buffer implements $Comparable<($DoubleBuffer)> {


public "get"(): double
public "get"(arg0: integer, arg1: (double)[]): $DoubleBuffer
public "get"(arg0: integer): double
public "get"(arg0: integer, arg1: (double)[], arg2: integer, arg3: integer): $DoubleBuffer
public "get"(arg0: (double)[], arg1: integer, arg2: integer): $DoubleBuffer
public "get"(arg0: (double)[]): $DoubleBuffer
public "put"(arg0: (double)[]): $DoubleBuffer
public "put"(arg0: integer, arg1: (double)[], arg2: integer, arg3: integer): $DoubleBuffer
public "put"(arg0: integer, arg1: (double)[]): $DoubleBuffer
public "put"(arg0: (double)[], arg1: integer, arg2: integer): $DoubleBuffer
public "put"(arg0: integer, arg1: $DoubleBuffer$Type, arg2: integer, arg3: integer): $DoubleBuffer
public "put"(arg0: $DoubleBuffer$Type): $DoubleBuffer
public "put"(arg0: integer, arg1: double): $DoubleBuffer
public "put"(arg0: double): $DoubleBuffer
public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "compareTo"(arg0: $DoubleBuffer$Type): integer
public static "wrap"(arg0: (double)[]): $DoubleBuffer
public static "wrap"(arg0: (double)[], arg1: integer, arg2: integer): $DoubleBuffer
public "position"(arg0: integer): $DoubleBuffer
public "limit"(arg0: integer): $DoubleBuffer
public "isDirect"(): boolean
public "hasArray"(): boolean
public "array"(): (double)[]
public "arrayOffset"(): integer
public "duplicate"(): $DoubleBuffer
public "order"(): $ByteOrder
public "mismatch"(arg0: $DoubleBuffer$Type): integer
public static "allocate"(arg0: integer): $DoubleBuffer
public "asReadOnlyBuffer"(): $DoubleBuffer
public "compact"(): $DoubleBuffer
get "direct"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DoubleBuffer$Type = ($DoubleBuffer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DoubleBuffer_ = $DoubleBuffer$Type;
}}
declare module "packages/java/nio/file/$FileSystem" {
import {$FileStore, $FileStore$Type} from "packages/java/nio/file/$FileStore"
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$Closeable, $Closeable$Type} from "packages/java/io/$Closeable"
import {$FileSystemProvider, $FileSystemProvider$Type} from "packages/java/nio/file/spi/$FileSystemProvider"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$WatchService, $WatchService$Type} from "packages/java/nio/file/$WatchService"
import {$Iterable, $Iterable$Type} from "packages/java/lang/$Iterable"
import {$PathMatcher, $PathMatcher$Type} from "packages/java/nio/file/$PathMatcher"
import {$UserPrincipalLookupService, $UserPrincipalLookupService$Type} from "packages/java/nio/file/attribute/$UserPrincipalLookupService"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $FileSystem implements $Closeable {


public "isOpen"(): boolean
public "provider"(): $FileSystemProvider
public "close"(): void
public "getPath"(arg0: string, ...arg1: (string)[]): $Path
public "isReadOnly"(): boolean
public "getSeparator"(): string
public "getRootDirectories"(): $Iterable<($Path)>
public "getFileStores"(): $Iterable<($FileStore)>
public "supportedFileAttributeViews"(): $Set<(string)>
public "getPathMatcher"(arg0: string): $PathMatcher
public "getUserPrincipalLookupService"(): $UserPrincipalLookupService
public "newWatchService"(): $WatchService
get "open"(): boolean
get "readOnly"(): boolean
get "separator"(): string
get "rootDirectories"(): $Iterable<($Path)>
get "fileStores"(): $Iterable<($FileStore)>
get "userPrincipalLookupService"(): $UserPrincipalLookupService
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FileSystem$Type = ($FileSystem);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FileSystem_ = $FileSystem$Type;
}}
declare module "packages/java/nio/channels/spi/$SelectorProvider" {
import {$DatagramChannel, $DatagramChannel$Type} from "packages/java/nio/channels/$DatagramChannel"
import {$Pipe, $Pipe$Type} from "packages/java/nio/channels/$Pipe"
import {$Channel, $Channel$Type} from "packages/java/nio/channels/$Channel"
import {$ServerSocketChannel, $ServerSocketChannel$Type} from "packages/java/nio/channels/$ServerSocketChannel"
import {$ProtocolFamily, $ProtocolFamily$Type} from "packages/java/net/$ProtocolFamily"
import {$SocketChannel, $SocketChannel$Type} from "packages/java/nio/channels/$SocketChannel"
import {$AbstractSelector, $AbstractSelector$Type} from "packages/java/nio/channels/spi/$AbstractSelector"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $SelectorProvider {


public static "provider"(): $SelectorProvider
public "inheritedChannel"(): $Channel
public "openSelector"(): $AbstractSelector
public "openDatagramChannel"(arg0: $ProtocolFamily$Type): $DatagramChannel
public "openDatagramChannel"(): $DatagramChannel
public "openPipe"(): $Pipe
public "openServerSocketChannel"(arg0: $ProtocolFamily$Type): $ServerSocketChannel
public "openServerSocketChannel"(): $ServerSocketChannel
public "openSocketChannel"(arg0: $ProtocolFamily$Type): $SocketChannel
public "openSocketChannel"(): $SocketChannel
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SelectorProvider$Type = ($SelectorProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SelectorProvider_ = $SelectorProvider$Type;
}}
declare module "packages/java/nio/charset/$CharsetDecoder" {
import {$CodingErrorAction, $CodingErrorAction$Type} from "packages/java/nio/charset/$CodingErrorAction"
import {$ByteBuffer, $ByteBuffer$Type} from "packages/java/nio/$ByteBuffer"
import {$CharBuffer, $CharBuffer$Type} from "packages/java/nio/$CharBuffer"
import {$Charset, $Charset$Type} from "packages/java/nio/charset/$Charset"
import {$CoderResult, $CoderResult$Type} from "packages/java/nio/charset/$CoderResult"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $CharsetDecoder {


public "maxCharsPerByte"(): float
public "onMalformedInput"(arg0: $CodingErrorAction$Type): $CharsetDecoder
public "onUnmappableCharacter"(arg0: $CodingErrorAction$Type): $CharsetDecoder
public "decode"(arg0: $ByteBuffer$Type): $CharBuffer
public "decode"(arg0: $ByteBuffer$Type, arg1: $CharBuffer$Type, arg2: boolean): $CoderResult
public "flush"(arg0: $CharBuffer$Type): $CoderResult
public "charset"(): $Charset
public "replacement"(): string
public "reset"(): $CharsetDecoder
public "malformedInputAction"(): $CodingErrorAction
public "unmappableCharacterAction"(): $CodingErrorAction
public "replaceWith"(arg0: string): $CharsetDecoder
public "averageCharsPerByte"(): float
public "isAutoDetecting"(): boolean
public "isCharsetDetected"(): boolean
public "detectedCharset"(): $Charset
get "autoDetecting"(): boolean
get "charsetDetected"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CharsetDecoder$Type = ($CharsetDecoder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CharsetDecoder_ = $CharsetDecoder$Type;
}}
declare module "packages/java/nio/channels/$Selector" {
import {$SelectorProvider, $SelectorProvider$Type} from "packages/java/nio/channels/spi/$SelectorProvider"
import {$Closeable, $Closeable$Type} from "packages/java/io/$Closeable"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$SelectionKey, $SelectionKey$Type} from "packages/java/nio/channels/$SelectionKey"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $Selector implements $Closeable {


public "isOpen"(): boolean
public "provider"(): $SelectorProvider
public "close"(): void
public "keys"(): $Set<($SelectionKey)>
public static "open"(): $Selector
public "wakeup"(): $Selector
public "select"(): integer
public "select"(arg0: $Consumer$Type<($SelectionKey$Type)>, arg1: long): integer
public "select"(arg0: $Consumer$Type<($SelectionKey$Type)>): integer
public "select"(arg0: long): integer
public "selectedKeys"(): $Set<($SelectionKey)>
public "selectNow"(): integer
public "selectNow"(arg0: $Consumer$Type<($SelectionKey$Type)>): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Selector$Type = ($Selector);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Selector_ = $Selector$Type;
}}
declare module "packages/java/nio/channels/$Pipe$SourceChannel" {
import {$ReadableByteChannel, $ReadableByteChannel$Type} from "packages/java/nio/channels/$ReadableByteChannel"
import {$ByteBuffer, $ByteBuffer$Type} from "packages/java/nio/$ByteBuffer"
import {$ScatteringByteChannel, $ScatteringByteChannel$Type} from "packages/java/nio/channels/$ScatteringByteChannel"
import {$AbstractSelectableChannel, $AbstractSelectableChannel$Type} from "packages/java/nio/channels/spi/$AbstractSelectableChannel"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $Pipe$SourceChannel extends $AbstractSelectableChannel implements $ReadableByteChannel, $ScatteringByteChannel {


public "validOps"(): integer
public "read"(arg0: $ByteBuffer$Type): integer
public "read"(arg0: ($ByteBuffer$Type)[], arg1: integer, arg2: integer): long
public "read"(arg0: ($ByteBuffer$Type)[]): long
public "isOpen"(): boolean
public "close"(): void
get "open"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Pipe$SourceChannel$Type = ($Pipe$SourceChannel);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Pipe$SourceChannel_ = $Pipe$SourceChannel$Type;
}}
declare module "packages/java/nio/file/attribute/$FileTime" {
import {$Comparable, $Comparable$Type} from "packages/java/lang/$Comparable"
import {$Instant, $Instant$Type} from "packages/java/time/$Instant"
import {$TimeUnit, $TimeUnit$Type} from "packages/java/util/concurrent/$TimeUnit"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $FileTime implements $Comparable<($FileTime)> {


public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "compareTo"(arg0: $FileTime$Type): integer
public "to"(arg0: $TimeUnit$Type): long
public static "from"(arg0: long, arg1: $TimeUnit$Type): $FileTime
public static "from"(arg0: $Instant$Type): $FileTime
public "toMillis"(): long
public "toInstant"(): $Instant
public static "fromMillis"(arg0: long): $FileTime
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FileTime$Type = ($FileTime);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FileTime_ = $FileTime$Type;
}}
declare module "packages/java/nio/$IntBuffer" {
import {$Comparable, $Comparable$Type} from "packages/java/lang/$Comparable"
import {$Buffer, $Buffer$Type} from "packages/java/nio/$Buffer"
import {$ByteOrder, $ByteOrder$Type} from "packages/java/nio/$ByteOrder"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $IntBuffer extends $Buffer implements $Comparable<($IntBuffer)> {


public "get"(): integer
public "get"(arg0: integer, arg1: (integer)[]): $IntBuffer
public "get"(arg0: integer): integer
public "get"(arg0: integer, arg1: (integer)[], arg2: integer, arg3: integer): $IntBuffer
public "get"(arg0: (integer)[], arg1: integer, arg2: integer): $IntBuffer
public "get"(arg0: (integer)[]): $IntBuffer
public "put"(arg0: (integer)[]): $IntBuffer
public "put"(arg0: integer, arg1: (integer)[], arg2: integer, arg3: integer): $IntBuffer
public "put"(arg0: integer, arg1: (integer)[]): $IntBuffer
public "put"(arg0: (integer)[], arg1: integer, arg2: integer): $IntBuffer
public "put"(arg0: integer, arg1: $IntBuffer$Type, arg2: integer, arg3: integer): $IntBuffer
public "put"(arg0: $IntBuffer$Type): $IntBuffer
public "put"(arg0: integer, arg1: integer): $IntBuffer
public "put"(arg0: integer): $IntBuffer
public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "compareTo"(arg0: $IntBuffer$Type): integer
public static "wrap"(arg0: (integer)[]): $IntBuffer
public static "wrap"(arg0: (integer)[], arg1: integer, arg2: integer): $IntBuffer
public "position"(arg0: integer): $IntBuffer
public "limit"(arg0: integer): $IntBuffer
public "isDirect"(): boolean
public "hasArray"(): boolean
public "array"(): (integer)[]
public "arrayOffset"(): integer
public "duplicate"(): $IntBuffer
public "order"(): $ByteOrder
public "mismatch"(arg0: $IntBuffer$Type): integer
public static "allocate"(arg0: integer): $IntBuffer
public "asReadOnlyBuffer"(): $IntBuffer
public "compact"(): $IntBuffer
get "direct"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IntBuffer$Type = ($IntBuffer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IntBuffer_ = $IntBuffer$Type;
}}
declare module "packages/java/nio/$ByteBuffer" {
import {$FloatBuffer, $FloatBuffer$Type} from "packages/java/nio/$FloatBuffer"
import {$DoubleBuffer, $DoubleBuffer$Type} from "packages/java/nio/$DoubleBuffer"
import {$Comparable, $Comparable$Type} from "packages/java/lang/$Comparable"
import {$ShortBuffer, $ShortBuffer$Type} from "packages/java/nio/$ShortBuffer"
import {$Buffer, $Buffer$Type} from "packages/java/nio/$Buffer"
import {$IntBuffer, $IntBuffer$Type} from "packages/java/nio/$IntBuffer"
import {$LongBuffer, $LongBuffer$Type} from "packages/java/nio/$LongBuffer"
import {$ByteOrder, $ByteOrder$Type} from "packages/java/nio/$ByteOrder"
import {$CharBuffer, $CharBuffer$Type} from "packages/java/nio/$CharBuffer"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $ByteBuffer extends $Buffer implements $Comparable<($ByteBuffer)> {


public "get"(arg0: integer, arg1: (byte)[], arg2: integer, arg3: integer): $ByteBuffer
public "get"(arg0: integer): byte
public "get"(arg0: (byte)[], arg1: integer, arg2: integer): $ByteBuffer
public "get"(): byte
public "get"(arg0: integer, arg1: (byte)[]): $ByteBuffer
public "get"(arg0: (byte)[]): $ByteBuffer
public "put"(arg0: $ByteBuffer$Type): $ByteBuffer
public "put"(arg0: integer, arg1: byte): $ByteBuffer
public "put"(arg0: integer, arg1: $ByteBuffer$Type, arg2: integer, arg3: integer): $ByteBuffer
public "put"(arg0: (byte)[]): $ByteBuffer
public "put"(arg0: integer, arg1: (byte)[]): $ByteBuffer
public "put"(arg0: integer, arg1: (byte)[], arg2: integer, arg3: integer): $ByteBuffer
public "put"(arg0: byte): $ByteBuffer
public "put"(arg0: (byte)[], arg1: integer, arg2: integer): $ByteBuffer
public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "compareTo"(arg0: $ByteBuffer$Type): integer
public "getShort"(arg0: integer): short
public "getShort"(): short
public "putShort"(arg0: short): $ByteBuffer
public "putShort"(arg0: integer, arg1: short): $ByteBuffer
public "getChar"(): character
public "getChar"(arg0: integer): character
public "putChar"(arg0: integer, arg1: character): $ByteBuffer
public "putChar"(arg0: character): $ByteBuffer
public "getInt"(): integer
public "getInt"(arg0: integer): integer
public "putInt"(arg0: integer, arg1: integer): $ByteBuffer
public "putInt"(arg0: integer): $ByteBuffer
public "getLong"(arg0: integer): long
public "getLong"(): long
public "putLong"(arg0: integer, arg1: long): $ByteBuffer
public "putLong"(arg0: long): $ByteBuffer
public "getFloat"(arg0: integer): float
public "getFloat"(): float
public "putFloat"(arg0: float): $ByteBuffer
public "putFloat"(arg0: integer, arg1: float): $ByteBuffer
public "getDouble"(): double
public "getDouble"(arg0: integer): double
public "putDouble"(arg0: integer, arg1: double): $ByteBuffer
public "putDouble"(arg0: double): $ByteBuffer
public "clear"(): $ByteBuffer
public static "wrap"(arg0: (byte)[]): $ByteBuffer
public static "wrap"(arg0: (byte)[], arg1: integer, arg2: integer): $ByteBuffer
public "position"(arg0: integer): $ByteBuffer
public "isDirect"(): boolean
public "hasArray"(): boolean
public "array"(): (byte)[]
public "arrayOffset"(): integer
public "reset"(): $ByteBuffer
public "slice"(): $ByteBuffer
public "slice"(arg0: integer, arg1: integer): $ByteBuffer
public "duplicate"(): $ByteBuffer
public "order"(): $ByteOrder
public "order"(arg0: $ByteOrder$Type): $ByteBuffer
public "mismatch"(arg0: $ByteBuffer$Type): integer
public static "allocate"(arg0: integer): $ByteBuffer
public "alignmentOffset"(arg0: integer, arg1: integer): integer
public static "allocateDirect"(arg0: integer): $ByteBuffer
public "asReadOnlyBuffer"(): $ByteBuffer
public "compact"(): $ByteBuffer
public "alignedSlice"(arg0: integer): $ByteBuffer
public "asCharBuffer"(): $CharBuffer
public "asShortBuffer"(): $ShortBuffer
public "asIntBuffer"(): $IntBuffer
public "asLongBuffer"(): $LongBuffer
public "asFloatBuffer"(): $FloatBuffer
public "asDoubleBuffer"(): $DoubleBuffer
get "short"(): short
get "char"(): character
get "int"(): integer
get "long"(): long
get "float"(): float
get "double"(): double
get "direct"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ByteBuffer$Type = ($ByteBuffer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ByteBuffer_ = $ByteBuffer$Type;
}}
declare module "packages/java/nio/channels/$FileChannel" {
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$OpenOption, $OpenOption$Type} from "packages/java/nio/file/$OpenOption"
import {$AbstractInterruptibleChannel, $AbstractInterruptibleChannel$Type} from "packages/java/nio/channels/spi/$AbstractInterruptibleChannel"
import {$SeekableByteChannel, $SeekableByteChannel$Type} from "packages/java/nio/channels/$SeekableByteChannel"
import {$ByteBuffer, $ByteBuffer$Type} from "packages/java/nio/$ByteBuffer"
import {$ScatteringByteChannel, $ScatteringByteChannel$Type} from "packages/java/nio/channels/$ScatteringByteChannel"
import {$WritableByteChannel, $WritableByteChannel$Type} from "packages/java/nio/channels/$WritableByteChannel"
import {$FileChannel$MapMode, $FileChannel$MapMode$Type} from "packages/java/nio/channels/$FileChannel$MapMode"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$ReadableByteChannel, $ReadableByteChannel$Type} from "packages/java/nio/channels/$ReadableByteChannel"
import {$GatheringByteChannel, $GatheringByteChannel$Type} from "packages/java/nio/channels/$GatheringByteChannel"
import {$MappedByteBuffer, $MappedByteBuffer$Type} from "packages/java/nio/$MappedByteBuffer"
import {$FileLock, $FileLock$Type} from "packages/java/nio/channels/$FileLock"
import {$FileAttribute, $FileAttribute$Type} from "packages/java/nio/file/attribute/$FileAttribute"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $FileChannel extends $AbstractInterruptibleChannel implements $SeekableByteChannel, $GatheringByteChannel, $ScatteringByteChannel {


public "lock"(): $FileLock
public "lock"(arg0: long, arg1: long, arg2: boolean): $FileLock
public "position"(): long
public "size"(): long
public "map"(arg0: $FileChannel$MapMode$Type, arg1: long, arg2: long): $MappedByteBuffer
public "write"(arg0: $ByteBuffer$Type, arg1: long): integer
public "write"(arg0: $ByteBuffer$Type): integer
public "write"(arg0: ($ByteBuffer$Type)[], arg1: integer, arg2: integer): long
public "write"(arg0: ($ByteBuffer$Type)[]): long
public "read"(arg0: $ByteBuffer$Type): integer
public "read"(arg0: $ByteBuffer$Type, arg1: long): integer
public "read"(arg0: ($ByteBuffer$Type)[]): long
public "read"(arg0: ($ByteBuffer$Type)[], arg1: integer, arg2: integer): long
public static "open"(arg0: $Path$Type, arg1: $Set$Type<(any)>, ...arg2: ($FileAttribute$Type<(any)>)[]): $FileChannel
public static "open"(arg0: $Path$Type, ...arg1: ($OpenOption$Type)[]): $FileChannel
public "transferTo"(arg0: long, arg1: long, arg2: $WritableByteChannel$Type): long
public "tryLock"(arg0: long, arg1: long, arg2: boolean): $FileLock
public "tryLock"(): $FileLock
public "force"(arg0: boolean): void
public "transferFrom"(arg0: $ReadableByteChannel$Type, arg1: long, arg2: long): long
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FileChannel$Type = ($FileChannel);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FileChannel_ = $FileChannel$Type;
}}
declare module "packages/java/nio/charset/$CharsetEncoder" {
import {$CodingErrorAction, $CodingErrorAction$Type} from "packages/java/nio/charset/$CodingErrorAction"
import {$ByteBuffer, $ByteBuffer$Type} from "packages/java/nio/$ByteBuffer"
import {$CharBuffer, $CharBuffer$Type} from "packages/java/nio/$CharBuffer"
import {$Charset, $Charset$Type} from "packages/java/nio/charset/$Charset"
import {$CoderResult, $CoderResult$Type} from "packages/java/nio/charset/$CoderResult"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $CharsetEncoder {


public "onMalformedInput"(arg0: $CodingErrorAction$Type): $CharsetEncoder
public "onUnmappableCharacter"(arg0: $CodingErrorAction$Type): $CharsetEncoder
public "maxBytesPerChar"(): float
public "encode"(arg0: $CharBuffer$Type, arg1: $ByteBuffer$Type, arg2: boolean): $CoderResult
public "encode"(arg0: $CharBuffer$Type): $ByteBuffer
public "flush"(arg0: $ByteBuffer$Type): $CoderResult
public "canEncode"(arg0: charseq): boolean
public "canEncode"(arg0: character): boolean
public "charset"(): $Charset
public "replacement"(): (byte)[]
public "reset"(): $CharsetEncoder
public "isLegalReplacement"(arg0: (byte)[]): boolean
public "malformedInputAction"(): $CodingErrorAction
public "unmappableCharacterAction"(): $CodingErrorAction
public "averageBytesPerChar"(): float
public "replaceWith"(arg0: (byte)[]): $CharsetEncoder
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CharsetEncoder$Type = ($CharsetEncoder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CharsetEncoder_ = $CharsetEncoder$Type;
}}
declare module "packages/java/nio/channels/$FileChannel$MapMode" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $FileChannel$MapMode {
static readonly "READ_ONLY": $FileChannel$MapMode
static readonly "READ_WRITE": $FileChannel$MapMode
static readonly "PRIVATE": $FileChannel$MapMode


public "toString"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FileChannel$MapMode$Type = ($FileChannel$MapMode);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FileChannel$MapMode_ = $FileChannel$MapMode$Type;
}}
declare module "packages/java/nio/channels/$ServerSocketChannel" {
import {$SocketAddress, $SocketAddress$Type} from "packages/java/net/$SocketAddress"
import {$ServerSocket, $ServerSocket$Type} from "packages/java/net/$ServerSocket"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$NetworkChannel, $NetworkChannel$Type} from "packages/java/nio/channels/$NetworkChannel"
import {$ProtocolFamily, $ProtocolFamily$Type} from "packages/java/net/$ProtocolFamily"
import {$SocketChannel, $SocketChannel$Type} from "packages/java/nio/channels/$SocketChannel"
import {$SocketOption, $SocketOption$Type} from "packages/java/net/$SocketOption"
import {$AbstractSelectableChannel, $AbstractSelectableChannel$Type} from "packages/java/nio/channels/spi/$AbstractSelectableChannel"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $ServerSocketChannel extends $AbstractSelectableChannel implements $NetworkChannel {


public "accept"(): $SocketChannel
public static "open"(arg0: $ProtocolFamily$Type): $ServerSocketChannel
public static "open"(): $ServerSocketChannel
public "bind"(arg0: $SocketAddress$Type, arg1: integer): $ServerSocketChannel
public "validOps"(): integer
public "getLocalAddress"(): $SocketAddress
public "socket"(): $ServerSocket
public "supportedOptions"(): $Set<($SocketOption<(any)>)>
public "getOption"<T>(arg0: $SocketOption$Type<(T)>): T
public "isOpen"(): boolean
public "close"(): void
get "localAddress"(): $SocketAddress
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ServerSocketChannel$Type = ($ServerSocketChannel);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ServerSocketChannel_ = $ServerSocketChannel$Type;
}}
declare module "packages/java/nio/file/spi/$FileSystemProvider" {
import {$ExecutorService, $ExecutorService$Type} from "packages/java/util/concurrent/$ExecutorService"
import {$FileSystem, $FileSystem$Type} from "packages/java/nio/file/$FileSystem"
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$OpenOption, $OpenOption$Type} from "packages/java/nio/file/$OpenOption"
import {$OutputStream, $OutputStream$Type} from "packages/java/io/$OutputStream"
import {$AccessMode, $AccessMode$Type} from "packages/java/nio/file/$AccessMode"
import {$AsynchronousFileChannel, $AsynchronousFileChannel$Type} from "packages/java/nio/channels/$AsynchronousFileChannel"
import {$SeekableByteChannel, $SeekableByteChannel$Type} from "packages/java/nio/channels/$SeekableByteChannel"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$DirectoryStream, $DirectoryStream$Type} from "packages/java/nio/file/$DirectoryStream"
import {$FileAttributeView, $FileAttributeView$Type} from "packages/java/nio/file/attribute/$FileAttributeView"
import {$BasicFileAttributes, $BasicFileAttributes$Type} from "packages/java/nio/file/attribute/$BasicFileAttributes"
import {$CopyOption, $CopyOption$Type} from "packages/java/nio/file/$CopyOption"
import {$DirectoryStream$Filter, $DirectoryStream$Filter$Type} from "packages/java/nio/file/$DirectoryStream$Filter"
import {$FileStore, $FileStore$Type} from "packages/java/nio/file/$FileStore"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$FileChannel, $FileChannel$Type} from "packages/java/nio/channels/$FileChannel"
import {$List, $List$Type} from "packages/java/util/$List"
import {$InputStream, $InputStream$Type} from "packages/java/io/$InputStream"
import {$URI, $URI$Type} from "packages/java/net/$URI"
import {$FileAttribute, $FileAttribute$Type} from "packages/java/nio/file/attribute/$FileAttribute"
import {$Map, $Map$Type} from "packages/java/util/$Map"
import {$LinkOption, $LinkOption$Type} from "packages/java/nio/file/$LinkOption"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $FileSystemProvider {


public "isHidden"(arg0: $Path$Type): boolean
public "delete"(arg0: $Path$Type): void
public "checkAccess"(arg0: $Path$Type, ...arg1: ($AccessMode$Type)[]): void
public "copy"(arg0: $Path$Type, arg1: $Path$Type, ...arg2: ($CopyOption$Type)[]): void
public "getScheme"(): string
public "getPath"(arg0: $URI$Type): $Path
public "createDirectory"(arg0: $Path$Type, ...arg1: ($FileAttribute$Type<(any)>)[]): void
public "getFileSystem"(arg0: $URI$Type): $FileSystem
public static "installedProviders"(): $List<($FileSystemProvider)>
public "newFileSystem"(arg0: $URI$Type, arg1: $Map$Type<(string), (any)>): $FileSystem
public "newFileSystem"(arg0: $Path$Type, arg1: $Map$Type<(string), (any)>): $FileSystem
public "newInputStream"(arg0: $Path$Type, ...arg1: ($OpenOption$Type)[]): $InputStream
public "newOutputStream"(arg0: $Path$Type, ...arg1: ($OpenOption$Type)[]): $OutputStream
public "newByteChannel"(arg0: $Path$Type, arg1: $Set$Type<(any)>, ...arg2: ($FileAttribute$Type<(any)>)[]): $SeekableByteChannel
public "newDirectoryStream"(arg0: $Path$Type, arg1: $DirectoryStream$Filter$Type<(any)>): $DirectoryStream<($Path)>
public "createSymbolicLink"(arg0: $Path$Type, arg1: $Path$Type, ...arg2: ($FileAttribute$Type<(any)>)[]): void
public "createLink"(arg0: $Path$Type, arg1: $Path$Type): void
public "deleteIfExists"(arg0: $Path$Type): boolean
public "move"(arg0: $Path$Type, arg1: $Path$Type, ...arg2: ($CopyOption$Type)[]): void
public "readSymbolicLink"(arg0: $Path$Type): $Path
public "getFileStore"(arg0: $Path$Type): $FileStore
public "isSameFile"(arg0: $Path$Type, arg1: $Path$Type): boolean
public "getFileAttributeView"<V extends $FileAttributeView>(arg0: $Path$Type, arg1: $Class$Type<(V)>, ...arg2: ($LinkOption$Type)[]): V
public "readAttributes"<A extends $BasicFileAttributes>(arg0: $Path$Type, arg1: $Class$Type<(A)>, ...arg2: ($LinkOption$Type)[]): A
public "readAttributes"(arg0: $Path$Type, arg1: string, ...arg2: ($LinkOption$Type)[]): $Map<(string), (any)>
public "setAttribute"(arg0: $Path$Type, arg1: string, arg2: any, ...arg3: ($LinkOption$Type)[]): void
public "newFileChannel"(arg0: $Path$Type, arg1: $Set$Type<(any)>, ...arg2: ($FileAttribute$Type<(any)>)[]): $FileChannel
public "newAsynchronousFileChannel"(arg0: $Path$Type, arg1: $Set$Type<(any)>, arg2: $ExecutorService$Type, ...arg3: ($FileAttribute$Type<(any)>)[]): $AsynchronousFileChannel
get "scheme"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FileSystemProvider$Type = ($FileSystemProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FileSystemProvider_ = $FileSystemProvider$Type;
}}
declare module "packages/java/nio/file/$LinkOption" {
import {$CopyOption, $CopyOption$Type} from "packages/java/nio/file/$CopyOption"
import {$OpenOption, $OpenOption$Type} from "packages/java/nio/file/$OpenOption"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $LinkOption extends $Enum<($LinkOption)> implements $OpenOption, $CopyOption {
static readonly "NOFOLLOW_LINKS": $LinkOption


public static "values"(): ($LinkOption)[]
public static "valueOf"(arg0: string): $LinkOption
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LinkOption$Type = (("nofollow_links")) | ($LinkOption);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LinkOption_ = $LinkOption$Type;
}}
declare module "packages/java/nio/file/attribute/$FileStoreAttributeView" {
import {$AttributeView, $AttributeView$Type} from "packages/java/nio/file/attribute/$AttributeView"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export interface $FileStoreAttributeView extends $AttributeView {

 "name"(): string

(): string
}

export namespace $FileStoreAttributeView {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FileStoreAttributeView$Type = ($FileStoreAttributeView);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FileStoreAttributeView_ = $FileStoreAttributeView$Type;
}}
declare module "packages/java/nio/channels/$DatagramChannel" {
import {$ByteChannel, $ByteChannel$Type} from "packages/java/nio/channels/$ByteChannel"
import {$MulticastChannel, $MulticastChannel$Type} from "packages/java/nio/channels/$MulticastChannel"
import {$ByteBuffer, $ByteBuffer$Type} from "packages/java/nio/$ByteBuffer"
import {$ScatteringByteChannel, $ScatteringByteChannel$Type} from "packages/java/nio/channels/$ScatteringByteChannel"
import {$MembershipKey, $MembershipKey$Type} from "packages/java/nio/channels/$MembershipKey"
import {$SocketAddress, $SocketAddress$Type} from "packages/java/net/$SocketAddress"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$GatheringByteChannel, $GatheringByteChannel$Type} from "packages/java/nio/channels/$GatheringByteChannel"
import {$ProtocolFamily, $ProtocolFamily$Type} from "packages/java/net/$ProtocolFamily"
import {$NetworkInterface, $NetworkInterface$Type} from "packages/java/net/$NetworkInterface"
import {$DatagramSocket, $DatagramSocket$Type} from "packages/java/net/$DatagramSocket"
import {$SocketOption, $SocketOption$Type} from "packages/java/net/$SocketOption"
import {$InetAddress, $InetAddress$Type} from "packages/java/net/$InetAddress"
import {$AbstractSelectableChannel, $AbstractSelectableChannel$Type} from "packages/java/nio/channels/spi/$AbstractSelectableChannel"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $DatagramChannel extends $AbstractSelectableChannel implements $ByteChannel, $ScatteringByteChannel, $GatheringByteChannel, $MulticastChannel {


public "write"(arg0: ($ByteBuffer$Type)[]): long
public "write"(arg0: ($ByteBuffer$Type)[], arg1: integer, arg2: integer): long
public "write"(arg0: $ByteBuffer$Type): integer
public "read"(arg0: ($ByteBuffer$Type)[]): long
public "read"(arg0: $ByteBuffer$Type): integer
public "read"(arg0: ($ByteBuffer$Type)[], arg1: integer, arg2: integer): long
public "connect"(arg0: $SocketAddress$Type): $DatagramChannel
public static "open"(arg0: $ProtocolFamily$Type): $DatagramChannel
public static "open"(): $DatagramChannel
public "validOps"(): integer
public "disconnect"(): $DatagramChannel
public "getLocalAddress"(): $SocketAddress
public "isConnected"(): boolean
public "receive"(arg0: $ByteBuffer$Type): $SocketAddress
public "send"(arg0: $ByteBuffer$Type, arg1: $SocketAddress$Type): integer
public "getRemoteAddress"(): $SocketAddress
public "socket"(): $DatagramSocket
public "setOption"<T>(arg0: $SocketOption$Type<(T)>, arg1: T): $DatagramChannel
public "join"(arg0: $InetAddress$Type, arg1: $NetworkInterface$Type): $MembershipKey
public "join"(arg0: $InetAddress$Type, arg1: $NetworkInterface$Type, arg2: $InetAddress$Type): $MembershipKey
public "close"(): void
public "supportedOptions"(): $Set<($SocketOption<(any)>)>
public "getOption"<T>(arg0: $SocketOption$Type<(T)>): T
public "isOpen"(): boolean
get "localAddress"(): $SocketAddress
get "connected"(): boolean
get "remoteAddress"(): $SocketAddress
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DatagramChannel$Type = ($DatagramChannel);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DatagramChannel_ = $DatagramChannel$Type;
}}
declare module "packages/java/nio/$FloatBuffer" {
import {$Comparable, $Comparable$Type} from "packages/java/lang/$Comparable"
import {$Buffer, $Buffer$Type} from "packages/java/nio/$Buffer"
import {$ByteOrder, $ByteOrder$Type} from "packages/java/nio/$ByteOrder"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $FloatBuffer extends $Buffer implements $Comparable<($FloatBuffer)> {


public "get"(): float
public "get"(arg0: integer, arg1: (float)[]): $FloatBuffer
public "get"(arg0: integer): float
public "get"(arg0: integer, arg1: (float)[], arg2: integer, arg3: integer): $FloatBuffer
public "get"(arg0: (float)[], arg1: integer, arg2: integer): $FloatBuffer
public "get"(arg0: (float)[]): $FloatBuffer
public "put"(arg0: (float)[]): $FloatBuffer
public "put"(arg0: integer, arg1: (float)[], arg2: integer, arg3: integer): $FloatBuffer
public "put"(arg0: integer, arg1: (float)[]): $FloatBuffer
public "put"(arg0: (float)[], arg1: integer, arg2: integer): $FloatBuffer
public "put"(arg0: integer, arg1: $FloatBuffer$Type, arg2: integer, arg3: integer): $FloatBuffer
public "put"(arg0: $FloatBuffer$Type): $FloatBuffer
public "put"(arg0: integer, arg1: float): $FloatBuffer
public "put"(arg0: float): $FloatBuffer
public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "compareTo"(arg0: $FloatBuffer$Type): integer
public static "wrap"(arg0: (float)[]): $FloatBuffer
public static "wrap"(arg0: (float)[], arg1: integer, arg2: integer): $FloatBuffer
public "position"(arg0: integer): $FloatBuffer
public "limit"(arg0: integer): $FloatBuffer
public "isDirect"(): boolean
public "hasArray"(): boolean
public "array"(): (float)[]
public "arrayOffset"(): integer
public "duplicate"(): $FloatBuffer
public "order"(): $ByteOrder
public "mismatch"(arg0: $FloatBuffer$Type): integer
public static "allocate"(arg0: integer): $FloatBuffer
public "asReadOnlyBuffer"(): $FloatBuffer
public "compact"(): $FloatBuffer
get "direct"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FloatBuffer$Type = ($FloatBuffer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FloatBuffer_ = $FloatBuffer$Type;
}}
declare module "packages/java/nio/channels/$NetworkChannel" {
import {$SocketAddress, $SocketAddress$Type} from "packages/java/net/$SocketAddress"
import {$Channel, $Channel$Type} from "packages/java/nio/channels/$Channel"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$SocketOption, $SocketOption$Type} from "packages/java/net/$SocketOption"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export interface $NetworkChannel extends $Channel {

 "bind"(arg0: $SocketAddress$Type): $NetworkChannel
 "getLocalAddress"(): $SocketAddress
 "supportedOptions"(): $Set<($SocketOption<(any)>)>
 "getOption"<T>(arg0: $SocketOption$Type<(T)>): T
 "setOption"<T>(arg0: $SocketOption$Type<(T)>, arg1: T): $NetworkChannel
 "isOpen"(): boolean
 "close"(): void
}

export namespace $NetworkChannel {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NetworkChannel$Type = ($NetworkChannel);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NetworkChannel_ = $NetworkChannel$Type;
}}
declare module "packages/java/nio/file/$WatchEvent$Kind" {
import {$Class, $Class$Type} from "packages/java/lang/$Class"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export interface $WatchEvent$Kind<T> {

 "name"(): string
 "type"(): $Class<(T)>
}

export namespace $WatchEvent$Kind {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WatchEvent$Kind$Type<T> = ($WatchEvent$Kind<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WatchEvent$Kind_<T> = $WatchEvent$Kind$Type<(T)>;
}}
declare module "packages/java/nio/channels/$ReadableByteChannel" {
import {$Channel, $Channel$Type} from "packages/java/nio/channels/$Channel"
import {$ByteBuffer, $ByteBuffer$Type} from "packages/java/nio/$ByteBuffer"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export interface $ReadableByteChannel extends $Channel {

 "read"(arg0: $ByteBuffer$Type): integer
 "isOpen"(): boolean
 "close"(): void
}

export namespace $ReadableByteChannel {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ReadableByteChannel$Type = ($ReadableByteChannel);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ReadableByteChannel_ = $ReadableByteChannel$Type;
}}
declare module "packages/java/nio/channels/$AsynchronousChannel" {
import {$Channel, $Channel$Type} from "packages/java/nio/channels/$Channel"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export interface $AsynchronousChannel extends $Channel {

 "close"(): void
 "isOpen"(): boolean
}

export namespace $AsynchronousChannel {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AsynchronousChannel$Type = ($AsynchronousChannel);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AsynchronousChannel_ = $AsynchronousChannel$Type;
}}
declare module "packages/java/nio/file/$PathMatcher" {
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export interface $PathMatcher {

 "matches"(arg0: $Path$Type): boolean

(arg0: $Path$Type): boolean
}

export namespace $PathMatcher {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PathMatcher$Type = ($PathMatcher);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PathMatcher_ = $PathMatcher$Type;
}}
declare module "packages/java/nio/channels/$Pipe$SinkChannel" {
import {$WritableByteChannel, $WritableByteChannel$Type} from "packages/java/nio/channels/$WritableByteChannel"
import {$GatheringByteChannel, $GatheringByteChannel$Type} from "packages/java/nio/channels/$GatheringByteChannel"
import {$ByteBuffer, $ByteBuffer$Type} from "packages/java/nio/$ByteBuffer"
import {$AbstractSelectableChannel, $AbstractSelectableChannel$Type} from "packages/java/nio/channels/spi/$AbstractSelectableChannel"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $Pipe$SinkChannel extends $AbstractSelectableChannel implements $WritableByteChannel, $GatheringByteChannel {


public "validOps"(): integer
public "write"(arg0: $ByteBuffer$Type): integer
public "write"(arg0: ($ByteBuffer$Type)[], arg1: integer, arg2: integer): long
public "write"(arg0: ($ByteBuffer$Type)[]): long
public "isOpen"(): boolean
public "close"(): void
get "open"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Pipe$SinkChannel$Type = ($Pipe$SinkChannel);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Pipe$SinkChannel_ = $Pipe$SinkChannel$Type;
}}
declare module "packages/java/nio/channels/$SelectableChannel" {
import {$SelectorProvider, $SelectorProvider$Type} from "packages/java/nio/channels/spi/$SelectorProvider"
import {$Channel, $Channel$Type} from "packages/java/nio/channels/$Channel"
import {$AbstractInterruptibleChannel, $AbstractInterruptibleChannel$Type} from "packages/java/nio/channels/spi/$AbstractInterruptibleChannel"
import {$Selector, $Selector$Type} from "packages/java/nio/channels/$Selector"
import {$SelectionKey, $SelectionKey$Type} from "packages/java/nio/channels/$SelectionKey"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $SelectableChannel extends $AbstractInterruptibleChannel implements $Channel {


public "isRegistered"(): boolean
public "register"(arg0: $Selector$Type, arg1: integer): $SelectionKey
public "register"(arg0: $Selector$Type, arg1: integer, arg2: any): $SelectionKey
public "provider"(): $SelectorProvider
public "configureBlocking"(arg0: boolean): $SelectableChannel
public "isBlocking"(): boolean
public "blockingLock"(): any
public "validOps"(): integer
public "keyFor"(arg0: $Selector$Type): $SelectionKey
public "isOpen"(): boolean
public "close"(): void
get "registered"(): boolean
get "blocking"(): boolean
get "open"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SelectableChannel$Type = ($SelectableChannel);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SelectableChannel_ = $SelectableChannel$Type;
}}
declare module "packages/java/nio/file/$WatchEvent" {
import {$WatchEvent$Kind, $WatchEvent$Kind$Type} from "packages/java/nio/file/$WatchEvent$Kind"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export interface $WatchEvent<T> {

 "context"(): T
 "count"(): integer
 "kind"(): $WatchEvent$Kind<(T)>
}

export namespace $WatchEvent {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WatchEvent$Type<T> = ($WatchEvent<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WatchEvent_<T> = $WatchEvent$Type<(T)>;
}}
declare module "packages/java/nio/channels/$ByteChannel" {
import {$WritableByteChannel, $WritableByteChannel$Type} from "packages/java/nio/channels/$WritableByteChannel"
import {$ReadableByteChannel, $ReadableByteChannel$Type} from "packages/java/nio/channels/$ReadableByteChannel"
import {$ByteBuffer, $ByteBuffer$Type} from "packages/java/nio/$ByteBuffer"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export interface $ByteChannel extends $ReadableByteChannel, $WritableByteChannel {

 "read"(arg0: $ByteBuffer$Type): integer
 "write"(arg0: $ByteBuffer$Type): integer
 "isOpen"(): boolean
 "close"(): void
}

export namespace $ByteChannel {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ByteChannel$Type = ($ByteChannel);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ByteChannel_ = $ByteChannel$Type;
}}
declare module "packages/java/nio/channels/$Channel" {
import {$Closeable, $Closeable$Type} from "packages/java/io/$Closeable"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export interface $Channel extends $Closeable {

 "isOpen"(): boolean
 "close"(): void
}

export namespace $Channel {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Channel$Type = ($Channel);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Channel_ = $Channel$Type;
}}
declare module "packages/java/nio/file/$FileStore" {
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$FileStoreAttributeView, $FileStoreAttributeView$Type} from "packages/java/nio/file/attribute/$FileStoreAttributeView"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $FileStore {


public "name"(): string
public "type"(): string
public "isReadOnly"(): boolean
public "getTotalSpace"(): long
public "getUsableSpace"(): long
public "getAttribute"(arg0: string): any
public "getUnallocatedSpace"(): long
public "getBlockSize"(): long
public "supportsFileAttributeView"(arg0: $Class$Type<(any)>): boolean
public "supportsFileAttributeView"(arg0: string): boolean
public "getFileStoreAttributeView"<V extends $FileStoreAttributeView>(arg0: $Class$Type<(V)>): V
get "readOnly"(): boolean
get "totalSpace"(): long
get "usableSpace"(): long
get "unallocatedSpace"(): long
get "blockSize"(): long
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FileStore$Type = ($FileStore);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FileStore_ = $FileStore$Type;
}}
declare module "packages/java/nio/channels/$WritableByteChannel" {
import {$Channel, $Channel$Type} from "packages/java/nio/channels/$Channel"
import {$ByteBuffer, $ByteBuffer$Type} from "packages/java/nio/$ByteBuffer"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export interface $WritableByteChannel extends $Channel {

 "write"(arg0: $ByteBuffer$Type): integer
 "isOpen"(): boolean
 "close"(): void
}

export namespace $WritableByteChannel {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WritableByteChannel$Type = ($WritableByteChannel);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WritableByteChannel_ = $WritableByteChannel$Type;
}}
declare module "packages/java/nio/channels/$FileLock" {
import {$Channel, $Channel$Type} from "packages/java/nio/channels/$Channel"
import {$FileChannel, $FileChannel$Type} from "packages/java/nio/channels/$FileChannel"
import {$AutoCloseable, $AutoCloseable$Type} from "packages/java/lang/$AutoCloseable"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $FileLock implements $AutoCloseable {


public "toString"(): string
public "position"(): long
public "size"(): long
public "close"(): void
public "release"(): void
public "channel"(): $FileChannel
public "isValid"(): boolean
public "acquiredBy"(): $Channel
public "isShared"(): boolean
public "overlaps"(arg0: long, arg1: long): boolean
get "valid"(): boolean
get "shared"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FileLock$Type = ($FileLock);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FileLock_ = $FileLock$Type;
}}
declare module "packages/java/nio/channels/$MembershipKey" {
import {$MulticastChannel, $MulticastChannel$Type} from "packages/java/nio/channels/$MulticastChannel"
import {$NetworkInterface, $NetworkInterface$Type} from "packages/java/net/$NetworkInterface"
import {$InetAddress, $InetAddress$Type} from "packages/java/net/$InetAddress"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $MembershipKey {


public "group"(): $InetAddress
public "block"(arg0: $InetAddress$Type): $MembershipKey
public "channel"(): $MulticastChannel
public "drop"(): void
public "isValid"(): boolean
public "sourceAddress"(): $InetAddress
public "networkInterface"(): $NetworkInterface
public "unblock"(arg0: $InetAddress$Type): $MembershipKey
get "valid"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MembershipKey$Type = ($MembershipKey);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MembershipKey_ = $MembershipKey$Type;
}}
declare module "packages/java/nio/channels/$CompletionHandler" {
import {$Throwable, $Throwable$Type} from "packages/java/lang/$Throwable"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export interface $CompletionHandler<V, A> {

 "completed"(arg0: V, arg1: A): void
 "failed"(arg0: $Throwable$Type, arg1: A): void
}

export namespace $CompletionHandler {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CompletionHandler$Type<V, A> = ($CompletionHandler<(V), (A)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CompletionHandler_<V, A> = $CompletionHandler$Type<(V), (A)>;
}}
declare module "packages/java/nio/file/attribute/$FileAttribute" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export interface $FileAttribute<T> {

 "name"(): string
 "value"(): T
}

export namespace $FileAttribute {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FileAttribute$Type<T> = ($FileAttribute<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FileAttribute_<T> = $FileAttribute$Type<(T)>;
}}
declare module "packages/java/nio/channels/spi/$AbstractSelectableChannel" {
import {$SelectorProvider, $SelectorProvider$Type} from "packages/java/nio/channels/spi/$SelectorProvider"
import {$Selector, $Selector$Type} from "packages/java/nio/channels/$Selector"
import {$SelectionKey, $SelectionKey$Type} from "packages/java/nio/channels/$SelectionKey"
import {$SelectableChannel, $SelectableChannel$Type} from "packages/java/nio/channels/$SelectableChannel"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $AbstractSelectableChannel extends $SelectableChannel {


public "isRegistered"(): boolean
public "register"(arg0: $Selector$Type, arg1: integer, arg2: any): $SelectionKey
public "provider"(): $SelectorProvider
public "configureBlocking"(arg0: boolean): $SelectableChannel
public "isBlocking"(): boolean
public "blockingLock"(): any
public "keyFor"(arg0: $Selector$Type): $SelectionKey
public "isOpen"(): boolean
public "close"(): void
get "registered"(): boolean
get "blocking"(): boolean
get "open"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AbstractSelectableChannel$Type = ($AbstractSelectableChannel);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AbstractSelectableChannel_ = $AbstractSelectableChannel$Type;
}}
declare module "packages/java/nio/$LongBuffer" {
import {$Comparable, $Comparable$Type} from "packages/java/lang/$Comparable"
import {$Buffer, $Buffer$Type} from "packages/java/nio/$Buffer"
import {$ByteOrder, $ByteOrder$Type} from "packages/java/nio/$ByteOrder"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $LongBuffer extends $Buffer implements $Comparable<($LongBuffer)> {


public "get"(): long
public "get"(arg0: integer, arg1: (long)[]): $LongBuffer
public "get"(arg0: integer): long
public "get"(arg0: integer, arg1: (long)[], arg2: integer, arg3: integer): $LongBuffer
public "get"(arg0: (long)[], arg1: integer, arg2: integer): $LongBuffer
public "get"(arg0: (long)[]): $LongBuffer
public "put"(arg0: (long)[]): $LongBuffer
public "put"(arg0: integer, arg1: (long)[], arg2: integer, arg3: integer): $LongBuffer
public "put"(arg0: integer, arg1: (long)[]): $LongBuffer
public "put"(arg0: (long)[], arg1: integer, arg2: integer): $LongBuffer
public "put"(arg0: integer, arg1: $LongBuffer$Type, arg2: integer, arg3: integer): $LongBuffer
public "put"(arg0: $LongBuffer$Type): $LongBuffer
public "put"(arg0: integer, arg1: long): $LongBuffer
public "put"(arg0: long): $LongBuffer
public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "compareTo"(arg0: $LongBuffer$Type): integer
public static "wrap"(arg0: (long)[]): $LongBuffer
public static "wrap"(arg0: (long)[], arg1: integer, arg2: integer): $LongBuffer
public "position"(arg0: integer): $LongBuffer
public "limit"(arg0: integer): $LongBuffer
public "isDirect"(): boolean
public "hasArray"(): boolean
public "array"(): (long)[]
public "arrayOffset"(): integer
public "duplicate"(): $LongBuffer
public "order"(): $ByteOrder
public "mismatch"(arg0: $LongBuffer$Type): integer
public static "allocate"(arg0: integer): $LongBuffer
public "asReadOnlyBuffer"(): $LongBuffer
public "compact"(): $LongBuffer
get "direct"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LongBuffer$Type = ($LongBuffer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LongBuffer_ = $LongBuffer$Type;
}}
declare module "packages/java/nio/charset/$CoderResult" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $CoderResult {
static readonly "UNDERFLOW": $CoderResult
static readonly "OVERFLOW": $CoderResult


public "length"(): integer
public "toString"(): string
public "isUnderflow"(): boolean
public "throwException"(): void
public "isOverflow"(): boolean
public static "unmappableForLength"(arg0: integer): $CoderResult
public "isError"(): boolean
public static "malformedForLength"(arg0: integer): $CoderResult
public "isMalformed"(): boolean
public "isUnmappable"(): boolean
get "underflow"(): boolean
get "overflow"(): boolean
get "error"(): boolean
get "malformed"(): boolean
get "unmappable"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CoderResult$Type = ($CoderResult);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CoderResult_ = $CoderResult$Type;
}}

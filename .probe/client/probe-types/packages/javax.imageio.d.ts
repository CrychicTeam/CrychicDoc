declare module "packages/javax/imageio/stream/$ImageInputStream" {
import {$DataInput, $DataInput$Type} from "packages/java/io/$DataInput"
import {$Closeable, $Closeable$Type} from "packages/java/io/$Closeable"
import {$IIOByteBuffer, $IIOByteBuffer$Type} from "packages/javax/imageio/stream/$IIOByteBuffer"
import {$ByteOrder, $ByteOrder$Type} from "packages/java/nio/$ByteOrder"

export interface $ImageInputStream extends $DataInput, $Closeable {

 "getStreamPosition"(): long
 "length"(): long
 "flush"(): void
 "read"(arg0: (byte)[], arg1: integer, arg2: integer): integer
 "read"(arg0: (byte)[]): integer
 "read"(): integer
 "readLine"(): string
 "close"(): void
 "readInt"(): integer
 "mark"(): void
 "reset"(): void
 "readUTF"(): string
 "readBytes"(arg0: $IIOByteBuffer$Type, arg1: integer): void
 "readChar"(): character
 "readFloat"(): float
 "readFully"(arg0: (short)[], arg1: integer, arg2: integer): void
 "readFully"(arg0: (character)[], arg1: integer, arg2: integer): void
 "readFully"(arg0: (double)[], arg1: integer, arg2: integer): void
 "readFully"(arg0: (float)[], arg1: integer, arg2: integer): void
 "readFully"(arg0: (long)[], arg1: integer, arg2: integer): void
 "readFully"(arg0: (integer)[], arg1: integer, arg2: integer): void
 "readFully"(arg0: (byte)[]): void
 "readFully"(arg0: (byte)[], arg1: integer, arg2: integer): void
 "skipBytes"(arg0: integer): integer
 "skipBytes"(arg0: long): long
 "readBoolean"(): boolean
 "readByte"(): byte
 "readUnsignedByte"(): integer
 "readShort"(): short
 "readUnsignedShort"(): integer
 "readLong"(): long
 "readDouble"(): double
 "seek"(arg0: long): void
 "isCached"(): boolean
 "getByteOrder"(): $ByteOrder
 "readUnsignedInt"(): long
 "setByteOrder"(arg0: $ByteOrder$Type): void
 "getBitOffset"(): integer
 "setBitOffset"(arg0: integer): void
 "readBit"(): integer
 "readBits"(arg0: integer): long
 "flushBefore"(arg0: long): void
 "getFlushedPosition"(): long
 "isCachedMemory"(): boolean
 "isCachedFile"(): boolean
}

export namespace $ImageInputStream {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ImageInputStream$Type = ($ImageInputStream);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ImageInputStream_ = $ImageInputStream$Type;
}}
declare module "packages/javax/imageio/stream/$IIOByteBuffer" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $IIOByteBuffer {

constructor(arg0: (byte)[], arg1: integer, arg2: integer)

public "getLength"(): integer
public "setLength"(arg0: integer): void
public "getOffset"(): integer
public "setOffset"(arg0: integer): void
public "getData"(): (byte)[]
public "setData"(arg0: (byte)[]): void
get "length"(): integer
set "length"(value: integer)
get "offset"(): integer
set "offset"(value: integer)
get "data"(): (byte)[]
set "data"(value: (byte)[])
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IIOByteBuffer$Type = ($IIOByteBuffer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IIOByteBuffer_ = $IIOByteBuffer$Type;
}}

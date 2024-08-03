declare module "packages/sun/misc/$Unsafe" {
import {$Throwable, $Throwable$Type} from "packages/java/lang/$Throwable"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$Field, $Field$Type} from "packages/java/lang/reflect/$Field"
import {$ByteBuffer, $ByteBuffer$Type} from "packages/java/nio/$ByteBuffer"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $Unsafe {
static readonly "INVALID_FIELD_OFFSET": integer
static readonly "ARRAY_BOOLEAN_BASE_OFFSET": integer
static readonly "ARRAY_BYTE_BASE_OFFSET": integer
static readonly "ARRAY_SHORT_BASE_OFFSET": integer
static readonly "ARRAY_CHAR_BASE_OFFSET": integer
static readonly "ARRAY_INT_BASE_OFFSET": integer
static readonly "ARRAY_LONG_BASE_OFFSET": integer
static readonly "ARRAY_FLOAT_BASE_OFFSET": integer
static readonly "ARRAY_DOUBLE_BASE_OFFSET": integer
static readonly "ARRAY_OBJECT_BASE_OFFSET": integer
static readonly "ARRAY_BOOLEAN_INDEX_SCALE": integer
static readonly "ARRAY_BYTE_INDEX_SCALE": integer
static readonly "ARRAY_SHORT_INDEX_SCALE": integer
static readonly "ARRAY_CHAR_INDEX_SCALE": integer
static readonly "ARRAY_INT_INDEX_SCALE": integer
static readonly "ARRAY_LONG_INDEX_SCALE": integer
static readonly "ARRAY_FLOAT_INDEX_SCALE": integer
static readonly "ARRAY_DOUBLE_INDEX_SCALE": integer
static readonly "ARRAY_OBJECT_INDEX_SCALE": integer
static readonly "ADDRESS_SIZE": integer


public "allocateInstance"(arg0: $Class$Type<(any)>): any
public "loadFence"(): void
public "storeFence"(): void
public "fullFence"(): void
public "getBoolean"(arg0: any, arg1: long): boolean
public "putBoolean"(arg0: any, arg1: long, arg2: boolean): void
public "getByte"(arg0: long): byte
public "getByte"(arg0: any, arg1: long): byte
public "putByte"(arg0: any, arg1: long, arg2: byte): void
public "putByte"(arg0: long, arg1: byte): void
public "getShort"(arg0: any, arg1: long): short
public "getShort"(arg0: long): short
public "putShort"(arg0: long, arg1: short): void
public "putShort"(arg0: any, arg1: long, arg2: short): void
public "getChar"(arg0: long): character
public "getChar"(arg0: any, arg1: long): character
public "putChar"(arg0: any, arg1: long, arg2: character): void
public "putChar"(arg0: long, arg1: character): void
public "getInt"(arg0: any, arg1: long): integer
public "getInt"(arg0: long): integer
public "putInt"(arg0: any, arg1: long, arg2: integer): void
public "putInt"(arg0: long, arg1: integer): void
public "getLong"(arg0: long): long
public "getLong"(arg0: any, arg1: long): long
public "putLong"(arg0: any, arg1: long, arg2: long): void
public "putLong"(arg0: long, arg1: long): void
public "getFloat"(arg0: long): float
public "getFloat"(arg0: any, arg1: long): float
public "putFloat"(arg0: long, arg1: float): void
public "putFloat"(arg0: any, arg1: long, arg2: float): void
public "getDouble"(arg0: long): double
public "getDouble"(arg0: any, arg1: long): double
public "putDouble"(arg0: long, arg1: double): void
public "putDouble"(arg0: any, arg1: long, arg2: double): void
public "getBooleanVolatile"(arg0: any, arg1: long): boolean
public "putBooleanVolatile"(arg0: any, arg1: long, arg2: boolean): void
public "getByteVolatile"(arg0: any, arg1: long): byte
public "putByteVolatile"(arg0: any, arg1: long, arg2: byte): void
public "getShortVolatile"(arg0: any, arg1: long): short
public "putShortVolatile"(arg0: any, arg1: long, arg2: short): void
public "getCharVolatile"(arg0: any, arg1: long): character
public "putCharVolatile"(arg0: any, arg1: long, arg2: character): void
public "getIntVolatile"(arg0: any, arg1: long): integer
public "putIntVolatile"(arg0: any, arg1: long, arg2: integer): void
public "getLongVolatile"(arg0: any, arg1: long): long
public "putLongVolatile"(arg0: any, arg1: long, arg2: long): void
public "getFloatVolatile"(arg0: any, arg1: long): float
public "putFloatVolatile"(arg0: any, arg1: long, arg2: float): void
public "getDoubleVolatile"(arg0: any, arg1: long): double
public "putDoubleVolatile"(arg0: any, arg1: long, arg2: double): void
public "getAndAddInt"(arg0: any, arg1: long, arg2: integer): integer
public "getAndAddLong"(arg0: any, arg1: long, arg2: long): long
public "getAndSetInt"(arg0: any, arg1: long, arg2: integer): integer
public "getAndSetLong"(arg0: any, arg1: long, arg2: long): long
public "park"(arg0: boolean, arg1: long): void
public "unpark"(arg0: any): void
public "throwException"(arg0: $Throwable$Type): void
public "objectFieldOffset"(arg0: $Field$Type): long
/**
 * 
 * @deprecated
 */
public "ensureClassInitialized"(arg0: $Class$Type<(any)>): void
public "staticFieldBase"(arg0: $Field$Type): any
public "staticFieldOffset"(arg0: $Field$Type): long
/**
 * 
 * @deprecated
 */
public "shouldBeInitialized"(arg0: $Class$Type<(any)>): boolean
public "getAddress"(arg0: long): long
public "putAddress"(arg0: long, arg1: long): void
public "freeMemory"(arg0: long): void
public "setMemory"(arg0: long, arg1: long, arg2: byte): void
public "setMemory"(arg0: any, arg1: long, arg2: long, arg3: byte): void
public "copyMemory"(arg0: long, arg1: long, arg2: long): void
public "copyMemory"(arg0: any, arg1: long, arg2: any, arg3: long, arg4: long): void
public "arrayBaseOffset"(arg0: $Class$Type<(any)>): integer
public "arrayIndexScale"(arg0: $Class$Type<(any)>): integer
public "allocateMemory"(arg0: long): long
public "reallocateMemory"(arg0: long, arg1: long): long
public "addressSize"(): integer
public "pageSize"(): integer
public "getLoadAverage"(arg0: (double)[], arg1: integer): integer
public "invokeCleaner"(arg0: $ByteBuffer$Type): void
public "getObject"(arg0: any, arg1: long): any
public "getObjectVolatile"(arg0: any, arg1: long): any
public "putObject"(arg0: any, arg1: long, arg2: any): void
public "putObjectVolatile"(arg0: any, arg1: long, arg2: any): void
public "getAndSetObject"(arg0: any, arg1: long, arg2: any): any
public "compareAndSwapObject"(arg0: any, arg1: long, arg2: any, arg3: any): boolean
public "compareAndSwapInt"(arg0: any, arg1: long, arg2: integer, arg3: integer): boolean
public "compareAndSwapLong"(arg0: any, arg1: long, arg2: long, arg3: long): boolean
public "putOrderedObject"(arg0: any, arg1: long, arg2: any): void
public "putOrderedInt"(arg0: any, arg1: long, arg2: integer): void
public "putOrderedLong"(arg0: any, arg1: long, arg2: long): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Unsafe$Type = ($Unsafe);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Unsafe_ = $Unsafe$Type;
}}

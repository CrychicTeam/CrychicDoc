declare module "packages/java/io/$DataInput" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export interface $DataInput {

 "readLine"(): string
 "readInt"(): integer
 "readUTF"(): string
 "readChar"(): character
 "readFloat"(): float
 "readFully"(arg0: (byte)[]): void
 "readFully"(arg0: (byte)[], arg1: integer, arg2: integer): void
 "skipBytes"(arg0: integer): integer
 "readBoolean"(): boolean
 "readByte"(): byte
 "readUnsignedByte"(): integer
 "readShort"(): short
 "readUnsignedShort"(): integer
 "readLong"(): long
 "readDouble"(): double
}

export namespace $DataInput {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DataInput$Type = ($DataInput);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DataInput_ = $DataInput$Type;
}}
declare module "packages/java/io/$Flushable" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export interface $Flushable {

 "flush"(): void

(): void
}

export namespace $Flushable {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Flushable$Type = ($Flushable);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Flushable_ = $Flushable$Type;
}}
declare module "packages/java/io/$PrintStream" {
import {$Closeable, $Closeable$Type} from "packages/java/io/$Closeable"
import {$OutputStream, $OutputStream$Type} from "packages/java/io/$OutputStream"
import {$FilterOutputStream, $FilterOutputStream$Type} from "packages/java/io/$FilterOutputStream"
import {$File, $File$Type} from "packages/java/io/$File"
import {$Appendable, $Appendable$Type} from "packages/java/lang/$Appendable"
import {$Charset, $Charset$Type} from "packages/java/nio/charset/$Charset"
import {$Locale, $Locale$Type} from "packages/java/util/$Locale"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $PrintStream extends $FilterOutputStream implements $Appendable, $Closeable {

constructor(arg0: string, arg1: string)
constructor(arg0: string)
constructor(arg0: $OutputStream$Type, arg1: boolean, arg2: $Charset$Type)
constructor(arg0: $OutputStream$Type, arg1: boolean, arg2: string)
constructor(arg0: string, arg1: $Charset$Type)
constructor(arg0: $File$Type, arg1: string)
constructor(arg0: $File$Type, arg1: $Charset$Type)
constructor(arg0: $File$Type)
constructor(arg0: $OutputStream$Type)
constructor(arg0: $OutputStream$Type, arg1: boolean)

public "println"(arg0: string): void
public "println"(arg0: any): void
public "println"(arg0: float): void
public "println"(arg0: double): void
public "println"(arg0: (character)[]): void
public "println"(arg0: boolean): void
public "println"(): void
public "println"(arg0: character): void
public "println"(arg0: integer): void
public "println"(arg0: long): void
public "flush"(): void
public "format"(arg0: string, ...arg1: (any)[]): $PrintStream
public "format"(arg0: $Locale$Type, arg1: string, ...arg2: (any)[]): $PrintStream
public "printf"(arg0: $Locale$Type, arg1: string, ...arg2: (any)[]): $PrintStream
public "printf"(arg0: string, ...arg1: (any)[]): $PrintStream
public "write"(arg0: (byte)[], arg1: integer, arg2: integer): void
public "write"(arg0: integer): void
public "write"(arg0: (byte)[]): void
public "print"(arg0: boolean): void
public "print"(arg0: string): void
public "print"(arg0: (character)[]): void
public "print"(arg0: long): void
public "print"(arg0: double): void
public "print"(arg0: float): void
public "print"(arg0: character): void
public "print"(arg0: integer): void
public "print"(arg0: any): void
public "close"(): void
public "writeBytes"(arg0: (byte)[]): void
public "checkError"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PrintStream$Type = ($PrintStream);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PrintStream_ = $PrintStream$Type;
}}
declare module "packages/java/io/$ObjectOutput" {
import {$AutoCloseable, $AutoCloseable$Type} from "packages/java/lang/$AutoCloseable"
import {$DataOutput, $DataOutput$Type} from "packages/java/io/$DataOutput"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export interface $ObjectOutput extends $DataOutput, $AutoCloseable {

 "flush"(): void
 "write"(arg0: (byte)[], arg1: integer, arg2: integer): void
 "write"(arg0: (byte)[]): void
 "write"(arg0: integer): void
 "writeObject"(arg0: any): void
 "close"(): void
 "writeInt"(arg0: integer): void
 "writeUTF"(arg0: string): void
 "writeBytes"(arg0: string): void
 "writeChar"(arg0: integer): void
 "writeFloat"(arg0: float): void
 "writeBoolean"(arg0: boolean): void
 "writeByte"(arg0: integer): void
 "writeShort"(arg0: integer): void
 "writeLong"(arg0: long): void
 "writeDouble"(arg0: double): void
 "writeChars"(arg0: string): void
}

export namespace $ObjectOutput {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ObjectOutput$Type = ($ObjectOutput);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ObjectOutput_ = $ObjectOutput$Type;
}}
declare module "packages/java/io/$Closeable" {
import {$AutoCloseable, $AutoCloseable$Type} from "packages/java/lang/$AutoCloseable"

export interface $Closeable extends $AutoCloseable {

 "close"(): void

(): void
}

export namespace $Closeable {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Closeable$Type = ($Closeable);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Closeable_ = $Closeable$Type;
}}
declare module "packages/java/io/$FilterOutputStream" {
import {$OutputStream, $OutputStream$Type} from "packages/java/io/$OutputStream"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $FilterOutputStream extends $OutputStream {

constructor(arg0: $OutputStream$Type)

public "flush"(): void
public "write"(arg0: (byte)[], arg1: integer, arg2: integer): void
public "write"(arg0: (byte)[]): void
public "write"(arg0: integer): void
public "close"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FilterOutputStream$Type = ($FilterOutputStream);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FilterOutputStream_ = $FilterOutputStream$Type;
}}
declare module "packages/java/io/$Externalizable" {
import {$Serializable, $Serializable$Type} from "packages/java/io/$Serializable"
import {$ObjectOutput, $ObjectOutput$Type} from "packages/java/io/$ObjectOutput"
import {$ObjectInput, $ObjectInput$Type} from "packages/java/io/$ObjectInput"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export interface $Externalizable extends $Serializable {

 "writeExternal"(arg0: $ObjectOutput$Type): void
 "readExternal"(arg0: $ObjectInput$Type): void
}

export namespace $Externalizable {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Externalizable$Type = ($Externalizable);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Externalizable_ = $Externalizable$Type;
}}
declare module "packages/java/io/$ObjectInputStream$GetField" {
import {$ObjectStreamClass, $ObjectStreamClass$Type} from "packages/java/io/$ObjectStreamClass"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $ObjectInputStream$GetField {

constructor()

public "get"(arg0: string, arg1: long): long
public "get"(arg0: string, arg1: integer): integer
public "get"(arg0: string, arg1: short): short
public "get"(arg0: string, arg1: float): float
public "get"(arg0: string, arg1: double): double
public "get"(arg0: string, arg1: any): any
public "get"(arg0: string, arg1: boolean): boolean
public "get"(arg0: string, arg1: byte): byte
public "get"(arg0: string, arg1: character): character
public "defaulted"(arg0: string): boolean
public "getObjectStreamClass"(): $ObjectStreamClass
get "objectStreamClass"(): $ObjectStreamClass
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ObjectInputStream$GetField$Type = ($ObjectInputStream$GetField);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ObjectInputStream$GetField_ = $ObjectInputStream$GetField$Type;
}}
declare module "packages/java/io/$FileFilter" {
import {$File, $File$Type} from "packages/java/io/$File"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export interface $FileFilter {

 "accept"(arg0: $File$Type): boolean

(arg0: $File$Type): boolean
}

export namespace $FileFilter {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FileFilter$Type = ($FileFilter);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FileFilter_ = $FileFilter$Type;
}}
declare module "packages/java/io/$FilenameFilter" {
import {$File, $File$Type} from "packages/java/io/$File"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export interface $FilenameFilter {

 "accept"(arg0: $File$Type, arg1: string): boolean

(arg0: $File$Type, arg1: string): boolean
}

export namespace $FilenameFilter {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FilenameFilter$Type = ($FilenameFilter);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FilenameFilter_ = $FilenameFilter$Type;
}}
declare module "packages/java/io/$PrintWriter" {
import {$OutputStream, $OutputStream$Type} from "packages/java/io/$OutputStream"
import {$File, $File$Type} from "packages/java/io/$File"
import {$Charset, $Charset$Type} from "packages/java/nio/charset/$Charset"
import {$Writer, $Writer$Type} from "packages/java/io/$Writer"
import {$Locale, $Locale$Type} from "packages/java/util/$Locale"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $PrintWriter extends $Writer {

constructor(arg0: string)
constructor(arg0: string, arg1: string)
constructor(arg0: string, arg1: $Charset$Type)
constructor(arg0: $File$Type)
constructor(arg0: $File$Type, arg1: string)
constructor(arg0: $File$Type, arg1: $Charset$Type)
constructor(arg0: $Writer$Type)
constructor(arg0: $Writer$Type, arg1: boolean)
constructor(arg0: $OutputStream$Type, arg1: boolean, arg2: $Charset$Type)
constructor(arg0: $OutputStream$Type, arg1: boolean)
constructor(arg0: $OutputStream$Type)

public "println"(arg0: (character)[]): void
public "println"(): void
public "println"(arg0: boolean): void
public "println"(arg0: string): void
public "println"(arg0: any): void
public "println"(arg0: double): void
public "println"(arg0: float): void
public "println"(arg0: long): void
public "println"(arg0: integer): void
public "println"(arg0: character): void
public "append"(arg0: charseq, arg1: integer, arg2: integer): $PrintWriter
public "append"(arg0: character): $PrintWriter
public "append"(arg0: charseq): $PrintWriter
public "flush"(): void
public "format"(arg0: string, ...arg1: (any)[]): $PrintWriter
public "format"(arg0: $Locale$Type, arg1: string, ...arg2: (any)[]): $PrintWriter
public "printf"(arg0: string, ...arg1: (any)[]): $PrintWriter
public "printf"(arg0: $Locale$Type, arg1: string, ...arg2: (any)[]): $PrintWriter
public "write"(arg0: (character)[], arg1: integer, arg2: integer): void
public "write"(arg0: string): void
public "write"(arg0: string, arg1: integer, arg2: integer): void
public "write"(arg0: (character)[]): void
public "write"(arg0: integer): void
public "print"(arg0: float): void
public "print"(arg0: long): void
public "print"(arg0: integer): void
public "print"(arg0: character): void
public "print"(arg0: boolean): void
public "print"(arg0: any): void
public "print"(arg0: string): void
public "print"(arg0: (character)[]): void
public "print"(arg0: double): void
public "close"(): void
public "checkError"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PrintWriter$Type = ($PrintWriter);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PrintWriter_ = $PrintWriter$Type;
}}
declare module "packages/java/io/$ByteArrayOutputStream" {
import {$OutputStream, $OutputStream$Type} from "packages/java/io/$OutputStream"
import {$Charset, $Charset$Type} from "packages/java/nio/charset/$Charset"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $ByteArrayOutputStream extends $OutputStream {

constructor()
constructor(arg0: integer)

public "toString"(arg0: string): string
public "toString"(arg0: $Charset$Type): string
public "toString"(): string
/**
 * 
 * @deprecated
 */
public "toString"(arg0: integer): string
public "size"(): integer
public "write"(arg0: integer): void
public "write"(arg0: (byte)[], arg1: integer, arg2: integer): void
public "close"(): void
public "toByteArray"(): (byte)[]
public "reset"(): void
public "writeBytes"(arg0: (byte)[]): void
public "writeTo"(arg0: $OutputStream$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ByteArrayOutputStream$Type = ($ByteArrayOutputStream);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ByteArrayOutputStream_ = $ByteArrayOutputStream$Type;
}}
declare module "packages/java/io/$ObjectInputStream" {
import {$ObjectStreamConstants, $ObjectStreamConstants$Type} from "packages/java/io/$ObjectStreamConstants"
import {$ObjectInputFilter, $ObjectInputFilter$Type} from "packages/java/io/$ObjectInputFilter"
import {$InputStream, $InputStream$Type} from "packages/java/io/$InputStream"
import {$ObjectInputValidation, $ObjectInputValidation$Type} from "packages/java/io/$ObjectInputValidation"
import {$ObjectInputStream$GetField, $ObjectInputStream$GetField$Type} from "packages/java/io/$ObjectInputStream$GetField"
import {$ObjectInput, $ObjectInput$Type} from "packages/java/io/$ObjectInput"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $ObjectInputStream extends $InputStream implements $ObjectInput, $ObjectStreamConstants {

constructor(arg0: $InputStream$Type)

public "defaultReadObject"(): void
public "readObject"(): any
public "read"(arg0: (byte)[], arg1: integer, arg2: integer): integer
public "read"(): integer
public "readFields"(): $ObjectInputStream$GetField
/**
 * 
 * @deprecated
 */
public "readLine"(): string
public "close"(): void
public "readInt"(): integer
public "available"(): integer
public "readUTF"(): string
public "readChar"(): character
public "readFloat"(): float
public "readFully"(arg0: (byte)[], arg1: integer, arg2: integer): void
public "readFully"(arg0: (byte)[]): void
public "skipBytes"(arg0: integer): integer
public "readBoolean"(): boolean
public "readByte"(): byte
public "readUnsignedByte"(): integer
public "readShort"(): short
public "readUnsignedShort"(): integer
public "readLong"(): long
public "readDouble"(): double
public "setObjectInputFilter"(arg0: $ObjectInputFilter$Type): void
public "getObjectInputFilter"(): $ObjectInputFilter
public "readUnshared"(): any
public "registerValidation"(arg0: $ObjectInputValidation$Type, arg1: integer): void
public "read"(arg0: (byte)[]): integer
public "skip"(arg0: long): long
set "objectInputFilter"(value: $ObjectInputFilter$Type)
get "objectInputFilter"(): $ObjectInputFilter
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ObjectInputStream$Type = ($ObjectInputStream);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ObjectInputStream_ = $ObjectInputStream$Type;
}}
declare module "packages/java/io/$FileDescriptor" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $FileDescriptor {
static readonly "in": $FileDescriptor
static readonly "out": $FileDescriptor
static readonly "err": $FileDescriptor

constructor()

public "sync"(): void
public "valid"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FileDescriptor$Type = ($FileDescriptor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FileDescriptor_ = $FileDescriptor$Type;
}}
declare module "packages/java/io/$FilterInputStream" {
import {$InputStream, $InputStream$Type} from "packages/java/io/$InputStream"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $FilterInputStream extends $InputStream {


public "read"(arg0: (byte)[], arg1: integer, arg2: integer): integer
public "read"(arg0: (byte)[]): integer
public "read"(): integer
public "close"(): void
public "mark"(arg0: integer): void
public "skip"(arg0: long): long
public "available"(): integer
public "markSupported"(): boolean
public "reset"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FilterInputStream$Type = ($FilterInputStream);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FilterInputStream_ = $FilterInputStream$Type;
}}
declare module "packages/java/io/$Writer" {
import {$Closeable, $Closeable$Type} from "packages/java/io/$Closeable"
import {$Appendable, $Appendable$Type} from "packages/java/lang/$Appendable"
import {$Flushable, $Flushable$Type} from "packages/java/io/$Flushable"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $Writer implements $Appendable, $Closeable, $Flushable {


public "append"(arg0: charseq, arg1: integer, arg2: integer): $Writer
public "append"(arg0: character): $Writer
public "flush"(): void
public "write"(arg0: string, arg1: integer, arg2: integer): void
public "write"(arg0: integer): void
public "write"(arg0: string): void
public "write"(arg0: (character)[], arg1: integer, arg2: integer): void
public "write"(arg0: (character)[]): void
public "close"(): void
public static "nullWriter"(): $Writer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Writer$Type = ($Writer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Writer_ = $Writer$Type;
}}
declare module "packages/java/io/$FileReader" {
import {$File, $File$Type} from "packages/java/io/$File"
import {$FileDescriptor, $FileDescriptor$Type} from "packages/java/io/$FileDescriptor"
import {$InputStreamReader, $InputStreamReader$Type} from "packages/java/io/$InputStreamReader"
import {$Charset, $Charset$Type} from "packages/java/nio/charset/$Charset"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $FileReader extends $InputStreamReader {

constructor(arg0: $File$Type, arg1: $Charset$Type)
constructor(arg0: string, arg1: $Charset$Type)
constructor(arg0: $FileDescriptor$Type)
constructor(arg0: $File$Type)
constructor(arg0: string)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FileReader$Type = ($FileReader);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FileReader_ = $FileReader$Type;
}}
declare module "packages/java/io/$RandomAccessFile" {
import {$DataInput, $DataInput$Type} from "packages/java/io/$DataInput"
import {$Closeable, $Closeable$Type} from "packages/java/io/$Closeable"
import {$File, $File$Type} from "packages/java/io/$File"
import {$FileChannel, $FileChannel$Type} from "packages/java/nio/channels/$FileChannel"
import {$FileDescriptor, $FileDescriptor$Type} from "packages/java/io/$FileDescriptor"
import {$DataOutput, $DataOutput$Type} from "packages/java/io/$DataOutput"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $RandomAccessFile implements $DataOutput, $DataInput, $Closeable {

constructor(arg0: $File$Type, arg1: string)
constructor(arg0: string, arg1: string)

public "length"(): long
public "write"(arg0: integer): void
public "write"(arg0: (byte)[], arg1: integer, arg2: integer): void
public "write"(arg0: (byte)[]): void
public "read"(arg0: (byte)[], arg1: integer, arg2: integer): integer
public "read"(arg0: (byte)[]): integer
public "read"(): integer
public "readLine"(): string
public "setLength"(arg0: long): void
public "close"(): void
public "writeInt"(arg0: integer): void
public "readInt"(): integer
public "writeUTF"(arg0: string): void
public "readUTF"(): string
public "getFD"(): $FileDescriptor
public "getChannel"(): $FileChannel
public "writeBytes"(arg0: string): void
public "writeChar"(arg0: integer): void
public "readChar"(): character
public "writeFloat"(arg0: float): void
public "readFloat"(): float
public "readFully"(arg0: (byte)[]): void
public "readFully"(arg0: (byte)[], arg1: integer, arg2: integer): void
public "skipBytes"(arg0: integer): integer
public "readBoolean"(): boolean
public "readByte"(): byte
public "readUnsignedByte"(): integer
public "readShort"(): short
public "readUnsignedShort"(): integer
public "readLong"(): long
public "readDouble"(): double
public "writeBoolean"(arg0: boolean): void
public "writeByte"(arg0: integer): void
public "writeShort"(arg0: integer): void
public "writeLong"(arg0: long): void
public "writeDouble"(arg0: double): void
public "writeChars"(arg0: string): void
public "getFilePointer"(): long
public "seek"(arg0: long): void
get "fD"(): $FileDescriptor
get "channel"(): $FileChannel
get "filePointer"(): long
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RandomAccessFile$Type = ($RandomAccessFile);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RandomAccessFile_ = $RandomAccessFile$Type;
}}
declare module "packages/java/io/$ObjectInput" {
import {$DataInput, $DataInput$Type} from "packages/java/io/$DataInput"
import {$AutoCloseable, $AutoCloseable$Type} from "packages/java/lang/$AutoCloseable"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export interface $ObjectInput extends $DataInput, $AutoCloseable {

 "readObject"(): any
 "read"(arg0: (byte)[], arg1: integer, arg2: integer): integer
 "read"(arg0: (byte)[]): integer
 "read"(): integer
 "close"(): void
 "skip"(arg0: long): long
 "available"(): integer
 "readLine"(): string
 "readInt"(): integer
 "readUTF"(): string
 "readChar"(): character
 "readFloat"(): float
 "readFully"(arg0: (byte)[]): void
 "readFully"(arg0: (byte)[], arg1: integer, arg2: integer): void
 "skipBytes"(arg0: integer): integer
 "readBoolean"(): boolean
 "readByte"(): byte
 "readUnsignedByte"(): integer
 "readShort"(): short
 "readUnsignedShort"(): integer
 "readLong"(): long
 "readDouble"(): double
}

export namespace $ObjectInput {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ObjectInput$Type = ($ObjectInput);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ObjectInput_ = $ObjectInput$Type;
}}
declare module "packages/java/io/$BufferedReader" {
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$Reader, $Reader$Type} from "packages/java/io/$Reader"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $BufferedReader extends $Reader {

constructor(arg0: $Reader$Type, arg1: integer)
constructor(arg0: $Reader$Type)

public "lines"(): $Stream<(string)>
public "read"(): integer
public "read"(arg0: (character)[], arg1: integer, arg2: integer): integer
public "readLine"(): string
public "close"(): void
public "mark"(arg0: integer): void
public "skip"(arg0: long): long
public "markSupported"(): boolean
public "reset"(): void
public "ready"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BufferedReader$Type = ($BufferedReader);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BufferedReader_ = $BufferedReader$Type;
}}
declare module "packages/java/io/$BufferedInputStream" {
import {$InputStream, $InputStream$Type} from "packages/java/io/$InputStream"
import {$FilterInputStream, $FilterInputStream$Type} from "packages/java/io/$FilterInputStream"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $BufferedInputStream extends $FilterInputStream {

constructor(arg0: $InputStream$Type, arg1: integer)
constructor(arg0: $InputStream$Type)

public "read"(arg0: (byte)[], arg1: integer, arg2: integer): integer
public "read"(): integer
public "close"(): void
public "mark"(arg0: integer): void
public "skip"(arg0: long): long
public "available"(): integer
public "markSupported"(): boolean
public "reset"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BufferedInputStream$Type = ($BufferedInputStream);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BufferedInputStream_ = $BufferedInputStream$Type;
}}
declare module "packages/java/io/$FileNotFoundException" {
import {$IOException, $IOException$Type} from "packages/java/io/$IOException"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $FileNotFoundException extends $IOException {

constructor()
constructor(arg0: string)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FileNotFoundException$Type = ($FileNotFoundException);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FileNotFoundException_ = $FileNotFoundException$Type;
}}
declare module "packages/java/io/$OutputStream" {
import {$Closeable, $Closeable$Type} from "packages/java/io/$Closeable"
import {$Flushable, $Flushable$Type} from "packages/java/io/$Flushable"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $OutputStream implements $Closeable, $Flushable {

constructor()

public "flush"(): void
public "write"(arg0: (byte)[]): void
public "write"(arg0: (byte)[], arg1: integer, arg2: integer): void
public "write"(arg0: integer): void
public "close"(): void
public static "nullOutputStream"(): $OutputStream
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $OutputStream$Type = ($OutputStream);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $OutputStream_ = $OutputStream$Type;
}}
declare module "packages/java/io/$UnsupportedEncodingException" {
import {$IOException, $IOException$Type} from "packages/java/io/$IOException"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $UnsupportedEncodingException extends $IOException {

constructor()
constructor(arg0: string)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $UnsupportedEncodingException$Type = ($UnsupportedEncodingException);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $UnsupportedEncodingException_ = $UnsupportedEncodingException$Type;
}}
declare module "packages/java/io/$InputStream" {
import {$Closeable, $Closeable$Type} from "packages/java/io/$Closeable"
import {$OutputStream, $OutputStream$Type} from "packages/java/io/$OutputStream"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $InputStream implements $Closeable {

constructor()

public "read"(arg0: (byte)[], arg1: integer, arg2: integer): integer
public "read"(arg0: (byte)[]): integer
public "read"(): integer
public "close"(): void
public "mark"(arg0: integer): void
public "readAllBytes"(): (byte)[]
public "readNBytes"(arg0: integer): (byte)[]
public "readNBytes"(arg0: (byte)[], arg1: integer, arg2: integer): integer
public "transferTo"(arg0: $OutputStream$Type): long
public "skip"(arg0: long): long
public "available"(): integer
public "markSupported"(): boolean
public "reset"(): void
public static "nullInputStream"(): $InputStream
public "skipNBytes"(arg0: long): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InputStream$Type = ($InputStream);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InputStream_ = $InputStream$Type;
}}
declare module "packages/java/io/$ObjectInputValidation" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export interface $ObjectInputValidation {

 "validateObject"(): void

(): void
}

export namespace $ObjectInputValidation {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ObjectInputValidation$Type = ($ObjectInputValidation);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ObjectInputValidation_ = $ObjectInputValidation$Type;
}}
declare module "packages/java/io/$BufferedWriter" {
import {$Writer, $Writer$Type} from "packages/java/io/$Writer"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $BufferedWriter extends $Writer {

constructor(arg0: $Writer$Type)
constructor(arg0: $Writer$Type, arg1: integer)

public "flush"(): void
public "write"(arg0: (character)[], arg1: integer, arg2: integer): void
public "write"(arg0: string, arg1: integer, arg2: integer): void
public "write"(arg0: integer): void
public "newLine"(): void
public "close"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BufferedWriter$Type = ($BufferedWriter);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BufferedWriter_ = $BufferedWriter$Type;
}}
declare module "packages/java/io/$InputStreamReader" {
import {$InputStream, $InputStream$Type} from "packages/java/io/$InputStream"
import {$CharsetDecoder, $CharsetDecoder$Type} from "packages/java/nio/charset/$CharsetDecoder"
import {$CharBuffer, $CharBuffer$Type} from "packages/java/nio/$CharBuffer"
import {$Charset, $Charset$Type} from "packages/java/nio/charset/$Charset"
import {$Reader, $Reader$Type} from "packages/java/io/$Reader"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $InputStreamReader extends $Reader {

constructor(arg0: $InputStream$Type, arg1: $CharsetDecoder$Type)
constructor(arg0: $InputStream$Type, arg1: $Charset$Type)
constructor(arg0: $InputStream$Type, arg1: string)
constructor(arg0: $InputStream$Type)

public "read"(): integer
public "read"(arg0: (character)[], arg1: integer, arg2: integer): integer
public "read"(arg0: $CharBuffer$Type): integer
public "close"(): void
public "getEncoding"(): string
public "ready"(): boolean
get "encoding"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InputStreamReader$Type = ($InputStreamReader);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InputStreamReader_ = $InputStreamReader$Type;
}}
declare module "packages/java/io/$DataOutput" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export interface $DataOutput {

 "write"(arg0: (byte)[], arg1: integer, arg2: integer): void
 "write"(arg0: (byte)[]): void
 "write"(arg0: integer): void
 "writeInt"(arg0: integer): void
 "writeUTF"(arg0: string): void
 "writeBytes"(arg0: string): void
 "writeChar"(arg0: integer): void
 "writeFloat"(arg0: float): void
 "writeBoolean"(arg0: boolean): void
 "writeByte"(arg0: integer): void
 "writeShort"(arg0: integer): void
 "writeLong"(arg0: long): void
 "writeDouble"(arg0: double): void
 "writeChars"(arg0: string): void
}

export namespace $DataOutput {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DataOutput$Type = ($DataOutput);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DataOutput_ = $DataOutput$Type;
}}
declare module "packages/java/io/$SerializablePermission" {
import {$BasicPermission, $BasicPermission$Type} from "packages/java/security/$BasicPermission"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $SerializablePermission extends $BasicPermission {

constructor(arg0: string)
constructor(arg0: string, arg1: string)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SerializablePermission$Type = ($SerializablePermission);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SerializablePermission_ = $SerializablePermission$Type;
}}
declare module "packages/java/io/$ObjectInputFilter$FilterInfo" {
import {$Class, $Class$Type} from "packages/java/lang/$Class"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export interface $ObjectInputFilter$FilterInfo {

 "depth"(): long
 "arrayLength"(): long
 "references"(): long
 "serialClass"(): $Class<(any)>
 "streamBytes"(): long
}

export namespace $ObjectInputFilter$FilterInfo {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ObjectInputFilter$FilterInfo$Type = ($ObjectInputFilter$FilterInfo);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ObjectInputFilter$FilterInfo_ = $ObjectInputFilter$FilterInfo$Type;
}}
declare module "packages/java/io/$ObjectStreamConstants" {
import {$SerializablePermission, $SerializablePermission$Type} from "packages/java/io/$SerializablePermission"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export interface $ObjectStreamConstants {

}

export namespace $ObjectStreamConstants {
const STREAM_MAGIC: short
const STREAM_VERSION: short
const TC_BASE: byte
const TC_NULL: byte
const TC_REFERENCE: byte
const TC_CLASSDESC: byte
const TC_OBJECT: byte
const TC_STRING: byte
const TC_ARRAY: byte
const TC_CLASS: byte
const TC_BLOCKDATA: byte
const TC_ENDBLOCKDATA: byte
const TC_RESET: byte
const TC_BLOCKDATALONG: byte
const TC_EXCEPTION: byte
const TC_LONGSTRING: byte
const TC_PROXYCLASSDESC: byte
const TC_ENUM: byte
const TC_MAX: byte
const baseWireHandle: integer
const SC_WRITE_METHOD: byte
const SC_BLOCK_DATA: byte
const SC_SERIALIZABLE: byte
const SC_EXTERNALIZABLE: byte
const SC_ENUM: byte
const SUBSTITUTION_PERMISSION: $SerializablePermission
const SUBCLASS_IMPLEMENTATION_PERMISSION: $SerializablePermission
const SERIAL_FILTER_PERMISSION: $SerializablePermission
const PROTOCOL_VERSION_1: integer
const PROTOCOL_VERSION_2: integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ObjectStreamConstants$Type = ($ObjectStreamConstants);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ObjectStreamConstants_ = $ObjectStreamConstants$Type;
}}
declare module "packages/java/io/$IOException" {
import {$Throwable, $Throwable$Type} from "packages/java/lang/$Throwable"
import {$Exception, $Exception$Type} from "packages/java/lang/$Exception"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $IOException extends $Exception {

constructor(arg0: $Throwable$Type)
constructor(arg0: string, arg1: $Throwable$Type)
constructor(arg0: string)
constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IOException$Type = ($IOException);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IOException_ = $IOException$Type;
}}
declare module "packages/java/io/$ObjectStreamField" {
import {$Comparable, $Comparable$Type} from "packages/java/lang/$Comparable"
import {$Class, $Class$Type} from "packages/java/lang/$Class"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $ObjectStreamField implements $Comparable<(any)> {

constructor(arg0: string, arg1: $Class$Type<(any)>)
constructor(arg0: string, arg1: $Class$Type<(any)>, arg2: boolean)

public "getName"(): string
public "toString"(): string
public "isPrimitive"(): boolean
public "compareTo"(arg0: any): integer
public "getType"(): $Class<(any)>
public "getTypeCode"(): character
public "getTypeString"(): string
public "getOffset"(): integer
public "isUnshared"(): boolean
get "name"(): string
get "primitive"(): boolean
get "type"(): $Class<(any)>
get "typeCode"(): character
get "typeString"(): string
get "offset"(): integer
get "unshared"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ObjectStreamField$Type = ($ObjectStreamField);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ObjectStreamField_ = $ObjectStreamField$Type;
}}
declare module "packages/java/io/$ObjectInputFilter" {
import {$ObjectInputFilter$Status, $ObjectInputFilter$Status$Type} from "packages/java/io/$ObjectInputFilter$Status"
import {$ObjectInputFilter$FilterInfo, $ObjectInputFilter$FilterInfo$Type} from "packages/java/io/$ObjectInputFilter$FilterInfo"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$Class, $Class$Type} from "packages/java/lang/$Class"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export interface $ObjectInputFilter {

 "checkInput"(arg0: $ObjectInputFilter$FilterInfo$Type): $ObjectInputFilter$Status

(arg0: $ObjectInputFilter$Type, arg1: $ObjectInputFilter$Type): $ObjectInputFilter
}

export namespace $ObjectInputFilter {
function merge(arg0: $ObjectInputFilter$Type, arg1: $ObjectInputFilter$Type): $ObjectInputFilter
function allowFilter(arg0: $Predicate$Type<($Class$Type<(any)>)>, arg1: $ObjectInputFilter$Status$Type): $ObjectInputFilter
function rejectFilter(arg0: $Predicate$Type<($Class$Type<(any)>)>, arg1: $ObjectInputFilter$Status$Type): $ObjectInputFilter
function rejectUndecidedClass(arg0: $ObjectInputFilter$Type): $ObjectInputFilter
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ObjectInputFilter$Type = ($ObjectInputFilter);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ObjectInputFilter_ = $ObjectInputFilter$Type;
}}
declare module "packages/java/io/$Serializable" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $Serializable {

}

export namespace $Serializable {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Serializable$Type = ($Serializable);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Serializable_ = $Serializable$Type;
}}
declare module "packages/java/io/$File" {
import {$FileFilter, $FileFilter$Type} from "packages/java/io/$FileFilter"
import {$Comparable, $Comparable$Type} from "packages/java/lang/$Comparable"
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$Serializable, $Serializable$Type} from "packages/java/io/$Serializable"
import {$FilenameFilter, $FilenameFilter$Type} from "packages/java/io/$FilenameFilter"
import {$URI, $URI$Type} from "packages/java/net/$URI"
import {$URL, $URL$Type} from "packages/java/net/$URL"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $File implements $Serializable, $Comparable<($File)> {
static readonly "separatorChar": character
static readonly "separator": string
static readonly "pathSeparatorChar": character
static readonly "pathSeparator": string

constructor(arg0: string)
constructor(arg0: string, arg1: string)
constructor(arg0: $URI$Type)
constructor(arg0: $File$Type, arg1: string)

public "getName"(): string
public "equals"(arg0: any): boolean
public "length"(): long
public "toString"(): string
public "hashCode"(): integer
public "isHidden"(): boolean
public "compareTo"(arg0: $File$Type): integer
public "list"(): (string)[]
public "list"(arg0: $FilenameFilter$Type): (string)[]
public "isAbsolute"(): boolean
public "getParent"(): string
public "delete"(): boolean
public "setReadOnly"(): boolean
public "canRead"(): boolean
public "getPath"(): string
public "toURI"(): $URI
/**
 * 
 * @deprecated
 */
public "toURL"(): $URL
public "exists"(): boolean
public "createNewFile"(): boolean
public "renameTo"(arg0: $File$Type): boolean
public "getAbsolutePath"(): string
public "getCanonicalPath"(): string
public "isDirectory"(): boolean
public "getAbsoluteFile"(): $File
public "mkdir"(): boolean
public "getCanonicalFile"(): $File
public "getParentFile"(): $File
public "mkdirs"(): boolean
public "setWritable"(arg0: boolean): boolean
public "setWritable"(arg0: boolean, arg1: boolean): boolean
public "setReadable"(arg0: boolean): boolean
public "setReadable"(arg0: boolean, arg1: boolean): boolean
public "setExecutable"(arg0: boolean, arg1: boolean): boolean
public "setExecutable"(arg0: boolean): boolean
public static "listRoots"(): ($File)[]
public static "createTempFile"(arg0: string, arg1: string, arg2: $File$Type): $File
public static "createTempFile"(arg0: string, arg1: string): $File
public "canWrite"(): boolean
public "isFile"(): boolean
public "lastModified"(): long
public "deleteOnExit"(): void
public "listFiles"(arg0: $FilenameFilter$Type): ($File)[]
public "listFiles"(arg0: $FileFilter$Type): ($File)[]
public "listFiles"(): ($File)[]
public "setLastModified"(arg0: long): boolean
public "canExecute"(): boolean
public "getTotalSpace"(): long
public "getFreeSpace"(): long
public "getUsableSpace"(): long
public "toPath"(): $Path
get "name"(): string
get "hidden"(): boolean
get "absolute"(): boolean
get "parent"(): string
get "path"(): string
get "absolutePath"(): string
get "canonicalPath"(): string
get "directory"(): boolean
get "absoluteFile"(): $File
get "canonicalFile"(): $File
get "parentFile"(): $File
set "writable"(value: boolean)
set "readable"(value: boolean)
set "executable"(value: boolean)
get "file"(): boolean
get "totalSpace"(): long
get "freeSpace"(): long
get "usableSpace"(): long
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $File$Type = ($Path$Type) | ($File);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $File_ = $File$Type;
}}
declare module "packages/java/io/$DataOutputStream" {
import {$OutputStream, $OutputStream$Type} from "packages/java/io/$OutputStream"
import {$FilterOutputStream, $FilterOutputStream$Type} from "packages/java/io/$FilterOutputStream"
import {$DataOutput, $DataOutput$Type} from "packages/java/io/$DataOutput"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $DataOutputStream extends $FilterOutputStream implements $DataOutput {

constructor(arg0: $OutputStream$Type)

public "flush"(): void
public "size"(): integer
public "write"(arg0: (byte)[], arg1: integer, arg2: integer): void
public "write"(arg0: integer): void
public "writeInt"(arg0: integer): void
public "writeUTF"(arg0: string): void
public "writeBytes"(arg0: string): void
public "writeChar"(arg0: integer): void
public "writeFloat"(arg0: float): void
public "writeBoolean"(arg0: boolean): void
public "writeByte"(arg0: integer): void
public "writeShort"(arg0: integer): void
public "writeLong"(arg0: long): void
public "writeDouble"(arg0: double): void
public "writeChars"(arg0: string): void
public "write"(arg0: (byte)[]): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DataOutputStream$Type = ($DataOutputStream);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DataOutputStream_ = $DataOutputStream$Type;
}}
declare module "packages/java/io/$ObjectStreamClass" {
import {$ObjectStreamField, $ObjectStreamField$Type} from "packages/java/io/$ObjectStreamField"
import {$Serializable, $Serializable$Type} from "packages/java/io/$Serializable"
import {$Class, $Class$Type} from "packages/java/lang/$Class"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $ObjectStreamClass implements $Serializable {
static readonly "NO_FIELDS": ($ObjectStreamField)[]


public "getName"(): string
public "toString"(): string
public static "lookup"(arg0: $Class$Type<(any)>): $ObjectStreamClass
public "getFields"(): ($ObjectStreamField)[]
public "getField"(arg0: string): $ObjectStreamField
public "getSerialVersionUID"(): long
public static "lookupAny"(arg0: $Class$Type<(any)>): $ObjectStreamClass
public "forClass"(): $Class<(any)>
get "name"(): string
get "fields"(): ($ObjectStreamField)[]
get "serialVersionUID"(): long
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ObjectStreamClass$Type = ($ObjectStreamClass);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ObjectStreamClass_ = $ObjectStreamClass$Type;
}}
declare module "packages/java/io/$EOFException" {
import {$IOException, $IOException$Type} from "packages/java/io/$IOException"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $EOFException extends $IOException {

constructor()
constructor(arg0: string)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EOFException$Type = ($EOFException);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EOFException_ = $EOFException$Type;
}}
declare module "packages/java/io/$DataInputStream" {
import {$DataInput, $DataInput$Type} from "packages/java/io/$DataInput"
import {$InputStream, $InputStream$Type} from "packages/java/io/$InputStream"
import {$FilterInputStream, $FilterInputStream$Type} from "packages/java/io/$FilterInputStream"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $DataInputStream extends $FilterInputStream implements $DataInput {

constructor(arg0: $InputStream$Type)

public "read"(arg0: (byte)[], arg1: integer, arg2: integer): integer
public "read"(arg0: (byte)[]): integer
/**
 * 
 * @deprecated
 */
public "readLine"(): string
public "readInt"(): integer
public static "readUTF"(arg0: $DataInput$Type): string
public "readUTF"(): string
public "readChar"(): character
public "readFloat"(): float
public "readFully"(arg0: (byte)[], arg1: integer, arg2: integer): void
public "readFully"(arg0: (byte)[]): void
public "skipBytes"(arg0: integer): integer
public "readBoolean"(): boolean
public "readByte"(): byte
public "readUnsignedByte"(): integer
public "readShort"(): short
public "readUnsignedShort"(): integer
public "readLong"(): long
public "readDouble"(): double
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DataInputStream$Type = ($DataInputStream);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DataInputStream_ = $DataInputStream$Type;
}}
declare module "packages/java/io/$Reader" {
import {$Closeable, $Closeable$Type} from "packages/java/io/$Closeable"
import {$CharBuffer, $CharBuffer$Type} from "packages/java/nio/$CharBuffer"
import {$Writer, $Writer$Type} from "packages/java/io/$Writer"
import {$Readable, $Readable$Type} from "packages/java/lang/$Readable"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $Reader implements $Readable, $Closeable {


public "read"(arg0: (character)[]): integer
public "read"(arg0: (character)[], arg1: integer, arg2: integer): integer
public "read"(arg0: $CharBuffer$Type): integer
public "read"(): integer
public "close"(): void
public "mark"(arg0: integer): void
public "transferTo"(arg0: $Writer$Type): long
public "skip"(arg0: long): long
public "markSupported"(): boolean
public "reset"(): void
public static "nullReader"(): $Reader
public "ready"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Reader$Type = ($Reader);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Reader_ = $Reader$Type;
}}
declare module "packages/java/io/$ObjectInputFilter$Status" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

/**
 * This class is not allowed By KubeJS!
 * You should not load the class, or KubeJS will throw an error.
 * Loading the class using require() will not throw an error, but the class will be undefined.
 */
export class $ObjectInputFilter$Status extends $Enum<($ObjectInputFilter$Status)> {
static readonly "UNDECIDED": $ObjectInputFilter$Status
static readonly "ALLOWED": $ObjectInputFilter$Status
static readonly "REJECTED": $ObjectInputFilter$Status


public static "values"(): ($ObjectInputFilter$Status)[]
public static "valueOf"(arg0: string): $ObjectInputFilter$Status
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ObjectInputFilter$Status$Type = (("rejected") | ("allowed") | ("undecided")) | ($ObjectInputFilter$Status);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ObjectInputFilter$Status_ = $ObjectInputFilter$Status$Type;
}}

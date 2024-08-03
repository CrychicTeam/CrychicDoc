declare module "packages/java/math/$MathContext" {
import {$Serializable, $Serializable$Type} from "packages/java/io/$Serializable"
import {$RoundingMode, $RoundingMode$Type} from "packages/java/math/$RoundingMode"

export class $MathContext implements $Serializable {
static readonly "UNLIMITED": $MathContext
static readonly "DECIMAL32": $MathContext
static readonly "DECIMAL64": $MathContext
static readonly "DECIMAL128": $MathContext

constructor(arg0: string)
constructor(arg0: integer, arg1: $RoundingMode$Type)
constructor(arg0: integer)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "getPrecision"(): integer
public "getRoundingMode"(): $RoundingMode
get "precision"(): integer
get "roundingMode"(): $RoundingMode
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MathContext$Type = ($MathContext);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MathContext_ = $MathContext$Type;
}}
declare module "packages/java/math/$RoundingMode" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $RoundingMode extends $Enum<($RoundingMode)> {
static readonly "UP": $RoundingMode
static readonly "DOWN": $RoundingMode
static readonly "CEILING": $RoundingMode
static readonly "FLOOR": $RoundingMode
static readonly "HALF_UP": $RoundingMode
static readonly "HALF_DOWN": $RoundingMode
static readonly "HALF_EVEN": $RoundingMode
static readonly "UNNECESSARY": $RoundingMode


public static "values"(): ($RoundingMode)[]
public static "valueOf"(arg0: string): $RoundingMode
public static "valueOf"(arg0: integer): $RoundingMode
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RoundingMode$Type = (("ceiling") | ("half_down") | ("half_up") | ("unnecessary") | ("up") | ("floor") | ("down") | ("half_even")) | ($RoundingMode);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RoundingMode_ = $RoundingMode$Type;
}}
declare module "packages/java/math/$BigDecimal" {
import {$Comparable, $Comparable$Type} from "packages/java/lang/$Comparable"
import {$BigInteger, $BigInteger$Type} from "packages/java/math/$BigInteger"
import {$RoundingMode, $RoundingMode$Type} from "packages/java/math/$RoundingMode"
import {$MathContext, $MathContext$Type} from "packages/java/math/$MathContext"

export class $BigDecimal extends number implements $Comparable<($BigDecimal)> {
static readonly "ZERO": $BigDecimal
static readonly "ONE": $BigDecimal
static readonly "TEN": $BigDecimal
/**
 * 
 * @deprecated
 */
static readonly "ROUND_UP": integer
/**
 * 
 * @deprecated
 */
static readonly "ROUND_DOWN": integer
/**
 * 
 * @deprecated
 */
static readonly "ROUND_CEILING": integer
/**
 * 
 * @deprecated
 */
static readonly "ROUND_FLOOR": integer
/**
 * 
 * @deprecated
 */
static readonly "ROUND_HALF_UP": integer
/**
 * 
 * @deprecated
 */
static readonly "ROUND_HALF_DOWN": integer
/**
 * 
 * @deprecated
 */
static readonly "ROUND_HALF_EVEN": integer
/**
 * 
 * @deprecated
 */
static readonly "ROUND_UNNECESSARY": integer

constructor(arg0: $BigInteger$Type, arg1: $MathContext$Type)
constructor(arg0: $BigInteger$Type)
constructor(arg0: long)
constructor(arg0: double, arg1: $MathContext$Type)
constructor(arg0: double)
constructor(arg0: integer, arg1: $MathContext$Type)
constructor(arg0: integer)
constructor(arg0: $BigInteger$Type, arg1: integer, arg2: $MathContext$Type)
constructor(arg0: $BigInteger$Type, arg1: integer)
constructor(arg0: long, arg1: $MathContext$Type)
constructor(arg0: (character)[], arg1: integer, arg2: integer, arg3: $MathContext$Type)
constructor(arg0: (character)[], arg1: integer, arg2: integer)
constructor(arg0: string, arg1: $MathContext$Type)
constructor(arg0: string)
constructor(arg0: (character)[], arg1: $MathContext$Type)
constructor(arg0: (character)[])

public "add"(arg0: $BigDecimal$Type): $BigDecimal
public "add"(arg0: $BigDecimal$Type, arg1: $MathContext$Type): $BigDecimal
public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "abs"(arg0: $MathContext$Type): $BigDecimal
public "abs"(): $BigDecimal
public "sqrt"(arg0: $MathContext$Type): $BigDecimal
public "pow"(arg0: integer, arg1: $MathContext$Type): $BigDecimal
public "pow"(arg0: integer): $BigDecimal
public "min"(arg0: $BigDecimal$Type): $BigDecimal
public "max"(arg0: $BigDecimal$Type): $BigDecimal
public "signum"(): integer
public "compareTo"(arg0: $BigDecimal$Type): integer
public "intValue"(): integer
public "longValue"(): long
public "floatValue"(): float
public "doubleValue"(): double
public static "valueOf"(arg0: double): $BigDecimal
public static "valueOf"(arg0: long, arg1: integer): $BigDecimal
public static "valueOf"(arg0: long): $BigDecimal
public "scale"(): integer
public "multiply"(arg0: $BigDecimal$Type): $BigDecimal
public "multiply"(arg0: $BigDecimal$Type, arg1: $MathContext$Type): $BigDecimal
public "round"(arg0: $MathContext$Type): $BigDecimal
public "ulp"(): $BigDecimal
public "precision"(): integer
public "setScale"(arg0: integer): $BigDecimal
/**
 * 
 * @deprecated
 */
public "setScale"(arg0: integer, arg1: integer): $BigDecimal
public "setScale"(arg0: integer, arg1: $RoundingMode$Type): $BigDecimal
public "negate"(arg0: $MathContext$Type): $BigDecimal
public "negate"(): $BigDecimal
public "subtract"(arg0: $BigDecimal$Type): $BigDecimal
public "subtract"(arg0: $BigDecimal$Type, arg1: $MathContext$Type): $BigDecimal
public "divide"(arg0: $BigDecimal$Type, arg1: $RoundingMode$Type): $BigDecimal
public "divide"(arg0: $BigDecimal$Type): $BigDecimal
public "divide"(arg0: $BigDecimal$Type, arg1: $MathContext$Type): $BigDecimal
/**
 * 
 * @deprecated
 */
public "divide"(arg0: $BigDecimal$Type, arg1: integer): $BigDecimal
/**
 * 
 * @deprecated
 */
public "divide"(arg0: $BigDecimal$Type, arg1: integer, arg2: integer): $BigDecimal
public "divide"(arg0: $BigDecimal$Type, arg1: integer, arg2: $RoundingMode$Type): $BigDecimal
public "divideToIntegralValue"(arg0: $BigDecimal$Type): $BigDecimal
public "divideToIntegralValue"(arg0: $BigDecimal$Type, arg1: $MathContext$Type): $BigDecimal
public "divideAndRemainder"(arg0: $BigDecimal$Type): ($BigDecimal)[]
public "divideAndRemainder"(arg0: $BigDecimal$Type, arg1: $MathContext$Type): ($BigDecimal)[]
public "stripTrailingZeros"(): $BigDecimal
public "scaleByPowerOfTen"(arg0: integer): $BigDecimal
public "unscaledValue"(): $BigInteger
public "plus"(): $BigDecimal
public "plus"(arg0: $MathContext$Type): $BigDecimal
public "toBigInteger"(): $BigInteger
public "longValueExact"(): long
public "remainder"(arg0: $BigDecimal$Type): $BigDecimal
public "remainder"(arg0: $BigDecimal$Type, arg1: $MathContext$Type): $BigDecimal
public "movePointLeft"(arg0: integer): $BigDecimal
public "movePointRight"(arg0: integer): $BigDecimal
public "toEngineeringString"(): string
public "toPlainString"(): string
public "toBigIntegerExact"(): $BigInteger
public "intValueExact"(): integer
public "shortValueExact"(): short
public "byteValueExact"(): byte
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BigDecimal$Type = ($BigDecimal);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BigDecimal_ = $BigDecimal$Type;
}}
declare module "packages/java/math/$BigInteger" {
import {$Comparable, $Comparable$Type} from "packages/java/lang/$Comparable"
import {$Random, $Random$Type} from "packages/java/util/$Random"

export class $BigInteger extends number implements $Comparable<($BigInteger)> {
static readonly "ZERO": $BigInteger
static readonly "ONE": $BigInteger
static readonly "TWO": $BigInteger
static readonly "TEN": $BigInteger

constructor(arg0: integer, arg1: $Random$Type)
constructor(arg0: string)
constructor(arg0: integer, arg1: integer, arg2: $Random$Type)
constructor(arg0: integer, arg1: (byte)[], arg2: integer, arg3: integer)
constructor(arg0: (byte)[])
constructor(arg0: (byte)[], arg1: integer, arg2: integer)
constructor(arg0: string, arg1: integer)
constructor(arg0: integer, arg1: (byte)[])

public "add"(arg0: $BigInteger$Type): $BigInteger
public "bitCount"(): integer
public "equals"(arg0: any): boolean
public "toString"(): string
public "toString"(arg0: integer): string
public "hashCode"(): integer
public "abs"(): $BigInteger
public "sqrt"(): $BigInteger
public "pow"(arg0: integer): $BigInteger
public "min"(arg0: $BigInteger$Type): $BigInteger
public "max"(arg0: $BigInteger$Type): $BigInteger
public "signum"(): integer
public "compareTo"(arg0: $BigInteger$Type): integer
public "intValue"(): integer
public "longValue"(): long
public "floatValue"(): float
public "doubleValue"(): double
public static "valueOf"(arg0: long): $BigInteger
public "toByteArray"(): (byte)[]
public "mod"(arg0: $BigInteger$Type): $BigInteger
public "setBit"(arg0: integer): $BigInteger
public "shiftLeft"(arg0: integer): $BigInteger
public "multiply"(arg0: $BigInteger$Type): $BigInteger
public "or"(arg0: $BigInteger$Type): $BigInteger
public "negate"(): $BigInteger
public "subtract"(arg0: $BigInteger$Type): $BigInteger
public "divide"(arg0: $BigInteger$Type): $BigInteger
public "divideAndRemainder"(arg0: $BigInteger$Type): ($BigInteger)[]
public "longValueExact"(): long
public "bitLength"(): integer
public "testBit"(arg0: integer): boolean
public "remainder"(arg0: $BigInteger$Type): $BigInteger
public "intValueExact"(): integer
public "shortValueExact"(): short
public "byteValueExact"(): byte
public "shiftRight"(arg0: integer): $BigInteger
public "getLowestSetBit"(): integer
public "modPow"(arg0: $BigInteger$Type, arg1: $BigInteger$Type): $BigInteger
public "modInverse"(arg0: $BigInteger$Type): $BigInteger
public static "probablePrime"(arg0: integer, arg1: $Random$Type): $BigInteger
public "nextProbablePrime"(): $BigInteger
public "sqrtAndRemainder"(): ($BigInteger)[]
public "gcd"(arg0: $BigInteger$Type): $BigInteger
public "and"(arg0: $BigInteger$Type): $BigInteger
public "xor"(arg0: $BigInteger$Type): $BigInteger
public "not"(): $BigInteger
public "andNot"(arg0: $BigInteger$Type): $BigInteger
public "clearBit"(arg0: integer): $BigInteger
public "flipBit"(arg0: integer): $BigInteger
public "isProbablePrime"(arg0: integer): boolean
set "bit"(value: integer)
get "lowestSetBit"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BigInteger$Type = ($BigInteger);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BigInteger_ = $BigInteger$Type;
}}

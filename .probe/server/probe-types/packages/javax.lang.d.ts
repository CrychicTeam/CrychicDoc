declare module "packages/javax/lang/model/type/$TypeVisitor" {
import {$UnionType, $UnionType$Type} from "packages/javax/lang/model/type/$UnionType"
import {$NullType, $NullType$Type} from "packages/javax/lang/model/type/$NullType"
import {$ExecutableType, $ExecutableType$Type} from "packages/javax/lang/model/type/$ExecutableType"
import {$TypeMirror, $TypeMirror$Type} from "packages/javax/lang/model/type/$TypeMirror"
import {$NoType, $NoType$Type} from "packages/javax/lang/model/type/$NoType"
import {$IntersectionType, $IntersectionType$Type} from "packages/javax/lang/model/type/$IntersectionType"
import {$ArrayType, $ArrayType$Type} from "packages/javax/lang/model/type/$ArrayType"
import {$DeclaredType, $DeclaredType$Type} from "packages/javax/lang/model/type/$DeclaredType"
import {$PrimitiveType, $PrimitiveType$Type} from "packages/javax/lang/model/type/$PrimitiveType"
import {$ErrorType, $ErrorType$Type} from "packages/javax/lang/model/type/$ErrorType"
import {$TypeVariable, $TypeVariable$Type} from "packages/javax/lang/model/type/$TypeVariable"
import {$WildcardType, $WildcardType$Type} from "packages/javax/lang/model/type/$WildcardType"

export interface $TypeVisitor<R, P> {

 "visit"(arg0: $TypeMirror$Type, arg1: P): R
 "visit"(arg0: $TypeMirror$Type): R
 "visitArray"(arg0: $ArrayType$Type, arg1: P): R
 "visitTypeVariable"(arg0: $TypeVariable$Type, arg1: P): R
 "visitWildcard"(arg0: $WildcardType$Type, arg1: P): R
 "visitIntersection"(arg0: $IntersectionType$Type, arg1: P): R
 "visitPrimitive"(arg0: $PrimitiveType$Type, arg1: P): R
 "visitNull"(arg0: $NullType$Type, arg1: P): R
 "visitDeclared"(arg0: $DeclaredType$Type, arg1: P): R
 "visitError"(arg0: $ErrorType$Type, arg1: P): R
 "visitExecutable"(arg0: $ExecutableType$Type, arg1: P): R
 "visitNoType"(arg0: $NoType$Type, arg1: P): R
 "visitUnknown"(arg0: $TypeMirror$Type, arg1: P): R
 "visitUnion"(arg0: $UnionType$Type, arg1: P): R
}

export namespace $TypeVisitor {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TypeVisitor$Type<R, P> = ($TypeVisitor<(R), (P)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TypeVisitor_<R, P> = $TypeVisitor$Type<(R), (P)>;
}}
declare module "packages/javax/lang/model/type/$UnionType" {
import {$Annotation, $Annotation$Type} from "packages/java/lang/annotation/$Annotation"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$TypeMirror, $TypeMirror$Type} from "packages/javax/lang/model/type/$TypeMirror"
import {$TypeVisitor, $TypeVisitor$Type} from "packages/javax/lang/model/type/$TypeVisitor"
import {$TypeKind, $TypeKind$Type} from "packages/javax/lang/model/type/$TypeKind"

export interface $UnionType extends $TypeMirror {

 "getAlternatives"(): $List<(any)>
 "equals"(arg0: any): boolean
 "toString"(): string
 "hashCode"(): integer
 "getAnnotation"<A extends $Annotation>(arg0: $Class$Type<(A)>): A
 "getAnnotationsByType"<A extends $Annotation>(arg0: $Class$Type<(A)>): (A)[]
 "accept"<R, P>(arg0: $TypeVisitor$Type<(R), (P)>, arg1: P): R
 "getKind"(): $TypeKind
 "getAnnotationMirrors"(): $List<(any)>
}

export namespace $UnionType {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $UnionType$Type = ($UnionType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $UnionType_ = $UnionType$Type;
}}
declare module "packages/javax/lang/model/element/$ModuleElement$DirectiveVisitor" {
import {$ModuleElement$ExportsDirective, $ModuleElement$ExportsDirective$Type} from "packages/javax/lang/model/element/$ModuleElement$ExportsDirective"
import {$ModuleElement$Directive, $ModuleElement$Directive$Type} from "packages/javax/lang/model/element/$ModuleElement$Directive"
import {$ModuleElement$ProvidesDirective, $ModuleElement$ProvidesDirective$Type} from "packages/javax/lang/model/element/$ModuleElement$ProvidesDirective"
import {$ModuleElement$RequiresDirective, $ModuleElement$RequiresDirective$Type} from "packages/javax/lang/model/element/$ModuleElement$RequiresDirective"
import {$ModuleElement$UsesDirective, $ModuleElement$UsesDirective$Type} from "packages/javax/lang/model/element/$ModuleElement$UsesDirective"
import {$ModuleElement$OpensDirective, $ModuleElement$OpensDirective$Type} from "packages/javax/lang/model/element/$ModuleElement$OpensDirective"

export interface $ModuleElement$DirectiveVisitor<R, P> {

 "visit"(arg0: $ModuleElement$Directive$Type): R
 "visit"(arg0: $ModuleElement$Directive$Type, arg1: P): R
 "visitUses"(arg0: $ModuleElement$UsesDirective$Type, arg1: P): R
 "visitExports"(arg0: $ModuleElement$ExportsDirective$Type, arg1: P): R
 "visitOpens"(arg0: $ModuleElement$OpensDirective$Type, arg1: P): R
 "visitProvides"(arg0: $ModuleElement$ProvidesDirective$Type, arg1: P): R
 "visitRequires"(arg0: $ModuleElement$RequiresDirective$Type, arg1: P): R
 "visitUnknown"(arg0: $ModuleElement$Directive$Type, arg1: P): R
}

export namespace $ModuleElement$DirectiveVisitor {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModuleElement$DirectiveVisitor$Type<R, P> = ($ModuleElement$DirectiveVisitor<(R), (P)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModuleElement$DirectiveVisitor_<R, P> = $ModuleElement$DirectiveVisitor$Type<(R), (P)>;
}}
declare module "packages/javax/lang/model/type/$WildcardType" {
import {$Annotation, $Annotation$Type} from "packages/java/lang/annotation/$Annotation"
import {$TypeMirror, $TypeMirror$Type} from "packages/javax/lang/model/type/$TypeMirror"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$List, $List$Type} from "packages/java/util/$List"
import {$TypeVisitor, $TypeVisitor$Type} from "packages/javax/lang/model/type/$TypeVisitor"
import {$TypeKind, $TypeKind$Type} from "packages/javax/lang/model/type/$TypeKind"

export interface $WildcardType extends $TypeMirror {

 "getExtendsBound"(): $TypeMirror
 "getSuperBound"(): $TypeMirror
 "equals"(arg0: any): boolean
 "toString"(): string
 "hashCode"(): integer
 "getAnnotation"<A extends $Annotation>(arg0: $Class$Type<(A)>): A
 "getAnnotationsByType"<A extends $Annotation>(arg0: $Class$Type<(A)>): (A)[]
 "accept"<R, P>(arg0: $TypeVisitor$Type<(R), (P)>, arg1: P): R
 "getKind"(): $TypeKind
 "getAnnotationMirrors"(): $List<(any)>
}

export namespace $WildcardType {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WildcardType$Type = ($WildcardType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WildcardType_ = $WildcardType$Type;
}}
declare module "packages/javax/lang/model/element/$ElementKind" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $ElementKind extends $Enum<($ElementKind)> {
static readonly "PACKAGE": $ElementKind
static readonly "ENUM": $ElementKind
static readonly "CLASS": $ElementKind
static readonly "ANNOTATION_TYPE": $ElementKind
static readonly "INTERFACE": $ElementKind
static readonly "ENUM_CONSTANT": $ElementKind
static readonly "FIELD": $ElementKind
static readonly "PARAMETER": $ElementKind
static readonly "LOCAL_VARIABLE": $ElementKind
static readonly "EXCEPTION_PARAMETER": $ElementKind
static readonly "METHOD": $ElementKind
static readonly "CONSTRUCTOR": $ElementKind
static readonly "STATIC_INIT": $ElementKind
static readonly "INSTANCE_INIT": $ElementKind
static readonly "TYPE_PARAMETER": $ElementKind
static readonly "OTHER": $ElementKind
static readonly "RESOURCE_VARIABLE": $ElementKind
static readonly "MODULE": $ElementKind
static readonly "RECORD": $ElementKind
static readonly "RECORD_COMPONENT": $ElementKind
static readonly "BINDING_VARIABLE": $ElementKind


public static "values"(): ($ElementKind)[]
public "isInterface"(): boolean
public static "valueOf"(arg0: string): $ElementKind
public "isField"(): boolean
public "isClass"(): boolean
get "interface"(): boolean
get "field"(): boolean
get "class"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ElementKind$Type = (("other") | ("package") | ("method") | ("static_init") | ("exception_parameter") | ("module") | ("constructor") | ("interface") | ("enum") | ("instance_init") | ("local_variable") | ("resource_variable") | ("record_component") | ("field") | ("parameter") | ("record") | ("annotation_type") | ("type_parameter") | ("enum_constant") | ("class") | ("binding_variable")) | ($ElementKind);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ElementKind_ = $ElementKind$Type;
}}
declare module "packages/javax/lang/model/element/$Name" {
import {$IntStream, $IntStream$Type} from "packages/java/util/stream/$IntStream"

export interface $Name extends charseq {

 "equals"(arg0: any): boolean
 "hashCode"(): integer
 "contentEquals"(arg0: charseq): boolean
 "length"(): integer
 "toString"(): string
 "charAt"(arg0: integer): character
 "isEmpty"(): boolean
 "codePoints"(): $IntStream
 "subSequence"(arg0: integer, arg1: integer): charseq
 "chars"(): $IntStream
}

export namespace $Name {
function compare(arg0: charseq, arg1: charseq): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Name$Type = ($Name);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Name_ = $Name$Type;
}}
declare module "packages/javax/lang/model/element/$AnnotationValue" {
import {$AnnotationValueVisitor, $AnnotationValueVisitor$Type} from "packages/javax/lang/model/element/$AnnotationValueVisitor"

export interface $AnnotationValue {

 "toString"(): string
 "getValue"(): any
 "accept"<R, P>(arg0: $AnnotationValueVisitor$Type<(R), (P)>, arg1: P): R
}

export namespace $AnnotationValue {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AnnotationValue$Type = ($AnnotationValue);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AnnotationValue_ = $AnnotationValue$Type;
}}
declare module "packages/javax/lang/model/element/$ModuleElement$RequiresDirective" {
import {$ModuleElement$Directive, $ModuleElement$Directive$Type} from "packages/javax/lang/model/element/$ModuleElement$Directive"
import {$ModuleElement$DirectiveKind, $ModuleElement$DirectiveKind$Type} from "packages/javax/lang/model/element/$ModuleElement$DirectiveKind"
import {$ModuleElement, $ModuleElement$Type} from "packages/javax/lang/model/element/$ModuleElement"
import {$ModuleElement$DirectiveVisitor, $ModuleElement$DirectiveVisitor$Type} from "packages/javax/lang/model/element/$ModuleElement$DirectiveVisitor"

export interface $ModuleElement$RequiresDirective extends $ModuleElement$Directive {

 "isStatic"(): boolean
 "isTransitive"(): boolean
 "getDependency"(): $ModuleElement
 "accept"<R, P>(arg0: $ModuleElement$DirectiveVisitor$Type<(R), (P)>, arg1: P): R
 "getKind"(): $ModuleElement$DirectiveKind
}

export namespace $ModuleElement$RequiresDirective {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModuleElement$RequiresDirective$Type = ($ModuleElement$RequiresDirective);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModuleElement$RequiresDirective_ = $ModuleElement$RequiresDirective$Type;
}}
declare module "packages/javax/lang/model/element/$Modifier" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $Modifier extends $Enum<($Modifier)> {
static readonly "PUBLIC": $Modifier
static readonly "PROTECTED": $Modifier
static readonly "PRIVATE": $Modifier
static readonly "ABSTRACT": $Modifier
static readonly "DEFAULT": $Modifier
static readonly "STATIC": $Modifier
static readonly "SEALED": $Modifier
static readonly "NON_SEALED": $Modifier
static readonly "FINAL": $Modifier
static readonly "TRANSIENT": $Modifier
static readonly "VOLATILE": $Modifier
static readonly "SYNCHRONIZED": $Modifier
static readonly "NATIVE": $Modifier
static readonly "STRICTFP": $Modifier


public "toString"(): string
public static "values"(): ($Modifier)[]
public static "valueOf"(arg0: string): $Modifier
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Modifier$Type = (("synchronized") | ("private") | ("static") | ("sealed") | ("non_sealed") | ("volatile") | ("abstract") | ("strictfp") | ("default") | ("public") | ("protected") | ("transient") | ("native") | ("final")) | ($Modifier);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Modifier_ = $Modifier$Type;
}}
declare module "packages/javax/lang/model/util/$Elements" {
import {$AnnotationMirror, $AnnotationMirror$Type} from "packages/javax/lang/model/element/$AnnotationMirror"
import {$ModuleElement$Directive, $ModuleElement$Directive$Type} from "packages/javax/lang/model/element/$ModuleElement$Directive"
import {$Element, $Element$Type} from "packages/javax/lang/model/element/$Element"
import {$PackageElement, $PackageElement$Type} from "packages/javax/lang/model/element/$PackageElement"
import {$Name, $Name$Type} from "packages/javax/lang/model/element/$Name"
import {$Elements$Origin, $Elements$Origin$Type} from "packages/javax/lang/model/util/$Elements$Origin"
import {$Writer, $Writer$Type} from "packages/java/io/$Writer"
import {$AnnotatedConstruct, $AnnotatedConstruct$Type} from "packages/javax/lang/model/$AnnotatedConstruct"
import {$TypeElement, $TypeElement$Type} from "packages/javax/lang/model/element/$TypeElement"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ModuleElement, $ModuleElement$Type} from "packages/javax/lang/model/element/$ModuleElement"
import {$RecordComponentElement, $RecordComponentElement$Type} from "packages/javax/lang/model/element/$RecordComponentElement"
import {$ExecutableElement, $ExecutableElement$Type} from "packages/javax/lang/model/element/$ExecutableElement"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export interface $Elements {

 "getTypeElement"(arg0: $ModuleElement$Type, arg1: charseq): $TypeElement
 "getTypeElement"(arg0: charseq): $TypeElement
 "getName"(arg0: charseq): $Name
 "isBridge"(arg0: $ExecutableElement$Type): boolean
 "isDeprecated"(arg0: $Element$Type): boolean
 "overrides"(arg0: $ExecutableElement$Type, arg1: $ExecutableElement$Type, arg2: $TypeElement$Type): boolean
 "getAllMembers"(arg0: $TypeElement$Type): $List<(any)>
 "getAllModuleElements"(): $Set<(any)>
 "getAllPackageElements"(arg0: charseq): $Set<(any)>
 "getElementValuesWithDefaults"(arg0: $AnnotationMirror$Type): $Map<(any), (any)>
 "getConstantExpression"(arg0: any): string
 "getAllAnnotationMirrors"(arg0: $Element$Type): $List<(any)>
 "getOrigin"(arg0: $AnnotatedConstruct$Type, arg1: $AnnotationMirror$Type): $Elements$Origin
 "getOrigin"(arg0: $Element$Type): $Elements$Origin
 "getOrigin"(arg0: $ModuleElement$Type, arg1: $ModuleElement$Directive$Type): $Elements$Origin
 "isFunctionalInterface"(arg0: $TypeElement$Type): boolean
 "getBinaryName"(arg0: $TypeElement$Type): $Name
 "getPackageElement"(arg0: charseq): $PackageElement
 "getPackageElement"(arg0: $ModuleElement$Type, arg1: charseq): $PackageElement
 "getAllTypeElements"(arg0: charseq): $Set<(any)>
 "getDocComment"(arg0: $Element$Type): string
 "getModuleElement"(arg0: charseq): $ModuleElement
 "printElements"(arg0: $Writer$Type, ...arg1: ($Element$Type)[]): void
 "getModuleOf"(arg0: $Element$Type): $ModuleElement
 "isAutomaticModule"(arg0: $ModuleElement$Type): boolean
 "getPackageOf"(arg0: $Element$Type): $PackageElement
 "hides"(arg0: $Element$Type, arg1: $Element$Type): boolean
 "recordComponentFor"(arg0: $ExecutableElement$Type): $RecordComponentElement
}

export namespace $Elements {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Elements$Type = ($Elements);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Elements_ = $Elements$Type;
}}
declare module "packages/javax/lang/model/type/$TypeVariable" {
import {$Annotation, $Annotation$Type} from "packages/java/lang/annotation/$Annotation"
import {$Element, $Element$Type} from "packages/javax/lang/model/element/$Element"
import {$TypeMirror, $TypeMirror$Type} from "packages/javax/lang/model/type/$TypeMirror"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$List, $List$Type} from "packages/java/util/$List"
import {$TypeVisitor, $TypeVisitor$Type} from "packages/javax/lang/model/type/$TypeVisitor"
import {$TypeKind, $TypeKind$Type} from "packages/javax/lang/model/type/$TypeKind"
import {$ReferenceType, $ReferenceType$Type} from "packages/javax/lang/model/type/$ReferenceType"

export interface $TypeVariable extends $ReferenceType {

 "asElement"(): $Element
 "getUpperBound"(): $TypeMirror
 "getLowerBound"(): $TypeMirror
 "equals"(arg0: any): boolean
 "toString"(): string
 "hashCode"(): integer
 "getAnnotation"<A extends $Annotation>(arg0: $Class$Type<(A)>): A
 "getAnnotationsByType"<A extends $Annotation>(arg0: $Class$Type<(A)>): (A)[]
 "accept"<R, P>(arg0: $TypeVisitor$Type<(R), (P)>, arg1: P): R
 "getKind"(): $TypeKind
 "getAnnotationMirrors"(): $List<(any)>
}

export namespace $TypeVariable {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TypeVariable$Type = ($TypeVariable);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TypeVariable_ = $TypeVariable$Type;
}}
declare module "packages/javax/lang/model/element/$TypeParameterElement" {
import {$Annotation, $Annotation$Type} from "packages/java/lang/annotation/$Annotation"
import {$Modifier, $Modifier$Type} from "packages/javax/lang/model/element/$Modifier"
import {$Element, $Element$Type} from "packages/javax/lang/model/element/$Element"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$List, $List$Type} from "packages/java/util/$List"
import {$TypeMirror, $TypeMirror$Type} from "packages/javax/lang/model/type/$TypeMirror"
import {$Name, $Name$Type} from "packages/javax/lang/model/element/$Name"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$ElementVisitor, $ElementVisitor$Type} from "packages/javax/lang/model/element/$ElementVisitor"
import {$ElementKind, $ElementKind$Type} from "packages/javax/lang/model/element/$ElementKind"

export interface $TypeParameterElement extends $Element {

 "getBounds"(): $List<(any)>
 "asType"(): $TypeMirror
 "getEnclosingElement"(): $Element
 "getGenericElement"(): $Element
 "equals"(arg0: any): boolean
 "hashCode"(): integer
 "getModifiers"(): $Set<($Modifier)>
 "getSimpleName"(): $Name
 "getAnnotation"<A extends $Annotation>(arg0: $Class$Type<(A)>): A
 "getAnnotationsByType"<A extends $Annotation>(arg0: $Class$Type<(A)>): (A)[]
 "accept"<R, P>(arg0: $ElementVisitor$Type<(R), (P)>, arg1: P): R
 "getEnclosedElements"(): $List<(any)>
 "getKind"(): $ElementKind
 "getAnnotationMirrors"(): $List<(any)>
}

export namespace $TypeParameterElement {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TypeParameterElement$Type = ($TypeParameterElement);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TypeParameterElement_ = $TypeParameterElement$Type;
}}
declare module "packages/javax/lang/model/element/$Parameterizable" {
import {$Annotation, $Annotation$Type} from "packages/java/lang/annotation/$Annotation"
import {$Modifier, $Modifier$Type} from "packages/javax/lang/model/element/$Modifier"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$Element, $Element$Type} from "packages/javax/lang/model/element/$Element"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Name, $Name$Type} from "packages/javax/lang/model/element/$Name"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$TypeMirror, $TypeMirror$Type} from "packages/javax/lang/model/type/$TypeMirror"
import {$ElementVisitor, $ElementVisitor$Type} from "packages/javax/lang/model/element/$ElementVisitor"
import {$ElementKind, $ElementKind$Type} from "packages/javax/lang/model/element/$ElementKind"

export interface $Parameterizable extends $Element {

 "getTypeParameters"(): $List<(any)>
 "equals"(arg0: any): boolean
 "hashCode"(): integer
 "getModifiers"(): $Set<($Modifier)>
 "getSimpleName"(): $Name
 "getAnnotation"<A extends $Annotation>(arg0: $Class$Type<(A)>): A
 "getAnnotationsByType"<A extends $Annotation>(arg0: $Class$Type<(A)>): (A)[]
 "accept"<R, P>(arg0: $ElementVisitor$Type<(R), (P)>, arg1: P): R
 "asType"(): $TypeMirror
 "getEnclosedElements"(): $List<(any)>
 "getKind"(): $ElementKind
 "getEnclosingElement"(): $Element
 "getAnnotationMirrors"(): $List<(any)>
}

export namespace $Parameterizable {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Parameterizable$Type = ($Parameterizable);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Parameterizable_ = $Parameterizable$Type;
}}
declare module "packages/javax/lang/model/element/$RecordComponentElement" {
import {$Annotation, $Annotation$Type} from "packages/java/lang/annotation/$Annotation"
import {$Modifier, $Modifier$Type} from "packages/javax/lang/model/element/$Modifier"
import {$Element, $Element$Type} from "packages/javax/lang/model/element/$Element"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$Name, $Name$Type} from "packages/javax/lang/model/element/$Name"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$TypeMirror, $TypeMirror$Type} from "packages/javax/lang/model/type/$TypeMirror"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ElementVisitor, $ElementVisitor$Type} from "packages/javax/lang/model/element/$ElementVisitor"
import {$ExecutableElement, $ExecutableElement$Type} from "packages/javax/lang/model/element/$ExecutableElement"
import {$ElementKind, $ElementKind$Type} from "packages/javax/lang/model/element/$ElementKind"

export interface $RecordComponentElement extends $Element {

 "getSimpleName"(): $Name
 "getAccessor"(): $ExecutableElement
 "getEnclosingElement"(): $Element
 "equals"(arg0: any): boolean
 "hashCode"(): integer
 "getModifiers"(): $Set<($Modifier)>
 "getAnnotation"<A extends $Annotation>(arg0: $Class$Type<(A)>): A
 "getAnnotationsByType"<A extends $Annotation>(arg0: $Class$Type<(A)>): (A)[]
 "accept"<R, P>(arg0: $ElementVisitor$Type<(R), (P)>, arg1: P): R
 "asType"(): $TypeMirror
 "getEnclosedElements"(): $List<(any)>
 "getKind"(): $ElementKind
 "getAnnotationMirrors"(): $List<(any)>
}

export namespace $RecordComponentElement {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecordComponentElement$Type = ($RecordComponentElement);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecordComponentElement_ = $RecordComponentElement$Type;
}}
declare module "packages/javax/lang/model/element/$ModuleElement$ExportsDirective" {
import {$ModuleElement$Directive, $ModuleElement$Directive$Type} from "packages/javax/lang/model/element/$ModuleElement$Directive"
import {$PackageElement, $PackageElement$Type} from "packages/javax/lang/model/element/$PackageElement"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ModuleElement$DirectiveKind, $ModuleElement$DirectiveKind$Type} from "packages/javax/lang/model/element/$ModuleElement$DirectiveKind"
import {$ModuleElement$DirectiveVisitor, $ModuleElement$DirectiveVisitor$Type} from "packages/javax/lang/model/element/$ModuleElement$DirectiveVisitor"

export interface $ModuleElement$ExportsDirective extends $ModuleElement$Directive {

 "getPackage"(): $PackageElement
 "getTargetModules"(): $List<(any)>
 "accept"<R, P>(arg0: $ModuleElement$DirectiveVisitor$Type<(R), (P)>, arg1: P): R
 "getKind"(): $ModuleElement$DirectiveKind
}

export namespace $ModuleElement$ExportsDirective {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModuleElement$ExportsDirective$Type = ($ModuleElement$ExportsDirective);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModuleElement$ExportsDirective_ = $ModuleElement$ExportsDirective$Type;
}}
declare module "packages/javax/lang/model/type/$ExecutableType" {
import {$Annotation, $Annotation$Type} from "packages/java/lang/annotation/$Annotation"
import {$TypeMirror, $TypeMirror$Type} from "packages/javax/lang/model/type/$TypeMirror"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$TypeVisitor, $TypeVisitor$Type} from "packages/javax/lang/model/type/$TypeVisitor"
import {$TypeKind, $TypeKind$Type} from "packages/javax/lang/model/type/$TypeKind"

export interface $ExecutableType extends $TypeMirror {

 "getReturnType"(): $TypeMirror
 "getParameterTypes"(): $List<(any)>
 "getThrownTypes"(): $List<(any)>
 "getReceiverType"(): $TypeMirror
 "getTypeVariables"(): $List<(any)>
 "equals"(arg0: any): boolean
 "toString"(): string
 "hashCode"(): integer
 "getAnnotation"<A extends $Annotation>(arg0: $Class$Type<(A)>): A
 "getAnnotationsByType"<A extends $Annotation>(arg0: $Class$Type<(A)>): (A)[]
 "accept"<R, P>(arg0: $TypeVisitor$Type<(R), (P)>, arg1: P): R
 "getKind"(): $TypeKind
 "getAnnotationMirrors"(): $List<(any)>
}

export namespace $ExecutableType {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ExecutableType$Type = ($ExecutableType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ExecutableType_ = $ExecutableType$Type;
}}
declare module "packages/javax/lang/model/$SourceVersion" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $SourceVersion extends $Enum<($SourceVersion)> {
static readonly "RELEASE_0": $SourceVersion
static readonly "RELEASE_1": $SourceVersion
static readonly "RELEASE_2": $SourceVersion
static readonly "RELEASE_3": $SourceVersion
static readonly "RELEASE_4": $SourceVersion
static readonly "RELEASE_5": $SourceVersion
static readonly "RELEASE_6": $SourceVersion
static readonly "RELEASE_7": $SourceVersion
static readonly "RELEASE_8": $SourceVersion
static readonly "RELEASE_9": $SourceVersion
static readonly "RELEASE_10": $SourceVersion
static readonly "RELEASE_11": $SourceVersion
static readonly "RELEASE_12": $SourceVersion
static readonly "RELEASE_13": $SourceVersion
static readonly "RELEASE_14": $SourceVersion
static readonly "RELEASE_15": $SourceVersion
static readonly "RELEASE_16": $SourceVersion
static readonly "RELEASE_17": $SourceVersion


public static "values"(): ($SourceVersion)[]
public static "valueOf"(arg0: string): $SourceVersion
public static "isName"(arg0: charseq): boolean
public static "isName"(arg0: charseq, arg1: $SourceVersion$Type): boolean
public static "isIdentifier"(arg0: charseq): boolean
public static "latest"(): $SourceVersion
public static "isKeyword"(arg0: charseq, arg1: $SourceVersion$Type): boolean
public static "isKeyword"(arg0: charseq): boolean
public static "latestSupported"(): $SourceVersion
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SourceVersion$Type = (("release_10") | ("release_7") | ("release_13") | ("release_6") | ("release_14") | ("release_5") | ("release_11") | ("release_4") | ("release_12") | ("release_3") | ("release_17") | ("release_2") | ("release_1") | ("release_15") | ("release_0") | ("release_16") | ("release_9") | ("release_8")) | ($SourceVersion);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SourceVersion_ = $SourceVersion$Type;
}}
declare module "packages/javax/lang/model/element/$AnnotationMirror" {
import {$DeclaredType, $DeclaredType$Type} from "packages/javax/lang/model/type/$DeclaredType"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export interface $AnnotationMirror {

 "getElementValues"(): $Map<(any), (any)>
 "getAnnotationType"(): $DeclaredType
}

export namespace $AnnotationMirror {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AnnotationMirror$Type = ($AnnotationMirror);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AnnotationMirror_ = $AnnotationMirror$Type;
}}
declare module "packages/javax/lang/model/type/$TypeMirror" {
import {$Annotation, $Annotation$Type} from "packages/java/lang/annotation/$Annotation"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$List, $List$Type} from "packages/java/util/$List"
import {$TypeVisitor, $TypeVisitor$Type} from "packages/javax/lang/model/type/$TypeVisitor"
import {$TypeKind, $TypeKind$Type} from "packages/javax/lang/model/type/$TypeKind"
import {$AnnotatedConstruct, $AnnotatedConstruct$Type} from "packages/javax/lang/model/$AnnotatedConstruct"

export interface $TypeMirror extends $AnnotatedConstruct {

 "equals"(arg0: any): boolean
 "toString"(): string
 "hashCode"(): integer
 "getAnnotation"<A extends $Annotation>(arg0: $Class$Type<(A)>): A
 "getAnnotationsByType"<A extends $Annotation>(arg0: $Class$Type<(A)>): (A)[]
 "accept"<R, P>(arg0: $TypeVisitor$Type<(R), (P)>, arg1: P): R
 "getKind"(): $TypeKind
 "getAnnotationMirrors"(): $List<(any)>
}

export namespace $TypeMirror {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TypeMirror$Type = ($TypeMirror);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TypeMirror_ = $TypeMirror$Type;
}}
declare module "packages/javax/lang/model/element/$ModuleElement$Directive" {
import {$ModuleElement$DirectiveKind, $ModuleElement$DirectiveKind$Type} from "packages/javax/lang/model/element/$ModuleElement$DirectiveKind"
import {$ModuleElement$DirectiveVisitor, $ModuleElement$DirectiveVisitor$Type} from "packages/javax/lang/model/element/$ModuleElement$DirectiveVisitor"

export interface $ModuleElement$Directive {

 "accept"<R, P>(arg0: $ModuleElement$DirectiveVisitor$Type<(R), (P)>, arg1: P): R
 "getKind"(): $ModuleElement$DirectiveKind
}

export namespace $ModuleElement$Directive {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModuleElement$Directive$Type = ($ModuleElement$Directive);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModuleElement$Directive_ = $ModuleElement$Directive$Type;
}}
declare module "packages/javax/lang/model/element/$Element" {
import {$Annotation, $Annotation$Type} from "packages/java/lang/annotation/$Annotation"
import {$Modifier, $Modifier$Type} from "packages/javax/lang/model/element/$Modifier"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$Name, $Name$Type} from "packages/javax/lang/model/element/$Name"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$TypeMirror, $TypeMirror$Type} from "packages/javax/lang/model/type/$TypeMirror"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ElementVisitor, $ElementVisitor$Type} from "packages/javax/lang/model/element/$ElementVisitor"
import {$ElementKind, $ElementKind$Type} from "packages/javax/lang/model/element/$ElementKind"
import {$AnnotatedConstruct, $AnnotatedConstruct$Type} from "packages/javax/lang/model/$AnnotatedConstruct"

export interface $Element extends $AnnotatedConstruct {

 "equals"(arg0: any): boolean
 "hashCode"(): integer
 "getModifiers"(): $Set<($Modifier)>
 "getSimpleName"(): $Name
 "getAnnotation"<A extends $Annotation>(arg0: $Class$Type<(A)>): A
 "getAnnotationsByType"<A extends $Annotation>(arg0: $Class$Type<(A)>): (A)[]
 "accept"<R, P>(arg0: $ElementVisitor$Type<(R), (P)>, arg1: P): R
 "asType"(): $TypeMirror
 "getEnclosedElements"(): $List<(any)>
 "getKind"(): $ElementKind
 "getEnclosingElement"(): $Element
 "getAnnotationMirrors"(): $List<(any)>
}

export namespace $Element {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Element$Type = ($Element);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Element_ = $Element$Type;
}}
declare module "packages/javax/lang/model/element/$VariableElement" {
import {$Annotation, $Annotation$Type} from "packages/java/lang/annotation/$Annotation"
import {$Modifier, $Modifier$Type} from "packages/javax/lang/model/element/$Modifier"
import {$Element, $Element$Type} from "packages/javax/lang/model/element/$Element"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$Name, $Name$Type} from "packages/javax/lang/model/element/$Name"
import {$TypeMirror, $TypeMirror$Type} from "packages/javax/lang/model/type/$TypeMirror"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ElementVisitor, $ElementVisitor$Type} from "packages/javax/lang/model/element/$ElementVisitor"
import {$ElementKind, $ElementKind$Type} from "packages/javax/lang/model/element/$ElementKind"

export interface $VariableElement extends $Element {

 "getSimpleName"(): $Name
 "asType"(): $TypeMirror
 "getConstantValue"(): any
 "getEnclosingElement"(): $Element
 "equals"(arg0: any): boolean
 "hashCode"(): integer
 "getModifiers"(): $Set<($Modifier)>
 "getAnnotation"<A extends $Annotation>(arg0: $Class$Type<(A)>): A
 "getAnnotationsByType"<A extends $Annotation>(arg0: $Class$Type<(A)>): (A)[]
 "accept"<R, P>(arg0: $ElementVisitor$Type<(R), (P)>, arg1: P): R
 "getEnclosedElements"(): $List<(any)>
 "getKind"(): $ElementKind
 "getAnnotationMirrors"(): $List<(any)>
}

export namespace $VariableElement {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $VariableElement$Type = ($VariableElement);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $VariableElement_ = $VariableElement$Type;
}}
declare module "packages/javax/lang/model/type/$DeclaredType" {
import {$Annotation, $Annotation$Type} from "packages/java/lang/annotation/$Annotation"
import {$Element, $Element$Type} from "packages/javax/lang/model/element/$Element"
import {$List, $List$Type} from "packages/java/util/$List"
import {$TypeMirror, $TypeMirror$Type} from "packages/javax/lang/model/type/$TypeMirror"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$TypeVisitor, $TypeVisitor$Type} from "packages/javax/lang/model/type/$TypeVisitor"
import {$TypeKind, $TypeKind$Type} from "packages/javax/lang/model/type/$TypeKind"
import {$ReferenceType, $ReferenceType$Type} from "packages/javax/lang/model/type/$ReferenceType"

export interface $DeclaredType extends $ReferenceType {

 "asElement"(): $Element
 "getTypeArguments"(): $List<(any)>
 "getEnclosingType"(): $TypeMirror
 "equals"(arg0: any): boolean
 "toString"(): string
 "hashCode"(): integer
 "getAnnotation"<A extends $Annotation>(arg0: $Class$Type<(A)>): A
 "getAnnotationsByType"<A extends $Annotation>(arg0: $Class$Type<(A)>): (A)[]
 "accept"<R, P>(arg0: $TypeVisitor$Type<(R), (P)>, arg1: P): R
 "getKind"(): $TypeKind
 "getAnnotationMirrors"(): $List<(any)>
}

export namespace $DeclaredType {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DeclaredType$Type = ($DeclaredType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DeclaredType_ = $DeclaredType$Type;
}}
declare module "packages/javax/lang/model/element/$ExecutableElement" {
import {$Modifier, $Modifier$Type} from "packages/javax/lang/model/element/$Modifier"
import {$Element, $Element$Type} from "packages/javax/lang/model/element/$Element"
import {$TypeMirror, $TypeMirror$Type} from "packages/javax/lang/model/type/$TypeMirror"
import {$Name, $Name$Type} from "packages/javax/lang/model/element/$Name"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$ElementKind, $ElementKind$Type} from "packages/javax/lang/model/element/$ElementKind"
import {$AnnotationValue, $AnnotationValue$Type} from "packages/javax/lang/model/element/$AnnotationValue"
import {$Annotation, $Annotation$Type} from "packages/java/lang/annotation/$Annotation"
import {$Parameterizable, $Parameterizable$Type} from "packages/javax/lang/model/element/$Parameterizable"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ElementVisitor, $ElementVisitor$Type} from "packages/javax/lang/model/element/$ElementVisitor"

export interface $ExecutableElement extends $Element, $Parameterizable {

 "getTypeParameters"(): $List<(any)>
 "getReturnType"(): $TypeMirror
 "getSimpleName"(): $Name
 "isVarArgs"(): boolean
 "isDefault"(): boolean
 "getDefaultValue"(): $AnnotationValue
 "getParameters"(): $List<(any)>
 "asType"(): $TypeMirror
 "getThrownTypes"(): $List<(any)>
 "getReceiverType"(): $TypeMirror
 "equals"(arg0: any): boolean
 "hashCode"(): integer
 "getModifiers"(): $Set<($Modifier)>
 "getAnnotation"<A extends $Annotation>(arg0: $Class$Type<(A)>): A
 "getAnnotationsByType"<A extends $Annotation>(arg0: $Class$Type<(A)>): (A)[]
 "accept"<R, P>(arg0: $ElementVisitor$Type<(R), (P)>, arg1: P): R
 "getEnclosedElements"(): $List<(any)>
 "getKind"(): $ElementKind
 "getEnclosingElement"(): $Element
 "getAnnotationMirrors"(): $List<(any)>
}

export namespace $ExecutableElement {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ExecutableElement$Type = ($ExecutableElement);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ExecutableElement_ = $ExecutableElement$Type;
}}
declare module "packages/javax/lang/model/type/$PrimitiveType" {
import {$Annotation, $Annotation$Type} from "packages/java/lang/annotation/$Annotation"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$List, $List$Type} from "packages/java/util/$List"
import {$TypeMirror, $TypeMirror$Type} from "packages/javax/lang/model/type/$TypeMirror"
import {$TypeVisitor, $TypeVisitor$Type} from "packages/javax/lang/model/type/$TypeVisitor"
import {$TypeKind, $TypeKind$Type} from "packages/javax/lang/model/type/$TypeKind"

export interface $PrimitiveType extends $TypeMirror {

 "equals"(arg0: any): boolean
 "toString"(): string
 "hashCode"(): integer
 "getAnnotation"<A extends $Annotation>(arg0: $Class$Type<(A)>): A
 "getAnnotationsByType"<A extends $Annotation>(arg0: $Class$Type<(A)>): (A)[]
 "accept"<R, P>(arg0: $TypeVisitor$Type<(R), (P)>, arg1: P): R
 "getKind"(): $TypeKind
 "getAnnotationMirrors"(): $List<(any)>
}

export namespace $PrimitiveType {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PrimitiveType$Type = ($PrimitiveType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PrimitiveType_ = $PrimitiveType$Type;
}}
declare module "packages/javax/lang/model/element/$TypeElement" {
import {$Modifier, $Modifier$Type} from "packages/javax/lang/model/element/$Modifier"
import {$Element, $Element$Type} from "packages/javax/lang/model/element/$Element"
import {$TypeMirror, $TypeMirror$Type} from "packages/javax/lang/model/type/$TypeMirror"
import {$Name, $Name$Type} from "packages/javax/lang/model/element/$Name"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$ElementKind, $ElementKind$Type} from "packages/javax/lang/model/element/$ElementKind"
import {$Annotation, $Annotation$Type} from "packages/java/lang/annotation/$Annotation"
import {$Parameterizable, $Parameterizable$Type} from "packages/javax/lang/model/element/$Parameterizable"
import {$NestingKind, $NestingKind$Type} from "packages/javax/lang/model/element/$NestingKind"
import {$QualifiedNameable, $QualifiedNameable$Type} from "packages/javax/lang/model/element/$QualifiedNameable"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ElementVisitor, $ElementVisitor$Type} from "packages/javax/lang/model/element/$ElementVisitor"

export interface $TypeElement extends $Element, $Parameterizable, $QualifiedNameable {

 "getSuperclass"(): $TypeMirror
 "getTypeParameters"(): $List<(any)>
 "getInterfaces"(): $List<(any)>
 "getSimpleName"(): $Name
 "getPermittedSubclasses"(): $List<(any)>
 "getRecordComponents"(): $List<(any)>
 "asType"(): $TypeMirror
 "getQualifiedName"(): $Name
 "getEnclosedElements"(): $List<(any)>
 "getNestingKind"(): $NestingKind
 "getEnclosingElement"(): $Element
 "equals"(arg0: any): boolean
 "hashCode"(): integer
 "getModifiers"(): $Set<($Modifier)>
 "getAnnotation"<A extends $Annotation>(arg0: $Class$Type<(A)>): A
 "getAnnotationsByType"<A extends $Annotation>(arg0: $Class$Type<(A)>): (A)[]
 "accept"<R, P>(arg0: $ElementVisitor$Type<(R), (P)>, arg1: P): R
 "getKind"(): $ElementKind
 "getAnnotationMirrors"(): $List<(any)>
}

export namespace $TypeElement {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TypeElement$Type = ($TypeElement);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TypeElement_ = $TypeElement$Type;
}}
declare module "packages/javax/lang/model/element/$ModuleElement$OpensDirective" {
import {$ModuleElement$Directive, $ModuleElement$Directive$Type} from "packages/javax/lang/model/element/$ModuleElement$Directive"
import {$PackageElement, $PackageElement$Type} from "packages/javax/lang/model/element/$PackageElement"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ModuleElement$DirectiveKind, $ModuleElement$DirectiveKind$Type} from "packages/javax/lang/model/element/$ModuleElement$DirectiveKind"
import {$ModuleElement$DirectiveVisitor, $ModuleElement$DirectiveVisitor$Type} from "packages/javax/lang/model/element/$ModuleElement$DirectiveVisitor"

export interface $ModuleElement$OpensDirective extends $ModuleElement$Directive {

 "getPackage"(): $PackageElement
 "getTargetModules"(): $List<(any)>
 "accept"<R, P>(arg0: $ModuleElement$DirectiveVisitor$Type<(R), (P)>, arg1: P): R
 "getKind"(): $ModuleElement$DirectiveKind
}

export namespace $ModuleElement$OpensDirective {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModuleElement$OpensDirective$Type = ($ModuleElement$OpensDirective);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModuleElement$OpensDirective_ = $ModuleElement$OpensDirective$Type;
}}
declare module "packages/javax/lang/model/type/$ArrayType" {
import {$Annotation, $Annotation$Type} from "packages/java/lang/annotation/$Annotation"
import {$TypeMirror, $TypeMirror$Type} from "packages/javax/lang/model/type/$TypeMirror"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$List, $List$Type} from "packages/java/util/$List"
import {$TypeVisitor, $TypeVisitor$Type} from "packages/javax/lang/model/type/$TypeVisitor"
import {$TypeKind, $TypeKind$Type} from "packages/javax/lang/model/type/$TypeKind"
import {$ReferenceType, $ReferenceType$Type} from "packages/javax/lang/model/type/$ReferenceType"

export interface $ArrayType extends $ReferenceType {

 "getComponentType"(): $TypeMirror
 "equals"(arg0: any): boolean
 "toString"(): string
 "hashCode"(): integer
 "getAnnotation"<A extends $Annotation>(arg0: $Class$Type<(A)>): A
 "getAnnotationsByType"<A extends $Annotation>(arg0: $Class$Type<(A)>): (A)[]
 "accept"<R, P>(arg0: $TypeVisitor$Type<(R), (P)>, arg1: P): R
 "getKind"(): $TypeKind
 "getAnnotationMirrors"(): $List<(any)>
}

export namespace $ArrayType {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ArrayType$Type = ($ArrayType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ArrayType_ = $ArrayType$Type;
}}
declare module "packages/javax/lang/model/type/$NullType" {
import {$Annotation, $Annotation$Type} from "packages/java/lang/annotation/$Annotation"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$List, $List$Type} from "packages/java/util/$List"
import {$TypeVisitor, $TypeVisitor$Type} from "packages/javax/lang/model/type/$TypeVisitor"
import {$TypeKind, $TypeKind$Type} from "packages/javax/lang/model/type/$TypeKind"
import {$ReferenceType, $ReferenceType$Type} from "packages/javax/lang/model/type/$ReferenceType"

export interface $NullType extends $ReferenceType {

 "equals"(arg0: any): boolean
 "toString"(): string
 "hashCode"(): integer
 "getAnnotation"<A extends $Annotation>(arg0: $Class$Type<(A)>): A
 "getAnnotationsByType"<A extends $Annotation>(arg0: $Class$Type<(A)>): (A)[]
 "accept"<R, P>(arg0: $TypeVisitor$Type<(R), (P)>, arg1: P): R
 "getKind"(): $TypeKind
 "getAnnotationMirrors"(): $List<(any)>
}

export namespace $NullType {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NullType$Type = ($NullType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NullType_ = $NullType$Type;
}}
declare module "packages/javax/lang/model/type/$TypeKind" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $TypeKind extends $Enum<($TypeKind)> {
static readonly "BOOLEAN": $TypeKind
static readonly "BYTE": $TypeKind
static readonly "SHORT": $TypeKind
static readonly "INT": $TypeKind
static readonly "LONG": $TypeKind
static readonly "CHAR": $TypeKind
static readonly "FLOAT": $TypeKind
static readonly "DOUBLE": $TypeKind
static readonly "VOID": $TypeKind
static readonly "NONE": $TypeKind
static readonly "NULL": $TypeKind
static readonly "ARRAY": $TypeKind
static readonly "DECLARED": $TypeKind
static readonly "ERROR": $TypeKind
static readonly "TYPEVAR": $TypeKind
static readonly "WILDCARD": $TypeKind
static readonly "PACKAGE": $TypeKind
static readonly "EXECUTABLE": $TypeKind
static readonly "OTHER": $TypeKind
static readonly "UNION": $TypeKind
static readonly "INTERSECTION": $TypeKind
static readonly "MODULE": $TypeKind


public static "values"(): ($TypeKind)[]
public "isPrimitive"(): boolean
public static "valueOf"(arg0: string): $TypeKind
get "primitive"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TypeKind$Type = (("declared") | ("other") | ("void") | ("package") | ("byte") | ("double") | ("module") | ("typevar") | ("none") | ("union") | ("float") | ("error") | ("int") | ("long") | ("executable") | ("wildcard") | ("boolean") | ("null") | ("array") | ("intersection") | ("char") | ("short")) | ($TypeKind);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TypeKind_ = $TypeKind$Type;
}}
declare module "packages/javax/lang/model/util/$Types" {
import {$ExecutableType, $ExecutableType$Type} from "packages/javax/lang/model/type/$ExecutableType"
import {$NullType, $NullType$Type} from "packages/javax/lang/model/type/$NullType"
import {$Element, $Element$Type} from "packages/javax/lang/model/element/$Element"
import {$TypeMirror, $TypeMirror$Type} from "packages/javax/lang/model/type/$TypeMirror"
import {$NoType, $NoType$Type} from "packages/javax/lang/model/type/$NoType"
import {$TypeKind, $TypeKind$Type} from "packages/javax/lang/model/type/$TypeKind"
import {$TypeElement, $TypeElement$Type} from "packages/javax/lang/model/element/$TypeElement"
import {$ArrayType, $ArrayType$Type} from "packages/javax/lang/model/type/$ArrayType"
import {$List, $List$Type} from "packages/java/util/$List"
import {$DeclaredType, $DeclaredType$Type} from "packages/javax/lang/model/type/$DeclaredType"
import {$PrimitiveType, $PrimitiveType$Type} from "packages/javax/lang/model/type/$PrimitiveType"
import {$WildcardType, $WildcardType$Type} from "packages/javax/lang/model/type/$WildcardType"

export interface $Types {

 "asElement"(arg0: $TypeMirror$Type): $Element
 "contains"(arg0: $TypeMirror$Type, arg1: $TypeMirror$Type): boolean
 "capture"(arg0: $TypeMirror$Type): $TypeMirror
 "isSameType"(arg0: $TypeMirror$Type, arg1: $TypeMirror$Type): boolean
 "isAssignable"(arg0: $TypeMirror$Type, arg1: $TypeMirror$Type): boolean
 "getArrayType"(arg0: $TypeMirror$Type): $ArrayType
 "getPrimitiveType"(arg0: $TypeKind$Type): $PrimitiveType
 "isSubtype"(arg0: $TypeMirror$Type, arg1: $TypeMirror$Type): boolean
 "erasure"(arg0: $TypeMirror$Type): $TypeMirror
 "getDeclaredType"(arg0: $TypeElement$Type, ...arg1: ($TypeMirror$Type)[]): $DeclaredType
 "getDeclaredType"(arg0: $DeclaredType$Type, arg1: $TypeElement$Type, ...arg2: ($TypeMirror$Type)[]): $DeclaredType
 "getWildcardType"(arg0: $TypeMirror$Type, arg1: $TypeMirror$Type): $WildcardType
 "unboxedType"(arg0: $TypeMirror$Type): $PrimitiveType
 "directSupertypes"(arg0: $TypeMirror$Type): $List<(any)>
 "asMemberOf"(arg0: $DeclaredType$Type, arg1: $Element$Type): $TypeMirror
 "isSubsignature"(arg0: $ExecutableType$Type, arg1: $ExecutableType$Type): boolean
 "boxedClass"(arg0: $PrimitiveType$Type): $TypeElement
 "getNoType"(arg0: $TypeKind$Type): $NoType
 "getNullType"(): $NullType
}

export namespace $Types {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Types$Type = ($Types);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Types_ = $Types$Type;
}}
declare module "packages/javax/lang/model/element/$PackageElement" {
import {$Annotation, $Annotation$Type} from "packages/java/lang/annotation/$Annotation"
import {$Modifier, $Modifier$Type} from "packages/javax/lang/model/element/$Modifier"
import {$QualifiedNameable, $QualifiedNameable$Type} from "packages/javax/lang/model/element/$QualifiedNameable"
import {$Element, $Element$Type} from "packages/javax/lang/model/element/$Element"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$Name, $Name$Type} from "packages/javax/lang/model/element/$Name"
import {$TypeMirror, $TypeMirror$Type} from "packages/javax/lang/model/type/$TypeMirror"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$ElementVisitor, $ElementVisitor$Type} from "packages/javax/lang/model/element/$ElementVisitor"
import {$ElementKind, $ElementKind$Type} from "packages/javax/lang/model/element/$ElementKind"

export interface $PackageElement extends $Element, $QualifiedNameable {

 "getSimpleName"(): $Name
 "asType"(): $TypeMirror
 "getQualifiedName"(): $Name
 "getEnclosedElements"(): $List<(any)>
 "getEnclosingElement"(): $Element
 "isUnnamed"(): boolean
 "equals"(arg0: any): boolean
 "hashCode"(): integer
 "getModifiers"(): $Set<($Modifier)>
 "getAnnotation"<A extends $Annotation>(arg0: $Class$Type<(A)>): A
 "getAnnotationsByType"<A extends $Annotation>(arg0: $Class$Type<(A)>): (A)[]
 "accept"<R, P>(arg0: $ElementVisitor$Type<(R), (P)>, arg1: P): R
 "getKind"(): $ElementKind
 "getAnnotationMirrors"(): $List<(any)>
}

export namespace $PackageElement {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PackageElement$Type = ($PackageElement);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PackageElement_ = $PackageElement$Type;
}}
declare module "packages/javax/lang/model/element/$QualifiedNameable" {
import {$Annotation, $Annotation$Type} from "packages/java/lang/annotation/$Annotation"
import {$Modifier, $Modifier$Type} from "packages/javax/lang/model/element/$Modifier"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$Element, $Element$Type} from "packages/javax/lang/model/element/$Element"
import {$Name, $Name$Type} from "packages/javax/lang/model/element/$Name"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$TypeMirror, $TypeMirror$Type} from "packages/javax/lang/model/type/$TypeMirror"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ElementVisitor, $ElementVisitor$Type} from "packages/javax/lang/model/element/$ElementVisitor"
import {$ElementKind, $ElementKind$Type} from "packages/javax/lang/model/element/$ElementKind"

export interface $QualifiedNameable extends $Element {

 "getQualifiedName"(): $Name
 "equals"(arg0: any): boolean
 "hashCode"(): integer
 "getModifiers"(): $Set<($Modifier)>
 "getSimpleName"(): $Name
 "getAnnotation"<A extends $Annotation>(arg0: $Class$Type<(A)>): A
 "getAnnotationsByType"<A extends $Annotation>(arg0: $Class$Type<(A)>): (A)[]
 "accept"<R, P>(arg0: $ElementVisitor$Type<(R), (P)>, arg1: P): R
 "asType"(): $TypeMirror
 "getEnclosedElements"(): $List<(any)>
 "getKind"(): $ElementKind
 "getEnclosingElement"(): $Element
 "getAnnotationMirrors"(): $List<(any)>
}

export namespace $QualifiedNameable {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $QualifiedNameable$Type = ($QualifiedNameable);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $QualifiedNameable_ = $QualifiedNameable$Type;
}}
declare module "packages/javax/lang/model/util/$Elements$Origin" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $Elements$Origin extends $Enum<($Elements$Origin)> {
static readonly "EXPLICIT": $Elements$Origin
static readonly "MANDATED": $Elements$Origin
static readonly "SYNTHETIC": $Elements$Origin


public static "values"(): ($Elements$Origin)[]
public static "valueOf"(arg0: string): $Elements$Origin
public "isDeclared"(): boolean
get "declared"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Elements$Origin$Type = (("explicit") | ("synthetic") | ("mandated")) | ($Elements$Origin);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Elements$Origin_ = $Elements$Origin$Type;
}}
declare module "packages/javax/lang/model/type/$IntersectionType" {
import {$Annotation, $Annotation$Type} from "packages/java/lang/annotation/$Annotation"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$TypeMirror, $TypeMirror$Type} from "packages/javax/lang/model/type/$TypeMirror"
import {$TypeVisitor, $TypeVisitor$Type} from "packages/javax/lang/model/type/$TypeVisitor"
import {$TypeKind, $TypeKind$Type} from "packages/javax/lang/model/type/$TypeKind"

export interface $IntersectionType extends $TypeMirror {

 "getBounds"(): $List<(any)>
 "equals"(arg0: any): boolean
 "toString"(): string
 "hashCode"(): integer
 "getAnnotation"<A extends $Annotation>(arg0: $Class$Type<(A)>): A
 "getAnnotationsByType"<A extends $Annotation>(arg0: $Class$Type<(A)>): (A)[]
 "accept"<R, P>(arg0: $TypeVisitor$Type<(R), (P)>, arg1: P): R
 "getKind"(): $TypeKind
 "getAnnotationMirrors"(): $List<(any)>
}

export namespace $IntersectionType {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IntersectionType$Type = ($IntersectionType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IntersectionType_ = $IntersectionType$Type;
}}
declare module "packages/javax/lang/model/element/$ModuleElement" {
import {$Annotation, $Annotation$Type} from "packages/java/lang/annotation/$Annotation"
import {$Modifier, $Modifier$Type} from "packages/javax/lang/model/element/$Modifier"
import {$QualifiedNameable, $QualifiedNameable$Type} from "packages/javax/lang/model/element/$QualifiedNameable"
import {$Element, $Element$Type} from "packages/javax/lang/model/element/$Element"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$Name, $Name$Type} from "packages/javax/lang/model/element/$Name"
import {$TypeMirror, $TypeMirror$Type} from "packages/javax/lang/model/type/$TypeMirror"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$ElementVisitor, $ElementVisitor$Type} from "packages/javax/lang/model/element/$ElementVisitor"
import {$ElementKind, $ElementKind$Type} from "packages/javax/lang/model/element/$ElementKind"

export interface $ModuleElement extends $Element, $QualifiedNameable {

 "getSimpleName"(): $Name
 "isOpen"(): boolean
 "asType"(): $TypeMirror
 "getQualifiedName"(): $Name
 "getEnclosedElements"(): $List<(any)>
 "getDirectives"(): $List<(any)>
 "getEnclosingElement"(): $Element
 "isUnnamed"(): boolean
 "equals"(arg0: any): boolean
 "hashCode"(): integer
 "getModifiers"(): $Set<($Modifier)>
 "getAnnotation"<A extends $Annotation>(arg0: $Class$Type<(A)>): A
 "getAnnotationsByType"<A extends $Annotation>(arg0: $Class$Type<(A)>): (A)[]
 "accept"<R, P>(arg0: $ElementVisitor$Type<(R), (P)>, arg1: P): R
 "getKind"(): $ElementKind
 "getAnnotationMirrors"(): $List<(any)>
}

export namespace $ModuleElement {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModuleElement$Type = ($ModuleElement);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModuleElement_ = $ModuleElement$Type;
}}
declare module "packages/javax/lang/model/element/$NestingKind" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $NestingKind extends $Enum<($NestingKind)> {
static readonly "TOP_LEVEL": $NestingKind
static readonly "MEMBER": $NestingKind
static readonly "LOCAL": $NestingKind
static readonly "ANONYMOUS": $NestingKind


public static "values"(): ($NestingKind)[]
public static "valueOf"(arg0: string): $NestingKind
public "isNested"(): boolean
get "nested"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NestingKind$Type = (("top_level") | ("member") | ("anonymous") | ("local")) | ($NestingKind);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NestingKind_ = $NestingKind$Type;
}}
declare module "packages/javax/lang/model/element/$AnnotationValueVisitor" {
import {$AnnotationValue, $AnnotationValue$Type} from "packages/javax/lang/model/element/$AnnotationValue"
import {$AnnotationMirror, $AnnotationMirror$Type} from "packages/javax/lang/model/element/$AnnotationMirror"
import {$VariableElement, $VariableElement$Type} from "packages/javax/lang/model/element/$VariableElement"
import {$List, $List$Type} from "packages/java/util/$List"
import {$TypeMirror, $TypeMirror$Type} from "packages/javax/lang/model/type/$TypeMirror"

export interface $AnnotationValueVisitor<R, P> {

 "visit"(arg0: $AnnotationValue$Type, arg1: P): R
 "visit"(arg0: $AnnotationValue$Type): R
 "visitAnnotation"(arg0: $AnnotationMirror$Type, arg1: P): R
 "visitArray"(arg0: $List$Type<(any)>, arg1: P): R
 "visitBoolean"(arg0: boolean, arg1: P): R
 "visitType"(arg0: $TypeMirror$Type, arg1: P): R
 "visitShort"(arg0: short, arg1: P): R
 "visitByte"(arg0: byte, arg1: P): R
 "visitChar"(arg0: character, arg1: P): R
 "visitInt"(arg0: integer, arg1: P): R
 "visitString"(arg0: string, arg1: P): R
 "visitFloat"(arg0: float, arg1: P): R
 "visitDouble"(arg0: double, arg1: P): R
 "visitLong"(arg0: long, arg1: P): R
 "visitEnumConstant"(arg0: $VariableElement$Type, arg1: P): R
 "visitUnknown"(arg0: $AnnotationValue$Type, arg1: P): R
}

export namespace $AnnotationValueVisitor {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AnnotationValueVisitor$Type<R, P> = ($AnnotationValueVisitor<(R), (P)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AnnotationValueVisitor_<R, P> = $AnnotationValueVisitor$Type<(R), (P)>;
}}
declare module "packages/javax/lang/model/element/$ModuleElement$UsesDirective" {
import {$TypeElement, $TypeElement$Type} from "packages/javax/lang/model/element/$TypeElement"
import {$ModuleElement$Directive, $ModuleElement$Directive$Type} from "packages/javax/lang/model/element/$ModuleElement$Directive"
import {$ModuleElement$DirectiveKind, $ModuleElement$DirectiveKind$Type} from "packages/javax/lang/model/element/$ModuleElement$DirectiveKind"
import {$ModuleElement$DirectiveVisitor, $ModuleElement$DirectiveVisitor$Type} from "packages/javax/lang/model/element/$ModuleElement$DirectiveVisitor"

export interface $ModuleElement$UsesDirective extends $ModuleElement$Directive {

 "getService"(): $TypeElement
 "accept"<R, P>(arg0: $ModuleElement$DirectiveVisitor$Type<(R), (P)>, arg1: P): R
 "getKind"(): $ModuleElement$DirectiveKind
}

export namespace $ModuleElement$UsesDirective {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModuleElement$UsesDirective$Type = ($ModuleElement$UsesDirective);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModuleElement$UsesDirective_ = $ModuleElement$UsesDirective$Type;
}}
declare module "packages/javax/lang/model/element/$ElementVisitor" {
import {$TypeElement, $TypeElement$Type} from "packages/javax/lang/model/element/$TypeElement"
import {$VariableElement, $VariableElement$Type} from "packages/javax/lang/model/element/$VariableElement"
import {$Element, $Element$Type} from "packages/javax/lang/model/element/$Element"
import {$PackageElement, $PackageElement$Type} from "packages/javax/lang/model/element/$PackageElement"
import {$ModuleElement, $ModuleElement$Type} from "packages/javax/lang/model/element/$ModuleElement"
import {$TypeParameterElement, $TypeParameterElement$Type} from "packages/javax/lang/model/element/$TypeParameterElement"
import {$RecordComponentElement, $RecordComponentElement$Type} from "packages/javax/lang/model/element/$RecordComponentElement"
import {$ExecutableElement, $ExecutableElement$Type} from "packages/javax/lang/model/element/$ExecutableElement"

export interface $ElementVisitor<R, P> {

 "visit"(arg0: $Element$Type, arg1: P): R
 "visit"(arg0: $Element$Type): R
 "visitModule"(arg0: $ModuleElement$Type, arg1: P): R
 "visitPackage"(arg0: $PackageElement$Type, arg1: P): R
 "visitRecordComponent"(arg0: $RecordComponentElement$Type, arg1: P): R
 "visitType"(arg0: $TypeElement$Type, arg1: P): R
 "visitVariable"(arg0: $VariableElement$Type, arg1: P): R
 "visitExecutable"(arg0: $ExecutableElement$Type, arg1: P): R
 "visitUnknown"(arg0: $Element$Type, arg1: P): R
 "visitTypeParameter"(arg0: $TypeParameterElement$Type, arg1: P): R
}

export namespace $ElementVisitor {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ElementVisitor$Type<R, P> = ($ElementVisitor<(R), (P)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ElementVisitor_<R, P> = $ElementVisitor$Type<(R), (P)>;
}}
declare module "packages/javax/lang/model/element/$ModuleElement$ProvidesDirective" {
import {$TypeElement, $TypeElement$Type} from "packages/javax/lang/model/element/$TypeElement"
import {$ModuleElement$Directive, $ModuleElement$Directive$Type} from "packages/javax/lang/model/element/$ModuleElement$Directive"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ModuleElement$DirectiveKind, $ModuleElement$DirectiveKind$Type} from "packages/javax/lang/model/element/$ModuleElement$DirectiveKind"
import {$ModuleElement$DirectiveVisitor, $ModuleElement$DirectiveVisitor$Type} from "packages/javax/lang/model/element/$ModuleElement$DirectiveVisitor"

export interface $ModuleElement$ProvidesDirective extends $ModuleElement$Directive {

 "getService"(): $TypeElement
 "getImplementations"(): $List<(any)>
 "accept"<R, P>(arg0: $ModuleElement$DirectiveVisitor$Type<(R), (P)>, arg1: P): R
 "getKind"(): $ModuleElement$DirectiveKind
}

export namespace $ModuleElement$ProvidesDirective {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModuleElement$ProvidesDirective$Type = ($ModuleElement$ProvidesDirective);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModuleElement$ProvidesDirective_ = $ModuleElement$ProvidesDirective$Type;
}}
declare module "packages/javax/lang/model/$AnnotatedConstruct" {
import {$Annotation, $Annotation$Type} from "packages/java/lang/annotation/$Annotation"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$List, $List$Type} from "packages/java/util/$List"

export interface $AnnotatedConstruct {

 "getAnnotation"<A extends $Annotation>(arg0: $Class$Type<(A)>): A
 "getAnnotationsByType"<A extends $Annotation>(arg0: $Class$Type<(A)>): (A)[]
 "getAnnotationMirrors"(): $List<(any)>
}

export namespace $AnnotatedConstruct {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AnnotatedConstruct$Type = ($AnnotatedConstruct);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AnnotatedConstruct_ = $AnnotatedConstruct$Type;
}}
declare module "packages/javax/lang/model/element/$ModuleElement$DirectiveKind" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $ModuleElement$DirectiveKind extends $Enum<($ModuleElement$DirectiveKind)> {
static readonly "REQUIRES": $ModuleElement$DirectiveKind
static readonly "EXPORTS": $ModuleElement$DirectiveKind
static readonly "OPENS": $ModuleElement$DirectiveKind
static readonly "USES": $ModuleElement$DirectiveKind
static readonly "PROVIDES": $ModuleElement$DirectiveKind


public static "values"(): ($ModuleElement$DirectiveKind)[]
public static "valueOf"(arg0: string): $ModuleElement$DirectiveKind
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModuleElement$DirectiveKind$Type = (("exports") | ("provides") | ("opens") | ("uses") | ("requires")) | ($ModuleElement$DirectiveKind);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModuleElement$DirectiveKind_ = $ModuleElement$DirectiveKind$Type;
}}
declare module "packages/javax/lang/model/type/$NoType" {
import {$Annotation, $Annotation$Type} from "packages/java/lang/annotation/$Annotation"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$List, $List$Type} from "packages/java/util/$List"
import {$TypeMirror, $TypeMirror$Type} from "packages/javax/lang/model/type/$TypeMirror"
import {$TypeVisitor, $TypeVisitor$Type} from "packages/javax/lang/model/type/$TypeVisitor"
import {$TypeKind, $TypeKind$Type} from "packages/javax/lang/model/type/$TypeKind"

export interface $NoType extends $TypeMirror {

 "equals"(arg0: any): boolean
 "toString"(): string
 "hashCode"(): integer
 "getAnnotation"<A extends $Annotation>(arg0: $Class$Type<(A)>): A
 "getAnnotationsByType"<A extends $Annotation>(arg0: $Class$Type<(A)>): (A)[]
 "accept"<R, P>(arg0: $TypeVisitor$Type<(R), (P)>, arg1: P): R
 "getKind"(): $TypeKind
 "getAnnotationMirrors"(): $List<(any)>
}

export namespace $NoType {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NoType$Type = ($NoType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NoType_ = $NoType$Type;
}}
declare module "packages/javax/lang/model/type/$ReferenceType" {
import {$Annotation, $Annotation$Type} from "packages/java/lang/annotation/$Annotation"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$List, $List$Type} from "packages/java/util/$List"
import {$TypeMirror, $TypeMirror$Type} from "packages/javax/lang/model/type/$TypeMirror"
import {$TypeVisitor, $TypeVisitor$Type} from "packages/javax/lang/model/type/$TypeVisitor"
import {$TypeKind, $TypeKind$Type} from "packages/javax/lang/model/type/$TypeKind"

export interface $ReferenceType extends $TypeMirror {

 "equals"(arg0: any): boolean
 "toString"(): string
 "hashCode"(): integer
 "getAnnotation"<A extends $Annotation>(arg0: $Class$Type<(A)>): A
 "getAnnotationsByType"<A extends $Annotation>(arg0: $Class$Type<(A)>): (A)[]
 "accept"<R, P>(arg0: $TypeVisitor$Type<(R), (P)>, arg1: P): R
 "getKind"(): $TypeKind
 "getAnnotationMirrors"(): $List<(any)>
}

export namespace $ReferenceType {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ReferenceType$Type = ($ReferenceType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ReferenceType_ = $ReferenceType$Type;
}}
declare module "packages/javax/lang/model/type/$ErrorType" {
import {$Annotation, $Annotation$Type} from "packages/java/lang/annotation/$Annotation"
import {$Element, $Element$Type} from "packages/javax/lang/model/element/$Element"
import {$List, $List$Type} from "packages/java/util/$List"
import {$TypeMirror, $TypeMirror$Type} from "packages/javax/lang/model/type/$TypeMirror"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$DeclaredType, $DeclaredType$Type} from "packages/javax/lang/model/type/$DeclaredType"
import {$TypeVisitor, $TypeVisitor$Type} from "packages/javax/lang/model/type/$TypeVisitor"
import {$TypeKind, $TypeKind$Type} from "packages/javax/lang/model/type/$TypeKind"

export interface $ErrorType extends $DeclaredType {

 "asElement"(): $Element
 "getTypeArguments"(): $List<(any)>
 "getEnclosingType"(): $TypeMirror
 "equals"(arg0: any): boolean
 "toString"(): string
 "hashCode"(): integer
 "getAnnotation"<A extends $Annotation>(arg0: $Class$Type<(A)>): A
 "getAnnotationsByType"<A extends $Annotation>(arg0: $Class$Type<(A)>): (A)[]
 "accept"<R, P>(arg0: $TypeVisitor$Type<(R), (P)>, arg1: P): R
 "getKind"(): $TypeKind
 "getAnnotationMirrors"(): $List<(any)>
}

export namespace $ErrorType {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ErrorType$Type = ($ErrorType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ErrorType_ = $ErrorType$Type;
}}

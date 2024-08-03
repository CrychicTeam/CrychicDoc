export {} // Mark the file as a module, do not remove unless there are other import/exports!
declare global {
interface long extends Number {}
interface integer extends Number {}
interface short extends Number {}
interface byte extends Number {}
interface double extends Number {}
interface float extends Number {}
interface character extends String {}
interface charseq extends String {}
}
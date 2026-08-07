# Add project specific ProGuard rules here.
# No blanket `-keep` needed here: the c4ds SDK's consumer ProGuard rules (inherited
# automatically by every plugin) already keep every `ToolDescriptor` subclass and its
# `(Context)` constructor, ViewModel constructors, and `R$*` classes — the only things the
# host needs to find by reflection/resource lookup. Everything else in this module is free
# to be shrunk/obfuscated.

# MapView exposes an internal listener type that isn't on this module's compile classpath;
# R8 only needs to know it's safe to ignore the reference (nothing in this APK calls it).
-dontwarn vision.combat.c4.view.mode.InputListener

# Give R8's obfuscated classes a package unique to this app. The host loads plugin APKs
# through a parent-first classloader; without this, R8's short obfuscated class names
# (e.g. a.a) collide across the host and every plugin, so the host's classes shadow this
# plugin's identically-named ones and the tool fails at runtime (NoSuchFieldError) instead
# of appearing in the Tools list. See docs/guides/getting-started.md
# (#release-builds-and-obfuscation) for the full explanation.
-repackageclasses vision.combat.c4.ds.sample.polka_dots.obf


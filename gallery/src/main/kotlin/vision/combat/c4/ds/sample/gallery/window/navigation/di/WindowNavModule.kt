package vision.combat.c4.ds.sample.gallery.window.navigation.di

import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.instance
import vision.combat.c4.ds.sample.gallery.window.navigation.WindowNavToolDescriptor
import vision.combat.c4.ds.sample.gallery.window.navigation.data.WindowNavRepository
import vision.combat.c4.ds.sdk.tool.requireQualifiedName

internal val windowNavModule = DI.Module("windowNavModule") {
    bindSingleton {
        WindowNavRepository(instance(arg = requireQualifiedName<WindowNavToolDescriptor>()))
    }
}


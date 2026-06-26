package vision.combat.c4.ds.sample.gallery.window.multiscreen.di

import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.instance
import vision.combat.c4.ds.sample.gallery.window.multiscreen.WindowMultiScreenInteractor
import vision.combat.c4.ds.sample.gallery.window.multiscreen.WindowMultiScreenToolDescriptor
import vision.combat.c4.ds.sample.gallery.window.multiscreen.data.WindowMultiScreenRepository
import vision.combat.c4.ds.sdk.tool.requireQualifiedName

internal val windowMultiScreenModule = DI.Module("windowMultiScreenModule") {
    bindSingleton {
        WindowMultiScreenRepository(instance(arg = requireQualifiedName<WindowMultiScreenToolDescriptor>()))
    }
    bindSingleton {
        WindowMultiScreenInteractor(instance())
    }
}

package vision.combat.c4.ds.sample.gallery.window.multiscreen.di

import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.bindSingletonOf
import org.kodein.di.instance
import vision.combat.c4.ds.sample.gallery.window.multiscreen.domain.WindowMultiScreenInteractor
import vision.combat.c4.ds.sample.gallery.window.multiscreen.WindowMultiScreenToolDescriptor
import vision.combat.c4.ds.sample.gallery.window.multiscreen.data.WindowMultiScreenRepositoryImpl
import vision.combat.c4.ds.sample.gallery.window.multiscreen.domain.repository.WindowMultiScreenRepository
import vision.combat.c4.ds.sdk.tool.requireQualifiedName

internal val windowMultiScreenModule = DI.Module("windowMultiScreenModule") {
    // Bound as the domain-facing interface; the Data-layer impl is an implementation
    // detail behind it (dependency inversion).
    bindSingleton<WindowMultiScreenRepository> {
        WindowMultiScreenRepositoryImpl(instance(arg = requireQualifiedName<WindowMultiScreenToolDescriptor>()))
    }
    bindSingletonOf(::WindowMultiScreenInteractor)
}

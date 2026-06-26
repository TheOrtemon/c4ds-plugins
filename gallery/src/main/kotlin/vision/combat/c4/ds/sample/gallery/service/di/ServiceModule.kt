package vision.combat.c4.ds.sample.gallery.service.di

import org.kodein.di.DI
import org.kodein.di.bindSingleton
import vision.combat.c4.ds.sample.gallery.service.ServiceSharedState

internal val serviceModule = DI.Module("serviceModule") {
    bindSingleton { ServiceSharedState() }
}

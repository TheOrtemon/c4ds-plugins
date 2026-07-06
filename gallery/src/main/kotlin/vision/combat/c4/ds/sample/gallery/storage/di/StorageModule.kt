package vision.combat.c4.ds.sample.gallery.storage.di

import android.content.Context
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.instance
import vision.combat.c4.ds.sample.gallery.storage.StorageToolDescriptor
import vision.combat.c4.ds.sample.gallery.storage.data.PreferencesStorageRepositoryImpl
import vision.combat.c4.ds.sample.gallery.storage.data.db.SampleDatabase
import vision.combat.c4.ds.sample.gallery.storage.domain.repository.PreferencesStorageRepository
import vision.combat.c4.ds.sdk.domain.interactor.CommonSessionStorageInteractor
import vision.combat.c4.ds.sdk.tool.requireQualifiedName

internal val storageModule = DI.Module("storageModule") {
    // Plugin-isolated SharedPreferences — keyed by the tool descriptor's qualified name,
    // exactly as the WindowMultiScreen sample does. Bound as the domain-facing interface;
    // the Data-layer impl is an implementation detail behind it (dependency inversion).
    bindSingleton<PreferencesStorageRepository> {
        PreferencesStorageRepositoryImpl(
            instance(arg = requireQualifiedName<StorageToolDescriptor>()),
        )
    }

    // Room database stored under the user-scoped directory provided by the SDK.
    bindSingleton {
        val context = instance<Context>()
        val storageInteractor = instance<CommonSessionStorageInteractor>()
        SampleDatabase.getInstance(context, storageInteractor.getUserDirectoryPath())
    }

    // Expose the DAO so ViewModels can depend on it directly.
    bindSingleton { instance<SampleDatabase>().noteDao() }
}

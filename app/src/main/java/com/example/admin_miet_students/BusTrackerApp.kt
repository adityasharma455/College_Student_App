package com.example.admin_miet_students

import android.app.Application
import androidx.preference.PreferenceManager
import com.example.admin_miet_students.common.CombinedModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.osmdroid.config.Configuration
import java.io.File

class BusTrackerApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@BusTrackerApp)
            modules(CombinedModules)
        }

        val ctx = applicationContext
        val prefs = PreferenceManager.getDefaultSharedPreferences(ctx)

        // ✅ Load osmdroid configuration properly
        Configuration.getInstance().load(ctx, prefs)
        Configuration.getInstance().userAgentValue = packageName

        // ✅ Use internal cache directory (works on Android 13+)
        val basePath = File(ctx.filesDir, "osmdroid")
        val tileCache = File(ctx.cacheDir, "osmdroid_tiles")

        basePath.mkdirs()
        tileCache.mkdirs()

        Configuration.getInstance().osmdroidBasePath = basePath
        Configuration.getInstance().osmdroidTileCache = tileCache
    }
}

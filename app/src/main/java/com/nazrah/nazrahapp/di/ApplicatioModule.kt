package com.nazrah.nazrahapp.di

import android.content.Context
import com.nazrah.nazrahapp.preferences.Preferences
import com.nazrah.nazrahapp.preferences.PreferencesWrapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object ApplicationModule {

    @Singleton
    @Provides
    internal fun providesPreferences(preferenceWrapper: PreferencesWrapper): Preferences {
        return Preferences(preferenceWrapper)
    }

    @Singleton
    @Provides
    internal fun providesPreferencesWrapper(@ApplicationContext context: Context): PreferencesWrapper {
        return PreferencesWrapper(context)
    }
}
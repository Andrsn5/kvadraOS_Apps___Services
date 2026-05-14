package dev.andre.kvadraos_apps___services.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.andre.kvadraos_apps___services.common.AppCoroutineDispatcher
import dev.andre.kvadraos_apps___services.common.AppCoroutineDispatcherImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DispatchersModule {
    @Provides
    @Singleton
    fun provideDispatcher(): AppCoroutineDispatcher = AppCoroutineDispatcherImpl()
}

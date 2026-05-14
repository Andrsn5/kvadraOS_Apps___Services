package dev.andre.kvadraos_apps___services.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.andre.kvadraos_apps___services.common.AppCoroutineDispatcher
import dev.andre.kvadraos_apps___services.data.ContactRepositoryImpl
import dev.andre.kvadraos_apps___services.domain.repository.ContactRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ContactModule {
    @Provides
    @Singleton
    fun provideContactRepository(
        @ApplicationContext context: Context,
        dispatcher: AppCoroutineDispatcher
    ): ContactRepository = ContactRepositoryImpl(
        contentResolver = context.contentResolver,
        dispatcher = dispatcher.io
    )
}
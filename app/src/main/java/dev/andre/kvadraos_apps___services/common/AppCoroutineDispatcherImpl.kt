package dev.andre.kvadraos_apps___services.common

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class AppCoroutineDispatcherImpl : AppCoroutineDispatcher {
    override val io: CoroutineDispatcher = Dispatchers.IO
}
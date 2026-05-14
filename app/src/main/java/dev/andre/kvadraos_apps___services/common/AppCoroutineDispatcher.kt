package dev.andre.kvadraos_apps___services.common

import kotlinx.coroutines.CoroutineDispatcher

interface AppCoroutineDispatcher {
    val io: CoroutineDispatcher
}

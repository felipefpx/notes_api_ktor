package me.porge.notes.helpers

import org.koin.core.KoinComponent
import org.koin.core.get
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier

object KoinInjector : KoinComponent {
    inline fun <reified T> koinGet(
        qualifier: Qualifier? = null,
        noinline parameters: ParametersDefinition? = null
    ) = get<T>(qualifier = qualifier, parameters = parameters)
}

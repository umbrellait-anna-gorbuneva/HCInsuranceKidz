package com.umbrellait.core.livedata_helpers

import androidx.lifecycle.Observer

class EventObserver<T>(private val observe: (T) -> Unit) : Observer<Event<T>> {
    override fun onChanged(event: Event<T>?) {
        event?.getIfPending()?.let { value ->
            observe(value)
        }
    }
}
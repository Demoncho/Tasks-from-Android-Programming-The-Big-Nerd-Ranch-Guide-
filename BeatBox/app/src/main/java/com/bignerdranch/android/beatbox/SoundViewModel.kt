package com.bignerdranch.android.beatbox

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable

class SoundViewModel(private val beatBox: BeatBox) : BaseObservable()  {
    var sound: Sound? = null
        set(sound) {
            field = sound
            notifyChange()
        }

    fun onButtonClicked() {
        sound?.let {
            beatBox.play(it)
        }
    }

    @get:Bindable
    val title: String?
        get() = sound?.name
}
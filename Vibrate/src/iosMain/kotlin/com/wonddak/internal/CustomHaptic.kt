package com.wonddak.internal

import kotlin.concurrent.atomics.AtomicBoolean
import kotlin.concurrent.atomics.ExperimentalAtomicApi
import kotlinx.cinterop.ExperimentalForeignApi
import platform.CoreHaptics.CHHapticDynamicParameter
import platform.CoreHaptics.CHHapticEngine
import platform.CoreHaptics.CHHapticEvent
import platform.CoreHaptics.CHHapticEventParameter
import platform.CoreHaptics.CHHapticEventParameterIDHapticIntensity
import platform.CoreHaptics.CHHapticEventTypeHapticContinuous
import platform.CoreHaptics.CHHapticPattern
import platform.CoreHaptics.CHHapticPatternPlayerProtocol

@OptIn(ExperimentalForeignApi::class, ExperimentalAtomicApi::class)
internal class CustomHaptic {
    private var engine: CHHapticEngine? = null
    private var player: CHHapticPatternPlayerProtocol? = null
    // Core Haptics can invoke reset callbacks off the main thread.
    private val needsReset = AtomicBoolean(false)

    fun play(pulses: List<Pulse>) {
        var position = 0.0
        val events = pulses.map { pulse ->
            position += pulse.delayMillis / 1000.0
            // Core Haptics caps each continuous event at 30 seconds.
            val duration = pulse.durationMillis.coerceAtMost(30_000L) / 1000.0
            CHHapticEvent(
                eventType = CHHapticEventTypeHapticContinuous,
                parameters = listOf(CHHapticEventParameter(
                    parameterID = CHHapticEventParameterIDHapticIntensity,
                    value = pulse.strength
                )),
                relativeTime = position,
                duration = duration
            ).also { position += pulse.durationMillis / 1000.0 }
        }

        val currentEngine = engine ?: CHHapticEngine(null, null).also {
            it.setResetHandler { needsReset.store(true) }
            it.setAutoShutdownEnabled(true)
            engine = it
        }
        if (!currentEngine.startAndReturnError(null)) return

        val pattern = CHHapticPattern(
            events = events,
            parameters = emptyList<CHHapticDynamicParameter>(),
            error = null
        )
        val nextPlayer = currentEngine.createPlayerWithPattern(pattern, null) ?: return
        stop()
        if (nextPlayer.startAtTime(0.0, null)) player = nextPlayer
    }

    fun stop() {
        if (!needsReset.exchange(false)) player?.stopAtTime(0.0, null)
        player = null
    }
}

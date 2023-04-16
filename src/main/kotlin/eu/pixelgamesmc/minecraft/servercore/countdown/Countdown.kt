package eu.pixelgamesmc.minecraft.servercore.countdown

import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitRunnable
import java.time.Duration

class Countdown(
    private val plugin: Plugin,
    var initialDuration: Duration,
    var callback: ((Countdown, Duration) -> Duration?)? = null,
    var endCallback: ((Countdown) -> Unit)? = null,
    var cancelCallback: ((Countdown) -> Unit)? = null,
    var tickDuration: Duration = Duration.ofSeconds(1)
): Runnable {

    private lateinit var currentDuration: Duration
    private lateinit var runnable: BukkitRunnable

    override fun run() {
        if (!runnable.isCancelled) {
            if (currentDuration.isZero) {
                runnable.cancel()

                endCallback?.invoke(this)
            } else {
                val duration = callback?.invoke(this, currentDuration)

                currentDuration = duration ?: currentDuration.minus(Duration.ofSeconds(1))
            }
        }
    }

    fun start() {
        currentDuration = initialDuration
        runnable = object : BukkitRunnable() {
            override fun run() {
                this@Countdown.run()
            }
        }

        runnable.runTaskTimer(plugin, 0, tickDuration.toSeconds() * 20)
    }

    fun cancel() {
        runnable.cancel()

        cancelCallback?.invoke(this)
    }
}
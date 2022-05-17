package com.jeanloth.project.android.kotlin.axounaut.workers

import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import com.jeanloth.project.android.kotlin.axounaut.R
import com.jeanloth.project.android.kotlin.domain.usescases.usecases.command.GetCommandsByStatusCodeUseCase
import com.jeanloth.project.android.kotlin.domain_models.entities.CommandStatusType
import org.koin.java.KoinJavaComponent.inject
import kotlin.random.Random

class NotificationWorker(
    private val context: Context,
    private val workerParameters: WorkerParameters
) : CoroutineWorker(context, workerParameters) {

    private val getCommandsByStatusUseCase by inject(GetCommandsByStatusCodeUseCase::class.java)

    override suspend fun doWork(): Result {
        val status = listOf(CommandStatusType.TO_DO)
        // Get commands by status
        val commands = getCommandsByStatusUseCase.countByStatus(status)

        status.forEach {
            if(commands[it.code] ?: 0 > 0){
                // Show notification with the number of command corresponding to each status
                showNotification(buildContentText(commands[it.code] ?: 0, it.label))
            }

            // Create/Update message to display in message center
        }
        return Result.success()
    }

    private fun buildContentText(count: Int, type: String) : String{
        return "Vous avez $count commandes $type"
    }

    private suspend fun showNotification(content : String){
        setForeground(
            ForegroundInfo(
                Random.nextInt(),
                NotificationCompat.Builder(context, "notification_channel")
                    .setSmallIcon(R.drawable.logo_kb)
                    .setContentText(content)
                    .setContentTitle("Axounaut Alert")
                    .build()
            )
        )
    }
}
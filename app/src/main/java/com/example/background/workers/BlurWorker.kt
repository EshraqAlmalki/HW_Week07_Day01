package com.example.background.workers

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.text.TextUtils
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.background.KEY_IMAGE_URI
import com.example.background.R

private const val TAG = "BlurWorker"
class BlurWorker(ctx: Context, params:WorkerParameters) : Worker(ctx,params) {
    override fun doWork(): Result {



        val appContext = applicationContext

        val resourceUri = inputData.getString(KEY_IMAGE_URI)


        makeStatusNotification("Blurring image", appContext)
        sleep()

        return try {


            if (TextUtils.isEmpty(resourceUri)) {

                Log.e(TAG, "Invalid input uri")
            }

            val resolver = appContext.contentResolver

            val picture = BitmapFactory.decodeStream(
                resolver
                    .openInputStream(Uri.parse(resourceUri))
            )

            val output = blurBitmap(picture, appContext)


            val outputUri = writeBitmapToFile(appContext, output)

           // Result.success()
            val outputData = workDataOf(KEY_IMAGE_URI to outputUri.toString())

            Result.success(outputData)

        } catch (throwable: Throwable) {
            Log.e(TAG, "error applying blur")
            Result.failure()
        }



    }
}
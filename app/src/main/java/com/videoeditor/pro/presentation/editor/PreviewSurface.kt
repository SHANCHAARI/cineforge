package com.videoeditor.pro.presentation.editor

import android.view.Surface
import android.view.SurfaceHolder
import android.view.SurfaceView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import timber.log.Timber

object NativeBridge {
    external fun nativeSetSurface(surface: Surface)
    external fun nativeReleaseSurface()
    external fun nativeSetColorGrading(brightness: Float, contrast: Float, saturation: Float)
    external fun nativeAddMediaClip(id: String, path: String, startTimeMs: Long, durationMs: Long)
    external fun nativeRemoveMediaClip(id: String)
    external fun nativeSetPlayheadMs(timeMs: Long)
    external fun nativeSplitClip(id: String, timeMs: Long)
    external fun nativeMoveClip(id: String, newStartTimeMs: Long)
}

@Composable
fun PreviewSurface(
    modifier: Modifier = Modifier
) {
    AndroidView(
        factory = { context ->
            SurfaceView(context).apply {
                holder.addCallback(object : SurfaceHolder.Callback {
                    override fun surfaceCreated(holder: SurfaceHolder) {
                        Timber.d("Surface created")
                        NativeBridge.nativeSetSurface(holder.surface)
                    }

                    override fun surfaceChanged(
                        holder: SurfaceHolder,
                        format: Int,
                        width: Int,
                        height: Int
                    ) {
                        Timber.d("Surface changed: $width x $height")
                        // Native engine handles resize processing via the window loop usually, 
                        // or we can pass explicit verify.
                        NativeBridge.nativeSetSurface(holder.surface)
                    }

                    override fun surfaceDestroyed(holder: SurfaceHolder) {
                        Timber.d("Surface destroyed")
                        NativeBridge.nativeReleaseSurface()
                    }
                })
            }
        },
        modifier = modifier.fillMaxSize()
    )
}

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

package com.example.kenobiscanner.utils

import android.opengl.GLSurfaceView
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class GLRenderer : GLSurfaceView.Renderer {
    override fun onDrawFrame(p0: GL10?) {
        p0?.glClearColor(1.0f, 0.0f, 0.0f, 1.0f)
    }

    override fun onSurfaceChanged(p0: GL10?, w: Int, h: Int) {
        p0?.glViewport(0, 0, w, h)
    }

    override fun onSurfaceCreated(p0: GL10?, p1: EGLConfig?) {
        p0?.glClear(GL10.GL_COLOR_BUFFER_BIT or GL10.GL_DEPTH_BUFFER_BIT)
    }

}
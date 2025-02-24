package com.example.apirickmorty.model

import android.graphics.Bitmap
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Shader
import com.squareup.picasso.Transformation

class OvalTransformation : Transformation {
    override fun transform(source: Bitmap): Bitmap {
        val width = source.width
        val height = source.height
        val output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

        val canvas = Canvas(output)
        val paint = Paint()
        paint.isAntiAlias = true
        paint.shader = BitmapShader(source, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)

        val rectF = RectF(0f, 0f, width.toFloat(), height.toFloat())
        canvas.drawOval(rectF, paint)

        if (output != source) {
            source.recycle()
        }
        return output
    }

    override fun key(): String {
        return "OvalTransformation"
    }
}
package com.dabong.bubble_level

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.BitmapDrawable
import android.util.AttributeSet
import android.view.View
import androidx.core.graphics.drawable.toBitmap
import kotlin.math.abs

class LevelView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defstyleAttr: Int = 0
) :
    View(context, attrs, defstyleAttr) {
    private var xCoordinate: Float = 0f
    private var yCoordinate: Float = 0f
    private var xAngle: Float = 0f
    private var yAngle: Float = 0f

    private var midX: Float = 0f
    private var midY: Float = 0f

    private var horizontalX = 0f
    private var horizontalY = 0f
    private var verticalX = 0f
    private var verticalY = 0f

    private var radius: Float = 0f
    private var fillPaint = Paint()
    private var storkePaint = Paint()
    private var fontPaint = Paint()


    init {
        fillPaint = Paint().apply {
            color = resources.getColor(R.color.circleFill)
            style = Paint.Style.FILL
            colorFilter
        }
        storkePaint = Paint().apply {
            color = resources.getColor(R.color.stroke)
            style = Paint.Style.STROKE
        }
        fontPaint = Paint().apply {
            color = resources.getColor(R.color.stroke)
            style = Paint.Style.FILL
        }
    }

    fun init(_radius: Float, _midX: Float, _midY: Float) {
        radius = _radius
        midX = _midX
        midY = _midY

        horizontalX = midX
        horizontalY = radius * 2
        verticalX = radius * 2
        verticalY = midY
        fontPaint.textSize = radius

    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //background
        canvas.drawCircle(midX, midY, radius * 7, fillPaint)
        canvas.drawCircle(midX, midY, radius * 7, storkePaint)
        canvas.drawCircle(midX, midY, radius * 5, storkePaint)
        canvas.drawCircle(midX, midY, radius * 3, storkePaint)
        canvas.drawCircle(midX, midY, radius, storkePaint)
        canvas.drawRoundRect(
            horizontalX - radius * 7,
            horizontalY - radius,
            horizontalX + radius * 7,
            horizontalY + radius,
            radius,
            radius,
            fillPaint
        )
        canvas.drawRoundRect(
            horizontalX - radius * 7,
            horizontalY - radius,
            horizontalX + radius * 7,
            horizontalY + radius,
            radius,
            radius,
            storkePaint
        )
        canvas.drawRoundRect(
            verticalX - radius,
            verticalY - radius * 7,
            verticalX + radius,
            verticalY + radius * 7,
            radius,
            radius,
            fillPaint
        )
        canvas.drawRoundRect(
            verticalX - radius,
            verticalY - radius * 7,
            verticalX + radius,
            verticalY + radius * 7,
            radius,
            radius,
            storkePaint
        )
        canvas.drawRect(
            horizontalX - radius,
            horizontalY - radius,
            horizontalX + radius,
            horizontalY + radius,
            storkePaint
        )
        canvas.drawRect(
            verticalX - radius,
            verticalY - radius,
            verticalX + radius,
            verticalY + radius,
            storkePaint
        )
        canvas.drawText(
            String.format("X = %.1f", xAngle),
            verticalX + radius,
            horizontalY + radius * 2,
            fontPaint
        )
        canvas.drawText(
            String.format("Y = %.1f", -yAngle),
            verticalX + radius,
            horizontalY + radius * 3,
            fontPaint
        )
        canvas.drawLine(midX, midY + radius * 7, midX, horizontalY + radius, storkePaint)
        canvas.drawLine(midX + radius * 7, midY, verticalX + radius, midY, storkePaint)

        //bubble
        val d = resources.getDrawable(R.drawable.ic_bubble)
        val bitmap = BitmapDrawable(
            getResources(),
            Bitmap.createScaledBitmap(
                d.toBitmap(),
                (radius * 2.2).toInt(),
                (radius * 2.2).toInt(),
                true
            )
        ).toBitmap()
        val d_center = resources.getDrawable(R.drawable.ic_bubble_center)
        val bitmap_center = BitmapDrawable(
            getResources(),
            Bitmap.createScaledBitmap(
                d_center.toBitmap(),
                (radius * 2.2).toInt(),
                (radius * 2.2).toInt(),
                true
            )
        ).toBitmap()

        if (abs(xAngle) < 1.0) {
            canvas.drawBitmap(
                bitmap_center,
                (horizontalX + xCoordinate - radius * 1.1).toFloat(),
                (horizontalY - radius * 1.1).toFloat(),
                null
            )
        } else {
            canvas.drawBitmap(
                bitmap,
                (horizontalX + xCoordinate - radius * 1.1).toFloat(),
                (horizontalY - radius * 1.1).toFloat(),
                null
            )

        }
        if (abs(yAngle) < 1.0) {
            canvas.drawBitmap(
                bitmap_center,
                (verticalX - radius * 1.1).toFloat(),
                (verticalY + yCoordinate - radius * 1.1).toFloat(),
                null
            )
        } else {
            canvas.drawBitmap(
                bitmap,
                (verticalX - radius * 1.1).toFloat(),
                (verticalY + yCoordinate - radius * 1.1).toFloat(),
                null
            )
        }
        if (abs(xAngle) < 1.0 && abs(yAngle) < 1.0) {
            canvas.drawBitmap(
                bitmap_center,
                (midX + xCoordinate - radius * 1.1).toFloat(),
                (midY + yCoordinate - radius * 1.1).toFloat(),
                null
            )
        } else {
            canvas.drawBitmap(
                bitmap,
                (midX + xCoordinate - radius * 1.1).toFloat(),
                (midY + yCoordinate - radius * 1.1).toFloat(),
                null
            )
        }

    }


    fun onSensorEvent(xAngle: Float, yAngle: Float) {
        this.xAngle = xAngle
        this.yAngle = -yAngle
        xCoordinate = xAngle / 90 * radius * 7
        yCoordinate = this.yAngle / 90 * radius * 7
        invalidate()
    }

}
package com.swen549.touchanalytics.util

import com.swen549.touchanalytics.data.Feature
import kotlin.math.atan2
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sqrt

data class TouchPoint(
    val x: Float,
    val y: Float,
    val timestamp: Long,
    val pressure: Float,
    val size: Float
)

data class Stroke(
    val userId: Int = 0,
    val startTime: Long = 0,
    val endTime: Long = 0,
    val points: List<TouchPoint> = emptyList()
) {
    fun toFeature(): Feature {
        if (points.isEmpty()) return Feature(userId = userId)

        val first = points.first()
        val last = points.last()

        // Position and Duration
        val startX = first.x
        val stopX = last.x
        val startY = first.y
        val stopY = last.y
        val strokeDuration = (endTime - startTime).toFloat()

        // Bounding Box Area
        val minX = points.minOf { it.x }
        val maxX = points.maxOf { it.x }
        val minY = points.minOf { it.y }
        val maxY = points.maxOf { it.y }
        val midStrokeArea = (maxX - minX) * (maxY - minY)

        // Mid-stroke Pressure
        val midStrokePressure =
            if (points.size >= 3) {
                points.subList(points.size / 4, 3 * points.size / 4)
                    .map { it.pressure }
                    .average()
                    .toFloat()
            } else 0f

        // Directions
        val directionEndToEnd =
            if (points.size >= 2) {
                atan2((last.y - first.y).toDouble(), (last.x - first.x).toDouble()).toFloat()
            } else 0f

        val averageDirection =
            if (points.size >= 2) {
                points.zipWithNext { a, b ->
                    atan2((b.y - a.y).toDouble(), (b.x - a.x).toDouble())
                }.average().toFloat()
            } else 0f

        // Velocities
        val averageVelocity =
            if (points.size >= 2) {
                val totalDistance = points.zipWithNext { a, b ->
                    sqrt((b.x - a.x).pow(2) + (b.y - a.y).pow(2))
                }.sum()
                val totalTime = last.timestamp - first.timestamp
                if (totalTime > 0) totalDistance / totalTime else 0f
            } else 0f

        val pairwiseVelocityPercentile =
            if (points.size >= 2) {
                val velocities = points.zipWithNext { a, b ->
                    val dt = b.timestamp - a.timestamp
                    if (dt > 0) {
                        val distance = sqrt((b.x - a.x).pow(2) + (b.y - a.y).pow(2))
                        distance / dt
                    } else null
                }.filterNotNull().sorted()

                if (velocities.isNotEmpty()) {
                    val index = ((velocities.size - 1) * 0.5f).roundToInt()
                    velocities[index]
                } else 0f
            } else 0f

        return Feature(
            userId = userId,
            startX = startX,
            stopX = stopX,
            startY = startY,
            stopY = stopY,
            strokeDuration = strokeDuration,
            midStrokeArea = midStrokeArea,
            midStrokePressure = midStrokePressure,
            directionEndToEnd = directionEndToEnd,
            averageDirection = averageDirection,
            averageVelocity = averageVelocity,
            pairwiseVelocityPercentile = pairwiseVelocityPercentile
        )
    }
}
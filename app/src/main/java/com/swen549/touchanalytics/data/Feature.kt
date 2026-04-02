package com.swen549.touchanalytics.data

data class Feature(
    val userId: Int = 0,
    val startX: Float = 0f,
    val stopX: Float = 0f,
    val startY: Float = 0f,
    val stopY: Float = 0f,
    val strokeDuration: Float = 0f,
    val midStrokeArea: Float = 0f,
    val midStrokePressure: Float = 0f,
    val directionEndToEnd: Float = 0f,
    val averageDirection: Float = 0f,
    val averageVelocity: Float = 0f,
    val pairwiseVelocityPercentile: Float = 0f
)

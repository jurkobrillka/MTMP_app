package com.example.mtmp_app

import java.io.Serializable


data class ProjectilePoint(
    val time: Float,
    val x: Float,
    val y: Float
) : Serializable
package com.example.mtmp_app

import retrofit2.http.Body
import retrofit2.http.POST

interface CalculationApi {
    @POST("calculate/trajectory")
    suspend fun calculateTrajectory(@Body request: CalculationRequest): retrofit2.Response<List<ProjectilePoint>>

    @POST("calculate/additionalInfo")
    suspend fun calculateAdditionalInfo(@Body request: CalculationRequest): retrofit2.Response<FlightAdditionalInfo>
}

package com.example.admin_miet_students.domain.repo

import com.example.admin_miet_students.common.ResultState
import com.example.admin_miet_students.domain.models.LocationDataModel
import kotlinx.coroutines.flow.Flow

interface BusRepo {
    fun listenToBusLocation(busId: String): Flow<ResultState<LocationDataModel>>
    fun checkBusExists(busId: String): Flow<ResultState<Boolean>>
}
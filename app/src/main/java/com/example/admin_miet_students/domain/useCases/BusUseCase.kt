package com.example.admin_miet_students.domain.useCases

import com.example.admin_miet_students.common.ResultState
import com.example.admin_miet_students.domain.models.LocationDataModel
import com.example.admin_miet_students.domain.repo.BusRepo
import kotlinx.coroutines.flow.Flow

class BusUseCase(
    private val repo: BusRepo
) {

    fun listenToBusLocation(busId: String): Flow<ResultState<LocationDataModel>> =
        repo.listenToBusLocation(busId)

    fun checkBusExists(busId: String): Flow<ResultState<Boolean>> =
        repo.checkBusExists(busId)
}

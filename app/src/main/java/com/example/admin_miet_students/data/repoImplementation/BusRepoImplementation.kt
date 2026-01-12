package com.example.admin_miet_students.data.repoImplementation


import com.example.admin_miet_students.common.BUSES_PATH
import com.example.admin_miet_students.common.ResultState
import com.example.admin_miet_students.domain.models.LocationDataModel
import com.example.admin_miet_students.domain.repo.BusRepo
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class BusRepoImplementation(
    private val firestore: FirebaseFirestore
) : BusRepo {

    override fun listenToBusLocation(busId: String): Flow<ResultState<LocationDataModel>> =
        callbackFlow {

            trySend(ResultState.Loading)

            val docRef = firestore.collection(BUSES_PATH).document(busId)

            val listener = docRef.addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(ResultState.Error(error.message.toString()))
                    return@addSnapshotListener
                }

                if (snapshot == null || !snapshot.exists()) {
                    trySend(ResultState.Error("Bus not found"))
                    return@addSnapshotListener
                }

                val latitude = snapshot.getDouble("latitude")
                val longitude = snapshot.getDouble("longitude")
                val lastUpdated = snapshot.getLong("lastUpdated") ?: 0L

                if (latitude != null && longitude != null) {
                    val location = LocationDataModel(
                        latitude = latitude,
                        longitude = longitude,
                        lastUpdated = lastUpdated
                    )
                    trySend(ResultState.Success(location))
                } else {
                    trySend(ResultState.Error("Invalid location data"))
                }
            }

            awaitClose { listener.remove() }
        }

    override fun checkBusExists(busId: String): Flow<ResultState<Boolean>> =
        callbackFlow {

            trySend(ResultState.Loading)

            val docRef = firestore.collection(BUSES_PATH).document(busId)

            val listener = docRef.addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(ResultState.Error(error.message.toString()))
                    return@addSnapshotListener
                }

                trySend(ResultState.Success(snapshot != null && snapshot.exists()))
            }

            awaitClose { listener.remove() }
        }
}

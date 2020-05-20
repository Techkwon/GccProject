package woogear.kwon.gccproject.viewmodel

import android.app.Application
import androidx.lifecycle.*
import androidx.paging.toLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import woogear.kwon.gccproject.dao.PlaceDb
import woogear.kwon.gccproject.model.GccResponse
import woogear.kwon.gccproject.model.Place
import woogear.kwon.gccproject.paging.PlacesDataSourceFactory
import woogear.kwon.gccproject.utils.APIClient
import woogear.kwon.gccproject.utils.GccService
import woogear.kwon.gccproject.utils.Listing
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class PlacesViewModel(
    application: Application,
    savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {
    private val api: GccService by lazy { APIClient.getClient().create(GccService::class.java) }
    private val ioExecutor: Executor = Executors.newFixedThreadPool(5)

    private val db = PlaceDb.getInstance(application)
    private val placeDao = db.placeDao()

    private val requestResult = savedStateHandle.getLiveData<String>(KEY_PROJECT).map {
        getPlace()
    }

    val places = requestResult.switchMap { it.pagedList }
    val networkState = requestResult.switchMap { it.networkState }

    companion object {
        const val KEY_PROJECT = "gccproject"
        const val DEFAULT_PROJECT = "defaultgccproject"
    }

    init {
        if (!savedStateHandle.contains(KEY_PROJECT))
            savedStateHandle.set(KEY_PROJECT, DEFAULT_PROJECT)
    }

    private fun getPlace(): Listing<Place> {
        val sourceFactory = PlacesDataSourceFactory(api, ioExecutor, getApplication())
        val livePagedList = sourceFactory.toLiveData(
            pageSize = 20,
            fetchExecutor = ioExecutor
        )
        return Listing(
            pagedList = livePagedList,
            networkState = sourceFactory.sourceLiveData.switchMap { it.networkState },
            retry = { sourceFactory.sourceLiveData.value?.retryAllFailed() }
        )
    }

    fun getSavedPlaces(): LiveData<List<Place>> {
        return placeDao.favoritePlacesByTimeAsc()
    }

    fun updatePlace(place: Place, isToSave: Boolean) = viewModelScope.launch(Dispatchers.IO) {
        if (isToSave) {
            place.saveTime = System.currentTimeMillis()
            placeDao.save(place)
        } else placeDao.delete(place)
    }
}
package woogear.kwon.gccproject.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import woogear.kwon.gccproject.dao.PlaceDb
import woogear.kwon.gccproject.model.Place

class PlaceInfoViewModel(application: Application) : AndroidViewModel(application) {

    // you can use application context
    private val db = PlaceDb.getInstance(application)
    private val placeDao = db.placeDao()

    fun updatePlace(place: Place, isToSave: Boolean) = viewModelScope.launch(Dispatchers.IO) {
        if (isToSave) {
            place.saveTime = System.currentTimeMillis()
            placeDao.save(place)
        } else placeDao.delete(place)
    }
}
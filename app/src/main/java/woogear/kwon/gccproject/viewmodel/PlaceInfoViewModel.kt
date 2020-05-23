package woogear.kwon.gccproject.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import woogear.kwon.gccproject.dao.PlaceDb

class PlaceInfoViewModel(application: Application) : AndroidViewModel(application) {

    // you can use application context
    private val db = PlaceDb.getInstance(application)
}
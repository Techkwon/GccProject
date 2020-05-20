package woogear.kwon.gccproject.paging

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import woogear.kwon.gccproject.model.Place
import woogear.kwon.gccproject.utils.GccService
import java.util.concurrent.Executor

class PlacesDataSourceFactory(
    private val gccApi: GccService,
    private val retryExecutor: Executor,
    private val application: Application
) : DataSource.Factory<Int, Place>() {
    val sourceLiveData = MutableLiveData<PlacesDataSource>()
    override fun create(): DataSource<Int, Place> {
        val source = PlacesDataSource(gccApi, retryExecutor, application)
        sourceLiveData.postValue(source)
        return source
    }
}
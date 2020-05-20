package woogear.kwon.gccproject.paging

import androidx.annotation.MainThread
import androidx.paging.PagedList
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import woogear.kwon.gccproject.model.GccResponse
import woogear.kwon.gccproject.model.Place
import woogear.kwon.gccproject.utils.GccService
import woogear.kwon.gccproject.utils.createStatusLiveData
import java.util.concurrent.Executor

class PlaceBoundaryCallback(
    private val gccService: GccService,
    private val ioExecutor: Executor,
    private val handleResponse: (GccResponse?) -> Unit,
    private val pageSize: Int
) : PagedList.BoundaryCallback<Place>() {

    val helper = PagingRequestHelper(ioExecutor)
    val networkState = helper.createStatusLiveData()
    var page = 1

    @MainThread
    override fun onZeroItemsLoaded() {
        helper.runIfNotRunning(PagingRequestHelper.RequestType.INITIAL) {
            gccService.getPlaces("1.json").enqueue(createWebServiceCallback(it))
        }
    }

    override fun onItemAtEndLoaded(itemAtEnd: Place) {
        if (page < 4) {
            helper.runIfNotRunning(PagingRequestHelper.RequestType.AFTER) {
                gccService.getPlaces("$page.json").enqueue(createWebServiceCallback(it))
            }
        }
    }

    private fun insertItems(response: Response<GccResponse>, it: PagingRequestHelper.Request.Callback) {
        ioExecutor.execute {
            handleResponse(response.body())
            it.recordSuccess()
        }
    }

    private fun createWebServiceCallback(it: PagingRequestHelper.Request.Callback) : Callback<GccResponse> {
        return object : Callback<GccResponse> {
            override fun onFailure(call: Call<GccResponse>, t: Throwable) {
                it.recordFailure(t)
            }

            override fun onResponse(call: Call<GccResponse>, response: Response<GccResponse>) {
                insertItems(response, it)
                page++
            }
        }
    }
}
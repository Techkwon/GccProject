package woogear.kwon.gccproject.paging

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import androidx.paging.PositionalDataSource
import woogear.kwon.gccproject.R
import woogear.kwon.gccproject.model.GccResponse
import woogear.kwon.gccproject.model.Place
import woogear.kwon.gccproject.utils.GccService
import woogear.kwon.gccproject.utils.NetworkState
import java.io.IOException
import java.util.concurrent.Executor

class PlacesDataSource(
    private val gccService: GccService,
    private val retryExecutor: Executor,
    private val context: Application
) : PositionalDataSource<Place>() {
    private var page = 1
    private var retry: (() -> Any)? = null

    val networkState = MutableLiveData<NetworkState>()

    fun retryAllFailed() {
        val prevRetry = retry
        retry = null
        prevRetry?.let {
            retryExecutor.execute { it.invoke() }
        }
    }

    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<Place>) {
        networkState.postValue(NetworkState.LOADING)

        val request = gccService.getPlaces("1.json")

        try {
            val response = request.execute()
            if (response.isSuccessful) {
                val result = response.body()

                if (result?.msg == context.getString(R.string.gcc_success_msg)) {
                    val data = result.data.product
                    retry = null
                    networkState.postValue(NetworkState.LOADED)
                    callback.onResult(data, params.requestedStartPosition, result.data.totalCount)
                    page++
                } else networkState.postValue(NetworkState.error(context.getString(R.string.wrong_with_request_msg)))
            } else networkState.postValue(NetworkState.error(context.getString(R.string.somerthing_went_wrong_msg)))
        } catch (ioException: IOException) {
            retry = { loadInitial(params, callback) }

            val error = NetworkState.error(ioException.message ?: context.getString(R.string.unknown_error_msg))
            networkState.postValue(error)
        }
    }

    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<Place>) {
        networkState.postValue(NetworkState.LOADING)
        val request = gccService.getPlaces("$page.json")

        try {
            val response = request.execute()
            if (response.isSuccessful) {
                val result = response.body()

                if (result?.msg == context.getString(R.string.gcc_success_msg)) {
                    val data = result.data.product
                    retry = null
                    networkState.postValue(NetworkState.LOADED)
                    callback.onResult(data)
                    page ++
                } else NetworkState.error(context.getString(R.string.wrong_with_request_msg))
            } else networkState.postValue(NetworkState.error(context.getString(R.string.somerthing_went_wrong_msg)))
        } catch (ioException: IOException) {
            retry = { loadRange(params, callback) }
            val error = NetworkState.error(ioException.message ?: context.getString(R.string.unknown_error_msg))
            networkState.postValue(error)
        }
    }
}
package woogear.kwon.gccproject.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_total.*
import kotlinx.android.synthetic.main.layout_network_disconnected.*
import woogear.kwon.gccproject.R
import woogear.kwon.gccproject.ui.adapter.TotalListAdapter
import woogear.kwon.gccproject.utils.NetworkState.Companion.LOADED
import woogear.kwon.gccproject.utils.NetworkState.Companion.LOADING

class TotalListFragment : BaseFragment() {
    private lateinit var adapter: TotalListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_total, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        getPlaces()
    }

    private fun getPlaces() {
        viewModel.places.observe(this, Observer { places ->
            adapter.submitList(places)
        })

        viewModel.getSavedPlaces().observe(this, Observer {
            adapter.updateSavedPlaces(it)
        })

        viewModel.networkState.observe(this, Observer { networkState ->
            when(networkState) {
                LOADING -> this.progress_bar_holder_fragment.visibility = View.VISIBLE
                LOADED -> this.progress_bar_holder_fragment.visibility = View.GONE
                else -> networkState.msg?.let { showErrorMsg(it) }
            }
        })
    }

    private fun initAdapter() {
        val glide = Glide.with(this)
        adapter = TotalListAdapter(
            viewModel,
            glide,
            attachedActivity
        )

        this.rv_total.adapter = adapter
        this.rv_total.layoutManager = LinearLayoutManager(activity)

        val divider = DividerItemDecoration(activity, LinearLayoutManager(activity).orientation)
        activity?.getDrawable(R.drawable.recycer_divider)?.let { divider.setDrawable(it) }
        this.rv_total.addItemDecoration(divider)
    }

    private fun showErrorMsg(msg: String) {
        this.progress_bar_holder_fragment.visibility = View.GONE
        this.network_error_view_holder.visibility = View.VISIBLE
        this.tv_network_disconnected.text = msg
    }
}
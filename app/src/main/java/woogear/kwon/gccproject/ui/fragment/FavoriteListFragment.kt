package woogear.kwon.gccproject.ui.fragment

import android.os.Bundle
import android.view.*
import android.widget.PopupMenu
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_favorite.*
import woogear.kwon.gccproject.R
import woogear.kwon.gccproject.ui.adapter.FavoriteListAdapter
import woogear.kwon.gccproject.utils.SortType
import woogear.kwon.gccproject.viewmodel.PlacesViewModel
import java.lang.Exception

class FavoriteListFragment : BaseFragment() {
    private lateinit var adapter: FavoriteListAdapter
    private lateinit var wrapper: ContextThemeWrapper
    private lateinit var popup: PopupMenu

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewModel = activity?.run {  ViewModelProviders.of(this).get(PlacesViewModel::class.java) } ?: throw Exception("Invalid Activity")
        return inflater.inflate(R.layout.fragment_favorite, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        initSort()
        getFavoritePlaces()
        setViewClickListener()
    }

    private fun getFavoritePlaces() {
        viewModel.getSavedPlaces().observe(this, Observer { places ->
            adapter.updateList(places)
            updateFavoriteNumber(places.size)
        })
    }

    private fun initAdapter() {
        val glide = Glide.with(this)
        adapter = FavoriteListAdapter(viewModel, glide, attachedActivity)

        this.rv_favorite.adapter = adapter
        this.rv_favorite.layoutManager = LinearLayoutManager(activity)

        val divider = DividerItemDecoration(activity, LinearLayoutManager(activity).orientation)
        activity?.getDrawable(R.drawable.recycer_divider)?.let { divider.setDrawable(it) }
        this.rv_favorite.addItemDecoration(divider)
    }

    private fun setViewClickListener() {
        this.constraint_sort_holder.setOnClickListener { popup.show() }
    }

    private fun initSort() {
        wrapper = ContextThemeWrapper(attachedActivity, R.style.Widget_AppCompat_Light_PopupMenu)
        popup = PopupMenu(wrapper, this.constraint_sort_holder, Gravity.CENTER)
        popup.menuInflater.inflate(R.menu.menu_favorite_sort, popup.menu)

        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.menu_rate_down -> {
                    sortKeyword(getString(R.string.sort_rate_down))
                    adapter.sortList(SortType.RateDown)
                }

                R.id.menu_rate_up -> {
                    sortKeyword(getString(R.string.sort_rate_up))
                    adapter.sortList(SortType.RateUp)
                }

                R.id.menu_latest_down -> {
                    sortKeyword(getString(R.string.sort_latest_down))
                    adapter.sortList(SortType.SavedTimeDown)
                }

                R.id.menu_latest_up -> {
                    sortKeyword(getString(R.string.sort_latest_up))
                    adapter.sortList(SortType.SavedTimeUP)
                }
            }
            true
        }
    }

    private fun sortKeyword(keyword: String) {
        this.tv_sort.text = keyword
    }

    private fun updateFavoriteNumber(number: Int) {
        this.tv_number_of_favorites.text = String.format(getString(R.string.number_of_favorite), number)
    }
}
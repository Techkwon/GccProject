package woogear.kwon.gccproject.ui.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import kotlinx.android.synthetic.main.item_place.view.*
import woogear.kwon.gccproject.R
import woogear.kwon.gccproject.model.Place
import woogear.kwon.gccproject.utils.CommonUtils.callPlaceInfoActivity
import woogear.kwon.gccproject.viewmodel.PlacesViewModel

class TotalListAdapter(
    private val viewModel: PlacesViewModel,
    private val glide: RequestManager,
    private val activity: Activity
): PagedListAdapter<Place, RecyclerView.ViewHolder>(PLACE_COMPARATOR) {
    private val savedPlaces = ArrayList<Place>()

    companion object {
        val PLACE_COMPARATOR = object : DiffUtil.ItemCallback<Place>() {
            override fun areItemsTheSame(oldItem: Place, newItem: Place) = oldItem == newItem
            override fun areContentsTheSame(oldItem: Place, newItem: Place) = oldItem.id == newItem.id
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return PlaceViewHolder.create(parent, glide, viewModel, activity)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        with(holder as PlaceViewHolder) {
            bind(getItem(position))
            checkIfSaved(savedPlaces)
        }
    }

    fun updateSavedPlaces(places: List<Place>) {
        savedPlaces.clear()
        savedPlaces.addAll(places)
        notifyDataSetChanged()
    }

    class PlaceViewHolder(
        view: View,
        private val glide: RequestManager,
        private val viewModel: PlacesViewModel,
        private val activity: Activity
    ) : RecyclerView.ViewHolder(view) {
        private val itemHolder: ConstraintLayout = view.total_places_holder
        private val ivThumbnail: AppCompatImageView = view.iv_thumbnail
        private val tvName: AppCompatTextView = view.tv_place_name
        private val tvRate: AppCompatTextView = view.tv_place_rate
        private val ibBookMark: AppCompatImageButton = view.ib_bookmark
        private var place: Place? = null

        companion object {
            fun create(parent: ViewGroup,
                       glide: RequestManager,
                       viewModel: PlacesViewModel,
                       activity: Activity): PlaceViewHolder {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_place, parent, false)
                return PlaceViewHolder(view, glide, viewModel, activity)
            }
        }

        init {
            itemHolder.setOnClickListener {
                place?.let { callPlaceInfoActivity(it, ibBookMark.isSelected ,activity) }
            }

            ibBookMark.setOnClickListener {
                setBookMarkButtonSelection(it, place)
            }
        }

        fun bind(place: Place?) {
            this.place = place
            tvName.text = place?.name ?: "로딩중"
            tvRate.text = place?.rate.toString()
            if (place?.thumbnail?.startsWith("http") == true) {
                glide.load(place.thumbnail).into(ivThumbnail)
            }
        }

        private fun setBookMarkButtonSelection(view: View, place: Place?) {
            place?.let {
                view.isSelected = !view.isSelected
                viewModel.updatePlace(it, view.isSelected)
            }
        }

        fun checkIfSaved(savedPlaces: List<Place>) {
            place?.let {
                if (savedPlaces.isNotEmpty()) ibBookMark.isSelected = savedPlaces.contains(it)
                else ibBookMark.isSelected = false
            }
        }
    }
}
package woogear.kwon.gccproject.ui.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import kotlinx.android.synthetic.main.item_place.view.*
import woogear.kwon.gccproject.R
import woogear.kwon.gccproject.model.Place
import woogear.kwon.gccproject.utils.CommonUtils.callPlaceInfoActivity
import woogear.kwon.gccproject.utils.SortType
import woogear.kwon.gccproject.viewmodel.PlacesViewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class FavoriteListAdapter(
    private val viewModel: PlacesViewModel,
    private val glide: RequestManager,
    private val activity: Activity
): RecyclerView.Adapter<FavoriteListAdapter.ViewHolder>() {
    private val list = ArrayList<Place>()
    private var sortType = SortType.SavedTimeUP

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_place, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    @SuppressLint("SimpleDateFormat")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            bind(list[position])
        }
    }

    fun updateList(data: List<Place>) {
        list.clear()
        list.addAll(data)
        sortList(sortType)
    }

    fun sortList(type: SortType) {
        when(type) {
            SortType.SavedTimeUP -> list.sortBy { it.saveTime }
            SortType.SavedTimeDown -> list.sortByDescending { it.saveTime }
            SortType.RateUp -> list.sortBy { it.rate }
            SortType.RateDown -> list.sortByDescending { it.rate }
        }
        notifyDataSetChanged()
        sortType = type
    }

    inner class ViewHolder(containerView: View) : RecyclerView.ViewHolder(containerView) {
        lateinit var place: Place
        private val itemHolder: ConstraintLayout = containerView.total_places_holder
        private val ivThumbnail: AppCompatImageView = containerView.iv_thumbnail
        private val tvName: AppCompatTextView = containerView.tv_place_name
        private val tvRate: AppCompatTextView = containerView.tv_place_rate
        private val ibBookMark: AppCompatImageButton = containerView.ib_bookmark
        private val tvTime: AppCompatTextView = containerView.tv_saved_time

        init {
            itemHolder.setOnClickListener { callPlaceInfoActivity(place, true, activity) }
            ibBookMark.setOnClickListener { setBookMarkButtonSelection(it, place) }
        }

        @SuppressLint("SimpleDateFormat", "SetTextI18n")
        fun bind(place: Place) {
            this.place = place
            val saveTime: Long = place.saveTime
            val formatter = SimpleDateFormat("yy/MM/dd hh:mm a")
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = saveTime
            tvTime.text = String.format(activity.getString(R.string.saved_at), formatter.format(calendar.time))

            glide.load(place.thumbnail).into(ivThumbnail)
            tvName.text = place.name
            tvRate.text = place.rate.toString()
            ibBookMark.isSelected = true
        }

        private fun setBookMarkButtonSelection(view: View, place: Place) {
            view.isSelected = !view.isSelected
            viewModel.updatePlace(place, view.isSelected)
        }
    }
}
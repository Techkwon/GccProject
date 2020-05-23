package woogear.kwon.gccproject.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_place_info.*
import woogear.kwon.gccproject.R
import woogear.kwon.gccproject.model.Place
import woogear.kwon.gccproject.utils.Constants.EXTRA_PLACE_IS_SAVED
import woogear.kwon.gccproject.utils.Constants.EXTRA_PLACE_OBJECT
import woogear.kwon.gccproject.viewmodel.PlacesViewModel

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class PlaceInfoActivity : AppCompatActivity() {
    private lateinit var place: Place
    private var placeIsSaved = false

    private val viewModel: PlacesViewModel by viewModels {
        object : AbstractSavedStateViewModelFactory(this, null) {
            override fun <T : ViewModel?> create(key: String, modelClass: Class<T>, handle: SavedStateHandle): T {
                @Suppress("UNCHECKED_CAST")
                return PlacesViewModel(application, handle) as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // network connection 체크 생략
        setContentView(R.layout.activity_place_info)
        getIntentData()
        setViews()
        setViewClickListener()
    }

    override fun onPause() {
        if (placeIsSaved != this.ib_place_info_bookmark.isSelected) {
            viewModel.updatePlace(place, this.ib_place_info_bookmark.isSelected)
        }
        super.onPause()
    }

    private fun getIntentData() {
        place = intent.getSerializableExtra(EXTRA_PLACE_OBJECT) as Place
        placeIsSaved = intent.getBooleanExtra(EXTRA_PLACE_IS_SAVED, false)
    }

    private fun setViews() {
        Glide.with(this)
            .load(place.description.imagePath)
            .placeholder(R.drawable.ic_photo_placeholder)
            .into(this.iv_place_original_image)
        this.tv_place_info_name.text = place.name
        this.ib_place_info_bookmark.isSelected = placeIsSaved
        this.tv_rate_place_info.text = place.rate.toString()
        val formattedAmount = String.format("%,d", place.description.price)
        this.tv_place_info_price.text = String.format("%s원", formattedAmount)
        this.tv_subject.text = place.description.subject
    }

    private fun setViewClickListener() {
        this.ib_place_info_bookmark.setOnClickListener { setBookmarkSelection() }
    }

    private fun setBookmarkSelection() {
        this.ib_place_info_bookmark.isSelected = !this.ib_place_info_bookmark.isSelected
    }
}

package woogear.kwon.gccproject.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_network_disconnected.*
import woogear.kwon.gccproject.R
import woogear.kwon.gccproject.ui.adapter.PagerAdapter
import woogear.kwon.gccproject.ui.fragment.FavoriteListFragment
import woogear.kwon.gccproject.ui.fragment.TotalListFragment
import woogear.kwon.gccproject.utils.CommonUtils.isNetworkAvailable
import woogear.kwon.gccproject.viewmodel.PlacesViewModel

class MainActivity : AppCompatActivity() {
    internal val viewModel: PlacesViewModel by viewModels {
        object : AbstractSavedStateViewModelFactory(this, null) {
            override fun <T : ViewModel?> create(key: String, modelClass: Class<T>, handle: SavedStateHandle): T {
                @Suppress("UNCHECKED_CAST")
                return PlacesViewModel(application, handle) as T
            }
        }
    }

    private var pagerAdapter: PagerAdapter? = null
    private var totalFragment: TotalListFragment? = null
    private var favoriteFragment: FavoriteListFragment? = null

    companion object {
        private const val FRAGMENT_TOTAL = "fragment_total"
        private const val FRAGMENT_FAVORITE = "fragment_favorite"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (isNetworkAvailable(this)) {
            setPagerAdapter(savedInstanceState)
        } else
            showNetworkError(getString(R.string.please_check_network_connection))
    }

    private fun setPagerAdapter(savedInstanceState: Bundle?) {
        savedInstanceState?.let { getFragmentsInstances(it) }
        totalFragment = totalFragment ?: TotalListFragment()
        favoriteFragment = favoriteFragment ?: FavoriteListFragment()

        pagerAdapter = pagerAdapter ?: PagerAdapter(
            supportFragmentManager
        )

        totalFragment?.let { pagerAdapter?.addFragment(it, "NEW") }
        favoriteFragment?.let { pagerAdapter?.addFragment(it, "FAVORITE") }

        this.viewpager.adapter = pagerAdapter
        this.tab_layout.setupWithViewPager(this.viewpager)
        this.tab_layout.tabRippleColor
    }

    private fun getFragmentsInstances(savedInstanceState: Bundle) {
        totalFragment = supportFragmentManager.getFragment(savedInstanceState,
            FRAGMENT_TOTAL
        )?.let { it as TotalListFragment }
        favoriteFragment = supportFragmentManager.getFragment(savedInstanceState,
            FRAGMENT_FAVORITE
        )?.let { it as FavoriteListFragment }
    }

    private fun showNetworkError(msg: String) {
        this.constraint_layout_main.visibility = View.GONE
        this.constraint_network_disconnected.visibility = View.VISIBLE
        this.tv_network_disconnected.text = msg
    }
}

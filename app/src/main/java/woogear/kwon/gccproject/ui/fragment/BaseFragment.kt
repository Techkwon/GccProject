package woogear.kwon.gccproject.ui.fragment

import android.content.Context
import androidx.fragment.app.Fragment
import woogear.kwon.gccproject.ui.activity.MainActivity
import woogear.kwon.gccproject.viewmodel.PlacesViewModel

open class BaseFragment : Fragment() {
    internal lateinit var attachedActivity: MainActivity
    internal lateinit var viewModel: PlacesViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        attachedActivity = context as MainActivity
        viewModel = attachedActivity.viewModel
    }
}
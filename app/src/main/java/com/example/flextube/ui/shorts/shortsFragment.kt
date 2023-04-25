package com.example.flextube.ui.shorts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.flextube.databinding.FragmentShortsBinding
import com.example.flextube.shorts.ShortsAdapter
import com.example.flextube.shorts.ShortsItem

class shortsFragment : Fragment() {

    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: RecyclerView.Adapter<ShortsAdapter.ShortsViewHolder>
    private lateinit var mLayoutManager: RecyclerView.LayoutManager
    public var Shortlist: ArrayList<ShortsItem> = ArrayList<ShortsItem>()
    public var atl: ArrayList<Int> = ArrayList()
    public var at: ArrayList<String> = ArrayList()
    public var l: ArrayList<String> = ArrayList()
    public var dl: ArrayList<String> = ArrayList()

    private var _binding: FragmentShortsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(shortsViewModel::class.java)

        _binding = FragmentShortsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        mRecyclerView = _binding!!.shortsRecyclerview
        mRecyclerView.setHasFixedSize(true)
        mLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false)
        val snapHelper = LinearSnapHelper()

        snapHelper.attachToRecyclerView(mRecyclerView)
        mRecyclerView.layoutManager = mLayoutManager

        ///endless recycler view endless scrolling
        val sh= ShortsItem("https://pbs.twimg.com/profile_images/556495456805453826/wKEOCDN0_400x400.png","Dudu","34","34")
        Shortlist.add(sh)

        val sh1= ShortsItem("https://upload.wikimedia.org/wikipedia/commons/c/c3/Jaros%C5%82aw_Kaczy%C5%84ski%2C_wicepremier_%28cropped%29.png","Kaczor","34","34")
        Shortlist.add(sh1)
        val sh2= ShortsItem("https://upload.wikimedia.org/wikipedia/commons/5/5d/Mateusz_Morawiecki_Prezes_Rady_Ministr%C3%B3w_%28cropped%29.jpg","Mati","34","34")
        Shortlist.add(sh2)

        val sh3= ShortsItem("https://www.malopolska.pl/_cache/councilors/790-790/fit/DudaJ.jpg","Old dudu","34","34")
        Shortlist.add(sh3)


        mAdapter = ShortsAdapter(Shortlist)

        mRecyclerView.adapter = mAdapter
        mAdapter.notifyDataSetChanged()

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}



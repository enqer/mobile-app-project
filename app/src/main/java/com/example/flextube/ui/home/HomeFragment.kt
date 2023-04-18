package com.example.flextube.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.flextube.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private lateinit var mRecyclerView: RecyclerView
//    private lateinit var mAdapter: RecyclerView.Adapter<VideoAdapter.VideoViewHolder>
    private lateinit var mLayoutManager: RecyclerView.LayoutManager
//    var videos: ArrayList<Video>

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

//        val textView: TextView = binding.textHome
//        homeViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }
        return root
    }


    fun getVideos(){


//        mRecyclerView = binding.root.findViewById(R.id.recyclerView)
//        mRecyclerView.setHasFixedSize(true)
//        mLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false)
//        mAdapter = PersonAdapter(newArr, this)
//        mRecyclerView.layoutManager=mLayoutManager
//        mRecyclerView.adapter = mAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
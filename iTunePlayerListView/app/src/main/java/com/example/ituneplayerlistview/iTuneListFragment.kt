package com.example.ituneplayerlistview

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ituneplayerlistview.iTuneXMLParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.example.ituneplayerlistview.databinding.FragmentItuneListBinding


/**
 * A simple [Fragment] subclass.
 * Use the [iTuneListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class iTuneListFragment : Fragment(), iTuneRecyclerViewAdapter.RecyclerViewClickListener  {
    interface OnSongSelectedListener{
        fun onSongSelected(title : String, cover : Bitmap, url : String)
    }
    var listener : OnSongSelectedListener?= null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as? OnSongSelectedListener
    }

    val adapter by lazy {
        iTuneRecyclerViewAdapter(listOf<SongItem>(), this)
    }
    val swipeRefreshLayout by lazy {
        //findViewById<SwipeRefreshLayout>(R.id.swipeRefreshLayout)
        binding.swipeRefreshLayout
    }

    lateinit var binding: FragmentItuneListBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_itune_list,
            container, false) as FragmentItuneListBinding

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(activity)
        // Refresh
        binding.swipeRefreshLayout.setOnRefreshListener {
            loadList()
        }
        loadList()
    }



    fun loadList() {
        GlobalScope.launch(Dispatchers.Main) {
            swipeRefreshLayout.isRefreshing = true
            var songs = listOf<SongItem>()

            withContext(Dispatchers.IO) {
                songs = iTuneXMLParser().parseURL(
                    "https://itunes.apple.com/us/rss/topsongs/limit=25/xml")
            }

            adapter.songs = songs
            swipeRefreshLayout.isRefreshing = false
        }
    }

    override fun onClick(position: Int) {
        val title = adapter.songs.get(position).title
        val cover = adapter.songs.get(position).cover
        val url = adapter.songs.get(position).url
        if (cover != null) {
            listener?.onSongSelected(title,cover,url)
        }
    }

}
package com.example.fragment_navigation

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fragment_navigation.iTuneXMLParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.example.fragment_navigation.databinding.FragmentItuneListBinding


/**
 * A simple [Fragment] subclass.
 * Use the [iTuneListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class iTuneListFragment : Fragment(), iTuneRecyclerViewAdapter.RecyclerViewClickListener  {
    interface OnSongSelectedListener{
        fun onSongSelected(title : String, cover : Bitmap,videoUrl : String)
    }
    var listener : OnSongSelectedListener?= null

    // Fragment 的生命周期方法，當這個 fragment 被附加 (attach) 到其所屬的 activity 上時就會呼叫。
    override fun onAttach(context: Context) {
        super.onAttach(context)
        // 將 context 轉為 OnSongSelectedListener 介面
        listener = context as? OnSongSelectedListener
    }

    val adapter by lazy {
        iTuneRecyclerViewAdapter(listOf<VideoItem>(), this)
    }
    val swipeRefreshLayout by lazy {
        //findViewById<SwipeRefreshLayout>(R.id.swipeRefreshLayout)
        binding.swipeRefreshLayout
    }

    // DataBinding 對應的綁定物件，綁定的是 fragment_itune_list 這個 layout
    lateinit var binding: FragmentItuneListBinding
    // 覆寫 onCreateView，用 DataBinding 載入 fragment_itune_list 的 layout
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
    // 當 view 被 onCreateView() 建立完成後呼叫：設定 RecyclerView、swipeRefreshLayout
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
            var videos = listOf<VideoItem>()

            withContext(Dispatchers.IO) {
                videos = iTuneXMLParser().parserURL(
                    "https://www.youtube.com/feeds/videos.xml?channel_id=UCupvZG-5ko_eiXAupbDfxWw")
            }

            adapter.videos = videos
            swipeRefreshLayout.isRefreshing = false
        }
    }

    override fun onClick(position: Int) {
        val title = adapter.videos.get(position).title
        val cover = adapter.videos.get(position).cover
        val videoUrl = adapter.videos.get(position).videoUrl
        if (cover != null) {
            // 當被點擊就呼叫 onSongSelected 函式，此函式會在 MainActivity 被實作
            listener?.onSongSelected(title,cover,videoUrl)
        }
    }

}
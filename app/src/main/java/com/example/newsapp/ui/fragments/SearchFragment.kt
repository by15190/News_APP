package com.example.newsapp.ui.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.R
import com.example.newsapp.adapter.Adapter
import com.example.newsapp.databinding.FragmentHeadlineBinding
import com.example.newsapp.databinding.FragmentSearchBinding
import com.example.newsapp.ui.MainActivity
import com.example.newsapp.ui.fragments.HeadlineFragment
import com.example.newsapp.util.Resource
import com.example.newsapp.util.constant
import com.example.newsapp.util.constant.Companion.SEARCH_NEWS_TIME_DELAY
import com.example.newsapp.viewmodel.viewmodel
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SearchFragment : Fragment(R.layout.fragment_search) {


    lateinit var newsviewmodel: viewmodel
    lateinit var newsadapter: Adapter
    lateinit var retryBtn: Button
    lateinit var errorText: TextView
    lateinit var itemSearcherror: CardView
    lateinit var binding: FragmentSearchBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentSearchBinding.bind(view)

        itemSearcherror = view.findViewById(R.id.searchError)
        val inflater =
            requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = inflater.inflate(R.layout.item_error, null)

        retryBtn = view.findViewById(R.id.reloadbtn)
        errorText = view.findViewById(R.id.ERRORtext)
        newsviewmodel = (activity as MainActivity).newsViewModel
        setupSearchRecycler()

        newsadapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("article", it)
            }
            findNavController().navigate(R.id.action_searchFragment2_to_articleFragment2, bundle)
        }


        var job: Job? = null
        binding.searchbox.addTextChangedListener() { editable ->
            job?.cancel()
            job = MainScope().launch {
                delay(SEARCH_NEWS_TIME_DELAY)
                editable?.let {
                    if (editable.toString().isNotEmpty()) {
                        newsviewmodel.searchnews(editable.toString())
                    }
                }
            }
        }

        newsviewmodel.searchnews.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.success<*> -> {
                    hideprogressbar()
                    hideErrormessage()
                    response.data?.let { newsresponse ->
                        newsadapter.differ.submitList(newsresponse.articles.toList())
                        val totalpages = newsresponse.totalResults / constant.QUERY_PAGE_SIZE + 2

                        isLastPage = newsviewmodel.searchNewsPage == totalpages
                        if (isLastPage) {
                            binding.rvsearchItemList.setPadding(0, 0, 0, 0)
                        }

                    }
                }

                is Resource.error<*> -> {
                    hideprogressbar()
                    response.message?.let { message ->
                        Toast.makeText(context, "error : $message", Toast.LENGTH_LONG).show()
                        showErrormessage(message)
                    }
                }

                is Resource.loading<*> -> {
                    showprogressbar()
                }
            }
        })

        retryBtn.setOnClickListener{
            if(binding.searchbox.text.toString().isNotEmpty()){
                newsviewmodel.searchnews(binding.searchbox.text.toString())
            }else{
                hideErrormessage()
            }
        }
    }

    var isError = false
    var isLoading = false
    var isLastPage = false
    var isScrolling = false


    private fun hideprogressbar() {
        binding.searchProgressbar.visibility = View.INVISIBLE
        isLoading = false
    }

    private fun showprogressbar() {
        binding.searchProgressbar.visibility = View.VISIBLE
        isLoading = true
    }

    private fun hideErrormessage() {
        itemSearcherror.visibility = View.INVISIBLE
        isError = false

    }

    private fun showErrormessage(message: String) {
        itemSearcherror.visibility = View.VISIBLE
        errorText.text = message
        isError = true
    }

    val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstvisibleitemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleitemcount = layoutManager.childCount
            val totalitemcount = layoutManager.itemCount

            val isNoError = !isError
            val isNtLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstvisibleitemPosition + visibleitemcount >= totalitemcount
            val isAtBeginningItem = firstvisibleitemPosition >= 0
            val isTotalMoreThanVisible = totalitemcount >= constant.QUERY_PAGE_SIZE
            val shouldPaginate =
                isNoError && isNtLoadingAndNotLastPage && isAtLastItem && isAtBeginningItem && isScrolling
            if (shouldPaginate) {
                newsviewmodel.searchnews(binding.searchbox.text.toString())
                isScrolling = false
            }

        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }

    }

    private fun setupSearchRecycler() {
        newsadapter = Adapter()
        binding.rvsearchItemList.apply {
            adapter = newsadapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(this@SearchFragment.scrollListener)
        }

    }
}

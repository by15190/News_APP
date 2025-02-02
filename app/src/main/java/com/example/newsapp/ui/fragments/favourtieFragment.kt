package com.example.newsapp.ui.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.newsapp.R
import com.example.newsapp.adapter.Adapter
import com.example.newsapp.databinding.FragmentFavourtieBinding
import com.example.newsapp.ui.MainActivity
import com.example.newsapp.ui.fragments.HeadlineFragment
import com.example.newsapp.util.Resource
import com.example.newsapp.util.constant
import com.example.newsapp.viewmodel.viewmodel
import com.google.android.material.snackbar.Snackbar

class favourtieFragment : Fragment(R.layout.fragment_favourtie) {

    lateinit var newsviewmodel: viewmodel
    lateinit var newsAdapter: Adapter
    lateinit var binding: FragmentFavourtieBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFavourtieBinding.bind(view)

        newsviewmodel = (activity as MainActivity).newsViewModel
        setupfavouraterecycler()

        newsAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("article", it)
            }
            findNavController().navigate(R.id.action_favourtieFragment2_to_articleFragment2, bundle)
        }

        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or
                    ItemTouchHelper.DOWN, ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder,
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val article = newsAdapter.differ.currentList[position]
                newsviewmodel.deletearticle(article)
                Snackbar.make(view, "Deleted", Snackbar.LENGTH_LONG).apply {
                    setAction("undo"){
                        newsviewmodel.addtoFAV(article)
                    }
                }.show()

            }
        }

ItemTouchHelper(itemTouchHelperCallback).apply{
    attachToRecyclerView(binding.rvFAV)

}

        newsviewmodel.getallFav().observe(viewLifecycleOwner, Observer{
            articles->
            newsAdapter.differ.submitList(articles)
        })

    }

    private fun setupfavouraterecycler() {
        newsAdapter = Adapter()
        binding.rvFAV.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }

    }

}
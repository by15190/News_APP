package com.example.newsapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.annotation.GlideType
import com.example.newsapp.R
import com.example.newsapp.models.Article

class Adapter : RecyclerView.Adapter<Adapter.viewHolder>() {

    inner class viewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    lateinit var articleImage: ImageView
    lateinit var articleSource: TextView
    lateinit var articleTitle: TextView
    lateinit var articleDescription: TextView
    lateinit var articleDate: TextView

    private var differcallback = object :
        DiffUtil.ItemCallback<Article>() { // while scrolling the list if new item is added or old item is removed
        override fun areItemsTheSame(
            oldItem: Article,
            newItem: Article,
        ): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(
            oldItem: Article,
            newItem: Article,
        ): Boolean {
            return oldItem == newItem
        }

    }
    val differ = AsyncListDiffer(this, differcallback)

    private var onItemClickListener:((Article) -> Unit)? = null


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): viewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rv_item_news, parent, false)
        return viewHolder(view)
    }


    override fun onBindViewHolder(
        holder: viewHolder,
        position: Int,
    ) {
        val article =
            differ.currentList[position] // get the current article from the updated list  // differ.currentList is the updated list

        // binding the view
        articleImage =
            holder.itemView.findViewById(R.id.articleImage) // find the view by id in the view holder class
        articleSource = holder.itemView.findViewById(R.id.articleSource)
        articleTitle = holder.itemView.findViewById(R.id.articleTitle)
        articleDescription = holder.itemView.findViewById(R.id.articleDescription)
        articleDate = holder.itemView.findViewById(R.id.articleDateTime)

        holder.itemView.apply {
            // binding the data in the view
            Glide.with(this).load(article.urlToImage).into(articleImage)
            articleSource.text = article.source.name
            articleTitle.text = article.title
            articleDescription.text = article.description
            articleDate.text = article.publishedAt


            setOnClickListener {
                onItemClickListener?.let { it(article) }
            }

        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }


    fun setOnItemClickListener(listener: (Article) -> Unit) {
        onItemClickListener = listener
    }

}


/// inner class is used to access the outer class members from the inner class

/*
 Using DiffUtil and AsyncListDiffer allows your adapter to efficiently update
 the list of articles without needing to refresh the entire list, which can lead to smoother scrolling and better performance.
 */
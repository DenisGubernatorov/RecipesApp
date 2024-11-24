package com.example.recipesapp

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.recipesapp.databinding.ItemCategoryBinding

class CategoriesListAdapter(private val dataSet: List<Category>) :
    RecyclerView.Adapter<CategoriesListAdapter.ViewHolder>() {
    class ViewHolder(private var binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(category: Category) {
            binding.cardTitleTextView.text = category.title
            binding.cardDescriptionTextView.text = category.description

            val drawableImage = try {
                Drawable.createFromStream(itemView.context.assets.open(category.imageUrl), null)

            } catch (e: Exception) {
                Log.e("!!!!__", "image not found $category")
                null
            }
            binding.cardImageView.setImageDrawable(drawableImage)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category = dataSet[position]
        holder.bind(category)

    }
}
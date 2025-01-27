package com.example.recipesapp.ui.categories

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.recipesapp.R
import com.example.recipesapp.databinding.ItemCategoryBinding
import com.example.recipesapp.model.Category

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
            binding.cardImageView.contentDescription =
                itemView.context.getString(R.string.category_image_description, category.title)

        }
    }

    interface OnItemClickListener {
        fun onItemClick(categoryId: Int)
    }

    private var itemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
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
        holder.itemView.setOnClickListener {
            itemClickListener?.onItemClick(position)
        }

    }
}
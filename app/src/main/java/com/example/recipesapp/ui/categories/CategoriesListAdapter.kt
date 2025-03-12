package com.example.recipesapp.ui.categories

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipesapp.R
import com.example.recipesapp.data.RecipesRepository
import com.example.recipesapp.databinding.ItemCategoryBinding
import com.example.recipesapp.model.Category

class CategoriesListAdapter(private var dataSet: List<Category>) :
    RecyclerView.Adapter<CategoriesListAdapter.ViewHolder>() {

    class ViewHolder(private var binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(category: Category) {
            binding.cardTitleTextView.text = category.title
            binding.cardDescriptionTextView.text = category.description

            Glide.with(binding.cardImageView)
                .load(RecipesRepository.IMAGE_URL + category.imageUrl)
                .placeholder(R.drawable.img_placeholder)
                .error(R.drawable.img_error)
                .into(binding.cardImageView)


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

    fun updateState(dataSet: List<Category>) {
        this.dataSet = dataSet
        notifyItemRangeChanged(0, dataSet.size)
    }
}
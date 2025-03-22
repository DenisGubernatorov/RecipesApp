package com.example.recipesapp.ui.common

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.recipesapp.R
import com.example.recipesapp.data.RecipesRepository
import com.example.recipesapp.databinding.ItemRecipeBinding
import com.example.recipesapp.model.Recipe


class RecipeListAdapter(private var dataSet: List<Recipe>) :
    RecyclerView.Adapter<RecipeListAdapter.ViewHolder>() {


    class ViewHolder(private var binding: ItemRecipeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(recipe: Recipe) {
            binding.recipeTitleTextView.text = recipe.title

            Glide.with(binding.recipeImageView)
                .load(RecipesRepository.IMAGE_URL + "${recipe.imageUrl}")
                .placeholder(R.drawable.img_placeholder)
                .error(R.drawable.img_error)
                .listener(object : RequestListener<Drawable> {

                    override fun onResourceReady(
                        resource: Drawable,
                        model: Any,
                        target: Target<Drawable>?,
                        dataSource: DataSource,
                        isFirstResource: Boolean
                    ): Boolean {
                        return false
                    }

                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>,
                        isFirstResource: Boolean
                    ): Boolean {
                        e?.let { exception ->
                            exception.logRootCauses("RRE")
                            exception.printStackTrace()
                        }
                        Log.e(
                            "RRE",
                            "FAIL TO LOAD IMAGE ${recipe.title}: id:${recipe.id} url:${recipe.imageUrl}  error: $e."
                        )

                        return false
                    }


                })
                .into(binding.recipeImageView)

            binding.recipeImageView.contentDescription =
                itemView.context.getString(R.string.recipe_header_image_description, recipe.title)
        }


    }

    interface OnItemClickListener {
        fun onItemClick(recipe: Recipe)
    }

    private var itemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRecipeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val recipe = dataSet[position]
        holder.bind(recipe)
        holder.itemView.setOnClickListener {
            itemClickListener?.onItemClick(recipe)
        }

    }

    fun updateState(dataSet: List<Recipe>) {
        this.dataSet = dataSet
        notifyItemRangeChanged(0, dataSet.size)
    }
}
package com.example.recipesapp


import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup

import androidx.recyclerview.widget.RecyclerView
import com.example.recipesapp.databinding.IngredientItemBinding

class IngredientsAdapter(private val dataSet: List<Ingredient>, private val context: Context) :
    RecyclerView.Adapter<IngredientsAdapter.ViewHolder>() {
    class ViewHolder(private var binding: IngredientItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(ingredient: Ingredient, context: Context) {
            binding.ingredientItemLft.text = ingredient.description

            binding.ingredientItemRgt.text = context.getString(
                R.string.ingredient_display_format,
                ingredient.quantity,
                ingredient.unitOfMeasure
            )
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding =
            IngredientItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ingredient = dataSet[position]
        holder.bind(ingredient, context)
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

}
package com.example.recipesapp.ui.recipes.recipe


import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.recipesapp.R
import com.example.recipesapp.databinding.IngredientItemBinding
import com.example.recipesapp.model.Ingredient
import java.math.BigDecimal
import java.math.RoundingMode

class IngredientsAdapter(var dataSet: List<Ingredient>, private val context: Context) :
    RecyclerView.Adapter<IngredientsAdapter.ViewHolder>() {
    private var quantity = 1

    class ViewHolder(private var binding: IngredientItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private fun getTotalQuantity(quantityStr: String, quantity: Int): Any {

            val totalQuantity = BigDecimal(quantityStr) * BigDecimal(quantity)
            val displayQuantity = totalQuantity
                .setScale(1, RoundingMode.HALF_UP)
                .stripTrailingZeros()
                .toPlainString()

            return displayQuantity

        }

        fun bind(ingredient: Ingredient, context: Context, quantity: Int) {
            binding.ingredientItemLft.text = ingredient.description


            binding.ingredientItemRgt.text = context.getString(
                R.string.ingredient_display_format,
                getTotalQuantity(ingredient.quantity, quantity),
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
        holder.bind(ingredient, context, quantity)
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    fun updateQuantity(newQuantity: Int) {
        quantity = newQuantity
        notifyItemRangeChanged(0, dataSet.size)
    }

}
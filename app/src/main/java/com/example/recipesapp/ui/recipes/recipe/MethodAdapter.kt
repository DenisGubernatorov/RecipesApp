package com.example.recipesapp.ui.recipes.recipe

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.recipesapp.R
import com.example.recipesapp.databinding.MethodItemBinding

class MethodAdapter(private var dataSet: List<String>, private val context: Context) :
    RecyclerView.Adapter<MethodAdapter.ViewHolder
            >() {

    class ViewHolder(private var binding: MethodItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(methodStep: String, context: Context, position: Int) {
            binding.methodItemText.text =
                context.getString(R.string.method_display_format, position.toString(), methodStep)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            MethodItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val offset = 1
        val methodStep = dataSet[position]
        holder.bind(methodStep, context, position + offset)
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    fun updateDataSet(method: List<String>?) {
        dataSet = method ?: return
        notifyItemRangeChanged(0, dataSet.size)
    }
}
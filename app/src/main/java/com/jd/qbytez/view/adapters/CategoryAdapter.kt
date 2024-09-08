package com.jd.qbytez.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.jd.qbytez.R
import com.jd.qbytez.databinding.ViewCategoryItemBinding
import com.jd.qbytez.models.CategoryEntity
import com.jd.qbytez.view.activities.MainActivity
import com.jd.qbytez.view.fragments.AddEditCategoriesFragment
import com.jd.qbytez.viewModel.CategoryViewModel
import com.maltaisn.icondialog.pack.IconPack

class CategoryAdapter(val fragment: Fragment, val categoryViewModel: CategoryViewModel,val iconDialogIconPack: IconPack) :
    RecyclerView.Adapter<CategoryAdapter.TransactionViewHolder>() {

    private var categoryList: List<CategoryEntity> = listOf()

    class TransactionViewHolder(itemView: ViewCategoryItemBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        val catImage = itemView.ivCatIcon
        val catName = itemView.tvCatName
        val tranType = itemView.tvTransactionType
        val ivDelete = itemView.ivDelete
        val ivEdit = itemView.ivEdit

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val binding = ViewCategoryItemBinding.inflate(
            LayoutInflater.from(fragment.context),
            parent,
            false
        )
        return TransactionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val item = categoryList[position]
        holder.catName.text= item.categoryName
        holder.tranType.text= item.transactionType

        holder.catImage.setImageDrawable(iconDialogIconPack?.getIcon(item.iconId)?.drawable)
        holder.catImage.setColorFilter(ContextCompat.getColor(
            fragment.requireContext(),
            R.color.blackToWhite
        ))
        if (item.transactionType == "Income") {
            holder.tranType.setTextColor(
                ContextCompat.getColor(
                    fragment.requireContext(),
                    R.color.greenToWhite
                )
            )

        } else {
            holder.tranType.setTextColor(
                ContextCompat.getColor(
                    fragment.requireContext(),
                    R.color.redToWhite
                )
            )


        }




        holder.ivEdit.setOnClickListener {
            var frag = AddEditCategoriesFragment()
            frag.categoryEntity = item
            (fragment.requireActivity() as MainActivity).pushFragments(frag)
        }
        holder.ivDelete.setOnClickListener {
            categoryViewModel.delete(item)
            notifyDataSetChanged()
        }


    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    fun categories(list: List<CategoryEntity>) {
        categoryList = list
        notifyDataSetChanged()
    }
}
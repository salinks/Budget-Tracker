package com.jd.qbytez.view.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.jd.qbytez.R
import com.jd.qbytez.databinding.ViewTransactionItemBinding
import com.jd.qbytez.models.TransactionsEntity
import com.jd.qbytez.utils.AnimationUtil
import com.jd.qbytez.utils.BudgetTrackerUtils
import com.jd.qbytez.view.activities.MainActivity
import com.jd.qbytez.view.fragments.TransactionDetailsFragment
import com.maltaisn.icondialog.pack.IconPack

class TransactionsAdapter(
    private val fragment: Fragment,
    private val iconDialogIconPack: IconPack
) :
    RecyclerView.Adapter<TransactionsAdapter.TransactionViewHolder>() {

    private var transactionList: List<TransactionsEntity> = listOf()

    class TransactionViewHolder(itemView: ViewTransactionItemBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        val catImage = itemView.ivCatIcon
        val tvTitle = itemView.tvTitle
        val tvDate = itemView.tvDate
        val tvAmount = itemView.tvAmount
        val tvCategoryName = itemView.tvCategoryName
        val clRoot = itemView.clRoot


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val binding = ViewTransactionItemBinding.inflate(
            LayoutInflater.from(fragment.context),
            parent,
            false
        )
        return TransactionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val item = transactionList[position]
        holder.tvCategoryName.text = item.categoryName
        holder.tvTitle.text = item.title
        holder.tvDate.text = com.jd.qbytez.utils.DateUtils.oneFormatToAnother(
            item.transactionDate,
            "yyyy-MM-dd",
            "dd-MMM-yyyy"
        )
        holder.tvAmount.text = BudgetTrackerUtils.getFormattedAmount(true,item.amount,false)

        holder.catImage.setImageDrawable(iconDialogIconPack.getIcon(item.categoryIconId)?.drawable)
        holder.catImage.setColorFilter(ContextCompat.getColor(
            fragment.requireContext(),
            R.color.blackToWhite
        ))
        if (item.transactionType == "Income") {
            holder.tvAmount.setTextColor(
                ContextCompat.getColor(
                    fragment.requireContext(),
                    R.color.greenToWhite
                )
            )

        } else {
            holder.tvAmount.setTextColor(
                ContextCompat.getColor(
                    fragment.requireContext(),
                    R.color.redToWhite
                )
            )


        }

        holder.clRoot.startAnimation(AnimationUtils.loadAnimation(holder.itemView.context,R.anim.list_in))

        holder.clRoot.setOnClickListener {

            var  frag = TransactionDetailsFragment()
            frag.transactionsEntity = item
            (fragment.requireActivity() as MainActivity).pushFragments(frag)

        }

    }

    override fun getItemCount(): Int {
        return transactionList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun transactions(list: List<TransactionsEntity>) {
        transactionList = list
        notifyDataSetChanged()
    }
}
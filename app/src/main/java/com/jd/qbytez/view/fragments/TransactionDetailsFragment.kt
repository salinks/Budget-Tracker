package com.jd.qbytez.view.fragments


import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.jd.qbytez.R
import com.jd.qbytez.application.BudgetTrackerApplication
import com.jd.qbytez.databinding.FragmentTransactionDetailsBinding
import com.jd.qbytez.databinding.FragmentTransactionsBinding
import com.jd.qbytez.models.TransactionsEntity
import com.jd.qbytez.utils.BudgetTrackerUtils
import com.jd.qbytez.utils.DateUtils
import com.jd.qbytez.view.activities.MainActivity
import com.jd.qbytez.viewModel.TransactionViewModel
import com.jd.qbytez.viewModel.TransactionViewModelFactory


class TransactionDetailsFragment : Fragment() {

    private lateinit var binding: FragmentTransactionDetailsBinding
    private val transactionViewModel: TransactionViewModel by viewModels {
        TransactionViewModelFactory((requireActivity().application as BudgetTrackerApplication).transactionsRepository)
    }
    var transactionsEntity: TransactionsEntity? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentTransactionDetailsBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setValuesInViews()
        binding.btnEdit.setOnClickListener {
            var  frag = AddTransactionFragment()
            frag.transactionsEntity = transactionsEntity
            (requireActivity() as MainActivity).pushFragments(frag)
        }

        binding.ntnDelete.setOnClickListener {
            transactionsEntity?.let { it1 -> transactionViewModel.delete(it1) }
            (requireActivity() as MainActivity).handleBackPressed()
        }

    }

    private fun setValuesInViews() {
        if (transactionsEntity != null) {
            binding.tvTransTitle.text = transactionsEntity?.title
            binding.tvTransAmount.text = transactionsEntity?.amount?.let {
                BudgetTrackerUtils.getFormattedAmount(
                    true,
                    it, false
                )
            }
            binding.tvTransType.text = transactionsEntity?.transactionType
            binding.tvTransCategory.text = transactionsEntity?.categoryName
            binding.tvTransDate.text = DateUtils.oneFormatToAnother(
                transactionsEntity?.transactionDate,
                "yyyy-MM-dd",
                "dd MMM yyyy"
            )
            binding.tvCreatedOn.text = DateUtils.oneFormatToAnother(
                transactionsEntity?.createdOn,
                "yyyy-MM-dd HH:mm:ss",
                "dd MMM yyyy HH:mm a"
            )

            if (TextUtils.isEmpty(transactionsEntity?.transactionDescription)) {
                binding.tvNotes.text = "-"
            } else {
                binding.tvNotes.text = transactionsEntity?.transactionDescription
            }
        }
    }


    override fun onResume() {
        super.onResume()
        if (requireActivity() is MainActivity) {
            (activity as MainActivity).showBottomNavigation(false)
            (activity as MainActivity).showAppBar(true)
            (activity as MainActivity).showTitle(getString(R.string.title_transaction_details))
        }
    }


}
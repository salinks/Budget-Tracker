package com.jd.qbytez.view.fragments


import android.annotation.SuppressLint
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.jd.qbytez.R
import com.jd.qbytez.application.BudgetTrackerApplication
import com.jd.qbytez.database.dao.TransactionFilterDateData
import com.jd.qbytez.databinding.FragmentTransactionsBinding
import com.jd.qbytez.models.CategoryEntity
import com.jd.qbytez.models.TransactionsEntity
import com.jd.qbytez.utils.BudgetTrackerUtils
import com.jd.qbytez.utils.DateUtils
import com.jd.qbytez.view.activities.MainActivity
import com.jd.qbytez.view.adapters.TransactionsAdapter
import com.jd.qbytez.viewModel.CategoryViewModel
import com.jd.qbytez.viewModel.CategoryViewModelFactory
import com.jd.qbytez.viewModel.TransactionViewModel
import com.jd.qbytez.viewModel.TransactionViewModelFactory
import com.maltaisn.icondialog.pack.IconPack
import com.maltaisn.icondialog.pack.IconPackLoader
import com.maltaisn.iconpack.defaultpack.createDefaultIconPack


class TransactionsFragment : Fragment() {

    private lateinit var binding: FragmentTransactionsBinding
    private lateinit var transactionsAdapter: TransactionsAdapter
    lateinit var transactionList: List<TransactionsEntity>
    lateinit var monthFilterList: List<TransactionFilterDateData>
    var selectedCategory = ""
    var selectedMonth = ""

    private val transactionViewModel: TransactionViewModel by viewModels {
        TransactionViewModelFactory((requireActivity().application as BudgetTrackerApplication).transactionsRepository)
    }

    private val categoryViewModel: CategoryViewModel by viewModels {
        CategoryViewModelFactory((requireActivity().application as BudgetTrackerApplication).categoryRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentTransactionsBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        observeAllTransactions()
        binding.rcvTransactions.layoutManager = LinearLayoutManager(requireContext())
        transactionsAdapter = TransactionsAdapter(this, getIconPack())
        binding.rcvTransactions.adapter = transactionsAdapter

        binding.btnClearFilter.setOnClickListener {
            binding.spnCategory.setSelection(0)
            binding.spnMonth.setSelection(0)
        }

    }

    private fun observeAllTransactions() {
        transactionViewModel.allTransactions.observe(viewLifecycleOwner) { transactions ->
            run {
                transactions.let {
                    if (it.isNotEmpty()) {
                        binding.lnrFilter.visibility = View.VISIBLE
                        binding.rcvTransactions.visibility = View.VISIBLE
                        binding.tvNoResult.visibility = View.GONE
                        transactionList = it
                        transactionsAdapter.transactions(it)
                    } else {
                        binding.rcvTransactions.visibility = View.GONE
                        binding.lnrFilter.visibility = View.GONE
                        binding.tvNoResult.visibility = View.VISIBLE
                    }
                }
            }
        }


        categoryViewModel.allCategories.observe(viewLifecycleOwner) { category ->
            run {
                category.let {
                    if (it.isNotEmpty()) {
                        binding.lnrCategory.visibility = View.VISIBLE
                        setCategorySpinner(category)
                    } else {
                        binding.lnrCategory.visibility = View.GONE
                    }
                }
            }
        }

        transactionViewModel.monthFilter.observe(viewLifecycleOwner) { transactions ->
            run {
                transactions.let {
                    if (it.isNotEmpty()) {
                        setMonthSpinner(transactions)
                    } else {
                        monthFilterList = emptyList()
                    }
                }
            }
        }
    }

    private fun setMonthSpinner(transactions: List<TransactionFilterDateData>) {
        transactions.forEach {
            it.monthYearName = BudgetTrackerUtils.getMonthYearName(it.monthName, it.yearNum)

        }
        monthFilterList = transactions

        var spnList = transactions.map { it.monthYearName }.toTypedArray()
        val list: MutableList<String> = spnList.toMutableList()
        list.add(0, "All")


        val adapter = ArrayAdapter(
            requireActivity(),
            R.layout.simple_spinner_item,
            R.id.tvSpinnerText,
            list
        )
        binding.spnMonth.adapter = adapter

        binding.spnMonth.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?, position: Int, id: Long
            ) {
                if (list[position].equals("All", true)) {
                    selectedMonth = ""
                } else {
                    selectedMonth = transactions[position - 1].monthYear
                }
                filterTransactionList()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }

    }

    private fun setCategorySpinner(category: List<CategoryEntity>) {
        var spnList = category.map { it.categoryName }.toTypedArray()
        val list: MutableList<String> = spnList.toMutableList()
        list.add(0, "All")


        val adapter = ArrayAdapter(
            requireActivity(),
            R.layout.simple_spinner_item,
            R.id.tvSpinnerText,
            list
        )
        binding.spnCategory.adapter = adapter


        binding.spnCategory.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?, position: Int, id: Long
            ) {
                if (list[position].equals("All", true)) {
                    selectedCategory = ""
                } else {
                    selectedCategory = spnList[position - 1]
                }
                filterTransactionList()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }

    }

    @SuppressLint("SetTextI18n")
    private fun filterTransactionList() {
        var tempList = emptyList<TransactionsEntity>()
        if (transactionList.isNotEmpty()) {
            tempList = transactionList
        }
        if (!TextUtils.isEmpty(selectedCategory)) {
            if (transactionList.isNotEmpty()) {
                tempList = transactionList.filter { it.categoryName.equals(selectedCategory, true) }
            }
        }
        if (!TextUtils.isEmpty(selectedMonth)) {
            if (transactionList.isNotEmpty()) {
                tempList = tempList.filter {
                    DateUtils.oneFormatToAnother(
                        it.transactionDate,
                        "yyyy-MM-dd",
                        "MM-yyyy"
                    ).equals(selectedMonth, true)
                }
            }

            if (monthFilterList.isNotEmpty() && (binding.spnMonth.selectedItemPosition >0)) {
                var obj = monthFilterList[binding.spnMonth.selectedItemPosition - 1]
                binding.lnrMonthlySummary.visibility = View.VISIBLE
                binding.tvTotalIncome.text ="₹ " + obj.totalIncome.toInt().toString()
                binding.tvTotalExpense.text ="₹ " + obj.totalExpense.toInt().toString()
                var balance = obj.totalIncome.toInt() - obj.totalExpense.toInt()
                binding.tvBalance.text = "₹ $balance"
                binding.tvMonthlySummary.text = getString(R.string.title_monthly_summary)+" - "+obj.monthYearName


            } else {
                binding.lnrMonthlySummary.visibility = View.GONE
            }


        } else {
            binding.lnrMonthlySummary.visibility = View.GONE
        }




        if (!TextUtils.isEmpty(selectedMonth) || !TextUtils.isEmpty(selectedCategory)) {

            if (tempList.isEmpty()) {
                binding.lnrClrFilter.visibility = View.VISIBLE
            } else {
                binding.lnrClrFilter.visibility = View.GONE
            }
        } else {
            binding.lnrClrFilter.visibility = View.GONE
        }

        transactionsAdapter.transactions(tempList)
    }


    override fun onResume() {
        super.onResume()
        if (requireActivity() is MainActivity) {
            (activity as MainActivity).showBottomNavigation(true)
            (activity as MainActivity).showAppBar(true)
            (activity as MainActivity).showTitle(getString(R.string.title_transactions))
        }
    }

    private fun getIconPack(): IconPack {
        val loader = IconPackLoader(requireActivity())
        var mIconPack = createDefaultIconPack(loader)
        mIconPack.loadDrawables(loader.drawableLoader)
        return mIconPack
    }

}
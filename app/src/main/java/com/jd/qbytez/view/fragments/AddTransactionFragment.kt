package com.jd.qbytez.view.fragments


import android.app.DatePickerDialog
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.jd.qbytez.R
import com.jd.qbytez.application.BudgetTrackerApplication
import com.jd.qbytez.databinding.FragmentAddRecordsBinding
import com.jd.qbytez.models.CategoryEntity
import com.jd.qbytez.models.TransactionsEntity
import com.jd.qbytez.utils.BudgetTrackerUtils
import com.jd.qbytez.utils.Constants
import com.jd.qbytez.utils.DateUtils
import com.jd.qbytez.view.activities.MainActivity
import com.jd.qbytez.viewModel.CategoryViewModel
import com.jd.qbytez.viewModel.CategoryViewModelFactory
import com.jd.qbytez.viewModel.TransactionViewModel
import com.jd.qbytez.viewModel.TransactionViewModelFactory
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar


class AddTransactionFragment : Fragment() {

    private lateinit var binding: FragmentAddRecordsBinding
    private lateinit var categoryList: List<CategoryEntity>
    private var cal: Calendar = Calendar.getInstance()
    var transactionsEntity: TransactionsEntity? = null
    private var editTransaction = false
    private var selectedDate = ""
    private var transactionType = ""
    private var categoryName = ""
    private var categoryIconId = 0

    private val categoryViewModel: CategoryViewModel by viewModels {
        CategoryViewModelFactory((requireActivity().application as BudgetTrackerApplication).categoryRepository)
    }

    private val transactionViewModel: TransactionViewModel by viewModels {
        TransactionViewModelFactory((requireActivity().application as BudgetTrackerApplication).transactionsRepository)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        binding = FragmentAddRecordsBinding.inflate(layoutInflater, container, false)
         return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (transactionsEntity != null) {
            editTransaction = true
        }

        setSpinners()
        observeAllCategories()
        onClickFunctions()
        setValuesInViews()

    }

    private fun setValuesInViews() {
        if (transactionsEntity != null) {
            (activity as MainActivity).showTitle(getString(R.string.title_update_transaction))
            binding.btnTransaction.text = getString(R.string.title_update)
            binding.etTransTitle.setText(transactionsEntity?.title)
            binding.etAmount.setText(transactionsEntity?.amount?.let {
                BudgetTrackerUtils.getFormattedAmount(
                    false,
                    it, false
                )
            })
            binding.etdesc.setText(transactionsEntity?.transactionDescription)
            selectedDate = transactionsEntity?.transactionDate.toString()
        } else {
            (activity as MainActivity).showTitle(getString(R.string.title_create_transaction))
            binding.btnTransaction.text = getString(R.string.title_create)
            selectedDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))

        }
        showFormattedDate()
    }

    private fun showFormattedDate() {
        binding.tvTansDate.text =
            DateUtils.oneFormatToAnother(selectedDate, "yyyy-MM-dd", "dd-MMM-yyyy")
    }

    private fun onClickFunctions() {

        binding.btnTransaction.setOnClickListener {
            if (validateForm()) {
                prepareData()
            }
        }

        binding.cvTransDate.setOnClickListener {
            val dpd = DatePickerDialog(
                requireActivity(),
                R.style.MaterialCalendarCustomStyle,
                { _, year, month, dayOfMonth ->
                    selectedDate = String.format("%d", year) + "-" +
                            String.format("%02d", month + 1) + "-" +
                            String.format("%02d", dayOfMonth)
                    showFormattedDate()
                },
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DATE)
            )
            dpd.datePicker.maxDate = cal.timeInMillis;
            dpd.show()
        }
    }

    private fun validateForm(): Boolean {

        if (TextUtils.isEmpty(binding.etTransTitle.text.toString())) {
            binding.etTransTitle.error = "Cannot be empty"
            Toast.makeText(requireActivity(), "Enter transaction title", Toast.LENGTH_SHORT).show()
            return false
        }

        if (TextUtils.isEmpty(binding.etAmount.text.toString())) {
            binding.etAmount.error = "Cannot be empty"
            Toast.makeText(requireActivity(), "Enter transaction amount", Toast.LENGTH_SHORT).show()
            return false
        }


        return true
    }

    private fun prepareData() {

        if (editTransaction) {
            transactionsEntity?.title = binding.etTransTitle.text.toString()
            transactionsEntity?.transactionDescription = binding.etdesc.text.toString()
            transactionsEntity?.amount = binding.etAmount.text.toString().toDouble()
            transactionsEntity?.transactionType = transactionType
            transactionsEntity?.categoryName = categoryName
            transactionsEntity?.categoryIconId = categoryIconId
            transactionsEntity?.transactionDate = selectedDate
            transactionsEntity?.let { transactionViewModel.update(it) }
        } else {

            transactionsEntity = TransactionsEntity(
                binding.etTransTitle.text.toString(),
                binding.etAmount.text.toString().toDouble(),
                transactionType,categoryName,categoryIconId,selectedDate,
                binding.etdesc.text.toString(),
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                0
            )
            transactionsEntity?.let { transactionViewModel.insert(it) }

        }
        (requireActivity() as MainActivity).handleBackPressed()
    }

    private fun observeAllCategories() {
        categoryViewModel.allCategories.observe(viewLifecycleOwner) { transactions ->
            run {
                transactions.let {
                    if (it.isNotEmpty()) {
                        categoryList = it
                    } else {
                        // call create category page from here
                    }
                }
            }
        }
    }

    private fun setSpinners() {

        val adapter = ArrayAdapter(
            requireActivity(),
            R.layout.simple_spinner_item,
            R.id.tvSpinnerText,
            Constants.transactionType()
        )
        binding.spnTransType.adapter = adapter

        if (transactionsEntity != null) {
            val spinnerPosition = adapter.getPosition(transactionsEntity?.transactionType)
            binding.spnTransType.setSelection(spinnerPosition)
        }

        binding.spnTransType.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View, position: Int, id: Long
            ) {
                transactionType = Constants.transactionType()[position]
                val transCatList = categoryList.filter {
                    it.transactionType.equals(
                        Constants.transactionType()[position],
                        true
                    )
                }
                setCategorySpinner(transCatList)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }

    }

    private fun setCategorySpinner(transCatList: List<CategoryEntity>) {


        var spnList = transCatList.map { it.categoryName }.toTypedArray()



        val adapter = ArrayAdapter(
            requireActivity(),
            R.layout.simple_spinner_item,
            R.id.tvSpinnerText,
            spnList
        )
        binding.spnCategory.adapter = adapter
        if (transactionsEntity != null) {
            val spinnerPosition = adapter.getPosition(transactionsEntity?.categoryName)
            binding.spnCategory.setSelection(spinnerPosition)
        }
        binding.spnCategory.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View, position: Int, id: Long
            ) {
                categoryName = transCatList[position].categoryName
                categoryIconId = transCatList[position].iconId
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (requireActivity() is MainActivity) {
            (activity as MainActivity).showBottomNavigation(false)
            (activity as MainActivity).showAppBar(true)

        }
    }


}
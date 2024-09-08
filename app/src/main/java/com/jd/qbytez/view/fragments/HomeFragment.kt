package com.jd.qbytez.view.fragments


import android.R.attr.data
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.jd.qbytez.application.BudgetTrackerApplication
import com.jd.qbytez.database.dao.BTChartData
import com.jd.qbytez.databinding.FragmentHomeBinding
import com.jd.qbytez.models.TransactionsEntity
import com.jd.qbytez.utils.Constants.chartDisplay
import com.jd.qbytez.utils.Constants.monthNum
import com.jd.qbytez.view.activities.MainActivity
import com.jd.qbytez.view.adapters.TransactionsAdapter
import com.jd.qbytez.viewModel.TransactionViewModel
import com.jd.qbytez.viewModel.TransactionViewModelFactory
import com.maltaisn.icondialog.pack.IconPack
import com.maltaisn.icondialog.pack.IconPackLoader
import com.maltaisn.iconpack.defaultpack.createDefaultIconPack


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val transactionViewModel: TransactionViewModel by viewModels {
        TransactionViewModelFactory((requireActivity().application as BudgetTrackerApplication).transactionsRepository)
    }





    private lateinit var transactionsAdapter: TransactionsAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)

        observeAll()

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        binding.rcvTransactions.layoutManager = LinearLayoutManager(requireContext())

        binding.fabNew.setOnClickListener {
            (requireActivity() as MainActivity).pushFragments(AddTransactionFragment())
        }
        binding.ivSettings.setOnClickListener {
            (requireActivity() as MainActivity).pushFragments(SettingsFragment())

        }
    }


    override fun onResume() {
        super.onResume()
        if (requireActivity() is MainActivity) {
            (activity as MainActivity).showBottomNavigation(true)
            (activity as MainActivity).showAppBar(false)
        }
    }



    private fun observeAll() {
        transactionViewModel.allTransactions.observe(viewLifecycleOwner) { transactions ->
            run {
                transactions.let {
                    if (it.isNotEmpty()) {
                        binding.clRecentTransactions.visibility = View.VISIBLE
                        showDashboardCount(transactions)


                    } else {
                        binding.clRecentTransactions.visibility = View.GONE

                    }
                }
            }
        }

        transactionViewModel.recentTransactions.observe(viewLifecycleOwner) { transactions ->
            run {
                transactions.let {
                    if (it.isNotEmpty()) {
                        binding.clRecentTransactions.visibility = View.VISIBLE
                        transactionsAdapter = TransactionsAdapter(this, getIconPack())
                        binding.rcvTransactions.adapter = transactionsAdapter
                        transactionsAdapter.transactions(transactions)

                    } else {
                        binding.clRecentTransactions.visibility = View.GONE

                    }
                }
            }
        }

        transactionViewModel.mChartData.observe(viewLifecycleOwner) { transactions ->
            run {
                transactions.let {
                    if (it.isNotEmpty()) {
                        binding.clChart.visibility = View.VISIBLE
                        prepareChart(transactions)

                    } else {
                        binding.clChart.visibility = View.GONE

                    }
                }
            }
        }
    }



    @SuppressLint("Recycle")
    private fun showDashboardCount(transactions: List<TransactionsEntity>?) {

        val transactionList: List<TransactionsEntity>? = transactions
        var income = 0.0
        var expense = 0.0

        if (transactionList != null) {
            for (i in transactionList){
                if (i.transactionType == "Income") {
                    income += i.amount
                } else {
                    expense += i.amount
                }

            }
        }

        val anim = ValueAnimator.ofInt(0, (income-expense).toInt())
        val anim1 = ValueAnimator.ofInt(0, income.toInt())
        val anim2 = ValueAnimator.ofInt(0, expense.toInt())
        anim.addUpdateListener {  binding.tvCashBalance.text = anim.animatedValue.toString()}
        anim1.addUpdateListener {  binding.tvIncome.text = anim1.animatedValue.toString()}
        anim2.addUpdateListener {  binding.tvExpense.text = anim2.animatedValue.toString()}

        anim.setDuration(1000);
        anim.start();

        anim1.setDuration(1000);
        anim1.start();

        anim2.setDuration(1000);
        anim2.start();


    }

    private fun getIconPack(): IconPack {
        val loader = IconPackLoader(requireActivity())
        var mIconPack = createDefaultIconPack(loader)
        mIconPack.loadDrawables(loader.drawableLoader)
        return mIconPack
    }

    private fun prepareChart(transactions: List<BTChartData>?) {

        var barDataSet1: BarDataSet? = null
        var barDataSet2: BarDataSet? = null

        var incomeBarEntries = ArrayList<BarEntry>()
        var expenseBarEntries = ArrayList<BarEntry>()
        monthNum.forEachIndexed { index, element ->

            var pos = index+1
             var cData = transactions?.filter { it.monthNum.equals(element) }
            if(cData?.isNotEmpty() == true){
                incomeBarEntries.add(BarEntry(pos.toFloat(), cData[0].totalIncome.toFloat()))
                expenseBarEntries.add(BarEntry(pos.toFloat(), cData[0].totalExpense.toFloat()))
            }
            else{
                incomeBarEntries.add(BarEntry(pos.toFloat(), 0f))
                expenseBarEntries.add(BarEntry(pos.toFloat(), 0f))
            }

        }


        barDataSet1 =  BarDataSet(incomeBarEntries, "Income");
        barDataSet1!!.setColor(Color.parseColor("#00C22C"))

        barDataSet2 =  BarDataSet(expenseBarEntries, "Expense");
        barDataSet2!!.setColor(Color.parseColor("#FFBF0606"))



        val mBarData = BarData(barDataSet1, barDataSet2)
        binding.idBarChart.data= mBarData
        binding.idBarChart.xAxis.valueFormatter = IndexAxisValueFormatter(chartDisplay)
        binding.idBarChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        binding.idBarChart.xAxis.setCenterAxisLabels(true)
        binding.idBarChart.xAxis.setAxisMinimum(0F)
        binding.idBarChart.setPinchZoom(false)
        binding.idBarChart.setDrawGridBackground(false)
        binding.idBarChart.setFitBars(true)
        binding.idBarChart.description.isEnabled =false
        binding.idBarChart.animate()
        binding.idBarChart.invalidate()

    }


}
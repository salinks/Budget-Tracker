package com.jd.qbytez.view.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.jd.qbytez.R
import com.jd.qbytez.application.BudgetTrackerApplication
import com.jd.qbytez.databinding.FragmentCategoriesBinding
import com.jd.qbytez.databinding.FragmentTransactionsBinding
import com.jd.qbytez.models.CategoryEntity
import com.jd.qbytez.view.activities.MainActivity
import com.jd.qbytez.view.adapters.CategoryAdapter
import com.jd.qbytez.viewModel.CategoryViewModel
import com.jd.qbytez.viewModel.CategoryViewModelFactory
import com.maltaisn.icondialog.pack.IconPack
import com.maltaisn.icondialog.pack.IconPackLoader
import com.maltaisn.iconpack.defaultpack.createDefaultIconPack


class CategoriesFragment : Fragment() {

    private lateinit var catAdapter: CategoryAdapter
    private lateinit var binding: FragmentCategoriesBinding
    private val categoryViewModel: CategoryViewModel by viewModels {
        CategoryViewModelFactory((requireActivity().application as BudgetTrackerApplication).categoryRepository)
    }




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        binding = FragmentCategoriesBinding.inflate(layoutInflater, container, false)
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.rcvCategories.layoutManager = LinearLayoutManager(requireContext())
        catAdapter = CategoryAdapter(this, categoryViewModel, getIconPack())
        binding.rcvCategories.adapter = catAdapter

        observeAllCategories()


        binding.fabNew.setOnClickListener {
            (requireActivity() as MainActivity).pushFragments(AddEditCategoriesFragment())
        }

    }
    private fun observeAllCategories() {
        categoryViewModel.allCategories.observe(viewLifecycleOwner) { category ->
            run {
                category.let {
                    if (it.isNotEmpty()) {
                        binding.rcvCategories.visibility = View.VISIBLE
                        binding.tvNoRecords.visibility = View.GONE
                        catAdapter.categories(it)
                    } else {
                        binding.rcvCategories.visibility = View.GONE
                        binding.tvNoRecords.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (requireActivity() is MainActivity) {
            (activity as MainActivity).showBottomNavigation(false)
            (activity as MainActivity).showAppBar(true)
            (activity as MainActivity).showTitle(getString(R.string.title_categories))
        }
    }

    private fun getIconPack(): IconPack {
        val loader = IconPackLoader(requireActivity())
        var mIconPack = createDefaultIconPack(loader)
        mIconPack.loadDrawables(loader.drawableLoader)
        return mIconPack
    }
}
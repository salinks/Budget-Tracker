package com.jd.qbytez.view.fragments


import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.jd.qbytez.R
import com.jd.qbytez.application.BudgetTrackerApplication
import com.jd.qbytez.databinding.FragmentAddEditCategoryBinding
import com.jd.qbytez.models.CategoryEntity
import com.jd.qbytez.utils.Constants
import com.jd.qbytez.view.activities.MainActivity
import com.jd.qbytez.viewModel.CategoryViewModel
import com.jd.qbytez.viewModel.CategoryViewModelFactory
import com.maltaisn.icondialog.IconDialog
import com.maltaisn.icondialog.IconDialogSettings
import com.maltaisn.icondialog.data.Icon
import com.maltaisn.icondialog.pack.IconPack
import com.maltaisn.icondialog.pack.IconPackLoader
import com.maltaisn.iconpack.defaultpack.createDefaultIconPack


class AddEditCategoriesFragment : Fragment(), IconDialog.Callback {


    private val categoryViewModel: CategoryViewModel by viewModels {
        CategoryViewModelFactory((requireActivity().application as BudgetTrackerApplication).categoryRepository)
    }

    private val iconDialogTag = "icon-dialog"
    private lateinit var binding: FragmentAddEditCategoryBinding
    private var editCategory = false
    private var iconId = -1
    var transactionType = ""
    var categoryEntity: CategoryEntity? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {

        binding = FragmentAddEditCategoryBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val iconDialog = childFragmentManager.findFragmentByTag(iconDialogTag) as IconDialog?
            ?: IconDialog.newInstance(IconDialogSettings())


        if (categoryEntity != null) {
            editCategory = true
        }

        setPageTitles()
        setDataOnViews()

        binding.cvCatIcon.setOnClickListener {
            iconDialog.show(childFragmentManager, iconDialogTag)
        }

        binding.btnCategory.setOnClickListener {
            if (validateForm()) {
                prepareData()
            }
        }
    }

    private fun prepareData() {
        if (editCategory) {
            categoryEntity?.categoryName = binding.etCategoryName.text.toString()
            categoryEntity?.transactionType = transactionType
            categoryEntity?.iconId = iconId
            categoryEntity?.let { categoryViewModel.update(it) }
        } else {

            categoryEntity = CategoryEntity(
                0,
                binding.etCategoryName.text.toString(),
                transactionType, iconId
            )
            categoryEntity?.let { categoryViewModel.insert(it) }

        }
        (requireActivity() as MainActivity).handleBackPressed()

    }

    private fun validateForm(): Boolean {
        if (TextUtils.isEmpty(binding.etCategoryName.text.toString())) {
            binding.etCategoryName.error = "Cannot be empty"
            Toast.makeText(requireActivity(), "Enter category name", Toast.LENGTH_SHORT).show()
            return false
        }

        if (iconId < 0) {
            Toast.makeText(requireActivity(), "Select category icon", Toast.LENGTH_SHORT).show()
            return false
        }


        return true
    }

    private fun setDataOnViews() {


        val adapter = ArrayAdapter(
            requireActivity(),
            R.layout.simple_spinner_item, R.id.tvSpinnerText, Constants.transactionType()
        )
        binding.spnTransType.adapter = adapter

        if (categoryEntity != null) {
            val spinnerPosition = adapter.getPosition(categoryEntity?.transactionType)
            binding.spnTransType.setSelection(spinnerPosition)
        }

        binding.spnTransType.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View, position: Int, id: Long
            ) {


                transactionType = Constants.transactionType()[position]
                if (Constants.transactionType()[position].equals("Income", true)) {
                    binding.etCategoryName.hint = "Salary,Investments..etc"
                } else {
                    binding.etCategoryName.hint = "Food,Transport,Entertainment..etc"
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }

    }

    private fun setPageTitles() {

        if (editCategory) {
            (activity as MainActivity).showTitle(getString(R.string.title_update_category))
            binding.btnCategory.text = getString(R.string.title_update)
            binding.etCategoryName.setText(categoryEntity?.categoryName)
            iconId = categoryEntity?.iconId!!
            binding.ivIcon.setImageDrawable(iconDialogIconPack.getIcon(iconId)?.drawable)
            binding.tvBtnText.visibility = View.GONE

        } else {
            (activity as MainActivity).showTitle(getString(R.string.title_create_category))
            binding.btnCategory.text = getString(R.string.title_create)
        }

    }

    override fun onResume() {
        super.onResume()
        if (requireActivity() is MainActivity) {
            (activity as MainActivity).showBottomNavigation(false)
            (activity as MainActivity).showAppBar(true)

        }
    }

    override val iconDialogIconPack: IconPack
        get() = getIconPack()



    override fun onIconDialogIconsSelected(dialog: IconDialog, icons: List<Icon>) {
        binding.ivIcon.setImageDrawable(icons.map { it.drawable }[0])
        binding.tvBtnText.visibility = View.GONE
        iconId = icons.map { it.id }[0]
    }

    private fun getIconPack(): IconPack {
        val loader = IconPackLoader(requireActivity())
        var mIconPack = createDefaultIconPack(loader)
        mIconPack.loadDrawables(loader.drawableLoader)
        return mIconPack
    }
}
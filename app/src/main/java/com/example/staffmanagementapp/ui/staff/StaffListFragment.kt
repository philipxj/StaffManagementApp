package com.example.staffmanagementapp.ui.stafflist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.staffmanagementapp.databinding.FragmentStaffListBinding
import com.example.staffmanagementapp.di.Injection
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class StaffListFragment : Fragment() {

    private var _binding: FragmentStaffListBinding? = null
    private val binding get() = _binding!!

    private val staffListViewModel: StaffListViewModel by lazy {
        ViewModelProvider(this, Injection.viewModelFactory)[StaffListViewModel::class.java]
    }

    private val args: StaffListFragmentArgs by navArgs()
    private lateinit var staffListAdapter: StaffListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStaffListBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup toolbar back navigation click listener
        binding.toolbar.setNavigationOnClickListener {
            val action = StaffListFragmentDirections.actionStaffListFragmentToLoginFragment()
            findNavController().navigate(action)
        }

        setupRecyclerView()
        setupLoadMoreButton()

        // Set the token passed from the login page (or use default if accessed directly)
        val loginToken = if (args.loginToken.isNotEmpty()) args.loginToken else "default-token"
        staffListViewModel.setLoginToken(loginToken)

        observeUiState()

        // Load the first page of data when entering for the first time
        staffListViewModel.loadInitialStaffList()
    }

    private fun setupRecyclerView() {
        staffListAdapter = StaffListAdapter()
        binding.recyclerViewStaff.apply {
            adapter = staffListAdapter
            layoutManager = LinearLayoutManager(context)

            // Add scroll listener to detect when user reaches bottom
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    updateLoadMoreButtonVisibility()
                }
            })
        }
    }

    private fun setupLoadMoreButton() {
        binding.buttonLoadMore.setOnClickListener {
            staffListViewModel.loadMoreStaff()
        }
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(androidx.lifecycle.Lifecycle.State.STARTED) {
                staffListViewModel.uiState.collectLatest { state ->
                    // Bind the state to DataBinding
                    binding.uiState = state
                    // Submit list data to the Adapter
                    staffListAdapter.submitList(state.staff) {
                        // Update button visibility after list is updated
                        updateLoadMoreButtonVisibility()
                    }
                    // Handle errors
                    if (state.error != null) {
                        Toast.makeText(context, state.error, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun updateLoadMoreButtonVisibility() {
        val layoutManager = binding.recyclerViewStaff.layoutManager as? LinearLayoutManager
        val currentState = staffListViewModel.uiState.value

        if (layoutManager != null && currentState != null) {
            val totalItemCount = layoutManager.itemCount
            val lastVisibleItem = layoutManager.findLastVisibleItemPosition()

            // Show button when user is near bottom and there are more pages
            val isNearBottom = lastVisibleItem >= totalItemCount - 3 // Show when 3 items from bottom
            val hasMorePages = !currentState.isLastPage
            val hasItems = currentState.staff.isNotEmpty()

            // Show button if there are more pages and items, regardless of scroll position
            // This ensures button shows immediately after fresh load if there are more pages
            binding.buttonLoadMore.visibility = if (hasMorePages && hasItems) {
                android.view.View.VISIBLE
            } else {
                android.view.View.GONE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Clean up binding to avoid memory leaks
        _binding = null
    }
}

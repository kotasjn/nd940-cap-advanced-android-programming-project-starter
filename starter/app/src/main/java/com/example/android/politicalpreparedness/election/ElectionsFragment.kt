package com.example.android.politicalpreparedness.election

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android.politicalpreparedness.base.BaseFragment
import com.example.android.politicalpreparedness.databinding.FragmentElectionBinding
import com.example.android.politicalpreparedness.election.adapter.ElectionListAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class ElectionsFragment : BaseFragment() {

    private lateinit var binding: FragmentElectionBinding

    override val viewModel by viewModel<ElectionsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentElectionBinding.inflate(inflater)
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = this
        setupRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadElections()
    }

    private fun setupRecyclerView() {
        val upcomingElectionsAdapter =
            ElectionListAdapter(ElectionListAdapter.ElectionListener(viewModel::navigateToElectionDetail))

        binding.upcomingElectionRecyclerView.apply {
            layoutManager = LinearLayoutManager(this.context)
            adapter = upcomingElectionsAdapter
        }

        val savedElectionsAdapter =
            ElectionListAdapter(ElectionListAdapter.ElectionListener(viewModel::navigateToElectionDetail))

        binding.savedElectionsRecyclerView.apply {
            layoutManager = LinearLayoutManager(this.context)
            adapter = savedElectionsAdapter
        }

    }
}
package com.example.android.politicalpreparedness.election.adapter

import android.view.View
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.android.politicalpreparedness.network.models.Election

@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: LiveData<List<Election>>) {
    data.value.let { electionList ->
        (recyclerView.adapter as ElectionListAdapter).apply {
            submitList(electionList)
        }
    }
}

@BindingAdapter("viewVisible")
fun bindRecyclerView(view: View, isLoading: LiveData<Boolean>) {
    isLoading.value?.let {
        view.visibility = if (it) View.VISIBLE else View.INVISIBLE
    }
}

package com.example.android.politicalpreparedness.election.adapter

import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.network.models.Election
import java.text.SimpleDateFormat
import java.util.*

@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: LiveData<List<Election>>) {
    data.value.let { electionList ->
        (recyclerView.adapter as ElectionListAdapter).apply {
            submitList(electionList)
        }
    }
}

@BindingAdapter("viewVisible")
fun bindRecyclerView(view: View, isLoading: Boolean) {
    view.visibility = if (isLoading) View.VISIBLE else View.INVISIBLE
}

@BindingAdapter("textDate")
fun textDate(textView: TextView, date: Date?) {
    if (date == null) {
        textView.text = ""
    } else {
        val format = SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.US)
        textView.text = format.format(date)
    }
}

@BindingAdapter("followText")
fun followText(button: AppCompatButton, isFollowing: Boolean?) {
    isFollowing?.let {
        if (it) button.setText(R.string.unfollow_election)
        else button.setText(R.string.follow_election)
    }
}

@BindingAdapter("goneIfNull")
fun goneIfNull(view: View, it: Any?) {
    view.visibility = if (it == null) View.GONE else View.VISIBLE
}

@BindingAdapter("setCorrespondenceAddress")
fun setCorrespondenceAddress(textView: TextView, correspondenceAddress: Address?) {
    correspondenceAddress?.let {
        textView.text = correspondenceAddress.toFormattedString()
    }
}
package com.example.android.politicalpreparedness.representative.adapter

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.RequestListener
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.representative.model.Representative


@BindingAdapter("profileImage")
fun fetchImage(view: ImageView, src: String?) {
    if (src != null) {
        val uri = src.toUri().buildUpon().scheme("https").build()
        Glide.with(view).load(uri)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    view.setImageResource(R.drawable.ic_profile)
                    return true
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }

            })
            .transform(CircleCrop())
            .placeholder(R.drawable.loading_animation)
            .into(view)
    } else {
        view.setImageResource(R.drawable.ic_profile)
    }
}

inline fun <reified T> toTypedAdapter(adapter: ArrayAdapter<*>): ArrayAdapter<T> {
    return adapter as ArrayAdapter<T>
}

@BindingAdapter("selectedValue")
fun setSpinnerValue(spinner: Spinner, value: String?) {
    val adapter = toTypedAdapter<String>(spinner.adapter as ArrayAdapter<*>)
    val position = when (adapter.getItem(0)) {
        is String -> adapter.getPosition(value)
        else -> spinner.selectedItemPosition
    }
    if (position >= 0) {
        spinner.setSelection(position)
    }
}

@InverseBindingAdapter(attribute = "selectedValue")
fun getSpinnerValue(spinner: Spinner): String {
    return spinner.selectedItem.toString()
}

@BindingAdapter("selectedValueAttrChanged")
fun setProvinceListener(spinner: Spinner, spinnerChange: InverseBindingListener?) {
    if (spinnerChange == null) {
        spinner.onItemSelectedListener = null
    } else {
        val listener: AdapterView.OnItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    spinnerChange.onChange()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    spinnerChange.onChange()
                }
            }
        spinner.onItemSelectedListener = listener
    }
}

@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: LiveData<List<Representative>>) {
    data.value.let { representativeList ->
        (recyclerView.adapter as RepresentativeListAdapter).apply {
            submitList(representativeList)
        }
    }
}

package com.nazrah.nazrahapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nazrah.nazrahapp.R
import com.nazrah.nazrahapp.databinding.LayoutDefaultItemsBinding
import com.nazrah.nazrahapp.databinding.WalkthroughItemBinding
import com.nazrah.nazrahapp.models.ProfileItem
import com.nazrah.nazrahapp.models.ViewPagerItem
import dagger.hilt.android.AndroidEntryPoint

class ProfileAdapter(
    private val mListener: ProfileItemClickListener,
    private val profileItems: List<ProfileItem?>
) : RecyclerView.Adapter<ProfileAdapter.ProfileHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileHolder {
        val binding = LayoutDefaultItemsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ProfileHolder(binding)
    }

    override fun onBindViewHolder(holder: ProfileHolder, position: Int) {
        holder.apply {
            binding.apply {
                clickListener = mListener
                data = profileItems[position]
                ivItemImage.setImageResource(data?.image ?: 0)

                binding.executePendingBindings()
            }
        }

    }

    override fun getItemCount() = profileItems.size

    inner class ProfileHolder(val binding: LayoutDefaultItemsBinding) :
        RecyclerView.ViewHolder(binding.root)

}

class ProfileItemClickListener(
    val onClick: (model:ProfileItem ) -> Unit,
) {
    fun onClicked(model:ProfileItem ) = onClick(model )
}
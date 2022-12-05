package com.nazrah.nazrahapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nazrah.nazrahapp.R
import com.nazrah.nazrahapp.databinding.WalkthroughItemBinding
import com.nazrah.nazrahapp.models.ViewPagerItem

class ViewPagerAdapter(
    private val mListener: ViewPagerClickListener,
    private val viewPagerItems: List<ViewPagerItem?>
) : RecyclerView.Adapter<ViewPagerAdapter.PageHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerAdapter.PageHolder {
        val binding = WalkthroughItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PageHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewPagerAdapter.PageHolder, position: Int) {
        holder.apply {
            binding.apply {
                clickListener = mListener
                data = viewPagerItems[position]
                ivThumbnail.setImageResource(data?.image ?: 0)
                if (position == viewPagerItems.size.dec()) {
                    btnNext.text=root.context.getString(R.string.start_journey)
                }
                binding.executePendingBindings()
            }
        }

    }

    override fun getItemCount() = viewPagerItems.size

    inner class PageHolder(val binding: WalkthroughItemBinding) :
        RecyclerView.ViewHolder(binding.root)

}

class ViewPagerClickListener(
    val nextClicked: () -> Unit,
) {
    fun onNextClicked() = nextClicked()
}

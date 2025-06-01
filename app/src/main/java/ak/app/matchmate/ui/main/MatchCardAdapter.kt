package ak.app.matchmate.ui.main

import ak.app.matchmate.R
import ak.app.matchmate.databinding.ItemMatchCardBinding
import ak.app.matchmate.ui.model.MatchCardUiModel
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class MatchCardAdapter(
    private val onAcceptClick: (String) -> Unit, // Callback for Accept button click (UUID)
    private val onDeclineClick: (String) -> Unit, // Callback for Decline button click (UUID)
    internal var showImages: Boolean // Flag to control image visibility
) : ListAdapter<MatchCardUiModel, MatchCardAdapter.MatchCardViewHolder>(MatchCardDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatchCardViewHolder {
        val binding = ItemMatchCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MatchCardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MatchCardViewHolder, position: Int) {
        val matchCard = getItem(position)
        holder.bind(matchCard, onAcceptClick, onDeclineClick, showImages)
    }

    class MatchCardViewHolder(private val binding: ItemMatchCardBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(
            matchCard: MatchCardUiModel,
            onAcceptClick: (String) -> Unit,
            onDeclineClick: (String) -> Unit,
            showImages: Boolean
        ) {
            binding.apply {
                // Set text views
                tvUserDetails.text = "${matchCard.fullName}, ${matchCard.details}"
                tvMatchScore.text = matchCard.matchScoreText
                tvEducation.text = matchCard.education
                tvReligion.text = matchCard.religion

                // Handle image loading based on showImages flag
                if (showImages) {
                    ivUserImage.visibility = View.VISIBLE
                    Glide.with(itemView.context)
                        .load(matchCard.imageUrl)
                        .circleCrop()
                        .placeholder(R.drawable.placeholder_circle)
                        .error(R.drawable.placeholder_circle)
                        .into(ivUserImage)
                } else {
                    ivUserImage.visibility = View.GONE

                }


                when (matchCard.status) {
                    "ACCEPTED" -> {
                        tvStatus.visibility = View.VISIBLE
                        tvStatus.text = "ACCEPTED"
                        tvStatus.setTextColor(ContextCompat.getColor(itemView.context, android.R.color.holo_green_dark))
                        layoutActionButtons.visibility = View.GONE
                    }
                    "DECLINED" -> {
                        tvStatus.visibility = View.VISIBLE
                        tvStatus.text = "DECLINED"
                        tvStatus.setTextColor(ContextCompat.getColor(itemView.context, android.R.color.holo_red_dark))
                        layoutActionButtons.visibility = View.GONE
                    }
                    "PENDING" -> {
                        tvStatus.visibility = View.GONE
                        layoutActionButtons.visibility = View.VISIBLE
                        btnAccept.setOnClickListener { onAcceptClick(matchCard.uuid) }
                        btnDecline.setOnClickListener { onDeclineClick(matchCard.uuid) }
                    }
                }
            }
        }
    }

    private class MatchCardDiffCallback : DiffUtil.ItemCallback<MatchCardUiModel>() {
        override fun areItemsTheSame(oldItem: MatchCardUiModel, newItem: MatchCardUiModel): Boolean {
            return oldItem.uuid == newItem.uuid // Items are the same if their UUIDs match
        }

        override fun areContentsTheSame(oldItem: MatchCardUiModel, newItem: MatchCardUiModel): Boolean {
            return oldItem == newItem // Contents are the same if all properties are equal (data class auto-generates equals)
        }
    }
}
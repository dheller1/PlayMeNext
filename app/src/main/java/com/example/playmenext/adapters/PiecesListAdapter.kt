package com.example.playmenext.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.playmenext.R
import com.example.playmenext.domain.PieceToPractice
import java.time.Duration
import java.time.LocalDateTime

class PiecesListAdapter(
    private val _onItemClickListener : ((PieceToPractice) -> Unit)? = null,
    private val _onItemLongClickListener : ((PieceToPractice) -> Boolean)? = null
) : ListAdapter<PieceToPractice, PiecesListAdapter.PieceViewHolder>(PieceComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : PieceViewHolder {
        return PieceViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: PieceViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current)
        if(_onItemClickListener != null) {
            holder.itemView.setOnClickListener { _onItemClickListener.invoke(current) }
        }
        if(_onItemLongClickListener != null) {
            holder.itemView.setOnLongClickListener{ _onItemLongClickListener.invoke(current) }
        }
    }

    class PieceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvTitle: TextView = itemView.findViewById(R.id.tv_title)
        private val tvSubtitle: TextView = itemView.findViewById(R.id.tv_subtitle)
        private val tvComposer: TextView = itemView.findViewById(R.id.tv_composer)
        private val tvLastPlayed: TextView = itemView.findViewById(R.id.tv_last_played)

        fun bind(piece: PieceToPractice) {
            tvTitle.text = piece.title
            tvComposer.text = piece.composer
            if(piece.subTitle.isNotEmpty()) {
                tvSubtitle.visibility = View.VISIBLE
                tvSubtitle.text = piece.subTitle
            }
            else {
                tvSubtitle.visibility = View.GONE
            }
            tvLastPlayed.text = lastPlayedText(piece)
        }

        private fun lastPlayedText(piece: PieceToPractice) : String {
            val res = itemView.resources
            val filler =
                if(piece.dateLastPlayed == null) {
                    res.getString(R.string.rc_never)
                }
                else {
                    val deltaT = Duration.between(piece.dateLastPlayed, LocalDateTime.now())
                    if(deltaT < Duration.ofMinutes(1)) {
                        res.getString(R.string.rc_just_now)
                    }
                    else if(deltaT < Duration.ofHours(1)) {
                        String.format(res.getString(R.string.rc_time_ago, "${deltaT.toMinutes()}m"))
                    }
                    else if(deltaT < Duration.ofDays(1)) {
                        String.format(res.getString(R.string.rc_time_ago, "${deltaT.toHours()}h"))
                    }
                    else {
                        String.format(res.getString(R.string.rc_time_ago,"${deltaT.toDays()}d"))
                    }
                }

            return String.format(res.getString(R.string.last_played_text), filler)
        }

        companion object {
            fun create(parent: ViewGroup): PieceViewHolder {
                val view : View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.piecetopractice_item, parent, false)
                return PieceViewHolder(view)
            }
        }
    }

    class PieceComparator : DiffUtil.ItemCallback<PieceToPractice>() {
        override fun areItemsTheSame(oldItem: PieceToPractice, newItem: PieceToPractice): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: PieceToPractice, newItem: PieceToPractice): Boolean {
            return oldItem.id == newItem.id
        }
    }

}
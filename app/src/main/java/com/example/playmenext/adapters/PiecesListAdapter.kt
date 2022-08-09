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
package developer.abdulahad.notes.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import developer.abdulahad.mydatabase.models.Notes
import developer.abdulahad.notes.databinding.NotesItemBinding

class RvAdapter(var list: ArrayList<Notes>, var clickItem: ClickItem) :
    RecyclerView.Adapter<RvAdapter.Vh>() {
    inner class Vh(var notesItemBinding: NotesItemBinding) :
        RecyclerView.ViewHolder(notesItemBinding.root) {
        @SuppressLint("SetTextI18n")
        fun onBind(notes: Notes, position: Int) {
            if (notes.name != null || notes.notes != null) {
                notesItemBinding.tvName.setText(notes.name)
                notesItemBinding.tvNotes.setText(notes.notes)
                notesItemBinding.time.text = notes.dataTime

                if (notesItemBinding.tvName.text.toString().length >= 12){
                    notesItemBinding.tvName.text = "${notesItemBinding.tvName.text.substring(0,10)}.."
                }

                if (notesItemBinding.tvNotes.text.toString().length >= 12){
                    notesItemBinding.tvNotes.text = "${notesItemBinding.tvNotes.text.substring(0,10)}.."
                }

                }
                notesItemBinding.card.setCardBackgroundColor(Color.parseColor(notes.color))
                notesItemBinding.popup.setOnClickListener {
                    clickItem.itemClick(notesItemBinding.root, notes, position)
                }
                notesItemBinding.card.setOnClickListener {
                    clickItem.cardClick(notes, position)
                }
                Glide.with(notesItemBinding.root.context).load(notes.image)
                    .into(notesItemBinding.image)
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(NotesItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(list[position], position)
    }

    override fun getItemCount(): Int = list.size

    interface ClickItem {
        fun itemClick(view: View, notes: Notes, position: Int)
        fun cardClick(notes: Notes, position: Int)
    }
}

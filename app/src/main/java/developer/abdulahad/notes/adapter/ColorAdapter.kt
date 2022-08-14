package developer.abdulahad.notes.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import developer.abdulahad.notes.databinding.ItemColorBinding

class ColorAdapter(var list:ArrayList<String>,var clickColor: ClickColor) : RecyclerView.Adapter<ColorAdapter.Vh>() {
    inner class Vh(var itemColorBinding: ItemColorBinding) : RecyclerView.ViewHolder(itemColorBinding.root){
        fun onBind(color:String,position: Int) {
            itemColorBinding.color.setColorFilter(Color.parseColor(color))
            itemColorBinding.color.setOnClickListener {
                clickColor.click(itemColorBinding,position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(ItemColorBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(list[position],position)
    }

    override fun getItemCount(): Int = list.size

    interface ClickColor{
        fun click(itemColorBinding: ItemColorBinding,position: Int)
    }
}
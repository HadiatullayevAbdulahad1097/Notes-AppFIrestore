package developer.abdulahad.notes

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import developer.abdulahad.mydatabase.models.MyObject
import developer.abdulahad.notes.databinding.ActivityMain4Binding

class MainActivity4 : AppCompatActivity() {
    lateinit var binding: ActivityMain4Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain4Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvName.text = MyObject.notes.name
        binding.tvId.text = MyObject.notes.id.toString()
        Glide.with(this).load(MyObject.notes.image).into(binding.imageAbout)
        binding.tvNotes.text = MyObject.notes.notes
    }
}
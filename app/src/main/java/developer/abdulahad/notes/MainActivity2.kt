package developer.abdulahad.notes

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import developer.abdulahad.mydatabase.models.MyObject
import developer.abdulahad.mydatabase.models.Notes
import developer.abdulahad.notes.adapter.RvAdapter
import developer.abdulahad.notes.databinding.ActivityMain2Binding
import developer.abdulahad.notes.databinding.ItemDialogPermissionBinding

class MainActivity2 : AppCompatActivity() {
    lateinit var binding: ActivityMain2Binding
    lateinit var firebaseFirestore: FirebaseFirestore
    lateinit var storage: FirebaseStorage
    lateinit var storageReference: StorageReference
    lateinit var adapter: RvAdapter
    lateinit var list: ArrayList<Notes>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseFirestore = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()
        storageReference = storage.getReference("imageNotes")

        list = ArrayList()

        adapter = RvAdapter(list, object : RvAdapter.ClickItem {
            @SuppressLint("NotifyDataSetChanged")
            override fun itemClick(view: View, notes: Notes, position: Int) {
                val popupMenu = PopupMenu(binding.root.context, view)
                popupMenu.inflate(R.menu.my_menu)

                popupMenu.setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.edit -> {
                            MyObject.notes = notes
                            MyObject.type = "Edit"
                            startActivity(Intent(this@MainActivity2, MainActivity3::class.java))
                        }
                        R.id.delete -> {
                            val document = firebaseFirestore.collection("Notes").document(notes.id)
                            val alertDialog = AlertDialog.Builder(this@MainActivity2).create()
                            val itemDialogPermissionBinding = ItemDialogPermissionBinding.inflate(layoutInflater)
                            document.delete()
                                .addOnSuccessListener {
                                    Toast.makeText(
                                        this@MainActivity2,
                                        "need to restart the program",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }.addOnFailureListener {
                                    Toast.makeText(
                                        this@MainActivity2,
                                        "Failed",
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                }
                            itemDialogPermissionBinding.btnOk.setOnClickListener {
                                finish()
                                startActivity(Intent(this@MainActivity2, MainActivity::class.java))
                                    Toast.makeText(
                                        this@MainActivity2,
                                        "Delete",
                                        Toast.LENGTH_SHORT
                                    ).show()
                            }
                            itemDialogPermissionBinding.btnCancel.setOnClickListener {
                                alertDialog.cancel()
                            }
                            alertDialog.setView(itemDialogPermissionBinding.root)
                            alertDialog.show()
                        }
                    }
                    true
                }
                popupMenu.show()
            }

            override fun cardClick(notes: Notes, position: Int) {
                MyObject.notes = notes
                startActivity(Intent(this@MainActivity2, MainActivity4::class.java))
            }
        })

        binding.rv.adapter = adapter

        binding.apply {
            firebaseFirestore.collection("Notes")
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        task.result.forEach {
                            val notes = it.toObject(Notes::class.java)
                                list.add(notes)
                        }
                    }
                    adapter.notifyDataSetChanged()
                }
            binding.floating.setOnClickListener {
                finish()
                MyObject.type = "Add"
                startActivity(Intent(this@MainActivity2, MainActivity3::class.java))
            }
        }
    }
}
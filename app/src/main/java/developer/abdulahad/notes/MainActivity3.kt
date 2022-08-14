package developer.abdulahad.notes

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import developer.abdulahad.mydatabase.models.MyObject
import developer.abdulahad.mydatabase.models.Notes
import developer.abdulahad.notes.adapter.ColorAdapter
import developer.abdulahad.notes.databinding.ActivityMain3Binding
import developer.abdulahad.notes.databinding.ItemColorBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MainActivity3 : AppCompatActivity() {
    lateinit var binding: ActivityMain3Binding
    lateinit var firebaseFirestore: FirebaseFirestore
    lateinit var storage: FirebaseStorage
    lateinit var storageReference: StorageReference
    lateinit var firebaseDatabase: FirebaseDatabase
    lateinit var references: DatabaseReference
    lateinit var colorAdapter: ColorAdapter
    lateinit var list: ArrayList<String>
    var imagePath = ""

    @SuppressLint("NotifyDataSetChanged", "NewApi", "SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain3Binding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseDatabase = FirebaseDatabase.getInstance()
        references = firebaseDatabase.getReference("uid")
        firebaseFirestore = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()
        storageReference = storage.getReference("imageNotes")
        list = ArrayList()
        list.add("#FF8A80")
        list.add("#FF9E80")
        list.add("#FFD180")
        list.add("#FFE57F")
        list.add("#FFFF8D")
        list.add("#CCFF90")
        list.add("#B9F6CA")
        list.add("#A7FFEB")
        list.add("#84FFFF")
        list.add("#80D8FF")
        list.add("#82B1FF")
        list.add("#B388FF")
        list.add("#EA80FC")
        list.add("#FF8A80")

        binding.apply {


            when (MyObject.type) {
                "Add" -> {
                    imageAdd.setOnClickListener {
                        getImageContent.launch("image/*")
                    }
                    btnSave.setOnClickListener { view ->
                        val name = edtName.text.toString()
                        var notes = edtNotes.text.toString()
                        val current = LocalDateTime.now()
                        val formatter = DateTimeFormatter.ofPattern("MM-dd HH:mm")
                        val formatted = current.format(formatter)
                        if (name.isNotEmpty() && notes.isNotEmpty() && imagePath.isNotEmpty()) {
                            firebaseFirestore.collection("Notes")
                                .add(
                                    Notes(
                                        name = name,
                                        notes = notes,
                                        image = imagePath,
                                        dataTime = formatted
                                    )
                                )
                                .addOnSuccessListener {
                                    references.child(it.id).setValue(it.id)
                                    val document =
                                        firebaseFirestore.collection("Notes").document(it.id)
                                    val map = mapOf<String, Any>("id" to it.id)
                                    document.update(map)
                                        .addOnSuccessListener { void ->
                                            Toast.makeText(
                                                this@MainActivity3,
                                                "Saved",
                                                Toast.LENGTH_SHORT
                                            )
                                                .show()
                                            rv.visibility = View.VISIBLE
                                            colorAdapter = ColorAdapter(
                                                list,
                                                object : ColorAdapter.ClickColor {
                                                    override fun click(
                                                        itemColorBinding: ItemColorBinding,
                                                        position: Int
                                                    ) {
                                                        val document2 =
                                                            firebaseFirestore.collection("Notes")
                                                                .document(it.id)
                                                        val map2 =
                                                            mapOf<String, Any>("color" to list[position])
                                                        document2.update(map2)
                                                        finish()
                                                        startActivity(
                                                            Intent(
                                                                this@MainActivity3,
                                                                MainActivity2::class.java
                                                            )
                                                        )
                                                    }
                                                })
                                            rv.adapter = colorAdapter

                                        }.addOnFailureListener {
                                            Toast.makeText(
                                                this@MainActivity3,
                                                "Failed",
                                                Toast.LENGTH_SHORT
                                            )
                                                .show()
                                        }

                                }
                        } else {
                            Toast.makeText(
                                this@MainActivity3,
                                "EditTexts or Image Blank",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
                "Edit" -> {
                    imageAdd.setOnClickListener {
                        getImageContent.launch("image/*")
                    }
                    binding.edtName.setText(MyObject.notes.name)
                    binding.edtNotes.setText(MyObject.notes.notes)
                    Glide.with(this@MainActivity3).load(MyObject.notes.image)
                        .into(binding.imageAdd)
                    btnSave.setOnClickListener {

                        var document =
                            firebaseFirestore.collection("Notes")
                                .document(MyObject.notes.id)

                        val name = binding.edtName.text.toString()
                        var notes = binding.edtNotes.text.toString()
                        val current = LocalDateTime.now()
                        val formatter = DateTimeFormatter.ofPattern("MM-dd HH:mm")
                        val formatted = current.format(formatter)
                        if (name.isNotEmpty() && notes.isNotEmpty()) {
                            val map = HashMap<String, Any>()
                            map["name"] = name
                            map["notes"] = notes
                            map["dataTime"] = formatted
                            if (imagePath.isNotEmpty()) {
                                map["image"] = imagePath
                            }
                            document.update(map)
                                .addOnSuccessListener {
                                    Toast.makeText(this@MainActivity3, "Edit", Toast.LENGTH_SHORT)
                                        .show()
                                    rv.visibility = View.VISIBLE
                                    colorAdapter =
                                        ColorAdapter(list, object : ColorAdapter.ClickColor {
                                            override fun click(
                                                itemColorBinding: ItemColorBinding,
                                                position: Int
                                            ) {
                                                val document2 =
                                                    firebaseFirestore.collection("Notes")
                                                        .document(MyObject.notes.id)
                                                val map2 =
                                                    mapOf<String, Any>("color" to list[position])
                                                document2.update(map2)
                                                finish()
                                                startActivity(
                                                    Intent(
                                                        this@MainActivity3,
                                                        MainActivity2::class.java
                                                    )
                                                )
                                            }
                                        })
                                    rv.adapter = colorAdapter
                                }
                                .addOnFailureListener {
                                    Toast.makeText(this@MainActivity3, "Failed", Toast.LENGTH_SHORT)
                                        .show()
                                }
                        } else {
                            Toast.makeText(
                                this@MainActivity3,
                                "EditTexts or Image Blank",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }
    }

    private var getImageContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri ?: return@registerForActivityResult
            val hours = System.currentTimeMillis()

            val tesk = storageReference.child(hours.toString()).putFile(uri)

            tesk.addOnSuccessListener {


                val downloadUrl = it.metadata?.reference?.downloadUrl

                downloadUrl?.addOnSuccessListener { uri ->
                    imagePath = uri.toString()
                    Glide.with(this).load(uri.toString()).into(binding.imageAdd)
                }

            }.addOnFailureListener {
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            }
        }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
        startActivity(Intent(this,MainActivity2::class.java))
    }
}
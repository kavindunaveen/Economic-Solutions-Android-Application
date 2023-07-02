package com.example.crudmyapp

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.crudmyapp.MainAdapter.myViewHolder
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.FirebaseDatabase
import com.orhanobut.dialogplus.DialogPlus
import com.orhanobut.dialogplus.ViewHolder
import de.hdodenhof.circleimageview.CircleImageView


// Class declaration
class MainAdapter
/**
 * Initialize a [RecyclerView.Adapter] that listens to a Firebase query. See
 * [FirebaseRecyclerOptions] for configuration options.
 *
 * @param options
 */
    (options: FirebaseRecyclerOptions<MainModel?>) :
    FirebaseRecyclerAdapter<MainModel, myViewHolder>(options) {

    // Method to bind data to the view holder
    override fun onBindViewHolder(
        holder: myViewHolder,
        @SuppressLint("RecyclerView") position: Int,
        model: MainModel
    ) {
        holder.name.text = model.itemName
        holder.price.text = model.price
        holder.description.text = model.description

        // Set click listener for the btnEdit
        holder.btnEdit.setOnClickListener { view ->
            val dialogPlus = DialogPlus.newDialog(holder.img.context)
                .setContentHolder(ViewHolder(R.layout.update_popup))
                .setExpanded(true, 1200)
                .create()

            //dialogPlus.show();

            // Get references to the views.
            val headerView = dialogPlus.headerView
            val name = headerView.findViewById<EditText>(R.id.txtItemName)

            val price = headerView.findViewById<EditText>(R.id.txtPrice)
            val description = headerView.findViewById<EditText>(R.id.txtDescription)
            val btnUpdate = view.findViewById<Button>(R.id.btnUpdate)

            // Set the current values in the update popup
            name.setText(model.itemName)
            price.setText(model.price)
            description.setText(model.description)


            dialogPlus.show()

            // Set click listener for the btnUpdate
            btnUpdate.setOnClickListener {
                val map: MutableMap<String, Any> = HashMap()
                map["itemName"] = name.text.toString()
                map["price"] = price.text.toString()
                map["description"] = description.text.toString()

                // Update the data in the Firebase database.
                FirebaseDatabase.getInstance().reference.child("Items")
                    .child(getRef(position).key!!).updateChildren(map)
                    .addOnSuccessListener {
                        Toast.makeText(
                            holder.name.context,
                            "Data updated successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                        dialogPlus.dismiss()
                    }
                    .addOnFailureListener {
                        Toast.makeText(
                            holder.name.context,
                            "Error while updating",
                            Toast.LENGTH_SHORT
                        ).show()
                        dialogPlus.dismiss()
                    }
            }
        }

        // Initialize the btnDelete button after the view holder is fully inflated..
        holder.btnDelete.setOnClickListener {
            val builder = AlertDialog.Builder(holder.name.context)
            builder.setTitle("Are you sure?")
            builder.setMessage("Delete data cannot be undo.")
            builder.setPositiveButton("Delete") { dialogInterface, which ->
                FirebaseDatabase.getInstance().reference.child("Items")
                    .child(getRef(position).key!!).removeValue()
            }
            builder.setNegativeButton("Cancel") { dialog, which ->
                Toast.makeText(
                    holder.name.context,
                    "Cancelled",
                    Toast.LENGTH_SHORT
                ).show()
            }
            builder.show()
        }
        holder.btnEdit.visibility = View.VISIBLE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.main_item, parent, false)
        return myViewHolder(view)
    }


    inner class myViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var img: CircleImageView
        var name: TextView
        var price: TextView
        var description: TextView
        var btnEdit: Button
        var btnDelete: Button

        init {
            img = itemView.findViewById<View>(R.id.img1) as CircleImageView
            name = itemView.findViewById<View>(R.id.itemNametext) as TextView
            price = itemView.findViewById<View>(R.id.pricetext) as TextView
            description = itemView.findViewById<View>(R.id.descriptiontext) as TextView
            btnEdit = itemView.findViewById<View>(R.id.btnEdit) as Button
            btnDelete = itemView.findViewById<View>(R.id.btnDelete) as Button
        }
    }
}
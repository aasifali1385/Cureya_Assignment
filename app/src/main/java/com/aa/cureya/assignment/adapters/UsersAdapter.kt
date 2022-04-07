package com.aa.cureya.assignment.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.aa.cureya.assignment.R
import com.aa.cureya.assignment.UserActivity
import com.aa.cureya.assignment.models.UsersUpload



class UsersAdapter(private val uContext: Context, uploads: List<UsersUpload>) : RecyclerView.Adapter<UsersAdapter.ImageViewHolder>() {


    private val cUploads: List<UsersUpload>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val v = LayoutInflater.from(uContext).inflate(R.layout.abc_user, parent, false)
        val vholder: ImageViewHolder = ImageViewHolder(v)

        vholder.item.setOnClickListener {
            val intent = Intent(uContext, UserActivity::class.java)
            intent.putExtra("key", cUploads[vholder.adapterPosition].key)
            uContext.startActivity(intent)
        }
        return vholder
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: UsersAdapter.ImageViewHolder, position: Int) {
        val uploadCurrent: UsersUpload = cUploads[position]

        holder.txtName.setText(uploadCurrent.name)

    }

    override fun getItemCount(): Int {
        return cUploads.size
    }

    inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtName: TextView
        val item: CardView
        init {
            txtName = itemView.findViewById(R.id.nametxt)
            item = itemView.findViewById(R.id.item_user)
        }
    }

    init {
        cUploads = uploads
    }

}
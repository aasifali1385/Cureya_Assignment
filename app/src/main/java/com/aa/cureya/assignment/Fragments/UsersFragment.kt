package com.aa.cureya.assignment.Fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aa.cureya.assignment.R
import com.aa.cureya.assignment.adapters.UsersAdapter
import com.aa.cureya.assignment.models.UsersUpload
import com.google.firebase.database.*
import java.util.*


class UsersFragment : Fragment() {


    private var uAdapter: UsersAdapter? = null
    private var mProgressCircle: ProgressBar? = null
    private var cDataRef: DatabaseReference? = null
    private var cDBListener: ValueEventListener? = null
    private var uUploads: List<UsersUpload>? = null
    private var mRecyclerView: RecyclerView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_users, container, false)

        mProgressCircle = view.findViewById(R.id.progress)

        mRecyclerView = view.findViewById(R.id.recycler)
        mRecyclerView!!.setHasFixedSize(false)
        mRecyclerView!!.setLayoutManager(LinearLayoutManager(activity))

        uUploads = ArrayList<UsersUpload>()
        uAdapter = UsersAdapter(requireActivity(), uUploads as ArrayList<UsersUpload>)
        mRecyclerView!!.setAdapter(uAdapter)

        cDataRef = FirebaseDatabase.getInstance().getReference("AllUser")
        cDBListener = cDataRef!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                (uUploads as ArrayList<UsersUpload>).clear()

                for (postSnapshot in dataSnapshot.children) {
                    val upload: UsersUpload? = postSnapshot.getValue(UsersUpload::class.java)
                    upload!!.key = (postSnapshot.key)
                    (uUploads as ArrayList<UsersUpload>).add(upload)
                }

                Collections.reverse(uUploads)
                uAdapter!!.notifyDataSetChanged()
                mProgressCircle!!.setVisibility(View.INVISIBLE)
            }
            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(activity, databaseError.message, Toast.LENGTH_SHORT).show()
                mProgressCircle!!.setVisibility(View.INVISIBLE)
            }
        })


        return view
    }

}



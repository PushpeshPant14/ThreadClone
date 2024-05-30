package com.example.threadclone.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.threadclone.model.ThreadModel
import com.example.threadclone.model.UserModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.util.UUID
import kotlin.concurrent.thread

class HomeViewModel : ViewModel() {

    private val db = FirebaseDatabase.getInstance()
    private val thread = db.getReference("threads")


    private var _threadAndUsers = MutableLiveData<List<Pair<ThreadModel,UserModel>>>()
    val threadAndUsers: LiveData<List<Pair<ThreadModel,UserModel>>> = _threadAndUsers

    init {
        fetchThreadsAndUsersData {
            _threadAndUsers.value = it
        }
    }

    private fun fetchThreadsAndUsersData(onResult:(List<Pair<ThreadModel,UserModel>>) -> Unit){
        thread.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                val result = mutableListOf<Pair<ThreadModel,UserModel>>()
                for(threadSnapshot in snapshot.children){
                    val thread = threadSnapshot.getValue(ThreadModel::class.java)
                    thread.let {
                        fetchUserFromThread(it!!){
                            user ->
                            result.add(0,it to user)
                            if (result.size==snapshot.childrenCount.toInt()){
                                onResult(result)
                            }
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    fun fetchUserFromThread(thread:ThreadModel,onResult: (UserModel)->Unit){
        db.getReference("users").child(thread.userId)
            .addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {

                   val user = snapshot.getValue(UserModel::class.java)
                    user?.let(onResult)
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
    }



}
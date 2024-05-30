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
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.util.UUID
import kotlin.concurrent.thread

class UserViewModel : ViewModel() {

    private val db = FirebaseDatabase.getInstance()
    private val threadRef = db.getReference("threads")
    private val userRef = db.getReference("users")

    private val _threads = MutableLiveData(listOf<ThreadModel>())
    val threads : LiveData<List<ThreadModel>> get() = _threads

    private val _users = MutableLiveData(UserModel())
    val users : LiveData<UserModel> get() = _users

    private val _followerList = MutableLiveData(listOf<String>())
    val followerList : LiveData<List<String>> get() = _followerList

    private val _followingList = MutableLiveData(listOf<String>())
    val followingList : LiveData<List<String>> get() = _followingList


    fun fetchUser(uid:String){
        userRef.child(uid).addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(UserModel::class.java)
                _users.postValue(user)
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    fun fetchThread(uid:String){
        threadRef.orderByChild("userId").equalTo(uid).addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val threadList = snapshot.children.mapNotNull {
                    it.getValue(ThreadModel::class.java)
                }
                _threads.postValue(threadList)
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    val firestoredB = Firebase.firestore

    fun followUsers(userId:String,currentUserId:String){
        val ref = firestoredB.collection("following").document(currentUserId)
        val followerRef = firestoredB.collection("following").document(userId)

        ref.update("followingIds",FieldValue.arrayUnion(userId))
        followerRef.update("followerIds",FieldValue.arrayUnion(currentUserId))
    }

    fun getFollowers(userId:String){
        firestoredB.collection("followers").document(userId)
            .addSnapshotListener { value, _ ->
                val followerIds = value?.get("followerIds") as? List<String> ?: listOf()
                _followerList.postValue(followerIds)
            }
    }

    fun getFollowing(userId:String){
        firestoredB.collection("following").document(userId)
            .addSnapshotListener { value, _ ->
                val followerIds = value?.get("followingIds") as? List<String> ?: listOf()
                _followingList.postValue(followerIds)
            }
    }
}
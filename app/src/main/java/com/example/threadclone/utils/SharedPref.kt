package com.example.threadclone.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE


object SharedPref {

    fun storeData(
        name:String,
        email:String,
        bio:String,
        username:String,
        imageUrl:String,
        context: Context
    ){
        val sharedPreferences = context.getSharedPreferences("users", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("name",name)
        editor.putString("email",email)
        editor.putString("bio",bio)
        editor.putString("username",username)
        editor.putString("imageUrl",imageUrl)
        editor.apply()
    }

    fun getUserName(context:Context):String{
        val sharedPreferences = context.getSharedPreferences("users",MODE_PRIVATE)
        return sharedPreferences.getString("username","")!!
    }

    fun getName(context:Context):String{
        val sharedPreferences = context.getSharedPreferences("users",MODE_PRIVATE)
        return sharedPreferences.getString("name","")!!
    }

    fun getEmail(context:Context):String{
        val sharedPreferences = context.getSharedPreferences("users",MODE_PRIVATE)
        return sharedPreferences.getString("email","")!!
    }

    fun getBio(context:Context):String{
        val sharedPreferences = context.getSharedPreferences("users",MODE_PRIVATE)
        return sharedPreferences.getString("bio","")!!
    }

    fun getImageUrl(context:Context):String{
        val sharedPreferences = context.getSharedPreferences("users",MODE_PRIVATE)
        return sharedPreferences.getString("imageUrl","")!!
    }
}
package com.universe.myapplication.viewmodels

import android.app.Application
import android.text.Editable
import android.text.TextWatcher
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.room.withTransaction
import com.universe.myapplication.database.MyAppDatabase
import com.universe.myapplication.database.UserDao
import com.universe.myapplication.models.User
import kotlinx.coroutines.launch


  class UserViewModel(application: Application) : AndroidViewModel(application) {
    private val dataBase: MyAppDatabase
    private val userDao:UserDao

init {
    dataBase= MyAppDatabase.getDatabase(application)
    userDao=dataBase.userDao()
}
    val user: LiveData<User> = userDao.getUser()


    val firstName = MutableLiveData<String>()
    val lastName = MutableLiveData<String>()
    val phoneNumber = MutableLiveData<String>()
    val visibility = MutableLiveData<Boolean>(false)

    fun saveUser() {
        val firstNameValue = firstName.value
        val lastNameValue = lastName.value
        val phoneNumberValue = phoneNumber.value

        if (!firstNameValue.isNullOrEmpty() && !lastNameValue.isNullOrEmpty() && !phoneNumberValue.isNullOrEmpty()) {
            val user = User(firstName = firstNameValue, lastName = lastNameValue, phoneNumber = phoneNumberValue)
            viewModelScope.launch {
                dataBase.withTransaction {
                    userDao.deleteAll()
                    userDao.insertUser(user)
                }

            }
        }
    }
    var textWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            // This method is called before the text is changed
        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

        }

        override fun afterTextChanged(s: Editable) {
            // This method is called after the text has changed
            saveUser()
        }
    }


}
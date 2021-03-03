package com.example.tugasweek9

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tugasweek9.adapter.userAdapter
import com.example.tugasweek9.databinding.ActivityMainBinding
import com.example.tugasweek9.model.user
import io.realm.Realm
import io.realm.exceptions.RealmException
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var userAdapter: userAdapter
    lateinit var realm: Realm
    private lateinit var binding: ActivityMainBinding
    val lm = LinearLayoutManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        initView()
        saveData()
        updateData()
        deleteData()
        getAllData()
    }
    fun initView(){
        binding.recyclerView.layoutManager = lm
        userAdapter = userAdapter(mutableListOf())
        binding.recyclerView.adapter = userAdapter
        realm = Realm.getDefaultInstance()
        getAllData()
    }
    fun saveData(){
        btnSave.setOnClickListener {
            realm.beginTransaction()

            var count = 0
            realm.where(user::class.java).findAll().let {
                for (i in it){
                    count ++
                }
            }
            try {
                var user = realm.createObject(user::class.java)
                user.setId(count+1)
                user.setNama(etNama.text.toString())
                user.setEmail(etEmail.text.toString())

                etNama.setText("")
                etEmail.setText("")
                realm.commitTransaction()
                getAllData()
            }catch (e:RealmException){
                Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }
    fun getAllData(){
        realm.where(user::class.java).findAll().let {
            userAdapter.setUser(it)
        }
    }
    fun updateData(){
        binding.btnUpdate.setOnClickListener {
            realm.beginTransaction()
            realm.where(user::class.java).equalTo("id", binding.etId.text.toString().toInt()).findFirst().let {
                it!!.setNama(binding.etNama.text.toString())
                it!!.setEmail(binding.etEmail.text.toString())
            }
            realm.commitTransaction()
            getAllData()
        }
    }
    fun deleteData(){
        binding.btnDelete.setOnClickListener {
            realm.beginTransaction()
            realm.where(user::class.java).equalTo("id", binding.etId.text.toString().toInt()).findFirst().let {
                it!!.deleteFromRealm()
            }
            realm.commitTransaction()
            getAllData()
        }
    }
}
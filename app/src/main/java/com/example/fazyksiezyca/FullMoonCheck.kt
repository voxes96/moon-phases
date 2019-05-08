package com.example.fazyksiezyca

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import kotlinx.android.synthetic.main.activity_full_moon_check.*
import java.util.*
import kotlin.collections.ArrayList

class FullMoonCheck : AppCompatActivity() {

    var algorithmNr = 3

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_full_moon_check)

        val extras = intent.extras ?: return
        algorithmNr = extras.getInt("Algorithm")

        val textChangeYear: EditText = findViewById(R.id.textChangeYear)
        //textChangeYear.setText((Calendar.getInstance().get(Calendar.YEAR)).toString())

        textChangeYear.addTextChangedListener(object: TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {


            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                //if (Integer.parseInt(textChangeYear.text.toString()) < 1900) textChangeYear.setText((1900).toInt())
                //if (Integer.parseInt(textChangeYear.text.toString()) > 2200) textChangeYear.setText((2200).toInt())

                //checkFullMoon(s.toString().toInt())
                if (s.toString().toIntOrNull() != null) {
                    if (s.toString().toInt() in 1900..2200) checkFullMoon(s.toString().toInt())
                    else {
                        //textAllMoons.text = ("Data musi być z przedziału 1900-2200")
                        val lista:ListView = findViewById(R.id.lista)
                        val listItems = arrayOfNulls<String>(1)
                        listItems[0] = ("Data musi być z przedziału 1900-2200")
                        val adapter = ArrayAdapter(this@FullMoonCheck, android.R.layout.simple_list_item_1, listItems)
                        lista.adapter = adapter
                    }
                } else {
                    //textAllMoons.text = ("Musisz wpisać jakąś datę")
                    val lista:ListView = findViewById(R.id.lista)
                    val listItems = arrayOfNulls<String>(1)
                    listItems[0] = ("Musisz wpisać jakąś datę")
                    val adapter = ArrayAdapter(this@FullMoonCheck, android.R.layout.simple_list_item_1, listItems)
                    lista.adapter = adapter
                }
            }
        })

        val fabAdd:View = findViewById(R.id.fabAdd)
        fabAdd.setOnClickListener{
            if(textChangeYear.text.toString() != "") {
                textChangeYear.setText((textChangeYear.text.toString().toInt()+1).toString())
            } else {
                textChangeYear.setText(("1").toString())
            }
        }

        val fabSub:View = findViewById(R.id.fabSub)
        fabSub.setOnClickListener {
            if(textChangeYear.text.toString() != "") {
                if ((textChangeYear.text.toString().toInt() - 1) < 0) {
                    textChangeYear.setText(("0").toString())
                } else {
                    textChangeYear.setText((textChangeYear.text.toString().toInt() - 1).toString())
                }
            } else {
                textChangeYear.setText(("1").toString())
            }
        }

    }


    fun checkFullMoon(year: Int) {
        //val textAllMoons: TextView = findViewById(R.id.textAllMoons)
        //textAllMoons.text = PhaseAlgorithms().findAllMoon(year, algorithmNr)

        val tablica: ArrayList<String> = PhaseAlgorithms().findAllMoon2(year, algorithmNr)
        val lista:ListView = findViewById(R.id.lista)
        val listItems = arrayOfNulls<String>(tablica.size)
        for (i in 0 until tablica.size) {
            val recipe = tablica[i]
            listItems[i] = recipe
        }
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, listItems)
        lista.adapter = adapter
    }

    fun changeToAlgorithmSelection(view: View) {
        val changePage = Intent(this, AlgorithmSelect::class.java)
        changePage.putExtra("Algorithm", algorithmNr)
        startActivity(changePage)
    }

}

package com.example.fazyksiezyca

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_algorithm_select.*

class AlgorithmSelect : AppCompatActivity() {

    var algorithmNr = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_algorithm_select)

        val extras = intent.extras ?: return
        algorithmNr = extras.getInt("Algorithm")

        val textDescription : TextView = findViewById(R.id.textDescription)
        when(algorithmNr) {
            1 -> {textDescription.text = ("Obecny algorytm: Simple").toString(); val radio:RadioButton = findViewById(R.id.radioSimple); radioGroupAlgorithms.check(radio.id)}
            2 -> {textDescription.text = ("Obecny algorytm: Conway").toString(); val radio:RadioButton = findViewById(R.id.radioConway); radioGroupAlgorithms.check(radio.id)}
            3 -> {textDescription.text = ("Obecny algorytm: Trigonometric 1").toString(); val radio:RadioButton = findViewById(R.id.radioTrig1); radioGroupAlgorithms.check(radio.id)}
            4 -> {textDescription.text = ("Obecny algorytm: Trigonometric 2").toString(); val radio:RadioButton = findViewById(R.id.radioTrig2); radioGroupAlgorithms.check(radio.id)}
            else -> textDescription.text = ("Obecny algorytm: błąd danych").toString()
        }

        /*radioGroupAlgorithms.setOnCheckedChangeListener(
            RadioGroup.OnCheckedChangeListener{ group, checkedId ->
                val radio: RadioButton = findViewById(checkedId)
                Toast.makeText(applicationContext,"On checked change: ${radio.text}", Toast.LENGTH_SHORT).show()
            }
        )*/

        buttonAlgSelect.setOnClickListener{
            //var id: Int = radioGroupAlgorithms.checkedRadioButtonId
            val radio:RadioButton = findViewById(radioGroupAlgorithms.checkedRadioButtonId)
            if(radio.text == "Simple") algorithmNr = 1
            if(radio.text == "Conway") algorithmNr = 2
            if(radio.text == "Trigonometric 1") algorithmNr = 3
            if(radio.text == "Trigonometric 2") algorithmNr = 4

            //Toast.makeText(applicationContext, "On button click: ${algorithmNr}", Toast.LENGTH_SHORT).show()

            val changePage = Intent(this, MainActivity::class.java)
            changePage.putExtra("Algorithm", algorithmNr)
            startActivity(changePage)
        }
    }

    fun algorithmSelectedButton() {
        //val radio: RadioButton = findViewById(radioGroupAlgorithms.checkedRadioButtonId)
        //Toast.makeText(applicationContext, "On click: ${radio.text}", Toast.LENGTH_SHORT).show()

        //val changePage = Intent(this, MainActivity::class.java)
        //changePage.putExtra("Algorithm", algorithmNr)
    }
}

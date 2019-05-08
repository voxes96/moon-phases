package com.example.fazyksiezyca

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import java.time.LocalDate
import java.util.*
import kotlin.math.round

class MainActivity : AppCompatActivity() {

    private var algorithmNr = 3

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //val buttonPelnie:Button = findViewById(R.id.buttonPelnie)
        //buttonPelnie.setOnClickListener()

        val extras = intent.extras ?: return
        algorithmNr = extras.getInt("Algorithm")

    }

    override fun onResume() {
        super.onResume()

        val today = Calendar.getInstance()
        val day = today.get(Calendar.DAY_OF_MONTH)
        val month = today.get(Calendar.MONTH)+1
        val year = today.get(Calendar.YEAR)

        //println(day.toString()+" "+month.toString()+" "+year.toString())
        //println("rok: $year miesiac: $month dzien: $day")
        var phase = 0
        if(algorithmNr == 1) {
            phase = PhaseAlgorithms().simple(year, month, day)
        } else if(algorithmNr == 2) {
            phase = PhaseAlgorithms().conway(year, month, day)
        } else if(algorithmNr == 4) {
            phase = PhaseAlgorithms().trig2(year, month, day)
        } else {
            phase = PhaseAlgorithms().trig1(year, month, day)
        }

        if(phase > 15) phase = 30 - phase
        phase = round(phase*100/15.0).toInt()
        val textDzisiaj: TextView = findViewById(R.id.textDzisiaj)
        textDzisiaj.text = ("Dzisiaj: $phase%").toString()

        val moonImage : ImageView = findViewById(R.id.moonImage)
        if(phase in 0..14) {
            moonImage.setImageResource(R.drawable.none_moon)
        } else if(phase in 15..40) {
            moonImage.setImageResource(R.drawable.one_moon)
        } else if(phase in 41..60) {
            moonImage.setImageResource(R.drawable.half_moon)
        } else if(phase in 61..85) {
            moonImage.setImageResource(R.drawable.three_moon)
        } else {
            moonImage.setImageResource(R.drawable.full_moon)
        }

        val prev = PhaseAlgorithms().findPrevNew(algorithmNr)
        val textPopNow: TextView = findViewById(R.id.textPopNow)
        textPopNow.text = ("Poprzedni nów: $prev r.").toString()

        val next = PhaseAlgorithms().findNextFull(algorithmNr)
        val textNastPel: TextView = findViewById(R.id.textNastPel)
        textNastPel.text = ("Następna pełnia: $next r.").toString()

    }

    fun changeToFullMoonScene(view: View) {
        val changePage = Intent(this, FullMoonCheck::class.java)
        changePage.putExtra("Algorithm", algorithmNr)
        startActivity(changePage)
        //println("wcisnieto")
    }

    fun changeToAlgorithmSelection(view: View) {
        val changePage = Intent(this, AlgorithmSelect::class.java)
        changePage.putExtra("Algorithm", algorithmNr)
        startActivity(changePage)
    }
}

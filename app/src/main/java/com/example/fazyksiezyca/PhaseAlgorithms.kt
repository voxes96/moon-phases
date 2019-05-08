package com.example.fazyksiezyca

import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class PhaseAlgorithms {

    //private var algorithmNr = 3
    //private val year = year
    //private val month = month
    //private val day = day

    fun simple(year:Int, month:Int, day:Int) : Int {
        val lp = 2551443
        val now = Date(year,month-1,day,20,35,0)
        //val now = Calendar.getInstance()
        //now.set(year, month-1, day, 20, 35, 0)
        val newMoon = Date(1970, 0, 7, 20, 35, 0)
        //val new_moon = Calendar.getInstance()
        //new_moon.set(1970, 0, 7, 20, 35, 0)
        val phase = ((now.getTime() - newMoon.getTime())/1000.0) % lp
        //val phase = ((now.get(Calendar.DATE) - new_moon.get(Calendar.DATE))/1000.0) % lp
        if((Math.floor((phase /(24*3600))) + 1).toInt() == 30) return 0
        else return (Math.floor((phase /(24*3600))) + 1).toInt()
    }

    fun conway(year:Int, month:Int, day:Int) : Int {
        var r:Double = (year % 100).toDouble()
        r %= 19
        if (r>9){ r -= 19;}
        r = ((r * 11) % 30) + month + day
        if (month<3){r += 2;}
        //r -= ((year<2000) ? 4 : 8.3)
        if(year < 2000) r -= 4
        else r -= 8.3
        r = Math.floor(r+0.5)%30
        //return (r < 0) ? r+30 : r
        if(r < 0) return (r+30).toInt()
        else return r.toInt()
    }

    fun trig1(year:Int, month:Int, day:Int) : Int {
        val thisJD = julday(year,month,day)
        val degToRad = 3.14159265 / 180
        val K0 = Math.floor((year-1900)*12.3685)
        val T = (year-1899.5) / 100
        val T2 = T*T
        val T3 = T*T*T
        val J0 = 2415020 + 29*K0
        val F0 = 0.0001178*T2 - 0.000000155*T3 + (0.75933 + 0.53058868*K0) - (0.000837*T + 0.000335*T2)
        val M0 = 360*(getFrac(K0*0.08084821133)) + 359.2242 - 0.0000333*T2 - 0.00000347*T3
        val M1 = 360*(getFrac(K0*0.07171366128)) + 306.0253 + 0.0107306*T2 + 0.00001236*T3
        val B1 = 360*(getFrac(K0*0.08519585128)) + 21.2964 - (0.0016528*T2) - (0.00000239*T3)
        var phase = 0
        var jday = 0
        var oldJ = 0
        while (jday < thisJD) {
            var F = F0 + 1.530588*phase
            val M5 = (M0 + phase*29.10535608)*degToRad
            val M6 = (M1 + phase*385.81691806)*degToRad
            val B6 = (B1 + phase*390.67050646)*degToRad
            F -= 0.4068*Math.sin(M6) + (0.1734 - 0.000393*T)*Math.sin(M5)
            F += 0.0161*Math.sin(2*M6) + 0.0104*Math.sin(2*B6)
            F -= 0.0074*Math.sin(M5 - M6) - 0.0051*Math.sin(M5 + M6)
            F += 0.0021*Math.sin(2*M5) + 0.0010*Math.sin(2*B6-M6)
            F += 0.5 / 1440
            oldJ = jday
            jday = (J0 + 28*phase + Math.floor(F)).toInt()
            phase++
        }
        return ((thisJD-oldJ)%30).toInt()
    }

    fun getFrac(fr:Double) : Double {
        return (fr - Math.floor(fr))
    }

    fun trig2(year:Int, month:Int, day:Int) : Int {
        val n = Math.floor(12.37 * (year -1900 + ((1.0 * month - 0.5)/12.0)))
        val RAD = 3.14159265/180.0
        val t = n / 1236.85
        val t2 = t * t
        val as1 = 359.2242 + 29.105356 * n
        val am = 306.0253 + 385.816918 * n + 0.010730 * t2
        var xtra = 0.75933 + 1.53058868 * n + ((1.178e-4) - (1.55e-7) * t) * t2
        xtra += (0.1734 - 3.93e-4 * t) * Math.sin(RAD * as1) - 0.4068 * Math.sin(RAD * am)
        //var i = (xtra > 0.0 ? Math.floor(xtra) :  Math.ceil(xtra - 1.0))
        var i = 0
        if(xtra > 0.0) i = Math.floor(xtra).toInt()
        else i = Math.ceil(xtra - 1).toInt()
        val j1 = julday(year,month,day)
        val jd = (2415020 + 28 * n) + i
        return ((j1-jd + 30)%30).toInt()
    }

    fun julday(year:Int, month:Int, day:Int) : Double {
        var year = year
        if (year < 0) year ++
        var jy = year
        var jm = month + 1
        if (month <= 2) {jy--;	jm += 12	}
        var jul = Math.floor(365.25 *jy) + Math.floor(30.6001 * jm) + day + 1720995
        if (day+31*(month+12*year) >= (15+31*(10+12*1582))) {
            val ja = Math.floor(0.01 * jy);
            jul = jul + 2 - ja + Math.floor(0.25 * ja);
        }
        return jul;
    }

    fun findNextFull(alg:Int): String {
        val today = Calendar.getInstance()
        var day = today.get(Calendar.DAY_OF_MONTH)
        var month = today.get(Calendar.MONTH)+1
        var year = today.get(Calendar.YEAR)

        var phase = 0
        if(alg == 1) {
            phase = PhaseAlgorithms().simple(year, month, day)
        } else if(alg == 2) {
            phase = PhaseAlgorithms().conway(year, month, day)
        } else if(alg == 4) {
            phase = PhaseAlgorithms().trig2(year, month, day)
        } else {
            phase = PhaseAlgorithms().trig1(year, month, day)
        }

        while (phase != 15) {
            today.add(Calendar.DAY_OF_MONTH, 1)
            day = today.get(Calendar.DAY_OF_MONTH)
            month = today.get(Calendar.MONTH)+1
            year = today.get(Calendar.YEAR)

            if(alg == 1) {
                phase = PhaseAlgorithms().simple(year, month, day)
            } else if(alg == 2) {
                phase = PhaseAlgorithms().conway(year, month, day)
            } else if(alg == 4) {
                phase = PhaseAlgorithms().trig2(year, month, day)
            } else {
                phase = PhaseAlgorithms().trig1(year, month, day)
            }
        }

        day = today.get(Calendar.DAY_OF_MONTH)
        month = today.get(Calendar.MONTH)+1
        year = today.get(Calendar.YEAR)

        //return SimpleDateFormat("dd-MM-yyyy").parse(today.time.toString())
        if (month < 10) return "$day.0$month.$year"
        else return "$day.$month.$year"
    }

    fun findPrevNew(alg:Int) : String {
        val today = Calendar.getInstance()
        var day = today.get(Calendar.DAY_OF_MONTH)
        var month = today.get(Calendar.MONTH)+1
        var year = today.get(Calendar.YEAR)

        var phase = 1
        if(alg == 1) {
            phase = PhaseAlgorithms().simple(year, month, day)
        } else if(alg == 2) {
            phase = PhaseAlgorithms().conway(year, month, day)
        } else if(alg == 4) {
            phase = PhaseAlgorithms().trig2(year, month, day)
        } else {
            phase = PhaseAlgorithms().trig1(year, month, day)
        }

        while (phase != 0) {
            today.add(Calendar.DAY_OF_MONTH, -1)
            day = today.get(Calendar.DAY_OF_MONTH)
            month = today.get(Calendar.MONTH)+1
            year = today.get(Calendar.YEAR)

            if(alg == 1) {
                phase = PhaseAlgorithms().simple(year, month, day)
            } else if(alg == 2) {
                phase = PhaseAlgorithms().conway(year, month, day)
            } else if(alg == 4) {
                phase = PhaseAlgorithms().trig2(year, month, day)
            } else {
                phase = PhaseAlgorithms().trig1(year, month, day)
            }
        }

        day = today.get(Calendar.DAY_OF_MONTH)
        month = today.get(Calendar.MONTH)+1
        year = today.get(Calendar.YEAR)

        //return SimpleDateFormat("dd-MM-yyyy").parse(today.time.toString())
        if (month < 10) return "$day.0$month.$year"
        else return "$day.$month.$year"
    }

    fun findAllMoon(year: Int, alg: Int) : String {
        var nYear = year
        var nMonth = 0
        var nDay = 1
        var phase = 0
        var allMoon = ""

        val date = Calendar.getInstance()
        date.set(nYear, nMonth, nDay)
        while (year == nYear) {
            if(alg == 1) {
                phase = PhaseAlgorithms().simple(nYear, nMonth, nDay)
            } else if(alg == 2) {
                phase = PhaseAlgorithms().conway(nYear, nMonth, nDay)
            } else if(alg == 4) {
                phase = PhaseAlgorithms().trig2(nYear, nMonth, nDay)
            } else {
                phase = PhaseAlgorithms().trig1(nYear, nMonth, nDay)
            }

            if (phase == 15) {
                if (nMonth < 10) allMoon += "$nDay.0$nMonth.$nYear r.\n"
                else allMoon += "$nDay.$nMonth.$nYear r.\n"
            }

            date.add(Calendar.DAY_OF_MONTH, +1)
            nDay = date.get(Calendar.DAY_OF_MONTH)
            nMonth = date.get(Calendar.MONTH)+1
            nYear = date.get(Calendar.YEAR)
        }

        return allMoon
    }

    fun findAllMoon2(year: Int, alg: Int) : ArrayList<String> {
        var nYear = year
        var nMonth = 0
        var nDay = 1
        var phase = 0
        var allMoon = ""
        var lista = ArrayList<String>()

        val date = Calendar.getInstance()
        date.set(nYear, nMonth, nDay)
        while (year == nYear) {
            if(alg == 1) {
                phase = PhaseAlgorithms().simple(nYear, nMonth, nDay)
            } else if(alg == 2) {
                phase = PhaseAlgorithms().conway(nYear, nMonth, nDay)
            } else if(alg == 4) {
                phase = PhaseAlgorithms().trig2(nYear, nMonth, nDay)
            } else {
                phase = PhaseAlgorithms().trig1(nYear, nMonth, nDay)
            }

            if (phase == 15) {
                if (nMonth < 10) lista.add("$nDay.0$nMonth.$nYear r.")
                else lista.add("$nDay.$nMonth.$nYear r.")
                //lista.add("$nDay.$nMonth.$nYear r.\n")
            }

            date.add(Calendar.DAY_OF_MONTH, +1)
            nDay = date.get(Calendar.DAY_OF_MONTH)
            nMonth = date.get(Calendar.MONTH)+1
            nYear = date.get(Calendar.YEAR)
        }

        return lista
    }
}
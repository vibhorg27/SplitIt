package com.example.tippy

import android.animation.ArgbEvaluator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import androidx.core.content.ContextCompat


private const val TAG = "MainActivity"
private const val  INITIAL_TIP_PERCENT = 15
private const val INITIAL_N_O_P = 2

class MainActivity : AppCompatActivity() {

    private lateinit var etBaseAmount: EditText
    private lateinit var seekBarTip: SeekBar
    private lateinit var tvTipPercentLabel: TextView
    private lateinit var tvTipAmount: TextView
    private lateinit var tvTotalAmount: TextView
    private lateinit var tvTipDescription: TextView

    // Addition to the Lecture
    private lateinit var seekBarNop: SeekBar
    private lateinit var tvNumberOfPeople: TextView
    private lateinit var tvTipSplitAmount: TextView
    private lateinit var tvTotalSplitAmount: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        etBaseAmount = findViewById(R.id.etBaseAmount)
        seekBarTip= findViewById(R.id.seekBarTip)
        tvTipPercentLabel= findViewById(R.id.tvTipPercentLabel)
        tvTipAmount= findViewById(R.id.tvTipAmount)
        tvTotalAmount= findViewById(R.id.tvTotal)
        tvTipDescription= findViewById(R.id.tvTipDescription)

        // Addition to the Lecture
        seekBarNop = findViewById(R.id.seekBarNop)
        tvNumberOfPeople = findViewById(R.id.tvNumberOfPeople)
        tvTipSplitAmount = findViewById(R.id.tvTipSplitAmount)
        tvTotalSplitAmount = findViewById(R.id.tvTotalSplitAmount)


        seekBarTip.progress = INITIAL_TIP_PERCENT
        tvTipPercentLabel.text = "$INITIAL_TIP_PERCENT %"
        seekBarNop.progress = INITIAL_N_O_P
        tvNumberOfPeople.text = "$INITIAL_N_O_P"
        updateTipDescription(INITIAL_TIP_PERCENT)

        seekBarTip.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                Log.i(TAG, "onProgressChanged $progress")

                tvTipPercentLabel.text = "$progress%"
                computeTipAndTotal()
                updateTipDescription(progress)
                computeSplit()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}

        })

        etBaseAmount.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val i = Log.i(TAG, "afterTextChanged $s")
                computeTipAndTotal()
                computeSplit()
            }

        })

        seekBarNop.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                Log.i(TAG, "onNOPChanged $progress")
                tvNumberOfPeople.text = "$progress"
                computeSplit()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}

        })
    }

    private fun updateTipDescription(tipPercent: Int) {
        val tipDescription = when(tipPercent){
            in 0..9 -> "Poor"
            in 10..14 -> "Acceptable"
            in 15..19 -> "Good"
            in 20..24 -> "Great"
            else -> "Amazing"
        }
        tvTipDescription.text = tipDescription
//        // Update Color based on the tip percentage
//        val color = ArgbEvaluator().evaluate(
//            tipPercent.toFloat() / seekBarTip.max
//
//            ContextCompat.getColor(this, R.color.color_worst_tip)
//            ContextCompat.getColor(this, R.color.color_best_tip)
//
//        )
    }



    private fun computeTipAndTotal() {
        if(etBaseAmount.text.isEmpty()){
            tvTipAmount.text= ""
            tvTotalAmount.text=""
            return
        }
        // 1. Get the value of the base and tip
        val baseAmount = etBaseAmount.text.toString().toDouble()
        val tipPercent = seekBarTip.progress
        // 2. Compute the tip and total
        val tipAmount = baseAmount * tipPercent/100
        val totalAmount= baseAmount + tipAmount
        // 3. Update the UI
        tvTipAmount.text = "%.2f".format(tipAmount)
        tvTotalAmount.text = "%.2f".format(totalAmount)
    }


    private fun computeSplit(){
        if(etBaseAmount.text.isEmpty()){
            tvTipSplitAmount.text= ""
            tvTotalSplitAmount.text= ""
            return
        }
        // 1. Get the value of Tip Amount and Total Amount
        val tipAmount = tvTipAmount.text.toString().toDouble()
        val totalAmount = tvTotalAmount.text.toString().toDouble()
        // 2. Compute Tip Split and Total Split
        val noOfPerson = seekBarNop.progress
        val tipSplit = tipAmount/noOfPerson
        val totalSplit = totalAmount/noOfPerson
        // 3. Update the UI
        tvTipSplitAmount.text= "%.2f".format(tipSplit)
        tvTotalSplitAmount.text= "%.2f".format(totalSplit)
    }

}
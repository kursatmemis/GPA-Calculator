package com.kursatmemis.gpacalculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import com.shashank.sony.fancytoastlib.FancyToast


class MainActivity : AppCompatActivity() {

    private lateinit var addLessonButton: Button
    private lateinit var calculateGPAButton: Button
    private lateinit var lessonNameAutoCompleteTextView: AutoCompleteTextView
    private lateinit var placeHolderLinear: LinearLayout
    private lateinit var inflater: LayoutInflater
    private lateinit var addedLessonLayout: ConstraintLayout
    private lateinit var lessonCreditSpinner: Spinner
    private lateinit var lessonNoteSpinner: Spinner
    private var lessonName: String = ""
    private var gradeScale: Map<String, Double> = generateGradeScale()
    private var lessonCredit: Int = 0
    private var totalCredit: Int = 0
    private var lessonCharNote: String = ""
    private var lessonNumberNote: Double = 0.0
    private var totalNoteTimesCredit: Double = 0.0
    private var gpa: Double = 0.0
    private lateinit var buttonParent: ConstraintLayout
    private lateinit var autoCompleteTextView: AutoCompleteTextView
    private var lessons =
        mutableListOf<String>("Matematik", "Türkçe", "Fizik", "Edebiyat", "Algoritma", "Tarih")
    private lateinit var adapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        getView()

        adapter = ArrayAdapter<String>(
            this, R.layout.lessons_layout, lessons
        )

        autoCompleteTextView.setAdapter(adapter)
        autoCompleteTextView.threshold = 1

        calculateGPAButton.visibility = View.INVISIBLE

        addLessonButton.setOnClickListener {
            addedLessonLayout =
                inflater.inflate(R.layout.added_lesson_layout, null) as ConstraintLayout

            lessonName = findViewById<TextView>(R.id.lessonNameAutoCompleteTextView).text.toString()
            lessonCredit = lessonCreditSpinner.selectedItem.toString()[0].toString().toInt()
            lessonCharNote = lessonNoteSpinner.selectedItem.toString()

            if (!lessons.contains(lessonName)) {
                lessons.add(lessonName)
                adapter.add(lessonName)
                adapter.notifyDataSetChanged()
                Log.e("msgxxx", "girdi")
            }

            addedLessonLayout.findViewById<TextView>(R.id.lessonCreditSpinnerAddedView).text =
                "Kredi: ${lessonCredit}"

            addedLessonLayout.findViewById<TextView>(R.id.lessonNoteSpinnerAddedView).text =
                lessonCharNote

            addedLessonLayout.findViewById<TextView>(R.id.lessonNameTextViewAddedView).text =
                lessonName

            addedLessonLayout.findViewById<Button>(R.id.removeLessonButton).setOnClickListener {
                Log.e("mkmmkm", "tiklandi")
                buttonParent = it.parent as ConstraintLayout
                val text = buttonParent.findViewById<TextView>(R.id.lessonCreditSpinnerAddedView)
                    .text.toString()
                lessonCredit = try {
                    text.substring(text.length - 2, text.length - 1).toInt()

                } catch (e: NumberFormatException) {
                    text[text.length - 1].toString().toInt()
                }
                Log.e("mkmmkm", "Kredi: $lessonCredit")
                totalCredit -= lessonCredit

                lessonCharNote =
                    buttonParent.findViewById<TextView>(R.id.lessonNoteSpinnerAddedView)
                        .text.toString()
                lessonNumberNote = gradeScale[lessonCharNote]!!

                Log.e("mkmmkm", "NumberNote: $lessonNumberNote")
                totalNoteTimesCredit -= (lessonNumberNote * lessonCredit)
                placeHolderLinear.removeView(buttonParent)
                Log.e("mkmmkm", "Total Credit: $totalCredit Carpim: $totalNoteTimesCredit")

                if (placeHolderLinear.childCount == 0) {
                    calculateGPAButton.visibility = View.INVISIBLE
                }
            }

            lessonCredit = lessonCreditSpinner.selectedItem.toString()[0].toString().toInt()
            totalCredit += lessonCredit
            lessonCharNote = lessonNoteSpinner.selectedItem.toString()
            lessonNumberNote = gradeScale[lessonCharNote]!!
            totalNoteTimesCredit += (lessonNumberNote * lessonCredit)

            placeHolderLinear.addView(addedLessonLayout)

            if (placeHolderLinear.childCount > 0) {
                calculateGPAButton.visibility = View.VISIBLE
            }

            lessonNameAutoCompleteTextView.setText("")

        }

        calculateGPAButton.setOnClickListener {
            gpa = calculateGPA()
            FancyToast.makeText(
                this,
                "GPA: ${gpa}",
                FancyToast.LENGTH_LONG,
                FancyToast.WARNING,
                true
            ).show();

        }
    }

    private fun calculateGPA(): Double {
        return totalNoteTimesCredit / totalCredit.toDouble()
    }

    private fun generateGradeScale(): Map<String, Double> {
        var gradeScale = mutableMapOf<String, Double>()
        gradeScale["AA"] = 4.00
        gradeScale["BA"] = 3.50
        gradeScale["BB"] = 3.00
        gradeScale["CB"] = 2.50
        gradeScale["CC"] = 2.00
        gradeScale["DC"] = 1.50
        gradeScale["DD"] = 1.00
        gradeScale["FF"] = 0.00
        return gradeScale
    }

    private fun getView() {
        addLessonButton = findViewById(R.id.addLessonButton)
        lessonNameAutoCompleteTextView = findViewById(R.id.lessonNameAutoCompleteTextView)
        placeHolderLinear = findViewById(R.id.placeHolderLinearLayout)
        lessonCreditSpinner = findViewById(R.id.lessonCreditSpinner)
        lessonNoteSpinner = findViewById(R.id.lessonNoteSpinner)
        inflater = LayoutInflater.from(applicationContext)
        calculateGPAButton = findViewById(R.id.calculateGPA)
        autoCompleteTextView = findViewById(R.id.lessonNameAutoCompleteTextView)
    }
}

package com.example.lab_week_10

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.lab_week_10.database.Total
import com.example.lab_week_10.database.TotalDatabase
import com.example.lab_week_10.database.TotalObject
import com.example.lab_week_10.viewmodels.TotalViewModel
import java.util.Date

class MainActivity : AppCompatActivity() {

    private val db by lazy { prepareDatabase() }

    private val viewModel by lazy {
        ViewModelProvider(this)[TotalViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeValueFromDatabase()
        updateText(viewModel.total.value ?: 0)

        viewModel.total.observe(this) { t ->
            updateText(t)
        }

        findViewById<Button>(R.id.button_increment).setOnClickListener {
            viewModel.incrementTotal()
        }
    }

    // Tampilkan toast tanggal update terakhir
    override fun onStart() {
        super.onStart()
        val rows = db.totalDao().getTotal(ID)
        if (rows.isNotEmpty()) {
            val date = rows.first().total.date
            Toast.makeText(this, "Last updated: $date", Toast.LENGTH_SHORT).show()
        }
    }

    // Simpan total + tanggal saat Activity pause
    override fun onPause() {
        super.onPause()
        val current = viewModel.total.value ?: 0
        val now = Date().toString()
        db.totalDao().update(Total(ID, TotalObject(current, now)))
    }

    private fun updateText(total: Int) {
        findViewById<TextView>(R.id.text_total).text =
            getString(R.string.text_total, total)
    }

    private fun prepareDatabase(): TotalDatabase {
        return Room.databaseBuilder(
            applicationContext,
            TotalDatabase::class.java,
            "total-database"
        ).allowMainThreadQueries().build()
    }

    private fun initializeValueFromDatabase() {
        val rows = db.totalDao().getTotal(ID)
        if (rows.isEmpty()) {
            val now = Date().toString()
            db.totalDao().insert(Total(id = ID, total = TotalObject(0, now)))
            viewModel.setTotal(0)
        } else {
            viewModel.setTotal(rows.first().total.value)
        }
    }

    companion object {
        const val ID: Long = 1
    }
}

package com.example.lab_week_10

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.lab_week_10.database.Total
import com.example.lab_week_10.database.TotalDatabase
import com.example.lab_week_10.viewmodels.TotalViewModel

class MainActivity : AppCompatActivity() {

    // DB dibuat saat pertama kali dipakai
    private val db by lazy { prepareDatabase() }

    // ViewModel dari commit 2 (LiveData)
    private val viewModel by lazy {
        ViewModelProvider(this)[TotalViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 1) Ambil nilai awal dari DB (insert 0 jika kosong)
        initializeValueFromDatabase()

        // 2) Tampilkan nilai awal (hindari flicker)
        updateText(viewModel.total.value ?: 0)

        // 3) Observe LiveData → sinkron UI
        viewModel.total.observe(this) { t ->
            updateText(t)
        }

        // 4) Increment dan biarkan ViewModel mengelola nilainya
        findViewById<Button>(R.id.button_increment).setOnClickListener {
            viewModel.incrementTotal()
        }
    }

    override fun onPause() {
        super.onPause()
        val current = viewModel.total.value ?: 0
        db.totalDao().update(Total(ID, current))
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
        )
            // Untuk simplicity demo modul (tidak direkomendasikan di produksi)
            .allowMainThreadQueries()
            .build()
    }

    private fun initializeValueFromDatabase() {
        val rows = db.totalDao().getTotal(ID)
        if (rows.isEmpty()) {
            // pertama kali → insert 0 dengan id tetap = 1
            db.totalDao().insert(Total(id = ID, total = 0))
            viewModel.setTotal(0)
        } else {
            // sudah ada → pakai nilai yang tersimpan
            viewModel.setTotal(rows.first().total)
        }
    }

    companion object {
        const val ID: Long = 1
    }
}

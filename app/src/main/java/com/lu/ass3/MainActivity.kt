package com.lu.ass3

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val etMessage: EditText = findViewById(R.id.textET)
        val btnSend: Button = findViewById(R.id.sendBtn)
        val context = this

        btnSend.setOnClickListener {
            val message = etMessage.text.toString()
            if (message.isBlank()) {
                Toast.makeText(context, "Enter a message", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val intent = Intent(this, MessageActivity::class.java)
            intent.putExtra(MSG_KEY, message)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.sample_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val selectedId = item.itemId
        when (selectedId) {
            R.id.menu1 -> {
                Toast.makeText(this, "Menu 1", Toast.LENGTH_SHORT).show()
                AlertDialog.Builder(this)
                    .setTitle("Custom Popup Dialog")
                    .setMessage("Menu 1 clicked")
                    .setNeutralButton("OK") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
            }

            R.id.menu2 -> {
                Toast.makeText(this, "Menu 2", Toast.LENGTH_SHORT).show()
            }

            R.id.innerMenu -> {
                Toast.makeText(this, "Inner Menu", Toast.LENGTH_SHORT).show()
            }
        }
        return true
    }

    companion object {
        const val MSG_KEY = "MSG_KEY"
    }
}

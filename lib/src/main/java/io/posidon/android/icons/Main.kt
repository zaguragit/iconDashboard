package io.posidon.android.icons

import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import io.posidon.android.icons.tools.Tools
import posidon.android.conveniencelib.isInstalled
import java.lang.ref.WeakReference

class Main : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)
    }

    fun applyPosidon(v: View) {
        if (packageManager.isInstalled("posidon.launcher")) {
            Intent(Intent.ACTION_MAIN).apply {
                component = ComponentName("posidon.launcher", "posidon.launcher.external.ApplyIcons")
                putExtra("iconpack", packageName)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(this)
            }
        } else Toast.makeText(this, R.string.not_installed, Toast.LENGTH_SHORT).show()
    }

    fun applyLawnchair(v: View) {
        try {
            Intent("ch.deletescape.lawnchair.APPLY_ICONS", null).apply {
                putExtra("packageName", packageName)
                startActivity(this)
            }
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this, R.string.not_installed, Toast.LENGTH_SHORT).show()
        }
    }

    fun applyNiagara(v: View) {
        try {
            Intent("bitpit.launcher.APPLY_ICONS").apply {
                `package` = "bitpit.launcher"
                putExtra("packageName", packageName)
                startActivity(this)
            }
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this, R.string.not_installed, Toast.LENGTH_SHORT).show()
        }
    }

    fun list(v: View) {
        startActivity(Intent(this, IconList::class.java))
    }

    fun github(v: View) {try {
        val uri = Uri.parse(v.resources.getString(R.string.github_link))
        val i = Intent(Intent.ACTION_VIEW, uri)
        startActivity(i)
    } catch (ignore: Exception) {} }
}
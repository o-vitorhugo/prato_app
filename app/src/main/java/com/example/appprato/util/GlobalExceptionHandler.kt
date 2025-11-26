package com.example.appprato.util

import android.app.Application
import android.content.Intent
import com.example.appprato.ui.error.ErrorActivity
import java.io.PrintWriter
import java.io.StringWriter
import kotlin.system.exitProcess

class GlobalExceptionHandler(
    private val application: Application,
    private val defaultHandler: Thread.UncaughtExceptionHandler
) : Thread.UncaughtExceptionHandler {

    override fun uncaughtException(thread: Thread, e: Throwable) {
        // Converte a "pilha de chamadas" do erro para uma String legível.
        val stackTrace = StringWriter()
        e.printStackTrace(PrintWriter(stackTrace))

        // Prepara a intent para abrir a nossa tela de erro.
        val intent = Intent(application, ErrorActivity::class.java).apply {
            putExtra("error", "FATAL EXCEPTION: ${thread.name}\n\n$stackTrace")
            // Flags para garantir que a tela de erro seja uma nova tarefa.
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        // Inicia a tela de erro.
        application.startActivity(intent)

        // Encerra o processo do aplicativo para evitar comportamento instável.
        exitProcess(1)
    }
}

package com.github.hugovallada.utilitybelt.converter

import org.springframework.shell.standard.ShellComponent
import org.springframework.shell.standard.ShellMethod
import org.springframework.shell.standard.ShellOption
import java.nio.file.Files
import kotlin.io.path.Path

@ShellComponent
class Scanner {

    @ShellMethod(key = ["scan"])
    fun scan(
        @ShellOption("src") source: String,
        @ShellOption("values") values: String
    ) {
        val lines = Files.readAllLines(Path(source))
        val valuesProcessed = processValues(values)
        var counter = 1
        lines.forEach { line ->
            valuesProcessed.forEach { value ->
                if (line.contains(value)) {
                    println("$value - $counter")
                }
            }
            counter++
        }

    }

    private fun processValues(values: String): List<String> {
        return values.split(",")
            .filter { it.isNotBlank() }
            .map { it.trim() }
    }


}
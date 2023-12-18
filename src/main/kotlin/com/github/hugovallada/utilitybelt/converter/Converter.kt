package com.github.hugovallada.utilitybelt.converter

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import org.springframework.shell.standard.ShellComponent
import org.springframework.shell.standard.ShellMethod
import org.springframework.shell.standard.ShellOption
import java.nio.file.Files
import kotlin.io.path.Path
import kotlin.io.path.writeText

@ShellComponent
class Converter {
    companion object {
        val TYPES = listOf("json", "yaml")
    }

    @ShellMethod(key = ["converter"])
    fun convert(
        @ShellOption("src") source: String,
        @ShellOption("type") type: String,
        @ShellOption("dest", defaultValue = "") destination: String
    ) {
        println(type)
        println(type.length)
        require(TYPES.contains(type.lowercase())) { "Tipo de conversão inválida!" }
        val fileContent = Files.readString(Path(source))
        val content = getContentConverted(type, fileContent)
        if (destination.isNotBlank()) writeToFile(destination, content)
    }

    private fun getContentConverted(type: String, fileContent: String) = when (type) {
        "json" -> {
            convertFromYamlToJson(fileContent).also { println(it) }
        }

        "yaml" -> {
            convertFromJsonToYaml(fileContent).also { println(it) }
        }

        else -> error("Conversão inválida")
    }

    fun convertFromYamlToJson(fileContent: String): String {
        val rawString = ObjectMapper(YAMLFactory()).readValue(fileContent, Any::class.java)
        val json = ObjectMapper().writeValueAsString(rawString)
        return json
    }

    private fun convertFromJsonToYaml(fileContent: String): String {
        val rawString = ObjectMapper().readValue(fileContent, Any::class.java)
        val yaml = ObjectMapper(YAMLFactory()).writeValueAsString(rawString)
        return yaml
    }

    private fun writeToFile(destination: String, content: String) {
        Files.createFile(Path(destination)).writeText(content.replace("---", "").trimStart())
    }


}
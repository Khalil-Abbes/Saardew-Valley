package de.unisaarland.cs.se.selab.parser

import com.github.erosb.jsonsKema.JsonParser
import com.github.erosb.jsonsKema.SchemaLoader
import com.github.erosb.jsonsKema.Validator
import java.io.File

/**
 * object to validate files against their schemas
 */
object Validator {

    /**
     * validate the file parameter with the schema path
     */
    fun validateFile(file: String, schema: String): Boolean {
        val schemaLoader = SchemaLoader.forURL(schema).load()
        val validator = Validator.forSchema(schemaLoader)
        val input = JsonParser(File(file).readText()).parse()
        validator.validate(input) ?: return true
        return false
    }
}

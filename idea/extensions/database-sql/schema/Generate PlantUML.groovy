import com.intellij.database.model.DasColumn
import com.intellij.database.model.DasIndex
import com.intellij.database.model.DasTable
import com.intellij.database.util.Case
import com.intellij.database.util.DasUtil

/*
 * Api at https://www.jetbrains.com/help/datagrip/data-extractors.html#api_for_custom_data_extractors
 * Available context bindings:
 *   SELECTION   Iterable<DasObject>
 *   PROJECT     project
 *   FILES       files helper
 */
FILES.chooseDirectoryAndSave("Choose directory", "Choose where to store UML file") { dir ->
    generate(SELECTION.filter { it instanceof DasTable }, dir)
}

static def generate(tables, dir) {
    new File(dir, "output.plantuml")
            .withPrintWriter { out ->
                out.println "@startuml"
                out.println "top to bottom direction"
                tables.each { generateTable(out, it) }
                out.println "@enduml"
            }
}

static def generateTable(out, DasTable table) {
    def tableCommentExpr = table.getComment() ? " as \"${table.getName()} /* ${table.getComment()} */\"" : ""
    out.println "class ${table.getName()}$tableCommentExpr {"
    DasUtil.getColumns(table).each {
        generateColumn(out, it)
    }
    DasUtil.getIndices(table).each {
        generateIndex(out, it)
    }
    out.println "}"
}

static def generateColumn(out, DasColumn col) {
    def columnCommentExpr = col.getComment() ? " /* ${col.getComment()} */" : ""
    def columnNullExp = col.isNotNull() ? " not null" : ""
    def pkExpr = DasUtil.isPrimary(col) ? " primary key" : ""
    out.println "\t${col.getName()}$columnCommentExpr: ${col.getDasType().getSpecification()}${columnNullExp}${pkExpr}"
}
static def generateIndex(out, DasIndex ind) {
    def indCommentExpr = ind.getComment() ? " /* ${ind.getComment()} */" : ""
    def indColumns = ind.columnsRef.names().join(',')
    def indUniqueExpr = ind.unique ? " unique" : ""
    out.println "\t${ind.getName()}($indColumns)${indCommentExpr}${indUniqueExpr}"
}

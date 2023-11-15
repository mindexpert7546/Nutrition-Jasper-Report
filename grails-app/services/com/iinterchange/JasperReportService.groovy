package com.iinterchange

import grails.transaction.Transactional

import net.sf.jasperreports.engine.JasperCompileManager
import net.sf.jasperreports.engine.JasperExportManager
import net.sf.jasperreports.engine.JasperFillManager
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource

@Transactional
class JasperReportService {

    def grailsApplication

    def generateProductReport(def data, Map<String, Object> parameters) {
        // Compile the JasperReport template
        def reportPath = grailsApplication.parentContext.getResource("./reports/nutritionReport.jrxml").file.toString()
        def jasperReport = JasperCompileManager.compileReport(reportPath)

        // Fill the report with data and parameters
        def jasperPrint = JasperFillManager.fillReport(
            jasperReport,
            parameters,
            new JRBeanCollectionDataSource(data)
        )

        return jasperPrint
    }
}

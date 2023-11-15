package com.iinterchange

import grails.transaction.Transactional
import net.sf.jasperreports.engine.JasperExportManager
import com.iinterchange.NutritionDetails
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource


@Transactional
class ProfileDetailsController {

   def jasperReportService
  

    def index() {
         // Fetch data from the database or any other source
        def data = ProfileDetails.list()
        def nutritionDetailsList = NutritionDetails.list()
        JRBeanCollectionDataSource nutritionDataSource= new JRBeanCollectionDataSource(nutritionDetailsList)

        // Create parameters map
        def fileParams = [:]

        if (data) {
            // If data is not empty, fill parameters
            def firstProfile = data.first()
            fileParams.put("name", firstProfile.name)
            fileParams.put("dob", firstProfile.dob)
            fileParams.put("age", firstProfile.age)
            fileParams.put("MainDataSet",nutritionDataSource)
            // Add more parameters as needed
        } else {
            // If data is empty, use default values
            fileParams.put("name", "Default Name")
            fileParams.put("dob", "Default DOB")
            fileParams.put("age", 0) // Default age
            // Add more default values as needed
        }

        // Generate the Jasper report using the service
        def jasperPrint = jasperReportService.generateProductReport(data, fileParams)

        // Export the report to PDF and send it as the response
        response.setHeader('Content-Disposition', 'inline; filename=ProductReport.pdf')
        response.setContentType('application/pdf')
        JasperExportManager.exportReportToPdfStream(jasperPrint, response.outputStream)
    }
}

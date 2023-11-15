# Nutrition-Jasper-Report
It's represent to get the jasper report the through Services , Table , and more ....


![Screenshot 2023-11-15 172909](https://github.com/mindexpert7546/Nutrition-Jasper-Report/assets/89348788/1f6b9a19-4759-4e7b-a003-4b9807275eb3)
![Screenshot 2023-11-15 172929](https://github.com/mindexpert7546/Nutrition-Jasper-Report/assets/89348788/d37ace40-651d-470e-bc3d-6f75d14e40c8)

## Dependency grails 3.x
```
 //Extra dependency to getting the jasper report
    compile "com.lowagie:itext:2.1.7"
    compile("net.sf.jasperreports:jasperreports:5.6.1") {
    exclude group: 'antlr', module: 'antlr'
    exclude group: 'commons-logging', module: 'commons-logging'
    exclude group: 'org.apache.ant', module: 'ant'
    exclude group: 'mondrian', module: 'mondrian'
    exclude group: 'commons-javaflow', module: 'commons-javaflow'
    exclude group: 'net.sourceforge.barbecue', module: 'barbecue'
    exclude group: 'xml-apis', module: 'xml-apis'
    exclude group: 'xalan', module: 'xalan'
    exclude group: 'org.codehaus.groovy', module: 'groovy-all'
    exclude group: 'org.hibernate ', module: 'hibernate'
    exclude group: 'javax.xml.soap', module: 'saaj-api'
    exclude group: 'javax.servlet', module: 'servlet-api'
    exclude group: 'org.springframework', module: 'spring-core'
    exclude group: 'org.beanshell', module: 'bsh'
    exclude group: 'org.springframework', module: 'spring-beans'
    exclude group: 'jaxen', module: 'jaxen'
    exclude group: 'net.sf.barcode4j ', module: 'barcode4j'
    exclude group: 'org.apache.xmlgraphics', module: 'batik-svg-dom'
    exclude group: 'org.apache.xmlgraphics', module: 'batik-xml'
    exclude group: 'org.apache.xmlgraphics', module: 'batik-awt-util'
    exclude group: 'org.apache.xmlgraphics', module: 'batik-dom'
    exclude group: 'org.apache.xmlgraphics', module: 'batik-css'
    exclude group: 'org.apache.xmlgraphics', module: 'batik-gvt'
    exclude group: 'org.apache.xmlgraphics', module: 'batik-script'
    exclude group: 'org.apache.xmlgraphics', module: 'batik-svggen'
    exclude group: 'org.apache.xmlgraphics', module: 'batik-util'
    exclude group: 'org.apache.xmlgraphics', module: 'batik-bridge'
    exclude group: 'javax.persistence', module: 'persistence-api'
    exclude group: 'eclipse', module: 'jdtcore'
    exclude group: 'org.olap4j', module: 'olap4j'
}

    compile "org.apache.poi:poi:3.10-FINAL"
    compile "commons-io:commons-io:2.5"
    compile 'net.sf.jasperreports:jasperreports:6.20.0'
```
## Sample code for Domain class 
### Nutrition Details 
```
package com.iinterchange

class NutritionDetails {
    String nutrient 
    int total 
    int goal 
    String metric
    static constraints = {
        metric blank:true
    }
}
```
### Profile Details 
```
package com.iinterchange

class ProfileDetails {

    String name
    String dob
    int age

    static constraints = {
    }
}
```
## Service Class for Jasper Report 

```
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

```
## Controller code : 
```
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
```
## Jasper Code : 
```

<?xml version="1.0" encoding="UTF-8"?>
<!-- Created with Jaspersoft Studio version 6.20.6.final using JasperReports Library version 6.20.6-5c96b6aa8a39ac1dc6b6bea4b81168e16dd39231  -->
<jasperReport xmlns="http://jasperreports.sourceforge.net/jasperreports" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://jasperreports.sourceforge.net/jasperreports http://jasperreports.sourceforge.net/xsd/jasperreport.xsd" name="nutritionReport" pageWidth="595" pageHeight="842" columnWidth="555" leftMargin="20" rightMargin="20" topMargin="20" bottomMargin="20" uuid="50575b68-dbc5-419a-b156-666c94c31792">
	<property name="com.jaspersoft.studio.data.defaultdataadapter" value="One Empty Record"/>
	<style name="Table_TH" mode="Opaque" backcolor="#F0F8FF">
		<box>
			<pen lineWidth="0.5" lineColor="#000000"/>
			<topPen lineWidth="0.5" lineColor="#000000"/>
			<leftPen lineWidth="0.5" lineColor="#000000"/>
			<bottomPen lineWidth="0.5" lineColor="#000000"/>
			<rightPen lineWidth="0.5" lineColor="#000000"/>
		</box>
	</style>
	<style name="Table_CH" mode="Opaque" backcolor="#BFE1FF">
		<box>
			<pen lineWidth="0.5" lineColor="#000000"/>
			<topPen lineWidth="0.5" lineColor="#000000"/>
			<leftPen lineWidth="0.5" lineColor="#000000"/>
			<bottomPen lineWidth="0.5" lineColor="#000000"/>
			<rightPen lineWidth="0.5" lineColor="#000000"/>
		</box>
	</style>
	<style name="Table_TD" mode="Opaque" backcolor="#FFFFFF">
		<box>
			<pen lineWidth="0.5" lineColor="#000000"/>
			<topPen lineWidth="0.5" lineColor="#000000"/>
			<leftPen lineWidth="0.5" lineColor="#000000"/>
			<bottomPen lineWidth="0.5" lineColor="#000000"/>
			<rightPen lineWidth="0.5" lineColor="#000000"/>
		</box>
	</style>
	<subDataset name="NutritionDataSet" uuid="719edad4-e9be-48ed-8dc0-56fc2374d5bb">
		<queryString>
			<![CDATA[]]>
		</queryString>
		<field name="nutrient" class="java.lang.String"/>
		<field name="total" class="java.lang.Integer"/>
		<field name="goal" class="java.lang.Integer"/>
		<field name="metric" class="java.lang.String"/>
	</subDataset>
	<parameter name="name" class="java.lang.String"/>
	<parameter name="dob" class="java.lang.String"/>
	<parameter name="age" class="java.lang.Integer"/>
	<parameter name="MainDataSet" class="net.sf.jasperreports.engine.data.JRBeanCollectionDataSource"/>
	<queryString>
		<![CDATA[]]>
	</queryString>
	<background>
		<band splitType="Stretch"/>
	</background>
	<title>
		<band height="103" splitType="Stretch">
			<frame>
				<reportElement mode="Opaque" x="2" y="6" width="550" height="90" backcolor="#FF110D" uuid="87ed4705-7a00-4b6e-aa4e-e7d8a2a1d688"/>
				<staticText>
					<reportElement x="20" y="30" width="180" height="40" uuid="eb98db11-0717-467b-9941-939ea217b8c7"/>
					<textElement textAlignment="Left">
						<font size="24"/>
					</textElement>
					<text><![CDATA[Nutrition Report]]></text>
				</staticText>
				<textField>
					<reportElement x="340" y="0" width="190" height="30" forecolor="#FFFFFF" uuid="e35cf121-2b99-48a5-95df-df3f90a71fa8"/>
					<textElement textAlignment="Right">
						<font size="16"/>
					</textElement>
					<textFieldExpression><![CDATA[$P{name}]]></textFieldExpression>
				</textField>
				<textField>
					<reportElement x="340" y="30" width="190" height="30" forecolor="#FFFFFF" uuid="8cd10d53-6ac5-4bd9-a0b1-fc5d7b8cfa44"/>
					<textElement textAlignment="Right">
						<font size="16"/>
					</textElement>
					<textFieldExpression><![CDATA["DOB :  " + $P{dob}]]></textFieldExpression>
				</textField>
				<textField>
					<reportElement x="340" y="60" width="190" height="30" forecolor="#FFFFFF" uuid="3a21886f-9860-4bb9-9e94-f0a38c840d6e"/>
					<textElement textAlignment="Right">
						<font size="16"/>
					</textElement>
					<textFieldExpression><![CDATA["Age : " + $P{age}]]></textFieldExpression>
				</textField>
			</frame>
		</band>
	</title>
	<pageHeader>
		<band height="35" splitType="Stretch"/>
	</pageHeader>
	<detail>
		<band height="405" splitType="Stretch">
			<componentElement>
				<reportElement x="2" y="-1" width="550" height="406" uuid="aa286d1d-0dd7-4ff4-855e-a98722f08abf">
					<property name="com.jaspersoft.studio.layout" value="com.jaspersoft.studio.editor.layout.VerticalRowLayout"/>
					<property name="com.jaspersoft.studio.table.style.table_header" value="Table_TH"/>
					<property name="com.jaspersoft.studio.table.style.column_header" value="Table_CH"/>
					<property name="com.jaspersoft.studio.table.style.detail" value="Table_TD"/>
				</reportElement>
				<jr:table xmlns:jr="http://jasperreports.sourceforge.net/jasperreports/components" xsi:schemaLocation="http://jasperreports.sourceforge.net/jasperreports/components http://jasperreports.sourceforge.net/xsd/components.xsd">
					<datasetRun subDataset="NutritionDataSet" uuid="9f3e693b-4196-4982-a52c-4fb662334661">
						<dataSourceExpression><![CDATA[$P{MainDataSet}]]></dataSourceExpression>
					</datasetRun>
					<jr:column width="179" uuid="2d394dbf-306d-4925-9355-76133e96124b">
						<property name="com.jaspersoft.studio.components.table.model.column.name" value="Column1"/>
						<jr:columnHeader style="Table_CH" height="30" rowSpan="1">
							<staticText>
								<reportElement x="0" y="0" width="179" height="30" forecolor="#000005" uuid="62ab0a55-3e9c-45c3-9afd-820dc3dc591a"/>
								<textElement textAlignment="Center" verticalAlignment="Middle">
									<font size="18"/>
								</textElement>
								<text><![CDATA[Nutrient]]></text>
							</staticText>
						</jr:columnHeader>
						<jr:detailCell style="Table_TD" height="30">
							<textField>
								<reportElement x="0" y="0" width="179" height="30" uuid="c4e031f9-13fd-4c19-b57e-a9a012d400ae"/>
								<textElement textAlignment="Center" verticalAlignment="Middle">
									<font size="12"/>
								</textElement>
								<textFieldExpression><![CDATA[$F{nutrient}]]></textFieldExpression>
							</textField>
						</jr:detailCell>
					</jr:column>
					<jr:column width="130" uuid="f8d11f0d-cafa-4e7d-a438-e87c0add7e2a">
						<property name="com.jaspersoft.studio.components.table.model.column.name" value="Column2"/>
						<jr:columnHeader style="Table_CH" height="30" rowSpan="1">
							<staticText>
								<reportElement x="0" y="0" width="130" height="30" forecolor="#000005" uuid="7c6c30a7-a9d3-4f41-9bc8-b724238fdcb2"/>
								<textElement textAlignment="Center" verticalAlignment="Middle">
									<font size="18"/>
								</textElement>
								<text><![CDATA[Total]]></text>
							</staticText>
						</jr:columnHeader>
						<jr:detailCell style="Table_TD" height="30">
							<textField>
								<reportElement x="0" y="0" width="130" height="30" uuid="3e84a231-1cb1-4bfb-9f3c-52b76d9268bc"/>
								<textElement textAlignment="Center" verticalAlignment="Middle">
									<font size="12"/>
								</textElement>
								<textFieldExpression><![CDATA[$F{total} +""+ $F{metric}]]></textFieldExpression>
							</textField>
						</jr:detailCell>
					</jr:column>
					<jr:column width="121" uuid="a49c5083-56bc-4e3e-bd89-53df0616cf98">
						<property name="com.jaspersoft.studio.components.table.model.column.name" value="Column3"/>
						<jr:columnHeader style="Table_CH" height="30" rowSpan="1">
							<staticText>
								<reportElement x="0" y="0" width="121" height="30" forecolor="#000005" uuid="941ac01d-4579-4ceb-ac58-ea2586adf235"/>
								<textElement textAlignment="Center" verticalAlignment="Middle">
									<font size="18"/>
								</textElement>
								<text><![CDATA[Goal]]></text>
							</staticText>
						</jr:columnHeader>
						<jr:detailCell style="Table_TD" height="30">
							<textField>
								<reportElement x="0" y="0" width="121" height="30" uuid="24e0f6c7-3c7a-4de3-867b-752018cd33e8"/>
								<textElement textAlignment="Center" verticalAlignment="Middle">
									<font size="12"/>
								</textElement>
								<textFieldExpression><![CDATA[$F{goal} + ""+$F{metric}]]></textFieldExpression>
							</textField>
						</jr:detailCell>
					</jr:column>
					<jr:column width="110" uuid="8a646725-0c0a-40d9-a96d-2b9eb5c9c401">
						<property name="com.jaspersoft.studio.components.table.model.column.name" value="Column4"/>
						<jr:columnHeader style="Table_CH" height="30" rowSpan="1">
							<staticText>
								<reportElement x="0" y="0" width="110" height="30" forecolor="#000005" uuid="ca6a030f-64e0-4d5d-92a8-02d4257151a5"/>
								<textElement textAlignment="Center" verticalAlignment="Middle">
									<font size="18"/>
								</textElement>
								<text><![CDATA[Left]]></text>
							</staticText>
						</jr:columnHeader>
						<jr:detailCell style="Table_TD" height="30">
							<textField>
								<reportElement x="0" y="0" width="110" height="30" uuid="2d9e28ca-74bd-49bb-ae0a-cc821d89ea23"/>
								<textElement textAlignment="Center" verticalAlignment="Middle">
									<font size="12"/>
								</textElement>
								<textFieldExpression><![CDATA[($F{goal}-$F{total}) +"" +$F{metric}]]></textFieldExpression>
							</textField>
						</jr:detailCell>
					</jr:column>
				</jr:table>
			</componentElement>
		</band>
	</detail>
	<columnFooter>
		<band height="45" splitType="Stretch"/>
	</columnFooter>
	<pageFooter>
		<band height="54" splitType="Stretch"/>
	</pageFooter>
	<summary>
		<band height="42" splitType="Stretch"/>
	</summary>
</jasperReport>
```

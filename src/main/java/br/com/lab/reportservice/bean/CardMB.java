package br.com.lab.reportservice.bean;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import br.com.lab.reportservice.model.Card;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRPptxExporter;

@SuppressWarnings("deprecation")
@ManagedBean
@RequestScoped
public class CardMB {
	private List<Card> cards;
	private HashMap<String, Object> params;

	public CardMB() {
		super();
		this.loadCards();
		this.loadDataFile();
	}

	private void loadCards() {
		cards = new ArrayList<Card>();
		Integer count = 1;
		while (count < 10) {
			cards.add(new Card(count, "CARD_".concat(count.toString()),
					"(XX) XXXXX-XXXX".replaceAll("X", count.toString())));
			count++;
		}
	}

	private void loadDataFile() {
		params = new HashMap<String, Object>();
		params.put("cards", cards);
		params.put("title", "Impressão de cartões");
	}

	public void exportPDF(ActionEvent actionEvent) {
		try {
			generateFile("PDF", "/card.jasper", "jsfReporte.pdf", params);
		} catch (JRException | IOException e) {
			e.printStackTrace();
		}
	}

	public void exportPPT(ActionEvent actionEvent) throws JRException, IOException {
		try {
			generateFile("PPT", "/card.jasper", "jsfReporte.ppt", params);
		} catch (JRException | IOException e) {
			e.printStackTrace();
		}
	}

	public void exportExcel(ActionEvent actionEvent) throws JRException, IOException {
		try {
			generateFile("XLS", "/card.jasper", "jsfReporte.xls", params);
		} catch (JRException | IOException e) {
			e.printStackTrace();
		}
	}

	public void exportDOC(ActionEvent actionEvent) throws JRException, IOException {
		try {
			generateFile("DOC", "/card.jasper", "jsfReporte.doc", params);
		} catch (JRException | IOException e) {
			e.printStackTrace();
		}
	}

	public void loadPDF(ActionEvent actionEvent) throws Exception {
		try {
			generateFile("LOAD_PDF", "/card.jasper", null, params);
		} catch (JRException | IOException e) {
			e.printStackTrace();
		}
	}

	private void generateFile(String typeFile, String jasperDir, String fileName, HashMap<String, Object> params)
			throws JRException, IOException {
		File jasper = new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath(jasperDir));
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasper.getPath(), params, new JREmptyDataSource());

		HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext()
				.getResponse();

		byte[] bytes = null;
		if (typeFile.equalsIgnoreCase("LOAD_PDF")) {
			bytes = JasperRunManager.runReportToPdf(jasper.getPath(), params, new JREmptyDataSource());
			response.setContentType("application/pdf");
			response.setContentLength(bytes.length);
		} else {
			response.addHeader("Content-disposition", "attachment; filename=".concat(fileName));
		}

		ServletOutputStream stream = response.getOutputStream();

		switch (typeFile) {
		case "PDF":
			JasperExportManager.exportReportToPdfStream(jasperPrint, stream);
			break;
		case "LOAD_PDF":
			stream.write(bytes, 0, bytes.length);
			break;
		case "PPT":
			JRPptxExporter exporterPPT = new JRPptxExporter();
			exporterPPT.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			exporterPPT.setParameter(JRExporterParameter.OUTPUT_STREAM, stream);
			exporterPPT.exportReport();
			break;
		case "XLS":
			JRXlsExporter exporterXLS = new JRXlsExporter();
			exporterXLS.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			exporterXLS.setParameter(JRExporterParameter.OUTPUT_STREAM, stream);
			exporterXLS.exportReport();
			break;
		case "DOC":
			JRDocxExporter exporterDOC = new JRDocxExporter();
			exporterDOC.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			exporterDOC.setParameter(JRExporterParameter.OUTPUT_STREAM, stream);
			exporterDOC.exportReport();
			break;
		}

		stream.flush();
		stream.close();
		FacesContext.getCurrentInstance().responseComplete();

	}
}

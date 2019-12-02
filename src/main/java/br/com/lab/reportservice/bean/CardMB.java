package br.com.lab.reportservice.bean;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import br.com.lab.reportservice.model.Card;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRPptxExporter;

@SuppressWarnings("deprecation")
@ManagedBean
@RequestScoped
public class CardMB {
	private List<Card> cards;

	public CardMB() {
		super();
		this.loadCards();

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

	public void exportPDF(ActionEvent actionEvent) throws JRException, IOException {
		File jasper = new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/card.jasper"));
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasper.getPath(), null,
				new JRBeanCollectionDataSource(cards));

		HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext()
				.getResponse();
		response.addHeader("Content-disposition", "attachment; filename=jsfReporte.pdf");
		ServletOutputStream stream = response.getOutputStream();

		JasperExportManager.exportReportToPdfStream(jasperPrint, stream);
		stream.flush();
		stream.close();

		FacesContext.getCurrentInstance().responseComplete();
	}

	public void exportPPT(ActionEvent actionEvent) throws JRException, IOException {
		File jasper = new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/card.jasper"));
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasper.getPath(), null,
				new JRBeanCollectionDataSource(cards));

		HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext()
				.getResponse();
		response.addHeader("Content-disposition", "attachment; filename=jsfReporte.ppt");
		ServletOutputStream outStream = response.getOutputStream();

		JRPptxExporter exporter = new JRPptxExporter();
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, outStream);
		exporter.exportReport();

		outStream.flush();
		outStream.close();
		FacesContext.getCurrentInstance().responseComplete();
	}

	public void exportExcel(ActionEvent actionEvent) throws JRException, IOException {
		File jasper = new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/card.jasper"));
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasper.getPath(), null,
				new JRBeanCollectionDataSource(cards));

		HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext()
				.getResponse();
		response.addHeader("Content-disposition", "attachment; filename=jsfReporte.xls");
		ServletOutputStream outStream = response.getOutputStream();

		JRXlsExporter exporter = new JRXlsExporter();
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, outStream);
		exporter.exportReport();

		outStream.flush();
		outStream.close();
		FacesContext.getCurrentInstance().responseComplete();
	}

	public void exportDOC(ActionEvent actionEvent) throws JRException, IOException {
		File jasper = new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/card.jasper"));
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasper.getPath(), null,
				new JRBeanCollectionDataSource(cards));

		HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext()
				.getResponse();
		response.addHeader("Content-disposition", "attachment; filename=jsfReporte.doc");
		ServletOutputStream outStream = response.getOutputStream();

		JRDocxExporter exporter = new JRDocxExporter();
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, outStream);
		exporter.exportReport();

		outStream.flush();
		outStream.close();
		FacesContext.getCurrentInstance().responseComplete();
	}

	public void loadPDF(ActionEvent actionEvent) throws Exception {
		File jasper = new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/card.jasper"));

		byte[] bytes = JasperRunManager.runReportToPdf(jasper.getPath(), null, new JRBeanCollectionDataSource(cards));
		HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext()
				.getResponse();
		response.setContentType("application/pdf");
		response.setContentLength(bytes.length);
		ServletOutputStream outStream = response.getOutputStream();
		outStream.write(bytes, 0, bytes.length);
		outStream.flush();
		outStream.close();

		FacesContext.getCurrentInstance().responseComplete();
	}
}

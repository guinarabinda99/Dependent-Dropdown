package com.csm.demo.Controller;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.csm.demo.Repositories.AllRepository;
import com.csm.demo.Repositories.CityRepository;
import com.csm.demo.Repositories.CountryRepository;
import com.csm.demo.Repositories.StateRepository;
import com.csm.demo.entities.AllCountryStateCity;
import com.csm.demo.entities.City;
import com.csm.demo.entities.Country;
import com.csm.demo.entities.State;
import com.itextpdf.awt.geom.Rectangle;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfDocument;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

@Controller
public class AllDependentController {
	@Autowired
	private CountryRepository countryRepo;

	@Autowired
	private StateRepository stateRepo;

	@Autowired
	private CityRepository cityRepo;

	@Autowired
	private AllRepository allRepository;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String manageBlockContractorDetails(Model model) {

		model.addAttribute("cc", countryRepo.findAll());
//		model.addAttribute("ss", stateRepo.findAll());
//		model.addAttribute("dd", cityRepo.findAll());
		model.addAttribute("view", allRepository.findAll());
		return "allCountry";
	}

	@RequestMapping(value = "find-state-by-country-id.htm", method = RequestMethod.GET)
	@ResponseBody
	public String findAllDepartmentByClass(@RequestParam("countryId") Integer countryId) throws JSONException {
		List<State> state = stateRepo.getAllStateByCountryId(countryId);
		JSONArray jarr = new JSONArray();
		JSONObject jobj = null;
		for (State states : state) {
			jobj = new JSONObject();
			jobj.put("stateId", states.getStateId());
			jobj.put("stateName", states.getStateName());
			jarr.put(jobj);
		}
		return jarr.toString();
	}

	@RequestMapping(value = "find-city-by-state-id.htm", method = RequestMethod.GET)
	@ResponseBody
	public String findCityByState(@RequestParam("stateId") Integer stateId,
			@RequestParam("countryId") Integer countryId) throws JSONException {
		List<City> city = cityRepo.getAllCityByStateId(stateId, countryId);
		JSONArray jarr = new JSONArray();
		JSONObject jobj = null;
		for (City cities : city) {
			jobj = new JSONObject();
			jobj.put("cityId", cities.getCityId());
			jobj.put("cityName", cities.getCityName());
			jarr.put(jobj);
		}
		return jarr.toString();
	}

	// save
	@RequestMapping(value = "saveAll", method = RequestMethod.POST)
	public String SaveAllCountryStateCity(Model model,
			@ModelAttribute("AllCountryStateCity") AllCountryStateCity allcsc, @RequestParam("conId") Country conId,
			@RequestParam("stId") State stateId, @RequestParam("cityId") City cityId) {
		allcsc.setCountry(conId);
		allcsc.setState(stateId);
		allcsc.setCity(cityId);

		allRepository.save(allcsc);
		System.out.println("Comming");
		return "redirect:/";
	}

	@RequestMapping(value = "country-state-city-excel.htm", method = RequestMethod.POST)
	public String SaveAllCountryStateCity(Model model, HttpServletResponse response) {

		List<AllCountryStateCity> allcountrystatecity = allRepository.findAll();
		Workbook workbook = new XSSFWorkbook();
		// CreationHelper createHelper = workbook.getCreationHelper();
		Sheet sheet = workbook.createSheet("Country_State_City");

		CellStyle headerCellStyle = workbook.createCellStyle();
		headerCellStyle.setFillBackgroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
		headerCellStyle.setFillPattern(FillPatternType.BIG_SPOTS);

		Font newFont = workbook.createFont();
		newFont.setBold(true);
		newFont.setColor(IndexedColors.WHITE.getIndex());
		newFont.setFontHeightInPoints((short) 13);
		newFont.setItalic(false);

		headerCellStyle.setFont(newFont);
		Row headerRow = sheet.createRow(2);
		Row rows2 = sheet.createRow(0);
		Cell genCell = rows2.createCell(0);
		genCell.setCellStyle(headerCellStyle);
		genCell.setCellValue("Generated On:");
		rows2.createCell(1).setCellValue(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date()));

		String[] columns = { "Sl.no", "Country Name", "State Name", "City Name" };

		for (int i = 0; i < columns.length; i++) {
			Cell cell = headerRow.createCell(i);
			cell.setCellValue(columns[i]);
			cell.setCellStyle(headerCellStyle);

		}

		int rowNum = 3;
		// NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
		int count = 0;

		for (AllCountryStateCity allcountrystatecities : allcountrystatecity) {
			Row row = sheet.createRow(rowNum++);
			if (allcountrystatecities.getClass() != null) {
				count = count + 1;
				String s = String.valueOf(count);
				row.createCell(0).setCellValue(s.trim());
				row.createCell(1).setCellValue(allcountrystatecities.getCountry().getCountryName());
				row.createCell(2).setCellValue(allcountrystatecities.getState().getStateName());
				row.createCell(3).setCellValue(allcountrystatecities.getCity().getCityName());

			}
		}
		for (int i = 0; i < columns.length; i++) {
			sheet.autoSizeColumn(i);
		}

		response.setContentType("application/vnd.ms-excel");

		String FILENAME = "All-Country-State-city.xls";
		response.addHeader("Content-Disposition", "attachment; filename=" + FILENAME);
		try {
			workbook.write(response.getOutputStream());
			response.getOutputStream().flush();
			response.getOutputStream().close();
			workbook.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "redirect:/";
	}

	@RequestMapping(value = "country-state-city-pdf.htm", method = RequestMethod.POST)
	public String AllCountryStateCityPdf(Model model, HttpServletResponse response) {

		try {
			List<AllCountryStateCity> allcountrystatecity = allRepository.findAll();
			Document document = new Document(PageSize.A4.rotate(), 10f, 10f, 10f, 0f);
			String FILENAME = "All-Country-State-City-report.pdf";
			PdfWriter writer = PdfWriter.getInstance(document, response.getOutputStream());
			response.setContentType("application/pdf");
			response.addHeader("Content-Disposition", "attachment; filename=" + FILENAME);
			document.open();
            
			 
			 Paragraph p =new Paragraph("All Country State City Report");
	         p.setAlignment(Element.ALIGN_CENTER);
	         document.add(p);
			
			PdfPTable table = new PdfPTable(2);
			table.setWidthPercentage(100);
			table.setSpacingBefore(10f);
			table.setSpacingAfter(0);
			table.setWidthPercentage(100);
			PdfPCell cell1 = new PdfPCell(new Paragraph("Generated On:"));
			cell1.setBackgroundColor(BaseColor.LIGHT_GRAY);
			cell1.setPaddingLeft(10);
			cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
			table.addCell(cell1);

			PdfPCell cell2 = new PdfPCell(
					new Paragraph(new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss").format(new Date())));
			cell2.setPaddingLeft(10);
			cell2.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
			table.addCell(cell2);

			table.addCell(new Paragraph(" "));

			
			document.add(table);
			PdfPTable space = new PdfPTable(1);
			space.getDefaultCell().setFixedHeight(65);
			space.setWidthPercentage(100);
			space.getDefaultCell().setBorder(Rectangle.OUT_BOTTOM);
			space.getDefaultCell().setBorderColor(BaseColor.LIGHT_GRAY);
			PdfPCell spaceCell = new PdfPCell(new Paragraph(" "));
			space.addCell(spaceCell);
			document.add(space);

			//Font font = (Font) FontFactory.getFont(FontFactory.COURIER, 10);

			PdfPTable table1 = new PdfPTable(4);
			table1.setWidthPercentage(100);
			table1.setSpacingBefore(0);
			table1.setSpacingAfter(0);
			table1.setWidthPercentage(100);

			// Font font=FontFactory.getFont(FontFactory.COURIER, 11);
			

			String[] columns = { "SlNo.", "Country", "State", "City" };
			for (int i = 0; i < columns.length; i++) {
				PdfPCell cell = new PdfPCell(new Paragraph(columns[i]));
				cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
				cell.setPaddingLeft(10);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				table1.addCell(cell);
			}
			int slNo = 1;
			
				for (int i = 0; i < allcountrystatecity.size(); i++) {
					PdfPCell cellRow1 = new PdfPCell(new Paragraph(String.valueOf(slNo)));

					cellRow1.setHorizontalAlignment(Element.ALIGN_CENTER);
					cellRow1.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table1.addCell(cellRow1);

					PdfPCell cellRow2s = new PdfPCell(new Paragraph(
							allcountrystatecity.get(i).getCountry() == null ? "NA" : allcountrystatecity.get(i).getCountry().getCountryName()));
					cellRow2s.setHorizontalAlignment(Element.ALIGN_LEFT);
					cellRow2s.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table1.addCell(cellRow2s);
					
					PdfPCell cellRow3s = new PdfPCell(new Paragraph(
							allcountrystatecity.get(i).getState() == null ? "NA" : allcountrystatecity.get(i).getState().getStateName()));
					cellRow3s.setHorizontalAlignment(Element.ALIGN_LEFT);
					cellRow3s.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table1.addCell(cellRow3s);
					
					PdfPCell cellRow4s = new PdfPCell(new Paragraph(
							allcountrystatecity.get(i).getCity() == null ? "NA" : allcountrystatecity.get(i).getCity().getCityName()));
					cellRow4s.setHorizontalAlignment(Element.ALIGN_LEFT);
					cellRow4s.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table1.addCell(cellRow4s);

					
					slNo++;
				}
			
			document.add(table1);
			document.close();
			writer.close();

		} catch (Exception e) {
			// TODO Auto-generated catch blocks
			System.out.println(e.toString());
			e.printStackTrace();
		}
		return "redirect:/";

	}

}

package br.com.douglasfernandes.main;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import br.com.douglasfernandes.dao.SalesDB;
import br.com.douglasfernandes.model.Sale;

public class Main {
	public static void main(String[] args) {
		Calendar tmp = Calendar.getInstance();
		
		tmp.set(2017, 0, 1);
		Date initialDate = new Date(tmp.getTimeInMillis());
		
		tmp.set(2017, 0, 31);
		Date endDate = new Date(tmp.getTimeInMillis());
		
		Main main = new Main();
		main.printReport(initialDate, endDate);
	}
	
	public void printReport(Date initialDate, Date endDate) {
		List<Sale> sales = getSales(initialDate, endDate);
		
		Collections.sort(sales, new Comparator<Sale>() {

			public int compare(Sale o1, Sale o2) {
				return o1.getStore().getId().compareTo(o2.getStore().getId());
			}
			
		});
		
		List<List<Sale>> listSplitByStore = new ArrayList<List<Sale>>();
		
		int lastId = 0;
		List<Sale> tmpList = new ArrayList<Sale>();
		
		for(Sale sale : sales) {
			
			int id = sale.getStore().getId();
			if(id != lastId) {
				lastId = id;
				if(tmpList != null && tmpList.size() > 0)
					listSplitByStore.add(tmpList);
				tmpList = new ArrayList<Sale>();
			}
			tmpList.add(sale);
		}
		
		for(List<Sale> salesOfStore : listSplitByStore) {
			
			String toPrint = ""+salesOfStore.get(0).getStore().getName()+";";
			
			HashMap<String,Double> map = new HashMap<String, Double>();
			
			List<String> flags = new ArrayList<String>();
			
			for(Sale sale : salesOfStore) {
				String creditCardName = sale.getCreditCard().getName();
				
				if(!flags.contains(creditCardName)) {
					flags.add(creditCardName);
				}
				
				if(map.get(creditCardName) != null) {
					double value = map.get(creditCardName) + sale.getValue();
					map.put(creditCardName, value);
				}
				else {
					map.put(creditCardName, sale.getValue());
				}
			}
			
			double sum = 0;
			
			for(String flag : flags) {
				sum += map.get(flag);
				toPrint += formatDouble(map.get(flag))+";";
			}
			
			toPrint += formatDouble(sum);
			
			System.out.println(toPrint);
		}
	}
	
	private String formatDouble(double value) {
		Locale.setDefault(Locale.ENGLISH);
		String result = String.format("%.2f", value);
		return result;
	}
	
	public List<Sale> getSales(Date initialDate, Date endDate) {
		return SalesDB.getSales(initialDate, endDate);
	}
}

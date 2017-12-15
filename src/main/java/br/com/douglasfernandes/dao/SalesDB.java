package br.com.douglasfernandes.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import br.com.douglasfernandes.model.CreditCard;
import br.com.douglasfernandes.model.Sale;
import br.com.douglasfernandes.model.Store;

public class SalesDB {
	private static Connection conn = null;
	
	public static List<Sale> getSales(Date initialDate, Date endDate) {
		try {
			conn = ConnectionFactory.getConnection();
			PreparedStatement stmt = conn.prepareStatement("SELECT id,store_id,credit_card_id,value FROM sale WHERE date >= ? and date <= ? order by store_id,credit_card_id");
			stmt.setDate(1, initialDate);
			stmt.setDate(2, endDate);
			
			ResultSet rs = stmt.executeQuery();
			
			List<Sale> lista = new ArrayList<Sale>();
			while(rs.next()) {
				Sale sale = new Sale();
				
				sale.setId(rs.getInt("id"));
				
				sale.setValue(rs.getDouble("value"));
				
				Store store = new Store();
				store.setId(rs.getInt("store_id"));
				sale.setStore(store);
				
				CreditCard creditCard = new CreditCard();
				creditCard.setId(rs.getInt("credit_card_id"));
				sale.setCreditCard(creditCard);
				
				lista.add(sale);
			}
			
			conn.close();
			
			for(Sale sale : lista) {
				Store store = StoreDB.getStoreById(sale.getStore().getId());
				sale.setStore(store);
			}
			
			for(Sale sale : lista) {
				CreditCard creditCard = CreditCardDB.getCreditCardById(sale.getCreditCard().getId());
				sale.setCreditCard(creditCard);
			}
			
			return lista;
			
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				conn.close();
			} catch(Exception ex) {
				ex.printStackTrace();
			}
		}
	}
	
}

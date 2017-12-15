package br.com.douglasfernandes.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import br.com.douglasfernandes.model.CreditCard;

public class CreditCardDB {
	private static Connection conn = null;
	
	public static CreditCard getCreditCardById(Integer id) {
		try {
			conn = ConnectionFactory.getConnection();
			PreparedStatement stmt = conn.prepareStatement("SELECT * FROM credit_card WHERE id = ?");
			stmt.setInt(1, id);
			
			ResultSet rs = stmt.executeQuery();
			
			CreditCard creditCard = null;
			
			while(rs.next()) {
				creditCard = new CreditCard();
				
				creditCard.setId(rs.getInt("id"));
				creditCard.setName(rs.getString("name"));
			}
			
			conn.close();
			
			return creditCard;
			
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

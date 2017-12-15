package br.com.douglasfernandes.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import br.com.douglasfernandes.model.Store;

public class StoreDB {
	private static Connection conn = null;
	
	public static Store getStoreById(Integer id) {
		try {
			conn = ConnectionFactory.getConnection();
			PreparedStatement stmt = conn.prepareStatement("SELECT * FROM store WHERE id = ?");
			stmt.setInt(1, id);
			
			ResultSet rs = stmt.executeQuery();
			
			Store store = null;
			
			while(rs.next()) {
				store = new Store();
				
				store.setId(rs.getInt("id"));
				store.setName(rs.getString("name"));
			}
			
			conn.close();
			
			return store;
			
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

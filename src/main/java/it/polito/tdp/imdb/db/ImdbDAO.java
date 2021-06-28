package it.polito.tdp.imdb.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.imdb.model.Actor;
import it.polito.tdp.imdb.model.Adiacenza;
import it.polito.tdp.imdb.model.Director;
import it.polito.tdp.imdb.model.Movie;

public class ImdbDAO {
	
	public void listAllActors(Map<Integer,Actor> idMap){
		String sql = "SELECT * FROM actors";
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				if(!idMap.containsKey(res.getInt("id"))) {
				Actor actor = new Actor(res.getInt("id"), res.getString("first_name"), res.getString("last_name"),
						res.getString("gender"));
				
				idMap.put(res.getInt("id"),actor);
				}
			}
			conn.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public List<Movie> listAllMovies(){
		String sql = "SELECT * FROM movies";
		List<Movie> result = new ArrayList<Movie>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Movie movie = new Movie(res.getInt("id"), res.getString("name"), 
						res.getInt("year"), res.getDouble("rank"));
				
				result.add(movie);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	public List<Director> listAllDirectors(){
		String sql = "SELECT * FROM directors";
		List<Director> result = new ArrayList<Director>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Director director = new Director(res.getInt("id"), res.getString("first_name"), res.getString("last_name"));
				
				result.add(director);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<String> getGeneri(){
		String sql = "SELECT DISTINCT genre "
				+ "FROM movies_genres";
		List<String> result = new ArrayList<String>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				result.add(res.getString("genre"));
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Actor> getVertici(String genere, Map<Integer,Actor> idMap){
		String sql = "SELECT DISTINCT(a.id) "
				+ "FROM actors a, roles r, movies_genres g "
				+ "WHERE a.id= r.actor_id AND r.movie_id= g.movie_id AND g.genre=?";

		List<Actor> ris = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, genere);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				if(idMap.containsKey(res.getInt("id"))) {
				Actor actor = idMap.get(res.getInt("id"));
				ris.add(actor);
				}
			}
			conn.close();
			return ris;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Adiacenza> getAdiacenze(String genere, Map<Integer, Actor> idMap){
		String sql ="SELECT a.id AS id1, a2.id AS id2, COUNT(*) as peso "
				+ "FROM actors a,actors a2, roles r, roles r2, movies_genres g "
				+ "WHERE a.id= r.actor_id AND a2.id=r2.actor_id AND r.movie_id = g.movie_id AND a.id< a2.id and r2.movie_id= g.movie_id AND g.genre = ? "
				+ "GROUP BY a.id,a2.id";
		
		Connection conn = DBConnect.getConnection();
		List<Adiacenza> ris = new ArrayList<>();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, genere);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				if(idMap.containsKey(res.getInt("id1")) && idMap.containsKey(res.getInt("id2"))) {
				   Actor a1 = idMap.get(res.getInt("id1"));
				   Actor a2 = idMap.get(res.getInt("id2"));
				   Adiacenza aa = new Adiacenza(a1, a2, res.getInt("peso"));
				   ris.add(aa);
				}
			}
			conn.close();
			return ris;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
}

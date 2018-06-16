//package jsonParser;
 
import java.io.BufferedReader;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
 
public class ReadReviewJSON 
{
 
    public void run_review() throws SQLException
    {
        // TODO Auto-generated method stub
        Connection conn = null;
        PreparedStatement ps = null;
        String sqlquery = "INSERT INTO REVIEWS"
                + "(funny_vote, useful_vote, cool_vote, user_id, review_id, stars, r_date, r_text, r_type, bid) VALUES"
                        + "(?,?,?,?,?,?,to_date(?,'yyyy-mm-dd'),?,?,?)";
         
        JSONParser parser = new JSONParser();
        try
        {
            conn = getDBConnection();
            ps = conn.prepareStatement(sqlquery);
             
            FileReader filereader = new FileReader("C:\\Users\\prati\\eclipse-workspace\\HomeworkAssignment3\\JSON Files\\yelp_review.json");
            BufferedReader bufferedReader = new BufferedReader(filereader);
            String line;
            while ((line = bufferedReader.readLine()) != null) 
            {
                Object obj = parser.parse(line);
                JSONObject jsonObject = (JSONObject) obj;
                //System.out.println(line);
                
                JSONObject votes = (JSONObject) jsonObject.get("votes");
                int funnyVotes,usefulVotes,coolVotes;
                
                funnyVotes = ((Long) votes.get("funny")).intValue();
                usefulVotes = ((Long) votes.get("useful")).intValue();
                coolVotes = ((Long) votes.get("cool")).intValue();
                ps.setInt(1, funnyVotes);
                ps.setInt(2, usefulVotes);
                ps.setInt(3, coolVotes);
                 
                String userId = (String) jsonObject.get("user_id");
                ps.setString(4, userId);
                 
                String reviewId = (String) jsonObject.get("review_id");
                ps.setString(5, reviewId);
                 
                int stars = ((Long) jsonObject.get("stars")).intValue();
                ps.setInt(6, stars);
                 
                String dateString = (String) jsonObject.get("date");
    			Calendar calendar = Calendar.getInstance();
                calendar.clear();
                calendar.set(Calendar.MONTH, Integer.parseInt(dateString.split("-")[1])-1);
                calendar.set(Calendar.YEAR,Integer.parseInt(dateString.split("-")[0]));
                Date date = (Date) calendar.getTime();
                
                java.sql.Date d = new java.sql.Date(date.getTime());
                ps.setString(7, d.toString());
                
                String text = (String) jsonObject.get("text");
                ps.setString(8, text);
                 
                String type = (String) jsonObject.get("type");
                ps.setString(9, type);
                 
                String businessId = (String) jsonObject.get("business_id");
                ps.setString(10, businessId);
                 
                ps.executeUpdate();
                 
            }
            filereader.close();
        }
        catch(FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        catch(ParseException e)
        {
            e.printStackTrace();
        }
        catch (SQLException e) 
        {
 
            e.printStackTrace();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (ps != null) 
            {
                ps.close();
            }
            if (conn != null) 
            {
                conn.close();
            }
        }
 
    }
    public static Connection getDBConnection() 
    {
 
        Connection conn = null;
 
        try {
 
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
 
            System.out.println(e.getMessage());
 
        }
 
        try {
 
            conn = DriverManager.getConnection(
            		"jdbc:oracle:thin:@localhost:1521:ORCL", "books_admin", "MyPassword");
            return conn;
 
        } catch (SQLException e) {
 
            System.out.println(e.getMessage());
 
        }
 
        return conn;
 
    }       
 
}

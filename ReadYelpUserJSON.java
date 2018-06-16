
 
import java.io.BufferedReader;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
 
public class ReadYelpUserJSON 
{
    public void run_user() throws SQLException
    {
        // TODO Auto-generated method stub
        Connection conn = null;
        PreparedStatement ps1 = null;
        PreparedStatement ps2 = null;
        PreparedStatement ps3 = null;
 
        String insertYelpQuery = "INSERT INTO YELP_USER"
        + "(yelping_since, funny_votes, useful_votes, cool_votes, review_count, user_name, user_id, fans, average_stars, u_type, hot_compliment, more_compliment, profile_compliment, cute_compliment, list_compliment, note_compliment, plain_compliment, cool_compliment, funny_compliment, writer_compliment, photos_compliment ) VALUES"
                + "(to_date(?,'yyyy-mm-dd'),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        String sqlquery2 = "INSERT INTO FRIENDS" + "(user_id, friend_id) VALUES" + "(?,?)";
        String sqlquery3 = "INSERT INTO ELITE_YEARS" + "(user_id, elite) VALUES" + "(?,?)";
         
        JSONParser parser = new JSONParser();
         
        try
        {
            conn = getDBConnection();
            ps1 = conn.prepareStatement(insertYelpQuery);
            ps2 = conn.prepareStatement(sqlquery2);
            ps3 = conn.prepareStatement(sqlquery3);
            FileReader filereader = new FileReader("C:\\Users\\prati\\eclipse-workspace\\HomeworkAssignment3\\JSON Files\\yelp_user.json");
            
            System.out.println(filereader);
            BufferedReader br = new BufferedReader(filereader);
            String line;
            while ((line = br.readLine()) != null) 
            {
            	System.out.println(line);
            	Object obj = parser.parse(line);
                JSONObject jsonObject = (JSONObject) obj;
                
                String date_string = (String) jsonObject.get("yelping_since");
                System.out.println(date_string);
    			Calendar calendar = Calendar.getInstance();
                calendar.clear();
                calendar.set(Calendar.DATE, 1);
                calendar.set(Calendar.MONTH, Integer.parseInt(date_string.split("-")[1])-1);
                calendar.set(Calendar.YEAR,Integer.parseInt(date_string.split("-")[0]));
                Date date = (Date) calendar.getTime();
                System.out.println(date);
                
                java.sql.Date d = new java.sql.Date(date.getTime());
                System.out.println("____________");
                System.out.println(d.toString());
                ps1.setString(1, d.toString());
                
//                String yelp_since = (String) jsonObject.get("yelping_since");
//                preparedStatement.setString(1, yelp_since);
             
                int review_count = ((Long) jsonObject.get("review_count")).intValue();
                ps1.setInt(5, review_count);
             
                String name = (String) jsonObject.get("name");
                ps1.setString(6, name);
             
                String user_id = (String) jsonObject.get("user_id");
                ps1.setString(7, user_id);
             
                int fans = ((Long) jsonObject.get("fans")).intValue();
                ps1.setInt(8, fans);
             
                float avg_stars = ((Double) jsonObject.get("average_stars")).floatValue();
                ps1.setFloat(9, avg_stars);
             
                String type = (String) jsonObject.get("type");
                ps1.setString(10, type);
                 
                JSONObject votes = (JSONObject) jsonObject.get("votes");
                int  funny_votes = ((Long) votes.get("funny")).intValue();
                int  useful_votes = ((Long) votes.get("useful")).intValue();
                int  cool_votes = ((Long) votes.get("cool")).intValue();
                 
                ps1.setInt(2, funny_votes);
                ps1.setInt(3, useful_votes);
                ps1.setInt(4, cool_votes);
                 
                 
                JSONObject compliments = (JSONObject) jsonObject.get("compliments");
                
                System.out.println("compliments:" + compliments);
                
                int hot_compliment;
                int more_compliment;
                int profile_compliment;
                int cute_compliment;
                int list_compliment;
                int note_compliment;
                int plain_compliment;
                int cool_compliment;
                int funny_compliment;
                int writer_compliment;
                int photos_compliment;
                if(compliments.get("hot")!=null)
                {
                    hot_compliment = ((Long) compliments.get("hot")).intValue();
                }
                
                else
                {
                    hot_compliment = 0;         
                }
                 
                if(compliments.get("more")!=null)
                {
                    more_compliment = ((Long) compliments.get("more")).intValue();
                }
                else
                {
                    more_compliment = 0;            
                }
                 
                if(compliments.get("profile")!=null)
                {
                    profile_compliment = ((Long) compliments.get("profile")).intValue();
                }
                else
                {
                    profile_compliment = 0;         
                }
                 
                if(compliments.get("cute")!=null)
                {
                    cute_compliment = ((Long) compliments.get("cute")).intValue();
                }
                else
                {
                    cute_compliment = 0;            
                }
                 
                if(compliments.get("list")!=null)
                {
                    list_compliment = ((Long) compliments.get("list")).intValue();
                }
                else
                {
                    list_compliment = 0;            
                }
                 
                if(compliments.get("note")!=null)
                {
                    note_compliment = ((Long) compliments.get("note")).intValue();
                }
                else
                {
                    note_compliment = 0;            
                }
                 
                if(compliments.get("plain")!=null)
                {
                    plain_compliment = ((Long) compliments.get("plain")).intValue();
                }
                else
                {
                    plain_compliment = 0;           
                }
                 
                if(compliments.get("cool")!=null)
                {
                    cool_compliment = ((Long) compliments.get("cool")).intValue();
                }
                else
                {
                    cool_compliment = 0;
                }
                 
                if(compliments.get("funny")!=null)
                {
                    funny_compliment = ((Long) compliments.get("funny")).intValue();
                }
                else
                {
                    funny_compliment = 0;           
                }
                 
                if(compliments.get("writer")!=null)
                {
                    writer_compliment = ((Long) compliments.get("writer")).intValue();
                }
                else
                {
                    writer_compliment = 0;          
                }
                 
                if(compliments.get("photos")!=null)
                {
                    photos_compliment = ((Long) compliments.get("photos")).intValue();
                }
                else
                {
                    photos_compliment = 0;          
                }
                 
                ps1.setInt(11, hot_compliment);
                ps1.setInt(12, more_compliment);
                ps1.setInt(13, profile_compliment);
                ps1.setInt(14, cute_compliment);
                ps1.setInt(15, list_compliment);
                ps1.setInt(16, note_compliment);
                ps1.setInt(17, plain_compliment);
                ps1.setInt(18, cool_compliment);
                ps1.setInt(19, funny_compliment);
                ps1.setInt(20, writer_compliment);
                ps1.setInt(21, photos_compliment);
                 
                ps1.executeUpdate();
 
                if(jsonObject.get("friends")!=null)
                {
                    JSONArray friendarray = (JSONArray) jsonObject.get("friends");
                 
					Iterator<String> iterator = friendarray.iterator();
                    String friend_id;
             
                    while(iterator.hasNext())
                    {
                        friend_id = iterator.next();
                        ps2.setString(1, user_id);
                        ps2.setString(2, friend_id);
                        ps2.executeUpdate();
                    }
                }
 
                if(jsonObject.get("elite")!=null)
                {
                    JSONArray elitearray = (JSONArray) jsonObject.get("elite");
                   
                    
					Iterator<Long> iterator2 = elitearray.iterator();
                    int elite_year;
                 
                    while(iterator2.hasNext())
                    {
                        elite_year = (iterator2.next()).intValue();
                        ps3.setString(1, user_id);
                        ps3.setInt(2, elite_year);
                        ps3.executeUpdate();
                    }
                }
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
            if (ps1 != null) 
            {
                ps1.close();
            }
            if (ps2 != null) 
            {
                ps2.close();
            }
            if (ps3 != null) 
            {
                ps3.close();
            }
 
            if (conn != null) 
            {
                conn.close();
            }
        }
 
    }
     
    public static Connection getDBConnection() 
    {
 
    	System.out.println("inside connection class");
        Connection dbConnection = null;
 
        try {
 
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
 
            System.out.println(e.getMessage());
 
        }
 
        try {
 
            dbConnection = DriverManager.getConnection(
            		"jdbc:oracle:thin:@localhost:1521:ORCL", "books_admin", "MyPassword");
            return dbConnection;
         } catch (SQLException e) {
 
            System.out.println(e.getMessage());
 
        }
 
        return dbConnection;
 
    }       
 
}
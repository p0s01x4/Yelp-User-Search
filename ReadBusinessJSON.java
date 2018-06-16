

import java.io.BufferedReader;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
 
public class ReadBusinessJSON {
 
    public void run_business() throws SQLException
    {
        // TODO Auto-generated method stub
    	Connection conn = null;
        PreparedStatement ps = null;
        PreparedStatement ps2 = null;
        PreparedStatement ps3 = null;
        PreparedStatement ps4 = null;
        PreparedStatement ps5 = null;
        PreparedStatement ps6 = null;
 
        String query = "INSERT INTO BUSINESS"
        + "(bid, full_address, open_now, city, review_count, b_name, longitude, state, stars, latitude, b_type) VALUES"
                + "(?,?,?,?,?,?,?,?,?,?,?)";
        String query2 = "INSERT INTO b_hours" + "(d_o_w, from_h, to_h, bid) VALUES" + "(?,?,?,?)";
        String query3 = "INSERT INTO b_main_category" + "(c_name, bid) VALUES" + "(?,?)";
        String query4 = "INSERT INTO b_sub_category" + "(c_name, bid) VALUES" + "(?,?)";
        String query5 = "INSERT INTO neighborhoods" + "(n_name, bid) VALUES" + "(?,?)";
        String query6 = "INSERT INTO b_attributes" + "(a_name, bid) VALUES" + "(?,?)";
         
        JSONParser parser = new JSONParser();
         
        try
        {
            conn = getDBConnection();
            ps = conn.prepareStatement(query);
            ps2 = conn.prepareStatement(query2);
            ps3 = conn.prepareStatement(query3);
            ps4 = conn.prepareStatement(query4);
            ps5 = conn.prepareStatement(query5);
            ps6 = conn.prepareStatement(query6);
            FileReader filereader = new FileReader("C:\\Users\\prati\\eclipse-workspace\\HomeworkAssignment3\\JSON Files\\yelp_business.json");
            System.out.println("POPULATING BUSINESS NOW!!!"); 
            BufferedReader br = new BufferedReader(filereader);
            String line;
            while ((line = br.readLine()) != null) 
            {
                System.out.println("Line:" + line);
            	Object obj = parser.parse(line);
                JSONObject jsonObject = (JSONObject) obj;
                 
                String businessId = (String) jsonObject.get("business_id");
                ps.setString(1, businessId);
                 
                String b_address = (String) jsonObject.get("full_address");
                ps.setString(2, b_address);
                 
                boolean isOpen = (Boolean) jsonObject.get("open");
                int openNow;
                if (isOpen)
                {
                    openNow = 1;
                }
                else
                {
                    openNow = 0;
                }
                ps.setInt(3, openNow);
                 
                String city = (String) jsonObject.get("city");
                ps.setString(4, city);
                 
                int reviewCount = ((Long) jsonObject.get("review_count")).intValue();
                ps.setInt(5, reviewCount);
                 
                String bName = (String) jsonObject.get("name");
                ps.setString(6, bName);
                 
                float longitude = ((Double) jsonObject.get("longitude")).floatValue();
                ps.setFloat(7, longitude);
                 
                String state = (String) jsonObject.get("state");
                ps.setString(8, state);
                 
                float stars = ((Double) jsonObject.get("stars")).floatValue();
                ps.setFloat(9, stars);
                 
                float latitude = ((Double) jsonObject.get("latitude")).floatValue();
                ps.setFloat(10, latitude);
                 
                String type = (String) jsonObject.get("type");
                ps.setString(11, type);
                 
                ps.executeUpdate();
                                
                if(jsonObject.get("neighborhoods")!=null)
                {
                    JSONArray nei_array = (JSONArray) jsonObject.get("neighborhoods");
                    Iterator<String> iterator = nei_array.iterator();
                    String n_name;
             
                    while(iterator.hasNext())
                    {
                        n_name = iterator.next();
                        ps5.setString(1, n_name);
                        ps5.setString(2, businessId);
                        ps5.executeUpdate();
                    }
                }
                 
                JSONArray catArray = (JSONArray) jsonObject.get("categories");
                Iterator<String> iterator = catArray.iterator();
                String cat;
         
                while(iterator.hasNext())
                {
                    cat = iterator.next();
                    if(cat.equals("Active Life") || cat.equals("Arts & Entertainment") || cat.equals("Automotive") || 
                            cat.equals("Car Rental") || cat.equals("Cafes") || cat.equals("Beauty & Spas") || 
                            cat.equals("Convenience Stores") || cat.equals("Dentists") || cat.equals("Doctors") ||
                            cat.equals("Drugstores") || cat.equals("Department Stores") || cat.equals("Education") ||
                            cat.equals("Event Planning & Services") || cat.equals("Flowers & Gifts") || 
                            cat.equals("Food") || cat.equals("Health & Medical") || cat.equals("Home Services") ||
                            cat.equals("Home & Garden") || cat.equals("Hospitals") || cat.equals("Hotels & Travel") ||
                            cat.equals("Hardware Stores") || cat.equals("Grocery") || cat.equals("Medical Centers") ||
                            cat.equals("Nurseries & Gardening") || cat.equals("Nightlife") || cat.equals("Restaurants") ||
                            cat.equals("Shopping") || cat.equals("Transportation"))
                    {
                        ps3.setString(1, cat);
                        ps3.setString(2, businessId);
                        ps3.executeUpdate();
                    }
                    else
                    {
                        ps4.setString(1, cat);
                        ps4.setString(2, businessId);
                        ps4.executeUpdate();
                    }
                     
                }
                 
                if(jsonObject.get("attributes")!=null)
                {
                    JSONObject jsonObject4 = (JSONObject) jsonObject.get("attributes");
                    for (Object key : jsonObject4.keySet()) 
                    {
                        String keyStr = (String)key;
                        Object keyvalue = jsonObject4.get(keyStr);                    
 
                        if (keyvalue instanceof JSONObject)
                        {
                            JSONObject jsonObject5 = (JSONObject) jsonObject4.get(key);
                            for (Object key2 : jsonObject5.keySet())
                            {
                                String keyStr2 = (String)key2;
                                Object keyvalue2 = jsonObject5.get(keyStr2);
                                if (keyvalue2 instanceof Integer)
                                {
                                    String aValue = ((Long) jsonObject5.get(keyStr2)).toString();
                                    keyStr2 = keyStr2 + "_" + aValue;
                                    ps6.setString(1, keyStr2);
                                    ps6.setString(2, businessId);
                                    ps6.executeUpdate();
                                }
                                else if (keyvalue2 instanceof String)
                                {
                                    String aValue = (String) jsonObject5.get(keyStr2);
                                    keyStr2 = keyStr2 + "_" + aValue;
                                    ps6.setString(1, keyStr2);
                                    ps6.setString(2, businessId);
                                    ps6.executeUpdate();
                                }
                                else if (keyvalue2 instanceof Boolean)
                                {
                                    boolean a = (Boolean) jsonObject5.get(keyStr2);
                                    String aValue = String.valueOf(a);
                                    keyStr2 = keyStr2 + "_" + aValue;
                                    ps6.setString(1, keyStr2);
                                    ps6.setString(2, businessId);
                                    ps6.executeUpdate();
                                }
                            }
                        }
                        else
                        {
                            if (keyvalue instanceof Integer)
                            {
                                String aValue = ((Long) jsonObject4.get(keyStr)).toString();
                                keyStr = keyStr + "_" + aValue;
                                ps6.setString(1, keyStr);
                                ps6.setString(2, businessId);
                                ps6.executeUpdate();
                            }
                            else if (keyvalue instanceof String)
                            {
                                String aValue = (String) jsonObject4.get(keyStr);
                                keyStr = keyStr + "_" + aValue;
                                ps6.setString(1, keyStr);
                                ps6.setString(2, businessId);
                                ps6.executeUpdate();
                            }
                            else if (keyvalue instanceof Boolean)
                            {
                                boolean a = (Boolean) jsonObject4.get(keyStr);
                                String aValue = String.valueOf(a);
                                keyStr = keyStr + "_" + aValue;
                                ps6.setString(1, keyStr);
                                ps6.setString(2, businessId);
                                ps6.executeUpdate();
                            } 
                        }
                         
                    }
                }
                 
                if(jsonObject.get("hours")!=null)
                {
                    JSONObject jsonObject2 = (JSONObject) jsonObject.get("hours");
                    for (Object key : jsonObject2.keySet()) 
                    {
                        String keyStr = (String)key;                        
                        JSONObject jsonObject3 = (JSONObject) jsonObject2.get(keyStr);
                        String openH = (String) jsonObject3.get("open");
                        Float oH = convert_hour(openH);
                        String closeH = (String) jsonObject3.get("close");
                        Float cH = convert_hour(closeH);
                        ps2.setString(1, keyStr);
                        ps2.setFloat(2, oH);
                        ps2.setFloat(3, cH);
                        ps2.setString(4, businessId);
                        ps2.executeUpdate();                                     
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
            if (ps != null) 
            {
                ps.close();
            }
            if (ps2 != null) 
            {
                ps2.close();
            }
            if (ps3 != null) 
            {
                ps3.close();
            }
            if (ps4 != null) 
            {
                ps4.close();
            }
            if (ps5 != null) 
            {
                ps5.close();
            }
            if (ps6 != null) 
            {
                ps6.close();
            }
 
            if (conn != null) 
            {
                conn.close();
            }
        }
 
    }
     
    public static Float convert_hour(String a)
    {
        String[] b = a.split(":");
        float c = Float.parseFloat(b[0]);
        float d = Float.parseFloat(b[1]);
        d = d / 100;
        c = c + d;
        return c;
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

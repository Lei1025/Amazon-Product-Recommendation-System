import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class Search {
	
	public Search() {
		
	}
	
	/*
	 * read json file form disk
	 */
	private File readFile() {
		try {
			String filePath = new File("").getAbsolutePath();
			File file = new File((filePath + "\\sourceFiles\\meta_All_Beauty.json"));
			return file;
		} catch (Exception e) {
			System.out.println("Read file error");
			e.printStackTrace();
		}
		return null;
	}
	
	/*
	 * match json object with key and search value
	 */
	private boolean search(JSONObject jsonObject, String key,String queryString) {
		if(jsonObject.get(key) == null) return false;
		String productObj = jsonObject.get(key).toString();
		String[] query = queryString.split("\\s+");
		
		if(productObj == null || productObj.isBlank()) return false;
		else {
			String regex = "(?i).*";//match case insensitive
			for (String str : query) {
				regex+=(str+".*");
			}
			return productObj.matches(regex); 
		}
	}
	
	
	/*
	 * return search result array
	 * @Param  queryString: can contain space 
	 */
	public ArrayList<Product> getSearchResult(String key, String queryString) throws FileNotFoundException, IOException, ParseException {
		ArrayList<Product> products = new ArrayList<Product>();
		JSONParser jsonParser = new JSONParser();
				
		try (BufferedReader br = new BufferedReader(new FileReader(readFile()))) {
			String line;
			while ((line = br.readLine()) != null) {
				JSONObject productObj = null;
				productObj = (JSONObject) jsonParser.parse(line);
				if (productObj != null && productObj.get("price") != null && search(productObj, key, queryString))
					products.add(new Product(productObj));
			}
		}
		return products;
	}
	
	public Product asinSearch(String asin) throws FileNotFoundException, IOException, ParseException {
		JSONParser jsonParser = new JSONParser();
		
		try (BufferedReader br = new BufferedReader(new FileReader(readFile()))) {
			String line;
			while ((line = br.readLine()) != null) {
				JSONObject productObj = null;
				productObj = (JSONObject) jsonParser.parse(line);
				if(productObj.get("asin").toString().equals(asin))
					return new Product(productObj);
			}
		}
		return null;		
	}
}


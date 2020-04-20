import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.simple.JSONObject;

//define product data structure
class Product {
	private String asin = "", title = "", brand = "",rankString = "", imageUrl = "", description = "";
	private int rank = -1;
	private double price = Double.MIN_VALUE, rate = Double.MIN_VALUE;

	public Product() {

	}
	
	public Product(JSONObject productObject) {
		if(productObject.get("asin") != null)
			setAsin(productObject.get("asin").toString());
		if(productObject.get("title") != null)
			setTitle(productObject.get("title").toString());
		
		if(productObject.get("rank") != null)
			setRankString(productObject.get("rank").toString());
		
		if(productObject.get("brand") != null)
			setBrand(productObject.get("brand").toString());

		// get rank
		try {
			if (productObject.get("rank").toString().matches("^\\d+.*")) {
				Pattern pattern = Pattern.compile("\\d{1,3}(\\,\\d{3})*");// rank string contains rank and category info
				
				Matcher matcher = pattern.matcher(productObject.get("rank").toString());
				if (matcher.find())
					setRank(Integer.parseInt(matcher.group(0).replaceAll(",", "")));
			}	
		} catch (Exception e) {
			//System.out.println("Failed to parse rank string" );
		}
		
		// get price
		try {
			setPrice(Double.parseDouble(
					productObject.get("price").toString().replaceAll("\\$", "").replaceAll(",", "")));
		} catch (Exception e) {
			System.out.println("Failed to parse price");
			System.out.println(productObject.get("price"));
		}
		
		// get description
		try {
			Pattern pattern = Pattern.compile(".*");
			Matcher matcher = pattern.matcher(productObject.get("description").toString());
			if (matcher.find())
				setDescription((matcher.group(0)));
		} catch (Exception e) {
			setDescription("");
		}
		
		//get imageUrl
		if (productObject.get("image") != null) {
			Pattern pattern = Pattern.compile("http[^,\\]\\\"]{1,}\\.jpg");//only get the first one
			Matcher matcher = pattern.matcher(productObject.get("image").toString());
			if (matcher.find())
				setImageUrl(matcher.group(0).replaceAll("\\\\/", "/"));
		}else {
			setImageUrl("");
		}
	}

	public String getAsin() {
		return asin;
	}

	public void setAsin(String asin) {
		this.asin = asin;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getRankString() {
		return rankString;
	}

	public void setRankString(String rankString) {
		this.rankString = rankString;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public double getRate() {
		return rate;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}
}
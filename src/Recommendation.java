import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.netlib.util.doubleW;

import java_cup.internal_error;
import weka.classifiers.lazy.IBk;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.EuclideanDistance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.CSVLoader;

public class Recommendation {
	private String fileName = "clenedMetaData.csv";

	/*
	 * Filter data into a csv file to get ready for KNN
	 */
	public void preProcessMetaFile() throws Exception {
		final long startTime = System.nanoTime();
		System.out.println("Start pre-processing data...");

		JSONParser jsonParser = new JSONParser();
		JSONObject productJson = null;

		String filePath = new File("").getAbsolutePath();
		File file = new File((filePath + "\\sourceFiles\\meta_All_Beauty.json"));

		HashSet<String> asinSet = new HashSet<String>();
		ArrayList<Product> products = new ArrayList<Product>();

		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String line;
			while ((line = br.readLine()) != null) {
				// get one single product info
				productJson = (JSONObject) jsonParser.parse(line);

				// if no value, skip
				if (productJson.get("asin") == null || productJson.get("asin").toString().isBlank())
					continue;
				if (asinSet.contains(productJson.get("asin").toString())) //if duplicated
					continue;
				if (productJson.get("title") == null || productJson.get("title").toString().isBlank())
					continue;
				if (productJson.get("title").toString().length() > 300)
					continue; // skip error records
				if (productJson.get("rank") == null || productJson.get("rank").toString().isBlank())
					continue;
				if (productJson.get("price") == null || productJson.get("price").toString().isBlank())
					continue;

				// if no record before, add one
				Product p = new Product();
				p.setAsin(productJson.get("asin").toString());
				p.setTitle(toCSVString(productJson.get("title").toString()));
				p.setRankString(toCSVString(productJson.get("rank").toString()));

				// get rank
				try {
					if (!productJson.get("rank").toString().matches("^\\d+.*"))
						continue;
					Pattern pattern = Pattern.compile("\\d{1,3}(\\,\\d{3})*");// rank string contains rank and category info
																				
					Matcher matcher = pattern.matcher(productJson.get("rank").toString());
					if (matcher.find())
						p.setRank(Integer.parseInt(matcher.group(0).replaceAll(",", "")));
				} catch (Exception e) {
					System.out.println("Failed to parse rank string");
					e.printStackTrace();
				}
				// get price
				try {
					p.setPrice(Double.parseDouble(
							productJson.get("price").toString().replaceAll("\\$", "").replaceAll(",", "")));
				} catch (Exception e) {
					System.out.println("Failed to parse price");
					System.out.println(productJson.get("price"));
				}
				products.add(p);
				asinSet.add(p.getAsin());
			}
		}

		FileWriter fileWriter = new FileWriter(fileName);
		// write data into .csv file
		// write first line which is titles
		fileWriter.append("asin,price,rank\n");
		for (Product p : products) {
			fileWriter.append(p.getAsin() + "," + p.getPrice() + "," + p.getRank() + "\n");
		}

		fileWriter.flush();
		fileWriter.close();

		final long duration = System.nanoTime() - startTime;
		System.out
				.println("preProcessMetaFile:" + TimeUnit.MILLISECONDS.convert(duration, TimeUnit.NANOSECONDS) + "ms");
	}

	public ArrayList<String> runKNN(int recommendationNum, Product product) throws Exception {
		final long startTime = System.nanoTime();
		CSVLoader loader = new CSVLoader();
		loader.setSource(new File(fileName));
		Instances data = loader.getDataSet();
		data.setClassIndex(0);
		
		data.deleteStringAttributes();
		//System.out.println(data.toString());

		IBk ibk = new IBk();
		ibk.getNearestNeighbourSearchAlgorithm().setDistanceFunction(new EuclideanDistance());// Use Euclidean Distance
		// find the nearest 6 neighbor
		ibk.setKNN(recommendationNum + 1);// need X recommendation, result must include itself, so add 1 neighbor

		ibk.buildClassifier(data);

		//set test instance
		//Instance testInstance = data.instance(93);
		Instance testInstance =  new DenseInstance(1.0, new double[] {0, product.getPrice(), product.getRank()});
		testInstance.setDataset(data);
		testInstance.setClassMissing();

		double resultDouble = ibk.classifyInstance(testInstance);
		//System.out.println("predict result:" + resultDouble);

		//Attribute att = data.attribute(0);
		//System.out.println("pridect class: " + att.value((int) resultDouble));

		double[] res = ibk.distributionForInstance(testInstance);

		ArrayList<Integer> KNNIndex = new ArrayList<Integer>(); // get all index of nearest neighbor
		double max = -1;
		for (int i = 0; i < res.length; i++) {
			if (res[i] > max) {// if max change, clean array
				max = res[i];
				KNNIndex.clear();
				KNNIndex.add(i);
			} else if (res[i] == max) {// if neighbor, add index
				KNNIndex.add(i);
			}
		}
		//System.out.println(KNNIndex);
		
		ArrayList<String> asinResult = new ArrayList<String>();
		for (Integer i : KNNIndex) {
			String asin = data.attribute(data.classIndex()).value(i);
			if(!asin.equals(product.getAsin())) {
				asinResult.add(asin);
			}		
		}
		//System.out.println(asinResult.toString());

		final long duration = System.nanoTime() - startTime;
		System.out.println("KNN time:" + TimeUnit.MILLISECONDS.convert(duration, TimeUnit.NANOSECONDS) + "ms");
		
		return asinResult;
	}

	//deal with double quote sign and comma sign with csv file
	public String toCSVString(String str) {
		if (str.contains(",")) {
			if (str.contains("\"")) {
				str = str.replaceAll("\"", "\"\"");
			}
			str = "\"" + str + "\"";
		}
		return str;
	}
	
	
	public ArrayList<Product> getRecomendation(int recommendationNum, Product product) throws Exception {
		//to get result successfully, at least need price, rank of product
		ArrayList<String> asins = runKNN(recommendationNum, product);
		ArrayList<Product> recommendProducts = new ArrayList<Product>();
		
		Search search = new Search();
		for (String asin : asins) {
			recommendProducts.add( search.asinSearch(asin));
		}
		
		return recommendProducts;		
	}
}

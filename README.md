# Amazon-Product-Recommendation-System

Performed KNN Algorithm in Java

![Screenshot](/assets/uploads/5e191dac7cb767d152d97ae9a02bf28.png)

# Tools and Libraries Used

* Java 13
* Weka 3.8.4
* Json simple 1.1.1
* Java Swing

# Data Source

[Amazon Review Data (2018)](https://nijianmo.github.io/amazon/index.html)

In my demo, I used `All Beauty - metadata` in Pre-category data, since it is large enough for testing result and also small enough to run fast in my PC.

# Recommendation Principle

## Original Source Data Snippet:

```json
{"title": "Workout Headphones by Arena Essentials", "image": ["https://images-na.ssl-images-amazon.com/images/I/61BM8VG0BCL._SS40_.jpg", "https://images-na.ssl-images-amazon.com/images/I/61YKSNFYPPL._SS40_.jpg"], "brand": "HarperCollins", "rank": "3,235,148inBeautyamp;PersonalCare(", "main_cat": "All Beauty", "asin": "0061073717"}
{"description": ["This is the NEW PURPLE BOTTLE that just came out (not the old black bottle which came out years ago and has been discontinued)\n\nBlack Diamond\nIndoor Lotion\nTingle\nNew\nFull Size Bottle", "", ""], "title": "Black Diamond", "brand": "Swedish Beauty", "rank": "1,462,563inBeautyamp;PersonalCare(", "also_view": ["B000LXTNMW"], "main_cat": "All Beauty", "asin": "0143026860"}
{"description": ["A brand-new, unused, unopened, undamaged item. Customer Satisfaction Guaranteed."], "title": "12 Pc BLUE HEAVEN KAJAL\"HERBAL kajal With VITAMIN E COLOR Natural BLACK", "also_buy": ["B00OWT3W28", "B010E1X15K", "B0796RMSV8", "B07HPBWSW2", "B010FQJWWK", "B00IGBID9A", "B07C9KZTXP", "B00DNK5UR2", "B0079Z48S4", "B005EIIAPA", "B00MSU90U6", "B079MHLF3Z", "B078VXMNQQ", "B072SJQW3B", "B00MAX5TCY", "B01HTGNIH4", "B01BIRIPA0", "B0777796G5", "B01BEABLO8", "B01KJK7UA8", "B007T8LXZC", "B00ISAPPLI", "B07DDCD86N", "B01N6MIQ27"], "brand": "BLUE HEAVE", "rank": "100,425inBeautyPersonalCare(", "also_view": ["B00OWT3W28", "B0796RMSV8", "B010E1X15K", "B010FQJWWK", "B00OXQXP04", "B0079Z48S4", "B07C9KZTXP", "B06XC324BV", "1201025818", "B01BEABLO8", "B078V56SX1", "B00FLQ3VIQ", "B0154B5PAY", "B07KSHBD3R", "B078X9FPRD", "B07BN2WX93", "B00F6W0IGI", "B015E689YS", "B074QLLZ7V", "B01GW09XRW", "B005287VJ8", "B0789K416H", "B074VX7K85", "B0798KV8S6", "B0768XX5PM", "B071D5N6Z3", "B07B95FR76", "B01N9JM42T", "B07GKHKX6M", "0000045284", "B006LXBSYM", "B076DFBZLQ", "4784294759", "B07HPBWSW2", "B00LX45G1A", "B00H4XWEGO", "B01HBUSDPK", "B07BZ1KJX9", "B01LX6I5X0", "B00JGQJL4U", "B07BPVDPT9", "B00CEETD1M", "B00CTTJH04", "B00UC05MEM", "B00MAX5TCY", "B01BY5KDEC", "B071G69PGN", "B07H3XDM3M", "B006HCJXBM", "B077SW7V67", "B006WZ9TPO", "B0768YNQND", "B0079Z7GH4", "B07JZ527T2", "B0742J58WT", "B07C3GZJRB", "B077J4Q3K3"], "main_cat": "All Beauty", "price": "$6.45", "asin": "014789302X"}
{"description": ["You have to wonder why there isn't a word in the English language for the fireworks that go off in your brain when you finally kiss someone you've wanted for years. Or for the intimacy and tenderness you feel as you hold the hand of a suffering friend. A generation after the height of the AIDS crisis, what is it like to be a young gay man in New York? How many words are there now for the different kinds of pain, the different kinds of love? Matthew Lopez's The Inheritance premieres in two parts at the Young Vic Theatre, London, in March 2018.", "Matthew Lopez is the author of The Whipping Man (Luna Stage Company, Manhattan Theatre Club), The Legend of Georgia McBride (Denver Center for Performing Arts; Manhattan Class Company, Geffen Playhouse), Somewhere (The Old Globe, Hartford Stage), Reverberation (Hartford Stage Company), and Zoey's Perfect Wedding (Denver Center for the Performing Arts). In London, he was represented in Headlong Theatre's 9/11 Decade anthology with his short play The Sentinels."], "title": "The Inheritance", "also_buy": ["1848426380", "B07JVF7M3C", "1644450003", "1635571766", "1559365978", "1559365862", "0857055429", "B07J36923G", "0525618643", "1939931614", "B07HC435LF", "0573697094", "B07D4ZWCMB", "0735223521", "162779834X", "1468315714", "1420956493", "0345806565", "B07HC5H94C", "1101874562", "1559363843", "082223226X", "1350069299", "B07895XF5C", "1559365730", "1559365420", "0062795252", "B07BMLQN93", "3836563487", "177046316X", "0872867862", "1559365382", "155936582X", "0393310329", "1559365560", "B07GGRJWRW", "0802137563", "B0767FCYDP", "031631613X", "0804172706", "B0788XVVD8", "155936534X", "086547771X", "1559365323", "B01H4Z7CZE", "0316316121", "1328764524", "0199832536", "0735218196", "081013358X", "0452274001", "1559365404", "0738215678", "1555977359", "1848426038", "082222156X", "1468311085", "076246481X", "B07GW7NRW8", "0822225336", "0316188549", "1848426313", "1593500750", "B078DDYRBL", "B07GGCZ7GZ", "0571328873", "155659495X", "1559365471", "0822231166", "1984854275", "1593501463", "039959227X", "0573640041", "1501198246", "0684843269", "0865479445", "1250122430", "1559364580", "1783191430", "0143128752", "0573705674", "B072ZM74YY", "B0001HAGRE", "1400032385", "0571245927", "0552162949", "0553419056", "0822200732", "B003554PYO", "1350045985", "0307275930", "155936551X", "1559360410", "1559361131", "1350055018", "0007465084"], "brand": "Sunatoria", "rank": "476,831inBeautyPersonalCare(", "also_view": ["0571352367", "0573697094", "1635571766", "1559365978", "1420956493", "1634242173", "1848426380", "155936582X", "1350071935", "1718116373", "1848426038", "031631613X", "0062795252"], "main_cat": "All Beauty", "asin": "0571348351"}
{"description": ["The Listening Cards are an eloquent primer in the art of listening, offering you quick, straightforward and memorable lessons and graphics to help you and those you know easily improve your listening skills without having to read a big book. Each deck also includes underlying philosophy for listening and instructions for using the cards individually, with a partner or with a group. Attending website has free lessons and videos to learn even more.", "", ""], "title": "The Listening Cards", "image": ["https://images-na.ssl-images-amazon.com/images/I/51VcC85I8lL._SS40_.jpg"], "brand": "Listening Planet", "rank": "2,967,592inBeautyamp;PersonalCare(", "main_cat": "All Beauty", "asin": "0692508988"}
```

Ideally, it is in need of `asin`, `price`, `rank`, `review rate`, but review data is in another much larger `review data` file, not given average rate but each rate of every user. Therefore, I just wrote the code cleaning data like below:

![](/assets/uploads/20200418215012.png)

There are multiple way to address data, such as using database(SQL) or filtering in Excel. I wrote a `preProcessMetaFile` method in Recommendation.java to clean my data to a `.csv` file, which is convenient for using `weka` library directly.

## KNN Algorithm
```java
IBk ibk = new IBk();
ibk.getNearestNeighbourSearchAlgorithm().setDistanceFunction(new EuclideanDistance());// Use Euclidean Distance
ibk.setKNN(4);// need X recommendation, result must include itself, so add 1 neighbor
```

`IBK` is "K-nearest neighbours classifier" of weka library.

Compare to [this example](https://towardsdatascience.com/prototyping-a-recommender-system-step-by-step-part-1-knn-item-based-collaborative-filtering-637969614ea), the author mentioned:

> Euclidean distance is unhelpful in high dimensions because all vectors are almost equidistant to the search query vector (target movie’s features). Instead, we will use cosine similarity for nearest neighbor search.

I only have 2 dimensions -- price and rank, at most to add one more --`review`. Therefore `Euclidean Distance` should implement in `KNN`. (It is also the default distance function in  `IBK`, do not need the configuration like I did.)

If given a product's price and rank, KNN will find the K nearest product. For example, K is set to 1, price is 10 and rank is 430,000, it may find the product in row 5 which is the most similar one. By default, IBK class will find the closest one.

In this case, 3 similar items is required and one of them is existing in data file. Thus K value needs to be set to 4, which means exclude the one I selected itself.

# Search

Search function ignores cases and using space can do multiple search keywords like what we familiar with Google.

# GUI

Since search and get recommendation functions take seconds to read original file and download image, performed asynchronous method and proper progress bar animation.

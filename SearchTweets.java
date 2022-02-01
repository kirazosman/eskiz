import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import twitter4j.Query;
import twitter4j.Query.ResultType;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class SearchTweets {

	static String filterWords(ArrayList<String> words) {
		for (int i = words.size() - 1; i >= 0; i--) {
			words.set(i, words.get(i).replace("?", "").trim().replaceAll("[.()’,':!-;&?\\\"]", "").toLowerCase());
			String word = words.get(i);
			if (words.get(i).replace("?", "").length() < 4) {
				words.remove(i);
			} else if (word.contains("\n") || word.contains("@") || word.contains("#") || word.contains("http")) {
				words.remove(i);
			}
		}
		String result = "";
		System.out.println("kelime sayisi " + words.size());
		for (int i = 0; i < words.size(); i++) {
			result += words.get(i) + " ";
		}
		return result.trim();
	}

	public static void main(String[] args) throws Exception {

		System.setProperty("jsse.enableSNIExtension", "false");
		String queryWord = args[0]; 
		System.out.println("Process basladi. " + "Aramada kullanilan kelime => " + queryWord);

		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true).setOAuthConsumerKey("GEREKEN DEGERI GIR")
				.setOAuthConsumerSecret("GEREKEN DEGERI GIR")
				.setOAuthAccessToken("GEREKEN DEGERI GIR")
				.setOAuthAccessTokenSecret("GEREKEN DEGERI GIR");

		TwitterFactory tf = new TwitterFactory(cb.build());
		Twitter twitter = tf.getInstance();
		ArrayList<String> tweetsList = new ArrayList<>();
		ArrayList<String> wordList = new ArrayList<>();
		int counter = 0;
		int page = 0;
		try {
			Query query = new Query(queryWord);
			query.setCount(100);
			String pattern = "yyyy-MM-dd";
			DateFormat df = new SimpleDateFormat(pattern);
			Date today = Calendar.getInstance().getTime();
			String todayAsString = df.format(today);
			query.since(todayAsString); //Sadece bugun atilan tweetleri almak icin
			query.setResultType(ResultType.recent);
			QueryResult result;
			do {
				page++;
				result = twitter.search(query);
				List<Status> tweets = result.getTweets();
				for (Status tweet : tweets) {
					counter++;
					tweetsList.add(tweet.getText());
				}
			} while ((query = result.nextQuery()) != null);

			System.out.println("page => " + page);
			System.out.println(counter);
			for (String s : tweetsList) {
				wordList.addAll(Arrays.asList(s.split(" ")));
			}
			String input = "";
			
			input = filterWords(wordList);
			input = input.replace("?", "");

			String currentTime = (new SimpleDateFormat("ddMMYYYYHHmmss")).format(Calendar.getInstance().getTime());
			BufferedWriter writer = null;
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("WordsForSearchKey" + currentTime + "_" + queryWord), StandardCharsets.UTF_8));
			writer.write(input);
			writer.close();
			
			System.exit(0);
		} catch (TwitterException te) {
			te.printStackTrace();
			System.out.println("Failed to search tweets: " + te.getMessage());
			System.exit(-1);
		}

	}
}

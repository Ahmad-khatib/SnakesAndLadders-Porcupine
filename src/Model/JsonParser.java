
package Model;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.List;
//
public class JsonParser<T> {

    private static JsonParser instance;

    private JsonParser() {}

    public static JsonParser getInstance() {
        if (instance == null)
            instance = new JsonParser();
        return instance;
    }

    public List<T> parseToList(String json, T prototype) {
        List<T> resultList = new ArrayList<>();
        JSONParser parser = new JSONParser();

        try {
            JSONObject jsonObject = (JSONObject) parser.parse(json);
            JSONArray jsonArray = (JSONArray) jsonObject.get("questions");

            for (Object obj : jsonArray) {
                JSONObject questionJson = (JSONObject) obj;
                String text = (String) questionJson.get("question");
                JSONArray answersArray = (JSONArray) questionJson.get("answers");
                String[] answers = new String[answersArray.size()];
                for (int i = 0; i < answersArray.size(); i++) {
                    answers[i] = (String) answersArray.get(i);
                }
                String correctAnswer = (String) questionJson.get("correct_ans");
                Difficulty level = Difficulty.valueOf(questionJson.get("difficulty").toString());

                // Create a new Question object with the parsed data
                Question question = new Question(text, answers[0], answers[1], answers[2], answers[3], correctAnswer, level);
                resultList.add((T) question);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return resultList;
    }

}

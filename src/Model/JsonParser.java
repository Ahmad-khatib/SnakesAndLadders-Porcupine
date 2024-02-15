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
                int questionId = Integer.parseInt(questionJson.get("questionId").toString());
                String text = (String) questionJson.get("question");
                String answer1 = (String) questionJson.get("answer1");
                String answer2 = (String) questionJson.get("answer2");
                String answer3 = (String) questionJson.get("answer3");
                String answer4 = (String) questionJson.get("answer4");
                String correctAnswer = (String) questionJson.get("correct_ans");
                Difficulty level = Difficulty.valueOf(questionJson.get("level").toString());

                Question question = new Question(questionId, text, answer1, answer2, answer3, answer4, correctAnswer, level);
                resultList.add((T) question);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return resultList;
    }
}

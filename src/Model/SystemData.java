package Model;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class SystemData {

    private static SystemData instance;
    private HashMap<Difficulty, ArrayList<Question>> questions;
    private ArrayList<GameBoard> games;
    private ArrayList<Player> players;

    private String questionJSONPath = "src/JSON/questions_scheme.txt";
    private final String originalPath = questionJSONPath;

    private SystemData() {
        questions = new HashMap<>();
        games = new ArrayList<>();
        players = new ArrayList<>();
    }

    public static SystemData getInstance() {
        if (instance == null)
            instance = new SystemData();
        return instance;
    }


    public HashMap<Difficulty, ArrayList<Question>> getQuestions() {
        return questions;
    }

    public void setQuestions(HashMap<Difficulty, ArrayList<Question>> questions) {
        this.questions = questions;
    }

    public ArrayList<GameBoard> getGameBoards() {
        return games;
    }

    public void setGameBoards(ArrayList<GameBoard> games) {
        this.games = games;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    public boolean loadQuestions(String externalPath) {
        String pathToUse = externalPath != null ? externalPath : originalPath;

        JSONParser parser = new JSONParser();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(pathToUse)))) {
            Object obj = parser.parse(reader);
            JSONObject jo = (JSONObject) obj;
            JSONArray questionsArray = (JSONArray) jo.get("questions");

            for (Object questionObj : questionsArray) {
                JSONObject q = (JSONObject) questionObj;
                int questionId = Integer.parseInt(q.get("questionId").toString()); // Assuming questionId is included in JSON
                String text = (String) q.get("question");
                String answer1 = (String) q.get("answer1");
                String answer2 = (String) q.get("answer2");
                String answer3 = (String) q.get("answer3");
                String answer4 = (String) q.get("answer4");
                String correctAnswer = (String) q.get("correct_ans");
                Difficulty level = SystemData.getQuestionLevel(Integer.parseInt(q.get("level").toString()));

                Question questionToAdd = new Question();
                questions.computeIfAbsent(questionToAdd.getLevel(), k -> new ArrayList<>()).add(questionToAdd);
            }
        } catch (Exception e) {
            e.printStackTrace();
            resetPathToDefault();
            return false;
        }
        resetPathToDefault();
        return true;
    }

    public void saveQuestions(String externalPath) {
        if (externalPath != null) {
            questionJSONPath = externalPath;
        }

        try {
            JSONArray JSONQuestions = new JSONArray();
            JSONObject toWrite = new JSONObject();

            for (ArrayList<Question> list : questions.values()) {
                if (list == null)
                    continue;

                for (Question q : list) {
                    JSONObject jo1 = new JSONObject();
                    jo1.put("questionId", q.getQuestionId()); // Assuming questionId is included in JSON
                    jo1.put("question", q.getText());
                    jo1.put("answer1", q.getAnswer1());
                    jo1.put("answer2", q.getAnswer2());
                    jo1.put("answer3", q.getAnswer3());
                    jo1.put("answer4", q.getAnswer4());
                    jo1.put("correct_ans", q.getCorrectAnswer());
                    jo1.put("level", q.getLevel());
                    JSONQuestions.add(jo1);
                }
            }
            toWrite.put("questions", JSONQuestions);

            FileWriter file = new FileWriter(questionJSONPath);
            file.write(toWrite.toJSONString());
            file.flush();
            System.out.println("JSON Question was saved successfully");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            resetPathToDefault();
        }
    }

    public static String readFileAsString(String file) throws IOException {
        return new String(Files.readAllBytes(Paths.get(file)));
    }

    public boolean loadData(Type type) {
        if (type == null)
            return false;

        try {
            if (type.equals(Type.QUESTIONS)) {
                String file = "src/JSON/QuestionsFormat.txt";
                String json = readFileAsString(file);
                List<Question> loadedQuestions = JsonParser.getInstance().parseToList(json, new Question());
                if (loadedQuestions != null) {
                    questions.clear();
                    for (Question question : loadedQuestions) {
                        questions.computeIfAbsent(question.getLevel(), k -> new ArrayList<>()).add(question);
                    }
                }
                return true;
            } else
                return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean writeData(Type type) {
        if (type == null)
            return false;
        FileWriter writer = null;

        try {
            if (type.equals(Type.QUESTIONS)) {
                String filePath = "src/JSON/QuestionsFormat.txt";
                writer = new FileWriter(filePath);
                JSONArray JSONQuestions = new JSONArray();

                for (ArrayList<Question> list : questions.values()) {
                    if (list == null)
                        continue;

                    for (Question q : list) {
                        JSONObject jo1 = new JSONObject();
                        jo1.put("questionId", q.getQuestionId());
                        jo1.put("question", q.getText());
                        jo1.put("answer1", q.getAnswer1());
                        jo1.put("answer2", q.getAnswer2());
                        jo1.put("answer3", q.getAnswer3());
                        jo1.put("answer4", q.getAnswer4());
                        jo1.put("correct_ans", q.getCorrectAnswer());
                        jo1.put("level", q.getLevel());
                        JSONQuestions.add(jo1);
                    }
                }

                JSONObject toWrite = new JSONObject();
                toWrite.put("questions", JSONQuestions);

                writer.write(toWrite.toJSONString());
                return true;
            } else
                return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (writer != null) {
                    writer.flush();
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private static Difficulty getQuestionLevel(int level) {
        return switch (level) {
            case 1 -> Difficulty.EASY;
            case 3 -> Difficulty.HARD;
            default -> Difficulty.MEDIUM;
        };
    }


    private void resetPathToDefault() {
        questionJSONPath = originalPath;
    }

}